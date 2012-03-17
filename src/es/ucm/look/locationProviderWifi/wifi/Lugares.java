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

import java.util.HashMap;

import es.ucm.look.locationProviderWifi.util.DeviceReader;


/**
 * Loads the nodes information file and provides fast access to its data.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class Lugares {
	
	/**
	 * Singleton instance of the object.
	 */
	private static Lugares INSTANCE = new Lugares();

	/**
	 * Nodes information file.
	 */
	public static final String FICHERO_LUGARES = "/sdcard/Lugares.txt";

	/**
	 * Stores node information indexed by id number
	 */
	private HashMap<String, Lugar> lugares;

	/**
	 * Default constructor. Reads the file and initializes the object.
	 */
	private Lugares() {
		lugares = new HashMap<String, Lugar>();
		DeviceReader in = new DeviceReader(FICHERO_LUGARES);

		String linea = null;

		do {
			linea = in.readln();
			if (linea != null) {

				String[] l = linea.split(" ");
				String key = l[0];
				lugares.put(key, new Lugar(Integer.parseInt(l[1]), Integer.parseInt(l[2]), Integer.parseInt(l[3]), l[4]));
			}

		} while (linea != null);
		in.close();
	}

	/**
	 * Returns the instance of the object.
	 * @return instance if the object
	 * 		
	 */
	public static Lugares getInstance() {
		return INSTANCE;
	}

	/**
	 * gets node information by id nunmber.
	 * @param nodo
	 * 			id number of the node
	 * @return Node information contained in the file.
	 */
	public Lugar getLugar(int nodo) {
		Lugar result = null;
		String key = Integer.toString(nodo);
		if (lugares.containsKey(key)) {
			result = lugares.get(key);
		} 
		return result;

	}

}
