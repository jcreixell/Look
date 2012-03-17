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
package es.ucm.look.ar.ar3D.core.camera;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Vector3;

public class Camera3D {

	/**
	 * Represents a vector pointing to the north in the scene. This vector will
	 * be take as reference for some calculations
	 */
	public static final Vector3 NORTH = new Vector3(0, 0, 1);

	/**
	 * Represents a vector pointing towards the sky, and perpendicular to it
	 */
	public static final Vector3 UP = new Vector3(0, 1, 0);

	/**
	 * Point pointed by the camera
	 */
	public Point3 eye;

	public Vector3 look;

	protected Vector3 up;

	protected Vector3 u, v, n;

	public Camera3D() {
		eye = new Vector3(0, 0, 0);
		look = new Vector3(NORTH);
		up = new Vector3(UP);
		n = new Vector3(0, 0, 0);
		u = new Vector3(0, 0, 0);
		v = new Vector3(0, 0, 0);
		calcVectors();
	}

	public void setCamera(GL10 gl) {
		GLU.gluLookAt(gl, eye.x, eye.y, eye.z, look.x, look.y, look.z, up.x, up.y, up.z);
	}

	public void setPosition(Point3 p) {
		eye.set(p);
	}

	public void calcVectors() {
		// n= eye - look
		n.set(eye.x - look.x, eye.y - look.y, eye.z - look.z);
		n.normalize();

		// u = up X n
		u = up.crossProduct(n);
		u.normalize();

		// v = n X u
		v = n.crossProduct(u);
	}

	public void roll(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		Vector3 t = new Vector3(u.x, u.y, u.z);
		Vector3 s = new Vector3(v.x, v.y, v.z);

		u.set(cos * t.x + sin * s.x, cos * t.y + sin * s.y, cos * t.z + sin * s.z);
		u.normalize();
		v.set(-t.x * sin + s.x * cos, -t.y * sin + s.y * cos, -t.z * sin + s.z * cos);
		v.normalize();

		up.set(v.x, v.y, v.z);
	}

	public void pitch(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		Vector3 t = new Vector3(v.x, v.y, v.z);
		Vector3 s = new Vector3(n.x, n.y, n.z);

		v.set(t.x * cos - s.x * sin, t.y * cos - s.y * sin, t.z * cos - s.z * sin);
		v.normalize();
		n.set(t.x * sin + s.x * cos, t.y * sin + s.y * cos, t.z * sin + s.z * cos);
		n.normalize();

		float module = Vector3.getVolatileVector(eye, look).module();

		look.set(n.x, n.y, n.z);
		look.scale(-module);
		look.add(eye);

		up.set(v.x, v.y, v.z);

	}

	public void yaw(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

		Vector3 t = new Vector3(n.x, n.y, n.z);
		Vector3 s = new Vector3(u.x, u.y, u.z);

		n.set(t.x * cos + s.x * sin, t.y * cos + s.y * sin, t.z * cos + s.z * sin);
		n.normalize();
		u.set(s.x * cos - t.x * sin, s.y * cos - t.y * sin, s.z * cos - t.z * sin);
		u.normalize();

		float module = Vector3.getVolatileVector(eye, look).module();

		look.set(n.x, n.y, n.z);
		look.scale(-module);
		look.add(eye);

	}
}
