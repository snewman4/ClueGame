/**
 * Computer Player Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class represents a computer player, which
 * is a child of Player. It holds the computer player's
 * hand, and handles their functions.
 */

package clueGame;

public class ComputerPlayer extends Player {
	public ComputerPlayer(String name) {
		super(name);
	}
	
	@Override
	public void updateHand(Card card) {
		this.hand.add(card);
	}

}
