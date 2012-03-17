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

import java.util.HashMap;
import java.util.Map;

import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.data.remote.LookProperties;

/**
 * Class holding the minimum data to represent an entity
 * @author Ángel Serrano
 *
 */
public class EntityData {
	
	/**
	 * Type
	 */
	private String type;

	/**
	 * Id
	 */
	private int id;

	/**
	 * Properties
	 */
	protected Map<String, String> properties;

	/**
	 * Location for the entity
	 */
	private Point3 location;
	
	/**
	 * 
	 * Constructs an entity with the given id, type and location
	 * 
	 * @param id
	 *            the id
	 * @param type
	 *            the type
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 */
	public EntityData(int id, String type, float x, float y, float z) {
		this.id = id;
		this.type = type;
		properties = new HashMap<String, String>();
		properties.put(LookProperties.PROPERTY_TYPE, type);
		location = new Point3(x, y, z);
	}

	public EntityData(int id, String type, float x, float y, float z,
			Map<String, String> properties) {
		this(id, type, x, y, z);
		this.properties = properties;
	}

	public EntityData(int id, String type) {
		this(id, type, 0.0f, 0.0f, 0.0f);
	}
	
	public EntityData( String type ){
		this(-1, type );
	}
	
	public EntityData( ){
		properties = new HashMap<String, String>();
		location = new Point3(0, 0, 0);
	}
	
	/**
	 * Returns entity's type
	 * 
	 * @return entity's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the value for the given key
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 */
	public String getPropertyValue(String key) {
		return properties.get(key);
	}

	/**
	 * Sets a the property value for the given key
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value for the property
	 */
	public void setPropertyValue(String key, String value) {
		properties.put(key, value);
		if ( key.equals(LookProperties.PROPERTY_TYPE)){
			type = value;
		}
	}

	/**
	 * Returns the location for the entity
	 * 
	 * @return the location for the entity
	 */
	public Point3 getLocation() {
		return location;
	}

	/**
	 * Sets the location for this entity
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public void setLocation(float x, float y, float z) {
		location.x = x;
		location.y = y;
		location.z = z;
	}

	/**
	 * Returns the unique id for the entity
	 * 
	 * @return the unique id for the entity
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the properties for this entity
	 * 
	 * @param properties
	 *            the properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setId(int id) {
		this.id = id;
	}



}
