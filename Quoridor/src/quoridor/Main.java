package quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) throws IOException {
		
		Player one = new Player(1);
		Player two = new Player(2);
		Board b = new Board(one,two);
		
		int turnNumber = 0;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String str = "";
	    while (str != null) {
	    	if (turnNumber%2==0) {
	    		System.out.print("> enter move for player one:");	
	    	} else {
	    		System.out.print("> enter move for player two:");
	    	}
	        str = in.readLine();
	        // if the move was valid increment turn number
	        if (processMove(str,one,two,b)==true) {;
	        	turnNumber ++;
	        }
	    }
		
		/*
		Player one = new Player(1);
		Player two = new Player(2);
		Board b = new Board(one,two);
		b.displayBoard();
		b.movePlayer(one,1,4);
		b.displayBoard();
		b.movePlayer(two,7,4);
		b.displayBoard();
		b.movePlayer(two,6,4);
		b.movePlayer(one,2,4);
		b.movePlayer(one,3,4);
		b.movePlayer(one,4,4);
		b.movePlayer(one,5,4);
		b.movePlayer(one,7,4);
		b.displayBoard();
		b.movePlayer(one,6,5);
		b.displayBoard();
		*/
	}
	
	//if this returns false a invalid move was given, dont increment turnNumber
	public static boolean processMove(String s,Player one, Player two,Board b) {
		String a[] = s.split(",");
		boolean r = b.movePlayer(one,Integer.parseInt(a[0]),Integer.parseInt(a[1]));
		b.displayBoard();
		return r;
	}
	
}
