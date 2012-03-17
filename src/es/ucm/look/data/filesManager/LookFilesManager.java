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
package es.ucm.look.data.filesManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import es.ucm.look.data.remote.ConfigNet;

/**
 * A file manager for accessing binary files remotely
 * 
 * @author Sergio
 *
 */
public class LookFilesManager {

	private static LookFilesManager instance;

	/**
	 * Base directory
	 */
	private File directory;

	private LookFilesManager() {
		directory = Environment.getExternalStorageDirectory();
	}

	/**
	 * Method singleton to access a unique instance of LookFilesManager
	 * @return
	 */
	public static LookFilesManager getFileManager() {
		if (instance == null)
			instance = new LookFilesManager();
		return instance;
	}

	/**
	 * Set the basis directory for the file manager
	 * 
	 * @param directory
	 *            the basis directory
	 */
	public void setDirectory(String directory) {
		File f = new File(directory);
		f.mkdirs();
		this.directory = f;
	}

	/**
	 * Returns an input stream for a file
	 * 
	 * @param uriFile
	 *            uri file
	 * @return the input stream
	 */
	public InputStream getInputStream(String uriFile) {
		File f = new File(directory, uriFile);
		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the bitmap for the given file
	 * 
	 * @param uriFile the uri file
	 * @return the bitmap
	 */
	public Bitmap getImageBitmap(String uriFile) {
		Bitmap bm = null;

		File isFile = new File(directory, uriFile);
		if (isFile.exists()) {

			bm = BitmapFactory.decodeFile(isFile.getAbsolutePath());
		} else if ( ConfigNet.getInstance().isConnected() ){

			try {
				URL aURL = new URL(ConfigNet.getInstance().getFilesURL(uriFile));
				URLConnection conn = aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				bm = BitmapFactory.decodeStream(bis);

				// Save the image in the sd
				FileOutputStream out = new FileOutputStream(new File(directory, uriFile ));

				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				is.close();

				bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();

				bis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bm;
	}
}
