package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game{
	public static void main(String[] args) throws IOException {
		
		Player one = new Player(1);
		Player two = new Player(2);
		Board b = new Board(one,two);
		
		int turnNumber = 0;
		boolean gameOver = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
	    while (str != null && gameOver == false) {
	    	if (turnNumber%2==0) {
	    		System.out.print("> enter move for player one(current position is ["+intToString(one.getY())+","+one.getX()+ "]):");
	    		str = in.readLine();
		        if (processMove(str,one,b)==true) {;
	        		turnNumber ++;
		        }
	    	} else {
	    		System.out.print("> enter move for player two(current position is ["+intToString(two.getY())+","+two.getX()+ "]):");
	    		str = in.readLine();
		        if (processMove(str,two,b)==true) {;
	        		turnNumber ++;
		        }
	    	}
	    	Player check = b.checkWinner(one, two);
	        if (b.checkWinner(one, two) != null) {
	        	System.out.print("Player " + check.getPlayer() + " wins");
	        	gameOver = true;
	        }
	        // if the move was valid increment turn number

	    }
	}
	
	//if this returns false a invalid move was given, dont increment turnNumber
	public static boolean processMove(String s,Player p,Board b) {
		char[] a = s.toCharArray();
		boolean r = b.movePlayer(p,Character.getNumericValue(a[1]),convertCharToInt(a[0]));
		b.displayBoard();
		
		return r;
	}
	//used to convert a-i to ints
	private static int convertCharToInt(char s) {
		return ((int)s-97);
	}
	
	private static String intToString(int i) {
		i = i + 97;
		char a[] = Character.toChars(i);
		return Character.toString(a[0]);
	}
}
