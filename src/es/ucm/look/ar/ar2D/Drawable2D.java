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

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Implemented by all those classes that can be drawn in a {@link Canvas}
 * 
 * @author Ángel Serrano
 * 
 */
public interface Drawable2D {

	/**
	 * Draws the element in the canvas
	 * 
	 * @param c
	 *            the canvas
	 */
	void draw(Canvas c);

	/**
	 * Updates the drawable
	 * 
	 * @param elapsed
	 *            elapsed time since last updated
	 */
	void update(long elapsed);

	/**
	 * Fills the touchable zone for the drawable only with the the given Paint.
	 * This method is used processing screen touches. It can be empty if
	 * drawable is not receiving touch events
	 * 
	 * @param c
	 *            the canvas
	 * @param p
	 *            the paint
	 */
	void drawTouchableArea(Canvas c, Paint p);

}
