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


public class Grid extends Mesh3D {
	public Grid() {
		float vertices[] = { 10.0f, 0.0f, 500.0f, 10.0f, 0.0f, -500.0f };

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}

	@Override
	public void draw(GL10 gl) {
		float translate = 0.0f;
		for (int j = 0; j < 2; j++) {
			gl.glRotatef(90.0f, 0, 1, 0);
			gl.glTranslatef(-100.0f, 0.0f, 0.0f);
			translate += 5.0f;
			for (int i = 0; i < 50; i++) {
				gl.glTranslatef(translate, 0.0f, 0.0f);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				gl.glDrawArrays(GL10.GL_LINES, 0, 2);
				gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
		}

	}

}
