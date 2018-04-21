/**
 * Main class for the game
 */
package sorry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Yuhang Lin
 *
 */
public class Main extends Application {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	Piece selected;
	Piece prevPiece;
	Circle selectedCircle;
	ArrayList<ArrayList<Integer>> moves;
	Deck deck = new Deck();
	String logFile = "game_status.txt";
	int currentTerm = 0; // current term for the player
	Board board;
	
	ArrayList<Player> players = new ArrayList<Player>();
	
	boolean hasDrawn;
	
	Text turnText;
	Text option1;
	Text option2;
	Text directions;
	
	boolean sorryCard;
	/**
	 * Save the current game status.
	 * 
	 * @return
	 */
	public boolean save() {
		try {
			PrintWriter printWriter = new PrintWriter(logFile);
			for (Card card : deck.getCards()) {
				printWriter.print(card.getName() + ","); // Save all the cards
			}
			printWriter.println("\n" + deck.getNumUsed());
			printWriter.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Restore the game status from a text file.
	 * 
	 * @return 0 if there is no error, 1 if file is not found, 2 otherwise.
	 */
	public int restore() {
		try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
			String cardList = br.readLine();
			Card[] newCards = new Card[deck.NUM_CARDS];
			int index = 0;
			for (String cardName : cardList.split(",")) {
				if (cardName.length() > 0) {
					newCards[index++] = new Card(cardName); // Restore all the cards
				}
			}
			deck.setCards(newCards);
			deck.setNumUsed(Integer.parseInt(br.readLine()));
		} catch (FileNotFoundException e) {
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		return 0;
	}

	@Override
	public void start(Stage primaryStage) {
		GridPane pane = new GridPane();
		board = new Board();

		primaryStage.setTitle("Sorry!");
		Text t = new Text(0, 0, "SORRY!");
		t.setFont(new Font(30));
		pane.add(t, 20, 0);
		drawBoard(pane, board);
		// t.setRotate(45);

		Button btnDraw = new Button("Draw");
		pane.add(btnDraw, 20, 2);
		Button btnSave = new Button("Save game");
		pane.add(btnSave, 30, 2);
		Button btnRestore = new Button("Restore game");
		pane.add(btnRestore, 50, 2);

		directions = new Text("Please draw a card.");
		pane.add(directions, 20, 5);
		option1 = new Text("");
		option2 = new Text("");
		pane.add(option1, 20, 6);
		pane.add(option2, 20, 7);
		
		turnText = new Text("Blue player's turn");
		turnText.setFill(Color.BLUE);
		pane.add(turnText, 20, 1);
		
		Player blue = new Computer(new Blue(), Computer.NiceLevel.MEAN, Computer.SmartLevel.SMART); // for testing only
		Player green = new Player(new Green());
		Player red = new Player(new Red());
		Player yellow = new Player(new Yellow());
		players.add(blue);
		players.add(yellow);
		players.add(green);
		players.add(red);
		//Player[] players = { blue, yellow, green, red };
		//board.addPlayers(players);

		for (int i = 0; i < players.size(); i++) {
			Piece[] pieces = players.get(i).getPieces();
			for (int j = 0; j < pieces.length; j++) {
				drawPiece(pieces[j], primaryStage, pane, board, j);
				ArrayList<ArrayList<Integer>> location = new ArrayList<ArrayList<Integer>>();
				location.add(pieces[j].getColor().getStartCoords().get(i));
				pieces[j].setLocation(location);
			}
			fillInSquares(players.get(i).getPlayerColor().getStartCoords(), players.get(i).getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players.get(i).getPlayerColor().getSafeCoords(), players.get(i).getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players.get(i).getPlayerColor().getHomeCoords(), players.get(i).getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players.get(i).getPlayerColor().getFirstSpot(), players.get(i).getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players.get(i).getPlayerColor().getLastSpot(), players.get(i).getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
		}

		Scene scene = new Scene(pane, 1000, 735);
		scene.setFill(Color.WHITE);
		primaryStage.setScene(scene);
		primaryStage.show();

		btnDraw.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hasDrawn = true;
				sorryCard = false;
				Card currCard = deck.draw();
				String card = currCard.getName();
				directions.setText("The card is: " + currCard.getName());
				
				Player currentPlayer = players.get(currentTerm);

				switch (card) {
				case "1":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 1 space.");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> oneMoves = new ArrayList<ArrayList<Integer>>();
						if(p.getIsInPlay()) {
							oneMoves.add(getMoveFromInt(board, p, 1));
						}else {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(p.getColor().getFirstSpot().get(0).get(0));
							temp.add(p.getColor().getFirstSpot().get(0).get(1));
							oneMoves.add(temp);
						}

						p.setPossibleMoves(oneMoves);
					}

					break;

				case "2":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 2 spaces.");


					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> twoMoves = new ArrayList<ArrayList<Integer>>();
						if(p.getIsInPlay()) {
							twoMoves.add(getMoveFromInt(board, p, 2));
						}else {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(p.getColor().getFirstSpot().get(0).get(0));
							temp.add(p.getColor().getFirstSpot().get(0).get(1));
							twoMoves.add(temp);
						}

						p.setPossibleMoves(twoMoves);
					}
					break;

				case "3":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 3 spaces.");
						option2.setText("");

						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> threeMoves = new ArrayList<ArrayList<Integer>>();

							threeMoves.add(getMoveFromInt(board, p, 3));
							p.setPossibleMoves(threeMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "4":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn backwards 4 spaces.");
						option2.setText("");

						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> fourMoves = new ArrayList<ArrayList<Integer>>();

							fourMoves.add(getMoveFromInt(board, p, -4));
							p.setPossibleMoves(fourMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}
					break;

				case "5":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 5 spaces.");
						option2.setText("");

						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> fiveMoves = new ArrayList<ArrayList<Integer>>();

							fiveMoves.add(getMoveFromInt(board, p, 5));
							p.setPossibleMoves(fiveMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "7":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 7 spaces.");
						option2.setText("");

						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> sevenMoves = new ArrayList<ArrayList<Integer>>();
							sevenMoves.add(getMoveFromInt(board, p, 7));
							p.setPossibleMoves(sevenMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}
					break;

				case "8":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 8 spaces.");
						option2.setText("");
						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> eightMoves = new ArrayList<ArrayList<Integer>>();

							eightMoves.add(getMoveFromInt(board, p, 8));
							p.setPossibleMoves(eightMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "10":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 10 spaces.");
						option2.setText("");
						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>>tenMoves = new ArrayList<ArrayList<Integer>>();

							tenMoves.add(getMoveFromInt(board, p, 10));
							p.setPossibleMoves(tenMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "11":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 11 spaces.");
						option2.setText("");
						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> elevenMoves = new ArrayList<ArrayList<Integer>>();

							elevenMoves.add(getMoveFromInt(board, p, 11));
							p.setPossibleMoves(elevenMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "12":
					if(currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 12 spaces.");
						option2.setText("");
						for (Piece p : currentPlayer.getPieces()) {
							ArrayList<ArrayList<Integer>> twelveMoves = new ArrayList<ArrayList<Integer>>();

							twelveMoves.add(getMoveFromInt(board, p, 12));
							p.setPossibleMoves(twelveMoves);
						}
					}else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;
				case "Sorry":
					ArrayList<ArrayList<Integer>> sorryMoves = new ArrayList<ArrayList<Integer>>();
					ArrayList<Piece> piecesOnBoard = piecesOnBoard();
					if(!piecesOnBoard.isEmpty()) {
						option1.setText(currCard.getInfo());
						option2.setText("Select a piece to bump.");
						sorryCard = true;
						
						for(Piece p: piecesOnBoard) {
							int xLoc = p.getLocation().get(0).get(0);
							int yLoc = p.getLocation().get(0).get(1);
							
							sorryMoves.add(new ArrayList<Integer>(Arrays.asList(xLoc, yLoc)));
							System.out.println(xLoc + ", " + yLoc);
						}
						
						for(Piece p : currentPlayer.getPieces()) {
							p.setPossibleMoves(sorryMoves);
						}
						
					}else{
						option1.setText("No players on board.");
						option2.setText("Unable to move");
					}
					
					break;
				}

			}
		});

		btnSave.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				boolean isSuccess = save();
				if (isSuccess) {
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(Calendar.getInstance().getTime());
					directions.setText("Succesfully saved the game at " + timeStamp);
				} else {
					directions.setText("Failed to save the game.");
				}
			}
		});

		btnRestore.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				int result = restore();
				if (result == 0) {
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(Calendar.getInstance().getTime());
					directions.setText("Succesfully restore the game at " + timeStamp);
				} else if (result == 1) {
					directions.setText("It seem that you didn't save the game before.");
				} else {
					directions.setText("Failed to restore the game.");
				}
			}
		});
	}
	
	public ArrayList<Piece> piecesOnBoard() {
		ArrayList<Piece> piecesOnBoard = new ArrayList<Piece>();
		for(Player player : players) {
			for(Piece piece : player.getPieces()) {
				if(piece.getIsInPlay()) {
					piecesOnBoard.add(piece);
				}
			}
		}
		return piecesOnBoard;
	}


	public void nextTurn() {
		if(currentTerm == 3) {
				currentTerm = 0;
			}else{
				currentTerm++;
			}
		
		switch (currentTerm) {
		case 0:
			turnText.setText("Blue player's turn");
			turnText.setStroke(Color.BLUE);
			break;
		case 1:
			turnText.setText("Yellow player's turn");
			turnText.setStroke(Color.rgb(153, 134, 0));
			break;
		case 2:
			turnText.setText("Green player's turn");
			turnText.setStroke(Color.GREEN);
			break;
		case 3: 
			turnText.setText("Red player's turn");
			turnText.setStroke(Color.RED);
		}
		
		option1.setText("");
		option2.setText("");
		directions.setText("Please draw a card.");
	}
	
	public ArrayList<Integer> getMoveFromInt(Board board, Piece piece, int increment) {
		ArrayList<Integer> move = new ArrayList<Integer>();
		for (int i = 0; i < board.getPathCoords().size(); i++) {
			for (int j = 0; j < 2; j++) {
				if (piece.getLocation().get(0).get(0) == board.getPathCoords().get(i).get(0)
						&& piece.getLocation().get(0).get(1) == board.getPathCoords().get(i).get(1)) {
					int boardIndex = (i + increment) % board.getPathLength();
					int newX = board.getPathCoords().get(boardIndex).get(0);
					int newY = board.getPathCoords().get(boardIndex).get(1);
					move.add(newX);
					move.add(newY);
				}
			}
		}
		return move;
	}

	public void fillInSquares(ArrayList<ArrayList<Integer>> grid, Color inside, Color outside, double radius, GridPane pane) {
		Stop[] stops1 = new Stop[] { new Stop(0.5, inside), new Stop(0.99, outside) };
		RadialGradient lg1 = new RadialGradient(0, 0, 0.5, 0.5, radius, true, CycleMethod.NO_CYCLE, stops1);
		for (int i = 0; i < grid.size(); i++) {
			if (!grid.get(i).isEmpty()) {
				Rectangle r = (Rectangle) getNodeFromGridPane(pane, grid.get(i).get(0), grid.get(i).get(1));
				r.setFill(lg1);
				r.setStroke(Color.BLACK);
			}
		}
	}

	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	public void drawPiece(Piece p, Stage primaryStage, GridPane pane, Board board, int i) {
		//p.setLocation(moves);
		Circle circle = new Circle();
		// c.setFill(p.getPlayerColor().getColor());
		circle.setStroke(Color.BLACK);
		circle.setRadius(10.0f);
		circle.setFill(p.getColor().getColor().deriveColor(0, 1, 10, 1));
		GridPane.setHalignment(circle, HPos.CENTER);
		GridPane.setValignment(circle, VPos.CENTER);
		pane.add(circle, p.getColor().getStartCoords().get(i).get(0), p.getColor().getStartCoords().get(i).get(1));

		 circle.setOnMousePressed(new EventHandler<MouseEvent>() {
			 @Override public void handle(MouseEvent event) {
				 circle.setStroke(Color.WHITE);
			 }
		 });
		
		 circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
			 @Override public void handle(MouseEvent event) {
				 if(sorryCard) {
					 if(!p.getIsInPlay()) {
						 selected = p;
						 selectedCircle = circle;
						 fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, selected.getColor().getColor(), .7, pane);
						 System.out.println(selected.getPossibleMoves());
						 //sorryCard = false;
					 }
				 }else {
					 if(hasDrawn) {
						 selected = p;
						 selectedCircle = circle;
						 System.out.println(selected.getPossibleMoves());
					 
						 fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, selected.getColor().getColor(), .7, pane);
					 }
				 }
				 
				 
				 
				 
				 
		 }
		
		 });
	}

	public void drawBoard(GridPane pane, Board board) {

		for (int i = 0; i <= 15; i++) {

			for (int j = 0; j <= 15; j++) {
				Rectangle rectangle;
				rectangle = new Rectangle(45, 45, Color.WHITE);
				pane.add(rectangle, i, j);
				ArrayList<ArrayList<Integer>> location = new ArrayList<ArrayList<Integer>>();
				location.add(new ArrayList<Integer>(Arrays.asList(i,j)));
				
				
				 rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
				 @Override public void handle(MouseEvent event) {
					 
					 if(sorryCard) {
						System.out.println("SORRY");
			 			Piece piece = bumpPiece(location);
			 			Node node = null;
			 			for (Node n : pane.getChildren()) {
			 				if(n instanceof Circle 
			 						&& GridPane.getColumnIndex(n) == location.get(0).get(0)
			 						&& GridPane.getRowIndex(n) == location.get(0).get(1)) {
			 						node = n;		
			 				}
			 				
			 			}
			 			int locX = piece.getColor().startCoords[piece.getHomeIndex()][0];
			 			int locY = piece.getColor().startCoords[piece.getHomeIndex()][1];
			 			ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
			 			temp.add(new ArrayList<Integer>(Arrays.asList(locX, locY)));
			 			
			 			pane.getChildren().remove(node);
	 					pane.add(node, locX, locY);
			 			pane.getChildren().remove(selectedCircle);
			 			
			 			piece.setLocation(temp);
			 			piece.setOutOfPlay();
			 			pane.add(selectedCircle, location.get(0).get(0), location.get(0).get(1));
			 			fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, Color.BLACK, 1, pane);
			 			selectedCircle.setStroke(Color.BLACK);
			 			selected.setLocation(location);
			 			selected.setInPlay();
			 			sorryCard = false;
			 			nextTurn();
			 		}else {
					 for(int k = 0; k < selected.getPossibleMoves().size(); k++){
					 		if((selected.getPossibleMoves().get(k).get(0) == location.get(k).get(0)) 
					 				&& selected.getPossibleMoves().get(k).get(1) == location.get(k).get(1)){
					 			pane.getChildren().remove(selectedCircle);
					 			pane.add(selectedCircle, location.get(0).get(0), location.get(0).get(1));
					 			if(!selected.getIsInPlay()) {
					 				selected.setInPlay();
					 			}
					 			selected.setLocation(location);
					 			selectedCircle.setStroke(Color.BLACK);
					 			
					 			
					 			
					 			fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, Color.BLACK, 1, pane);
					 			nextTurn();
					 			
					 		}
					 	}
				 	}
				 }
				 });

			}
		}

		fillInSquares(board.getPathCoords(), Color.LIGHTGRAY, Color.BLACK, 1, pane);
	}

	public void moveFromStart(Piece p, GridPane pane, Circle circle) {
		ArrayList<ArrayList<Integer>> firstSpot = p.getColor().getFirstSpot();
		pane.getChildren().remove(circle);
		pane.add(circle, firstSpot.get(0).get(0), firstSpot.get(0).get(1));
		p.setLocation(firstSpot);
		p.setInPlay();
		circle.setStroke(Color.BLACK);
	}
	
	public Piece bumpPiece(ArrayList<ArrayList<Integer>> location) {
		for(Piece piece : piecesOnBoard()) {
			if(piece.getLocation() == location) {
				return piece;
			}
		}
		return null;
	}

}
