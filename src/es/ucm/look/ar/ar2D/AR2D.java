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
package es.ucm.look.ar.ar2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import es.ucm.look.ar.LookAR;
import es.ucm.look.ar.math.geom.Matrix3;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.util.DeviceOrientation;
import es.ucm.look.ar.util.LookARUtil;
import es.ucm.look.data.LookData;
import es.ucm.look.data.World;
import es.ucm.look.data.WorldEntity;

/**
 * View holding 2D representation of the world
 * 
 * @author Ángel Serrano
 * 
 */
public class AR2D extends View {

	/**
	 * Constant used in camera rotation
	 */
	private static final float DIFF_ROT = 0.01f;

	/**
	 * Current values for pitch and azimuth, to make compares
	 */
	private float oldPitch = -100.0f, oldAzimuth = -100.0f;

	/**
	 * Device orientation
	 */
	private DeviceOrientation orientation;

	/**
	 * Buffer used to process touch events
	 */
	private Bitmap buffer;

	/**
	 * Canvas holding the buffer
	 */
	private Canvas canvasBuffer;

	/**
	 * Matrix holding the camera rotation
	 */
	private Matrix3 cameraMatrix = new Matrix3();

	/**
	 * Maximum distance for an entity to be represented
	 */
	private float maxZ = 50.0f;

	/**
	 * Time of the last time update
	 */
	private long lastTime;

	/**
	 * Repaint timer
	 */
	private Timer repaintTimer;

	/**
	 * List with the entities ordered by its distance to the viewer
	 */
	private ZOrderedList zOrderedList = new ZOrderedList();

	/**
	 * Lock for the list
	 */
	private Integer listLock = new Integer(0);

	/**
	 * Canvas' width
	 */
	private int width = -1;

	/**
	 * Canvas' height
	 */
	private int height = -1;

	private float correctFactor;

	private float distance;

	private WorldEntity currentCameraEntity;

	private boolean cameraListening = true;

	private ArrayList<HUDElement> hud;

	/**
	 * Minimum scale to use with elements
	 */
	private float minScale = 0.01f;

	/**
	 * Maximum scale to use with elements
	 */
	private float maxScale = 1.0f;

	/**
	 * Creates a 2D view with the given context
	 * 
	 * @param context
	 *            the context
	 * @param maxDist
	 *            max distance to be shown
	 */
	public AR2D(LookAR context, float maxDist) {
		super(context);
		orientation = DeviceOrientation.getDeviceOrientation(context);
		repaintTimer = new Timer();
		repaintTimer.schedule(new RepaintTask(), 0, 100);
		hud = new ArrayList<HUDElement>();
		lastTime = -1;
		this.maxZ = maxDist;
		this.setClickable(true);
		this.setEnabled(true);
		this.setLongClickable(true);

	}

	/**
	 * Returns the list of drawables for the HUD
	 * 
	 * @return the list of drawables for the HUD
	 */
	public List<HUDElement> getHUD() {
		return hud;
	}

	public void addHUDElement(HUDElement hudelement) {
		hud.add(hudelement);
	}

	/**
	 * Returns the canvas width
	 * 
	 * @return the canvas width
	 */
	public int getCanvasWidth() {
		return width;
	}

	/**
	 * Returns the canvas height
	 * 
	 * @return the canvas height
	 */
	public int getCanvasHeight() {
		return height;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		long now = System.currentTimeMillis();
		long elapsed = lastTime == -1 ? 0 : now - lastTime;
		lastTime = now;

		LookData.getInstance().getWorld().update(elapsed);

		projectEntities(canvas, elapsed);

		for (EntityProjected w : zOrderedList) {
			canvas.save();
			canvas.translate(w.projection.x, w.projection.y);
			// Scale 2D with distance
			float scale = maxScale - w.projection.z * (maxScale - minScale)
					/ maxZ;
			canvas.scale(scale, scale);
			w.we.draw(canvas);
			canvas.restore();

		}

		for (Drawable2D d : hud) {
			d.update(elapsed);
			d.draw(canvas);
		}

		// synchronized (listLock) {
		// if (buffer != null)
		// canvas.drawBitmap(buffer, new Matrix(), new Paint());
		// }

		if (cameraListening) {
			checkCamera(canvas.getWidth(), canvas.getHeight());
		}

	}

	private void checkCamera(int width, int height) {
		updateBuffer();
		WorldEntity w = getEntity( width / 2, height / 2 );
		if ( currentCameraEntity != w ){
			if ( currentCameraEntity != null )
				currentCameraEntity.cameraExited();
			
			if ( w!= null )
				w.cameraEntered();
			
			currentCameraEntity = w;
		}

	}

	private void projectEntities(Canvas canvas, long elapsed) {
		synchronized (listLock) {
			World world = LookData.getInstance().getWorld();
			updateCameraRotation();
			this.zOrderedList.clear();

			if (canvas.getWidth() != width || canvas.getHeight() != height) {
				width = canvas.getWidth();
				height = canvas.getHeight();
				correctFactor = (float) width / (float) height;
				distance = (float) ((width / 2) / Math
						.tan(LookARUtil.CAMERA_VIEW_ANGLE / 2.0f))
						/ correctFactor;
			}

			for (WorldEntity we : world.getWorldEntities()) {
				if (we.isVisible()) {
					Point3 pr = new Point3(we.getLocation());
					pr.add(-world.getLocation().x, -world.getLocation().y,
							-world.getLocation().z);
					cameraMatrix.transform(pr);

					float x = distance * pr.x / pr.z + width / 2;
					float y = distance * pr.y / pr.z + height / 2;
					float z = pr.z;
					pr.set(x, y, z);

					if (pr.z > -1.0f && pr.z < maxZ) {
						this.zOrderedList.add(we, pr);
					}
				}
			}
		}

	}

	/**
	 * Projects the given point with the current situation
	 * 
	 * @param p
	 *            the point
	 */
	public void projectPoint(Point3 p) {
		World world = LookData.getInstance().getWorld();
		p.add(-world.getLocation().x, -world.getLocation().y,
				-world.getLocation().z);
		cameraMatrix.transform(p);

		float x = distance * p.x / p.z + width / 2;
		float y = distance * p.y / p.z + height / 2;
		float z = p.z;
		p.set(x, y, z);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (buffer != null) {
			buffer.recycle();
		}
		buffer = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvasBuffer = new Canvas(buffer);
	}

	/**
	 * Updates camera rotation
	 */
	private void updateCameraRotation() {
		if (Math.abs(orientation.getPitch() - oldPitch) > DIFF_ROT
				|| Math.abs(orientation.getAzimuth() - oldAzimuth) > DIFF_ROT) {
			cameraMatrix.setIdentity();
			cameraMatrix.rotate(orientation.getPitch(), -orientation.getAzimuth(), 0.0f);
			oldPitch = orientation.getPitch();
			oldAzimuth = orientation.getAzimuth();
		}
	}

	/**
	 * Updates touch buffer
	 */
	private void updateBuffer() {
		Paint p = new Paint();
		canvasBuffer.drawARGB(255, 0, 0, 0);
		for (EntityProjected w : zOrderedList) {
			canvasBuffer.save();
			canvasBuffer.translate(w.projection.x, w.projection.y);
			String s = Integer.toHexString(w.we.getId() + 1);
			while (s.length() < 6) {
				s = "0" + s;
			}
			int r = Integer.decode("0x" + s.substring(0, 2));
			int g = Integer.decode("0x" + s.substring(2, 4));
			int b = Integer.decode("0x" + s.substring(4, 6));
			p.setARGB(255, r, g, b);
			w.we.drawTouchableArea(canvasBuffer, p);
			canvasBuffer.restore();
		}

	}

	/**
	 * Repainting task
	 * 
	 * @author Ángel Serrano
	 * 
	 */
	private class RepaintTask extends TimerTask {

		@Override
		public void run() {
			LookARUtil.getApp().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AR2D.this.postInvalidate();
					AR2D.this.requestLayout();
				}

			});
		}

	}

	private class EntityProjected {

		public WorldEntity we;

		public Point3 projection;

		public EntityProjected(WorldEntity e, Point3 p) {
			we = e;
			projection = p;
		}

	}

	private class ZOrderedList extends ArrayList<EntityProjected> {

		private static final long serialVersionUID = 1L;

		public boolean add(WorldEntity e, Point3 p) {
			int i = 0;
			for (EntityProjected proj : this) {
				if (p.z > proj.projection.z) {
					break;
				}
				i++;
			}

			this.add(i, new EntityProjected(e, p));

			return true;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i("Gallery", "Event sí: " + event.getAction());
		if (!proceesEventByHUD(event))
			synchronized (listLock) {
				updateBuffer();
				WorldEntity we = getEntity(Math.round(event.getX()),
						Math.round(event.getY()));
				if (we != null && we.isEnable() && we.isTouchable()) {
					return we.touch(event);
				}
			}

		return false;
	}

	private WorldEntity getEntity(int x, int y) {
		int color = 0;
		if (buffer != null) {

			if (x > 0 && x < buffer.getWidth() && y > 0
					&& y < buffer.getHeight()) {
				color = buffer.getPixel(x, y);

				String idString = Integer.toHexString(color);
				int id = Integer.decode("0x" + idString.substring(2)) - 1;
				return LookData.getInstance().getWorld().getWorldEntity(id);
			}
		}
		return null;
	}

	private boolean proceesEventByHUD(MotionEvent event) {
		for (HUDElement e : hud) {
			if (e.contains(event.getX(), event.getY())) {
				return e.touch(event);
			}

		}
		return false;
	}

	public void setCameraListening(boolean cameraListening) {
		this.cameraListening = cameraListening;
	}

}
