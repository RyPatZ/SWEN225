
public class EmptyTile implements Tile {
	boolean canMoveTo = false;
	int xpos;
	int ypos;
	char icon;

	public EmptyTile(int x, int y, char icon) {
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
		return 0;
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