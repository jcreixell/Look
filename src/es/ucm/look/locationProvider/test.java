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
package es.ucm.look.locationProvider;

import android.app.Activity;
import android.os.Bundle;

public class test extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LocationProvider locationProvider = new LocationProvider(this);
        locationProvider.run();  // ESTO ACTUALIZA LA INFORMACION
        System.out.println("test: " + LocationProvider.getPosition()[0] + " " + LocationProvider.getPosition()[1]);
        System.out.println("test: " + LocationProvider.getDisplacement()[0] + " " + LocationProvider.getDisplacement()[1]);
        System.out.println("test: " + LocationProvider.getMapPosition()[0] + " " + LocationProvider.getMapPosition()[1]);
        System.out.println("test: " + LocationProvider.isMoving());


        
    }
}
