package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class clIO implements IO {

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/* (non-Javadoc)
	 * @see quoridor.Printer#printMessage(java.lang.Object)
	 */
	public void printMessage (Object s) {
		System.out.println(s);
	}
	
	/* (non-Javadoc)
	 * @see quoridor.Printer#printMessageBlank(java.lang.Object)
	 */
	public void printMessageBlank (Object s) {
		System.out.print(s);
	}

	/* (non-Javadoc)
	 * @see quoridor.IO#readInput()
	 */
	@Override
	public String readInput() throws IOException {
		return in.readLine();
	}

}
