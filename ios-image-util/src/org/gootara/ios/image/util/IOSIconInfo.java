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
	//           filename           width   height  scale      description
	ICON_20   ( "20x20.png"       , 20    , 20    , SCALE.x1    ), // iPad Notifications
	ICON_20x2 ( "20x20@2x.png"    , 40    , 40    , SCALE.x2    ), // iPhone/iPad Notifications
	ICON_20x3 ( "20x20@3x.png"    , 60    , 60    , SCALE.x3    ), // iPhone Notifications
	ICON_29   ( "Small.png"       , 29    , 29    , SCALE.x1    ), // iPhone3G / 3GS / iPad / iPad2 / iPad mini
	ICON_29x2 ( "Small@2x.png"    , 58    , 58    , SCALE.x2    ), // iPhone4 / iPad3 or later
	ICON_29x3 ( "Small@3x.png"    , 87    , 87    , SCALE.x3    ), // iPhone6 Plus
	ICON_40   ( "Small-40.png"    , 40    , 40    , SCALE.x1    ), // iPad2 / iPad mini
	ICON_40x2 ( "Small-40@2x.png" , 80    , 80    , SCALE.x2    ), // iPhone4 / iPad3 or later
	ICON_40x3 ( "Small-40@3x.png" , 120   , 120   , SCALE.x3    ), // iPhone6 Plus
	ICON_50   ( "Small-50.png"    , 50    , 50    , SCALE.x1    ), // iPad / iPad2 / iPad mini
	ICON_50x2 ( "Small-50@2x.png" , 100   , 100   , SCALE.x2    ), // iPad3 or later
	ICON_57   ( "57.png"          , 57    , 57    , SCALE.x1    ), // iPhone 3G / 3GS
	ICON_57x2 ( "57@2x.png"       , 114   , 114   , SCALE.x2    ), // iPhone4 or later
//	ICON_60   ( "60.png"          , 60    , 60    , SCALE.x1    ), // (not exists)
	ICON_60x2 ( "60@2x.png"       , 120   , 120   , SCALE.x2    ), // iPhone4 or later
	ICON_60x3 ( "60@3x.png"       , 180   , 180   , SCALE.x3    ), // iPhone6 Plus
	ICON_72   ( "72.png"          , 72    , 72    , SCALE.x1    ), // iPad / iPad2 / iPad mini
	ICON_72x2 ( "72@2x.png"       , 144   , 144   , SCALE.x2    ), // iPad3 or later
	ICON_76   ( "76.png"          , 76    , 76    , SCALE.x1    ), // iPad / iPad2 / iPad mini
	ICON_76x2 ( "76@2x.png"       , 152   , 152   , SCALE.x2    ), // iPad3 or later
	ICON_83_5x2 ( "83.5@2x.png"       , 167   , 167   , SCALE.x2    ), // iPad Pro

	ICON_120x120 ( "120x120@1x.png"    , 120   , 120   , SCALE.x1    ), // CarPlay
	ICON_60x60x2 ( "60x60@2x.png"      , 120   , 120   , SCALE.x2    ), // CarPlay
	ICON_60x60x3 ( "60x60@3x.png"      , 180   , 180   , SCALE.x3    ), // CarPlay

	ICON_24x24x2     ( "24x24@2x.png"    , 48  , 48    , SCALE.x2    ), // Apple Watch 38mm Nofitication Center
	ICON_27_5x27_5x2 ( "27.5x27.5@2x.png", 55  , 55    , SCALE.x2    ), // Apple Watch 42mm Nofitication Center
	ICON_29x29x2     ( "29x29@2x.png"    , 58  , 58    , SCALE.x2    ), // Apple Watch 2x CompanionSettings
	ICON_29x29x3     ( "29x29@3x.png"    , 87  , 87    , SCALE.x3    ), // Apple Watch 3x CompanionSettings
	ICON_40x40x2     ( "40x40@2x.png"    , 80  , 80    , SCALE.x2    ), // Apple Watch 38mm LongLook & App Launcher (both)
	ICON_44x44x2     ( "44x44@2x.png"    , 88  , 88    , SCALE.x2    ), // Apple Watch 42mm LongLook
	ICON_46x46x2     ( "46x46@2x.png"    , 92  , 92    , SCALE.x2    ), // Apple Watch 41mm LongLook 
	ICON_50x50x2     ( "50x50@2x.png"    , 100  , 100    , SCALE.x2    ), // Apple Watch 44mm LongLook
	ICON_51x51x2     ( "51x51@2x.png"    , 102  , 102    , SCALE.x2    ), // Apple Watch 45mm LongLook

	ICON_86x86x2     ( "86x86@2x.png"    , 172 , 172   , SCALE.x2    ), // Apple Watch 38mm QuickLook
	ICON_98x98x2     ( "98x98@2x.png"    , 196 , 196   , SCALE.x2    ), // Apple Watch 42mm QUickLook
	ICON_108x108x2     ( "108x108@2x.png"    , 216 , 216   , SCALE.x2    ), // Apple Watch 44mm QUickLook
	ICON_117x117x2     ( "117x117@2x.png"    , 234 , 234   , SCALE.x2    ), // Apple Watch 45mm QUickLook

	ICON_16x16x1   ( "16x16.png"     , 16  , 16  , SCALE.x1), // Mac
	ICON_16x16x2   ( "16x16@2x.png"  , 32  , 32  , SCALE.x2), // Mac retina
	ICON_32x32x1   ( "32x32.png"     , 32  , 32  , SCALE.x1), // Mac
	ICON_32x32x2   ( "32x32@2x.png"  , 64  , 64  , SCALE.x2), // Mac retina
	ICON_128x128x1 ( "128x128.png"   , 128 , 128 , SCALE.x1), // Mac
	ICON_128x128x2 ( "128x128@2x.png", 256 , 256 , SCALE.x2), // Mac retina
	ICON_256x256x1 ( "256x256.png"   , 256 , 256 , SCALE.x1), // Mac
	ICON_256x256x2 ( "256x256@2x.png", 512 , 512 , SCALE.x2), // Mac retina
	ICON_512x512x1 ( "512x512.png"   , 512 , 512 , SCALE.x1), // Mac
	ICON_512x512x2 ( "512x512@2x.png", 1024, 1024, SCALE.x2), // Mac retina

	ICON_1024x1024x1 ( "1024x1024.png", 1024, 1024, SCALE.x1), // Marketing
	;

	private String filename;
	private Dimension size;
	private SCALE scale;
	private IOSIconInfo(String filename, double width, double height, SCALE scale) {
		this.filename = filename;
		this.size = new Dimension();
		this.size.setSize(width, height);
		this.scale = scale;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return this.size; }
	@Override public SCALE getScale() { return this.scale; }
}
