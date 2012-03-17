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

import java.util.Date;
import java.util.List;

import android.content.Context;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.LookData;
import es.ucm.look.data.interfaces.DataHandler;
import es.ucm.look.data.local.contentprovider.LookContentProvider;

/**
 * To have the persistence to a database, implements {@link DataHandler}
 * 
 * @author Sergio
 *
 */
public class DBDataHandler implements DataHandler {
	
	private LookContentProvider dataBase;
	
	public DBDataHandler( ){
		dataBase = LookContentProvider.getInstance();
	}
	
	public DBDataHandler( Context c ){
		dataBase = LookContentProvider.getInstance(c);
	}

	@Override
	public void addEntity(EntityData data) {
		dataBase.addEntity(data);
		
	}

	@Override
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date) {
		return dataBase.getElementsUpdated(x, y, z, radius, date);
	}

	@Override
	public void updatePosition(EntityData data, float x, float y, float z) {
		dataBase.updatePosition(data, x, y, z);	
	}

	@Override
	public void updateProperty(EntityData data, String property, String newValue) {
		dataBase.updateProperty(data, property, newValue);
		LookData.getInstance().updateData();
	}

}
