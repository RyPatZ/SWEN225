package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Weapon on the board
 */
public class Weapon extends Entity {
	private BufferedImage image;
	/**
	 * Create new weapon object
	 * @param board
	 * @param room
	 * @param chars
	 */
	@Deprecated
	public Weapon(Board board, Room room, char[] chars){
		super(board, room.getAvailableCell(), chars);
	}

	/**
	 * Create new weapon object
	 * @param board
	 * @param room
	 * @param chars
	 */
	public Weapon(Board board, Room room, char[] chars, String fileName){
		super(board, room.getAvailableCell(), chars);

		try {
			image = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			throw new Error("Failed to read image: " + fileName);
		}
	}


	/**
	 * Draw the weapon
	 *
	 * @param g        the graphics to draw to
	 * @param cellSize
	 */
	@Override
	public void draw(Graphics g, int cellSize) {
		g.drawImage(image, getLocation().getX() * cellSize, getLocation().getY() * cellSize, cellSize, cellSize, null);
	}
}
