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

public class ClientConfig {

	private static ClientConfig INSTANCE = null;

	// Private constructor suppresses
	private ClientConfig() {
	}

	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ClientConfig();
		}
	}

	public static ClientConfig getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}
	
	
	//VARIABLES
	private int id = -1;//TODO default value, to change

	public int getId() {
		return id;
	}
	
	public void setId(int id2) {
		id = id2;
	}


}
