package Game;

import Gui.GameView;

import javax.swing.*;
import java.util.*;

/**
 * Overview of the game.
 * Stores the board, all players and handles the game loop.
 */
public class Game {
    private Board board;
    private List<Player> players;
    private int currentTurn;
    private int incorrectGuesses;
    private boolean gameOver;
    private GameView gameView;

    public static final String[] allSuspects = new String[]
            {"Miss Scarlett", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
    public static final String[] allWeapons = new String[]
            {"Candlestick", "Dagger", "Lead Pipe", "Revolver", "Rope", "Spanner"};
    public static final String[] allRooms = new String[]
            {"Kitchen", "Ball Room", "Conservatory", "Dining Room", "Billiard Room", "Lounge", "Hall", "Study", "Library"};
    public static final String[] suspectImages = new String[]
            {"img/players/Miss Scarlett.png", "img/players/Colonel Mustard.png", "img/players/Mrs. White.png",
                    "img/players/Mr. Green.png", "img/players/Mrs. Peacock.png", "img/players/Professor Plum.png"};
    public static final String[] weaponImages = new String[]
            {"img/items/Candlestick.png", "img/items/Dagger.png", "img/items/Lead pipe.png",
                    "img/items/Revolver.png", "img/items/Rope.png", "img/items/Spanner.png"};

    // easy way to look up the index of a suspect in the array. Contains some aliases
    public static final Map<String, Integer> suspectAliases = new HashMap<String, Integer>() {{
        put("miss scarlett", 0);
        put("scarlett", 0);
        put("ms", 0);
        put("col mustard", 1);
        put("col. mustard", 1);
        put("mustard", 1);
        put("cm", 1);
        put("mrs. white", 2);
        put("mrs white", 2);
        put("white", 2);
        put("mw", 2);
        put("mr. green", 3);
        put("mr green", 3);
        put("green", 3);
        put("mg", 3);
        put("mrs. peacock", 4);
        put("mrs peacock", 4);
        put("peacock", 4);
        put("mp", 4);
        put("prof. plum", 5);
        put("prof plum", 5);
        put("plum", 5);
        put("pp", 5);
    }};

    private Card murderer, weapon, room;


    /**
     * Initialize a new game from the menu view
     *
     * @param characters
     */
    public Game(GameView gameView, List<String> characters, List<String> names) {
        if (characters.size() > allSuspects.length || characters.size() < 3)
            throw new IllegalArgumentException("Invalid number of players.");
        if (names.size() != characters.size())
            throw new IllegalArgumentException("There must be the same number of names as characters");

        this.gameView = gameView;

        // create the board
        board = new Board("map/", 26, 27);
        currentTurn = 0;
        gameOver = false;
        incorrectGuesses = 0;

        players = new ArrayList<>();
        List<Card> playerCards = new ArrayList<>();

        // pick the murder circumstances randomly
        // choose suspect
        int randomSuspect = (int) (Math.random() * allSuspects.length);
        for (int i = 0; i < allSuspects.length; i++) {
            Card card = new Card(allSuspects[i], Card.CardType.SUSPECT);
            if (i == randomSuspect)
                murderer = card;
            else
                playerCards.add(card);
        }
        // choose room
        int randomRoom = (int) (Math.random() * allRooms.length);
        for (int i = 0; i < allRooms.length; i++) {
            Card card = new Card(allRooms[i], Card.CardType.ROOM);
            if (i == randomRoom)
                room = card;
            else
                playerCards.add(card);
        }
        // choose weapon
        int randomWeapon = (int) (Math.random() * allWeapons.length);
        for (int i = 0; i < allWeapons.length; i++) {
            Card card = new Card(allWeapons[i], Card.CardType.WEAPON);
            if (i == randomWeapon)
                weapon = card;
            else
                playerCards.add(card);
        }

        // let players decide their characters

        List<String> charactersLeft = new ArrayList<>(Arrays.asList(allSuspects));

        // add all players
        for (int p = 0; p < characters.size(); p++) {
            int index = suspectAliases.get(characters.get(p).toLowerCase());
            String name = names.get(p);

            // add the player
            Player player = new Player(this, name, board.getSuspect(index));
            charactersLeft.remove(allSuspects[index]);
            players.add(player);
        }

        // give cards to players
        Collections.shuffle(playerCards);

        for (int i = 0, p = 0; i < playerCards.size(); i++, p++, p %= characters.size()) {
            Player player = players.get(p);
            player.addCard(playerCards.get(i));
        }

        players.get(0).turn();
    }

    /**
     * Draw the board
     */
    public void draw() {
        System.out.print("\n" + board.toString());
    }


    /**
     * Check if an accusation is correct
     */
    public boolean checkAccusation(Card suspect, Card weapon, Card room) {
        return suspect.equals(this.murderer) && weapon.equals(this.weapon) && room.equals(this.room);
    }


    /**
     * Check a suggestion by asking each player
     * @param suspect
     * @param weapon
     * @param room
     */
    public void checkSuggestion(Card suspect, Card weapon, Card room) {
        // move the weapon and suspect to the room
        Suspect otherSuspect = null;
        for (int i = 0; i < allSuspects.length; i++) {
            if (suspect.getName().equals(allSuspects[i]))
                otherSuspect = getBoard().getSuspect(i);
        }
        if (otherSuspect == null) {
            throw new Error("Can't find that suspect in allSuspects");
        }
        Weapon otherWeapon = null;
        for (int i = 0; i < allWeapons.length; i++) {
            if (weapon.getName().equals(allWeapons[i]))
                otherWeapon = getBoard().getWeapon(i);
        }
        if (otherWeapon == null) {
            throw new Error("Can't find that suspect in allWeapons");
        }

        Player currentPlayer = getPlayer(currentTurn);

        otherSuspect.enterRoom(currentPlayer.getSuspect().getCurrentRoom());
        otherWeapon.moveTo(currentPlayer.getSuspect().getCurrentRoom().getAvailableCell());

        gameView.repaint();


        // check with other players

        int pIndex = currentTurn;
        pIndex++;
        pIndex %= players.size();

        while (currentTurn != pIndex) {
            Player player = players.get(pIndex);

            // get list of cards they have from the suspect, weapon and room.
            List<Card> refutedCards = new ArrayList<>();
            if (player.hasCard(suspect)) refutedCards.add(suspect);
            if (player.hasCard(weapon)) refutedCards.add(weapon);
            if (player.hasCard(room)) refutedCards.add(room);

            if (refutedCards.size() == 0) {
                // player does not have that card
                gameView.showMessage(player + ": You cannot refute any of those cards");
            } else {
                // found a player with one of those cards.
                gameView.showRefutationDialog(player, refutedCards);
                nextPlayer();

                return;
            }

            pIndex++;
            pIndex %= players.size();
        }

        gameView.showMessage("No players could refute those cards.");
        nextPlayer();
    }

    /**
     * When a player accuses incorrectly, take them out of the game.
     *
     * @param player Returns if the game is still running
     */
    public boolean invalidAccusation(Player player) {
        player.setAccused();
        incorrectGuesses++;
        return incorrectGuesses < players.size();
    }


    /**
     * Get the board
     *
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get player object from index
     *
     * @param index
     * @return
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Get the game view
     * @return
     */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Move to the next player
     */
    public void nextPlayer() {

        currentTurn++;
        currentTurn %= players.size();

        Player player = players.get(currentTurn);

        if (!player.hasAccused()) {
            player.turn();
            gameView.swapPlayer(player);
            gameView.updatePlayerState();
        } else nextPlayer();

    }


    /**
     * Run the game loop
     */
    @Deprecated
    public void run() {
        while (!gameOver) {
            draw();
            Player player = players.get(currentTurn);

            if (!player.hasAccused()) {
                System.out.println(player);

                player.turn();
            }

            currentTurn++;
            currentTurn %= players.size();
        }
    }

    /**
     * Check if an accusation is correct for a given player
     */
    @Deprecated
    public void checkAccusation(Player player, Card suspect, Card weapon, Card room) {
        if (suspect.equals(this.murderer) && weapon.equals(this.weapon) && room.equals(this.room)) {
            endGame(player);
        } else {
            incorrectGuesses++;
            System.out.println("\n" + player + " has accused incorrectly, so is out of the game!");
            if (incorrectGuesses == players.size()) endGame(null);
        }
    }

    /**
     * Check a suggestion. The function assumes the person with the index currentTurn is the player that made the suggestion.
     * The first player in a clockwise direction that contains one of the suggested cards is asked to refute.
     */
    @Deprecated
    public void checkSuggestionOld(Card suspect, Card weapon, Card room) {
        Player currentPlayer = players.get(currentTurn);

        System.out.println();
        int pIndex = currentTurn;
        pIndex++;
        pIndex %= players.size();

        while (currentTurn != pIndex) {
            Player player = players.get(pIndex);

            // get list of cards they have from the suspect, weapon and room.
            List<Card> refutedCards = new ArrayList<>();
            if (player.hasCard(suspect)) refutedCards.add(suspect);
            if (player.hasCard(weapon)) refutedCards.add(weapon);
            if (player.hasCard(room)) refutedCards.add(room);

            if (refutedCards.size() == 0) {
                // player does not have that card
                System.out.println(player + " has none of the suggested cards.");
            } else {
                // found a player with one of those cards.
                System.out.println(player + ", you have at least one of the suggested cards. Pick one to refute to " + currentPlayer);

                System.out.print("Card Options: [");
                for (int i = 0; i < refutedCards.size(); i++) {
                    System.out.print(i + ": " + refutedCards.get(i));
                    if (i != refutedCards.size() - 1) System.out.print(", ");
                }
                System.out.println("]");

                int cardIndex = getNumberInput(new Scanner(System.in), 0, refutedCards.size() - 1);

                Card card = refutedCards.get(cardIndex);

                System.out.println(currentPlayer + ", " + player + " has the " + card + " card.");

                return;
            }

            pIndex++;
            pIndex %= players.size();
        }

        System.out.println(currentPlayer + ", no players have refuted those cards.");

    }

    /**
     * End the game by printing the winner and the murder circumstances
     *
     * @param correctGuess non-null will imply this person has won the game.
     */
    @Deprecated
    public void endGame(Player correctGuess) {
        if (correctGuess != null)
            System.out.println("\n" + correctGuess + " has accused correctly and has won the game!");
        else System.out.println("\nNo one managed to accuse the murder correctly, so the game is over.");
        System.out.println("\nMurderer: " + murderer.getName());
        System.out.println("Weapon: " + weapon.getName());
        System.out.println("Room: " + room.getName());

        gameOver = true;
    }

    /**
     * Get suspect card input
     *
     * @param scanner
     * @return
     */
    @Deprecated
    public static Card getSuspectInput(Scanner scanner) {
        System.out.print("Suspects: [");
        for (int i = 0; i < allSuspects.length; i++) {
            System.out.print(i + ": " + allSuspects[i]);
            if (i != allSuspects.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.print("Enter the number corresponding to the suspect you pick: ");
        int index = getNumberInput(scanner, 0, allSuspects.length - 1);

        String name = allSuspects[index];
        Card card = new Card(name, Card.CardType.SUSPECT);

        return card;
    }

    /**
     * Get weapon card input
     *
     * @param scanner
     * @return
     */
    @Deprecated
    public static Card getWeaponInput(Scanner scanner) {
        System.out.print("Weapons: [");
        for (int i = 0; i < allWeapons.length; i++) {
            System.out.print(i + ": " + allWeapons[i]);
            if (i != allWeapons.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.print("Enter the number corresponding to the weapon you pick: ");
        int index = getNumberInput(scanner, 0, allWeapons.length - 1);

        String name = allWeapons[index];
        Card card = new Card(name, Card.CardType.WEAPON);

        return card;
    }

    /**
     * Get weapon card input
     *
     * @param scanner
     * @return
     */
    @Deprecated
    public static Card getRoomInput(Scanner scanner) {
        System.out.print("Rooms: [");
        for (int i = 0; i < allRooms.length; i++) {
            System.out.print(i + ": " + allRooms[i]);
            if (i != allRooms.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.print("Enter the number corresponding to the room you pick: ");
        int index = getNumberInput(scanner, 0, allRooms.length - 1);

        String name = allRooms[index];
        Card card = new Card(name, Card.CardType.ROOM);

        return card;
    }

    /**
     * Get a number input
     *
     * @return
     */
    @Deprecated
    public static int getNumberInput(Scanner scanner, int min, int max) {
        while (true) {
            String line = scanner.nextLine();

            try {
                int choice = Integer.parseInt(line);
                if (choice < min || choice > max) {
                    System.out.print("Invalid option, Enter again: ");
                    continue;
                }

                return choice;
            } catch (NumberFormatException e) {
                System.out.print("Invalid option, Enter again: ");
            }
        }
    }
}
