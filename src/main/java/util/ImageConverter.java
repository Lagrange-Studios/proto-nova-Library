package util;

import java.awt.image.BufferedImage;

public class ImageConverter {

  public static BufferedImage getSprite(BufferedImage sheet, int imageNumber, int size) {
    // x and y are the top-left coordinates of the desired sprite within the sheet
    // width and height are the dimensions of the sprite
    int sheetWidth = sheet.getWidth();
    int sheetHeight = sheet.getHeight();

    int spritesPerRow = sheetWidth / size;
    int spritesPerColumn = sheetHeight / size;

    int y = 0;
    int x = 0;
    if (imageNumber > 2) {
      x = imageNumber - 3;
      y = 1;
    } else {
      x = imageNumber - 1;
    }

    if (x >= spritesPerRow || y >= spritesPerColumn) {
      throw new IllegalArgumentException(
          "Sprite number "
              + imageNumber
              + " is out of bounds for sheet size "
              + sheetWidth
              + "x"
              + sheetHeight
              + ". Sheet can only hold "
              + (spritesPerRow * spritesPerColumn)
              + " sprites.");
    }

    return sheet.getSubimage(x * size, y * size, size, size);
  }
}
