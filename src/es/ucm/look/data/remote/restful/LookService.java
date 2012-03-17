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
package es.ucm.look.data.remote.restful;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.local.contentprovider.LookContentProvider;
import es.ucm.look.data.remote.ConfigNet;
import es.ucm.look.data.remote.LookProperties;

/**
 * Service of Look!, implements an service Android with the logic to process data from Remote Service
 * 
 * @author Sergio
 *
 */
public class LookService extends Service {

	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks = new RemoteCallbackList<IRemoteServiceCallback>();

	/**
	 * The service is started
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/**
	 * The service is created
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**
	 * The service is destroy
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mCallbacks.kill();
	}

	/**
	 * Implements the methods of service, view {@link RemoteDataHandler}
	 */
	private final RemoteDataHandler.Stub binder = new RemoteDataHandler.Stub() {
		// implements setter methods, view RemoteDataHandler.aidl
		public void doLogin(String username, String password) {

			int id = -1;

			Message msg = new Message();
			Bundle data = new Bundle();

			String userQuery = "SELECT e FROM Properties e WHERE e.propertiesPK.property = 'user' AND e.value = '"
					+ username + "'";

			JSONObject response = RestMethod.doGet(ConfigNet.getInstance()
					.getURL("propertiess/?max=1&query="
							+ URLEncoder.encode(userQuery)));

			try {
				id = response.getJSONObject("properties")
						.getJSONObject("propertiesPK").getInt("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (id != -1) {
				String passQuery = "SELECT e FROM Properties e WHERE e.propertiesPK.property = 'password' AND e.propertiesPK.id = '"
						+ id + "'";

				response = RestMethod.doGet(ConfigNet.getInstance()
						.getURL("propertiess/?query="
								+ URLEncoder.encode(passQuery)));
				String passwordBack = "";
				try {
					passwordBack = response.getJSONObject("properties")
							.getString("value");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (!passwordBack.equals(password)) {
					id = -2;
				}

			}

			data.putString("response", String.valueOf(id));

			msg.setData(data);

			msg.what = LookProperties.ACTION_LOGIN;
			mHandler.sendMessage(msg);
		}

		@Override
		public void updateElementPosition(int id, float x, float y, float z)
				throws RemoteException {
			// This method don't need update our sql lite
			if (id != -1) {
				JSONObject obj = new JSONObject();
				try {
					obj.put("id", new Integer(id));
					obj.put("x", new Double(x));
					obj.put("y", new Double(y));
					obj.put("z", new Double(z));
					// date falta
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				HttpResponse response = RestMethod.doPut(
						ConfigNet.getInstance().getURL("mains/" + String.valueOf(id)), obj);

				if (response != null) {
					String responseString = RestMethod.decodeResponse(response);
					if (responseString.equalsIgnoreCase("")) {
						Log.i("LookServer", "Element update in server");
					} else {
						Log.i("LookServer", responseString);
					}
				}
			} else {
				Log.e("LookServer", "ERROR The element to update is -1");
			}
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void addElement(String type, float x, float y, float z,
				Map properties) throws RemoteException {

			Message msg = new Message();
			Bundle data = new Bundle();

			JSONObject obj = new JSONObject();
			try {
				obj.put("x", new Double(x));
				obj.put("y", new Double(y));
				obj.put("z", new Double(z));
				// date set DATE
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			HttpResponse response = RestMethod.doPost(
					ConfigNet.getInstance().getURL("mains/"), obj);

			if (response != null) {
				String responseString = RestMethod.decodeResponse(response);
				if (responseString.equalsIgnoreCase("")) {
					Log.i("LookServer", "Element added to server");
				} else {
					Log.i("LookServer", responseString);
				}

				int id = RestMethod.getLastId();

				if (id != -1) {
					// if the element has been inserted correctly add its
					// properties to server and insert all on the DB
//					LookContentProvider.getInstance().updateOrAddElement(id, x,
//							y, z);

					// format of json properties:
					// {"propertiesPK":{"id":"x","property":"xxx"},"value":"xxx"}
					JSONObject objProp = new JSONObject();
					try {
						JSONObject objPropPK = new JSONObject();
						objPropPK.put("id", new Integer(id));
						objPropPK.put("property", new String("type"));
						objProp.put("propertiesPK", objPropPK);
						objProp.put("value", new String(type));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					HttpResponse response2 = RestMethod.doPost(
							ConfigNet.getInstance().getURL("propertiess/"), objProp);
					String responseString2 = RestMethod
							.decodeResponse(response2);
					if (responseString2.equalsIgnoreCase("")) {
						Log.i("LookServer", "Property type added to server");
						LookContentProvider.getInstance().updateOrAddProperty(
								id, LookProperties.PROPERTY_TYPE, type);
					} else {
						Log.i("LookServer", responseString2);
					}

					// send a post for each property
					Iterator itr = properties.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry e = (Map.Entry) itr.next();

						String key = (String) e.getKey();
						String value = (String) e.getValue();

						objProp = new JSONObject();
						try {
							JSONObject objPropPK = new JSONObject();
							objPropPK.put("id", new Integer(id));
							objPropPK.put("property", new String(key));
							objProp.put("propertiesPK", objPropPK);
							objProp.put("value", new String(value));
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						response2 = RestMethod.doPost(
								ConfigNet.getInstance().getURL("propertiess/"), objProp);

						responseString2 = RestMethod.decodeResponse(response2);
						if (responseString2.equalsIgnoreCase("")) {
							Log.i("LookServer", "Property " + key
									+ " added to server");
							LookContentProvider.getInstance()
									.updateOrAddProperty(id, key, value);

						} else {
							Log.i("LookServer", responseString2);
						}
					}
					
					data.putString("response", String.valueOf(id));
					msg.setData(data);

					msg.what = LookProperties.ACTION_ADD_ELEMENT;
					mHandler.sendMessage(msg);
				}

			} else {
				Log.e("LookServer",
						"Error, To update element the response return null");
			}
		}

		@Override
		public void updateOrAddProperty(int id, String propertyName,
				String propertyValue) throws RemoteException {
			Message msg = new Message();
			Bundle data = new Bundle();

			JSONObject objProp = new JSONObject();
			try {
				JSONObject objPropPK = new JSONObject();
				objPropPK.put("id", new Integer(id));
				objPropPK.put("property", new String(propertyName));
				objProp.put("propertiesPK", objPropPK);
				objProp.put("value", new String(propertyValue));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			HttpResponse response = RestMethod.doPost(
					ConfigNet.getInstance().getURL("propertiess/"), objProp);

			if (response != null) {
				String responseString = RestMethod.decodeResponse(response);

				if (responseString.equalsIgnoreCase("")) {
					Log.i("LookServer", "Property " + propertyName
							+ " added to server");

					LookContentProvider.getInstance().updateOrAddProperty(id,
							propertyName, propertyValue);

				} else {

					HttpResponse response2 = RestMethod.doPut(
							ConfigNet.getInstance().getURL("propertiess/" + id + ","
									+ propertyName), objProp);

					if (response2 != null) {
						String responseString2 = RestMethod
								.decodeResponse(response2);

						if (responseString2.equalsIgnoreCase("")) {
							Log.i("LookServer", "Property " + propertyName
									+ " modify in server");

							LookContentProvider.getInstance()
									.updateOrAddProperty(id, propertyName,
											propertyValue);

							data.putString("response",
									String.valueOf(propertyName));

							msg.setData(data);

							msg.what = LookProperties.ACTION_MODIFY_PROPERTY;
							mHandler.sendMessage(msg);

						} else {
							Log.e("LookServer", "Error: " + responseString);
						}
					} else {

						Log.e("LookServer",
								"Error, Modifying property, the response return null");
					}
				}

			} else {
				Log.e("LookServer",
						"Error, Adding property, the response return null");
			}
		}

		@Override
		public List getElementsUpdated(float x, float y, float z, float radius, String date)
				throws RemoteException {

			List<EntityData> list = new ArrayList<EntityData>();
			Message msg = new Message();
			Bundle data = new Bundle();

			float x1 = x - (radius / 2);
			float x2 = x + (radius / 2);
			float y1 = y - (radius / 2);
			float y2 = y + (radius / 2);

			// FIXME meter lógica de la fecha
			String query = "SELECT e FROM Main e WHERE (e.x BETWEEN " + x1
					+ " AND " + x2 + ") AND (e.y BETWEEN " + y1 + " AND " + y2
					+ ") AND (e.z = " + z + ")";

			JSONObject response = RestMethod.doGet(ConfigNet.getInstance()
					.getURL("mains/?max=0&query=" + URLEncoder.encode(query)));

			JSONArray recs;

			try {
				recs = response.getJSONArray("main");

				for (int i = 0; i < recs.length(); i++) {
					EntityData entityData = new EntityData( );

					int resid = recs.getJSONObject(i).getInt("id");
					float resx = (float) recs.getJSONObject(i).getDouble("x");
					float resy = (float) recs.getJSONObject(i).getDouble("y");
					float resz = (float) recs.getJSONObject(i).getDouble("z");
					
					entityData.setId(resid);
					entityData.setLocation(resx, resy, resz);

					// Update all properties of this element
					String propertyQuery = "SELECT e FROM Properties e WHERE e.propertiesPK.id = '"
							+ resid + "'";

					JSONObject responseProp = RestMethod.doGet(ConfigNet.getInstance()
							.getURL("propertiess/?query="
									+ URLEncoder.encode(propertyQuery)));

					JSONArray recsProp;

					try {
						recsProp = responseProp.getJSONArray("properties");

						for (int j = 0; j < recsProp.length(); j++) {

							String propertyName = recsProp.getJSONObject(j)
									.getJSONObject("propertiesPK")
									.getString("property");

							String propertyValue = recsProp.getJSONObject(j)
									.getString("value");

							entityData.setPropertyValue(propertyName, propertyValue);
						}
					} catch (JSONException e) {
						// if no result
						Log.i("LookService", "Element without properties");
					}

					Log.i("LookService",
							"Update element and properties of number: " + resid);
					list.add(entityData);
				}

			} catch (JSONException e) {
				// if no result
				Log.i("LookService", "Nothing to update");
			}

			data.putString("response", String.valueOf(1));
			msg.setData(data);
			msg.what = LookProperties.UPDATE_DB;
			mHandler.sendMessage(msg);
			
			return list;

		}

		public void registerCallback(IRemoteServiceCallback cb) {
			if (cb != null)
				mCallbacks.register(cb);
		}

	};

	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			// Broadcast to all clients the new value.
			final int N = mCallbacks.beginBroadcast();
			for (int i = 0; i < N; i++) {
				try {
					switch (msg.what) {
					case LookProperties.ACTION_LOGIN:
						String response = msg.getData().getString("response");
						mCallbacks.getBroadcastItem(i).userLogIn(response);
						break;
					case LookProperties.ACTION_ADD_ELEMENT:
						String response1 = msg.getData().getString("response");
						mCallbacks.getBroadcastItem(i).sendResponse1(response1);
						break;
					case LookProperties.ACTION_MODIFY_PROPERTY:
						String response3 = msg.getData().getString("response");
						mCallbacks.getBroadcastItem(i).sendResponse3(response3);
						break;
					case LookProperties.UPDATE_DB:
						String response2 = msg.getData().getString("response");
						mCallbacks.getBroadcastItem(i).sendResponse2(response2);
						break;
					default:
						super.handleMessage(msg);
						return;

					}
				} catch (RemoteException e) {
				}
			}
			mCallbacks.finishBroadcast();
		}
	};

}
