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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The asset catalogs for iOS.
 *
 * @author gootara.org
 */
public interface IOSAssetCatalogs {

	public enum JSON_KEY {
			IDIOM                  ("idiom")
		,	FILENAME               ("filename")
		,	SUBTYPE                ("subtype")
		,	ROLE                   ("role")
		,	SCALE                  ("scale")
		,	SIZE                   ("size")
		,	ORIENTATION            ("orientation")
		,	EXTENT                 ("extent")
		,	SCREEN_WIDTH           ("screenWidth")
		,	WIDTH_CLASS            ("widthClass")
		,	HEIGHT_CLASS           ("heightClass")
		,	MINIMUM_SYSTEM_VERSION ("minimum-system-version")
		;
		private String keyName;
		JSON_KEY(String keyName) { this.keyName = keyName; }
		@Override public String toString() { return this.keyName; }
	}

	public enum SYSTEM_VERSION {
			ANY     (0.0f)
		,	IOS7    (7.0f)
		,	IOS8    (8.0f)
		,	IOS9    (9.0f)
		,	IOS10   (10.0f)
		,	IOS11   (11.0f)
		;
		private float value;
		SYSTEM_VERSION(float value) { this.value = value; }
		@Override public String toString() { return String.format("%.1f", value); }
		public float value() { return value; }
		public boolean later(SYSTEM_VERSION that) { return that.value < this.value; }
		public boolean andLater(SYSTEM_VERSION that) { return that.value <= this.value; }
		public boolean prior(SYSTEM_VERSION that) { return that.value > this.value; }
		public boolean andPrior(SYSTEM_VERSION that) { return that.value >= this.value; }
	}

	public enum IDIOM {
			UNIVERSAL  ("universal")
		,	IPHONE     ("iphone")
		,	IPAD       ("ipad")
		,	CARPLAY    ("car")
		,	APPLEWATCH ("watch")
		,	MAC        ("mac")
		,	TV         ("tv")
		,	IOS_MARKETING ("ios-marketing")
		,	WATCH_MARKETING ("watch-marketing")
		;
		private String idiom;
		IDIOM(String idiom) { this.idiom = idiom; }
		@Override public String toString() { return this.idiom; }
		public boolean isUniversal() { return this.idiom.equals(UNIVERSAL.toString()); }
		public boolean isIphone() { return this.idiom.equals(IPHONE.toString()); }
		public boolean isIpad() { return this.idiom.equals(IPAD.toString()); }
		public boolean isAppleWatch() { return this.idiom.equals(APPLEWATCH.toString()); }
		public boolean isCarplay() { return this.idiom.equals(CARPLAY.toString()); }
		public boolean isMac() { return this.idiom.equals(MAC.toString()); }
		public boolean isTv() { return this.idiom.equals(TV.toString()); }
	}

	public enum SUBTYPE {
			RETINA4  ("retina4")
		,	H667     ("667h")
		,	H736     ("736h")
		,	H1366    ("1366h")
		,	H2436    ("2436h")
		,	MM38     ("38mm")
		,	MM42     ("42mm")
		;
		private String subtype;
		SUBTYPE(String subtype) { this.subtype = subtype; }
		@Override public String toString() { return this.subtype; }
	}

	public enum EXTENT {
			FULL_SCREEN     ("full-screen")
		,	TO_STATUS_BAR   ("to-status-bar")
		;
		private String extent;
		EXTENT(String extent) { this.extent = extent; }
		@Override public String toString() { return this.extent; }
	}

	public enum ROLE {
			NOTIFICATION_CENTER ("notificationCenter")
		,	COMPANION_SETTINGS  ("companionSettings")
		,	APP_LAUNCHER        ("appLauncher")
		,	LONG_LOOK           ("longLook")
		,	QUICK_LOOK          ("quickLook")
		;
		private String role;
		ROLE(String role) { this.role = role; }
		@Override public String toString() { return this.role; }
	}

	public enum JSON_VALUE {
			SCREEN_WIDTH_38MM      ("{130,145}")
		,	SCREEN_WIDTH_42MM      ("{146,165}")
		,	ORIENTATION_LANDSCAPE  ("landscape")
		,	ORIENTATION_PORTRAIT   ("portrait")
		,	SIZE_CLASS_ANY         ("any")
		,	SIZE_CLASS_COMPACT     ("compact")
		,	SIZE_CLASS_REGULAR     ("regular")
		;
		private String value;
		JSON_VALUE(String value) { this.value = value; }
		@Override public String toString() { return this.value; }
	}

	public enum JSON_INFO_KEY {
			VERSION    ("version")
		,	AUTHOR     ("author")
		,	RENDERING  ("template-rendering-intent")
		;
		private String keyName;
		JSON_INFO_KEY(String keyName) { this.keyName = keyName; }
		@Override public String toString() { return this.keyName; }
	}

	public enum JSON_INFO_VALUE {
			VERSION_1          ("1")
		,	AUTHOR_XCODE       ("\"xcode\"")
		,	RENDERING_ORIGINAL ("\"original\"")
		,	RENDERING_TEMPLATE ("\"template\"")
		;
		private String value;
		JSON_INFO_VALUE(String value) { this.value = value; }
		@Override public String toString() { return this.value; }
	}

	public enum JSON_PROPERTY_KEY {
			PRE_RENDERED  ("pre-rendered")
		;
		private String keyName;
		JSON_PROPERTY_KEY(String keyName) { this.keyName = keyName; }
		@Override public String toString() { return this.keyName; }
	}

	public enum JSON_PROPERTY_VALUE {
			PRE_RENDERED_TRUE  ("true")
		;
		private String value;
		JSON_PROPERTY_VALUE(String value) { this.value = value; }
		@Override public String toString() { return this.value; }
	}

	class JsonUtility extends ArrayList<IOSAssetCatalogs> {
		private LinkedHashMap<JSON_INFO_KEY, JSON_INFO_VALUE> info;
		private LinkedHashMap<JSON_PROPERTY_KEY, JSON_PROPERTY_VALUE> properties;

		public JsonUtility() {
			super();
			this.info = new LinkedHashMap<JSON_INFO_KEY, JSON_INFO_VALUE>();
			setInfo(JSON_INFO_KEY.VERSION, JSON_INFO_VALUE.VERSION_1);
			setInfo(JSON_INFO_KEY.AUTHOR, JSON_INFO_VALUE.AUTHOR_XCODE);
			this.properties = new LinkedHashMap<JSON_PROPERTY_KEY, JSON_PROPERTY_VALUE>();
		}

		@Override public void clear() {
			this.info.clear();
			this.properties.clear();
			super.clear();
		}

		/**
		 * Set additional json infromation.
		 *
		 * @param key
		 * @param value
		 * @return
		 */
		public JsonUtility setInfo(JSON_INFO_KEY key, JSON_INFO_VALUE value) {
			if (value == null) {
				this.removeInfo(key);
			} else {
				info.put(key, value);
			}
			return this;
		}

		/**
		 * Get additional json information.
		 *
		 * @param key
		 * @return
		 */
		public JSON_INFO_VALUE getInfo(JSON_INFO_KEY key) {
			return info.get(key);
		}

		/**
		 * Remove additional json information.
		 *
		 * @param key
		 * @return
		 */
		public boolean removeInfo(JSON_INFO_KEY key) {
			return info.remove(key) != null;
		}

		/**
		 * Contains additional json information.
		 *
		 * @param key
		 * @return
		 */
		public boolean containsInfo(JSON_INFO_KEY key) {
			return info.containsKey(key);
		}

		/**
		 * Set additional json property.
		 *
		 * @param key
		 * @param value
		 * @return
		 */
		public JsonUtility setProperty(JSON_PROPERTY_KEY key, JSON_PROPERTY_VALUE value) {
			if (value == null) {
				this.removeProperty(key);
			} else {
				properties.put(key, value);
			}
			return this;
		}

		/**
		 * Get additional json property.
		 *
		 * @param key
		 * @return
		 */
		public JSON_PROPERTY_VALUE getProperty(JSON_PROPERTY_KEY key) {
			return properties.get(key);
		}

		/**
		 * Remove additional json property.
		 *
		 * @param key
		 * @return
		 */
		public boolean removeProperty(JSON_PROPERTY_KEY key) {
			return properties.remove(key) != null;
		}

		/**
		 * Contains additional json property.
		 *
		 * @param key
		 * @return
		 */
		public boolean containsProperty(JSON_PROPERTY_KEY key) {
			return properties.containsKey(key);
		}

		/**
		 * Write "Content.json" to File
		 *
		 * @param f
		 * @throws IOException
		 */
		public void writeContentJson(File f) throws IOException {
			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(f));
				writer.write("{\n  \"images\" : [\n");
				boolean first = true;
				for (IOSAssetCatalogs imageSet : this) {
					if (first) {
						first = false;
					} else {
						writer.write(",\n");
					}
					writer.write(((IOSAssetCatalogs) imageSet).toJson());
				}

				writer.write("\n  ],\n  \"info\" : {\n");
				first = true;
				for (Map.Entry<JSON_INFO_KEY, JSON_INFO_VALUE> keyValue : info.entrySet()) {
					writer.write(String.format("%s    \"%s\" : %s", (first ? "" : ",\n"), keyValue.getKey(), keyValue.getValue()));
					first = false;
				}
				writer.write("\n  }");

				if (properties.size() > 0) {
					writer.write(",\n  \"properties\" : {\n");
					first = true;
					for (Map.Entry<JSON_PROPERTY_KEY, JSON_PROPERTY_VALUE> keyValue : properties.entrySet()) {
						writer.write(String.format("%s    \"%s\" : %s", (first ? "" : ",\n"), keyValue.getKey(), keyValue.getValue()));
						first = false;
					}
					writer.write("\n  }");
				}

				writer.write("\n}");
				writer.close();
				writer = null;
			} catch (IOException ioex) {
				throw ioex;
			} finally {
				if (writer != null) {
					try { writer.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			}
		}
	}

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
	public IDIOM getIdiom();

	/**
	 * Get minimum system version.
	 *
	 * @return minimum system version
	 */
	public SYSTEM_VERSION getMinimumSystemVersion();

	/**
	 * Get image filename.
	 *
	 * @return filename
	 */
	public String getFilename();

	/**
	 * Get subtype.
	 *
	 * @return subtype
	 */
	public SUBTYPE getSubType();

	/**
	 * Get role.
	 *
	 * @return subtype
	 */
	public ROLE getRole();

	/**
	 * Get JSON string.
	 *
	 * @return JSON string
	 */
	public String toJson();
}
