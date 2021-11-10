/**
 * Board Cell Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class creates the individual cells that make up the game board,
 * and stores relevant information about them
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean isRoom;
	private boolean isOccupied;
	private boolean isSecretPassage;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		
		adjList = new HashSet<>();
	}
	
	// Method to for each cell to draw itself
	public void draw(Graphics g, int cellWidth, int cellHeight) {
		// Determine where to draw the cell based on cell sizes and its row and column
		int x = column * cellWidth;
		int y = row * cellHeight;
		// Determine what color to draw the cell based on the type of cell
		switch(initial) {
			case 'X': // If the cell is inaccessible
				g.setColor(Color.BLACK);
				g.fillRect(x, y, cellWidth, cellHeight);
				break;
			case 'W': // If the cell is a walkway
				g.setColor(Color.YELLOW);
				g.fillRect(x, y, cellWidth, cellHeight);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, cellWidth, cellHeight);
				break;
			default: // If the cell is a room
				g.setColor(Color.GRAY);
				g.fillRect(x, y, cellWidth, cellHeight);
				break;
		}
	}
	
	// All of the getters and setters for BoardCell class
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
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
	
	public void setDoorway(DoorDirection direction) {
		this.doorDirection = direction;
	}
	
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
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
		this.isSecretPassage = true;
	}
	
	public boolean isSecretPassage() {
		return isSecretPassage;
	}
	
	public Character getSecretPassage() {
		return secretPassage;
	}
}