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
	IPHONE7_480x2 (IOS7SplashInfo.SPLASH_480x2 , IOSAssetCatalogs.iPhone , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, null),
	IPHONE7_568x2 (IOS7SplashInfo.SPLASH_568x2 , IOSAssetCatalogs.iPhone , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD7_PORTRAIT   (IOS7SplashInfo.SPLASH_PORTRAIT    , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, null),
	IPAD7_LANDSCAPE  (IOS7SplashInfo.SPLASH_LANDSCAPE   , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, null),
	IPAD7_PORTRAITx2 (IOS7SplashInfo.SPLASH_PORTRAITx2  , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, null),
	IPAD7_LANDSCAPEx2(IOS7SplashInfo.SPLASH_LANDSCAPEx2 , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 7.0, null),
	// iOS6 or prior
	IPHONE_480   (IOS7SplashInfo.SPLASH_480   , IOSAssetCatalogs.iPhone , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 0.0, null),
	IPHONE_480x2 (IOS7SplashInfo.SPLASH_480x2 , IOSAssetCatalogs.iPhone , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 0.0, null),
	IPHONE_568x2 (IOS7SplashInfo.SPLASH_568x2 , IOSAssetCatalogs.iPhone , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN, 0.0, IOSSplashAssetCatalogs.SUBTYPE_RETINA4),
	IPAD_PORTRAIT      (IOS6SplashInfo.SPLASH_PORTRAIT    , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , 0.0, null),
	IPAD_PORTRAIT_FS   (IOS7SplashInfo.SPLASH_PORTRAIT    , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , 0.0, null),
	IPAD_LANDSCAPE     (IOS6SplashInfo.SPLASH_LANDSCAPE   , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , 0.0, null),
	IPAD_LANDSCAPE_FS  (IOS7SplashInfo.SPLASH_LANDSCAPE   , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , 0.0, null),
	IPAD_PORTRAITx2    (IOS6SplashInfo.SPLASH_PORTRAITx2  , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , 0.0, null),
	IPAD_PORTRAIT_FSx2 (IOS7SplashInfo.SPLASH_PORTRAITx2  , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , 0.0, null),
	IPAD_LANDSCAPEx2   (IOS6SplashInfo.SPLASH_LANDSCAPEx2 , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR , 0.0, null),
	IPAD_LANDSCAPE_FSx2(IOS7SplashInfo.SPLASH_LANDSCAPEx2 , IOSAssetCatalogs.iPad , IOSSplashAssetCatalogs.EXTENT_FULL_SCREEN   , 0.0, null),
	;

	public final static String EXTENT_FULL_SCREEN   = "full-screen";
	public final static String EXTENT_TO_STATUS_BAR = "to-status-bar";
	public final static String SUBTYPE_RETINA4 = "retina4";
	private IOSImageInfo info;
	private String idiom;
	private String extent;
	private String subtype;
	private double minimumSystemVersion;
	private IOSSplashAssetCatalogs(IOSImageInfo info, String idiom, String extent, double minimumSystemVersion, String subtype) {
		this.info = info;
		this.idiom = idiom;
		this.extent = extent;
		this.minimumSystemVersion = minimumSystemVersion;
		this.subtype = subtype;
	}

	@Override public IOSImageInfo getIOSImageInfo() { return this.info; }
	@Override public String getIdiom() { return this.idiom; }
	@Override public String getFilename() { return info.getFilename(); }
	@Override public String getScale() { return (info.isRetina() ? "2x" : "1x"); }
	@Override public boolean isIphone() { return this.getIdiom().equals(IOSAssetCatalogs.iPhone); }
	@Override public boolean isIpad() { return this.getIdiom().equals(IOSAssetCatalogs.iPad); }

	public String getExtent() { return this.extent; }
	public double getMinimumSystemVersion() { return this.minimumSystemVersion; }

	public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		sb.append(String.format("      \"orientation\" : \"%s\",\n", (this.info.getSize().getWidth() > this.info.getSize().getHeight() ? "landscape" : "portrait")));
		sb.append(String.format("      \"idiom\" : \"%s\",\n", this.getIdiom()));
		sb.append(String.format("      \"extent\" : \"%s\",\n", this.extent));
		if (this.minimumSystemVersion > 0.0) {
			sb.append(String.format("      \"minimum-system-version\" : \"%s\",\n", String.format("%.1f", this.minimumSystemVersion)));
		}
		sb.append(String.format("      \"filename\" : \"%s\",\n", info.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"subtype\" : \"%s\",\n", this.subtype));
		}
		sb.append(String.format("      \"scale\" : \"%s\"\n", this.getScale()));
		sb.append("    }");
		return sb.toString();
	}
}
