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

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Adapter of the INS to the Look! framework
 * 
 * @author Jorge Creixell Rojo
 * Based on Indoor Navigation System for Handheld Devices
 * by Manh Hung V. Le, Dimitris Saragas, Nathan Webb
 * 
 */
public class LocationProvider implements SensorEventListener, Runnable {

	/**
	 * Application context.
	 */
	private static Context CONTEXT;

	/**
	 * Android Sensor Manager.
	 */
	private SensorManager mSensorManager;

	/**
	 * Contructor. Initializes the INS and registers listeners.
	 * 
	 * @param context
	 *            Application context.
	 */
	public LocationProvider(Context context) {
		LocationProvider.CONTEXT = context;

		Positioning.initialize();

		mSensorManager = (SensorManager) CONTEXT
				.getSystemService(Context.SENSOR_SERVICE);

		Positioning.startPositioning();
		Positioning.resetINS();

		Sensor asensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		Sensor msensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor osensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mSensorManager.registerListener(this, asensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, msensor,
				SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(this, osensor,
				SensorManager.SENSOR_DELAY_FASTEST);
	}

	/**
	 * Add new motion sample to the queue
	 * 
	 * @return current context.
	 */
	public static Context getContext() {
		return CONTEXT;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		float[] data = Util.copyArray(event.values);
		if (type == Sensor.TYPE_ACCELEROMETER) {
			DeviceSensor.setDevA(data);
			Positioning.updateINS();

		} else if (type == Sensor.TYPE_MAGNETIC_FIELD) {
			DeviceSensor.setDevM(data);
			DeviceSensor.toEarthCS();
		} else if (type == Sensor.TYPE_ORIENTATION) {
			DeviceSensor.setDevO(data);
		}

	}

	/**
	 * Recalculates distance moved
	 */
	@Override
	public void run() {
		Positioning.process();

	}
	
	public void resetINS() {
		Positioning.resetINS();
	}

	/**
	 * Returns position in map coordinates.
	 * 
	 * @return Position in map coordinates
	 */
	public static float[] getMapPosition() {
		return Positioning.mapPosition();
	}

	/**
	 * Returns raw position
	 * 
	 * @return Position in world coordinates
	 */
	public static float[] getPosition() {
		return Positioning.position();
	}
	
	/**
	 * Returns relative displacement.
	 * 
	 * @return relative displacement
	 */

	public static float[] getDisplacement() {
		return Positioning.displacement();
	}
	
	/**
	 * Returns whether the device is moving or not.
	 * 
	 * @return whether the device is moving or not
	 */
	public static boolean isMoving() {
		return Positioning.isMoving();
	}

}
