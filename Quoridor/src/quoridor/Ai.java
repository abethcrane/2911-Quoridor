package quoridor;
import java.util.*;
public class Ai {

	static IO io = IOFactory.getIO();
	
	/**
	 * @param b Game board
	 * @param players Array of all players
	 * @param depth Current minmax depth
	 * @return Maximum of minmax
	 */
	public static MinMax max (Board b,Player[] players,int depth) {
		
		//if we at the last depth of the min max return the score of this node
		if (depth == 0) {
			//return 0 if at goal
			if (b.findPlayer(2).x == 8) {
				return new MinMax(b,0);
			}
			double rand = Math.random()*2;
			return new MinMax(b,(((b.stepsToGoal(players[2]))-b.stepsToGoal(players[1]))*0.6) + ((b.stepsToNextRow(players[1]))*0.8)+((Math.pow(b.stepsToNextRow(players[2]),-1))*6)-rand);
			//return new MinMax(b,b.stepsToGoal(players[1]));
		}
		//gets the list of all possible moves from current board state
		List<Board> moves = generateMoves(b,players,2);
		Iterator<Board> it = moves.iterator();
		// sets alpha to an arbitrary high or low number for use later in the function
		double alpha = 999;
		MinMax bestMove = new MinMax(b, 0);
		// loops through all the moves evaluating them by calling minmax again(min this time)
		while (it.hasNext()) {
			Board x = it.next();
			Position y = x.findPlayer(1);
			Player[] ps = new Player[3];
			ps[1] = new Player(1,2);
			ps[1].setX(y.x);
			ps[1].setY(y.y);
			Position z = x.findPlayer(2);
			ps[2] = new Player(2,2);
			ps[2].setX(z.x);
			ps[2].setY(z.y);
			MinMax next = min(x,ps,depth-1);
			// if this move is better then the previously chosen move
			// changes the return move to the current move 
			if (next.s < alpha) {
				alpha = next.s;
				bestMove.setScore(alpha);
				bestMove.setBoard(x);
			}
		}
		return bestMove;
	}
	
	/**
	 * @param b Game board
	 * @param players Array of all game players
	 * @param depth Current minmax depth
	 * @return Minimum of minmax
	 */
	public static MinMax min (Board b, Player[] players, int depth) {
		
		if (depth == 0) {
			if (b.findPlayer(1).x == 0) {
				return new MinMax(b,0);
			}
			double rand = (int)(Math.random() * 2);
			//return new MinMax(b,(((b.stepsToGoal(players[1]))-b.stepsToGoal(players[2]))*0.6) + ((b.stepsToNextRow(players[2]))*0.8)+((Math.pow(b.stepsToNextRow(players[1]),-1))*6)-rand);
			return new MinMax(b,(((b.stepsToGoal(players[1]))-b.stepsToGoal(players[2])));
			//return new MinMax(b,b.stepsToGoal(players[2]));
		}
		List<Board> moves = generateMoves(b,players,2);
		Iterator<Board> it = moves.iterator();
		double alpha = 999;
		MinMax bestMove = new MinMax(b, 0);
		while (it.hasNext()) {
			Board x = it.next();
			Position y = x.findPlayer(1);
			Player[] ps = new Player[3];
			ps[1] = new Player(1,2);
			ps[1].setX(y.x);
			ps[1].setY(y.y);
			Position z = x.findPlayer(2);
			ps[2] = new Player(2,2);
			ps[2].setX(z.x);
			ps[2].setY(z.y);
			MinMax next = max(x,ps,depth-1);
			if (next.s < alpha) {
				alpha = next.s;
				bestMove.setScore(alpha);
				bestMove.setBoard(x);
			}
		}
		//io.printMessage("alpha = " + alpha);
		return bestMove;
	}
	
	/**
	 * @param b Game board
	 * @param players Array of all players
	 * @param playerNum ID of current player
	 * @return Position of made move
	 */
	public static Position makeMove(Board b,Player[] players, int playerNum) {
		io.printMessage("AI is thinking");
		// calls min max to get the best next move
		MinMax next = max(b,players,1);
		// sets the position the ai wants to move too
		Position m = next.b.findPlayer(2);
		Wall newWall = b.findNewWall(next.b);
		//if the ai placed a wall  changes the position to that of the wall
		if (newWall != null) {
			m.x = newWall.x;
			m.y = newWall.y;
			m.d = newWall.d;
		} else {
			m.d = ' ';
		}		
		
		return m;
	}
	/**
	 * @param b Board
	 * @param players Array of all players in the game
	 * @param playerNum ID of player generating moves for
	 * @return List of all possible new boards for the next move
	 */
	public static List<Board> generateMoves(Board b,Player[] players, int playerNum) {
		List <Board> moves = new LinkedList<Board>();
	
		
		// sets the postions of the two players
		Position playerPositions[] = new Position[3];
		playerPositions[1] = b.findPlayer(1);
		playerPositions[2] = b.findPlayer(2);
		Player[] newPlayers = new Player[3];
		newPlayers[1] = new Player(1,2);
		newPlayers[2] = new Player(2,2);
		newPlayers[1].setX(playerPositions[1].x);
		newPlayers[1].setY(playerPositions[1].y);
		newPlayers[1].setWalls(players[1].getNumWalls());
		newPlayers[2].setX(playerPositions[2].x);
		newPlayers[2].setY(playerPositions[2].y);
		newPlayers[2].setWalls(players[2].getNumWalls());
		
		
		
		for (int i = 1; i <=2; i++) {
			newPlayers[playerNum].setX(playerPositions[playerNum].x);
			newPlayers[playerNum].setY(playerPositions[playerNum].y);
			//
			if (b.isLegalMove(players[playerNum],newPlayers[playerNum].getX()-i,newPlayers[playerNum].getY()) == true) {
				newPlayers[playerNum].setX(newPlayers[playerNum].getX()-i);
				Board up = new Board(newPlayers);
				up.copyBoard(b);
				//up.displayBoard();
				moves.add(up);
			}
		}
		for (int i = 1; i <=2; i++) {
			newPlayers[playerNum].setX(playerPositions[playerNum].x);
			newPlayers[playerNum].setY(playerPositions[playerNum].y);
			//move board down
			if (b.isLegalMove(players[playerNum],newPlayers[playerNum].getX()+i,newPlayers[playerNum].getY()) == true) {
				newPlayers[playerNum].setX(newPlayers[playerNum].getX()+i);
				Board down = new Board(newPlayers);
				down.copyBoard(b);
				//down.displayBoard();
				moves.add(down);
			}
		}
		for (int i = 1; i <=2; i++) {
			newPlayers[playerNum].setX(playerPositions[playerNum].x);
			newPlayers[playerNum].setY(playerPositions[playerNum].y);
			//generating left board
			if (b.isLegalMove(players[playerNum], newPlayers[playerNum].getX(), newPlayers[playerNum].getY()-i) == true) {
				newPlayers[playerNum].setY(newPlayers[playerNum].getY()-i);
				Board left = new Board(newPlayers);
				left.copyBoard(b);

				moves.add(left);
			}
		}
		for (int i = 1; i <=2; i++) {
			newPlayers[playerNum].setX(playerPositions[playerNum].x);
			newPlayers[playerNum].setY(playerPositions[playerNum].y);
			//generating right board
			if (b.isLegalMove(players[playerNum], newPlayers[playerNum].getX(), newPlayers[playerNum].getY()+i) == true) {
				newPlayers[playerNum].setY(newPlayers[playerNum].getY()+i);
				Board right = new Board(newPlayers);
				right.copyBoard(b);
				moves.add(right);
			}
		}
		
		for (int i = (newPlayers[1].getX()-2); i <= (newPlayers[1].getX()+2); i ++) {
			for (int j = 1; j < 9; j ++) {
				newPlayers[playerNum].setX(playerPositions[playerNum].x);
				newPlayers[playerNum].setY(playerPositions[playerNum].y);
				Board wboard = new Board(newPlayers);
				wboard.copyBoard(b);
				if (i >=0 && j >= 0 && i <=8 && j <=8) {
					if (wboard.isLegalWall(newPlayers,i,j,'h') == true) {
						wboard.placeWall(newPlayers, playerNum, i, j, 'h');
						moves.add(wboard);
					} else {
					}
				}
			}
		}
		for (int i = 1; i < 9; i ++) {
			for (int j = (newPlayers[1].getY()-2); j < (newPlayers[1].getX()+2); j ++) {
				newPlayers[playerNum].setX(playerPositions[playerNum].x);
				newPlayers[playerNum].setY(playerPositions[playerNum].y);
				Board wboard = new Board(newPlayers);
				wboard.copyBoard(b);
				if (i >=0 && j >= 0 && i <=8 && j <=8) {
					if (wboard.isLegalWall(newPlayers,i,j,'v') == true) {
						wboard.placeWall(newPlayers, playerNum, i, j, 'v');
					
						moves.add(wboard);
					} else {
					}
				}
			}
		}

		for (int i = (newPlayers[2].getX()-2); i <= (newPlayers[2].getX()+2); i ++) {
			for (int j = 1; j < 9; j ++) {
				newPlayers[playerNum].setX(playerPositions[playerNum].x);
				newPlayers[playerNum].setY(playerPositions[playerNum].y);
				Board wboard = new Board(newPlayers);
				wboard.copyBoard(b);
				if (i >=0 && j >= 0 && i <=8 && j <=8) {
					if (wboard.isLegalWall(newPlayers,i,j,'h') == true) {
						wboard.placeWall(newPlayers, playerNum, i, j, 'h');
					
						moves.add(wboard);
					} else {
					}
				}
			}
		}
		for (int i = 1; i < 9; i ++) {
			for (int j = (newPlayers[2].getY()-2); j < (newPlayers[2].getX()+2); j ++) {
				newPlayers[playerNum].setX(playerPositions[playerNum].x);
				newPlayers[playerNum].setY(playerPositions[playerNum].y);
				Board wboard = new Board(newPlayers);
				wboard.copyBoard(b);
				if (i >=0 && j >= 0 && i <=8 && j <=8) {
					if (wboard.isLegalWall(newPlayers,i,j,'v') == true) {
						wboard.placeWall(newPlayers, playerNum, i, j, 'v');
					
						moves.add(wboard);
					} else {
					}
				}
			}
		}
		return moves;
	}
}