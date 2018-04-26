package sorry;

import javafx.scene.paint.Color;

/**
 * Red color for the piece.
 * 
 * First version was done by Austin Batistoni, then Yuhang Lin refactored the
 * code by removing duplicate functions already implemented in the super class.
 * 
 * @author Austin Batistoni, Yuhang Lin
 */
public class Red extends PieceColor {

	public Red() {
		startCoords = new int[][] { { 1, 10 }, { 2, 10 }, { 1, 11 }, { 2, 11 } };
		safeCoords = new int[][] { { 1, 13 }, { 2, 13 }, { 3, 13 }, { 4, 13 }, { 5, 13 } };
		homeCoords = new int[][] { { 6, 12 }, { 7, 12 }, { 6, 13 }, { 7, 13 } };
		firstSpot = new int[][] { { 0, 11 } };
		lastSpot = new int[][] { { 0, 13 } };
		color = Color.DARKRED;
	}

	@Override
	public String toString() {
		return "Red";
	}

}
