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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Board {
	private static final int TOTAL_COLUMNS = 25;
	private static final int TOTAL_ROWS = 25;
	private int numRows, numColumns;
	private BoardCell[][] board;
	private String layoutConfigFile, setupConfigFile;
	private Map<Character, Room> roomMap;
	private static Board theInstance = new Board(); // Private, single instance of board
	
	// Board constructor, can only be accessed by this class
	private Board() {
		super();
		this.numRows = TOTAL_ROWS;
		this.numColumns = TOTAL_COLUMNS;
	}
	
	// Initialize the board
	public void initialize() {
		board = new BoardCell[numRows][numColumns];
		roomMap = new HashMap<Character, Room>();
		// Initialize each cell on the board
		for(int row = 0; row < numRows; row++) {
			for(int column = 0; column < numColumns; column++) {
				board[row][column] = new BoardCell(row, column);
			}
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
	
	public void loadSetupConfig() {
		
	}
	
	public void loadLayoutConfig() {
		
	}
	
	public BoardCell getCell(int row, int col) {
		return board[row][col];
	}
	
	public Room getRoom(Character initial) {
		return new Room('T', "Test");
	}
	
	public Room getRoom(BoardCell cell) {
		return new Room('T', "Test");
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
}
