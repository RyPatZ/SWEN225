package Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a room.
 * Contains the cells in the room.
 */
public class Room {
    private List<RoomEntityCell> roomCells; // list of all the places things can be put in the room.
    private List<RoomEntranceCell> roomEntrances; // list of all the entrances to the room
    private String name;

    /**
     * Create a room
     * @param name
     */
    public Room(String name) {
        this.name = name;
        roomCells = new ArrayList<>();
        roomEntrances = new ArrayList<>();
    }


    /**
     * List of all the room entrances
     * @return
     */
    public List<RoomEntranceCell> getRoomEntrances() {
        return roomEntrances;
    }

    /**
     * Returns the first free room entity cell.
     * @return
     */
    public Cell getAvailableCell() {
        for (RoomEntityCell cell : roomCells) {
        	if (cell.getEntity() == null) {
        		return cell;
        	}
        }
        return null;
    }

    /**
     * Add a Game.RoomEntityCell to the room
     * @param cell
     */
    public void addEntityCell(RoomEntityCell cell) {
        roomCells.add(cell);
    }

    /**
     * Add a Game.RoomEntranceCell to the room
     * @param cell
     */
    public void addEntranceCell(RoomEntranceCell cell) {
        roomEntrances.add(cell);
    }

    /**
     * Get the room name
     */
    public String getName() {
        return name;
    }
}
