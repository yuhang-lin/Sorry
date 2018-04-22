package sorry;

import java.util.ArrayList;

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

	public void Move(int squareNum) {
		movement = squareNum;
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
	private void calculateScore() {
		for (Piece piece : getPieces()) {
			for (ArrayList<Integer> move : piece.getPossibleMoves()) {
				// Get the entry to the safe space
				ArrayList<Integer> safe = piece.getColor().getSafeCoords().get(0);
				int safeIndex = Board.getPathIndex(safe);
				int targetIndex = Board.getPathIndex(move);
				int diff = Math.abs(safeIndex - targetIndex);
			}
		}
	}
}
