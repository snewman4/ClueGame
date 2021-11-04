package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.*;

class ComputerAITest {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		// Deal the cards so that a hand is given to every player, and a solution is chosen
		board.deal();
	}

	/*
	 * Test the creation of a computer suggestion
	 */
	@Test
	public void testComputerSuggestion() {
		Player testPlayer = new ComputerPlayer("Test");
		Map<String, Card> preDealDeck = board.getCards();
		// Set the player to location (4, 2), which is the center of the Batcave room
		testPlayer.setRow(4);
		testPlayer.setColumn(2);
		Card expectedRoom = preDealDeck.get("Batcave");
		
		// Update the cards that the player has already seen
		Card seenPerson = preDealDeck.get("Sergeant Sam");
		Card seenRoom = preDealDeck.get("Office");
		Card seenWeapon = preDealDeck.get("Electric Fly Swatter");
		testPlayer.updateSeen(seenPerson);
		testPlayer.updateSeen(seenRoom);
		testPlayer.updateSeen(seenWeapon);
		// Update the cards in the player's hand
		Card handPerson = preDealDeck.get("Aurora Angel");
		Card handRoom = preDealDeck.get("Bedroom");
		Card handWeapon = preDealDeck.get("Nitrous Gas");
		testPlayer.updateHand(handPerson);
		testPlayer.updateHand(handRoom);
		testPlayer.updateHand(handWeapon);
		// A few random cards that are not known by player
		Card testPerson1 = preDealDeck.get("Doctor Dora");
		Card testPerson2 = preDealDeck.get("Howard Handy");
		Card testWeapon1 = preDealDeck.get("Petrol Can");
		Card testWeapon2 = preDealDeck.get("Quail Beak");
		// Counters for each card
		int person1Ct, person2Ct, knownRoomCt, weapon1Ct, weapon2Ct, seenPersonCt, seenWeaponCt, handPersonCt, handWeaponCt;
		// Generate a suggestion from the computer
		Solution suggestion = testPlayer.createSuggestion();
		
		// Ensure that the suggestion is not empty
		assertNotEquals(null, suggestion.getPerson());
		assertNotEquals(null, suggestion.getRoom());
		assertNotEquals(null, suggestion.getWeapon());
		
		// Create 50 suggestions, updating appropriate counters based on card
		for(int i = 0; i < 50; i++) {
			suggestion = testPlayer.createSuggestion();
			if(suggestion.getPerson() == testPerson1)
				person1Ct++;
			if(suggestion.getPerson() == testPerson2)
				person2Ct++;
			if(suggestion.getRoom() == expectedRoom)
				knownRoomCt++;
			if(suggestion.getWeapon() == testWeapon1)
				weapon1Ct++;
			if(suggestion.getWeapon() == testWeapon2)
				weapon2Ct++;
			if(suggestion.getPerson() == seenPerson)
				seenPersonCt++;
			if(suggestion.getPerson() == handPerson)
				handPersonCt++;
			if(suggestion.getWeapon() == seenWeapon)
				seenWeaponCt++;
			if(suggestion.getWeapon() == handWeapon)
				handWeaponCt++;
		}
		
		// Check that the randomly selected cards are chosen at least once
		assertTrue(person1Ct > 1);
		assertTrue(person2Ct > 1);
		assertEquals(50, knownRoomCt); // The expected room (Batcave) should be suggested every time
		assertTrue(weapon1Ct > 1);
		assertTrue(weapon2Ct > 1);
		// Check that cards known by the player are never chosen
		assertEquals(0, seenPersonCt);
		assertEquals(0, handPersonCt);
		assertEquals(0, seenWeaponCt);
		assertEquals(0, handWeaponCt);
	}
	
	/*
	 * Test the selection of a computer player's target
	 */
	@Test
	public void testComputerTarget() {
		Player testPlayer = new ComputerPlayer("Test");
		Map<String, Card> preDealDeck = board.getCards();
		// Set the player location to (12, 18), which is in the middle of a walkway
		testPlayer.setRow(12);
		testPlayer.setColumn(18);
		// Make it so that the player has seen an accessible room
		Card seenRoom = preDealDeck.get("Toilet");
		Card targetRoom = preDealDeck.get("Malecave");
		testPlayer.updateSeen(seenRoom);
		BoardCell currCell = board.getCell(12, 18); // Reference to the cell where the player is
		
		// Test a roll of 1 (there should be four options, ensure each is selected once
		int cellA, cellB, cellC, cellD;
		BoardCell target;
		for(int i = 0; i < 20; i++) {
			target = testPlayer.selectTarget(currCell, 1);
			if(target == board.getCell(11, 18))
				cellA++;
			if(target == board.getCell(13, 18))
				cellB++;
			if(target == board.getCell(12, 17))
				cellC++;
			if(target == board.getCell(12, 19))
				cellD++;
		}
		assertTrue(cellA > 1);
		assertTrue(cellB > 1);
		assertTrue(cellC > 1);
		assertTrue(cellD > 1);
		
		// Test a roll of 6 (it should always choose to go to the Malecave)
		BoardCell targetRoomCell = board.getRoom('M').getCenterCell();
		BoardCell notTargetCell = board.getRoom('T').getCenterCell();
		// Check a few times to ensure that Malecave is always selected, and toilet never is
		for(int i = 0; i < 10; i++) {
			target = testPlayer.selectTarget(currCell, 6);
			assertEquals(targetRoomCell, target);
			assertNotEquals(notTargetCell, target);
		}
		
		// Test that it selects randomly if all accessible rooms have been seen
		testPlayer.updateSeen(targetRoom);
		cellA = 0;
		cellB = 0;
		cellC = 0;
		cellD = 0;
		
		for(int i = 0; i < 50; i++) {
			target = testPlayer.selectTarget(currCell, 6);
			if(target == board.getCell(11, 23))
				cellA++;
			if(target == board.getCell(8, 16))
				cellB++;
			if(target == targetRoomCell)
				cellC++;
			if(target == notTargetCell)
				cellD++;
		}
		
		assertTrue(cellA > 1);
		assertTrue(cellB > 1);
		assertTrue(cellC > 1);
		assertTrue(cellD > 1);
	}
}
