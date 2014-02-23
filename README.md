ios-image-util
==============

OVERVIEW:
---------
This tiny java application generate all size of icon & launch image png files those are required for iOS7, iOS6 or prior.

Only two icon files are needed(one for iOS7, another for iOS6 or prior), or just one icon file for all version of iOS.(if you do not care the corner of icon which will be slightly shifted. Only one icon file will be enough in most cases, maybe.)

And better prepare a square png (1536 x 1536 pixels) for the launch images. The image will be put in the center of each size of launch images with the color of upper-left point as a background-color. (A square is not a requirement, but it's easy way to make universal launch images.)

Also Support output with "Asset Catalogs" format.

Each setting can be specified by using command line options with gui.
Further the generate process can be automated by using command line options with no gui.

<img src="https://github.com/gootara-org/ios-image-util/blob/master/ios-image-util/docs/screen.png?raw=true" />

Generate files below.
<table>
	<tr>
		<th>Version</th>
		<th>Type</th>
		<th>Filename</th>
		<th>Width</th>
		<th>Height</th>
		<th>Description</th>
	</tr>
	<tr>
		<td rowspan="8">iOS6 or lower</td>
		<td rowspan="8">Icon</td>
		<td>Icon.png</td>
		<td align="right">57</td>
		<td align="right">57</td>
		<td>iPhone 3G / 3GS</td>
	</tr>
	<tr>
		<td>Icon@2x.png</td>
		<td align="right">114</td>
		<td align="right">114</td>
		<td>iPhone4 or later</td>
	</tr>
	<tr>
		<td>Icon-72.png</td>
		<td align="right">72</td>
		<td align="right">72</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Icon-72@2x.png</td>
		<td align="right">144</td>
		<td align="right">144</td>
		<td>iPad3 or later</td>
	</tr>
	<tr>
		<td>Icon-Small.png</td>
		<td align="right">29</td>
		<td align="right">29</td>
		<td>iPhone3G / 3GS / iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Icon-Small@2x.png</td>
		<td align="right">58</td>
		<td align="right">58</td>
		<td>iPhone4 / iPad3 or later</td>
	</tr>
	<tr>
		<td>Icon-Small-50.png</td>
		<td align="right">50</td>
		<td align="right">50</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Icon-Small-50@2x.png</td>
		<td align="right">100</td>
		<td align="right">100</td>
		<td>iPad3 or later</td>
	</tr>
	<tr>
		<td rowspan="7">iOS7 or upper</td>
		<td rowspan="7">Icon</td>
		<td>Icon-60@2x.png</td>
		<td align="right">120</td>
		<td align="right">120</td>
		<td>iPhone4 or later</td>
	</tr>
	<tr>
		<td>Icon-76.png</td>
		<td align="right">76</td>
		<td align="right">76</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Icon-76@2x.png</td>
		<td align="right">152</td>
		<td align="right">152</td>
		<td>iPad3 or later</td>
	</tr>
	<tr>
		<td>Icon-Small-40.png</td>
		<td align="right">40</td>
		<td align="right">40</td>
		<td>iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Icon-Small-40@2x.png</td>
		<td align="right">80</td>
		<td align="right">80</td>
		<td>iPhone4 / iPad3 or later</td>
	</tr>
	<tr>
		<td>iTunesArtwork</td>
		<td align="right">512</td>
		<td align="right">512</td>
		<td>iTunes Artwork</td>
	</tr>
	<tr>
		<td>iTunesArtwork@2x</td>
		<td align="right">1024</td>
		<td align="right">1024</td>
		<td>iTunes Artwork(Retina)</td>
	</tr>
	<tr>
		<td rowspan="11">Launch</td>
		<td rowspan="7">(default)</td>
		<td>Default.png</td>
		<td align="right">320</td>
		<td align="right">480</td>
		<td>iPhone 3G / 3GS</td>
	</tr>
	<tr>
		<td>Default@2x.png</td>
		<td align="right">640</td>
		<td align="right">960</td>
		<td>iPhone4 / 4S</td>
	</tr>
	<tr>
		<td>Default-568h@2x.png</td>
		<td align="right">640</td>
		<td align="right">1136</td>
		<td>iPhone 5 or later</td>
	</tr>
	<tr>
		<td>Default-Portrait~ipad.png</td>
		<td align="right">768</td>
		<td align="right">1024</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Default-Portrait@2x~ipad.png</td>
		<td align="right">1536</td>
		<td align="right">2048</td>
		<td>iPad 3 or later</td>
	</tr>
	<tr>
		<td>Default-Landscape~ipad.png</td>
		<td align="right">1024</td>
		<td align="right">768</td>
		<td>iPad 3 or later</td>
	</tr>
	<tr>
		<td>Default-Landscape@2x~ipad.png</td>
		<td align="right">2048</td>
		<td align="right">1536</td>
		<td>iPad 3 or later</td>
	</tr>
	<tr>
		<td rowspan="4">(optional)</td>
		<td>Default-Portrait.png</td>
		<td align="right">768</td>
		<td align="right">1004</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Default-Portrait@2x.png</td>
		<td align="right">1536</td>
		<td align="right">2008</td>
		<td>iPad 3 or later</td>
	</tr>
	<tr>
		<td>Default-Landscape.png</td>
		<td align="right">1024</td>
		<td align="right">748</td>
		<td>iPad / iPad2 / iPad mini</td>
	</tr>
	<tr>
		<td>Default-Landscape@2x.png</td>
		<td align="right">2048</td>
		<td align="right">1496</td>
		<td>iPad 3 or later</td>
	</tr>
</table>



PREREQUISITES:
--------------
- JDK 1.6 or higher

RECOMMENDED:
------------
- You can compile this code with pure JDK or Eclipse or Ant or Maven. (Any way you want, this code does not depend on any external libraries.)

- Eclipse Java Development Tools (Any version maybe. This code does not depend on any external libraries.)

- Ant (Any version maybe. I tested with 1.9.3. Move to the directory same as build.xml on your console and just type 'ant'.)

- Maven (Any version maybe. I tested with 3.1.1. Move to the directory same as pom.xml on your console and just type 'mvn clean compile jar:jar'.)


COMMAND LINE:
-------------
- java -jar ios-image-util.jar

  or run with main-class.

- java -classpath ios-image-util.jar org.gootara.ios.image.util.IOSImageUtil

COMMAND LINE OPTIONS:
---------------------
    -h, -help                   show this help message and exit
    -b, -batch                  run as batch mode (no gui)
    -v, -verbose                verbose mode (available with batch mode only)
    -silent                     no log (available with batch mode only)
    -icon6 [icon png path]      iOS 6 icon png file location (full path)
    -icon7 [icon png path]      iOS 7 icon png file loaction (full path)
    -launch [launch image path] launch image png file location (full path)
    -output [output directory]  output directory location (full path)
    -iphoneonly                 output iPhone images only (default all)
    -ipadonly                   output iPad images only (default all)
    -to-status-bar              generate 'to-status-bar' launch images
    -asset                      generate images as asset catalogs
    -lscale [0-3]               launch image scaling
                                  0: no resizing (iPhone only)
                                  1: no resizing (iPhone & iPad)
                                  2: fit to the screen height (default)
                                  3: fit to the screen

CHANGE LOG:
-----------
2014/01/15
- Change output filenames suitable for the xcode 5.
- "Generate old size of Splash images for iPad" checkbox was added.(optional)

2014/01/29
- Add new output option. iPhone only, iPad only or both.
- "Fit iPhone's Splash images to the screen" checkbox was added.
- Change optional output filenames.

2014/02/01
- Add two splash scaling options. (No resizing)

2014/02/14
- Add build.xml for Ant and pom.xml for Maven.

2014/02/22
- Support output with "Asset Catalogs" format.
- Change word: "Splash" -> "Launch"

2014/02/24
- Support command line options.
  Each setting can be specified by using command line options with gui.
  Also the generate process can be automated by using command line options with no gui.

