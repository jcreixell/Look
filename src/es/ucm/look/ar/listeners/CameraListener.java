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
package es.ucm.look.ar.listeners;

import es.ucm.look.data.WorldEntity;

/**
 * 
 * 3D Elements can implement this listener.
 * 
 */
public interface CameraListener {

	/**
	 * Called when camera points directly to the object
	 * 
	 * @param entity
	 *            the entity pointed
	 */
	void onCameraEntered(WorldEntity entity);

	/**
	 * Called when camera points to somewhere else after point this object
	 * 
	 * @param entity
	 *            the entity
	 */
	void onCameraExited(WorldEntity entity);

}
