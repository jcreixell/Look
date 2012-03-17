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

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.math.collision.SphericalArmature;
import es.ucm.look.ar.math.geom.Point3;


public class SquarePrimitive extends Mesh3D {
	
	public SquarePrimitive( ){
		float vertices[] = new float[]{
			0.0f, -1.0f, -1.0f,
			0.0f, 1.0f, -1.0f,
			0.0f, -1.0f, 1.0f,
			0.0f, 1.0f, 1.0f	
		};
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		armature = new SphericalArmature( new Point3( 0, 0, 0 ), 1.0f);
	}
	
	public void draw(GL10 gl){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
