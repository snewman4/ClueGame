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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private static final int FONT_SIZE = 20;
	private int row;
	private int column;
	private int cellWidth;
	private int cellHeight;
	private char initial;
	private char secretPassage;
	private DoorDirection doorDirection;
	private boolean roomLabel;
	private boolean roomCenter;
	private boolean isRoom;
	private boolean isOccupied;
	private int numOccupied;
	private boolean isSecretPassage;
	private Set<BoardCell> adjList;
	
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		
		adjList = new HashSet<>();
	}
	
	// Method to for each cell to draw itself
	public void draw(Graphics g, int cellWidth, int cellHeight, boolean flag) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
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
				// If the cell is a target, make it blue
				if(flag)
					g.setColor(Color.CYAN);
				else
					g.setColor(Color.PINK);
				g.fillRect(x, y, cellWidth, cellHeight);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, cellWidth, cellHeight);
				break;
			default: // If the cell is a room
				// If the room is a target, make it blue
				if(flag)
					g.setColor(Color.CYAN);
				else
					g.setColor(Color.GRAY);
				g.fillRect(x, y, cellWidth, cellHeight);
				break;
		}
	}
	
	// Method for boardcells that are labels to draw their label
	public void drawLabel(Graphics g, String label, int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		// Try to position the center of the text in the center of the cell
		int x = (column * cellWidth) - (cellWidth / 2);
		int y = (row * cellHeight) + (cellHeight / 2);
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE));
		g.drawString(label, x, y);
	}
	
	// Method to draw a doorway
	public void drawDoor(Graphics g, int cellWidth, int cellHeight) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		Graphics2D g2 = (Graphics2D) g; // Convert to 2D graphics so we can change size of line
		int x1 = 0;
		int x2 = 0;
		int y1 = 0;
		int y2 = 0;
		// Determine how to draw the door
		switch(doorDirection) {
			case RIGHT:
				x1 = column * cellWidth + cellWidth;
				x2 = x1;
				y1 = row * cellHeight;
				y2 = y1 + cellHeight;
				break;
			case LEFT:
				x1 = column * cellWidth;
				x2 = x1;
				y1 = row * cellHeight;
				y2 = y1 + cellHeight;
				break;
			case UP:
				x1 = column * cellWidth;
				x2 = x1 + cellWidth;
				y1 = row * cellHeight;
				y2 = y1;
				break;
			case DOWN:
				x1 = column * cellWidth;
				x2 = x1 + cellWidth;
				y1 = row * cellHeight + cellHeight;
				y2 = y1;
				break;
			default:
				break;
		}
		// Make doors represented by a thick blue line
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(5));
		g2.drawLine(x1, y1, x2, y2);
	}
	
	// Method to determine if a click was within the cell
	public boolean containsClick(int mouseX, int mouseY) {
		int x = column * cellWidth;
		int y = row * cellHeight;
		Rectangle rect = new Rectangle(x, y, cellWidth, cellHeight);
		return rect.contains(new Point(mouseX, mouseY));
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
	
	// Method to update whether the cell is occupied
	public void setOccupied(boolean isOccupied) {
		// The counter is used for rooms that can be occupied by multiple players
		if(isOccupied)
			numOccupied++;
		else
			numOccupied--;
		this.isOccupied = isOccupied;
	}
	
	public boolean isOccupied() {
		return isOccupied;
	}
	
	public int getNumOccupied() {
		return numOccupied;
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