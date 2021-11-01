package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	// Array of pre-set starting locations
	private static final int[][] startLocation = {{0, 8}, {0, 16}, {12, 24}, {24, 16}, {24, 8}, {12, 0}};
	// Array of pre-set colors
	private static final Color[] playerColors = {Color.RED, Color.PINK, Color.GREEN, Color.BLACK, Color.YELLOW, Color.BLUE};
	private static int currPlayerNum = 0; // Which player is currently being made, used to access correct starting location
	
	private String name;
	private Color color;
	protected int row;
	protected int column;
	protected Set<Card> hand;
	
	protected Player(String name) {
		super();
		this.name = name;
		this.hand = new HashSet<>();
		// Use the pre-set arrays to define individual players' starting locations and colors
		this.color = playerColors[currPlayerNum];
		this.row = startLocation[currPlayerNum][0];
		this.column = startLocation[currPlayerNum][1];
		currPlayerNum++; // Update playerNum so next instance uses makes appropriate player
		// Cycle back through indexes, don't go out of bounds
		if(currPlayerNum == 6) {
			currPlayerNum = 0;
		}
	}
	
	public abstract void updateHand(Card card);
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Set<Card> getHand() {
		return hand;
	}
}
