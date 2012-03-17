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
package es.ucm.look.data;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import es.ucm.look.ar.ar2D.Drawable2D;
import es.ucm.look.ar.ar2D.drawables.Circle2D;
import es.ucm.look.ar.ar3D.Drawable3D;
import es.ucm.look.ar.ar3D.core.drawables.Entity3D;
import es.ucm.look.ar.listeners.CameraListener;
import es.ucm.look.ar.listeners.TouchListener;
import es.ucm.look.ar.math.geom.Point3;

/**
 * A world entity
 * 
 * @author Ángel Serrano
 * 
 */
public class WorldEntity implements Drawable2D, Drawable3D {
	
	/**
	 * Entity data
	 */
	private EntityData data;

	/**
	 * Entity's radius
	 */
	private float radius;

	/**
	 * Drawable 2D
	 */
	private Drawable2D drawable2D;

	/**
	 * Drawable 3D
	 */
	private Drawable3D drawable3D;

	/**
	 * If this entity is focusable
	 */
	private boolean focusable;

	/**
	 * If this entity is enabled to receive events
	 */
	private boolean enable = true;

	/**
	 * If this entity is touchable
	 */
	private boolean touchable;

	/**
	 * If this entity is visible
	 */
	protected boolean visible = true;

	/**
	 * Touch listeners list
	 */
	private List<TouchListener> touchListeners;

	/**
	 * Camera listeners list
	 */
	private List<CameraListener> cameraListeners;

	/**
	 * If 2D appearance has been initialized
	 */
	private boolean initialized2D;

	/**
	 * If 3D appearance has been initialized
	 */
	private boolean initialized3D;

	private Drawable2D touchableArea;

	public WorldEntity( EntityData data ){
		this.data = data;
		focusable = false;
		touchable = false;
	}

	
	/**
	 * Returns the entity's radius
	 * 
	 * @return the entity's radius
	 */
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * @return the drawable2D
	 */
	public Drawable2D getDrawable2D() {
		return drawable2D;
	}

	/**
	 * @param drawable2d
	 *            the drawable2D to set
	 */
	public void setDrawable2D(Drawable2D drawable2d) {
		this.drawable2D = drawable2d;
	}

	/**
	 * @return the drawable3D
	 */
	public Drawable3D getDrawable3D() {
		return drawable3D;
	}

	/**
	 * @param drawable3d
	 *            the drawable3D to set
	 */
	public void setDrawable3D(Drawable3D drawable3d) {
		drawable3D = drawable3d;
		if ( drawable3D instanceof Entity3D )
			radius = ((Entity3D) drawable3D ).getRadius();
		else
			radius = 50;
			
		
			this.touchableArea = new Circle2D(radius);
	}

	@Override
	public void draw(GL10 gl) {
		if (drawable3D != null) {
			if (!initialized3D) {
				initialized3D = true;
				init3D();
			}
			drawable3D.draw(gl);
		}
	}

	@Override
	public void draw(Canvas c) {
		if (drawable2D != null) {
			if (!initialized2D) {
				initialized2D = true;
				init2D();
			}
			drawable2D.draw(c);
		}
	}

	/**
	 * @return If this entity is enabled to receive events
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * Sets if this entity is enabled to receive events
	 * 
	 * @param enable
	 *            if this entity is enabled to receive events
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 
	 * @return if this entity is touchable
	 */
	public boolean isTouchable() {
		return touchable;
	}

	/**
	 * Sets if this entity is touchable.
	 * 
	 * @param touchable
	 *            If <b>true</b>, it'll receive touch events
	 */
	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}

	/**
	 * Sets if this entity is focusable
	 * 
	 * @param focusable
	 *            if this entity is focusable
	 */
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}

	/**
	 * Return whether this entity is focusable
	 * 
	 * @return if this entity is focusable
	 */
	public boolean isFocusable() {
		return focusable;
	}

	/**
	 * 
	 * @return If this entity is visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets if this entity is visible
	 * 
	 * @param visible
	 * 
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Adds a touch listener to this entity, and makes this entity touchable
	 * 
	 * @param t
	 *            the touch listener
	 */
	public void addTouchListener(TouchListener t) {
		if (t == null)
			return;
		touchable = true;
		if (touchListeners == null)
			touchListeners = new ArrayList<TouchListener>();

		touchListeners.add(t);
	}

	/**
	 * Adds a camera listener to this entity, and makes this entity focusable
	 * 
	 * @param c
	 *            the camera listener
	 */
	public void addCameraListener(CameraListener c) {
		if (c == null)
			return;
		focusable = true;
		if (cameraListeners == null)
			cameraListeners = new ArrayList<CameraListener>();

		cameraListeners.add(c);
	}

	/**
	 * Camera pointed to this entity
	 */
	public void cameraEntered() {
		if (cameraListeners != null)
			for (CameraListener c : cameraListeners) {
				c.onCameraEntered(this);
			}
	}

	/**
	 * Camera pointed to this entity
	 */
	public void cameraExited() {
		if (cameraListeners != null)
			for (CameraListener c : cameraListeners) {
				c.onCameraExited(this);
			}
	}

	/**
	 * Entity receives a touch event
	 * 
	 * @param action
	 *            touch's action
	 * @return true if the event was processed by the entity
	 */
	public boolean touch(MotionEvent event) {
		if (touchListeners != null) {
			float x = event.getX();
			float y = event.getY();
			for (TouchListener t : touchListeners) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					return t.onTouchDown(this, x, y);
				case MotionEvent.ACTION_UP:
					return t.onTouchUp(this, x, y);
				case MotionEvent.ACTION_MOVE:
					return t.onTouchMove(this, x, y);
				}
			}
		}
		return false;
	}

	public boolean equals(Object o) {
		if (o instanceof WorldEntity) {
			return this.data.getId() == ((WorldEntity) o).data.getId();
		}
		return false;
	}

	@Override
	public void update(long elapsed) {
		if (drawable2D != null)
			drawable2D.update(elapsed);

		if (drawable3D != null)
			drawable3D.update(elapsed);

	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		if (drawable2D != null)
			drawable2D.drawTouchableArea(c, p);
		if (touchableArea != null)
			touchableArea.drawTouchableArea(c, p);

	}

	/**
	 * Initializes 2D appearance. Must be overridden for inheriting classes
	 */
	public void init2D() {

	}

	/**
	 * Initializes 3D appearance. Must be overridden for inheriting classes
	 */
	public void init3D() {

	}

	/**
	 * Returns the entity's position
	 * @return
	 */
	public Point3 getLocation() {
		return data.getLocation();
	}


	/**
	 * Returns the entity's id
	 * @return the id
	 */
	public int getId() {
		return data.getId();
	}
	
	/**
	 * Returns entity's data
	 * @return
	 */
	public EntityData getData( ){
		return data;
	}


	/**
	 * Returns entity's type
	 * @return
	 */
	public String getType() {
		return data.getType();
	}

}
