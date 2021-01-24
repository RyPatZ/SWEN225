
import java.util.*;

public class Room {

	List<Player> playersInside;
	List<Weapon> weapons;
	private String name;
	private List<DoorTile> doors;

	// ------------------------
	// CONSTRUCTOR
	// ------------------------

	public Room(String name) {
		this.name = name;
		playersInside = new ArrayList<>();
		doors = new ArrayList<>();
		weapons = new ArrayList<>();
	}

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	

	public void addDoor(DoorTile dt) {
		doors.add(dt);
	}
	
	public String getName() {
		return name;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}
	
	public void addPlayerToRoom(Player p) {
		playersInside.add(p);
		System.out.println("added player to " + name);
	}
	public void removePlayerFromRoom(Player p) {
		playersInside.remove(p);
	}

	public List<Player> getPlayersInside() {
		return playersInside;
	}

	public List<DoorTile> getDoors() {
		return doors;
	}
	
	public String toString() {
		return name;
	}

	public void removeWeapon(Weapon wep) {
		weapons.remove(wep);
		
	}

}