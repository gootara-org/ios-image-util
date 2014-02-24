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
 * @author gootara.org
 *
 */
public enum IOSSplashAssetCatalogs implements IOSAssetCatalogs {
	// iOS7 or posterior
	IPHONE7_480x2 (IOSSplashInfo.SPLASH_480x2 , iPhone , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPHONE7_568x2 (IOSSplashInfo.SPLASH_568x2 , iPhone , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD7_PORTRAIT    (IOSSplashInfo.SPLASH_PORTRAIT    , iPad , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_LANDSCAPE   (IOSSplashInfo.SPLASH_LANDSCAPE   , iPad , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_PORTRAITx2  (IOSSplashInfo.SPLASH_PORTRAITx2  , iPad , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPAD7_LANDSCAPEx2 (IOSSplashInfo.SPLASH_LANDSCAPEx2 , iPad , SYSTEM_VERSION_7, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	// iOS6 or prior
	IPHONE_480   (IOSSplashInfo.SPLASH_480   , iPhone , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPHONE_480x2 (IOSSplashInfo.SPLASH_480x2 , iPhone , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, null),
	IPHONE_568x2 (IOSSplashInfo.SPLASH_568x2 , iPhone , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD_PORTRAIT_STB    (IOSSplashInfo.SPLASH_PORTRAIT_STB    , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_PORTRAIT        (IOSSplashInfo.SPLASH_PORTRAIT        , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STB   (IOSSplashInfo.SPLASH_LANDSCAPE_STB   , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_LANDSCAPE       (IOSSplashInfo.SPLASH_LANDSCAPE       , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_PORTRAIT_STBx2  (IOSSplashInfo.SPLASH_PORTRAIT_STBx2  , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_PORTRAITx2      (IOSSplashInfo.SPLASH_PORTRAITx2      , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	IPAD_LANDSCAPE_STBx2 (IOSSplashInfo.SPLASH_LANDSCAPE_STBx2 , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , null),
	IPAD_LANDSCAPEx2     (IOSSplashInfo.SPLASH_LANDSCAPEx2     , iPad , SYSTEM_VERSION_ANY, IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , null),
	;

	public final static String EXTENT_FULL_SCREEN   = "full-screen";
	public final static String EXTENT_TO_STATUS_BAR = "to-status-bar";
	public final static String SUBTYPE_RETINA4 = "retina4";
	private IOSImageInfo info;
	private String idiom;
	private String extent;
	private String subtype;
	private double minimumSystemVersion;
	private IOSSplashAssetCatalogs(IOSImageInfo info, String idiom, double minimumSystemVersion, String extent, String subtype) {
		this.info = info;
		this.idiom = idiom;
		this.minimumSystemVersion = minimumSystemVersion;
		this.extent = extent;
		this.subtype = subtype;
	}

	@Override public IOSImageInfo getIOSImageInfo() { return this.info; }
	@Override public String getIdiom() { return this.idiom; }
	@Override public double getMinimumSystemVersion() { return this.minimumSystemVersion; }
	@Override public String getFilename() { return info.getFilename(); }
	@Override public String getScale() { return (info.isRetina() ? "2x" : "1x"); }
	@Override public boolean isIphone() { return this.getIdiom().equals(iPhone); }
	@Override public boolean isIpad() { return this.getIdiom().equals(iPad); }

	public String getExtent() { return this.extent; }
	public String getSubType() { return this.subtype; }

	public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		sb.append(String.format("      \"orientation\" : \"%s\",\n", (this.info.getSize().getWidth() > this.info.getSize().getHeight() ? "landscape" : "portrait")));
		sb.append(String.format("      \"idiom\" : \"%s\",\n", this.getIdiom()));
		sb.append(String.format("      \"extent\" : \"%s\",\n", this.getExtent()));
		if (this.minimumSystemVersion > SYSTEM_VERSION_ANY) {
			sb.append(String.format("      \"minimum-system-version\" : \"%s\",\n", String.format("%.1f", this.getMinimumSystemVersion())));
		}
		sb.append(String.format("      \"filename\" : \"%s\",\n", info.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"subtype\" : \"%s\",\n", this.getSubType()));
		}
		sb.append(String.format("      \"scale\" : \"%s\"\n", this.getScale()));
		sb.append("    }");
		return sb.toString();
	}
}
