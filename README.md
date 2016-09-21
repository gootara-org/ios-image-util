ios-image-util
==============

OVERVIEW:
---------
This tiny Java application generate all size of icon & launch image files with PNG format for various version of iOS.  
Support iPhone, iPad and optionally supports Apple Watch, CarPlay, Mac.

![alt text](https://raw.github.com/gootara-org/ios-image-util/master/ios-image-util/docs/screen.png "GUI")

To generate images, just drop original icon PNG file (1024 x 1024 pixels recommended) to the window and press "Generate" button.  
Contents.json and scaled images going to be generated with appropriate filename.  
For compatibility maintenance, iOS 6 icon can specify separately, but usually does not need to set for it.  
Also icon and launch images especially for iPad can be specified. (If not set, same images as iPhone will be used.)

Optionally, Apple Watch, CarPlay and Mac icon could set separately, too.
These are not requirement. You should set these images only when you want to generate.  
*Icon for Apple Watch generate as 24 bits RGB PNG, without alpha channel.

The launch images are also optional.
Square PNG file 2048 x 2048 pixels recommended.  
The square shape is not a requirement, but it's easy way to generate universal launch images.

![alt text](https://raw.github.com/gootara-org/ios-image-util/master/ios-image-util/docs/settings.png "Settings")

Each settings can be specified by using command line options.
This means generating process can be automated by using command line options with no gui.

You can store settings to XML property file and load next time.
Individual command-line options are stronger than XML property file.  
From Ver 2.0, last settings will be restored automatically at the next startup.
Settings stored to 'ios-image-util.properties' on window closing,
this means the last settings will not be restored when executing by jar file from other directories.



Extra feature, generating @3x, @2x, @1x images from one PNG file.
(drop/select multiple files are supported.)

![alt text](https://raw.github.com/gootara-org/ios-image-util/master/ios-image-util/docs/splitter.png "Splitter")

From Ver 2.0, Image Set with Size Class is supported.

![alt text](https://raw.github.com/gootara-org/ios-image-util/master/ios-image-util/docs/splitter2.png "Splitter")

With 'Generate with Image Set' option, Contents.json going to be generated with scaled images.

*This application will not support "Brand Assets". (In other words, Apple TV icons are not supported.)
 Also will not support "Memory" and "Graphics" properties for Image Set.
 These are available with Xcode 7 or later.


OUTPUT:
-------
| Type        | iOS Ver | Filename                       | Width  | Height  |
| ----------- | ------- | ------------------------------ | ------ | ------- |
| Icon        |  ~6.x   | Icon.png                       |    57  |     57  |
|             |  ~6.x   | Icon@2x.png                    |   114  |    114  |
|             |  ~7.x~  | Icon-Small.png                 |    29  |     29  |
|             |  ~7.x~  | Icon-Small@2x.png              |    58  |     58  |
|             |   8.x~  | Icon-Small@3x.png              |    87  |     87  |
|             |   7.x~  | Icon-Small-40.png              |    40  |     40  |
|             |   7.x~  | Icon-Small-40@2x.png           |    80  |     80  |
|             |   8.x~  | Icon-Small-40@3x.png           |   120  |    120  |
|             |  ~6.x   | Icon-Small-50.png              |    50  |     50  |
|             |  ~6,x   | Icon-Small-50@2x.png           |   100  |    100  |
|             |   7.x~  | Icon-60@2x.png                 |   120  |    120  |
|             |   8.x~  | Icon-60@3x.png                 |   180  |    180  |
|             |  ~6.x   | Icon-72.png                    |    72  |     72  |
|             |  ~6.x   | Icon-72@2x.png                 |   144  |    144  |
|             |   7.x~  | Icon-76.png                    |    76  |     76  |
|             |   7.x~  | Icon-76@2x.png                 |   152  |    152  |
|             |   9.x~  | Icon-83.5@2x.png               |   167  |    167  |
|(Notification|   7.x~  | 20x20.png                      |    20  |     20  |
|             |   7.x~  | 20x20@2x.png                   |    40  |     40  |
|             |   7.x~  | 20x20@3x.png                   |    60  |     60  |
|(Apple Watch)|   8.x~  | 24x24@2x.png                   |    48  |     48  |
|             |   8.x~  | 27.5x27.5@2x.png               |    55  |     55  |
|             |   8.x~  | 29x29@2x.png                   |    58  |     58  |
|             |   8.x~  | 29x29@3x.png                   |    87  |     87  |
|             |   8.x~  | 40x40@2x.png                   |    80  |     80  |
|             |   8.x~  | 44x44@2x.png                   |    88  |     88  |
|             |   8.x~  | 86x86@2x.png                   |   172  |    172  |
|             |   8.x~  | 98x98@2x.png                   |   196  |    196  |
|  (CarPlay)  |   8.x   | 120x120@1x.png                 |   120  |    120  |
|             |   9.x~  | 60x60@2x.png                   |   120  |    120  |
|             |   9.x~  | 60x60@3x.png                   |   180  |    180  |
| Launch      |  ~6.x   | Default.png                    |   320  |    480  |
|             |  ~7.x~  | Default@2x.png                 |   640  |    960  |
|             |  ~7.x~  | Default-568h@2x.png            |   640  |   1136  |
|             |  ~8.x~  | Default-667h@2x.png            |   750  |   1334  |
|             |  ~8.x~  | Default-736h@3x.png            |  1242  |   2208  |
|             |  ~8.x~  | Default-1366h@2x.png           |  2048  |   2732  |
|             |  ~8.x~  | Default-Laundscape-736h@3x.png |  2208  |   1242  |
|             |  ~7.x~  | Default-Portrait~ipad.png      |   768  |   1024  |
|             |  ~7.x~  | Default-Portrait@2x~ipad.png   |  1536  |   2048  |
|             |  ~7.x~  | Default-Landscape~ipad.png     |  1024  |    768  |
|             |  ~7.x~  | Default-Landscape@2x~ipad.png  |  2048  |   1536  |
| (optional)  |  ~6.x   | Default-Portrait.png           |   768  |   1004  |
|             |  ~6.x   | Default-Portrait@2x.png        |  1536  |   2008  |
|             |  ~6.x   | Default-Landscape.png          |  1024  |    748  |
|             |  ~6.x   | Default-Landscape@2x.png       |  2048  |   1496  |
| (Apple TV)  |   9.x~  | Default-Landscape-Tv.png       |  1920  |   1080  |
| Artwork     |  -      | iTunesArtwork                  |   512  |    512  |
|             |  -      | iTunesArtwork@2x               |  1024  |   1024  |



PREREQUISITES:
--------------
- JDK 1.6 or later



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
    -props                      property file location (full path)
    -n, -new                    clear previous settings on startup (gui only)
    -r, -reset                  force reset all properties (gui only)

    -icon6 "icon png path"      iOS 6 icon png file location (full path)
    -watch "icon png path"      Apple Watch icon png file location (full path)
    -carplay "icon png path"    CarPlay icon png file location (full path)
    -mac "icon png path"        Mac icon png file loaction (full path)
    -iconipad "icon png path"   iPad icon png file loaction (full path)
    -icon7 "icon png path"      iOS 7 icon png file loaction (full path)
    -launch "launch image path" launch image png file location (full path)
    -launchipad "image path"    iPad launch image png file location (full path)
    -output "output directory"  output directory location (full path)
    -iphoneonly                 output iPhone images only (default both)
    -ipadonly                   output iPad images only (default both)
    -to-status-bar              generate 'to-status-bar' launch images
    -noasset                    not generate images as asset catalogs
    -noartwork                  not generate ArtWork images for Store
    -noprerender                not generate as pre-rendered
    -noclean                    not clean target files before generate
    -minver [0,7,8]             minimum version to generate (default: 0)
                                  0: All versions
                                  7: iOS 7 and Later
                                  8: iOS 8 and Later
    -lscale [0-5]               launch image scaling (default: 4)
                                  0: no resizing (iPhone only)
                                  1: no resizing (iPhone & iPad)
                                  2: fit to screen height (iPhone only)
                                  3: fit to screen
                                  4: fill screen (prefer long side)
                                  5: fill screen (no aspect ratio)
    -lbgcolor [RGB|ARGB]        '000000' black, '00FFFFFF' white 100% transparent
    -imagetype [0-13]           choose image type (@see BufferedImage)
    For Image Set:
    -sp3x                       deprecated. Use '-spSize 100%:100%' instead
    -spSize width:height        generate @3x, @2x, @1x with @1x(px)|@3x(%) size
    -spNoReplace                not overwrite if file already exists
    -spNoClean                  not clean target files before generate
    -spBgcolor                  background color.
    -spScaling                  scaling type (default: 3)
                                  0: no resizing
                                  1: fit to short side
                                  2: fit to long side
                                  3: fit with aspect ratio
                                  4: fill without aspect ratio
    -spDir "path"               Output directory. (relative or absolute)
    -spFile "png path"          image sets png file location (full path)
                    multiple files available. (Separate by system path separator)
                    -spFile option is available with batch mode only.



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
- Supports output with "Asset Catalogs" format.
- Change word: "Splash" -> "Launch"

2014/02/24
- Supports command line options.
  Each setting can be specified by using command line options with gui.
  Also the generate process can be automated by using command line options with no gui.

2014/02/26
- Refactoring and performance improvement.
- Add launch image scaling option 'fill screen (prefer long side)' and 'fill screen (no aspect ratio)'.

2014/03/08
- Add launch image background color option.
- Add output image type option.
- Update documents.

2014/09/11
- Supports iPhone 6 and iPhone 6 Plus.
- Apple will no longer supports Launch Images in the near future, maybe.

2014/09/23
- Supports Launch Image Retina HD 4.7 and Retina HD 5.5 (both portrait and landscape)

2014/09/28
- Simplified User Interface. Just add a surface, but it looks little bit modern, isn't it? :p
- To use SwingWorker thread with generating images.

2014/10/05
- Fix problem when maximized.
- Fix build.xml for ant.
- Add image splitter. Generate @3x, @2x, @1x images from one png file.

2014/11/13
- Fix readme.
- Set maximum thumbnail size.
- Add out of memory error message.

2015/03/28
- Clear images by click.
- Refresh images when edit path text directly.

2015/04/29
- Supports Apple Watch and CarPlay icons.
- Choose icon images by pane clicking.
- Add "Do not generate ArtWork" option.
- Add some tooltips.
- Use anti-aliasing font on Windows platform in Japanese by default. (Coz default Japanese font is awful.)
- Some bug fixed.

2015/06/03
- To generate images by parallel processing.
- Supports to specify output subdirectory with the image set generator.
- Supports to drop multiple files to the image set generator. (gui mode only)
- Enable drag & drop on linux.

2015/06/07
- The XML property file is supported. (Other command-line options are stronger than property file)
- Supports to choose multiple files to the image set generator. (batch mode also)
- Apply same fix with the parallel processing.
- Add menu bar.

2015/07/19 - Version 2.0.0.0
- Supports Image Sets with Size Class.
- Restore last settings at the next startup.
- Update checker is implemented. (very easy one)
- And more.

2015/09/10 - 2.7.1.0
- Support part of Xcode 7.1 beta.
- Support Apple TV launch image. (Launch image only, not support icons)
- Support new CarPlay Icons (60x60x2, 60x60x3)
- Bug fix.
- This application will not support "Brand Assets".

2015/09/19 - 2.7.1.1
- Fix abnormal focus problem with dropping files on mac.

2016/01/24 - 2.7.2.0
- Support iPad Pro icon.

2016/01/24 - 2.7.2.1
- Selected scaling algorithm and image type applies to preview.
- Support to open output directory by selecting menu item or clicking label.
- Fix some bugs.

2016/02/04 - 2.7.2.2
- Add png sufix to iTunesArtwork images and force no alpha channel.

2016/09/22 - 2.8.0.0
- Add Notification Icons and 1366h Launch Image (for Xcode 8.0)
