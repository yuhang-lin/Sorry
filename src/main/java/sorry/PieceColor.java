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
public class PieceColor {
	protected Color color;
	protected int[][] homeCoords;
	protected int[][] safeCoords;
	protected int[][] startCoords;
	protected int[][] firstSpot;
	protected int[][] lastSpot;
	

	public Color getColor() {
		return color;
	}

	/**
	 * @return the homeCoords
	 */
	public int[][] getHomeCoords() {
		return homeCoords;
	}

	/**
	 * @return the safeCoords
	 */
	public int[][] getSafeCoords() {
		return safeCoords;
	}

	/**
	 * @return the startCoords
	 */
	public int[][] getStartCoords() {
		return startCoords;
	}

	/**
	 * @return the firstSpot
	 */
	public int[][] getFirstSpot() {
		return firstSpot;
	}

	/**
	 * @return the lastSpot
	 */
	public int[][] getLastSpot() {
		return lastSpot;
	}
}