package sorry;

import java.util.ArrayList;

class Choice implements Comparable<Choice>{
	private Piece piece;
	private ArrayList<Integer> move;
	private int score;
	
	public Choice(Piece piece, ArrayList<Integer> move, int score) {
		this.piece = piece;
		this.move = move;
		this.score = score;
	}

	@Override
	public int compareTo(Choice o) {
		if (this.score == o.score) {
			return 0;
		} else if (this.score > o.score) {
			return 1;
		}
		return -1;
	}

	public Piece getPiece() {
		return piece;
	}

	public ArrayList<Integer> getMove() {
		return move;
	}

	public int getScore() {
		return score;
	}
	
}
