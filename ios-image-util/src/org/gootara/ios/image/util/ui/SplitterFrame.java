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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gootara.ios.image.util.IOSAssetCatalogs;
import org.gootara.ios.image.util.IOSAssetCatalogs.JSON_VALUE;
import org.gootara.ios.image.util.IOSImageSet;
import org.gootara.ios.image.util.IOSImageUtil;

/**
 * The main window frame of IOSImageUtil.
 *
 * @author gootara.org
 */
public class SplitterFrame extends JFrame {
	private MainFrame owner;
	private JPanel settings, card;
	private JTabbedPane deviceSpecificTabs;
	private JToggleButton universal, deviceSpecific;
	private JCheckBox overwriteAlways, asImageSets, cleanBeforeGenerate;
	private JComboBox widthType, heightType, scalingType, renderAs;
	private JLabel widthLabel, heightLabel;
	private JTextField subdir, bgcolor;
	private JButton splitButton;
	private File previousTargetDirectory, previousOutputDirectory;
	private SplitterSizePanel sizeDefault, sizeUniversal, sizeiPhone, sizeiPad, sizeAppleWatch, sizeMac;
	private JProgressBar progress;
	private ExecutorService pool;
	private HashMap<File, ArrayList<IOSImageSet>> contentCache;

	/**
	 * Constructor
	 *
	 * @param owner
	 * @param title
	 */
	public SplitterFrame(MainFrame owner, String title) {
		super(title);
		this.owner = owner;
		//super(owner, title, Dialog.ModalityType.MODELESS);
		initFrame();
	}

//	protected void frameInit() {
	private void initFrame() {
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
		contentCache = new HashMap<File, ArrayList<IOSImageSet>>();
		final String placeHolder = owner.getResource("splitter.label", "Same as origin.");
		boolean isMacLAF = UIManager.getLookAndFeel().getName().toLowerCase().indexOf("mac") >= 0;
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(320, 544);
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		Font fontLarge = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 14);
		Font fontNormal = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 12);
		Font fontSmall = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 11);
		previousOutputDirectory = null;

		ItemListener propertyChangedItemListener = (new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				owner.setStorePropertiesRequested(true);
				applyComponentStatus();
			}
		});

		settings = new JPanel();
		settings.setLayout(new BorderLayout());
		settings.setBackground(Color.WHITE);

		JPanel settingsNorth = new JPanel();
		settingsNorth.setBackground(Color.WHITE);
		settingsNorth.add(this.asImageSets = new JCheckBox(owner.getResource("splitter.as.imagesets", "Generate images as ImageSets"), false));
		settingsNorth.add(new JSeparator(JSeparator.VERTICAL));
		settingsNorth.add(this.universal = new JToggleButton(owner.getResource("splitter.toggle.universal", "   Universal   "), true));
		settingsNorth.add(this.deviceSpecific = new JToggleButton(owner.getResource("splitter.toggle.device.specific", "Device Specific"), false));
		this.asImageSets.setToolTipText(owner.getResource("splitter.as.imagesets.tooltip", "ImageSets directory and Contents.json will be generated automatically. It could be to copy & paste to the Xcode project."));
		this.asImageSets.setBackground(Color.WHITE);
		this.asImageSets.addItemListener(propertyChangedItemListener);
		this.asImageSets.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				String imagesXcassets = owner.getResource("string.dir.assets", "Images.xcassets");
				if (asImageSets.isSelected()) {
					if (subdir.getText().trim().isEmpty() || subdir.getText().trim().equals(placeHolder)) {
						setOutputDirectory(imagesXcassets);
					}
				} else {
					if (subdir.getText().trim().equals(imagesXcassets)) {
						setOutputDirectory("");
					}
				}
			}
		});

		this.universal.setBackground(Color.WHITE);
		this.universal.addItemListener(propertyChangedItemListener);
		this.deviceSpecific.setBackground(Color.WHITE);
		this.deviceSpecific.addItemListener(propertyChangedItemListener);
		ButtonGroup group = new ButtonGroup();
		group.add(this.universal);
		group.add(this.deviceSpecific);

		Vector<String> sizeItems = new Vector<String>();
		sizeItems.add("Any");
		sizeItems.add("Any & Compact");
		sizeItems.add("Any & Regular");
		widthType = new JComboBox(sizeItems);
		widthType.setFont(fontSmall);
		heightType = new JComboBox(sizeItems);
		heightType.setFont(fontSmall);
		settingsNorth.add(widthType);
		settingsNorth.add(heightType);
		settingsNorth.add(widthType);
		settingsNorth.add(heightType);
		settingsNorth.add(widthLabel = new JLabel("Width"));
		settingsNorth.add(heightLabel = new JLabel("Height"));

		// Scaling
		Vector<String> renderItems = new Vector<String>();
		renderItems.add(owner.getResource("splitter.item.render.as.default", "Default"));
		renderItems.add(owner.getResource("splitter.item.render.as.original", "Original"));
		renderItems.add(owner.getResource("splitter.item.render.as.template", "Template"));
		renderAs = new JComboBox(renderItems);
		renderAs.setFont(fontSmall);
		renderAs.addItemListener(propertyChangedItemListener);
		//JLabel renderAsLabel = new JLabel(owner.getResource("splitter.item.render.as", "Render As"));
		//settingsNorth.add(renderAsLabel);
		settingsNorth.add(renderAs);

		SpringLayout layoutNorth = new SpringLayout();
		layoutNorth.putConstraint(SpringLayout.WEST, asImageSets, 8, SpringLayout.WEST, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.NORTH, asImageSets, 8, SpringLayout.NORTH, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.EAST, deviceSpecific, isMacLAF ? -4 : -8, SpringLayout.EAST, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.NORTH, deviceSpecific, 8, SpringLayout.NORTH, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.EAST, universal, -4, SpringLayout.WEST, deviceSpecific);
		layoutNorth.putConstraint(SpringLayout.NORTH, universal, 8, SpringLayout.NORTH, settingsNorth);

		layoutNorth.putConstraint(SpringLayout.WEST, widthLabel, 16, SpringLayout.WEST, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.VERTICAL_CENTER, widthLabel, 0, SpringLayout.VERTICAL_CENTER, widthType);
		layoutNorth.putConstraint(SpringLayout.WEST, widthType, isMacLAF ? 0 : 4, SpringLayout.EAST, widthLabel);
		layoutNorth.putConstraint(SpringLayout.NORTH, widthType, 10, SpringLayout.SOUTH, universal);
		layoutNorth.putConstraint(SpringLayout.WEST, heightLabel, isMacLAF ? 8 : 16, SpringLayout.EAST, widthType);
		layoutNorth.putConstraint(SpringLayout.VERTICAL_CENTER, heightLabel, 0, SpringLayout.VERTICAL_CENTER, heightType);
		layoutNorth.putConstraint(SpringLayout.WEST, heightType, isMacLAF ? 0 : 4, SpringLayout.EAST, heightLabel);
		layoutNorth.putConstraint(SpringLayout.NORTH, heightType, 0, SpringLayout.NORTH, widthType);
		//layoutNorth.putConstraint(SpringLayout.WEST, renderAsLabel, isMacLAF ? 8 : 16, SpringLayout.EAST, heightType);
		//layoutNorth.putConstraint(SpringLayout.VERTICAL_CENTER, renderAsLabel, 0, SpringLayout.VERTICAL_CENTER, renderAs);
		layoutNorth.putConstraint(SpringLayout.EAST, renderAs, isMacLAF ? -4 : -8, SpringLayout.EAST, settingsNorth);
		layoutNorth.putConstraint(SpringLayout.NORTH, renderAs, 0, SpringLayout.NORTH, widthType);

		settingsNorth.setLayout(layoutNorth);
		int preferredWidthNorth = layoutNorth.getConstraints(widthLabel).getWidth().getPreferredValue()
				+ layoutNorth.getConstraints(widthType).getWidth().getPreferredValue()
				+ layoutNorth.getConstraints(heightLabel).getWidth().getPreferredValue()
				+ layoutNorth.getConstraints(heightType).getWidth().getPreferredValue()
		//		+ layoutNorth.getConstraints(renderAsLabel).getWidth().getPreferredValue()
				+ layoutNorth.getConstraints(renderAs).getWidth().getPreferredValue()
				+ (isMacLAF ? 34 : 68);
		int preferredHeightNorth = layoutNorth.getConstraints(widthType).getY().getPreferredValue()
				+ layoutNorth.getConstraints(widthType).getHeight().getPreferredValue()
				+ 12;
		settingsNorth.setPreferredSize(new Dimension(preferredWidthNorth, preferredHeightNorth));
		settings.add(settingsNorth, BorderLayout.NORTH);

		card = new JPanel();
		card.setBackground(Color.WHITE);
		card.setLayout(new CardLayout());
		card.add(sizeDefault = new SplitterSizePanel(SplitterSizePanel.DEVICE_TYPE.DEFAULT, owner), SplitterSizePanel.DEVICE_TYPE.DEFAULT.toString());
		card.add(sizeUniversal = new SplitterSizePanel(SplitterSizePanel.DEVICE_TYPE.UNIVERSAL, owner), SplitterSizePanel.DEVICE_TYPE.UNIVERSAL.toString());
		card.add(deviceSpecificTabs = new JTabbedPane(), SplitterSizePanel.DEVICE_TYPE.IPHONE.toString());
		sizeiPhone = SplitterSizePanel.newAsTab(SplitterSizePanel.DEVICE_TYPE.IPHONE, deviceSpecificTabs, owner);
		sizeiPad = SplitterSizePanel.newAsTab(SplitterSizePanel.DEVICE_TYPE.IPAD, deviceSpecificTabs, owner);
		sizeAppleWatch = SplitterSizePanel.newAsTab(SplitterSizePanel.DEVICE_TYPE.APPLE_WATCH, deviceSpecificTabs, owner);
		sizeMac = SplitterSizePanel.newAsTab(SplitterSizePanel.DEVICE_TYPE.MAC, deviceSpecificTabs, owner);
		settings.add(card, BorderLayout.CENTER);

		widthType.addItemListener(sizeUniversal.createSizeChangedItemListener(owner, widthType, heightType));
		heightType.addItemListener(sizeUniversal.createSizeChangedItemListener(owner, widthType, heightType));
		widthType.addItemListener(sizeiPhone.createSizeChangedItemListener(owner, widthType, heightType));
		heightType.addItemListener(sizeiPhone.createSizeChangedItemListener(owner, widthType, heightType));
		widthType.addItemListener(sizeiPad.createSizeChangedItemListener(owner, widthType, heightType));
		heightType.addItemListener(sizeiPad.createSizeChangedItemListener(owner, widthType, heightType));

		JPanel settingsSouth = new JPanel();
		settingsSouth.setBackground(Color.WHITE);

		// Scaling
		Vector<String> scalingItems = new Vector<String>();
		scalingItems.add(owner.getResource("splitter.item.noresize", "No resizing"));
		scalingItems.add(owner.getResource("splitter.item.fittoshortside", "Fit to short side"));
		scalingItems.add(owner.getResource("splitter.item.fittolongside", "Fit to long side"));
		scalingItems.add(owner.getResource("splitter.item.fittoscreen", "Fit with aspect ratio"));
		scalingItems.add(owner.getResource("splitter.item.noaspectratio", "Fit without aspect ratio"));
		scalingType = new JComboBox(scalingItems);
		scalingType.setToolTipText(owner.getResource("splitter.label.scaling", "Scaling Options"));
		scalingType.setFont(fontSmall);
		scalingType.setSelectedIndex(3);
		scalingType.addItemListener(propertyChangedItemListener);
		settingsSouth.add(scalingType);

		// background color
		final String BGCOLOR_PLACEHOLDER = owner.getResource("splitter.placeholder.transparent", "Transparent");
		bgcolor = new JTextField(8);
		bgcolor.setFont(fontSmall);
		bgcolor.setToolTipText(owner.getResource("tooltip.splash.bgcolor", "tooltip.splash.bgcolor"));
		bgcolor.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (bgcolor.getText().trim().equals(BGCOLOR_PLACEHOLDER)) {
					bgcolor.setText("");
				} else {
					bgcolor.selectAll();
				}
			}
			@Override public void focusLost(FocusEvent e) {
				setBackgroundColor(bgcolor.getText());
			}
		});
		JButton bgcolorButton = new JButton("...");
		bgcolorButton.setFont(fontSmall);
		bgcolorButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Color c = null;
				try {
					c = JColorChooser.showDialog(bgcolor, "Color Chooser", bgcolor.getText().equals(BGCOLOR_PLACEHOLDER) ? null : new Color(Long.valueOf(bgcolor.getText(), 16).intValue(), true));
				} catch (Throwable t) {
					owner.handleThrowable(t);
				}
				if (c != null) {
					setBackgroundColor(String.format("%2h%2h%2h%2h", c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue()).replace(' ', '0'));
				}
			}
		});
		JLabel bgcolorLabel = new JLabel(owner.getResource("splitter.label.bgcolor", "Background Color:"));
		settingsSouth.add(bgcolor);
		settingsSouth.add(bgcolorButton);
		settingsSouth.add(bgcolorLabel);

		settingsSouth.add(this.overwriteAlways = new JCheckBox(owner.getResource("splitter.overwrite.always", "Overwrite always"), false));
		this.overwriteAlways.setToolTipText(owner.getResource("splitter.overwrite.always.tooltip", "Overwrite always"));
		this.overwriteAlways.setBackground(Color.WHITE);
		this.overwriteAlways.addItemListener(propertyChangedItemListener);

		settingsSouth.add(this.cleanBeforeGenerate = new JCheckBox(owner.getResource("splitter.clean.before.generate", "Clean"), false));
		this.cleanBeforeGenerate.setToolTipText(owner.getResource("splitter.clean.before.generate.tooltip", "Clean before generate"));
		this.cleanBeforeGenerate.setBackground(Color.WHITE);
		this.cleanBeforeGenerate.addItemListener(propertyChangedItemListener);

		JLabel subdirLabel = new JLabel(owner.getResource("splitter.label.subdir", "Output Dir :"), SwingConstants.LEFT);
		subdirLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		subdirLabel.setToolTipText(owner.getResource("splitter.open.subdir", "Click to open previous target directory."));
		settingsSouth.add(subdirLabel);
		subdirLabel.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				openOutputDirectory();
			}
		});
		settingsSouth.add(this.subdir = new JTextField(placeHolder, 16));
		Insets insets = new Insets(2, 2, 2, 2);
		this.subdir.setMargin(insets);
		this.subdir.setFont(fontNormal);
		this.subdir.setToolTipText(owner.getResource("splitter.tooltip.subdir", "Same as origin with empty value. Relative and absolute pathes are available."));
		this.subdir.setForeground(Color.GRAY);
		this.subdir.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { this.setStorePropertiesRequested(); }
			@Override public void insertUpdate(DocumentEvent e) { this.setStorePropertiesRequested(); }
			@Override public void removeUpdate(DocumentEvent e) { this.setStorePropertiesRequested(); }
			private void setStorePropertiesRequested() {
				if (subdir.getText().trim().isEmpty() || subdir.getText().trim().equals(placeHolder)) {
					return;
				}
				owner.setStorePropertiesRequested(true);
			}
		});
		this.subdir.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (subdir.getText().trim().equals(placeHolder)) {
					setOutputDirectory("", true);
				} else {
					subdir.selectAll();
				}
			}
			@Override public void focusLost(FocusEvent e) {
				if (subdir.getText().trim().isEmpty()) {
					setOutputDirectory(placeHolder);
				}
			}
		});

		SpringLayout layoutSouth = new SpringLayout();
		layoutSouth.putConstraint(SpringLayout.EAST, scalingType, -16, SpringLayout.HORIZONTAL_CENTER, settingsSouth);
		layoutSouth.putConstraint(SpringLayout.NORTH, scalingType, isMacLAF ? 8 : 10, SpringLayout.NORTH, settingsSouth);
		//layoutSouth.putConstraint(SpringLayout.EAST, scalingLabel, -4, SpringLayout.WEST, scalingType);
		//layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, scalingLabel, 4, SpringLayout.VERTICAL_CENTER, scalingType);

		layoutSouth.putConstraint(SpringLayout.WEST, bgcolorLabel, 8, SpringLayout.HORIZONTAL_CENTER, settingsSouth);
		layoutSouth.putConstraint(SpringLayout.WEST, bgcolor, isMacLAF ? 0 : 2, SpringLayout.EAST, bgcolorLabel);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, bgcolor, 0, SpringLayout.VERTICAL_CENTER, scalingType);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, bgcolorLabel, 0, SpringLayout.VERTICAL_CENTER, scalingType);
		layoutSouth.putConstraint(SpringLayout.WEST, bgcolorButton, isMacLAF ? 0 : 4, SpringLayout.EAST, bgcolor);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, bgcolorButton, 0, SpringLayout.VERTICAL_CENTER, scalingType);

		layoutSouth.putConstraint(SpringLayout.EAST, subdirLabel, isMacLAF ? -2 : -4, SpringLayout.WEST, subdir);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, subdirLabel, 0, SpringLayout.VERTICAL_CENTER, subdir);
		layoutSouth.putConstraint(SpringLayout.EAST, subdir, -16, SpringLayout.WEST, overwriteAlways);
		layoutSouth.putConstraint(SpringLayout.NORTH, subdir, 8, SpringLayout.SOUTH, scalingType);
		layoutSouth.putConstraint(SpringLayout.EAST, overwriteAlways, -8, SpringLayout.WEST, cleanBeforeGenerate);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, overwriteAlways, 0, SpringLayout.VERTICAL_CENTER, subdir);
		layoutSouth.putConstraint(SpringLayout.EAST, cleanBeforeGenerate, -16, SpringLayout.EAST, settingsSouth);
		layoutSouth.putConstraint(SpringLayout.VERTICAL_CENTER, cleanBeforeGenerate, 0, SpringLayout.VERTICAL_CENTER, subdir);
		settingsSouth.setLayout(layoutSouth);
		int preferredWidthSouth1 = layoutSouth.getConstraints(subdirLabel).getWidth().getPreferredValue()
				+ layoutSouth.getConstraints(subdir).getWidth().getPreferredValue()
				+ layoutSouth.getConstraints(overwriteAlways).getWidth().getPreferredValue()
				+ layoutSouth.getConstraints(cleanBeforeGenerate).getWidth().getPreferredValue()
				+ (isMacLAF ? 48 : 56);
		int preferredWidthSouth2 = layoutSouth.getConstraints(bgcolorButton).getX().getPreferredValue()
				+ layoutSouth.getConstraints(bgcolorButton).getWidth().getPreferredValue()
				+ 16;
		int preferredWidthSouth = preferredWidthSouth1 > preferredWidthSouth2 ? preferredWidthSouth1 : preferredWidthSouth2;
		int preferredHeightSouth = layoutSouth.getConstraints(subdir).getY().getPreferredValue()
				+ layoutSouth.getConstraints(subdir).getHeight().getPreferredValue()
				+ 10;
		settingsSouth.setPreferredSize(new Dimension(preferredWidthSouth, preferredHeightSouth));

		settings.add(settingsSouth, BorderLayout.SOUTH);
		this.add(settings, BorderLayout.NORTH);

		splitButton = new JButton(owner.getResource("splitter.button", "Split"), new ImageIcon(this.getClass().getResource("img/splitter.png")));
		splitButton.setBackground(new Color(0x007AFF));
		splitButton.setForeground(Color.WHITE);
		splitButton.setBorderPainted(false);
		splitButton.setFocusPainted(false);
		splitButton.setDisabledIcon(new ImageIcon(this.getClass().getResource("img/generate.gif")));
		splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/splitter_rollover.png")));
		splitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		splitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		splitButton.setFont(fontLarge);
		splitButton.setMargin(new Insets(48, 16, 48, 16));
		splitButton.setOpaque(true);
		//splitButton.setTransferHandler(new TransferHandler() {
		this.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (isGenerating()) {
						return false;
					}

					if (canImport(support)) {
						final List<File> files = new LinkedList<File>();
						if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
							if (list instanceof List<?>) {
								for (Object o : ((List<?>)list)) {
									if (o instanceof File) {
										files.add((File)o);
									}
								}
							}
						} else if (support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
							String urls = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
							StringTokenizer tokens = new StringTokenizer(urls);
							while (tokens.hasMoreTokens()) {
								try {
									File f = new File(URLDecoder.decode((new URL(tokens.nextToken()).getFile()), "UTF-8"));
									if (f.isFile()) {
										files.add(f);
									}
								} catch (Exception ex) {
									System.out.println(ex.getMessage());
								}
			                }
						}
						if (files.size() <= 0) {
							return false;
						}
						setAlwaysOnTop(true);
						toFront();
						requestFocus();
						setAlwaysOnTop(false);
						// JOptionPane.showConfirmDialog has abnormal focus during dropping files on mac. Try another thread.
						(new SwingWorker<Boolean, Integer>() {
							@Override protected Boolean doInBackground() throws Exception {
								try {
									return dropFiles(files);
								} catch (Throwable t) {
									owner.handleThrowable(t);
								}
								return false;
							}
						}).execute();
						return true;
					}
				} catch (Throwable t) {
					owner.handleThrowable(t);
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
					for (DataFlavor flavor : support.getDataFlavors()) {
						if (flavor.getSubType().equals("uri-list")) {
							result = true;
							break;
						}
					}
	            }
				return result;
			}
		});
		splitButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (isGenerating()) {
					if (!yesNo(owner.getResource("splitter.question.cancel", "Are you sure to cancel generating images?"))) {
						return;
					}
					try {
						pool.shutdownNow();
					} catch (Throwable t) {
						owner.handleThrowable(t);
					}
					splitButton.setText(owner.getResource("label.cancel.generate", "Cancel generate..."));
					return;
				}

				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				chooser.setApproveButtonText(owner.getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setMultiSelectionEnabled(true);
				if (previousTargetDirectory == null) {
					previousTargetDirectory = owner.getChosenDirectory();
				}
				if (previousTargetDirectory != null) {
					if (previousTargetDirectory.isFile()) {
						chooser.setCurrentDirectory(previousTargetDirectory.getParentFile());
					} else {
						chooser.setCurrentDirectory(previousTargetDirectory);
					}
				}
				int returnVal = chooser.showOpenDialog(SplitterFrame.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						List<File> files = new LinkedList<File>();
						for (File f : chooser.getSelectedFiles()) {
							files.add(f);
						}
						dropFiles(files);
					} catch (Throwable t) {
						owner.handleThrowable(t);
					}
			    }

			}
		});
		this.add(splitButton, BorderLayout.CENTER);

		progress = new JProgressBar();
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setBorderPainted(false);
		progress.setBackground(isMacLAF ? Color.WHITE : MainFrame.BGCOLOR_LIGHT_GRAY);
		progress.setForeground(splitButton.getBackground());
		progress.setOpaque(true);
		this.add(progress, BorderLayout.SOUTH);

		this.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				if (owner.isBatchMode()) {
					return;
				}
				if (isGenerating()) {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					alert(owner.getResource("error.close.window", "Please wait until generating process will be finished."));
					return;
				}
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			@Override public void windowOpened(WindowEvent e) {
				// Swing components sometimes will not be drawn on some graphics borad. Hack it.
				setSize(getSize().width + 1, getSize().height + 1);
				pack();
				repaint();	// This code does not need, maybe.
			}
		});
		this.pack();
		this.applyComponentStatus();
	}
	
	/**
	 * Open previous target directory.
	 */
	protected void openOutputDirectory() {
		try {
			File dir = previousOutputDirectory;
			if (dir == null && !IOSImageUtil.isNullOrWhiteSpace(subdir.getText())) {
				dir = new File(subdir.getText());
				if (!dir.isAbsolute()) {
					dir = null;
				}
			}
			if (dir == null && previousTargetDirectory != null) {
				dir = previousTargetDirectory;
				if (!IOSImageUtil.isNullOrWhiteSpace(subdir.getText())) {
					dir = new File(dir, subdir.getText());
				}
			}
			if (dir != null && dir.exists()) {
				Desktop.getDesktop().open(dir);
			}
		} catch (Throwable t) {
			owner.handleThrowable(t);
		}
	}

	/**
	 * true - Generating images just now.
	 *
	 * @return
	 */
	protected boolean isGenerating() {
		return (pool != null && !pool.isTerminated());
	}

	/**
	 * Process dropped files.
	 *
	 * @param files
	 * @return
	 */
	protected boolean dropFiles(List<File> files) {
		try {
			if (this.isGenerating()) {
				return false;
			}

			contentCache.clear();
			LinkedList<IOSImageSet> queue = new LinkedList<IOSImageSet>();
			HashMap<String, File> deleteTargets = new HashMap<String, File>();
			for (File file : files) {
				if (!file.exists()) {
					alert("[" + file.getCanonicalPath() + "] " + owner.getResource("error.not.exists", "is not exists."));
					continue;
				}
				if (!file.isFile()) {
					alert("[" + file.getCanonicalPath() + "] " + owner.getResource("error.not.file", "is directory. Choose file."));
					continue;
				}
				previousTargetDirectory = file.getParentFile();
				File dir = file.getParentFile();
				String sub = this.subdir.getText().trim();
				if (sub.equals(owner.getResource("splitter.label", "Same as origin."))) {
					sub = "";
				}
				if (!sub.isEmpty()) {
					File subPath = new File(sub);
					if (subPath.isAbsolute()) {
						// absolute path.
						dir = subPath;
						if (!dir.exists() || !dir.isDirectory()) {
							alert(owner.getResource("splitter.error.subdir", "Only existance directory is available with absolute path."));
							return false;
						}
					} else {
						// relative path.
						dir = new File(dir, sub);
						if (!dir.exists()) {
							dir.mkdirs();
						}
					}
				}
				previousOutputDirectory = dir;
				if (this.asImageSets.isSelected()) {
					if (this.universal.isSelected()) {
						queue = this.sizeUniversal.addImageQueue(owner, queue, file, dir);
					} else {
						queue = this.sizeiPhone.addImageQueue(owner, queue, file, dir);
						queue = this.sizeiPad.addImageQueue(owner, queue, file, dir);
						queue = this.sizeAppleWatch.addImageQueue(owner, queue, file, dir);
						queue = this.sizeMac.addImageQueue(owner, queue, file, dir);
					}
					if (this.cleanBeforeGenerate.isSelected() && queue.size() > 0) {
						IOSImageSet imageSet = queue.peek();
						deleteTargets.put(imageSet.getImageName(), imageSet.getFile().getParentFile());
					}
				} else {
					queue = this.sizeDefault.addImageQueue(owner, queue, file, dir);
				}
			}

			if (!owner.isBatchMode() && !this.overwriteAlways.isSelected()) {
				String existingFilename = null;
				for (IOSImageSet imageSets : queue) {
					if (imageSets.getFile().exists()) {
						existingFilename = imageSets.getFile().getAbsolutePath();
						break;
					}
				}
				if (existingFilename != null) {
					if (!yesNo(String.format(owner.getResource("splitter.already.exists", "[%s] already exists. Replace file?"), existingFilename))) {
						return false;
					}
				}
			}

			if (this.asImageSets.isSelected() && this.cleanBeforeGenerate.isSelected() && deleteTargets.size() > 0) {
				for (Map.Entry<String, File>keyValue : deleteTargets.entrySet()) {
					File targetDirectory = keyValue.getValue();
					final String orgFilename = keyValue.getKey();
					String[] filenames = targetDirectory.list(new FilenameFilter() {
						@Override public boolean accept(File dir, String name) {
							if (!IOSImageUtil.isNullOrWhiteSpace(name)) {
								if (name.toLowerCase().trim().matches(String.format("^%s-(iphone|ipad|mac|watch|universal)+(-compact(w|h)|-regular(w|h)|-retina4|-38mm|-42mm)*(-compact(w|h)|-regular(w|h)|-retina4)*(-compact(w|h)|-regular(w|h)|-retina4)*@(1|2|3)x\\.png$", orgFilename))) {
									return true;
								}
							}
							return false;
						}
					});
					if (filenames != null) {
						for (String filename : filenames) {
							File f = new File(targetDirectory, filename);
							if (f.exists() && f.isFile()) {
								if (!f.delete()) {
									System.err.println("Could not delete file - " + f.getAbsolutePath());
								}
							}
						}
					}
				}
			}

			progress.setMaximum(queue.size());
			progress.setValue(0);
			if (owner.isBatchMode()) {
				if (!owner.isSilentMode() && !owner.isVerboseMode()) {
					System.out.println("Split Images:");
					for (int i = 0; i < progress.getMaximum(); i++) {
						System.out.print(".");
					}
				}
				System.out.print((char)0x0D);
			} else {
				this.enableGUI(false);
			}

			pool = Executors.newFixedThreadPool(Math.min(IOSImageUtil.getSystemIntProperty("org.gootara.ios.image.util.max.threads", 12), Math.max(IOSImageUtil.getSystemIntProperty("org.gootara.ios.image.util.max.threads", 4), Runtime.getRuntime().availableProcessors())));
			IOSImageSet imageSets;
			while ((imageSets = queue.poll()) != null) {
				if (!imageSets.getOriginalFile().exists()) {
					alert("[" + imageSets.getFile().getCanonicalPath() + "] " + owner.getResource("error.not.exists", "is not exists."));
					this.addProgress(false);
					continue;
				}
				split(imageSets);
			}

			SwingWorker<Boolean, Integer> watchdog = new SwingWorker<Boolean, Integer>() {
				@Override protected Boolean doInBackground() throws Exception {
					boolean result = true;
					try {
						pool.shutdown();
						if (pool.awaitTermination(progress.getMaximum(), TimeUnit.MINUTES)) {
							if (asImageSets.isSelected()) {
								for (ArrayList<IOSImageSet> list : contentCache.values()) {
									if (list.size() <= 0) {
										continue;
									}

									Collections.sort(list);
									IOSAssetCatalogs.JsonUtility jsonUtility = new IOSAssetCatalogs.JsonUtility();
									jsonUtility.addAll(list);
									if (renderAs.getSelectedIndex() != 0) {
										jsonUtility.setInfo(IOSImageSet.JSON_INFO_KEY.RENDERING, renderAs.getSelectedIndex() == 2 ? IOSImageSet.JSON_INFO_VALUE.RENDERING_TEMPLATE : IOSImageSet.JSON_INFO_VALUE.RENDERING_ORIGINAL);
									}

									File f = new File(list.get(0).getFile().getParentFile(), owner.getResource("string.filename.contents.json", "Contents.json"));
									jsonUtility.writeContentJson(f);
									list.clear();
								}
							}
						} else {
							result = false;
						}
					} catch (Throwable t) {
						owner.handleThrowable(t);
					} finally {
						if (owner.isBatchMode()) {
							if (!owner.isSilentMode() && !owner.isVerboseMode()) {
								System.out.println();
								System.out.println();
							}
						} else {
							SplitterFrame.this.enableGUI(true);
						}
						contentCache.clear();
						System.gc();
						progress.setValue(0);
					}
					return result;
				}
			};
			watchdog.execute();
			if (owner.isBatchMode()) {
				return watchdog.get();
			}
		} catch (Throwable t) {
			owner.handleThrowable(t);
			if (owner.isBatchMode()) {
				if (!owner.isSilentMode() && !owner.isVerboseMode()) {
					System.out.println();
					System.out.println();
				}
			} else {
				this.enableGUI(true);
			}
			return false;
		}
		return true;
	}

	/**
	 * Set gui components availability.
	 *
	 * @param b
	 */
	private void enableGUI(boolean b) {
		if (owner.isBatchMode()) {
			return;
		}

		splitButton.setText(b ? owner.getResource("splitter.button", "Split") : owner.getResource("splitter.executing", "Split images..."));
		splitButton.setBackground(b ? new Color(0x007AFF) : new Color(0xF7F7F7));
		splitButton.setForeground(b ? Color.WHITE : Color.GRAY);
		splitButton.setIcon(new ImageIcon(this.getClass().getResource(b ? "img/splitter.png" : "img/generate.gif")));
		splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource(b ? "img/splitter_rollover.png" : "img/generate.gif")));
		splitButton.setEnabled(b);
		setContainerEnabled(settings, b);
		if (b) {
			this.applyComponentStatus();
		}
	}

	/**
	 * Set container enabled.
	 *
	 * @param parent
	 * @param b
	 */
	protected static void setContainerEnabled(Container parent, boolean b) {
		for (Component c : parent.getComponents()) {
			if (c instanceof JTextField) {
				((JTextField)c).setEditable(b);
			} else {
				c.setEnabled(b);
			}
			if (c instanceof Container) {
				setContainerEnabled((Container)c, b);
			}
		}
	}

	/**
	 * Add progress.
	 */
	private void addProgress(boolean b) {
		if (owner.isBatchMode()) {
			if (!owner.isSilentMode() && !owner.isVerboseMode()) {
				System.out.print(b ? "o" : "-");
			}
		} else {
			progress.setValue(progress.getValue() + 1);
		}
	}

	/**
	 * Apply properties.
	 *
	 * @param props properties
	 * @return same as props
	 */
	protected Properties applyProperties(Properties props, boolean system) {
		Properties def = this.getDefaultProperties(new Properties());
		this.setAsImageSets(owner.getBoolProperty(props, "splitter.as.imagesets.selected", def));
		this.setUniversal(owner.getBoolProperty(props, "splitter.universal.selected", def));
		this.setDeviceSpecific(owner.getBoolProperty(props, "splitter.device.specific.selected", def));
		this.setWidthType(owner.getIntProperty(props, "splitter.width.type", def));
		this.setHeightType(owner.getIntProperty(props, "splitter.height.type", def));
		this.setRenderAs(owner.getIntProperty(props, "splitter.render.as", def));

		this.setOverwriteAlways(owner.getBoolProperty(props, "splitter.overwrite.always.selected", def));
		this.setCleanBeforeGenerate(owner.getBoolProperty(props, "splitter.clean.before.generate.selected", def));
		this.setOutputDirectory(props.getProperty("splitter.sub.directory", "").trim().equals(owner.getResource("splitter.label", "Same as origin.")) ? "" : owner.getStringProperty(props, "splitter.sub.directory", def));
		if (owner.getBoolProperty(props, "splitter.as3x.selected", def)) {
			information(owner.getResource("splitter.info.as3x", "'splitter.as3x.selected' property was deprecated. Use 100% size instead."));
			this.setWidth1x("100 %");
			this.setHeight1x("100 %");
		}
		this.setBackgroundColor(owner.getStringProperty(props, "splitter.image.bgcolor", def));
		this.setScalingType(owner.getIntProperty(props, "splitter.scaling.type", def));

		if (system) {
			String lastTargetDirectory = props.getProperty("system.splitter.last.target.directory", "").trim();
			if (!lastTargetDirectory.isEmpty()) {
				previousTargetDirectory = new File(lastTargetDirectory);
				if (!previousTargetDirectory.exists() || !previousTargetDirectory.isDirectory()) {
					previousTargetDirectory = null;
				}
			} else {
				previousTargetDirectory = null;
			}
		}
		props = this.sizeDefault.applyProperties(owner, props);
		props = this.sizeUniversal.applyProperties(owner, props);
		props = this.sizeiPhone.applyProperties(owner, props);
		props = this.sizeiPad.applyProperties(owner, props);
		props = this.sizeAppleWatch.applyProperties(owner, props);
		props = this.sizeMac.applyProperties(owner, props);
		return props;
	}

	/**
	 * Store properties.
	 *
	 * @param props properties
	 * @return same as props
	 */
	protected Properties storeProperties(Properties props, boolean system) {
		props.put("splitter.overwrite.always.selected", Boolean.toString(this.overwriteAlways.isSelected()));
		props.put("splitter.clean.before.generate.selected", Boolean.toString(this.cleanBeforeGenerate.isSelected()));
		props.put("splitter.sub.directory", this.getOutputDirectory());
		props.put("splitter.as.imagesets.selected", Boolean.toString(this.asImageSets.isSelected()));
		props.put("splitter.universal.selected", Boolean.toString(this.universal.isSelected()));
		props.put("splitter.device.specific.selected", Boolean.toString(this.deviceSpecific.isSelected()));
		props.put("splitter.width.type", Integer.toString(this.widthType.getSelectedIndex()));
		props.put("splitter.height.type", Integer.toString(this.heightType.getSelectedIndex()));
		props.put("splitter.render.as", Integer.toString(this.renderAs.getSelectedIndex()));
		props.put("splitter.image.bgcolor", this.getBackgroundColor());
		props.put("splitter.scaling.type", Integer.toString(this.scalingType.getSelectedIndex()));
		if (system) {
			try {
				props.put("system.splitter.last.target.directory", previousTargetDirectory == null ? "" : previousTargetDirectory.getCanonicalPath());
			} catch (Exception ex) {
				ex.printStackTrace(); props.put("system.splitter.last.target.directory", "");
			}
		}
		props = this.sizeDefault.storeProperties(props);
		props = this.sizeUniversal.storeProperties(props);
		props = this.sizeiPhone.storeProperties(props);
		props = this.sizeiPad.storeProperties(props);
		props = this.sizeAppleWatch.storeProperties(props);
		props = this.sizeMac.storeProperties(props);
		return props;
	}

	/**
	 * Put default properties.
	 *
	 * @param props
	 * @return
	 */
	protected Properties getDefaultProperties(Properties props) {
		props.put("splitter.width1x", "44 px");
		props.put("splitter.height1x", "44 px");
		props.put("splitter.overwrite.always.selected", Boolean.toString(false));
		props.put("splitter.clean.before.generate.selected", Boolean.toString(true));
		props.put("splitter.sub.directory", "");
		props.put("splitter.as3x.selected", (new Boolean(false)).toString());
		props.put("splitter.as.imagesets.selected", (new Boolean(false)).toString());
		props.put("splitter.universal.selected", (new Boolean(true)).toString());
		props.put("splitter.device.specific.selected", (new Boolean(false)).toString());
		props.put("splitter.width.type", Integer.toString(0));
		props.put("splitter.height.type", Integer.toString(0));
		props.put("splitter.image.bgcolor", "");
		props.put("splitter.scaling.type", Integer.toString(3));
		props = this.sizeDefault.getDefaultProperties(props);
		props = this.sizeUniversal.getDefaultProperties(props);
		props = this.sizeiPhone.getDefaultProperties(props);
		props = this.sizeiPad.getDefaultProperties(props);
		props = this.sizeAppleWatch.getDefaultProperties(props);
		props = this.sizeMac.getDefaultProperties(props);
		return props;
	}

	// Options
	private void setAsImageSets(boolean b) { this.asImageSets.setSelected(b); this.applyComponentStatus(); }
	private void setUniversal(boolean b) { this.universal.setSelected(b); this.applyComponentStatus(); }
	private void setDeviceSpecific(boolean b) { this.deviceSpecific.setSelected(b); this.applyComponentStatus(); }
	private void setWidthType(int index) { this.widthType.setSelectedIndex(index); this.applyComponentStatus(); }
	private void setHeightType(int index) { this.heightType.setSelectedIndex(index); this.applyComponentStatus(); }
	private void setRenderAs(int index) { this.renderAs.setSelectedIndex(index); }

	protected void setWidth1x(String width) { sizeDefault.setWidthAny(width); }
	protected void setHeight1x(String height) { sizeDefault.setHeightAny(height); }
	protected void setOverwriteAlways(boolean b) { this.overwriteAlways.setSelected(b); }
	protected void setCleanBeforeGenerate(boolean b) { this.cleanBeforeGenerate.setSelected(b); }
	protected void setOutputDirectory(String relativePath) { this.setOutputDirectory(relativePath, false); }
	private void setOutputDirectory(String relativePath, boolean focusGained) {
		String placeHolder = owner.getResource("splitter.label", "Same as origin.");
		if (IOSImageUtil.isNullOrWhiteSpace(relativePath) || relativePath.equals(placeHolder)) {
			subdir.setText(focusGained ? "" : placeHolder);
			subdir.setForeground(focusGained ? Color.BLACK : MainFrame.COLOR_GRAY);
		} else {
			this.subdir.setText(relativePath);
			subdir.setForeground(Color.BLACK);
		}
	}
	protected void setScalingType(int index) { scalingType.setSelectedIndex(index); }

	/**
	 * Get output directory
	 *
	 * @return
	 */
	private String getOutputDirectory() {
		if (subdir.getText().trim().isEmpty() || owner.getResource("splitter.label", "Same as origin.").equals(subdir.getText())) {
			return new String();
		}
		return subdir.getText();
	}

	/**
	 * Set image background color.
	 *
	 * @param s color string
	 */
	public void setBackgroundColor(String s) {
		final String BGCOLOR_PLACEHOLDER = owner.getResource("splitter.placeholder.transparent", "Transparent");
		String prevText = this.bgcolor.getText().trim().isEmpty() ? BGCOLOR_PLACEHOLDER : this.bgcolor.getText();
		Color col = null;
		if (!IOSImageUtil.isNullOrWhiteSpace(s)) {
			while (s.length() < 6) s = "0".concat(s);
			if (s.length() > 8) s = s.substring(0, 8);
			if (s.length() == 7) s = "0".concat(s);
			try {
				col = new Color(Long.valueOf(s, 16).intValue(), true);
				bgcolor.setText(s);
				bgcolor.setBackground(new Color(col.getRed(), col.getGreen(), col.getBlue()));
				float[] hsb = Color.RGBtoHSB(col.getRed(), col.getGreen(), col.getBlue(), null);
				//bgcolor.setForeground(new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)Math.floor(Math.acos(hsb[2])))));
				bgcolor.setForeground(new Color(Color.HSBtoRGB(hsb[0], 1.0f - hsb[1], 1.0f - hsb[2])));
			} catch (Exception ex) {
				bgcolor.setBackground(Color.WHITE);
				bgcolor.setForeground(Color.LIGHT_GRAY);
				ex.printStackTrace();
			}
		}
		if (col == null) {
			bgcolor.setText(BGCOLOR_PLACEHOLDER);
			bgcolor.setForeground(Color.LIGHT_GRAY);
			bgcolor.setBackground(Color.WHITE);
		}
		if (!prevText.equals(this.bgcolor.getText())) {
			owner.setStorePropertiesRequested(true);
		}
	}

	/**
	 * Get background color text.
	 *
	 * @return background color text
	 */
	public String getBackgroundColor() {
		final String BGCOLOR_PLACEHOLDER = owner.getResource("splitter.placeholder.transparent", "Transparent");
		if (bgcolor.getText().trim().isEmpty() || (BGCOLOR_PLACEHOLDER).equals(bgcolor.getText())) {
			return new String();
		}
		return bgcolor.getText();
	}

	/**
	 * Get image background color.
	 *
	 * @return
	 */
	public Color getImageBackgroundColor() {
		try {
			if (!this.getBackgroundColor().isEmpty()) {
				return new Color(Long.valueOf(this.getBackgroundColor(), 16).intValue(), true);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Apply components status by item selections.
	 */
	private void applyComponentStatus() {
		this.universal.setVisible(this.asImageSets.isSelected());
		this.deviceSpecific.setVisible(this.asImageSets.isSelected());
		if (this.asImageSets.isSelected()) {
			((CardLayout)card.getLayout()).show(card, this.universal.isSelected() ? SplitterSizePanel.DEVICE_TYPE.UNIVERSAL.toString() : SplitterSizePanel.DEVICE_TYPE.IPHONE.toString());
		} else {
			((CardLayout)card.getLayout()).show(card, SplitterSizePanel.DEVICE_TYPE.DEFAULT.toString());
		}
		this.widthType.setVisible(this.asImageSets.isSelected());
		this.heightType.setVisible(this.asImageSets.isSelected());
		this.widthLabel.setVisible(this.asImageSets.isSelected());
		this.heightLabel.setVisible(this.asImageSets.isSelected());
		this.renderAs.setVisible(this.asImageSets.isSelected());

		this.cleanBeforeGenerate.setEnabled(this.asImageSets.isSelected());

		SplitterSizePanel[] sizes = { this.sizeDefault, this.sizeUniversal, this.sizeiPhone, this.sizeiPad, this.sizeAppleWatch, this.sizeMac };
		for (SplitterSizePanel p : sizes) {
			p.applyComponentStatus();
		}
	}

	/**
	 * Do split images with scale.
	 *
	 * @param srcFile	image file
	 * @param src		buffer
	 * @param scale		scale
	 * @throws Exception
	 */
	private void split(final IOSImageSet imageSet) {
		SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
			@Override protected Boolean doInBackground() throws Exception {
				boolean result = false;
				try {
					if (!imageSet.getFile().getParentFile().exists()) {
						imageSet.getFile().getParentFile().mkdirs();
					}
					boolean writable = (imageSet.getFile().exists() && imageSet.getFile().canWrite());
					if (!writable && !imageSet.getFile().exists()) {
						try { writable = imageSet.getFile().createNewFile(); } catch (Exception ex) { writable = false; }
					}
					if (!writable) {
						throw new IOException(String.format("%s [%s]", owner.getResource("error.not.writable", "not writable"), imageSet.getFile().getAbsolutePath()));
					}
					owner.verbose(imageSet.getFile());

					BufferedImage src = ImageIO.read(imageSet.getOriginalFile());
					Dimension imageSize = new Dimension(src.getWidth(), src.getHeight());

					// width / height @1x.
					double outputWidth1x = SplitterSizePanel.getImageSize(imageSet.getWidthAsString(), imageSize.getWidth());
					double outputHeight1x = SplitterSizePanel.getImageSize(imageSet.getHeightAsString(), imageSize.getHeight());

					// Rescue empty width / height.
					if (outputWidth1x == 0) {
						outputWidth1x = imageSize.getWidth() * (outputHeight1x / imageSize.getHeight());
					}
					if (outputHeight1x == 0) {
						outputHeight1x = imageSize.getHeight() * (outputWidth1x / imageSize.getWidth());
					}

					Dimension outputSize = new Dimension();
					outputSize.setSize(outputWidth1x * imageSet.getScale().value(), outputHeight1x * imageSet.getScale().value());
					imageSet.setSize(outputSize);
					BufferedImage buf = new BufferedImage(outputSize.width, outputSize.height, BufferedImage.TYPE_INT_ARGB);
					Graphics g = buf.getGraphics();
					Color backgroundColor = getImageBackgroundColor();
					if (backgroundColor != null) {
						g.setColor(backgroundColor);
						g.fillRect(0, 0, outputSize.width, outputSize.height);
					}
					double percentage = 1.0d;
					if (scalingType.getSelectedIndex() == 1) {
						// Fit to short side
						percentage = outputSize.getWidth() > outputSize.getHeight() ? outputSize.getHeight() / imageSize.getHeight() : outputSize.getWidth() / imageSize.getWidth();
					} else if (scalingType.getSelectedIndex() == 2) {
						// Fit to long side
						percentage = outputSize.getWidth() < outputSize.getHeight() ? outputSize.getHeight() / imageSize.getHeight() : outputSize.getWidth() / imageSize.getWidth();
					} else if (scalingType.getSelectedIndex() == 3) {
						// Fit with aspect ratio
						if (outputSize.getWidth() / outputSize.getHeight() == imageSize.getWidth() / imageSize.getHeight()) {
							imageSize.setSize(outputSize);
						} else {
							percentage = outputSize.getWidth() / imageSize.getWidth();
							if (Math.floor(imageSize.getWidth() * percentage) > outputSize.getWidth() || Math.floor(imageSize.getHeight() * percentage) > outputSize.getHeight()) {
								percentage = outputSize.getHeight() / imageSize.getHeight();
							}
							/*
							percentage = outputSize.getWidth() > outputSize.getHeight() ? outputSize.getHeight() / imageSize.getHeight() : outputSize.getWidth() / imageSize.getWidth();
							if (Math.ceil(imageSize.getWidth() * percentage) > outputSize.getWidth() || Math.ceil(imageSize.getHeight() * percentage) > outputSize.getHeight()) {
								percentage = outputSize.getWidth() < outputSize.getHeight() ? outputSize.getHeight() / imageSize.getHeight() : outputSize.getWidth() / imageSize.getWidth();
							}
							*/
						}
					} else if (scalingType.getSelectedIndex() == 4) {
						// Fill without aspect ratio
						imageSize.setSize(outputSize);
					}
					if (percentage != 1.0d) {
						imageSize.setSize(imageSize.getWidth() * percentage, imageSize.getHeight() * percentage);
					}
					Image img = src.getScaledInstance(imageSize.width, imageSize.height, owner.getScalingAlgorithm());
					g.drawImage(img, (int)((outputSize.getWidth() - imageSize.getWidth()) / 2.0), (int)((outputSize.getHeight() - imageSize.getHeight()) / 2.0), SplitterFrame.this);
					img.flush();
					img = null;
					// Image Set image type is always ARGB.
					ImageIO.write(owner.fixImageColor(buf, src), "png", imageSet.getFile());
					//ImageIO.write(buf, "png", imageSet.getFile());
					buf.flush();
					buf = null;
					src.flush();
					src = null;
					addContentCache(imageSet);
					result = true;
				} catch (Throwable t) {
					owner.handleThrowable(t);
				} finally {
					addProgress(result);
				}
				return result;
			}
		};
		pool.submit(worker);
	}

	private synchronized void addContentCache(IOSImageSet imageSet) {
		ArrayList<IOSImageSet> list = contentCache.get(imageSet.getOriginalFile());
		if (list == null) {
			contentCache.put(imageSet.getOriginalFile(), list = new ArrayList<IOSImageSet>());
		}
		list.add(imageSet);
	}


	/**
	 * Show information message.
	 *
	 * @param message	message
	 */
	private void information(String message) {
		owner.information(this, message);
	}

	/**
	 * Show alert.
	 *
	 * @param message	alert message
	 */
	private void alert(String message) {
		owner.alert(this, message);
	}

	/**
	 * Show confirm.
	 * Always 'true' with the batch mode.
	 *
	 * @param message	message
	 * @return	true - ok / false - cancel
	 */
	private boolean confirm(String message) {
		return owner.confirm(this, message);
	}

	/**
	 * Show yes / no dialog.
	 * Always 'false' with the batch mode.
	 *
	 * @param message	message
	 * @return true - yes / false - no
	 */
	private boolean yesNo(String message) {
		return owner.yesNo(this, message);
	}
}



class SplitterSizePanel extends JPanel
{
	public static enum DEVICE_TYPE {
			DEFAULT     ("Default")
		,	UNIVERSAL   ("Universal")
		,	IPHONE      ("iPhone")
		,	IPAD        ("iPad")
		,	APPLE_WATCH ("AppleWatch")
		,	MAC         ("Mac")
		,	CARPLAY     ("CarPlay")
		;
		private final String value;
		private DEVICE_TYPE(final String value) {
			this.value = value;
		}
		public String toString() {
			return this.value;
		}
	};

	private JPanel settings;
	private JTextField widthAny, heightAny, width38mm, height38mm, width42mm, height42mm;
	private JTextField[] widthTraits = { null, null, null };
	private JTextField[] heightTraits = { null, null, null };
	private JCheckBox generateTarget, generateRetina4;
	private JComboBox traits;
	private ImageIcon iconChecked, iconUnchecked;
	private SplitterSizePanel.DEVICE_TYPE type;
	private JTabbedPane tabs;
	private int widthType, heightType;

	public SplitterSizePanel(final SplitterSizePanel.DEVICE_TYPE type, final MainFrame owner) {
		this.type = type;
		widthType = heightType = 0;
		Font fontLarge = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.BOLD, type == DEVICE_TYPE.DEFAULT || type == DEVICE_TYPE.UNIVERSAL ? 14 : 12);
		Font fontSmall = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 11);
		iconChecked = new ImageIcon(this.getClass().getResource("img/checked.png"));
		iconUnchecked = new ImageIcon(this.getClass().getResource("img/unchecked.png"));
		boolean isMacLAF = UIManager.getLookAndFeel().getName().toLowerCase().indexOf("mac") >= 0;
		DocumentListener documentListener = (new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { owner.setStorePropertiesRequested(true); }
			@Override public void insertUpdate(DocumentEvent e) { owner.setStorePropertiesRequested(true); }
			@Override public void removeUpdate(DocumentEvent e) { owner.setStorePropertiesRequested(true); }
		});

		this.setLayout(new BorderLayout());

		settings = new JPanel();

		JPanel northPane = new JPanel();
		northPane.setLayout(new GridLayout(1, 2));
		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			northPane.setBorder(new EmptyBorder(4, 8, 0, 8));
		} else {
			northPane.setBorder(new EmptyBorder(4, 8, 2, 8));
		}
		northPane.add(this.generateTarget = new JCheckBox(owner.getResource("splitter.generate.target", "Generage Images"), false));
		this.generateTarget.setHorizontalAlignment(SwingConstants.LEFT);
		this.generateTarget.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getSource() instanceof JCheckBox) {
					owner.setStorePropertiesRequested(true);
					applyComponentStatus();
				}
			}
		});
		northPane.add(this.generateRetina4 = new JCheckBox(owner.getResource("splitter.generate.retina4", "Generage Retina4"), false));
		this.generateRetina4.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getSource() instanceof JCheckBox) {
					owner.setStorePropertiesRequested(true);
				}
			}
		});
		this.generateRetina4.setHorizontalAlignment(SwingConstants.RIGHT);
		this.generateRetina4.setToolTipText(owner.getResource("splitter.generate.retina4.tooltip", "Same as @2x."));

		JLabel labelSized = new JLabel(owner.getResource("splitter.label.info", "Tooltip will be shown with pointing mouse on TextField."));
		JLabel labelWidth = new JLabel(owner.getResource("splitter.label.width", "Width"));
		JLabel labelHeight = new JLabel(owner.getResource("splitter.label.height", "Height"));
		JLabel labelAny = new JLabel("Any");
		settings.add(labelSized);
		settings.add(labelWidth);
		settings.add(labelHeight);
		labelSized.setFont(fontSmall);
		labelSized.setForeground(MainFrame.COLOR_DARK_GRAY);// Color(0x8E8E93));
		labelWidth.setFont(fontLarge);
		labelHeight.setFont(fontLarge);

		FocusListener focusListener = (new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField text = (JTextField)e.getSource();
					text.select(0, text.getText().indexOf(" ") < 0 ? text.getText().length() : text.getText().indexOf(" "));
				}
			}
			@Override public void focusLost(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField text = (JTextField)e.getSource();
					setSizeText(text, text.getText());
				}
			}
		});
		settings.add(this.widthAny = this.createSizeText(owner.getResource("splitter.label.sized", "Syntax: [@1x px] or [@3x %]"), focusListener, documentListener));
		settings.add(this.heightAny = this.createSizeText(owner.getResource("splitter.label.sized", "Syntax: [@1x px] or [@3x %]"), focusListener, documentListener));

		if (this.getType() != DEVICE_TYPE.DEFAULT) {
			settings.add(labelAny);
		}
		if (this.isSupprtSizeClasses()) {
			traits = new JComboBox();
			traits.setAlignmentX(RIGHT_ALIGNMENT);
			traits.setFont(new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 10));
			traits.addItemListener(new ItemListener() {
				@Override public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						applyComponentStatus();
						if (traits.getSelectedIndex() >= 0) {
							JTextField tt = widthTraits[traits.getSelectedIndex()];
							if (tt.isEditable() && tt.isEditable() && !tt.getText().trim().isEmpty()) {
								tt.requestFocusInWindow();
								tt.selectAll();
							}
						}
					}
				}
			});
			settings.add(traits);

			for (int i = 0; i < widthTraits.length; i++) {
				settings.add(this.widthTraits[i] = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
			}
			for (int i = 0; i < heightTraits.length; i++) {
				settings.add(this.heightTraits[i] = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
			}
		}

		JLabel label38mm, label42mm;
		label38mm = label42mm = null;
		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			settings.add(label38mm = new JLabel(IOSAssetCatalogs.SUBTYPE.MM38.toString()));
			settings.add(label42mm = new JLabel(IOSAssetCatalogs.SUBTYPE.MM42.toString()));
			settings.add(width38mm = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
			settings.add(height38mm = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
			settings.add(width42mm = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
			settings.add(height42mm = this.createSizeText(owner.getResource("splitter.tooltip.optional", "Same as 'Any' with empty value."), focusListener, documentListener));
		}

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelWidth, 0, SpringLayout.HORIZONTAL_CENTER, widthAny);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelHeight, 0, SpringLayout.HORIZONTAL_CENTER, heightAny);
		layout.putConstraint(SpringLayout.NORTH, labelWidth, this.getType() == DEVICE_TYPE.UNIVERSAL ? 8 : 0, SpringLayout.NORTH, settings);
		layout.putConstraint(SpringLayout.NORTH, labelHeight, this.getType() == DEVICE_TYPE.UNIVERSAL ? 8 : 0, SpringLayout.NORTH, settings);

		int gapw = -8;
		int gaph = 8;
		int hgap = 8;
		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.UNIVERSAL) {
			this.setBackground(Color.WHITE);
			settings.setBackground(Color.WHITE);
			northPane.setBackground(Color.WHITE);
			this.generateTarget.setBackground(Color.WHITE);
			this.generateRetina4.setBackground(Color.WHITE);
			if (this.getType() == DEVICE_TYPE.UNIVERSAL) {
				this.setBorder(new EmptyBorder(hgap * 2, 8, 0, 8));
			}
		} else {
			hgap = isMacLAF ? 2 : 4;
			this.add(northPane, BorderLayout.NORTH);
			this.setBorder(new EmptyBorder(0, 0, 0, 0));
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			layout.putConstraint(SpringLayout.NORTH, widthAny, hgap, SpringLayout.SOUTH, labelWidth);
			layout.putConstraint(SpringLayout.NORTH, heightAny, hgap, SpringLayout.SOUTH, labelHeight);
		} else {
			JSeparator separator = (new JSeparator(JSeparator.HORIZONTAL));
			//separator.setPreferredSize(new Dimension(4, 4));
			settings.add(separator);

			layout.putConstraint(SpringLayout.NORTH, separator, isMacLAF ? 0 : 2, SpringLayout.SOUTH, labelWidth);
			layout.putConstraint(SpringLayout.WEST, separator, -32, SpringLayout.WEST, widthAny);
			layout.putConstraint(SpringLayout.EAST, separator, 32, SpringLayout.EAST, heightAny);
			layout.putConstraint(SpringLayout.NORTH, widthAny, isMacLAF ? 2 : 10, SpringLayout.SOUTH, separator);
			layout.putConstraint(SpringLayout.NORTH, heightAny, isMacLAF ? 2 : 10, SpringLayout.SOUTH, separator);
		}

		layout.putConstraint(SpringLayout.EAST, widthAny, gapw, SpringLayout.HORIZONTAL_CENTER, settings);
		layout.putConstraint(SpringLayout.WEST, heightAny, gaph, SpringLayout.HORIZONTAL_CENTER, settings);

		if (this.getType() == DEVICE_TYPE.DEFAULT) {
			JLabel labelExtra1 = new JLabel(owner.getResource("splitter.label.extra1", "Syntax: e.g. \"44 px\" or \"100 %\""));
			JLabel labelExtra2 = new JLabel(owner.getResource("splitter.label.extra2", "Pixel(px) = size at @1x"));
			JLabel labelExtra3 = new JLabel(owner.getResource("splitter.label.extra3", "% of original size = size at @3x"));
			labelExtra1.setFont(labelSized.getFont());
			labelExtra1.setForeground(labelSized.getForeground());
			labelExtra2.setFont(labelSized.getFont());
			labelExtra2.setForeground(labelSized.getForeground());
			labelExtra3.setFont(labelSized.getFont());
			labelExtra3.setForeground(labelSized.getForeground());
			labelSized.setText(owner.getResource("splitter.label.extra4", "Also accept empty value either."));
			settings.add(labelExtra1);
			settings.add(labelExtra2);
			settings.add(labelExtra3);

			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelExtra1, 0, SpringLayout.HORIZONTAL_CENTER, settings);
			layout.putConstraint(SpringLayout.NORTH, labelExtra1, hgap * 2, SpringLayout.SOUTH, heightAny);
			layout.putConstraint(SpringLayout.EAST, labelExtra2, 0, SpringLayout.EAST, labelExtra3);
			layout.putConstraint(SpringLayout.NORTH, labelExtra2, hgap, SpringLayout.SOUTH, labelExtra1);
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelExtra3, 0, SpringLayout.HORIZONTAL_CENTER, settings);
			layout.putConstraint(SpringLayout.NORTH, labelExtra3, 2, SpringLayout.SOUTH, labelExtra2);
			layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSized, 0, SpringLayout.HORIZONTAL_CENTER, settings);
			layout.putConstraint(SpringLayout.NORTH, labelSized, hgap * 15 / 10, SpringLayout.SOUTH, labelExtra3);
		} else {
			layout.putConstraint(SpringLayout.EAST, labelAny, -8, SpringLayout.WEST, widthAny);
			layout.putConstraint(SpringLayout.VERTICAL_CENTER, labelAny, 0, SpringLayout.VERTICAL_CENTER, widthAny);

			if (this.getType() == DEVICE_TYPE.MAC) {
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSized, 0, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, labelSized, 16, SpringLayout.SOUTH, widthAny);
			}
			if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
				layout.putConstraint(SpringLayout.EAST, width38mm, gapw, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, width38mm, hgap, SpringLayout.SOUTH, widthAny);
				layout.putConstraint(SpringLayout.WEST, height38mm, gaph, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, height38mm, hgap, SpringLayout.SOUTH, heightAny);

				layout.putConstraint(SpringLayout.EAST, label38mm, -8, SpringLayout.WEST, width38mm);
				layout.putConstraint(SpringLayout.VERTICAL_CENTER, label38mm, 0, SpringLayout.VERTICAL_CENTER, width38mm);

				layout.putConstraint(SpringLayout.EAST, width42mm, gapw, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, width42mm, hgap, SpringLayout.SOUTH, width38mm);
				layout.putConstraint(SpringLayout.WEST, height42mm, gaph, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, height42mm, hgap, SpringLayout.SOUTH, height38mm);

				layout.putConstraint(SpringLayout.EAST, label42mm, -8, SpringLayout.WEST, width42mm);
				layout.putConstraint(SpringLayout.VERTICAL_CENTER, label42mm, 0, SpringLayout.VERTICAL_CENTER, width42mm);

				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSized, 0, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, labelSized, 6, SpringLayout.SOUTH, width42mm);
			}
			if (this.isSupprtSizeClasses()) {
				for (int i = 0; i < widthTraits.length; i++) {
					layout.putConstraint(SpringLayout.EAST, widthTraits[i], gapw, SpringLayout.HORIZONTAL_CENTER, settings);
					layout.putConstraint(SpringLayout.NORTH, widthTraits[i], hgap, SpringLayout.SOUTH, widthAny);
				}
				for (int i = 0; i < heightTraits.length; i++) {
					layout.putConstraint(SpringLayout.WEST, heightTraits[i], gaph, SpringLayout.HORIZONTAL_CENTER, settings);
					layout.putConstraint(SpringLayout.NORTH, heightTraits[i], hgap, SpringLayout.SOUTH, heightAny);
				}
				layout.putConstraint(SpringLayout.EAST, traits, isMacLAF ? 0 : -8, SpringLayout.WEST, widthTraits[0]);
				layout.putConstraint(SpringLayout.VERTICAL_CENTER, traits, 0, SpringLayout.VERTICAL_CENTER, widthTraits[0]);
				layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSized, 0, SpringLayout.HORIZONTAL_CENTER, settings);
				layout.putConstraint(SpringLayout.NORTH, labelSized, 16, SpringLayout.SOUTH, widthTraits[0]);
			}
		}
		settings.setLayout(layout);

		int preferredWidth = layout.getConstraints(labelSized).getX().getPreferredValue() + layout.getConstraints(labelSized).getWidth().getPreferredValue() + 32;
		if (this.isSupprtSizeClasses()) {
			traits.addItem("HeightClass - Compact");
			preferredWidth = layout.getConstraints(heightAny).getX().getPreferredValue() + layout.getConstraints(heightAny).getWidth().getPreferredValue() + 32;
			if (preferredWidth < layout.getConstraints(labelSized).getWidth().getPreferredValue() + 32) {
				preferredWidth = layout.getConstraints(labelSized).getWidth().getPreferredValue() + 32;
			}
			if (generateRetina4 != null) {
				preferredWidth = layout.getConstraints(traits).getWidth().getPreferredValue()
						+ layout.getConstraints(widthAny).getWidth().getPreferredValue()
						+ layout.getConstraints(heightAny).getWidth().getPreferredValue()
						+ layout.getConstraints(generateRetina4).getWidth().getPreferredValue()
						+ 48;
			}
		}
		int preferredHeight = layout.getConstraints(labelSized).getY().getPreferredValue() + layout.getConstraints(labelSized).getHeight().getPreferredValue() + 8;
		settings.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		this.add(settings, BorderLayout.CENTER);

		refreshTraits();
		applyComponentStatus();
	}

	/**
	 * Initialize text field to size box.
	 *
	 * @param tooltipText tooltip text
	 * @param focusListener
	 * @param documentListener
	 * @return
	 */
	private JTextField createSizeText(String tooltipText, FocusListener focusListener, DocumentListener documentListener) {
		JTextField textField = new JTextField("", 5);
		Insets insets = new Insets(2, 2, 2, 4);
		textField.setToolTipText(tooltipText);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setMargin(insets);
		textField.addFocusListener(focusListener);
		textField.getDocument().addDocumentListener(documentListener);
		return textField;
	}

	/**
	 * This device type supports size classes?
	 *
	 * @return true / false
	 */
	private boolean isSupprtSizeClasses() {
		return (	this.getType() == DEVICE_TYPE.UNIVERSAL
				||	this.getType() == DEVICE_TYPE.IPHONE
				||	this.getType() == DEVICE_TYPE.IPAD
				);
	}

	/**
	 * Set size as text to text field.
	 *
	 * @param text
	 * @param value
	 */
	private void setSizeText(JTextField text, String value) {
		try {
			setImageSize(text, value);
		} catch (Throwable t) {
			t.printStackTrace();
			text.requestFocusInWindow();
		}
	}

	/**
	 * Get size text.
	 *
	 * @param text null returns empty string.
	 * @return
	 */
	private String getSizeText(JTextField text) {
		if (text == null) {
			return "";
		}
		return text.getText().trim();
	}

	// Options.
	public void setWidthAny(String s) { this.setSizeText(this.widthAny, s); }
	public String getWidthAny() { return this.getSizeText(this.widthAny); }
	public void setHeightAny(String s) { this.setSizeText(this.heightAny, s); }
	public String getHeightAny() { return  this.getSizeText(this.heightAny); }

	public void setWidthTraits(int index, String s) { this.setSizeText(this.widthTraits[index], s); }
	public String getWidthTraits(int index) { return this.getSizeText(this.widthTraits[index]); }
	public void setHeightTraits(int index, String s) { this.setSizeText(this.heightTraits[index], s); }
	public String getHeightTraits(int index) { return this.getSizeText(this.heightTraits[index]); }

	public void setWidth38mm(String s) { this.setSizeText(this.width38mm, s); }
	public String getWidth38mm() { return this.getSizeText(this.width38mm); }
	public void setHeight38mm(String s) { this.setSizeText(this.height38mm, s); }
	public String getHeight38mm() { return this.getSizeText(this.height38mm); }

	public void setWidth42mm(String s) { this.setSizeText(this.width42mm, s); }
	public String getWidth42mm() { return this.getSizeText(this.width42mm); }
	public void setHeight42mm(String s) { this.setSizeText(this.height42mm, s); }
	public String getHeight42mm() { return this.getSizeText(this.height42mm); }

	public void setGenerateTarget(boolean b) {
		generateTarget.setSelected(b);
		applyComponentStatus();
	}
	public boolean isGenerateTarget() { return this.generateTarget.isSelected(); }
	public void setGenerateRetina4(boolean b) { generateRetina4.setSelected(b); }
	public boolean isGenerateRetina4() { return this.generateRetina4.isSelected(); }

	public void setWidthType(int type) {
		widthType = type;
		refreshTraits();
		applyComponentStatus();
	}
	public int getWidthType() { return widthType; }
	public void setHeightType(int type) {
		heightType = type;
		refreshTraits();
		applyComponentStatus();
	}
	public int getHeightType() { return heightType; }

	/**
	 * Refresh traits combobox text.
	 */
	private void refreshTraits() {
		if (!this.isSupprtSizeClasses()) {
			return;
		}

		int selectedIndex = this.traits.getSelectedIndex();
		final String[] sizes = { "?", "Compact", "Regular" };
		this.traits.removeAllItems();
		this.traits.addItem(String.format("WidthClass - %s", sizes[this.getWidthType()]));
		this.traits.addItem(String.format("HeightClass - %s", sizes[this.getHeightType()]));
		this.traits.addItem(String.format("%s & %s", sizes[this.getWidthType()], sizes[this.getHeightType()]));
		this.traits.setSelectedIndex(selectedIndex);
	}

	/**
	 * Apply components status by item selection.
	 */
	protected void applyComponentStatus() {
		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.UNIVERSAL) {
			this.generateRetina4.setEnabled(false);
			// do something some time in the future.
		} else {
			this.generateRetina4.setEnabled(this.generateTarget.isSelected());
			SplitterFrame.setContainerEnabled(settings, this.generateTarget.isSelected());
			if (tabs != null) {
				tabs.setIconAt(tabs.indexOfComponent(SplitterSizePanel.this), generateTarget.isSelected() ? iconChecked : iconUnchecked);
			}
		}
		this.generateRetina4.setVisible(this.getType() == DEVICE_TYPE.IPHONE);

		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.MAC) {
			return;
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			width38mm.setEditable(this.generateTarget.isSelected());
			height38mm.setEditable(this.generateTarget.isSelected());
			width42mm.setEditable(this.generateTarget.isSelected());
			height42mm.setEditable(this.generateTarget.isSelected());
			return;
		}

		widthTraits[0].setEditable(this.getWidthType() > 0);
		heightTraits[0].setEditable(this.getWidthType() > 0);
		widthTraits[1].setEditable(this.getHeightType() > 0);
		heightTraits[1].setEditable(this.getHeightType() > 0);
		widthTraits[2].setEditable(this.getWidthType() > 0 && this.getHeightType() > 0);
		heightTraits[2].setEditable(this.getWidthType() > 0 && this.getHeightType() > 0);

		widthTraits[0].setVisible(this.traits.getSelectedIndex() <= 0);
		heightTraits[0].setVisible(this.traits.getSelectedIndex() <= 0);
		widthTraits[1].setVisible(this.traits.getSelectedIndex() == 1);
		heightTraits[1].setVisible(this.traits.getSelectedIndex() == 1);
		widthTraits[2].setVisible(this.traits.getSelectedIndex() == 2);
		heightTraits[2].setVisible(this.traits.getSelectedIndex() == 2);
	}

	/**
	 * Get device type.
	 *
	 * @return device type
	 * @see #DEVICE_TYPE
	 */
	public DEVICE_TYPE getType() {
		return type;
	}

	/**
	 * Apply properties.
	 *
	 * @param props properties
	 * @return same as props
	 */
	protected Properties applyProperties(MainFrame frame, Properties props) {
		Properties def = this.getDefaultProperties(new Properties());
		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.UNIVERSAL) {
			// do something some time in the future.
		} else {
			this.setGenerateTarget(frame.getBoolProperty(props, "splitter.generate.target.selected." + this.getType().toString(), def));
			if (this.getType() == DEVICE_TYPE.IPHONE) {
				this.setGenerateRetina4(frame.getBoolProperty(props, "splitter.generate.retina4.selected." + this.getType().toString(), def));
			}
		}

		this.setWidthAny(frame.getStringProperty(props, this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.width1x" : "splitter.width.any." + this.getType().toString(), def));
		this.setHeightAny(frame.getStringProperty(props, this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.height1x" : "splitter.height.any." + this.getType().toString(), def));

		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.MAC) {
			return props;
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			this.setWidth38mm(frame.getStringProperty(props, "splitter.width.38mm." + this.getType().toString(), def));
			this.setHeight38mm(frame.getStringProperty(props, "splitter.height.38mm." + this.getType().toString(), def));
			this.setWidth42mm(frame.getStringProperty(props, "splitter.width.42mm." + this.getType().toString(), def));
			this.setHeight42mm(frame.getStringProperty(props, "splitter.height.42mm." + this.getType().toString(), def));
			return props;
		}

		for (int i = 0; i < widthTraits.length; i++) {
			this.setWidthTraits(i, frame.getStringProperty(props, String.format("splitter.width.traits%d.%s", i, this.getType().toString()), def));
		}
		for (int i = 0; i < heightTraits.length; i++) {
			this.setHeightTraits(i, frame.getStringProperty(props, String.format("splitter.height.traits%d.%s", i, this.getType().toString()), def));
		}
		return props;
	}

	/**
	 * Store properties.
	 *
	 * @param props properties
	 * @return same as props
	 */
	protected Properties storeProperties(Properties props) {
		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.UNIVERSAL) {
			// do something some time in the future.
		} else {
			props.put("splitter.generate.target.selected." + this.getType().toString(), Boolean.toString(this.isGenerateTarget()));
			if (this.getType() == DEVICE_TYPE.IPHONE) {
				props.put("splitter.generate.retina4.selected." + this.getType().toString(), Boolean.toString(this.isGenerateRetina4()));
			}
		}

		props.put(this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.width1x" : "splitter.width.any." + this.getType().toString(), this.getWidthAny());
		props.put(this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.height1x" : "splitter.height.any." + this.getType().toString(), this.getHeightAny());

		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.MAC) {
			return props;
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			props.put("splitter.width.38mm." + this.getType().toString(), this.getWidth38mm());
			props.put("splitter.height.38mm." + this.getType().toString(), this.getHeight38mm());
			props.put("splitter.width.42mm." + this.getType().toString(), this.getWidth42mm());
			props.put("splitter.height.42mm." + this.getType().toString(), this.getHeight42mm());
			return props;
		}

		for (int i = 0; i < widthTraits.length; i++) {
			props.put(String.format("splitter.width.traits%d.%s", i, this.getType().toString()), this.getWidthTraits(i));
		}
		for (int i = 0; i < heightTraits.length; i++) {
			props.put(String.format("splitter.height.traits%d.%s", i, this.getType().toString()), this.getHeightTraits(i));
		}
		return props;
	}

	/**
	 * Get default properties.
	 *
	 * @param props
	 * @return
	 */
	protected Properties getDefaultProperties(Properties props) {
		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.UNIVERSAL) {
			// do something some time in the future.
		} else {
			props.put("splitter.generate.target.selected." + this.getType().toString(), Boolean.toString(this.getType() == DEVICE_TYPE.IPHONE || this.getType() == DEVICE_TYPE.IPAD));
			if (this.getType() == DEVICE_TYPE.IPHONE) {
				props.put("splitter.generate.retina4.selected." + this.getType().toString(), Boolean.toString(false));
			}
		}

		String sizeAny = "44 px";
		if (this.getType() == DEVICE_TYPE.IPAD) {
			sizeAny = "66 px";
		} else if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			sizeAny = "40 px";
		} else if (this.getType() == DEVICE_TYPE.MAC) {
			sizeAny = "64 px";
		}
		props.put(this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.width1x" : "splitter.width.any." + this.getType().toString(), sizeAny);
		props.put(this.getType() == DEVICE_TYPE.DEFAULT ? "splitter.height1x" : "splitter.height.any." + this.getType().toString(), sizeAny);

		if (this.getType() == DEVICE_TYPE.DEFAULT || this.getType() == DEVICE_TYPE.MAC) {
			return props;
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			props.put("splitter.width.38mm." + this.getType().toString(), "35 px");
			props.put("splitter.height.38mm." + this.getType().toString(), "35 px");
			props.put("splitter.width.42mm." + this.getType().toString(), "40 px");
			props.put("splitter.height.42mm." + this.getType().toString(), "40 px");
			return props;
		}

		for (int i = 0; i < widthTraits.length; i++) {
			props.put(String.format("splitter.width.traits%d.%s", i, this.getType().toString()), "");
		}
		for (int i = 0; i < heightTraits.length; i++) {
			props.put(String.format("splitter.height.traits%d.%s", i, this.getType().toString()), "");
		}
		return props;
	}

	/**
	 * Set image size text.
	 * Syntax: e.g. "44 px" or "100 %"
	 *
	 * @param text
	 * @param size
	 */
	private static void setImageSize(JTextField text, String size) {
		if (IOSImageUtil.isNullOrWhiteSpace(size)) {
			text.setText("");
			return;
		}
		size = size.toLowerCase().trim();
		if (size.equals("px") || size.equals("%")) {
			text.setText("");
			return;
		}
		if (size.endsWith("px")) { size = size.replaceAll("px", "").trim(); }
		boolean per = size.endsWith("%");
		if (per) { size = size.replaceAll("%", "").trim(); }
		String newText = new Integer(Math.abs(Integer.parseInt(size))).toString().concat(" ").concat(per ? "%" : "px");
		if (!newText.equals(text.getText())) {
			text.setText(newText);
		}
	}

	/**
	 * Get image size text.
	 *
	 * @param size
	 * @param imageSize
	 * @return
	 */
	protected static double getImageSize(final String inputSize, final double imageSize) {
		if (IOSImageUtil.isNullOrWhiteSpace(inputSize)) {
			return 0;
		}
		String size = inputSize.toLowerCase().trim();
		if (size.equals("px") || size.equals("%")) {
			return 0;
		}

		if (size.endsWith("px")) { size = size.replaceAll("px", "").trim(); }
		boolean per = size.endsWith("%");
		if (per) { size = size.replaceAll("%", "").trim(); }
		return per ? (imageSize * (Double.parseDouble(size) / 100.0) / 3.0) : Double.parseDouble(size);
	}

	/**
	 * Add image queue.
	 *
	 * @param owner
	 * @param queue
	 * @param srcFile original image file path
	 * @param outputDir output directory path
	 * @return
	 * @throws Exception
	 */
	protected LinkedList<IOSImageSet> addImageQueue(final MainFrame owner, LinkedList<IOSImageSet> queue, final File srcFile, final File outputDir) throws Exception {
		if (this.getWidthAny().isEmpty() && this.getHeightAny().isEmpty()) {
			throw new Exception(owner.getResource("splitter.error.empty.both", "Both width and height are empty. Required at least either."));
		}

		if (this.getType() == DEVICE_TYPE.DEFAULT) {
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x1, null, null, null, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x2, null, null, null, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x3, null, null, null, null));
			return queue;
		}

		if (this.getType() != DEVICE_TYPE.UNIVERSAL && !this.isGenerateTarget()) {
			// Not output target.
			return queue;
		}

		if (this.getType() == DEVICE_TYPE.APPLE_WATCH) {
			IOSImageSet.IDIOM idiom = IOSImageSet.IDIOM.APPLEWATCH;
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x2, idiom, null, null, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidth38mm(), this.getWidthAny(), this.getHeight38mm(), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, null, null, IOSImageSet.SUBTYPE.MM38).setSubtype(null).setOption(IOSImageSet.JSON_KEY.SCREEN_WIDTH, IOSImageSet.JSON_VALUE.SCREEN_WIDTH_38MM));
			queue.add(createImageSet(outputDir, srcFile, this.getWidth42mm(), this.getWidthAny(), this.getHeight42mm(), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, null, null, IOSImageSet.SUBTYPE.MM42).setSubtype(null).setOption(IOSImageSet.JSON_KEY.SCREEN_WIDTH, IOSImageSet.JSON_VALUE.SCREEN_WIDTH_42MM));
			return queue;
		}

		if (this.getType() == DEVICE_TYPE.MAC) {
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x1, IOSAssetCatalogs.IDIOM.MAC, null, null, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x2, IOSAssetCatalogs.IDIOM.MAC, null, null, null));
			return queue;
		}

		IOSImageSet.IDIOM idiom = IOSImageSet.IDIOM.UNIVERSAL;
		if (this.getType() == DEVICE_TYPE.IPHONE) {
			idiom = IOSAssetCatalogs.IDIOM.IPHONE;
		} else if (this.getType() == DEVICE_TYPE.IPAD) {
			idiom = IOSAssetCatalogs.IDIOM.IPAD;
		}

		queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x1, idiom, null, null, null));
		queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x2, idiom, null, null, null));
		if (this.getType() == DEVICE_TYPE.IPHONE && this.isGenerateRetina4())
			{ queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x2, idiom, null, null, IOSImageSet.SUBTYPE.RETINA4)); }
		if (this.getType() == DEVICE_TYPE.UNIVERSAL || this.getType() == DEVICE_TYPE.IPHONE)
			{ queue.add(createImageSet(outputDir, srcFile, this.getWidthAny(), null, this.getHeightAny(), null, IOSImageSet.SCALE.x3, idiom, null, null, null)); }

		final JSON_VALUE[] sizeClasses = { IOSImageSet.JSON_VALUE.SIZE_CLASS_ANY, IOSImageSet.JSON_VALUE.SIZE_CLASS_COMPACT, IOSImageSet.JSON_VALUE.SIZE_CLASS_REGULAR };
		JSON_VALUE widthClass = sizeClasses[this.getWidthType()];
		JSON_VALUE heightClass = sizeClasses[this.getHeightType()];

		if (widthClass != IOSImageSet.JSON_VALUE.SIZE_CLASS_ANY) {
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(0), this.getWidthAny(), this.getHeightTraits(0), this.getHeightAny(), IOSImageSet.SCALE.x1, idiom, widthClass, null, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(0), this.getWidthAny(), this.getHeightTraits(0), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, widthClass, null, null));
			if (this.getType() == DEVICE_TYPE.IPHONE && this.isGenerateRetina4())
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(0), this.getWidthAny(), this.getHeightTraits(0), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, widthClass, null, IOSImageSet.SUBTYPE.RETINA4)); }
			if (this.getType() == DEVICE_TYPE.UNIVERSAL || this.getType() == DEVICE_TYPE.IPHONE)
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(0), this.getWidthAny(), this.getHeightTraits(0), this.getHeightAny(), IOSImageSet.SCALE.x3, idiom, widthClass, null, null)); }
		}

		if (heightClass != IOSImageSet.JSON_VALUE.SIZE_CLASS_ANY) {
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(1), this.getWidthAny(), this.getHeightTraits(1), this.getHeightAny(), IOSImageSet.SCALE.x1, idiom, null, heightClass, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(1), this.getWidthAny(), this.getHeightTraits(1), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, null, heightClass, null));
			if (this.getType() == DEVICE_TYPE.IPHONE && this.isGenerateRetina4())
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(1), this.getWidthAny(), this.getHeightTraits(1), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, null, heightClass, IOSImageSet.SUBTYPE.RETINA4)); }
			if (this.getType() == DEVICE_TYPE.UNIVERSAL || this.getType() == DEVICE_TYPE.IPHONE)
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(1), this.getWidthAny(), this.getHeightTraits(1), this.getHeightAny(), IOSImageSet.SCALE.x3, idiom, null, heightClass, null)); }
		}

		if (widthClass != IOSImageSet.JSON_VALUE.SIZE_CLASS_ANY && heightClass != IOSImageSet.JSON_VALUE.SIZE_CLASS_ANY) {
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(2), this.getWidthAny(), this.getHeightTraits(2), this.getHeightAny(), IOSImageSet.SCALE.x1, idiom, widthClass, heightClass, null));
			queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(2), this.getWidthAny(), this.getHeightTraits(2), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, widthClass, heightClass, null));
			if (this.getType() == DEVICE_TYPE.IPHONE && this.isGenerateRetina4())
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(2), this.getWidthAny(), this.getHeightTraits(2), this.getHeightAny(), IOSImageSet.SCALE.x2, idiom, widthClass, heightClass, IOSImageSet.SUBTYPE.RETINA4)); }
			if (this.getType() == DEVICE_TYPE.UNIVERSAL || this.getType() == DEVICE_TYPE.IPHONE)
				{ queue.add(createImageSet(outputDir, srcFile, this.getWidthTraits(2), this.getWidthAny(), this.getHeightTraits(2), this.getHeightAny(), IOSImageSet.SCALE.x3, idiom, widthClass, heightClass, null)); }
		}
		return queue;
	}


	/**
	 * Create IOImageSet.
	 *
	 * @param outputDir	output directory path
	 * @param srcFile	original file path
	 * @param width		output image width
	 * @param height	output image height
	 * @param scale		output image scale
	 * @param idiom		output image idiom
	 * @param widthClass	output image width class
	 * @param heightClass	output image height class
	 * @param subtype	output image subtype
	 * @return
	 */
	private IOSImageSet createImageSet(final File outputDir, final File srcFile, final String width, final String defaultWidth, final String height, final String defaultHeight, final IOSImageSet.SCALE scale, final IOSImageSet.IDIOM idiom, final IOSImageSet.JSON_VALUE widthClass, final IOSImageSet.JSON_VALUE heightClass, final IOSImageSet.SUBTYPE subtype) {
		String imageName = srcFile.getName();
		int idx = imageName.lastIndexOf(".");
		if (idx >= 0) {
			imageName = imageName.substring(0, idx);
		}
		File dstDir = outputDir;
		if (this.getType() != DEVICE_TYPE.DEFAULT) {
			dstDir = new File(outputDir, String.format("%s.imageset", imageName));
		}
		String imageFilename = imageName;
		imageFilename = imageFilename.concat(idiom == null ? "" : "-" + idiom);
		imageFilename = imageFilename.concat(widthClass == null ? "" : "-" + widthClass + "w");
		imageFilename = imageFilename.concat(heightClass == null ? "" : "-" + heightClass + "h");
		imageFilename = imageFilename.concat(subtype == null ? "" : "-" + subtype);
		File dstFile = new File(dstDir, String.format("%s@%s%s", imageFilename, scale, ".png"));

		String outputWidth;
		String outputHeight;
		if (IOSImageUtil.isNullOrWhiteSpace(width) && IOSImageUtil.isNullOrWhiteSpace(height)) {
			// Apply 'Any' value when both empty.
			outputWidth = defaultWidth == null ? "" : defaultWidth;
			outputHeight = defaultHeight == null ? "" : defaultHeight;
		} else {
			outputWidth = width;
			outputHeight = height;
		}

		return (new IOSImageSet()).setWidthAsString(outputWidth)
									.setHeightAsString(outputHeight)
									.setScale(scale)
									.setIdiom(idiom)
									.setOriginalFile(srcFile)
									.setFile(dstFile)
									.setImageName(imageName)
									.setSubtype(subtype)
									.setOption(IOSImageSet.JSON_KEY.WIDTH_CLASS, widthClass)
									.setOption(IOSImageSet.JSON_KEY.HEIGHT_CLASS, heightClass)
									;
	}

	/**
	 * Create SplitterSizePanel as new tab.
	 *
	 * @param type	device type
	 * @param tabs	tabbed pane
	 * @param owner
	 * @return
	 */
	protected static SplitterSizePanel newAsTab(final SplitterSizePanel.DEVICE_TYPE type, final JTabbedPane tabs, final MainFrame owner) {
		SplitterSizePanel ssp = new SplitterSizePanel(type, owner);
		tabs.addTab(ssp.getType().toString(), ssp.iconUnchecked, ssp);
		ssp.tabs = tabs;
		ssp.setBackground(tabs.getBackground());
		tabs.setBackgroundAt(tabs.getTabCount() - 1, new Color(0xEEEEEE));
		return ssp;
	}

	/**
	 * Create size changed item listner.
	 *
	 * @param owner
	 * @param widthType
	 * @param heightType
	 * @return
	 */
	protected ItemListener createSizeChangedItemListener(final MainFrame owner, final JComboBox widthType, final JComboBox heightType) {
		return (new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				owner.setStorePropertiesRequested(true);
				setWidthType(widthType.getSelectedIndex());
				setHeightType(heightType.getSelectedIndex());
				refreshTraits();
				applyComponentStatus();
			}
		});
	}
}

