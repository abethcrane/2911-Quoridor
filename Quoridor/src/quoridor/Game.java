package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game{

	public static final int num_moves = 100;
	public static  int num_players = 4;

	public static void main(String[] args) throws IOException {

		String keepPlaying = "y";
		while (keepPlaying.equals("y"))	{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String str = "";
			System.out.print("How many players would you like? 2-4: ");
			str = in.readLine();

			num_players = getNumPlayers(str);
			if (num_players == -1) {
				System.out.println("There should be between 2 and 4 players. Selecting 2");
				num_players = 2;
			}
			boolean ai = false;
			if (num_players ==2) {
				System.out.print("would you like to play against an AI? [y/n]");
				str = in.readLine();
				if (str.equals("y")) {
					ai = true;
				}
			}
			System.out.println(num_players);
			int turnNumber = 0;
			int totalTurnNumber = 0;
			boolean gameOver = false;	
			String[] moves = new String[num_moves];
			String[] allMoves = new String[num_moves];

			Player[] players = new Player[num_players+1];
			for (int i = 1; i <= num_players; i++) {
				players[i] = new Player(i,num_players);
				moves[i-1] = intToString(players[i].getY()).concat(((Integer)(players[i].getX()+1)).toString());
				allMoves[i-1] = moves[i-1];
				turnNumber += 1;
				totalTurnNumber += 1;
			}


			Board b = new Board(players);
			b.displayBoard();
			int playerTurn = 0;
		
			while (str != null && gameOver == false) {
				playerTurn = (turnNumber % num_players) + 1;
				if (ai == true && playerTurn == 2) {
					Position aimove = Ai.makeMove(b,players,playerTurn);
					if (aimove.d == ' ') { 
						System.out.println("Ai is moving to "+(Character.toString(convertIntToChar(aimove.y)))+","+(aimove.x+1));
						b.movePlayer(players[playerTurn], aimove.x, aimove.y, false);	
					} else {
						System.out.println("Ai placing a wall "+(Character.toString(convertIntToChar(aimove.y))+(aimove.x)+aimove.d));
						b.placeWall(players, playerTurn, aimove.x, aimove.y, aimove.d);
					}
				
					String s = Character.toString(convertIntToChar(aimove.y));
					s += Integer.toString(aimove.x+1);
					if (aimove.d != ' ') {
						s+=Character.toString(aimove.d);
					}
					moves[turnNumber] = s;
					allMoves[totalTurnNumber] = s;
					turnNumber++;
					totalTurnNumber ++;
					Player check = b.checkWinner(players);
					if (check != null) {
						System.out.println("Player " + check.getPlayer() + " wins");
						gameOver = true;
					}
					// if the move was valid increment turn number
					printMoves(moves, turnNumber);
					b.displayBoard();
				} else {
					System.out.print("> enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+(players[playerTurn].getX()+1)+ "] "+players[playerTurn].getNumWalls()+" walls left):");
					str = in.readLine();
					//System.out.println("Str = " + str);
					// If they're undoing we call the undo function, we then proceed play as normal
					if (str.equals("u")) {
						if (turnNumber < 2+num_players) {
							System.out.println("Error: Not enough moves to undo");
						} else { 
							undoMove(b, moves, players, turnNumber,ai);
							allMoves[totalTurnNumber] = str;
							totalTurnNumber++;
							if (ai == true) {
								turnNumber -=2;
							} else {
								turnNumber -=1;
							}
							playerTurn = (turnNumber % num_players) + 1;
						
						}
						System.out.print("> enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+(players[playerTurn].getX()+1)+ "] "+players[playerTurn].getNumWalls()+" walls left):");
						str = in.readLine();
					} else if (
						str.equals("end")) {
						gameOver = true;
					}
					if (gameOver != true) {
						while (!isValidInput(str)) {
							System.out.println("Valid moves are of the form yx[vh]*");
							System.out.print("> enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+(players[playerTurn].getX()+1)+ "] "+players[playerTurn].getNumWalls()+" walls left):");
							str = in.readLine();
						}
						if (processMove(str,players,playerTurn,b, false) == true) {
							moves[turnNumber] = str;
							allMoves[totalTurnNumber] = str;
							turnNumber++;
							totalTurnNumber++;
							b.displayBoard();
						}
						Player check = b.checkWinner(players);
						if (check != null) {
							System.out.println("Player " + check.getPlayer() + " wins");
							gameOver = true;
						}
						// if the move was valid increment turn number
						printMoves(moves, turnNumber);
					}

				}
			}
			System.out.println("Would you like to step through a replay of the game?(y/n)");
			String replay = in.readLine();
			if (replay.equals("y") || replay.equals("yes")) {
				System.out.println("----------GAME REPLAY---------");
				Player[] replayPlayers = new Player[num_players+1];
				turnNumber = 0;
				totalTurnNumber = 0;
				for (int i = 1; i <= num_players; i++) {
					replayPlayers[i] = new Player(i,num_players);
					turnNumber += 1;
					totalTurnNumber += 1;
				}
				Board replayBoard = new Board(replayPlayers);
				int count = 2;
				while (allMoves[count] != null) {
					playerTurn = (turnNumber % num_players) + 1;
					String move = allMoves[count];
					System.out.println("player " + playerTurn + "move: " + move);
					processMove(move, replayPlayers, playerTurn, replayBoard, false);
					replayBoard.displayBoard();
					turnNumber ++;
					count ++;
					System.out.println("press any key to view the next move");
					in.readLine();
				}
			}
			System.out.println("Would you like to play again(y/n)");
			keepPlaying = in.readLine();
		}
	}
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
	


	//if this returns false a invalid move was given, don't increment turnNumber
	public static boolean processMove(String s, Player[] players,int player, Board b, boolean undo) {
		//System.out.println("processing moves :" + s);
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
					System.out.println("out of walls");
					r=false;
				}
			} else {
				r = b.movePlayer(players[player],x,y,false);
			}
		} else {
			//System.out.println("Invalid Move: square out of range");
			r = false;
		}
		//System.out.println("player " + players[player].getPlayer() + "is " + b.stepsToNextRow(players[player]));
		//System.out.print(27+"[2J");

		return r;
	}

	public static void undoMove (Board b, String[] moves, Player[] players, int turnNumber, boolean ai) {
		// move player's position back to where they were two moves ago
		// do the same for the other player as well
		// can only undo if you have more than 1 move
		
	
		
		for (int i = 1; i < players.length; i++) {
			// if it's a wall remove it
			if (moves[turnNumber-i].length() == 3) {
				System.out.println("removing wall of player " + (((turnNumber -i)  % num_players) + 1) + " to " + moves[turnNumber-i]);
				processMove(moves[turnNumber-i], players,((turnNumber-i) % num_players) + 1, b, true);
			// else revert back to the previous position
			} else {
				System.out.println("reverting player " + ((turnNumber -i)  % num_players + 1) + " to " + moves[turnNumber-i-num_players]);
				processMove(moves[turnNumber-i-num_players], players,((turnNumber-i) % num_players) +1, b, true);
			}
		}

		
		b.displayBoard();
	}

	//used to convert a-i to its
	private static int convertCharToInt(char s) {
		return ((int)s-97);
	}
	private static char convertIntToChar(int s) {
		return (char) ((char)s+97);
	}

	private static String intToString(int i) {
		i = i + 97;
		char a[] = Character.toChars(i);
		return Character.toString(a[0]);
	}

	public static void printMoves(String[] moves, int turnNumber) {
		for (int i = 0; i < turnNumber; i++) {
			System.out.print(moves[i] + " ");
		}
		System.out.println();
	}
}
