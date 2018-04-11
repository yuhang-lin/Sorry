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
