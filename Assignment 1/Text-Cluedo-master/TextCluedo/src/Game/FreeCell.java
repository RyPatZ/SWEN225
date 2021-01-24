package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Free Cell
 */
public class FreeCell extends Cell {
    /**
     * Free cell. By default will always be blank and you can move into it.
     * @param x
     * @param y
     */
    public FreeCell(int x, int y, BufferedImage image) {
        super(x, y, "  ".toCharArray(), true, image);
    }
}
