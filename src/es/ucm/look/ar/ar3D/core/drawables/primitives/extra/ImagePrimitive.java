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
package es.ucm.look.ar.ar3D.core.drawables.primitives.extra;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.drawables.primitives.SquarePrimitive;


public class ImagePrimitive extends SquarePrimitive {
	
	public static final String NAME = "ImagePrimitive";

	private ByteBuffer textureBuffer;

	public ImagePrimitive( ) {
		byte texture[] = new byte[] { 1, 0, 1, 1, 0, 0, 0, 1 };

		textureBuffer = ByteBuffer.allocateDirect(texture.length);
		textureBuffer.order(ByteOrder.nativeOrder());
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}

	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_BYTE, 0, textureBuffer);
		super.draw(gl);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

}
