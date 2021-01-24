import java.util.*;
import java.util.concurrent.TimeUnit;

public class Player {

	String name;
	Board board;
	char boardIcon;
	int xcurrent;
	int ycurrent;
	boolean validMove;
	int destX;
	int destY;
	int bWidth;
	int bHeight;
	CardSet murderset;
	int roll;
	int currentSteps;
	List<Card> cards;
	boolean stillIngame;
	boolean inRoom;
	Room room;
	private Game game;
	public boolean CorrectAccusation;

	public Player(String name, char icon, int startX, int startY, CardSet murs) {
		this.boardIcon = icon;
		this.name = name;
		this.murderset = murs;
		this.xcurrent = startX;
		this.ycurrent = startY;
		cards = new ArrayList<>();
		validMove = false;
		inRoom = false;
		stillIngame = true;
		room = null;
		CorrectAccusation = false;
	}

	public void setGame(Game game) {
		this.game = game;
		murderset = game.murderCards;
	}

	public void addCard(Card card) {
		cards.add(card);
	}

	public String getCardsAsString() {
		String toReturn = "";
		toReturn += "Your known cards are:\n";
		for (Card c : cards) {
			toReturn += c.getName() + "\n";
		}
		return toReturn;
	}

	public List<Card> getCards() {
		return cards;
	}

	public boolean move(Board board, int nextX, int nextY, int roll, List<Player> players) {
		this.clearSteps();

		if (!(board.getTileAt(nextX, nextY) instanceof DoorTile)) {
			for (Player p : players) {
				if (p.xcurrent == nextX && p.ycurrent == nextY) {
					System.out.println("There is another player in the way.");
					return false;
				}
			}
		}

		// check first to see if a move is impossible
		int steps = this.calculateSteps(nextX, nextY);
		if (steps > roll && !(board.getTileAt(xcurrent, ycurrent) instanceof DoorTile)) {
			System.out.println("You are trying to move further then your roll allows ... sneaky :) ...");
			return false;
		}
		// check to see that you are not moving to an invalid cell
		else if (!(board.getTileAt(nextX, nextY) instanceof HallwayTile)
				&& !(board.getTileAt(nextX, nextY) instanceof DoorTile)) {
			System.out.println(
					"You are trying to move to a tile that cannot be moved to please move to a Hallway or Door tile.");
			return false;
		}

		// else you are moving to a doorway or hallway so need to check the route
		else {
			this.destX = nextX;
			this.destY = nextY;
			this.roll = roll;
			bWidth = board.BOARD_WIDTH;
			bHeight = board.BOARD_HEIGHT;
			if (this.checkValidMove(board.getTileAt(nextX, nextY), roll)) {
				if (board.getTileAt(xcurrent, ycurrent) instanceof HallwayTile)
					board.getTileAt(xcurrent, ycurrent).setIcon('.');
				else {
					room.removePlayerFromRoom(this);
					inRoom = false;
					room = null;
				}

				if (board.getTileAt(nextX, nextY) instanceof DoorTile) {
					((DoorTile) board.getTileAt(nextX, nextY)).getRoom().addPlayerToRoom(this);
					room = ((DoorTile) board.getTileAt(nextX, nextY)).getRoom();
					inRoom = true;
				}

				xcurrent = destX;
				ycurrent = destY;

				return true;
			} else {
				System.out.println("Invalid move, cannot move there without going on the same square twice.");
				return false;
			}
		}

	}

	public void suggestionOrSAccusation() {
		boolean validAns = false;
		Scanner input = new Scanner(System.in);
		List<Player> playersToLeft = getPlayersToLeft(); // get an arraylist of all the players to the left.
		System.out.println("You are currently in room : " + this.room);
		System.out.println("Suggestion or Accusation? (s/a)\n");
		if (input.next().equals("s")) {
			this.makeSuggestion(playersToLeft);
			System.out.println("Would you like to make an accusation now? (y/n)");
			while (!validAns) {
				String ans  = input.next();
				if (ans.equals("y")) {
					this.makeAccusation();
					validAns = true;
				}
				else if (ans.equals("n")) {
					validAns = true;
				}
					
			}
		} else {
			this.makeAccusation();
		}
	}

	public void makeAccusation() {
		boolean validWeapon = false;
		boolean validPlayer = false;
		boolean validRoom = false;
		WeaponCard accusedWeapon = null;
		PersonCard accusedPerson = null;
		RoomCard accusedRoom = null;

		System.out.println("You are now making a accusation please provide a weapon and a player and a room. \n");
		while (validWeapon == false) {
			Scanner weaponString = new Scanner(System.in);
			System.out.println("Please enter a weapon providing the full name e.g. Dagger :");
			String weapon = weaponString.nextLine();
			if (game.getDeck().containsKey(weapon)) {
				accusedWeapon = (WeaponCard) game.getDeck().get(weapon);
				validWeapon = true;
			}
		}
		while (validPlayer == false) {
			Scanner playerString = new Scanner(System.in);
			System.out.println("Please enter a Player providing the full name e.g. Mr. Green :");
			String Splayer = playerString.nextLine();
			if (game.getDeck().containsKey(Splayer)) {
				accusedPerson = (PersonCard) game.getDeck().get(Splayer);
				validPlayer = true;
			}
		}
		while (validRoom == false) {
			Scanner roomString = new Scanner(System.in);
			System.out.println("Please enter a Room providing the full name e.g. Dining room :");
			String Sroom = roomString.nextLine();
			if (game.getDeck().containsKey(Sroom)) {
				accusedRoom = (RoomCard) game.getDeck().get(Sroom);
				validRoom = true;
			}
		}
		checkMurderMatch(accusedWeapon, accusedPerson, accusedRoom);
	}

	private void checkMurderMatch(WeaponCard accusedWeapon, PersonCard accusedPerson, RoomCard accusedRoom) {
		if (murderset.getRoom()== accusedRoom && murderset.getWeapon() == accusedWeapon
				&& murderset.getPerson() == accusedPerson) {

			System.out.println("Congrats Detective you got it right");
			game.playing = false;
			CorrectAccusation = true;

		} else {
			System.out.println("That's incorrect you lose");
			this.stillIngame = false;
		}

	}

	public void makeSuggestion(List<Player> playersToLeft) {

		boolean validWeapon = false;
		boolean validPlayer = false;
		List<Player> sequence = playersToLeft;
		WeaponCard suggestedWeapon = null;
		PersonCard suggestedPlayer = null;
		RoomCard suggestedRoom = (RoomCard) game.getDeck().get(room.toString());
		System.out.println("You are now making a sugestion please provide a weapon and a player. \n");
		// System.out.println("the above example means Weapon = Dagger , Player = Mrs.
		// Scarlett \n");

		while (validWeapon == false) {
			Scanner weaponString = new Scanner(System.in);
			System.out.println("Please enter a weapon providing the full name e.g. Dagger :");
			String weapon = weaponString.nextLine();
			if (game.getDeck().containsKey(weapon)) {
				suggestedWeapon = (WeaponCard) game.getDeck().get(weapon);
				validWeapon = true;
			}
		}
		while (validPlayer == false) {
			Scanner playerString = new Scanner(System.in);
			System.out.println("Please enter a Player providing the full name e.g. Mr. Green :");
			String Splayer = playerString.nextLine();
			if (game.getDeck().containsKey(Splayer)) {
				suggestedPlayer = (PersonCard) game.getDeck().get(Splayer);
				validPlayer = true;
			}
		}
		boolean foundCard = false;
		Card returnedSuggestion = null;
		for (Player opponent : sequence) {
			if (opponent.getCards().contains(suggestedWeapon) || opponent.getCards().contains(suggestedPlayer)
					|| opponent.getCards().contains(suggestedRoom)) {
				returnedSuggestion = this.returnSuggestion(opponent, suggestedWeapon, suggestedPlayer, suggestedRoom);
				foundCard = true;
				break; 
			}
		}
		
		//Move weapon to Room
		Weapon wep = game.getWeapons().get(suggestedWeapon.getName());
		wep.getRoom().removeWeapon(wep);
		wep.setRoom(room);
		room.addWeapon(wep);
		
		//Move player to room
		for (Player p : game.getPlayers()) {
			if (p.getPlayerName() == suggestedPlayer.getName()) {
				if (p.inRoom) {
					p.getPlayerLocation().removePlayerFromRoom(p);
				}
				else 
					board.getTileAt(p.xcurrent, p.ycurrent).setIcon('.');
				
					
				p.setRoom(room);
				room.addPlayerToRoom(p);
				
				p.xcurrent = xcurrent;
				p.ycurrent = ycurrent;
				board.updateBoard();
				System.out.println(board.getBoard());
				break;
			}
		}
		
		if (foundCard == false) {
			System.out.println("No other player has a card that matches the suggested... Getting close detective!");
		} else {
			for (int i = 0; i < 80; ++i)
				System.out.println();
			game.askForPlayer(this);
			System.out.println("Someone showed you they have the " +returnedSuggestion.getName() + " card!");
		}
	}

	private Card returnSuggestion(Player opponent, WeaponCard suggestedWeapon, PersonCard suggestedPlayer,
			RoomCard suggestedRoom) {
		Map<Integer, Card> matchedCards = new HashMap<>();
		int i = 1;
		for (Card c : opponent.getCards()) {
			if (c == suggestedWeapon || c == suggestedPlayer || c == suggestedRoom) {
				matchedCards.put(i, c);
				i++;
			}
		}
		game.askForPlayer(opponent);
		System.out.println(opponent.name
				+ ", you have a card(s) that is in the suggestion " + name + " made.");
		for (int index : matchedCards.keySet()) {
			System.out.println(matchedCards.get(index).getName() + " (" + index + ")");
		}
		Scanner scanChoice = new Scanner(System.in);
		System.out.println("Please enter a number that corresponds to the card you would like to show e.g. 2 :");
		String choiceIndex = scanChoice.nextLine();
		int index = Integer.valueOf(choiceIndex);
		return matchedCards.get(index);
	}

	public List<Player> getPlayersToLeft() {
		List<Player> currentGamePlayers = board.getPlayers();
		List<Player> order = new ArrayList<>();
		int indexOfCurr = currentGamePlayers.indexOf(this);

		// get all the players after the index;
		if (indexOfCurr < currentGamePlayers.size() - 1) {
			for (int i = indexOfCurr + 1; i < currentGamePlayers.size(); i++) {
				order.add(currentGamePlayers.get(i));
			}
		}
		// get all the players before the index;
		if (indexOfCurr > 0) {
			for (int i = 0; i < indexOfCurr; i++) {
				order.add(currentGamePlayers.get(i));
			}
		}

		return order;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public boolean checkValidMove(Tile dest, int roll) {
		validMove = false;
		if (board.getTileAt(xcurrent, ycurrent) instanceof HallwayTile)
			checkValidMoveHelper(board.getTileAt(xcurrent, ycurrent), new HashSet<Tile>(), 0, dest, roll);
		else
			for (DoorTile dt : ((DoorTile) board.getTileAt(xcurrent, ycurrent)).getRoom().getDoors()) {
				checkValidMoveHelper(dt, new HashSet<Tile>(), 0, dest, roll);
			}
		return validMove;

	}

	public void checkValidMoveHelper(Tile tile, Set<Tile> visited, int steps, Tile dest, int roll) {
		if (tile == dest && (steps == roll || tile instanceof DoorTile)) {
			validMove = true;
			return;
		}
		if (steps >= roll)
			return;
		List<Tile> neighbours = getTileNeighbours(tile);
		for (Tile t : neighbours) {
			if (!visited.contains(t) && (t instanceof HallwayTile || (t instanceof DoorTile && t == dest))) {
				visited.add(t);
				checkValidMoveHelper(t, visited, steps + 1, dest, roll);
				visited.remove(t);
			}
		}
	}

	private List<Tile> getTileNeighbours(Tile current) {
		List<Tile> neighbours = new ArrayList<>();

		if (current.getX() > 0) {
			neighbours.add(board.getTileAt(current.getX() - 1, current.getY())); // get the neighbour to the right
		}
		if (current.getX() < bWidth - 1) {
			neighbours.add(board.getTileAt(current.getX() + 1, current.getY())); // get the neighbour to the left
		}
		if (current.getY() > 0) {
			neighbours.add(board.getTileAt(current.getX(), current.getY() - 1)); // get the neighbour above
		}
		if (current.getY() < bHeight - 1) {
			neighbours.add(board.getTileAt(current.getX(), current.getY() + 1)); // get the neighbour below
		}
		return neighbours;
	}

	private int calculateSteps(int x, int y) {
		int changeX = Math.abs(xcurrent - x);
		int changeY = Math.abs(ycurrent - y);
		int totalSteps = changeX + changeY;
		return totalSteps;
	}

	public void clearSteps() {
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				board.getTileAt(x, y).setStepToHere(0);
			}
		}
	}

	public boolean inRoom() {
		return inRoom;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Room getRoom() {
		return room;
	}

	public String getPlayerName() {
		return name;
	}

	public Room getPlayerLocation() {
		return room;
	}

	@Override
	public String toString() {
		return "Player{" + "name='" + name + '\'' + '}';
	}
}