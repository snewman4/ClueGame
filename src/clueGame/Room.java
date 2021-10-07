/**
 * Room Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class stores information about each room on the board
 */

package clueGame;

public class Room {
	private Character initial;
	private String name;
	private BoardCell centerCell, labelCell;
	
	public Room(Character initial, String name) {
		super();
		this.initial = initial;
		this.name = name;
	}
	
	public Character getInitial() {
		return initial;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCenterCell(BoardCell cell) {
		this.centerCell = cell;
	}
	
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public void setLabelCell(BoardCell cell) {
		this.labelCell = cell;
	}
	
	public BoardCell getLabelCell() {
		return labelCell;
	}
}
