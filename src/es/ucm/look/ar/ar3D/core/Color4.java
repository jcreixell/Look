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

public class Color4 {

	public float[] rgba;
	
	private static final float FACTOR = 0.2f;

	public Color4(float r, float g, float b, float a) {
		rgba = new float[] { r, g, b, a };
		normalizeLevels();
	}

	public Color4(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}
	
	public Color4( float[] rgba ) throws IllegalArgumentException {
		if ( rgba.length != 4 ){
			throw new IllegalArgumentException( "rgba array must have 4 components");
		}
		this.rgba = rgba;
		normalizeLevels();
	}
	
	private void normalizeLevels( ){
		for ( int i = 0; i < rgba.length; i++ ){
			rgba[i] = rgba[i] > 1.0f ? 1.0f : rgba[i];
			rgba[i] = rgba[i] < 0.0f ? 0.0f : rgba[i];
		}
	}
	
	public Color4 darker( ){
		return new Color4( rgba[0] - FACTOR, rgba[1] - FACTOR, rgba[2] - FACTOR, rgba[3]);
	}
	
	public Color4 brighter( ){
		return new Color4( rgba[0] + FACTOR, rgba[1] + FACTOR, rgba[2] + FACTOR, rgba[3]);
	}

}
