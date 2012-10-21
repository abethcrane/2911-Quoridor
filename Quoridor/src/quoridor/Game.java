package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game{

	public static final int num_moves = 100;
	public static int num_players = 4;
	public static final int ai_player_num = 2;

	static IO io = IOFactory.getIO();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		// Default keep playing as yes, so that we play the initial game
		String keepPlaying = "y";
		while (keepPlaying.equals("y"))	{


			io.printMessageBlank("How many players would you like? 2-4: ");
			String str = io.readInput();

			num_players = getNumPlayers(str);
			if (num_players == -1) {
				io.printMessage("There should be between 2 and 4 players. Selecting 2");
				num_players = 2;
			}

			// Set AI to default, unless otherwise specified
			boolean ai = false;
			if (num_players == 2) {
				io.printMessageBlank("would you like to play against an AI? [y/n]");
				str = io.readInput();
				if (str.equals("y")) {
					ai = true;
				}
			}

			// Set up some game storage information, so that we can do undo and run replay
			int turnNumber = 0;
			int totalTurnNumber = 0;
			boolean gameOver = false;	
			String[] moves = new String[num_moves];
			String[] allMoves = new String[num_moves];

			// Create all the players, and store their starting positions in the move strings
			Player[] players = new Player[num_players+1];
			for (int i = 1; i <= num_players; i++) {
				players[i] = new Player(i,num_players);
				moves[i-1] = intToString(players[i].getY()).concat(((Integer)(players[i].getX()+1)).toString());
				allMoves[i-1] = moves[i-1];
				turnNumber++;
				totalTurnNumber++;
			}


			// Create the board and display the initial blank state
			Board b = new Board(players);
			b.displayBoard();
			int playerTurn = 0;


			// Whilst the game is still in play, we read turns
			while (str != null && gameOver == false) {
				playerTurn = (turnNumber % num_players) + 1;
				// If it's an AI's turn
				if (ai == true && playerTurn == ai_player_num) {
					Position aimove = Ai.makeMove(b,players,playerTurn);

					// If the AI doesn't move a wall, print out its move
					if (aimove.d == ' ') { 
						io.printMessage("AI is moving to "+(Character.toString(convertIntToChar(aimove.y)))+","+(aimove.x+1));
						b.movePlayer(players[playerTurn], aimove.x, aimove.y, false);
						// If it does place a wall, print that out
					} else {
						io.printMessage("AI placing a wall "+(Character.toString(convertIntToChar(aimove.y))+(aimove.x)+aimove.d));
						b.placeWall(players, playerTurn, aimove.x, aimove.y, aimove.d);
					}

					// Calculate the move string of the AI (with the y converted to a character)
					// This is used for storing in the move string
					String s = Character.toString(convertIntToChar(aimove.y));
					s += Integer.toString(aimove.x+1);
					if (aimove.d != ' ') {
						s+=Character.toString(aimove.d);
					}
					moves[turnNumber] = s;
					allMoves[totalTurnNumber] = s;
					turnNumber++;
					totalTurnNumber++;
					// If it's a human's turn
				} else {
					boolean played = false;
					while (!played) {
						io.printMessageBlank("> Enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+(players[playerTurn].getX()+1)+ "] "+players[playerTurn].getNumWalls()+" walls left):");
						str = io.readInput();
						// If they're undoing we call the undo function
						// Then proceed play as normal
						// It has to be 2*num_players, because we have to be able to set it back to your first move
						// And the move count includes starting positions
						if (str.equals("u")) {
							if (turnNumber < num_players*2) {
								io.printMessage("Error: Not enough moves to undo");
							} else { 
								undoMove(b, moves, players, turnNumber);
								allMoves[totalTurnNumber] = str;
								totalTurnNumber++;
								turnNumber -= num_players;
								playerTurn = (turnNumber % num_players) + 1;

							}
							// If the user has decided to end the game, quit now
						} else if (str.equals("end")) {
							gameOver = true;
						} else {
							// Whilst we don't have valid input, inform the user of what is valid
							if (!isValidInput(str)) {
								io.printMessage("Valid moves are of the form yx[vh]*");
								// If we validly processed the move then we implement that
							} else {
								if (processMove(str,players,playerTurn,b, false) == true) {
									played = true;
									moves[turnNumber] = str;
									allMoves[totalTurnNumber] = str;
									turnNumber++;
									totalTurnNumber++;
									//If it's an illegal move, we print an error message to the user	
								} else {

									if (str.length() == 3) {
										io.printMessage("Illegal wall placement;\n" +
												"Legal wall placements are within bounds\n" +
												"They don't intersect other walls\n" +
												"And don't cut off player's paths to their goal");
									} else {
										io.printMessage("Invalid move;\n" +
												"Valid moves are to an unoccupied cell up/down/left/right\n" +
												"Or jumping over a player (diagonally in the case of a wall)");
									}


								}
							}
						}
					}
				}
				// Check if a player has won; if so the game ends and we print that
				if (gameOver != true) {
					Player check = b.checkWinner(players);
					if (check != null) {
						io.printMessage("Player " + check.getPlayer() + " wins");
						gameOver = true;
					}
					b.displayBoard();
				}

			}
			// Once the game has ended, offer to replay it for the user
			io.printMessage("Would you like to step through a replay of the game?(y/n)");
			String replay = io.readInput();
			if (replay.equals("y") || replay.equals("yes")) {
				replayGame(allMoves);
			}
			io.printMessage("Would you like to play again(y/n)");
			keepPlaying = io.readInput();
		}
	}

	public static void replayGame(String[] allMoves) throws IOException {
		io.printMessage("----------GAME REPLAY---------");
		Player[] replayPlayers = new Player[num_players+1];
		int turnNumber = 0;
		int totalTurnNumber = 0;
		for (int i = 1; i <= num_players; i++) {
			replayPlayers[i] = new Player(i,num_players);
			turnNumber += 1;
			totalTurnNumber += 1;
		}
		Board replayBoard = new Board(replayPlayers);
		int count = 2;
		while (allMoves[count] != null) {
			int playerTurn = (turnNumber % num_players) + 1;
			String move = allMoves[count];
			io.printMessage("player " + playerTurn + "move: " + move);
			processMove(move, replayPlayers, playerTurn, replayBoard, false);
			replayBoard.displayBoard();
			turnNumber ++;
			count ++;
			io.printMessage("press any key to view the next move");
			String temp = io.readInput();
		}
	}

	/**
	 * @param s String to parse from input
	 * @return Number of players passed in
	 */
	public static int getNumPlayers(String s) {
		int retVal = -1;
		if (s.length() == 1) {
			retVal = (int)(s.toCharArray()[0]-48);
			if (retVal < 2 || retVal > 4) {
				retVal = -1;
			}
		}
		return retVal;
	}

	/**
	 * @param s Input string
	 * @return Whether or not the input is of valid form
	 */
	public static boolean isValidInput(String s) {
		boolean retVal = true;
		char[] a = s.toCharArray();
		if (s.length() < 2 || s.length() > 3) {
			retVal = false;
		} else {
			if (convertCharToInt(a[0]) < 0 || convertCharToInt(a[0]) > 8) {
				retVal = false;
			} else if (Character.getNumericValue(a[1]) <= 0 || Character.getNumericValue(a[1]) >10){
				retVal = false;
			} else if (s.length() == 3) {
				if (a[2] != 'h' && a[2] != 'v') {
					retVal = false;
				}
			}
		}
		return retVal;
	}



	/**
	 * @param s Move string to process
	 * @param players Array of all players
	 * @param player Number of current player trying to move
	 * @param b Game board
	 * @param undo Whether or not this move is an undo, or instead a new play
	 * @return Whether a valid move was given
	 */
	public static boolean processMove(String s, Player[] players, int player, Board b, boolean undo) {

		char[] a = s.toCharArray();
		boolean r = true;
		int y = convertCharToInt(a[0]);
		int x = Character.getNumericValue(a[1])-1;
		if (x == -1) { x = 0;}
		if (convertCharToInt(a[0])<Board.numRows && Character.getNumericValue(a[1])-1<Board.numCols) {
			if (a.length == 3) {
				if (players[player].getNumWalls()>0) {
					if (undo) {
						r = b.removeWall(players[player], x+1, y, a[2]);
					} else {
						r = b.placeWall(players,player, x+1, y, a[2]);
					}
				} else {
					io.printMessage("Out of walls");
					r=false;
				}
			} else {
				r = b.movePlayer(players[player],x,y,false);
			}
		} else {
			r = false;
		}
		return r;
	}

	/**
	 * Move player's position back to where they were two moves ago
	 * Do the same for the other player as well
	 * Can only undo if you have more than 1 move
	 * @param b Game board
	 * @param moves String array of all moves
	 * @param players Array of all players
	 * @param turnNumber Current turn number (to reference in moves string)
	 */
	public static void undoMove (Board b, String[] moves, Player[] players, int turnNumber) {

		for (int i = 1; i < players.length; i++) {
			// If it's a wall remove it
			if (moves[turnNumber-i].length() == 3) {
				io.printMessage("Removing wall of player " + (((turnNumber -i)  % num_players) + 1) + " to " + moves[turnNumber-i]);
				processMove(moves[turnNumber-i], players,((turnNumber-i) % num_players) + 1, b, true);
				// Else revert back to the previous position
			} else {
				io.printMessage("Reverting player " + ((turnNumber -i)  % num_players + 1) + " to " + moves[turnNumber-i-num_players]);
				processMove(moves[turnNumber-i-num_players], players,((turnNumber-i) % num_players) +1, b, true);
			}
		}


		b.displayBoard();
	}

	/**
	 * @param s Char of a move
	 * @return The corresponding cell reference
	 */
	private static int convertCharToInt(char s) {
		return ((int)s-97);
	}
	/**
	 * @param s Int of a move
	 * @return Corresponding alphabetic cell reference
	 */
	private static char convertIntToChar(int s) {
		return (char) ((char)s+97);
	}

	/**
	 * @param i Int of a move
	 * @return Corresponding alphabetic cell reference, as a string.
	 */
	private static String intToString(int i) {
		i = i + 97;
		char a[] = Character.toChars(i);
		return Character.toString(a[0]);
	}

	/**
	 * @param moves String array of all moves
	 * @param turnNumber Current turn number
	 */
	public static void printMoves(String[] moves, int turnNumber) {
		for (int i = 0; i < turnNumber; i++) {
			io.printMessageBlank(moves[i] + " ");
		}
		io.printMessage("");
	}
}
