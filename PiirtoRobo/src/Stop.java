import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * Hätäseis-painikkeen säie
 * @author Sami Tuppurainen, Väinö Kojo
 *
 */
public class Stop extends Thread {
	Port port = LocalEV3.get().getPort("S1");
	SensorModes sensor = new EV3TouchSensor(port);
	SampleProvider touch = ((EV3TouchSensor)sensor).getTouchMode();
	float[]  sample = new float [touch.sampleSize()];
	private boolean käynnissä = true;

	/**
	 * Säie katsoo onko painiketta painettu. Jos on, ohjelma sulkeutuu.
	 */
	public void run() {
		while(käynnissä) {
			touch.fetchSample(sample, 0);
			if(sample[0]>0.5) {
				System.out.println("Hätäseis painettu!");
				System.exit(0);
			}
		}

	} 
	
	/**
	 * Säikeen toiminnan lopettaminen
	 */
	public void lopeta() {
		käynnissä = false;
	}
	

}