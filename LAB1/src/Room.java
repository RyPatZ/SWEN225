/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.5054.a63bc4685 modeling language!*/


import java.util.*;

// line 2 "model.ump"
// line 85 "model.ump"
public class Room
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Room Associations
  private List<GameFixture> fixtures;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Room()
  {
    fixtures = new ArrayList<GameFixture>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public GameFixture getFixture(int index)
  {
    GameFixture aFixture = fixtures.get(index);
    return aFixture;
  }

  public List<GameFixture> getFixtures()
  {
    List<GameFixture> newFixtures = Collections.unmodifiableList(fixtures);
    return newFixtures;
  }

  public int numberOfFixtures()
  {
    int number = fixtures.size();
    return number;
  }

  public boolean hasFixtures()
  {
    boolean has = fixtures.size() > 0;
    return has;
  }

  public int indexOfFixture(GameFixture aFixture)
  {
    int index = fixtures.indexOf(aFixture);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfFixtures()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addFixture(GameFixture aFixture)
  {
    boolean wasAdded = false;
    if (fixtures.contains(aFixture)) { return false; }
    fixtures.add(aFixture);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeFixture(GameFixture aFixture)
  {
    boolean wasRemoved = false;
    if (fixtures.contains(aFixture))
    {
      fixtures.remove(aFixture);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addFixtureAt(GameFixture aFixture, int index)
  {  
    boolean wasAdded = false;
    if(addFixture(aFixture))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFixtures()) { index = numberOfFixtures() - 1; }
      fixtures.remove(aFixture);
      fixtures.add(index, aFixture);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveFixtureAt(GameFixture aFixture, int index)
  {
    boolean wasAdded = false;
    if(fixtures.contains(aFixture))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfFixtures()) { index = numberOfFixtures() - 1; }
      fixtures.remove(aFixture);
      fixtures.add(index, aFixture);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addFixtureAt(aFixture, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    fixtures.clear();
  }

  // line 8 "model.ump"
   public String getRoomDescription(){
    String des = "I'm in a room. I see:\n";
    for (GameFixture fixture : fixtures ){
    des += fixture.getFullFixtureDescription() + "\n";
    }
  return des;
  }

  // line 15 "model.ump"
   public void playScenario(){
    TreasureChest fixture = new TreasureChest("A large and heavy treasure chest.");
  addFixture(fixture);
  Wardrobe war = new Wardrobe("A Wardrobe");
  addFixture(war);
  System.out.println(getRoomDescription());
  fixture.setOpen(true);
  war.setLocked(false);
  System.out.println(getRoomDescription());
  }

  // line 26 "model.ump"
   public static  void main(String... args){
    new Room().playScenario();
  }

}