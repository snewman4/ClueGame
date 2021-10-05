package clueGame;

import java.util.HashMap;
import java.util.Map;

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
	
	public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
		this.layoutConfigFile = layoutConfigFile;
		this.setupConfigFile = setupConfigFile;
	}
	
	// Return the only board
	public static Board getInstance() {
		return theInstance;
	}
	
	// Initialize the board
	public void initialize() {
		board = new BoardCell[numRows][numColumns];
		HashMap<Character, Room> roomMap = new HashMap<Character, Room>();
		// Initialize each cell on the board
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				board[i][j] = new BoardCell(i, j);
			}
		}
		
		// Create each cell's adjacency list
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				BoardCell cell = board[i][j];
				if((i - 1) >= 0) {
					cell.addAdjecency(board[i-1][j]);
				}
				if((i + 1) < numRows) {
					cell.addAdjecency(board[i+1][j]);
				}
				if((j - 1) >= 0) {
					cell.addAdjecency(board[i][j-1]);
				}
				if((j + 1) < numColumns) {
					cell.addAdjecency(board[i][j+1]);
				}
			}
		}
	}
	
	public void loadSetupConfig() {
		
	}
	
	public void loadLayoutConfig() {
		
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
