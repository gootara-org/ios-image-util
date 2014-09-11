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
 * The informations of icon for iOS.
 *
 * @author gootara.org
 * @see org.gootara.ios.image.util.IOSImageInfo
 */
public enum IOSIconInfo implements IOSImageInfo {
    //           filename                width   height  scale      description
    ICON_29   ( "Icon-Small.png"       , 29    , 29    , 1    ), // iPhone3G / 3GS / iPad / iPad2 / iPad mini
    ICON_29x2 ( "Icon-Small@2x.png"    , 58    , 58    , 2    ), // iPhone4 / iPad3 or later
    ICON_29x3 ( "Icon-Small@3x.png"    , 87    , 87    , 3    ), // iPhone6 Plus
    ICON_40   ( "Icon-Small-40.png"    , 40    , 40    , 1    ), // iPad2 / iPad mini
    ICON_40x2 ( "Icon-Small-40@2x.png" , 80    , 80    , 2    ), // iPhone4 / iPad3 or later
    ICON_40x3 ( "Icon-Small-40@3x.png" , 120   , 120   , 3    ), // iPhone6 Plus
    ICON_50   ( "Icon-Small-50.png"    , 50    , 50    , 1    ), // iPad / iPad2 / iPad mini
    ICON_50x2 ( "Icon-Small-50@2x.png" , 100   , 100   , 2    ), // iPad3 or later
    ICON_57   ( "Icon.png"             , 57    , 57    , 1    ), // iPhone 3G / 3GS
    ICON_57x2 ( "Icon@2x.png"          , 114   , 114   , 2    ), // iPhone4 or later
//  ICON_60   ( "Icon-60.png"          , 60    , 60    , 1    ), // (not exists)
    ICON_60x2 ( "Icon-60@2x.png"       , 120   , 120   , 2    ), // iPhone4 or later
    ICON_60x3 ( "Icon-60@3x.png"       , 180   , 180   , 3    ), // iPhone6 Plus
    ICON_72   ( "Icon-72.png"          , 72    , 72    , 1    ), // iPad / iPad2 / iPad mini
    ICON_72x2 ( "Icon-72@2x.png"       , 144   , 144   , 2    ), // iPad3 or later
    ICON_76   ( "Icon-76.png"          , 76    , 76    , 1    ), // iPad / iPad2 / iPad mini
    ICON_76x2 ( "Icon-76@2x.png"       , 152   , 152   , 2    ), // iPad3 or later
	;

	private String filename;
	private Dimension size;
	private int scale;
	private IOSIconInfo(String filename, int width, int height, int scale) {
		this.filename = filename;
		this.size = new Dimension(width, height);
		this.scale = scale;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return this.size; }
	//@Override public boolean isRetina() { return this.retina; }
	@Override public int getScale() { return this.scale; }
}
