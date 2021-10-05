/**
 * Test Board Class
 * 
 * @author Sam
 *
 * Class to create a 4x4 test board using the test board
 * cells. This board will be used to test the movement algorithm.
 */
package experiment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TestBoard {
	private int numRows;
	private int numCols;
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	private Set<TestBoardCell> visited;
	
	public TestBoard(int numRows, int numCols) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		board = new TestBoardCell[numRows][numCols];
		// Initialize each cell on the board
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}
		
		// Create each cell's adjacency list
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				TestBoardCell cell = board[i][j];
				if((i - 1) >= 0)
					cell.addAdjecency(board[i-1][j]);
				if((i + 1) < numRows)
					cell.addAdjecency(board[i+1][j]);
				if((j - 1) >= 0)
					cell.addAdjecency(board[i][j-1]);
				if((j + 1) < numCols)
					cell.addAdjecency(board[i][j+1]);
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		visited.add(startCell);
		for(TestBoardCell cell : startCell.getAdjList()) {
			if(visited.contains(cell))
				continue;
			visited.add(cell);
			if(pathLength == 1)
				targets.add(cell);
			else
				calcTargets(cell, pathLength - 1);
			visited.remove(cell);
		}
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int column) {
		return board[row][column];
	}
}
