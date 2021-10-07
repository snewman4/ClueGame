package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row, column;
	private char initial, secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel, roomCenter, isRoom, isOccupied;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		adjList = new HashSet<BoardCell>();
	}
	
	// All of the getters and setters for BoardCell class	
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
	
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public void setLabel(boolean roomLabel) {
		this.roomLabel = roomLabel;
	}
	
	public boolean isLabel() {
		return roomLabel;
	}
	
	public void setRoomCenter(boolean roomCenter) {
		this.roomCenter = roomCenter;
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
	
	public void setInitial(Character initial) {
		this.initial = initial;
	}
	
	public Character getInitial() {
		return initial;
	}
	
	public void setSecretPassage(Character secretPassage) {
		this.secretPassage = secretPassage;
	}
	
	public Character getSecretPassage() {
		return secretPassage;
	}
}