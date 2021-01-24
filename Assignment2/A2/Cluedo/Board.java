
import java.util.*;

// line 12 "model ump"
// line 103 "model ump"
public class Board {

	Tile[][] board;

	public static final int BOARD_WIDTH = 24;
	public static final int BOARD_HEIGHT = 25;
	Map<String, Weapon> weapons;
	Map<String, Room> rooms;
	List<Player> players;

	String startingBoard =
			// ABCDEFGHIJKLMNOPQRSTUVWX
			"######### #### #########" + // 1
					"KKKKKK#   BBBB   #CCCCCC" + // 2
					"KKKKKK  BBBBBBBB  CCCCCC" + // 3
					"KKKKKK  BBBBBBBB  CCCCCC" + // 4
					"KKKKKK  BBBBBBBB   +CCCC" + // 5
					"KKKKKK  +BBBBBB+   CCCC#" + // 6
					"#KKK+K  BBBBBBBB        " + // 7
					"        B+BBBB+B       #" + // 8
					"#                 RRRRRR" + // 9
					"DDDDD             +RRRRR" + // 10
					"DDDDDDDD  #####   RRRRRR" + // 11
					"DDDDDDDD  #####   RRRRRR" + // 12
					"DDDDDDD+  #####   RRRR+R" + // 13
					"DDDDDDDD  #####        #" + // 14
					"DDDDDDDD  #####   YY+YY#" + // 15
					"DDDDDD+D  #####  YYYYYYY" + // 16
					"#         #####  +YYYYYY" + // 17
					"                 YYYYYYY" + // 18
					"#        HH++HH   YYYYY#" + // 19
					"LLLLLL+  HHHHHH         " + // 20
					"LLLLLLL  HHHHH+        #" + // 21
					"LLLLLLL  HHHHHH  S+SSSSS" + // 22
					"LLLLLLL  HHHHHH  SSSSSSS" + // 23
					"LLLLLLL  HHHHHH  SSSSSSS" + // 24
					"LLLLLL# #HHHHHH# #SSSSSS"; // 25

	public Board(Map<String, Room> rooms, List<Player> players, Map<String, Weapon> weapons) {
		this.players = players;
		this.rooms = rooms;
		this.weapons = weapons;
	}

	public void makeBoard() {
		board = new Tile[BOARD_WIDTH][BOARD_HEIGHT];
		int stringPos = 0;
		char currentKey;
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				currentKey = startingBoard.charAt(stringPos);
				stringPos++;
				switch (currentKey) {
				case ' ':
					board[x][y] = new HallwayTile(x, y, '.');
					break;
				case '#':
					board[x][y] = new EmptyTile(x, y, '#');
					break;
				case '+':
					board[x][y] = new DoorTile(x, y, '/');
					break;
				case 'D':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Dining room"));
					break;
				case 'B':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Ballroom"));
					break;
				case 'H':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Hall"));
					break;
				case 'Y':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Library"));
					break;
				case 'K':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Kitchen"));
					break;
				case 'R':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Billiard room"));
					break;
				case 'C':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Conservatory"));
					break;
				case 'L':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Lounge"));
					break;
				case 'S':
					board[x][y] = new RoomTile(x, y, ' ', rooms.get("Study"));
					break;

				}

			}

		}
		addWalls();
		updatePlayerIcons();
		AddRoomNumbers();
		AssignDoorsRooms();
	}

	public void updateBoard() {
		clearRoomTiles();
		addWalls();
		AddRoomNumbers();
		updatePlayerIcons();
	}

	private void clearRoomTiles() {
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				Tile tile = board[x][y];
				if (!(tile instanceof RoomTile))
					continue;
				tile.setIcon(' ');
			}
		}
		
	}

	private void AssignDoorsRooms() {
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				Tile tile = board[x][y];
				if (!(tile instanceof DoorTile))
					continue;
				for (Tile t : getTileNeighbours(tile)) {
					if (t instanceof RoomTile) {
						((DoorTile) tile).setRoom(((RoomTile) t).getRoom());
						System.out.println(((RoomTile) t).getRoom());
						System.out.println((DoorTile) tile);
						
						((RoomTile) t).getRoom().addDoor(((DoorTile) tile));
					}
				}
			}
		}
	}

	public List<Player> getPlayers() {
		return players;
	}
	
	public Player getPlayerFromName (String name){
		Player toReturn=null;
		for (Player p : players) {
			if(p.name.equals(name)){
				toReturn=p;
			}
		}
		return toReturn;
	}
	
	private void AddRoomNumbers() {
		this.setIconsToWord(2, 3, "1");
		this.setIconsToWord(11, 4, "2");
		this.setIconsToWord(21, 2, "3");
		this.setIconsToWord(21, 9, "4");
		this.setIconsToWord(21, 15, "5");
		this.setIconsToWord(21, 22, "6");
		this.setIconsToWord(11, 20, "7");
		this.setIconsToWord(2, 20, "8");
		this.setIconsToWord(2, 11, "9");
	}

	public void setIconsToWord(int x, int y, String word) {
		for (int i = 0; i < word.length(); i++) {
			board[x + i][y].setIcon(word.charAt(i));
		}
	}

	private void updatePlayerIcons() {
		for (Player player : players) {
			if (player.inRoom()) {
				continue;
			} else if (player.stillIngame && !(board[player.xcurrent][player.ycurrent] instanceof DoorTile)) {
				board[player.xcurrent][player.ycurrent].setIcon(player.boardIcon);
			}
		}
		for (Room room : rooms.values()) {
			if (!room.getPlayersInside().isEmpty()) {
				String playerIcons = "";
				for (Player p : room.getPlayersInside()) {
					playerIcons += p.boardIcon + " ";
				}
				playerIcons.trim();
				switch (room.getName()) {
				case "Ballroom":
					this.setIconsToWord(9, 5, playerIcons);
					break;
				case "Kitchen":
					this.setIconsToWord(1, 4, playerIcons);
					break;
				case "Dining room":
					this.setIconsToWord(1, 12, playerIcons);
					break;
				case "Lounge":
					this.setIconsToWord(1, 21, playerIcons);
					break;
				case "Hall":
					this.setIconsToWord(10, 21, playerIcons);
					break;
				case "Study":
					this.setIconsToWord(18, 23, playerIcons);
					break;
				case "Conservatory":
					this.setIconsToWord(19, 3, playerIcons);
					break;
				case "Library":
					this.setIconsToWord(18, 16, playerIcons);
					break;
				case "Billiard Room":
					this.setIconsToWord(19, 10, playerIcons);
					break;
				}

			}
		}

	}

	private void addWalls() {

		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				if (!(board[x][y] instanceof RoomTile))
					continue;
				if (x + 2 > BOARD_WIDTH
						|| !(board[x + 1][y] instanceof RoomTile || board[x + 1][y] instanceof DoorTile)) {
					board[x][y].setIcon('|');
				}
				if (x - 1 < 0 || !(board[x - 1][y] instanceof RoomTile || board[x - 1][y] instanceof DoorTile)) {
					board[x][y].setIcon('|');
				}
				if (y + 2 > BOARD_HEIGHT
						|| !(board[x][y + 1] instanceof RoomTile || board[x][y + 1] instanceof DoorTile)) {
					board[x][y].setIcon('—');
				}
				if (y - 1 < 0 || !(board[x][y - 1] instanceof RoomTile || board[x][y - 1] instanceof DoorTile)) {
					board[x][y].setIcon('—');
				}
			}

		}
	}

	public Tile getTileAt(int x, int y) {
		return board[x][y];
	}

	public String getBoard() {
		String result = "";
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWX";
		for (int h = 0; h < BOARD_WIDTH; h++) {
			result += letters.charAt(h) + " ";

		}
		result += "\n";
		for (int y = 0; y < BOARD_HEIGHT; y++) {
			for (int x = 0; x < BOARD_WIDTH; x++) {
				result += board[x][y].getIcon() + " ";
			}
			result += " " + y + "\n";
		}
		result += "Room keys:\n" + "Room 1: Kitchen\n" + "Room 2: BallRoom\n" + "Room 3: Conservatory\n"
				+ "Room 4: Billiard room\n" + "Room 5: Library\n" + "Room 6: Study\n" + "Room 7: Hall\n"
				+ "Room 8: Lounge\n" + "Room 9: Dining room\n\nWeapon locations:\n";
		for (Weapon w : weapons.values()) {
			result += w.toString() + "\n";
		}
		return result;
	}

	private List<Tile> getTileNeighbours(Tile current) {
		List<Tile> neighbours = new ArrayList<>();

		if (current.getX() > 0) {
			neighbours.add(this.getTileAt(current.getX() - 1, current.getY())); // get the neighbour to the right
		}
		if (current.getX() < BOARD_WIDTH - 1) {
			neighbours.add(this.getTileAt(current.getX() + 1, current.getY())); // get the neighbour to the left
		}
		if (current.getY() > 0) {
			neighbours.add(this.getTileAt(current.getX(), current.getY() - 1)); // get the neighbour above
		}
		if (current.getY() < BOARD_HEIGHT - 1) {
			neighbours.add(this.getTileAt(current.getX(), current.getY() + 1)); // get the neighbour below
		}
		return neighbours;
	}

}