package clueGame;

import java.awt.Color;
import java.awt.Dimension;
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
	private static final String SEEN = "Seen:";
	private static final String IN_HAND = "In Hand:";
	// Variables for each type of card, which will be updated throughout game
	private JPanel peoplePanel;
	private JPanel roomsPanel;
	private JPanel weaponsPanel;
	// Text fields that will store the first card of that type seen. Starts as none.
	private JTextField peopleFirstSeen;
	private JTextField roomsFirstSeen;
	private JTextField weaponsFirstSeen;
	
	/*
	 * Constructor for the display. Most of the
	 * action happens here
	 */
	public KnownCardsDisplay() {
		setPreferredSize(new Dimension(200, 700));
		setLayout(new GridLayout(3, 1));
		setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));

		// Create the known people display
		peoplePanel = new JPanel();
		peoplePanel.setLayout(new GridLayout(0, 1));
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		peoplePanel.add(new JLabel(IN_HAND));
		
		add(peoplePanel);

		// Create the known people display
		roomsPanel = new JPanel();
		roomsPanel.setLayout(new GridLayout(0, 1));
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		roomsPanel.add(new JLabel(IN_HAND));
		
		add(roomsPanel);

		// Create the known people display
		weaponsPanel = new JPanel();
		weaponsPanel.setLayout(new GridLayout(0, 1));
		weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		weaponsPanel.add(new JLabel(IN_HAND));
		
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
					peoplePanel.add(field);
					break;
				case ROOM:
					roomsPanel.add(field);
					break;
				case WEAPON:
					weaponsPanel.add(field);
					break;
				default:
					break;
			}
		}
		// If no cards were read in for a certain field, update it so that it shows none
		if(peoplePanel.getComponentCount() == 1) {
			JTextField noneField = new JTextField();
			setNoneField(noneField);
			peoplePanel.add(noneField);
		}
		if(roomsPanel.getComponentCount() == 1) {
			JTextField noneField = new JTextField();
			setNoneField(noneField);
			roomsPanel.add(noneField);
		}
		if(weaponsPanel.getComponentCount() == 1) {
			JTextField noneField = new JTextField();
			setNoneField(noneField);
			weaponsPanel.add(noneField);
		}
		// Add the seen labels
		peoplePanel.add(new JLabel(SEEN));
		roomsPanel.add(new JLabel(SEEN));
		weaponsPanel.add(new JLabel(SEEN));
		// Set the seen fields to "None" (the player will always start without seeing anything)
		peopleFirstSeen = new JTextField();
		roomsFirstSeen = new JTextField();
		weaponsFirstSeen = new JTextField();
		
		setNoneField(peopleFirstSeen);
		peoplePanel.add(peopleFirstSeen);
		setNoneField(roomsFirstSeen);
		roomsPanel.add(roomsFirstSeen);
		setNoneField(weaponsFirstSeen);
		weaponsPanel.add(weaponsFirstSeen);
	}
	
	// Method to set up a "None"-valued text field
	private JTextField setNoneField(JTextField currField) {
		currField.setText("None");
		currField.setEditable(false);
		currField.setBackground(Color.WHITE);
		return currField;
	}
	
	// Method to determine where to add a seen card
	public void updateSeen(Card card, Player cardHolder) {
		switch(card.getType()) {
			case PERSON:
				addCardSeen(peopleFirstSeen, peoplePanel, card, cardHolder);
				break;
			case ROOM:
				addCardSeen(roomsFirstSeen, roomsPanel, card, cardHolder);
				break;
			case WEAPON:
				addCardSeen(weaponsFirstSeen, weaponsPanel, card, cardHolder);
				break;
			default:
				break;
		}
	}

	// Method to add a card to the specified seen field, using appropriate coloring
	private void addCardSeen(JTextField firstSeen, JPanel activePanel, Card card, Player cardHolder) {
		// If the current panel still has a none field, this is the first card of that type seen
		if(firstSeen.getText().equals("None")) {
			firstSeen.setText(card.getName());
			firstSeen.setBackground(cardHolder.getColor());
		}
		// If it doesn't have a none field, then a new field must be added
		else {
			JTextField newField = new JTextField();
			newField.setText(card.getName());
			newField.setBackground(cardHolder.getColor());
			newField.setEditable(false);
			activePanel.add(newField);
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
		 * To test without seeing any cards, remove // from line 176 and line 178
		 * To test with seeing cards, add // to line 176 and line 178
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