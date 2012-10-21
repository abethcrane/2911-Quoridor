package quoridor;

public class Player implements PlayerInterface{

	/**
	 *  The x location of the player
	 */
	private int x;
	
	/**
	 * The y location of the player 
	 */
	private int y;
	
	/**
	 * The ID number of the player
	 */
	private int player;
	
	/**
	 * The max x location for the player's x goal 
	 */
	public int maxGoalX;
	
	/**
	 * The max y location for the player's y goal
	 */
	public int maxGoalY;
	
	/**
	 * The min x location for the player's x goal 
	 */
	public int minGoalX;
	
	/**
	 * THe min y location for the player's y goal 
	 */
	public int minGoalY;
	
	/**
	 * The number of walls the player has left 
	 */
	private int numWalls;
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#getX()
	 */
	public int getX() {
		return x;
	}
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#getY()
	 */
	public int getY() {
		return y;
	}
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#getPlayer()
	 */
	public int getPlayer() {
		return player;
	}
	
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#setX(int)
	 */
	public void setX(int i) {
		x = i;
	}
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#setY(int)
	 */
	public void setY(int i) {
		y = i;
	}

	/**
	 * Hardocdes in the starting position of the player, depending on their player ID
	 * @param i The ID of the player
	 * @param num_players The total number of players
	 */
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
		setWalls(20/num_players);
		player = i;
	}
	
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#getNumWalls()
	 */
	public int getNumWalls() {
		return numWalls;
	}
	/* (non-Javadoc)
	 * @see quoridor.PlayerInterface#useWall()
	 */
	public void useWall() {
		numWalls--;
	}
	
	/**
	 * Increases the number of walls a player has, in the event of a wall placement being undone
	 */
	public void unUseWall() {
		numWalls++;
	}
	
	/**
	 * @param i The number of walls the player has
	 */
	public void setWalls(int i) {
		numWalls = i;
	}
	
}
