/**
 * Test Board Class
 * 
 * @author Sam
 *
 * Class to create a 4x4 test board using the test board
 * cells. This board will be used to test the movement algorithm.
 */
package experiment;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TestBoard {
	private int numRows;
	private int numCols;
	private TestBoardCell[][] board;
	private Set<TestBoardCell> targets;
	
	public TestBoard(int numRows, int numCols) {
		super();
		this.numRows = numRows;
		this.numCols = numCols;
		board = new TestBoardCell[numRows][numCols];
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				board[i][j] = new TestBoardCell(i, j);
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int column) {
		return board[row][column];
	}
}
