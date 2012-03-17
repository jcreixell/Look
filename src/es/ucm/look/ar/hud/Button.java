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
package es.ucm.look.ar.hud;

import es.ucm.look.ar.ar3D.core.drawables.primitives.TrianglePrimitive;
import es.ucm.look.ar.math.geom.Point3;

public class Button extends TrianglePrimitive {

	private float x, y, width, height;
	
	private ActionListener actionListener;
	
	private boolean pressed;

	public Button(float x, float y, float width, float height) {
		super(new Point3(x, y + height, 0.0f), new Point3(x + width, y + height, 0.0f), new Point3(x + width / 2, y, 0.0f));
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		pressed = false;

	}

	public boolean contains(float touchX, float touchY) {
		return x < touchX && touchX < x + width && y < touchY && touchY < y + height;
	}
	
	public void setPressed( boolean p ){
		this.pressed = p;
	}
	
	public boolean isPressed(){
		return pressed;
	}
	
	public void actionPerformed( ){
		actionListener.actionPerformed(this);
	}
	
	public void setActionListener( ActionListener listener ){
		this.actionListener = listener;
	}
}
