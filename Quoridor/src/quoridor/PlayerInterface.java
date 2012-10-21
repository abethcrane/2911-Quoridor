package quoridor;

public interface PlayerInterface {

	/**
	 * @return The x location of the player
	 */
	public int getX();
	
	/**
	 * @return The y location of the player
	 */
	public int getY();
	
	/**
	 * @return The Player's number ID 
	 */
	public int getPlayer();
	
	/**
	 * @param i Sets the x location of the player
	 */
	public void setX(int i);
	
	/**
	 * @param i Sets the y location of the player
	 */
	public void setY(int i);
	
	/**
	 * @return The number of walls the player has left
	 */
	public int getNumWalls();
	
	/**
	 * Decreases the number of walls the player has left 
	 */
	public void useWall();
	
}
