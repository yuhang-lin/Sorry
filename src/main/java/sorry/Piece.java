package sorry;

import java.util.ArrayList;

public class Piece {

	private PieceColor color;
	private ArrayList<ArrayList<Integer>> location;
	private ArrayList<ArrayList<Integer>> possibleMoves;
	private int movement;
	private Player player;
	private boolean isInPlay;
	private boolean isSelected;
	private int homeIndex;

	public Piece(PieceColor c, Player p, ArrayList<ArrayList<Integer>> location, int index) {
		this.color = c;
		this.player = p;
		this.isInPlay = false;
		this.homeIndex = index;
	}

	public void Move(int squareNum) {
		movement = squareNum;
	}

	public void setLocation(ArrayList<ArrayList<Integer>> sq) {

		location = sq;
	}

	public ArrayList<ArrayList<Integer>> getLocation() {
		return location;
	}

	public PieceColor getColor() {
		return color;
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the possibleMoves
	 */
	public ArrayList<ArrayList<Integer>> getPossibleMoves() {
		return possibleMoves;
	}

	/**
	 * @param possibleMoves
	 *            the possibleMoves to set
	 */
	public void setPossibleMoves(ArrayList<ArrayList<Integer>> possibleMoves) {
		this.possibleMoves = possibleMoves;
	}

	public boolean getIsInPlay() {
		return isInPlay;
	}

	public void setInPlay() {
		this.isInPlay = true;
	}

	public void setOutOfPlay() {
		this.isInPlay = false;
	}

	public boolean getIsSelected() {
		return isSelected;
	}

	public void select() {
		this.isSelected = true;
	}

	public void unSelect() {
		this.isSelected = false;
	}

	public int getHomeIndex() {
		return homeIndex;
	}

}
