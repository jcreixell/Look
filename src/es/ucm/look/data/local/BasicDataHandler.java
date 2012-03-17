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
package es.ucm.look.data.local;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ucm.look.data.EntityData;
import es.ucm.look.data.LookData;
import es.ucm.look.data.interfaces.DataHandler;

/**
 * A basic data getter
 * 
 * @author Ángel Serrano
 *
 */
public class BasicDataHandler implements DataHandler {
	
	private static int ID_GENERATOR = 1;
	
	private List<EntityData> newDataList;
	
	private boolean clear = false;
	
	public BasicDataHandler( ){
		newDataList = new ArrayList<EntityData>();
	}

	@Override
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date) {
		if ( clear ){
			newDataList.clear();
		}
		clear = true;
		return newDataList;
	}

	@Override
	public void addEntity(EntityData data) {
		data.setId(ID_GENERATOR++);
		if ( clear ){
			newDataList.clear();
			clear = false;
		}
		newDataList.add(data);	
	}

	@Override
	public void updatePosition(EntityData data, float x, float y, float z) {
		data.getLocation().set(x, y, z);
	}

	@Override
	public void updateProperty(EntityData data, String property, String newValue) {
		data.setPropertyValue(property, newValue);
		newDataList.add(data);
		LookData.getInstance().updateData();
	}
}
