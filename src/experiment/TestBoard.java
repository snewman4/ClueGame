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

public class TestBoard {
	private Set<TestBoardCell> board;
	private Set<TestBoardCell> targets;
	private Map<TestBoardCell, Set<TestBoardCell>> adjMatrix;
	
	public TestBoard() {
		super();
		
	}
	
	public void calcTargets(TestBoardCell startCell, int pathLength) {
		
	}
	
	public Set<TestBoardCell> getTargets() {
		return targets;
	}
	
	public TestBoardCell getCell(int row, int column) {
		
	}
}
