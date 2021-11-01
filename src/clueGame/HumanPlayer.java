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
