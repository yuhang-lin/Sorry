package sorry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class holds information for the game board, specifically the 
 * main path (not including safety, home, or start zones)
 * @author Austin Batistoni, Yuhang Lin
 */
public class Board {
	
	private final int PATH_LENGTH = 60;
	//To store the indexes and associated coordinates for the game path
	private static HashMap<String, Integer> pathMap = new HashMap<>();

	//The x,y coordinates of the main game path
	private int[][] pathArray = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 }, { 7, 0 },
			{ 8, 0 }, { 9, 0 }, { 10, 0 }, { 11, 0 }, { 12, 0 }, { 13, 0 }, { 14, 0 }, { 15, 0 }, { 15, 1 }, { 15, 2 },
			{ 15, 3 }, { 15, 4 }, { 15, 5 }, { 15, 6 }, { 15, 7 }, { 15, 8 }, { 15, 9 }, { 15, 10 }, { 15, 11 },
			{ 15, 12 }, { 15, 13 }, { 15, 14 }, { 15, 15 }, { 14, 15 }, { 13, 15 }, { 12, 15 }, { 11, 15 }, { 10, 15 },
			{ 9, 15 }, { 8, 15 }, { 7, 15 }, { 6, 15 }, { 5, 15 }, { 4, 15 }, { 3, 15 }, { 2, 15 }, { 1, 15 },
			{ 0, 15 }, { 0, 14 }, { 0, 13 }, { 0, 12 }, { 0, 11 }, { 0, 10 }, { 0, 9 }, { 0, 8 }, { 0, 7 }, { 0, 6 },
			{ 0, 5 }, { 0, 4 }, { 0, 3 }, { 0, 2 }, { 0, 1 } };
	
	//To be populated with pathArray data
	private ArrayList<ArrayList<Integer>> pathCoords = new ArrayList<ArrayList<Integer>>();

	//Pieces on the board
	private ArrayList<ArrayList<Piece>> pieces;

	//Default constructor
	public Board() {
		int coordIndex = 0;
		//Populate arraylist with data from pathArray
		for (int[] coord : pathArray) {
			ArrayList<Integer> newCoord = new ArrayList<>();
			String key = "";
			for (int index : coord) {
				newCoord.add(index);
				key += index + " ";
			}
			pathCoords.add(newCoord);
			pathMap.put(key, coordIndex++);
		}
	}

	/**
	 * This method gets the relative index (0-60) associated with a given
	 * x,y coordinate on the board.
	 * @param coords: the x,y coordinate of the grid pane
	 * @return the relative index of that coordinate
	 */
	public static int getPathIndex(ArrayList<Integer> coords) {
		if (coords == null || coords.size() != 2) {
			return -2;
		}
		String key = "";
		for (Integer index : coords) {
			key += index + " ";
		}
		if (pathMap.containsKey(key)) {
			return pathMap.get(key);
		} else {
			return -1;
		}
	}

	/**
	 * This method adds one to the number of finished pieces for the player
	 * 
	 * @param p the piece that is finished
	 */
	public void pieceFinished(Piece p) {
		p.getPlayer().addFinishedPieces();
	}


	/**
	 * Adds players and their pieces to the board
	 * @param players: The array of players
	 */
	public void addPlayers(Player[] players) {
		for (int i = 0; i < players.length; i++) {
			pieces.add(new ArrayList<Piece>(Arrays.asList(players[i].getPieces())));
		}
	}

	/**
	 * Getter for pathCoords
	 * @return the pathCoords
	 */
	public ArrayList<ArrayList<Integer>> getPathCoords() {
		return pathCoords;
	}

	/**
	 * Get the length of the path.
	 * 
	 * @return an integer repesenting the length of the path
	 */
	public int getPathLength() {
		return PATH_LENGTH;
	}

	/**
	 * Get the pieces on the board
	 * @return pieces
	 */
	public ArrayList<ArrayList<Piece>> getPieces() {
		return this.pieces;
	}
}
