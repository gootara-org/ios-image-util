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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gootara.ios.image.util.IOSArtworkInfo;
import org.gootara.ios.image.util.IOSAssetCatalogs;
import org.gootara.ios.image.util.IOSIconAssetCatalogs;
import org.gootara.ios.image.util.IOSImageInfo;
import org.gootara.ios.image.util.IOSSplashAssetCatalogs;

/**
 * The main window frame of IOSImageUtil.
 *
 * @author gootara.org
 */
public class MainFrame extends JFrame {
	public static final String PLACEHOLDER_SPLASH_BGCOL = "(e.g. ffffff)";
	private ResourceBundle resource;
	private JTextField icon6Path, icon7Path, splashPath, outputPath, splashBackgroundColor, carplayPath, watchPath;
	private JComboBox scaleAlgorithm, splashScaling, imageType, optionalIcons;
	private ImagePanel icon6Image, icon7Image, splashImage, carplayImage, watchImage;
	private JProgressBar progress;
	private JCheckBox generateOldSplashImages, generateAsAssetCatalogs, generateArtwork;
	private JRadioButton iPhoneOnly, iPadOnly, iBoth;
	private JButton generateButton, settingsButton, cancelButton, splitButton;
	private JLabel outputPathDisplay;
	private SimpleShutterAnimation animator;
	private SplitterFrame splitter;
	private File splitTarget = null;
	private boolean batchMode = false;
	private boolean silentMode = false;
	private boolean verboseMode = false;
	private boolean cancelRequested = false;

	public static final Color COLOR_DARK_GRAY = new Color(0x727284);
	public static final Color COLOR_CYAN = new Color(0x2399CD);
	public static final Color BGCOLOR_LIGHT_GRAY = new Color(0xf7f7f7);
	// Anti-ailiasing for microsoft windows.

	/**
	 * Constructor.
	 */
	protected void frameInit() {
		super.frameInit();
		resource = ResourceBundle.getBundle("application");
		this.setTitle(getResource("window.title", "iOS Image Util"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		Font fontNormal = new Font(getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 12);
		Font fontExtraLarge = new Font(getResource("font.default.name", Font.SANS_SERIF), Font.BOLD, 16);

		final JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(620, 280));
		this.add(layeredPane, BorderLayout.NORTH);

		final JPanel settings = new JPanel();
		settings.setPreferredSize(layeredPane.getPreferredSize());
		layeredPane.add(settings, JLayeredPane.DEFAULT_LAYER);

		// iOS6 Icon Path.
		// -> Optional Icons
		Vector<String> oiItems = new Vector<String>();
		oiItems.add(getResource("item.icon6.path", "Icon for iOS6"));
		oiItems.add(getResource("item.applewatch.path", "Icon for Apple Watch"));
		oiItems.add(getResource("item.carplay.path", "Icon for CarPlay"));
		optionalIcons = new JComboBox(oiItems);
		optionalIcons.setFont(optionalIcons.getFont().deriveFont(Font.PLAIN, 11.0f));
		optionalIcons.setToolTipText(getResource("tooltip.optional.icons", "Paths for optional icon. ( if necessary, not required )"));
		optionalIcons.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				icon6Path.setVisible(optionalIcons.getSelectedIndex() == 0);
				watchPath.setVisible(optionalIcons.getSelectedIndex() == 1);
				carplayPath.setVisible(optionalIcons.getSelectedIndex() == 2);
			}
		});

		JButton refIcon6Path = new JButton("...");
		refIcon6Path.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(icon6Path, icon6Image);
			}
		});

		JPanel optionalIconBase = new JPanel();
		optionalIconBase.setLayout(new GridLayout(1, 2, 5, 0));
		optionalIconBase.add(new JLabel(getResource("label.optional.icons", "Optional Icons:"), SwingConstants.RIGHT));
		optionalIconBase.add(optionalIcons);

		settings.add(optionalIconBase);
		settings.add(icon6Path = new JTextField());
		settings.add(refIcon6Path);
		settings.add(carplayPath = new JTextField());
		settings.add(watchPath = new JTextField());
		icon6Path.setVisible(false);
		watchPath.setVisible(true);
		carplayPath.setVisible(false);
		optionalIcons.setSelectedIndex(1);

		// iOS7 Icon Path.
		JLabel icon7Label = new JLabel(getResource("label.icon7.path", " iOS7 Icon PNG (1024x1024):"), SwingConstants.RIGHT);
		icon7Path = new JTextField();
		JButton refIcon7Path = new JButton("...");
		refIcon7Path.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(icon7Path, icon7Image);
			}
		});
		settings.add(icon7Label);
		settings.add(icon7Path);
		settings.add(refIcon7Path);

		// Splash Image Path.
		JLabel splashLabel = new JLabel(getResource("label.splash.path", "      Splash PNG (2048x2048):"), SwingConstants.RIGHT);
		splashPath = new JTextField();
		JButton refSplashPath = new JButton("...");
		refSplashPath.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(splashPath, splashImage);
			}
		});
		settings.add(splashLabel);
		settings.add(splashPath);
		settings.add(refSplashPath);
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
		scaleAlgorithmPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
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
		JLabel splashScalingLabel = new JLabel(getResource("label.splash.image", "Splash:"));
		JPanel splashScalingPanel = new JPanel();
		splashScalingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
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
		imageTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
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
					splashBackgroundColor.setForeground(Color.BLACK);
				}
			}
			@Override public void focusLost(FocusEvent e) {
				setSplashBackgroundColor(splashBackgroundColor.getText());
			}
		});
		JButton refSplashBackgroundColor = new JButton("...");
		refSplashBackgroundColor.setFont(refSplashBackgroundColor.getFont().deriveFont(Font.PLAIN, 11.0f));
		refSplashBackgroundColor.addActionListener(new ActionListener() {
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
		splashBackgroundColorPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		splashBackgroundColorPanel.add(splashBackgroundColorLabel);
		splashBackgroundColorPanel.add(splashBackgroundColor);
		splashBackgroundColorPanel.add(refSplashBackgroundColor);
		settings.add(splashBackgroundColorPanel);

		// Checkboxes and Radio Buttons.
		JPanel generateOptions = new JPanel();
		//generateOptions.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		generateOptions.setLayout(new BoxLayout(generateOptions, BoxLayout.LINE_AXIS));
		generateOptions.add(new JLabel(getResource("label.generate.options", "Options:")));
		generateOptions.add(this.iBoth = new JRadioButton(getResource("label.output.both", "Output Both"), true));
		generateOptions.add(this.iPhoneOnly = new JRadioButton(getResource("label.iphone.only", "iPhone Only")));
		generateOptions.add(this.iPadOnly = new JRadioButton(getResource("label.ipad.only", "iPad Only")));
		generateOptions.add(Box.createHorizontalStrut(5));
		generateOptions.add(new JSeparator(SwingConstants.VERTICAL));
		generateOptions.add(Box.createHorizontalStrut(5));
		generateOptions.add(this.generateArtwork = new JCheckBox(getResource("label.generate.artwork", "Artwork"), true));
		generateOptions.add(this.generateOldSplashImages = new JCheckBox(getResource("label.generate.old.splash", "Generate Old Splash Images"), false));
		generateArtwork.setToolTipText(getResource("tooltip.generate.artwork", "Generate iTunes Store Artwork images."));
		generateOldSplashImages.setToolTipText(getResource("tooltip.generate.old.splash", "Generate \"to-status-bar\" launch images specified for iOS 6 iPad."));
		ButtonGroup group = new ButtonGroup();
		group.add(this.iBoth);
		group.add(this.iPhoneOnly);
		group.add(this.iPadOnly);
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

		// Output Path.
		outputPath = new JTextField();
		outputPath.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText())); }
			@Override public void insertUpdate(DocumentEvent e) { outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText())); }
			@Override public void removeUpdate(DocumentEvent e) { outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText())); }
		});
		JButton refOutputPath = new JButton("...");
		JLabel outputPathLabel = new JLabel(getResource("label.output.path", "Output Dir:"), SwingConstants.RIGHT);
		refOutputPath.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (outputPath.getText().trim().length() > 0) {
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
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(outputPath, chooser.getSelectedFile(), null);
			    }
			}
		});
		settings.add(outputPathLabel, BorderLayout.WEST);
		settings.add(outputPath, BorderLayout.CENTER);
		settings.add(refOutputPath, BorderLayout.EAST);

		SpringLayout layout = new SpringLayout();
		settings.setLayout(layout);
		// Layout for iOS 6 Path
		layout.putConstraint(SpringLayout.WEST, optionalIconBase, 5, SpringLayout.WEST, settings);
//		layout.putConstraint(SpringLayout.EAST, optionalIconBase, 0, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.NORTH, optionalIconBase, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.EAST, refIcon6Path, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refIcon6Path, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.WEST, icon6Path, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, icon6Path, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, icon6Path, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.WEST, watchPath, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, watchPath, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, watchPath, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.WEST, carplayPath, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, carplayPath, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, carplayPath, 25, SpringLayout.NORTH, settings);

		// Layout for iOS 7 Path
		layout.putConstraint(SpringLayout.WEST, icon7Label, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, icon7Label, 0, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.NORTH, icon7Label, 7, SpringLayout.SOUTH, refIcon6Path);
		layout.putConstraint(SpringLayout.EAST, refIcon7Path, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refIcon7Path, 5, SpringLayout.SOUTH, refIcon6Path);
		layout.putConstraint(SpringLayout.WEST, icon7Path, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, icon7Path, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, icon7Path, 5, SpringLayout.SOUTH, refIcon6Path);

		// Layout for Launch Image Path
		layout.putConstraint(SpringLayout.WEST, splashLabel, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, splashLabel, 0, SpringLayout.EAST, optionalIconBase);
		layout.putConstraint(SpringLayout.NORTH, splashLabel, 7, SpringLayout.SOUTH, refIcon7Path);
		layout.putConstraint(SpringLayout.EAST, refSplashPath, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refSplashPath, 5, SpringLayout.SOUTH, refIcon7Path);
		layout.putConstraint(SpringLayout.WEST, splashPath, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, splashPath, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, splashPath, 5, SpringLayout.SOUTH, refIcon7Path);

		// Layout comboboxes and checkboxes.
		layout.putConstraint(SpringLayout.WEST, separatorNorth, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, separatorNorth, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, separatorNorth, 10, SpringLayout.SOUTH, refSplashPath);
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
		layout.putConstraint(SpringLayout.WEST, generateAsAssetCatalogs, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.NORTH, generateAsAssetCatalogs, 6, SpringLayout.SOUTH, separatorSouth);
		layout.putConstraint(SpringLayout.WEST, outputPathLabel, 8, SpringLayout.EAST, generateAsAssetCatalogs);
		layout.putConstraint(SpringLayout.NORTH, outputPathLabel, 10, SpringLayout.SOUTH, separatorSouth);
		layout.putConstraint(SpringLayout.EAST, refOutputPath, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refOutputPath, 8, SpringLayout.SOUTH, separatorSouth);
		layout.putConstraint(SpringLayout.WEST, outputPath, 5, SpringLayout.EAST, outputPathLabel);
		layout.putConstraint(SpringLayout.EAST, outputPath, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, outputPath, 8, SpringLayout.SOUTH, separatorSouth);



		// Image Panels.
		icon7Image = initImagePanel(new ImagePanel(getResource("label.icon7.drop", "Drop iOS7 Icon PNG Here")), icon7Path);
		icon7Image.setForeground(COLOR_CYAN);//0x34AADC));//0x007AFF));//0x34AADC));
		splashImage = initImagePanel(new ImagePanel(getResource("label.splash.drop", "Drop Splash Image PNG Here")), splashPath);

		icon6Image = initImagePanel(new ImagePanel(getResource("label.icon6.drop", "Drop iOS6 Icon PNG Here")), icon6Path);
		carplayImage = initImagePanel(new ImagePanel(getResource("label.carplay.drop", "Icon for CarPlay\n( Optional )")), carplayPath);
		watchImage = initImagePanel(new ImagePanel(getResource("label.watch.drop", "Drop Apple Watch Icon PNG Here\n( if necessary, not required )")), watchPath);

		JPanel optionalIconImages = new JPanel();
		optionalIconImages.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImages.setLayout(new GridLayout(2, 1, 0, 2));
		JPanel optionalIconImagesSub = new JPanel();
		optionalIconImagesSub.setBackground(BGCOLOR_LIGHT_GRAY);
		optionalIconImagesSub.setLayout(new GridLayout(1, 2, 2, 0));
		optionalIconImagesSub.add(carplayImage);
		optionalIconImagesSub.add(icon6Image);
		optionalIconImages.add(watchImage);
		optionalIconImages.add(optionalIconImagesSub);

		JPanel imagesPanel = new JPanel();
		imagesPanel.setBorder(new LineBorder(BGCOLOR_LIGHT_GRAY, 4));
		imagesPanel.setBackground(BGCOLOR_LIGHT_GRAY);
		imagesPanel.setLayout(new GridLayout(1, 3, 2, 2));
		imagesPanel.add(optionalIconImages);
		imagesPanel.add(icon7Image);
		imagesPanel.add(splashImage);
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

		generateButton = initMenuButton(new JButton(getResource("button.generate", "Generate"), new ImageIcon(this.getClass().getResource("img/generate.png"))), Color.WHITE, new Color(0xFF4981), fontExtraLarge);
		generateButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/generate_rollover.png")));
		generateButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		surface.add(generateButton);

		cancelButton = initMenuButton(new JButton(getResource("button.cancel", "Cancel"), new ImageIcon(this.getClass().getResource("img/generate.gif"))), new Color(0x8E8E93), new Color(0xf7f7f7), fontExtraLarge);
		cancelButton.setRolloverEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelGenerate();
			}

		});
		surface.add(cancelButton);
		cancelButton.setVisible(false);

		settingsButton = initMenuButton(new JButton(getResource("button.settings", "Settings"), new ImageIcon(this.getClass().getResource("img/settings.png"))), Color.WHITE, new Color(0x4CD964), fontExtraLarge);
		settingsButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/settings_disabled.png")));
		settingsButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/settings_rollover.png")));
		surface.add(settingsButton);

		splitButton = initMenuButton(new JButton(getResource("button.split", "Split"), new ImageIcon(this.getClass().getResource("img/splitter.png"))), Color.WHITE, new Color(0x007AFF), fontExtraLarge);
		splitButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/splitter_disabled.png")));
		splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/splitter_rollover.png")));
		final JFrame dialogOwner = this;
		splitButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (!splitter.isVisible()) {
					Point p = new Point(dialogOwner.getX() + dialogOwner.getWidth() + 4, dialogOwner.getY());
					if (java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width < p.x + splitter.getWidth()) {
						splitter.setLocationRelativeTo(dialogOwner);
					} else {
						splitter.setLocation(p);
					}
				}
				splitter.setVisible(!splitter.isVisible());
			}
		});
		surface.add(splitButton);

		progress = new JProgressBar(0, IOSIconAssetCatalogs.values().length + IOSArtworkInfo.values().length + IOSSplashAssetCatalogs.values().length);
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setBorderPainted(false);
		progress.setBackground(BGCOLOR_LIGHT_GRAY);//new Color(0xF7F7F7));
		progress.setForeground(COLOR_CYAN);//new Color(0x007AFF));
		progress.setOpaque(true);
		surface.add(progress);

		outputPathDisplay = new JLabel("");
		outputPathDisplay.setForeground(COLOR_CYAN);//new Color(0x34AADC));//0x007AFF));//0x34AADC));
		outputPathDisplay.setFont(fontNormal);
		surface.add(outputPathDisplay);

		final JButton gripeButton = new JButton(new ImageIcon(this.getClass().getResource("img/gripe.png")));
		gripeButton.setBackground(Color.WHITE);
		gripeButton.setBorderPainted(false);
		gripeButton.setFocusPainted(false);
		gripeButton.setRolloverEnabled(true);
		gripeButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/gripe_rollover.png")));
		gripeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		gripeButton.setMargin(null);

		settingsButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				animator.animate(new ActionListener() {
					@Override public void actionPerformed(ActionEvent e) {
						outputPathDisplay.setVisible(false);
						gripeButton.setVisible(true);
					}
				});
			}
		});
		gripeButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				gripeButton.setVisible(false);
				outputPathDisplay.setVisible(true);
				animator.animate(null);
			}
		});
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


		this.addComponentListener(new ComponentListener() {
			@Override public void componentHidden(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
			@Override public void componentResized(ComponentEvent e) {
				Rectangle rect = e.getComponent().getBounds();
				Rectangle crect = outputPath.getBounds();
				int height = crect.y + crect.height + 8;
				layeredPane.setPreferredSize(new Dimension(rect.width, height));
				layeredPane.doLayout();
			}
		});
		layeredPane.addComponentListener(new ComponentListener() {
			@Override public void componentHidden(ComponentEvent e) {}
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
			@Override public void componentResized(ComponentEvent e) {
				Rectangle rect = e.getComponent().getBounds();
				settings.setBounds(0, 0, rect.width, rect.height);
				surface.setBounds(0, surface.getBounds().y, rect.width, rect.height + 16);
				settings.doLayout();
				surface.doLayout();
			}
		});

		// initial initialize
		this.setSplashBackgroundColor("");
		surface.setLocation(0, -16);
		this.pack();

		splitter = new SplitterFrame(this, getResource("splitter.title", "Splitter"));
	}

	/**
	 * Set preferences to ImagePanel.
	 *
	 * @param imagePanel
	 * @param textField
	 * @return imagePanel
	 */
	private ImagePanel initImagePanel(ImagePanel imagePanel, JTextField textField) {
		final ImagePanel fImagePanel = imagePanel;
		final JTextField fTextField = textField;
		imagePanel.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		imagePanel.setBackground(BGCOLOR_LIGHT_GRAY);
		imagePanel.setForeground(COLOR_DARK_GRAY);
		imagePanel.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List<?>)list).get(0);
							if (file instanceof File) {
								if (setFilePath(fTextField, (File)file, fImagePanel)) {
									return true;
								}
							}
						}
					}
				} catch (Throwable t) {
					handleThrowable(t);
				}
				return false;
			}
			@Override public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
		});
		textField.setTransferHandler(imagePanel.getTransferHandler());
		imagePanel.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent e) { }
			@Override public void mouseReleased(MouseEvent e) { }
			@Override public void mouseEntered(MouseEvent e) { }
			@Override public void mouseExited(MouseEvent e) { }
			@Override public void mouseClicked(MouseEvent e) {
				if (fImagePanel.getImage() != null && fImagePanel.getImageFile() != null) {
					if (yesNo(getResource("question.clear.image", "Clear this image?"))) {
						fTextField.setText("");
						fImagePanel.clear();
					}
				} else {
					setFilePathActionPerformed(fTextField, fImagePanel);
				}
			}
		});

		textField.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) { }
			@Override public void focusLost(FocusEvent e) {
				try {
					String currentPath = (fImagePanel.getImageFile() == null) ? "" : fImagePanel.getImageFile().getCanonicalPath();
					if (!currentPath.equals(fTextField.getText())) {
						setFilePath(fTextField, fTextField.getText().trim().length() <= 0 ? null : new File(fTextField.getText()), fImagePanel);
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
	 * Set file path from file chooser dialog.
	 *
	 * @param textField		TextField to set the file path
	 * @param imagePanel	ImagePnel to set the image
	 */
	private void setFilePathActionPerformed(JTextField textField, ImagePanel imagePanel) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
		if (textField.getText().trim().length() > 0) {
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
		int returnVal = chooser.showOpenDialog(null);
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
		try {
			if (textField == null || f == null) {
				if (textField != null) textField.setText("");
				if (imagePanel != null) imagePanel.clear();
				return false;
			}
			this.setCursor(waitCursor);
			textField.setText(f.getCanonicalPath());
			if (imagePanel != null) {
				ImageFile imageFile = checkFile(textField);
				if (imageFile == null) {
					textField.setText("");
					if (imagePanel != null) imagePanel.clear();
					return false;
				}
				if (!this.isBatchMode()) {
					imagePanel.setImage(imageFile.getImage());
					imagePanel.setImageFile(imageFile.getFile());
				}
				if (outputPath.getText().trim().length() <= 0) {
					File g = new File(f.getParentFile(), this.generateAsAssetCatalogs.isSelected() ? this.getResource("string.dir.assets", "Images.xcassets") : this.getResource("string.dir.generate", "generated"));
					outputPath.setText(g.getCanonicalPath());
				}
				if (imagePanel == splashImage) {
					Color c = imageFile.getDefaultBackgroundColor();
					this.setSplashBackgroundColor(String.format("%2h%2h%2h%2h", c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()).replace(' ', '0'));
				}
			}

			if (textField == icon6Path) { optionalIcons.setSelectedIndex(0); }
			if (textField == watchPath) { optionalIcons.setSelectedIndex(1); }
			if (textField == carplayPath) { optionalIcons.setSelectedIndex(2); }
		} catch (Throwable t) {
			handleThrowable(t);
			textField.setText("");
			if (imagePanel != null) imagePanel.clear();
			return false;
		} finally {
			this.setCursor(defaultCursor);
		}
		return true;
	}

	/**
	 * @return Chosen directory
	 */
	public File getChosenDirectory() {
		File dir;
		if ((dir = getChosenDirectory(icon7Path)) != null) return dir;
		if ((dir = getChosenDirectory(splashPath)) != null) return dir;
		if ((dir = getChosenDirectory(watchPath)) != null) return dir;
		if ((dir = getChosenDirectory(icon6Path)) != null) return dir;
		if ((dir = getChosenDirectory(carplayPath)) != null) return dir;
		if ((dir = getChosenDirectory(outputPath)) != null) return dir;
		return null;
	}

	private File getChosenDirectory(JTextField textField) {
		if (textField == null || textField.getText().trim().length() <= 0) {
			return null;
		}
		File f = new File(textField.getText());
		if (!f.exists()) {
			return null;
		}
		return f.isDirectory() ? f : f.getParentFile();
	}

	// for command line option switches.
	public boolean setIcon6Path(String path) { return setFilePath(icon6Path, new File(path), icon6Image); }
	public boolean setIcon7Path(String path) { return setFilePath(icon7Path, new File(path), icon7Image); }
	public boolean setSplashPath(String path) { return setFilePath(splashPath, new File(path), splashImage); }
	public boolean setCarplayPath(String path) { return setFilePath(carplayPath, new File(path), carplayImage); }
	public boolean setWatchPath(String path) { return setFilePath(watchPath, new File(path), watchImage); }
	public void setOutputPath(String path) throws IOException { outputPath.setText((new File(path)).getCanonicalPath()); }
	public void setSplashScaling(int idx) { splashScaling.setSelectedIndex(idx); }
	public void setGenerateOldSplashImages(boolean b) { this.generateOldSplashImages.setSelected(b); }
	public void setGenerateArtwork(boolean b) { this.generateArtwork.setSelected(b); }
	public void setGenerateAsAssetCatalogs(boolean b) { this.generateAsAssetCatalogs.setSelected(b); }
	public void selectIphoneOnly() { this.iBoth.setSelected(false);this.iPadOnly.setSelected(false);this.iPhoneOnly.setSelected(true); }
	public void selectIpadOnly() { this.iBoth.setSelected(false);this.iPhoneOnly.setSelected(false);this.iPadOnly.setSelected(true); }
	public void setBatchMode(boolean b) { this.batchMode = b; }
	public void setSilentMode(boolean b) { this.silentMode = b; }
	public void setVerboseMode(boolean b) { this.verboseMode = b; }
	public boolean isBatchMode() { return this.batchMode; }
	public boolean isSilentMode() { return this.silentMode; }
	public boolean isVerboseMode() { return this.verboseMode; }
	public void setImageType(int idx) { imageType.setSelectedIndex(idx); }
	public boolean isGenerateImagesReqested() { return !icon6Path.getText().trim().isEmpty() || !icon7Path.getText().trim().isEmpty() || !splashPath.getText().trim().isEmpty() || !watchPath.getText().trim().isEmpty() || !carplayPath.getText().trim().isEmpty();  }
	// hidden option.
	public void setScalingAlgorithm(int idx) { scaleAlgorithm.setSelectedIndex(idx); }
	protected int getScalingAlgorithm() { return ((ComboBoxItem)this.scaleAlgorithm.getSelectedItem()).getItemValue(); }

	// Splitter
	public void setAs3x(boolean b) { splitter.setAs3x(b); splitter.setSized(!b); }
	public void setSized(boolean b) { splitter.setSized(b); splitter.setAs3x(!b); }
	public void setWidth1x(String width) { splitter.setWidth1x(width); }
	public void setHeight1x(String height) { splitter.setHeight1x(height); }
	public void setOverwriteAlways(boolean b) { splitter.setOverwriteAlways(b); }
	public void setSplitTarget(String path) { splitTarget = new File(path); }
	public boolean isSplitImageRequested() { return splitTarget != null; }
	public boolean split() {
		if (!this.isBatchMode()) return false;
		if (splitTarget == null) { System.out.println("No image file specified."); return false; }
		if (!splitTarget.exists()) { System.out.println(String.format("[%s] is not exists.", splitTarget.getAbsolutePath())); return false; }
		try {
			splitter.split(splitTarget);
		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
		return true;
	}

	/**
	 * Set launch image background color.
	 *
	 * @param s color string
	 */
	public void setSplashBackgroundColor(String s) {
		Color col = null;
		if (s != null && !s.trim().isEmpty()) {
			while (s.length() < 6) s = "0".concat(s);
			if (s.length() > 8) s = s.substring(0, 8);
			if (s.length() == 7) s = "0".concat(s);
			try {
				col = new Color(Long.valueOf(s, 16).intValue(), true);
				splashBackgroundColor.setText(s);
				splashBackgroundColor.setBackground(new Color(col.getRed(), col.getGreen(), col.getBlue()));
				splashBackgroundColor.setForeground(col.getRed()+col.getGreen()+col.getBlue()>384?Color.BLACK:Color.WHITE);
				splashImage.setBackground(splashBackgroundColor.getBackground());
			} catch (Exception ex) {
				//
				ex.printStackTrace();
			}
		}
		if (col == null) {
			splashBackgroundColor.setText("");
			splashBackgroundColor.setText(PLACEHOLDER_SPLASH_BGCOL);
			splashBackgroundColor.setForeground(Color.LIGHT_GRAY);
			splashBackgroundColor.setBackground(Color.WHITE);
			splashImage.setBackground(null);
		}
	}

	/**
	 * Add progress.
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
	private void handleThrowable(Throwable t) {
		t.printStackTrace(System.err);
		if (t instanceof OutOfMemoryError) {
			alert(this.getResource("error.out.of.memory", "Out of Memory Error. Increase heap size with -Xmx java option."));
		} else {
			alert(t.toString() + " (" + t.getMessage() + ")");
		}
	}

	/**
	 * Show information message.
	 *
	 * @param message	message
	 */
	private void information(String message) {
		if (this.isBatchMode()) {
			if (!this.isSilentMode()) System.out.println(message);
		} else {
			JOptionPane.showMessageDialog(this, message, getResource("title.information", "Information"), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Show alert.
	 *
	 * @param message	alert message
	 */
	private void alert(String message) {
		if (this.isBatchMode()) {
			System.err.println(message);
		} else {
			JOptionPane.showMessageDialog(this, message, getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
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
		if (this.isBatchMode()) {
			return true;
		}
		return (JOptionPane.showConfirmDialog(this, message, getResource("title.confirm", "Confirm"), JOptionPane.OK_CANCEL_OPTION) != JOptionPane.CANCEL_OPTION);
	}

	/**
	 * Show yes / no dialog.
	 * Always 'false' with the batch mode.
	 *
	 * @param message	message
	 * @return true - yes / false - no
	 */
	private boolean yesNo(String message) {
		if (this.isBatchMode()) {
			return false;
		}
		return (JOptionPane.showConfirmDialog(this, message, getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);

	}

	/**
	 * Do generate images.
	 *
	 * @return true - sucess / false - failed
	 */
	public boolean generate() {
		boolean result = true;
		final ImageFileSet ifs = this.createValidImageFileSet();
		if (ifs == null) {
			return false;
		}

		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				// Do not use PropertyChangeListener currently.
				boolean result = true;
				Color settingsColor = settingsButton.getBackground();
				if (!isBatchMode()) {
					cancelButton.setVisible(true);
					generateButton.setVisible(false);
					settingsButton.setBackground(BGCOLOR_LIGHT_GRAY);//new Color(0xF7F7F7));
					settingsButton.setEnabled(false);
				}
				cancelRequested = false;
				try {
					progress.setMaximum(generateImages(ifs, false));
				} catch (Exception ex) {
					progress.setMaximum(IOSIconAssetCatalogs.values().length + IOSArtworkInfo.values().length + IOSSplashAssetCatalogs.values().length);
				}
				try {
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

					if (isBatchMode() && !isSilentMode() && !isVerboseMode()) {
						System.out.println();
					}
					if (!isBatchMode()) {
						progress.setValue(progress.getMaximum());
					}

				} catch (Throwable ex) {
					result = false;
					handleThrowable(ex);
				} finally {
					System.gc();
					if (!isBatchMode()) {
						generateButton.setVisible(true);
						cancelButton.setVisible(false);
						settingsButton.setBackground(settingsColor);
						settingsButton.setEnabled(true);
						if (result) {
							information(getResource("label.finish.generate", "The images are generated."));
						} else if (cancelRequested) {
							information(getResource("info.generate.canceled", "Generate images are canceled."));
						}
						progress.setValue(0);
						outputPathDisplay.setText(String.format("%s [%s]", getResource("string.output", "Output to"), outputPath.getText()));
					}
				}
				return result;
			}

			@Override protected void done() {
				// for future reference.
			}
		};

		try {
			worker.execute();
			if (this.isBatchMode()) {
				result = worker.get();
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
			ifs.setTargetSystemVersion(IOSAssetCatalogs.SYSTEM_VERSION_ANY);
			// Path Check.
			if (icon6Path.getText().trim().length() > 0) {
				ifs.setIcon6File(checkFile(icon6Path));
				if (ifs.getIcon6File() == null) return null;
			}
			if (watchPath.getText().trim().length() > 0) {
				ifs.setWatchFile(checkFile(watchPath));
				if (ifs.getWatchFile() == null) return null;
			}
			if (carplayPath.getText().trim().length() > 0) {
				ifs.setCarplayFile(checkFile(carplayPath));
				if (ifs.getCarplayFile() == null) return null;
			}
			if (icon7Path.getText().trim().length() > 0) {
				ifs.setIcon7File(checkFile(icon7Path));
				if (ifs.getIcon7File() == null) return null;
			}
			if (splashPath.getText().trim().length() > 0) {
				ifs.setSplashFile(checkFile(splashPath));
				if (ifs.getSplashFile() == null) return null;
			}

			// Error Check.
			if (outputPath.getText().trim().length() <= 0) {
				outputPath.requestFocusInWindow();
				alert(getResource("error.not.choosen.output.path", "Choose output dir."));
				return null;
			}
			if (splashBackgroundColor.getText().trim().length() > 0 && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
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
					outputPath.requestFocusInWindow();
					outputPath.selectAll();
					alert("[" + ifs.getOutputDirectory().getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return null;
				}
			}
			if (!ifs.getOutputDirectory().isDirectory()) {
				outputPath.requestFocusInWindow();
				outputPath.selectAll();
				alert("[" + ifs.getOutputDirectory().getCanonicalPath() + "] " + getResource("error.not.directory", "is not directory. Choose directory."));
				return null;
			}

			// generate images for iOS6, or not
			if (ifs.getIcon6File() == null && ifs.getIcon7File() != null) {
				// do not create iOS 6 icon by default.
				this.generateOldSplashImages.setSelected(false);
				ifs.setTargetSystemVersion(IOSAssetCatalogs.SYSTEM_VERSION_7);
				if (this.isBatchMode()) {
					information(getResource("info.ios6.image.not.generate", "The iOS6 image files will not be generated."));
				}
			}

			// images for iOS7 must be generated.
			if (ifs.getIcon6File() != null && ifs.getIcon7File() == null) {
				if (yesNo(getResource("question.use.icon6.instead", "An iOS7 Icon PNG file is not choosen. Use iOS6 Icon PNG file instead?"))) {
					ifs.setIcon7File(ifs.getIcon6File());
				} else {
					icon7Path.requestFocusInWindow();
					return null;
				}
			}

			// generate images for launch, or not
			if (ifs.getSplashFile() == null) {
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
		StringBuilder buffer = new StringBuilder();
		HashMap<String, IOSAssetCatalogs> filesOutput = new HashMap<String, IOSAssetCatalogs>();

		if (ifs.getIcon6File() != null || ifs.getIcon7File() != null || ifs.getWatchFile() != null || ifs.getCarplayFile() != null) {
			// generate icons
			for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
				if (cancelRequested) { count = -1; break; }
				ImageFile image = null;
				if (asset.isAppleWatch()) {
					if (ifs.getWatchFile() != null) { image = ifs.getWatchFile(); }
				} else if (asset.isCarPlay()) {
					if (ifs.getCarplayFile() != null) { image = ifs.getCarplayFile(); }
				} else {
					if (ifs.getIcon7File() == null && ifs.getIcon6File() == null) continue;
					image = asset.getMinimumSystemVersion() < IOSAssetCatalogs.SYSTEM_VERSION_7 ? (ifs.getIcon6File() == null ? ifs.getIcon7File() : ifs.getIcon6File()) : ifs.getIcon7File();
				}
				if (image == null) continue;
				if (ifs.getWatchFile() != null && ifs.getWatchFile().getImage() == null) continue;
				if (ifs.getCarplayFile() != null && ifs.getCarplayFile().getImage() == null) continue;
				if (!asset.isAppleWatch() && !asset.isCarPlay()) {
					if (!asset.isIpad() && iPadOnly.isSelected()) continue;
					if (!asset.isIphone() && iPhoneOnly.isSelected()) continue;
				}
				if (asset.getMinimumSystemVersion() < ifs.getTargetSystemVersion()) continue;

				if (generateAsAssetCatalogs.isSelected()) {
					if (buffer.length() > 0) buffer.append(",\n");
					buffer.append(asset.toJson());
				}

				if (filesOutput.containsKey(asset.getFilename())) {
					// upper version is more strong
					if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
				}

				filesOutput.put(asset.getFilename(), asset);
				count++;
				if (!generate) continue;

				writeIconImage(image, asset.getIOSImageInfo(), ifs.getIconOutputDirectory(), asset.isAppleWatch());
				addProgress(1);
			}
			if (generate && generateAsAssetCatalogs.isSelected()) {
				writeContentsJson(ifs.getIconOutputDirectory(), buffer);
			}
			buffer.setLength(0);
			filesOutput.clear();

			// generate artwork
			if (generateArtwork.isSelected()) {
				for (IOSArtworkInfo artwork : IOSArtworkInfo.values()) {
					count++;
					if (!generate) continue;
					addProgress(1);
					ImageFile defaultImage = ifs.getDefaultIconImage();
					if (defaultImage != null) {
						writeIconImage(defaultImage, artwork, ifs.getOutputDirectory(), false);
					}
				}
			}
		}

		if (ifs.getSplashFile() != null) {
			// generate launch images
			for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
				if (cancelRequested) { count = -1; break; }
				if (asset.isIphone() && iPadOnly.isSelected()) continue;
				if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
				if (asset.getMinimumSystemVersion() < ifs.getTargetSystemVersion()) continue;
				if (asset.getExtent() != null && asset.getExtent().equals(IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR) && !generateOldSplashImages.isSelected()) continue;

				if (generateAsAssetCatalogs.isSelected()) {
					if (buffer.length() > 0) buffer.append(",\n");
					buffer.append(asset.toJson());
				}

				if (filesOutput.containsKey(asset.getFilename())) {
					// upper version is more strong
					if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
				}

				filesOutput.put(asset.getFilename(), asset);
				count++;
				if (!generate) continue;

				writeSplashImage(ifs.getSplashFile(), asset, ifs.getSplashOutputDirectory());
				addProgress(1);
			}
			if (generate && generateAsAssetCatalogs.isSelected()) {
				writeContentsJson(ifs.getSplashOutputDirectory(), buffer);
			}
			buffer.setLength(0);
			filesOutput.clear();
		}

		return count;
	}

	/**
	 * Cancel to generate.
	 */
	private void cancelGenerate() {
		this.cancelRequested = true;
		outputPathDisplay.setText(getResource("label.cancel.generate", "Cancel generate..."));
	}

	/**
	 * Write icon image to the file.
	 *
	 * @param srcFile		source image
	 * @param info		image information
	 * @param outputDir	output directory
	 * @throws Exception	exception
	 */
	private void writeIconImage(ImageFile srcFile, IOSImageInfo info, File outputDir, boolean forceIntRGB) throws Exception {
		File f = new File(outputDir, info.getFilename());
		int width = (int)info.getSize().getWidth();
		int height = (int)info.getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, forceIntRGB ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
		int hints = this.getScalingAlgorithm();
		Image img = srcFile.getImage().getScaledInstance(width, height, hints);
		if (forceIntRGB) {
			Graphics g = buf.getGraphics();
			g.setColor(srcFile.getDefaultBackgroundColor());
			g.fillRect(0, 0, width, height);
		}
		buf.getGraphics().drawImage(img, 0, 0, this);
		img.flush();
		img = null;

		ImageIO.write(forceIntRGB ? buf : fixImageColor(buf, srcFile.getImage()), "png", f);
		buf.flush();
		buf = null;
		verbose(f);
	}

	/**
	 * Write launch image to the file.
	 *
	 * @param src		source image
	 * @param asset		asset catalogs
	 * @param outputDir	output directory
	 * @throws Exception	exception
	 */
	private void writeSplashImage(ImageFile srcFile, IOSSplashAssetCatalogs asset, File outputDir) throws Exception {
		File f = new File(outputDir, asset.getFilename());
		int width = (int)asset.getIOSImageInfo().getSize().getWidth();
		int height = (int)asset.getIOSImageInfo().getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		g.setColor(srcFile.getDefaultBackgroundColor());
		if (splashBackgroundColor.getText().trim().length() > 0 && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
			Color col = new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true);
			if (col != null) g.setColor(col);
		}
		g.fillRect(0, 0, width, height);

		BufferedImage src = srcFile.getImage();
		double p = (width > height) ? (double)height / (double)src.getHeight() : (double)width / (double)src.getWidth();
		if (splashScaling.getSelectedIndex() == 0) {
			// No resizing(iPhone only)
			if (asset.isIphone()) p = asset.getIOSImageInfo().getScale() > 1 ? 1.0d : 0.5d;
		} else if (splashScaling.getSelectedIndex() == 1) {
			// No resizing(iPhone & iPad)
			p = asset.getIOSImageInfo().getScale() > 1 ? 1.0d : 0.5d;
		} else if (splashScaling.getSelectedIndex() == 2) {
			// Fit to the screen height(iPhone only)
			if (asset.isIphone()) p = (double)height / (double)src.getHeight();
		} else if (splashScaling.getSelectedIndex() == 4) {
			p = (width < height) ? (double)height / (double)src.getHeight() : (double)width / (double)src.getWidth();
		}// else default
		int w = (int) ((double)src.getWidth() * p);
		int h = (int) ((double)src.getHeight() * p);
		if (splashScaling.getSelectedIndex() == 5) {
			w = width;
			h = height;
		}
   		int x = (int) ((width - w) / 2);
   		int y = (int) ((height - h) / 2);
		int hints = this.getScalingAlgorithm();
		Image img = src.getScaledInstance(w, h, hints);
		buf.getGraphics().drawImage(img, x, y, this);
		img.flush();
		img = null;

		ImageIO.write(fixImageColor(buf, src), "png", f);
		buf.flush();
		buf = null;
		verbose(f);
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

	/**
	 * Write json string to the file.
	 *
	 * @param outputDir	output directory
	 * @param buffer	json string (main body)
	 * @throws IOException	excepiton
	 */
	private void writeContentsJson(File outputDir, StringBuilder buffer) throws IOException {
		BufferedWriter writer = null;
		try {
			File f = new File(outputDir, this.getResource("string.filename.contents.json", "Contents.json"));
			writer = new BufferedWriter(new FileWriter(f));
			writer.write(IOSAssetCatalogs.JSON_HEADER);
			writer.write(buffer.toString());
			writer.write(IOSAssetCatalogs.JSON_FOOTER);
			verbose(f);
		} catch (IOException ioex) {
			throw ioex;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
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
				//path.requestFocusInWindow();
				//path.selectAll();
				alert("[" + f.getCanonicalPath() + "] " + getResource("error.not.exists", "is not exists."));
				return null;
			}
			if (f.isDirectory()) {
				//path.requestFocusInWindow();
				//path.selectAll();
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
			return resource.getString(key);
		}
		return def;
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
	 * @return
	 */
	public Color getDefaultBackgroundColor() {
		Color c = null;
		for (int xy = 0; xy < Math.min(this.getImage().getWidth(), this.getImage().getHeight()); xy++) {
			Color col = new Color(this.getImage().getRGB(xy, xy), true);
			if (col.getAlpha() != 0) {
				c = new Color(col.getRGB(), false);
				break;
			}
		}
		return c == null ? new Color(this.getImage().getRGB(0, 0), false) : c;
	}
}

class ImageFileSet
{
	private float targetSystemVersion = IOSAssetCatalogs.SYSTEM_VERSION_ANY;
	private ImageFile icon6File;
	private ImageFile watchFile;
	private ImageFile carplayFile;
	private ImageFile icon7File;
	private File iconOutputDirectory;
	private ImageFile splashFile;
	private File splashOutputDirectory;
	private File outputDirectory;

	public ImageFile getDefaultIconImage() {
		ImageFile defaultImage = null;
		if (getIcon7File() != null) { defaultImage = getIcon7File(); }
		if (defaultImage == null && getWatchFile() != null) { defaultImage = getWatchFile(); }
		if (defaultImage == null && getIcon6File() != null) { defaultImage = getIcon6File(); }
		if (defaultImage == null && getCarplayFile() != null) { defaultImage = getCarplayFile(); }
		return defaultImage;
	}

	/**
	 * @return targetSystemVersion
	 */
	public float getTargetSystemVersion() {
		return targetSystemVersion;
	}
	/**
	 * @param targetSystemVersion set targetSystemVersion
	 */
	public void setTargetSystemVersion(float targetSystemVersion) {
		this.targetSystemVersion = targetSystemVersion;
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
	}

	public boolean animate(ActionListener callback) {
		if (timer != null && timer.isRunning()) {
			return false;
		}

		this.callbackListener = callback;
		this.direction = target.getY() == 0 - this.base ? SimpleShutterAnimation.DIRECTION_UP : SimpleShutterAnimation.DIRECTION_DOWN;
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


