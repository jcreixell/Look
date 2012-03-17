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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import es.ucm.look.locationProviderWifi.util.DateUtils;
import es.ucm.look.locationProviderWifi.util.DeviceReader;
import es.ucm.look.locationProviderWifi.util.DeviceWriter;
import es.ucm.look.locationProviderWifi.wifi.Lugar;
import es.ucm.look.locationProviderWifi.wifi.Lugares;
import es.ucm.look.locationProviderWifi.wifi.NodoWifi;

/**
 * Location Service implemented as Android Service.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class WifiService extends Service {

	/**
	 * Unique identification number for the notification.
	 */
	public int NOTIFICATION = 777;
	
	/**
	 * Service name.
	 */
	private static final String TAG = "LocationService";
	
	/**
	 * Service Binder.
	 */
	private final IBinder mBinder = new ServicioBinder();

	/**
	 * Fingerprints file.
	 */
	public static final String ENTRENAMIENTO = "/sdcard/Entrenamiento.txt";
	
	/**
	 * Access points file.
	 */
	public static final String APS = "/sdcard/APs.txt";
	
	/**
	 * Log file.
	 */
	public static final String LOG_FILE = "/sdcard/log.txt";
	
	/**
	 * Nunmber of scans for each measurement.
	 */
	public static final int MAX_COUNT = 5;
	
	/**
	 * Whether to enable logging or not.
	 */
	public static final boolean LOG = true;

	/**
	 * Log buffer.
	 */
	private StringBuffer log_data;
	
	/**
	 * Device writer object.
	 */
	private DeviceWriter out;

	/**
	 * Whether scanning is active or not.
	 */
	private boolean scan_activo = false;

	/**
	 * Timer to control time intervals between scans.
	 */
	private Timer timer = null;
	
	/**
	 * Number of scans already performed in the current measurement.
	 */
	private int count = 1;
	
	/**
	 * Data of the scans of the current measurement.
	 */
	private HashMap<String, ArrayList<Integer>> tmpData;

	/**
	 * Nunmber of seconds between scans.
	 */
	private static final int REFRESH_RATE = 1; // segundos

	/**
	 * Android Wifi Manager.
	 */
	private WifiManager mWifiManager;
	
	/**
	 * Android Wifi Receiver.
	 */
	private WifiReceiver mWifiReceiver;

	/**
	 * Fingerprints file data.
	 */
	private HashMap<String, ArrayList<NodoWifi>> datosEntrenamiento;
	
	/**
	 * Access porints file data.
	 */
	private Set<String> aps;

	/**
	 * Current location information.
	 */
	private Lugar lugarActual;

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	/**
	 * Service binder class.
	 */
	public class ServicioBinder extends Binder {
		public WifiService getService() {
			return WifiService.this;
		}
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "Servicio iniciado");

		out = new DeviceWriter(LOG_FILE);

		datosEntrenamiento = new HashMap<String, ArrayList<NodoWifi>>();
		aps = Collections.synchronizedSet(new HashSet<String>());
		tmpData = new HashMap<String, ArrayList<Integer>>();

		count = 1;
		lugarActual = new Lugar();

		log_data = new StringBuffer();

		leeDatosEntrenamiento();
		leeAPs();

		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);
		mWifiManager.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY,
				"Scan Only");
		mWifiReceiver = new WifiReceiver();

		registerReceiver(mWifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Received start id " + startId + ": " + intent);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "Servicio detenido");
		this.unregisterReceiver(mWifiReceiver);
		out.close();

	}

	public void start() {

		if (!scan_activo) {

			scan_activo = true;
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					mWifiManager.startScan();
				}

			}, 0, REFRESH_RATE * 1000);
		}
	}

	public void stop() {
		if (scan_activo) {
			scan_activo = false;
			timer.cancel();
		}
	}
	
	public Lugar getPosicion() {
		return lugarActual;
	}

	private void leeDatosEntrenamiento() {
		DeviceReader in = new DeviceReader(ENTRENAMIENTO);

		String ap = null;
		String bssid, clave;
		int id;
		int level;

		do {
			ap = in.readln();
			if (ap != null) {

				String[] data = ap.split(" ");
				bssid = data[0];
				level = Integer.parseInt(data[1]);
				id = Integer.parseInt(data[2]);

				NodoWifi nodo = new NodoWifi(bssid, level, id);

				clave = Integer.toString(id);

				if (datosEntrenamiento.containsKey(clave)) {
					datosEntrenamiento.get(clave).add(nodo);
				} else {
					ArrayList<NodoWifi> l = new ArrayList<NodoWifi>();
					l.add(nodo);
					datosEntrenamiento.put(clave, l);
				}
			}

		} while (ap != null);
		in.close();
	}

	private void leeAPs() {

		DeviceReader in = new DeviceReader(APS);
		String bssid = null;

		do {
			bssid = in.readln();
			if (bssid != null) {

				aps.add(bssid);
			}

		} while (bssid != null);
		in.close();

		/*Iterator<String> it = aps.iterator();
		while (it.hasNext()) {
			String key = it.next();
		}*/

	}

	private synchronized void evaluaPosicion() {

		String maxPuntKey = null;
		double minDistEuclidea = Double.MAX_VALUE;

		Iterator<String> itAps = aps.iterator();

		// annadimos los nodos no detectados con sennal minima para tener igual
		// num de aps
		while (itAps.hasNext()) {
			String key = itAps.next();
			if (!tmpData.containsKey(key)) {
				ArrayList<Integer> lista = new ArrayList<Integer>();
				lista.add(-100);
				tmpData.put(key, lista);
			}
		}

		double acum = 0;

		Set<String> keys = datosEntrenamiento.keySet();
		Iterator<String> it = keys.iterator();

		while (it.hasNext()) {
			String key = it.next();
			acum = 0;

			ArrayList<NodoWifi> listaNodos = datosEntrenamiento.get(key);

			for (int i = 0; i < listaNodos.size(); i++) {

				NodoWifi nodo = listaNodos.get(i);

				Set<String> tmpKeys = tmpData.keySet();
				Iterator<String> it2 = tmpKeys.iterator();

				while (it2.hasNext()) {
					String bssid = it2.next();

					if (bssid.equals(nodo.getBssid()) && aps.contains(bssid)) {
						acum += Math
								.pow((NodoWifi.averageLevel(tmpData.get(bssid)) - nodo
										.getLevel()), 2);

						log("\t " + bssid + " Ent: " + nodo.getLevel()
								+ " Scan: "
								+ NodoWifi.averageLevel(tmpData.get(bssid))
								+ "\n");

					}
				}

			}

			double distanciaEuclidea = Math.sqrt(acum);

			if (distanciaEuclidea < minDistEuclidea) {
				minDistEuclidea = distanciaEuclidea;
				maxPuntKey = key;
			}
			log("Time: " + DateUtils.now() + " Nodo: " + key + " punt: "
					+ distanciaEuclidea + "\n");

		}
		log("MaxPunt: " + minDistEuclidea + "\n");

		if (maxPuntKey != null) {

			final int idFinal = datosEntrenamiento.get(maxPuntKey).get(0)
					.getId();

			Lugar lugar = Lugares.getInstance().getLugar(idFinal);

			if (lugar != null) {
				lugarActual = lugar;
				log("Nodo seleccionado: " + idFinal + "\n");
				log("\n");
			}
		}
	}

	private void log(String s) {
		log_data.append(s);
		//Log.i(TAG, "log: " + s);

	}

	private class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			if (scan_activo) {

				if (count == 0) {
					Log.i(TAG, "Evaluando posicion");

				}
				WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				List<ScanResult> scanResult = mWifiManager.getScanResults();

				if (count == 0) {

					evaluaPosicion();

					tmpData = new HashMap<String, ArrayList<Integer>>();

					for (int i = 0; i < scanResult.size(); i++) {

						ArrayList<Integer> signals = new ArrayList<Integer>();
						signals.add(scanResult.get(i).level);
						tmpData.put(scanResult.get(i).BSSID, signals);
					}

				} else {
					for (int i = 0; i < scanResult.size(); i++) {

						if (tmpData.containsKey(scanResult.get(i).BSSID)) {
							tmpData.get(scanResult.get(i).BSSID).add(
									scanResult.get(i).level);
						} else {
							ArrayList<Integer> signals = new ArrayList<Integer>();
							signals.add(scanResult.get(i).level);
							tmpData.put(scanResult.get(i).BSSID, signals);
						}

					}
				}

				count = (count + 1) % (MAX_COUNT + 1);

			}
		}
	}

}
