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

/**
 * Here are defined the Constants for the Callbacks and the commons fields of databases
 * 
 * @author Sergio
 * 
 */
public class LookProperties {

	//--------------- actions to recognize the callback-----------
	
	/**
	 * Action name for when we get logged
	 */
	public static final int ACTION_LOGIN = 0;

	/**
	 * Action name for when an element is added
	 */
	public static final int ACTION_ADD_ELEMENT = 1;
	
	/**
	 * Action name for when the local entities are updated
	 */
	public static final int UPDATE_DB = 2;
	
	/**
	 * Action name for when a property is modified
	 */
	public static final int ACTION_MODIFY_PROPERTY = 3;
	
	
	//--------------- fields commons in the database---------------
	
	// COMMON FIELDS
	public static final String FIELD_ID = "id";
	
	// MAIN TABLE
	public static final String FIELD_POS_X = "pos_x";
	public static final String FIELD_POS_Y = "pos_y";
	public static final String FIELD_POS_Z = "pos_z";
	public static final String FIELD_LAST_UPDATE = "last_update";
	
	// PROPERTY TABLE
	public static final String FIELD_PROPERTY = "property";
	public static final String FIELD_VALUE = "value";

	//COMMON PROPERTIES
	public static final String PROPERTY_TYPE = "type";





}
