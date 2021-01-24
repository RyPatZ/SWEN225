package Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A grid space on the board
 */
public abstract class Cell {

	/**
	 * A direction
	 */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;

        /**
         * Get a direction from a string
         * @param direction
         * @return
         */
		public static Direction getDirection(String direction) {
		    if(direction.equalsIgnoreCase("up") || direction.equalsIgnoreCase("u")) {
		        return Direction.UP;
            }
		    else if(direction.equalsIgnoreCase("down") || direction.equalsIgnoreCase("d")) {
		        return Direction.DOWN;
            }
		    else if(direction.equalsIgnoreCase("left") || direction.equalsIgnoreCase("l")) {
		        return Direction.LEFT;
            }
		    else if(direction.equalsIgnoreCase("right") || direction.equalsIgnoreCase("r")) {
		        return Direction.RIGHT;
            }
		    else return null;
		}


        /**
         * The reverse of this direction
         * @return
         */
		public Direction reverse() {
            switch(this) {
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    return null;
            }
        }
	}


    private final int x;
    private final int y;
    private final char[] chars;
    private boolean free;
    private Entity entity;
    private BufferedImage image;

    /**
     * Constructor for a cell object
     *
     * @param x     x location
     * @param y     y location
     * @param chars default characters to represent the cell
     * @param free  if the cell can be moved onto
     */
    public Cell(int x, int y, char[] chars, boolean free, BufferedImage image) {
        this.x = x;
        this.y = y;
        if (chars.length != 2) throw new IllegalArgumentException("Cells may only be represented by 2 characters.");
        this.chars = chars;
        this.free = free;
        this.entity = null;
        this.image = image;
    }


    /**
     * Get the characters at this
     *
     * @return
     */
    public char[] getChars() {
        return entity == null ? chars : entity.getChars();
    }

    /**
     * Set the entity at this cell
     */
    public void setEntity(Entity entity) {
        if (!isFree() || this.entity != null) throw new IllegalStateException("Cannot move an entity into an occupied cell.");
        this.entity = entity;
    }

	/**
	 * Remove the entity at this cell
	 */
	public void removeEntity() {
		this.entity = null;
	}

	/**
     * If you can walk to this cell
     *
     * @return
     */
    public boolean isFree() {
        return free && entity == null;
    }

    /**
     * X
     */
    public int getX() {
        return x;
    }
    /**
     * Y
     */
    public int getY() {
        return y;
    }
    
    /**
     * entity
     */
    public Entity getEntity() {
        return entity;
    }

	/**
	 * Draw cell and entity onto the graphics
	 * @param g graphics to draw to
     * @param cellSize
	 */
    public void draw(Graphics g, int cellSize) {
		drawCell(g, cellSize);
		if(entity != null) {
			entity.draw(g, cellSize);
		}
	}

	/**
	 * Draw the cell onto the graphics
	 * @param g graphics to draw to
	 * @param cellSize
	 */
	public void drawCell(Graphics g, int cellSize) {
        g.drawImage(image, x * cellSize, y * cellSize, cellSize, cellSize, null);
    }


    /**
     * Highlight this cell with a given color
     * @param g
     * @param cellSize
     * @param color
     */
	public void highlightCell(Graphics g, int cellSize, Color color) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 127));
        g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
    }

    /**
     * Outline this cell with a given color
     * @param g
     * @param cellSize
     * @param color
     */
    public void outlineCell(Graphics g, int cellSize, Color color) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(2));
        g2.setColor(color);
        g2.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
        g2.setStroke(new BasicStroke(1));
    }



    /**
     * For testing
     * @return
     */
	public String toString() {
        return getClass().getName() + " at " + x + "," + y;
    }
}