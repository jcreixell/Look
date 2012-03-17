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
package es.ucm.look.ar.math.collision.debug;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.math.collision.Armature;


/**
 * A debug armature is an armature with properties only used during debug
 * processes
 * 
 * @author Ángel Serrano
 * 
 */
public interface DebugArmature extends Armature {
	
	/**
	 * Debug draw
	 * 
	 * @param gl
	 */
	public void debugDraw(GL10 gl);
}
