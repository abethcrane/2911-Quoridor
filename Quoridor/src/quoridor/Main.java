package quoridor;

public class Main {
	public static void main(String[] args) {
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
	}
	
}
