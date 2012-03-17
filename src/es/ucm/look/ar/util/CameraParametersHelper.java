/**
	Copyright (C) 2009,2010  Tobias Domhan

    This file is part of AndOpenGLCam.

    AndObjViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndObjViewer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndObjViewer.  If not, see <http://www.gnu.org/licenses/>.
 
 */
package es.ucm.look.ar.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * Only the newer versions of the SDK support querying the available preview
 * sizes. This class and it descendants encapsulate this functionality, so that
 * no verify exception is thrown on older(1.6) mobile phones.
 * 
 * @author Tobi
 * 
 */
public class CameraParametersHelper {
	private static Method getSupportedPreviewFormats = null;
	private static Method getSupportedPreviewSizes = null;
	
	static {
		initCompatibility();
	};

	private static void initCompatibility() {
		// separate exception handling is needed, in case on method is
		// supported, the other not
		try {
			getSupportedPreviewSizes = Parameters.class.getMethod("getSupportedPreviewSizes", (Class[]) null);
			/* success, this is a newer device */
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device */
		}
		try {
			getSupportedPreviewFormats = Parameters.class.getMethod("getSupportedPreviewFormats", (Class[]) null);
			/* success, this is a newer device */
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device */
		}
	}

	@SuppressWarnings("unchecked")
	public static void setCameraParameters(Camera camera, int width, int height) {
		// reduce preview frame size for performance reasons
		if (getSupportedPreviewSizes != null) {
			Parameters params = camera.getParameters();
			// since SDK 5 web can query the available preview sizes
			// let's choose the smallest available preview size
			List<Size> sizes;
			try {
				Object supportedFormats = getSupportedPreviewSizes.invoke(params, (Object[]) null);
				if (supportedFormats instanceof List<?>) {
					sizes = (List<Camera.Size>) supportedFormats;// params.getSupportedPreviewSizes();
					Size optimalSize = getOptimalPreviewSize(sizes, width, height);
					Size currentSize = params.getPreviewSize();
					if (!(optimalSize.height == currentSize.height && optimalSize.width == currentSize.width)) {
						// the optimal size was not set, yet. so let's do so now
						Log.d("AndAR", "'query preview sizes' available, setting size to: " + width + " x " + height);
						params.setPreviewSize(optimalSize.width, optimalSize.height);
						try {
							camera.setParameters(params);
						} catch (RuntimeException ex) {
							ex.printStackTrace();
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			// we don't have any information about available previewsizes...
			Parameters params = camera.getParameters();
			Size currentSize = params.getPreviewSize();
			if (!(160 == currentSize.height && 240 == currentSize.width)) {
				// try to set the preview size to this fixed value
				params.setPreviewSize(240, 160);
				try {
					camera.setParameters(params);
				} catch (RuntimeException ex) {
					ex.printStackTrace();
				}
			}
		}

		// now set the pixel format of the preview frames:
		if (getSupportedPreviewFormats != null) {
			Parameters params = camera.getParameters();
			// we may query the available pixelformats in newer SDk versions
			List<Integer> supportedFormats;
			try {
				supportedFormats = (List<Integer>) getSupportedPreviewFormats.invoke(params, (Object[]) null);
				if (supportedFormats != null) {
					int format = getBestSupportedFormat(supportedFormats);
					if (format != -1) {
						params.setPreviewFormat(format);
						try {
							camera.setParameters(params);
						} catch (RuntimeException ex) {
							ex.printStackTrace();
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		} else {
			Parameters params = camera.getParameters();
			if (params.getPreviewFormat() != PixelFormat.YCbCr_420_SP) {
				// try to set the preview format, if it was not YCbCr_420
				// already
				params.setPreviewFormat(PixelFormat.YCbCr_420_SP);
				try {
					camera.setParameters(params);
				} catch (RuntimeException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns the best pixel format of the list or -1 if none suites.
	 * 
	 * @param listOfFormats
	 * @return
	 */
	public static int getBestSupportedFormat(List<Integer> listOfFormats) {
		int format = -1;
		for (Iterator<Integer> iterator = listOfFormats.iterator(); iterator.hasNext();) {
			Integer integer = iterator.next();
			if (integer.intValue() == PixelFormat.YCbCr_420_SP) {
				// alright the optimal format is supported..let's return
				format = PixelFormat.YCbCr_420_SP;
				return format;
			} else if (integer.intValue() == PixelFormat.YCbCr_422_SP) {
				format = PixelFormat.YCbCr_422_SP;
				// this format is not optimal. do not return, a better format
				// might be in the list.
			}
		}
		return format;
	}
	
private final static double epsilon = 0.17;
	
	/**
	 * Get the optimal preview size for the given screen size.
	 * @param sizes
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static Size getOptimalPreviewSize(List<Size> sizes, int screenWidth, int screenHeight) {
		double aspectRatio = ((double)screenWidth)/screenHeight;
		Size optimalSize = null;
		for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
			Size currSize =  iterator.next();
			double curAspectRatio = ((double)currSize.width)/currSize.height;
			//do the aspect ratios equal?
			if ( Math.abs( aspectRatio - curAspectRatio ) < epsilon ) {
				//they do
				if(optimalSize!=null) {
					//is the current size smaller than the one before
					if(optimalSize.height>currSize.height && optimalSize.width>currSize.width) {
						optimalSize = currSize;
					}
				} else {
					optimalSize = currSize;
				}
			}
		}
		if(optimalSize == null) {
			//did not find a size with the correct aspect ratio.. let's choose the smallest instead
			for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
				Size currSize =  iterator.next();
				if(optimalSize!=null) {
					//is the current size smaller than the one before
					if(optimalSize.height>currSize.height && optimalSize.width>currSize.width) {
						optimalSize = currSize;
					} else {
						optimalSize = currSize;
					}
				}else {
					optimalSize = currSize;
				}
				
			}
		}
		return optimalSize;
	}
	
	public static boolean containsSize(List<Size> sizes, Size size) {
		for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
			Size currSize =  iterator.next();
			if(currSize.width == size.width && currSize.height == size.height) {
				return true;
			}			
		}
		return false;
	}
	
	public static Size getSmallestSize(List<Size> sizes) {
		Size optimalSize = null;
		for (Iterator<Size> iterator = sizes.iterator(); iterator.hasNext();) {
			Size currSize =  iterator.next();		
			if(optimalSize == null) {
				optimalSize = currSize;
			} else if(optimalSize.height>currSize.height && optimalSize.width>currSize.width) {
				optimalSize = currSize;
			}
		}
		return optimalSize;
	}

}
