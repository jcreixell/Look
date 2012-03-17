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


public class LinesLoopPrimitive extends Mesh3D {
	
	public LinesLoopPrimitive( Point3... points ){
		float vertices[] = new float[points.length*3];
		int i = 0;
		for ( Point3 p: points ){
			vertices[i++] = p.x;
			vertices[i++] = p.y;
			vertices[i++] = p.z;		
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
		gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
