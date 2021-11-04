/**
 * Solution Class
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This class holds the solution to the game.
 */

package clueGame;

public class Solution {
	private Card person;
	private Card room;
	private Card weapon;
	
	public Solution(Card person, Card room, Card weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	
	public boolean equals(Solution target) {
		return (this.person == target.person && this.room == target.room && this.weapon == target.weapon);
	}
	
	public Card getPerson() {
		return person;
	}
	
	public Card getRoom() {
		return room;
	}
	
	public Card getWeapon() {
		return weapon;
	}
}
