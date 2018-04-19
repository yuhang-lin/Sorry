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
	Circle prevCircle;
	ArrayList<ArrayList<Integer>> moves;
	Deck deck = new Deck();
	String logFile = "game_status.txt";
	int currentTerm = 0; // current term for the player

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
		Board board = new Board();

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

		Text t1 = new Text("Please draw a card");
		pane.add(t1, 20, 5);
		Text option1 = new Text("");
		Text option2 = new Text("");
		pane.add(option1, 20, 6);
		pane.add(option2, 20, 7);
		Player blue = new Computer(new Blue(), Computer.NiceLevel.MEAN, Computer.SmartLevel.SMART); // for testing only
		Player green = new Player(new Green());
		Player red = new Player(new Red());
		Player yellow = new Player(new Yellow());
		Player[] players = { blue, green, red, yellow };
		// board.addPlayers(players);

		for (int i = 0; i < players.length; i++) {
			Piece[] pieces = players[i].getPieces();
			for (int j = 0; j < pieces.length; j++) {
				drawPiece(pieces[j], primaryStage, pane, board, j);
				ArrayList<ArrayList<Integer>> location = new ArrayList<ArrayList<Integer>>();
				location.add(pieces[j].getColor().getStartCoords().get(i));
				pieces[j].setLocation(location);
			}
			fillInSquares(players[i].getPlayerColor().getStartCoords(), players[i].getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players[i].getPlayerColor().getSafeCoords(), players[i].getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players[i].getPlayerColor().getHomeCoords(), players[i].getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players[i].getPlayerColor().getFirstSpot(), players[i].getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
			fillInSquares(players[i].getPlayerColor().getLastSpot(), players[i].getPlayerColor().getColor(),
					Color.BLACK, 1, pane);
		}

		Scene scene = new Scene(pane, 1000, 735);
		scene.setFill(Color.WHITE);
		primaryStage.setScene(scene);
		primaryStage.show();

		btnDraw.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				Card currCard = deck.draw();
				String card = currCard.getName();
				t1.setText("The card is: " + currCard.getName());
				
				Player currentPlayer = players[currentTerm];

				switch (card) {
				case "1":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 1 space.");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> oneMoves = new ArrayList<ArrayList<Integer>>();
						if(selected.getIsInPlay()) {
							oneMoves.add(getMoveFromInt(board, selected, 1));
						}else {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(selected.getColor().getFirstSpot().get(0).get(0));
							temp.add(selected.getColor().getFirstSpot().get(0).get(1));
							oneMoves.add(temp);
						}

						selected.setPossibleMoves(oneMoves);
					//}

					break;

				case "2":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 2 spaces.");


					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> twoMoves = new ArrayList<ArrayList<Integer>>();
						if(selected.getIsInPlay()) {
							twoMoves.add(getMoveFromInt(board, selected, 2));
						}else {
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(selected.getColor().getFirstSpot().get(0).get(0));
							temp.add(selected.getColor().getFirstSpot().get(0).get(1));
							twoMoves.add(temp);
						}

						selected.setPossibleMoves(twoMoves);
					//}
					break;

				case "3":
					option1.setText("You must move a pawn forward 3 spaces.");
					option2.setText("");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> threeMoves = new ArrayList<ArrayList<Integer>>();

						threeMoves.add(getMoveFromInt(board, selected, 3));
						selected.setPossibleMoves(threeMoves);
					//}

					break;

				case "4":
					option1.setText("You must move a pawn backwards 4 spaces.");
					option2.setText("");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> fourMoves = new ArrayList<ArrayList<Integer>>();

						fourMoves.add(getMoveFromInt(board, selected, -4));
						selected.setPossibleMoves(fourMoves);
					//}
					break;

				case "5":
					option1.setText("You must move a pawn forward 5 spaces.");
					option2.setText("");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> fiveMoves = new ArrayList<ArrayList<Integer>>();

						fiveMoves.add(getMoveFromInt(board, selected, 5));
						selected.setPossibleMoves(fiveMoves);
					//}

					break;

				case "7":
					option1.setText("You must move a pawn forward 7 spaces.");
					option2.setText("");

					for (Piece p : currentPlayer.getPieces()) {
						ArrayList<ArrayList<Integer>> sevenMoves = new ArrayList<ArrayList<Integer>>();

						sevenMoves.add(getMoveFromInt(board, selected, 7));
						selected.setPossibleMoves(sevenMoves);
					//}
					break;

				case "8":
					option1.setText("You must move a pawn forward 8 spaces.");
					option2.setText("");
					//for (Piece p : blue.getPieces()) {
						ArrayList<ArrayList<Integer>> eightMoves = new ArrayList<ArrayList<Integer>>();

						eightMoves.add(getMoveFromInt(board, selected, 8));
						selected.setPossibleMoves(eightMoves);
					//}

					break;

				case "10":
					option1.setText("You must move a pawn forward 10 spaces.");
					option2.setText("");
					//for (Piece p : blue.getPieces()) {
						ArrayList<ArrayList<Integer>>tenMoves = new ArrayList<ArrayList<Integer>>();

						tenMoves.add(getMoveFromInt(board, selected, 10));
						selected.setPossibleMoves(tenMoves);
					//}

					break;

				case "11":
					option1.setText("You must move a pawn forward 11 spaces.");
					option2.setText("");
					//for (Piece p : blue.getPieces()) {
						ArrayList<ArrayList<Integer>> elevenMoves = new ArrayList<ArrayList<Integer>>();

						elevenMoves.add(getMoveFromInt(board, selected, 11));
						selected.setPossibleMoves(elevenMoves);
					//}

					break;

				case "12":
					option1.setText("You must move a pawn forward 12 spaces.");
					option2.setText("");
					//for (Piece p : blue.getPieces()) {
						ArrayList<ArrayList<Integer>> twelveMoves = new ArrayList<ArrayList<Integer>>();

						twelveMoves.add(getMoveFromInt(board, selected, 12));
						selected.setPossibleMoves(twelveMoves);
					//}

					break;
				case "Sorry":
					option1.setText(currCard.getInfo());
					option2.setText("");
					break;
				}
				System.out.println(selected.getIsInPlay());
				System.out.println(prevPiece.getPossibleMoves());
				System.out.println(selected.getPossibleMoves());


			}
		});

		btnSave.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				boolean isSuccess = save();
				if (isSuccess) {
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(Calendar.getInstance().getTime());
					t1.setText("Succesfully saved the game at " + timeStamp);
				} else {
					t1.setText("Failed to save the game.");
				}
			}
		});

		btnRestore.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				int result = restore();
				if (result == 0) {
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy").format(Calendar.getInstance().getTime());
					t1.setText("Succesfully restore the game at " + timeStamp);
				} else if (result == 1) {
					t1.setText("It seem that you didn't save the game before.");
				} else {
					t1.setText("Failed to restore the game.");
				}
			}
		});
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
					move.set(0, newX);
					move.set(1, newY);
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
		
				 selected = p;
				 if(prevPiece != null && prevPiece.getPossibleMoves() != null){
					 fillInSquares(prevPiece.getPossibleMoves(), Color.LIGHTGRAY, Color.BLACK, 1,
							 pane);
				 }
				 if(prevCircle != null){
					 prevCircle.setStroke(Color.BLACK);
				 }
				 circle.setStroke(Color.WHITE);
		
				 if(selected.getIsInPlay()){
					 if(selected.getPossibleMoves() != null){
						 moves = selected.getPossibleMoves();
						 fillInSquares(moves, Color.LIGHTGRAY, selected.getColor().getColor(),
								 .7, pane);
					 }
				 }else{
					 if(selected.getPossibleMoves() != null){
						 moves = selected.getPossibleMoves();
						 fillInSquares(moves, selected.getColor().getColor(), Color.WHITE, .7,
								 pane);
					 }
				 }
		
		
		
		 //}
		 prevPiece = p;
		 prevCircle = circle;
		 }
		
		 });
	}

	public void drawBoard(GridPane pane, Board board) {

		for (int i = 0; i <= 15; i++) {

			for (int j = 0; j <= 15; j++) {
				Rectangle rectangle;
				rectangle = new Rectangle(45, 45, Color.WHITE);
				pane.add(rectangle, i, j);
				int[][] location = new int[1][2];
				location[0][0] = i;
				location[0][1] = j;
				
				 rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
				 @Override public void handle(MouseEvent event) {
					 if(selected.getPossibleMoves() != null && selected != null){
						 	for(int k = 0; k < selected.getPossibleMoves().size(); k++){
						 		if((selected.getPossibleMoves().get(k).get(0) == location[k][0]) 
						 				&& selected.getPossibleMoves().get(k).get(1) == location[k][1]){
						 			if(!selected.getIsInPlay()) {
						 				selected.setInPlay();
						 			}
//						 			pane.getChildren().remove(prevCircle);
						 			pane.add(prevCircle, location[0][0], location[0][1]);
//						 			prevCircle.setStroke(Color.BLACK);
//						 			
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

}
