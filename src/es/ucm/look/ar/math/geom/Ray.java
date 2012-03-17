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
package es.ucm.look.ar.math.geom;

/**
 * Represents a geometric ray, compound of a {@link Point3} and a
 * {@link Vector3}
 * 
 * @author Ángel Serrano
 * 
 */
public class Ray {

	private Point3 point;
	private Vector3 vector;

	/**
	 * Constructs a ray from a point and a vector
	 * 
	 * @param p
	 *            the point
	 * @param v
	 *            the vector
	 */
	public Ray(Point3 p, Vector3 v) {
		this.point = p;
		this.vector = v;
	}

	/**
	 * Returns the point in the ray that corresponds to the given t parameter
	 * 
	 * @param t
	 *            t parameter
	 * @return the corresponding point
	 */
	public Point3 getPoint(float t) {
		Point3 p = new Point3(t * vector.x, t * vector.y, t * vector.z);
		p.add(point);
		return p;
	}

	/**
	 * Returns the starting point for this ray
	 * 
	 * @return the starting point for this ray
	 */
	public Point3 getPoint() {
		return point;
	}

	/**
	 * Returns the vector defining the ray
	 * 
	 * @return the vector defining the ray
	 */
	public Vector3 getVector() {
		return vector;
	}
	
	public void setVector(float x, float y, float z){
		this.vector.set(x, y, z);
	}
	
	private static Ray r = new Ray( new Point3( 0, 0, 0 ), new Vector3( 0, 0, 0 ));
	
	public static Ray getVolatileRay( Point3 p, Vector3 v ){
		r.point = p;
		r.vector = v;
		return r;
	}

}
