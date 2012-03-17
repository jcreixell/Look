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

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;

import es.ucm.look.ar.ar3D.Drawable3D;
import es.ucm.look.ar.ar3D.core.Color4;
import es.ucm.look.ar.ar3D.core.TextureFactory;
import es.ucm.look.ar.math.collision.Armature;
import es.ucm.look.ar.math.collision.SphericalArmature;
import es.ucm.look.ar.math.geom.Matrix3;

/**
 * Represents an 3D entity that can be contained in a scene
 * 
 * @author Ángel Serrano
 * 
 */
public class Entity3D implements Drawable3D {

	/**
	 * Matrix with entity transformation
	 */
	private Matrix3 matrix;

	/**
	 * The {@link Mesh3D} that will be drawn in the {@link Entity3D#draw(GL10)}
	 * method
	 */
	protected Mesh3D drawable;

	/**
	 * Entity's material
	 */
	protected Color4 material;

	/**
	 * If entity has texture
	 */
	protected boolean isTextured = false;

	/**
	 * Texture identifier
	 */
	protected int texture;

	private int textureBind;
	
	private Bitmap textureBitMap;

	private boolean textureInit = false;

	private String textureString;

	/**
	 * If entity is affect by light
	 */
	private boolean lighted;

	/**
	 * Constructs an entity with a drawable. Its initial position and rotation
	 * is ( 0, 0, 0 )
	 * 
	 * @param id
	 *            the id
	 * @param drawable
	 *            the drawable
	 */
	public Entity3D(Mesh3D drawable) {
		matrix = new Matrix3();
		this.drawable = drawable;
		material = new Color4(1.0f, 1.0f, 1.0f);
		lighted = true;
	}

	/**
	 * Returns the matrix with the current transformation for the entity
	 * 
	 * @return the matrix with the current transformation for the entity
	 */
	public Matrix3 getMatrix() {
		return matrix;
	}

	/**
	 * Sets the drawable for this entity
	 * 
	 * @param drawable
	 *            the drawable
	 */
	public void setDrawable(Mesh3D drawable) {
		this.drawable = drawable;
	}

	/**
	 * Draws this entity into the given GL context
	 * 
	 * @param gl
	 *            the GL context
	 */
	public void draw(GL10 gl) {
		if (drawable != null) {
			if (lighted) {
				gl.glEnable(GL10.GL_LIGHTING);
			} else
				gl.glDisable(GL10.GL_LIGHTING);

			if (isTextured) {
				if (!textureInit) {
					initTexture(gl);
					textureInit = true;
				}
				gl.glEnable(GL10.GL_TEXTURE_2D);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureBind);

			} else {
				gl.glDisable(GL10.GL_TEXTURE_2D);
			}

			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK,
					GL10.GL_AMBIENT_AND_DIFFUSE, material.rgba, 0);
			gl.glPushMatrix();
			gl.glMultMatrixf(matrix.getMatrix(), 0);
			drawable.draw(gl);
			gl.glPopMatrix();
		}
	}

	private void initTexture(GL10 gl) {
		if ( textureBitMap != null ){
			textureBind = TextureFactory.getInstance().getTexture(textureBitMap, false);
		}
		else if (textureString != null) {
			textureBind = TextureFactory.getInstance()
					.getTexture(textureString);
		} else
			textureBind = TextureFactory.getInstance().getTexture(texture);
	}

	/**
	 * Returns an {@link Armature} in the local system coordiantes of this
	 * entity
	 * 
	 * @return an {@link Armature} in the local system coordiantes of this
	 *         entity
	 */
	public Armature getArmature() {
		if (drawable != null)
			return drawable.getArmarture();
		return null;
	}

	/**
	 * Updates entity the given time
	 * 
	 * @param elapsed
	 *            elapsed time since last update (in milliseconds)
	 */
	public void update(long elapsed) {
		if (drawable != null)
			drawable.update(elapsed);
	}

	/**
	 * Sets entity's material
	 * 
	 * @param m
	 *            entity's material
	 */
	public void setMaterial(Color4 m) {
		material = m;
	}

	/**
	 * Returns entity's material
	 * 
	 * @return entity's material
	 */
	public Color4 getMaterial() {
		return material;
	}

	/**
	 * Sets the texture for this entity
	 * 
	 * @param texture
	 *            the resource
	 */
	public void setTexture(int texture) {
		this.isTextured = true;
		this.textureInit = false;
		this.texture = texture;
		this.textureBitMap = null;
		this.textureString = null;
	}

	/**
	 * Sets the texture for this entity
	 * 
	 * @param uri
	 *            the uri for the texture
	 */
	public void setTexture(String uri) {
		this.textureString = uri;
		this.isTextured = true;
		this.textureInit = false;
	}

	/**
	 * Sets whether entity is affect by light
	 */
	public void setLighted(boolean lighted) {
		this.lighted = lighted;
	}

	/**
	 * Returns the integer associated to the open gl texture used by this entity
	 * 
	 * @return
	 */
	public int getTextureBind() {
		return textureBind;
	}
	
	public void setTextureBind( int textureBind ){
		this.textureBind = textureBind;
	}

	public float getRadius() {
		if (getArmature() instanceof SphericalArmature)
			return ((SphericalArmature) getArmature()).getRadius() * 25;
		else
			return 50;
	}

	public void setTexture(Bitmap bitmap) {
		this.textureBitMap = bitmap;
	}

}
