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
package es.ucm.look.ar.ar3D.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import es.ucm.look.ar.math.geom.Point2;
import es.ucm.look.ar.math.geom.Point3;
import es.ucm.look.ar.math.geom.Vector3;

/**
 * A parser for *.obj 3D files. It supports vertices, faces, normals and
 * textures. It does NOT support materials
 * 
 * @author Ángel Serrano Laguna
 * 
 */
public class MeshObjParser {

	private static final String NAME = "o";
	private static final String VERTICE = "v";
	private static final String NORMAL = "vn";
	private static final String FACE = "f";
	private static final String TEXTURE = "vt";

	private ArrayList<Point3> vertexCoords;
	private ArrayList<Vector3> normalCoords;
	private ArrayList<Point2> textCoords;
	private ArrayList<Integer> facesIndices;
	private ArrayList<Integer> textureIndices;
	private ArrayList<Integer> normalIndices;
	private String name;
	private boolean smooth = true;

	// Some vars to store values to be used at Armature creation

	private float maxX, minX, maxY, minY, maxZ, minZ;

	public MeshObjParser() {
		vertexCoords = new ArrayList<Point3>();
		facesIndices = new ArrayList<Integer>();
		normalCoords = new ArrayList<Vector3>();
		textCoords = new ArrayList<Point2>();
		textureIndices = new ArrayList<Integer>();
		normalIndices = new ArrayList<Integer>();
	}

	/**
	 * Parse an *.obj file and fill vertex arrays, which can be accessed by
	 * {@link MeshObjParser#getVertices()}, {@link MeshObjParser#getNormals()},
	 * etc
	 * 
	 * @param resources
	 *            General resources from the app
	 * @param resourceId
	 *            Resource id
	 * @return {@code true} if everything was OK
	 * 
	 */
	public boolean parse(InputStream input) {

		maxX = maxY = maxZ = Float.MIN_VALUE;
		minX = minY = minZ = Float.MAX_VALUE;

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

		String line = null;

		try {
			while ((line = bufferedReader.readLine()) != null) {
				StringTokenizer parts = new StringTokenizer(line, " ");

				String type = parts.nextToken();
				float x, y, z;

				if (type.equals(NAME)) {
					name = parts.nextToken();
				} else if (type.equals(VERTICE)) {
					x = Float.parseFloat(parts.nextToken());
					y = Float.parseFloat(parts.nextToken());
					z = Float.parseFloat(parts.nextToken());
					vertexCoords.add(new Point3(x, y, z));
					maxX = x > maxX ? x : maxX;
					minX = x < minX ? x : minX;
					maxY = y > maxX ? y : maxX;
					minY = y < minX ? y : minX;
					maxZ = z > maxX ? z : maxX;
					minZ = z < minX ? z : minX;
				} else if (type.equals(NORMAL)) {
					x = Float.parseFloat(parts.nextToken());
					y = Float.parseFloat(parts.nextToken());
					z = Float.parseFloat(parts.nextToken());
					normalCoords.add(new Vector3(x, y, z));
				} else if (type.equals(TEXTURE)) {
					x = Float.parseFloat(parts.nextToken());
					y = Float.parseFloat(parts.nextToken()) * -1.0f;
					textCoords.add(new Point2(x, y));
				} else if (type.equals(FACE)) {
					for (int i = 0; i < 3; i++) {
						String nextToken = parts.nextToken();
						if (nextToken.contains("//")) {
							StringTokenizer faceParts = new StringTokenizer(nextToken, "//");
							facesIndices.add(Integer.parseInt(faceParts.nextToken()) - 1);

							normalIndices.add(Integer.parseInt(faceParts.nextToken()) - 1);
						} else {
							StringTokenizer faceParts = new StringTokenizer(nextToken, "/");
							facesIndices.add(Integer.parseInt(faceParts.nextToken()) - 1);

							textureIndices.add(Integer.parseInt(faceParts.nextToken()) - 1);

							normalIndices.add(Integer.parseInt(faceParts.nextToken()) - 1);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Returns mesh name
	 * 
	 * @return mesh name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the vertex array, in the form { v1.x, v1.y, v1.z, v2.x, v2.y,
	 * v2.z ... } and so on
	 * 
	 * @return the vertex array
	 */
	public float[] getVertices() {
		float[] vertices = new float[facesIndices.size() * 3];
		int i = 0;
		for (Integer index : facesIndices) {
			vertices[i++] = vertexCoords.get(index).x;
			vertices[i++] = vertexCoords.get(index).y;
			vertices[i++] = vertexCoords.get(index).z;
		}
		return vertices;
	}

	/**
	 * Returns the normals array, in the form { n1.x, n1.y, n1.z, n2.x, n2.y,
	 * ... } and so on
	 * 
	 * @return the normals array
	 */
	public float[] getNormals() {
		if (smooth) {
			ArrayList<Vector3> mergedNormalCoords = new ArrayList<Vector3>(vertexCoords.size());
			for (int i = 0; i < vertexCoords.size(); i++) {
				mergedNormalCoords.add(new Vector3(0.0f, 0.0f, 0.0f));
			}

			int i = 0;
			for (Integer vertexIndex : facesIndices) {
				int normalIndex = normalIndices.get(i++);
				mergedNormalCoords.get(vertexIndex).add(normalCoords.get(normalIndex), true);
			}

			float[] vertices = new float[facesIndices.size() * 3];
			int j = 0;
			for (Integer index : facesIndices) {
				vertices[j++] = mergedNormalCoords.get(index).x;
				vertices[j++] = mergedNormalCoords.get(index).y;
				vertices[j++] = mergedNormalCoords.get(index).z;
			}
			return vertices;

		} else {
			float[] vertices = new float[facesIndices.size() * 3];
			int i = 0;
			for (Integer index : normalIndices) {
				vertices[i++] = normalCoords.get(index).x;
				vertices[i++] = normalCoords.get(index).y;
				vertices[i++] = normalCoords.get(index).z;
			}
			return vertices;
		}
	}

	/**
	 * Returns the faces array, in the form { f1.v1, f1.v2, f1.v3, f2.v1, f2.v2,
	 * f2.v3... } and so on
	 * 
	 * @return the faces array
	 */
	public short[] getFaceIndeces() {
		short[] indices = new short[facesIndices.size()];
		int i = 0;
		for (Integer integer : facesIndices) {
			indices[i] = integer.shortValue();
			i++;
		}
		return indices;
	}

	/**
	 * Returns the texture vertices array, in the form { t1.x, t1.y, t2.x, t2.y,
	 * ... } and so on
	 * 
	 * @return the texture vertices array
	 */
	public float[] getTextureCoords() {
		float[] vertices = new float[textureIndices.size() * 2];
		int i = 0;
		for (Integer index : textureIndices) {
			vertices[i++] = textCoords.get(index).x;
			vertices[i++] = textCoords.get(index).y;
		}
		return vertices;
	}

	/**
	 * Returns the center point for the mesh
	 * 
	 * @return the center point for the mesh
	 */
	public Point3 getCenter() {
		float x = (maxX + minX) / 2;
		float y = (maxY + minY) / 2;
		float z = (maxZ + minZ) / 2;
		return new Point3(x, y, z);
	}

	/**
	 * Returns the radius of the sphere that, with its center in
	 * {@link MeshObjParser#getCenter()}, contains all the mesh
	 * 
	 * @return
	 */
	public float getRadius() {
		float xdist = Math.abs(maxX - minX) / 2;
		float ydist = Math.abs(maxX - minX) / 2;
		float zdist = Math.abs(maxX - minX) / 2;

		float max = xdist;
		max = ydist > max ? ydist : max;
		max = zdist > max ? zdist : max;
		return max;
	}

}
