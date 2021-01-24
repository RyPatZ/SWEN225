import java.util.*;

public class Weapon {
	String name;
	Room room;

	public Weapon(String name) {
		this.name = name;
		room = null;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	public String toString() {
		return "The " + name + " is currently in the " + room.getName() + "."; 
	}
}
