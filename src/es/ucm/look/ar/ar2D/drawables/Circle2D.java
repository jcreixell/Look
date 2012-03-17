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

package es.ucm.look.ar.ar2D.drawables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import es.ucm.look.ar.ar2D.Drawable2D;


/**
 * A 2D drawable circle, centered at ( 0, 0 )
 * 
 * @author Ángel Serrano
 * 
 */
public class Circle2D implements Drawable2D {

	private boolean drawCircle;

	private float radius;
	
	private Paint p;
	
	public Circle2D(float radius ){
		this( radius, false, Color.BLACK );
	}

	public Circle2D(float radius, boolean drawCircle, int color ) {
		this.radius = radius;
		this.drawCircle = drawCircle;
		p = new Paint();
		p.setColor(color);
		
	}

	@Override
	public void draw(Canvas c) {
		if (drawCircle) {
			c.drawCircle(0, 0, radius, p);
		}
	}

	@Override
	public void update(long elapsed) {

	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		c.drawCircle(0, 0, radius, p);
	}

}
