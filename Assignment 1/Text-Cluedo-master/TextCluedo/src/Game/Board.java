package Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Overview of the board.
 * Contains all the cells on the board and all the entities.
 */
public class Board {
    private static final String roomsFileName = "rooms.txt";
    private static final String freeSpacesFileName = "free spaces.txt";
    private static final String startingPlacesFileName = "starting places.txt";
    private static final String boardFileName = "board.txt";

    private final int width;
    private final int height;
    private Cell[][] cells;
    private List<Suspect> allSuspects;
    private List<Room> allRooms;
    private List<Weapon> allWeapons;

    /**
     * Initialize the board by loading from a file.
     */
    public Board(String dir, int width, int height) {
        this.width = width;
        this.height = height;

        // load rooms
        File roomsFile = new File(dir + roomsFileName);
        File freeSpacesFile = new File(dir + freeSpacesFileName);
        File startingPlacesFile = new File(dir + startingPlacesFileName);
        File boardFile = new File(dir + boardFileName);

        // load board image
        BufferedImage image;
        try {
            image = ImageIO.read(new File("img/board.png"));
        } catch (IOException e) {
            throw new Error("Cant load board image");
        }

        // make lists
        allRooms = new ArrayList<>();
        allWeapons = new ArrayList<>();
        allSuspects = new ArrayList<>();

        // creates all cells
        cells = new Cell[height][width];

        // read the board and assume all the cells are wall. Cells that are not walls will be changed latter.
        try {
            BufferedReader boardReader = new BufferedReader(new FileReader(boardFile));

            String line;

            // process the board string
            for (int y = 0; y < height; y++) {
                // skip lines that are comments
                {
                    line = boardReader.readLine();
                }
                while (line.startsWith("#")) ;

                for (int x = 0; x < width; x++) {
                    char[] chars = {line.charAt(x * 2), line.charAt(x * 2 + 1)};
                    cells[y][x] = new WallCell(x, y, chars, image.getSubimage(x * 16, y * 16, 16, 16));
                }
            }
        } catch (IOException e) {
            throw new Error(e);
        }

        // load all the rooms
        try {
            Scanner roomsScanner = new Scanner(roomsFile);
            Room room = null;
            while (roomsScanner.hasNext()) {
                String token = roomsScanner.next();
                // skip comments
                if (token.startsWith("#")) {
                    roomsScanner.nextLine();
                    continue;
                } else if (token.equals("name")) {
                    roomsScanner.next(); // skipping '='
                    room = new Room(roomsScanner.nextLine()); // name
                    allRooms.add(room);
                } else if (room == null) {
                    throw new Error("You must give a name for the room before anything else");
                } else if (token.equals("entity")) {
                    roomsScanner.next(); // skipping '='
                    // get the cell from the board and replace it with a room entity cell.
                    int x = roomsScanner.nextInt();
                    int y = roomsScanner.nextInt();
                    Cell oldCell = cells[y][x];
                    RoomEntityCell newCell = new RoomEntityCell(room, x, y, oldCell.getChars(), image.getSubimage(x * 16, y * 16, 16, 16));
                    room.addEntityCell(newCell);
                    cells[y][x] = newCell;
                } else if (token.equals("door")) {
                    roomsScanner.next(); // skipping '='
                    // get the old cell and replace it with a Game.Room Game.Entity cell
                    int x = roomsScanner.nextInt();
                    int y = roomsScanner.nextInt();
                    String directionAsString = roomsScanner.next();
                    Cell.Direction direction;
                    if (directionAsString.equals("up")) {
                        direction = Cell.Direction.UP;
                    } else if (directionAsString.equals("down")) {
                        direction = Cell.Direction.DOWN;
                    } else if (directionAsString.equals("left")) {
                        direction = Cell.Direction.LEFT;
                    } else if (directionAsString.equals("right")) {
                        direction = Cell.Direction.RIGHT;
                    } else {
                        throw new Error("Unknown direction in room file: " + directionAsString);
                    }
                    Cell oldCell = cells[y][x];
                    RoomEntranceCell newCell = new RoomEntranceCell(room, x, y, oldCell.getChars(), direction, image.getSubimage(x * 16, y * 16, 16, 16));
                    room.addEntranceCell(newCell);
                    cells[y][x] = newCell;
                } else {
                    throw new Error("Unknown token in rooms File: " + token);
                }
            }

            roomsScanner.close();

        } catch (IOException e) {
            throw new Error(e);
        }

        // Read all the free spaces and replace all the walls that should be free spaces with free spaces.
        try {
            Scanner freeSpaceScanner = new Scanner(freeSpacesFile);
            int x = 0;
            int y = 0;
            while (freeSpaceScanner.hasNext()) {
                String token = freeSpaceScanner.next();
                // skip comments
                if (token.startsWith("#")) {
                    freeSpaceScanner.nextLine();
                    continue;
                } else if (token.equals(".")) {
                    cells[y][x] = new FreeCell(x, y, image.getSubimage(x * 16, y * 16, 16, 16));
                } else if (!token.equals("X")) {
                    throw new Error("Got unknown token in Free space file");
                }

                x++;
                if (x >= width) {
                    y++;
                    x = 0;
                }
            }
            freeSpaceScanner.close();

        } catch (IOException e) {
            throw new Error(e);
        }

        // read all the suspects and place them onto the board
        try {
            Scanner startingPlacesScanner = new Scanner(startingPlacesFile);
            while (startingPlacesScanner.hasNext()) {
                String token = startingPlacesScanner.next();
                // skip comments
                if (token.startsWith("#")) {
                    startingPlacesScanner.nextLine();
                    continue;
                }

                int x = Integer.parseInt(token);
                int y = startingPlacesScanner.nextInt();
                String initials = startingPlacesScanner.next();
                char[] chars = {initials.charAt(0), initials.charAt(1)};

                int index = Game.suspectAliases.get(initials.toLowerCase());
                String fileName = Game.suspectImages[index];

                Suspect suspect = new Suspect(this, x, y, chars, fileName);
                allSuspects.add(suspect);
            }
            startingPlacesScanner.close();

        } catch (IOException e) {
            throw new Error(e);
        }

        // place a each weapon into a random room until all the rooms are full or your out of weapons.
        String[] allRemainingWeapons = {"CS", "Da", "LP", "Re", "Ro", "Sp"}; //Game.Game.allWeapons.clone();
        List<Room> allRoomsShuffled = new ArrayList<>(allRooms);
        Collections.shuffle(allRoomsShuffled);
        for (int i = 0; i < allRemainingWeapons.length && i < allRoomsShuffled.size(); i++) {
            allWeapons.add(new Weapon(this, allRoomsShuffled.get(i), allRemainingWeapons[i].toCharArray(), Game.weaponImages[i]));
        }
    }

    /**
     * Get a set of all the available directions coming from this cell
     *
     * @param cell
     * @param exceptions Cells that are not allowed to be visited
     * @return
     */
    public Set<Cell.Direction> getAvailableNeighbours(Cell cell, Set<Cell> exceptions) {
        Set<Cell.Direction> directions = new HashSet<>();

        for (Cell.Direction d : Cell.Direction.values()) {
            Cell neighbour = getNeighbourCell(cell, d);
            if (neighbour == null) continue; // if neighbour exists
            if (exceptions.contains(neighbour)) continue; // if neighbour is not in exceptions
            if (!neighbour.isFree()) continue; // if neighbour is free
            if (neighbour instanceof RoomEntranceCell) { // check room entrance is correct orientation
                RoomEntranceCell ent = (RoomEntranceCell) neighbour;
                if(d.reverse() != ent.getDirection()) continue;
            }
            directions.add(d);
        }

        return directions;
    }


    /**
     * Get the cell in the given direction
     * Null if there is no cell in that direction
     *
     * @param direction
     * @return
     */
    public Cell getNeighbourCell(Cell cell, Cell.Direction direction) {
        int x = cell.getX();
        int y = cell.getY();

        switch (direction) {
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
            case UP:
                y--;
                break;
            case DOWN:
                y++;
                break;
        }

        if (x < 0 || x >= width || y < 0 || y >= height) return null;
        else return cells[y][x];
    }

    /**
     * Get the cell at a position
     */
    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Invalid position on the board.");
        return cells[y][x];
    }


    /**
     * The entire board as a string
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                sb.append(cell.getChars()[0]);
                sb.append(cell.getChars()[1]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Get a suspect at an index
     *
     * @param index
     * @return
     */
    public Suspect getSuspect(int index) {
        return allSuspects.get(index);
    }

    /**
     * Get a suspect at an index
     *
     * @param index
     * @return
     */
    public Weapon getWeapon(int index) {
        return allWeapons.get(index);
    }
}
