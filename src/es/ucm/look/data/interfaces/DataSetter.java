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
package es.ucm.look.data.interfaces;

import es.ucm.look.data.EntityData;

/**
 * General interface for classes adding or changing data to the application
 * 
 * @author Ángel Serrano
 * 
 */
public interface DataSetter {

	/**
	 * Adds an entity to the world
	 * 
	 * @param data
	 */
	public void addEntity(EntityData data);

	/**
	 * Modifies the position for an entity
	 * 
	 * @param data
	 *            data representing the entity
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public void updatePosition(EntityData data, float x, float y, float z);

	/**
	 * Updates the value from a entity data property
	 * 
	 * @param data
	 *            entity data
	 * @param property
	 *            the property name
	 * @param newValue
	 *            the nuew value for the property
	 */
	public void updateProperty(EntityData data, String property, String newValue);
}
