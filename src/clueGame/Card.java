/**
 * Card Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class represents a single card in the deck,
 * storing the name and type of card.
 */

package clueGame;

public class Card {
	private String cardName;
	private char cardInitial;
	private CardType type;
	
	public Card(char cardInitial, String cardName, CardType type) {
		super();
		this.cardInitial = cardInitial;
		this.cardName = cardName;
		this.type = type;
	}
	
	public String getName() {
		return cardName;
	}
	
	public char getInitial() {
		return cardInitial;
	}
	public CardType getType() {
		return type;
	}
}
