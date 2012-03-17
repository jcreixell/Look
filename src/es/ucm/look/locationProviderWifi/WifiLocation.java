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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import es.ucm.look.locationProviderWifi.util.DateUtils;
import es.ucm.look.locationProviderWifi.util.DeviceReader;
import es.ucm.look.locationProviderWifi.util.DeviceWriter;
import es.ucm.look.locationProviderWifi.wifi.Lugar;
import es.ucm.look.locationProviderWifi.wifi.Lugares;
import es.ucm.look.locationProviderWifi.wifi.NodoWifi;

/**
 * Location Service implemented as a thread.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class WifiLocation {
	/**
	 * Singleton instance of the object.
	 */
	private static WifiLocation INSTANCE = new WifiLocation();

	/**
	 * Service name.
	 */
	private static final String TAG = "LocationService";

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
	 * Number of scans for each measurement .
	 */
	public static final int MAX_COUNT = 5;

	/**
	 * Whether to enable logging or not.
	 */
	public static final boolean LOG = false;

	/**
	 * Log buffer..
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
	 * Seconds between scans..
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
	 * APs file data.
	 */
	private Set<String> aps;

	/**
	 * current location information.
	 */
	private Lugar lugarActual;

	/**
	 * Application context.
	 */
	private Context context;

	private WifiLocation() {

	}

	/**
	 * Returns the service instance
	 * 
	 * @return Wifi Location object instance
	 */
	public static WifiLocation getInstance() {
		return INSTANCE;
	}

	/**
	 * Initialize and starts location service.
	 * 
	 * @param context
	 *            Application context.
	 **/
	public void start(Context context) {

		this.context = context;

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

		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(true);
		mWifiManager.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY,
				"Scan Only");
		mWifiReceiver = new WifiReceiver();

		context.registerReceiver(mWifiReceiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

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

	/**
	 * Stops the location service.
	 **/
	public void stop() {
		if (scan_activo) {
			scan_activo = false;
			timer.cancel();
		}

		Log.i(TAG, "Servicio detenido");
		context.unregisterReceiver(mWifiReceiver);
		out.close();
		context = null;
	}

	/**
	 * Returns the current location
	 * 
	 * @return Current location information
	 */
	public Lugar getPosicion() {
		return lugarActual;
	}

	/**
	 * Loads the data from the fingerprints file
	 */
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

	/**
	 * Loads the data from the Access Points file
	 */
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

	}

	/**
	 * Evaluates the current position using the tmpData and the CN algorithm
	 */
	private synchronized void evaluaPosicion() throws NullPointerException {

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

	/**
	 * Writes to log file
	 */
	private void log(String s) {
		if (LOG) {
			try {
				log_data.append(s);
			} catch (OutOfMemoryError e) {
				log_data.delete(0, log_data.length());
			}
		}
	}

	private class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			if (scan_activo) {

				if (count == 0) {
					Log.i(TAG, "Evaluando posicion");

				}
				WifiManager mWifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				List<ScanResult> scanResult = mWifiManager.getScanResults();

				if (count == 0) {
					try {
						evaluaPosicion();
					} catch (NullPointerException e) {

						Log.i(TAG, "Reinicio evaluapoisicion");
					}

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
