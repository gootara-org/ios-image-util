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

import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.gootara.ios.image.util.ui.MainFrame;

/**
 * The main class of IOSImageUtil.
 *
 * @author gootara.org
 */
public class IOSImageUtil {

	public static void main(String[] args) {
		try {
			// Use anti-aliasing font on Windows platform in Japanese by default.
			System.setProperty("awt.useSystemAAFontSettings", "lcd"); // Maybe not effective. Don't care.
			ResourceBundle resource = ResourceBundle.getBundle("application");
			if (resource.containsKey("font.default.name")) {
				UIDefaults uiDefaults = UIManager.getLookAndFeelDefaults();
				for (Enumeration<Object> enu = uiDefaults.keys(); enu.hasMoreElements();) {
					String key = enu.nextElement().toString();
					Font font;
					if (key.toLowerCase().endsWith("font") && (font = uiDefaults.getFont(key)) != null) {
						uiDefaults.put(key, new Font(resource.getString("font.default.name"), font.getStyle(), font.getSize()));
					}
				}
			}

			long l1 = System.currentTimeMillis();
			MainFrame mainFrame = new MainFrame();
			mainFrame.setSize(640, 555);
			mainFrame.setLocationByPlatform(true);

			if (args.length > 0) {
				if (!initialize(mainFrame, args)) {
					if (mainFrame.isBatchMode()) {
						System.exit(1);
					}
				}
			}

			if (mainFrame.isBatchMode()) {
				int exitCode = 0;
				if (!mainFrame.isSilentMode()) {
					System.out.println(String.format("START: %s", (new java.util.Date(l1)).toString()));
					System.out.println(String.format("  Initializing takes %.2f secs.", ((double)(System.currentTimeMillis() - l1) / 1000d)));
					System.out.println();
				}
				if (mainFrame.isGenerateImagesReqested() && !mainFrame.generate()) {
					exitCode = 1;
					usage();
				}
				if (mainFrame.isSplitImageRequested() && !mainFrame.batchSplit()) {
					exitCode = 1;
					usage();
				}
				long l2 = System.currentTimeMillis();
				if (!mainFrame.isSilentMode()) {
					System.out.println(String.format("FINISH: %s", (new java.util.Date(l2)).toString()));
					System.out.println(String.format("  Generate images takes %.2f secs.", ((double)(l2 - l1) / 1000d)));
					System.out.println();
				}
				System.exit(exitCode);
			} else {
				mainFrame.setVisible(true);
			}
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			System.exit(1);
		}
	}

	/**
	 * Initializing.
	 *
	 * @param mainFrame	application's main frame
	 * @param args arguments from command-line
	 * @return true - success / false - failed
	 */
	private static boolean initialize(MainFrame mainFrame, String[] args) {
		String argc = null;
		boolean noerr = true;
		try {

			List<String> options = Arrays.asList("-b", "-batch", "-v", "-verbose", "-noasset", "-h", "-help", "-silent", "-icon6", "-icon7", "-launch", "-output", "-iphoneonly", "-ipadonly", "-to-status-bar", "-lscale", "-algorithm", "-imagetype", "-lbgcolor", "-sp3x", "-spsize", "-spnoreplace", "-spfile", "-spdir", "-noartwork", "-watch", "-carplay", "-props");
			int i;
			for (i = 0; i < args.length; i++) {
				argc = args[i];
				String arg = args[i].toLowerCase();
				if (arg.startsWith("/")) arg = "-".concat(arg.substring(1));

				if (arg.equals("-b") || arg.equals("-batch")) { mainFrame.setBatchMode(true); mainFrame.setOverwriteAlways(true); }
				if (arg.equals("-v") || arg.equals("-verbose")) { mainFrame.setVerboseMode(true); }
				if (arg.equals("-h") || arg.equals("-help")) { usage(); return false; }
			}

			for (i = 0; i < args.length; i++) {
				argc = args[i];
				String arg = args[i].toLowerCase();
				if (arg.startsWith("/")) arg = "-".concat(arg.substring(1));
				if (arg.equals("-props")) { i++; mainFrame.loadProperties(new File(args[i])); }
			}

			for (i = 0; i < args.length; i++) {
				argc = args[i];
				String arg = args[i].toLowerCase();
				if (arg.startsWith("/")) arg = "-".concat(arg.substring(1));
				if (arg.equals("-noasset")) { mainFrame.setGenerateAsAssetCatalogs(false); }
			}

			for (i = 0; i < args.length; i++) {
				argc = args[i];
				String arg = args[i].toLowerCase();
				if (arg.startsWith("/")) arg = "-".concat(arg.substring(1));
				if (!options.contains(arg)) { System.err.println(String.format("Illegal option: %s", arg)); usage(); noerr = false; break; }

				if (arg.equals("-silent")) { mainFrame.setSilentMode(true); }
				if (arg.equals("-icon6")) { i++; if (!mainFrame.setIcon6Path(args[i])) { noerr = false; break; } }
				if (arg.equals("-watch")) { i++; if (!mainFrame.setWatchPath(args[i])) { noerr = false; break; } }
				if (arg.equals("-carplay")) { i++; if (!mainFrame.setCarplayPath(args[i])) { noerr = false; break; } }
				if (arg.equals("-icon7")) { i++; if (!mainFrame.setIcon7Path(args[i])) { noerr = false; break; } }
				if (arg.equals("-launch")) { i++; if (!mainFrame.setSplashPath(args[i])) { noerr = false; break; } }
				if (arg.equals("-output")) { i++; mainFrame.setOutputPath(args[i]); }
				if (arg.equals("-iphoneonly")) { mainFrame.selectIphoneOnly(); }
				if (arg.equals("-ipadonly")) { mainFrame.selectIpadOnly(); }
				if (arg.equals("-to-status-bar")) { mainFrame.setGenerateOldSplashImages(true); }
				if (arg.equals("-lscale")) { i++; mainFrame.setSplashScaling(Integer.parseInt(args[i])); }
				if (arg.equals("-noartwork")) { mainFrame.setGenerateArtwork(false); }
				// hidden option.
				if (arg.equals("-algorithm")) { i++; mainFrame.setScalingAlgorithm(Integer.parseInt(args[i])); }
				if (arg.equals("-imagetype")) { i++; mainFrame.setImageType(Integer.parseInt(args[i])); }
				if (arg.equals("-lbgcolor")) { i++; }
				if (arg.equals("-props")) { i++; }

				// Splitter
				if (arg.equals("-spsize")) { i++; mainFrame.setSized(true); String[] size = args[i].split(":"); mainFrame.setWidth1x(size[0]); mainFrame.setHeight1x(size[1]); }
				if (arg.equals("-spnoreplace")) { mainFrame.setOverwriteAlways(false); }
				if (arg.equals("-spdir")) { i++; mainFrame.setSplitterOutputDirectory(args[i]); }
				if (arg.equals("-spfile")) {
					i++;
					StringTokenizer st = new StringTokenizer(args[i], System.getProperty("path.separator", ":"));
					while (st.hasMoreTokens()) { mainFrame.addSplitTarget(st.nextToken()); }
				}
			}

			for (i = 0; i < args.length; i++) {
				argc = args[i];
				String arg = args[i].toLowerCase();
				if (arg.startsWith("/")) arg = "-".concat(arg.substring(1));

				if (arg.equals("-lbgcolor")) { i++; mainFrame.setSplashBackgroundColor(args[i]); }
				if (arg.equals("-sp3x")) { mainFrame.setAs3x(true); }
			}

		} catch (ArrayIndexOutOfBoundsException ex) {
			System.err.println();
			if (argc != null) System.err.println(String.format("Illegal option: %s", argc));
			System.err.println(String.format("Missing argument. (%s)", ex.getMessage()));
			usage();
			return false;
		} catch (IllegalArgumentException ex) {
			System.err.println();
			if (argc != null) System.err.println(String.format("Illegal option: %s", argc));
			System.err.println(String.format("Illegal argument. (%s)", ex.getMessage()));
			usage();
			return false;
		} catch (Throwable t) {
			System.err.println();
			if (argc != null) System.err.println(String.format("Illegal option: %s", argc));
			t.printStackTrace(System.err);
			usage();
			return false;
		} finally {
			mainFrame.setStorePropertiesRequested(false);
		}

		return noerr;
	}

	/**
	 * Get default directory for property file.
	 *
	 * @param mainFrame
	 * @return
	 */
	public static File getDefaultDirectory(MainFrame mainFrame) {
		try {
			String resourcePath = mainFrame.getClass().getPackage().getName().replace('.', '/').concat("/img/settings.png");
			URL url = mainFrame.getClass().getClassLoader().getResource(resourcePath);
			if (url != null) {
				URI uri = url.toURI();
				if (url.getProtocol().equals("jar")) {
					uri = new URI(url.getFile());
				}
				String path = uri.getSchemeSpecificPart();
				File dir = new File(path.substring(0, path.length() - resourcePath.length()));
				if (url.getProtocol().equals("jar")) {
					dir = dir.getParentFile();
				}
				if (dir.exists() && dir.isDirectory()) {
					return dir;
				}
			}
		} catch (Exception ex) {
			// default properties not supported. ignore it.
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Get system property as int.
	 * This can be specified with java -D option.
	 *
	 * @param key	Property Name
	 * @param defaultValue	Default Value
	 * @return
	 */
	public static int getSystemIntProperty(String key, int defaultValue) {
		return IOSImageUtil.getIntProperty(System.getProperties(), key, defaultValue);
	}

	/**
	 * Get property as int.
	 *
	 * @param props	Properties
	 * @param key	Property Name
	 * @param defaultValue	Default Value
	 * @return
	 */
	public static int getIntProperty(Properties props, String key, int defaultValue) {
		try {
			return Integer.parseInt(props.getProperty(key, Integer.toString(defaultValue)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * Get property as boolean.
	 *
	 * @param props	Properties
	 * @param key	Property Name
	 * @param defaultValue	Default Value
	 * @return
	 */
	public static boolean getBoolProperty(Properties props, String key, boolean defaultValue) {
		if (props.containsKey(key)) {
			return props.getProperty(key).trim().equalsIgnoreCase("true");
		}
		return defaultValue;
	}

	/**
	 * Show usage to System.out.
	 */
	private static void usage() {
		System.out.println();
		System.out.println("Usage: java -jar ios-image-util.jar [options]");
		System.out.println();
		System.out.println("Options:");
		System.out.println("  -h, -help                   show this help message and exit");
		System.out.println("  -b, -batch                  run as batch mode (no gui)");
		System.out.println("  -v, -verbose                verbose mode (available with batch mode only)");
		System.out.println("  -silent                     no log (available with batch mode only)");
		System.out.println("  -props                      property file location (full path)");
		System.out.println();
		System.out.println("  -icon6 \"icon png path\"      iOS 6 icon png file location (full path)");
		System.out.println("  -watch \"icon png path\"      Apple Watch icon png file location (full path)");
		System.out.println("  -carplay \"icon png path\"    CarPlay icon png file location (full path)");
		System.out.println("  -icon7 \"icon png path\"      iOS 7 icon png file loaction (full path)");
		System.out.println("  -launch \"launch image path\" launch image png file location (full path)");
		System.out.println("  -output \"output directory\"  output directory location (full path)");
		System.out.println("  -iphoneonly                 output iPhone images only (default all)");
		System.out.println("  -ipadonly                   output iPad images only (default all)");
		System.out.println("  -to-status-bar              generate 'to-status-bar' launch images");
		System.out.println("  -noasset                    not generate images as asset catalogs");
	    System.out.println("  -noartwork                  not generate ArtWork images for Store");
	    System.out.println("  -lscale [0-5]               launch image scaling (default: 4)");
		System.out.println("                                0: no resizing (iPhone only)");
		System.out.println("                                1: no resizing (iPhone & iPad)");
		System.out.println("                                2: fit to the screen height");
		System.out.println("                                3: fit to the screen");
		System.out.println("                                4: fill screen (prefer long side)");
		System.out.println("                                5: fill screen (no aspect ratio)");
		System.out.println("  -lbgcolor [RGB|ARGB]        '000000' black, '00FFFFFF' white 100% transparent");
		System.out.println("  -imagetype [0-13]           choose image type (@see BufferedImage)");
		System.out.println();
		System.out.println("For Image Set:");
		System.out.println("  -sp3x                       Generate @3x, @2x, @1x images from @3x");
		System.out.println("  -spSize width:height        Generate @3x, @2x, @1x with @1x(px)|@3x(%) size");
		System.out.println("  -spNoReplace                Not overwrite if file already exists.");
		System.out.println("  -spDir \"relative path\"      Output sub directory. (relative to -spFile path)");
		System.out.println("  -spFile \"png path\"          Image set png file location (full path)");
		System.out.println("                    Multiple files available. (Separate by system path separator)");
		System.out.println("                    -spFile option is available with batch mode only.");


		System.out.println();

	}

}
