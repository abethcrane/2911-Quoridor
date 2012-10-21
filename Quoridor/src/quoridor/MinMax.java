package quoridor;

public class MinMax {

	Board b;
	double s;
	
	/**
	 * @param board The board to be operated on
	 * @param score The score to use
	 */
	public MinMax(Board board, double score) {
		b = board;
		s = score;
	}
	
	/**
	 * @param score The score to use
	 */
	public void setScore(double score) {
		s = score;
	}
	
	/**
	 * @param board The board to operate on
	 */
	public void setBoard (Board board) {
		b = board;
	}
}
