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

import java.util.Set;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name) {
		super(name);
	}

	// TODO: Write the following three methods
	@Override
	protected Solution createAccusation() {
		return null;
	}

	//@Override
	protected BoardCell selectTarget(Set<BoardCell> targets) {
		return null;
	}

	@Override
	protected Solution createSuggestion() {
		return null;
	}
}
