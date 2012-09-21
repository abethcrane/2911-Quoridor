package quoridor;

public interface BoardInterface {

	//dont know if we need this
	Board createBoard();
	
	//given the board and the x and y coordinates of a move checks if it 
	//is a legal move
	int isLegalMove(Board b,Player P,int x,int y);
	
	//places a wall ?assume isLegalMove has been called
	void placeWall(Board b, Player p, int x, int y);
	
	//moves the player ?assume isLegal Move has been called
	void movePlayer(Board b, Player p, int x, int y);

	//displays the board
	void displayBoard(Board b);
}
