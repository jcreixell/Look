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
package es.ucm.look.ar.ar3D.core.drawables;

import java.util.HashMap;
import java.util.Map;

import es.ucm.look.ar.ar3D.core.drawables.primitives.ObjMesh3D;
import es.ucm.look.ar.util.LookARUtil;

public class DrawablesDataBase {
	
	private Map<Integer, ObjMesh3D> drawables;
	
	private static DrawablesDataBase instance;
	
	private DrawablesDataBase( ){
		drawables = new HashMap<Integer, ObjMesh3D>();
	}
	
	public static DrawablesDataBase getInstance( ){
		if ( instance == null ){
			instance = new DrawablesDataBase( );
		}
		
		return instance;
	}
	
	public void putDrawable( Integer id, ObjMesh3D d ){
		drawables.put(id, d);
	}
	
	public ObjMesh3D getDrawable3D( Integer id ){
		if ( !drawables.containsKey(id)){
			drawables.put(id, new ObjMesh3D( LookARUtil.getApp(), id));
		}
		return drawables.get(id);
	}
	

}
