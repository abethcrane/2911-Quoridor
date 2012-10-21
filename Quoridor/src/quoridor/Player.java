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
	public Player(int i, int num_players) {
		if (i == 2) {
			x = 0;
			y = 4;
			maxGoalX = 8;
			minGoalX = 8;
			maxGoalY = 8;
			minGoalY = 0;
		} else if (i == 1) {
			x = 8;
			y = 4;
			maxGoalX = 0;
			minGoalX = 0;
			maxGoalY = 8;
			minGoalY = 0;
		} else if (i == 3) {
			x = 4;
			y = 0;
			maxGoalX = 8;
			minGoalX = 0;
			maxGoalY = 8;
			minGoalY = 8;
		} else if (i == 4) {
			x = 4;
			y = 8;
			maxGoalX = 8;
			minGoalX = 0;
			maxGoalY = 0;
			minGoalY = 0;
		}
		numWalls = 20/num_players;
		player = i;
	}
	
	public int getNumWalls() {
		return numWalls;
	}
	public void useWall() {
		numWalls --;
	}
	
	public void unUseWall() {
		numWalls ++;
	}
	
	public void setWalls(int i) {
		numWalls = i;
	}
	
}
