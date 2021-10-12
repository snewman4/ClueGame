package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms() {
		// First, the bedroom that only has a single door but a secret room
		Set<BoardCell> testList = board.getAdjList(3, 21);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(3, 18)));
		assertTrue(testList.contains(board.getCell(18, 1)));

		// now test the femalecave, which has 3 doors
		testList = board.getAdjList(19, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 4)));
		assertTrue(testList.contains(board.getCell(19, 4)));
		assertTrue(testList.contains(board.getCell(19, 11)));

		// one more room, the kitchen (not marked on the spreadsheet because of multiple tests)
		// the kitchen has two doors, plus a secret passage
		testList = board.getAdjList(18, 1);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(18, 3)));
		assertTrue(testList.contains(board.getCell(19, 3)));
		assertTrue(testList.contains(board.getCell(3, 21)));
	}


	// Ensure door locations include their rooms and also additional walkways
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyDoor() {
		// test doorway to the left
		Set<BoardCell> testList = board.getAdjList(5, 11);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(4, 9)));
		assertTrue(testList.contains(board.getCell(4, 11)));
		assertTrue(testList.contains(board.getCell(5, 12)));
		assertTrue(testList.contains(board.getCell(6, 11)));

		// test doorway to the right
		testList = board.getAdjList(5, 13);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(4, 13)));
		assertTrue(testList.contains(board.getCell(5, 12)));
		assertTrue(testList.contains(board.getCell(5, 15)));
		assertTrue(testList.contains(board.getCell(6, 13)));

		// test doorway down, that is also in a corner
		testList = board.getAdjList(6, 24);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(6, 23)));
		assertTrue(testList.contains(board.getCell(8, 22)));
		
		// test doorway up
		testList = board.getAdjList(11, 21);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(8, 22)));
		assertTrue(testList.contains(board.getCell(11, 20)));
		assertTrue(testList.contains(board.getCell(11, 22)));
		assertTrue(testList.contains(board.getCell(12, 21)));
	}

	// Test a variety of walkway scenarios
	// These tests are DARK ORANGE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways() {
		// Test on top edge of board, with only one adjacency
		Set<BoardCell> testList = board.getAdjList(0, 8);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(1, 8)));

		// Test in a corner, with room on two sides but no door
		testList = board.getAdjList(5, 17);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(4, 17)));
		assertTrue(testList.contains(board.getCell(5, 18)));

		// Test adjacent to walkways
		testList = board.getAdjList(12, 2);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(11, 2)));
		assertTrue(testList.contains(board.getCell(12, 1)));
		assertTrue(testList.contains(board.getCell(12, 3)));
		assertTrue(testList.contains(board.getCell(13, 2)));
		
		// Test near two doors, but adjacent to none
		testList = board.getAdjList(12, 20);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(11, 20)));
		assertTrue(testList.contains(board.getCell(12, 19)));
		assertTrue(testList.contains(board.getCell(12, 21)));
		assertTrue(testList.contains(board.getCell(13, 20)));

		// Test next to closet corner
		testList = board.getAdjList(15, 14);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(15, 15)));
		assertTrue(testList.contains(board.getCell(16, 14)));

		// Test bottom edge of board, with only one adjacency
		testList = board.getAdjList(24, 16);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(23, 16)));
	}


	// Tests out of room center with rolls of 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsInToilet() {
		// test a roll of 1
		board.calcTargets(board.getCell(8, 22), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(6, 24)));
		assertTrue(targets.contains(board.getCell(11, 21)));	

		// test a roll of 3
		board.calcTargets(board.getCell(8, 22), 3);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(6, 22)));
		assertTrue(targets.contains(board.getCell(11, 23)));	
		assertTrue(targets.contains(board.getCell(12, 22)));
		assertTrue(targets.contains(board.getCell(13, 21)));
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(11, 19)));

		// test a roll of 4
		board.calcTargets(board.getCell(8, 22), 4);
		targets = board.getTargets();
		assertEquals(11, targets.size());
		assertTrue(targets.contains(board.getCell(6, 21)));
		assertTrue(targets.contains(board.getCell(10, 19)));
		assertTrue(targets.contains(board.getCell(11, 18)));	
		assertTrue(targets.contains(board.getCell(11, 20)));
		assertTrue(targets.contains(board.getCell(11, 22)));
		assertTrue(targets.contains(board.getCell(12, 19)));	
		assertTrue(targets.contains(board.getCell(12, 21)));
		assertTrue(targets.contains(board.getCell(12, 23)));
		assertTrue(targets.contains(board.getCell(13, 20)));	
		assertTrue(targets.contains(board.getCell(13, 22)));
		assertTrue(targets.contains(board.getCell(17, 21)));
	}

	@Test
	public void testTargetsInKitchen() {
		// test a roll of 1
		board.calcTargets(board.getCell(18, 1), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(3, 21)));
		assertTrue(targets.contains(board.getCell(18, 3)));
		assertTrue(targets.contains(board.getCell(19, 3)));

		// test a roll of 3
		board.calcTargets(board.getCell(18, 1), 3);
		targets = board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(3, 21)));
		assertTrue(targets.contains(board.getCell(16, 3)));	
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(17, 4)));
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(19, 4)));	
		assertTrue(targets.contains(board.getCell(19, 7)));
		assertTrue(targets.contains(board.getCell(20, 3)));
		assertTrue(targets.contains(board.getCell(20, 4)));
		assertTrue(targets.contains(board.getCell(21, 3)));

		// test a roll of 4
		board.calcTargets(board.getCell(18, 1), 4);
		targets = board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(3, 21)));
		assertTrue(targets.contains(board.getCell(15, 3)));	
		assertTrue(targets.contains(board.getCell(16, 4)));
		assertTrue(targets.contains(board.getCell(17, 3)));
		assertTrue(targets.contains(board.getCell(17, 4)));
		assertTrue(targets.contains(board.getCell(18, 3)));	
		assertTrue(targets.contains(board.getCell(18, 4)));
		assertTrue(targets.contains(board.getCell(19, 3)));
		assertTrue(targets.contains(board.getCell(19, 4)));
		assertTrue(targets.contains(board.getCell(19, 7)));	
		assertTrue(targets.contains(board.getCell(21, 4)));
		assertTrue(targets.contains(board.getCell(22, 3)));
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(21, 21), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(17, 21)));
		assertTrue(targets.contains(board.getCell(21, 20)));	
		assertTrue(targets.contains(board.getCell(21, 22)));
		assertTrue(targets.contains(board.getCell(22, 21)));

		// test a roll of 3
		board.calcTargets(board.getCell(21, 21), 3);
		targets = board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(17, 21)));
		assertTrue(targets.contains(board.getCell(21, 18)));
		assertTrue(targets.contains(board.getCell(21, 20)));	
		assertTrue(targets.contains(board.getCell(21, 22)));
		assertTrue(targets.contains(board.getCell(22, 19)));
		assertTrue(targets.contains(board.getCell(22, 21)));
		assertTrue(targets.contains(board.getCell(22, 23)));	
		assertTrue(targets.contains(board.getCell(23, 20)));
		assertTrue(targets.contains(board.getCell(23, 22)));

		// test a roll of 4
		board.calcTargets(board.getCell(21, 21), 4);
		targets = board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(17, 21)));
		assertTrue(targets.contains(board.getCell(21, 17)));
		assertTrue(targets.contains(board.getCell(21, 19)));	
		assertTrue(targets.contains(board.getCell(21, 23)));
		assertTrue(targets.contains(board.getCell(22, 18)));
		assertTrue(targets.contains(board.getCell(22, 20)));
		assertTrue(targets.contains(board.getCell(22, 22)));
		assertTrue(targets.contains(board.getCell(23, 19)));	
		assertTrue(targets.contains(board.getCell(23, 21)));
		assertTrue(targets.contains(board.getCell(23, 23)));
	}

	@Test
	public void testTargetsInWalkway1() {
		// test a roll of 1
		board.calcTargets(board.getCell(2, 12), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 11)));
		assertTrue(targets.contains(board.getCell(2, 13)));
		assertTrue(targets.contains(board.getCell(3, 12)));

		// test a roll of 3
		board.calcTargets(board.getCell(2, 12), 3);
		targets = board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(1, 10)));
		assertTrue(targets.contains(board.getCell(1, 14)));
		assertTrue(targets.contains(board.getCell(2, 9)));
		assertTrue(targets.contains(board.getCell(2, 11)));
		assertTrue(targets.contains(board.getCell(2, 13)));
		assertTrue(targets.contains(board.getCell(2, 15)));
		assertTrue(targets.contains(board.getCell(3, 10)));
		assertTrue(targets.contains(board.getCell(3, 14)));
		assertTrue(targets.contains(board.getCell(5, 12)));

		// test a roll of 4
		board.calcTargets(board.getCell(2, 12), 4);
		targets = board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(1, 9)));
		assertTrue(targets.contains(board.getCell(1, 15)));
		assertTrue(targets.contains(board.getCell(2, 10)));
		assertTrue(targets.contains(board.getCell(2, 14)));
		assertTrue(targets.contains(board.getCell(3, 11)));
		assertTrue(targets.contains(board.getCell(3, 13)));
		assertTrue(targets.contains(board.getCell(4, 12)));
		assertTrue(targets.contains(board.getCell(5, 11)));
		assertTrue(targets.contains(board.getCell(6, 12)));
	}

	@Test
	public void testTargetsInWalkway2() {
		// test a roll of 1
		board.calcTargets(board.getCell(12, 24), 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(12, 23)));

		// test a roll of 3
		board.calcTargets(board.getCell(12, 24), 3);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(11, 22)));
		assertTrue(targets.contains(board.getCell(12, 21)));
		assertTrue(targets.contains(board.getCell(13, 22)));	

		// test a roll of 4
		board.calcTargets(board.getCell(12, 24), 4);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(11, 21)));
		assertTrue(targets.contains(board.getCell(11, 23)));
		assertTrue(targets.contains(board.getCell(12, 20)));
		assertTrue(targets.contains(board.getCell(12, 22)));
		assertTrue(targets.contains(board.getCell(13, 21)));
		assertTrue(targets.contains(board.getCell(13, 23)));
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 4 blocked 2 left
		board.getCell(11, 18).setOccupied(true);
		board.calcTargets(board.getCell(11, 20), 4);
		board.getCell(11, 18).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(8, 22)));
		assertTrue(targets.contains(board.getCell(11, 22)));
		assertTrue(targets.contains(board.getCell(12, 17)));
		assertFalse(targets.contains(board.getCell(11, 18)));
		assertFalse(targets.contains(board.getCell(11, 16)));

		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(2, 18).setOccupied(true);
		board.getCell(3, 21).setOccupied(true);
		BoardCell testCell = board.getCell(3, 18);
		board.calcTargets(board.getCell(3, 18), 1);
		board.getCell(2, 18).setOccupied(false);
		board.getCell(3, 21).setOccupied(false);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(3, 17)));	
		assertTrue(targets.contains(board.getCell(3, 21)));	
		assertTrue(targets.contains(board.getCell(4, 18)));	

		// check leaving a room with a blocked doorway
		board.getCell(19, 11).setOccupied(true);
		board.calcTargets(board.getCell(19, 7), 3);
		board.getCell(19, 11).setOccupied(false);
		targets = board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(16, 4)));
		assertTrue(targets.contains(board.getCell(18, 1)));
		assertTrue(targets.contains(board.getCell(20, 3)));
		assertFalse(targets.contains(board.getCell(17, 11)));
		assertFalse(targets.contains(board.getCell(19, 11)));
	}
}
