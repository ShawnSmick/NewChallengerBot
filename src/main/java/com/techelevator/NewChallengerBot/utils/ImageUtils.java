package com.techelevator.NewChallengerBot.utils;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static BufferedImage imageResize(BufferedImage image, int widthClamp, int heightClamp) {
		int width = image.getWidth();
		int height = image.getHeight();
		boolean scaled = false;
		if (height > heightClamp) {
			width = (int) (((double) heightClamp / (double) height) * width);
			height = heightClamp;
			scaled = true;
		}
		if (width > widthClamp) {
			height = (int) (((double) widthClamp / (double) width) * height);
			width = widthClamp;
			scaled = true;
		}

		if (height < heightClamp && !scaled) {
			width = (int) (((double) heightClamp / (double) height) * width);
			height = heightClamp;
			scaled = true;
		}
		if (width > widthClamp && !scaled) {
			height = (int) (((double) widthClamp / (double) width) * height);
			width = widthClamp;
			scaled = true;
		}

		BufferedImage tempImage = new BufferedImage(width, height, image.getType());
		tempImage.getGraphics().drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0,
				null);
		return tempImage;
	}
	public static BufferedImage createImageFromText(String text,Font textFont) {
		FontRenderContext frc = new FontRenderContext(null, false, false);
		Rectangle2D bounds = null;
		TextLayout layout = new TextLayout(text, textFont, frc);

		bounds = layout.getBounds();
		BufferedImage tempImage = new BufferedImage((int) bounds.getWidth() + 60, (int) bounds.getHeight() + 60,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = tempImage.createGraphics();
		layout.draw(g2d, 30,
				(((int) bounds.getHeight() - layout.getDescent()) / 2) + (layout.getAscent() - layout.getDescent()));
		g2d.dispose();
		return tempImage;
	}

	public static byte[] createLogoFromText(String text, Font textFont) {
		BufferedImage tempImage = createImageFromText(text,textFont);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			ImageIO.write(tempImage, "png", bytes);
		} catch (IOException ex) {
			System.out.println("We have failed to write to a variable in our program \n this should never happen");
		}
		return bytes.toByteArray();
	}

}
