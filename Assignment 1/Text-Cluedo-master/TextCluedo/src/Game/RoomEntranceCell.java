package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Room Entrance Cell
 */
public class RoomEntranceCell extends Cell{
    private Cell.Direction direction;
    private Room room;

	/**
	 * Create an entrance to a room
	 * @param room
	 * @param x
	 * @param y
	 * @param chars
	 * @param direction
	 */
    public RoomEntranceCell(Room room, int x, int y, char[] chars, Cell.Direction direction, BufferedImage image) {
        super(x, y, chars, true, image);
        this.direction = direction;

        this.room = room;
    }

	/**
	 * Get the direction
	 * @return
	 */
	public Cell.Direction getDirection() {
    	return direction;
	}

    /**
     * Get the room
     */
    public Room getRoom() {
        return room;
    }
}
