import java.util.*;


public class WeaponCard extends Card
{

  //WeaponCard Associations
  private List<Player> players;
  Weapon weapon;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WeaponCard(String aName, Weapon weapon)
  {
    super(aName);
    players = new ArrayList<Player>();
    this.weapon = weapon;
    }

    public Weapon getWeapon(){
      return weapon;
    }

  }