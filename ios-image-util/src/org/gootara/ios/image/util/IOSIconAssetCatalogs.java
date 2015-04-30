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
 * The asset catalogs of icon for iOS.
 *
 * @author gootara.org
 * @see org.gootara.ios.image.util.IOSAssetCatalogs
 */
public enum IOSIconAssetCatalogs implements IOSAssetCatalogs {
	IPHONE_29   (IOSIconInfo.ICON_29   , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, null),
	IPHONE_29x2 (IOSIconInfo.ICON_29x2 , IDIOM_IPHONE , SYSTEM_VERSION_7, null, null),
	IPHONE_29x3 (IOSIconInfo.ICON_29x3 , IDIOM_IPHONE , SYSTEM_VERSION_8, null, null),
	IPHONE_40x2 (IOSIconInfo.ICON_40x2 , IDIOM_IPHONE , SYSTEM_VERSION_7, null, null),
	IPHONE_40x3 (IOSIconInfo.ICON_40x3 , IDIOM_IPHONE , SYSTEM_VERSION_8, null, null),
	IPHONE_57   (IOSIconInfo.ICON_57   , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, null),
	IPHONE_57x2 (IOSIconInfo.ICON_57x2 , IDIOM_IPHONE , SYSTEM_VERSION_ANY, null, null),
	IPHONE_60x2 (IOSIconInfo.ICON_60x2 , IDIOM_IPHONE , SYSTEM_VERSION_7, null, null),
	IPHONE_60x3 (IOSIconInfo.ICON_60x3 , IDIOM_IPHONE , SYSTEM_VERSION_8, null, null),

	IPAD_29   (IOSIconInfo.ICON_29   , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),
	IPAD_29x2 (IOSIconInfo.ICON_29x2 , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),
	IPAD_40   (IOSIconInfo.ICON_40   , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),
	IPAD_40x2 (IOSIconInfo.ICON_40x2 , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),
	IPAD_50   (IOSIconInfo.ICON_50   , IDIOM_IPAD , SYSTEM_VERSION_ANY, null, null),
	IPAD_50x2 (IOSIconInfo.ICON_50x2 , IDIOM_IPAD , SYSTEM_VERSION_ANY, null, null),
	IPAD_72   (IOSIconInfo.ICON_72   , IDIOM_IPAD , SYSTEM_VERSION_ANY, null, null),
	IPAD_72x2 (IOSIconInfo.ICON_72x2 , IDIOM_IPAD , SYSTEM_VERSION_ANY, null, null),
	IPAD_76   (IOSIconInfo.ICON_76   , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),
	IPAD_76x2 (IOSIconInfo.ICON_76x2 , IDIOM_IPAD , SYSTEM_VERSION_7, null, null),

	CAR_120x120 (IOSIconInfo.ICON_120x120, IDIOM_CARPLAY, SYSTEM_VERSION_ANY, null, null),

	WATCH_24x24x2     (IOSIconInfo.ICON_24x24x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_38MM, ROLE_NOTIFICATION_CENTER),
	WATCH_27_5x27_5x2 (IOSIconInfo.ICON_27_5x27_5x2, IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_42MM, ROLE_NOTIFICATION_CENTER),
	WATCH_29x29x2     (IOSIconInfo.ICON_29x29x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, null        , ROLE_COMPANION_SETTINGS),
	WATCH_29x29x3     (IOSIconInfo.ICON_29x29x3    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, null        , ROLE_COMPANION_SETTINGS),
	WATCH_40x40x2     (IOSIconInfo.ICON_40x40x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_38MM, ROLE_APP_LAUNCHER),
	WATCH_44x44x2     (IOSIconInfo.ICON_44x44x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_42MM, ROLE_LONG_LOOK),
	WATCH_86x86x2     (IOSIconInfo.ICON_86x86x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_38MM, ROLE_QUICK_LOOK),
	WATCH_98x98x2     (IOSIconInfo.ICON_98x98x2    , IDIOM_APPLEWATCH, SYSTEM_VERSION_8, SUBTYPE_42MM, ROLE_QUICK_LOOK),
	;

	private IOSImageInfo info;
	private String idiom;
	private String subtype;
	private String role;
	private float minimumSystemVersion;
	private IOSIconAssetCatalogs(IOSImageInfo info, String idiom, float minimumSystemVersion, String subtype, String role) {
		this.info = info;
		this.idiom = idiom;
		this.subtype = subtype;
		this.role = role;
		this.minimumSystemVersion = minimumSystemVersion;
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
	@Override public String getRole() { return this.role; }
	@Override public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		Double w = new Double(info.getSize().getWidth() / info.getScale());
		Double h = new Double(info.getSize().getHeight() / info.getScale());
		if (Math.rint(w.doubleValue()) == w.doubleValue() && Math.rint(h.doubleValue()) == h.doubleValue()) {
			sb.append(String.format("      \"size\" : \"%dx%d\",\n", w.intValue(), h.intValue()));
		} else {
			sb.append(String.format("      \"size\" : \"%.1fx%.1f\",\n", w.doubleValue(), h.doubleValue()));
		}
		sb.append(String.format("      \"idiom\" : \"%s\",\n", this.getIdiom()));
		sb.append(String.format("      \"filename\" : \"%s\",\n", info.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"subtype\" : \"%s\",\n", this.getSubType()));
		}
		if (role != null) {
			sb.append(String.format("      \"role\" : \"%s\",\n", this.getRole()));
		}
		sb.append(String.format("      \"scale\" : \"%s\"\n", this.getScale()));
		sb.append("    }");
		return sb.toString();
	}

}
