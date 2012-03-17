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
package es.ucm.look.ar.ar3D.core.drawables.primitives;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.util.LookARUtil;

public class LinePrimitive extends Mesh3D {

	public LinePrimitive(Point3 p1, Point3 p2) {
		this( p1.x, p1.y, p1.z, p2.x, p2.y, p2.z );
	}
	
	public LinePrimitive( float x1, float y1, float z1, float x2, float y2, float z2 ){
		float vertices[] = new float[] { x1, y1, z1, x2, y2, z2 };
		vertexBuffer = LookARUtil.makeFloatBuffer(vertices);
	}

	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
	}

}
