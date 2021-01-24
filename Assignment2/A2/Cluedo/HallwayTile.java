
public class HallwayTile implements Tile {
	boolean canMoveTo = true;
	int xpos;
	int ypos;
	char icon;
	int numSteps = 0;

	public HallwayTile(int x, int y, char icon) {
		this.xpos = x;
		this.ypos = y;
		this.icon = icon;

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
		numSteps=n;
	}

	@Override
	public void movePlayerTile(Player current) {

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
