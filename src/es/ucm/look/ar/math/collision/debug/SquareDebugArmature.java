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

import es.ucm.look.ar.ar3D.core.drawables.primitives.LinesLoopPrimitive;
import es.ucm.look.ar.math.collision.SquareArmature;
import es.ucm.look.ar.math.geom.Point3;


public class SquareDebugArmature extends SquareArmature implements DebugArmature {

	private LinesLoopPrimitive s;
	
	public SquareDebugArmature(Point3 topLeft, Point3 bottomLeft, Point3 bottomRight, Point3 topRight) {
		super(topLeft, bottomLeft, bottomRight, topRight);
		s = new LinesLoopPrimitive(topLeft, bottomLeft, bottomRight, topRight );
	}

	@Override
	public void debugDraw(GL10 gl) {
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
		gl.glLineWidth(4);
		s.draw(gl);
		
	}
}
