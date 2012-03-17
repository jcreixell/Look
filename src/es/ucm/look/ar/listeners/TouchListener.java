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
 * Listener for touch events
 * 
 * @author Ángel Serrano
 * 
 */
public interface TouchListener {

	/**
	 * Called on touch down event
	 * 
	 * @param e
	 *            entity touched
	 * @param x
	 *            x screen coordinate for the event
	 * @param y
	 *            y screen coordinate for the event
	 * @return if the event has been processed
	 */
	boolean onTouchDown(WorldEntity e, float x, float y);

	/**
	 * Called on touch up event
	 * 
	 * @param e
	 *            entity touched
	 * @param x
	 *            x screen coordinate for the event
	 * @param y
	 *            y screen coordinate for the event
	 * @return if the event has been processed
	 */
	boolean onTouchUp(WorldEntity e, float x, float y);

	/**
	 * Called on touch move event
	 * 
	 * @param e
	 *            entity touched
	 * @param x
	 *            x screen coordinate for the event
	 * @param y
	 *            y screen coordinate for the event
	 * @return if the event has been processed
	 */
	boolean onTouchMove(WorldEntity e, float x, float y);

}
