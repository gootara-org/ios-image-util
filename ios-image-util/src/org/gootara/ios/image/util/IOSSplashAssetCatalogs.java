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

    // iOS8 and iPhone 6 upper
    IPHONE8_736x3           (IOSSplashInfo.SPLASH_736x3           , IDIOM_IPHONE, SYSTEM_VERSION_8, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE_736H),
    IPHONE8_LANDSCAPE_736x3 (IOSSplashInfo.SPLASH_LANDSCAPE_736x3 , IDIOM_IPHONE, SYSTEM_VERSION_8, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE_736H),
    IPHONE8_667x2           (IOSSplashInfo.SPLASH_667x2           , IDIOM_IPHONE, SYSTEM_VERSION_8, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE_667H),

	// iOS7 or posterior
	IPHONE7_480x2 (IOSSplashInfo.SPLASH_480x2 , IDIOM_IPHONE , SYSTEM_VERSION_7, null, null),
	IPHONE7_568x2 (IOSSplashInfo.SPLASH_568x2 , IDIOM_IPHONE , SYSTEM_VERSION_7, null, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD7_PORTRAIT    (IOSSplashInfo.SPLASH_PORTRAIT    , IDIOM_IPAD , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_LANDSCAPE   (IOSSplashInfo.SPLASH_LANDSCAPE   , IDIOM_IPAD , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_PORTRAITx2  (IOSSplashInfo.SPLASH_PORTRAITx2  , IDIOM_IPAD , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_LANDSCAPEx2 (IOSSplashInfo.SPLASH_LANDSCAPEx2 , IDIOM_IPAD , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	// iOS6 or prior
	IPHONE_480   (IOSSplashInfo.SPLASH_480   , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, null),
	IPHONE_480x2 (IOSSplashInfo.SPLASH_480x2 , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, null),
	IPHONE_568x2 (IOSSplashInfo.SPLASH_568x2 , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD_PORTRAIT_STB    (IOSSplashInfo.SPLASH_PORTRAIT_STB    , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_PORTRAIT        (IOSSplashInfo.SPLASH_PORTRAIT        , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STB   (IOSSplashInfo.SPLASH_LANDSCAPE_STB   , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_LANDSCAPE       (IOSSplashInfo.SPLASH_LANDSCAPE       , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_PORTRAIT_STBx2  (IOSSplashInfo.SPLASH_PORTRAIT_STBx2  , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_PORTRAITx2      (IOSSplashInfo.SPLASH_PORTRAITx2      , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STBx2 (IOSSplashInfo.SPLASH_LANDSCAPE_STBx2 , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_LANDSCAPEx2     (IOSSplashInfo.SPLASH_LANDSCAPEx2     , IDIOM_IPAD , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	;

	public final static String EXTENT_FULL_SCREEN   = "full-screen";
	public final static String EXTENT_TO_STATUS_BAR = "to-status-bar";
	private IOSImageInfo info;
	private String idiom;
	private String extent;
	private String subtype;
	private float minimumSystemVersion;
	private IOSSplashAssetCatalogs(IOSImageInfo info, String idiom, float minimumSystemVersion, String extent, String subtype) {
		this.info = info;
		this.idiom = idiom;
		this.minimumSystemVersion = minimumSystemVersion;
		this.extent = extent;
		this.subtype = subtype;
	}

	@Override public IOSImageInfo getIOSImageInfo() { return this.info; }
	@Override public String getIdiom() { return this.idiom; }
	@Override public float getMinimumSystemVersion() { return this.minimumSystemVersion; }
	@Override public String getFilename() { return info.getFilename(); }
	@Override public String getScale() { return (String.format("%dx", info.getScale())); }
	@Override public boolean isIphone() { return this.getIdiom().equals(IDIOM_IPHONE); }
	@Override public boolean isIpad() { return this.getIdiom().equals(IDIOM_IPAD); }
	@Override public boolean isCarPlay() { return this.getIdiom().equals(IDIOM_CARPLAY); }
	@Override public boolean isAppleWatch() { return this.getIdiom().equals(IDIOM_APPLEWATCH); }
	@Override public String getSubType() { return this.subtype; }
	@Override public String getRole() { return null; }
	@Override public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		sb.append(String.format("      \"orientation\" : \"%s\",\n", (this.info.getSize().getWidth() > this.info.getSize().getHeight() ? "landscape" : "portrait")));
		sb.append(String.format("      \"idiom\" : \"%s\",\n", this.getIdiom()));
		if (extent != null) {
			sb.append(String.format("      \"extent\" : \"%s\",\n", this.getExtent()));
		}
		if (this.minimumSystemVersion > SYSTEM_VERSION_ANY) {
			sb.append(String.format("      \"minimum-system-version\" : \"%.1f\",\n", this.getMinimumSystemVersion()));
		}
		sb.append(String.format("      \"filename\" : \"%s\",\n", info.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"subtype\" : \"%s\",\n", this.getSubType()));
		}
		sb.append(String.format("      \"scale\" : \"%s\"\n", this.getScale()));
		sb.append("    }");
		return sb.toString();
	}

	/**
	 * Get extent.
	 *
	 * @return extent
	 */
	public String getExtent() { return this.extent; }

}
