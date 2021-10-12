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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	private int numRows, numColumns;
	private BoardCell[][] board;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board(); // Private, single instance of board
	private Set<BoardCell> targets; // Used to find the targets a certain cell has
	private Set<BoardCell> visited; // Used to store which cells were visited in a turn
	
	// Board constructor, can only be accessed by this class
	private Board() {
		super();
	}
	
	// Initialize the board
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (BadConfigFormatException e) {
			e.getMessage();
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
	
	// Method to read in the setup from the setup config file
	public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
		roomMap = new HashMap<Character, Room>();
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
			String cellType = splitRead[0]; // Type of cell we are creating
			String cellName = splitRead[1]; // Name of that cell
			Character cellInitial = splitRead[2].charAt(0); // Initial of that cell
			
			// If the cellType is a room, create a room with the name and initial
			if(cellType.equals("Room") || cellType.equals("Space")) {
				Room newRoom = new Room(cellInitial, cellName);
				roomMap.put(cellInitial, newRoom); // Add to room map
			}
			else {
				throw new BadConfigFormatException("Could not evaluate type in " + setupConfigFile);
			}
		}
		
		in.close();
	}
	
	// Method to read in the layout from the layout config file
	public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(layoutConfigFile);
		Scanner in = new Scanner(reader);
		ArrayList<String> firstRead = new ArrayList<String>(); // Array of each row
		// Array of Arrays of Strings that contain initial for each cell
		ArrayList<String[]> splitRead = new ArrayList<String[]>();
		
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
		board = new BoardCell[numRows][numColumns];
		
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
		board[row][column] = new BoardCell(row, column);
		BoardCell cell = board[row][column];
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
				BoardCell cell = board[row][column];
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
		BoardCell nextCell = board[row][column];
		Character nextCellType = nextCell.getInitial();
		if(nextCellType == 'W') {
			cell.addAdjecency(nextCell);
		}
	}

	// Method to set up adjacencies when there is a door
	private void addDoorAdjacency(int row, int column, BoardCell cell) {
		BoardCell nextCell = board[row][column]; // Directly adjacent cell
		Character nextCellType = nextCell.getInitial(); // Room type
		BoardCell roomCenter = roomMap.get(nextCellType).getCenterCell(); // Room's center
		cell.addAdjecency(roomCenter);
		roomCenter.addAdjecency(cell); // Update the room's adjacency to include the cell with the door
	}
	
	// Method to get the adjacency list of a given cell
	public Set<BoardCell> getAdjList(int row, int col) {
		return board[row][col].getAdjList();
	}
	
	/*
	 * Public method that will initialize new visited and targets sets, and
	 * will send them into the private function findAllTargets
	 */
	public void calcTargets(BoardCell startCell, int roll) {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
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

	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public BoardCell getCell(int row, int col) {
		return board[row][col];
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
}
