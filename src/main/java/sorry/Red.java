/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;

import javafx.scene.paint.Color;

/**
 *
 * @author austinbatistoni
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
