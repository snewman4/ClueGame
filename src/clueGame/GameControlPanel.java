package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GameControlPanel extends JPanel {
	private Board gameboard;
	private JTextField currPlayer;
	private JTextField currRoll;
	private JTextField guess;
	private JTextField result;
	/*
	 * Constructor for the control panel. Most of the
	 * action happens here
	 */
	public GameControlPanel() {
		// Get a reference to the board so that we can control logic from here
		gameboard = Board.getInstance();
		setPreferredSize(new Dimension(1000, 200));
		setLayout(new GridLayout(2, 0));
		
		// Set up the upper half of the control panel
		JPanel upperHalf = new JPanel();
		upperHalf.setLayout(new GridLayout(1, 4));
		
		// Turn panel displays whose turn it is
		JPanel turnPanel = new JPanel();
		JLabel whoseTurn = new JLabel("Whose turn?");
		currPlayer = new JTextField(15);
		currPlayer.setEditable(false); // Make it so user can't edit
		Player firstPlayer = gameboard.getPlayers().get(0);
		turnPanel.add(whoseTurn);
		turnPanel.add(currPlayer);
		upperHalf.add(turnPanel);
		
		// Roll panel displays what the player's roll was
		JPanel rollPanel = new JPanel();
		JLabel roll = new JLabel("Roll:");
		currRoll = new JTextField(5);
		currRoll.setEditable(false); // Make it so user can't edit
		rollPanel.add(roll);
		rollPanel.add(currRoll);
		upperHalf.add(rollPanel);
		
		// Accusation button allows the player to make an accusation
		JButton accusationButton = new JButton("Make Accusation");
		upperHalf.add(accusationButton);
		
		// Next button allows the player to move on to the next turn
		JButton nextButton = new JButton("Next Turn");
		nextButton.addActionListener(new NextListener());
		upperHalf.add(nextButton);
		
		// Set up the lower half of the control panel
		JPanel lowerHalf = new JPanel();
		lowerHalf.setLayout(new GridLayout(0, 2));
		
		// Guess panel shows the current player's guess
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new GridLayout(1, 0));
		guess = new JTextField(30);
		guess.setEditable(false); // Make it so user can't edit
		guessPanel.add(guess);
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		lowerHalf.add(guessPanel);
		
		// Guess result panel shows the result of the player's guess
		JPanel guessResult = new JPanel();
		guessResult.setLayout(new GridLayout(1, 0));
		result = new JTextField(30);
		result.setEditable(false); // Make it so user can't edit
		guessResult.add(result);
		guessResult.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		lowerHalf.add(guessResult);
		
		// Add both halves to the final layout
		add(upperHalf, BorderLayout.NORTH);
		add(lowerHalf, BorderLayout.SOUTH);
	}
	
	// Listens for when the next button is pressed
	class NextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// If the player is done:
			if(gameboard.playerDone()) {
				// Load up the next player's turn
				Player nextPlayer = gameboard.getNextPlayer();
				int roll = gameboard.diceRoll();
				setTurn(nextPlayer, roll); // Update the display
				gameboard.newPlayerTurn(roll); // Move on to the next turn
				// If the new player is a human player:
				if(nextPlayer.getClass() == HumanPlayer.class) {
					gameboard.displayTargets();
					gameboard.setPlayerDone(false);
				}
				// If the new player is a computer player
				else {
					// Try to make an accusation
					Solution accusation = nextPlayer.createAccusation();
					// If an accusation was made:
					if(accusation != null) {
						if(gameboard.checkAccusation(accusation)) {
							// TODO: Handle a correct accusation
						}
						else {
							// TODO: Handle an incorrect accusation
						}
					}
					
					// Tell the board to have the player move
					gameboard.doComputerMove();
					// Check if the player moved to a room
					if(nextPlayer.findCurrentRoom() != null) {
						// Generate a suggestion from that room
						accusation = nextPlayer.createSuggestion();
						guess.setText(accusation.getPerson().getName() + ", " + accusation.getRoom().getName() + ", " + accusation.getWeapon().getName()); // Update control panel
						guess.setBackground(nextPlayer.getColor());
						Card disprover = gameboard.handleSuggestion(nextPlayer, accusation); // Try to disprove
						nextPlayer.updateSeen(disprover); // Add the disproving card to the player's hand
						// Because this will always be for the computer player, don't display the disprover
						if(disprover != null)
							result.setText(nextPlayer.getName() + "'s guess was disproven");
						else
							result.setText("No one can disprove that suggestion");
					}
				}
			}
			else {
				// If the player is not done, give them an error
				JOptionPane.showMessageDialog(null, "Please complete your turn");
			}
		}
	}
	
	// Method to display the current player and the current roll
	public void setTurn(Player player, int roll) {
		currPlayer.setText(player.getName());
		currPlayer.setBackground(player.getColor());
		currRoll.setText(String.valueOf(roll));
	}
	
	// Method to display the player's guess
	public void setGuess(String guess) {
		this.guess.setText(guess);
	}
	
	// Method to display the result of the guess
	public void setGuessResult(String guessResult) {
		result.setText(guessResult);
	}
	
	// Main method for testing
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel(); // Create the panel
		JFrame frame = new JFrame(); // Create the frame
		frame.setContentPane(panel); // Put the panel in the frame
		frame.setSize(750, 180); // Size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stop program on close
		frame.setVisible(true); // Make it visible
		
		// Test filling in the data
		Player mustard = new ComputerPlayer("Col. Mustard");
		panel.setTurn(mustard, 5);
		panel.setGuess("I have no guess!");
		panel.setGuessResult("So you have nothing?");
	}
}
