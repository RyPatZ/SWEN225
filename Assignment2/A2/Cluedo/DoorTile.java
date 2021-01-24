
public class DoorTile implements Tile {

	boolean canMoveTo = true;
	Room entryTo;
	Player currPlayer;
	int xpos;
	int ypos;
	char icon;
	int numSteps = 0;

	public DoorTile(int x, int y, char icon) {
		this.xpos = x;
		this.ypos = y;
		this.icon = icon;

	}

	public void setRoom(Room belong) {
		entryTo = belong;
	}

	public Room getRoom() {
		return this.entryTo;
	}

	@Override
	public int getX() {
		return xpos;
	}

	@Override
	public int getY() {
		return ypos;
	}

	@Override
	public int compareTo(Tile other) {
		return 0;
	}

	@Override
	public int getStepToHere() {
		return numSteps;
	}

	@Override
	public void setStepToHere(int n) {
		this.numSteps = n;
	}

	@Override
	public void movePlayerTile(Player current) {
		this.currPlayer = current;
	}

	@Override
	public Player getPlayerTile() {
		return this.currPlayer;
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