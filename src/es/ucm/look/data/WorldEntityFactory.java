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
package es.ucm.look.data;

import es.ucm.look.ar.ar2D.drawables.Text2D;
import es.ucm.look.ar.ar3D.core.drawables.primitives.Cube;

/**
 * A general world entity factory. Creates world entities from type and
 * properties. Must be extended for add functionality
 * 
 * @author Ángel Serrano
 * 
 */
public class WorldEntityFactory {
	
	protected Cube cube = new Cube();

	/**
	 * Creates a world entity for the given data
	 * 
	 * @param data
	 *            the data
	 */
	public WorldEntity createWorldEntity(EntityData data) {
		WorldEntity w = new WorldEntity(data);
		w.setDrawable2D(new Text2D( "Entiy " + data.getId() + "; Type: " + data.getType()));
		w.setDrawable3D(cube);
		return w;
	}

}
