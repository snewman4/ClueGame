/**
 * BadConfigFormat Exception
 * 
 * @author Sam Newman
 * @section CSCI 306A
 * 
 * This exception reports if the config files are not
 * appropriately configured
 */

package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super("One of the config files was not formatted properly. Please check them and try again.");
		printToLog("One of the config files was not formatted properly. Please check them and try again.");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
		printToLog(message);
	}

	private void printToLog(String message) {
		try {
			PrintWriter out = new PrintWriter("exceptionLog.txt");
			out.println(message);
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("exceptionLog.txt could not be initialized");
		}
	}
}
