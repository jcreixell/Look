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



public class Cube extends Mesh3D {
	
	public static final String DRAWABLE_NAME = "cube";

	private ByteBuffer indexBuffer;

    private float vertices[] = {
			    		-1.0f, -1.0f, 1.0f, 
			    		1.0f, -1.0f, 1.0f,  
			    		-1.0f, 1.0f, 1.0f,
			    		1.0f, 1.0f, 1.0f,
			    		
			    		1.0f, -1.0f, 1.0f,
			    		1.0f, -1.0f, -1.0f,    		
			    		1.0f, 1.0f, 1.0f,
			    		1.0f, 1.0f, -1.0f,
			    		
			    		1.0f, -1.0f, -1.0f,
			    		-1.0f, -1.0f, -1.0f,    		
			    		1.0f, 1.0f, -1.0f,
			    		-1.0f, 1.0f, -1.0f,
			    		
			    		-1.0f, -1.0f, -1.0f,
			    		-1.0f, -1.0f, 1.0f,    		
			    		-1.0f, 1.0f, -1.0f,
			    		-1.0f, 1.0f, 1.0f,
			    		
			    		-1.0f, -1.0f, -1.0f,
			    		1.0f, -1.0f, -1.0f,    		
			    		-1.0f, -1.0f, 1.0f,
			    		1.0f, -1.0f, 1.0f,
			    		
			    		-1.0f, 1.0f, 1.0f,
			    		1.0f, 1.0f, 1.0f,    		
			    		-1.0f, 1.0f, -1.0f,
			    		1.0f, 1.0f, -1.0f,
											};
    
	private float normals[] = {
			0.0f, 0.0f, 1.0f, 						
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f, 
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
			
			0.0f, 0.0f, 1.0f, 
			0.0f, 0.0f, -1.0f, 
			0.0f, 1.0f, 0.0f, 
			0.0f, -1.0f, 0.0f,
								};
    
   
    private byte indices[] = {
			    		0,1,3, 0,3,2, 			
			    		4,5,7, 4,7,6, 			
			    		8,9,11, 8,11,10, 		
			    		12,13,15, 12,15,14, 	
			    		16,17,19, 16,19,18, 	
			    		20,21,23, 20,23,22, 	
    										};

	public Cube( ) {		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		armature = new SphericalArmature( new Point3( 0, 0, 0 ), 2.0f);
	}

	public void draw(GL10 gl) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

}
