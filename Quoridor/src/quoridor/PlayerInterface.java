package quoridor;

public interface PlayerInterface {

	public int getX();
	public int getY();
	//public int getGoal();
	public int getPlayer();
	public void setX(int i);
	public void setY(int i);
	public int getNumWalls();
	public void useWall();
	
}
