package quoridor;

import java.io.IOException;

public interface IO {

	/**
	 * @param s The object to be printed (with a newline). Anything that is printable.
	 */
	public void printMessage (Object s);
	
	/**
	 * @param s The object to be printed (without a newline). Anything that is printable.
	 */
	public void printMessageBlank (Object s);

	/**
	 * 
	 * @return String of input read in.
	 * @throws IOException 
	 */
	public String readInput () throws IOException;
	
}
