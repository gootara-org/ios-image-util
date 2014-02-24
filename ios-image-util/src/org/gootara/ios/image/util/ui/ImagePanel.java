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
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JPanel;

/**
 * @author gootara.org
 *
 */
public class ImagePanel extends JPanel {

	private BufferedImage image;
	private Image scaledImage;
	private String placeHolder, hyphenatorBoL, hyphnatorEoL;

	public ImagePanel() {
		this("");
	}

	public ImagePanel(String placeHolder) {
		this.image = null;
		this.scaledImage = null;
		this.setPlaceHolder(placeHolder);
		this.setHyphenator("", "");

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
    		// Maybe padding, border and text-align will be implemented in some future.(but not need currently)
    		FontMetrics fm = this.getFontMetrics(this.getFont());
    		ArrayList<String> lines = this.getPlaceHolderLines(fm);
    		int y = (this.getHeight() / 2) - ((fm.getHeight() * lines.size()) / 2) + (fm.getAscent() / 2);
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

	/**
	 * I tried to write a little seriously.
	 **/
	public ArrayList<String> getPlaceHolderLines(FontMetrics fm) {
		ArrayList<String> lines = new ArrayList<String>();
		StringBuilder line = new StringBuilder();
		Locale l = Locale.getDefault();
		BreakIterator boundary = BreakIterator.getWordInstance(l.equals(Locale.JAPAN) || l.equals(Locale.JAPANESE) ? l : Locale.US);
        boundary.setText(this.getPlaceHolder());
        int startIndex = boundary.first();
        for (int endIndex = boundary.next(); endIndex != BreakIterator.DONE; startIndex = endIndex, endIndex = boundary.next()) {
        	String word = this.getPlaceHolder().substring(startIndex, endIndex);
        	if (fm.stringWidth(line.toString()) + fm.stringWidth(word) > this.getWidth()) {
        		// Very easy hyphenation. (just only one character)
        		if (this.hyphenatorBoL != null && word.length() == 1 && this.hyphenatorBoL.indexOf(word.charAt(0)) >= 0) {
        			line.append(word);
        			word = new String();
        		} else if (this.hyphnatorEoL != null && line.length() > 1 && this.hyphnatorEoL.indexOf(line.charAt(line.length() - 1)) >= 0) {
        			word = line.substring(line.length() - 1).concat(word);
        			line.setLength(line.length() - 1);
        		}
        		if (line.toString().replace('\u3000', ' ').trim().length() > 0) {
        			lines.add(line.toString());
        		}
        		line.setLength(0);
        	}
        	line.append(word);
        }
        if (line.toString().replace('\u3000', ' ').trim().length() > 0) {
        	lines.add(line.toString());
        }
		return lines;
	}

	private synchronized void createScaledImage() {
		if (this.scaledImage != null) {
			this.scaledImage.flush();
			this.scaledImage = null;
		}
		if (image == null) {
			return;
		}
		Dimension size = this.getPreferredSize();
		if (size.width <= 0 || size.height <= 0) return;
		double p = (this.getWidth() > this.getHeight()) ? (double)this.getHeight() / size.getHeight() : (double)this.getWidth() / size.getWidth();
		if (size.width != size.height) {
			double p2 = (size.width < size.height) ? (double)this.getHeight() / size.getHeight() : (double)this.getWidth() / size.getWidth();
			if (p2 < p) { p = p2; }
		}
		int w = (int) (size.getWidth() * p);
		int h = (int) (size.getHeight() * p);
		if (w <= 0 || h <= 0) return;
		this.scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	}

	public void setHyphenator(String beginingOfLine, String endOfLine) {
		this.hyphenatorBoL = beginingOfLine;
		this.hyphnatorEoL = endOfLine;
	}
}


