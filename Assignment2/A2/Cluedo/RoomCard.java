
import java.util.*;

public class RoomCard extends Card
{

  //RoomCard Associations
  private List<Player> players;
private Room room;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public RoomCard(String aName, Room room)
  {
    super(aName);
    this.room = room;
   
  }
  
  public Room getRoom() {
	  return room;
  }
}