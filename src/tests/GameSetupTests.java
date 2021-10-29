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
	private static final int NUM_CARDS = 21;
	private static int cardsPer;
	private static int cardsPerMin;
	private static int cardsPerMax;
	private static Board board;
	
	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
		
		cardsPer = (NUM_CARDS - 3) / 6; // Calculate estimated number of cards per player
		cardsPerMin = cardsPer - 1; // Each player should have within one card of each other
		cardsPerMax = cardsPer + 1; // Each player should have within one card of each other
	}
	
	// Test to make sure players load correctly
	@Test
	public void testPlayerLoad() {
		Map<Integer, Player> playerMap = board.getPlayers();
		assertEquals(playerMap.size(), 6); // There can be up to 6 players
		// Player 0 should always be the human player
		assertTrue(playerMap.get(0).getClass() == HumanPlayer.class);
		// Rest of the players should be computer players
		assertTrue(playerMap.get(1).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(2).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(3).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(4).getClass() == ComputerPlayer.class);
		assertTrue(playerMap.get(5).getClass() == ComputerPlayer.class);
	}
	
	// Test to make sure the deck of cards is loaded properly
	@Test
	public void testCardLoad() {
		Map<Character, Card> deck = board.getDeck();
		assertEquals(deck.size(), NUM_CARDS);
		
		// Test player cards are loaded properly
		assertEquals("Sergeant Sam", deck.get("S").getName()); // Test player card name
		assertEquals(Color.RED, deck.get("S").getColor()); // Test player card color
		assertEquals(CardType.PERSON, deck.get("S").getType()); // Ensure card is a person
		assertEquals("Doctor Dora", deck.get("D").getName()); // Test player card name
		assertEquals(Color.GREEN, deck.get("D").getColor()); // Test player card color
		assertEquals(CardType.PERSON, deck.get("D").getType()); // Ensure card is a person
		assertEquals("Icky Ivan", deck.get("I").getName()); // Test player card name
		assertEquals(Color.YELLOW, deck.get("I").getColor()); // Test player card color
		assertEquals(CardType.PERSON, deck.get("I").getType()); // Ensure card is a person
		assertEquals("Jon-von Johnson", deck.get("J").getName()); // Test player card name
		assertEquals(Color.BLUE, deck.get("J").getColor()); // Test player card color
		assertEquals(CardType.PERSON, deck.get("J").getType()); // Ensure card is a person
		
		// Test that weapon cards are loaded properly
		assertEquals("Electric Fly Swatter", deck.get("E").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("E").getType()); // Ensure card is a weapon
		assertEquals("Rod", deck.get("R").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("R").getType()); // Ensure card is a weapon
		assertEquals("Quail Beak", deck.get("Q").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("Q").getType()); // Ensure card is a weapon
		
		// Test that room cards are loaded properly
		assertEquals("Office", deck.get("O").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("O").getType()); // Ensure card is a room
		assertEquals("Malecave", deck.get("M").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("M").getType()); // Ensure card is a room
		assertEquals("Batcave", deck.get("C").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("C").getType()); // Ensure card is a room
		assertEquals("Lounge", deck.get("L").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("L").getType()); // Ensure card is a room
		assertEquals("Guardroom", deck.get("G").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("G").getType()); // Ensure card is a room
	}

	// Test to make sure the solution is drawn correctly
	@Test
	public void testSolutionLoad() {
		Map<Character, Card> preDealDeck = board.getDeck(); // Save deck before dealing
		Map<Integer, Player> playerMap = board.getPlayers();
		board.deal(); // Deal the cards
		Solution solution = board.getSolution();
		Map<Character, Card> deck = board.getDeck();
		
		assertEquals(preDealDeck.size(), NUM_CARDS); // Check that the initial deck is of the correct size
		// Check that the solution was drawn directly from the deck
		assertTrue(preDealDeck.contains(solution.getPerson()));
		assertTrue(preDealDeck.contains(solution.getRoom()));
		assertTrue(preDealDeck.contains(solution.getWeapon()));
		// Check that the deck does not contain the solution cards after deal()
		assertFalse(deck.contains(solution.getPerson()));
		assertFalse(deck.contains(solution.getRoom()));
		assertFalse(deck.contains(solution.getWeapon()));
		
		// Check that the each solution card is the appropriate type
		assertEquals(CardType.PERSON, solution.getPerson().getType());
		assertEquals(CardType.ROOM, solution.getRoom().getType());
		assertEquals(CardType.WEAPON, solution.getWeapon().getType());
		
		// Check that none of the players have the solution cards
		for(int i = 0; i < 6; i++) {
			Player player = playerMap.get(i);
			assertFalse(player.getHand().contains(solution.getPerson()));
			assertFalse(player.getHand().contains(solution.getRoom()));
			assertFalse(player.getHand().contains(solution.getWeapon()));
		}
	}
	
	// Test to make sure cards are dealt to players properly
	@Test
	public void testDeal() {
		Map<Character, Card> deck = board.getDeck();
		Map<Integer, Player> playerMap = board.getPlayers();
		
		assertEquals(deck.size(), 0); // Make sure that all cards were dealt
		// For each player:
		for(int i = 0; i < 6; i++) {
			Player player = playerMap.get(i);
			// Ensure each player has within one card of the expected hand size
			assertTrue(cardsPerMin <= player.getHand().size() && player.getHand().size() <= cardsPerMax);
		}
	}
}
