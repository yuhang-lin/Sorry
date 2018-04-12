package sorry;

public class Computer extends Player {
	
	

	private PieceColor color;
	private int movement;
	private boolean occupied;
	private NiceLevel niceLevel;
	private SmartLevel smartLevel;
	
	public Computer(PieceColor c) {
		super(c);
	}
	 public void Computer(PieceColor c, NiceLevel n, SmartLevel s){
         this.color = c;
         this.niceLevel = n;
         this.smartLevel = s;
     }
	 
	 public enum NiceLevel{
		 NICE, MEAN; 
	 }
	 
	public enum SmartLevel{
		SMART, DUMB;
	}
	 
	public PieceColor getColor(){
		return color;
	}
		
	public void Move(int squareNum){
		movement = squareNum;
	}
		
	public void canMove(boolean isOccupied){
		occupied = isOccupied;
	}
}
