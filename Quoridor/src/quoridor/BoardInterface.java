package quoridor;

public interface BoardInterface {
	
	/**
	 * 
	 * @param P A Player to test the validity of a move for
	 * @param x The desired new x location
	 * @param y The desired new y location
	 * @return Whether or not the move is valid
	 */
	boolean isLegalMove(Player P, int x, int y);
	
	/**
	 *
	 * @param players An array of all players in the game
	 * @param player The player the wall is being placed for
	 * @param x The x location for the new wall		
	 * @param y The y location for the new wall
	 * @param d The direction of the wall
	 * @return Whether the wall was validly placed
	 */
	boolean placeWall(Player[] players, int player, int x, int y, char d);
	
	/**
	 * 
	 * @param p The player to remove the wall for
	 * @param x The x location of the wall
	 * @param y The y location of the wall
	 * @param d The direction of the wall
	 * @return Whether the wall was validly removed
	 */
	boolean removeWall(Player p, int x, int y, char d);
	
	/** 
	 * 
	 * @param p The player who is being moved
	 * @param x The new x location
	 * @param y The new y location
	 * @param undo Whether or not this player is being moved or unmoved
	 * @return Whether the player was moved without error
	 */
	boolean movePlayer(Player p, int x, int y, boolean undo);

	/**
	 * 
	 * @param players The array of all players in the game
	 * @return The player who has won, or null
	 */
	Player checkWinner(Player[] players);
	
	/**
	 * Displays the board
	 */
	void displayBoard();
	
	
}
