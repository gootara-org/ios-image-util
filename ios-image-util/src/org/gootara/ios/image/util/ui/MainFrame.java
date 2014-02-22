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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.gootara.ios.image.util.IOS6IconInfo;
import org.gootara.ios.image.util.IOS6SplashInfo;
import org.gootara.ios.image.util.IOS7IconInfo;
import org.gootara.ios.image.util.IOS7SplashInfo;
import org.gootara.ios.image.util.IOSAssetCatalogs;
import org.gootara.ios.image.util.IOSIconAssetCatalogs;
import org.gootara.ios.image.util.IOSImageInfo;
import org.gootara.ios.image.util.IOSSplashAssetCatalogs;

/**
 * @author gootara.org
 *
 */
public class MainFrame extends JFrame {
	private ResourceBundle resource;
	private JTextField icon6Path, icon7Path, splashPath, outputPath;
	private JComboBox<ComboBoxItem> scaleAlgorithm;
	private JComboBox<String> splashScaling;
	private ImagePanel icon6Image, icon7Image, splashImage;
	private JProgressBar progress;
	private JCheckBox generateOldSplashImages, generateAsAssetCatalogs;
	private JRadioButton iPhoneOnly, iPadOnly, iBoth;

	public MainFrame() {
		resource = ResourceBundle.getBundle("application");

		this.setTitle(getResource("window.title", "iOS Image Util"));

		// iOS6 Icon Path.
		icon6Path = new JTextField();
		JButton refIcon6Path = new JButton("...");
		JPanel icon6PathPanel = new JPanel();
		JLabel icon6Label = new JLabel(getResource("label.icon6.path", " iOS6 Icon PNG (1024x1024):"));
		icon6PathPanel.setLayout(new BorderLayout(2, 2));
		icon6PathPanel.add(icon6Label, BorderLayout.WEST);
		icon6PathPanel.add(icon6Path, BorderLayout.CENTER);
		icon6PathPanel.add(refIcon6Path, BorderLayout.EAST);
		refIcon6Path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				if (icon6Path.getText().trim().length() > 0) {
					File f = new File(icon6Path.getText());
					if (f.exists()) {
						if (f.getParentFile() != null) {
							chooser.setCurrentDirectory(f.getParentFile());
						}
						chooser.setSelectedFile(f);
					}
				}
				chooser.setApproveButtonText(getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(icon6Path, chooser.getSelectedFile(), icon6Image);
			    }
			}
		});

		// iOS7 Icon Path.
		icon7Path = new JTextField();
		JButton refIcon7Path = new JButton("...");
		JPanel icon7PathPanel = new JPanel();
		JLabel icon7Label = new JLabel(getResource("label.icon7.path", " iOS7 Icon PNG (1024x1024):"));
		icon7PathPanel.setLayout(new BorderLayout(2, 2));
		icon7PathPanel.add(icon7Label, BorderLayout.WEST);
		icon7PathPanel.add(icon7Path, BorderLayout.CENTER);
		icon7PathPanel.add(refIcon7Path, BorderLayout.EAST);
		refIcon7Path.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				if (icon7Path.getText().trim().length() > 0) {
					File f = new File(icon7Path.getText());
					if (f.exists()) {
						if (f.getParentFile() != null) {
							chooser.setCurrentDirectory(f.getParentFile());
						}
						chooser.setSelectedFile(f);
					}
				}
				chooser.setApproveButtonText(getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(icon7Path, chooser.getSelectedFile(), icon7Image);
			    }
			}
		});

		// Splash Image Path.
		splashPath = new JTextField();
		JButton refSplashPath = new JButton("...");
		JPanel splashPathPanel = new JPanel();
		JLabel splashLabel = new JLabel(getResource("label.splash.path", "      Splash PNG (2048x2048):"));
		splashPathPanel.setLayout(new BorderLayout(2, 2));
		splashPathPanel.add(splashLabel, BorderLayout.WEST);
		splashPathPanel.add(splashPath, BorderLayout.CENTER);
		splashPathPanel.add(refSplashPath, BorderLayout.EAST);
		refSplashPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				if (splashPath.getText().trim().length() > 0) {
					File f = new File(splashPath.getText());
					if (f.exists()) {
						if (f.getParentFile() != null) {
							chooser.setCurrentDirectory(f.getParentFile());
						}
						chooser.setSelectedFile(f);
					}
				}
				chooser.setApproveButtonText(getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(splashPath, chooser.getSelectedFile(), splashImage);
			    }
			}
		});

		// Scaling Algorithm.
		Vector<ComboBoxItem> items = new Vector<ComboBoxItem>();
		items.add(new ComboBoxItem(Image.SCALE_AREA_AVERAGING, "SCALE_AREA_AVERAGING"));
		items.add(new ComboBoxItem(Image.SCALE_DEFAULT, "SCALE_DEFAULT"));
		items.add(new ComboBoxItem(Image.SCALE_FAST, "SCALE_FAST"));
		items.add(new ComboBoxItem(Image.SCALE_REPLICATE, "SCALE_REPLICATE"));
		items.add(new ComboBoxItem(Image.SCALE_SMOOTH, "SCALE_SMOOTH"));
		scaleAlgorithm = new JComboBox<ComboBoxItem>(items);
		scaleAlgorithm.setFont(scaleAlgorithm.getFont().deriveFont(Font.PLAIN, 11.0f));
		scaleAlgorithm.setSelectedIndex(4);
		JLabel scaleLabel = new JLabel(getResource("label.scaling.algorithm", "  Scaling Algorithm:"));
		JPanel scalePanel = new JPanel();
//		scalePanel.setLayout(new BorderLayout(2, 2));
//		scalePanel.add(scaleLabel, BorderLayout.WEST);
//		scalePanel.add(scaleAlgorithm, BorderLayout.CENTER);
		scalePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		scalePanel.add(scaleLabel);
		scalePanel.add(scaleAlgorithm);

		// Checkboxes and Radio Buttons.
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		radioPanel.add(this.iBoth = new JRadioButton(getResource("label.output.both", "Output Both"), true));
		radioPanel.add(this.iPhoneOnly = new JRadioButton(getResource("label.iphone.only", "iPhone Only")));
		radioPanel.add(this.iPadOnly = new JRadioButton(getResource("label.ipad.only", "iPad Only")));
		ButtonGroup group = new ButtonGroup();
		group.add(iBoth);
		group.add(iPhoneOnly);
		group.add(iPadOnly);

		// Slpash Scaling
		Vector<String> ssItems = new Vector<String>();
		ssItems.add(getResource("item.splash.noresize", "No resizing(iPhone only)"));
		ssItems.add(getResource("item.splash.noresize.both", "No resizing(iPhone & iPad)"));
		ssItems.add(getResource("item.splash.resize", "Fit to the screen height(iPhone only)"));
		ssItems.add(getResource("item.splash.fittoscreen", "Fit to the screen"));
		splashScaling = new JComboBox<String>(ssItems);
		splashScaling.setFont(splashScaling.getFont().deriveFont(Font.PLAIN, 11.0f));
		splashScaling.setSelectedIndex(2);
		splashScaling.setToolTipText(getResource("tooltip.splash.scaling", "The Non-Retina images will be scaled down in the fixed 50%, even if 'No resizing' is selected."));
		JLabel ssLabel = new JLabel(getResource("label.splash.image", "Splash:"));
		JPanel ssPanel = new JPanel();
		ssPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		ssPanel.add(ssLabel);
		ssPanel.add(splashScaling);

		this.generateOldSplashImages = new JCheckBox(getResource("label.generate.old.splash", "Generate Old Splash Images"), false);

		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new GridLayout(2, 2, 2, 0));
		settingPanel.add(scalePanel);
		settingPanel.add(ssPanel);
		settingPanel.add(radioPanel);
		settingPanel.add(this.generateOldSplashImages);

		// Set Components for North Panel.
		JPanel refPanel = new JPanel();
		refPanel.setLayout(new GridLayout(3, 1, 2, 2));
		refPanel.add(icon6PathPanel);
		refPanel.add(icon7PathPanel);
		refPanel.add(splashPathPanel);
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout(0, 2));
		northPanel.add(refPanel, BorderLayout.NORTH);
		northPanel.add(settingPanel, BorderLayout.SOUTH);

		// Image Panels.
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.add(northPanel, BorderLayout.NORTH);


		icon6Image = new ImagePanel(getResource("label.icon6.drop", "Drop iOS6 Icon PNG Here"));
		icon6Image.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		icon6Image.setTransferHandler(new TransferHandler() {
			public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List)list).get(0);
							if (file instanceof File) {
								setFilePath(icon6Path, (File)file, icon6Image);
							}
						}
					}
				} catch (Exception ex) {
					handleException(ex);
				}
				return false;
			}
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
		});
		icon6Path.setTransferHandler(icon6Image.getTransferHandler());

		icon7Image = new ImagePanel(getResource("label.icon7.drop", "Drop iOS7 Icon PNG Here"));
		icon7Image.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		icon7Image.setTransferHandler(new TransferHandler() {
			public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List)list).get(0);
							if (file instanceof File) {
								setFilePath(icon7Path, (File)file, icon7Image);
							}
						}
					}
				} catch (Exception ex) {
					handleException(ex);
				}
				return false;
			}
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
		});
		icon7Path.setTransferHandler(icon7Image.getTransferHandler());

		splashImage = new ImagePanel(getResource("label.splash.drop", "Drop Splash Image PNG Here"));
		splashImage.setHyphenator(getResource("props.hyphenator.begin", "=!),.:;?]})"), getResource("props.hyphenator.end", "([{"));
		splashImage.setTransferHandler(new TransferHandler() {
			public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							Object file = ((List)list).get(0);
							if (file instanceof File) {
								setFilePath(splashPath, (File)file, splashImage);
							}
						}
					}
				} catch (Exception ex) {
					handleException(ex);
				}
				return false;
			}
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
		});
		splashPath.setTransferHandler(splashImage.getTransferHandler());

		JPanel imagesPanel = new JPanel();
		imagesPanel.setLayout(new GridLayout(1, 3, 2, 2));
		imagesPanel.add(icon6Image);
		imagesPanel.add(icon7Image);
		imagesPanel.add(splashImage);
		mainPanel.add(imagesPanel, BorderLayout.CENTER);

		// Output Path.
		outputPath = new JTextField();
		JButton refOutputPath = new JButton("...");
		JPanel outputPathPanel = new JPanel();
		JLabel outputPathLabel = new JLabel(getResource("label.output.path", " Output Dir:"));
		outputPathPanel.setLayout(new BorderLayout(2, 2));
		outputPathPanel.add(outputPathLabel, BorderLayout.WEST);
		outputPathPanel.add(outputPath, BorderLayout.CENTER);
		outputPathPanel.add(refOutputPath, BorderLayout.EAST);
		refOutputPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				if (outputPath.getText().trim().length() > 0) {
					File dir = new File(outputPath.getText());
					if (dir.exists()) {
						if (dir.getParentFile() != null) {
							chooser.setCurrentDirectory(dir.getParentFile());
						}
						chooser.setSelectedFile(dir);
					}
				}
				chooser.setApproveButtonText(getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					setFilePath(outputPath, chooser.getSelectedFile(), null);
			    }
			}
		});

		// Generate Button and Progress Bar.

		this.generateAsAssetCatalogs = new JCheckBox(getResource("label.generate.as.asset.catalogs", "Generate As Asset Catalogs"), false);
		this.generateAsAssetCatalogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

		JButton generateButton = new JButton(getResource("button.generate", "Generate"));
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate();
			}

		});
		progress = new JProgressBar(0, IOS6IconInfo.values().length + IOS7IconInfo.values().length + IOS6SplashInfo.values().length + IOS7SplashInfo.values().length);
		progress.setValue(0);
		progress.setStringPainted(true);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
		buttonPanel.add(this.generateAsAssetCatalogs);
		buttonPanel.add(progress);
		buttonPanel.add(generateButton);

		// Set Components for South Panel.
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(2, 1, 2, 2));
		southPanel.add(outputPathPanel);
		southPanel.add(buttonPanel);

		mainPanel.add(southPanel, BorderLayout.SOUTH);

		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);

		this.addWindowListener(new WindowListener() {
			@Override public void windowOpened(WindowEvent arg0) {}
			@Override public void windowActivated(WindowEvent arg0) {}
			@Override public void windowClosed(WindowEvent arg0) { System.exit(0); }
			@Override public void windowClosing(WindowEvent arg0) { arg0.getWindow().dispose(); }
			@Override public void windowDeactivated(WindowEvent arg0) {}
			@Override public void windowDeiconified(WindowEvent arg0) {}
			@Override public void windowIconified(WindowEvent arg0) {}
		});
	}

	private void setFilePath(JTextField textField, File f, ImagePanel imagePanel) {
		try {
			textField.setText(f.getCanonicalPath());
			if (imagePanel != null) {
				BufferedImage image = ImageIO.read(f);
				if (image == null) {
					JOptionPane.showMessageDialog(this, "[" + f.getCanonicalPath() + "] " + getResource("error.illegal.image", "is illegal image."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
					textField.setText("");
					imagePanel.setImage(null);
					return;
				}
				imagePanel.setImage(image);
				if (outputPath.getText().trim().length() <= 0) {
					File g = new File(f.getParentFile(), this.generateAsAssetCatalogs.isSelected() ? this.getResource("string.dir.assets", "Images.xcassets") : this.getResource("string.dir.generate", "generated"));
					outputPath.setText(g.getCanonicalPath());
				}
			}
		} catch (Exception ex) {
			handleException(ex);
			textField.setText("");
			imagePanel.setImage(null);
		}
	}

	private void handleException(Exception ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(this, ex.getMessage(), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
	}

	private void generate() {
		try {
			if (icon6Path.getText().trim().length() <= 0 && icon7Path.getText().trim().length() <= 0 && splashPath.getText().trim().length() <= 0) {
				JOptionPane.showMessageDialog(this, getResource("error.not.choosen", "Choose at least one Icon or Splash PNG file."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Path Check.
			File icon6File, icon7File, splashFile;
			icon6File = icon7File = splashFile = null;
			if (icon6Path.getText().trim().length() > 0) {
				icon6File = checkFile(icon6Path);
				if (icon6File == null) return;
			}
			if (icon7Path.getText().trim().length() > 0) {
				icon7File = checkFile(icon7Path);
				if (icon7File == null) return;
			}
			if (splashPath.getText().trim().length() > 0) {
				splashFile = checkFile(splashPath);
				if (splashFile == null) return;
			}

			// Error Check.
			File outputDir = null;
			if (outputPath.getText().trim().length() <= 0) {
				outputPath.requestFocusInWindow();
				JOptionPane.showMessageDialog(this, getResource("error.not.choosen.output.path", "Choose output dir."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			outputDir = new File(outputPath.getText());
			if (!outputDir.exists()) {
				if (JOptionPane.showConfirmDialog(this, "[" + outputPath.getText() + "] " + getResource("confirm.output.path.create", "is not exists. Create it?"), getResource("title.confirm", "Confirm"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
				if (!outputDir.mkdirs()) {
					outputPath.requestFocusInWindow();
					outputPath.selectAll();
					JOptionPane.showMessageDialog(this, "[" + outputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (!outputDir.isDirectory()) {
				outputPath.requestFocusInWindow();
				outputPath.selectAll();
				JOptionPane.showMessageDialog(this, "[" + outputDir.getCanonicalPath() + "] " + getResource("error.not.directory", "is not directory. Choose directory."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (icon6File == null && icon7File != null) {
				if (JOptionPane.showConfirmDialog(this, getResource("question.use.icon7.instead", "An iOS6 Icon PNG file is not choosen. Use iOS7 Icon PNG file instead?"), getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					icon6File = icon7File;
				}
			}

			if (icon6File != null && icon7File == null) {
				if (JOptionPane.showConfirmDialog(this, getResource("question.use.icon6.instead", "An iOS7 Icon PNG file is not choosen. Use iOS6 Icon PNG file instead?"),getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					icon7File = icon6File;
				}
			}

			if (splashFile == null) {
				if (JOptionPane.showConfirmDialog(this, getResource("confirm.splash.not.generate", "The Splash image will not be generated."), getResource("title.confirm", "Confirm"), JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}

			File iconOutputDir = outputDir;
			File splashOutputDir = outputDir;
			if (this.generateAsAssetCatalogs.isSelected()) {
				// Asset Catalogs
				iconOutputDir = new File(outputDir, getResource("string.dir.appicon", "AppIcon.appiconset"));
				if (!iconOutputDir.exists() && !iconOutputDir.mkdirs()) {
					JOptionPane.showMessageDialog(this, "[" + iconOutputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				splashOutputDir = new File(outputDir, getResource("string.dir.launchimage", "LaunchImage.launchimage"));
				if (!splashOutputDir.exists() && !splashOutputDir.mkdirs()) {
					JOptionPane.showMessageDialog(this, "[" + splashOutputDir.getCanonicalPath() + "] " + getResource("error.create.dir", "could not create."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			// Write Images.
			progress.setValue(0);
			if (icon6File == null) {
				progress.setValue(progress.getValue() + IOS6IconInfo.values().length);
			} else {
				BufferedImage image = ImageIO.read(icon6File);
				for (IOS6IconInfo info : IOS6IconInfo.values()) {
					writeIconImage(image, info, iconOutputDir);
				}
			}
			if (icon7File == null) {
				progress.setValue(progress.getValue() + IOS7IconInfo.values().length);
			} else {
				BufferedImage image = ImageIO.read(icon7File);
				for (IOS7IconInfo info : IOS7IconInfo.values()) {
					writeIconImage(image, info, iconOutputDir);
				}
			}

			if (splashFile == null) {
				progress.setValue(progress.getValue() + IOS6SplashInfo.values().length + IOS7SplashInfo.values().length);
			} else {
				BufferedImage image = ImageIO.read(splashFile);
				if (this.generateOldSplashImages.isSelected()) {
					// Generate old size of Splash images when checkbox selected.
					for (IOS6SplashInfo info : IOS6SplashInfo.values()) {
						writeSplashImage(image, info, splashOutputDir);
					}
				} else {
					progress.setValue(progress.getValue() + IOS6SplashInfo.values().length);
				}
				for (IOS7SplashInfo info : IOS7SplashInfo.values()) {
					writeSplashImage(image, info, splashOutputDir);
				}
			}

			if (this.generateAsAssetCatalogs.isSelected()) {
				// Contents json
				writeIconJson(iconOutputDir);
				writeSplashJson(splashOutputDir);
			}

			JOptionPane.showMessageDialog(this, getResource("label.finish.generate", "The images are generated."), getResource("title.information", "Information"), JOptionPane.INFORMATION_MESSAGE);
			progress.setValue(0);
		} catch (Exception ex) {
			handleException(ex);
		}
	}

	private void writeIconImage(BufferedImage src, IOSImageInfo info, File outputDir) throws Exception {
		progress.setValue(progress.getValue() + 1);
		if (this.iPhoneOnly.isSelected() && !info.isIphoneImage()) return;
		if (this.iPadOnly.isSelected() && !info.isIpadImage()) return;

		File f = new File(outputDir, info.getFilename());
		int width = (int)info.getSize().getWidth();
		int height = (int)info.getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, src.getColorModel().getPixelSize() > 8 ? src.getType() : BufferedImage.TYPE_INT_ARGB);
		int hints = ((ComboBoxItem)this.scaleAlgorithm.getSelectedItem()).getItemValue();
		buf.getGraphics().drawImage(src.getScaledInstance(width, height, hints), 0, 0, this);

		ImageIO.write(buf, "png", f);
		buf.flush();
		buf = null;
		progress.paint(progress.getGraphics());;
	}

	private void writeSplashImage(BufferedImage src, IOSImageInfo info, File outputDir) throws Exception {
		progress.setValue(progress.getValue() + 1);
		if (this.iPhoneOnly.isSelected() && !info.isIphoneImage()) return;
		if (this.iPadOnly.isSelected() && !info.isIpadImage()) return;

		File f = new File(outputDir, info.getFilename());
		int width = (int)info.getSize().getWidth();
		int height = (int)info.getSize().getHeight();
		BufferedImage buf = new BufferedImage(width, height, src.getColorModel().getPixelSize() > 8 ? src.getType() : BufferedImage.TYPE_INT_ARGB);
		int c = src.getRGB(0, 0);
		Graphics g = buf.getGraphics();
		g.setColor(new Color(ImageUtil.r(c), ImageUtil.g(c), ImageUtil.b(c), ImageUtil.a(c)));
		g.fillRect(0, 0, width, height);

//		double p = (width > height || (!this.resizeSplashImage.isSelected() && info.isIphoneImage())) ? (double)height / (double)src.getHeight() : (double)width / (double)src.getWidth();
		double p = (width > height) ? (double)height / (double)src.getHeight() : (double)width / (double)src.getWidth();
		if (splashScaling.getSelectedIndex() == 0) {
			// No resizing(iPhone only)
			if (info.isIphoneImage()) {
				p = info.isRetina() ? 1.0d : 0.5d;
			}
		} else if (splashScaling.getSelectedIndex() == 1) {
			// No resizing(iPhone & iPad)
			p = info.isRetina() ? 1.0d : 0.5d;
		} else if (splashScaling.getSelectedIndex() == 2) {
			// Fit to the screen height(iPhone only)
			p = (double)height / (double)src.getHeight();
		} // else default
		int w = (int) ((double)src.getWidth() * p);
		int h = (int) ((double)src.getHeight() * p);
   		int x = (int) ((width - w) / 2);
   		int y = (int) ((height - h) / 2);
		int hints = ((ComboBoxItem)this.scaleAlgorithm.getSelectedItem()).getItemValue();
		buf.getGraphics().drawImage(src.getScaledInstance(w, h, hints), x, y, this);

		ImageIO.write(buf, "png", f);
		buf.flush();
		buf = null;
		progress.paint(progress.getGraphics());
	}

	private void writeIconJson(File outputDir) throws IOException {
		BufferedWriter writer = null;
		try {
			File f = new File(outputDir, this.getResource("string.filename.contents.json", "Contents.json"));
			writer = new BufferedWriter(new FileWriter(f));
			writer.write(IOSAssetCatalogs.JSON_HEADER);
			boolean firstItem = true;
			for (IOSIconAssetCatalogs asset : IOSIconAssetCatalogs.values()) {
				if (asset.isIphone() && iPadOnly.isSelected()) continue;
				if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
				if (firstItem) {
					firstItem = false;
				} else {
					writer.write(",\n");
				}
				writer.write(asset.toJson());
			}
			writer.write(IOSAssetCatalogs.JSON_FOOTER);
		} catch (IOException ioex) {
			throw ioex;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private void writeSplashJson(File outputDir) throws IOException {
		BufferedWriter writer = null;
		try {
			File f = new File(outputDir, this.getResource("string.filename.contents.json", "Contents.json"));
			writer = new BufferedWriter(new FileWriter(f));
			writer.write(IOSAssetCatalogs.JSON_HEADER);
			boolean firstItem = true;
			for (IOSSplashAssetCatalogs asset : IOSSplashAssetCatalogs.values()) {
				if (asset.isIphone() && iPadOnly.isSelected()) continue;
				if (asset.isIpad() && iPhoneOnly.isSelected()) continue;
				if (!generateOldSplashImages.isSelected() && asset.getExtent().equals(IOSSplashAssetCatalogs.EXTENT_TO_STATUS_BAR)) continue;
				if (firstItem) {
					firstItem = false;
				} else {
					writer.write(",\n");
				}
				writer.write(asset.toJson());
			}
			writer.write(IOSAssetCatalogs.JSON_FOOTER);
		} catch (IOException ioex) {
			throw ioex;
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	private File checkFile(JTextField path) {
		try {
			File f = new File(path.getText());
			if (!f.exists()) {
				path.requestFocusInWindow();
				path.selectAll();
				JOptionPane.showMessageDialog(this, "[" + f.getCanonicalPath() + "] " + getResource("error.not.exists", "is not exists."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			if (f.isDirectory()) {
				path.requestFocusInWindow();
				path.selectAll();
				JOptionPane.showMessageDialog(this, "[" + f.getCanonicalPath() + "] " + getResource("error.not.file", "is directory. Choose file."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return null;
			}

			BufferedImage image = ImageIO.read(f);
			if (image == null) {
				JOptionPane.showMessageDialog(this, "[" + f.getCanonicalPath() + "] " + getResource("error.illegal.image", "is illegal image."), getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			image.flush();
			image = null;

			return f;
		} catch (Exception ex) {
			handleException(ex);
		}
		return null;
	}

	private String getResource(String key, String def) {
		if (resource.containsKey(key)) {
			return resource.getString(key);
		}
		return def;
	}
}

class ComboBoxItem {
	private int itemValue;
	private String itemName;

	public ComboBoxItem(int value, String name) {
		this.setItemValue(value);
		this.setItemName(name);
	}
	/**
	 * @return itemValue
	 */
	public int getItemValue() {
		return itemValue;
	}
	/**
	 * @param itemValue set itemValue
	 */
	public void setItemValue(int itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * @return itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName set itemName
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String toString() {
		return this.getItemName();
	}
}

class ImageUtil {
	public static int a(int c) {
		return c >>> 24;
	}
	public static int r(int c) {
		return c >> 16 & 0xff;
	}
	public static int g(int c) {
		return c >> 8 & 0xff;
	}
	public static int b(int c) {
		return c & 0xff;
	}
	public static int rgb(int r, int g, int b) {
		return 0xff000000 | r << 16 | g << 8 | b;
	}
	public static int argb(int a, int r, int g, int b) {
		return a << 24 | r << 16 | g << 8 | b;
	}
}
