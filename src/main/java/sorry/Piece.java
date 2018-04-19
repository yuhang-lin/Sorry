package sorry;
import java.util.ArrayList;
public class Piece {

	private PieceColor color;
	private ArrayList<ArrayList<Integer>> location;
	private ArrayList<ArrayList<Integer>> possibleMoves;
	private int movement;
	private Player player;

	public Piece(PieceColor c, Player p) {
		this.color = c;
		this.player = p;
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

}
