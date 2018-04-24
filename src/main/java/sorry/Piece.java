package sorry;

import java.util.ArrayList;
import java.util.Arrays;

public class Piece {

	private PieceColor color;
	private ArrayList<ArrayList<Integer>> location;
	private ArrayList<ArrayList<Integer>> possibleMoves;
	private int movement;
	private Player player;
	private boolean isInPlay;
	private boolean isSelected;
	private int homeIndex;
	private boolean isSafe;
	private boolean isHome;

	public Piece(PieceColor c, Player p, ArrayList<ArrayList<Integer>> location, int index) {
		this.color = c;
		this.player = p;
		this.isInPlay = false;
		this.homeIndex = index;
		this.isHome = false;
		this.isSafe = false;
		this.location = location;
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
	
	public boolean isSafe() {
		return isSafe;
	}
	
	public void setSafe() {
		this.isSafe = true;
		this.setOutOfPlay();
	}
	
	public void setNotSafe() {
		this.isSafe = false;
		this.setInPlay();
	}
	
	public boolean isPieceSafe() {
		for(int i = 0; i < this.color.getSafeCoords().size(); i++) {
			if(this.getLocation().get(0).get(0) == this.color.getSafeCoords().get(i).get(0)
					&& this.getLocation().get(0).get(1) == this.color.getSafeCoords().get(i).get(1)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isHome() {
		return isHome;
	}
	
	public void setIsHome() {
		this.isHome = true;
	}

}
