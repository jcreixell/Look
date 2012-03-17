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
package es.ucm.look.ar.math.collision;

import es.ucm.look.ar.math.geom.Plane;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Ray;
import es.ucm.look.ar.math.geom.Vector3;

/**
 * A Spherical Armature. It's created from a point (sphere's center) and a
 * radius (sphere's radius)
 * 
 * @author Ángel Serrano
 * 
 */
public class SphericalArmature implements Armature {

	private Point3 center;
	private float radius;

	/**
	 * Constructs a spherical armature from its center and its radius
	 * 
	 * @param center
	 *            Center point
	 * @param radius
	 *            Sphere radius
	 */
	public SphericalArmature(Point3 center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public boolean contains(Point3 p) {
		if (p != null) {
			float distance = Vector3.getVolatileVector(p, center).module();
			return distance <= radius;
		}
		return false;
	}

	@Override
	public Point3 getIntersectionPoint(Ray r) {
		float t = Plane.getVolatilePlane(center, r.getVector()).intersects(r);
		if (t >= 0) {
			Point3 p = r.getPoint(t);
			if (this.contains(p)) {
				return p;
			} else
				return null;
		} else
			return null;
	}

	@Override
	public boolean intersects(Ray r) {
		Point3 p = this.getIntersectionPoint(r);
		return this.contains(p);

	}

	public float getRadius() {
		return radius;
	}

}
