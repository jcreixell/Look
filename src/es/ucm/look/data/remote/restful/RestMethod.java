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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import es.ucm.look.data.remote.ConfigNet;

/**
 * Class to connect directly with the Server
 * 
 * @author Sergio
 * 
 */
public class RestMethod {

	/**
	 * Used to insert an element
	 * 
	 * @param url
	 * 		Element URI
	 * @param c
	 * 		The element represented with a JSON
	 * @return
	 * 		The response
	 */
	public static HttpResponse doPost(String url, JSONObject c) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);
		StringEntity s = null;
		try {
			s = new StringEntity(c.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");

		request.setEntity(s);
		request.addHeader("accept", "application/json");

		try {
			return httpclient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * To Update an element
	 * 
	 * @param url
	 * 		Element URI
	 * @param c
	 * 		The element to update represented with a JSON
	 * @return
	 * 		The response
	 */
	public static HttpResponse doPut(String url, JSONObject c) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut request = new HttpPut(url);
		StringEntity s = null;
		try {
			s = new StringEntity(c.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");

		request.setEntity(s);
		request.addHeader("accept", "application/json");

		try {
			return httpclient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Delete a resource in the server
	 * 
	 * @param url
	 * 		Element URI
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void doDelete(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete(url);
		delete.addHeader("accept", "application/json");
		httpclient.execute(delete);
	}

	/**
	 * Retrieve a resource from the server
	 * 
	 * @param url
	 * 		Element URI to get
	 * @return
	 * 		The Element as JSON
	 */
	public static JSONObject doGet(String url) {
		JSONObject json = null;

		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Accept JSON
		httpget.addHeader("accept", "application/json");

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);

			// Get the response entity
			HttpEntity entity = response.getEntity();

			// If response entity is not null
			if (entity != null) {

				// get entity contents and convert it to string
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				// construct a JSON object with result
				json = new JSONObject(result);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return the json
		return json;
	}

	/**
	 * Convert Inputstream to string
	 * 
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * To know the 'number' of last id inserted in the Server
	 * 
	 * @return
	 */
	public static int getLastId() {

		int result = -1;

		String query = URLEncoder
				.encode("SELECT e FROM Main e ORDER BY e.id DESC");

		String params = "?max=1&query=" + query;

		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(ConfigNet.getInstance().getURL(
				"mains/" + params));

		// Accept Text/plain
		httpget.addHeader("accept", "application/json");

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);

			// Get the response entity
			HttpEntity entity = response.getEntity();

			// If response entity is not null
			if (entity != null) {

				// get entity contents and convert it to string
				InputStream instream = entity.getContent();
				String resultResponse = convertStreamToString(instream);

				// construct a JSON object with result
				try {
					// extract field id of the json
					JSONObject json = new JSONObject(resultResponse);
					JSONObject mainEntity = json.getJSONObject("main");
					result = mainEntity.getInt("id");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return the result
		return result;
	}

	/**
	 * Translate the response server to an String
	 * 
	 * @param response
	 * 		Response of the server
	 * @return
	 * 		A string with the response
	 */
	public static String decodeResponse(HttpResponse response) {

		// Get the response entity
		HttpEntity entity = response.getEntity();

		String result = "";

		// If response entity is not null
		if (entity != null) {

			// get entity contents and convert it to string
			InputStream instream = null;
			try {
				instream = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result = convertStreamToString(instream);

			// Closing the input stream will trigger connection release
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
