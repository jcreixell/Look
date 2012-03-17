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
package es.ucm.look.ar.ar3D.core.drawables;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.Drawable3D;
import es.ucm.look.ar.math.collision.Armature;

/**
 * Represents an 3D object that can be drawn.
 * 
 * @author Ángel Serrano
 * 
 */
public abstract class Mesh3D implements Drawable3D {

	protected FloatBuffer vertexBuffer;
	protected FloatBuffer normalBuffer;

	protected Armature armature;

	/**
	 * Constructs an entity
	 * 
	 * @param id
	 *            Entity id
	 */
	public Mesh3D() {

	}
	
	public void setVertexBuffer( FloatBuffer vertexBuffer ){
		this.vertexBuffer = vertexBuffer;
	}
	
	public void setNormalBuffer( FloatBuffer normalBuffer ){
		this.normalBuffer = normalBuffer;
	}

	/**
	 * Draw the entity into the {@link GL10} context
	 * 
	 * @param gl
	 *            {@link GL10} context
	 */
	public void draw(GL10 gl) {

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertexBuffer.capacity() / 3);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

	}

	/**
	 * Updates entity for the elapsed time
	 * 
	 * @param elapsed
	 *            elapsed time since last update
	 */
	public void update(long elapsed) {

	}

	/**
	 * Returns an {@link Armature} in the local system coordiantes of this
	 * entity
	 * 
	 * @return an {@link Armature} in the local system coordiantes of this
	 *         entity
	 */
	public Armature getArmarture() {
		return armature;
	}

}
