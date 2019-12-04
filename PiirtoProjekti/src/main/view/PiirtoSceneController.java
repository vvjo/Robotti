package main.view;

import java.awt.Desktop;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import lejos.robotics.navigation.Waypoint;
import lejos.utility.Delay;
import main.Main;
import model.PcDataOut;
import model.Tiedostonkäsittely;
import model.WP;

/**
 * Kontrolleri piirtosovellukselle
 * @author Väinö Kojo
 *
 */
public class PiirtoSceneController {
	@FXML
	private Canvas piirtoCanvas;
	@FXML
	private Label nimi;
	@FXML
	private Button connectBtn;
	@FXML
	private Button sendBtn;
	private GraphicsContext gc;
	private Main main;
	private PcDataOut out = null;
	private FileChooser chooser;
	private File file;
	/**
	 * Transmittable
	 */
	private ArrayList<Waypoint> lista = new ArrayList<>();
	/**
	 * Serializable
	 */
	private ArrayList<WP> wplista = new ArrayList<>();

	public PiirtoSceneController() {
	}

	/**
	 * FXML dokumentista liitetty painike
	 * Lähettää Waypoint-listan robotille
	 */
	@FXML
	private void sendPiirros() {	//Lähettää waypoint-oliolistan robotille
		System.out.println(lista.size() + " :kokoinen lista lähetetty");
		out.out(lista);
	}

	/**
	 * FXML painike.
	 * Ottaa yhteyden robottiin ja luo PcDataOut olin
	 * Asettaa send painikkeen available, kun yhteys muodostuu
	 */
	@FXML
	public void roboConnect() {		
		Socket s = null;
		try {
			s = new Socket("10.0.1.1", 1111); 
			System.out.println("Soketti luotu");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataOutputStream outs = null;
		try {
			outs = new DataOutputStream(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = new PcDataOut(outs);
		connectBtn.setStyle("-fx-background-color: green; ");
		connectBtn.setDisable(true);		
		sendBtn.setDisable(false);
	}

	/**
	 * FXML painike
	 * Tyhjentää canvasin piirroksesta ja samalla alustaa wp ja Waypoint listat
	 */
	@FXML
	private void clearPiirros() {	
		gc.setFill(Color.WHITE);
		gc.fillRect(1, 1, piirtoCanvas.getWidth() - 2, piirtoCanvas.getHeight() - 2);
		lista.clear();
		wplista.clear();
		gc.setFill(Color.BLACK);
	}

	/**
	 * FXML painike
	 * Tekee waypoint-oliolistasta wp-oliolistan ja tallentaa sen valitsemaasi paikkaan dat-tiedostoon
	 */
	@FXML
	private void save() {	
		for (int i = 0; i < lista.size(); i++) {
			wplista.add(new WP(lista.get(i)));
		}
		chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("dat files (*.dat)", "*.dat");
		chooser.getExtensionFilters().add(extFilter);
		file = chooser.showSaveDialog(main.getPrimaryStage());
		if (file != null) {
			Tiedostonkäsittely.kirjoitaTiedosto(file.getAbsolutePath(), wplista);
			System.out.println(wplista.size());
		}
	}

	/**
	 * FXML painike
	 * Lataa valitsemasi piirustuksen wp-oliolistan dat-tiedostosta ja piirtää sen uudestaan canvasille
	 */
	
	@FXML
	private void load() {	
		wplista.clear();
		lista.clear();
		clearPiirros();
		chooser = new FileChooser();
		file = chooser.showOpenDialog(main.getPrimaryStage());
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("dat files (*.dat)", "*.dat");
		chooser.getExtensionFilters().add(extFilter);
		if (file != null) {
			wplista = (ArrayList<WP>) Tiedostonkäsittely.lueTiedosto(file.getAbsolutePath());
			System.out.println(wplista.size());
		}
		for (int i = 0; i < wplista.size(); i++) {
			lista.add(new Waypoint(wplista.get(i).getX(), wplista.get(i).getY()));
		}
		draw();
	}

	/**
	 * FXML painike
	 * Avaa oletusselaimella wikipedian
	 */
	@FXML
	private void help() {	
		String url = "https://en.wikipedia.org/wiki/Main_Page";
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * FXML painike
	 * Sulkee sovelluksen
	 */
	@FXML
	private void close() {	
		System.exit(-1);
	}

	/**
	 * Piirtää tiedostosta luetun piirroksen canvasille
	 */
	private void draw() {	
		gc.beginPath();
		gc.moveTo(wplista.get(0).getX(), wplista.get(0).getY());
		WP cwp = wplista.get(0);
		for (WP swp : wplista) {
			if (swp.getX() != 1000 && cwp.getX() != 1000 && swp.getX() != 0 && cwp.getX() != 0) {
				gc.lineTo(swp.getX(), swp.getY());
				gc.stroke();
			} else {
				gc.moveTo(swp.getX(), swp.getY());
			}
			cwp = swp;
		}
		gc.closePath();
	}

	/**
	 * Alustaa canvas elementin: lisää mouse eventit canvaseille, jotta piirtäminen onnistuu
	 * Eventit tallentavat hiiren koordinaatit canvasiin nähden waypoint-olioina listaan
	 */
	private void initCanvas() {		
		gc = piirtoCanvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		piirtoCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				gc.beginPath();
				gc.moveTo(e.getX(), e.getY());
				gc.setStroke(Color.BLACK);
				lista.add(new Waypoint(1000, 1000));		//Viivan aloituspiste
				System.out.println(e.getX());
				gc.stroke();
			}
		});
		piirtoCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				gc.lineTo(e.getX(), e.getY());
				lista.add(new Waypoint(e.getX(), e.getY()));
				gc.setStroke(Color.BLACK);
				gc.stroke();
				System.out.println(lista.size());
			}
		});
		piirtoCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				lista.add(new Waypoint(1000, 1000));		//Viivan lopetuspiste
				gc.closePath();
			}
		});
	}

	/**
	 * @param main Antaa mahdollisuuden kutsua main luokan primarystage oliota
	 * Laittaa send painikkeen disabled tilaan
	 */
	public void setMainApp(Main main) {		
		this.main = main;
		sendBtn.setDisable(true);		
		connectBtn.setStyle("-fx-background-color: red; ");
		initCanvas();
	}
}
