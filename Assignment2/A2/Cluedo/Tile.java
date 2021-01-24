import java.util.*;

public interface Tile extends Comparable<Tile>{

	public int getX();


	public int getY();

	@Override
	public String toString();

	public int compareTo(Tile other);


	public int getStepToHere();
	
	public char getIcon();
	
	public void setIcon(char icon);

	public void setStepToHere(int n); // set the number of steps to a tile
	


	public void movePlayerTile(Player current);

	public Player getPlayerTile();
}
