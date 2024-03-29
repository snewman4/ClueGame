/**
 * Test Board Cell Class
 * 
 * @author Sam Newman
 * 
 * Class to create a test board cell, which stores various relevant
 * information and will be used to test the movement algorithm.
 */

package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
	// Cell's location on board:
	private int row, column;
	private Set<TestBoardCell> adjacencyList; // Cells adjacent to this
	private boolean isRoom, occupied; // If cell is in a room or not, if cell is occupied or not
	
	public TestBoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		adjacencyList = new HashSet<TestBoardCell>();
	}
	
	public void addAdjecency(TestBoardCell cell) {
		adjacencyList.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList() {
		return adjacencyList;
	}
	
	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
}
