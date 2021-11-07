package clueGame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class KnownCardsDisplay extends JPanel {
	// Variables for each type of card, which will be updated throughout game
	private JPanel peopleHand;
	private JPanel peopleSeen;
	private JPanel roomHand;
	private JPanel roomSeen;
	private JPanel weaponHand;
	private JPanel weaponSeen;
	
	/*
	 * Constructor for the display. Most of the
	 * action happens here
	 */
	public KnownCardsDisplay() {
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));

		// Create the known people display
		JPanel peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(2, 1));
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		
		peopleHand = new JPanel();
		peopleHand.setLayout(new GridLayout(0, 1));
		peopleHand.add(new JLabel("In Hand:"));
		peoplePanel.add(peopleHand);
		
		peopleSeen = new JPanel();
		peopleSeen.setLayout(new GridLayout(0, 1));
		peopleSeen.add(new JLabel("Seen:"));
		peoplePanel.add(peopleSeen);
		
		add(peoplePanel);

		// Create the known people display
		JPanel roomsPanel = new JPanel();
		roomsPanel.setLayout(new GridLayout(2, 1));
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		
		roomHand = new JPanel();
		roomHand.setLayout(new GridLayout(0, 1));
		roomHand.add(new JLabel("In Hand:"));
		roomsPanel.add(roomHand);
		
		roomSeen = new JPanel();
		roomSeen.setLayout(new GridLayout(0, 1));
		roomSeen.add(new JLabel("Seen:"));
		roomsPanel.add(roomSeen);
		
		add(roomsPanel);

		// Create the known people display
		JPanel weaponsPanel = new JPanel();
		weaponsPanel.setLayout(new GridLayout(2, 1));
		weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		
		weaponHand = new JPanel();
		weaponHand.setLayout(new GridLayout(0, 1));
		weaponHand.add(new JLabel("In Hand:"));
		weaponsPanel.add(weaponHand);
		
		weaponSeen = new JPanel();
		weaponSeen.setLayout(new GridLayout(0, 1));
		weaponSeen.add(new JLabel("Seen:"));
		weaponsPanel.add(weaponSeen);
		
		add(weaponsPanel);
	}
	
	// Method to cycle through the player's hand and load their cards
	public void loadHand(Set<Card> hand) {
		for(Card card : hand) {
			JTextField field = new JTextField();
			field.setEditable(false);
			field.setText(card.getName());
			field.setBackground(Color.WHITE);
			switch(card.getType()) {
				case PERSON:
					peopleHand.add(field);
					break;
				case ROOM:
					roomHand.add(field);
					break;
				case WEAPON:
					weaponHand.add(field);
					break;
				default:
					break;
			}
		}
	}
	
	// Method to add a new card to those seen by the player
	public void updateSeen(Card card, Player cardHolder) {
		JTextField field = new JTextField();
		field.setEditable(false);
		field.setText(card.getName());
		field.setBackground(cardHolder.getColor());
		switch(card.getType()) {
			case PERSON:
				peopleSeen.add(field);
				break;
			case ROOM:
				roomSeen.add(field);
				break;
			case WEAPON:
				weaponSeen.add(field);
				break;
			default:
				break;
		}
	}

	// Main method for testing
	public static void main(String[] args) {
		KnownCardsDisplay display = new KnownCardsDisplay(); // Create the panel
		JFrame frame = new JFrame(); // Create the frame
		frame.setContentPane(display); // Put the panel in the frame
		frame.setSize(225, 750); // Size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stop program on close
		
		// Test with only the hand known, nothing else
		Set<Card> hand = new HashSet<>();
		hand.add(new Card('P', "Professor Plum", CardType.PERSON));
		hand.add(new Card('S', "Miss Scarlett", CardType.PERSON));
		hand.add(new Card('W', "Wrench", CardType.WEAPON));
		
		display.loadHand(hand);
		
		/*
		 * To test without seeing any cards, remove // from line 148 and line 150
		 * To test with seeing cards, add // to line 148 and line 150
		 */
		//frame.setVisible(true); // Make it visible
		
		///*
		// Test with a small selection of players showing cards
		Player player0 = new ComputerPlayer("Steamboat Wiley");
		Player player1 = new ComputerPlayer("Dark InVader");
		Player player2 = new ComputerPlayer("Captain Oil");
		Player player3 = new ComputerPlayer("FatMan");
		
		display.updateSeen(new Card('W', "Mrs. White", CardType.PERSON), player0);
		display.updateSeen(new Card('P', "Mrs. Peacock", CardType.PERSON), player1);
		display.updateSeen(new Card('G', "Reverend Green", CardType.PERSON), player0);
		
		display.updateSeen(new Card('H', "Hall", CardType.ROOM), player2);
		display.updateSeen(new Card('B', "Ballroom", CardType.ROOM), player3);
		display.updateSeen(new Card('K', "Kitchen", CardType.ROOM), player3);
		display.updateSeen(new Card('R', "Billiard Room", CardType.ROOM), player2);
		display.updateSeen(new Card('C', "Conservatory", CardType.ROOM), player0);
		display.updateSeen(new Card('L', "Lounge", CardType.ROOM), player1);
		display.updateSeen(new Card('B', "Library", CardType.ROOM), player0);
		display.updateSeen(new Card('D', "Dining Room", CardType.ROOM), player2);
		
		display.updateSeen(new Card('L', "Lead Pipe", CardType.WEAPON), player1);
		display.updateSeen(new Card('D', "Dagger", CardType.WEAPON), player3);
		display.updateSeen(new Card('R', "Revolver", CardType.WEAPON), player3);
		
		frame.setVisible(true); // Make it visible
		//*/
	}
}
