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
package es.ucm.look.ar.math.collision.debug;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.ar3D.core.drawables.primitives.CirclePrimitive;
import es.ucm.look.ar.ar3D.core.drawables.primitives.PointPrimitive;
import es.ucm.look.ar.math.collision.SphericalArmature;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Ray;


public class SphericalDebugArmature extends SphericalArmature implements DebugArmature {
	
	private CirclePrimitive c;
	
	private PointPrimitive p;
	
	public SphericalDebugArmature(Point3 center, float radius) {
		super(center, radius);
		c = new CirclePrimitive(center, radius, 20);
		p = new PointPrimitive(new Point3(0,0,0) );
	}
	
	private float ang = 0;
	@Override
	public void debugDraw(GL10 gl) {
		gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
		gl.glPushMatrix();
		gl.glRotatef(ang++, 1, 1, 1);
		c.draw(gl);
		gl.glPopMatrix();
		p.draw(gl);
	}
	
	public Point3 getIntersectionPoint(Ray r) {
		Point3 p = super.getIntersectionPoint(r);
		if ( p != null ){
			this.p.setPoint(p);
		}
		return p;
	}
}
