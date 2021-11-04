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
	
	public BoardCell selectTarget(BoardCell currCell, int roll) {
		return currCell;
	}
	
	public Solution createSuggestion() {
		return new Solution(new Card('T', "Test", CardType.PERSON), new Card('T', "Test", CardType.PERSON), new Card('T', "Test", CardType.PERSON));
	}
}
