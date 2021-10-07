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
import java.util.Map;
import java.util.Scanner;

public class Board {
	private int numRows, numColumns;
	private BoardCell[][] board;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board(); // Private, single instance of board
	
	// Board constructor, can only be accessed by this class
	private Board() {
		super();
	}
	
	// Initialize the board
	public void initialize() {
		roomMap = new HashMap<Character, Room>();

		try {
			loadSetupConfig();
		} catch (FileNotFoundException e1) {
			e1.getMessage();
		}
		try {
			loadLayoutConfig();
		} catch (FileNotFoundException e) {
			e.getMessage();
		}

		generateAdjLists();
	}

	private void generateAdjLists() {
		// Create each cell's adjacency list
		for(int row = 0; row < numRows; row++) {
			for(int column = 0; column < numColumns; column++) {
				BoardCell cell = board[row][column];
				if((row - 1) >= 0) {
					cell.addAdjecency(board[row-1][column]);
				}
				if((row + 1) < numRows) {
					cell.addAdjecency(board[row+1][column]);
				}
				if((column - 1) >= 0) {
					cell.addAdjecency(board[row][column-1]);
				}
				if((column + 1) < numColumns) {
					cell.addAdjecency(board[row][column+1]);
				}
			}
		}
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
	public void loadSetupConfig() throws FileNotFoundException {
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
			if(cellType.equals("Room")) {
				Room newRoom = new Room(cellInitial, cellName);
				roomMap.put(cellInitial, newRoom); // Add to room map
			}
		}
	}
	
	// Method to read in the layout from the layout config file
	public void loadLayoutConfig() throws FileNotFoundException {
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
		
		// Set numColumns to number of elements in one String[]
		numColumns = splitRead.get(0).length;
		
		// Initialize the board
		board = new BoardCell[numRows][numColumns];
		
		// Set each room's data, based on the string in splitRead
		for(int row = 0; row < numRows; row++) {
			for(int column = 0; column < numColumns; column++) {
				board[row][column] = new BoardCell(row, column);
				char roomInitial = splitRead.get(row)[column].charAt(0);
				board[row][column].setInitial(roomInitial);
				
				// Check if there is an optional char. If there is, store it
				if(splitRead.get(row)[column].length() > 1) {
					char cellOption = splitRead.get(row)[column].charAt(1);
					switch(cellOption) {
						case '>':
							board[row][column].setDoorway(DoorDirection.RIGHT);
							break;
						case '^':
							board[row][column].setDoorway(DoorDirection.UP);
							break;
						case '<':
							board[row][column].setDoorway(DoorDirection.LEFT);
							break;
						case 'v':
							board[row][column].setDoorway(DoorDirection.DOWN);
							break;
						case '#':
							board[row][column].setLabel(true);
							break;
						case '*':
							board[row][column].setRoomCenter(true);
							break;
					}
				}
			}
		}
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
