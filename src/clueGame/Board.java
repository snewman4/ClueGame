/**
 * Board Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class creates the game board based on information fed to it by
 * the config files.
 */

package clueGame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener {
	private static final int NUM_PLAYERS = 6;
	private GameEngine engine;
	// Game board variables
	private static Board theInstance = new Board(); // Private, single instance of board
	private int numRows;
	private int numColumns;
	private BoardCell[][] gameBoard;
	// Initialization variables
	private String layoutConfigFile;
	private String setupConfigFile;
	// Movement variables
	private Map<Character, Room> roomMap;
	private Set<BoardCell> targets; // Used to find the targets a certain cell has
	private Set<BoardCell> visited; // Used to store which cells were visited in a turn
	// Player and card variables
	private ArrayList<Player> players; // Stores the players
	private Map<String, Card> cards; // A list of the card references
	// Three temporary lists to independently store each type of card, to be chosen by solution
	private ArrayList<Card> personCards;
	private ArrayList<Card> roomCards;
	private ArrayList<Card> weaponCards;
	private ArrayList<Card> deck; // The deck of cards
	// Solution and accusation variables
	private Solution theAnswer; // Stores the solution
	private SuggestionDialog suggestionDialog;
	private AccusationDialog accusationDialog;
	
	// Board constructor, can only be accessed by this class
	private Board() {
		super();
		// Set how big the gameboard wants to be
		setPreferredSize(new Dimension(700, 700));
		addMouseListener(this);
	}
	
	// Initialize the board
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		generateAdjLists();
	}
	
	// Return the only board
	public static Board getInstance() {
		return theInstance;
	}
	
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = "data/" + layoutConfigFile;
		this.setupConfigFile = "data/" + setupConfigFile;
	}
	
	public void setEngine(GameEngine engine) {
		this.engine = engine;
	}
	
	// Method to read in the setup from the setup config file
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		players = new ArrayList<>();
		cards = new HashMap<>();
		personCards = new ArrayList<>();
		roomCards = new ArrayList<>();
		weaponCards = new ArrayList<>();
		roomMap = new HashMap<>();
		targets = new HashSet<>();
		visited = new HashSet<>();
		FileReader reader = new FileReader(setupConfigFile);
		Scanner in = new Scanner(reader);
		
		// Read in each line of the file
		while(in.hasNext()) {
			String inRead = in.nextLine();
			// If the next line is a comment, skip it
			if(inRead.contains("//")) {
				continue;
			}
			
			// Split each line up, read out the various information
			String[] splitRead = inRead.split(", ");
			String itemType = splitRead[0]; // Type of item we are creating
			String itemName = splitRead[1]; // Name of that item
			Character itemInitial = splitRead[2].charAt(0); // Initial of that item
			
			switch(itemType) {
				// If the itemType is a room, create a room and card with the name and initial
				case "Room":
					Room newRoom = new Room(itemInitial, itemName);
					roomMap.put(itemInitial, newRoom); // Add to room map
					addCard(itemName, itemInitial, CardType.ROOM);
					break;
				// If the itemType is a space, it needs to be created as a room, but not made a card
				case "Space":
					Room newSpace = new Room(itemInitial, itemName);
					roomMap.put(itemInitial, newSpace);
					break;
				// If the itemType is a person, create a card with the name and initial
				case "Person":
					// Create a new player object, determine if it is human or computer
					Player newPlayer;
					// Player 0 should always be human player
					if(players.isEmpty()) {
						newPlayer = new HumanPlayer(itemName);
					}
					// All other players are computer players
					else {
						newPlayer = new ComputerPlayer(itemName);
					}
					players.add(newPlayer);
					// There cannot be more than 6 players
					if(players.size() > NUM_PLAYERS) {
						throw new BadConfigFormatException("Too many players defined in " + setupConfigFile);
					}
					// Create a card for the person
					addCard(itemName, itemInitial, CardType.PERSON);
					break;
				// If the itemType is a weapon, create a card with the name and initial
				case "Weapon":
					addCard(itemName, itemInitial, CardType.WEAPON);
					break;
				default:
					throw new BadConfigFormatException("Could not evaluate type in " + setupConfigFile);
			}
		}
		in.close();
	}

	// Method to add cards to the appropriate data sets
	private void addCard(String itemName, Character itemInitial, CardType type) {
		Card newCard = new Card(itemInitial, itemName, type);
		cards.put(itemName, newCard); // Add card to deck
		// Split the cards up based on type
		switch(type) {
			case ROOM:
				roomCards.add(newCard); // Add card to room card list
				break;
			case PERSON:
				personCards.add(newCard); // Add card to person card list
				break;
			case WEAPON:
				weaponCards.add(newCard); // Add card to weapon card list
				break;
			default:
				break;
		}
	}
	
	// Method to read in the layout from the layout config file
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(layoutConfigFile);
		Scanner in = new Scanner(reader);
		ArrayList<String> firstRead = new ArrayList<>(); // Array of each row
		// Array of Arrays of Strings that contain initial for each cell
		ArrayList<String[]> splitRead = new ArrayList<>();
		
		// Read in each row
		while(in.hasNext() ) {
			String nextString = in.next();
			firstRead.add(nextString);
		}
		
		// Set numRows to the rows read in
		numRows = firstRead.size();
		
		// Add each row, split, to splitRead
		for(String row : firstRead) {
			splitRead.add(row.split(","));
		}
		
		// Check that all rows have the same amount of columns
		for(int row = 0; row < numRows; row++) {
			if(splitRead.get(row).length != splitRead.get(0).length) {
				throw new BadConfigFormatException("Not all rows have the same amount of elements in " + layoutConfigFile);
			}
		}
		
		// Set numColumns to number of elements in one String[]
		numColumns = splitRead.get(0).length;
		
		// Initialize the board
		gameBoard = new BoardCell[numRows][numColumns];
		
		// Set each room's data, based on the string in splitRead
		for(int row = 0; row < numRows; row++) {
			for(int column = 0; column < numColumns; column++) {
				cellSetUp(splitRead, row, column);
			}
		}
		
		in.close();
	}

	/* Method to take a line from the layout file, along with the active cell, and 
	 * format the cell appropriately
	 */
	private void cellSetUp(ArrayList<String[]> splitRead, int row, int column) throws BadConfigFormatException {
		gameBoard[row][column] = new BoardCell(row, column);
		BoardCell cell = gameBoard[row][column];
		char roomInitial = splitRead.get(row)[column].charAt(0);
		
		// Check that the initial is a room that we know of
		if(!(roomMap.containsKey(roomInitial))) {
			throw new BadConfigFormatException("The initial " + roomInitial + " could not be matched with a room. Please check that it is the correct initial.");
		}
		cell.setInitial(roomInitial); // Initial of the room
		cell.setDoorway(DoorDirection.NONE); // Default of no door
		
		// Check if there is an optional char. If there is, store it
		if(splitRead.get(row)[column].length() > 1) {
			char cellOption = splitRead.get(row)[column].charAt(1);
			// Determine what to do based on the optional character
			handleCellOption(cell, roomInitial, cellOption);
		}
	}

	// Method to format a cell further if the cell has an optional character
	private void handleCellOption(BoardCell cell, char roomInitial, char cellOption) {
		switch(cellOption) {
			case '>': // Door right
				cell.setDoorway(DoorDirection.RIGHT);
				break;
			case '^': // Door up
				cell.setDoorway(DoorDirection.UP);
				break;
			case '<': // Door left
				cell.setDoorway(DoorDirection.LEFT);
				break;
			case 'v': // Door down
				cell.setDoorway(DoorDirection.DOWN);
				break;
			case '#': // Room label
				cell.setLabel(true);
				cell.setRoom(true);
				roomMap.get(roomInitial).setLabelCell(cell);
				break;
			case '*': // Room center
				cell.setRoomCenter(true);
				cell.setRoom(true);
				roomMap.get(roomInitial).setCenterCell(cell);
				break;
			default: // Secret passage to another room
				cell.setRoom(true);
				cell.setSecretPassage(cellOption);
				break;
		}
	}
	
	private void generateAdjLists() {
		// Create each cell's adjacency list
		for(int row = 0; row < numRows; row++) {
			for(int column = 0; column < numColumns; column++) {
				BoardCell cell = gameBoard[row][column];
				Character cellType = cell.getInitial();
				// Handle walkway adjacencies
				if(cellType == 'W') {
					if((row - 1) >= 0) {
						addWalkwayAdjacency(row - 1, column, cell);
					}
					if((row + 1) < numRows) {
						addWalkwayAdjacency(row + 1, column, cell);
					}
					if((column - 1) >= 0) {
						addWalkwayAdjacency(row, column - 1, cell);
					}
					if((column + 1) < numColumns) {
						addWalkwayAdjacency(row, column + 1, cell);
					}
					
					// If the cell is a doorway, add the room as an adjacency
					if(cell.isDoorway()) {
						DoorDirection cellDirection = cell.getDoorDirection(); // See where door faces
						switch(cellDirection) {
							case UP:
								addDoorAdjacency(row - 1, column, cell);
								break;
							case DOWN:
								addDoorAdjacency(row + 1, column, cell);
								break;
							case RIGHT:
								addDoorAdjacency(row, column + 1, cell);
								break;
							case LEFT:
								addDoorAdjacency(row, column - 1, cell);
								break;
							default: break;
						}
					}
				}
				
				// Add adjacencies for secret passages
				if(cell.isSecretPassage()) {
					BoardCell roomCenter = roomMap.get(cellType).getCenterCell(); // Center of currnt room
					BoardCell nextCell = roomMap.get(cell.getSecretPassage()).getCenterCell(); // Center of passage room
					roomCenter.addAdjecency(nextCell); // Add passage room to current room adjacency
				}
			}
		}
	}

	// Method to set up adjacencies for walkways
	private void addWalkwayAdjacency(int row, int column, BoardCell cell) {
		BoardCell nextCell = gameBoard[row][column];
		Character nextCellType = nextCell.getInitial();
		if(nextCellType == 'W') {
			cell.addAdjecency(nextCell);
		}
	}

	// Method to set up adjacencies when there is a door
	private void addDoorAdjacency(int row, int column, BoardCell cell) {
		BoardCell nextCell = gameBoard[row][column]; // Directly adjacent cell
		Character nextCellType = nextCell.getInitial(); // Room type
		BoardCell roomCenter = roomMap.get(nextCellType).getCenterCell(); // Room's center
		cell.addAdjecency(roomCenter);
		roomCenter.addAdjecency(cell); // Update the room's adjacency to include the cell with the door
	}
	
	// Method to deal the cards to the players. Will also store three cards in solution
	public void deal() {
		Random rand = new Random();
		deck = new ArrayList<>();
		// Choose the solution randomly
		Card personSol = personCards.get(rand.nextInt(personCards.size()));
		Card roomSol = roomCards.get(rand.nextInt(roomCards.size()));
		Card weaponSol = weaponCards.get(rand.nextInt(weaponCards.size()));
		theAnswer = new Solution(personSol, roomSol, weaponSol);
		
		// Iterate through each card. If it is not in the solution, add it to the deck
		for(Map.Entry<String, Card> card : cards.entrySet()) {
			if(card.getValue() != personSol && card.getValue() != roomSol && card.getValue() != weaponSol) {
				deck.add(card.getValue());
			}
		}
		
		// Deal to the players sequentially
		int playerCounter = 0; // Tracks which player is receiving the card
		while(!deck.isEmpty()) { // Make sure all cards are dealt
			Card randCard = deck.get(rand.nextInt(deck.size())); // Pick a random card from the deck
			Player activePlayer = players.get(playerCounter); // Current player
			randCard.setHolder(activePlayer);
			activePlayer.updateHand(randCard); // Give card to player
			deck.remove(randCard); // Remove card from deck
			playerCounter++; // Move on to next player
			// Cycle back from last to first player
			if(playerCounter == NUM_PLAYERS) {
				playerCounter = 0;
			}
		}
		// Make sure all players can see all card references
		for(Player player : players) {
			player.giveAllCards(cards);
			player.giveAllRooms(roomMap);
		}
	}
	
	// Method to handle a suggestion provided by a player
	public Card handleSuggestion(Player suggestor, Solution suggestion) {
		// Get indexes to track the suggestor and the one attempting to disprove
		int suggestorNumber = players.indexOf(suggestor);
		int currPlayerNum = suggestorNumber;
		// Find which player was suggested so that they can be moved
		Player suggestedPlayer = null;
		for(int i = 0; i < NUM_PLAYERS; i++) {
			if(players.get(i).getName().equals(suggestion.getPerson().getName())) {
				suggestedPlayer = players.get(i);
				break;
			}
		}
		BoardCell oldCell = getCell(suggestedPlayer.getRow(), suggestedPlayer.getColumn());
		oldCell.setOccupied(false); // Mark that their old cell is not unoccupied
		suggestedPlayer.setCurrentCell(suggestor.getCurrentCell()); // Move the player the the room
		repaint();
		while(true) {
			currPlayerNum++; // Move on to next player
			// Loop back around to player 0
			if(currPlayerNum == players.size()) {
				currPlayerNum = 0;
			}
			// If it gets back to the suggestor, no one can disprove
			if(currPlayerNum == suggestorNumber) {
				return null;
			}
			// Let each sequential player attempt to disprove
			Card disproveAttempt = players.get(currPlayerNum).disproveSuggestion(suggestion);
			// Return the first card that disproves the suggestion
			if(disproveAttempt != null) {
				return disproveAttempt;
			}
		}
	}
	
	// Method to check an accusation against the solution
	public boolean checkAccusation(Solution accusation) {
		return theAnswer.isEqual(accusation);
	}
	
	// Method to get the adjacency list of a given cell
	public Set<BoardCell> getAdjList(int row, int col) {
		return gameBoard[row][col].getAdjList();
	}
	
	/*
	 * Public method that will initialize new visited and targets sets, and
	 * will send them into the private function findAllTargets
	 */
	public void calcTargets(BoardCell startCell, int roll) {
		visited.clear();
		targets.clear();
		visited.add(startCell); // The current cell cannot be returned to
		findAllTargets(startCell, roll);
	}
	
	/*
	 * Private method to recursively find all targets for a cell, given the
	 * roll (pathLength). Must be accessed via calcTargets
	 */
	private void findAllTargets(BoardCell startCell, int pathLength) {
		// For each cell adjacent to the start cell:
		for(BoardCell cell : startCell.getAdjList()) {
			// If the cell has already been visited or the cell is occupied:
			if(visited.contains(cell) || (cell.isOccupied() && !cell.isRoomCenter())) {
				// Return to loop, do not move to cell
				continue;
			}
			visited.add(cell);
			// If no more moves or in room:
			if(pathLength == 1 || cell.isRoom()) {
				targets.add(cell);
			}
			// Recursive call:
			else {
				findAllTargets(cell, pathLength - 1);
			}
			visited.remove(cell); // Cleanup
		}
	}
	
	// Method to cause the gameboard to be drawn
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Determine how big each cell should be based on the size of the board
		int cellWidth = getWidth() / numColumns;
		int cellHeight = getHeight() / numRows;
		
		// Tell each cell to draw itself, giving it expected height and width
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				boolean flag = false;
				BoardCell cell = gameBoard[i][j];
				if(targets.contains(getRoomCellCenter(cell)) || targets.contains(cell))
					flag = true;
				cell.draw(g, cellWidth, cellHeight, flag);
			}
		}
		
		// Tell each label cell to draw its label, and each door cell to draw its door
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				BoardCell currCell = gameBoard[i][j];
				// If it is a label cell
				if(currCell.isLabel()) {
					// Get the name of the room of the cell
					Room currRoom = roomMap.get(currCell.getInitial());
					currCell.drawLabel(g, currRoom.getName(), cellWidth, cellHeight);
				}
				// If it is a door cell
				if(currCell.isDoorway())
					currCell.drawDoor(g, cellWidth, cellHeight);
			}
		}
		
		// Tell each player to draw themselves, with offset if necessary
		int offset = cellWidth / 2; // Amount to be offset by
		for(int i = 0; i < NUM_PLAYERS; i++) {
			int currOffset = 0; // Initial offset
			Player currPlayer = players.get(i);
			// Check only players prior, adding one offset for each player in the same cell
			for(int j = 0; j < i; j++) {
				Player comparePlayer = players.get(j);
				if(currPlayer.getCurrentCell().equals(comparePlayer.getCurrentCell()))
					currOffset += offset;
			}
			// Draw themselves
			currPlayer.draw(g, cellWidth, cellHeight, currOffset);
		}
	}
	
	public void generateSuggestionDialog(Room currRoom) {
		suggestionDialog = new SuggestionDialog(currRoom);
		suggestionDialog.setVisible(true);
	}
	
	public void generateAccusationDialog() {
		accusationDialog = new AccusationDialog();
		accusationDialog.setVisible(true);
	}
	
	// Class to allow for suggestions
	public class SuggestionDialog extends JDialog {
		private JComboBox<String> person;
		private JComboBox<String> weapon;
		private JLabel room;
		
		public SuggestionDialog(Room room) {
			setTitle("Make a Suggestion");
			setSize(300, 300);
			setLayout(new GridLayout(4, 2));
			// The only allowed suggestion is the current room
			JLabel roomLabel = new JLabel("Room:");
			this.room = new JLabel(room.getName());
			add(roomLabel);
			add(this.room);
			// Generate persons from person cards
			JLabel personLabel = new JLabel("Person:");
			person = new JComboBox<>();
			for(Card personCard : personCards) {
				person.addItem(personCard.getName());
			}
			add(personLabel);
			add(person);
			// Generate weapons from weapon cards
			JLabel weaponLabel = new JLabel("Weapon:");
			weapon = new JComboBox<>();
			for(Card weaponCard : weaponCards) {
				weapon.addItem(weaponCard.getName());
			}
			add(weaponLabel);
			add(weapon);
			// Button to submit suggestion
			JButton submitButton = new JButton("Submit");
			submitButton.addActionListener(new SubmitListener());
			add(submitButton);
			
			setLocationRelativeTo(null); // Makes it so dialog launches at center of screen
		}
		
		class SubmitListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// Generate a suggestion based on the selected values
				Card selectedPerson = cards.get(person.getSelectedItem().toString());
				Card selectedRoom = cards.get(room.getText());
				Card selectedWeapon = cards.get(weapon.getSelectedItem().toString());
				// Pass the suggestion to the engine
				engine.humanSuggestion(selectedPerson, selectedRoom, selectedWeapon);
			}
		}
	}
	
	// Class to allow for accusations
	public class AccusationDialog extends JDialog {
		private JComboBox<String> person;
		private JComboBox<String> room;
		private JComboBox<String> weapon;
		
		public AccusationDialog() {
			setTitle("Make an Accusation");
			setSize(300, 300);
			setLayout(new GridLayout(4, 2));
			// Generate rooms from room cards
			JLabel roomLabel = new JLabel("Room:");
			room = new JComboBox<>();
			for(Card roomCard : roomCards) {
				room.addItem(roomCard.getName());
			}
			add(roomLabel);
			add(this.room);
			// Generate persons from person cards
			JLabel personLabel = new JLabel("Person:");
			person = new JComboBox<>();
			for(Card personCard : personCards) {
				person.addItem(personCard.getName());
			}
			add(personLabel);
			add(person);
			// Generate weapons from weapon cards
			JLabel weaponLabel = new JLabel("Weapon:");
			weapon = new JComboBox<>();
			for(Card weaponCard : weaponCards) {
				weapon.addItem(weaponCard.getName());
			}
			add(weaponLabel);
			add(weapon);
			// Button to submit accusation
			JButton submitButton = new JButton("Submit");
			submitButton.addActionListener(new SubmitListener());
			add(submitButton);
			// Button to cancel accusation
			JButton cancelButton = new JButton("Cancel");
			add(cancelButton);
			
			setLocationRelativeTo(null); // Makes it so dialog launches at center of screen
		}
		
		class SubmitListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// Generate a suggestion based on the selected values
				Card selectedPerson = cards.get(person.getSelectedItem().toString());
				Card selectedRoom = cards.get(room.getSelectedItem().toString());
				Card selectedWeapon = cards.get(weapon.getSelectedItem().toString());
				// Pass the suggestion to the engine
				engine.humanAccusation(selectedPerson, selectedRoom, selectedWeapon);
			}
		}
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public BoardCell getCell(int row, int col) {
		return gameBoard[row][col];
	}
	
	public Map<Character, Room> getRoomMap() {
		return roomMap;
	}
	
	public Room getRoom(Character initial) {
		return roomMap.get(initial);
	}
	
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public Player getPlayer(int playerNum) {
		return players.get(playerNum);
	}
	
	public Card getCard(String name) {
		return cards.get(name);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) { /* Not needed function */ }
	
	// Method to handle the mouse being clicked
	@Override
	public void mousePressed(MouseEvent e) {
		// If it is not the player's turn, do nothing
		if(engine.getCurrPlayer() == 0) {
			// Figure out where the player clicked
			BoardCell whichCell = null;
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numColumns; j++) {
					if(gameBoard[i][j].containsClick(e.getX(), e.getY())) {
						whichCell = gameBoard[i][j];
						break;
					}
				}
			}
			
			// Handle whether the box was a target or not
			if(targets.contains(whichCell))
				engine.humanMove(whichCell);
			// If the clicked cell is a cell in an allowed room
			else if(targets.contains(getRoomCellCenter(whichCell)))
				engine.humanMove(getRoomCellCenter(whichCell));
			else
				JOptionPane.showMessageDialog(null, "Please select a valid target.");
		}
	}

	// Method to find the center of a room based on any room cell
	private BoardCell getRoomCellCenter(BoardCell roomCell) {
		return roomMap.get(roomCell.getInitial()).getCenterCell();
	}

	@Override
	public void mouseReleased(MouseEvent e) { /* Not needed function */ }

	@Override
	public void mouseEntered(MouseEvent e) { /* Not needed function */ }

	@Override
	public void mouseExited(MouseEvent e) { /* Not needed function */ }
	
	public Solution getSolution() {
		return theAnswer;
	}
	
	// The below getters are used for testing
	public List<Player> getPlayers() {
		return players;
	}
	
	public Map<String, Card> getCards() {
		return cards;
	}
	
	public List<Card> getDeck() {
		return deck;
	}
}