import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import org.junit.Test;

public class CluedoTests {
		
	@Test
	public void testInvalidMoveThroughWall() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			board.movePlayer(p, Board.dir.NORTH);
			String oldState = board.getBoardState();
			assertFalse(board.movePlayer(p, Board.dir.WEST));
			String newState = board.getBoardState();
			assertEquals(oldState, newState);
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testValidMoveThroughDoor() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertFalse(oldState.equals(newState));
			
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testInvalidMoveOffMap() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			String oldState = board.getBoardState();
			assertFalse(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertEquals(oldState, newState);
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testInvalidMoveOntoInaccessible() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			String oldState = board.getBoardState();
			assertFalse(board.movePlayer(p, Board.dir.EAST));
			assertFalse(board.movePlayer(p, Board.dir.WEST));
			String newState = board.getBoardState();
			assertEquals(oldState, newState);
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testInvalidMoveIntoPlayer() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<7; i++) board.movePlayer(p, Board.dir.NORTH);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertFalse(board.movePlayer(p, Board.dir.WEST));
			String newState = board.getBoardState();
			assertTrue(oldState.equals(newState));
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testValidMoveIntoWeapon() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			for(int i=0;i<4;i++) board.movePlayer(p, Board.dir.SOUTH);
			for(int i=0;i<4;i++) board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertTrue(board.movePlayer(p, Board.dir.WEST));
			String newState = board.getBoardState();
			assertFalse(oldState.equals(newState));
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testInvalidMoveOntoRepeatTile() {
		Board board = initializeBoard(5);
		try {
			Player p = board.getPlayers().get(0);
			board.movePlayer(p, Board.dir.NORTH);
			String oldState = board.getBoardState();
			assertFalse(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertEquals(oldState, newState);
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testValidHandsize_1() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			assertEquals(6, p.getHand().size());
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testValidHandsize_2() {
		Board board = initializeBoard(6);
		try {
			Player p = board.getPlayers().get(0);
			assertEquals(3, p.getHand().size());
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testCannotMove() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.EAST);
			board.movePlayer(p, Board.dir.SOUTH);
			assertFalse(board.movePlayer(p, Board.dir.NORTH));
			assertFalse(board.movePlayer(p, Board.dir.EAST));
			assertFalse(board.movePlayer(p, Board.dir.SOUTH));
			assertFalse(board.movePlayer(p, Board.dir.WEST));
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testDiceRoll() {
		Board board = initializeBoard(3);
		try {
			for(int i=0;i<100;i++) {
				int roll = board.rollDice();
				assert(roll >= 2 && roll <=12);
			}
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testSuggestionMovesPlayer() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertFalse(oldState.equals(newState));
			board.debugSuggestion(p, board.getPlayers().get(3), board.getWeapons().get(3));
			assertFalse(board.movePlayer(p, Board.dir.SOUTH));
			assertTrue(board.getPlayers().get(3).getPosition().getName().equals("Lounge"));
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testSuggestionDoesntStackPlayers() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertFalse(oldState.equals(newState));
			board.debugSuggestion(p, board.getPlayers().get(3), board.getWeapons().get(3));
			board.debugSuggestion(p, board.getPlayers().get(4), board.getWeapons().get(4));
			assertFalse(board.movePlayer(p, Board.dir.WEST));
			assertTrue(board.getPlayers().get(4).getPosition().getName().equals("Lounge"));
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testSuggestionDoesntMoveSelf() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			String oldState = board.getBoardState();
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			String newState = board.getBoardState();
			assertFalse(oldState.equals(newState));
			board.debugSuggestion(p, board.getPlayers().get(0), board.getWeapons().get(3));
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testSuggestionMovesWeapon() {
		Board board = initializeBoard(3);
		try {
			List<Weapon> weps = board.getWeapons();
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			board.debugSuggestion(p, board.getPlayers().get(0), weps.get(3));
			assertTrue(weps.get(3).getPosition().getName().equals("Lounge"));
			board.movePlayer(p, Board.dir.SOUTH);
			assertTrue(p.getPosition().weapon == weps.get(3));
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testSuggestionMovesWeaponNotOntoPlayer() {
		Board board = initializeBoard(3);
		try {
			List<Weapon> weps = board.getWeapons();
			Player p = board.getPlayers().get(0);
			for(int i=0; i<6; i++) board.movePlayer(p, Board.dir.NORTH);
			board.movePlayer(p, Board.dir.WEST);
			assertTrue(board.movePlayer(p, Board.dir.SOUTH));
			board.debugSuggestion(p, board.getPlayers().get(1), weps.get(3));
			assertTrue(weps.get(3).getPosition().getName().equals("Lounge"));
			board.movePlayer(p, Board.dir.WEST);
			assertTrue(p.getPosition().weapon == weps.get(3));
			
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testAccusationWin() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			List<Card> mc = board.getMurderCards();
			assertTrue(board.debugAccusation(p, mc));
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testGameOverWin() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			List<Card> mc = board.getMurderCards();
			assertTrue(board.debugAccusation(p, mc));
			assertTrue(board.gameOver);
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testAccusationLoss() {
		Board board = initializeBoard(3);
		try {
			Player p = board.getPlayers().get(0);
			List<Card> mc = new ArrayList<Card>();
			for(int i=0;i<3;i++) mc.add(new Card("name", "player"));
			assertFalse(board.debugAccusation(p, mc));
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	@Test
	public void testGameOverLoss() {
		Board board = initializeBoard(3);
		try {
			List<Player> p = board.getPlayers();
			List<Card> mc = new ArrayList<Card>();
			for(int i=0;i<3;i++) mc.add(new Card("name", "player"));
			for(int i=0;i<3;i++)
				assertFalse(board.debugAccusation(p.get(i), mc));
			assertTrue(board.runGame());
		} catch (NullPointerException e) {
			System.out.println("There was an error. NullPointer Exception. "+e);
		}
	}
	
	//Initialization Methods
	
	private Board initializeBoard(int numPlayers) {
		Tile[][] tiles = new Tile[24][25];
		  List<Player> players = new ArrayList<Player>();
		  List<Weapon> weapons = new ArrayList<Weapon>();
		  List<Card> allCards = new ArrayList<Card>();
		  List<Card> cards = new ArrayList<Card>();
		  List<Card> murderCards = new ArrayList<Card>();
		  List<Door> doors = new ArrayList<Door>();
		  
		  for(int x=0; x<24; x++) {
			  for(int y=0; y<25; y++) {
				  if((x<9 && y==0)	||
						  (x>9 && x<14 && y==0)	||
						  (x>14 && y==0)	||
						  (x==6 && y==1)	||
						  (x==17 && y==1)	||
						  ((x==0 || x==23) && (y==5 || y==7))	||
						  (x>9 && x<15 && y>9 && y<17)	||
						  (x==23 && y>12 && y<15)	||
						  (x==0 && (y==16 || y==18))	||
						  (x==23 && (y==18 || y==20))	||
						  ((x==6 || x==8 || x==15 || x==17) && y==24))
					  tiles[x][y] = new Tile("Inaccessible",x,y);
				  else if(x<6 && y<7)
					  tiles[x][y] = new Tile("Kitchen",x,y);
				  else if((x>=8 && x<16 && y>=2 && y<8) || (x>=10 && x<14 && y==1))
					  tiles[x][y] = new Tile("Ball Room",x,y);
				  else if((x>=18 && y<5) || (x>=19 && y==5)) 
					  tiles[x][y] = new Tile("Conservatory",x,y);
				  else if((x<5 && y==9) || (x<8 && y>9 && y<16))
					  tiles[x][y] = new Tile("Dining Room",x,y);
				  else if(x>9 && x<15 && y>9 && y<17)
					  tiles[x][y] = new Tile("Cellar",x,y);
				  else if(x>17 && y>7 && y<13)
					  tiles[x][y] = new Tile("Billiard Room",x,y);
				  else if((x>17 && y>13 && y<19) || (x==17 && y>14 && y<18))
					  tiles[x][y] = new Tile("Library",x,y);
				  else if(x<7 && y>18) 
					  tiles[x][y] = new Tile("Lounge",x,y);
				  else if(x>8 && x<15 && y>17)
					  tiles[x][y] = new Tile("Hall",x,y);
				  else if(x>16 && y>20)
					  tiles[x][y] = new Tile("Study",x,y);
				  else 
					  tiles[x][y] = new Tile("Walkway",x,y);
			  }
		  }
		  
		  //players
		  players.add(new Player("Miss Scarlett", tiles[7][24], 'S', true));
		  players.add(new Player("Colonel Mustard", tiles[0][17], 'M', true));
		  players.add(new Player("Mrs. White", tiles[9][0], 'W', true));
		  players.add(new Player("Mr. Green", tiles[14][0], 'G', true));
		  players.add(new Player("Mrs. Peacock", tiles[23][6], 'P', true));
		  players.add(new Player("Professor Plum", tiles[23][19], 'L', true));
		  
		  //weapons
		  weapons.add(new Weapon("Candlestick", tiles[18][22], 'c'));
		  weapons.add(new Weapon("Dagger", tiles[3][3], 'd'));
		  weapons.add(new Weapon("Lead Pipe", tiles[22][2], 'l'));
		  weapons.add(new Weapon("Revolver", tiles[4][11], 'v'));
		  weapons.add(new Weapon("Rope", tiles[20][8], 'r'));
		  weapons.add(new Weapon("Spanner", tiles[1][22], 's'));
		  
		  //cards
		  cards.add(new Card("Kitchen", "Room"));
		  cards.add(new Card("Ball Room", "Room"));
		  cards.add(new Card("Conservatory", "Room"));
		  cards.add(new Card("Billiard Room", "Room"));
		  cards.add(new Card("Library", "Room"));
		  cards.add(new Card("Study", "Room"));
		  cards.add(new Card("Dining Room", "Room"));
		  cards.add(new Card("Hall", "Room"));
		  cards.add(new Card("Lounge", "Room"));
		  
		  for(Player p: players) cards.add(new Card(p.getName(), "Player"));
		  for(Weapon w: weapons) cards.add(new Card(w.getName(), "Weapon"));
		  for(Card c: cards) allCards.add(c);
		  
		  //murderCards
		  //rooms are .get(0..8), players are .get(9..14), weapons are .get(15..20)
		  murderCards.add(cards.get(0));
		  murderCards.add(cards.get(9));
		  murderCards.add(cards.get(15));
		  cards.remove(0);
		  cards.remove(9);
		  cards.remove(15);
		  
		  //Doors
		  doors.add(new Door(new Tile("Walkway", 4, 6), new Tile("Kitchen", 4, 7)));
		  doors.add(new Door(new Tile("Walkway", 7, 5), new Tile("Ball Room", 8, 5)));
		  doors.add(new Door(new Tile("Walkway", 9, 8), new Tile("Ball Room", 9, 7)));
		  doors.add(new Door(new Tile("Walkway", 14, 8), new Tile("Ball Room", 14, 7)));
		  doors.add(new Door(new Tile("Walkway", 16, 5), new Tile("Ball Room", 15, 5)));
		  doors.add(new Door(new Tile("Walkway", 18, 5), new Tile("Conservatory", 18, 4)));
		  doors.add(new Door(new Tile("Walkway", 17, 9), new Tile("Billiard Room", 18, 9)));
		  doors.add(new Door(new Tile("Walkway", 22, 13), new Tile("Billiard Room", 22, 12)));
		  doors.add(new Door(new Tile("Walkway", 20, 13), new Tile("Library", 20, 14)));
		  doors.add(new Door(new Tile("Walkway", 16, 16), new Tile("Library", 17, 16)));
		  doors.add(new Door(new Tile("Walkway", 17, 20), new Tile("Study", 17, 21)));
		  doors.add(new Door(new Tile("Walkway", 15, 20), new Tile("Hall", 14, 20)));
		  doors.add(new Door(new Tile("Walkway", 12, 17), new Tile("Hall", 12, 18)));
		  doors.add(new Door(new Tile("Walkway", 11, 17), new Tile("Hall", 11, 18)));
		  doors.add(new Door(new Tile("Walkway", 6, 18), new Tile("Lounge", 6, 19)));
		  doors.add(new Door(new Tile("Walkway", 6, 16), new Tile("Dining Room", 6, 15)));
		  doors.add(new Door(new Tile("Walkway", 8, 12), new Tile("Dining Room", 7, 12)));
		  
		  for(Door door: doors) {
			  int x1 = door.t1.getXPos(); int y1 = door.t1.getYPos();
			  int x2 = door.t2.getXPos(); int y2 = door.t2.getYPos();
			  tiles[x1][y1].setDoor();
			  tiles[x2][y2].setDoor();
		  }
		  for(Player player: players) {
			  tiles[player.getPosition().getXPos()][player.getPosition().getYPos()].setPlayer(player);
		  }
		  for(Weapon weapon: weapons) {
			  tiles[weapon.getPosition().getXPos()][weapon.getPosition().getYPos()].setWeapon(weapon);
		  }
		  
		  List<Player> playing = new ArrayList<Player>();
		  
		  for(int i=0; i<numPlayers; i++) {
			  players.get(i).setPlayer("name");
			  playing.add(players.get(i));
		  } 
		  
		  int nextPlayer = (int)Math.random()*numPlayers+1;
		  while(!cards.isEmpty()) {
			  if(nextPlayer > numPlayers) nextPlayer = 1;
			  int randomCard = (int)(Math.random()*(cards.size()-1));
			  playing.get(nextPlayer-1).addToHand(cards.get(randomCard));
			  cards.get(randomCard).setPlayer(playing.get(nextPlayer-1));
			  cards.remove(randomCard);
			  nextPlayer++;
		  }
		  
		  return new Board(tiles, players, weapons, murderCards, doors, allCards, playing);
	}
	
}
