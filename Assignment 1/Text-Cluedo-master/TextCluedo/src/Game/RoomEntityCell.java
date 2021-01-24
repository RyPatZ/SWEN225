package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Cells in the room where entities can appear
 */
public class RoomEntityCell extends Cell {
    private Room room;

    /**
     * Cells in the room where entities can appear
     *
     * @param x
     * @param y
     * @param chars
     */
    public RoomEntityCell(Room room, int x, int y, char[] chars, BufferedImage image) {
        super(x, y, chars, true, image);

        this.room = room;
    }


    /**
     * Get the room
     * @return
     */
    public Room getRoom() {
        return room;
    }
}
