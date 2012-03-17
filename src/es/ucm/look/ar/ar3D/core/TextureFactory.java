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
package es.ucm.look.ar.ar3D.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * Holds all textures manipulation
 * 
 * @author Ángel Serrano
 * 
 */
public class TextureFactory {

	/**
	 * Map for textures of app resources
	 */
	private Map<Integer, Integer> textures;

	/**
	 * Map for textures of external resources
	 */
	private Map<String, Integer> texturesURI;

	/**
	 * Singleton instance
	 */
	private static TextureFactory instance;

	/**
	 * App context
	 */
	private Context c;

	/**
	 * GL10 context to generate the textures
	 */
	private GL10 gl;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            App context
	 * @param gl
	 *            GL10 context to generate the textures
	 */
	private TextureFactory(Context c, GL10 gl) {
		this.c = c;
		this.gl = gl;
		textures = new HashMap<Integer, Integer>();
		texturesURI = new HashMap<String, Integer>();
	}

	/**
	 * Initializes texture factory instance
	 * 
	 * @param c
	 *            App context
	 * @param gl
	 *            GL10 context to generate the textures
	 */
	public static void init(Context c, GL10 gl) {
		instance = new TextureFactory(c, gl);
	}

	/**
	 * Returns an instance of TextureFactory
	 * 
	 * @return an instance of TextureFactory
	 */
	public static TextureFactory getInstance() {
		return instance;
	}

	/**
	 * Returns the texture id for an URI. This id can be bind whit
	 * {@link GL10#glBindTexture(int, int)}
	 * 
	 * @param uri
	 *            the absolute uri for the resource
	 * @return the texture name or id
	 */
	public int getTexture(String uri) {
		if (texturesURI.containsKey(uri)) {
			return texturesURI.get(uri);
		} else {
			try {
				FileInputStream is = new FileInputStream(uri);
				int t = generateTexture(is);
				texturesURI.put(uri, t);
				return t;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return 0;
			}
		}

	}

	/**
	 * Returns the texture id for an app resource. This id can be bind whit
	 * {@link GL10#glBindTexture(int, int)}
	 * 
	 * @param id
	 *            the resource id
	 * @return the texture name or id
	 */
	public int getTexture(int id) {
		Integer idI = new Integer(id);
		if (textures.containsKey(idI)) {
			return textures.get(idI).intValue();
		} else {
			InputStream is = c.getResources().openRawResource(id);
			int texture = generateTexture(is);
			textures.put(new Integer(id), new Integer(texture));
			return texture;
		}
	}

	/**
	 * Generate a texture name for the given InputStream
	 * 
	 * @param is
	 *            the input stream
	 * @return the texture name or id
	 */
	private int generateTexture(InputStream is) {
		

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		return getTexture(bitmap, true);

	}

	public int getTexture(Bitmap bitmap, boolean recycle ) {
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		if ( recycle )
			bitmap.recycle();
		
		return textures[0];
	}

}
