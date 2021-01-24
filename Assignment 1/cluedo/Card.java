/**
 * Represents a single card in the game
 * @author Cameron Rose & Lucas Westenra
 * @version 1.0
 */

public class Card
{
  private String name;
  private String type;
  private Player player;

  public Card(String name, String type)
  {
    this.name = name;
    this.type = type;
  }
  
  public void setPlayer(Player player) {
	  this.player = player;
  }
  
  public Player getPlayer() {
	  return this.player;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public String getType()
  {
    return type;
  }

  public String toString()
  {
    return  "["+ "name" + ":" + getName()+ "," +
            "type" + ":" + getType()+ "]";
  }
}
