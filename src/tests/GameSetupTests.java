package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.Board;

class GameSetupTests {
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}
	
	@Test
	public void testPlayerLoad() {
		Map<Integer, Player> playerMap = board.getPlayers();
		assertEquals(playerMap.size(), 6);
		assertTrue(playerMap.get(0).getClass() == HumanPlayer.class);
		assertTrue(playerMap.get(1).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(2).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(3).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(4).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(5).getClass() == ComputerPlayer.class);
	}
	
	@Test
	public void testCardLoad() {
		Map<Character, Card> deck = board.getDeck();
		assertEquals(deck.size(), 21);
		assertEquals("Sergeant Sam", deck.get("S").getName());
		assertEquals(Color.RED, deck.get("S").getColor());
		assertEquals(CardType.PERSON, deck.get("S").getType());
		assertEquals("Doctor Dora", deck.get("D").getName());
		assertEquals(Color.GREEN, deck.get("D").getColor());
		assertEquals(CardType.PERSON, deck.get("D").getType());
		assertEquals("Icky Ivan", deck.get("I").getName());
		assertEquals(Color.YELLOW, deck.get("I").getColor());
		assertEquals(CardType.PERSON, deck.get("I").getType());
		assertEquals("Jon-von Johnson", deck.get("J").getName());
		assertEquals(Color.BLUE, deck.get("J").getColor());
		assertEquals(CardType.PERSON, deck.get("J").getType());
		
		assertEquals("Electric Fly Swatter", deck.get("E").getName());
		assertEquals(CardType.WEAPON, deck.get("E").getType());
		assertEquals("Rod", deck.get("R").getName());
		assertEquals(CardType.WEAPON, deck.get("R").getType());
		assertEquals("Quail Beak", deck.get("Q").getName());
		assertEquals(CardType.WEAPON, deck.get("Q").getType());
		
		assertEquals("Office", deck.get("O").getName());
		assertEquals(CardType.ROOM, deck.get("O").getType());
		assertEquals("Malecave", deck.get("M").getName());
		assertEquals(CardType.ROOM, deck.get("M").getType());
		assertEquals("Batcave", deck.get("C").getName());
		assertEquals(CardType.ROOM, deck.get("C").getType());
		assertEquals("Lounge", deck.get("L").getName());
		assertEquals(CardType.ROOM, deck.get("L").getType());
		assertEquals("Guardroom", deck.get("G").getName());
		assertEquals(CardType.ROOM, deck.get("G").getType());
	}

	@Test
	public void testSolutionLoad() {
		Map<Character, Card> preDealDeck = board.getDeck();
		Map<Integer, Player> playerMap = board.getPlayers();
		board.deal();
		Solution solution = board.getSolution();
		Map<Character, Card> deck = board.getDeck();
		
		assertEquals(preDealDeck.size(), 21);
		assertTrue(preDealDeck.contains(solution.getPerson()));
		assertTrue(preDealDeck.contains(solution.getRoom()));
		assertTrue(preDealDeck.contains(solution.getWeapon()));
		assertFalse(deck.contains(solution.getPerson()));
		assertFalse(deck.contains(solution.getRoom()));
		assertFalse(deck.contains(solution.getWeapon()));
		
		assertEquals(CardType.PERSON, solution.getPerson().getType());
		assertEquals(CardType.ROOM, solution.getRoom().getType());
		assertEquals(CardType.WEAPON, solution.getWeapon().getType());
		
		for(int i = 0; i < 6; i++) {
			Player player = playerMap.get(i);
			assertFalse(player.getHand().contains(solution.getPerson()));
			assertFalse(player.getHand().contains(solution.getRoom()));
			assertFalse(player.getHand().contains(solution.getWeapon()));
		}
	}
	
	@Test
	public void testDeal() {
		Map<Character, Card> deck = board.getDeck();
		Map<Integer, Player> playerMap = board.getPlayers();
		
		assertEquals(deck.size(), 0);
		assertEquals(3, playerMap.get(0).getHand().size());
		assertEquals(3, playerMap.get(1).getHand().size());
		assertEquals(3, playerMap.get(3).getHand().size());
		assertEquals(3, playerMap.get(5).getHand().size());
		
		
	}
}
