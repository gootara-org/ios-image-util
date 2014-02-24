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
 * The informations of artwork for iOS.
 *
 * @author gootara.org
 * @see org.gootara.ios.image.util.IOSImageInfo
 */
public enum IOSArtworkInfo implements IOSImageInfo {
    //                 filename                width    height  retina      description
    ITUNES_ARTWORK(   "iTunesArtwork"         , 512   , 512   , false ), // -
    ITUNES_ARTWORKx2( "iTunesArtwork@2x"      , 1024  , 1024  , true  ), // -
	;

	private String filename;
	private Dimension size;
	private boolean retina;
	private IOSArtworkInfo(String filename, int width, int height, boolean retina) {
		this.filename = filename;
		this.size = new Dimension(width, height);
		this.retina = retina;
	}
	@Override public String getFilename() { return this.filename; }
	@Override public Dimension getSize() { return this.size; }
	@Override public boolean isRetina() { return this.retina; }
}
