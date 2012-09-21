package quoridor;

public class Player implements PlayerInterface{

	private int x;
	private int y;
	private int player;
	//goal x coordinate
	private int goal;
	private int numWalls;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getPlayer() {
		return player;
	}
	
	public int getGoal() {
		return goal;
	}
	
	public void setX(int i) {
		x = i;
	}
	
	public void setY(int i) {
		y = i;
	}
	
	public Player(int i) {
		if (i == 1) {
			x = 0;
			y = 4;
			goal = 8;
		} else {
			x = 8;
			y = 4;
			goal = 0;
		}
		numWalls = 10;
		player = i;
	}
	
	public int getNumWalls() {
		return numWalls;
	}
	public void useWall() {
		numWalls --;
	}
}
