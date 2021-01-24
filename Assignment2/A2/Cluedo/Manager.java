
import java.util.*;
import java.util.Scanner;


public class Manager {
	
	
	private Game game; 
	
	public static void main(String[] args) {
		new Manager().gameManager();
	}

	private void clearConsole() {
		for (int i = 0; i < 80; ++i)
			System.out.println();
	}
	
	
	private void gameManager() {
		boolean playing = true;
		Scanner input = new Scanner(System.in);
		while (playing) {
			gameSetup();
			System.out.println("Play again? (y/n)\n");
			if (input.next().equals("n"))
				playing = false;
		}
		clearConsole();
	}

	private void gameSetup() {
		System.out.println("How many players are there? (pick any number 3-6) \n");
		Scanner input = new Scanner(System.in);
		int numOfPlayers = input.nextInt();
		while (numOfPlayers > 6 || numOfPlayers < 3) {		// invalid player number entered
			System.out.println("Please pick a number between 3 and 6.");
			numOfPlayers = input.nextInt();
		}
		game = new Game(numOfPlayers);
		clearConsole();
		game.initialiseGame();
		game.play();
	}

}