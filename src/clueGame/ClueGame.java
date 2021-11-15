package clueGame;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private Player humanPlayer;

	public ClueGame() {
		// The entire window
		setTitle("Clue Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Visuals for the gameboard itself
		Board gameboard = Board.getInstance();
		gameboard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameboard.initialize();
		add(gameboard, BorderLayout.CENTER);
		// Other UI panels
		GameControlPanel controlPanel = new GameControlPanel();
		add(controlPanel, BorderLayout.SOUTH);
		KnownCardsDisplay cardsDisplay = new KnownCardsDisplay();
		add(cardsDisplay, BorderLayout.EAST);
		// Set the window to be an appropriate size
		pack();
		// Get the game started by dealing the cards
		gameboard.deal();
		// Update the display
		humanPlayer = gameboard.getPlayers().get(0);
		Set<Card> humanPlayerHand = humanPlayer.getHand();
		cardsDisplay.loadHand(humanPlayerHand);
		// Show the board
		setLocationRelativeTo(null); // Makes it so app launches at center of screen
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public static void main(String[] args) {
		ClueGame game = new ClueGame();
		game.setVisible(true);

		// Display a brief introduction to the game
		JOptionPane.showMessageDialog(null, "You are playing as " + game.getHumanPlayer().getName() + "!\n Find out"
				+ " who the killer is\nbefore the other players can!");
	}
}
