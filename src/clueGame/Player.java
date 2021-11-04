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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
	protected Set<Card> seen;
	
	protected Player(String name) {
		super();
		this.name = name;
		this.hand = new HashSet<>();
		this.seen = new HashSet<>();
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
	
	// This is used for testing only
	public void clearHand() {
		hand.clear();
	}
	public void removeCard(Card card) {
		hand.remove(card);
	}
	
	public static void reset() {
		currPlayerNum = 0;
	}
}
