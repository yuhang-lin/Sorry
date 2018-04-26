package sorry;

/**
 * Board square in the grid pane.
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
