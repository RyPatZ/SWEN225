
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * Repsents the game board
 * @author Cameron Rose & Lucas Westenra
 * @version 1.0
 */
public class Board extends GUI{

	//Fields
	private Tile[][] tiles = new Tile[24][25];
	private List<Player> players;
	private List<Weapon> weapons;
	private List<Card> murderCards;
	private List<Door> doors;
	private List<Card> allCards;
	private List<Player> playing;
	private Player playersTurn;
	private static String inp;

	//Variables
	public enum dir{NORTH, EAST, SOUTH, WEST}
	static boolean gameOver = false;
	private List<Tile> moveTiles = new ArrayList<Tile>();

	//Constructor
	public Board(Tile[][] tiles, List<Player> players, List<Weapon> weapons, List<Card> murderCards, List<Door> doors, List<Card> allCards, List<Player> playing)
	{
		this.allCards = allCards;
		this.playersTurn = players.get((int)Math.random()*players.size());
		this.murderCards = murderCards;
		
		//Creates a new tile for each position on the board
		for(int x=0; x<24; x++) {
			for(int y=0; y<25; y++) {
				this.tiles[x][y] = tiles[x][y];
			}
		}
		this.doors = doors;
		this.weapons = weapons;
		this.players = players;
		this.playing = playing;
		Board.inp = "";
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public List<Weapon> getWeapons(){
		return this.weapons;
	}
	
	public List<Card> getMurderCards(){
		return this.murderCards;
	}
	
	public static void setInp(String inp) {
		Board.inp = inp;
	}
	
	public static String getInp() {
		return Board.inp;
	}

	/**
	 * used for debugging, prints the board's characters out as a string
	 * @return string
	 */
	public String getBoardState(){
		String state = "";
		for(int y=0;y<25;y++) {
			for(int x=0;x<24;x++) {
				if(tiles[x][y].player != null) state += tiles[x][y].player.getPiece();
				else if(tiles[x][y].weapon != null) state += tiles[x][y].weapon.getPiece();
				else if(tiles[x][y].isDoor) state += 'o';
				else if(tiles[x][y].getName().equals("Walkway")) state += ' ';
				else if(tiles[x][y].getName().equals("Inaccessible")) state += 'X';
				else state += '*';
			}
			state += "\n";
		}
		return state;
	}

	/**
	 * moves param player in dir of param dir.
	 * Checks if player can move in direction dir before moving
	 * sets player position and tile positions as new location if successful
	 * returns false if player cannot move in direction dir
	 * @param player
	 * @param dir
	 * @return boolean
	 */
	public boolean movePlayer(Player player, dir dir) {
		Tile current = player.getPosition();

		int x = current.getXPos();
		int y = current.getYPos();
		Tile next=null;
		switch(dir){
		case NORTH:
			if(y-1<0)return false; //Null check
			next = tiles[x][y-1];
			for(Tile t: moveTiles) {
				if(t.equals(next)) {

					return false;
				}
			}
			break;  
		case SOUTH:
			if(y+1>24)return false; //Null check
			next = tiles[x][y+1];
			for(Tile t: moveTiles) {
				if(t.equals(next)) {

					return false;
				}
			}
			break;
		case EAST:
			if(x+1>23)return false; //Null check
			next = tiles[x+1][y];
			for(Tile t: moveTiles) {
				if(t.equals(next)) {

					return false;
				}
			}
			break;
		case WEST:
			if(x-1<0)return false; //Null check
			next = tiles[x-1][y];
			for(Tile t: moveTiles) {
				if(t.equals(next)) {

					return false;
				}
			}
			break;
		}
		
		//Checks if the player can move between two tiles and the moves the player if it can
		if(canMove(current, next)) {
			next.setPlayer(player);
			current.setPlayer(null);
			player.setPosition(next);
			moveTiles.add(current);
			return true;
		}
		return false;
	}

	/**
	 * This method works out whether the player can move 
	 * between two different tiles next to each other on the
	 * board
	 * @param current This is the current tile the player is on
	 * @param next This is the tile that the player intends on moving to 
	 * @return boolean
	 */
	public boolean canMove(Tile current, Tile next) {
		if(next.getName().equals("Inaccessible"))return false; //Player tries to move into inaccessible tile
		if(next.player != null) {
			return false; //Player tries to move into a tile occupied by another player
		}
		for(Tile t: moveTiles) {
			if(t.equals(next)) {
				return false; //Player tries to move to a tile that they have already been on in the move
			}
		}
		if(current.getName().equals(next.getName())) { 
			return true; //Player moves tiles which are in the same room
		}

		if(current.isDoor && next.isDoor) {
			return true; //Player moves through a tile that is occupied by a door
		}

		return false;
	}

	/**
	 * returns a random number from 2-12 inclusive
	 * @return int
	 */
	public static int rollDice() {
		return (int)(Math.random()*10+2);
	}

	/**
	 * returns an inhabitable tile of the same name as current tile
	 * determines inhabitable time based on proximity and then direction from current clockwise
	 * @param current
	 * @return
	 */
	public Tile checkDir(Tile current) {
		int x = current.getXPos();
		int y = current.getYPos();
		String name = current.getName();
		if(y>0)
			if(tiles[x][y-1].getName().equals(name) &&
					tiles[x][y-1].player == null) return tiles[x][y-1];
		if(x<23)
			if(tiles[x+1][y].getName().equals(name) &&
					tiles[x+1][y].player == null) return tiles[x+1][y];
		if(y<24)
			if(tiles[x][y+1].getName().equals(name) &&
					tiles[x][y+1].player == null) return tiles[x][y+1];
		if(x>0)
			if(tiles[x-1][y].getName().equals(name) &&
					tiles[x-1][y].player == null) return tiles[x-1][y];
		if(y>1)
			if(tiles[x][y-2].getName().equals(name) &&
					tiles[x][y-2].player == null) return tiles[x][y-2];
		if(x<22)
			if(tiles[x+2][y].getName().equals(name) &&
					tiles[x+2][y].player == null) return tiles[x+2][y];
		if(y<23)
			if(tiles[x][y+2].getName().equals(name) &&
					tiles[x][y+2].player == null) return tiles[x][y+2];
		if(x>1)
			if(tiles[x-2][y].getName().equals(name) &&
					tiles[x-2][y].player == null) return tiles[x-2][y];
		return null;
	}

	/**
	 * loops for number returned by rollDice()
	 * provides information for player such as moves remaining
	 * prompts player to perform an action 
	 * handles the action
	 */
	private void takeTurn(Player player) {
		int moves = rollDice();
		int allMoves = moves;
		String tileName = "Walkway";
		String endTurnMessage = "";
		String outputMessage = ("It's "+player.getPlayer()+"'s turn! ("+player.getName()+")\n");
		outputMessage += (player.getName() +" rolled a "+moves+"!");
		getTextOutputArea().setText(outputMessage);

		for(int i=moves; i>0;) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!getInp().equals("")) { 
				boolean noMove = true;
				boolean deadEnd = false;
				while(noMove) {
					int playerX = player.getPosition().getXPos();
					int playerY = player.getPosition().getYPos();
					
					//Checks if player has moved into a dead end and ends their turn if they have
					if(playerX+1 > 23 || !canMove(player.getPosition(), tiles[playerX+1][playerY])) {
						if(playerX-1 < 0 || !canMove(player.getPosition(), tiles[playerX-1][playerY])) {
							if(playerY+1 >24 || !canMove(player.getPosition(), tiles[playerX][playerY+1])) {
								if(playerY-1 < 0 || !canMove(player.getPosition(), tiles[playerX][playerY-1])) {
									i=0;
									noMove=false;
									deadEnd = true;
									break;
								}
							}
						}
					}
	
					if(inp.equalsIgnoreCase("w")) {
						if(movePlayer(player, dir.NORTH)) {
							noMove = false;
							i--;
						}
					}
					else if(inp.equalsIgnoreCase("a")) {
						if(movePlayer(player, dir.WEST)) {
							noMove = false;
							i--;
						}
					}
					else if(inp.equalsIgnoreCase("s")) {
						if(movePlayer(player, dir.SOUTH)) {
							noMove = false;
							i--;
						}
					}
					else if(inp.equalsIgnoreCase("d")) {
						if(movePlayer(player, dir.EAST)) {
							noMove = false;
							i--;
						}
					}
					else if(inp.equalsIgnoreCase("h")){
						String hand = ("Your hand is: ");
						for(Card card: player.getHand())
							hand += ("\n\t"+card.getType()+": "+card.getName());
						getTextOutputArea().setText(hand);
					}
					else if(!(player.getPosition().getName().equals("Walkway")) && inp.equalsIgnoreCase("e")){
						i=0;
						noMove = false;
					}
					setInp("");
	
				}
				redraw();
				tileName = player.getPosition().getName();
				if(i>0) {
					outputMessage = (player.getName()+"has rolled a "+allMoves+"\n");
					outputMessage += (player.getName() + " has " + (i) + " moves left.");
					if(!player.getPosition().getName().equals("Walkway")){
						outputMessage += ("\n"+player.getName()+" is in a room and may end their turn.");
					}
					getTextOutputArea().setText(outputMessage);
				} else {
					endTurnMessage = (player.getName()+" has run out of moves!");
				}
				
			}
			moveTiles.clear();		
			getTextOutputArea().setText(outputMessage);
			if(!gameOver) {
				redraw();
			}
		}
		
		setInp("");
		
		endTurnMessage += ("\n\t End Turn.\n\t Make an Accusation (usable once per game).");
		if(!player.getPosition().getName().equals("Walkway"))
			endTurnMessage += ("\n\t Make a Suggestion (using the "+player.getPosition().getName()+")");
		getTextOutputArea().setText(endTurnMessage);
		
		for(int i=0; i<1;) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(inp.equalsIgnoreCase("c") || inp.equalsIgnoreCase("e")) i++;
			else if(!player.getPosition().getName().equalsIgnoreCase("Walkway"))
				if(inp.equalsIgnoreCase("g")) i++;
		}
		
		
		if(!player.getPlaying())
			setInp("e");

		if(inp.equals("c")) 
			if(player.isPlaying()) doAccusation(player);
		if(inp.equals("g")) 
			doSuggestion(player, tileName);	
	}



	/**
	 * prompts player to choose cards to suggest
	 * players suggested are moved to player
	 * weapons suggested are moved to player
	 * displays which cards are held if a player only holds 1 suggested card
	 * prompts other player to select a card to refute with if a player holds more than 1 suggested card
	 * @param player
	 * @param tileName
	 */
	private void doSuggestion(Player player, String tileName) {
		String found = "";
		List<Card> roomCards = new ArrayList<Card>();
		List<Card> playerCards = new ArrayList<Card>();
		List<Card> weaponCards = new ArrayList<Card>();
		for(Card c: allCards) {
			if(c.getType().equals("Room")) roomCards.add(c);
			else if(c.getType().equals("Player")) playerCards.add(c);
			else if(c.getType().equals("Weapon")) weaponCards.add(c);
		}
		List<Card> guessedCards = new ArrayList<Card>();
		for(Card room: roomCards) 
			if(room.getName().equals(tileName)) 
				guessedCards.add(room);
		guessedCards.add(playerCards.get(character("")));
		guessedCards.add(weaponCards.get(weapon("")));
		for(Player p: players) {
			if(p.getName().equals(guessedCards.get(1).getName()) && !guessedCards.get(1).getName().equals(player.getName())) {
				p.getPosition().setPlayer(null);
				p.setPosition(checkDir(player.getPosition()));
				p.getPosition().setPlayer(p);
			}
		}
		for(Weapon w: weapons) {
			if(w.getName().equals(guessedCards.get(2).getName())) {
				w.getPosition().setWeapon(null);
				w.setPosition(checkDir(player.getPosition()));
				w.getPosition().setWeapon(w);
			}
		}

		List<Card> foundCards = new ArrayList<Card>();
		for(Player p: playing) {
			for(int cc=0; cc<3; cc++) {
				if(p != player)
					if(p.getHand().contains(guessedCards.get(cc))) {
						found += ("Player "+p.getName()+" has the "+guessedCards.get(cc).getName()+" card!\n");
						foundCards.add(guessedCards.get(cc));
					}
			} 
			
		}
		for(Card c: guessedCards) {
			if(!foundCards.contains(c))
				found += ("The "+c.getName()+" card was not found!\n");
		}
		getTextOutputArea().setText(found);
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * prompts player to choose a room card, player card and weapon card to accuse with
	 * if any of these cards are not the actaul murder cards then the player is eliminated
	 * if all 3 accusation cards match the 3 murder cards the player wins and the game ends
	 * @param player
	 */
	private void doAccusation(Player player) {
		String out = "";
		List<Card> roomCards = new ArrayList<Card>();
		List<Card> playerCards = new ArrayList<Card>();
		List<Card> weaponCards = new ArrayList<Card>();
		for(Card c: allCards) {
			if(c.getType().equals("Room")) roomCards.add(c);
			else if(c.getType().equals("Player")) playerCards.add(c);
			else if(c.getType().equals("Weapon")) weaponCards.add(c);
		}
		List<Card> guessedCards = new ArrayList<Card>();
		guessedCards.add(roomCards.get(room("")));
		guessedCards.add(playerCards.get(character("")));
		guessedCards.add(weaponCards.get(weapon("")));
		if(guessedCards.get(0) == murderCards.get(0) &&
				guessedCards.get(1) == murderCards.get(1) &&
				guessedCards.get(2) == murderCards.get(2)) {
			out += (player.getName()+"'s accusation was CORRECT and has won the game!\n");
			gameOver = true;
		}
		else {
			out += (player.getName()+"'s accusation was INCORRECT!\n"
					+player.getName()+" cannot make any more accusations!\n");
			
			player.setPlaying(false);
		}
		getTextOutputArea().setText(out);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * initialize board state fully, create a new board object and call looping function to play game
	 * @param args
	 */
	public static void main(String[] args) {
		//initialize arrays needed to create new board object
		Tile[][] tiles = new Tile[24][25];
		List<Player> players = new ArrayList<Player>();
		List<Weapon> weapons = new ArrayList<Weapon>();
		List<Card> allCards = new ArrayList<Card>();
		List<Card> cards = new ArrayList<Card>();
		List<Card> murderCards = new ArrayList<Card>();
		List<Door> doors = new ArrayList<Door>();

		//initialize tiles 
		for(int x=0; x<24; x++) {
			for(int y=0; y<25; y++) {
				if((x<9 && y==0)	||
						(x>9 && x<14 && y==0)	||
						(x>14 && y==0)	||
						(x==6 && y==1)	||
						(x==17 && y==1)	||
						((x==0) && (y==6 || y==8))	||
						((x == 23) && (y==5 || y==7))	||
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
		for(Weapon w: weapons){
			cards.add(new Card(w.getName(), "Weapon"));
			w.weaponColor = new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));

		}
		for(Card c: cards) allCards.add(c);

		//murderCards
		//rooms are .get(0..8), players are .get(9..14), weapons are .get(15..20)
		int roomNum = (int)(Math.random()*9);
		int personNum = (int)(Math.random()*6) + 9;
		int weaponNum = (int)(Math.random()*6) + 15;
		murderCards.add(cards.get(roomNum));
		murderCards.add(cards.get(personNum));
		murderCards.add(cards.get(weaponNum));
		cards.remove(weaponNum);
		cards.remove(personNum);
		cards.remove(roomNum);

		//Doors
		doors.add(new Door(new Tile("Walkway", 4, 7), new Tile("Kitchen", 4, 6)));
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

		//set field to true in tiles to hold a door
		for(Door door: doors) {
			int x1 = door.t1.getXPos(); int y1 = door.t1.getYPos();
			int x2 = door.t2.getXPos(); int y2 = door.t2.getYPos();
			tiles[x1][y1].setDoor();
			tiles[x2][y2].setDoor();
		}

		//set field of players and weapons in tiles to hold the respective objects
		for(Player player: players) {
			tiles[player.getPosition().getXPos()][player.getPosition().getYPos()].setPlayer(player);
		}
		for(Weapon weapon: weapons) {
			tiles[weapon.getPosition().getXPos()][weapon.getPosition().getYPos()].setWeapon(weapon);
		}

		List<Player> playing = new ArrayList<Player>();
		
	

		//startup options, choose characters and number of players
		List<String>names = new ArrayList<String>();
		List<Integer>selections = new ArrayList<Integer>();
		int nplayers = getNumPlayers();
		if(nplayers == -1) return;
		for(int i=0; i<nplayers; i++) { 
			
			//choose a unique player name
			String name = getPlayerName("");
			if(name == null) return;
			while(names.contains(name)) name = getPlayerName("Must be a unique name!\n");
			names.add(name);
			
			//choose a unique player selection
			int selection = getCharacterChoice("");
			if(selection == -1) return;
			while(selections.contains(selection)) selection = getCharacterChoice("Must be a unique character!\n");
			selections.add(selection);
			
			players.get(selection).setPlayer(name);
			playing.add(players.get(selection));
			
		} 

		//add a random card to a looping player, looping until all remaining cards are given out
		int nextPlayer = (int)Math.random()*nplayers+1;
		while(!cards.isEmpty()) {
			if(nextPlayer > nplayers) nextPlayer = 1;
			int randomCard = (int)(Math.random()*(cards.size()-1));
			playing.get(nextPlayer-1).addToHand(cards.get(randomCard));
			cards.get(randomCard).setPlayer(playing.get(nextPlayer-1));
			cards.remove(randomCard);
			nextPlayer++;
		}

		Board board = new Board(tiles, players, weapons, murderCards, doors, allCards, playing);
		board.redraw();
		

		board.runGame();
	}

	/**
	 * Returns boolean for debugging 
	 * returns true if game won by player
	 * returns false if no player won the game
	 * contains the main gameplay loop, breaking if gameOver
	 * @return boolean
	 */
	public boolean runGame() {
		while(!gameOver) {
			int count = 0;
			for(Player pl: playing) if(!pl.getPlaying()) count++;
			if(count == playing.size()) { 
				getTextOutputArea().setText("Every Player has gotten their accusation wrong!\n"
						+ "Game Over!");
				return false;
			}
			for(Player p: playing) {
				if(gameOver) {
					return true;
				}
				takeTurn(p);
			}
		}
		return true;
	}
	
	/**
	 * This method is used to test the doSuggestion method for the JUnit test suite
	 * @param player
	 * @param pl
	 * @param wep
	 */
	public void debugSuggestion(Player player, Player pl, Weapon wep) {
		List<Card> roomCards = new ArrayList<Card>();
		List<Card> playerCards = new ArrayList<Card>();
		List<Card> weaponCards = new ArrayList<Card>();
		for(Card c: allCards) {
			if(c.getType().equals("Room")) roomCards.add(c);
			else if(c.getType().equals("Player")) playerCards.add(c);
			else if(c.getType().equals("Weapon")) weaponCards.add(c);
		}

		for(Player p: players) {
			if(p.getName().equals(pl.getName()) && !pl.getName().equals(player.getName())) {
				p.getPosition().setPlayer(null);
				p.setPosition(checkDir(player.getPosition()));
				p.getPosition().setPlayer(p);
			}
		}
		for(Weapon w: weapons) {
			if(w.getName().equals(wep.getName())) {
				w.getPosition().setWeapon(null);
				w.setPosition(checkDir(player.getPosition()));
				w.getPosition().setWeapon(w);
			}
		}
	}

	/**
	 * This method is used to test the doAccusation method for the JUnit test suite
	 * @param player
	 * @param murderC
	 * @return
	 */
	public boolean debugAccusation(Player player, List<Card> murderC) {
		List<Card> roomCards = new ArrayList<Card>();
		List<Card> playerCards = new ArrayList<Card>();
		List<Card> weaponCards = new ArrayList<Card>();
		for(Card c: allCards) {
			if(c.getType().equals("Room")) roomCards.add(c);
			else if(c.getType().equals("Player")) playerCards.add(c);
			else if(c.getType().equals("Weapon")) weaponCards.add(c);
		}
		List<Card> guessedCards = new ArrayList<Card>();
		guessedCards.add(murderC.get(0));
		guessedCards.add(murderC.get(1));
		guessedCards.add(murderC.get(2));
		if(guessedCards.get(0) == murderCards.get(0) &&
				guessedCards.get(1) == murderCards.get(1) &&
				guessedCards.get(2) == murderCards.get(2)) {
			gameOver = true;
			return true;
		}
		else {
			player.setPlaying(false);
		}
		return false;
	}

	@Override
	protected void redraw(Graphics g) {
	
		
		int width = getDrawingAreaDimension().width;
		int height = getDrawingAreaDimension().height-20;

		int offset;
		if(width < height){
			offset = width/24;
		}
		else {
			offset = height/25;
		}
		
		int centre = (width/2)-(12*offset); //The number of pixels needed to center the board
		
		for(int x=0; x<24; x++) {
			for(int y=0; y<25; y++) {
				if(tiles[x][y].getName().equals("Inaccessible")) {
					g.setColor(new Color(164, 251 ,239));	
					g.fillRect((x*offset)+centre, y*offset, offset, offset);
				}
				else if(tiles[x][y].getName().equals("Walkway")) {
					g.setColor(new Color(255, 255 ,100));
					g.fillRect((x*offset)+centre, y*offset, offset, offset);
				}
				
				if(tiles[x][y].player != null){
					g.setColor(new Color(0,0,0));
					g.fillOval(x*offset+centre+(int)(offset*0.125), y*offset+(int)(offset*0.125), (int)(offset*0.8), (int)(offset*0.8));
					g.setColor(new Color(255,255,255));
					g.setFont(new Font("TimesRoman", Font.BOLD, (int)(offset*0.6)));
					g.drawString(tiles[x][y].player.getPiece()+"", x*offset+centre+(offset/4)+1,y*offset+(offset/1)-5);
				}
				
				if(tiles[x][y].weapon != null) {
					g.setColor(tiles[x][y].weapon.weaponColor);
					g.fillOval(x*offset+centre+(int)(offset*0.15), y*offset+(int)(offset*0.15), (int)(offset*0.7), (int)(offset*0.7));
					g.setColor(new Color(255,255,255));
					g.setFont(new Font("Verdana", Font.BOLD, (int)(offset*0.6)));
					g.drawString(tiles[x][y].weapon.getPiece()+"", x*offset+centre+(offset/3),y*offset+(int)(offset*0.66));
				}

			}
		}
		
		((Graphics2D) g).setStroke(new BasicStroke(2));
		
		for(int x=0; x<24; x++) {
			for(int y=0; y<25; y++) {
				
				g.setColor(new Color(0, 0, 0));
				if(x+1 < 24 && !(tiles[x][y].getName().equals(tiles[x+1][y].getName()))) {
					g.drawLine(x*offset+centre+offset, y*offset, x*offset+centre+offset, y*offset+offset);
				}
				if(y+1 < 25 && !(tiles[x][y].getName().equals(tiles[x][y+1].getName()))) {
					g.drawLine(x*offset+centre, y*offset+offset, x*offset+centre+offset, y*offset+offset);
				}
				
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.setColor(new Color(255, 255 ,100));
				for(Door d: doors) {
					if(tiles[x][y].isDoor) {
						if(x == d.t1.getXPos() && y == d.t1.getYPos()) {
							if(x+1 == d.t2.getXPos() && y == d.t2.getYPos()) {	
								g.drawLine(x*offset+centre+offset, y*offset, x*offset+centre+offset, y*offset+offset);
							}
							else if(x-1 == d.t2.getXPos() && y == d.t2.getYPos()) {	
								g.drawLine(x*offset+centre, y*offset, x*offset+centre, y*offset+offset);
							}
							else if(x == d.t2.getXPos() && y+1 == d.t2.getYPos()) {
								g.drawLine(x*offset+centre, y*offset+offset, x*offset+centre+offset, y*offset+offset);
							}
							else if(x == d.t2.getXPos() && y-1 == d.t2.getYPos()) {
								g.drawLine(x*offset+centre, y*offset, x*offset+centre+offset, y*offset);
							}
						}

					}
				}
					

			}
		}
		
		float alpha = 0.15f; //Transparency of 0.15
		g.setColor(new Color(0 ,0, 0, alpha));	
		((Graphics2D) g).setStroke(new BasicStroke(1));
		
		for(int x=0; x<24; x++) {
			int y=0;
			g.drawLine(x*offset+centre, y*offset, x*offset+centre, (y*offset)+(25*offset));
		}
		
		for(int y=0; y<25; y++) {
			int x=0;
			g.drawLine((x*offset)+centre, y*offset, (x*offset)+(24*offset)+centre, y*offset);	
		}
		g.setColor(new Color(0 ,0, 0));	
		((Graphics2D) g).setStroke(new BasicStroke(5));
		g.drawRect(centre, 0, 24*offset, 25*offset); //Black border around the board
		
		//Here to end of method
		alpha = 0.4f; //Transparency of 0.15
		g.setColor(new Color(0 ,0, 0, alpha));
		g.setFont(new Font("Verdana", Font.BOLD, (int)(offset/1.5)));
		
		g.drawString("HALL", centre+(offset*11),offset*22);
		g.drawString("STUDY", centre+(offset*19)+(offset/2),offset*24-(int)(offset/1.5));
		g.drawString("LIBRARY", centre+(offset*19),offset*17);
		g.drawString("BILLIARD", centre+(offset*19)+(offset/4),offset*11-(offset/2));
		g.drawString("ROOM", centre+(offset*20)-(offset/4),offset*12-(offset/2));
		g.drawString("CONSERV.", centre+(offset*19),offset*4);
		g.drawString("BALL ROOM", centre+(offset*10)-(offset/6),offset*5);
		g.drawString("KITCHEN", centre+(offset)+(offset/3),offset*3+(offset/2));
		g.drawString("DINING ROOM", centre+(offset*1)+(offset/4),offset*13);
		g.drawString("LOUNGE", centre+(offset*2),offset*22+(offset/4));
	}

}
