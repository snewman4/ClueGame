package clueGame;

public class Board {
	private int numRows;
	private int numCols;
	private BoardCell[][] board;
	private String layoutConfigFile;
	private String setupConfigFile;
	private static Board theInstance = new Board(); // Private, single instance of board
	
	// Board constructor, can only be accessed by this class
	private Board() {
		super();
	}
	
	// Return the only board
	public static Board getInstance() {
		return theInstance;
	}
	
	// Initialize the board
	public void initialize() {
		
	}
	
	public void loadSetupConfig() {
		
	}
	
	public void loadLayoutConfig() {
		
	}
}
