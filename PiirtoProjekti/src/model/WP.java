package model;

import java.io.Serializable;



import lejos.robotics.navigation.Waypoint;

/**
 * Muuttaa transmittable Waypoint-oliot serializable WP-olioiksi
 * @author Väinö Kojo
 *
 */
public class WP implements Serializable {
	private int x, y;

	public WP(Waypoint wp) {
		this.x = (int) wp.getX();
		this.y = (int) wp.getY();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
