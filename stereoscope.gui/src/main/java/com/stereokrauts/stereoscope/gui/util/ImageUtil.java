package com.stereokrauts.stereoscope.gui.util;

import java.net.URL;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public final class ImageUtil {
	public static ImageData getImageDataFrom(final URL url) {
		ImageData returnValue;
		try {
			returnValue = new ImageData(url.openStream());
		} catch (final Exception e) {
			returnValue = new ImageData(50, 50, 24, new PaletteData(0, 0, 0));
		}
		return returnValue;
	}

	public static ImageData getScaledImageDataFrom(final URL url, final int maxWidth, final int maxHeight) {
		final ImageData imageData = getImageDataFrom(url);

		final int width = imageData.width;
		final int height = imageData.height;

		double scaleFactor = 1;

		final double scaleFactorWidth = Math.min(1.0, (double) maxWidth / width);
		final double scaleFactorHeight = Math.min(1.0, (double) maxHeight / height);

		if (scaleFactorWidth > scaleFactorHeight) {
			scaleFactor = scaleFactorHeight;
		} else {
			scaleFactor = scaleFactorWidth;
		}

		return imageData.scaledTo((int) (width * scaleFactor), (int) (height * scaleFactor));
	}
}
