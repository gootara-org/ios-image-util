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
	/** Content.json header */
	public static final String JSON_HEADER = "{\n  \"images\" : [\n";
	/** Content.json footer */
	public static final String JSON_FOOTER = "\n  ],\n  \"info\" : {\n    \"version\" : 1,\n    \"author\" : \"xcode\"\n  }\n}";
	public static final float SYSTEM_VERSION_ANY = 0.0f;
	public static final float SYSTEM_VERSION_7   = 7.0f;
	public static final float SYSTEM_VERSION_8   = 8.0f;

	static final String IDIOM_IPHONE = "iphone";
	static final String IDIOM_IPAD = "ipad";
	static final String IDIOM_CARPLAY = "car";
	static final String IDIOM_APPLEWATCH = "watch";

	public final static String SUBTYPE_RETINA4 = "retina4";
	public final static String SUBTYPE_667H    = "667h";
	public final static String SUBTYPE_736H    = "736h";
	public final static String SUBTYPE_38MM    = "38mm";
	public final static String SUBTYPE_42MM    = "42mm";

	public final static String ROLE_NOTIFICATION_CENTER = "notificationCenter";
	public final static String ROLE_COMPANION_SETTINGS  = "companionSettings";
	public final static String ROLE_APP_LAUNCHER        = "appLauncher";
	public final static String ROLE_LONG_LOOK           = "longLook";
	public final static String ROLE_QUICK_LOOK          = "quickLook";

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
	public float getMinimumSystemVersion();

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
	 * Is image for the CarPlay or not.
	 *
	 * @return true - for CarPlay / false - not for CarPlay
	 */
	public boolean isCarPlay();

	/**
	 * Is image for the Apple Watch or not.
	 *
	 * @return true - for Apple Watch / false - not for Apple Watch
	 */
	public boolean isAppleWatch();

	/**
	 * Get subtype.
	 *
	 * @return subtype
	 */
	public String getSubType();

	/**
	 * Get role.
	 *
	 * @return subtype
	 */
	public String getRole();

	/**
	 * Get JSON string.
	 *
	 * @return JSON string
	 */
	public String toJson();
}
