/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.1.5054.a63bc4685 modeling language!*/



// line 64 "model.ump"
// line 101 "model.ump"
public class Wardrobe extends GameFixture
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wardrobe Attributes
  private boolean locked;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wardrobe(String aFixtureDescription)
  {
    super(aFixtureDescription);
    locked = true;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLocked(boolean aLocked)
  {
    boolean wasSet = false;
    locked = aLocked;
    wasSet = true;
    return wasSet;
  }

  public boolean getLocked()
  {
    return locked;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isLocked()
  {
    return locked;
  }

  public void delete()
  {
    super.delete();
  }

  // line 69 "model.ump"
  public String getExtraFixtureDescription(){
    String des = "It is " + doorDescription();
    return des;
  }

  // line 73 "model.ump"
   private String doorDescription(){
    String doorDes = null;
    if (locked){doorDes = "locked";}
    else {doorDes = "unlocked";}
    return doorDes;
  }


  public String toString()
  {
    return super.toString() + "["+
            "locked" + ":" + getLocked()+ "]";
  }
}