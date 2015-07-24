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

import java.awt.Dimension;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * @author gootara.org
 * @param <E>
 *
 */
public class IOSImageSet implements IOSAssetCatalogs, IOSImageInfo, Cloneable, Comparable<IOSImageSet> {
	private File dstFile, srcFile;
	private String widthAsString;
	private String heightAsString;
	private String imageName;
	private Dimension size;
	private SCALE scale = SCALE.x1;
	private IDIOM idiom;
	private SUBTYPE subtype;
	private ROLE role;
	private SYSTEM_VERSION minimumSystemVersion = SYSTEM_VERSION.ANY;
	private LinkedHashMap<JSON_KEY, JSON_VALUE> options = new LinkedHashMap<JSON_KEY, JSON_VALUE>();

	public IOSImageSet() {}
	public IOSImageSet(String width, String height, SCALE scale, IDIOM idiom, File srcFile, File dstFile) {
		this.setWidthAsString(width);
		this.setHeightAsString(height);
		this.setScale(scale);
		this.setIdiom(idiom);
		this.setOriginalFile(srcFile);
		this.setFile(dstFile);
		this.setMinimumSystemVersion(SYSTEM_VERSION.ANY);
		this.setSubtype(null);
		this.setRole(null);
	}
	@Override public IOSImageInfo getIOSImageInfo() { return this; }
	@Override public IDIOM getIdiom() { return this.idiom; }
	@Override public SYSTEM_VERSION getMinimumSystemVersion() { return this.minimumSystemVersion; }
	@Override public String getFilename() { return dstFile == null ? null : dstFile.getName(); }
	@Override public SUBTYPE getSubType() { return this.subtype; }
	@Override public ROLE getRole() { return this.role; }
	@Override public String toJson() {
		StringBuilder sb = new StringBuilder("    {\n");
		if (this.getIdiom() != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.IDIOM, this.getIdiom()));
		}
		if (this.getSubtype() != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.SUBTYPE, this.getSubType()));
		}
		if (this.getRole() != null) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.ROLE, this.getRole()));
		}
		sb.append(String.format("      \"%s\" : \"%s\",\n", JSON_KEY.SCALE, this.getScale()));

		for (Entry<JSON_KEY, JSON_VALUE>keyVal : this.options.entrySet()) {
			sb.append(String.format("      \"%s\" : \"%s\",\n", keyVal.getKey(), keyVal.getValue()));
		}
		sb.append(String.format("      \"%s\" : \"%s\"\n", JSON_KEY.FILENAME, this.getFilename()));
		sb.append("    }");
		return sb.toString();
	}

	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override public int compareTo(IOSImageSet that) {
		int c = (this.getIdiom() == null ? "" : this.getIdiom().toString()).compareTo(that.getIdiom() == null ? "" : that.getIdiom().toString());
		if (c != 0) { return c; }

		if (this.getScale() != null && that.getScale() != null) {
			c = this.getScale().greaterThan(that.getScale()) ? 1 : this.getScale().lessThan(that.getScale()) ? -1 : 0;
			if (c != 0) { return c; }
		}

		c = (this.getSubtype() == null ? "" : this.getSubtype().toString()).compareTo(that.getSubtype() == null ? "" : that.getSubtype().toString());
		if (c != 0) { return c; }

		c = (this.getRole() == null ? "" : this.getRole()).toString().compareTo(that.getRole() == null ? "" : that.getRole().toString());
		if (c != 0) { return c; }

		if (this.getMinimumSystemVersion() != null && that.getMinimumSystemVersion() != null) {
			c = (int)(this.getMinimumSystemVersion().value() - that.getMinimumSystemVersion().value());
			if (c != 0) { return c; }
		}

		if (this.getFilename() != null && that.getFilename() != null) {
			return this.getFilename().compareTo(that.getFilename());
		}
		return 0;
	}

	/**
	 * @return file
	 */
	public File getFile() {
		return dstFile;
	}

	/**
	 * @param file set file
	 */
	public IOSImageSet setFile(File file) {
		this.dstFile = file;
		return this;
	}

	/**
	 * @return size
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @param size set size
	 */
	public IOSImageSet setSize(Dimension size) {
		this.size = size;
		return this;
	}

	/**
	 * @return subtype
	 */
	public SUBTYPE getSubtype() {
		return subtype;
	}

	/**
	 * @param subtype set subtype
	 */
	public IOSImageSet setSubtype(SUBTYPE subtype) {
		this.subtype = subtype;
		return this;
	}

	/**
	 * @param scale set scale
	 */
	public IOSImageSet setScale(SCALE scale) {
		this.scale = scale;
		return this;
	}

	/**
	 * @return scale
	 */
	public SCALE getScale() {
		return scale;
	}

	/**
	 * @param idiom set idiom
	 */
	public IOSImageSet setIdiom(IDIOM idiom) {
		this.idiom = idiom;
		return this;
	}

	/**
	 * @param role set role
	 */
	public IOSImageSet setRole(ROLE role) {
		this.role = role;
		return this;
	}

	/**
	 * @param minimumSystemVersion set minimumSystemVersion
	 */
	public IOSImageSet setMinimumSystemVersion(SYSTEM_VERSION minimumSystemVersion) {
		this.minimumSystemVersion = minimumSystemVersion;
		return this;
	}

	/**
	 * @return srcFile
	 */
	public File getOriginalFile() {
		return srcFile;
	}

	/**
	 * @param srcFile set srcFile
	 */
	public IOSImageSet setOriginalFile(File srcFile) {
		this.srcFile = srcFile;
		return this;
	}

	/**
	 * @return imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName set imageName
	 */
	public IOSImageSet setImageName(String imageName) {
		this.imageName = imageName;
		return this;
	}

	/**
	 * @return widthAsString
	 */
	public String getWidthAsString() {
		return widthAsString;
	}

	/**
	 * @param widthAsString set widthAsString
	 */
	public IOSImageSet setWidthAsString(String widthAsString) {
		this.widthAsString = widthAsString;
		return this;
	}

	/**
	 * @return heightAsString
	 */
	public String getHeightAsString() {
		return heightAsString;
	}

	/**
	 * @param heightAsString set heightAsString
	 */
	public IOSImageSet setHeightAsString(String heightAsString) {
		this.heightAsString = heightAsString;
		return this;
	}

	/**
	 * Set additional property.
	 *
	 * @param key
	 * @param value
	 */
	public IOSImageSet setOption(JSON_KEY key, JSON_VALUE value) {
		if (value == null) {
			this.removeOption(key);
		} else {
			this.options.put(key, value);
		}
		return this;
	}

	/**
	 * Get additional property.
	 * Returns empty string when not contains key.
	 *
	 * @param key
	 * @return
	 */
	public JSON_VALUE getOption(JSON_KEY key) {
		return this.options.get(key);
	}

	/**
	 * Remove additional property.
	 *
	 * @param key
	 * @return
	 */
	public boolean removeOption(JSON_KEY key) {
		return this.options.remove(key) != null;
	}

	/**
	 * Additional properties contains key.
	 *
	 * @param key
	 * @return
	 */
	public boolean containsOption(JSON_KEY key) {
		return this.options.containsKey(key);
	}
}