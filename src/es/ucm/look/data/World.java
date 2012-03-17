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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import es.ucm.look.ar.math.geom.Point3;

/**
 * The world containing all the data to be represented
 * 
 * @author Ángel Serrano
 * 
 */
public class World {

	/**
	 * World's entities
	 */
	private Map<Integer, WorldEntity> entities;

	/**
	 * Location for the user
	 */
	private Point3 location;

	private static final Integer lock = 1;

	/**
	 * Constructs an empty world
	 */
	public World() {
		entities = new HashMap<Integer, WorldEntity>();
		location = new Point3(0.0f, 0.0f, 0.0f);
	}

	/**
	 * Adds an entity to the world
	 * 
	 * @param e
	 *            the entity to be added
	 */
	public void addEntity(WorldEntity e) {
		synchronized (lock) {
			entities.put(e.getId(), e);
		}
	}

	/**
	 * Removes an entity to the world
	 * 
	 * @param id
	 *            the entity's id
	 */
	public void removeEntity(Integer id) {
		synchronized (lock) {
			entities.remove(id);
		}
	}

	/**
	 * Returns the world entities
	 * 
	 * @return the world entities
	 */
	public Collection<WorldEntity> getWorldEntities() {
		return entities.values();
	}

	/**
	 * Sets the location for the user in the world
	 * 
	 * @param location
	 *            the location for the user in the world
	 */
	public void setLocation(Point3 location) {
		this.location = location;
	}

	/**
	 * Returns the user location in the world
	 * 
	 * @return the user location in the world
	 */
	public Point3 getLocation() {
		return location;
	}

	/**
	 * Returns the entity for the given id
	 * 
	 * @param id
	 *            the id
	 * @return the entity for the given id
	 */
	public WorldEntity getWorldEntity(int id) {
		return entities.get(id);
	}

	
	
	/**
	 * Remove all the entities of the world
	 */
	public void removeAllEntities() {
		synchronized (lock) {
			entities.clear();
		}
	}
	
	
	/**
	 * Updates the world animation
	 * 
	 * @param elapsed
	 *            elapsed time
	 */
	public void update(long elapsed) {
		for (WorldEntity w : entities.values()) {
			w.update(elapsed);
		}
	}

	public void draw(GL10 gl) {
		gl.glTranslatef(-getLocation().x, -getLocation().y, -getLocation().z);
		synchronized (lock) {
			for (WorldEntity w : getWorldEntities()) {
				if (w.isVisible()) {
					gl.glPushMatrix();
					gl.glTranslatef(w.getLocation().x, w.getLocation().y,
							w.getLocation().z);
					w.draw(gl);
					gl.glPopMatrix();
				}
			}
		}

	}

	

}
