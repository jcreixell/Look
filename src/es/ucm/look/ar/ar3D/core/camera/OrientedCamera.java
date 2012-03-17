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
package es.ucm.look.ar.ar3D.core.camera;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.util.DeviceOrientation;
import es.ucm.look.ar.util.LookARUtil;

public class OrientedCamera extends Camera3D {

	private DeviceOrientation orientation;

	private static final float DIFF = 0.05f;

	private float azimuth, roll, pitch;

	public OrientedCamera( ) {
		orientation = DeviceOrientation.getDeviceOrientation(LookARUtil.getApp());
	}

	public void setCamera(GL10 gl) {
		if (Math.abs(orientation.getAzimuth() - azimuth) > DIFF || Math.abs(orientation.getPitch() - pitch) > DIFF || Math.abs(orientation.getRoll() - roll) > DIFF) {
			azimuth = orientation.getAzimuth();
			roll = orientation.getRoll();
			pitch = orientation.getPitch();

			look.set(0.0f, 0.0f, -1.0f);
			eye.set(0.0f, 0.0f, 0.0f);
			up.set(0.0f, 1.0f, 0.0f);
			calcVectors();

			yaw(azimuth);
			pitch((float) (pitch + Math.PI ) );
		}
		super.setCamera(gl);
	}

}
