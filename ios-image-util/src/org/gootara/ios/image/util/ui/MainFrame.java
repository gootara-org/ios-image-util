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
	private JTextField icon6Path, icon7Path, splashPath, outputPath, splashBackgroundColor;
	private JComboBox scaleAlgorithm, splashScaling, imageType;
	private ImagePanel icon6Image, icon7Image, splashImage;
	private JProgressBar progress;
	private JCheckBox generateOldSplashImages, generateAsAssetCatalogs;
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

	/**
	 * Constructor.
	 */
	protected void frameInit() {
		super.frameInit();
		resource = ResourceBundle.getBundle("application");
		this.setTitle(getResource("window.title", "iOS Image Util"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		final JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(640, 240));
		this.add(layeredPane, BorderLayout.NORTH);

		final JPanel settings = new JPanel();
		settings.setPreferredSize(layeredPane.getPreferredSize());
		layeredPane.add(settings, JLayeredPane.DEFAULT_LAYER);

		// iOS6 Icon Path.
		JLabel icon6Label = new JLabel(getResource("label.icon6.path", " iOS6 Icon PNG (1024x1024):"), SwingConstants.RIGHT);
		icon6Path = new JTextField();
		JButton refIcon6Path = new JButton("...");
		refIcon6Path.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				setFilePathActionPerformed(icon6Path, icon6Image);
			}
		});
		settings.add(icon6Label);
		settings.add(icon6Path);
		settings.add(refIcon6Path);

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
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		radioPanel.add(this.iBoth = new JRadioButton(getResource("label.output.both", "Output Both"), true));
		radioPanel.add(this.iPhoneOnly = new JRadioButton(getResource("label.iphone.only", "iPhone Only")));
		radioPanel.add(this.iPadOnly = new JRadioButton(getResource("label.ipad.only", "iPad Only")));
		ButtonGroup group = new ButtonGroup();
		group.add(this.iBoth);
		group.add(this.iPhoneOnly);
		group.add(this.iPadOnly);
		settings.add(radioPanel);

		this.generateOldSplashImages = new JCheckBox(getResource("label.generate.old.splash", "Generate Old Splash Images"), false);
		settings.add(this.generateOldSplashImages);

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
		layout.putConstraint(SpringLayout.WEST, icon6Label, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, icon6Label, 0, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.NORTH, icon6Label, 27, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.EAST, refIcon6Path, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refIcon6Path, 25, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.WEST, icon6Path, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, icon6Path, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, icon6Path, 25, SpringLayout.NORTH, settings);

		// Layout for iOS 7 Path
		layout.putConstraint(SpringLayout.WEST, icon7Label, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, icon7Label, 0, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.NORTH, icon7Label, 7, SpringLayout.SOUTH, refIcon6Path);
		layout.putConstraint(SpringLayout.EAST, refIcon7Path, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, refIcon7Path, 5, SpringLayout.SOUTH, refIcon6Path);
		layout.putConstraint(SpringLayout.WEST, icon7Path, 5, SpringLayout.EAST, splashLabel);
		layout.putConstraint(SpringLayout.EAST, icon7Path, -5, SpringLayout.WEST, refIcon6Path);
		layout.putConstraint(SpringLayout.NORTH, icon7Path, 5, SpringLayout.SOUTH, refIcon6Path);

		// Layout for Launch Image Path
		layout.putConstraint(SpringLayout.WEST, splashLabel, 5, SpringLayout.WEST, settings);
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
		layout.putConstraint(SpringLayout.EAST, radioPanel, 0, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, radioPanel, 5, SpringLayout.SOUTH, imageTypePanel);
		layout.putConstraint(SpringLayout.WEST, generateOldSplashImages, 16, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.NORTH, generateOldSplashImages, 5, SpringLayout.SOUTH, imageTypePanel);

		// Layout for Output Image Path
		layout.putConstraint(SpringLayout.WEST, separatorSouth, 5, SpringLayout.WEST, settings);
		layout.putConstraint(SpringLayout.EAST, separatorSouth, -5, SpringLayout.EAST, settings);
		layout.putConstraint(SpringLayout.NORTH, separatorSouth, 8, SpringLayout.SOUTH, radioPanel);
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
		Font imagesFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		Color imagesColor = new Color(0xf7f7f7);
		icon6Image = new ImagePanel(getResource("label.icon6.drop", "Drop iOS6 Icon PNG Here"));
		icon6Image.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		icon6Image.setBackground(imagesColor);
		icon6Image.setFont(imagesFont);
		icon6Image.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List<?>)list).get(0);
							if (file instanceof File) { setFilePath(icon6Path, (File)file, icon6Image); }
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
		icon6Path.setTransferHandler(icon6Image.getTransferHandler());

		icon7Image = new ImagePanel(getResource("label.icon7.drop", "Drop iOS7 Icon PNG Here"));
		icon7Image.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		icon7Image.setBackground(imagesColor);
//		icon7Image.setForeground(new Color(0x4AA7B4));
		icon7Image.setForeground(new Color(0x34AADC));//0x007AFF));//0x34AADC));
		icon7Image.setFont(imagesFont);
		icon7Image.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List<?>)list).get(0);
							if (file instanceof File) { setFilePath(icon7Path, (File)file, icon7Image); }
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
		icon7Path.setTransferHandler(icon7Image.getTransferHandler());

		splashImage = new ImagePanel(getResource("label.splash.drop", "Drop Splash Image PNG Here"));
		splashImage.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		splashImage.setBackground(imagesColor);
		splashImage.setFont(imagesFont);
		splashImage.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List<?>)list).get(0);
							if (file instanceof File) { setFilePath(splashPath, (File)file, splashImage); }
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
		splashPath.setTransferHandler(splashImage.getTransferHandler());

		JPanel imagesPanel = new JPanel();
		imagesPanel.setBorder(new LineBorder(imagesColor, 4));
		imagesPanel.setBackground(imagesColor);
		imagesPanel.setLayout(new GridLayout(1, 3, 2, 2));
		imagesPanel.add(icon6Image);
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

		Font buttonFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		generateButton = new JButton(getResource("button.generate", "Generate"), new ImageIcon(this.getClass().getResource("img/generate.png")));
		generateButton.setBackground(new Color(0xFF4981));
		generateButton.setForeground(Color.WHITE);
		generateButton.setBorderPainted(false);
		generateButton.setFocusPainted(false);
		generateButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/generate_rollover.png")));
		generateButton.setHorizontalTextPosition(SwingConstants.CENTER);
		generateButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		generateButton.setFont(buttonFont);
		generateButton.setMargin(new Insets(2, 16, 2, 16));
		generateButton.setOpaque(true);
		generateButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		surface.add(generateButton);

		cancelButton = new JButton(getResource("button.cancel", "Cancel"), new ImageIcon(this.getClass().getResource("img/generate.gif")));
		cancelButton.setBackground(new Color(0xf7f7f7));
		cancelButton.setForeground(new Color(0x8E8E93));
		cancelButton.setBorderPainted(false);
		cancelButton.setFocusPainted(false);
		cancelButton.setHorizontalTextPosition(SwingConstants.CENTER);
		cancelButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		cancelButton.setFont(buttonFont);
		cancelButton.setMargin(new Insets(2, 16, 2, 16));
		cancelButton.setOpaque(true);
		cancelButton.setDoubleBuffered(true);
		cancelButton.setRolloverEnabled(false);
		cancelButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				cancelGenerate();
			}

		});
		surface.add(cancelButton);
		cancelButton.setVisible(false);

		settingsButton = new JButton(getResource("button.settings", "Settings"), new ImageIcon(this.getClass().getResource("img/settings.png")));
		settingsButton.setBackground(new Color(0x4CD964));
		settingsButton.setForeground(Color.WHITE);
		settingsButton.setBorderPainted(false);
		settingsButton.setFocusPainted(false);
		settingsButton.setRolloverEnabled(true);
		settingsButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/settings_disabled.png")));
		settingsButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/settings_rollover.png")));
		settingsButton.setHorizontalTextPosition(SwingConstants.CENTER);
		settingsButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		settingsButton.setFont(buttonFont);
		settingsButton.setMargin(new Insets(2, 16, 2, 16));
		settingsButton.setOpaque(true);
		surface.add(settingsButton);


		splitButton = new JButton(getResource("button.split", "Split"), new ImageIcon(this.getClass().getResource("img/splitter.png")));
		splitButton.setBackground(new Color(0x007AFF));
		splitButton.setForeground(Color.WHITE);
		splitButton.setBorderPainted(false);
		splitButton.setFocusPainted(false);
		splitButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/splitter_disabled.png")));
		splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/splitter_rollover.png")));
		splitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		splitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		splitButton.setFont(buttonFont);
		splitButton.setMargin(new Insets(2, 16, 2, 16));
		splitButton.setOpaque(true);
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
		progress.setBackground(new Color(0xF7F7F7));
		progress.setForeground(new Color(0x007AFF));
		progress.setOpaque(true);
		surface.add(progress);

		outputPathDisplay = new JLabel("");
		outputPathDisplay.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		outputPathDisplay.setForeground(new Color(0x34AADC));//0x007AFF));//0x34AADC));
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
		try {
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
				}
				if (outputPath.getText().trim().length() <= 0) {
					File g = new File(f.getParentFile(), this.generateAsAssetCatalogs.isSelected() ? this.getResource("string.dir.assets", "Images.xcassets") : this.getResource("string.dir.generate", "generated"));
					outputPath.setText(g.getCanonicalPath());
				}
				if (imagePanel == splashImage) {
					Color c = new Color(imageFile.getImage().getRGB(0, 0), true);
					this.setSplashBackgroundColor(String.format("%2h%2h%2h%2h", c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()).replace(' ', '0'));
				}
			}
		} catch (Throwable t) {
			handleThrowable(t);
			textField.setText("");
			if (imagePanel != null) imagePanel.clear();
			return false;
		}
		return true;
	}

	private File getChosenDirectory() {
		File dir = getChosenDirectory(icon7Path);
		if (dir != null) return dir;
		dir = getChosenDirectory(splashPath);
		if (dir != null) return dir;
		dir = getChosenDirectory(icon6Path);
		if (dir != null) return dir;
		dir = getChosenDirectory(outputPath);
		if (dir != null) return dir;
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
	public void setOutputPath(String path) throws IOException { outputPath.setText((new File(path)).getCanonicalPath()); }
	public void setSplashScaling(int idx) { splashScaling.setSelectedIndex(idx); }
	public void setGenerateOldSplashImages(boolean b) { this.generateOldSplashImages.setSelected(b); }
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
	public boolean isGenerateImagesReqested() { return !icon6Path.getText().trim().isEmpty() && !icon7Path.getText().trim().isEmpty() && !splashPath.getText().trim().isEmpty();  }
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
		float targetSystemVersion = IOSAssetCatalogs.SYSTEM_VERSION_ANY;
		boolean result = true;

		try {
			if (icon6Path.getText().trim().length() <= 0 && icon7Path.getText().trim().length() <= 0 && splashPath.getText().trim().length() <= 0) {
				alert(getResource("error.not.choosen", "Choose at least one Icon or Splash PNG file."));
				return false;
			}

			// Path Check.
			ImageFile icon6File, icon7File, splashFile;
			icon6File = icon7File = splashFile = null;
			if (icon6Path.getText().trim().length() > 0) {
				icon6File = checkFile(icon6Path);
				if (icon6File == null) return false;
				if (!this.isBatchMode()) icon6Image.setImage(icon6File.getImage());
			}
			if (icon7Path.getText().trim().length() > 0) {
				icon7File = checkFile(icon7Path);
				if (icon7File == null) return false;
				if (!this.isBatchMode()) icon7Image.setImage(icon7File.getImage());
			}
			if (splashPath.getText().trim().length() > 0) {
				splashFile = checkFile(splashPath);
				if (splashFile == null) return false;
				if (!this.isBatchMode()) splashImage.setImage(splashFile.getImage());
			}

			// Error Check.
			File outputDir = null;
			if (outputPath.getText().trim().length() <= 0) {
				outputPath.requestFocusInWindow();
				alert(getResource("error.not.choosen.output.path", "Choose output dir."));
				return false;
			}
			if (splashBackgroundColor.getText().trim().length() > 0 && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
				Color col = null;
				try { col = new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true); } catch (Exception ex) {}
				if (col == null) {
					alert("[" + splashBackgroundColor.getText() + "]" + getResource("error.illegal.bgcolor", "is illegal bgcolor."));
					return false;
				}
			}

			// Make sure output directory is exists.
			outputDir = new File(outputPath.getText());
			if (!outputPath.getText().equals(outputDir.getCanonicalPath())) {
				outputPath.setText(outputDir.getCanonicalPath());
				outputDir = new File(outputPath.getText());
			}
			if (!outputDir.exists()) {
				if (!confirm("[" + outputPath.getText() + "] " + getResource("confirm.output.path.create", "is not exists. Create it?"))) {
					return false;
				}
				if (!outputDir.mkdirs()) {
					outputPath.requestFocusInWindow();
					outputPath.selectAll();
					alert("[" + outputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return false;
				}
			}
			if (!outputDir.isDirectory()) {
				outputPath.requestFocusInWindow();
				outputPath.selectAll();
				alert("[" + outputDir.getCanonicalPath() + "] " + getResource("error.not.directory", "is not directory. Choose directory."));
				return false;
			}

			// generate images for iOS6, or not
			if (icon6File == null && icon7File != null) {
				// do not create iOS 6 icon by default.
				this.generateOldSplashImages.setSelected(false);
				targetSystemVersion = IOSAssetCatalogs.SYSTEM_VERSION_7;
				if (this.isBatchMode()) {
					information(getResource("info.ios6.image.not.generate", "The iOS6 image files will not be generated."));
				}
				/*
				if (yesNo(getResource("question.use.icon7.instead", "An iOS6 Icon PNG file is not choosen. Use iOS7 Icon PNG file instead?"))) {
					icon6File = icon7File;
				} else {
					this.generateOldSplashImages.setSelected(false);
					information(getResource("info.ios6.image.not.generate", "The iOS6 image files will not be generated."));
					// images for iOS6 will not be generated.
					targetSystemVersion = IOSAssetCatalogs.SYSTEM_VERSION_7;
				}
				*/
			}

			// images for iOS7 must be generated.
			if (icon6File != null && icon7File == null) {
				if (confirm(getResource("question.use.icon6.instead", "An iOS7 Icon PNG file is not choosen. Use iOS6 Icon PNG file instead?"))) {
					icon7File = icon6File;
				} else {
					icon7Path.requestFocusInWindow();
					return false;
				}
			}

			// generate images for launch, or not
			if (splashFile == null) {
				// do not create launch imagesn by default.
				if (this.isBatchMode()) {
					information(getResource("confirm.splash.not.generate", "The Splash image will not be generated."));
				}
				/*
				if (!confirm(getResource("confirm.splash.not.generate", "The Splash image will not be generated."))) {
					return false;
				}
				*/
			}

			File iconOutputDir = outputDir;
			File splashOutputDir = outputDir;
			if (this.generateAsAssetCatalogs.isSelected()) {
				// Asset Catalogs
				iconOutputDir = new File(outputDir, getResource("string.dir.appicon", "AppIcon.appiconset"));
				if (!iconOutputDir.exists() && !iconOutputDir.mkdirs()) {
					alert("[" + iconOutputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return false;
				}
				splashOutputDir = new File(outputDir, getResource("string.dir.launchimage", "LaunchImage.launchimage"));
				if (!splashOutputDir.exists() && !splashOutputDir.mkdirs()) {
					alert("[" + splashOutputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."));
					return false;
				}
			}


			try {
				int max = 0;
				HashMap<String, IOSAssetCatalogs> filesOutput = new HashMap<String, IOSAssetCatalogs>();

				if (icon6File != null || icon7File != null) {
					// generate icons
					for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
						BufferedImage image = asset.getMinimumSystemVersion() < IOSAssetCatalogs.SYSTEM_VERSION_7 ? (icon6File == null ? icon7File.getImage() : icon6File.getImage()) : icon7File.getImage();
						if (image == null) continue;
						if (asset.isIphone() && iPadOnly.isSelected()) continue;
						if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
						if (asset.getMinimumSystemVersion() < targetSystemVersion) continue;
						if (filesOutput.containsKey(asset.getFilename())) {
							// upper version is more strong
							if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
						}
						max++;
						filesOutput.put(asset.getFilename(), asset);
					}
					filesOutput.clear();

					// generate artwork
					for (IOSArtworkInfo artwork : IOSArtworkInfo.values()) {
						if (artwork != null) { max++; }
					}
				}

				if (splashFile != null) {
					// generate launch images
					for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
						if (asset.isIphone() && iPadOnly.isSelected()) continue;
						if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
						if (asset.getMinimumSystemVersion() < targetSystemVersion) continue;
						if (asset.getExtent() != null && asset.getExtent().equals(IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR) && !generateOldSplashImages.isSelected()) continue;
						if (filesOutput.containsKey(asset.getFilename())) {
							// upper version is more strong
							if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
						}
						max++;
						filesOutput.put(asset.getFilename(), asset);
					}
					filesOutput.clear();
				}
				progress.setMaximum(max);
			} catch (Exception ex) {
				progress.setMaximum(IOSIconAssetCatalogs.values().length + IOSArtworkInfo.values().length + IOSSplashAssetCatalogs.values().length);
			}

			// start generate Images.
			if (this.isBatchMode()) {
				if (!this.isSilentMode() && !this.isVerboseMode()) {
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

			// generate images
			final float finalTargetSystemVersion = targetSystemVersion;
			final ImageFile finalIcon6File = icon6File;
			final ImageFile finalIcon7File = icon7File;
			final File finalIconOutputDir = iconOutputDir;
			final ImageFile finalSplashFile = splashFile;
			final File finalSplashOutputDir = splashOutputDir;
			final File finalOutputDir = outputDir;
			SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
				@Override protected Boolean doInBackground() throws Exception {
					// Do not use PropertyChangeListener currently.
					boolean result = true;
					Color settingsColor = settingsButton.getBackground();
					Color splitColor = splitButton.getBackground();
					if (!isBatchMode()) {
						cancelButton.setVisible(true);
						generateButton.setVisible(false);
						settingsButton.setBackground(new Color(0xF7F7F7));
						settingsButton.setEnabled(false);
					}
					cancelRequested = false;
					try {
						StringBuilder buffer = new StringBuilder();
						HashMap<String, IOSAssetCatalogs> filesOutput = new HashMap<String, IOSAssetCatalogs>();

						if (finalIcon6File != null || finalIcon7File != null) {
							// generate icons
							for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
								if (cancelRequested) { result = false; break; }
								BufferedImage image = asset.getMinimumSystemVersion() < IOSAssetCatalogs.SYSTEM_VERSION_7 ? (finalIcon6File == null ? finalIcon7File.getImage() : finalIcon6File.getImage()) : finalIcon7File.getImage();
								if (image == null) continue;
								if (asset.isIphone() && iPadOnly.isSelected()) continue;
								if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
								if (asset.getMinimumSystemVersion() < finalTargetSystemVersion) continue;

								if (generateAsAssetCatalogs.isSelected()) {
									if (buffer.length() > 0) buffer.append(",\n");
									buffer.append(asset.toJson());
								}

								if (filesOutput.containsKey(asset.getFilename())) {
									// upper version is more strong
									if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
								}

								writeIconImage(image, asset.getIOSImageInfo(), finalIconOutputDir);
								filesOutput.put(asset.getFilename(), asset);
								addProgress(1);
							}
							if (generateAsAssetCatalogs.isSelected()) {
								writeContentsJson(finalIconOutputDir, buffer);
							}
							buffer.setLength(0);
							filesOutput.clear();

							// generate artwork
							for (IOSArtworkInfo artwork : IOSArtworkInfo.values()) {
								addProgress(1);
								writeIconImage(finalIcon7File == null ? finalIcon6File.getImage() : finalIcon7File.getImage(), artwork, finalOutputDir);
							}
						}

						if (finalSplashFile != null) {
							// generate launch images
							for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
								if (cancelRequested) { result = false; break; }
								if (asset.isIphone() && iPadOnly.isSelected()) continue;
								if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
								if (asset.getMinimumSystemVersion() < finalTargetSystemVersion) continue;
								if (asset.getExtent() != null && asset.getExtent().equals(IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR) && !generateOldSplashImages.isSelected()) continue;

								if (generateAsAssetCatalogs.isSelected()) {
									if (buffer.length() > 0) buffer.append(",\n");
									buffer.append(asset.toJson());
								}

								if (filesOutput.containsKey(asset.getFilename())) {
									// upper version is more strong
									if (filesOutput.get(asset.getFilename()).getMinimumSystemVersion() >= asset.getMinimumSystemVersion()) continue;
								}

								writeSplashImage(finalSplashFile.getImage(), asset, finalSplashOutputDir);
								filesOutput.put(asset.getFilename(), asset);
								addProgress(1);
							}
							if (generateAsAssetCatalogs.isSelected()) {
								writeContentsJson(finalSplashOutputDir, buffer);
							}
							buffer.setLength(0);
							filesOutput.clear();
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
			worker.execute();
			if (this.isBatchMode()) {
				result = worker.get();
			}


		} catch (Throwable t) {
			handleThrowable(t);
			return false;
		}
		return result;
	}

	private void cancelGenerate() {
		this.cancelRequested = true;
		outputPathDisplay.setText(getResource("label.cancel.generate", "Cancel generate..."));
	}

	/**
	 * Write icon image to the file.
	 *
	 * @param src		source image
	 * @param info		image information
	 * @param outputDir	output directory
	 * @throws Exception	exception
	 */
	private void writeIconImage(BufferedImage src, IOSImageInfo info, File outputDir) throws Exception {
		File f = new File(outputDir, info.getFilename());
		int width = (int)info.getSize().getWidth();
		int height = (int)info.getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int hints = this.getScalingAlgorithm();
		Image img = src.getScaledInstance(width, height, hints);
		buf.getGraphics().drawImage(img, 0, 0, this);
		img.flush();
		img = null;

		ImageIO.write(fixImageColor(buf, src), "png", f);
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
	private void writeSplashImage(BufferedImage src, IOSSplashAssetCatalogs asset, File outputDir) throws Exception {
		File f = new File(outputDir, asset.getFilename());
		int width = (int)asset.getIOSImageInfo().getSize().getWidth();
		int height = (int)asset.getIOSImageInfo().getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = buf.getGraphics();
		g.setColor(new Color(src.getRGB(0, 0)));
		if (splashBackgroundColor.getText().trim().length() > 0 && !splashBackgroundColor.getText().trim().equals(PLACEHOLDER_SPLASH_BGCOL)) {
			Color col = new Color(Long.valueOf(splashBackgroundColor.getText(), 16).intValue(), true);
			if (col != null) g.setColor(col);
		}
		g.fillRect(0, 0, width, height);

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
				path.requestFocusInWindow();
				path.selectAll();
				alert("[" + f.getCanonicalPath() + "] " + getResource("error.not.exists", "is not exists."));
				return null;
			}
			if (f.isDirectory()) {
				path.requestFocusInWindow();
				path.selectAll();
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


