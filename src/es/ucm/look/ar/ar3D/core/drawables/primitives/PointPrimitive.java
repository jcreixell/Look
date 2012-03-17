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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.math.geom.Point3;


public class PointPrimitive extends Mesh3D {

	private Point3 point;
	private FloatBuffer vertexBuffer;

	public PointPrimitive(Point3 p) {
		point = new Point3( 0.0f, 0.0f, 0.0f );
		initPoint(p);
	}

	public void setPoint(Point3 p) {
		initPoint(p);
	}

	private void initPoint(Point3 p) {
		this.point.x = p.x;
		this.point.y = p.y;
		this.point.z = p.z;
		float vertices[] = point.getCoordinatesArray();

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	@Override
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glPointSize(5.0f);
		gl.glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
		gl.glDrawArrays(GL10.GL_POINTS, 0, 1);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public void update(long elapsed) {
		// TODO Auto-generated method stub

	}

}
