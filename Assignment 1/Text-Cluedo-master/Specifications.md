CLUEDO RULES
------------
Any rules not mentioned that are implemented must be stated in the report.


PLAYERS:
3-6. Each player chooses one character. Each character has a designated starting position.

WEAPONS:
Begin at a random position, no 2 weapons are in the same room

SETUP:
1 weapon card, suspect card and room card is chosen at random. The remaining cards are dealt randomly to all players. Some players may end up with more cards than others.

MOVEMENT:
Dice roll with 2 dice. Your character moves horizontally, vertically.
- don't enter the same space twice on one turn (have a set of cells, if the cell you move to is in that set, it is not allowed)
- Cannot go through other suspects
- No secret passages like the official game
- Assumption: Rooms act like one giant cell and multiple suspects can be inside. If you enter a room you don't need to move any more spaces from your dice roll.

SUGGESTION:
If you enter a room you may make a suggestion.
Suggest a suspect and a weapon that could have occured within that room. The Suspect and the weapon are moved into that room on that turn.

In a clockwise order, each player must check if they have cards matching any of the weapon, suspect or room mentioned. If they do, they get to choose which one to show to the suggesting player.

ACCUSATION:
Instead of a suggestion, you could do an accusation. You get to guess the weapon, suspect and room that the murder took place. If a success, you win. If you fail, you are taken out of the game however you still need to respond to suggestions.

Assumption: You can do accusations anywhere, suggestions only in the specific rooms.


HOW A TURN LOOKS:
- Dice roll. 6 * Math.random() + 6 * Math.random() + 2
- You get to decide how you move. Options presented will be up, down, left and right. If you can't go in a particular direction the option will not be given.
- If you end up in a room then you will stop.
- If you begin in a room, one of the options will be "remain" which lets you stay in the room you are already in.
- After your movement is over you will be asked to either make a suggestion or an accusation (if in a room), or you will be asked if you want to make an accusation (yes/no) - (for outside of a room)
- suggestion/accusation will ask you for the suspect/weapon (and room if accusation)
- a player may have to pick one of their cards to reveal to you.
