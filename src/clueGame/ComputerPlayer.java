/**
 * Computer Player Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class represents a computer player, which
 * is a child of Player. It holds the computer player's
 * hand, and handles their functions.
 */

package clueGame;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	
	public ComputerPlayer(String name) {
		super(name);
	}
	
	public BoardCell selectTarget(Set<BoardCell> targetList) {
		Random rand = new Random();
		ArrayList<BoardCell> randTargets = new ArrayList<>();
		for(BoardCell cell : targetList) {
			// If the board cell is a room, get the corresponding room card
			if(cell.isRoomCenter()) {
				Room currentRoom = roomMap.get(cell.getInitial());
				Card corrCard = allCards.get(currentRoom.getName());
				// If the room is known, add it to randomly selectable options
				if(hand.contains(corrCard) || seen.contains(corrCard)) {
					randTargets.add(cell);
				}
				// If it is not known, go to this target
				else {
					return cell;
				}
			}
			// If target is not a room, just add it to list
			else {
				randTargets.add(cell);
			}
		}
		// Randomly select and return a target
		return randTargets.get(rand.nextInt(randTargets.size()));
	}
	
	public Solution createSuggestion() {
		Random rand = new Random();
		// Data structures to store unseen cards by type
		ArrayList<Card> unseenPerson = new ArrayList<>();
		ArrayList<Card> unseenWeapon = new ArrayList<>();
		// Check each card in the deck
		for(Map.Entry<String, Card> entry : allCards.entrySet()) {
			Card card = entry.getValue();
			// If it is already known, skip it
			if(seen.contains(card) || hand.contains(card)) {
				continue;
			}
			// Otherwise, split it up by type
			switch(card.getType()) {
				case PERSON:
					unseenPerson.add(card);
					break;
				case WEAPON:
					unseenWeapon.add(card);
					break;
				// We don't need to worry about room cards, as it must suggest the room it is in
				default:
					break;
			}
		}
		// Determine which room the player is currently in, get the matching card
		Room currRoom = findCurrentRoom();
		Card currRoomCard = allCards.get(currRoom.getName());
		// Select random cards from the unseen cards
		Card suggPersonCard = unseenPerson.get(rand.nextInt(unseenPerson.size()));
		Card suggWeaponCard = unseenWeapon.get(rand.nextInt(unseenWeapon.size()));
		// Create and return the suggestion
		return new Solution(suggPersonCard, currRoomCard, suggWeaponCard);
	}
	
	// TODO: Write the createAccusation method
	// Method to generate an accusation if possible. If not, return null
	public Solution createAccusation() {
		return null;
	}
}
