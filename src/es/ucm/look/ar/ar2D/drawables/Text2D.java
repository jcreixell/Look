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
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import es.ucm.look.ar.ar2D.Drawable2D;

public class Text2D implements Drawable2D {

	private String text;

	private RectF rf;
	
	private int textColor;
	
	private int bubbleColor;
	
	private int bubbleBorderColor;
	
	private int alpha;

	public Text2D(String text, int textColor, int bubbleColor, int bubbleBorderColor, int alpha) {
		this.text = text;
		this.textColor = textColor;
		this.bubbleColor = bubbleColor;
		this.bubbleBorderColor = bubbleBorderColor;
		this.alpha = alpha;
	}

	public Text2D(String text) {
		this( text, Color.BLACK, Color.WHITE, Color.BLACK, 200 );
	}

	public RectF getBounds() {
		return rf;
	}

	@Override
	public void draw(Canvas c) {
		Paint p = new Paint();

		Rect r = new Rect();
		p.getTextBounds(text, 0, text.length(), r);
		c.save();
		c.translate(-r.width() / 2.0f, 0.0f );
		
		int margin = 15;
		rf = new RectF(r);
		rf.inset(-margin, -margin);
		
		// Draw bubble
		p.setColor(bubbleColor);
		p.setAlpha(alpha);
		p.setStyle(Style.FILL);
		c.drawRoundRect(rf, 5.0f, 5.0f, p);
		
		
		// Draw bubble border
		p.setColor(bubbleBorderColor);
		p.setAlpha(alpha);
		p.setStyle(Style.STROKE);
		c.drawRoundRect(rf, 5.0f, 5.0f, p);
		
		
		// Draw text
		p.setColor(textColor);
		p.setAlpha(alpha);
		p.setStyle(Style.FILL);
		c.drawText(text, 0, 0, p);
		c.restore();
		
	}

	@Override
	public void update(long elapsed) {

	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		c.drawRoundRect(rf, 5.0f, 5.0f, p);
	}

}
