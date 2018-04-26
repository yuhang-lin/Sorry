package sorry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author austinbatistoni
 */
public class Board {

	private final int PATH_LENGTH = 60;
	private final int SLIDE_LENGTH = 4;
	private final ArrayList<BoardSquare> path = new ArrayList<BoardSquare>();
	private static HashMap<String, Integer> pathMap = new HashMap<>();

	private int[][] pathArray = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 }, { 7, 0 },
			{ 8, 0 }, { 9, 0 }, { 10, 0 }, { 11, 0 }, { 12, 0 }, { 13, 0 }, { 14, 0 }, { 15, 0 }, { 15, 1 }, { 15, 2 },
			{ 15, 3 }, { 15, 4 }, { 15, 5 }, { 15, 6 }, { 15, 7 }, { 15, 8 }, { 15, 9 }, { 15, 10 }, { 15, 11 },
			{ 15, 12 }, { 15, 13 }, { 15, 14 }, { 15, 15 }, { 14, 15 }, { 13, 15 }, { 12, 15 }, { 11, 15 }, { 10, 15 },
			{ 9, 15 }, { 8, 15 }, { 7, 15 }, { 6, 15 }, { 5, 15 }, { 4, 15 }, { 3, 15 }, { 2, 15 }, { 1, 15 },
			{ 0, 15 }, { 0, 14 }, { 0, 13 }, { 0, 12 }, { 0, 11 }, { 0, 10 }, { 0, 9 }, { 0, 8 }, { 0, 7 }, { 0, 6 },
			{ 0, 5 }, { 0, 4 }, { 0, 3 }, { 0, 2 }, { 0, 1 } };

	private ArrayList<ArrayList<Integer>> pathCoords = new ArrayList<ArrayList<Integer>>();

	private ArrayList<ArrayList<Piece>> pieces;

	public Board() {
		int coordIndex = 0;
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
	 * @param p
	 *            the piece that is finished
	 */
	public void pieceFinished(Piece p) {
		p.getPlayer().addFinishedPieces();
	}

	/**
	 * This method brings a pawn out of the start circle and adds it to the main
	 * path at corresponding start position the color.
	 * 
	 * @param p
	 *            the player starting a piece
	 * @param piece
	 *            the piece that the player is starting
	 */
	// public void startPiece(Player p, Piece piece){
	// p.removeStartPieces();
	// if(p.getColor() == Color.BLUE){
	// path[BLUE_FIRST_SQUARE].setIsOccupied(true, piece);
	// }else if(p.getColor() == Color.GREEN){
	// path[GREEN_FIRST_SQUARE].setIsOccupied(true, piece);
	// }else if(p.getColor() == Color.RED){
	// path[RED_FIRST_SQUARE].setIsOccupied(true, piece);
	// }else{
	// path[YELLOW_FIRST_SQUARE].setIsOccupied(true, piece);
	// }
	// }

	/**
	 * This method bumps a piece back to the start
	 * 
	 * @param piece
	 *            the piece that is bumped back to start
	 */
	// public void bumpPiece(Piece piece){
	// piece.removeFromBoard(path);
	// piece.getPlayer().addStartPieces();
	// }

	/**
	 * This method contains the main moving logic. Checks various conditions before
	 * moving the piece.
	 * 
	 * @param piece
	 *            the pawn being moved
	 * @param location
	 *            the tentative new location for the piece, based on the card drawn
	 */
	// public void move(Piece piece, int location){
	// //Bumps opponent back to start if occupying the location square
	// if (path[location].isOccupied() && path[location].getPiece().getColor() !=
	// piece.getColor()){
	// bumpPiece(path[location].getPiece());
	// }
	//
	// //Checks if the tentative location is a usable slide, slides the pawn +4
	// spaces if so
	// if (canSlide(piece,location)){
	// piece.setLocation(location+SLIDE_LENGTH);
	// path[location+SLIDE_LENGTH].setIsOccupied(true, piece);
	// }else{
	// //If the pawn's move takes it off the main board and into its "home stretch"
	// if (canGoHome(piece, location)){
	// addToHome(piece, location);
	// }else{
	// piece.setLocation(location);
	// path[location].setIsOccupied(true, piece);
	// }
	// }
	// }

	/**
	 * This method checks if a tentative location is a slide that can be used by the
	 * piece in question.
	 * 
	 * @param piece
	 *            the piece being moved
	 * @param location
	 *            the tentative new location for the piece
	 * @return true if the piece can slide, false otherwise
	 */
	// public boolean canSlide(Piece piece, int location){
	// if (location == BLUE_SLIDE){
	// if (piece.getColor() != Color.BLUE){
	// return true;
	// }
	// }else if(location == GREEN_SLIDE){
	// if (piece.getColor() != Color.GREEN){
	// return true;
	// }
	// }else if(location == RED_SLIDE){
	// if (piece.getColor() != Color.RED){
	// return true;
	// }
	// }else if(location == YELLOW_SLIDE){
	// if (piece.getColor() != Color.RED){
	// return true;
	// }
	// }
	// return false;
	// }
	//
	/**
	 * This method checks if the tentative new location for a pawn is in their "home
	 * stretch"
	 * 
	 * @param p
	 *            the piece being moved
	 * @param location
	 *            the tentative new location for the piece
	 * @return true if the piece should be moved to its home array, false otherwise
	 */
	// public boolean canGoHome(Piece p, int location){
	// if(p.getColor() == Color.BLUE){
	// if (location > BLUE_LAST_SQUARE){
	// return true;
	// }
	// }else if(p.getColor() == Color.GREEN){
	// if (location > GREEN_LAST_SQUARE){
	// return true;
	// }
	// }else if(p.getColor() == Color.RED){
	// if (location > RED_LAST_SQUARE){
	// return true;
	// }
	// }else if(p.getColor() == Color.YELLOW){
	// if (location > YELLOW_LAST_SQUARE){
	// return true;
	// }
	// }
	// return false;
	// }

	// /**
	// * This method moves a piece to its home array, removing it from the main path
	// * @param p the piece being moved
	// * @param location the tentative new location for the piece
	// */
	// public void addToHome(Piece p, int location){
	// int newLocation = location - p.getLocation();
	// path[location].setIsOccupied(false, null);
	// p.getPlayer().home[newLocation].setIsOccupied(true, p);
	// p.setLocation(newLocation);
	// }

	public void addPlayers(Player[] players) {
		for (int i = 0; i < players.length; i++) {
			pieces.add(new ArrayList<Piece>(Arrays.asList(players[i].getPieces())));
		}
	}

	/**
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

	public ArrayList<ArrayList<Piece>> getPieces() {
		return this.pieces;
	}
}
