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
 * The asset catalogs of launch image for iOS.
 *
 * @author gootara.org
 * @see org.gootara.ios.image.util.IOSAssetCatalogs
 */
public enum IOSSplashAssetCatalogs implements IOSAssetCatalogs {
	// iOS11 and Later (iPhone X)
	IPHONEX_812x3           (IOSSplashInfo.SPLASH_812x3           , IDIOM.IPHONE, SYSTEM_VERSION.IOS11, EXTENT.FULL_SCREEN, SUBTYPE.H2436),
	IPHONEX_LANDSCAPE_812x3 (IOSSplashInfo.SPLASH_LANDSCAPE_812x3 , IDIOM.IPHONE, SYSTEM_VERSION.IOS11, EXTENT.FULL_SCREEN, SUBTYPE.H2436),
	
	// Apple TV
	TV_LANDSCAPEx2          (IOSSplashInfo.SPLASH_LANDSCAPE_TVx2  , IDIOM.TV   , SYSTEM_VERSION.IOS11, EXTENT.FULL_SCREEN  , null),
	TV_LANDSCAPE            (IOSSplashInfo.SPLASH_LANDSCAPE_TV    , IDIOM.TV   , SYSTEM_VERSION.IOS9, EXTENT.FULL_SCREEN  , null),

	// iOS8 and Later (iPhone)
	IPHONE8_736x3           (IOSSplashInfo.SPLASH_736x3           , IDIOM.IPHONE, SYSTEM_VERSION.IOS8, EXTENT.FULL_SCREEN, SUBTYPE.H736),
	IPHONE8_LANDSCAPE_736x3 (IOSSplashInfo.SPLASH_LANDSCAPE_736x3 , IDIOM.IPHONE, SYSTEM_VERSION.IOS8, EXTENT.FULL_SCREEN, SUBTYPE.H736),
	IPHONE8_667x2           (IOSSplashInfo.SPLASH_667x2           , IDIOM.IPHONE, SYSTEM_VERSION.IOS8, EXTENT.FULL_SCREEN, SUBTYPE.H667),

	// iOS7 and Later (iPhone)
	IPHONE7_480x2 (IOSSplashInfo.SPLASH_480x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, null),
	IPHONE7_568x2 (IOSSplashInfo.SPLASH_568x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE.RETINA4),

	// iOS7 and Later (iPad)
	IPAD7_PORTRAIT    (IOSSplashInfo.SPLASH_PORTRAIT    , IDIOM.IPAD , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, null),
	IPAD7_LANDSCAPE   (IOSSplashInfo.SPLASH_LANDSCAPE   , IDIOM.IPAD , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, null),
	IPAD7_PORTRAITx2  (IOSSplashInfo.SPLASH_PORTRAITx2  , IDIOM.IPAD , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, null),
	IPAD7_LANDSCAPEx2 (IOSSplashInfo.SPLASH_LANDSCAPEx2 , IDIOM.IPAD , SYSTEM_VERSION.IOS7, EXTENT.FULL_SCREEN, null),
	
	// iOS6 and prior
	IPHONE_480   (IOSSplashInfo.SPLASH_480   , IDIOM.IPHONE , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN, null),
	IPHONE_480x2 (IOSSplashInfo.SPLASH_480x2 , IDIOM.IPHONE , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN, null),
	IPHONE_568x2 (IOSSplashInfo.SPLASH_568x2 , IDIOM.IPHONE , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN, SUBTYPE.RETINA4),
	
	IPAD_PORTRAIT_STB    (IOSSplashInfo.SPLASH_PORTRAIT_STB    , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.TO_STATUS_BAR , null),
	IPAD_PORTRAIT        (IOSSplashInfo.SPLASH_PORTRAIT        , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STB   (IOSSplashInfo.SPLASH_LANDSCAPE_STB   , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.TO_STATUS_BAR , null),
	IPAD_LANDSCAPE       (IOSSplashInfo.SPLASH_LANDSCAPE       , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN   , null),
	IPAD_PORTRAIT_STBx2  (IOSSplashInfo.SPLASH_PORTRAIT_STBx2  , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.TO_STATUS_BAR , null),
	IPAD_PORTRAITx2      (IOSSplashInfo.SPLASH_PORTRAITx2      , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STBx2 (IOSSplashInfo.SPLASH_LANDSCAPE_STBx2 , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.TO_STATUS_BAR , null),
	IPAD_LANDSCAPEx2     (IOSSplashInfo.SPLASH_LANDSCAPEx2     , IDIOM.IPAD , SYSTEM_VERSION.ANY, EXTENT.FULL_SCREEN   , null),

	// iOS8 and Later (iPad) -- removed from iOS11?
	IPAD_PORTRAIT_1366x2    (IOSSplashInfo.SPLASH_1366x2          , IDIOM.IPAD,   SYSTEM_VERSION.IOS8, EXTENT.FULL_SCREEN, SUBTYPE.H1366),
	
	;

	private IOSImageInfo info;
	private IDIOM idiom;
	private EXTENT extent;
	private SUBTYPE subtype;
	private SYSTEM_VERSION minimumSystemVersion;
	private IOSSplashAssetCatalogs(IOSImageInfo info, IDIOM idiom, SYSTEM_VERSION minimumSystemVersion, EXTENT extent, SUBTYPE subtype) {
		this.info = info;
		this.idiom = idiom;
		this.minimumSystemVersion = minimumSystemVersion;
		this.extent = extent;
		this.subtype = subtype;
	}

	@Override public IOSImageInfo getIOSImageInfo() { return this.info; }
	@Override public IDIOM getIdiom() { return this.idiom; }
	@Override public SYSTEM_VERSION getMinimumSystemVersion() { return this.minimumSystemVersion; }
	@Override public String getFilename() { return info.getFilename(); }
	@Override public SUBTYPE getSubType() { return this.subtype; }
	@Override public ROLE getRole() { return null; }
	@Override public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.ORIENTATION, (this.info.getSize().getWidth() > this.info.getSize().getHeight() ? JSON_VALUE.ORIENTATION_LANDSCAPE : JSON_VALUE.ORIENTATION_PORTRAIT)));
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.IDIOM, this.getIdiom()));
		if (extent != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.EXTENT, this.getExtent()));
		}
		if (this.minimumSystemVersion.later(SYSTEM_VERSION.ANY)) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.MINIMUM_SYSTEM_VERSION, this.getMinimumSystemVersion()));
		}
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.FILENAME, this.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.SUBTYPE, this.getSubType()));
		}
		sb.append(String.format("      \"%s\" : \"%s\"\n", JSON_KEY.SCALE, info.getScale()));
		sb.append("    }");
		return sb.toString();
	}

	/**
	 * Get extent.
	 *
	 * @return extent
	 */
	public EXTENT getExtent() { return this.extent; }

}
