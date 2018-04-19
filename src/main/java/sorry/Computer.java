package sorry;

import java.util.ArrayList;

public class Computer extends Player {

	private PieceColor color;
	private int movement;
	private boolean occupied;
	private NiceLevel niceLevel;
	private SmartLevel smartLevel;

	public Computer(PieceColor c, NiceLevel n, SmartLevel s) {
		super(c);
		this.niceLevel = n;
		this.smartLevel = s;
	}

	public enum NiceLevel {
		NICE, MEAN;
	}

	public enum SmartLevel {
		SMART, DUMB;
	}

	public PieceColor getColor() {
		return color;
	}

	public void Move(int squareNum) {
		
		movement = squareNum;
	}

	public boolean canMove(ArrayList<ArrayList<Integer>> targetLocation) {
		return true;
	}
	
	

	public NiceLevel getNiceLevel() {
		return niceLevel;
	}

	public SmartLevel getSmartLevel() {
		return smartLevel;
	}
}
