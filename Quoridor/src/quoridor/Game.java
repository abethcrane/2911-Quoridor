package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game{
	
	public static final int num_moves = 100;
	public static final int num_players = 2;

	public static void main(String[] args) throws IOException {		
		int turnNumber = 0;
		int totalTurnNumber = 0;
		boolean gameOver = false;	
		String[] moves = new String[num_moves];
		String[] allMoves = new String[num_moves];
		
		Player[] players = new Player[num_players+1];
		for (int i = 1; i <= num_players; i++) {
			players[i] = new Player(i);
			moves[i-1] = intToString(players[i].getY()).concat(((Integer)players[i].getX()).toString());
			allMoves[i-1] = moves[i-1];
			System.out.println(moves[i-1]);
			turnNumber += 1;
			totalTurnNumber += 1;
		}

		Board b = new Board(players);
		b.displayBoard();
		int playerTurn = 0;

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
		while (str != null && gameOver == false) {
			playerTurn = (turnNumber % num_players) + 1;
			
			System.out.print("> enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+players[playerTurn].getX()+ "] "+players[playerTurn].getNumWalls()+" walls left):");
			str = in.readLine();
			System.out.println("Str = " + str);
			// If they're undoing we call the undo function, we then proceed play as normal
			if (str.equals("u")) {
				if (turnNumber < 2+num_players) {
					System.out.println("Error: Not enough moves to undo");
				} else { 
	    			undoMove(b, moves, players, turnNumber);
	    			allMoves[totalTurnNumber] = str;
	    			totalTurnNumber++;
	    			turnNumber -= 2;
				}
				System.out.print("> enter move for player " + playerTurn + " (current position is ["+intToString(players[playerTurn].getY())+","+players[playerTurn].getX()+ "] "+players[playerTurn].getNumWalls()+" walls left):");
    			str = in.readLine();
    		}
    		if (processMove(str,players[playerTurn],b) == true) {
    			moves[turnNumber] = str;
    			allMoves[totalTurnNumber] = str;
    			turnNumber++;
    			totalTurnNumber++;
    			b.displayBoard();
    		}

    		Player check = b.checkWinner(players);
    		if (check != null) {
    			System.out.print("Player " + check.getPlayer() + " wins");
    			gameOver = true;
    		}
    		// if the move was valid increment turn number
    		printMoves(moves, turnNumber);

		}
	}

	//if this returns false a invalid move was given, don't increment turnNumber
	public static boolean processMove(String s, Player p, Board b) {
		char[] a = s.toCharArray();
		boolean r = false;
		if (convertCharToInt(a[0])<Board.numRows && Character.getNumericValue(a[1])<b.numCols) {
			if (a.length == 3) {
				if (p.getNumWalls()>0) {
					r = b.placeWall(p, Character.getNumericValue(a[1]), convertCharToInt(a[0]), a[2]);
				} else {
					System.out.println("out of walls");
					r=false;
				}
			} else {
				r = b.movePlayer(p,Character.getNumericValue(a[1]),convertCharToInt(a[0]));
			}
		} else {
			System.out.println("Invalid Move: square out of range");
		}
		//System.out.print(27+"[2J");

		return r;
	}
	
	public static void undoMove (Board b, String[] moves, Player[] players, int turnNumber) {
		// move player's position back to where they were two moves ago
		// do the same for the other player as well
		// can only undo if you have more than 1 move
		//undo walls as well!!
		System.out.println("reverting player " + ((turnNumber -1)  % num_players + 1) + " to " + moves[turnNumber-3]);
		processMove(moves[turnNumber-3], players[((turnNumber -1) % num_players + 1)], b);
		System.out.println("reverting player " + ((turnNumber -2)  % num_players + 1) + " to " + moves[turnNumber-4]);
		processMove(moves[turnNumber-4], players[((turnNumber -2)  % num_players)+1], b);
		b.displayBoard();
		//return b;
		
	}

	//used to convert a-i to its
	private static int convertCharToInt(char s) {
		return ((int)s-97);
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
