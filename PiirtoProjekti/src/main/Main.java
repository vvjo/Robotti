package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.view.PiirtoSceneController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private PiirtoSceneController controller;

	/**
	 *Lataa graafisen käyttöliittymän grafiikan ja ominaisuudet
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Piirtorobo");
		initRootLayout();
		showPiirtoOverview();
	}

	/**
	 * Lataa roolayoutin FXML tiedostosta ja asettaa sen primarystagelle
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Asettaa piirtoscenen rootlayoutille
	 */
	public void showPiirtoOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/PiirtoScene.fxml"));
			AnchorPane piirtoOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(piirtoOverview);
			controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);

	}
}