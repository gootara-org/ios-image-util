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
public enum IOSIconInfo implements IOSImageInfo {
    //                 filename                width    height  retina      description
    ICON_29(          "Icon-Small.png"        , 29    , 29    , false ), // iPhone3G / 3GS / iPad / iPad2 / iPad mini
    ICON_29x2(        "Icon-Small@2x.png"     , 58    , 58    , true  ), // iPhone4 / iPad3 or later
    ICON_40(          "Icon-Small-40.png"     , 40    , 40    , false ), // iPad2 / iPad mini
    ICON_40x2(        "Icon-Small-40@2x.png"  , 80    , 80    , true  ), // iPhone4 / iPad3 or later
    ICON_50(          "Icon-Small-50.png"     , 50    , 50    , false ), // iPad / iPad2 / iPad mini
    ICON_50x2(        "Icon-Small-50@2x.png"  , 100   , 100   , true  ), // iPad3 or later
    ICON_57(          "Icon.png"              , 57    , 57    , false ), // iPhone 3G / 3GS
    ICON_57x2(        "Icon@2x.png"           , 114   , 114   , true  ), // iPhone4 or later
//  ICON_60(          "Icon-60.png"           , 60    , 60    , false ), // (not exists)
    ICON_60x2(        "Icon-60@2x.png"        , 120   , 120   , true  ), // iPhone4 or later
    ICON_72(          "Icon-72.png"           , 72    , 72    , false ), // iPad / iPad2 / iPad mini
    ICON_72x2(        "Icon-72@2x.png"        , 144   , 144   , true  ), // iPad3 or later
    ICON_76(          "Icon-76.png"           , 76    , 76    , false ), // iPad / iPad2 / iPad mini
    ICON_76x2(        "Icon-76@2x.png"        , 152   , 152   , true  ), // iPad3 or later
	;

	private String filename;
	private Dimension size;
	private boolean retina;
	private IOSIconInfo(String filename, int width, int height, boolean retina) {
		this.filename = filename;
		this.size = new Dimension(width, height);
		this.retina = retina;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return this.size; }
	@Override public boolean isRetina() { return this.retina; }
}
