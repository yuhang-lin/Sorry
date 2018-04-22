/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;

import java.util.ArrayList;

/**
 *
 * @author austinbatistoni, Yuhang Lin
 */
public class Player {
	final int NUM_PIECES = 4;
	String name;
	private int piecesHome;
	private int startPieces;
	public BoardSquare[] home = new BoardSquare[5];
	private PieceColor color;
	private Piece[] pieceArray = new Piece[4];
	

	public Player(PieceColor c) {
		this.color = c;
		this.piecesHome = 0;
		this.startPieces = 4;
		for (int i = 0; i < 4; i++) {
			ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
			temp.add(c.getHomeCoords().get(i));
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
}
