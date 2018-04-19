/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorry;


import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author austinbatistoni
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
		this.piecesHome = 4;
		this.startPieces = 0;
		for (int i = 0; i < 4; i++) {
			ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
			temp.add(c.getHomeCoords().get(i));
			pieceArray[i] = new Piece(c, this, temp);
		}
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
