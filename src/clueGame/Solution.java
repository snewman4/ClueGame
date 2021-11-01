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
