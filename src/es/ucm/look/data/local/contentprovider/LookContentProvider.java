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
package es.ucm.look.data.local.contentprovider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import es.ucm.look.ar.util.LookARUtil;
import es.ucm.look.data.EntityData;
import es.ucm.look.data.interfaces.DataHandler;
import es.ucm.look.data.local.contentprovider.sql.LookSQLContentProvider;
import es.ucm.look.data.local.contentprovider.sql.LookSQLHelper;
import es.ucm.look.data.remote.LookProperties;

/**
 * Data handler for a local data base
 * 
 */
public class LookContentProvider implements DataHandler {

	private static LookContentProvider INSTANCE = null;

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private ContentResolver c;

	// Private constructor suppresses
	private LookContentProvider(Context context) {
		c = context.getContentResolver();
	}

	// Creador sincronizado para protegerse de posibles problemas multi-hilo
	// otra prueba para evitar instanciación múltiple
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LookContentProvider(LookARUtil.getApp());
		}
	}

	public static LookContentProvider getInstance() {
		if (INSTANCE == null)
			createInstance();
		return INSTANCE;
	}

	public static LookContentProvider getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new LookContentProvider(context);
		}
		return INSTANCE;
	}

	@Override
	public List<EntityData> getElementsUpdated(float x, float y, float z,
			float radius, Date date) {

		List<EntityData> listEntities = new ArrayList<EntityData>();

		String query = "";
		if (radius > 0) {
			float x1 = x - (radius / 2);
			float x2 = x + (radius / 2);
			float y1 = y - (radius / 2);
			float y2 = y + (radius / 2);
			float z1 = z - (radius / 2);
			float z2 = z + (radius / 2);

			query += "(" + LookProperties.FIELD_POS_X + " BETWEEN \"" + x1
					+ "\" AND \"" + x2 + "\") AND ("
					+ LookProperties.FIELD_POS_Y + " BETWEEN \"" + y1
					+ "\" AND \"" + y2 + "\") AND ("
					+ LookProperties.FIELD_POS_Z + " BETWEEN \"" + z1
					+ "\" AND \"" + z2 + "\") AND ";
		}

		if (date != null)
			query += "(" + LookProperties.FIELD_LAST_UPDATE + ">'"
					+ this.dateFormat.format(date) + "')";

		Cursor lookCursor = c.query(LookSQLContentProvider.MAIN_CONTENT_URI,
				LookSQLHelper.MAIN_PROJECTION_ALL_FIELDS, query, null, null);

		while (lookCursor != null && lookCursor.moveToNext()) {

			int id = lookCursor.getInt(lookCursor
					.getColumnIndex(LookProperties.FIELD_ID));

			float getX = lookCursor.getFloat(lookCursor
					.getColumnIndex(LookProperties.FIELD_POS_X));

			float getY = lookCursor.getFloat(lookCursor
					.getColumnIndex(LookProperties.FIELD_POS_Y));

			float getZ = lookCursor.getFloat(lookCursor
					.getColumnIndex(LookProperties.FIELD_POS_Z));

			String type = this.getType(id);
			listEntities.add(new EntityData(id, type, getX, getY, getZ));
		}

		return listEntities;
	}

	@Override
	public void addEntity(EntityData data) {
		ContentValues values = new ContentValues();
		Date date = new Date();

		values.put(LookProperties.FIELD_POS_X, data.getLocation().x);
		values.put(LookProperties.FIELD_POS_Y, data.getLocation().y);
		values.put(LookProperties.FIELD_POS_Z, data.getLocation().z);
		values.put(LookProperties.FIELD_LAST_UPDATE, dateFormat.format(date));

		Uri result = c.insert(LookSQLContentProvider.MAIN_CONTENT_URI, values);
		if (result == null) {
			Log.i("LookDB", "Entity data not added");
		} else {
			int id = getIDfromUri(result);
			data.setId(id);

		}

	}

	@Override
	public void updatePosition(EntityData data, float x, float y, float z) {
		ContentValues values = new ContentValues();
		Date date = new Date();

		values.put(LookProperties.FIELD_POS_X, data.getLocation().x);
		values.put(LookProperties.FIELD_POS_Y, data.getLocation().y);
		values.put(LookProperties.FIELD_POS_Z, data.getLocation().z);
		values.put(LookProperties.FIELD_LAST_UPDATE, dateFormat.format(date));

		c.update(LookSQLContentProvider.MAIN_CONTENT_URI, values,
				LookProperties.FIELD_ID + "=\"" + data.getId() + "\"", null);

	}

	@Override
	public void updateProperty(EntityData data, String propertyName,
			String propertyValue) {
		ContentValues cvalues = new ContentValues();
		cvalues.put(LookProperties.FIELD_ID, data.getId());
		cvalues.put(LookProperties.FIELD_PROPERTY, propertyName);
		cvalues.put(LookProperties.FIELD_VALUE, propertyValue);

		// first try to insert
		Uri uri = c.insert(LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				cvalues);

		if (uri == null) {
			c.update(LookSQLContentProvider.PROPERTIES_CONTENT_URI, cvalues,
					LookProperties.FIELD_ID + "=\"" + data.getId() + "\""
							+ " AND " + LookProperties.FIELD_PROPERTY + "=\""
							+ propertyName + "\"", null);
		}

	}

	/**
	 * Returns all the properties for a given id
	 * 
	 * @param id
	 *            the id
	 * @param propertiesName
	 *            the list of properties
	 * @return a map with the properties mapped to their values
	 */
	public Map<String, String> getPropertiesValue(int id,
			List<String> propertiesName) {

		Map<String, String> values = new HashMap<String, String>();

		// TODO comprobar esto
		Cursor lookCursor = c.query(
				LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS,
				LookProperties.FIELD_ID + "=\"" + id + "\"", null, null);

		while (lookCursor != null && lookCursor.moveToNext()) {

			String property = null;

			Iterator<String> it = propertiesName.iterator();

			while (it.hasNext()) {
				property = it.next();
				if (lookCursor.getString(
						lookCursor
								.getColumnIndex(LookProperties.FIELD_PROPERTY))
						.contentEquals(property)) {

					values.put(property, lookCursor.getString(lookCursor
							.getColumnIndex(LookProperties.FIELD_VALUE)));

				}
			}

		}
		return values;
	}

	public String getPropertyValue(int id, String propertyName) {

		Cursor lookCursor = c.query(
				LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS,
				LookProperties.FIELD_ID + "=\"" + id + "\"", null, null);

		while (lookCursor != null && lookCursor.moveToNext()) {

			if (lookCursor.getString(
					lookCursor.getColumnIndex(LookProperties.FIELD_PROPERTY))
					.contentEquals(propertyName)) {
				return lookCursor.getString(lookCursor
						.getColumnIndex(LookProperties.FIELD_VALUE));
			}
		}

		return null;
	}

	public Map<String, String> getAllProperties(int id) {
		Map<String, String> values = new HashMap<String, String>();

		Cursor lookCursor = c.query(
				LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS,
				LookProperties.FIELD_ID + "=\"" + id + "\"", null, null);

		while (lookCursor != null && lookCursor.moveToNext()) {

			values.put(lookCursor.getString(lookCursor
					.getColumnIndex(LookProperties.FIELD_PROPERTY)), lookCursor
					.getString(lookCursor
							.getColumnIndex(LookProperties.FIELD_VALUE)));

		}

		return values;
	}

	public int getId(String property, String value) {

		Cursor cursor = c.query(LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS,
				LookProperties.FIELD_PROPERTY + "=\"" + property + "\" AND "
						+ LookProperties.FIELD_VALUE + "=\"" + value + "\"",
				null, null);

		if (cursor != null && cursor.moveToFirst()) {

			return cursor
					.getInt(cursor.getColumnIndex(LookProperties.FIELD_ID));

		} else {

			return -1;
		}
	}

	// QUERIES BD
	/**
	 * Returns all the entity's id for the given type
	 * 
	 * @param type
	 *            the type
	 * @return the ids list
	 */
	public List<Integer> getAllIds(String type) {

		List<Integer> listIds = new ArrayList<Integer>();

		String queryString = LookProperties.FIELD_PROPERTY + "=\""
				+ LookProperties.PROPERTY_TYPE + "\" AND "
				+ LookProperties.FIELD_VALUE + "=\"" + type + "\"";

		Cursor cursor = c.query(LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS, queryString,
				null, null);

		while (cursor != null && cursor.moveToNext()) {
			listIds.add(cursor.getInt(cursor
					.getColumnIndex(LookProperties.FIELD_ID)));

		}
		return listIds;
	}

	public void updateOrAddProperty(int id, String propertyName,
			String propertyValue) {

		ContentValues cvalues = new ContentValues();
		cvalues.put(LookProperties.FIELD_ID, id);
		cvalues.put(LookProperties.FIELD_PROPERTY, propertyName);
		cvalues.put(LookProperties.FIELD_VALUE, propertyValue);

		// first try to insert
		Uri uri = c.insert(LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				cvalues);

		if (uri == null) {
			c.update(LookSQLContentProvider.PROPERTIES_CONTENT_URI, cvalues,
					LookProperties.FIELD_ID + "=\"" + id + "\"" + " AND "
							+ LookProperties.FIELD_PROPERTY + "=\""
							+ propertyName + "\"", null);
		}

	}

	// deleteElement(id)

	// AUXILIAR FUNCTIONS

	/**
	 * Return the type for an id, if it not exits return null
	 * 
	 * @param id
	 * @return A String with the type or null if it don't exits
	 */
	private String getType(int id) {

		String queryString = LookProperties.FIELD_ID + "=\"" + id + "\" AND "
				+ LookProperties.FIELD_PROPERTY + "=\""
				+ LookProperties.PROPERTY_TYPE + "\"";
		Cursor cursor = c.query(LookSQLContentProvider.PROPERTIES_CONTENT_URI,
				LookSQLHelper.PROPERTIES_PROJECTION_ALL_FIELDS, queryString,
				null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return cursor.getString(cursor
					.getColumnIndex(LookProperties.FIELD_VALUE));
		} else
			return "";

	}

	public int getIDfromUri(Uri uri) {
		String id = "-1";
		switch (LookSQLContentProvider.MAIN_URI_MATCHER.match(uri)) {
		case LookSQLContentProvider.CODE_SINGLE_ITEM:
			id = uri.getPathSegments().get(1);
			break;
		case UriMatcher.NO_MATCH:
			throw new IllegalArgumentException(
					LookSQLContentProvider.INVALID_URI_MESSAGE + uri);
		}

		return Integer.parseInt(id);
	}

}
