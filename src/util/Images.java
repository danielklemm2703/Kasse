package util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import datameer.com.google.common.base.Supplier;

public class Images {
    public static final Try<BufferedImage> loadImage(final String path) {
	return Try.of(new Supplier<BufferedImage>() {
	    @Override
	    public BufferedImage get() {
		try {
		    return ImageIO.read(new File(path));
		} catch (IOException e) {
		    throw new IllegalStateException(e.getLocalizedMessage());
		}
	    }
	});
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
	int w = img.getWidth();
	int h = img.getHeight();
	BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
	Graphics2D g = dimg.createGraphics();
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
	g.dispose();
	return dimg;
    }
}
