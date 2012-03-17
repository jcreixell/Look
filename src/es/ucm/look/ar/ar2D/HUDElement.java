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
package es.ucm.look.ar.ar2D;

import android.view.MotionEvent;

/**
 * A HUD element to be represented at the 2D layer
 * 
 * @author Ángel Serrano
 * 
 */
public interface HUDElement extends Drawable2D {

	/**
	 * If the elements contains the point
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return if the element contains the pint
	 */
	public boolean contains(float x, float y);

	/**
	 * Process a motion event
	 * 
	 * @param motionEvent
	 *            the motion event
	 * @return if the event was processed by the HUD Element
	 */
	public boolean touch(MotionEvent motionEvent);

}
