package sorry;

public class Piece {

	private String color;
	private Square location;
	private int movement;
	private boolean occupied;
	
	public void Move(int squareNum){
		movement = squareNum;
	}
	
	public void canMove(boolean isOccupied){
		occupied = isOccupied;
	}
	
	public void setLocation(Square sq){
		location = sq;
	}
	
	public Square getLocation(){
		return location;
	}
	
	public String getColor(){
		return color;
	}
	
	
}
