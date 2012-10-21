package quoridor;

import java.util.*;
public class Board implements BoardInterface {

	final static int numRows = 9;
	final static int numCols = 9;
	final static int numDirections = 4;

	public Cell board[][] = new Cell [numRows][numCols];

	// The printer class to interface with the GUI
	IO io = IOFactory.getIO();

	/**
	 * @param players The array of all players in the game
	 */
	public Board(Player[] players) {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				board[i][j] = new Cell();
			}
		}

		// We number our players from 1, as that's how they appear in the display
		for (int i = 1; i < players.length; i++) {
			board[players[i].getX()][players[i].getY()].playerNum = players[i].getPlayer();
		}

		// Set the initial edges to have walls, so as to make it easy to say it's illegal to place one there
		for (int i = 0; i < numRows; i++) {
			board[i][0].v = true;
			board[0][i].h = true;
		}

	}

	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#isLegalMove(quoridor.Player, int, int)
	 */
	@Override
	public boolean isLegalMove(Player p, int x, int y) {
		
		boolean result = false;
		// The move is false if it's out of the bounds of the board
		if (x < numRows && y < numCols && x >= 0 && y >= 0) {

			// If there's a player in the cell we're moving to then it's invalid
			if (board[x][y].playerNum == 0) {
				// 0 = p.getX()-x, 1 = p.getY()-y, 2 = checkX, 3 = checkY, 4 = wall Direction
				// up, down, left, right
				int[][] checks = {
						{ 1,  0, p.getX(), y,        1},
						{-1,  0, x,        y,        1},
						{ 0, -1, x,        y,        0},
						{ 0,  1, x,        p.getY(), 0}};

				// For each direction, if there's no wall where they're moving it's valid
				for (int i = 0; (i < numDirections) && (result == false); i++) {
					if ((p.getX()-x) == checks[i][0] && (p.getY()-y) == checks[i][1]) {
						// If wallDir is 1 we check h, else v
						// If there's a wall result is false, else it's true (hence the not)
						result = checks[i][4] == 1 ? !board[checks[i][2]][checks[i][3]].h : !board[checks[i][2]][checks[i][3]].v;
					}  
				}

				// If they didn't move straight up/down/left/right, check if it's a jump or diagonal
				if (result == false) {
					if (isLegalJumpMove(p,x,y)== true || isLegalDiagonalMove(p,x,y) == true ) {
						result = true;
					}
				}

			} 

		}
		return result;
	}

	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#placeWall(quoridor.Player[], int, int, int, char)
	 */
	@Override
	public boolean placeWall(Player[] players, int current, int x, int y, char d) {
		boolean r = false;

		// If it's a legal wall placement then place the wall
		if (isLegalWall(players,x,y,d) == true) {
			r = true;
			players[current].useWall();
			if (d == 'h') {
				board[x][y].h = true;
				board[x][y+1].h = true;
			} else if (d == 'v') {
				board[x-1][y+1].v = true;
				board[x][y+1].v = true;
			}
		}
		return r;
	}

	// Written for use with undo, this removes a wall and changes the playcount back
	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#removeWall(quoridor.Player, int, int, char)
	 */
	public boolean removeWall(Player p, int x, int y, char d) {
		boolean r = false;
		if (d == 'h') {
			if (board[x][y].h == true) {
				board[x][y].h = false;
				board[x][y+1].h = false;
				p.unUseWall();
				r = true;
			}
		} else if (d == 'v') {
			if (board[x][y].v == true) {
				board[x][y].v = false;
				board[x-1][y].v = false;
				p.unUseWall();
				r = true;
			}
		}
		return r;

	}

	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#movePlayer(quoridor.Player, int, int, boolean)
	 */
	@Override
	public boolean movePlayer(Player p, int x, int y, boolean undo) {
		boolean r = false;
		// Undo doesn't have to be a legal move, so we trust it's valid
		if (undo || isLegalMove(p,x,y)) {
			r = true;
			int startx = p.getX();
			int starty = p.getY();
			// Switch the current position to have no player
			board[startx][starty].playerNum = 0;
			// And the player's new position to have their player number
			board[x][y].playerNum = p.getPlayer();
			// Change the player's stored position
			p.setX(x);
			p.setY(y);
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#displayBoard()
	 */
	@Override

	public void displayBoard() {

		// For each row we print out spaces and dashes where appropriate
		for (int i = 0; i < numRows; i++) {
			io.printMessageBlank(" ");
			for (int j = 0; j < numCols; j++) {
				if (board[i][j].h == false) {
					io.printMessageBlank("  ");
				} else {
					io.printMessageBlank(" _");
				}
				io.printMessageBlank(" ");

			}
			io.printMessage("");

			// For each cell, we print whether or not there's a player there
			// And if there are vertical walls or not
			for (int j = 0; j < numCols; j++) {
				if (board[i][j].v == false) {
					io.printMessageBlank(" ");
				} else {
					io.printMessageBlank("|");
					if (j == 0) {
						io.printMessageBlank(" ");
					}
				}
				if (board[i][j].playerNum == 0) {
					io.printMessageBlank("#");
				} else {
					io.printMessageBlank(board[i][j].playerNum);
				}
				io.printMessageBlank(" ");
			}	

			// Printing the end pipe before the row number
			io.printMessage("|"+(i+1));
		}

		// We have to print the column numbers as well
		io.printMessage( "  _  _  _  _  _  _  _  _  _");
		io.printMessage( "  a  b  c  d  e  f  g  h  i");
	}

	/* (non-Javadoc)
	 * @see quoridor.BoardInterface#checkWinner(quoridor.Player[])
	 */
	@Override
	public Player checkWinner(Player[] players) {
		// For each player, we check if they're between their goalX and goalY ranges
		// If a player is, they've won
		for (int i = 1; i < players.length; i++) {
			if (players[i].getX() <= players[i].maxGoalX 
					&& players[i].getX() >= players[i].minGoalX
					&& players[i].getY() <= players[i].maxGoalY 
					&& players[i].getY() >= players[i].minGoalY) {
				return players[i];
			}
		}
		return null;
	}
 
	/**
	 * Checks that the wall is going to be placed within the borders of the game
	 * And won't collide with other walls
	 * @param players The array of all players
	 * @param x The x location of the wall 
	 * @param y The y location of the wall
	 * @param d The direction of the wall
	 * @return Whether the wall is legal
	 */
	public boolean isLegalWall(Player[] players, int  x, int y, char d) {
		boolean r = false;

		// Don't bother checking further if it's out of bounds
		if (x > 0 && y >= 0 && x < numRows && y < numCols -1) {

			// Check there's no horizontal wall
			if (d == 'h') {
				if (board[x][y].h == false && board[x][y+1].h == false) {
					// If there are no intersecting vertical walls, it's legal
					if (!(board[x-1][y+1].v == true && board[x][y+1].v==true)) {
						r = true;
						// Temporarily set the walls to true
						board[x][y].h = true;
						board[x][y+1].h = true;
					}
				}	
				// Check there's no vertical wall there
			} else if (d == 'v') {
				if ((board[x][y+1].v == false) && (board[x-1][y+1].v == false)) {
					// If there are no intersecting horizontal walls, it's legal
					if (!(board[x][y].h == true && board[x][y+1].h == true)) {
						r = true;
						// Temporarily set the walls to true
						board[x-1][y+1].v = true;
						board[x][y+1].v = true;

					}
				}
			}

		}

		// If it cuts off another players path to their goal, it's not legal
		// So we set the walls to true above, now check this, and then delete the walls
		if (r == true) {
			for (int i = 1; i < players.length; i ++) {
				if (stepsToGoal(players[i]) == -1) {
					r = false;
				}
			}

			// Delete the walls
			if (d == 'h') {
				board[x][y].h = false;
				board[x][y+1].h = false;
			} else {
				board[x-1][y+1].v = false;
				board[x][y+1].v = false;
			}
		}

		return r;
	}

	/**
	 * Checks if it's legal to jump over a player
	 * Checks for existence of player, and non-existence of walls
	 * @param p The player whose move we're testing
	 * @param x The potential x location
	 * @param y The potential y location
	 * @return Whether the move is a legal jump
	 */
	
	private boolean isLegalJumpMove(Player p, int x, int y) {
		boolean r = false;


		// Check the destination cell is in bounds first
		if (x > 0 && x < numRows && y > 0 && y < numCols) {

			// 0 = p.getX()-x, 1 = p.getY()-y, 2 = checkXPlayer, 3 = checkYPlayer, 
			// 4 = wallX1, 5 = wallY1, 6 = wallY1, 7 = wallY2, 8 = wall Direction  
			int[][] checks = {{ 2,  0, p.getX()-1, 			y, p.getX()-1, 			y, p.getX(), 		y, 1},
					{-2,  0, p.getX()+1, 			y, p.getX()+1, 			y, p.getX(), 		y, 1},
					{ 0,  2, 			x, p.getY()-1, 			x, p.getY()-1, 		  x, p.getY(), 0},
					{ 0, -2, 			x, p.getY()+1, 			x, p.getY()+2, 		  x, p.getY(), 0}};

			// Check each direction
			for (int i = 0; (i < numDirections) && (r == false); i++) {
				// If we're moving in this current direction (the right x-x and y-y gaps)
				if ((p.getX()-x) == checks[i][0] && (p.getY()-y) == checks[i][1]) {
					// If there's a player in the adjacent cell in the right direction
					if (board[checks[i][2]][checks[i][3]].playerNum != 0) {
						// If it's a vertical jump we check there's no wall horizontally
						if (checks[i][8] == 1) {
							if (board[checks[i][4]][checks[i][5]].h == false &&
									board[checks[i][6]][checks[i][7]].h == false) {
								r = true;
							}
							// And if it's a horizontal jump we check for vertical walls
						} else {
							if (board[checks[i][4]][checks[i][5]].v == false &&
									board[checks[i][6]][checks[i][7]].v == false) {
								r = true;
							}
						}
					}  
				}

			}

		}

		return r;

	}

	/**
	 * 	 * It's legal if there's a player adjacent to them and a wall behind the player
	 * And they jump in that direction
	 * @param p The player whose move we're testing
	 * @param x The potential new x location
	 * @param y The potential new y location
	 * @return Whether it's a legal diagonal move
	 */
	private boolean isLegalDiagonalMove (Player p, int x, int y) {
		boolean r = false;

		int curX = p.getX();
		int curY = p.getY();
		// Check if we're changing X and Y by one each (valid diagonal)
		if (Math.abs(curX-x) == 1 && Math.abs(curY-y) == 1) {
			// We need to have a player at either one col different from us (in our jump direction)
			if (board[curX][y].playerNum != 0) {
				// If there's a vertical wall on the col on their other side, it's valid
				if (board[curX][y+y-curY].v == true || board[curX-1][y+y-curY].v == true) {
					r  = true;
				}
			} 

			// Or we need to have a player at one row different from us (in our jump direction)
			if (board[x][curY].playerNum != 0) {
				int b = x+x-curX;
				// If there's a horizontal wall on the row on their other side, it's valid
				if (b+1 < 9 && board[b+1][curY].h == true){ 
					r = true;
				}
			}

		}

		return r;
	}


	/**
	 * Used to determine if there's a valid path to the goal
	 * And if there is, how long it is (used for AI)
	 * @param p The player we're testing
	 * @return The number of steps to the goal, or -1 if there's no path
	 */
	public int stepsToGoal (Player p) {
		boolean foundGoal = false;
		Queue<Position> moves = new LinkedList<Position>();
		Position start = new Position(p.getX(),p.getY(),0,' ');
		moves.add(start);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		// While we haven't found the goal and have moves to check, check them
		while ((foundGoal == false) && (moves.isEmpty() == false)) {
			Position current = moves.remove();
			if (current.x <= p.maxGoalX 
					&& current.x >= p.minGoalX
					&& current.y <= p.maxGoalY 
					&& current.y >= p.minGoalY) {
				return current.steps;
			}
			
			if (current.y-1 >=0 && board[current.x][current.y].v == false && board[current.x][current.y-1].playerNum == 0) {
				Position up = new Position(current.x,current.y-1,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y-1);
				if (map.containsKey(temp) == false) {
					moves.add(up);
					map.put(temp, 1);
				}
			}

			if (current.y-2 >=0 && board[current.x][current.y-1].v == false && current.steps == 0
					&& board[current.x][current.y-1].playerNum != 0 && board[current.x][current.y-2].playerNum == 0) {
				Position jumpLeft = new Position(current.x,current.y-2,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y-2);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}
			if (current.y+1 <=8 && current.x >= 0 && board[current.x][current.y+1].v == false && board[current.x][current.y+1].playerNum == 0) {
				Position down = new Position(current.x,current.y+1,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y+1);
				if (map.containsKey(temp) == false) {
					moves.add(down);
					map.put(temp,1);
				}
			}
			if (current.y+2 <=8 &&current.x >= 0  && board[current.x][current.y+2].v == false && current.steps == 0
					&& board[current.x][current.y+1].playerNum != 0 && board[current.x][current.y+2].playerNum == 0) {
				Position jumpLeft = new Position(current.x,current.y-2,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y+2);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}
			if (current.x-1>=0 && current.y >=0 && board[current.x][current.y].h == false && board[current.x-1][current.y].playerNum == 0) {
				Position left = new Position(current.x-1,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x-1) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(left);
					map.put(temp, 1);
				}
			}
			if (current.x-2 >=0 && current.y >=0 && board[current.x-1][current.y].h == false && current.steps == 0
					&& board[current.x-1][current.y].playerNum != 0 && board[current.x-2][current.y].playerNum == 0) {
				Position jumpLeft = new Position(current.x-2,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x-2) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}
			if (current.x+1 <=8 && current.y >= 0 && board[current.x+1][current.y].h == false && board[current.x+1][current.y].playerNum == 0) {
				Position right = new Position(current.x+1,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x+1) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(right);
					map.put(temp,1);
				}
			}
			if (current.x+2 <=8 && current.y >= 0 &&board[current.x+1][current.y].h == false && current.steps == 0
					&& board[current.x+1][current.y].playerNum != 0 && board[current.x+2][current.y].playerNum == 0) {
				Position jumpLeft = new Position(current.x+2,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x+2) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}
		}
		return -1;
	}

	/**
	 * @param p The player we're testing
	 * @return The number of steps to the next row towards the player's goal, or -1 if no path
	 */
	public int stepsToNextRow (Player p) {
		boolean foundGoal = false;
		Queue<Position> moves = new LinkedList<Position>();
		Position start = new Position(p.getX(),p.getY(),0,' ');
		moves.add(start);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		while ((foundGoal == false) && (moves.isEmpty() == false)) {
			Position current = moves.remove();

			if (p.getPlayer() == 1) {
				if (current.x == p.getX() -1) {
					return current.steps;
				}
			}
			if (p.getPlayer() == 2) {
				if (current.x == p.getX() +1) {
					return current.steps;
				}
			}

			if (current.y-1 >=0 && board[current.x][current.y].v == false && board[current.x][current.y-1].playerNum == 0) {
				Position up = new Position(current.x,current.y-1,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y-1);
				if (map.containsKey(temp) == false) {
					moves.add(up);
					map.put(temp, 1);
				}
			}

			if (current.y-2 >=0 && board[current.x][current.y-1].v == false && current.steps == 0
					&& board[current.x][current.y-1].playerNum != 0 && board[current.x][current.y-2].playerNum == 0) {
				Position jumpLeft = new Position(current.x,current.y-2,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y-2);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}

			if (current.y >= 0 && current.y+1 <=8 && current.x >= 0 && board[current.x][current.y+1].v == false && board[current.x][current.y+1].playerNum == 0) {
				Position down = new Position(current.x,current.y+1,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y+1);
				if (map.containsKey(temp) == false) {
					moves.add(down);
					map.put(temp,1);
				}
			}
			if (current.y+2 <=8 &&current.x >= 0  && board[current.x][current.y+2].v == false && current.steps == 0
					&& board[current.x][current.y+1].playerNum != 0 && board[current.x][current.y+2].playerNum == 0) {
				Position jumpLeft = new Position(current.x,current.y-2,current.steps+1,' ');
				String temp = Integer.toString(current.x) + Integer.toString(current.y+2);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}

			if (current.x-1>=0 && current.y >=0 && board[current.x][current.y].h == false && board[current.x-1][current.y].playerNum == 0) {
				Position left = new Position(current.x-1,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x-1) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(left);
					map.put(temp, 1);
				}
			}
			if (current.x-2 >=0 && current.y >=0 && board[current.x-1][current.y].h == false && current.steps == 0
					&& board[current.x-1][current.y].playerNum != 0 && board[current.x-2][current.y].playerNum == 0) {
				Position jumpLeft = new Position(current.x-2,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x-2) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}

			if (current.x+1 <=8 && current.y >= 0 && board[current.x+1][current.y].h == false && board[current.x+1][current.y].playerNum == 0) {
				Position right = new Position(current.x+1,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x+1) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(right);
					map.put(temp,1);
				}
			}
			if (current.x+2 <=8 && current.y >= 0 &&board[current.x+1][current.y].h == false && current.steps == 0
					&& board[current.x+1][current.y].playerNum != 0 && board[current.x+2][current.y].playerNum == 0) {
				Position jumpLeft = new Position(current.x+2,current.y,current.steps+1,' ');
				String temp = Integer.toString(current.x+2) + Integer.toString(current.y);
				if (map.containsKey(temp) == false) {
					moves.add(jumpLeft);
					map.put(temp, 1);
				}
			}
		}
		return -1;
	}

	// Copies the board over to another existing Board
	/**
	 * @param b
	 */
	public void copyBoard(Board b) {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				board[i][j].h = b.board[i][j].h;
				board[i][j].v = b.board[i][j].v;
			}
		}
	}
 
	/**
	 * @param b Another board to test
	 * @return Whether there are any newly placed walls
	 */
	public Wall findNewWall(Board b) {
		int x;
		int y;
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j ++) {

				// If there's a new wall here, mark x and y and return the new Wall
				if ((board[i][j].h == false && b.board[i][j].h == true)) {
					x = i;
					y = j;
					return (new Wall(x,y,'h'));

				}
				if ((board[i][j].v == false && b.board[i][j].v == true)) {
					x = i;
					y = j;
					return (new Wall(x,y,'v'));
				}
			}
		}
		io.printMessage("No new wall detected");
		return null;
	}

	/**
	 * @param playerNum The number of the player to find
	 * @return The position of the player on board, or null
	 */
	public Position findPlayer(int playerNum) {
		for (int i = 0; i < numRows; i ++) {
			for (int j = 0; j < numCols; j ++) {
				if (board[i][j].playerNum == playerNum) {
					// If found, return the position. Else null is returned.
					return new Position(i,j,0,' ');
				}
			}
		}
		return null;
	}
}
