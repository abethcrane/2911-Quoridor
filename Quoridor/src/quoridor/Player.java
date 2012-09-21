package quoridor;

public class Player implements PlayerInterface{

	public int x;
	public int y;
	public int player;
	
	public Player(int i) {
		if (i == 1) {
			x = 0;
			y = 4;
		} else {
			x = 8;
			y = 4;
		}
		player = i;
	}
}
