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
	IPHONE_20x2 (IOSIconInfo.ICON_20x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, null, null),	// iPhone Notifications
	IPHONE_20x3 (IOSIconInfo.ICON_20x3 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, null, null),	// iPhone Notifications
	IPHONE_29   (IOSIconInfo.ICON_29   , IDIOM.IPHONE , SYSTEM_VERSION.ANY, null, null),
	IPHONE_29x2 (IOSIconInfo.ICON_29x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, null, null),
	IPHONE_29x3 (IOSIconInfo.ICON_29x3 , IDIOM.IPHONE , SYSTEM_VERSION.IOS8, null, null),
	IPHONE_40x2 (IOSIconInfo.ICON_40x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, null, null),
	IPHONE_40x3 (IOSIconInfo.ICON_40x3 , IDIOM.IPHONE , SYSTEM_VERSION.IOS8, null, null),
	IPHONE_57   (IOSIconInfo.ICON_57   , IDIOM.IPHONE , SYSTEM_VERSION.ANY, null, null),
	IPHONE_57x2 (IOSIconInfo.ICON_57x2 , IDIOM.IPHONE , SYSTEM_VERSION.ANY, null, null),
	IPHONE_60x2 (IOSIconInfo.ICON_60x2 , IDIOM.IPHONE , SYSTEM_VERSION.IOS7, null, null),
	IPHONE_60x3 (IOSIconInfo.ICON_60x3 , IDIOM.IPHONE , SYSTEM_VERSION.IOS8, null, null),

	IPAD_20   (IOSIconInfo.ICON_20   , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),	// iPad Notifications
	IPAD_20x2 (IOSIconInfo.ICON_20x2 , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),	// iPad Notifications
	IPAD_29   (IOSIconInfo.ICON_29   , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_29x2 (IOSIconInfo.ICON_29x2 , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_40   (IOSIconInfo.ICON_40   , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_40x2 (IOSIconInfo.ICON_40x2 , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_50   (IOSIconInfo.ICON_50   , IDIOM.IPAD , SYSTEM_VERSION.ANY, null, null),
	IPAD_50x2 (IOSIconInfo.ICON_50x2 , IDIOM.IPAD , SYSTEM_VERSION.ANY, null, null),
	IPAD_72   (IOSIconInfo.ICON_72   , IDIOM.IPAD , SYSTEM_VERSION.ANY, null, null),
	IPAD_72x2 (IOSIconInfo.ICON_72x2 , IDIOM.IPAD , SYSTEM_VERSION.ANY, null, null),
	IPAD_76   (IOSIconInfo.ICON_76   , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_76x2 (IOSIconInfo.ICON_76x2 , IDIOM.IPAD , SYSTEM_VERSION.IOS7, null, null),
	IPAD_83_5x2 (IOSIconInfo.ICON_83_5x2 , IDIOM.IPAD , SYSTEM_VERSION.IOS9, null, null),

	CAR_120x120 (IOSIconInfo.ICON_120x120, IDIOM.CARPLAY, SYSTEM_VERSION.ANY, null, null),
	CAR_60x60x2 (IOSIconInfo.ICON_60x60x2, IDIOM.CARPLAY, SYSTEM_VERSION.IOS9, null, null),
	CAR_60x60x3 (IOSIconInfo.ICON_60x60x3, IDIOM.CARPLAY, SYSTEM_VERSION.IOS9, null, null),

	WATCH_24x24x2     (IOSIconInfo.ICON_24x24x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM38, ROLE.NOTIFICATION_CENTER),
	WATCH_27_5x27_5x2 (IOSIconInfo.ICON_27_5x27_5x2, IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM42, ROLE.NOTIFICATION_CENTER),
	WATCH_29x29x2     (IOSIconInfo.ICON_29x29x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, null         , ROLE.COMPANION_SETTINGS),
	WATCH_29x29x3     (IOSIconInfo.ICON_29x29x3    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, null         , ROLE.COMPANION_SETTINGS),
	WATCH_40x40x2     (IOSIconInfo.ICON_40x40x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM38, ROLE.APP_LAUNCHER),
	WATCH_44x44x2     (IOSIconInfo.ICON_44x44x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM42, ROLE.LONG_LOOK),
	WATCH_86x86x2     (IOSIconInfo.ICON_86x86x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM38, ROLE.QUICK_LOOK),
	WATCH_98x98x2     (IOSIconInfo.ICON_98x98x2    , IDIOM.APPLEWATCH, SYSTEM_VERSION.IOS8, SUBTYPE.MM42, ROLE.QUICK_LOOK),

	MAC_16x16x1    (IOSIconInfo.ICON_16x16x1   , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_16x16x2    (IOSIconInfo.ICON_16x16x2   , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_32x32x1    (IOSIconInfo.ICON_32x32x1   , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_32x32x2    (IOSIconInfo.ICON_32x32x2   , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_128x128x1  (IOSIconInfo.ICON_128x128x1 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_128x128x2  (IOSIconInfo.ICON_128x128x2 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_256x256x1  (IOSIconInfo.ICON_256x256x1 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_256x256x2  (IOSIconInfo.ICON_256x256x2 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_512x512x1  (IOSIconInfo.ICON_512x512x1 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),
	MAC_512x512x2  (IOSIconInfo.ICON_512x512x2 , IDIOM.MAC, SYSTEM_VERSION.ANY, null, null),

	;

	private IOSImageInfo info;
	private IDIOM idiom;
	private SUBTYPE subtype;
	private ROLE role;
	private SYSTEM_VERSION minimumSystemVersion;
	private IOSIconAssetCatalogs(IOSImageInfo info, IDIOM idiom, SYSTEM_VERSION minimumSystemVersion, SUBTYPE subtype, ROLE role) {
		this.info = info;
		this.idiom = idiom;
		this.subtype = subtype;
		this.role = role;
		this.minimumSystemVersion = minimumSystemVersion;
	}

	@Override public IOSImageInfo getIOSImageInfo() { return this.info; }
	@Override public IDIOM getIdiom() { return this.idiom; }
	@Override public SYSTEM_VERSION getMinimumSystemVersion() { return this.minimumSystemVersion; }
	@Override public SUBTYPE getSubType() { return this.subtype; }
	@Override public ROLE getRole() { return this.role; }
	@Override public String getFilename() {
		if (this.idiom.isIphone()) {
			return String.format("Icon-%s", info.getFilename());
		}
		return String.format("Icon-%s-%s", this.getIdiom(), info.getFilename());
	}
	@Override public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		Double w = new Double(info.getSize().getWidth() / info.getScale().value());
		Double h = new Double(info.getSize().getHeight() / info.getScale().value());
		if (Math.rint(w.doubleValue()) == w.doubleValue() && Math.rint(h.doubleValue()) == h.doubleValue()) {
			sb.append(String.format("      \"%s\" : \"%dx%d\",\n", JSON_KEY.SIZE, w.intValue(), h.intValue()));
		} else {
			sb.append(String.format("      \"%s\" : \"%.1fx%.1f\",\n", JSON_KEY.SIZE, w.doubleValue(), h.doubleValue()));
		}
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.IDIOM, this.getIdiom()));
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.FILENAME, this.getFilename()));
		if (subtype != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.SUBTYPE, this.getSubType()));
		}
		if (role != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.ROLE, this.getRole()));
		}
		sb.append(String.format("      \"%s\" : \"%s\"\n", JSON_KEY.SCALE, info.getScale()));
		sb.append("    }");
		return sb.toString();
	}

}
