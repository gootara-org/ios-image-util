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
import java.awt.Insets;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;

/**
 * The main window frame of IOSImageUtil.
 *
 * @author gootara.org
 */
public class SplitterFrame extends JDialog {
	private JRadioButton as3x, sized;
	private JCheckBox overwriteAlways;
	private JTextField width1x, height1x;
	private JButton splitButton;

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
		this.setSize(320, 512);
		this.setResizable(false);
		this.setBackground(Color.WHITE);

		final JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(5, 1));
		settings.setBackground(Color.WHITE);

		// Checkboxes and Radio Buttons.
		settings.add(this.as3x = new JRadioButton(owner.getResource("splitter.as3x", "as 3x"), true));
		settings.add(this.sized = new JRadioButton(owner.getResource("splitter.sized", "sized"), false));
		this.as3x.setBackground(Color.WHITE);
		this.sized.setBackground(Color.WHITE);
		ButtonGroup group = new ButtonGroup();
		group.add(this.as3x);
		group.add(this.sized);

		JPanel sizePanel = new JPanel();
		sizePanel.setBackground(Color.WHITE);
		sizePanel.setLayout(new FlowLayout());
		sizePanel.add(new JLabel(owner.getResource("splitter.label.size", "size:")));
		sizePanel.add(this.width1x = new JTextField("44", 4));
		sizePanel.add(new JLabel(owner.getResource("splitter.label.x", "x")));
		sizePanel.add(this.height1x = new JTextField("44", 4));
		this.width1x.setHorizontalAlignment(JTextField.RIGHT);
		this.height1x.setHorizontalAlignment(JTextField.RIGHT);
		settings.add(sizePanel);

		settings.add(this.overwriteAlways = new JCheckBox(owner.getResource("splitter.overwrite.always", "Overwrite always"), false));
		this.overwriteAlways.setBackground(Color.WHITE);
		this.overwriteAlways.setHorizontalAlignment(SwingConstants.CENTER);

		final JLabel label = new JLabel(owner.getResource("splitter.label", "Output images to directory same as origin."), SwingConstants.CENTER);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
		label.setForeground(new Color(0x34AADC));
		settings.add(label);

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
		splitButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		splitButton.setMargin(new Insets(64, 64, 64, 64));
		splitButton.setOpaque(true);
		splitButton.setTransferHandler(new TransferHandler() {
			public boolean importData(TransferSupport support) {
				try {
					if (canImport(support)) {
						final Object list = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						if (list instanceof List) {
							splitButton.setEnabled(false);
							splitButton.setBackground(new Color(0xF7F7F7));
							splitButton.setText(owner.getResource("splitter.executing", "Split images..."));

							SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
								@Override protected Boolean doInBackground() throws Exception {
									try {
										split((File)((List<?>)list).get(0));
										return true;
									} catch (Throwable t) {
										t.printStackTrace();
										JOptionPane.showMessageDialog(splitButton.getParent(), t.toString() + " (" + t.getMessage() + ")", owner.getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
										return false;
									} finally {
										splitButton.setText(owner.getResource("splitter.button", "Split"));
										splitButton.setBackground(new Color(0x007AFF));
										splitButton.setEnabled(true);
									}
								}
							};
							worker.execute();
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					JOptionPane.showMessageDialog(splitButton.getParent(), t.toString() + " (" + t.getMessage() + ")", owner.getResource("title.error", "Error"), JOptionPane.ERROR_MESSAGE);
				}
				return false;
			}
			public boolean canImport(TransferSupport support) {
				return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
		});
		this.add(splitButton, BorderLayout.CENTER);

		this.pack();
	}

	// for command line option switches.
	protected void setAs3x(boolean b) { this.as3x.setSelected(b); }
	protected void setSized(boolean b) { this.sized.setSelected(b); }
	protected void setWidth1x(int width) { this.width1x.setText(Integer.valueOf(width).toString()); }
	protected void setHeight1x(int height) { this.height1x.setText(Integer.valueOf(height).toString()); }
	protected void setOverwriteAlways(boolean b) { this.overwriteAlways.setSelected(b); }

	/**
	 * Do split images.
	 *
	 * @param f	image file
	 * @throws Exception
	 */
	protected void split(File f) throws Exception {
		BufferedImage image = ImageIO.read(f);
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
	private void split(File srcFile, BufferedImage src, int scale) throws Exception {
		MainFrame parent = (MainFrame)this.getParent();
		splitButton.setText(String.format("%s(%d/3)", parent.getResource("splitter.executing", "Split images..."), scale));
		String fileName = srcFile.getName();
		String sufix = "";
		int idx = fileName.lastIndexOf(".");
		if (idx >= 0) {
			sufix = fileName.substring(idx);
			fileName = fileName.substring(0, idx);
		}
		File f = new File(srcFile.getParentFile(), String.format("%s@%dx%s", fileName, scale, sufix));
		if (f.exists() && !this.overwriteAlways.isSelected()) {
			if (parent.isBatchMode()) {
				System.out.println(String.format("[%s] is already exists. Skip it.", f.getAbsolutePath()));
				return;
			} else {
				if (JOptionPane.showConfirmDialog(this, String.format(parent.getResource("splitter.already.exists", "[%s] already exists. Replace file?"), f.getAbsolutePath()), parent.getResource("title.question", "Question"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}
		parent.verbose(f);

		Dimension size = new Dimension(Integer.parseInt(this.width1x.getText()) * scale, Integer.parseInt(this.height1x.getText()) * scale);
		if (this.as3x.isSelected()) {
			size = new Dimension(src.getWidth() / 3 * scale, src.getHeight() / 3 * scale);
		}

		BufferedImage buf = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		buf.getGraphics().drawImage(src.getScaledInstance(size.width, size.height, parent.getScalingAlgorithm()), 0, 0, this);

		ImageIO.write(parent.fixImageColor(buf, src), "png", f);
		buf.flush();
		buf = null;
	}
}
