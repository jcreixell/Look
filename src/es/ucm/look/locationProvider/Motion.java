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
package es.ucm.look.locationProvider;


/**
 * Motion object storing timestamp and distance moved
 * 
 * @author Jorge Creixell Rojo
 * Based on Indoor Navigation System for Handheld Devices
 * by Manh Hung V. Le, Dimitris Saragas, Nathan Webb
 * 
 */
public class Motion {
	public float[] distance;
	public long time;

	public Motion() {
		this.distance = new float[2];
		this.time = 0;
	}

	public Motion(long time) {
		this.distance = new float[2];
		distance[0] = 0;
		distance[1] = 0;
		this.time = time;
	}

	public Motion(Motion m) {
		this.distance = Util.copyArray(m.distance);
		this.time = m.time;
	}
}
