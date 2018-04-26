package sorry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Computer opponent of Sorry game.
 * <p>
 * Basic initial code was created by Chirs Bratkovics, then Yuhang Lin completed
 * all the rest of the functions, including getting scores for each movement and
 * getting correct choice.
 * 
 * @author Yuhang Lin, Chris Bratkovics
 */
public class Computer extends Player {

	private PieceColor color;
	private NiceLevel niceLevel;
	private SmartLevel smartLevel;
	private ArrayList<Choice> choiceList = new ArrayList<>();
	private HashMap<Integer, Piece> boardMap = new HashMap<>();

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

	/**
	 * Choose one available from the list of Choice then use than choice to move the
	 * piece.
	 * 
	 * @param piecesOnBoard
	 *            pieces by other players
	 */
	public void move(ArrayList<Piece> piecesOnBoard) {
		Choice choice = getSelectedChoice(piecesOnBoard);
		Piece piece = choice.getPiece();
		ArrayList<ArrayList<Integer>> nextLocation = new ArrayList<>();
		nextLocation.add(choice.getMove());
		int targetIndex = Board.getPathIndex(choice.getMove());
		int firstSpotIndex = Board.getPathIndex(piece.getColor().getFirstSpot().get(0));
		if (boardMap.containsKey(targetIndex)) {
			Piece bumped = boardMap.get(targetIndex);
			bumped.setOutOfPlay();
			bumped.getPlayer().addStartPieces();
		}
		if (firstSpotIndex == targetIndex) {
			piece.getPlayer().removeStartPieces();
		}
		if (!piece.getIsInPlay() && !piece.isPieceSafe()) {
			piece.setInPlay();
		}
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
		boardMap.clear();
		for (Piece piece : piecesOnBoard) {
			boardMap.put(Board.getPathIndex(piece.getLocation().get(0)), piece);
		}
		for (Piece piece : getPieces()) {
			if (piece == null || piece.getPossibleMoves() == null) {
				continue;
			}
			for (ArrayList<Integer> move : piece.getPossibleMoves()) {
				if (move.isEmpty()) {
					continue;
				}
				// Get the entry to the safe space
				ArrayList<Integer> safe = piece.getColor().getLastSpot().get(0);
				int safeIndex = Board.getPathIndex(safe);
				int targetIndex = Board.getPathIndex(move);
				int score = Math.abs(safeIndex - targetIndex);
				if (boardMap.containsKey(targetIndex)) {
					if (this.niceLevel == NiceLevel.MEAN) {
						score += 5;
					} else if (this.niceLevel == NiceLevel.NICE) {
						score -= 5;
					}
				}
				choiceList.add(new Choice(piece, move, score));
			}
		}
		Collections.sort(choiceList);
	}

	/**
	 * Get selected choice of next move.
	 * 
	 * @param piecesOnBoard
	 *            an ArrayList of all the pieces on the board of other players
	 * @return the selected choice
	 */
	public Choice getSelectedChoice(ArrayList<Piece> piecesOnBoard) {
		calculateScore(piecesOnBoard);
		int numChoice = choiceList.size();
		if (numChoice == 0) {
			return null;
		}
		Choice choice = choiceList.get(numChoice - 1);
		if (this.smartLevel == SmartLevel.DUMB) {
			int randomNum = ThreadLocalRandom.current().nextInt(0, numChoice);
			choice = choiceList.get(randomNum);
		}
		return choice;
	}
}
