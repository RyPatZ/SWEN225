import java.util.*;

public class Game {

	// ------------------------
	// MEMBER VARIABLES
	// ------------------------

	// Game Attributes
	private List<Card> deck;
	private Map<String, Card> deckCopy;
	CardSet murderCards;
	boolean playing = true;
	private Board board;

	// Game Associations
	private List<Player> players;
	private Map<String, Room> rooms;
	private Map<String, Weapon> weapons;
	private int numOfPlayers;

	public Game(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;

	}

	public Map<String, Card> getDeck() {
		return deckCopy;
	}

	public void initialiseGame() {

		setupWeapons();
		SetupRooms();
		AssignPlayers();
		cardSetup();
		setMurderCards();
		setPlayersCards();
		GenerateBoard();

		for (Player p : players) {
			p.setGame(this);
		}
	}

	private void setupWeapons() {
		weapons = new HashMap<>();

		weapons.put("Candlestick", new Weapon("Candlestick"));
		weapons.put("Lead pipe", new Weapon("Lead pipe"));
		weapons.put("Revolver", new Weapon("Revolver"));
		weapons.put("Rope", new Weapon("Rope"));
		weapons.put("Spanner", new Weapon("Spanner"));
		weapons.put("Dagger", new Weapon("Dagger"));

	}

	private void SetupRooms() {
		rooms = new HashMap<>();

		// Add all rooms to the map of rooms
		rooms.put("Ballroom", new Room("Ballroom"));
		rooms.put("Kitchen", new Room("Kitchen"));
		rooms.put("Dining room", new Room("Dining room"));
		rooms.put("Lounge", new Room("Lounge"));
		rooms.put("Hall", new Room("Hall"));
		rooms.put("Study", new Room("Study"));
		rooms.put("Conservatory", new Room("Conservatory"));
		rooms.put("Library", new Room("Library"));
		rooms.put("Billiard room", new Room("Billiard room"));

		List<String> keys = new ArrayList<String>(rooms.keySet());
		List<Weapon> weaponsList = new ArrayList<Weapon>(weapons.values());
		Random rand = new Random();
		Room room = null;
		for (int i = 0; i < 6; i++) {

			while (room == null || room.getWeapons().size() > 0) {
				String key = keys.get(rand.nextInt(keys.size()));
				room = rooms.get(key);
			}
			room.addWeapon(weaponsList.get(i));
			weaponsList.get(i).setRoom(room);
		}
	}

	private int diceRoll() {
		Random rn = new Random();
		return 1 + rn.nextInt(6 - 1 + 1) + 1 + rn.nextInt(6 - 1 + 1);
	}

	public void play() {
		Scanner sc = new Scanner(System.in);
		int stillInGameCount = 0;
		while (playing) {
			stillInGameCount = 0;
			for (Player p : players) {
				if (playing) {
					nextTurn(p, sc);
					stillInGameCount++;
				}

			}
			if (stillInGameCount == 0) {
				playing = false;
				System.out.println("No players are left! You all lose :(");
			}

		}
	}

	private void nextTurn(Player p, Scanner sc) { // still working on
		
		if (!p.stillIngame) {
			System.out.println("Player " + p.getPlayerName() + " is out of the game!");
			return;
		}
		askForPlayer(p);
		System.out.println("Player " + p.getPlayerName() + "'s (" + p.boardIcon + ") Turn to Play. \n");
		System.out.println(board.getBoard());
		System.out.println(p.getCardsAsString());
		int moves = diceRoll(); // keeps track of how many moves the player has
		System.out.println("You rolled a " + moves + " Enter a Position in this format : (a1)");
		String moveTo = sc.nextLine();
		moveTo.trim();
		moveTo.toLowerCase();
		while (moveTo.length() > 3 || moveTo.length() < 2 || (!Character.isLetter(moveTo.charAt(0)))
				|| (!Character.isDigit(moveTo.charAt(1)) || (!Character.isDigit(moveTo.charAt(moveTo.length() - 1))))) {
			System.out.println("You rolled a " + moves + " Enter a Position in this format : (a1)");
			moveTo = sc.nextLine();
		}
		int x = moveTo.charAt(0) - 97; // ascii to int: a -> 0, b -> 1 ...
		int y = Integer.parseInt(moveTo.substring(1, moveTo.length()));
		System.out.println(x + " " + y);
		while (!p.move(board, x, y, moves, players)) {
			System.out.println("Enter a Position in this format : (a1)");
			moveTo = sc.nextLine();
			x = moveTo.charAt(0) - 97; // ascii to int: a -> 0, b -> 1 ...
			y = Integer.parseInt(moveTo.substring(1, moveTo.length()));
		}
		board.updateBoard();
		System.out.println(board.getBoard());

		if (p.inRoom) {
			p.suggestionOrSAccusation();
			checkWin(p);
		}
		System.out.println("End turn? (y/n)");
		while (!sc.next().equals("y")) {
			System.out.println("End turn? (y/n)");
		}

	}

	public void askForPlayer(Player p) {
		for (int i = 0; i < 80; ++i)
			System.out.println();
		System.out.println("Only " + p.name + " should be looking at the screen now. Are you " + p.name + "? (y/n)");
		Scanner sc = new Scanner(System.in);
		boolean correctPlayer = false;
		if (sc.next().equals("y")) {
			correctPlayer = true;
		}
		while (!correctPlayer) {
			System.out
					.println("Only " + p.name + " should be looking at the screen now. Are you " + p.name + "? (y/n)");
			if (sc.next().equals("y")) {
				correctPlayer = true;
			}
		}
		for (int i = 0; i < 80; ++i)
			System.out.println();
	}

	private void checkWin(Player p) {
		if (!p.stillIngame && p.CorrectAccusation) {
			playing = false;
		}
	}

	private void setPlayersCards() {
		while (!deck.isEmpty()) {
			for (Player player : players) {
				if (!deck.isEmpty()) {
					player.addCard(deck.get(0));
					deck.remove(0);
				} else
					return;
			}
		}
	}

	private void GenerateBoard() {
		board = new Board(rooms, players, weapons);
		board.makeBoard();
		for (Player player : players) {
			player.setBoard(board);
		}
		System.out.println(board.getBoard());

	}

	private void cardSetup() {
		deck = new ArrayList<>();
		// add all person cards to the deck
		deck.add(new PersonCard("Miss Scarlett"));
		deck.add(new PersonCard("Colonel Mustard"));
		deck.add(new PersonCard("Mrs. White"));
		deck.add(new PersonCard("Mr. Green"));
		deck.add(new PersonCard("Mrs. Peacock"));
		deck.add(new PersonCard("Professor Plum"));

		// add all room cards to the deck
		deck.add(new RoomCard("Hall", rooms.get("Hall")));
		deck.add(new RoomCard("Lounge", rooms.get("Lounge")));
		deck.add(new RoomCard("Dining room", rooms.get("Dining room")));
		deck.add(new RoomCard("Kitchen", rooms.get("Kitchen")));
		deck.add(new RoomCard("Ballroom", rooms.get("Ballroom")));
		deck.add(new RoomCard("Conservatory", rooms.get("Conservatory")));
		deck.add(new RoomCard("Billiard room", rooms.get("Billiard room")));
		deck.add(new RoomCard("Library", rooms.get("Library")));
		deck.add(new RoomCard("Study", rooms.get("Study")));

		// add all weapon cards to the deck
		deck.add(new WeaponCard("Candlestick", weapons.get("Candlestick")));
		deck.add(new WeaponCard("Dagger", weapons.get("Dagger")));
		deck.add(new WeaponCard("Lead pipe", weapons.get("Lead pipe")));
		deck.add(new WeaponCard("Revolver", weapons.get("Revolver")));
		deck.add(new WeaponCard("Rope", weapons.get("Rope")));
		deck.add(new WeaponCard("Spanner", weapons.get("Spanner")));

		// copy the deck to a map
		deckCopy = new HashMap<>();
		for (Card c : deck) {
			deckCopy.put(c.getName(), c);
		}

		// randomize the order of the cards;
		Collections.shuffle(deck);
	}

	private void setMurderCards() {
		PersonCard pCard = null;
		WeaponCard wCard = null;
		RoomCard rCard = null;
		for (Card person : deck) {
			if (person instanceof PersonCard) {
				pCard = (PersonCard) person;
				deck.remove(person);
				break;
			}
		}
		for (Card weapon : deck) {
			if (weapon instanceof WeaponCard) {
				wCard = (WeaponCard) weapon;
				deck.remove(weapon);
				break;
			}
		}
		for (Card room : deck) {
			if (room instanceof RoomCard) {
				rCard = (RoomCard) room;
				deck.remove(room);
				break;
			}
		}
		murderCards = new CardSet(wCard, rCard, pCard);
	}

	private void AssignPlayers() { // Starting positions need proper numbers
		players = new ArrayList<>();
		switch (numOfPlayers) {
		case 3:
			players.add(new Player("Miss Scarlett", 'S', 7, 24, murderCards));
			players.add(new Player("Colonel Mustard", 'M', 0, 17, murderCards));
			players.add(new Player("Mrs. White", 'W', 9, 0, murderCards));

			break;
		case 4:
			players.add(new Player("Miss Scarlett", 'S', 7, 24, murderCards));
			players.add(new Player("Colonel Mustard", 'M', 0, 17, murderCards));
			players.add(new Player("Mrs. White", 'W', 9, 0, murderCards));
			players.add(new Player("Mr. Green", 'G', 14, 0, murderCards));

			break;
		case 5:
			players.add(new Player("Miss Scarlett", 'S', 7, 24, murderCards));
			players.add(new Player("Colonel Mustard", 'M', 0, 17, murderCards));
			players.add(new Player("Mrs. White", 'W', 9, 0, murderCards));
			players.add(new Player("Mr. Green", 'G', 14, 0, murderCards));
			players.add(new Player("Mrs. Peacock", 'C', 23, 19, murderCards));
			break;
		case 6:
			players.add(new Player("Miss Scarlett", 'S', 7, 24, murderCards));
			players.add(new Player("Colonel Mustard", 'M', 0, 17, murderCards));
			players.add(new Player("Mrs. White", 'W', 9, 0, murderCards));
			players.add(new Player("Mr. Green", 'G', 14, 0, murderCards));
			players.add(new Player("Mrs. Peacock", 'C', 23, 6, murderCards));
			players.add(new Player("Professor Plum", 'P', 23, 19, murderCards));
		}
	}

	public Map<String, Room> getRooms() {
		return rooms;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public Map<String, Weapon> getWeapons() {
		return weapons;
	}

}
