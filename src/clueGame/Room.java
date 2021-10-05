package clueGame;

public class Room {
	private String name;
	private BoardCell centerCell, labelCell;
	
	public String getName() {
		return name;
	}
	
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	public BoardCell getLabelCell() {
		return labelCell;
	}
}
