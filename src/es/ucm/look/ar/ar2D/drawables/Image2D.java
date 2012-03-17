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

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import es.ucm.look.ar.ar2D.Drawable2D;

public class Image2D implements Drawable2D {

	private List<Bitmap> bitmaps;

	private int timePerFrame = 0;

	private int currentFrame = 0;

	private int elapsedTime = 0;

	private Rect dst;

	private boolean loop;

	public Image2D(Bitmap bitmap, int width, int height) {
		this.bitmaps = new ArrayList<Bitmap>();
		bitmaps.add(bitmap);
		dst = new Rect();
		dst.left = -width / 2;
		dst.right = width / 2;
		dst.top = -height / 2;
		dst.bottom = height / 2;
	}

	public Image2D(Bitmap bitmap) {
		this(bitmap, bitmap.getWidth(), bitmap.getHeight());
	}

	public Image2D(List<Bitmap> bitmaps, int timePerFrame, boolean loop) {
		this.bitmaps = bitmaps;
		this.timePerFrame = timePerFrame;
		dst = new Rect();
		int width = bitmaps.get(0).getWidth();
		int height = bitmaps.get(0).getHeight();
		dst.left = -width / 2;
		dst.right = width / 2;
		dst.top = -height / 2;
		dst.bottom = height / 2;
		this.loop = loop;
	}

	@Override
	public void draw(Canvas c) {
		Bitmap bitmap = bitmaps.get(currentFrame);
		c.drawBitmap(bitmap, null, dst, new Paint());
	}

	@Override
	public void update(long elapsed) {
		elapsedTime += elapsed;
		while (elapsedTime >= timePerFrame) {
			currentFrame++;
			elapsedTime -= timePerFrame;
		}
		if ( loop ){
			if ( currentFrame >= bitmaps.size() ){
				currentFrame = bitmaps.size() - 1;
			}
		}
		else
			currentFrame = currentFrame % bitmaps.size();

	}

	@Override
	public void drawTouchableArea(Canvas c, Paint p) {
		c.drawRect(dst, p);

	}

}
