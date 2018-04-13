/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;

/**
 * 
 * @author austinbatistoni
 */
class BoardSquare {
	private int[][] squareLocation;
	private boolean isOccupied;
	private Piece piece;

	/**
	 * Constructor initializes boardsquare with its location on the GridPane
	 * 
	 * @param location
	 *            location on the gridpane
	 */
	public BoardSquare(int[][] location) {
		this.squareLocation = location;
	}

	/**
	 * @return squares location on the GridPane
	 */
	public int[][] getLocation() {
		return this.squareLocation;
	}

	/**
	 * @return the isOccupied
	 */
	public boolean isOccupied() {
		return isOccupied;
	}

	/**
	 * @param isOccupied
	 *            the isOccupied to set
	 */
	public void setIsOccupied(boolean isOccupied, Piece p) {
		this.isOccupied = isOccupied;
		this.piece = p;
	}

	/**
	 * @return the Piece occupying the square
	 */
	public Piece getPiece() {
		return piece;
	}

}
