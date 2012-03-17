package es.ucm.look.data.remote.restful;

import es.ucm.look.data.remote.restful.IRemoteServiceCallBack;

import java.util.Map;

interface RemoteDataHandler {
	/**
	 * Adds an entity to the world in a position
	 * 
	 * @param type
	 *            type of the entity
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param properties
	 *            Map with the properties of the entity
	 * 
	 */
	void addElement(in String type, in float x, in float y, in float z, in Map properties );

	/**
	 * Updates an element position
	 * 
	 * @param id
	 *            element's id
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	void updateElementPosition(in int id, in float x, in float y, in float z);

	/**
	 * Updates or add a property
	 * 
	 * @param id
	 *            elemnt's id
	 * @param propertyName
	 *            property name
	 * @param propertyValue
	 *            property value
	 */
	void updateOrAddProperty(in int id, in String propertyName, in String propertyValue);

	/**
	 * For to do a Login, need to the username and password
	 * 
	 * @param username
	 *            username
	 * @param propertyName
	 *            password
	 */
    void doLogin(in String username, in String password);
    
	/**
	 * For to do a Login, need to the username and password
	 * 
	 * @param username
	 *            username
	 * @param propertyName
	 *            password
	 */
    List getElementsUpdated(float x, float y, float z, float radius, String date);
     
    void registerCallback(IRemoteServiceCallback cb);
    
   //delete element 
   
}
