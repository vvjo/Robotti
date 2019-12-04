package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import lejos.robotics.navigation.Waypoint;

/**
 * Tietovirta tietokoneelta robotille lähetettävälle datalle
 * @author Väinö Kojo
 *
 */
public class PcDataOut {

	private DataOutputStream out;

	/**
	 * @param out Outputstream parametrina
	 */
	public PcDataOut(DataOutputStream out) {
		this.out = out;
	}

	/**
	 * Lähettää listan robotille
	 * Viimeinen waypoint on (-1,-1) josta robotti tietää että kaikki arvot on saatu
	 * @param lista transmittable Waypoint lista
	 */
	public void out(List<Waypoint> lista) { 
		try {
			lista.add(new Waypoint(-1, -1));
			for (Waypoint a : lista) {
				a.dumpObject(out);
				System.out.println(a);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Yksittäisten integer arvojen lähetys
	 */
	public void out(int i) {
		try {
			out.writeInt(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sulkee outputstreamin
	 */
	public void lopeta() { 
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}