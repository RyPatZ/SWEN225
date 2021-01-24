import java.awt.Color;

/**
 * Represents a weapon in the game
 * @author Cameron Rose & Lucas Westenra
 * @version 1.0
 */

public class Weapon
{
  private String name;
  private Tile position;
  private char piece;
  Color weaponColor;
  
  public Weapon(String name, Tile position, char piece){
    this.name=name;
    this.position=position;
    this.piece=piece;
  }

  public void setName(String name){
    this.name = name;
  }
  
  public void setPosition(Tile position){
	this.position=position;
  }
  
  public void setPiece(char piece) {
	  this.piece=piece;
  }
  
  public String getName(){
	    return name;
  }
  
  public Tile getPosition(){
    return position;
  }
  
  public char getPiece() {
	  return piece;
  }

  public String toString()
  {
    return "[" + "name" + ":" + getName()+ "," + "position:" + position.toString() + "]"; 
  }
}
