/**
 * Copyright (c) 2013 gootara.org <http://gootara.org>
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gootara.ios.image.util;

/**
 * The asset catalogs for iOS.
 *
 * @author gootara.org
 */
public interface IOSAssetCatalogs {
	/** Content.json header. */
	public static final String JSON_HEADER = "{\n  \"images\" : [\n";
	/** Content.json footer. */
	public static final String JSON_FOOTER = "\n  ],\n  \"info\" : {\n    \"version\" : 1,\n    \"author\" : \"xcode\"\n  }\n}";
	public static final double SYSTEM_VERSION_ANY = 0.0d;
	public static final double SYSTEM_VERSION_7   = 7.0d;
	static final String iPhone = "iphone";
	static final String iPad = "ipad";

	/**
	 * Get IOSImageInfo.
	 *
	 * @return IOSImageInfo
	 * @see org.gootara.ios.image.util.IOSImageInfo
	 */
	public IOSImageInfo getIOSImageInfo();

	/**
	 * Get idiom.
	 *
	 * @return idiom
	 */
	public String getIdiom();

	/**
	 * Get minimum system version.
	 *
	 * @return minimum system version
	 */
	public double getMinimumSystemVersion();

	/**
	 * Get image filename.
	 *
	 * @return filename
	 */
	public String getFilename();

	/**
	 * Get scale. (e.g. 1x, 2x)
	 *
	 * @return scale
	 */
	public String getScale();

	/**
	 * Is image for the iPhone or not.
	 *
	 * @return true - for iPhone / false - not for iPhone
	 */
	public boolean isIphone();

	/**
	 * Is image for the iPad or not.
	 *
	 * @return true - for iPad / false - not for iPad
	 */
	public boolean isIpad();

	/**
	 * Get JSON string.
	 *
	 * @return JSON string
	 */
	public String toJson();
}
