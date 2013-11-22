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

/**
 * @author gootara.org
 *
 */
public enum IOS7SplashInfo implements IOSImageInfo {
	SPLASH_PORTRAIT("Default-Portrait-1024h.png", 768, 1024, "iPad / iPad2 / iPad mini"),
	SPLASH_PORTRAITx2("Default-Portrait-1024h@2x.png", 1536, 2048, "iPad 3 or later"),
	SPLASH_LANDSCAPE("Default-Landscape-768h.png", 1024, 768, "iPad / iPad2 / iPad mini"),
	SPLASH_LANDSCAPEx2("Default-Landscape-768h@2x.png", 2048, 1536, "iPad 3 or later"),
	;

	private String filename;
	private Dimension size;
	private String description;
	private IOS7SplashInfo(String filename, int width, int height, String description) {
		this.filename = filename;
		this.size = new Dimension(width, height);
		this.description = description;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return size; }
	@Override public String getDescription() { return this.description; }
}
