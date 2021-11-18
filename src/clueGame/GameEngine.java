package clueGame;

import java.awt.Color;
import java.util.Random;
import java.util.Set;

public class GameEngine {
	// UI variables
	private Board gameboard;
	private GameControlPanel controlPanel;
	private KnownCardsDisplay cardsDisplay;
	// Player variables
	private Player humanPlayer;
	private boolean playerIsDone;
	private int currPlayer; // Used to determine who is currently taking their turn

	public GameEngine() {
		super();
		gameboard = Board.getInstance(); // Get the board
		// Initialize data about the game
		initializeGame();
		gameboard.setEngine(this);
		// Create the other UI elements
		controlPanel = new GameControlPanel();
		controlPanel.setEngine(this);
		cardsDisplay = new KnownCardsDisplay();
		currPlayer = -1; // Game always starts at player 0, and starts by adding 1 to find new player
	}

	// Method to initialize the board with necessary elements
	private void initializeGame() {
		gameboard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameboard.initialize();
	}

	// Method to get the game running
	public void launchGame() {
		// Initialize the board with necessary elements
		gameboard.setControlPanel(controlPanel);
		gameboard.setCardsDisplay(cardsDisplay);
		// Deal the cards
		gameboard.deal();
		// Set up the human's known card display
		humanPlayer = gameboard.getPlayer(0);
		Set<Card> humanPlayerHand = humanPlayer.getHand();
		cardsDisplay.loadHand(humanPlayerHand);
	}

	// Method to handle a new turn
	public void newTurn() {
		// Get the roll and the new player
		int roll = diceRoll();
		Player activePlayer = getNextPlayer();
		controlPanel.setTurn(activePlayer, roll); // Update the UI
		BoardCell cell = gameboard.getCell(activePlayer.getRow(), activePlayer.getColumn());
		gameboard.calcTargets(cell, roll);
		// Determine how to handle the turn based on type of player
		if(activePlayer.getClass() == HumanPlayer.class)
			humanTurn();
		else
			computerTurn(activePlayer);
	}

	// Method to move on to and return the next player
	public Player getNextPlayer() {
		// Move on to the next player. Loop back around to 0
		currPlayer++;
		currPlayer = currPlayer % 6;

		return gameboard.getPlayer(currPlayer);
	}

	// Method to roll the dice for the new player
	public int diceRoll() {
		Random dice = new Random();
		return (dice.nextInt(6) + 1);
	}

	// Method that is run when it is the human player's turn
	public void humanTurn() {
		gameboard.repaint(); // Repaint to display targets
		playerIsDone = false;
	}
	
	// Method to allow the human to move
	public void humanMove(BoardCell target) {
		humanPlayer.setCurrentCell(target); // Update player's location
		gameboard.getTargets().clear();
		gameboard.repaint();
		// Determine if the player moved to a room
		if(humanPlayer.findCurrentRoom() != null) {
			gameboard.generateSuggestionDialog(humanPlayer.findCurrentRoom()); // Tell the gameboard to make the player make a suggestion
		}
		// Flag that the player's turn is done
		playerIsDone = true;
	}
	
	// Method to handle the human making a suggestion
	public void humanSuggestion(Card personCard, Card roomCard, Card weaponCard) {
		Solution suggestion = new Solution(personCard, roomCard, weaponCard); // Generate a solution object
		// Update the control panel with the suggestion
		controlPanel.setGuess(personCard.getName() + ", " + roomCard.getName() + ", " + weaponCard.getName(), humanPlayer.getColor());
		// Attempt to disprove the suggestion
		Card disprover = gameboard.handleSuggestion(humanPlayer, suggestion);
		if(disprover != null) {
			controlPanel.setGuessResult("Your suggestion was disproven by " + disprover.getHolder().getName() +
					" with " + disprover.getName(), disprover.getHolder().getColor());
			// If the human player hasn't seen the resulting card, update their display
			if(!humanPlayer.getSeen().contains(disprover))
				cardsDisplay.updateSeen(disprover, disprover.getHolder());
			humanPlayer.updateSeen(disprover);
		}
		else
			controlPanel.setGuessResult("Your suggestion could not be disproven.", Color.WHITE);
	}

	// Method that is run when it is a computer's turn
	public void computerTurn(Player activePlayer) {
		// Computer will always start by trying to make an accusation
		Solution accusation = activePlayer.createAccusation();
		// The accusation will be null if one cannot be made, skip
		if(accusation != null) {
			if(gameboard.checkAccusation(accusation)) {
				// TODO: Handle a correct accusation
			}
			else {
				// TODO: Handle an incorrect accusation
			}
		}
		// If the computer doesn't make an accusation, it must move
		BoardCell oldCell = gameboard.getCell(activePlayer.getRow(), activePlayer.getColumn()); // Where the player currently is
		BoardCell newCell = activePlayer.selectTarget(gameboard.getTargets()); // Where the player will go
		activePlayer.setCurrentCell(newCell); // Update player's location
		oldCell.setOccupied(false);
		newCell.setOccupied(true);
		gameboard.getTargets().clear();
		gameboard.repaint(); // Redraw the board with the player moved
		// If the computer moved into a room, it must make an suggestion
		if(activePlayer.findCurrentRoom() != null) {
			// Generate a suggestion from that room
			Solution suggestion = activePlayer.createSuggestion();
			controlPanel.setGuess(suggestion.getPerson().getName() + ", " + suggestion.getRoom().getName() + ", " + suggestion.getWeapon().getName(),
					activePlayer.getColor()); // Update the control panel
			// See if anyone is able to disprove that suggestion
			Card disprover = gameboard.handleSuggestion(activePlayer, suggestion);
			// Because this will always be for the computer player, don't display the disprover
			if(disprover != null) {
				activePlayer.updateSeen(disprover); // Add the disproving card to the player's hand
				controlPanel.setGuessResult(activePlayer.getName() + "'s guess was disproven", Color.WHITE);
			}
			else
				controlPanel.setGuessResult("No one can disprove that suggestion", Color.WHITE);
		}
	}

	// Getters and Setters
	public Board getGameboard() {
		return gameboard;
	}

	public GameControlPanel getControlPanel() {
		return controlPanel;
	}

	public KnownCardsDisplay getCardsDisplay() {
		return cardsDisplay;
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public boolean playerIsDone() {
		return playerIsDone;
	}
	
	public int getCurrPlayer() {
		return currPlayer;
	}
}
