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
package es.ucm.test.look.data.util;

import android.app.Application;
import es.ucm.look.data.remote.restful.ServiceManager;

public class ApplicationState extends Application {
	
	ServiceManager servicemanager;
	
	public void setServiceManager(ServiceManager servicemanager) {
		this.servicemanager = servicemanager;
	}

	public ServiceManager getServiceManager() {
		return servicemanager;
	}

	
	
	
}
