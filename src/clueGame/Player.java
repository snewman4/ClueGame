/**
 * Player Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class represents a player, and is an abstract
 * class with children HumanPlayer and ComputerPlayer.
 * It holds information necessary to every player.
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	// Array of pre-set starting locations
	private static final int[][] startLocation = {{0, 8}, {0, 16}, {12, 24}, {24, 16}, {24, 8}, {12, 0}};
	// Array of pre-set colors
	private static final Color[] playerColors = {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
	private static int currPlayerNum = 0; // Which player is currently being made, used to access correct starting location
	
	private String name;
	private Color color;
	protected int row;
	protected int column;
	protected Set<Card> hand;
	protected Set<Card> seen;
	protected Map<String, Card> allCards;
	protected Map<Character, Room> roomMap;
	
	protected Player(String name) {
		super();
		this.name = name;
		this.hand = new HashSet<>();
		this.seen = new HashSet<>();
		this.allCards = new HashMap<>();
		this.roomMap = new HashMap<>();
		// Use the pre-set arrays to define individual players' starting locations and colors
		this.color = playerColors[currPlayerNum];
		this.row = startLocation[currPlayerNum][0];
		this.column = startLocation[currPlayerNum][1];
		currPlayerNum++; // Update playerNum so next instance uses makes appropriate player
		// Cycle back through indexes, don't go out of bounds
		if(currPlayerNum == 6) {
			reset();
		}
	}
	
	public BoardCell getCurrentCell() {
		return new BoardCell(row, column);
	}
	
	public void setCurrentCell(BoardCell cell) {
		row = cell.getRow();
		column = cell.getColumn();
	}
	
	// Method to disprove a suggestion made by another player
	public Card disproveSuggestion(Solution suggestion) {
		Random rand = new Random();
		// Get suggested cards
		Card suggPerson = suggestion.getPerson();
		Card suggRoom = suggestion.getRoom();
		Card suggWeapon = suggestion.getWeapon();
		Card returnCard = null;
		ArrayList<Card> matching = new ArrayList<>(); // List of cards that are in both suggestion and hand
		
		// Add matching cards to the list
		if(hand.contains(suggPerson))
			matching.add(suggPerson);
		if(hand.contains(suggRoom))
			matching.add(suggRoom);
		if(hand.contains(suggWeapon))
			matching.add(suggWeapon);
		
		// Randomly select one of the matching cards
		if(!matching.isEmpty()) {
			returnCard = matching.get(rand.nextInt(matching.size()));
		}
		return returnCard;
	}
	
	// Method to identify which room the player is in. If not in a room, returns null
	public Room findCurrentRoom() {
		// Check each room in the roomMap
		for(Map.Entry<Character, Room> entry : roomMap.entrySet()) {
			// Don't check walkways or closets
			if(entry.getValue().getInitial() == 'W' || entry.getValue().getInitial() == 'X')
				continue;
			BoardCell checkRoomCell = entry.getValue().getCenterCell();
			// If the player is in the current check room, return the room
			if(checkRoomCell.getRow() == row && checkRoomCell.getColumn() == column) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	// Method to reset the currPlayerNum
	public static void reset() {
		currPlayerNum = 0;
	}
	
	// Method to allow the player to draw itself
	public void draw(Graphics g, int cellWidth, int cellHeight, int offset) {
		// Determine where to draw the player based on cell sizes and row and column
		int x = column * cellWidth + offset;
		int y = row * cellHeight;
		
		g.setColor(color);
		g.fillOval(x, y, cellWidth, cellHeight);
	}
	
	// Method to pass the player the references to all cards
	public void giveAllCards(Map<String, Card> allCards) {
		this.allCards = allCards;
	}
	
	// Method to pass the player the references to all rooms
	public void giveAllRooms(Map<Character, Room> roomMap) {
		this.roomMap = roomMap;
	}
	
	// Getters and Setters
	public void updateHand(Card card) {
		hand.add(card);
	}
	
	public void updateSeen(Card card) {
		seen.add(card);
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
	
	public int getColumn() {
		return column;
	}
	
	public Set<Card> getHand() {
		return hand;
	}
	
	public Set<Card> getSeen() {
		return seen;
	}
	
	// This is used for testing only
	public void clearHand() {
		hand.clear();
	}
	public void removeCard(Card card) {
		hand.remove(card);
	}

	protected abstract Solution createAccusation();
	protected abstract BoardCell selectTarget(Set<BoardCell> targets);
	protected abstract Solution createSuggestion();
}
