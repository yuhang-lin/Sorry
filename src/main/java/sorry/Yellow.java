package sorry;

import javafx.scene.paint.Color;

/**
 * Yellow color for the piece.
 * <p>
 * First version was done by Austin Batistoni, then Yuhang Lin refactored the
 * code by removing duplicate functions already implemented in the super class.
 * 
 * @author Austin Batistoni, Yuhang Lin
 */
public class Yellow extends PieceColor {

	public Yellow() {
		startCoords = new int[][] { { 13, 4 }, { 14, 4 }, { 13, 5 }, { 14, 5 } };
		safeCoords = new int[][] { { 14, 2 }, { 13, 2 }, { 12, 2 }, { 11, 2 }, { 10, 2 } };
		homeCoords = new int[][] { { 8, 2 }, { 9, 2 }, { 8, 3 }, { 9, 3 } };
		firstSpot = new int[][] { { 15, 4 } };
		lastSpot = new int[][] { { 15, 2 } };
		color = Color.rgb(153, 134, 0);
	}

	@Override
	public String toString() {
		return "Yellow";
	}
}
