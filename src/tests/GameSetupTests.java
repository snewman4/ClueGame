package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.*;

class GameSetupTests {
	private static final int NUM_CARDS = 21;
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
		
		int cardsPer = (NUM_CARDS - 3) / 6; // Calculate estimated number of cards per player
		cardsPerMin = cardsPer - 1; // Each player should have within one card of each other
		cardsPerMax = cardsPer + 1; // Each player should have within one card of each other
	}
	
	// Test to make sure players load correctly
	@Test
	public void testPlayerLoad() {
		Map<Integer, Player> playerMap = board.getPlayers();
		assertEquals(playerMap.size(), 6); // There can be up to 6 players
		// Check the human player
		assertEquals(HumanPlayer.class, playerMap.get(0).getClass()); // Player 0 should always be the human player
		assertEquals("Sergeant Sam", playerMap.get(0).getName()); // Test player name
		assertEquals(Color.RED, playerMap.get(0).getColor()); // Test player color
		// Test start location of human player
		assertEquals(0, playerMap.get(0).getRow());
		assertEquals(8, playerMap.get(0).getColumn());
		// Rest of the players should be computer players. Also test respective locations
		assertTrue(playerMap.get(1).getClass() == ComputerPlayer.class);
		assertEquals("Aurora Angel", playerMap.get(1).getName()); // Test player name
		assertEquals(Color.PINK, playerMap.get(1).getColor()); // Test player color
		assertEquals(0, playerMap.get(1).getRow());
		assertEquals(16, playerMap.get(1).getColumn());
		assertTrue(playerMap.get(2).getClass() == ComputerPlayer.class);
		assertEquals("Doctor Dora", playerMap.get(2).getName()); // Test player name
		assertEquals(Color.GREEN, playerMap.get(2).getColor()); // Test player color
		assertEquals(12, playerMap.get(2).getRow());
		assertEquals(24, playerMap.get(2).getColumn());
		assertTrue(playerMap.get(3).getClass() == ComputerPlayer.class);
		assertEquals("Howard Handy", playerMap.get(3).getName()); // Test player name
		assertEquals(Color.BLACK, playerMap.get(3).getColor()); // Test player color
		assertEquals(24, playerMap.get(3).getRow());
		assertEquals(16, playerMap.get(3).getColumn());
		assertTrue(playerMap.get(4).getClass() == ComputerPlayer.class);
		assertEquals("Icky Ivan", playerMap.get(4).getName()); // Test player name
		assertEquals(Color.YELLOW, playerMap.get(4).getColor()); // Test player color
		assertEquals(24, playerMap.get(4).getRow());
		assertEquals(8, playerMap.get(4).getColumn());
		assertTrue(playerMap.get(5).getClass() == ComputerPlayer.class);
		assertEquals("Jon-von Johnson", playerMap.get(5).getName()); // Test player name
		assertEquals(Color.BLUE, playerMap.get(5).getColor()); // Test player color
		assertEquals(12, playerMap.get(5).getRow());
		assertEquals(0, playerMap.get(5).getColumn());
	}
	
	// Test to make sure the deck of cards is loaded properly
	@Test
	public void testCardLoad() {
		Map<String, Card> deck = board.getDeck();
		assertEquals(deck.size(), NUM_CARDS);
		
		// Test player cards are loaded properly
		assertEquals("Sergeant Sam", deck.get("Sergeant Sam").getName()); // Test player card name
		assertEquals(CardType.PERSON, deck.get("Sergeant Sam").getType()); // Ensure card is a person
		assertEquals("Doctor Dora", deck.get("Doctor Dora").getName()); // Test player card name
		assertEquals(CardType.PERSON, deck.get("Doctor Dora").getType()); // Ensure card is a person
		assertEquals("Icky Ivan", deck.get("Icky Ivan").getName()); // Test player card name
		assertEquals(CardType.PERSON, deck.get("Icky Ivan").getType()); // Ensure card is a person
		assertEquals("Jon-von Johnson", deck.get("Jon-von Johnson").getName()); // Test player card name
		assertEquals(CardType.PERSON, deck.get("Jon-von Johnson").getType()); // Ensure card is a person
		
		// Test that weapon cards are loaded properly
		assertEquals("Electric Fly Swatter", deck.get("Electric Fly Swatter").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("Electric Fly Swatter").getType()); // Ensure card is a weapon
		assertEquals("Rod", deck.get("Rod").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("Rod").getType()); // Ensure card is a weapon
		assertEquals("Quail Beak", deck.get("Quail Beak").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, deck.get("Quail Beak").getType()); // Ensure card is a weapon
		
		// Test that room cards are loaded properly
		assertEquals("Office", deck.get("Office").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("Office").getType()); // Ensure card is a room
		assertEquals("Malecave", deck.get("Malecave").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("Malecave").getType()); // Ensure card is a room
		assertEquals("Batcave", deck.get("Batcave").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("Batcave").getType()); // Ensure card is a room
		assertEquals("Lounge", deck.get("Lounge").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("Lounge").getType()); // Ensure card is a room
		assertEquals("Guardroom", deck.get("Guardroom").getName()); // Test room card name
		assertEquals(CardType.ROOM, deck.get("Guardroom").getType()); // Ensure card is a room
	}

	// Test to make sure the solution is drawn correctly
	@Test
	public void testSolutionLoad() {
		Map<String, Card> preDealDeck = board.getDeck(); // Save deck before dealing
		Map<Integer, Player> playerMap = board.getPlayers();
		board.deal(); // Deal the cards
		Solution solution = board.getSolution();
		Map<String, Card> deck = board.getDeck();
		
		assertEquals(preDealDeck.size(), NUM_CARDS); // Check that the initial deck is of the correct size
		// Check that the solution was drawn directly from the deck
		assertTrue(preDealDeck.containsKey(solution.person.getName()));
		assertTrue(preDealDeck.containsKey(solution.room.getName()));
		assertTrue(preDealDeck.containsKey(solution.weapon.getName()));
		// Check that the deck does not contain the solution cards after deal()
		assertFalse(deck.containsKey(solution.person.getName()));
		assertFalse(deck.containsKey(solution.room.getName()));
		assertFalse(deck.containsKey(solution.weapon.getName()));
		
		// Check that the each solution card is the appropriate type
		assertEquals(CardType.PERSON, solution.person.getType());
		assertEquals(CardType.ROOM, solution.room.getType());
		assertEquals(CardType.WEAPON, solution.weapon.getType());
		
		// Check that none of the players have the solution cards
		for(int i = 0; i < 6; i++) {
			Player player = playerMap.get(i);
			assertFalse(player.getHand().contains(solution.person));
			assertFalse(player.getHand().contains(solution.room));
			assertFalse(player.getHand().contains(solution.weapon));
		}
	}
	
	// Test to make sure cards are dealt to players properly
	@Test
	public void testDeal() {
		Map<String, Card> deck = board.getDeck();
		Map<Integer, Player> playerMap = board.getPlayers();
		
		assertEquals(deck.size(), 0); // Make sure that all cards were dealt
		assertEquals(playerMap.size(), 6); // Make sure all players are loaded
		// For each player:
		for(int i = 0; i < playerMap.size(); i++) {
			Player player = playerMap.get(i);
			// Ensure each player has within one card of the expected hand size
			assertTrue(cardsPerMin <= player.getHand().size() && player.getHand().size() <= cardsPerMax);
			// Ensure that no two players have the same card
			for(Card card : player.getHand()) { // For each card in player's hand
				for(int j = 0; j < playerMap.size(); j++) { // For each player
					// If the current player is the same as the query player, skip
					if(i == j) {
						continue;
					}
					assertFalse(playerMap.get(j).getHand().contains(card));
				}
			}
		}
	}
}
