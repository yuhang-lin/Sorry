package sorry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Chris Bratkovics, Yuhang Lin
 */
public class Computer extends Player {

	private PieceColor color;
	private int movement;
	private boolean occupied;
	private NiceLevel niceLevel;
	private SmartLevel smartLevel;
	private ArrayList<Choice> choiceList = new ArrayList<>();
	
	class Choice implements Comparable<Choice>{
		Piece piece;
		ArrayList<Integer> move;
		int score;
		
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
		
	}

	public Computer(PieceColor c, NiceLevel n, SmartLevel s) {
		super(c);
		this.niceLevel = n;
		this.smartLevel = s;
	}

	public Computer(PieceColor c) {
		super(c);
		this.niceLevel = NiceLevel.NICE;
		this.smartLevel = SmartLevel.SMART;
	}

	public enum NiceLevel {
		NICE, MEAN;
	}

	public enum SmartLevel {
		SMART, DUMB;
	}

	public PieceColor getColor() {
		return color;
	}

	public void move(ArrayList<Piece> piecesOnBoard) {
		calculateScore(piecesOnBoard);
		int numChoice = choiceList.size();
		if (numChoice == 0) {
			return;
		}
		Choice choice = choiceList.get(numChoice - 1);
		if (this.smartLevel == SmartLevel.DUMB) {
			int randomNum = ThreadLocalRandom.current().nextInt(0, numChoice);
			choice = choiceList.get(randomNum);
		}
		Piece piece = choice.piece;
		ArrayList<ArrayList<Integer>> nextLocation = new ArrayList<>();
		nextLocation.add(choice.move);
		piece.setLocation(nextLocation);
	}

	public NiceLevel getNiceLevel() {
		return niceLevel;
	}

	public SmartLevel getSmartLevel() {
		return smartLevel;
	}

	public void setNiceLevel(NiceLevel niceLevel) {
		this.niceLevel = niceLevel;
	}

	public void setSmartLevel(SmartLevel smartLevel) {
		this.smartLevel = smartLevel;
	}

	/**
	 * Calculate scores for all the possible moves. The closer to get home, the
	 * higher the score is.
	 */
	private void calculateScore(ArrayList<Piece> piecesOnBoard) {
		HashMap<Integer, Piece> boardMap = new HashMap<>();
		for (Piece piece : piecesOnBoard) {
			boardMap.put(Board.getPathIndex(piece.getLocation().get(0)), piece);
		}
		for (Piece piece : getPieces()) {
			for (ArrayList<Integer> move : piece.getPossibleMoves()) {
				// Get the entry to the safe space
				ArrayList<Integer> safe = piece.getColor().getSafeCoords().get(0);
				int safeIndex = Board.getPathIndex(safe);
				int targetIndex = Board.getPathIndex(move);
				int score = Math.abs(safeIndex - targetIndex);
				if (boardMap.containsKey(targetIndex)) {
					if (this.niceLevel == NiceLevel.MEAN) {
						score += 10;
					} else if (this.niceLevel == NiceLevel.NICE) {
						score -= 10;
					}
				}
				choiceList.add(new Choice(piece, move, score));
			}
		}
		Collections.sort(choiceList);
	}
}
