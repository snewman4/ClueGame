package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

class GameSolutionTest {
	private static final int NUM_CARDS = 21;
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
	 *  Test that the solution is loaded properly, and that no players share
	 *   a hand with the solution.
	 */
	@Test
	public void testSolution() {
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

	/*
	 *  Test that a correct accusation returns True, while an incorrect one
	 *  returns False
	 */
	@Test
	public void testAccusation() {
		Map<String, Card> preDealDeck = board.getCards(); // Save deck w/ references to all cards
		Solution solution = board.getSolution();
		// Get the correct cards for the solution
		Card personSol = solution.getPerson();
		Card roomSol = solution.getRoom();
		Card weaponSol = solution.getWeapon();
		// Get cards that aren't solution to test against
		Card notPerson = new Card('T', "Test", CardType.PERSON);
		Card notRoom = new Card('T', "Test", CardType.ROOM);
		Card notWeapon = new Card('T', "Test", CardType.WEAPON);
		// Loop through deck of all cards. Select one card from each type that is not solution.
		for(Map.Entry<String, Card> card : preDealDeck.entrySet()) {
			if(card.getValue().getType() == CardType.PERSON && card.getValue() != personSol) {
				notPerson = card.getValue();
			}
			if(card.getValue().getType() == CardType.ROOM && card.getValue() != roomSol) {
				notRoom = card.getValue();
			}
			if(card.getValue().getType() == CardType.WEAPON && card.getValue() != weaponSol) {
				notWeapon = card.getValue();
			}
		}

		// Tests on checkAccusation
		assertTrue(board.checkAccusation(new Solution(personSol, roomSol, weaponSol))); // Check that an accusation w/ correct cards is true
		assertFalse(board.checkAccusation(new Solution(personSol, roomSol, personSol))); // Check that one incorrect card of wrong type returns false
		assertFalse(board.checkAccusation(new Solution(notPerson, notRoom, notWeapon))); // Check that all incorrect cards returns false
		assertFalse(board.checkAccusation(new Solution(notPerson, roomSol, weaponSol))); // Check that the wrong person returns false
		assertFalse(board.checkAccusation(new Solution(personSol, notRoom, weaponSol))); // Check that the wrong room returns false
		assertFalse(board.checkAccusation(new Solution(personSol, roomSol, notWeapon))); // Check that the wrong weapon returns false
	}

	/*
	 * Test that the disproving of a suggestion is handled appropriately
	 */
	@Test
	public void testDisproveSuggestion() {
		Map<String, Card> preDealDeck = board.getCards();
		Player testPlayer = new HumanPlayer("Test");
		// Pre-set cards chosen for suggestion
		Card suggPerson = preDealDeck.get("Sergeant Sam");
		Card suggRoom = preDealDeck.get("Office");
		Card suggWeapon = preDealDeck.get("Electric Fly Swatter");
		// Pre-set cards in player's hand. Make none match suggestion
		Card handPerson = preDealDeck.get("Aurora Angel");
		Card handRoom = preDealDeck.get("Batcave");
		Card handWeapon = preDealDeck.get("Rod");
		testPlayer.updateHand(handPerson);
		testPlayer.updateHand(handRoom);
		testPlayer.updateHand(handWeapon);
		
		Solution suggestion = new Solution(suggPerson, suggRoom, suggWeapon);
		assertEquals(null, testPlayer.disproveSuggestion(suggestion));
		
		// Pre-set cards with one match suggestion
		testPlayer.removeCard(handPerson);
		handPerson = suggPerson;
		testPlayer.updateHand(handPerson);
		
		assertEquals(handPerson, testPlayer.disproveSuggestion(suggestion));
		
		// Pre-set cards with all matching suggestions
		testPlayer.removeCard(handRoom);
		testPlayer.removeCard(handWeapon);
		handRoom = suggRoom;
		handWeapon = suggWeapon;
		testPlayer.updateHand(handRoom);
		testPlayer.updateHand(handWeapon);
		// Counters to test that every type of matching card is returned at least once
		int personCount = 0;
		int roomCount = 0;
		int weaponCount = 0;
		// Disprove suggestion 30 times, which should be enough to have each card shown at least once
		for(int i = 0; i < 30; i++) {
			Card suggResult = testPlayer.disproveSuggestion(suggestion);
			if(suggResult == handPerson) {
				personCount++;
			}
			else if(suggResult == handRoom) {
				roomCount++;
			}
			else if(suggResult == handWeapon) {
				weaponCount++;
			}
		}
		
		assertTrue(personCount > 0);
		assertTrue(roomCount > 0);
		assertTrue(weaponCount > 0);
	}
	
	/*
	 * Test the handling of a suggestion made
	 */
	@Test
	public void testHandleSuggestion() {
		Map<String, Card> preDealDeck = board.getCards();
		// Take all players, clear their hands so they don't interfere
		Player player1 = board.getPlayers().get(0);
		player1.clearHand();
		Player player2 = board.getPlayers().get(1);
		player2.clearHand();
		Player player3 = board.getPlayers().get(2);
		player3.clearHand();
		Player player4 = board.getPlayers().get(3);
		player4.clearHand();
		Player player5 = board.getPlayers().get(4);
		player5.clearHand();
		Player player6 = board.getPlayers().get(5);
		player6.clearHand();
		// Pre-set suggestion cards
		Card suggPerson = preDealDeck.get("Sergeant Sam");
		Card suggRoom = preDealDeck.get("Office");
		Card suggWeapon = preDealDeck.get("Electric Fly Swatter");
		// Test that no players can disprove the suggestion
		player1.updateHand(preDealDeck.get("Aurora Angel"));
		player1.updateHand(preDealDeck.get("Rod"));
		player2.updateHand(preDealDeck.get("Batcave"));
		player2.updateHand(preDealDeck.get("Malecave"));
		player3.updateHand(preDealDeck.get("Howard Handy"));
		player3.updateHand(preDealDeck.get("Nitrous Gas"));
		// Should not be able to return a card
		Solution testSuggestion = new Solution(suggPerson, suggRoom, suggWeapon);
		assertNull(board.handleSuggestion(player1, testSuggestion));
		
		// Update hand so that only player1, the suggestor, can disprove the suggestion
		player1.updateHand(suggPerson);
		// The suggestor should not disprove his own suggestion
		assertNull(board.handleSuggestion(player1, testSuggestion));
		
		// Check that the human player returns the correct card
		assertEquals(suggPerson, board.handleSuggestion(player2, testSuggestion));
		
		// Update hand so that player3 can disprove suggestion
		player3.updateHand(suggWeapon);
		// Should return only suggWeapon
		assertEquals(suggWeapon, board.handleSuggestion(player1, testSuggestion));
		assertNotEquals(suggPerson, board.handleSuggestion(player1, testSuggestion));
		
		// Update hand so that both player2 and player3 can disprove the suggestion
		player2.updateHand(suggRoom);
		// Should return only suggRoom, as that is the one that player2, the next in line, has
		assertEquals(suggRoom, board.handleSuggestion(player1, testSuggestion));
		assertNotEquals(suggPerson, board.handleSuggestion(player1, testSuggestion));
		assertNotEquals(suggWeapon, board.handleSuggestion(player1, testSuggestion));
	}
}
