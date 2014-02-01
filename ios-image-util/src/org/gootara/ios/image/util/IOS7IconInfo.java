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
public enum IOS7IconInfo implements IOSImageInfo {
    //                 filename                width    height  iPhone  iPad    retina   description
//  ICON_60(          "Icon-60.png"           , 60    , 60    , false , false , false , "-"),
    ICON_60x2(        "Icon-60@2x.png"        , 120   , 120   , true  , false , true  , "iPhone4 or later"),
    ICON_76(          "Icon-76.png"           , 76    , 76    , false , true  , false , "iPad / iPad2 / iPad mini"),
    ICON_76x2(        "Icon-76@2x.png"        , 152   , 152   , false , true  , true  , "iPad3 or later"),
    ICON_40(          "Icon-Small-40.png"     , 40    , 40    , false , true  , false , "iPad2 / iPad mini"),
    ICON_40x2(        "Icon-Small-40@2x.png"  , 80    , 80    , true  , true  , true  , "iPhone4 / iPad3 or later"),
    ITUNES_ARTWORK(   "iTunesArtwork"         , 512   , 512   , true  , true  , false , "-"),
    ITUNES_ARTWORKx2( "iTunesArtwork@2x"      , 1024  , 1024  , true  , true  , true  , "-"),
	;

	private String filename;
	private Dimension size;
	private boolean iphoneImage;
	private boolean ipadImage;
	private boolean retina;
	private String description;
	private IOS7IconInfo(String filename, int width, int height, boolean iphoneImage, boolean ipadImage, boolean retina, String description) {
		this.filename = filename;
		this.size = new Dimension(width, height);
		this.iphoneImage = iphoneImage;
		this.ipadImage = ipadImage;
		this.description = description;
		this.retina = retina;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return this.size; }
	@Override public boolean isIphoneImage() { return this.iphoneImage; }
	@Override public boolean isIpadImage() { return this.ipadImage; }
	@Override public boolean isRetina() { return this.retina; }
	@Override public String getDescription() { return this.description; }
}
