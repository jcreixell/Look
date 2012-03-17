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
 * To configure the data remote service. Need a URL for connect the server and
 * another one to download the binaries files.
 * 
 * @author Sergio
 * 
 */
public class ConfigNet {

	private boolean connected = false;

	private String serverURL;

	private String filesURL;

	private static ConfigNet instance;

	/**
	 * Constructor class
	 * 
	 * @param connected
	 * 		If the server will be connected
	 * @param serverURL
	 * 		URL from Server
	 * @param filesURL
	 * 		URL from Files, where its are downloads
	 */
	private ConfigNet(boolean connected, String serverURL, String filesURL) {
		this.connected = connected;
		this.serverURL = serverURL;
		this.filesURL = filesURL;
	}

	/**
	 * Sets the server configuration
	 * 
	 * @param serverUrl
	 *            server url
	 * @param filesURL
	 *            url where files will be downloaded
	 */
	public static void setNetConfiguration(String serverUrl, String filesURL) {
		instance = new ConfigNet(true, serverUrl, filesURL);
	}

	/**
	 * Get an unique instance, Singleton class.
	 * @return
	 */
	public static ConfigNet getInstance() {
		if (instance == null)
			instance = new ConfigNet(false, null, null);
		return instance;
	}

	/**
	 * Returns if it exists a server for the app
	 * 
	 * @return if it exists a server for the app
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * To change the server url.
	 * 
	 * @param URL
	 */
	public void setServerURL(String URL) {
		serverURL = URL;
	}

	/**
	 * To change the URL files directory.
	 * 
	 * @param URL
	 * 		New URL
	 */
	public void setFilesURL(String URL) {
		filesURL = URL;
	}

	/**
	 * Retrieve the URL of an element from Server
	 * 
	 * @param suburl
	 * 		Suburl to know the main URL 
	 * @return
	 * 		The complete URL
	 * 		
	 */
	public String getURL(String suburl) {
		return serverURL + suburl;
	}

	/**
	 * Retrieve the URL of an element from Files
	 * 
	 * @param suburl
	 * 		Suburl to know the main URL 
	 * @return
	 * 		The complete URL
	 * 		
	 */
	public String getFilesURL(String suburl) {
		return filesURL + suburl;
	}

}
