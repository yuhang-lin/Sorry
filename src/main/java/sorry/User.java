package sorry;

public class User extends Player {

	private PieceColor color;
	private int movement;
	private boolean occupied;

	public User(PieceColor c) {
		super(c);
	}

	public PieceColor getColor() {
		return color;
	}

	public void Move(int squareNum) {
		movement = squareNum;
	}

	public void canMove(boolean isOccupied) {
		occupied = isOccupied;
	}

}
