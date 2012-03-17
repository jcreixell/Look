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
package es.ucm.look.locationProviderWifi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Example Client for the Service WifiService.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class Cliente extends Activity {

	/**
	 * Service to bind.
	 */
	private WifiService mBoundService;
	
	/**
	 * If the service is bound.
	 */
	private boolean mIsBound = false;
	
	/**
	 * Timer to retrieve location information in regular time intervals.
	 */
	private Timer timer;
	
	/**
	 * The connection to the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((WifiService.ServicioBinder) service).getService();
			mBoundService.start();

			timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				public void run() {
					
					//Obtener aqui la posicion
					System.out.println("Cliente: " + mBoundService.getPosicion().toString());
				}
			};
			timer.scheduleAtFixedRate(timerTask, 0, 1000);

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBoundService = null;

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doBindService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		doUnbindService();
	}

	/**
	 * Bind the service
	 */
	void doBindService() {
		bindService(new Intent(Cliente.this, WifiService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	/**
	 * UnBind the service.
	 */
	void doUnbindService() {
		if (mIsBound) {
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}

}
