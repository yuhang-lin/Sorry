package sorry;

import javafx.scene.paint.Color;

/**
 * Blue color for the piece.
 * 
 * First version was done by Austin Batistoni, then Yuhang Lin refactored the
 * code by removing duplicate functions already implemented in the super class.
 * 
 * @author Austin Batistoni, Yuhang Lin
 */
public class Blue extends PieceColor {

	public Blue() {
		startCoords = new int[][] { { 4, 1 }, { 4, 2 }, { 5, 1 }, { 5, 2 } };
		safeCoords = new int[][] { { 2, 1 }, { 2, 2 }, { 2, 3 }, { 2, 4 }, { 2, 5 } };
		homeCoords = new int[][] { { 2, 6 }, { 2, 7 }, { 3, 6 }, { 3, 7 } };
		firstSpot = new int[][] { { 4, 0 } };
		lastSpot = new int[][] { { 2, 0 } };
		color = Color.DARKBLUE;
	}

	@Override
	public String toString() {
		return "Blue";
	}

}
