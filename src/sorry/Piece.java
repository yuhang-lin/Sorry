package sorry;

public class Piece {

	private String color;
	private BoardSquare location;
	private int movement;
	private boolean occupied;
	
	public void Move(int squareNum){
		movement = squareNum;
	}
	
	public void canMove(boolean isOccupied){
		occupied = isOccupied;
	}
	
	public void setLocation(BoardSquare sq){
		location = sq;
	}
	
	public BoardSquare getLocation(){
		return location;
	}
	
	public String getColor(){
		return color;
	}
	
	
}
