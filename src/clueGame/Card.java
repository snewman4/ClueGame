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
	
	public boolean equals(Card target) {
		return false;
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
