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

import java.util.ArrayList;

/**
 * Mantains information of an Access Point
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class NodoWifi implements Comparable<Object> {
	
	/**
	 * MAX signal level - MIN signal level
	 */
	public static final int MAX_DIF = 100;

	/**
	 * MAC address of the access point.
	 */
	private String bssid;
	
	/**
	 * Signal level of the access point
	 */
	private int level;
	
	/**
	 * id number of the access point
	 */
	private int id;

	/**
	 * Parameterized contructor.
	 * @param bssid
	 * 			MAC address of the access point
	 * @param level
	 * 			Signal level
	 * @param id
	 * 			Id nunmber
	 */
	public NodoWifi(String bssid, int level, int id) {
		this.bssid = bssid;
		this.level = level;
		this.id = id;
		
	}

	/**
	 * Returns MAC address of the access point
	 * @return MAC address
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * Returns signal level of the access point
	 * @return signal level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Returns access point information as a string
	 * @return String representing AP information
	 */
	public String toString() {
		return ("Nodo: " + bssid + " " + level + " " + id);
	}
	
	
	
	/**
	 * Compares the current AP signal with another and returns the similarity level between them.
	 * @param signal
	 * 			Signal level of the other AP.
	 * @return Similarity between signals
	 */
	//de 1 a 100, grado de similitud en la se�al recibida
	//signal1 en formato de 1 a 35
	public int getSignalSimilarity(int signal) {
		int signal1 = java.lang.Math.abs(level);//WifiManager.calculateSignalLevel(level,35);
		int signal2 = java.lang.Math.abs(signal);//WifiManager.calculateSignalLevel(signal,35);
		
		int dif = java.lang.Math.abs(signal2-signal1);
		int similarity = MAX_DIF-dif;
		
		int result = (Math.round(((similarity/10))));

		return result;
	}

	/**
	 * Returns the id number of the access point
	 * @return id number
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the id number of the access point as a string
	 * @return id number as a string
	 */
	public String getKey() {
		return (Integer.toString(id));
	}
	
	/**
	 * Calculates the average signal of a list of signal values
	 * @param l
	 * 			List of signal levels
	 * @return mean of the signal levels
	 */
	public static int averageLevel(ArrayList<Integer> l) {
		int sum = 0;
		for (int i =0; i < l.size(); i++) {
			sum += l.get(i);
		}
		return (sum/l.size());
	}
	
	/**
	 * Calculates the maximum signal of a list of signal values
	 * @param l
	 * 			List of signal levels
	 * @return maximum value
	 */
	public static int maxLevel(ArrayList<Integer> l) {
		int max = -100;
		for (int i =0; i < l.size(); i++) {
			if (l.get(i) > max) {
				max = l.get(i);
			}
		}
		return (max);
	}
	
	
	/**
	 * Compares two nodes by signal level
	 * @param o
	 * 			Node to compare with the current
	 * @return 0 if equals, -1 if current node lower, 1 if current node higher
	 */
	@Override
	public int compareTo(Object o) {
		int result = 0;
		NodoWifi nodo = (NodoWifi)o;
		if (nodo.level == this.level)
			result = 0;
		else if (this.level > nodo.level)
			result = 1;
		else
			result = -1;
		
		return result;
		
	}

}
