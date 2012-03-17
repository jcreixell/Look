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

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.ar3D.parser.MeshObjParser;
import es.ucm.look.ar.math.collision.SphericalArmature;

public class ObjMesh3D extends Mesh3D {
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer textureBuffer;
	private int numVertices;

	public ObjMesh3D(Context c, int resourceId) {
		create( c.getResources().openRawResource(resourceId) );
	}
	
	public ObjMesh3D(InputStream inputStream){
		create(inputStream);
	}
	
	public void create( InputStream inputStream ){
		MeshObjParser parser = new MeshObjParser();
		if (parser.parse(inputStream)) {

			float vertices[] = parser.getVertices();

			ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			vertexBuffer = byteBuf.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);

			numVertices = vertices.length / 3;

			float normals[] = parser.getNormals();

			ByteBuffer byteBuf2 = ByteBuffer.allocateDirect(normals.length * 4);
			byteBuf2.order(ByteOrder.nativeOrder());
			normalBuffer = byteBuf2.asFloatBuffer();
			normalBuffer.put(normals);
			normalBuffer.position(0);

			float texture[] = parser.getTextureCoords();

			ByteBuffer byteBuf4 = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf4.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf4.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);

			armature = new SphericalArmature(parser.getCenter(),
					parser.getRadius());
		}
	}

	public void draw(GL10 gl) {

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numVertices);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

}
