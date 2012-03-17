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
import es.ucm.look.ar.math.geom.Point3;


public class CirclePrimitive extends Mesh3D {
	
	public CirclePrimitive( Point3 p, float radius, int iterations ){
		float vertices[] = new float[ iterations * 3 ];
		
		float incAng = (float) (2 * Math.PI / iterations);
		
		float angle = 0;
		for ( int i = 0; i < iterations * 3; i+=3 ){
			float x = (float) (Math.cos(angle) * radius);
			float y = (float) (Math.sin(angle) * radius);
			vertices[i] = p.x + x;
			vertices[i + 1] = p.y + y;
			vertices[i + 2] = p.z;
			angle += incAng;
		}
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	public void draw( GL10 gl ){
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, vertexBuffer.capacity() / 3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
