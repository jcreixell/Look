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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Date Utils.
 * 
 * @author Jorge Creixell Rojo
 * 
 */
public class DateUtils {
  public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Returns current time 
	 * 
	 * @return current time in yyyy-MM-dd HH:mm:ss format
	 */
  public static String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    return sdf.format(cal.getTime());

  }

  public static void  main(String arg[]) {
    System.out.println("Now : " + DateUtils.now());
  }
}
