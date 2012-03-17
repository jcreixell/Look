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
 * Represents a 3D point
 * 
 * @author Ángel Serrano
 * 
 */
public class Point3 extends Point2 {

	/**
	 * z coordinate
	 */
	public float z;

	/**
	 * Constructs a 3D point
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public Point3(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}

	public Point3(Point3 p) {
		this( p.x, p.y, p.z );
	}

	/**
	 * Sets point coordinates
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Adds given coordinates to 3D point coordinates
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public void add(float x, float y, float z) {
		set(this.x + x, this.y + y, this.z + z);
	}

	/**
	 * Adds the point's coordinates
	 * 
	 * @param p
	 *            the point
	 */
	public void add(Point3 p) {
		add(p.x, p.y, p.z);
	}

	public float[] getCoordinatesArray() {
		return new float[] { x, y, z };
	}

	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

	private static Point3 p = new Point3(0, 0, 0);

	public static Point3 getVolatilePoint(float x, float y, float z) {
		p.x = x;
		p.y = y;
		p.z = z;
		return p;
	}

	/**
	 * Subtracts the point's coordinates
	 * 
	 * @param p
	 *            the point
	 */
	public void subtract(Point3 p) {
		set(this.x - p.x, this.y - p.y, this.z - p.z);
	}

	/**
	 * Returns a three dimension array with the coordinates
	 * 
	 * @return
	 */
	public float[] array() {
		return new float[] { x, y, z };
	}

	public void set(float[] floatArray) {
		if (floatArray != null && floatArray.length == 3) {
			x = floatArray[0];
			y = floatArray[1];
			z = floatArray[2];
		}

	}

	/**
	 * Scales the point, dividing the current coordinates with the given scale
	 * 
	 * @param scale
	 *            the scale
	 */
	public void inverseScale(Point3 scale) {
		x /= scale.x;
		y /= scale.y;
		z /= scale.z;
	}

	/**
	 * Scales the point, multiplying the current coordinates with the given
	 * scale
	 * 
	 * @param scale
	 *            the scale
	 */
	public void scale(Point3 scale) {
		x *= scale.x;
		y *= scale.y;
		z *= scale.z;
	}
	
	/**
	 * Copies the coordinates of the given point
	 * 
	 * @param p
	 *            the point
	 */
	public void set(Point3 p) {
		x = p.x;
		y = p.y;
		z = p.z;
	}
}
