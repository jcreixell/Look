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

import javax.microedition.khronos.opengles.GL10;

public class BasicHud implements HUD {

	public Button b;

	public BasicHud(int width, int height) {
		float buttonWidth = width / 5;
		float buttonHeight = height / 5;
		b = new Button(width - buttonWidth - 5, height - buttonHeight - 5, buttonWidth, buttonHeight);
	}

	@Override
	public void draw(GL10 gl, float width, float height ) {
		b.draw(gl);

	}

	@Override
	public boolean touch(float x, float y, int type) {
		if ( b.contains(x, y) ){
			b.actionPerformed();
			return true;
		}
		return false;
		
	}

}
