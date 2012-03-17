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
package es.ucm.look.location;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.locationProvider.LocationProvider;
import es.ucm.look.locationProvider.map.Mapa;
import es.ucm.look.locationProviderWifi.WifiLocation;
import es.ucm.look.locationProviderWifi.wifi.Lugar;

/**
 * Provides a public API for the location modules.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class LocationManager {
	
	/**
	 * Inertial Navigation System Refresh Rate in miliseconds.
	 */
	private static final int INS_REFRESH_RATE = 200;
	
	/**
	 * Wifi Location System Refresh Rate in miliseconds.
	 */
	private static final int WIFI_REFRESH_RATE = WifiLocation.MAX_COUNT * 1000;
	
	private Point3 lastLocation = new Point3(0,0,0);


	
	/**
	 * Application context.
	 */
	Context context = null;
	
	/**
	 * Whether or not to use the inertial navigation system.
	 */
	private boolean ins = false;
	
	/**
	 * Whether or not to use the wifi location system.
	 */
	private boolean wifi = false;
	
	/**
	 * Timer to schedule wifi location updates.
	 */
	private Timer wifiTimer = null;
	
	/**
	 * Timer to schedule inertial navigation system updates.
	 */
	private Timer insTimer = null;
	
	/**
	 * Current location in (x,y,z) coordinates.
	 */
	private Point3 location = new Point3(0, 0, 0);
	
	/**
	 * Inertial Navigation System location provider.
	 */
	private LocationProvider insLocationProvider = null;
	
	/**
	 * Wifi location provider.
	 */
	private WifiLocation wifiLocationProvider = null;



	
	/**
	 * Initializes the location system.
	 * @param context
	 * 			Application context
	 * @param ins
	 * 			Whether to use the inertial navigation system
	 * @param wifi
	 * 			Whether to use the wifi positioning system
	 */
	public LocationManager(Context context, boolean ins, boolean wifi) {
		super();
		this.ins = ins;
		this.wifi = wifi;
		this.context = context;
	}
	
	/**
	 * Run the selected location systems
	 */
	public void start() {
		if (ins) {
			insLocationProvider= new LocationProvider(context);
			insTimer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					insLocationProvider.run();
					float position[] = LocationProvider.getDisplacement();
					float rawCoords[] = new float[2];
					rawCoords[0] = position[0];
					rawCoords[1] = position[1];
					float[] mapCoords = Mapa.toMapCS(rawCoords);
					lastLocation = location;
					location.set(location.x + mapCoords[0], location.y + mapCoords[1], location.z);
					insLocationProvider.resetINS();
				}
			};
			insTimer.scheduleAtFixedRate(timerTask, 0, INS_REFRESH_RATE);
		}
		
		if (wifi) {
			wifiLocationProvider = WifiLocation.getInstance();
			wifiLocationProvider.start(context);
			
			wifiTimer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					Lugar lugar = wifiLocationProvider.getPosicion();
					location.set(lugar.getX(), lugar.getY(), lugar.getPlanta());
				}
			};
			wifiTimer.scheduleAtFixedRate(timerTask, 0, WIFI_REFRESH_RATE);
		}
	}
	
	/**
	 * Stops the running location systems 
	 */
	public void stop() {
		if (ins) {
			insTimer.cancel();
		}
		
		if (wifi) {
			wifiTimer.cancel();
			wifiLocationProvider.stop();
		}
	}
	
	/**
	 * Returns whether the user is walking or not
	 * @ return whether the user is walking
	 */
	public boolean isWalking() {
		if (ins)
			return LocationProvider.isMoving();
		else
			return false;
	}
	
	/**
	 * Returns the current wifi node information
	 * @ return current wifi node information
	 */
	public Lugar getWifiNode() {
		if (wifi)
			return wifiLocationProvider.getPosicion();
		else
			return null;
	}
	
	/**
	 * Returns the current position
	 * @ return the current position in (x,y,z) coordinates
	 */
	public Point3 getPosition() {
		return location;
	}
	
	
	public Point3 getDisplacement() {
		return new Point3(lastLocation.x-location.x, lastLocation.y-location.y, lastLocation.z-location.z);
	}
	
}
