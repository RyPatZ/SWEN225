package Game;

import Gui.Dice;

import java.util.*;

/**
 * Represents a player in the game.
 * Has an ID, a suspect that they control and a list of cards.
 */
public class Player {
    /**
     * Possible states of the player
     */
    public enum PlayerState {
        WAITING, MOVING, FINISHED, NOT_TURN
    }

    private Game game;
    private Suspect suspect;
    private Scanner scanner;
    private boolean hasAccused;
    private List<Card> cards;
    private PlayerState currentState;
    private String name;

    // movement
    private int movesLeft = 0;
    private Set<Cell> visitedCells = new HashSet<>();

    /**
     * Initialize a player object with a username
     *
     * @param game
     * @param name    the userrname
     * @param suspect
     */
    public Player(Game game, String name, Suspect suspect) {
        this.game = game;
        this.name = name;
        this.suspect = suspect;
        this.hasAccused = false;
        this.currentState = PlayerState.NOT_TURN;
        cards = new ArrayList<>();
    }

    /**
     * Add a card to your hand
     *
     * @param card
     */
    public void addCard(Card card) {
        cards.add(card);
    }


    /**
     * Get a list of cards
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Return the current state of the player
     *
     * @return
     */
    public PlayerState getCurrentState() {
        return currentState;
    }

    /**
     * Get suspect
     *
     * @return
     */
    public Suspect getSuspect() {
        return suspect;
    }

    /**
     * Get turns left
     *
     * @return
     */
    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * Have your turn
     */
    public void turn() {
        currentState = PlayerState.WAITING;
    }

    /**
     * Roll the dice
     */
    public void rollDice(Dice dice) {
        if (currentState != PlayerState.WAITING) return;

        currentState = PlayerState.MOVING;
        dice.roll();

        this.movesLeft = dice.getValue();
        visitedCells = new HashSet<>();

        game.getGameView().updatePlayerState();
    }

    /**
     * Move in a given direction
     *
     * @param direction
     */
    public void move(Cell.Direction direction) {
        if (currentState != PlayerState.MOVING) return;

        // check if move is valid
        if (suspect.getCurrentRoom() == null) {
            Set<Cell.Direction> directions = suspect.getAvaliableDirections(visitedCells);
            if (!directions.contains(direction)) return; // invalid direction

            visitedCells.add(suspect.getLocation());
            suspect.move(direction);

            move();
        }
    }

    /**
     * Get a list of all the clickable cells
     */
    public List<Cell> getAvailableCells() {
        List<Cell> cells = new ArrayList<>();

        if (suspect.getCurrentRoom() == null) {
            Set<Cell.Direction> directions = suspect.getAvaliableDirections(visitedCells);

            for (Cell.Direction direction : directions) {
                Cell neighbour = suspect.getBoard().getNeighbourCell(suspect.getLocation(), direction);
                cells.add(neighbour);
            }
        }
        else {
            cells.addAll(suspect.getAvaliableRoomExits());
        }

        return cells;
    }


    /**
     * Move to a given cell
     *
     * @param cell
     */
    public void move(Cell cell) {
        if (currentState != PlayerState.MOVING) return;

        if (visitedCells.contains(cell)) return;

        // move to an adjacent cell
        if (suspect.getCurrentRoom() == null) {
            Set<Cell.Direction> directions = suspect.getAvaliableDirections(visitedCells);

            for (Cell.Direction direction : directions) {
                Cell neighbour = suspect.getBoard().getNeighbourCell(suspect.getLocation(), direction);

                // yes valid
                if (neighbour == cell) {
                    visitedCells.add(suspect.getLocation());
                    suspect.move(direction);

                    move();

                    return;
                }
            }
        }
        // move out of a room by clicking the exit
        else if (cell instanceof RoomEntranceCell) {
            RoomEntranceCell exit = (RoomEntranceCell) cell;

            List<RoomEntranceCell> cellList = suspect.getAvaliableRoomExits();

            if (cellList.contains(exit)) {
                visitedCells.add(exit);
                suspect.exitRoom(exit);
                move();
            }
        }
    }

    /**
     * Finish of a move
     * <p>
     * will change states if all moves left are used up
     */
    private void move() {
        movesLeft--;
        // STOP MOVING
        if (suspect.getCurrentRoom() != null || movesLeft <= 0 || suspect.getAvaliableDirections(visitedCells).size() == 0) {
            currentState = PlayerState.FINISHED;
        }
        game.getGameView().repaint();
        game.getGameView().updatePlayerState();
    }

    /**
     * Finish turn
     */
    public void finishTurn() {
        if (currentState != PlayerState.FINISHED) return;

        currentState = PlayerState.NOT_TURN;
        game.nextPlayer();
    }


    /**
     * Set hasAccused to true
     */
    public void setAccused() {
        hasAccused = true;
    }

    /**
     * Has accused
     *
     * @return
     */
    public boolean hasAccused() {
        return hasAccused;
    }


    /**
     * If the player has a card
     *
     * @param card
     * @return
     */
    public boolean hasCard(Card card) {
        return cards.contains(card);
    }


    /**
     * Player string.
     *
     * @return
     */
    public String toString() {
        return name;
    }


    /**
     * Initialize a player object (OLD)
     *
     * @param game
     * @param scanner the scanner for input. Use System.in for user-input
     * @param num
     * @param suspect
     */
    @Deprecated
    public Player(Game game, Scanner scanner, int num, Suspect suspect) {
        this.game = game;
        this.name = "Player " + num;
        this.scanner = scanner;
        this.suspect = suspect;
        this.hasAccused = false;
        cards = new ArrayList<>();
    }

    /**
     * Make an accusation
     */
    @Deprecated
    public void accuse() {
        hasAccused = true;


        System.out.println("\nPick the circumstances of the murder correctly to win the game. Guess incorrectly and you will be out.\n");

        Card suspect = Game.getSuspectInput(scanner);

        System.out.println();

        Card weapon = Game.getWeaponInput(scanner);

        System.out.println();

        Card room = Game.getRoomInput(scanner);

        game.checkAccusation(this, suspect, weapon, room);
    }

    /**
     * Make a suggestion within a room
     */
    @Deprecated
    public void suggest() {
        if (suspect.getCurrentRoom() == null) throw new IllegalStateException("Cannot suggest if not in a room.");

        System.out.println("\nPick the circumstances of the murder.\n");

        Card otherSuspectCard = Game.getSuspectInput(scanner);
        Suspect otherSuspect = null;
        for (int i = 0; i < Game.allSuspects.length; i++) {
            if (otherSuspectCard.getName().equals(Game.allSuspects[i]))
                otherSuspect = suspect.getBoard().getSuspect(i);
        }
        if (otherSuspect == null) {
            throw new Error("Cant find that suspect in allSuspects");
        }

        System.out.println();

        Card weaponCard = Game.getWeaponInput(scanner);
        Weapon weapon = null;
        for (int i = 0; i < Game.allSuspects.length; i++) {
            if (weaponCard.getName().equals(Game.allWeapons[i]))
                weapon = suspect.getBoard().getWeapon(i);
        }
        if (weapon == null) {
            throw new Error("Cant find that suspect in allWeapons");
        }

        otherSuspect.enterRoom(suspect.getCurrentRoom());
        weapon.moveTo(suspect.getCurrentRoom().getAvailableCell());

        Card roomCard = new Card(suspect.getCurrentRoom().getName(), Card.CardType.ROOM);

        game.checkSuggestion(otherSuspectCard, weaponCard, roomCard);
    }

    /**
     * Let the player move a number of steps
     *
     * @param nSteps
     */
    @Deprecated
    public void move(int nSteps) {
        if (game != null) System.out.println("\nDice roll: " + nSteps);
        Set<Cell> visited = new HashSet<>();

        while (nSteps > 0) {
            if (game != null) game.draw();

            // regular move through free spaces
            if (suspect.getCurrentRoom() == null) {
                // get available directions
                Set<Cell.Direction> directions = suspect.getAvaliableDirections(visited);

                if (directions.size() == 0) {
                    if (game != null) System.out.println("You are unable to move further.");
                    break;
                }
                if (game != null) System.out.println("Moves left: " + nSteps);

                if (game != null) System.out.println("\nAvailable Directions: " + directions);
                if (game != null) System.out.print("Enter direction: ");

                // get choice
                Cell.Direction direction = Cell.Direction.getDirection(scanner.nextLine());

                while (direction == null || !directions.contains(direction)) {
                    if (game != null) System.out.print("Invalid Direction, Enter again: ");
                    direction = Cell.Direction.getDirection(scanner.nextLine());
                }

                // move
                visited.add(suspect.getLocation());
                suspect.move(direction);

                // stop moving if you reach a room
                if (suspect.getCurrentRoom() != null) {
                    break;
                }
            }
            // exit a room
            else {
                List<RoomEntranceCell> cellList = suspect.getAvaliableRoomExits();

                if (cellList.size() == 0) {
                    if (game != null) System.out.println("You are unable to exit the room as all exits are blocked.");
                    break;
                }

                // get the list of options to pick as a string
                List<String> options = new ArrayList<>();

                Cell.Direction previousDir = null;
                int doorCount = 0;

                for (RoomEntranceCell cell : cellList) {
                    // multiple doors with the same direction - add a number to the end
                    if (previousDir == cell.getDirection()) {
                        doorCount++;
                        options.add(options.size() + ": " + cell.getDirection() + " DOOR " + doorCount);
                    } else {
                        doorCount = 1;
                        previousDir = cell.getDirection();
                        options.add(options.size() + ": " + cell.getDirection() + " DOOR");
                    }

                }
                if (game != null) System.out.println("Moves left: " + nSteps);

                if (game != null) System.out.println("\nAvailable Exits: " + options);
                if (game != null) System.out.print("Enter the number corresponding to the door you want to exit: ");

                // get choice
                int choice = Game.getNumberInput(scanner, 0, cellList.size() - 1);

                // move
                RoomEntranceCell exit = cellList.get(choice);

                visited.add(exit);
                suspect.exitRoom(exit);
            }

            nSteps--;
        }

        if (game != null) game.draw();
        if (game != null) System.out.println("Movement finished.");
    }
}
