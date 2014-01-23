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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author gootara.org
 *
 */
public class ImagePanel extends JPanel {

	private BufferedImage image;
	private Image scaledImage;
	private String placeHolder;

	public ImagePanel() {
		this("");
	}

	public ImagePanel(String placeHolder) {
		this.image = null;
		this.scaledImage = null;
		this.setPlaceHolder(placeHolder);

		this.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { createScaledImage(); repaint(); }
			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
			@Override public void componentHidden(ComponentEvent e) {}
		});
	}

	public void setImage(BufferedImage image){
		this.image = image;
		createScaledImage();
		this.repaint();
	}

	public BufferedImage getImage(){
		return this.image;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
    	if(!isShowing()){
			return;
    	}
    	if (image == null || scaledImage == null) {
    		FontMetrics fm = this.getFontMetrics(this.getFont());
    		String[] lines = this.getPlaceHolder().split("<br>");
//    		ArrayList<String> lines = this.getPlaceHolderLines(fm);
//    		int y = (this.getHeight() / 2) - ((fm.getHeight() * lines.size()) / 2) + (fm.getAscent() / 2);
    		int y = (this.getHeight() / 2) - ((fm.getHeight() * lines.length) / 2) + (fm.getAscent() / 2);
    		for (String line : lines) {
        		int x = (this.getWidth() - fm.stringWidth(line)) / 2;
        		g.setColor(new Color(128, 128, 128));
        		g.drawString(line, x, y);
        		y += fm.getHeight();
    		}

    	} else {
    		int x = (int) ((this.getWidth() - this.scaledImage.getWidth(this)) / 2);
    		int y = (int) ((this.getHeight() - this.scaledImage.getHeight(this)) / 2);
    		g.drawImage(scaledImage, x, y, this);
    	}
		g.setColor(new Color(128, 128, 128));
		g.drawRect(0,  0, this.getWidth() - 1, this.getHeight() - 1);
    }

    public Dimension getPreferredSize() {
    	if (image == null) {
    		return (new Dimension(0, 0));
    	}
    	return (new Dimension(image.getWidth(), image.getHeight()));
    }

    public void update(Graphics g) {
    	paint(g);
    }

	/**
	 * @return placeHolder
	 */
	public String getPlaceHolder() {
		return placeHolder;
	}

	/**
	 * @param placeHolder set placeHolder
	 */
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}

	/*
	 * For japanese below.
	public ArrayList<String> getPlaceHolderLines(FontMetrics fm) {
		ArrayList<String> lines = new ArrayList<String>();
		String line = this.getPlaceHolder();
		while (line.length() > 0) {
			int i;
			for (i = 1; i < line.length(); i++) {
				if (this.getWidth() <= fm.stringWidth(line.substring(0, i))) {
					if (i > 1) i--;
					break;
				}
			}
			lines.add(line.substring(0, i));
			if (line.length() <= 1) break;
			line = line.substring(i);
		}
		return lines;
	}
	*/

	private synchronized void createScaledImage() {
		if (image == null) {
			this.scaledImage = null;
			return;
		}
		Dimension size = this.getPreferredSize();
		double p = (this.getWidth() > this.getHeight()) ? (double)this.getHeight() / size.getHeight() : (double)this.getWidth() / size.getWidth();
		int w = (int) (size.getWidth() * p);
		int h = (int) (size.getHeight() * p);
		scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	}
}


