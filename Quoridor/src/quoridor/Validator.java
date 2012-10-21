package quoridor;

import java.util.List;

public class Validator {

	// TODO complete this class using your project code
	// you must implement the no-arg constructor and the check method
	
	// you may add extra fields and methods to this class
	// but the ProvidedTests code only calls the specified methods
	
	public static final int num_moves = 100;
	public static  int num_players = 2;
	int turnNumber;
	int totalTurnNumber;
	boolean gameOver;
	String[] moves;
	String[] allMoves;
	Player[] players;
	Board b;
	int playerTurn;
	Game game;

	public Validator() {
		turnNumber = 0;
		totalTurnNumber = 0;
		gameOver = false;
		moves = new String[num_moves];
		allMoves = new String[num_moves];
		players = new Player[num_players+1];
		for (int i = 1; i <= num_players; i++) {
			players[i] = new Player(i,num_players);
			moves[i-1] = intToString(players[i].getY()).concat(((Integer)players[i].getX()).toString());
			allMoves[i-1] = moves[i-1];
			turnNumber += 1;
			totalTurnNumber += 1;
		}
		game = new Game();
		b = new Board(players);
	}

	/**
	 * Check the validity of a given list of moves.
	 * Each move is represented by a string.
	 * The list is valid if and only if each move in the list is valid,
	 * after applying the preceding moves in the list, assuming they are valid,
	 * starting from the initial position of the game.
	 * When the game has been won, no further moves are valid.
	 * @param moves a list of successive moves
	 * @return validity of the list of moves
	 */
	public boolean check(List<String> moves) {
		boolean r = true;
		for (int i = 0; i < moves.size(); i ++) {
			System.out.println( (i % 2) + 1);
			//System.out.println("Player " + 1+ " is at location ["+ (players[1].getX()+1)+","+players[1].getY()+"]");
			//System.out.println("Player " + 2 + " is at location ["+ (players[2].getX()+1)+","+players[2].getY()+"]");
			if (Game.processMove(moves.get(i), players, (turnNumber % num_players) + 1, b, false)== false) {
				System.out.println(moves.get(i)+ " is not a valid move");
				r = false;
			} else {
				System.out.println(moves.get(i)+ " is a valid move");
			}
			turnNumber++;	
		}
		return r;
	}
	
	private static String intToString(int i) {
		i = i + 97;
		char a[] = Character.toChars(i);
		return Character.toString(a[0]);
	}

}
