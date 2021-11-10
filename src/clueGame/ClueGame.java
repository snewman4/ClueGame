package clueGame;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	
	public static void main(String[] args) {
		// The entire window
		JFrame frame = new JFrame("Clue Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Visuals for the gameboard itself
		Board gameboard = Board.getInstance();
		gameboard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameboard.initialize();
		frame.add(gameboard, BorderLayout.CENTER);
		// Other UI panels
		GameControlPanel controlPanel = new GameControlPanel();
		frame.add(controlPanel, BorderLayout.SOUTH);
		KnownCardsDisplay cardsDisplay = new KnownCardsDisplay();
		frame.add(cardsDisplay, BorderLayout.EAST);
		// Set the window to be an appropriate size
		frame.pack();
		// Get the game started by dealing the cards
		gameboard.deal();
		// Update the display
		Set<Card> humanPlayerHand = gameboard.getPlayers().get(0).getHand();
		cardsDisplay.loadHand(humanPlayerHand);
		// Show the board
		frame.setVisible(true);
	}
}
