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

import java.util.Date;
import java.util.List;

import es.ucm.look.data.EntityData;

/**
 * Implemented by the classes which provides data to the application
 * 
 * @author Ángel Serrano
 * 
 */
public interface DataGetter {

	/**
	 * Returns all elements near the given point with the given radius that
	 * changed since last update
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param radius
	 *            radius to be checked. If radius is -1, is considered as
	 *            infinitum
	 * @param date
	 *            time of the last update. If date is null, all elements will be
	 *            returned
	 * @return the list with the ids
	 */
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date);

}
