package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Luo tietovirrat tiedostoon kirjoitusta tai lukua varten.
 * @author Väinö Kojo
 *
 */
public class Tiedostonkäsittely {

	/**
	 * @param tiedostoNimi Käyttäjän antama tiedoston nimi
	 * @param lista WP lista joka kirjoitetaan tiedostoon
	 */
	public static void kirjoitaTiedosto(String tiedostoNimi, List<WP> lista) {
		FileOutputStream tiedosto = null;
		ObjectOutputStream os = null;
		try {
			tiedosto = new FileOutputStream(tiedostoNimi);
			os = new ObjectOutputStream(tiedosto);
			os.writeObject(lista);
			os.close();
		} catch (IOException io) {
			io.printStackTrace();
		}
		System.out.println("Tiedot tallennettu tiedostoon " + tiedostoNimi);
	}

	/**
	 * @param tiedostonNimi Luettavan tiedoston nimi
	 * @return Palauttaa WP listan luetusta tiedostosta
	 */
	@SuppressWarnings("unchecked")
	public static List<WP> lueTiedosto(String tiedostonNimi) {
		List<WP> lista = null;
		FileInputStream tiedosto = null;
		ObjectInputStream is = null;
		try {
			tiedosto = new FileInputStream(tiedostonNimi);
			is = new ObjectInputStream(tiedosto);
			lista = (List<WP>) is.readObject();
			is.close();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (Exception oi) {
			System.out.println("is.readobject = null");
		}
		System.out.println("Tiedot haettu tiedostosta " + tiedostonNimi);
		return lista;
	}
}