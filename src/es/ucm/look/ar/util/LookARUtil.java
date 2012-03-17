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
package es.ucm.look.ar.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import es.ucm.look.ar.LookAR;

/**
 * A class with static common functions
 * 
 * @author Ángel Serrano
 * 
 */
public class LookARUtil {

	/**
	 * Default camera angle
	 */
	public static final float CAMERA_VIEW_ANGLE = (float) (Math.PI / 3);

	private static LookAR lookAr;

	/**
	 * Init the LookARUtil
	 * 
	 * @param lookAr
	 *            Application
	 */
	public static void init(LookAR lookAr) {
		LookARUtil.lookAr = lookAr;
	}

	/**
	 * Returns the main activity
	 * 
	 * @return the main activity
	 */
	public static LookAR getApp() {
		return lookAr;
	}

	/**
	 * Returns the display for the app
	 * 
	 * @return the display for the app
	 */
	public static Display getDisplay() {
		return lookAr.getWindowManager().getDefaultDisplay();
	}

	/**
	 * Make a FloatBuffer from an array of floats
	 * 
	 * @param f
	 *            The array
	 * @return the FloatBuffer
	 */
	public static FloatBuffer makeFloatBuffer(float[] f) {
		ByteBuffer bytBuffer = ByteBuffer.allocateDirect(f.length * 4);
		bytBuffer.order(ByteOrder.nativeOrder());

		FloatBuffer floatBuffer = bytBuffer.asFloatBuffer();
		floatBuffer.put(f);
		floatBuffer.position(0);

		return floatBuffer;
	}

	public static View getView(int resourceId, ViewGroup v) {
		return lookAr.getLayoutInflater().inflate(resourceId, v);
	}

	/**
	 * Returns the minimum between the width and the height of the screen
	 * 
	 * @return the minimum between the width and the height of the screen
	 */
	public static int getMinimumSize() {
		if (getDisplay().getHeight() > getDisplay().getWidth()) {
			return getDisplay().getWidth();
		} else
			return getDisplay().getHeight();
	}

}
