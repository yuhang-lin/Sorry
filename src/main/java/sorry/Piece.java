package sorry;

public class Piece {

	private PieceColor color;
	private int[][] location;
	private int[][] possibleMoves;
	private int movement;
	private Player player;

	public Piece(PieceColor c, Player p) {
		this.color = c;
		this.player = p;
	}

	public void Move(int squareNum) {
		movement = squareNum;
	}

	public void setLocation(int[][] sq) {
		location = sq;
	}

	public int[][] getLocation() {
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
	public int[][] getPossibleMoves() {
		return possibleMoves;
	}

	/**
	 * @param possibleMoves
	 *            the possibleMoves to set
	 */
	public void setPossibleMoves(int[][] possibleMoves) {
		this.possibleMoves = possibleMoves;
	}

}
