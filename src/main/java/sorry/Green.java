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
public class Green extends PieceColor {

	public Green() {
		startCoords = new int[][] { { 10, 13 }, { 10, 14 }, { 11, 13 }, { 11, 14 } };
		homeCoords = new int[][] { { 13, 9 }, { 13, 8 }, { 12, 9 }, { 12, 8 } };
		safeCoords = new int[][] { { 13, 14 }, { 13, 13 }, { 13, 12 }, { 13, 11 }, { 13, 10 } };
		firstSpot = new int[][] { { 11, 15 } };
		lastSpot = new int[][] { { 13, 15 } };
		color = Color.GREEN;
	}

	@Override
	public String toString() {
		return "Green";
	}
}
