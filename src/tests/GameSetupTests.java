package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.util.List;
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
		Player.reset();
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
		List<Player> playerList = board.getPlayers();
		assertEquals(6, playerList.size()); // There can be up to 6 players
		// Check the human player
		assertEquals(HumanPlayer.class, playerList.get(0).getClass()); // Player 0 should always be the human player
		assertEquals("Sergeant Sam", playerList.get(0).getName()); // Test player name
		assertEquals(Color.RED, playerList.get(0).getColor()); // Test player color
		// Test start location of human player
		assertEquals(0, playerList.get(0).getRow());
		assertEquals(8, playerList.get(0).getColumn());
		// Rest of the players should be computer players. Also test respective locations
		assertTrue(playerList.get(1).getClass() == ComputerPlayer.class);
		assertEquals("Aurora Angel", playerList.get(1).getName()); // Test player name
		assertEquals(Color.GRAY, playerList.get(1).getColor()); // Test player color
		assertEquals(0, playerList.get(1).getRow());
		assertEquals(16, playerList.get(1).getColumn());
		assertTrue(playerList.get(2).getClass() == ComputerPlayer.class);
		assertEquals("Doctor Dora", playerList.get(2).getName()); // Test player name
		assertEquals(Color.GREEN, playerList.get(2).getColor()); // Test player color
		assertEquals(12, playerList.get(2).getRow());
		assertEquals(24, playerList.get(2).getColumn());
		assertTrue(playerList.get(3).getClass() == ComputerPlayer.class);
		assertEquals("Howard Handy", playerList.get(3).getName()); // Test player name
		assertEquals(Color.CYAN, playerList.get(3).getColor()); // Test player color
		assertEquals(24, playerList.get(3).getRow());
		assertEquals(16, playerList.get(3).getColumn());
		assertTrue(playerList.get(4).getClass() == ComputerPlayer.class);
		assertEquals("Icky Ivan", playerList.get(4).getName()); // Test player name
		assertEquals(Color.MAGENTA, playerList.get(4).getColor()); // Test player color
		assertEquals(24, playerList.get(4).getRow());
		assertEquals(8, playerList.get(4).getColumn());
		assertTrue(playerList.get(5).getClass() == ComputerPlayer.class);
		assertEquals("Jon-von Johnson", playerList.get(5).getName()); // Test player name
		assertEquals(Color.ORANGE, playerList.get(5).getColor()); // Test player color
		assertEquals(12, playerList.get(5).getRow());
		assertEquals(0, playerList.get(5).getColumn());
	}
	
	// Test to make sure the deck of cards is loaded properly
	@Test
	public void testCardLoad() {
		Map<String, Card> cardMap = board.getCards();
		assertEquals(cardMap.size(), NUM_CARDS);
		
		// Test player cards are loaded properly
		assertEquals("Sergeant Sam", cardMap.get("Sergeant Sam").getName()); // Test player card name
		assertEquals(CardType.PERSON, cardMap.get("Sergeant Sam").getType()); // Ensure card is a person
		assertEquals("Doctor Dora", cardMap.get("Doctor Dora").getName()); // Test player card name
		assertEquals(CardType.PERSON, cardMap.get("Doctor Dora").getType()); // Ensure card is a person
		assertEquals("Icky Ivan", cardMap.get("Icky Ivan").getName()); // Test player card name
		assertEquals(CardType.PERSON, cardMap.get("Icky Ivan").getType()); // Ensure card is a person
		assertEquals("Jon-von Johnson", cardMap.get("Jon-von Johnson").getName()); // Test player card name
		assertEquals(CardType.PERSON, cardMap.get("Jon-von Johnson").getType()); // Ensure card is a person
		
		// Test that weapon cards are loaded properly
		assertEquals("Electric Fly Swatter", cardMap.get("Electric Fly Swatter").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, cardMap.get("Electric Fly Swatter").getType()); // Ensure card is a weapon
		assertEquals("Rod", cardMap.get("Rod").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, cardMap.get("Rod").getType()); // Ensure card is a weapon
		assertEquals("Quail Beak", cardMap.get("Quail Beak").getName()); // Test weapon card name
		assertEquals(CardType.WEAPON, cardMap.get("Quail Beak").getType()); // Ensure card is a weapon
		
		// Test that room cards are loaded properly
		assertEquals("Office", cardMap.get("Office").getName()); // Test room card name
		assertEquals(CardType.ROOM, cardMap.get("Office").getType()); // Ensure card is a room
		assertEquals("Malecave", cardMap.get("Malecave").getName()); // Test room card name
		assertEquals(CardType.ROOM, cardMap.get("Malecave").getType()); // Ensure card is a room
		assertEquals("Batcave", cardMap.get("Batcave").getName()); // Test room card name
		assertEquals(CardType.ROOM, cardMap.get("Batcave").getType()); // Ensure card is a room
		assertEquals("Lounge", cardMap.get("Lounge").getName()); // Test room card name
		assertEquals(CardType.ROOM, cardMap.get("Lounge").getType()); // Ensure card is a room
		assertEquals("Guardery", cardMap.get("Guardery").getName()); // Test room card name
		assertEquals(CardType.ROOM, cardMap.get("Guardery").getType()); // Ensure card is a room
	}

	// Test to make sure the solution is drawn correctly
	@Test
	public void testSolutionLoad() {
		board.deal(); // Deal the cards
		Map<String, Card> preDealDeck = board.getCards(); // Save deck w/ references to all cards
		List<Player> playerList = board.getPlayers();
		Solution solution = board.getSolution();
		List<Card> deck = board.getDeck();
		
		assertEquals(NUM_CARDS, preDealDeck.size()); // Check that the initial deck is of the correct size
		// Check that the solution was drawn directly from the deck
		assertTrue(preDealDeck.containsKey(solution.getPerson().getName()));
		assertTrue(preDealDeck.containsKey(solution.getRoom().getName()));
		assertTrue(preDealDeck.containsKey(solution.getWeapon().getName()));
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
			Player player = playerList.get(i);
			assertFalse(player.getHand().contains(solution.getPerson()));
			assertFalse(player.getHand().contains(solution.getRoom()));
			assertFalse(player.getHand().contains(solution.getWeapon()));
		}
	}
	
	// Test to make sure cards are dealt to players properly
	@Test
	public void testDeal() {
		List<Card> deck = board.getDeck();
		List<Player> playerList = board.getPlayers();
		
		assertEquals(0, deck.size()); // Make sure that all cards were dealt
		assertEquals(6, playerList.size()); // Make sure all players are loaded
		// For each player:
		for(int i = 0; i < playerList.size(); i++) {
			Player player = playerList.get(i);
			// Ensure each player has within one card of the expected hand size
			assertTrue(cardsPerMin <= player.getHand().size() && player.getHand().size() <= cardsPerMax);
			// Ensure that no two players have the same card
			for(Card card : player.getHand()) { // For each card in player's hand
				for(int j = 0; j < playerList.size(); j++) { // For each player
					// If the current player is the same as the query player, skip
					if(i == j) {
						continue;
					}
					assertFalse(playerList.get(j).getHand().contains(card));
				}
			}
		}
	}
}
