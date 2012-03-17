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
package es.ucm.look.ar.ar3D.core.drawables.primitives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import es.ucm.look.ar.ar3D.core.drawables.Mesh3D;
import es.ucm.look.ar.math.geom.Plane;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Vector3;


public class TrianglePrimitive extends Mesh3D {

	public TrianglePrimitive(Point3 p1, Point3 p2, Point3 p3) {

		float vertices[] = new float[] { p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, p3.x, p3.y, p3.z };

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		Vector3 n = new Plane(p1, p2, p3).getNormal();
		n.normalize();

		float normals[] = new float[] { n.x, n.y, n.z, n.x, n.y, n.z, n.x, n.y, n.z };

		byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(normals);
		normalBuffer.position(0);
	}

}
