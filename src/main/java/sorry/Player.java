/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;

import java.util.ArrayList;

/**
 * <p>
 * First version was done by Austin Batistoni, then Yuhang Lin refactored the
 * code by adding the setter for the piece array and use constant to initialize
 * the piece array.
 * 
 * @author austinbatistoni, Yuhang Lin
 */
public class Player {
	private static final int NUM_PIECES = 4;
	String name;
	private int piecesHome; // number of pieces that are in the home
	private int startPieces; // number of pieces that are in the start
	public BoardSquare[] home = new BoardSquare[5];
	private PieceColor color; // color of the pieces

	private Piece[] pieceArray = new Piece[NUM_PIECES];

	public Player(PieceColor c) {
		this.color = c;
		this.piecesHome = 0;
		this.startPieces = NUM_PIECES;
		for (int i = 0; i < NUM_PIECES; i++) {
			ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
			temp.add(c.getStartCoords().get(i));
			pieceArray[i] = new Piece(c, this, temp, i);
		}
	}

	public boolean hasPiecesOnBoard() {
		for (Piece piece : pieceArray) {
			if (piece.getIsInPlay()) {
				return true;
			}
		}
		return false;
	}

	public void removeStartPieces() {
		startPieces--;
	}

	public void addStartPieces() {
		startPieces++;
	}

	public void addFinishedPieces() {
		piecesHome++;
	}

	public PieceColor getPlayerColor() {
		return color;
	}

	public Piece[] getPieces() {
		return pieceArray;
	}

	public int getPiecesHome() {
		return piecesHome;
	}

	public int getStartPieces() {
		return startPieces;
	}

	public static int getNumPieces() {
		return NUM_PIECES;
	}

	/**
	 * Sets the piece array to be the given array.
	 * @param pieceArray an piece array of NUM_PIECES
	 */
	public void setPieceArray(Piece[] pieceArray) {
		if (pieceArray != null && pieceArray.length == NUM_PIECES) {
			this.pieceArray = pieceArray;
		} else {
			System.out.println("PieceArray is not in the correct format");
		}
	}
}
