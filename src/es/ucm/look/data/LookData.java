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
package es.ucm.look.data;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.util.LookARUtil;
import es.ucm.look.data.filesManager.LookFilesManager;
import es.ucm.look.data.interfaces.DataHandler;
import es.ucm.look.data.local.BasicDataHandler;
import es.ucm.look.data.remote.restful.ServiceManager;
import es.ucm.look.location.LocationManager;

/**
 * A class holding all the information required across all activities
 * 
 * 
 */
public class LookData {

	private static LookData instance = null;
	
	/**
	 * Entity's container
	 */
	private World world;

	/**
	 * Location provider
	 */
	private LocationManager location;

	/**
	 * Data getter
	 */
	private DataHandler dataHandler;
	
	/**
	 * World entity factory
	 */
	private WorldEntityFactory factory = new WorldEntityFactory( );
	
	private Timer timerLocation;
	
	/**
	 * Device location
	 */
	private Point3 deviceLocation = new Point3( 0, 0, 0 );
	
	/**
	 * Date of last update
	 */
	private Date lastUpdate = new Date( 1900, 1, 1);
	
	/**
	 * Maximum distance for an element to be added into the world
	 */
	private float distance = -1;
	
	private LookData() {
		dataHandler = new BasicDataHandler( );
		this.setWorld(new World());
		

		// Create the main directory "look" in the sd for save the files
		//filesManager = new LookFilesManager();
		//File dir = new File(ConfigNet.routeSD);
		//dir.mkdirs();
	}

	public static LookData getInstance() {
		if (instance == null)
			createInstance();
		return instance;
	}
	
	private synchronized static void createInstance() {
		if (instance == null) {
			instance = new LookData();
		}
	}
	
	public void setDataHandler(DataHandler dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	public void setWorldEntityFactory( WorldEntityFactory factory ){
		this.factory = factory;
	}
	
	/**
	 * Sets the maximum distance for an element to be added into the world
	 * @param distance the distance
	 */
	public void setDistance( float distance ){
		this.distance = distance;
	}
	
	/**
	 * Updates the data for the world
	 */
	public void updateData( ){
		 if ( dataHandler != null && factory != null ){
			 for ( EntityData data: dataHandler.getElementsUpdated(deviceLocation.x, deviceLocation.y, deviceLocation.z, distance, lastUpdate)){
				 WorldEntity w = factory.createWorldEntity(data);
				 world.addEntity(w);
			 }
		 }
		 
		 // Sets last update to now
		 lastUpdate = new Date();
	}

	/**
	 * Id to represent our entity
	 */
	private int id = -1;

	private ServiceManager servicemanager;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setWorld(World w) {
		this.world = w;
		LookARUtil.getApp().setWorld(w);
	}

	/**
	 * Starts the location provider
	 * 
	 * @param time
	 *            time between updats
	 * @param inertial
	 *            if the inertial system must be used
	 * @param wifi
	 *            if the wifi system must be used
	 */
	public void startLocation(int time, boolean inertial, boolean wifi) {
		location = new LocationManager(LookARUtil.getApp(), inertial, wifi);
		location.start();

		timerLocation = new Timer();
		timerLocation.schedule(new TimerTask() {

			@Override
			public void run() {
				if (location != null && world != null) {
					synchronized (world.getLocation()) {
						world.setLocation(location.getPosition());
					}
				}

			}

		}, 0, time);
	}
	
	/**
	 * Stops location
	 */
	public void stopLocation( ){
		if ( timerLocation != null )
			timerLocation.cancel();
		
		if ( location != null ){
			location.stop();
			location = null;
		}
	}

	/**
	 * Returns the current location
	 * 
	 * @return the current location
	 */
	public Point3 getLocation() {
		return world.getLocation();
	}

	public World getWorld() {
		return world;
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

//	public RemoteDataHandler getDataSetter() {
//		return servicemanager.restfulService;
//	}

	public void setServiceManager(ServiceManager servicemanager) {
		this.servicemanager = servicemanager;
	}

	public ServiceManager getServiceManager() {
		return servicemanager;
	}

	public LookFilesManager getFilesManager() {
		return filesManager;
	}
	
	private static LookFilesManager filesManager;

	public String getPropertyValue(int id, String propertyName ) {
		return world.getWorldEntity(id).getData().getPropertyValue(propertyName);
	}

	public Map<String, String> getAllProperties(int id) {
		return world.getWorldEntity(id).getData().getProperties();
	}

}
