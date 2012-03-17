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
package es.ucm.look.data.remote;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.RemoteException;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.interfaces.DataGetter;
import es.ucm.look.data.interfaces.DataHandler;
import es.ucm.look.data.interfaces.DataSetter;
import es.ucm.look.data.local.DBDataHandler;
import es.ucm.look.data.remote.restful.ServiceManager;

/**
 * Implement the {@link DataHandler} to the persistence with the remote service
 * 
 * @author Sergio
 *
 */
public class RemoteDBHandler extends DBDataHandler implements DataGetter, DataSetter {
	
	private ServiceManager serviceManager;
	
	/**
	 * Constructor class. 
	 * 
	 * @param c
	 * 		Context when it is created
	 * @param s
	 * 		ServiceManager
	 * @param serverURL
	 * 		URL server
	 * @param fileURL
	 * 		URL to access to the files
	 */
	public RemoteDBHandler( Context c, ServiceManager s, String serverURL, String fileURL ){
		super( c );
		this.serviceManager = s;
		serviceManager.startService();
		serviceManager.bindService();
		ConfigNet.setNetConfiguration(serverURL, fileURL );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date) {
		try {
			List<EntityData> list = serviceManager.restfulService.getElementsUpdated(x, y, z, radius, date.toString());
			for ( EntityData data: list ){
				super.addEntity(data);
			}
			return list;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addEntity(EntityData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition(EntityData data, float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProperty(EntityData data, String property, String newValue) {
		// TODO Auto-generated method stub
		
	}

}
