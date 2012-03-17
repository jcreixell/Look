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

public interface HUD {

	public void draw( GL10 gl, float width, float height );
	
	public boolean touch( float x, float y, int type );
}
