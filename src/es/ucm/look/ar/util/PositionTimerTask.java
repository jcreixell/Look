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

import java.util.TimerTask;

import android.content.Context;
import android.view.Display;
import android.view.Surface;
import es.ucm.look.data.World;
import es.ucm.look.locationProvider.LocationProvider;

public class PositionTimerTask extends TimerTask {

	private LocationProvider locationProvider;
	private float factor;
	private Display display;
	private World world;
	private float[] lastPosition;

	public PositionTimerTask(Display d, Context c, World world) {
		locationProvider = new LocationProvider(c);
		display = d;
		this.world = world;
		factor = 2.0f;
	}

	@Override
	public void run() {
		locationProvider.run();
		float[] f = LocationProvider.getPosition();

		if (lastPosition != null) {
			float diffX = f[0] - lastPosition[0];
			float diffY = f[1] - lastPosition[1];

				synchronized (world.getLocation()) {
					int rotation = display.getRotation();
					switch (rotation) {
					case Surface.ROTATION_0:
						world.getLocation().add(diffX * factor, 0.0f, diffY * factor);
						break;
					case Surface.ROTATION_90:
						world.getLocation().add(diffY * factor, 0.0f, -diffX * factor);
						break;
					case Surface.ROTATION_270:
						world.getLocation().add(-diffY * factor, 0.0f, diffX * factor);
						break;

					}
				}

		}

		lastPosition = f;
	}
}
