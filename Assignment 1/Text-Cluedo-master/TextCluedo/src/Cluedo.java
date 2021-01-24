import Game.Game;
import Gui.MenuView;
import Gui.Window;

import java.util.Scanner;

/**
 * Main class (Entry point)
 */
public class Cluedo {

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		Window window = new Window("", 800, 600);
		new MenuView(window);
	}
}
