
public class RoomTile implements Tile {

	boolean canMoveTo = false;
	int xpos;
	int ypos;
	char icon;
	Room belongsTo;

	public RoomTile(int x, int y, char icon, Room r) {
		this.xpos = x;
		this.ypos = y;
		this.icon = icon;
		belongsTo = r;

	}

	@Override
	public int getX() {
		return xpos;
	}

	@Override
	public int getY() {
		return ypos;
	}

	public Room getRoom() {
		return this.belongsTo;
		
	}

	@Override
	public int compareTo(Tile other) {
		return 0;
	}

	@Override
	public int getStepToHere() {
		return -1;
	}

	@Override
	public void setStepToHere(int n) {
		return;
	}

	@Override
	public void movePlayerTile(Player current) {
		return;
	}

	@Override
	public Player getPlayerTile() {
		return null;
	}

	@Override
	public char getIcon() {
		return icon;
	}
	@Override
	public void setIcon(char icon) {
		this.icon = icon;
		
	}
}
