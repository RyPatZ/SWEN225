import java.util.*;

public class CardSet {

	// CardSet Attributes
	private WeaponCard weapon;
	private RoomCard room;
	private PersonCard person;

	public CardSet(WeaponCard aWeapon, RoomCard aRoom, PersonCard aPerson) {
		weapon = aWeapon;
		room = aRoom;
		person = aPerson;
	}
	
	public WeaponCard getWeapon() {
		return weapon;
	}

	public RoomCard getRoom() {
		return room;
	}

	public PersonCard getPerson() {
		return person;
	}

}