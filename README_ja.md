ios-image-util
==============

OVERVIEW:
---------
iOS 用アイコン＆起動イメージ PNG を、必要とされる全てのサイズで生成する Java アプリケーションです。
（Apple Watch と、ついでに CarPlay に対応しました）

元となるアイコン画像（1024x1024 推奨）をウィンドウにドロップして、「イメージ生成」ボタンを押すだけで、必要な全てのサイズの PNG を然るべきファイル名で生成します。
（互換維持の為に iOS 6 用のアイコンを個別に指定できるようにしていますが、通常は指定する必要はありません）

Apple Watch と CarPlay 用のアイコンは、それぞれ出力したい場合のみ指定してください（出力したくない場合は、空のままにしてください）。
また、Apple Watch のアイコンは、自動的にアルファ無しの 24 bit PNG として出力されます。

起動イメージ（Launch Images）も、オプションです。
1536×1536 ピクセルの正方形 PNG 画像を元にして、全てのサイズを出力します。
その際、元画像は中央に配置され、背景色は自動的に推測されます（指定することも可能です）。
元画像は正方形でなくても構いませんが、特にユニバーサルな場合は一番楽にそれっぽくなると思います。
起動イメージに関しては、スケーリングオプションもいくつか用意しています。

Asset Catalogs 形式での出力に対応しました。

また、GUI 上の各設定値をコマンドラインオプションで指定できるようにしました。
GUI を表示せずにバッチ実行するようにオプションで指定することも可能ですので、自動化処理に組み込めるかも知れません。

保存した XML プロパティファイルを読み込んで、設定値を一括で指定することも可能です。
プロパティファイルは、メニューかウィンドウの上半分へのドロップ、もしくはコマンドラインオプションで指定して読み込みます。
プロパティファイルで一括指定した設定値よりも、個別のコマンドラインオプションが優先されます。


![alt text](https://github.com/gootara-org/ios-image-util/blob/master/ios-image-util/docs/screen_ja.png?raw=true "GUI")

![alt text](https://github.com/gootara-org/ios-image-util/blob/master/ios-image-util/docs/settings_ja.png?raw=true "Settings")

それから、１つの PNG 画像から @1x、@2x、@3x のイメージを切り分ける機能をおまけで追加しました。
ドロップしたファイルを @3x として切り分けたり、@1x のサイズを指定しての切り分けが可能です（複数ファイルの選択／ドロップに対応しました）。

![alt text](https://github.com/gootara-org/ios-image-util/blob/master/ios-image-util/docs/splitter_ja.png?raw=true "Splitter")



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
|(Apple Watch)|   8.x~  | 24x24@2x.png                   |    48  |     48  |
|             |   8.x~  | 27.5x27.5@2x.png               |    55  |     55  |
|             |   8.x~  | 29x29@2x.png                   |    58  |     58  |
|             |   8.x~  | 29x29@3x.png                   |    87  |     87  |
|             |   8.x~  | 40x40@2x.png                   |    80  |     80  |
|             |   8.x~  | 44x44@2x.png                   |    88  |     88  |
|             |   8.x~  | 86x86@2x.png                   |   172  |    172  |
|             |   8.x~  | 98x98@2x.png                   |   196  |    196  |
|  (CarPlay)  |   8.x~  | 120x120@1x.png                 |   120  |    120  |
| Launch      |  ~6.x   | Default.png                    |   320  |    480  |
|             |  ~7.x~  | Default@2x.png                 |   640  |    960  |
|             |  ~7.x~  | Default-568h@2x.png            |   640  |   1136  |
|             |  ~8.x~  | Default-667h@2x.png            |   750  |   1334  |
|             |  ~8.x~  | Default-736h@3x.png            |  1242  |   2208  |
|             |  ~8.x~  | Default-Laundscape-736h@3x.png |  2208  |   1242  |
|             |  ~7.x~  | Default-Portrait~ipad.png      |   768  |   1024  |
|             |  ~7.x~  | Default-Portrait@2x~ipad.png   |  1536  |   2048  |
|             |  ~7.x~  | Default-Landscape~ipad.png     |  1024  |    768  |
|             |  ~7.x~  | Default-Landscape@2x~ipad.png  |  2048  |   1536  |
| (optional)  |  ~6.x   | Default-Portrait.png           |   768  |   1004  |
|             |  ~6.x   | Default-Portrait@2x.png        |  1536  |   2008  |
|             |  ~6.x   | Default-Landscape.png          |  1024  |    748  |
|             |  ~6.x   | Default-Landscape@2x.png       |  2048  |   1496  |
| Artwork     |  -      | iTunesArtwork                  |   512  |    512  |
|             |  -      | iTunesArtwork@2x               |  1024  |   1024  |



PREREQUISITES:
--------------
- JDK 1.6 以上



RECOMMENDED:
------------
- いかなる外部ライブラリも利用していませんので、コンパイルは JDK のみで行えます。お好みで Eclipse、Ant、Maven 等も利用できます。

- Eclipse Java Development Tools (外部ライブラリを使用していませんので、バージョンはなんでも構いません)

- Ant (バージョンはなんでも構いません。1.9.3で動作確認しました。コンソール上で build.xml と同じディレクトリに移動して、単に「ant」を実行してください)

- Maven (バージョンはなんでも構いません。3.1.1で動作確認しました。コンソール上で pom.xml と同じディレクトリに移動して、単に「mvn clean compile jar:jar」を実行してください)



COMMAND LINE:
-------------
- java -jar ios-image-util.jar

  または、以下のようにメインクラスを指定して実行してください。

- java -classpath ios-image-util.jar org.gootara.ios.image.util.IOSImageUtil



COMMAND LINE OPTIONS:
---------------------
    -h, -help                   show this help message and exit
    -b, -batch                  run as batch mode (no gui)
    -v, -verbose                verbose mode (available with batch mode only)
    -silent                     no log (available with batch mode only)
    -props                      property file location (full path)

    -icon6 "icon png path"      iOS 6 icon png file location (full path)
    -watch "icon png path"      Apple Watch icon png file location (full path)
    -carplay "icon png path"    CarPlay icon png file location (full path)
    -icon7 "icon png path"      iOS 7 icon png file loaction (full path)
    -launch "launch image path" launch image png file location (full path)
    -output "output directory"  output directory location (full path)
    -iphoneonly                 output iPhone images only (default all)
    -ipadonly                   output iPad images only (default all)
    -to-status-bar              generate 'to-status-bar' launch images
    -noasset                    not generate images as asset catalogs
    -noartwork                  not generate ArtWork images for Store
    -lscale [0-5]               launch image scaling (default: 4)
                                  0: no resizing (iPhone only)
                                  1: no resizing (iPhone & iPad)
                                  2: fit to the screen height
                                  3: fit to the screen
                                  4: fill screen (prefer long side)
                                  5: fill screen (no aspect ratio)
    -lbgcolor [RGB|ARGB]        '000000' black, '00FFFFFF' white 100% transparent
    -imagetype [0-13]           choose image type (@see BufferedImage)

    For Image Set:
    -sp3x                       Generate @3x, @2x, @1x images from @3x
    -spSize width:height        Generate @3x, @2x, @1x with @1x(px)|@3x(%) size
    -spNoReplace                Not overwrite if file already exists.
    -spDir "relative path"      Output sub directory. (relative to -spFile path)
    -spFile "png path"          Image set png file location (full path)
                    Multiple files available. (Separate by system path separator)
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
- Support output with "Asset Catalogs" format.
- Change word: "Splash" -> "Launch"

2014/02/24
- Support command line options.
  Each setting can be specified by using command line options with gui.
  Also the generate process can be automated by using command line options with no gui.

2014/02/25
- Refactoring and performance improvement.
- Add launch image scaling option 'fill screen (prefer long side)' and 'fill screen (no aspect ratio)'.

2014/03/08
- Add launch image background color option.
- Add output image type option.
- Update documents.

2014/09/11
- Support iPhone 6 and iPhone 6 Plus.
- Apple will no longer support Launch Images in the near future, maybe.

2014/09/23
- Support Launch Image Retina HD 4.7 and Retina HD 5.5 (both portrait and landscape)

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
- Support Apple Watch and CarPlay icons.
- Choose icon images by pane clicking.
- Add "Do not generate ArtWork" option.
- Add some tooltips.
- Use anti-aliasing font on Windows platform in Japanese by default. (Coz default Japanese font is awful.)
- Some bug fixed.

2015/06/03
- To generate images by parallel processing.
- Support to specify output subdirectory with the image set generator.
- Support to drop multiple files to the image set generator. (gui mode only)
- Enable drag & drop on linux.

2015/06/07
- The XML property file is supported. (Other command-line options are stronger than property file)
- Support to choose multiple files to the image set generator. (batch mode also)
- Apply same fix with the parallel processing.
- Add menu bar.

