/**
 * Human Player Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class represents a human player, which
 * is a child of Player. It holds the human player's
 * hand, and handles their functions.
 */

package clueGame;

public class HumanPlayer extends Player {
	public HumanPlayer(String name) {
		super(name);
	}
	
	@Override
	public void updateHand(Card card) {
		this.hand.add(card);
	}
}
