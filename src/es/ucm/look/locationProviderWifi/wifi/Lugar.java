/**
*-----------------------------------------------------------------------------
* Copyright (c) 2012, Look! Development Team
* All rights reserved.
*
* Distributed under the terms of the BSD Simplified License.
*
* The full license is in the LICENSE file, distributed with this software.
*-----------------------------------------------------------------------------
*/
package es.ucm.look.locationProviderWifi.wifi;

/**
 * Stores information of a defined node.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class Lugar {
	/**
	 * floor.
	 */
	private int planta;
	
	/**
	 * X coordinate.
	 */
	private int x;
	
	/**
	 * Y coordinate.
	 */
	private int y;
	
	/**
	 * Name of the node.
	 */
	private String nombre;
	
	/**
	 * Parameterized contructor
	 * @param planta
	 *      floor
	 * @param x
	 *      X coordinate
	 * @param y
	 *      Y coordinate
	 * @param nombre
	 *      Node name
	 */
	public Lugar(int planta, int x, int y, String nombre) {
		super();
		this.planta = planta;
		this.x = x;
		this.y = y;
		this.nombre = nombre;
	}
	
	/**
	 * Default constructor.
	 */
	public Lugar() {
		super();
		this.planta = 0;
		this.x = 0;
		this.y = 0;
		this.nombre = "-";
	}
	
	/**
	 * Returns the floor 
	 * 
	 * @return floor
	 */
	public int getPlanta() {
		return planta;
	}
	
	/**
	 * Returns the X coordinate
	 * 
	 * @return X coordinate
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the Y coordinate
	 * 
	 * @return Y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the name 
	 * 
	 * @return name
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Returns the node as a string
	 * 
	 * @return string representing the node information
	 */
	public String toString() {
		return new String(nombre + ": " + planta + " " + x + " " + y);
	}

}
