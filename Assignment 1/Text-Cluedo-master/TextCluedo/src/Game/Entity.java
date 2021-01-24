package Game;

import java.awt.*;

/**
 * Represents a movable item on the board.
 */
public abstract class Entity {
    private Cell location;
    private Board board;
    private char[] chars;

    /**
     * Create a new entity
     * @param board the board
     * @param location cell the entity begins at
     * @param chars the characters to represent on the board
     */
    public Entity(Board board, Cell location, char[] chars) {
        this.location = location;
        this.board = board;
        this.chars = chars;

        // add the entity to the location
        location.setEntity(this);
    }

    /**
     * Moves the entity to a given cell
     * @cell the cell to move to
     */
    public void moveTo(Cell cell) {
        location.removeEntity();
        cell.setEntity(this);
        this.location = cell;
    }

    /**
     * Chars that represent the entity
     * @return
     */
    public char[] getChars() {
        return chars;
    }


    /**
     * The board
     * @return
     */
    protected Board getBoard() {
        return board;
    }

    /**
     * The location
     * @return
     */
    public Cell getLocation() {
        return location;
    }

    /**
     * Draw the entity
     * @param g the graphics to draw to
     * @param x
     * @param y
     * @param cellSize
     */
	public abstract void draw(Graphics g, int cellSize);
}
