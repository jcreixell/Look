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
 * INS extern API. Mantains INS information and provides access methods.
 * 
 * @author Jorge Creixell Rojo
 * Based on Indoor Navigation System for Handheld Devices
 * by Manh Hung V. Le, Dimitris Saragas, Nathan Webb
 * 
 */



public class Positioning {

	private static InertialNavigationSystem ins;
	private static Object insLock = new Object();
		
	

	public static void initialize() {
		ins = new InertialNavigationSystem();
		displacement[0] = 0;
		displacement[1] = 0;
		position[0] = 0;
		position[1] = 0;
		mapPosition[0] = 0;
		mapPosition[1] = 0;
	}


	/*
	 * ------------------------------------------------------------------------
	 * 
	 * INERTIAL NAVIGATION SYSTEM
	 * ------------------------------------------------------------------------
	 */
	public static void updateINS() {
		synchronized (insLock) {
			ins.addMotion();
		}
	}

	public static float[] getDisplacement() {
		synchronized (insLock) {
			return ins.displacement();
		}
	}

	public static void resetINS() {
		synchronized (insLock) {
			ins.reset();
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 * 
	 * POSITIONING INTEGRATION
	 * ------------------------------------------------------------------------
	 */
	public static void process() {
		
		
		displacement = getDisplacement();
		position[0]+= displacement[0];
		position[1]+= displacement[1];
		
		//mapPosition = MapaUCM.toMapCS(position);
		
		isMoving = DeviceSensor.isMoving();
		
		
		resetINS();
	}
	
	
	
	
	/*
	 * ------------------------------------------------------------------------
	 * 
	 * SYNCHRONIZED POSITION
	 * ------------------------------------------------------------------------
	 */
	private static float[] position = new float[2];
	private static Object positionLock = new Object();

	public static void position(float[] p) {
		synchronized (positionLock) {
			Util.copyArray(position, p);
		}
	}

	public static float[] position() {
		synchronized (positionLock) {
			return Util.copyArray(position);
		}
	}

	private static float[] displacement = new float[2];
	private static Object displacementLock = new Object();

	public static void displacement(float[] p) {
		synchronized (displacementLock) {
			Util.copyArray(displacement, p);
		}
	}

	public static float[] displacement() {
		synchronized (displacementLock) {
			return Util.copyArray(displacement);
		}
	}
	
	
	
	

	/*
	 * ------------------------------------------------------------------------
	 * 
	 * SYNCHRONIZED POSITION
	 * ------------------------------------------------------------------------
	 */
	private static float[] mapPosition = new float[2];
	private static Object mapPositionLock = new Object();

	public static void mapPosition(float[] p) {
		synchronized (mapPositionLock) {
			Util.copyArray(mapPosition, p);
		}
	}

	public static float[] mapPosition() {
		synchronized (mapPositionLock) {
			return Util.copyArray(mapPosition);
		}
	}
	
	
	
	private static boolean isMoving = false;
	private static Object isMovingLock = new Object();

	public static boolean isMoving() {
		synchronized (isMovingLock) {
			return isMoving;
		}
	}
	
	
	


	/*
	 * ------------------------------------------------------------------------
	 * 
	 * SYNCHRONIZED POSITIONING
	 * ------------------------------------------------------------------------
	 */
	private static boolean isPositioning;
	private static Object isPositioningLock = new Object();

	public static void startPositioning() {
		synchronized (isPositioningLock) {
			isPositioning = true;
		}
	}

	public static void stopPositioning() {
		synchronized (isPositioningLock) {
			isPositioning = false;
		}
	}

	public static boolean isPositioning() {
		synchronized (isPositioningLock) {
			return isPositioning;
		}
	}
}
