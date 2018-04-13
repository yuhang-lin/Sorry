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
public class Blue extends PieceColor {

	Blue() {
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
