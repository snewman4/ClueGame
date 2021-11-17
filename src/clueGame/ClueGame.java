package clueGame;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private GameEngine engine;
	private Player humanPlayer;
	private Board gameboard;
	private GameControlPanel controlPanel;
	private KnownCardsDisplay cardsDisplay;

	public ClueGame() {
		// The entire window
		setTitle("Clue Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the game engine, which runs most of the logic of the game
		engine = new GameEngine();
		// Get the UI elements from the engine
		gameboard = engine.getGameboard();
		controlPanel = engine.getControlPanel();
		cardsDisplay = engine.getCardsDisplay();
		// Launch the game
		engine.launchGame();
		// Set up the UI panels
		add(gameboard, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
		add(cardsDisplay, BorderLayout.EAST);
		// Set the window to be an appropriate size
		pack();
		humanPlayer = engine.getHumanPlayer();
		setLocationRelativeTo(null); // Makes it so app launches at center of screen
	}

	public GameEngine getEngine() {
		return engine;
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
		
		GameEngine engine = game.getEngine();
		engine.newTurn(); // It becomes the human's turn once the dialog is closed
	}
}
