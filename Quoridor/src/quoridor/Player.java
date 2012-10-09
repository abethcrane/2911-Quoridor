package quoridor;

public class Player implements PlayerInterface{

	private int x;
	private int y;
	private int player;
	public int maxGoalX;
	public int maxGoalY;
	public int minGoalX;
	public int minGoalY;
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
	
	
	public void setX(int i) {
		x = i;
	}
	
	public void setY(int i) {
		y = i;
	}
	
	//generalise this to have more players, but for now okay
	public Player(int i) {
		if (i == 1) {
			x = 0;
			y = 4;
			maxGoalX = 8;
			minGoalX = 8;
			maxGoalY = 8;
			minGoalY = 0;
		} else if (i == 2) {
			x = 8;
			y = 4;
			maxGoalX = 0;
			minGoalX = 0;
			maxGoalY = 8;
			minGoalY = 0;
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
