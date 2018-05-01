package sorry;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;

/**
 * This class is the superclass for piece color that holds information on the
 * specific locations of board elements relative to the color
 * 
 * @author Austin Batistoni
 */
public class PieceColor {
	protected Color color;
	protected int[][] homeCoords;
	protected int[][] safeCoords;
	protected int[][] startCoords;
	protected int[][] firstSpot;
	protected int[][] lastSpot;

	/**
	 * Gets the JavaFx color of the player
	 * 
	 * @return color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the homeCoords
	 */
	public ArrayList<ArrayList<Integer>> getHomeCoords() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < homeCoords.length; i++) {
			int x = homeCoords[i][0];
			int y = homeCoords[i][1];
			temp.add(new ArrayList<Integer>(Arrays.asList(x, y)));
		}

		return temp;
	}

	/**
	 * @return the safeCoords
	 */
	public ArrayList<ArrayList<Integer>> getSafeCoords() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < safeCoords.length; i++) {
			int x = safeCoords[i][0];
			int y = safeCoords[i][1];
			temp.add(new ArrayList<Integer>(Arrays.asList(x, y)));
		}
		return temp;
	}

	/**
	 * @return the startCoords
	 */
	public ArrayList<ArrayList<Integer>> getStartCoords() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < startCoords.length; i++) {
			int x = startCoords[i][0];
			int y = startCoords[i][1];
			temp.add(new ArrayList<Integer>(Arrays.asList(x, y)));
		}
		return temp;
	}

	/**
	 * @return the firstSpot
	 */
	public ArrayList<ArrayList<Integer>> getFirstSpot() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < firstSpot.length; i++) {
			int x = firstSpot[i][0];
			int y = firstSpot[i][1];
			temp.add(new ArrayList<Integer>(Arrays.asList(x, y)));
		}

		return temp;
	}

	/**
	 * @return the lastSpot
	 */
	public ArrayList<ArrayList<Integer>> getLastSpot() {
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < lastSpot.length; i++) {
			int x = lastSpot[i][0];
			int y = lastSpot[i][1];
			temp.add(new ArrayList<Integer>(Arrays.asList(x, y)));
		}

		return temp;
	}
}
