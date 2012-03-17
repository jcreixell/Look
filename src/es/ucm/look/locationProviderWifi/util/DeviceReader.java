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
package es.ucm.look.locationProviderWifi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * File read utilities.
 * 
 * @author Jorge Creixell Rojo
 * Based on Indoor Navigation System for Handheld Devices
 * by Manh Hung V. Le, Dimitris Saragas, Nathan Webb
 * 
 */
public class DeviceReader {
	private String path;
	private String file;
	private String line;
	private FileInputStream fis;
	private InputStreamReader isr;
	private BufferedReader in;

	/**
	 * Constructor with no specified file. The default file location is
	 * /sdcard/samples.txt
	 */
	public DeviceReader() {
		path = new String("/sdcard/");
		file = new String("samples.txt");
		initialize();
	}

	/**
	 * Constructor with an input specified location file
	 * 
	 * @param s
	 *            The string contains the file path to read from
	 */
	public DeviceReader(String s) {
		if (s.contains("/")) {
			this.path = s.substring(0, s.lastIndexOf("/") + 1);
			this.file = s.substring(s.lastIndexOf("/") + 1);
		} else {
			this.path = "/sdcard/";
			this.file = s;
		}
		initialize();
	}

	/**
	 * Write a string to the specified file
	 */
	public String readln() {
		try {
			this.line = in.readLine();
		} catch (IOException e) {
		}
		return this.line;
	}

	/**
	 * Close the file
	 */
	public void close() {
		try {

			this.in.close();
			this.isr.close();
			this.fis.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Initialize the output stream in order to write to the specified file.
	 */
	private void initialize() {
		File f = new File(new String(this.path + this.file));
		try {
			this.fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
		}
		this.isr = new InputStreamReader(this.fis);
		this.in = new BufferedReader(isr);
	}

	public String toString() {
		return new String(this.path + this.file);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFileName(String fileName) {
		this.file = fileName;
	}

	public String getPath() {
		return this.path;
	}

	public String getFileName() {
		return this.file;
	}
}
