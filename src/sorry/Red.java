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
	private int[][] redStart = { { 1, 10 }, { 2, 10 }, { 1, 11 }, { 2, 11 } };
	private int[][] redSafe = { { 1, 13 }, { 2, 13 }, { 3, 13 }, { 4, 13 }, { 5, 13 } };
	private int[][] redHome = { { 6, 12 }, { 7, 12 }, { 6, 13 }, { 7, 13 } };
	private int[][] redFirst = { { 0, 11 } };
	private int[][] redLast = { { 0, 13 } };

	public Red() {

	}

	@Override
	public Color getColor() {
		return Color.DARKRED;
	}

	/**
	 * @return the homeCoords
	 */
	@Override
	public int[][] getHomeCoords() {
		return redHome;
	}

	/**
	 * @return the safeCoords
	 */
	@Override
	public int[][] getSafeCoords() {
		return redSafe;
	}

	/**
	 * @return the startCoords
	 */
	@Override
	public int[][] getStartCoords() {
		return redStart;
	}

	/**
	 * @return the firstSpot
	 */
	@Override
	public int[][] getFirstSpot() {
		return redFirst;
	}

	/**
	 * @return the lastSpot
	 */
	@Override
	public int[][] getLastSpot() {
		return redLast;
	}

	@Override
	public String toString() {
		return "Red";
	}

}
