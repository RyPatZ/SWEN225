package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Wall Cell
 */
public class WallCell extends Cell {

    /**
     * Wall Cell
     * Never free
     * @param x
     * @param y
     * @param chars characters that represent the wall
     */
    public WallCell(int x, int y, char[] chars, BufferedImage image) {
        super(x, y, chars, false, image);
    }
}
