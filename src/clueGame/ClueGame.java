package clueGame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ClueGame extends JFrame {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Clue Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Board gameboard = Board.getInstance();
		gameboard.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		gameboard.initialize();
		
		frame.add(gameboard, BorderLayout.CENTER);
		
		frame.pack();
		
		frame.setVisible(true);
	}
}
