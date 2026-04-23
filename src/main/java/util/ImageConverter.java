package util;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

public class ImageConverter {

    public static BufferedImage getSprite(BufferedImage sheet, int imageNumber, int size) {
        // x and y are the top-left coordinates of the desired sprite within the sheet
        // width and height are the dimensions of the sprite
    	int y = 0;
    	int x = 0;
    	if (imageNumber > 2) {
    		x = imageNumber - 3;
    		y = 1;
    	} else {
    		x = imageNumber - 1;
    	}
        return sheet.getSubimage(x*size, y*size, size, size);
    }
}
