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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.HBox;
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
import javafx.util.Duration;

/**
 * @author Yuhang Lin, Austin Batistoni
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
	int currentTurn = 0; // current turn of the game
	Board board;
	GridPane pane;

	String userName = "SorryUser";

	ArrayList<Player> players = new ArrayList<>();
	
	HashMap<String, Piece> otherPieceMap = new HashMap<>();
	HashSet<String> selfPieceSet = new HashSet<>();

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
			printWriter.println();
			printWriter.println(deck.getNumUsed());
			printWriter.println(currentTurn);
			printWriter.println(players.size());
			// Save location of all pieces
			for (Player player : players) {
				printWriter.println(player.getPlayerColor());
				if (player instanceof Computer) {
					printWriter.println("computer");
				} else {
					printWriter.println("user");
				}
				for (Piece piece : player.getPieces()) {
					ArrayList<ArrayList<Integer>> location = piece.getLocation();
					for (ArrayList<Integer> point : location) {
						printWriter.println(point.get(0) + "," + point.get(1));
					}
				}
			}
			printWriter.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Resume the game status from a text file.
	 * 
	 * @return 0 if there is no error, 1 if file is not found, 2 otherwise.
	 */
	public int resume(Stage stage) {
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
			currentTurn = Integer.parseInt(br.readLine());
			int numPlayer = Integer.parseInt(br.readLine());
			ArrayList<Player> newPlayers = new ArrayList<>();
			for (int i = 0; i < numPlayer; i++) {
				String colorName = br.readLine();
				PieceColor color = null;
				switch (colorName.toLowerCase()) {
				case "blue":
					color = new Blue();
					break;
				case "red":
					color = new Red();
					break;
				case "yellow":
					color = new Yellow();
					break;
				case "green":
					color = new Green();
					break;
				default:
					color = new Blue();
					break;
				}
				String playerSetting = br.readLine();
				Player player = null;
				if (playerSetting.equals("user")) {
					player = new Player(color);
				} else {
					player = new Computer(color);
				}
				Piece[] pieceArray = new Piece[Player.getNumPieces()];
				for (int j = 0; j < Player.getNumPieces(); j++) {
					ArrayList<ArrayList<Integer>> location = new ArrayList<>();
					String[] indices = br.readLine().split(",");
					ArrayList<Integer> point = new ArrayList<>();
					for (String indexPoint : indices) {
						point.add(Integer.parseInt(indexPoint));
					}
					location.add(point);
					Piece piece = new Piece(color, player, location, j);
					pieceArray[j] = piece;
				}
				player.setPieceArray(pieceArray);
				newPlayers.add(player);
			}
			players = newPlayers;
			resetBoard(stage);
			resetText();
		} catch (FileNotFoundException e) {
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		return 0;
	}

	private void resetBoard(Stage stage) {
		for (int i = 0; i < players.size(); i++) {
			Piece[] pieces = players.get(i).getPieces();
			for (int j = 0; j < pieces.length; j++) {
				drawPiece(pieces[j], stage, pane, board, j);
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
	}

	private void setPlayers() {
		Computer blue = new Computer(new Blue(), Computer.NiceLevel.MEAN, Computer.SmartLevel.SMART); // for testing
																										// only
		Player green = new Player(new Green());
		Player red = new Player(new Red());
		Player yellow = new Player(new Yellow());
		players.add(blue);
		players.add(yellow);
		players.add(green);
		players.add(red);
		/*
		 * if (players.get(0) instanceof Computer) { System.out.println("hi"); }
		 */
		// ((Computer)players.get(0)).getSelectedChoice();
	}

	@Override
	public void start(Stage primaryStage) {
		pane = new GridPane();
		board = new Board();

		primaryStage.setTitle("Sorry!");
		Text t = new Text(0, 0, "SORRY!");
		t.setFont(new Font(30));
		pane.add(t, 20, 0);
		drawBoard(pane, board);
		// t.setRotate(45);

		Button btnDraw = new Button("Draw");
		pane.add(btnDraw, 20, 2);
		
		HBox buttonGroup = new HBox();
		
		Button btnSave = new Button("Save game");
		Button btnLeaderBoard = new Button("Leader Board");
		Button btnRestore = new Button("Resume game");
		buttonGroup.getChildren().addAll(btnSave,btnLeaderBoard,btnRestore);
		pane.add(buttonGroup, 20, 12);

		directions = new Text("Please draw a card.");
		pane.add(directions, 20, 5);
		option1 = new Text("");
		option2 = new Text("");
		pane.add(option1, 20, 6);
		pane.add(option2, 20, 7);

		turnText = new Text("Blue player's turn");
		turnText.setFill(Color.BLUE);
		pane.add(turnText, 20, 1);
		
		Button showHelp = new Button("Help");
		pane.add(showHelp, 20, 10);
		
		showHelp.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Help help = new Help();
				help.start(new Stage());
				
			}
		});

		setPlayers();
		resetBoard(primaryStage);
		

		Scene scene = new Scene(pane, 1100, 735);
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

				Player currentPlayer = players.get(currentTurn);
				
				switch (card) {
				case "1":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 1 space.");
					setPossibleMoves(new ArrayList<Integer>(Arrays.asList(1)));
					break;

				case "2":
					option1.setText("You can move a pawn to the start square.");
					option2.setText("You can move a pawn forward 2 spaces.");
					setPossibleMoves(new ArrayList<Integer>(Arrays.asList(2)));
					break;

				case "3":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 3 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(3)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "4":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn backwards 4 spaces.");
						option2.setText("");

						// TODO: IMPLEMENT moving backwards out of safe array with a 4
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(-4)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}
					break;

				case "5":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 5 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(5)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "7":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 7 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(7)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}
					break;

				case "8":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 8 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(8)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "10":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 10 spaces, or move backward 1 space.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(10, -1)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "11":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 11 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(11)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;

				case "12":
					if (currentPlayer.hasPiecesOnBoard()) {
						option1.setText("You must move a pawn forward 12 spaces.");
						option2.setText("");
						setPossibleMoves(new ArrayList<Integer>(Arrays.asList(12)));
					} else {
						option1.setText("Unable to move this turn!");
						option2.setText("");
					}

					break;
				case "Sorry":
					ArrayList<ArrayList<Integer>> sorryMoves = new ArrayList<ArrayList<Integer>>();
					ArrayList<Piece> piecesOnBoard = getPiecesOnBoard();
					if (!piecesOnBoard.isEmpty()) {
						option1.setText(currCard.getInfo());
						option2.setText("Select a piece to bump.");
						sorryCard = true;

						for (Piece p : piecesOnBoard) {
							if (p.getIsInPlay()) {
								int xLoc = p.getLocation().get(0).get(0);
								int yLoc = p.getLocation().get(0).get(1);

								sorryMoves.add(new ArrayList<Integer>(Arrays.asList(xLoc, yLoc)));
								System.out.println(xLoc + ", " + yLoc);
							}
						}

						for (Piece p : currentPlayer.getPieces()) {
							p.setPossibleMoves(sorryMoves);
						}

					} else {
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
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy")
							.format(Calendar.getInstance().getTime());
					directions.setText("Succesfully saved the game at " + timeStamp);
				} else {
					directions.setText("Failed to save the game.");
				}
			}
		});

		btnRestore.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				int result = resume(primaryStage);
				if (result == 0) {
					String timeStamp = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy")
							.format(Calendar.getInstance().getTime());
					directions.setText("Succesfully restore the game at " + timeStamp);
				} else if (result == 1) {
					directions.setText("It seems that you didn't save the game before.");
				} else {
					directions.setText("Failed to restore the game.");
				}
			}
		});

		btnLeaderBoard.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Leaderboard leaderBoard = new Leaderboard();
				leaderBoard.start(new Stage());
			}
		});
	}
	


	private void setPossibleMoves(ArrayList<Integer> moveNumList) {
		selfPieceSet.clear();
		Player currentPlayer = players.get(currentTurn);
		for (Piece p: currentPlayer.getPieces()) {
			selfPieceSet.add(p.getLocation().toString());
		}
		for (Piece p : currentPlayer.getPieces()) {
			ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
			for (int cardNum : moveNumList) {
				ArrayList<Integer> move = new ArrayList<>();
				if (cardNum >= 1 && cardNum <= 5) {
					if (canPieceFinish(p, cardNum)) {
						p.getPlayer().addFinishedPieces();
						move = p.getColor().getHomeCoords().get(p.getHomeIndex());
					}
					if (p.isPieceSafe()) {
						if (canMoveWithinSafe(p, cardNum)) {
							move = moveWithinSafeZone(p, cardNum);
						}
					} else {
						if (p.getIsInPlay()) {
							if (canMoveToSafe(p, cardNum)) {
								move = getSafeLocation(p, cardNum);
							} else {
								move = getMoveFromInt(board, p, cardNum);
							}
						} else if (cardNum >= 1 && cardNum <= 2){ // Start a pawn
							ArrayList<Integer> temp = new ArrayList<Integer>();
							temp.add(p.getColor().getFirstSpot().get(0).get(0));
							temp.add(p.getColor().getFirstSpot().get(0).get(1));
							move = temp;
						}
					}
				} else {
					if (cardNum >= 6 && canMoveToHome(p, cardNum)) {
						move = p.getColor().getHomeCoords().get(p.getHomeIndex());
					} else {
						if (canMoveToSafe(p, cardNum)) {
							move = getSafeLocation(p, cardNum);
						} else {
							move = getMoveFromInt(board, p, cardNum);
						}
					}
				}
				if (!selfPieceSet.contains(move.toString())) {
					moves.add(move);
				}
			}
			p.setPossibleMoves(moves);
		}
	}

	public boolean canPieceFinish(Piece p, int card) {
		if (p.isPieceSafe()) {
			int i = getIndexOfSafeArray(p);
			if (i + card == 5) {
				return true;
			}
		} else {
			// TODO: Implement logic for a piece going home from outside of safe array
		}
		return false;
	}

	public boolean canMoveWithinSafe(Piece p, int card) {
		int i = getIndexOfSafeArray(p);
		if (i + card > p.getColor().getSafeCoords().size() - 1) {
			return false;
		}
		return true;
	}

	public int getIndexOfSafeArray(Piece p) {
		for (int i = 0; i < p.getColor().getSafeCoords().size(); i++) {
			if (p.getColor().getSafeCoords().get(i).get(0) == p.getLocation().get(0).get(0)
					&& p.getColor().getSafeCoords().get(i).get(1) == p.getLocation().get(0).get(1)) {
				return i;
			}
		}
		return 0;
	}

	public ArrayList<Integer> moveWithinSafeZone(Piece p, int card) {
		ArrayList<Integer> newLocation = new ArrayList<Integer>();
		int i = getIndexOfSafeArray(p);
		newLocation = p.getColor().getSafeCoords().get(i + card);
		return newLocation;
	}

	public boolean canMoveToSafe(Piece p, int card) {
		int currentIndex = board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = board.getPathIndex(p.getColor().getLastSpot().get(0));
		if (p.getColor() instanceof Blue && currentIndex >= 10) {

			playersLastSpot += 60;
		}
		if ((currentIndex <= playersLastSpot && currentIndex + card > playersLastSpot)
				&& (card - (playersLastSpot - currentIndex) - 1) < p.getColor().getSafeCoords().size()) {
			// p.setOutOfPlay();
			return true;
		}
		return false;
	}

	public boolean canMoveToHome(Piece p, int card) {
		int currentIndex = board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = board.getPathIndex(p.getColor().getLastSpot().get(0));
		if (p.getColor() instanceof Blue) {
			playersLastSpot += 60;
		}
		if ((currentIndex <= playersLastSpot && currentIndex + card > playersLastSpot)
				&& (card - (playersLastSpot - currentIndex) - 1) == p.getColor().getSafeCoords().size()) {
			// p.setOutOfPlay();
			return true;
		}
		return false;
	}

	public ArrayList<Integer> getSafeLocation(Piece p, int card) {
		int currentIndex = board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = board.getPathIndex(p.getColor().getLastSpot().get(0));

		ArrayList<Integer> newLocation = new ArrayList<Integer>();

		ArrayList<ArrayList<Integer>> playersSafeArray = p.getColor().getSafeCoords();
		if (p.getColor() instanceof Blue && currentIndex >= 10) {

			playersLastSpot += 60;
		}
		newLocation = playersSafeArray.get(card - (playersLastSpot - currentIndex) - 1);

		return newLocation;
	}

	public void returnSquareColor() {
		fillInSquares(board.getPathCoords(), Color.LIGHTGRAY, Color.BLACK, 1, pane);
		for (int i = 0; i < players.size(); i++) {
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

	}

	/**
	 * Get all the pieces on board excluding the current player's pieces.
	 * 
	 * @return an ArrayList of pieces on board excluding the current player's
	 *         pieces.
	 */
	public ArrayList<Piece> getPiecesOnBoard() {
		ArrayList<Piece> piecesOnBoard = new ArrayList<Piece>();
		for (int i = 0; i < players.size(); i++) {
			if (i == currentTurn) {
				continue;
			}

			for (Piece piece : players.get(i).getPieces()) {
				for (ArrayList<Integer> location : board.getPathCoords()) {

					if (piece.getLocation().get(0).get(0) == location.get(0)
							&& piece.getLocation().get(0).get(1) == location.get(1)) {
						System.out.println(location);
						piecesOnBoard.add(piece);
					}
				}

				// if (piece.getIsInPlay()) {
				// piecesOnBoard.add(piece);
				// }
			}
		}
		return piecesOnBoard;
	}

	/**
	 * Switch to the next turn.
	 */
	public void nextTurn() {
		Player currentPlayer = players.get(currentTurn);
		if (currentPlayer.getPiecesHome() == 4) {
			directions.setText("Player" + currentPlayer.getPlayerColor() + "wins!");
			endGame();
		}
		if (currentTurn == 3) {
			currentTurn = 0;
		} else {
			currentTurn++;
		}
		resetText();
	}

	/**
	 * Reset the texts on the screen.
	 */
	private void resetText() {
		switch (currentTurn) {
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
					int boardIndex = Math.floorMod((i + increment), board.getPathLength());
					int newX = board.getPathCoords().get(boardIndex).get(0);
					int newY = board.getPathCoords().get(boardIndex).get(1);
					move.add(newX);
					move.add(newY);
				}
			}
		}
		return move;
	}

	public void fillInSquares(ArrayList<ArrayList<Integer>> grid, Color inside, Color outside, double radius,
			GridPane pane) {
		if (grid == null) {
			return;
		}
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
		// p.setLocation(moves);
		Circle circle = new Circle();
		// c.setFill(p.getPlayerColor().getColor());
		circle.setStroke(Color.BLACK);
		circle.setRadius(10.0f);
		circle.setFill(p.getColor().getColor().deriveColor(0, 1, 10, 1));
		GridPane.setHalignment(circle, HPos.CENTER);
		GridPane.setValignment(circle, VPos.CENTER);
		pane.add(circle, p.getLocation().get(0).get(0), p.getLocation().get(0).get(1));

		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				returnSquareColor();
				if (p.getColor() == (players.get(currentTurn)).getPlayerColor()) {
					if (selectedCircle != null) {
						selectedCircle.setStroke(Color.BLACK);
					}
					circle.setStroke(Color.WHITE);
				}
			}
		});

		circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (p.getColor() == (players.get(currentTurn)).getPlayerColor()) {
					if (sorryCard) {
						if (!p.getIsInPlay()) {
							selected = p;
							selectedCircle = circle;
							fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, selected.getColor().getColor(),
									.7, pane);
							// sorryCard = false;
						}
					} else {
						if (hasDrawn) {
							selected = p;
							selectedCircle = circle;

							fillInSquares(selected.getPossibleMoves(), Color.LIGHTGRAY, selected.getColor().getColor(),
									.7, pane);
						}
					}
				}

			}

		});
		p.setCircle(circle);
	}

	public void drawBoard(GridPane pane, Board board) {

		for (int i = 0; i <= 15; i++) {

			for (int j = 0; j <= 15; j++) {
				Rectangle rectangle;
				rectangle = new Rectangle(45, 45, Color.WHITE);
				pane.add(rectangle, i, j);
				ArrayList<ArrayList<Integer>> location = new ArrayList<ArrayList<Integer>>();
				location.add(new ArrayList<Integer>(Arrays.asList(i, j)));

				rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						returnSquareColor();
						otherPieceMap.clear();
						for (Piece piece : getPiecesOnBoard()) {
							otherPieceMap.put(piece.getLocation().toString(), piece);
						}
						if (sorryCard) {
							Piece piece = getBumpedPiece(location);
							if (piece != null) {
								removeBumpedPiece(location);
								pane.add(selectedCircle, location.get(0).get(0), location.get(0).get(1));
								// returnSquareColor();
								selectedCircle.setStroke(Color.BLACK);
								selected.setLocation(location);
								selected.setInPlay();
								sorryCard = false;
								nextTurn();
							}
						} else {
							if (selected != null && selected.getPossibleMoves() != null) {
								for (int k = 0; k < selected.getPossibleMoves().size(); k++) {
									if ((selected.getPossibleMoves().get(k).get(0) == location.get(k).get(0))
											&& selected.getPossibleMoves().get(k).get(1) == location.get(k).get(1)) {

										// Check if it can bump other players' pawns
										if (otherPieceMap.containsKey(location.toString())) {
											Piece bumped = otherPieceMap.get(location.toString());
											removeBumpedPiece(location);
										}
										for (int i = 0; i < selected.getColor().getSafeCoords().size(); i++) {
											if ((location.get(k).get(0) == selected.getColor().getSafeCoords().get(i)
													.get(0))
													&& (location.get(k).get(1) == selected.getColor().getSafeCoords()
															.get(i).get(1))) {
												selected.setOutOfPlay();
												System.out.println("Out of play");

											}
										}

										pane.getChildren().remove(selectedCircle);
										pane.add(selectedCircle, location.get(0).get(0), location.get(0).get(1));
										if (!selected.getIsInPlay() && !selected.isPieceSafe()) {
											selected.setInPlay();
										}
										selected.setLocation(location);
										boolean safe = false;
										for (int i = 0; i < selected.getColor().getSafeCoords().size(); i++) {

											if (selected.getLocation().get(0).get(0) == selected.getColor()
													.getSafeCoords().get(i).get(0)
													&& selected.getLocation().get(0).get(1) == selected.getColor()
															.getSafeCoords().get(i).get(1)) {
												safe = true;
											}
										}
										selectedCircle.setStroke(Color.BLACK);

										nextTurn();
									}
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
	
	private void removeBumpedPiece(ArrayList<ArrayList<Integer>> location) {
		Piece piece = getBumpedPiece(location);
		if (piece == null) {
			return;
		}
		Node node = null;
		for (Node n : pane.getChildren()) {
			if (n instanceof Circle && GridPane.getColumnIndex(n) == location.get(0).get(0)
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
	}

	public Piece getBumpedPiece(ArrayList<ArrayList<Integer>> location) {
		for (Piece piece : getPiecesOnBoard()) {
			if (piece.getLocation() == location) {
				return piece;
			}
		}
		return null;
	}

	/**
	 * Save record of this game to MySQL database
	 */
	private void endGame() {
		String sqlQuery = "";
		try (Connection mysqlConn = MysqlConnect.myConnect(); Statement statement = mysqlConn.createStatement()) {
			sqlQuery = "INSERT INTO `player`(`name`) VALUES (?)";
			PreparedStatement preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setString(1, userName);
			try {
				preStatement.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {
				// The record already exists which can be ignored
			}
			// Get user id
			sqlQuery = "SELECT id FROM `player` where name = ?";
			preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setString(1, userName);
			ResultSet myResult = preStatement.executeQuery();
			int userId = 0;
			while (myResult.next()) {
				userId = myResult.getInt("id");
			}
			sqlQuery = String.format("UPDATE player SET last_game = now() WHERE id = %d", userId);
			statement.executeUpdate(sqlQuery);
			String pc1 = "nice & smart";
			String pc2 = "mean & smart";
			String pc3 = "nice & smart";
			String color = "red";
			String result = "win";
			// Add record
			sqlQuery = "INSERT INTO `record` (`player`, `pc1`, `pc2`, `pc3`, `color`, `result`) VALUES (?, ?, ?, ?, ?, ?)";
			preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setInt(1, userId);
			preStatement.setString(2, pc1);
			preStatement.setString(3, pc2);
			preStatement.setString(4, pc3);
			preStatement.setString(5, color);
			preStatement.setString(6, result);
			preStatement.executeUpdate();
		} catch (SQLException e) {// Catch exception if any
			System.out.println("SQL-> " + sqlQuery.toString());
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
