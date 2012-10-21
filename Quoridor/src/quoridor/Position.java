package quoridor;

public class Position {

	int x,y,steps;
	char d;
	
	/**
	 * Used to pass the AI's move back to the game
	 * @param a New x location
	 * @param b New y location
	 * @param s Number of steps to the goal 
	 * @param q Direction of any wall
	 */
	public Position(int a, int b, int s, char q) {
		x = a;
		y = b;
		steps = s;
		d = q;
	}
}
