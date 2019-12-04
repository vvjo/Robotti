import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.Waypoint;

/**
 * Robotin pääohjelma
 * @author Sami Tuppurainen, Väinö Kojo
 *
 */
public class Main {

	public static void main(String[] args) {
		/**
		 * Moottoreiden alustus
		 */
		RegulatedMotor X = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor Y = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor C = new EV3MediumRegulatedMotor(MotorPort.C);
		X.setSpeed(100);
		Y.setSpeed(100);
		C.setSpeed(100);
		/**
		 * Yhteyden luonti robotin ja tietokoneen välille
		 */
		Socket s = null;
		try {
			ServerSocket serveri = new ServerSocket(1111);
			s = serveri.accept();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * Inputstreamin, DataIn ja hätäseis säikeiden luonti
		 */
		DataInputStream ins = null;
		try {
			ins = new DataInputStream(s.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		DataIn in = new DataIn(ins);
		Stop seis = new Stop();
		in.start();		
		try {
			in.join();	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		seis.start();
		in.lopeta();
		
		/**
		 * Waypoint listan täyttö tietokoneelta tulleilla waypointeilla
		 */
		ArrayList<Waypoint> pisteet = in.returnPisteet();
		/**
		 * Boolean rotated katsoo onko kynä jo alhaalla tai ylhäällä
		 */
		boolean rotated = false;
		/**
		 * X ja Y moottoreita liikutetaan waypointtien x ja y arvojen mukaan
		 * C moottoria liikutetaan kun saadaan viivan alku tai loppu waypoint(1000,1000)
		 */
		for (Waypoint a : pisteet) {
			if (a.getX() < 999.0) {
				X.rotateTo((int) a.getX());
				X.waitComplete();
				Y.rotateTo((int) a.getY() * (-1));
				Y.waitComplete();
				if((C.getTachoCount() > 22 && C.getTachoCount() < 27) && rotated == false) {
					C.rotateTo(-12);
					rotated=true;
				}
			} else {
				C.rotateTo(25);		
				rotated=false;
			}
	
		}	C.rotateTo(0);
		
		seis.lopeta();
	}
}
