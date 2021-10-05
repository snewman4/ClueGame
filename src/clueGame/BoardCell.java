package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row, col;
	private char initial, secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel, roomCenter, isRoom, isOccupied;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.col = column;
		adjList = new HashSet<BoardCell>();
	}
	
	public void addAdjecency(BoardCell cell) {
		adjList.add(cell);
	}
	
	public Set<BoardCell> getAdjList() {
		return adjList;
	}
	
	public void setRoom(boolean isRoom) {
		this.isRoom = isRoom;
	}
	
	public boolean isRoom() {
		return isRoom;
	}
	
	public void setOccupied(boolean occupied) {
		this.isOccupied = occupied;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	public boolean isDoorway() {
		if(doorDirection == DoorDirection.NONE) {
			return false;
		}
		return true;
	}
	
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	
	public Character getInitial() {
		return initial;
	}
	
	public Character getSecretPassage() {
		return secretPassage;
	}
}