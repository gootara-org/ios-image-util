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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The main window frame of IOSImageUtil.
 *
 * @author gootara.org
 */
public class SplitterFrame extends JDialog {
	private JRadioButton as3x, sized;
	private JCheckBox overwriteAlways;
	private JTextField width1x, height1x, subdir;
	private JButton splitButton;
	private File targetFile;
	private JProgressBar progress;
	private ExecutorService pool;

	/**
	 * Constructor
	 *
	 * @param owner
	 * @param title
	 */
	public SplitterFrame(Window owner, String title) {
		super(owner, title, Dialog.ModalityType.MODELESS);
	}

	/**
	 * dialog init.
	 */
	protected void dialogInit() {
		super.dialogInit();
		final MainFrame owner = (MainFrame)this.getParent();
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setSize(320, 544);
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		Font fontSmall = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 11);
		Font fontLarge = new Font(owner.getResource("font.default.name", Font.SANS_SERIF), Font.PLAIN, 14);

		final JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(5, 1));
		settings.setBackground(Color.WHITE);

		// Checkboxes and Radio Buttons.
		settings.add(this.as3x = new JRadioButton(owner.getResource("splitter.as3x", "as 3x"), true));
		settings.add(this.sized = new JRadioButton(owner.getResource("splitter.sized", "sized"), false));
		this.as3x.setBackground(Color.WHITE);
		this.sized.setBackground(Color.WHITE);
		this.as3x.setBorder(new EmptyBorder(0, 10, 0, 10));
		this.sized.setBorder(new EmptyBorder(0, 10, 0, 10));

		ButtonGroup group = new ButtonGroup();
		group.add(this.as3x);
		group.add(this.sized);

		JPanel sizePanel = new JPanel();
		sizePanel.setBackground(Color.WHITE);
		sizePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 4));
		sizePanel.add(new JLabel(owner.getResource("splitter.label.size", "size:")));
		sizePanel.add(this.width1x = new JTextField("44 px", 5));
		sizePanel.add(new JLabel(owner.getResource("splitter.label.x", "x")));
		sizePanel.add(this.height1x = new JTextField("44 px", 5));
		this.width1x.setHorizontalAlignment(JTextField.RIGHT);
		this.height1x.setHorizontalAlignment(JTextField.RIGHT);
		Insets insets = this.width1x.getInsets();
		insets.left = 2; insets.right = 2;
		this.width1x.setMargin(insets);
		insets = this.height1x.getInsets();
		insets.left = 2; insets.right = 2;
		this.height1x.setMargin(insets);
		this.width1x.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) { width1x.select(0, width1x.getText().indexOf(" ") < 0 ? width1x.getText().length() : width1x.getText().indexOf(" ")); }
			@Override public void focusLost(FocusEvent e) {
				try {
					setWidth1x(width1x.getText());
				} catch (Throwable t) {
					t.printStackTrace();
					width1x.requestFocus();
				}
			}
		});
		this.height1x.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) { height1x.select(0, height1x.getText().indexOf(" ") < 0 ? height1x.getText().length() : height1x.getText().indexOf(" ")); }
			@Override public void focusLost(FocusEvent e) {
				try {
					setHeight1x(height1x.getText());
				} catch (Throwable t) {
					t.printStackTrace();
					height1x.requestFocus();
				}
			}
		});
		this.width1x.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { setSized(true); }
			@Override public void insertUpdate(DocumentEvent e) { setSized(true); }
			@Override public void removeUpdate(DocumentEvent e) { setSized(true); }
		});
		this.height1x.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { setSized(true); }
			@Override public void insertUpdate(DocumentEvent e) { setSized(true); }
			@Override public void removeUpdate(DocumentEvent e) { setSized(true); }
		});
		JLabel both = new JLabel(owner.getResource("splitter.label.both", "(Accept empty either)"));
		both.setFont(fontSmall);
		both.setForeground(MainFrame.COLOR_DARK_GRAY);// Color(0x8E8E93));
		sizePanel.add(both);

		settings.add(sizePanel);


		settings.add(this.overwriteAlways = new JCheckBox(owner.getResource("splitter.overwrite.always", "Overwrite always"), false));
		this.overwriteAlways.setBackground(Color.WHITE);
		this.overwriteAlways.setHorizontalAlignment(SwingConstants.CENTER);

		final JLabel label = new JLabel(owner.getResource("splitter.label.subdir", "Output Dir (Relative Path) :"), SwingConstants.LEFT);
		//label.setForeground(MainFrame.COLOR_CYAN);// Color(0x34AADC));
		JPanel dirPanel = new JPanel();
		dirPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 0));
		dirPanel.setBackground(Color.WHITE);
		dirPanel.add(label);

		final String placeHolder = owner.getResource("splitter.label", "Same as origin.");
		dirPanel.add(this.subdir = new JTextField(placeHolder, 16));
		this.subdir.setMargin(insets);
		this.subdir.setForeground(Color.GRAY);
		this.subdir.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent e) {
				if (subdir.getText().trim().equals(placeHolder)) {
					subdir.setText("");
					subdir.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (subdir.getText().trim().isEmpty()) {
					subdir.setText(placeHolder);
					subdir.setForeground(Color.GRAY);
				}
			}
		});
		settings.add(dirPanel);
		this.add(settings, BorderLayout.NORTH);

		final JDialog dialog = this;

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
		splitButton.setMargin(new Insets(64, 16, 64, 16));
		splitButton.setOpaque(true);
		splitButton.setTransferHandler(new TransferHandler() {
			@Override public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						List<File> files = new LinkedList<File>();
						if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
							Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
							if (list instanceof List<?>) {
								for (Object o : ((List<?>)list)) {
									if (o instanceof File) {
										if (((File)o).isFile()) {
											files.add((File)o);
										}
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
						dialog.setAlwaysOnTop(true);
						dialog.toFront();
						dialog.requestFocus();
						dialog.setAlwaysOnTop(false);
						if (dropFiles(files)) {
							return true;
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					JOptionPane.showMessageDialog(splitButton.getParent(), t.toString() + " (" + t.getMessage() + ")", owner.getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				} finally {
					dialog.setAlwaysOnTop(false);
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
						}
					}
	            }
				return result;
			}
		});
		splitButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (pool != null && !pool.isTerminated()) {
					if (JOptionPane.showConfirmDialog(dialog, owner.getResource("splitter.question.cancel", "Are you sure to cancel generating images?"), owner.getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
						return;
					}
					if (pool != null && !pool.isTerminated()) {
						try {
							pool.shutdownNow();
						} catch (Throwable t) {
							owner.handleThrowable(t);
						}
					}
					splitButton.setText(owner.getResource("label.cancel.generate", "Cancel generate..."));
					return;
				}

				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				if (targetFile == null) {
					targetFile = owner.getChosenDirectory();
				}
				if (targetFile != null) {
					if (targetFile.isFile()) {
						chooser.setCurrentDirectory(targetFile.getParentFile());
					}
					if (targetFile.exists()) {
						chooser.setSelectedFile(targetFile);
					}
				} else {
					chooser.setCurrentDirectory(targetFile);
				}
				chooser.setApproveButtonText(owner.getResource("button.approve", "Choose"));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int returnVal = chooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						List<File> files = new LinkedList<File>();
						files.add(chooser.getSelectedFile());
						dropFiles(files);
					} catch (Throwable t) {
						t.printStackTrace();
						JOptionPane.showMessageDialog(splitButton.getParent(), t.toString() + " (" + t.getMessage() + ")", owner.getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
					}
			    }

			}
		});
		this.add(splitButton, BorderLayout.CENTER);

		progress = new JProgressBar();
		progress.setValue(0);
		progress.setStringPainted(true);
		progress.setBorderPainted(false);
		progress.setBackground(MainFrame.BGCOLOR_LIGHT_GRAY);
		progress.setForeground(splitButton.getBackground());
		progress.setOpaque(true);
		this.add(progress, BorderLayout.SOUTH);


		this.pack();
	}

	private boolean dropFiles(List<File> files) {
		final MainFrame owner = (MainFrame)this.getParent();
		try {
			if (pool != null && !pool.isShutdown()) {
				return false;
			}

			if (!this.overwriteAlways.isSelected()) {
				String existingFilename = null;
				for (File file : files) {
					for (int i = 1; i <= 3; i++) {
						File f = this.getOutputFile(file, i);
						if (f.exists()) {
							existingFilename = f.getAbsolutePath();
							break;
						}
					}
				}
				if (existingFilename != null && !owner.isBatchMode()) {
					if (JOptionPane.showConfirmDialog(this, String.format(owner.getResource("splitter.already.exists", "[%s] already exists. Replace file?"), existingFilename), owner.getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
						return false;
					}
				}
			}

			progress.setMaximum(files.size() * 3);
			progress.setValue(0);
			if (!owner.isBatchMode()) {
				//splitButton.setEnabled(false);
				splitButton.setIcon(new ImageIcon(this.getClass().getResource("img/generate.gif")));
				splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/generate.gif")));
				splitButton.setBackground(new Color(0xF7F7F7));
				splitButton.setForeground(Color.GRAY);
				splitButton.setText(owner.getResource("splitter.executing", "Split images..."));
			}

			int threadCount = Runtime.getRuntime().availableProcessors();
			if (threadCount > 8) {
				threadCount = 8;
			}
			pool = Executors.newFixedThreadPool(threadCount);
			for (File file : files) {
				final File target = file;
				SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
					@Override protected Boolean doInBackground() throws Exception {
						try {
							split(target);
							return true;
						} catch (Throwable t) {
							owner.handleThrowable(t);
							return false;
						}
					}
				};
				pool.submit(worker);
			}

			SwingWorker<Boolean, Integer> watchdog = new SwingWorker<Boolean, Integer>() {
				@Override protected Boolean doInBackground() throws Exception {
					boolean result = true;
					try {
						pool.shutdown();
						if (!pool.awaitTermination(progress.getMaximum(), TimeUnit.MINUTES)) {
							result = false;
						}
					} catch (Throwable t) {
						owner.handleThrowable(t);
					} finally {
						if (!owner.isBatchMode()) {
							splitButton.setText(owner.getResource("splitter.button", "Split"));
							splitButton.setBackground(new Color(0x007AFF));
							splitButton.setForeground(Color.WHITE);
							splitButton.setEnabled(true);
							splitButton.setIcon(new ImageIcon(this.getClass().getResource("img/splitter.png")));
							splitButton.setRolloverIcon(new ImageIcon(this.getClass().getResource("img/splitter_rollover.png")));
						}
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
			return false;
		}
		return true;
	}

	// for command line option switches.
	protected void setAs3x(boolean b) { this.as3x.setSelected(b); }
	protected void setSized(boolean b) { this.sized.setSelected(b); }
	protected void setWidth1x(String width) { this.setImageSize(this.width1x, width); }
	protected void setHeight1x(String height) { this.setImageSize(this.height1x, height); }
	protected void setOverwriteAlways(boolean b) { this.overwriteAlways.setSelected(b); }
	protected void setOutputDirectory(String relativePath) { this.subdir.setText(relativePath); }
	private void setImageSize(JTextField text, String size) {
		size = size.toLowerCase().trim();
		if (size.isEmpty() || size.equals("px") || size.equals("%")) {
			text.setText("");
			return;
		}
		if (size.endsWith("px")) { size = size.replaceAll("px", "").trim(); }
		boolean per = size.endsWith("%");
		if (per) { size = size.replaceAll("%", "").trim(); }
		text.setText(new Integer(Integer.parseInt(size)).toString().concat(" ").concat(per ? "%" : "px"));
	}
	private float getImageSize(String size, float imageSize) {
		size = size.toLowerCase().trim();
		if (size.isEmpty() || size.equals("px") || size.equals("%")) {
			return 0;
		}

		if (size.endsWith("px")) { size = size.replaceAll("px", "").trim(); }
		boolean per = size.endsWith("%");
		if (per) { size = size.replaceAll("%", "").trim(); }
		return per ? imageSize * (Float.parseFloat(size) / 100.0f) : Float.parseFloat(size);
	}
	private File getOutputFile(File srcFile, int scale) throws Exception {
		String fileName = srcFile.getName();
		String sufix = "";
		int idx = fileName.lastIndexOf(".");
		if (idx >= 0) {
			sufix = fileName.substring(idx);
			fileName = fileName.substring(0, idx);
		}

		File dir = srcFile.getParentFile();
		String sub = this.subdir.getText().trim();
		if (sub.equals(((MainFrame)this.getParent()).getResource("splitter.label", "Same as origin."))) {
			sub = "";
		}
		if (!sub.isEmpty()) {
			dir = new File(dir, sub);
			if (!dir.exists() && !dir.mkdirs()) {
				throw new IOException(String.format("Can't create directory. [%s]", dir.getAbsolutePath()));
			}
		}
		return new File(dir, String.format("%s@%dx%s", fileName, scale, sufix));
	}


	/**
	 * Do split images.
	 *
	 * @param f	image file
	 * @throws Exception
	 */
	protected void split(File f) throws Exception {
		targetFile = f;
		BufferedImage image = ImageIO.read(f);
		if (image == null) {
			((MainFrame)this.getParent()).alert("[" + f.getCanonicalPath() + "] " + ((MainFrame)this.getParent()).getResource("error.illegal.image", "is illegal image."));
			return;
		}

		split(f, image, 1);
		split(f, image, 2);
		split(f, image, 3);
		image.flush();
		image = null;
		System.gc();
	}

	/**
	 * Do split images with scale.
	 *
	 * @param srcFile	image file
	 * @param src		buffer
	 * @param scale		scale
	 * @throws Exception
	 */
	private void split(File srcFile, BufferedImage src, int scale) {
		MainFrame parent = (MainFrame)this.getParent();
		try {
			File f = this.getOutputFile(srcFile, scale);
			if (f.exists() && !this.overwriteAlways.isSelected()) {
				if (parent.isBatchMode()) {
					System.out.println(String.format("[%s] is already exists. Skip it.", f.getAbsolutePath()));
					return;
				}
			}
			parent.verbose(f);
			if (src == null) {
				return;
			}

			Dimension size = null;
			if (this.as3x.isSelected()) {
				size = new Dimension(src.getWidth() / 3 * scale, src.getHeight() / 3 * scale);
			} else {
				if (this.width1x.getText().trim().isEmpty() && this.height1x.getText().trim().isEmpty()) {
					throw new Exception(parent.getResource("splitter.error.empty.both", "Either the width or height is required."));
				}
				float width = this.getImageSize(this.width1x.getText(), (float)src.getWidth() / 3.0f);
				float height = this.getImageSize(this.height1x.getText(), (float)src.getHeight() / 3.0f);
				float p = width == 0 ? height / ((float)src.getHeight() / 3.0f) : width / ((float)src.getWidth() / 3.0f);
				width = width == 0 ? ((float)src.getWidth() / 3.0f) * p : width;
				height = height == 0 ? ((float)src.getHeight() / 3.0f) * p : height;
				size = new Dimension(Math.round(width) * scale, Math.round(height) * scale);
			}

			Image img = src.getScaledInstance(size.width, size.height, parent.getScalingAlgorithm());
			BufferedImage buf = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
			buf.getGraphics().drawImage(img, 0, 0, this);
			img.flush();
			img = null;

			ImageIO.write(parent.fixImageColor(buf, src), "png", f);
			buf.flush();
			buf = null;
		} catch (Throwable t) {
			parent.handleThrowable(t);
		} finally {
			progress.setValue(progress.getValue() + 1);
		}
	}
}
