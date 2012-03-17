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
package es.ucm.test.look.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.R;
import es.ucm.look.data.local.contentprovider.LookContentProvider;
import es.ucm.look.data.remote.LookProperties;
import es.ucm.look.data.remote.restful.ServiceManager;
import es.ucm.test.look.data.util.ApplicationState;
import es.ucm.test.look.data.util.ClientConfig;

/**
 * 
 * 
 */
public class TestData extends Activity {

	public static final int RESULT_OK = 1;
	public static final int RESULT_WRONG = -1;
	
	private static final String TYPE_USER = "user";

	private ServiceManager servicemanager;
	private ApplicationState application;
	private ProgressDialog progressDialog;

	TextView editText;

	private void initialize() {

		// Start service
//		servicemanager = new ServiceManager<LookSocial>(this, LookSocial.class);
//
//		servicemanager.startService();
//		servicemanager.bindService();
//
//		application = (ApplicationState) this.getApplication();
//		application.setServiceManager(servicemanager);

		// Create the dir "look" in the sd
		//File dir = new File(ConfigNet.routeSD);
		//dir.mkdirs();

	}

	@Override
	public void onCreate(Bundle bundle) {
		
		
		super.onCreate(bundle);
		this.setContentView(R.layout.test);
		
		this.initialize();
		
		editText = (TextView) this.findViewById(R.id.editText1);

		Button btn_new_user = (Button) this.findViewById(R.id.button1);
		btn_new_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				addElement();

			}
		});

		Button btn_update_user = (Button) this.findViewById(R.id.button2);
		btn_update_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				updateElement();

			}
		});

		Button btn_addproperty = (Button) this.findViewById(R.id.button3);
		btn_addproperty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				addProperty();

			}
		});

		Button btn_do_login = (Button) this.findViewById(R.id.button4);
		btn_do_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				doLogin("sergio", "1234");

			}
		});

		Button btn_update_DB = (Button) this.findViewById(R.id.button5);
		btn_update_DB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				updateDB();

			}
		});

		Button btn_getId = (Button) this.findViewById(R.id.button6);
		btn_getId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getId();

			}

		});

		Button btn_getIds = (Button) this.findViewById(R.id.button7);
		btn_getIds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getIds();

			}

		});

		Button btn_getPropertiesValue = (Button) this
				.findViewById(R.id.button8);
		btn_getPropertiesValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getPropertiesValue();

			}

		});

		Button btn_getPropertyValue = (Button) this.findViewById(R.id.button9);
		btn_getPropertyValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getPropertyValue();
			}
		});

		Button btn_getAllProperties = (Button) this.findViewById(R.id.button10);
		btn_getAllProperties.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getAllProperties();
			}
		});

		Button btn_getAllIds = (Button) this.findViewById(R.id.button11);
		btn_getAllIds.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				getAllIds();
			}

		});

	}

	private void getAllIds() {
		List<Integer> listEntities = LookContentProvider.getInstance()
				.getAllIds(TYPE_USER);

		String numIds = "";

		for (int i = 0; i < listEntities.size(); i++) {
			numIds += (listEntities.get(i) + " ");
		}

		editText.setText(numIds);

		Toast.makeText(TestData.this, "Gets: " + numIds, Toast.LENGTH_SHORT)
				.show();
	}

	private void getAllProperties() {
		Map<String, String> value = LookContentProvider.getInstance()
				.getAllProperties(55);

		String numIds = "";
		
		for ( String key: value.keySet() ){
			numIds += ("clave: " + key + " valor:" + value.get(key) + "  ");
		}

		editText.setText(numIds);

		Toast.makeText(TestData.this, "Gets: " + numIds, Toast.LENGTH_SHORT)
				.show();

	}

	private void getPropertyValue() {
		String value = LookContentProvider.getInstance().getPropertyValue(55,
				"name");

		editText.setText(value);

		Toast.makeText(TestData.this, "Get: " + value, Toast.LENGTH_SHORT)
				.show();
	}

	private void getPropertiesValue() {

		List<String> values = new ArrayList<String>();
		values.add("user");
		values.add("password");
		values.add("name");

		Map<String, String> value = LookContentProvider.getInstance()
				.getPropertiesValue(55, values);

		String numIds = "";

		for ( String key: value.keySet() ){
			numIds += ("clave: " + key + " valor:" + value.get(key) + "  ");
		}

		editText.setText(numIds);

		Toast.makeText(TestData.this, "Gets: " + numIds, Toast.LENGTH_SHORT)
				.show();

	}

	private void getId() {
		int id = LookContentProvider.getInstance().getId(
				"user", "sergio");
		editText.setText(String.valueOf(id));

		Toast.makeText(TestData.this, "Get: " + id, Toast.LENGTH_SHORT).show();
	}

	private void getIds() {
		List<EntityData> listEntities = LookContentProvider.getInstance()
				.getElementsUpdated(5, 5, 0, 20, new Date());

		String numIds = "";

		for (int i = 0; i < listEntities.size(); i++) {
			EntityData worldEntity = listEntities.get(i);

			numIds += (worldEntity.getId() + " ");
		}

		editText.setText(numIds);

		Toast.makeText(TestData.this, "Gets: " + numIds, Toast.LENGTH_SHORT)
				.show();
	}

	private void updateDB() {
//		try {
//			//servicemanager.restfulService.updateDB(0, 0, 0, 10);
//
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	private void updateElement() {
//		try {
//			servicemanager.restfulService.updateElementPosition(ClientConfig
//					.getInstance().getId(), 2, 3, 4);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	private void addProperty() {
//		try {
//			servicemanager.restfulService.updateOrAddProperty(53, "info",
//					"hola hola que tal estas");
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	private void addElement() {

//		// the properties are created
//		Map<String, String> properties = new HashMap<String, String>();
//		properties.put("password", "1234");
//		properties.put("user", "sergio");
//		properties.put("name", "mi nombre");
//
//		progressDialog = new ProgressDialog(TestData.this);
//		progressDialog.setMessage("Creating user...");
//		progressDialog.show();
//
//		servicemanager.setHandler(mHandler);
//
//		try {
//			servicemanager.restfulService.addElement(TYPE_USER, 0, 0, 0,
//					properties);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	private void doLogin(String user, String pass) {

//		progressDialog = new ProgressDialog(TestData.this);
//		progressDialog.setMessage("Logging you in...");
//		progressDialog.show();
//
//		servicemanager.setHandler(mHandler);
//
//		try {
//			servicemanager.restfulService.doLogin(user, pass);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case LookProperties.ACTION_ADD_ELEMENT:

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				String idString = (String) msg.obj.toString();
				int id = Integer.parseInt(idString);

				editText.setText(idString);
				// ID of new user

				if (id != -1) {
					// save our user
					ClientConfig.getInstance().setId(id);

					Toast.makeText(TestData.this, "The user has been created",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(TestData.this, "The user already exits",
							Toast.LENGTH_SHORT).show();
				}
				break;

			case LookProperties.ACTION_LOGIN:

				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				String idString2 = (String) msg.obj.toString();
				int id2 = Integer.parseInt(idString2);

				editText.setText(idString2);
				// ID of new user

				if (id2 == -1) {
					Toast.makeText(TestData.this, "Error, incorrect user",
							Toast.LENGTH_SHORT).show();
				} else if (id2 == -2) {

					Toast.makeText(TestData.this, "Error, incorrect password",
							Toast.LENGTH_SHORT).show();
				} else {
					// save our user
					ClientConfig.getInstance().setId(id2);

					Toast.makeText(TestData.this, "Logged correctly",
							Toast.LENGTH_SHORT).show();

				}

				break;
			default:
			}
		}
	};

}
