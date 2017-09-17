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
package org.gootara.ios.image.util.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gootara.ios.image.util.IOSArtworkInfo;
import org.gootara.ios.image.util.IOSAssetCatalogs;
import org.gootara.ios.image.util.IOSAssetCatalogs.JSON_PROPERTY_KEY;
import org.gootara.ios.image.util.IOSAssetCatalogs.JSON_PROPERTY_VALUE;
import org.gootara.ios.image.util.IOSIconAssetCatalogs;
import org.gootara.ios.image.util.IOSImageInfo;
import org.gootara.ios.image.util.IOSImageUtil;
import org.gootara.ios.image.util.IOSSplashAssetCatalogs;

/**
 * The main window frame of IOSImageUtil.
 *
 * @author gootara.org
 */
public class MainFrame extends JFrame implements AssetImageGenerator {
	public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(640, 584);
	public static final String PLACEHOLDER_SPLASH_BGCOL = "(e.g. ffffff)";
	public static final Color COLOR_DARK_GRAY        = new Color(0x727284);
	public static final Color COLOR_CYAN             = new Color(0x2399CD);
	public static final Color COLOR_WINERED          = new Color(0xcd2344);
	public static final Color COLOR_GRAY             = new Color(0x8E8E93);
	public static final Color COLOR_LIGHT_PINK       = new Color(0xffd2e0);
	public static final Color COLOR_ULTRA_LIGHT_CYAN = new Color(0xe1f2fa);
	public static final Color COLOR_ULTRA_LIGHT_BLUE = new Color(0xe6eeff);
	public static final Color COLOR_ULTRA_LIGHT_PINK = new Color(0xffe6ee);
	public static final Color COLOR_ULTRA_LIGHT_RED  = new Color(0xfae1e6);
	public static final Color BGCOLOR_LIGHT_GRAY = new Color(0xf7f7f7);
	public static final Color BGCOLOR_GRAY       = new Color(0xeeeeee);
	public static final Color BGCOLOR_GENERATE   = new Color(0xFF4981);
	public static final Color BGCOLOR_SETTINGS   = new Color(0x4CD964);
	public static final Color BGCOLOR_SPLIT      = new Color(0x007AFF);

	private ResourceBundle resource;
	private JTextField icon6Path, icon7Path, splashPath, outputPath, splashBackgroundColor, carplayPath, watchPath, macPath, ipadIconPath, ipadLaunchPath;
	private JComboBox scaleAlgorithm, splashScaling, imageType, optionalImages, minimumVersion;
	private ImagePanel icon6Image, icon7Image, splashImage, carplayImage, watchImage, macImage, ipadIconImage, ipadLaunchImage;
	private JProgressBar progress;
	private JCheckBox generateOldSplashImages, generateTvSplash, generateAsAssetCatalogs, generateArtwork, generateAsPrerendered, cleanBeforeGenerate;
	private JToggleButton generateIphone, generateIpad;
	private JButton generateButton, settingsButton, cancelButton, splitButton, forwardButton, backButton;
	private JLabel outputPathDisplay;
	private SimpleShutterAnimation animator;
	private SplitterFrame splitter;
	private LinkedList<File> splitTarget = null;
	private ExecutorService pool;
	private File propertiesFile, selectedDirectory;
	private JMenu helpMenu, historiesMenu;
	private JMenuItem menuItemCheckForUpdates, menuItemExecuteUpdate;
	private JCheckBoxMenuItem menuItemCheckForUpdatesOnStartUp, menuItemSaveChangesOnClose;
	private ActionListener historiesAction;
	private boolean batchMode = false;
	private boolean silentMode = false;
	private boolean verboseMode = false;
	private boolean cancelRequested = false;
	private boolean storePropertiesRequested = false;

	/**
	 * Constructor.
	 */
	protected void frameInit() {
		super.frameInit();
		resource = ResourceBundle.getBundle("application");
		boolean isMacLAF = UIManager.getLookAndFeel().getName().toLowerCase().indexOf("mac") >= 0;
		this.setWindowTitle();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		Font fontNormal = new Font(getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 12);
		Font fontExtraLarge = new Font(getResource("font.default.name", Font.SANS_SERIF), Font.BOLD, 16);
		ArrayList<Image> icons = new ArrayList<Image>();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon16.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon24.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon32.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon48.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon64.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon96.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon128.png")));
		icons.add(toolkit.getImage(this.getClass().getResource("img/icon256.png")));
		this.setIconImages(icons);

		final JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(DEFAULT_WINDOW_SIZE.width, DEFAULT_WINDOW_SIZE.height / 2));
		this.add(layeredPane, BorderLayout.NORTH);

		final JPanel settings = new JPanel();
		settings.setPreferredSize(layeredPane.getPreferredSize());
		layeredPane.add(settings, JLayeredPane.DEFAULT_LAYER);

		ItemListener propertyChangedItemListener = (new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				setStorePropertiesRequested(true);
			}
		});

		// iOS6 Icon Path.
		// -> Optional Icons
		Vector<String> oiItems = new Vector<String>();
		oiItems.add(getResource("item.icon6.path", "Icon for iOS6"));
		oiItems.add(getResource("item.applewatch.path", "Icon for Apple Watch"));
		oiItems.add(getResource("item.carplay.path", "Icon for CarPlay"));
		oiItems.add(getResource("item.mac.path", "Icon for Mac"));
		oiItems.add(getResource("item.ipad.path", "iPad Icon"));
		oiItems.add(getResource("item.ipad.launch.path", "iPad Launch"));
		optionalImages = new JComboBox(oiItems);
		optionalImages.setFont(optionalImages.getFont().deriveFont(Font.PLAIN, 11.0f));
		optionalImages.setToolTipText(getResource("tooltip.optional.icons", "Paths for optional icon. ( if necessary, not required )"));
		optionalImages.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				final JTextField[] textFields = { icon6Path, watchPath, carplayPath, macPath, ipadIconPath, ipadLaunchPath };
				for (int i = 0; i < textFields.length; i++) {
					textFields[i].setVisible(optionalImages.getSelectedIndex() == i);
				}
			}
		});

		JButton icon6PathButton = new JButton("...");
		icon6PathButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(icon6Path, icon6Image);
			}
		});

		JPanel optionalIconBase = new JPanel();
		optionalIconBase.setLayout(new FlowLayout(FlowLayout.CENTER, isMacLAF ? 0 : 2, 0));
		optionalIconBase.add(new JLabel(getResource("label.optional.icons", "Optional Icons:"), SwingConstants.RIGHT));
		optionalIconBase.add(optionalImages);

		settings.add(optionalIconBase);
		settings.add(icon6Path = new JTextField());
		settings.add(icon6PathButton);
		settings.add(carplayPath = new JTextField());
		settings.add(watchPath = new JTextField());
		settings.add(macPath = new JTextField());
		settings.add(ipadIconPath = new JTextField());
		settings.add(ipadLaunchPath = new JTextField());
		optionalImages.setSelectedIndex(1);

		// iOS7 Icon Path.
		JLabel icon7PathLabel = new JLabel(getResource("label.icon7.path", " iOS7 Icon PNG (1024x1024):"), SwingConstants.RIGHT);
		icon7Path = new JTextField();
		JButton icon7PathButton = new JButton("...");
		icon7PathButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(icon7Path, icon7Image);
			}
		});
		settings.add(icon7PathLabel);
		settings.add(icon7Path);
		settings.add(icon7PathButton);

		// Splash Image Path.
		JLabel splashPathLabel = new JLabel(getResource("label.splash.path", "      Splash PNG (2048x2048):"), SwingConstants.RIGHT);
		splashPath = new JTextField();
		JButton splashPathButton = new JButton("...");
		splashPathButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(splashPath, splashImage);
			}
		});
		settings.add(splashPathLabel);
		settings.add(splashPath);
		settings.add(splashPathButton);
		JSeparator separatorNorth = new JSeparator(JSeparator.HORIZONTAL);
		settings.add(separatorNorth);

		// Scaling Algorithm.
		Vector<ComboBoxItem> items = new Vector<ComboBoxItem>();
		items.add(new ComboBoxItem(Image.SCALE_AREA_AVERAGING, "AREA_AVERAGING"));
		items.add(new ComboBoxItem(Image.SCALE_DEFAULT, "DEFAULT"));
		items.add(new ComboBoxItem(Image.SCALE_FAST, "FAST"));
		items.add(new ComboBoxItem(Image.SCALE_REPLICATE, "REPLICATE"));
		items.add(new ComboBoxItem(Image.SCALE_SMOOTH, "SMOOTH"));
		scaleAlgorithm = new JComboBox(items);
		scaleAlgorithm.setFont(scaleAlgorithm.getFont().deriveFont(Font.PLAIN, 11.0f));
		scaleAlgorithm.setSelectedIndex(4);
		JLabel scaleAlgorithmLabel = new JLabel(getResource("label.scaling.algorithm", "  Scaling Algorithm:"));
		JPanel scaleAlgorithmPanel = new JPanel();
		scaleAlgorithmPanel.setLayout(new FlowLayout(FlowLayout.LEFT, isMacLAF ? 0 : 4, 0));
		scaleAlgorithmPanel.add(scaleAlgorithmLabel);
		scaleAlgorithmPanel.add(scaleAlgorithm);
		settings.add(scaleAlgorithmPanel);

		// Slpash Scaling
		Vector<String> ssItems = new Vector<String>();
		ssItems.add(getResource("item.splash.noresize", "No resizing(iPhone only)"));
		ssItems.add(getResource("item.splash.noresize.both", "No resizing(iPhone & iPad)"));
		ssItems.add(getResource("item.splash.resize", "Fit to the screen height(iPhone only)"));
		ssItems.add(getResource("item.splash.fittoscreen", "Fit to the screen"));
		ssItems.add(getResource("item.splash.fittolongside", "Fit to long side"));
		ssItems.add(getResource("item.splash.noaspectratio", "Fit to launch image size(no aspect ratio)"));
		splashScaling = new JComboBox(ssItems);
		splashScaling.setFont(splashScaling.getFont().deriveFont(Font.PLAIN, 11.0f));
		splashScaling.setSelectedIndex(4);
		splashScaling.setToolTipText(getResource("tooltip.splash.scaling", "The Non-Retina images will be scaled down in the fixed 50%, even if 'No resizing' is selected."));
		splashScaling.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (splashScaling.getSelectedIndex() < 0) {
					splashImage.setScalingType(3);
					ipadLaunchImage.setScalingType(3);
				} else {
					int scalingType = splashScaling.getSelectedIndex();
					splashImage.setScalingType(splashScaling.getSelectedIndex());
					if (scalingType == 0 || scalingType == 2) {
						ipadLaunchImage.setScalingType(3);
					} else {
						ipadLaunchImage.setScalingType(scalingType);
					}
				}
			}
		});
		JLabel splashScalingLabel = new JLabel(getResource("label.splash.image", "Splash:"));
		JPanel splashScalingPanel = new JPanel();
		splashScalingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, isMacLAF ? 0 : 4, 0));
		splashScalingPanel.add(splashScalingLabel);
		splashScalingPanel.add(splashScaling);
		settings.add(splashScalingPanel);

		// choose image types
		Vector<ComboBoxItem> imageTypes = new Vector<ComboBoxItem>();
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_CUSTOM         , "(same as source)"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_INT_RGB        , "INT_RGB"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_INT_ARGB       , "INT_ARGB"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_INT_ARGB_PRE   , "INT_ARGB_PRE"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_INT_BGR        , "INT_BGR"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_3BYTE_BGR      , "3BYTE_BGR"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_4BYTE_ABGR     , "4BYTE_ABGR"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_4BYTE_ABGR_PRE , "4BYTE_ABGR_PRE"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_USHORT_565_RGB , "USHORT_565_RGB"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_USHORT_555_RGB , "USHORT_555_RGB"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_BYTE_GRAY      , "BYTE_GRAY"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_USHORT_GRAY    , "USHORT_GRAY"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_BYTE_BINARY    , "BYTE_BINARY"));
		imageTypes.add(new ComboBoxItem(BufferedImage.TYPE_BYTE_INDEXED   , "BYTE_INDEXED"));
		imageType = new JComboBox(imageTypes);
		imageType.setFont(imageType.getFont().deriveFont(Font.PLAIN, 11.0f));
		imageType.setSelectedIndex(0);
		JLabel imageTypeLabel = new JLabel(getResource("label.image.type", "Image Type:"));
		JPanel imageTypePanel = new JPanel();
		imageTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT, isMacLAF ? 0 : 4, 0));
		imageTypePanel.add(imageTypeLabel);
		imageTypePanel.add(imageType);
		settings.add(imageTypePanel);

		// specify launch image background color
		splashBackgroundColor = new JTextField(8);
		splashBackgroundColor.setFont(splashBackgroundColor.getFont().deriveFont(Font.PLAIN, 11.0f));
		splashBackgroundColor.setToolTipText(getResource("tooltip.splash.bgcolor", "tooltip.splash.bgcolor"));
		splashBackgroundColor.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
					splashBackgroundColor.setText("");
				} else {
					splashBackgroundColor.selectAll();
				}
			}
			@Override public void focusLost(FocusEvent e) {
				setSplashBackgroundColor(splashBackgroundColor.getText());
			}
		});
		JButton splashBackgroundColorButton = new JButton("...");
		splashBackgroundColorButton.setFont(splashBackgroundColorButton.getFont().deriveFont(Font.PLAIN, 11.0f));
		splashBackgroundColorButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Color c = null;
				try {
					c = JColorChooser.showDialog(splashBackgroundColor, "Color Chooser", splashBackgroundColor.getText().equals(PLACEHOLDER_SPLASH_BGCOL) ? null : new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true));
				} catch (Throwable t) {
					handleThrowable(t);
				}
				if (c != null) {
					setSplashBackgroundColor(String.format("%2h%2h%2h%2h", c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()).replace(' ', '0'));
				}
			}
		});
		JLabel splashBackgroundColorLabel = new JLabel(getResource("label.splash.bgcolor", "Launch image bgcolor:"));
		JPanel splashBackgroundColorPanel = new JPanel();
		splashBackgroundColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, isMacLAF ? 0 : 4, 0));
		splashBackgroundColorPanel.add(splashBackgroundColorLabel);
		splashBackgroundColorPanel.add(splashBackgroundColor);
		splashBackgroundColorPanel.add(splashBackgroundColorButton);
		settings.add(splashBackgroundColorPanel);

		// Checkboxes and Radio Buttons.
		JPanel generateOptions = new JPanel();
		generateOptions.setLayout(new BoxLayout(generateOptions, BoxLayout.LINE_AXIS));
		generateOptions.add(new JLabel(getResource("label.generate.options", "Options:")));
		generateOptions.add(Box.createHorizontalStrut(5));

		Vector<String> versionItems = new Vector<String>();
		versionItems.add(getResource("item.version.any", "All Versions"));
		versionItems.add(getResource("item.version.7", "iOS 7 and Later"));
		versionItems.add(getResource("item.version.8", "iOS 8 and Later"));
		minimumVersion = new JComboBox(versionItems);
		minimumVersion.setFont(minimumVersion.getFont().deriveFont(Font.PLAIN, 11.0f));
		minimumVersion.setSelectedIndex(0);
		minimumVersion.setToolTipText(getResource("tooltip.minimum.version", "Versions for iPhone and iPad"));
		generateOptions.add(minimumVersion);
		generateOptions.add(Box.createHorizontalStrut(5));
		generateOptions.add(this.generateIphone = new JToggleButton(getResource("toggle.iphone", "iPhone"), true));
		generateOptions.add(Box.createHorizontalStrut(5));
		generateOptions.add(this.generateIpad = new JToggleButton(getResource("toggle.ipad", "iPad"), true));
		this.generateIphone.setBackground(BGCOLOR_GRAY);
		this.generateIpad.setBackground(BGCOLOR_GRAY);

		generateOptions.add(Box.createHorizontalStrut(8));
		generateOptions.add(new JSeparator(SwingConstants.VERTICAL));
		generateOptions.add(Box.createHorizontalStrut(8));
		generateOptions.add(this.generateArtwork = new JCheckBox(getResource("label.generate.artwork", "Artwork"), true));
		generateOptions.add(this.generateOldSplashImages = new JCheckBox(getResource("label.generate.old.splash", "Generate Old Splash Images"), false));
		generateOptions.add(this.generateTvSplash = new JCheckBox(getResource("label.generate.tv.splash", "tv"), false));
		generateArtwork.setToolTipText(getResource("tooltip.generate.artwork", "Generate iTunes Store Artwork images."));
		generateOldSplashImages.setToolTipText(getResource("tooltip.generate.old.splash", "Generate \"to-status-bar\" launch images specified for iOS 6 iPad."));
		generateTvSplash.setToolTipText(getResource("label.generate.tv.splash.tooltip", "Generate launch images for Apple TV (not generate Icons for TV)"));
		settings.add(generateOptions);

		JSeparator separatorSouth = new JSeparator(JSeparator.HORIZONTAL);
		settings.add(separatorSouth);

		this.generateAsAssetCatalogs = new JCheckBox(getResource("label.generate.as.asset.catalogs", "Generate As Asset Catalogs"), true);
		this.generateAsAssetCatalogs.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					String generated = getResource("string.dir.generate", "generated");
					String assets = getResource("string.dir.assets", "Images.xcassets");
					if (generateAsAssetCatalogs.isSelected() && outputPath.getText().endsWith(generated)) {
						outputPath.setText((new File(new File(outputPath.getText()).getParentFile(), assets)).getCanonicalPath());
					}
					if (!generateAsAssetCatalogs.isSelected() && outputPath.getText().endsWith(assets)) {
						outputPath.setText((new File(new File(outputPath.getText()).getParentFile(), generated)).getCanonicalPath());
					}
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		});
		settings.add(this.generateAsAssetCatalogs);

		this.generateAsPrerendered = new JCheckBox(getResource("label.generate.as.prerendered", "pre-rendered"), true);
		settings.add(this.generateAsPrerendered);
		this.cleanBeforeGenerate = new JCheckBox(getResource("label.clean.before.generate", "Clean"), true);
		this.cleanBeforeGenerate.setToolTipText(getResource("tooltip.clean.before.generate", "Delete all files in output directory before generating. (Asset Catalogs only)"));
		settings.add(this.cleanBeforeGenerate);

		minimumVersion.addItemListener(propertyChangedItemListener);
		generateIphone.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (!generateIphone.isSelected() && !generateIpad.isSelected()) {
					generateIpad.setSelected(true);
				}
				setStorePropertiesRequested(true);
			}
		});
		generateIpad.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (!generateIpad.isSelected() && !generateIphone.isSelected()) {
					generateIphone.setSelected(true);
				}
				setStorePropertiesRequested(true);
			}
		});
		generateArtwork.addItemListener(propertyChangedItemListener);
		generateOldSplashImages.addItemListener(propertyChangedItemListener);
		generateTvSplash.addItemListener(propertyChangedItemListener);
		generateAsAssetCatalogs.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				generateAsPrerendered.setEnabled(generateAsAssetCatalogs.isSelected());
				setStorePropertiesRequested(true);
			}
		});
		generateAsPrerendered.addItemListener(propertyChangedItemListener);
		cleanBeforeGenerate.addItemListener(propertyChangedItemListener);

		// Output Path.
		outputPath = new JTextField();
		outputPath.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { this.setDisplayOutputPath(); }
			@Override public void insertUpdate(DocumentEvent e) { this.setDisplayOutputPath(); }
			@Override public void removeUpdate(DocumentEvent e) { this.setDisplayOutputPath(); }
			private void setDisplayOutputPath() {
				if (IOSImageUtil.isNullOrWhiteSpace(outputPath.getText())) {
					outputPathDisplay.setText("");
				} else {
					outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText()));
				}
				outputPathDisplay.setToolTipText(outputPathDisplay.getText());
				setStorePropertiesRequested(true);
			}
		});
		outputPath.addFocusListener(new FocusAdapter() {
			@Override public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					((JTextField)e.getSource()).selectAll();
				}
			}
		});
		JButton outputPathButton = new JButton("...");
		JLabel outputPathLabel = new JLabel(getResource("label.output.path", "Output Dir:"), SwingConstants.RIGHT);
		outputPathButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (!IOSImageUtil.isNullOrWhiteSpace(outputPath.getText())) {
					File dir = new File(outputPath.getText());
					if (dir.exists()) {
						chooser.setCurrentDirectory(dir);
					} else if (dir.getParentFile() != null && dir.getParentFile().exists()) {
						chooser.setCurrentDirectory(dir.getParentFile());
					}
				} else {
					File dir = getChosenDirectory();
					if (dir != null) chooser.setCurrentDirectory(dir);
				}
				chooser.setApproveButtonText(getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(MainFrame.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(outputPath, chooser.getSelectedFile(), null);
			    }
			}
		});
		settings.add(outputPathLabel, BorderLayout.WEST);
		settings.add(outputPath, BorderLayout.CENTER);
		settings.add(outputPathButton, BorderLayout.EAST);

		SpringLayout layout = new SpringLayout();
		settings.setLayout(layout);
		// Layout for iOS 6 Path
		layout.putConstraint(SpringLayout.EAST, icon6PathButton, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, icon6PathButton, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.WEST, optionalIconBase, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, optionalIconBase, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, icon6Path, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, icon6Path, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, icon6Path, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, watchPath, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, watchPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, watchPath, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, carplayPath, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, carplayPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, carplayPath, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, macPath, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, macPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, macPath, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, ipadIconPath, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, ipadIconPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, ipadIconPath, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, ipadLaunchPath, 5, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.EAST, ipadLaunchPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, ipadLaunchPath, 0, SpringLayout.VERTICAL_CENTER, icon6PathButton);

		// Layout for iOS 7 Path
		layout.putConstraint(SpringLayout.EAST, icon7PathButton, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, icon7PathButton, 5, SpringLayout.SOUTH, icon6PathButton);
		layout.putConstraint(SpringLayout.WEST, icon7PathLabel, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, icon7PathLabel, 0, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, icon7PathLabel, 0, SpringLayout.VERTICAL_CENTER, icon7PathButton);
		layout.putConstraint(SpringLayout.WEST, icon7Path, 5, SpringLayout.EAST, icon7PathLabel);
		layout.putConstraint(SpringLayout.EAST, icon7Path, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, icon7Path, 0, SpringLayout.VERTICAL_CENTER, icon7PathButton);

		// Layout for Launch Image Path
		layout.putConstraint(SpringLayout.EAST, splashPathButton, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, splashPathButton, 5, SpringLayout.SOUTH, icon7PathButton);
		layout.putConstraint(SpringLayout.WEST, splashPathLabel, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, splashPathLabel, 0, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, splashPathLabel, 0, SpringLayout.VERTICAL_CENTER, splashPathButton);
		layout.putConstraint(SpringLayout.WEST, splashPath, 5, SpringLayout.EAST, splashPathLabel);
		layout.putConstraint(SpringLayout.EAST, splashPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, splashPath, 0, SpringLayout.VERTICAL_CENTER, splashPathButton);

		// Layout comboboxes and checkboxes.
		layout.putConstraint(SpringLayout.WEST, separatorNorth, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, separatorNorth, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, separatorNorth, 10, SpringLayout.SOUTH, splashPathButton);
		// 1st row.
		layout.putConstraint(SpringLayout.EAST, scaleAlgorithmPanel, -48, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, scaleAlgorithmPanel, 10, SpringLayout.SOUTH, separatorNorth);
		layout.putConstraint(SpringLayout.WEST, splashScalingPanel, -24, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, splashScalingPanel, 10, SpringLayout.SOUTH, separatorNorth);
		// 2nd row.
		layout.putConstraint(SpringLayout.EAST, imageTypePanel, -48, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, imageTypePanel, 5, SpringLayout.SOUTH, scaleAlgorithmPanel);
		layout.putConstraint(SpringLayout.WEST, splashBackgroundColorPanel, -24, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, splashBackgroundColorPanel, 5, SpringLayout.SOUTH, scaleAlgorithmPanel);
		// 3rd row.
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, generateOptions, 0, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, generateOptions, 5, SpringLayout.SOUTH, imageTypePanel);

		// Layout for Output Image Path
		layout.putConstraint(SpringLayout.WEST, separatorSouth, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, separatorSouth, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, separatorSouth, 8, SpringLayout.SOUTH, generateOptions);
		layout.putConstraint(SpringLayout.EAST, outputPathButton, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, outputPathButton, 0, SpringLayout.VERTICAL_CENTER, generateAsAssetCatalogs);
		layout.putConstraint(SpringLayout.WEST, generateAsAssetCatalogs, 8, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.NORTH, generateAsAssetCatalogs, 0, SpringLayout.SOUTH, separatorSouth);
		layout.putConstraint(SpringLayout.EAST, generateAsPrerendered, 0, SpringLayout.EAST, generateAsAssetCatalogs);
		layout.putConstraint(SpringLayout.NORTH, generateAsPrerendered, 0, SpringLayout.SOUTH, generateAsAssetCatalogs);
		layout.putConstraint(SpringLayout.WEST, cleanBeforeGenerate, 10, SpringLayout.EAST, generateAsAssetCatalogs);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, cleanBeforeGenerate, 0, SpringLayout.VERTICAL_CENTER, outputPathButton);
		layout.putConstraint(SpringLayout.WEST, outputPathLabel, 8, SpringLayout.EAST, cleanBeforeGenerate);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, outputPathLabel, 0, SpringLayout.VERTICAL_CENTER, outputPathButton);
		layout.putConstraint(SpringLayout.WEST, outputPath, 5, SpringLayout.EAST, outputPathLabel);
		layout.putConstraint(SpringLayout.EAST, outputPath, -5, SpringLayout.WEST, icon6PathButton);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, outputPath, 0, SpringLayout.VERTICAL_CENTER, outputPathButton);

		// Image Panels.
		final JPanel imagesPanel = new JPanel();
		icon7Image = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.IPHONE_60x2, getResource("label.icon7.drop", "Drop iOS7 Icon PNG Here")), icon7Path);
		icon7Image.setForeground(COLOR_CYAN);
		splashImage = initImagePanel(new ImagePanel(this, IOSSplashAssetCatalogs.IPHONE_568x2, getResource("label.splash.drop", "Drop Splash Image PNG Here")), splashPath);

		icon6Image = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.IPHONE_57x2, getResource("label.icon6.drop", "Drop iOS6 Icon PNG Here")), icon6Path);
		carplayImage = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.CAR_60x60x2, getResource("label.carplay.drop", "Icon for CarPlay\n( Optional )")), carplayPath);
		watchImage = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.WATCH_29x29x3, getResource("label.watch.drop", "Icon for Apple Watch\n( Optional )")), watchPath);
		macImage = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.MAC_256x256x2, getResource("label.mac.drop", "Icon for Mac\n( Optional )")), macPath);
		ipadIconImage = initImagePanel(new ImagePanel(this, IOSIconAssetCatalogs.IPAD_76x2, getResource("label.ipad.drop", "Icon for iPad\n( Optional )")), ipadIconPath);
		ipadLaunchImage = initImagePanel(new ImagePanel(this, IOSSplashAssetCatalogs.IPAD_LANDSCAPEx2, getResource("label.ipad.launch.drop", "Launch Image for iPad\n( Optional )")), ipadLaunchPath);

		ItemListener imageItemListener = (new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				setStorePropertiesRequested(true);
				for (ImagePanel ip : Arrays.asList(icon7Image, splashImage, icon6Image, carplayImage, watchImage, macImage, ipadIconImage, ipadLaunchImage)) {
					ip.refresh();
				}
			}
		});
		scaleAlgorithm.addItemListener(imageItemListener);
		imageType.addItemListener(imageItemListener);

		
		JPanel optionalIconImages1 = new JPanel();
		optionalIconImages1.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImages1.setLayout(new GridLayout(2, 1, 0, 2));
		optionalIconImages1.add(watchImage);
		JPanel optionalIconImagesSub1 = new JPanel();
		optionalIconImagesSub1.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImagesSub1.setLayout(new GridLayout(1, 2, 2, 0));
		forwardButton = new JButton(getResource("label.optional.forward.button", "More >>"));
		forwardButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				((CardLayout)imagesPanel.getLayout()).show(imagesPanel, "options");
			}
		});
		forwardButton.setBackground(Color.WHITE);
		forwardButton.setForeground(COLOR_DARK_GRAY);
		optionalIconImagesSub1.add(forwardButton);
		optionalIconImagesSub1.add(icon6Image);
		optionalIconImages1.add(optionalIconImagesSub1);

		JPanel imagesPanel1 = new JPanel();
		imagesPanel1.setBorder(new LineBorder(BGCOLOR_LIGHT_GRAY, 4));
		imagesPanel1.setBackground(BGCOLOR_LIGHT_GRAY);
		imagesPanel1.setLayout(new GridLayout(1, 3, 2, 2));
		imagesPanel1.add(optionalIconImages1);
		imagesPanel1.add(icon7Image);
		imagesPanel1.add(splashImage);

		JPanel optionalIconImages2 = new JPanel();
		//optionalIconImages2.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImages2.setLayout(new GridLayout(2, 1, 0, 2));
		optionalIconImages2.add(macImage);
		JPanel optionalIconImagesSub2 = new JPanel();
		optionalIconImagesSub2.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImagesSub2.setLayout(new GridLayout(1, 2, 2, 0));
		backButton = new JButton(getResource("label.optional.back.button", "<< Back"));
		backButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				((CardLayout)imagesPanel.getLayout()).show(imagesPanel, "main");
			}
		});
		backButton.setBackground(BGCOLOR_LIGHT_GRAY);
		backButton.setForeground(COLOR_DARK_GRAY);
		optionalIconImagesSub2.add(backButton);
		optionalIconImagesSub2.add(carplayImage);
		optionalIconImages2.add(optionalIconImagesSub2);

		JPanel imagesPanel2 = new JPanel();
		imagesPanel2.setBorder(new LineBorder(BGCOLOR_GRAY, 4));
		imagesPanel2.setBackground(BGCOLOR_GRAY);
		imagesPanel2.setLayout(new GridLayout(1, 3, 2, 2));
		imagesPanel2.add(optionalIconImages2);
		imagesPanel2.add(ipadIconImage);
		imagesPanel2.add(ipadLaunchImage);
		macImage.setBackground(BGCOLOR_GRAY);
		carplayImage.setBackground(BGCOLOR_GRAY);
		ipadIconImage.setBackground(BGCOLOR_GRAY);
		ipadLaunchImage.setBackground(BGCOLOR_GRAY);

		imagesPanel.setLayout(new CardLayout());
		imagesPanel.add(imagesPanel1, "main");
		imagesPanel.add(imagesPanel2, "options");
		((CardLayout)imagesPanel.getLayout()).show(imagesPanel, "main");
		this.add(imagesPanel, BorderLayout.CENTER);

		// Surface
		final JPanel surface = new JPanel();
		surface.setPreferredSize(new Dimension(layeredPane.getPreferredSize().width, layeredPane.getPreferredSize().height + 16));
		surface.setBackground(Color.WHITE);
		layeredPane.add(surface, JLayeredPane.PALETTE_LAYER);

		SpringLayout surfaceLayout = new SpringLayout();
		surface.setLayout(surfaceLayout);
		surface.addMouseListener(new MouseListener() {
			// Consume event.
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseReleased(MouseEvent e) {}
		});

		generateButton = initMenuButton(new JButton(getResource("button.generate", "Generate"), new ImageIcon(this.getClass().getResource("img/generate.png"))), Color.WHITE, BGCOLOR_GENERATE, fontExtraLarge);
		generateButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/generate_rollover.png")));
		ActionListener generateAction = (new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		generateButton.addActionListener(generateAction);
		surface.add(generateButton);

		cancelButton = initMenuButton(new JButton(getResource("button.cancel", "Cancel"), new ImageIcon(this.getClass().getResource("img/generate.gif"))), COLOR_GRAY, BGCOLOR_LIGHT_GRAY, fontExtraLarge);
		cancelButton.setRolloverEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelGenerate();
			}

		});
		surface.add(cancelButton);
		cancelButton.setVisible(false);

		settingsButton = initMenuButton(new JButton(getResource("button.settings", "Settings"), new ImageIcon(this.getClass().getResource("img/settings.png"))), Color.WHITE, BGCOLOR_SETTINGS, fontExtraLarge);
		settingsButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/settings_disabled.png")));
		settingsButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/settings_rollover.png")));
		surface.add(settingsButton);

		splitButton = initMenuButton(new JButton(getResource("button.split", "Split"), new ImageIcon(this.getClass().getResource("img/splitter.png"))), Color.WHITE, BGCOLOR_SPLIT, fontExtraLarge);
		splitButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/splitter_disabled.png")));
		splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/splitter_rollover.png")));
		ActionListener splitAction = (new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				splitter.setVisible(!splitter.isVisible());
				splitter.requestFocus();
			}
		});
		splitButton.addActionListener(splitAction);
		surface.add(splitButton);

		progress = new JProgressBar(0, IOSIconAssetCatalogs.values().length + IOSArtworkInfo.values().length + IOSSplashAssetCatalogs.values().length);
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setBorderPainted(false);
		progress.setBackground(isMacLAF ? Color.WHITE : BGCOLOR_LIGHT_GRAY);
		progress.setForeground(COLOR_CYAN);
		progress.setOpaque(true);
		surface.add(progress);
		outputPathDisplay = new JLabel("");
		outputPathDisplay.setForeground(COLOR_CYAN);
		outputPathDisplay.setFont(fontNormal);
		outputPathDisplay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		surface.add(outputPathDisplay);

		final JButton gripeButton = new JButton(new ImageIcon(this.getClass().getResource("img/gripe.png")));
		gripeButton.setBackground(Color.WHITE);
		gripeButton.setBorderPainted(false);
		gripeButton.setFocusPainted(false);
		gripeButton.setRolloverEnabled(true);
		gripeButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/gripe_rollover.png")));
		gripeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		gripeButton.setMargin(null);

		final ActionListener settingsActionCallback = (new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				outputPathDisplay.setVisible(false);
				gripeButton.setVisible(true);
				surface.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		});
		ActionListener settingsAction = (new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (isGenerating()) {
					return;
				}
				if (!animator.isTargetAtBaseLocation()) {
					gripeButton.setVisible(false);
					outputPathDisplay.setVisible(true);
					surface.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				animator.animate(animator.isTargetAtBaseLocation() ? settingsActionCallback : null);
			}
		});
		settingsButton.addActionListener(settingsAction);
		gripeButton.addActionListener(settingsAction);
		surface.add(gripeButton);
		gripeButton.setVisible(false);

		surfaceLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, generateButton, 0, SpringLayout.HORIZONTAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.NORTH, generateButton, -96, SpringLayout.VERTICAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, cancelButton, 0, SpringLayout.HORIZONTAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.NORTH, cancelButton, -96, SpringLayout.VERTICAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.EAST, settingsButton, -112, SpringLayout.HORIZONTAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.NORTH, settingsButton, -96, SpringLayout.VERTICAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.WEST, splitButton, 128, SpringLayout.HORIZONTAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.NORTH, splitButton, -96, SpringLayout.VERTICAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.WEST, progress, 0, SpringLayout.WEST, settingsButton);
		surfaceLayout.putConstraint(SpringLayout.EAST, progress, 0, SpringLayout.EAST, generateButton);
		surfaceLayout.putConstraint(SpringLayout.NORTH, progress, 16, SpringLayout.SOUTH, generateButton);
		surfaceLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, outputPathDisplay, 0, SpringLayout.HORIZONTAL_CENTER, surface);
		surfaceLayout.putConstraint(SpringLayout.NORTH, outputPathDisplay, 8, SpringLayout.SOUTH, progress);
		surfaceLayout.putConstraint(SpringLayout.WEST, gripeButton, 0, SpringLayout.WEST, surface);
		surfaceLayout.putConstraint(SpringLayout.EAST, gripeButton, 0, SpringLayout.EAST, surface);
		surfaceLayout.putConstraint(SpringLayout.SOUTH, gripeButton, 2, SpringLayout.SOUTH, surface);

		animator = new SimpleShutterAnimation(surface, 16);

		this.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				Rectangle rect = e.getComponent().getBounds();
				Rectangle crect = generateAsPrerendered.getBounds();
				int height = crect.y + crect.height + 2;
				layeredPane.setPreferredSize(new Dimension(rect.width, height));
				layeredPane.doLayout();
			}
		});
		layeredPane.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				Rectangle rect = e.getComponent().getBounds();
				settings.setBounds(0, 0, rect.width, rect.height);
				surface.setBounds(0, surface.getBounds().y, rect.width, rect.height + 16);
				settings.doLayout();
				surface.doLayout();
			}
		});

		// Create MenuBar.
		int shortcutKeyMask = InputEvent.CTRL_DOWN_MASK;
		try {
			shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(this.getResource("menu.file", "File"));
		JMenuItem menuItemClear = new JMenuItem(this.getResource("menu.file.new", "New (Clear Settings)"));
		menuItemClear.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));
		menuItemClear.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (isStorePropertiesRequested()) {
					if (yesNo(getResource("question.store.properties", "Save current settings?"))) {
						if (propertiesFile == null) {
							if (!storeProperties()) {
								return;
							}
						} else {
							storeProperties(propertiesFile);
						}
					}
				}
				applyProperties(getDefaultProperties(new Properties()));
				setPropertiesFile(null);
			}
		});
		fileMenu.add(menuItemClear);
		fileMenu.add(new JSeparator());
		JMenuItem menuItemLoad = new JMenuItem(this.getResource("menu.file.load.properties", "Load Settings..."));
		menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutKeyMask));
		menuItemLoad.setToolTipText(this.getResource("tooltip.load.properties", "Also could be loaded settings by dropping a settings file in the upper half of window."));
		menuItemLoad.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (isStorePropertiesRequested()) {
					if (yesNo(getResource("question.store.properties", "Save current settings?"))) {
						if (propertiesFile == null) {
							if (!storeProperties()) {
								return;
							}
						} else {
							storeProperties(propertiesFile);
						}
					}
				}
				loadProperties();
			}
		});
		fileMenu.add(menuItemLoad);
		fileMenu.add(historiesMenu = new JMenu(this.getResource("menu.file.histories", "Histories")));
		historiesAction = (new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (!(e.getSource() instanceof JMenuItem)) {
					return;
				}
				if (isStorePropertiesRequested()) {
					if (yesNo(getResource("question.store.properties", "Save current settings?"))) {
						if (propertiesFile == null) {
							if (!storeProperties()) {
								return;
							}
						} else {
							storeProperties(propertiesFile);
						}
					}
				}
				loadProperties(new File(((JMenuItem)e.getSource()).getText()));
			}
		});
		fileMenu.add(new JSeparator());
		JMenuItem menuItemOverwrite = new JMenuItem(this.getResource("menu.file.overwrite.properties", "Save"));
		menuItemOverwrite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask));
		menuItemOverwrite.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (propertiesFile == null) {
					storeProperties();
				} else {
					storeProperties(propertiesFile);
				}
			}
		});
		fileMenu.add(menuItemOverwrite);
		JMenuItem menuItemStore = new JMenuItem(this.getResource("menu.file.store.properties", "Save Settings..."));
		menuItemStore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutKeyMask + InputEvent.SHIFT_DOWN_MASK));
		menuItemStore.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				storeProperties();
			}
		});
		fileMenu.add(menuItemStore);
		fileMenu.add(new JSeparator());

		JMenuItem menuItemQuit = new JMenuItem(this.getResource("menu.file.quit", "Quit"));
		menuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask + InputEvent.SHIFT_DOWN_MASK));
		menuItemQuit.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				processWindowEvent(new WindowEvent(MainFrame.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		fileMenu.add(menuItemQuit);
		menuBar.add(fileMenu);

		JMenu editMenu = new JMenu(this.getResource("menu.edit", "Edit"));
		JMenuItem menuItemGenerate = new JMenuItem(this.getResource("menu.edit.generate", "Generate Images"));
		menuItemGenerate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, shortcutKeyMask));
		menuItemGenerate.addActionListener(generateAction);
		editMenu.add(menuItemGenerate);
		final JMenuItem menuItemOpenDirectory = new JMenuItem(this.getResource("menu.edit.open.output", "Open Output Directory"));
		menuItemOpenDirectory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, shortcutKeyMask));
		menuItemOpenDirectory.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					if (splitter.isActive()) {
						splitter.openOutputDirectory();
					} else {
						if (!IOSImageUtil.isNullOrWhiteSpace(outputPath.getText())) {
							File dir = new File(outputPath.getText());
							if (dir.exists()) {
								Desktop.getDesktop().open(dir);
							} else {
								alert(String.format("[%s] %s", dir.getAbsolutePath(), getResource("error.not.exists", "isnot exists")));
							}
						}
					}
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		});
		editMenu.add(menuItemOpenDirectory);
		outputPathDisplay.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				menuItemOpenDirectory.doClick();
			}
		});
		editMenu.add(new JSeparator());
		JMenuItem menuItemSettings = new JMenuItem(this.getResource("menu.edit.settings", "Settings..."));
		menuItemSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutKeyMask));
		menuItemSettings.addActionListener(settingsAction);
		editMenu.add(menuItemSettings);
		menuBar.add(editMenu);

		JMenu windowMenu = new JMenu(this.getResource("menu.window", "Window"));
		JMenuItem menuItemSplitter = new JMenuItem(this.getResource("menu.window.splitter", "Image Set Generator..."));
		menuItemSplitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, shortcutKeyMask));
		menuItemSplitter.addActionListener(splitAction);
		windowMenu.add(menuItemSplitter);
		windowMenu.add(new JSeparator());
		menuItemSaveChangesOnClose = new JCheckBoxMenuItem(this.getResource("menu.window.save.changes", "Confirm save changes on close"), true);
		windowMenu.add(menuItemSaveChangesOnClose);
		JMenuItem menuItemResetWindowSize = new JMenuItem(this.getResource("menu.window.reset", "Reset window size"));
		menuItemResetWindowSize.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				MainFrame.this.setSize(DEFAULT_WINDOW_SIZE);
			}
		});
		windowMenu.add(menuItemResetWindowSize);
		menuBar.add(windowMenu);

		helpMenu = new JMenu(this.getResource("menu.help", "Help"));
		JMenuItem menuItemOpenArticle = new JMenuItem(this.getResource("menu.help.open.article", "Open article... (Japanese)"));
		menuItemOpenArticle.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(getResource("menu.help.open.article.url", "http://gootara.org/library/2013/12/xcode-ios.html")));
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		});
		helpMenu.add(menuItemOpenArticle);
		JMenuItem menuItemOpenGitHub = new JMenuItem(this.getResource("menu.help.open.github", "Open GitHub repository..."));
		menuItemOpenGitHub.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(getResource("menu.help.open.github.url", "https://github.com/gootara-org/ios-image-util")));
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		});
		helpMenu.add(menuItemOpenGitHub);
		helpMenu.add(new JSeparator());
		menuItemCheckForUpdatesOnStartUp = new JCheckBoxMenuItem(this.getResource("menu.help.check.update.on.statup", "Check for Updates on Startup"), true);
		helpMenu.add(menuItemCheckForUpdatesOnStartUp);
		menuItemCheckForUpdates = new JMenuItem(this.getResource("menu.help.check.update", "Check for Updates"));
		menuItemCheckForUpdates.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				checkForUpdates(true);
			}
		});
		helpMenu.add(menuItemCheckForUpdates);
		menuItemExecuteUpdate = new JMenuItem(this.getResource("menu.help.execute.update", "Update Jar file"));
		menuItemExecuteUpdate.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				executeUpdate();
			}
		});
		menuItemExecuteUpdate.setEnabled(false);
		helpMenu.add(menuItemExecuteUpdate);
		helpMenu.add(new JSeparator());
		JMenuItem menuItemVersion = new JMenuItem(this.getResource("menu.help.show.version", "Version..."));
		menuItemVersion.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainFrame.this, String.format("Version: %s - gootara.org", IOSImageUtil.getVersion(IOSImageUtil.getLocalVersionURL(MainFrame.this))), "Version", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(this.getClass().getResource("img/icon64.png")));
			}
		});
		helpMenu.add(menuItemVersion);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);

		if (!IOSImageUtil.isExecutableJarFile(this)) {
			// Not executed by jar file.
			this.menuItemCheckForUpdatesOnStartUp.setSelected(false);
			this.menuItemCheckForUpdatesOnStartUp.setEnabled(false);
			this.menuItemCheckForUpdates.setEnabled(false);
		}

		surface.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						File file = null;
						if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
							if (list instanceof List) {
								Object obj = ((List<?>)list).get(0);
								if (obj instanceof File) {
									file = (File)obj;
								}
							}
						} else if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
							String urls = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
							StringTokenizer tokens = new StringTokenizer(urls);
							if (tokens.hasMoreTokens()) {
								file = new File(URLDecoder.decode((new URL(tokens.nextToken()).getFile()), "UTF-8"));
							}
						}
						if (file == null) {
							return false;
						}
						setAlwaysOnTop(true);
						toFront();
						requestFocus();
						setAlwaysOnTop(false);
						final File targetFile = file;
						// JOptionPane.showConfirmDialog has abnormal focus during dropping files on mac. Try another thread.
						(new SwingWorker<Boolean, Integer>() {
							@Override protected Boolean doInBackground() throws Exception {
								try {
									if (isStorePropertiesRequested()) {
										if (yesNo(getResource("question.store.properties", "Save current settings?"))) {
											if (propertiesFile == null) {
												if (!storeProperties()) {
													return false;
												}
											} else {
												storeProperties(propertiesFile);
											}
										}
									}
									loadProperties(targetFile);
									return true;
								} catch (Throwable t) {
									handleThrowable(t);
								}
								return false;
							}
						}).execute();
						return true;
					}
				} catch (Throwable t) {
					handleThrowable(t);
				} finally {
					setAlwaysOnTop(false);
				}
				return false;
			}
			@Override public boolean canImport(TransferSupport support) {
				if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					return true;
				}
				boolean result = false;
				if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					for (DataFlavor flavor : support.getTransferable().getTransferDataFlavors()) {
						if (flavor.getSubType().equals("uri-list")) {
							result = true;
							break;
						}
					}
	            }
				return result;
			}

		});
		this.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				if (isBatchMode()) {
					return;
				}
				if (isGenerating() || splitter.isGenerating()) {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					if (splitter.isGenerating() && !splitter.isShowing()) {
						splitButton.doClick();
					}
					alert(getResource("error.close.window", "Please wait until generating process will be finished."));
					return;
				}
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				try {
					if (menuItemSaveChangesOnClose.isSelected()) {
						if (!isStorePropertiesRequested()) {
							return;
						}
						if (!yesNo(getResource("question.store.properties", "Save current settings?"))) {
							return;
						}
						if (propertiesFile == null) {
							if (!storeProperties()) {
								setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
							}
						} else {
							storeProperties(propertiesFile);
						}
					}
				} catch (Exception ex) {
					// not fatal. ignore this.
					ex.printStackTrace();
				} finally {
					storeProperties(new File(IOSImageUtil.getDefaultDirectory(MainFrame.this), IOSImageUtil.DEFAULT_PROPERTIES_FILENAME), true);
				}
			}
		});

		// initial initialize
		surface.setLocation(0, -16);
		this.pack();

		splitter = new SplitterFrame(this, getResource("splitter.title", "Splitter"));

		this.setSize(DEFAULT_WINDOW_SIZE);

		// Apply default properties.
		this.applyProperties(this.getDefaultProperties(new Properties()));
		this.setStorePropertiesRequested(false);

		// Splitter default location.
		Point p = new Point(getX() + getWidth() + 4, getY());
		Rectangle vitualBounds = MainFrame.getVirtualScreenBounds();
		if (vitualBounds.width < p.x + splitter.getWidth()) {
			splitter.setLocationRelativeTo(MainFrame.this);
		} else {
			splitter.setLocation(p);
		}
	}

	/**
	 * Initialize gui functions.
	 * Called only once on startup with gui mode before window shown.
	 */
	public void initializeGUI() {
		/**
		 * I can't understand what a hell is going on here,
		 * but window move event is never raised on my Mac OS X Yosemite with java 1.7.0_45, 1.8.0_51. (default look and feel.)
		 * I know code like this is ugly, but rescue this bug because menu item and combobox pulldown
		 * and something like that are sticky on previous location.
		 * (Is this real bug? Anyone doesn't care about this?)
		 * window.getLocation and getBounds values are not updated after window moved on Mac's desktop.
		 * I can't believe this at all.
		 *
		 * If something wrong occurs with this code, change os.name by java command line option like below.
		 *   java -jar -Dos.name=Windows ios-image-util.jar
		 */
		final boolean isMac = System.getProperty("os.name", "").toLowerCase().indexOf("mac") >= 0;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			long[] limits = { 0, 0 };
			Point[] locations = { new Point(-1, -1), new Point(-1, -1) };
			@Override public void eventDispatched(AWTEvent event) {
				if (event instanceof MouseEvent) {
					if (!isMac) { return; }
					if (event.getID() != MouseEvent.MOUSE_MOVED) {
						return;
					}
					MouseEvent e = (MouseEvent)event;
					Object source = event.getSource();
					if (source == null) return;
					if (!(source instanceof Component)) return;
					if (source instanceof Dialog) return;

					Component c = (Component)source;
					Window target = SwingUtilities.windowForComponent(c);
					if (target == null || (target != MainFrame.this && target != splitter)) {
						return;
					}
					int index = target == MainFrame.this ? 0 : 1;
					Point inside = e.getPoint();
					Point realLocation = e.getLocationOnScreen();
					Point location = target.getLocation();
					Point converted = SwingUtilities.convertPoint(c, inside, target);
					realLocation.translate(0 - converted.x, 1 - converted.y);
					if (realLocation.distance(location) > 8.0) {
						if (System.currentTimeMillis() < limits[index]) {
							return;
						}
						if (locations[index].distance(realLocation) != 0.0) {
							locations[index].setLocation(realLocation);
							limits[index] = System.currentTimeMillis() + 0xFF;
							return;
						}
						// Window moved silently.
						target.setLocation(realLocation);
					}
				} else if (event instanceof KeyEvent) {
					if (event.getID() != KeyEvent.KEY_PRESSED) {
						return;
					}
					Object source = event.getSource();
					if (source != null && source instanceof Component) {
						Window target = SwingUtilities.windowForComponent((Component)source);
						if (target != null && target == splitter) {
							KeyEvent e = (KeyEvent)event;
							if (e.getModifiers() != 0 && e.getKeyCode() > 0x1f) {
								// Send menu shortcut keys to main window.
								MainFrame.this.dispatchEvent(new KeyEvent(MainFrame.this, e.getID(), System.currentTimeMillis(), e.getModifiers(), e.getKeyCode(), e.getKeyChar()));
							}
						}
					}
				}
			}
		}, AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.KEY_EVENT_MASK);

		/* By default, Swing's JTextField is not undoable.
		 * Surely, it's possible to make all textfields undoable.
		 * But application will change some text automatically,
		 * and some conflict might be occurred with Undo/Redo operations.
		 * Moreover, Undo/Redo operations are not important to this application.
		 * So Undo/Redo is not available currently.
		 */
	}

	/**
	 * Set preferences to ImagePanel.
	 *
	 * @param imagePanel
	 * @param textField
	 * @return imagePanel
	 */
	private ImagePanel initImagePanel(final ImagePanel imagePanel, final JTextField textField) {
		imagePanel.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		imagePanel.setBackground(BGCOLOR_LIGHT_GRAY);
		imagePanel.setForeground(COLOR_DARK_GRAY);
		imagePanel.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (isGenerating()) {
						return false;
					}
					if (canImport(support)) {
						File file = null;
						if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
							if (list instanceof List) {
								Object obj = ((List<?>)list).get(0);
								if (obj instanceof File) {
									file = (File)obj;
								}
							}
						} else if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
							String urls = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
							StringTokenizer tokens = new StringTokenizer(urls);
							if (tokens.hasMoreTokens()) {
								file = new File(URLDecoder.decode((new URL(tokens.nextToken()).getFile()), "UTF-8"));
							}
						}
						if (file == null) {
							return false;
						}
						MainFrame.this.setAlwaysOnTop(true);
						MainFrame.this.toFront();
						MainFrame.this.requestFocus();
						MainFrame.this.setAlwaysOnTop(false);
						// JOptionPane.showConfirmDialog has abnormal focus during dropping files on mac. Try another thread.
						final File targetFile = file;
						(new SwingWorker<Boolean, Integer>() {
							@Override protected Boolean doInBackground() throws Exception {
								try {
									if (setFilePath(textField, targetFile, imagePanel)) {
										return true;
									}
								} catch (Throwable t) {
									handleThrowable(t);
								}
								return false;
							}
						}).execute();
					}
				} catch (Throwable t) {
					handleThrowable(t);
				} finally {
					MainFrame.this.setAlwaysOnTop(false);
				}
				return false;
			}
			@Override public boolean canImport(TransferSupport support) {
				if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					return true;
				}
				boolean result = false;
				if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					for (DataFlavor flavor : support.getTransferable().getTransferDataFlavors()) {
						if (flavor.getSubType().equals("uri-list")) {
							result = true;
							break;
						}
					}
	            }
				return result;
			}
		});
		imagePanel.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if (isGenerating()) {
					return;
				}
				if (imagePanel.getImage() != null && imagePanel.getImageFile() != null) {
					if (yesNo(getResource("question.clear.image", "Clear this image?"))) {
						setFilePath(textField, null, imagePanel);
					}
				} else {
					setFilePathActionPerformed(textField, imagePanel);
				}
			}
		});

		textField.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					((JTextField)e.getSource()).selectAll();
				}
			}
			@Override public void focusLost(FocusEvent e) {
				try {
					String currentPath = (imagePanel.getImageFile() == null) ? "" : imagePanel.getImageFile().getCanonicalPath();
					if (!currentPath.equals(textField.getText())) {
						setFilePath(textField, IOSImageUtil.isNullOrWhiteSpace(textField.getText()) ? null : new File(textField.getText()), imagePanel);
						setStorePropertiesRequested(true);
					}
				} catch (Throwable t) {
					handleThrowable(t);
				}
			}
		});
		return imagePanel;
	}

	/**
	 * Set preferences to Menu button.
	 *
	 * @param button
	 * @param foregroundColor
	 * @param backgroundColor
	 * @param font
	 * @return
	 */
	private JButton initMenuButton(JButton button, Color foregroundColor, Color backgroundColor, Font font) {
		button.setBackground(backgroundColor);
		button.setForeground(foregroundColor);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
		button.setFont(font);
		button.setMargin(new Insets(2, 16, 2, 16));
		button.setOpaque(true);
		button.setDoubleBuffered(true);
		button.setRolloverEnabled(true);
		return button;
	}

	/**
	 * Set gui components availability.
	 *
	 * @param enabled
	 * @param cancelable
	 */
	public void enableGUI(boolean enabled, boolean cancelable) {
		if (isBatchMode()) {
			return;
		}

		settingsButton.setBackground(enabled ? BGCOLOR_SETTINGS : BGCOLOR_LIGHT_GRAY);
		settingsButton.setEnabled(enabled);
		generateButton.setBackground(enabled ? BGCOLOR_GENERATE : BGCOLOR_LIGHT_GRAY);
		generateButton.setEnabled(enabled);
		generateButton.setVisible(enabled);
		cancelButton.setEnabled(!enabled);
		cancelButton.setVisible(cancelable ? !enabled : false);
		if (!enabled && cancelButton.isVisible()) {
			cancelButton.requestFocus();
		}
		for (int i = 0; i < getJMenuBar().getMenuCount(); i++) {
			getJMenuBar().getMenu(i).setEnabled(enabled);
		}
	}

	/**
	 * Get virtual screen bounds. (For multi display)
	 *
	 * @return
	 */
	private static Rectangle getVirtualScreenBounds() {
		Rectangle virtualBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) {
		    GraphicsDevice gd = gs[j];
		    GraphicsConfiguration[] gc = gd.getConfigurations();
		    for (int i=0; i < gc.length; i++) {
		        virtualBounds = virtualBounds.union(gc[i].getBounds());
		    }
		}
		return virtualBounds;
	}

	/**
	 * Keep window location inside of screen.
	 *
	 * @param windowBounds
	 * @return
	 */
	protected static Rectangle keepWindowInsideScreen(Rectangle windowBounds) {
		Rectangle virtualBounds = MainFrame.getVirtualScreenBounds();
		int dx = windowBounds.x < 0 ? 1 : windowBounds.x + windowBounds.width > virtualBounds.width ? -1 : 0;
		int dy = windowBounds.y < 0 ? 1 : windowBounds.y + windowBounds.height > virtualBounds.height ? -1 : 0;
		int dcnt = 0;
		while ((dx != 0 || dy != 0) && !virtualBounds.contains(windowBounds)) {
			windowBounds.translate(dx , dy);
			dx = windowBounds.x < 0 ? 1 : windowBounds.x + windowBounds.width > virtualBounds.width ? -1 : 0;
			dy = windowBounds.y < 0 ? 1 : windowBounds.y + windowBounds.height > virtualBounds.height ? -1 : 0;
			dcnt++;
			if (dcnt > Math.max(virtualBounds.width, virtualBounds.height)) {
				break;
			}
		}
		return windowBounds;
	}

	/**
	 * Set file path from file chooser dialog.
	 *
	 * @param textField		TextField to set the file path
	 * @param imagePanel	ImagePnel to set the image
	 */
	private void setFilePathActionPerformed(JTextField textField, ImagePanel imagePanel) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
		if (!IOSImageUtil.isNullOrWhiteSpace(textField.getText())) {
			File f = new File(textField.getText());
			if (f.getParentFile() != null && f.getParentFile().exists()) {
				chooser.setCurrentDirectory(f.getParentFile());
			}
			if (f.exists()) {
				chooser.setSelectedFile(f);
			}
		} else {
			File dir = getChosenDirectory();
			if (dir != null) chooser.setCurrentDirectory(dir);
		}
		chooser.setApproveButtonText(getResource("button.approve", "Choose"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			setFilePath(textField, chooser.getSelectedFile(), imagePanel);
	    }
	}

	/**
	 * Set file path.
	 *
	 * @param textField		TextField to set the file path
	 * @param f				File to set.
	 * @param imagePanel	ImagePnel to set the image
	 * @return	true - no problem / false - error occured
	 */
	private boolean setFilePath(JTextField textField, File f, ImagePanel imagePanel) {
		final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		final Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
		String prevText = textField.getText();
		try {
			if (textField == null || f == null) {
				if (textField != null) textField.setText("");
				if (imagePanel != null) imagePanel.clear();
				if (imagePanel == splashImage) { this.setSplashBackgroundColor(null); }
				return false;
			}
			this.setCursor(waitCursor);
			textField.setText(f.getCanonicalPath());
			if (imagePanel != null) {
				ImageFile imageFile = checkFile(textField);
				if (imageFile == null) {
					textField.setText("");
					if (imagePanel != null) imagePanel.clear();
					if (imagePanel == splashImage) { this.setSplashBackgroundColor(null); }
					return false;
				}
				if (!this.isBatchMode()) {
					imagePanel.setImage(imageFile.getImage());
					imagePanel.setImageFile(imageFile.getFile());
				}
				selectedDirectory = imageFile.getFile().getParentFile();
				if (IOSImageUtil.isNullOrWhiteSpace(outputPath.getText())) {
					File g = new File(f.getParentFile(), this.generateAsAssetCatalogs.isSelected() ? this.getResource("string.dir.assets", "Images.xcassets") : this.getResource("string.dir.generate", "generated"));
					outputPath.setText(g.getCanonicalPath());
				}
				if (imagePanel == splashImage) {
					Color c = imageFile.getDefaultBackgroundColor();
					this.setSplashBackgroundColor(String.format("%2h%2h%2h%2h", c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()).replace(' ', '0'));
				}
			}

			final JTextField[] paths = { icon6Path, watchPath, carplayPath, macPath, ipadIconPath, ipadLaunchPath };
			for (int i = 0; i < paths.length; i++) {
				if (paths[i] == textField) {
					optionalImages.setSelectedIndex(i);
				}
			}
		} catch (Throwable t) {
			handleThrowable(t);
			textField.setText("");
			if (imagePanel != null) imagePanel.clear();
			return false;
		} finally {

			boolean mainImageSelected = icon7Image.getImage() != null
									||	splashImage.getImage() != null
									||	watchImage.getImage() != null
									||	icon6Image.getImage() != null;
			boolean optionImageSelected = ipadIconImage.getImage() != null
									||	ipadLaunchImage.getImage() != null
									||	macImage.getImage() != null
									||	carplayImage.getImage() != null;
			forwardButton.setForeground(optionImageSelected ? COLOR_CYAN : COLOR_DARK_GRAY);
//			forwardButton.setText(String.format("%s%s", optionImageSelected ? "* " : "", getResource("label.optional.forward.button", "More >>")));
//			forwardButton.setBorder(new LineBorder(optionImageSelected ? COLOR_CYAN : COLOR_DARK_GRAY, 1));
			backButton.setForeground(mainImageSelected ? COLOR_WINERED : COLOR_DARK_GRAY);
//			backButton.setText(String.format("%s%s", getResource("label.optional.back.button", "<< Back"), mainImageSelected ? " *" : ""));
//			backButton.setBorder(new LineBorder(mainImageSelected ? COLOR_WINERED : COLOR_DARK_GRAY, 1));
			this.setCursor(defaultCursor);
			if (!prevText.equals(textField.getText())) {
				this.setStorePropertiesRequested(true);
			}
		}
		return true;
	}

	/**
	 * @return Chosen directory
	 */
	public File getChosenDirectory() {
		File dir;
		if (this.selectedDirectory != null && this.selectedDirectory.exists() && this.selectedDirectory.isDirectory()) return this.selectedDirectory;
		if ((dir = getChosenDirectory(icon7Path)) != null) return dir;
		if ((dir = getChosenDirectory(splashPath)) != null) return dir;
		if ((dir = getChosenDirectory(watchPath)) != null) return dir;
		if ((dir = getChosenDirectory(ipadIconPath)) != null) return dir;
		if ((dir = getChosenDirectory(ipadLaunchPath)) != null) return dir;
		if ((dir = getChosenDirectory(icon6Path)) != null) return dir;
		if ((dir = getChosenDirectory(carplayPath)) != null) return dir;
		if ((dir = getChosenDirectory(macPath)) != null) return dir;
		if ((dir = getChosenDirectory(outputPath)) != null) return dir;
		return null;
	}

	private File getChosenDirectory(JTextField textField) {
		if (textField == null || IOSImageUtil.isNullOrWhiteSpace(textField.getText())) {
			return null;
		}
		File f = new File(textField.getText());
		if (!f.exists()) {
			return null;
		}
		return f.isDirectory() ? f : f.getParentFile();
	}

	// Manage properties
	/**
	 * true - Show confirm dialog when closing window.
	 *
	 * @param b
	 */
	public void setStorePropertiesRequested(boolean b) {
		this.storePropertiesRequested = b;
		this.setWindowTitle();
	}

	/**
	 * Are properties changed?
	 *
	 * @return
	 */
	public boolean isStorePropertiesRequested() {
		return storePropertiesRequested;
	}

	/**
	 * Refresh window title string.
	 */
	private void setWindowTitle() {
		this.setTitle((this.isStorePropertiesRequested() ? " * " : "") + getResource("window.title", "iOS Image Util") + " " + (propertiesFile == null ? "" : " - [" + propertiesFile.getAbsolutePath() + "]"));
	}

	/**
	 * Set property file.
	 *
	 * @param f property file
	 */
	private void setPropertiesFile(File f) {
		this.propertiesFile = f;
		this.setStorePropertiesRequested(false);
		addHistory(f);
	}

	private void addHistory(File f) {
		if (f == null) {
			return;
		}
		try {
			String path = f.getCanonicalPath();
			boolean exists = true;
			while (exists) {
				exists = false;
				for (int i = 0; i < historiesMenu.getItemCount(); i++) {
					if (historiesMenu.getItem(i).getText().equals(path)) {
						exists = true;
						historiesMenu.remove(i);
						break;
					}
				}
			}
			JMenuItem item = new JMenuItem(path);
			item.addActionListener(historiesAction);
//			item.setMaximumSize(new Dimension(200, 24));
			historiesMenu.insert(item, 0);
			while (historiesMenu.getItemCount() > 10) {
				historiesMenu.remove(historiesMenu.getItemCount() - 1);
			}
		} catch (Exception ex) {
			handleThrowable(ex);
		}
	}

	/**
	 * Load properties from choosen file.
	 */
	private void loadProperties() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Properties XML files", "xml"));
		chooser.setApproveButtonText(getResource("button.approve", "Choose"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (propertiesFile != null) {
			chooser.setCurrentDirectory(propertiesFile.getParentFile());
		} else {
			File dir = IOSImageUtil.getDefaultDirectory(this);
			if (dir == null) {
				dir = getChosenDirectory();
			}
			if (dir != null) {
				chooser.setCurrentDirectory(dir);
			}
		}
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			loadProperties(chooser.getSelectedFile());
	    }
	}

	/**
	 * Load properties from file.
	 *
	 * @param f property file
	 */
	public void loadProperties(File f) {
		this.loadProperties(f, false, false);
	}
	public void loadProperties(File f, boolean system, boolean forceNew) {
		if (f == null || !f.exists()) {
			alert("[" + f.getAbsolutePath() + "] " + getResource("error.not.exists", "is not exists."));
			return;
		}
		BufferedInputStream bin = null;
		try {
			Properties props = new Properties();
			if (system) {
				props.load(bin = new BufferedInputStream(new FileInputStream(f)));
				if (IOSImageUtil.isExecutableJarFile(this)) {
					this.setCheckForUpdatesOnStartUp(IOSImageUtil.getBoolProperty(props, "system.check.for.updates.on.startup", true));
				}
				if (forceNew) {
					props.put("system.last.properties.file", "");
				}
			} else {
				props.loadFromXML(bin = new BufferedInputStream(new FileInputStream(f)));
			}
			this.applyProperties(props, system);
			if (system) {
				applySystemProperties(props);
			} else {
				this.setPropertiesFile(f);
			}
		} catch (Throwable t) {
			handleThrowable(t);
		} finally {
			if (bin != null) {
				try { bin.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}

	/**
	 * Apply system properties.
	 *
	 * @param props
	 */
	private void applySystemProperties(Properties props) {
		boolean lastStorePropertiesRequested = IOSImageUtil.getBoolProperty(props, "system.last.store.properties.requested", false);
		String lastPropertiesFilePath = props.getProperty("system.last.properties.file", "").trim();
		if (!lastPropertiesFilePath.isEmpty()) {
			File lastPropertiesFile = new File(lastPropertiesFilePath);
			if (lastPropertiesFile.exists() && lastPropertiesFile.isFile()) {
				this.setPropertiesFile(lastPropertiesFile);
			}
		}
		String lastSelectedDirectoryPath = props.getProperty("system.last.selected.directory", "").trim();
		if (!lastSelectedDirectoryPath.isEmpty()) {
			File dir = new File(lastSelectedDirectoryPath);
			if (dir.exists() && dir.isDirectory()) {
				selectedDirectory = dir;
			}
		}
		this.setStorePropertiesRequested(lastStorePropertiesRequested);
		this.menuItemSaveChangesOnClose.setSelected(IOSImageUtil.getBoolProperty(props, "system.save.changes.on.close", true));

		for (int i = 0; i < 10; i++) {
			String path = props.getProperty(String.format("system.file.histories.%d", i), "");
			if (!IOSImageUtil.isNullOrWhiteSpace(path)) {
				File f = new File(path);
				if (f.exists()) {
					this.addHistory(f);
				}
			}
		}

		int x = IOSImageUtil.getIntProperty(props, "system.window.x", -1);
		int y = IOSImageUtil.getIntProperty(props, "system.window.y", -1);
		int width = IOSImageUtil.getIntProperty(props, "system.window.width", -1);
		int height = IOSImageUtil.getIntProperty(props, "system.window.height", -1);
		if (x != -1 && y != -1 && width != -1 && height != -1) {
			Rectangle windowBounds = new Rectangle(x, y, width, height);
			this.setBounds(MainFrame.keepWindowInsideScreen(windowBounds));
		}
		if (IOSImageUtil.getBoolProperty(props, "system.window.maximized", false)) {
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
		}

		x = IOSImageUtil.getIntProperty(props, "system.dialog.x", -1);
		y = IOSImageUtil.getIntProperty(props, "system.dialog.y", -1);
		if (x != -1 && y != -1) {
			Rectangle dialogBounds = splitter.getBounds();
			dialogBounds.setLocation(x, y);
			dialogBounds = MainFrame.keepWindowInsideScreen(dialogBounds);
			splitter.setLocation(dialogBounds.x, dialogBounds.y);
			if (IOSImageUtil.getBoolProperty(props, "system.dialog.visibility", false)) {
				splitter.setVisible(true);
			}
		}
	}

	/**
	 * Apply stored properties.
	 *
	 * @param props stored properties
	 * @return same as props
	 */
	public Properties applyProperties(Properties props) {
		return applyProperties(props, false);
	}
	private Properties applyProperties(Properties props, boolean system) {
		try {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Properties def = this.getDefaultProperties(new Properties());
			this.setGenerateAsAssetCatalogs(this.getBoolProperty(props, "generate.as.asset.catalogs", def));
			this.setGenerateAsPrerendered(this.getBoolProperty(props, "generate.as.prerendered", def));
			this.setCleanBeforeGenerate(this.getBoolProperty(props, "clean.before.generate", def));
			this.setIcon7Path(this.getStringProperty(props, "icon.image.path", def));
			this.setSplashPath(this.getStringProperty(props, "launch.image.path", def));
			this.setIcon6Path(this.getStringProperty(props, "ios6.icon.image.path", def));
			this.setCarplayPath(this.getStringProperty(props, "carplay.icon.image.path", def));
			this.setWatchPath(this.getStringProperty(props, "watch.icon.image.path", def));
			this.setMacPath(this.getStringProperty(props, "mac.icon.image.path", def));
			this.setIpadIconPath(this.getStringProperty(props, "ipad.icon.image.path", def));
			this.setIpadLaunchPath(this.getStringProperty(props, "ipad.launch.image.path", def));
			this.setOutputPath(this.getStringProperty(props, "output.dir.path", def));
			this.setSplashScaling(this.getIntProperty(props, "launch.scaling.type", def));
			this.setGenerateOldSplashImages(this.getBoolProperty(props, "generate.to-status-bar.images", def));
			this.setGenerateTvSplash(this.getBoolProperty(props, "generate.tv.splash", def));
			this.setGenerateArtwork(this.getBoolProperty(props, "generate.artwork.images", def));
			if (this.getBoolProperty(props, "generate.iphone.images.only", def)) { this.selectIphoneOnly(); }
			else if (this.getBoolProperty(props, "generate.ipad.images.only", def)) { this.selectIpadOnly(); }
			else { this.generateIphone.setSelected(true); this.generateIpad.setSelected(true); }
			this.setImageType(this.getIntProperty(props, "image.type", def));
			this.setScalingAlgorithm(this.getIntProperty(props, "scaling.algorithm.type", def));
			this.setMinimumVersion(this.getIntProperty(props, "generate.minimum.version", def));

			this.setSplashBackgroundColor(props.getProperty("launch.image.back.color", "").trim().equals(PLACEHOLDER_SPLASH_BGCOL) ? "" : this.getStringProperty(props, "launch.image.back.color", def));
		} catch (Throwable t) {
			handleThrowable(t);
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		return splitter.applyProperties(props, system);
	}

	/**
	 * Get property as String with default value.
	 *
	 * @param props properties
	 * @param key property name
	 * @param defaultProperties	default values
	 * @return
	 */
	protected String getStringProperty(Properties props, String key, Properties defaultProperties) {
		return props.getProperty(key, defaultProperties.getProperty(key, ""));
	}

	/**
	 * Get property as int with default value.
	 *
	 * @param props properties
	 * @param key property name
	 * @param defaultProperties	default values
	 * @return
	 */
	protected int getIntProperty(Properties props, String key, Properties defaultProperties) {
		return IOSImageUtil.getIntProperty(props, key, IOSImageUtil.getIntProperty(defaultProperties, key, 0));
	}

	/**
	 * Get property as boolean with default value.
	 *
	 * @param props properties
	 * @param key property name
	 * @param defaultProperties	default values
	 * @return
	 */
	protected boolean getBoolProperty(Properties props, String key, Properties defaultProperties) {
		return IOSImageUtil.getBoolProperty(props, key, IOSImageUtil.getBoolProperty(defaultProperties, key, false));
	}

	/**
	 * Get default properties.
	 *
	 * @param props default properties
	 * @return
	 */
	public Properties getDefaultProperties(Properties props) {
		props.put("generate.as.asset.catalogs", Boolean.toString(true));
		props.put("generate.as.prerendered", Boolean.toString(true));
		props.put("clean.before.generate", Boolean.toString(true));
		props.put("icon.image.path", "");
		props.put("launch.image.path", "");
		props.put("ios6.icon.image.path", "");
		props.put("carplay.icon.image.path", "");
		props.put("watch.icon.image.path", "");
		props.put("mac.icon.image.path", "");
		props.put("ipad.icon.image.path", "");
		props.put("ipad.launch.image.path", "");
		props.put("output.dir.path", "");
		props.put("launch.scaling.type", "4");
		props.put("generate.to-status-bar.images", Boolean.toString(false));
		props.put("generate.tv.splash", Boolean.toString(false));
		props.put("generate.artwork.images", Boolean.toString(true));
		props.put("generate.iphone.images.only", Boolean.toString(false));
		props.put("generate.ipad.images.only", Boolean.toString(false));
		props.put("image.type", "0");
		props.put("scaling.algorithm.type", "4");
		props.put("launch.image.back.color", "");
		props.put("generate.minimum.version", "0");

		return splitter.getDefaultProperties(props);
	}

	/**
	 * Store properties to choosen file.
	 */
	private boolean storeProperties() {
		JFileChooser chooser = new JFileChooser();
		chooser.setApproveButtonText(getResource("button.store", "Save"));
		chooser.setFileFilter(new FileNameExtensionFilter("Properties XML files", "xml"));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (propertiesFile != null) {
			chooser.setCurrentDirectory(propertiesFile.getParentFile());
		} else {
			File dir = IOSImageUtil.getDefaultDirectory(this);
			if (dir == null) {
				dir = getChosenDirectory();
			}
			if (dir != null) {
				chooser.setCurrentDirectory(dir);
			}
		}
		int returnVal = chooser.showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			if (f.getName().indexOf(".") < 0) {
				f = new File(f.getParentFile(), f.getName().concat(".xml"));
			}

			if (f.exists()) {
				if (!yesNo(getResource("question.store.overwrite", "A file which has same name is already exists. Overwrite it?"))) {
					return false;
				}
			}
			storeProperties(f);
			return true;
	    }
		return false;
	}

	private Properties storeSystemProperties(Properties props) {
		props.put("system.check.for.updates.on.startup", Boolean.toString(this.isCheckForUpdatesOnStartUp()));
		props.put("system.save.changes.on.close", Boolean.toString(this.menuItemSaveChangesOnClose.isSelected()));
		props.put("system.last.store.properties.requested", Boolean.toString(this.isStorePropertiesRequested()));
		String lastPropertiesFilePath = "";
		String lastSelectedDirectoryPath = "";
		try {
			if (this.propertiesFile != null && this.propertiesFile.exists() && this.propertiesFile.isFile()) {
				lastPropertiesFilePath = this.propertiesFile.getCanonicalPath();
			}
			if (this.selectedDirectory != null && this.selectedDirectory.exists() && this.selectedDirectory.isDirectory()) {
				lastSelectedDirectoryPath = this.selectedDirectory.getCanonicalPath();
			}
		} catch (Exception ex) {
			// Not fatal. Ignore this.
			ex.printStackTrace();
			lastPropertiesFilePath = "";
			lastSelectedDirectoryPath = "";
		}
		props.put("system.last.properties.file", lastPropertiesFilePath);
		props.put("system.last.selected.directory", lastSelectedDirectoryPath);

		int idx = 0;
		for (int i = 0; i < historiesMenu.getItemCount(); i++) {
			String path = historiesMenu.getItem(i).getText();
			if (!IOSImageUtil.isNullOrWhiteSpace(path)) {
				File f = new File(path);
				if (!f.exists()) {
					continue;
				}
				props.put(String.format("system.file.histories.%d", idx), path);
				idx++;
			}
		}
		
		props.put("system.window.maximized", (this.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0 ? "true" : "false");
		this.setExtendedState(Frame.NORMAL);
		Rectangle r = this.getBounds();
		props.put("system.window.x", Integer.toString(r.x));
		props.put("system.window.y", Integer.toString(r.y));
		props.put("system.window.width", Integer.toString(r.width));
		props.put("system.window.height", Integer.toString(r.height));

		Rectangle rd = splitter.getBounds();
		props.put("system.dialog.x", Integer.toString(rd.x));
		props.put("system.dialog.y", Integer.toString(rd.y));
		props.put("system.dialog.visibility", Boolean.toString(splitter.isVisible()));

		return props;
	}

	/**
	 * Store properties to file.
	 *
	 * @param f property file
	 */
	private void storeProperties(File f) {
		storeProperties(f, false);
	}
	private void storeProperties(File f, boolean system) {
		Properties props = new Properties();
		BufferedOutputStream bout = null;
		try {
			if (system) {
				this.storeSystemProperties(props);
				this.storeProperties(props, true).store((bout = new BufferedOutputStream(new FileOutputStream(f))), "ios-image-util properties");
			} else {
				this.storeProperties(props, false).storeToXML((bout = new BufferedOutputStream(new FileOutputStream(f))), "ios-image-util properties");
				this.setPropertiesFile(f);
			}
		} catch (Throwable t) {
			handleThrowable(t);
		} finally {
			if (bout != null) {
				try { bout.close(); } catch (Exception ex) { ex.printStackTrace(); }
			}
		}
	}

	/**
	 * Set properties to store.
	 *
	 * @param props Properties to store
	 * @return same as props
	 */
	public Properties storeProperties(Properties props, boolean system) {
		props.put("icon.image.path", this.icon7Path.getText());
		props.put("launch.image.path", this.splashPath.getText());
		props.put("ios6.icon.image.path", this.icon6Path.getText());
		props.put("carplay.icon.image.path", this.carplayPath.getText());
		props.put("watch.icon.image.path", this.watchPath.getText());
		props.put("mac.icon.image.path", this.macPath.getText());
		props.put("ipad.icon.image.path", this.ipadIconPath.getText());
		props.put("ipad.launch.image.path", this.ipadLaunchPath.getText());
		props.put("output.dir.path", this.outputPath.getText());
		props.put("launch.scaling.type", Integer.toString(this.splashScaling.getSelectedIndex()));
		props.put("generate.to-status-bar.images", Boolean.toString(this.generateOldSplashImages.isSelected()));
		props.put("generate.tv.splash", Boolean.toString(this.generateTvSplash.isSelected()));
		props.put("generate.artwork.images", Boolean.toString(this.generateArtwork.isSelected()));
		props.put("generate.iphone.images.only", Boolean.toString(this.generateIphone.isSelected() && !this.generateIpad.isSelected()));
		props.put("generate.ipad.images.only", Boolean.toString(!this.generateIphone.isSelected() && this.generateIpad.isSelected()));
		props.put("generate.as.asset.catalogs", Boolean.toString(this.generateAsAssetCatalogs.isSelected()));
		props.put("generate.as.prerendered", Boolean.toString(this.generateAsPrerendered.isSelected()));
		props.put("clean.before.generate", Boolean.toString(this.cleanBeforeGenerate.isSelected()));
		props.put("image.type", Integer.toString(this.imageType.getSelectedIndex()));
		props.put("scaling.algorithm.type", Integer.toString(this.scaleAlgorithm.getSelectedIndex()));
		props.put("generate.minimum.version", Integer.toString(this.minimumVersion.getSelectedIndex()));
		if (!this.splashBackgroundColor.getText().equals(PLACEHOLDER_SPLASH_BGCOL)) { props.put("launch.image.back.color", this.splashBackgroundColor.getText()); }
		return splitter.storeProperties(props, system);
	}

	public boolean isCheckForUpdatesOnStartUp() {
		return menuItemCheckForUpdatesOnStartUp.isSelected();
	}
	public void setCheckForUpdatesOnStartUp(boolean b) {
		menuItemCheckForUpdatesOnStartUp.setSelected(b);
	}

	// for command line option switches.
	public boolean setIcon6Path(String path) { return setFilePath(icon6Path, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), icon6Image); }
	public boolean setIcon7Path(String path) { return setFilePath(icon7Path, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), icon7Image); }
	public boolean setSplashPath(String path) { return setFilePath(splashPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), splashImage); }
	public boolean setCarplayPath(String path) { return setFilePath(carplayPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), carplayImage); }
	public boolean setWatchPath(String path) { return setFilePath(watchPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), watchImage); }
	public boolean setMacPath(String path) { return setFilePath(macPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), macImage); }
	public boolean setIpadIconPath(String path) { return setFilePath(ipadIconPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), ipadIconImage); }
	public boolean setIpadLaunchPath(String path) { return setFilePath(ipadLaunchPath, IOSImageUtil.isNullOrWhiteSpace(path) ? null : new File(path), ipadLaunchImage); }
	public void setOutputPath(String path) { outputPath.setText(IOSImageUtil.isNullOrWhiteSpace(path) ? "" : (new File(path)).getAbsolutePath()); }
	public void setSplashScaling(int idx) { splashScaling.setSelectedIndex(idx); }
	public void setGenerateOldSplashImages(boolean b) { this.generateOldSplashImages.setSelected(b); }
	public void setGenerateTvSplash(boolean b) { this.generateTvSplash.setSelected(b); }
	public void setGenerateArtwork(boolean b) { this.generateArtwork.setSelected(b); }
	public void setGenerateAsAssetCatalogs(boolean b) { this.generateAsAssetCatalogs.setSelected(b); }
	public void setGenerateAsPrerendered(boolean b) { this.generateAsPrerendered.setSelected(b); }
	public void setCleanBeforeGenerate(boolean b) { this.cleanBeforeGenerate.setSelected(b); }
	public void selectIphoneOnly() { this.generateIphone.setSelected(true); this.generateIpad.setSelected(false); }
	public void selectIpadOnly() { this.generateIphone.setSelected(false); this.generateIpad.setSelected(true); }
	public void setBatchMode(boolean b) { this.batchMode = b; }
	public void setSilentMode(boolean b) { this.silentMode = b; }
	public void setVerboseMode(boolean b) { this.verboseMode = b; }
	public boolean isBatchMode() { return this.batchMode; }
	public boolean isSilentMode() { return this.silentMode; }
	public boolean isVerboseMode() { return this.verboseMode; }
	public void setImageType(int idx) { imageType.setSelectedIndex(idx); }
	public void setMinimumVersion(int index) { this.minimumVersion.setSelectedIndex(index); }
	public boolean isGenerateImagesReqested() {
		return !icon6Path.getText().trim().isEmpty()
			|| !icon7Path.getText().trim().isEmpty()
			|| !splashPath.getText().trim().isEmpty()
			|| !watchPath.getText().trim().isEmpty()
			|| !carplayPath.getText().trim().isEmpty()
			|| !macPath.getText().trim().isEmpty()
			|| !ipadIconPath.getText().trim().isEmpty()
			|| !ipadLaunchPath.getText().trim().isEmpty()
		;
	}
	// hidden option.
	public void setScalingAlgorithm(int idx) { scaleAlgorithm.setSelectedIndex(idx); }
	protected int getScalingAlgorithm() { return ((ComboBoxItem)this.scaleAlgorithm.getSelectedItem()).getItemValue(); }

	// Splitter
	public void setWidth1x(String width) { splitter.setWidth1x(width); }
	public void setHeight1x(String height) { splitter.setHeight1x(height); }
	public void setOverwriteAlways(boolean b) { splitter.setOverwriteAlways(b); }
	public void setSplitterCleanBeforeGenerate(boolean b) { splitter.setCleanBeforeGenerate(b); }
	public void setSplitterOutputDirectory(String relativePath) { splitter.setOutputDirectory(relativePath); }
	public boolean isSplitImageRequested() { return splitTarget != null; }
	public void setSplitterBackgroundColor(String s) { splitter.setBackgroundColor(s); }
	public void setSplitterScalingType(int index) { splitter.setScalingType(index); }
	public void addSplitTarget(String path) {
		if (splitTarget == null) {
			splitTarget = new LinkedList<File>();
		}
		splitTarget.push(new File(path));
	}
	public boolean batchSplit() {
		if (!this.isBatchMode()) return false;
		if (splitTarget == null || splitTarget.size() <= 0) { System.out.println("No image file specified."); return false; }
		try {
			return splitter.dropFiles(splitTarget);
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
	}

	/**
	 * Set launch image background color.
	 *
	 * @param s color string
	 */
	public void setSplashBackgroundColor(String s) {
		String prevText = IOSImageUtil.isNullOrWhiteSpace(this.splashBackgroundColor.getText()) ? PLACEHOLDER_SPLASH_BGCOL : this.splashBackgroundColor.getText();
		Color col = null;
		if (!IOSImageUtil.isNullOrWhiteSpace(s)) {
			while (s.length() < 6) s = "0".concat(s);
			if (s.length() > 8) s = s.substring(0, 8);
			if (s.length() == 7) s = "0".concat(s);
			try {
				col = new Color(Long.valueOf(s, 16).intValue(), true);
				splashBackgroundColor.setText(s);
				splashBackgroundColor.setBackground(new Color(col.getRed(), col.getGreen(), col.getBlue()));
				float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
				splashBackgroundColor.setForeground(new Color(Color.HSBtoRGB(hsb[0], 1.0f - hsb[1], 1.0f - hsb[2])));
				splashImage.setBackground(splashBackgroundColor.getBackground());
				splashImage.setForeground(splashBackgroundColor.getForeground());
				ipadLaunchImage.setBackground(splashImage.getBackground());
			} catch (Exception ex) {
				ex.printStackTrace();
				splashBackgroundColor.setForeground(COLOR_DARK_GRAY);
				splashBackgroundColor.setBackground(Color.WHITE);
				splashImage.setBackground(null);
				splashImage.setForeground(COLOR_DARK_GRAY);
				ipadLaunchImage.setBackground(BGCOLOR_GRAY);
			}
		}
		if (col == null) {
			splashBackgroundColor.setText(PLACEHOLDER_SPLASH_BGCOL);
			splashBackgroundColor.setForeground(COLOR_DARK_GRAY);
			splashBackgroundColor.setBackground(Color.WHITE);
			splashImage.setBackground(null);
			splashImage.setForeground(COLOR_DARK_GRAY);
			ipadLaunchImage.setBackground(BGCOLOR_GRAY);
		}
		ipadLaunchImage.setForeground(splashImage.getForeground());
		if (!prevText.equals(this.splashBackgroundColor.getText())) {
			this.setStorePropertiesRequested(true);
		}
	}

	/**
	 * Add .
	 *
	 * @param i	value to add
	 */
	private void addProgress(int i) {
		if (this.isBatchMode()) {
			if (!this.isSilentMode() && !this.isVerboseMode()) for (int j = 0; j < i; j++) { System.out.print("*"); }
		} else {
			progress.setValue(progress.getValue() + i);
		}
	}

	/**
	 * Output file path to System.out.
	 *
	 * @param f	File to output
	 * @throws IOException
	 */
	protected void verbose(File f) throws IOException {
		if (!this.isBatchMode() || !this.isVerboseMode() || this.isSilentMode()) {
			return;
		}
		System.out.println(String.format("[%s] is generated.", f.getCanonicalPath()));
	}

	/**
	 * Output stack trace and show alert.
	 *
	 * @param ex	exception
	 */
	protected void handleThrowable(Throwable t) {
		if (t == null) { return; }
		t.printStackTrace(System.err);
		if (t instanceof OutOfMemoryError) {
			alert(this.getResource("error.out.of.memory", "Out of Memory Error. Increase heap size with -Xmx java option."));
		} else {
			if (t.getMessage() != null && t.toString().indexOf(t.getMessage()) < 0) {
				alert(t.toString() + " (" + t.getMessage() + ")");
			} else {
				alert(t.toString());
			}
		}
	}

	/**
	 * Show information message.
	 *
	 * @param message	message
	 */
	private void information(String message) {
		this.information(this, message);
	}
	protected void information(Component parentComponent, String message) {
		if (this.isBatchMode()) {
			if (!this.isSilentMode()) System.out.println(message);
		} else {
			JOptionPane.showMessageDialog(parentComponent, message, getResource("title.information", "Information"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Show alert.
	 *
	 * @param message	alert message
	 */
	private void alert(String message) {
		alert(this, message);
	}
	protected void alert(Component parentComponent, String message) {
		if (this.isBatchMode()) {
			System.err.println(message);
		} else {
			JOptionPane.showMessageDialog(parentComponent, message, getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Show confirm.
	 * Always 'true' with the batch mode.
	 *
	 * @param message	message
	 * @return	true - ok / false - cancel
	 */
	private boolean confirm(String message) {
		return confirm(this, message);
	}
	protected boolean confirm(Component parentComponent, String message) {
		if (this.isBatchMode()) {
			return true;
		}
		return (JOptionPane.showConfirmDialog(parentComponent, message, getResource("title.confirm", "Confirm"), JOptionPane.OK_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION);
	}

	/**
	 * Show yes / no dialog.
	 * Always 'false' with the batch mode.
	 *
	 * @param message	message
	 * @return true - yes / false - no
	 */
	private boolean yesNo(String message) {
		return yesNo(this, message);
	}
	protected boolean yesNo(Component parentComponent, String message) {
		if (this.isBatchMode()) {
			return false;
		}
		return (JOptionPane.showConfirmDialog(parentComponent, message, getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

	}

	/**
	 * Are images generating?
	 *
	 * @return generating images
	 */
	protected boolean isGenerating() {
		return 	(pool != null && !pool.isTerminated());
	}

	/**
	 * Do generate images.
	 *
	 * @return true - sucess / false - failed
	 */
	public boolean generate() {
		boolean result = true;
		cancelRequested = false;
		if (this.isGenerating()) {
			return false;
		}

		final ImageFileSet ifs = this.createValidImageFileSet();
		if (ifs == null) {
			return false;
		}

		if (!animator.isTargetAtBaseLocation()) {
			settingsButton.doClick();
		}

		enableGUI(false, true);

		try {
			progress.setMaximum(generateImages(ifs, false));
		} catch (Exception ex) {
			progress.setMaximum(IOSIconAssetCatalogs.values().length + IOSArtworkInfo.values().length + IOSSplashAssetCatalogs.values().length);
		}
		try {
			pool = Executors.newFixedThreadPool(Math.min(IOSImageUtil.getSystemIntProperty("org.gootara.ios.image.util.max.threads", 12), Math.max(IOSImageUtil.getSystemIntProperty("org.gootara.ios.image.util.max.threads", 4), Runtime.getRuntime().availableProcessors())));

			// start generate Images.
			if (isBatchMode()) {
				if (!isSilentMode() && !isVerboseMode()) {
					System.out.print("0%");
					for (int i = 2; i < progress.getMaximum() - 2; i++) { System.out.print(" "); }
					System.out.println("100%");
					System.out.print("+");
					for (int i = 1; i < progress.getMaximum() - 1; i++) { System.out.print("-"); }
					System.out.println("+");
				}
			} else {
				progress.setValue(0);
			}

			if (generateImages(ifs, true) < 0) {
				result = false;
			}

		} catch (Throwable ex) {
			result = false;
			handleThrowable(ex);
		}

		if (!result) {
			try {
				pool.shutdownNow();
			} catch (Throwable t) {
				handleThrowable(t);
			}
			enableGUI(true, false);
			return false;
		}

		SwingWorker<Boolean, Integer> watchdog = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				boolean result = true;
				try {
					pool.shutdown();
					if (!pool.awaitTermination(progress.getMaximum() * 3, TimeUnit.MINUTES)) {
						result = false;
					}

					if (isBatchMode() && !isSilentMode() && !isVerboseMode()) {
						System.out.println();
					}
					if (!isBatchMode()) {
						progress.setValue(progress.getMaximum());
					}

				} catch (Throwable t) {
					handleThrowable(t);
				} finally {
					ifs.clear();
					System.gc();
					if (isBatchMode()) {
						if (!isSilentMode()) {
							System.out.println();
						}
					} else {
						enableGUI(true, false);
						if (cancelRequested) {
							information(getResource("info.generate.canceled", "Generate images are canceled."));
						} else if (result) {
							information(getResource("label.finish.generate", "The images are generated."));
						}
						progress.setValue(0);
						outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText()));
					}
				}
				return result;
			}
		};

		try {
			watchdog.execute();
			if (this.isBatchMode()) {
				result = watchdog.get();
			}
		} catch (Throwable t) {
			result = false;
			handleThrowable(t);
		}
		return result;
	}

	/**
	 * Create valid image file set.
	 *
	 * @return ImageFileSet. Return null when failed to validate.
	 */
	private ImageFileSet createValidImageFileSet() {
		try {
			if (!isGenerateImagesReqested()) {
				alert(getResource("error.not.choosen", "Choose at least one Icon or Splash PNG file."));
				return null;
			}

			ImageFileSet ifs = new ImageFileSet();
			// Path Check.
			if (!IOSImageUtil.isNullOrWhiteSpace(icon6Path.getText())) {
				ifs.setIcon6File(checkFile(icon6Path));
				if (ifs.getIcon6File() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(watchPath.getText())) {
				ifs.setWatchFile(checkFile(watchPath));
				if (ifs.getWatchFile() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(carplayPath.getText())) {
				ifs.setCarplayFile(checkFile(carplayPath));
				if (ifs.getCarplayFile() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(macPath.getText())) {
				ifs.setMacFile(checkFile(macPath));
				if (ifs.getMacFile() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(ipadIconPath.getText())) {
				ifs.setIpadIconFile(checkFile(ipadIconPath));
				if (ifs.getIpadIconFile() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(ipadLaunchPath.getText())) {
				ifs.setIpadLaunchFile(checkFile(ipadLaunchPath));
				if (ifs.getIpadLaunchFile() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(icon7Path.getText())) {
				ifs.setIcon7File(checkFile(icon7Path));
				if (ifs.getIcon7File() == null) return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(splashPath.getText())) {
				ifs.setSplashFile(checkFile(splashPath));
				if (ifs.getSplashFile() == null) return null;
			}

			// Error Check.
			if (IOSImageUtil.isNullOrWhiteSpace(outputPath.getText())) {
				if (!animator.isTargetAtBaseLocation()) {
					outputPath.requestFocusInWindow();
				}
				alert(getResource("error.not.choosen.output.path", "Choose output dir."));
				return null;
			}
			if (!IOSImageUtil.isNullOrWhiteSpace(splashBackgroundColor.getText()) && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
				Color col = null;
				try { col = new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true); } catch (Exception ex) {}
				if (col == null) {
					alert("[" + splashBackgroundColor.getText() + "]" + getResource("error.illegal.bgcolor", "is illegal bgcolor."));
					return null;
				}
			}

			// Make sure output directory is exists.
			ifs.setOutputDirectory(new File(outputPath.getText()));
			if (!outputPath.getText().equals(ifs.getOutputDirectory().getCanonicalPath())) {
				outputPath.setText(ifs.getOutputDirectory().getCanonicalPath());
				ifs.setOutputDirectory(new File(outputPath.getText()));
			}
			if (!ifs.getOutputDirectory().exists()) {
				if (!confirm("[" + outputPath.getText() + "] " + getResource("confirm.output.path.create", "is not exists. Create it?"))) {
					return null;
				}
				if (!ifs.getOutputDirectory().mkdirs()) {
					if (!animator.isTargetAtBaseLocation()) {
						outputPath.requestFocusInWindow();
						outputPath.selectAll();
					}
					alert("[" + ifs.getOutputDirectory().getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return null;
				}
			}
			if (!ifs.getOutputDirectory().isDirectory()) {
				if (!animator.isTargetAtBaseLocation()) {
					outputPath.requestFocusInWindow();
					outputPath.selectAll();
				}
				alert("[" + ifs.getOutputDirectory().getCanonicalPath() + "] " + getResource("error.not.directory", "is not directory. Choose directory."));
				return null;
			}
			
			// generate images for iOS6, or not
			/* minimum system version to be choosable from ver 2.0
			if (ifs.getIcon6File() == null && ifs.getIcon7File() != null) {
				// do not create iOS 6 icon by default.
				this.generateOldSplashImages.setSelected(false);
				ifs.setTargetSystemVersion(IOSAssetCatalogs.SYSTEM_VERSION.IOS7);
				if (this.isBatchMode()) {
					information(getResource("info.ios6.image.not.generate", "The iOS6 image files will not be generated."));
				}
			}
			*/

			// images for iOS7 must be generated.
			if (ifs.getIcon6File() != null && ifs.getIcon7File() == null) {
				if (yesNo(getResource("question.use.icon6.instead", "An iOS7 Icon PNG file is not choosen. Use iOS6 Icon PNG file instead?"))) {
					ifs.setIcon7File(ifs.getIcon6File());
				} else {
					if (!animator.isTargetAtBaseLocation()) {
						icon7Path.requestFocusInWindow();
					}
					return null;
				}
			}

			if (!this.generateIphone.isSelected() && !this.generateIpad.isSelected()) {
				if (ifs.getIcon6File() != null || ifs.getIcon7File() != null || ifs.getIpadIconFile() != null || ifs.getIpadLaunchFile() != null || ifs.getSplashFile() != null) {
					alert(getResource("error.both.deselect", "Both iPhone and iPad are deselected. Choose either or both."));
					return null;
				}
			}

			// generate images for launch, or not
			if (!ifs.hasLaunchFile()) {
				// do not create launch images by default.
				if (this.isBatchMode()) {
					information(getResource("confirm.splash.not.generate", "The Splash image will not be generated."));
				}
			}

			ifs.setIconOutputDirectory(ifs.getOutputDirectory());
			ifs.setSplashOutputDirectory(ifs.getOutputDirectory());
			if (this.generateAsAssetCatalogs.isSelected()) {
				// Asset Catalogs
				ifs.setIconOutputDirectory(new File(ifs.getOutputDirectory(), getResource("string.dir.appicon", "AppIcon.appiconset")));
				if (ifs.getDefaultIconImage() != null && !ifs.getIconOutputDirectory().exists() && !ifs.getIconOutputDirectory().mkdirs()) {
					alert("[" + ifs.getIconOutputDirectory().getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return null;
				}
				ifs.setSplashOutputDirectory(new File(ifs.getOutputDirectory(), getResource("string.dir.launchimage", "LaunchImage.launchimage")));
				if (ifs.getSplashFile() != null && !ifs.getSplashOutputDirectory().exists() && !ifs.getSplashOutputDirectory().mkdirs()) {
					alert("[" + ifs.getSplashOutputDirectory().getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return null;
				}
			}
			return ifs;

		} catch (Throwable t) {
			handleThrowable(t);
			return null;
		}
	}

	/**
	 * Generate image files.
	 *
	 * @param ifs		ImageFileSet
	 * @param generate	true - generate images / false - count only
	 * @return output file count
	 */
	private int generateImages(ImageFileSet ifs, boolean generate) throws Exception {
		int count = 0;
		if (ifs.hasIconFile()) {
			// delete before generate
			if (this.cleanBeforeGenerate.isSelected()) {
				for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
					File f = new File(ifs.getIconOutputDirectory(), asset.getFilename());
					if (f.exists() && f.isFile()) {
						if (!f.delete()) {
							System.err.println("Could not delete - " + f.getAbsolutePath());
						}
					}
				}
				for (IOSArtworkInfo artwork : IOSArtworkInfo.values()) {
					File f = new File(ifs.getOutputDirectory(), artwork.getFilename());
					if (f.exists() && f.isFile()) {
						if (!f.delete()) {
							System.err.println("Could not delete - " + f.getAbsolutePath());
						}
					}
				}
				File f = new File(ifs.getIconOutputDirectory(), this.getResource("string.filename.contents.json", "Contents.json"));
				if (f.exists() && f.isFile()) {
					if (!f.delete()) {
						System.err.println("Could not delete - " + f.getAbsolutePath());
					}
				}
			}

			IOSAssetCatalogs.JsonUtility jsonUtility = new IOSAssetCatalogs.JsonUtility();
			// generate icons
			for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
				ImageFile image = null;
				if (asset.getIdiom().isAppleWatch()) {
					if (ifs.getWatchFile() != null) { image = ifs.getWatchFile(); }
				} else if (asset.getIdiom().isWatchMarketing()) {
					if (ifs.getWatchFile() != null) { image = ifs.getWatchFile(); }
				} else if (asset.getIdiom().isCarplay()) {
					if (ifs.getCarplayFile() != null) { image = ifs.getCarplayFile(); }
				} else if (asset.getIdiom().isMac()) {
					if (ifs.getMacFile() != null) { image = ifs.getMacFile(); }
				} else {
					if (asset.getIdiom().isIpad()) {
						if (ifs.getIpadIconFile() != null) { image = ifs.getIpadIconFile(); }
					}
					if (image == null) {
						if (ifs.getIcon7File() == null && ifs.getIcon6File() == null) continue;
						image = asset.getMinimumSystemVersion().prior(IOSAssetCatalogs.SYSTEM_VERSION.IOS7) ? (ifs.getIcon6File() == null ? ifs.getIcon7File() : ifs.getIcon6File()) : ifs.getIcon7File();
					}
				}
				if (image == null) continue;
				if (ifs.getWatchFile() != null && ifs.getWatchFile().getImage() == null) continue;
				if (ifs.getCarplayFile() != null && ifs.getCarplayFile().getImage() == null) continue;
				if (ifs.getMacFile() != null && ifs.getMacFile().getImage() == null) continue;
				if (!asset.getIdiom().isAppleWatch() && !asset.getIdiom().isCarplay() && !asset.getIdiom().isMac()) {
					if (asset.getIdiom().isIphone() && !this.generateIphone.isSelected()) continue;
					if (asset.getIdiom().isIpad() && !this.generateIpad.isSelected()) continue;
				}
				if (asset.getIdiom().isIphone() || asset.getIdiom().isIpad()) {
					if (minimumVersion.getSelectedIndex() == 1 && asset.getMinimumSystemVersion().prior(IOSAssetCatalogs.SYSTEM_VERSION.IOS7)) continue;
					if (minimumVersion.getSelectedIndex() == 2 && asset.getMinimumSystemVersion().prior(IOSAssetCatalogs.SYSTEM_VERSION.IOS8)) continue;
				}

				if (generateAsAssetCatalogs.isSelected()) {
					jsonUtility.add(asset);
				}

				count++;
				if (!generate) continue;

				writeIconImage(image, asset.getIOSImageInfo(), new File(ifs.getIconOutputDirectory(), asset.getFilename()), asset.getIdiom().isAppleWatch());
			}
			if (generate && generateAsAssetCatalogs.isSelected()) {
				if (this.generateAsPrerendered.isSelected()) {
					jsonUtility.setProperty(JSON_PROPERTY_KEY.PRE_RENDERED, JSON_PROPERTY_VALUE.PRE_RENDERED_TRUE);
				}
				jsonUtility.writeContentJson(new File(ifs.getIconOutputDirectory(), this.getResource("string.filename.contents.json", "Contents.json")));
			}
			jsonUtility.clear();

			// generate artwork
			if (generateArtwork.isSelected()) {
				for (IOSArtworkInfo artwork : IOSArtworkInfo.values()) {
					count++;
					if (!generate) continue;
					ImageFile defaultImage = ifs.getDefaultIconImage();
					if (defaultImage != null) {
						writeIconImage(defaultImage, artwork, new File(ifs.getOutputDirectory(), artwork.getFilename()), true);
					}
				}
			}
		}

		if (ifs.hasLaunchFile()) {
			// delete before generate
			if (this.cleanBeforeGenerate.isSelected()) {
				for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
					File f = new File(ifs.getSplashOutputDirectory(), asset.getFilename());
					if (f.exists() && f.isFile()) {
						if (!f.delete()) {
							System.err.println("Could not delete - " + f.getAbsolutePath());
						}
					}
				}
				File f = new File(ifs.getSplashOutputDirectory(), this.getResource("string.filename.contents.json", "Contents.json"));
				if (f.exists() && f.isFile()) {
					if (!f.delete()) {
						System.err.println("Could not delete - " + f.getAbsolutePath());
					}
				}
			}

			IOSAssetCatalogs.JsonUtility jsonUtility = new IOSAssetCatalogs.JsonUtility();
			HashMap<String, IOSAssetCatalogs> filesOutput = new HashMap<String, IOSAssetCatalogs>();
			// generate launch images
			for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
				ImageFile image = ifs.getSplashFile();
				if (asset.getIdiom().isIpad() && ifs.getIpadLaunchFile() != null) {
					image = ifs.getIpadLaunchFile();
				}
				if (image == null) {
					continue;
				}
				if (asset.getIdiom().isIphone() && !this.generateIphone.isSelected()) continue;
				if (asset.getIdiom().isIpad() && !this.generateIpad.isSelected()) continue;
				if (asset.getIdiom().isTv() && !this.generateTvSplash.isSelected()) continue;
				if (asset.getIdiom().isIphone() || asset.getIdiom().isIpad()) {
					if (minimumVersion.getSelectedIndex() == 1 && asset.getMinimumSystemVersion().prior(IOSAssetCatalogs.SYSTEM_VERSION.IOS7)) continue;
					if (minimumVersion.getSelectedIndex() == 2 && asset.getMinimumSystemVersion().prior(IOSAssetCatalogs.SYSTEM_VERSION.IOS8)) continue;
				}
				if (asset.getExtent() != null && asset.getExtent().equals(IOSSplashAssetCatalogs.EXTENT.TO_STATUS_BAR) && !generateOldSplashImages.isSelected()) continue;

				if (generateAsAssetCatalogs.isSelected()) {
					jsonUtility.add(asset);
				}

				if (filesOutput.containsKey(asset.getFilename())) {
					// upper version is more strong
					if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion().andLater(asset.getMinimumSystemVersion())) continue;
				}

				filesOutput.put(asset.getFilename(), asset);
				count++;
				if (!generate) continue;
				writeSplashImage(image, asset, new File(ifs.getSplashOutputDirectory(), asset.getFilename()));
			}
			if (generate && generateAsAssetCatalogs.isSelected()) {
				jsonUtility.writeContentJson(new File(ifs.getSplashOutputDirectory(), this.getResource("string.filename.contents.json", "Contents.json")));
			}
			jsonUtility.clear();
			filesOutput.clear();
		}

		return count;
	}

	/**
	 * Cancel to generate.
	 */
	private void cancelGenerate() {
		cancelRequested = true;
		if (this.isGenerating()) {
			try {
				pool.shutdownNow();
			} catch (Throwable t) {
				this.handleThrowable(t);
			}
		}
		outputPathDisplay.setText(getResource("label.cancel.generate", "Cancel generate..."));
	}

	/**
	 * delete png and json files in target directory.
	 *
	 * @param targetDirectory
	 * @return false - some files could not deleted, but continue.
	 */
	protected boolean cleanDirectory(final File targetDirectory) {
		boolean result = true;
		String[] filenames = targetDirectory.list(new FilenameFilter() {
			@Override public boolean accept(File dir, String name) {
				if (targetDirectory.equals(dir)) {
					if (!IOSImageUtil.isNullOrWhiteSpace(name)) {
						if (name.toLowerCase().trim().endsWith(".png")) {
							return true;
						}
						if (name.toLowerCase().trim().endsWith(".json")) {
							return true;
						}
					}
				}
				return false;
			}
		});
		for (String filename : filenames) {
			File f = new File(targetDirectory, filename);
			if (f.exists() && f.isFile()) {
				if (!f.delete()) {
					System.err.println("Could not delete file - " + f.getAbsolutePath());
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Write icon image to the file.
	 *
	 * @param srcImageFile		source image
	 * @param imageInfo		image information
	 * @param targetFile	file to output
	 * @param forceIntRGB	set true when generate apple watch icon
	 * @throws Exception	exception
	 */
	private void writeIconImage(final ImageFile srcImageFile, final IOSImageInfo imageInfo, final File targetFile, final boolean forceIntRGB) throws Exception {
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				try {
					boolean writable = (targetFile.exists() && targetFile.canWrite());
					if (!writable && !targetFile.exists()) {
						try { writable = targetFile.createNewFile(); } catch (Exception ex) { writable = false; }
					}
					if (!writable) {
						throw new IOException(String.format("%s [%s]", getResource("error.not.writable", "not writable"), targetFile.getAbsolutePath()));
					}
					BufferedImage buf = generateIconImage(srcImageFile.getImage(), (int)imageInfo.getSize().getWidth(), (int)imageInfo.getSize().getHeight(), forceIntRGB);
					ImageIO.write(buf, "png", targetFile);
					buf.flush();
					buf = null;
					verbose(targetFile);
					return true;
				} catch (Throwable t) {
					handleThrowable(t);
					return false;
				} finally {
					addProgress(1);
				}
			}
		};
		pool.submit(worker);
	}
	
	/**
	 * Generate icon image.
	 *
	 * @param srcImage	source image
	 * @param width		image width
	 * @param height	image height
	 * @param forceIntRGB	set true when generate apple watch icon
	 * @throws Exception	exception
	 */
	public BufferedImage generateIconImage(BufferedImage srcImage, int width, int height, boolean forceIntRGB) throws Exception {
		BufferedImage buf = this.createBufferedImage(srcImage, width, height, forceIntRGB);
		int hints = getScalingAlgorithm();
		Image img = srcImage.getScaledInstance(width, height, hints);
		if (forceIntRGB) {
			Graphics g = buf.getGraphics();
			g.setColor(ImageFile.getDefaultBackgroundColor(srcImage));
			g.fillRect(0, 0, width, height);
		}
		buf.getGraphics().drawImage(img, 0, 0, MainFrame.this);
		img.flush();
		img = null;
		return forceIntRGB ? buf : fixImageColor(buf, srcImage);
	}
	
	/**
	 * Write launch image to the file.
	 *
	 * @param srcImageFile	source image
	 * @param asset			image information
	 * @param targetFile	file to output
	 * @throws Exception	exception
	 */
	private void writeSplashImage(final ImageFile srcImageFile, final IOSSplashAssetCatalogs asset, final File targetFile) throws Exception {
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				try {
					boolean writable = (targetFile.exists() && targetFile.canWrite());
					if (!writable && !targetFile.exists()) {
						try { writable = targetFile.createNewFile(); } catch (Exception ex) { writable = false; }
					}
					if (!writable) {
						throw new IOException(String.format("%s [%s]", getResource("error.not.writable", "not writable"), targetFile.getAbsolutePath()));
					}
					int width = (int)asset.getIOSImageInfo().getSize().getWidth();
					int height = (int)asset.getIOSImageInfo().getSize().getHeight();
					BufferedImage buf = generateLaunchImage(srcImageFile.getImage(), width, height, asset);
					ImageIO.write(buf, "png", targetFile);
					buf.flush();
					buf = null;
					verbose(targetFile);
					return true;
				} catch (Throwable t) {
					handleThrowable(t);
					return false;
				} finally {
					addProgress(1);
				}
			}
		};
		pool.submit(worker);
	}
	
	/**
	 * Write launch image to the file.
	 *
	 * @param srcImageFile		source image
	 * @param width		image width
	 * @param height	image height
	 * @param assetCatalogs		image information
	 * @throws Exception	exception
	 */
	public BufferedImage generateLaunchImage(BufferedImage srcImage, int width, int height, IOSAssetCatalogs assetCatalogs) throws Exception {
		BufferedImage buf = this.createBufferedImage(srcImage, width, height, false);
		Graphics g = buf.getGraphics();
		if (!IOSImageUtil.isNullOrWhiteSpace(splashBackgroundColor.getText()) && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
			g.setColor(ImageFile.getDefaultBackgroundColor(srcImage));
			Color col = new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true);
			if (col != null) g.setColor(col);
			g.fillRect(0, 0, width, height);
		}

		// Default is fit with aspect ratio.
		double p = (double)width / (double)srcImage.getWidth();
		if (Math.floor((double)srcImage.getWidth() * p) > width || Math.floor((double)srcImage.getHeight() * p) > height) {
			p = (double)height / (double)srcImage.getHeight();
		}
		if (splashScaling.getSelectedIndex() == 0) {
			// No resizing(iPhone only)
			if (assetCatalogs.getIdiom().isIphone()) p = assetCatalogs.getIOSImageInfo().getScale().greaterThan(IOSImageInfo.SCALE.x1) ? 1.0d : 0.5d;
		} else if (splashScaling.getSelectedIndex() == 1) {
			// No resizing(iPhone & iPad)
			p = assetCatalogs.getIOSImageInfo().getScale().greaterThan(IOSImageInfo.SCALE.x1) ? 1.0d : 0.5d;
		} else if (splashScaling.getSelectedIndex() == 2) {
			// Fit to the screen height(iPhone only)
			if (assetCatalogs.getIdiom().isIphone()) p = (double)height / (double)srcImage.getHeight();
		} else if (splashScaling.getSelectedIndex() == 4) {
			p = (width < height) ? (double)height / (double)srcImage.getHeight() : (double)width / (double)srcImage.getWidth();
		}// else default
		int w = (int) ((double)srcImage.getWidth() * p);
		int h = (int) ((double)srcImage.getHeight() * p);
		if (splashScaling.getSelectedIndex() == 5) {
			w = width;
			h = height;
		}
   		int x = (int) ((width - w) / 2);
   		int y = (int) ((height - h) / 2);
		int hints = getScalingAlgorithm();
		Image img = srcImage.getScaledInstance(w, h, hints);
		buf.getGraphics().drawImage(img, x, y, MainFrame.this);
		img.flush();
		img = null;
		return fixImageColor(buf, srcImage);
	}
	
	
	protected BufferedImage createBufferedImage(BufferedImage src, int width, int height, boolean forceIntRGB) {
		if (forceIntRGB) {
			return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}
		
		int type = ((ComboBoxItem)imageType.getSelectedItem()).getItemValue();
		if (type == BufferedImage.TYPE_CUSTOM) {
			type = src.getType();
		}
		BufferedImage tmp = new BufferedImage(1, 1, type);
		ColorModel scm = src.getColorModel();
		ColorModel dcm = tmp.getColorModel();
		tmp.flush();
		tmp = null;
		if (scm instanceof IndexColorModel && ((IndexColorModel)scm).getTransparentPixel() < 0 && dcm instanceof IndexColorModel) {
			return new BufferedImage(width, height, type, (IndexColorModel)scm);
		}
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Apply source image type or combobox image type.
	 *
	 * @param buf buffered image
	 * @param src source image
	 * @return fixed image
	 */
	protected BufferedImage fixImageColor(BufferedImage buf, BufferedImage src) {
		try {
			ColorModel model = buf.getColorModel();
			if (model instanceof IndexColorModel && ((IndexColorModel) model).getTransparentPixel() < 0) {
				return buf;
			}
			
			int srcColorType = src.getType();
			int dstColorType = ((ComboBoxItem)imageType.getSelectedItem()).getItemValue();
			if ((srcColorType != BufferedImage.TYPE_INT_ARGB && srcColorType != BufferedImage.TYPE_CUSTOM) || (dstColorType != BufferedImage.TYPE_INT_ARGB && dstColorType != BufferedImage.TYPE_CUSTOM)) {
				BufferedImage tmp = null;
				ColorModel cm = src.getColorModel();
				if ((cm instanceof IndexColorModel) && cm.hasAlpha()) {
					if (dstColorType != BufferedImage.TYPE_CUSTOM) {
						tmp = new BufferedImage(1, 1, dstColorType);
						if (tmp.getColorModel() instanceof IndexColorModel) {
							tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), srcColorType, (IndexColorModel)cm);
						} else {
							tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), dstColorType);
						}
					} else {
						tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), srcColorType, (IndexColorModel)cm);
					}
				} else {
					if (dstColorType != BufferedImage.TYPE_CUSTOM) {
						tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), dstColorType);
						if (cm.hasAlpha() && (tmp.getColorModel() instanceof IndexColorModel)) {
							// index color with transparent
							tmp.getGraphics().drawImage(buf, 0, 0, this);
							IndexColorModel icm = (IndexColorModel)tmp.getColorModel();
							int mapSize = icm.getMapSize();
							byte[] reds = new byte[mapSize];
							byte[] greens = new byte[mapSize];
							byte[] blues = new byte[mapSize];
							icm.getReds(reds);
							icm.getGreens(greens);
							icm.getBlues(blues);
							tmp.flush();
							tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), dstColorType, new IndexColorModel(8, mapSize, reds, greens, blues, 0));
						}
					} else {
						tmp = new BufferedImage(buf.getWidth(), buf.getHeight(), srcColorType);
					}
				}
				tmp.getGraphics().drawImage(buf, 0, 0, this);
				if (cm.hasAlpha() && (tmp.getColorModel() instanceof IndexColorModel) && tmp.getColorModel().hasAlpha()) {
					// fix transparent
					ColorModel tmpColorModel = tmp.getColorModel();
					for (int x = 0; x < buf.getWidth(); x++) {
						for (int y = 0; y < buf.getHeight(); y++) {
							int bufRGB = buf.getRGB(x, y);
							if (bufRGB == 0) {
								tmp.setRGB(x, y, 0);
							} else if (tmpColorModel.getAlpha(tmp.getRGB(x, y)) == 0) {
								tmp.setRGB(x, y, bufRGB);
							}
						}
					}
				}
				buf.flush();
				return tmp;
			}
		} catch (IllegalArgumentException ex) {
			System.out.println("Failed apply color model. TYPE_INT_ARGB applied. (" + ex.getMessage() + ")");
		}
		return buf;
	}
	
	protected IndexColorModel getIndexColorModel(BufferedImage src) {
		// index color with transparent
		ColorModel cm = src.getColorModel();
		if (cm instanceof IndexColorModel) {
			IndexColorModel icm = (IndexColorModel)cm;
			int mapSize = icm.getMapSize();
			byte[] reds = new byte[mapSize];
			byte[] greens = new byte[mapSize];
			byte[] blues = new byte[mapSize];
			icm.getReds(reds);
			icm.getGreens(greens);
			icm.getBlues(blues);
			return new IndexColorModel(8, mapSize, reds, greens, blues, 0);
		}
		return null;
	}

	/**
	 * The error check of the file path.
	 *
	 * @param path	file path
	 * @return	null when error occured
	 */
	private ImageFile checkFile(JTextField path) {
		try {
			File f = new File(path.getText());
			if (!path.getText().equals(f.getCanonicalPath())) {
				path.setText(f.getCanonicalPath());
				f = new File(path.getText());
			}
			if (!f.exists()) {
				alert("[" + f.getCanonicalPath() + "] " + getResource("error.not.exists", "is not exists."));
				return null;
			}
			if (f.isDirectory()) {
				alert("[" + f.getCanonicalPath() + "] " + getResource("error.not.file", "is directory. Choose file."));
				return null;
			}

			BufferedImage image = ImageIO.read(f);
			if (image == null) {
				alert("[" + f.getCanonicalPath() + "] " + getResource("error.illegal.image", "is illegal image."));
				return null;
			}

			return new ImageFile(image, f);
		} catch (Throwable t) {
			handleThrowable(t);
		}
		return null;
	}

	/**
	 * Get resource string.
	 *
	 * @param key	key
	 * @param def	default string
	 * @return	resource string
	 */
	protected String getResource(String key, String def) {
		if (resource.containsKey(key)) {
			String s = resource.getString(key);
			if (s.startsWith("\"")) {
				s = s.substring(1);
			}
			if (s.endsWith("\"")) {
				s = s.substring(0, s.length() - 1);
			}
			return s;
		}
		return def;
	}

	/**
	 * Check for updates.
	 *
	 * @param gui true - show alert dialog
	 * @return
	 */
	private boolean checkForUpdates(final boolean gui) {
		menuItemCheckForUpdates.setEnabled(false);
		menuItemExecuteUpdate.setEnabled(false);
		boolean result = false;
		try {
			String localVersion = IOSImageUtil.getVersion(IOSImageUtil.getLocalVersionURL(MainFrame.this));
			if (localVersion == null) {
				System.err.println("Local Version not found.");
				return false;
			}
			String remoteVersion = IOSImageUtil.getVersion(new URL(MainFrame.this.getResource("url.version.file", "https://raw.githubusercontent.com/gootara-org/ios-image-util/master/ios-image-util/src/org/gootara/ios/image/util/ui/version.txt")));
			if (remoteVersion == null) {
				if (gui) {
					alert(getResource("update.error.remote.version", "Error. Check network connection."));
				} else {
					System.err.println(getResource("update.error.remote.version", "Error. Check network connection."));
				}
				return false;
			}
			if (localVersion.compareTo(remoteVersion) < 0) {
				ImageIcon noticeIcon = new ImageIcon(MainFrame.this.getClass().getResource("img/notice.png"));
				helpMenu.setIcon(noticeIcon);
				menuItemExecuteUpdate.setIcon(noticeIcon);
				menuItemExecuteUpdate.setEnabled(true);
				menuItemExecuteUpdate.setText(String.format("%s (%s)", getResource("menu.help.execute.update", "Update Jar file"), remoteVersion));
			} else {
				if (gui) {
					information(getResource("update.already.latest", "Current version is the latest."));
				} else {
					System.err.println(getResource("update.already.latest", "Current version is the latest."));
				}
			}
			result = true;
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			menuItemCheckForUpdates.setEnabled(true);
		}
		return result;
	}

	/**
	 * Check for Updates in background.
	 */
	public void checkForUpdatesInBackground() {
		if (!IOSImageUtil.isExecutableJarFile(this)) {
			System.out.println("Check for Updates only available with executed by jar file.");
			return;
		}

		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				return checkForUpdates(false);
			}
		};
		worker.execute();
	}

	/**
	 * Excecute update.
	 */
	private void executeUpdate() {
		if (!IOSImageUtil.isExecutableJarFile(this)) {
			System.out.println("Execute update only available with executed by jar file.");
			return;
		}

		menuItemExecuteUpdate.setEnabled(false);
		FileOutputStream fout = null;
		InputStream ins = null;
		try {
			String remoteVersion = IOSImageUtil.getVersion(new URL(MainFrame.this.getResource("url.version.file", "https://raw.githubusercontent.com/gootara-org/ios-image-util/master/ios-image-util/src/org/gootara/ios/image/util/ui/version.txt")));
			if (remoteVersion == null) {
				alert(getResource("update.error.remote.version", "Error. Check network connection."));
				return;
			}
			File currentJarFile = IOSImageUtil.getExecutableJarFile(this);
			File newJarFile = new File(currentJarFile.getParentFile(), String.format("ios-image-util_%s.jar", remoteVersion));
			if (newJarFile.exists()) {
				alert(String.format(getResource("update.already.exists", "Same version is already downloaded. Use this file at the next time.\n[%s]"), newJarFile.getAbsolutePath()));
				return;
			}

			URL url = new URL(this.getResource("url.latest.jar.file", "https://raw.githubusercontent.com/gootara-org/ios-image-util/master/ios-image-util/jar/ios-image-util.jar"));
			File temporaryFile = new File(currentJarFile.getParentFile(), "ios-image-util.tmp");
			byte[] buf = new byte[4096];
			int size;
			ins = url.openStream();
			fout = new FileOutputStream(temporaryFile);
			while ((size = ins.read(buf)) != -1) {
				fout.write(buf, 0, size);
			}
			fout.close();
			ins.close();

			if (temporaryFile.renameTo(newJarFile)) {
				information(String.format(getResource("update.success.download", "The laatest jar file was downloaded. Use new jar file at the next time.\n[%s]"), newJarFile.getAbsolutePath()));
			} else {
				System.err.println("Delete temporary file - " + temporaryFile.delete());
				alert(getResource("error.update.download", "Error. The latest jar file was not downloaded correctly."));
			}

		} catch (Exception ex) {
			handleThrowable(ex);
		} finally {
			if (fout != null) { try { fout.close(); ins = null; } catch (Exception ex) { ex.printStackTrace(); } }
			if (ins != null) { try { ins.close(); ins = null; } catch (Exception ex) { ex.printStackTrace(); } }
			helpMenu.setIcon(null);
			menuItemExecuteUpdate.setIcon(null);
		}
	}
}

class ImageFile
{
	private BufferedImage image;
	private File file;

	/**
	 * Constructor.
	 *
	 * @param image	image
	 * @param file	file
	 */
	public ImageFile(BufferedImage image, File file) {
		this.setImage(image);
		this.setFile(file);
	}

	/**
	 * Get image.
	 *
	 * @return image
	 */
	public BufferedImage getImage() {
		return image;
	}
	/**
	 * Set image.
	 *
	 * @param image set image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	/**
	 * Get file
	 *
	 * @return file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * Set file
	 *
	 * @param file set file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Clear image buffer.
	 */
	public void clear() {
		if (image != null) image.flush();
		image = null;
		file = null;
	}

	/**
	 * Guess background color without alpha channel.
	 *
	 * @return guessed color
	 */
	public Color getDefaultBackgroundColor() {
		return ImageFile.getDefaultBackgroundColor(this.getImage());
	}
	
	/**
	 * Guess background color without alpha channel.
	 *
	 * @return guessed color
	 */
	public static Color getDefaultBackgroundColor(BufferedImage img) {
		Color c = null;
		for (int xy = 0; xy < Math.min(img.getWidth(), img.getHeight()); xy++) {
			Color col = new Color(img.getRGB(xy, xy), true);
			if (col.getAlpha() != 0) {
				c = new Color(col.getRGB(), false);
				break;
			}
		}
		return c == null ? new Color(img.getRGB(0, 0), false) : c;
	}
}

class ImageFileSet
{
	private ImageFile icon6File;
	private ImageFile watchFile;
	private ImageFile carplayFile;
	private ImageFile icon7File;
	private ImageFile macFile;
	private ImageFile ipadIconFile;
	private File iconOutputDirectory;
	private ImageFile splashFile;
	private ImageFile ipadLaunchFile;
	private File splashOutputDirectory;
	private File outputDirectory;

	public ImageFile getDefaultIconImage() {
		ImageFile defaultImage = null;
		if (getIcon7File() != null) { defaultImage = getIcon7File(); }
		if (defaultImage == null && getIpadIconFile() != null) { defaultImage = getIpadIconFile(); }
		if (defaultImage == null && getWatchFile() != null) { defaultImage = getWatchFile(); }
		if (defaultImage == null && getIcon6File() != null) { defaultImage = getIcon6File(); }
		if (defaultImage == null && getCarplayFile() != null) { defaultImage = getCarplayFile(); }
		if (defaultImage == null && getMacFile() != null) { defaultImage = getMacFile(); }
		return defaultImage;
	}

	public boolean hasIconFile() {
		return getIcon6File() != null
				|| getIcon7File() != null
				|| getWatchFile() != null
				|| getCarplayFile() != null
				|| getMacFile() != null
				|| getIpadIconFile() != null
		;
	}

	public boolean hasLaunchFile() {
		return getSplashFile() != null || getIpadLaunchFile() != null;
	}
	/**
	 * @return icon6File
	 */
	public ImageFile getIcon6File() {
		return icon6File;
	}
	/**
	 * @param icon6File set icon6File
	 */
	public void setIcon6File(ImageFile icon6File) {
		this.icon6File = icon6File;
	}
	/**
	 * @return icon7File
	 */
	public ImageFile getIcon7File() {
		return icon7File;
	}
	/**
	 * @param icon7File set icon7File
	 */
	public void setIcon7File(ImageFile icon7File) {
		this.icon7File = icon7File;
	}
	/**
	 * @return iconOutputDirectory
	 */
	public File getIconOutputDirectory() {
		return iconOutputDirectory;
	}
	/**
	 * @param iconOutputDirectory set iconOutputDirectory
	 */
	public void setIconOutputDirectory(File iconOutputDirectory) {
		this.iconOutputDirectory = iconOutputDirectory;
	}
	/**
	 * @return splashFile
	 */
	public ImageFile getSplashFile() {
		return splashFile;
	}
	/**
	 * @param splashFile set splashFile
	 */
	public void setSplashFile(ImageFile splashFile) {
		this.splashFile = splashFile;
	}
	/**
	 * @return splashOutputDirectory
	 */
	public File getSplashOutputDirectory() {
		return splashOutputDirectory;
	}
	/**
	 * @param splashOutputDirectory set splashOutputDirectory
	 */
	public void setSplashOutputDirectory(File splashOutputDirectory) {
		this.splashOutputDirectory = splashOutputDirectory;
	}
	/**
	 * @return outputDirectory
	 */
	public File getOutputDirectory() {
		return outputDirectory;
	}
	/**
	 * @param outputDirectory set outputDirectory
	 */
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	/**
	 * @return watchFile
	 */
	public ImageFile getWatchFile() {
		return watchFile;
	}
	/**
	 * @param watchFile set watchFile
	 */
	public void setWatchFile(ImageFile watchFile) {
		this.watchFile = watchFile;
	}
	/**
	 * @return carplayFile
	 */
	public ImageFile getCarplayFile() {
		return carplayFile;
	}
	/**
	 * @param carplayFile set carplayFile
	 */
	public void setCarplayFile(ImageFile carplayFile) {
		this.carplayFile = carplayFile;
	}

	/**
	 * @return macFile
	 */
	public ImageFile getMacFile() {
		return macFile;
	}

	/**
	 * @param macFile set macFile
	 */
	public void setMacFile(ImageFile macFile) {
		this.macFile = macFile;
	}

	/**
	 * @return ipadIconFile
	 */
	public ImageFile getIpadIconFile() {
		return ipadIconFile;
	}

	/**
	 * @param ipadIconFile set ipadIconFile
	 */
	public void setIpadIconFile(ImageFile ipadIconFile) {
		this.ipadIconFile = ipadIconFile;
	}

	/**
	 * @return ipadLaunchFile
	 */
	public ImageFile getIpadLaunchFile() {
		return ipadLaunchFile;
	}

	/**
	 * @param ipadLaunchFile set ipadLaunchFile
	 */
	public void setIpadLaunchFile(ImageFile ipadLaunchFile) {
		this.ipadLaunchFile = ipadLaunchFile;
	}

	public void clear() {
		if (icon6File != null) icon6File.clear();
		if (watchFile != null) watchFile.clear();
		if (carplayFile != null) carplayFile.clear();
		if (icon7File != null) icon7File.clear();
		if (splashFile != null) splashFile.clear();
		if (macFile != null) macFile.clear();
		if (ipadIconFile != null) ipadIconFile.clear();
		if (ipadLaunchFile != null) ipadLaunchFile.clear();
		icon6File = null;
		watchFile = null;
		carplayFile = null;
		iconOutputDirectory = null;
		splashFile = null;
		splashOutputDirectory = null;
		outputDirectory = null;
		macFile = null;
		ipadIconFile = null;
		ipadLaunchFile = null;
	}
}

/**
 * ComboBox item.
 *
 * @author gootara.org
 */
class ComboBoxItem {
	private int itemValue;
	private String itemName;

	/**
	 * Constructor.
	 *
	 * @param value	value
	 * @param name	value to display
	 */
	public ComboBoxItem(int value, String name) {
		this.setItemValue(value);
		this.setItemName(name);
	}

	/**
	 * Get item value.
	 *
	 * @return itemValue
	 */
	public int getItemValue() {
		return itemValue;
	}

	/**
	 * Set item value.
	 *
	 * @param itemValue set itemValue
	 */
	public void setItemValue(int itemValue) {
		this.itemValue = itemValue;
	}

	/**
	 * Get item value to display.
	 *
	 * @return itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * Set item value to display.
	 *
	 * @param itemName set itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override public String toString() {
		return this.getItemName();
	}

}

class SimpleShutterAnimation
{
static final int DIRECTION_UP = 0;
static final int DIRECTION_DOWN = 1;
private Component target;
private Timer timer;
private ActionListener callbackListener;
private double base;
private int step;
private int direction;

	public SimpleShutterAnimation(Component target) {
		this(target, 0d);
	}

	public SimpleShutterAnimation(Component target, double base) {
		this.target = target;
		this.base = base;
		((JComponent)target).setDoubleBuffered(true);
	}

	public boolean isTargetAtBaseLocation() {
		return target.getY() == 0 - this.base;
	}

	public boolean animate(ActionListener callback) {
		if (timer != null && timer.isRunning()) {
			return false;
		}

		this.callbackListener = callback;
		this.direction = isTargetAtBaseLocation() ? SimpleShutterAnimation.DIRECTION_UP : SimpleShutterAnimation.DIRECTION_DOWN;
		step = 0;
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (step < 100) {
					double h = target.getHeight() - base;
					target.setLocation(new Point(target.getX(), new Double(h * Math.pow(2.98, -0.054 * step) * Math.cos(0.086 * step) - (direction == DIRECTION_UP ? h : 0 - base)).intValue() * (direction == DIRECTION_UP ? 1 : -1)));
					step++;
				} else {
					target.setLocation(new Point(target.getX(), direction == DIRECTION_UP ? target.getBounds().y : new Double(0 - base).intValue()));
					timer.stop();
					if (callbackListener != null) {
						callbackListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "finish"));
					}
				}
			}
		});
		timer.start();
		return true;
	}
}

interface AssetImageGenerator {
	
	public BufferedImage generateIconImage(BufferedImage srcImage, int width, int height, boolean forceIntRGB) throws Exception;
	public BufferedImage generateLaunchImage(BufferedImage srcImage, int width, int height, IOSAssetCatalogs assetCatalogs) throws Exception;

}
