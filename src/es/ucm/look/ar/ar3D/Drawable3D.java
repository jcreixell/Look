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
package es.ucm.look.ar.ar3D;

import javax.microedition.khronos.opengles.GL10;

/**
 * Implemented by all those classes that can be drawn in a {@link GL10} context
 * 
 * @author Ángel Serrano
 * 
 */
public interface Drawable3D {

	/**
	 * Draws the element in the {@link GL10} context
	 * 
	 * @param gl
	 *            the {@link GL10} context
	 * 
	 */
	void draw(GL10 gl);

	/**
	 * Updates drawable with the elapsed time
	 * 
	 * @param elapsed
	 *            elapsed time since last update
	 */
	void update(long elapsed);
}
