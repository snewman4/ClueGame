package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import experiment.*;

class BoardTestsExp {
	TestBoard expBoard;
	
	@BeforeEach
	public void setUp() {
		expBoard = new TestBoard(4, 4);
	}
	
	/*
	 * Test the adjacencies for several locations, including the four corners,
	 * two edges, and two center cells
	 */
	@Test
	void adjacencyListTest() {
		// Tests for the four corner cells
		TestBoardCell cell = expBoard.getCell(0, 0); // Cell (0, 0)
		Set<TestBoardCell> testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 0)));
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 1)));
		Assert.assertEquals(2, testList.size());
		
		cell = expBoard.getCell(0, 3); // Cell (0, 3)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 3)));
		Assert.assertEquals(2, testList.size());
		
		cell = expBoard.getCell(3, 0); // Cell (3, 0)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(3, 1)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 0)));
		Assert.assertEquals(2, testList.size());
		
		cell = expBoard.getCell(3, 3); // Cell (3, 3)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(3, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 3)));
		Assert.assertEquals(2, testList.size());
		
		// Tests for two edge cells
		cell = expBoard.getCell(0, 2); // Cell (0, 2)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 1)));
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 3)));
		Assert.assertEquals(3, testList.size());
		
		cell = expBoard.getCell(1, 3); // Cell (1, 3)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 3)));
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 3)));
		Assert.assertEquals(3, testList.size());
		
		// Tests for two center cells
		cell = expBoard.getCell(1, 1); // Cell (1, 1)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(0, 1)));
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 0)));
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 1)));
		Assert.assertEquals(4, testList.size());
		
		cell = expBoard.getCell(2, 2); // Cell (2, 2)
		testList = cell.getAdjList();
		Assert.assertTrue(testList.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 1)));
		Assert.assertTrue(testList.contains(expBoard.getCell(2, 3)));
		Assert.assertTrue(testList.contains(expBoard.getCell(3, 2)));
		Assert.assertEquals(4, testList.size());
	}
	
	/*
	 * Test the target generation for several starting locations, with several rolls
	 */
	@Test
	void TargetsNormalTest() {
		// Starting location (0, 0), roll 3
		TestBoardCell cell = expBoard.getCell(0, 0);
		expBoard.calcTargets(cell, 3);
		Set<TestBoardCell> targets = expBoard.getTargets();
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(expBoard.getCell(3, 0)));
		Assert.assertTrue(targets.contains(expBoard.getCell(2, 1)));
		Assert.assertTrue(targets.contains(expBoard.getCell(0, 1)));
		Assert.assertTrue(targets.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(targets.contains(expBoard.getCell(0, 3)));
		Assert.assertTrue(targets.contains(expBoard.getCell(1, 0)));
		
		// Starting location (1, 1), roll 3
		cell = expBoard.getCell(1, 1);
		expBoard.calcTargets(cell, 3);
		targets = expBoard.getTargets();
		Assert.assertEquals(8, targets.size());
		Assert.assertTrue(targets.contains(expBoard.getCell(0, 1)));
		Assert.assertTrue(targets.contains(expBoard.getCell(0, 3)));
		Assert.assertTrue(targets.contains(expBoard.getCell(1, 0)));
		Assert.assertTrue(targets.contains(expBoard.getCell(1, 2)));
		Assert.assertTrue(targets.contains(expBoard.getCell(2, 1)));
		Assert.assertTrue(targets.contains(expBoard.getCell(2, 3)));
		Assert.assertTrue(targets.contains(expBoard.getCell(3, 0)));
		Assert.assertTrue(targets.contains(expBoard.getCell(3, 2)));
	}
}
