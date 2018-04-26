package sorry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Game logic and GUI for the game board.
 * 
 * @author Austin Batistoni, Yuhang Lin
 */
public class Main extends Application {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	Card currCard;
	Piece selected;
	Piece prevPiece;
	Circle selectedCircle;
	ArrayList<ArrayList<Integer>> moves;
	Deck deck = new Deck();
	String logFile = "game_status.txt";
	int currentTurn = 0; // current turn of the game
	Board board;
	GridPane pane;
	VBox sideBar;
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
	 * @return true if success, false otherwise
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
			initBoard(stage);
			resetText();
		} catch (FileNotFoundException e) {
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		return 0;
	}

	/**
	 * Initialize the game board GUI and corresponding data structure.
	 * 
	 * @param stage
	 */
	private void initBoard(Stage stage) {
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

	/**
	 * Add all players of the game.
	 */
	private void setPlayers() {
		Computer blue = new Computer(new Blue(), Computer.NiceLevel.MEAN, Computer.SmartLevel.SMART); // for testing
		// Player blue = new Player(new Blue());
		Player green = new Player(new Green());
		Player red = new Player(new Red());
		Player yellow = new Player(new Yellow());
		players.add(blue);
		players.add(yellow);
		//players.add(green);
		//players.add(red);
	}

	@Override
	public void start(Stage primaryStage) {
		HBox root = new HBox();
		pane = new GridPane();
		board = new Board();

		sideBar = new VBox(20);
		sideBar.setPrefWidth(1100);
		sideBar.setStyle("-fx-background-color: rgba(0, 0, 0, .1);");

		primaryStage.setTitle("Sorry!");
		sideBar.getChildren().add(sorryTitleEffect());
		drawBoard(pane, board);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

		turnText = new Text("Blue player's turn");
		turnText.setEffect(ds);
		turnText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		turnText.setFill(Color.BLUE);
		turnText.setFont(new Font(20));
		sideBar.getChildren().add(turnText);
		Button btnDraw = new Button("Draw");
		btnDraw.setMaxWidth(200);

		sideBar.getChildren().add(btnDraw);

		HBox buttonGroup = new HBox();

		Button btnSave = new Button("Save game");
		Button btnLeaderBoard = new Button("Leader Board");
		Button btnRestore = new Button("Resume game");
		buttonGroup.getChildren().addAll(btnSave, btnLeaderBoard, btnRestore);
		sideBar.getChildren().add(buttonGroup);
		sideBar.getChildren().add(new CardPane(new Card("Draw")));
		directions = new Text("Please draw a card.");
		sideBar.getChildren().add(directions);
		option1 = new Text("");
		option2 = new Text("");
		sideBar.getChildren().add(option1);
		sideBar.getChildren().add(option2);

		Button btnHelp = new Button("Help");
		sideBar.getChildren().add(btnHelp);
		sideBar.setAlignment(Pos.CENTER);

		setPlayers();
		initBoard(primaryStage);

		root.getChildren().add(sideBar);
		root.getChildren().add(pane);

		Scene scene = new Scene(root, 1030, 735);
		scene.setFill(Color.WHITE);
		primaryStage.setScene(scene);
		primaryStage.show();

		//--------------------Button Event Handlers-----------------------//
		btnHelp.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Help help = new Help();
				help.start(new Stage());
			}
		});

		btnDraw.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hasDrawn = true;
				sorryCard = false;
				currCard = deck.draw();
				String card = currCard.getName();
				directions.setText("The card is: " + currCard.getName());
				sideBar.getChildren().remove(4);
				sideBar.getChildren().add(4, (new CardPane(currCard)));

				directions.setText("The card is: " + card);
				cardToMoves(card);
				Player currentPlayer = players.get(currentTurn);
				if (currentPlayer instanceof Computer) {
					System.out.println("Computer got the card " + card);
					Choice choice = ((Computer) currentPlayer).getSelectedChoice(getPiecesOnBoard());
					if (choice != null) {
						Piece piece = choice.getPiece();
						ArrayList<ArrayList<Integer>> nextLocation = new ArrayList<>();
						nextLocation.add(choice.getMove());
						selected = piece;
						selectedCircle = selected.getCircle();
						movePiece(nextLocation);
					} else {
						nextTurn();
					}
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

	/**
	 * Generate title effect.
	 * 
	 * @return the title effect
	 */
	private HBox sorryTitleEffect() {
		HBox text = new HBox();

		Text s = new Text("S");
		s.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		DropShadow red = new DropShadow();
		red.setOffsetY(3.0f);
		red.setColor(Color.RED);
		s.setEffect(red);

		Text o = new Text("o");
		o.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		DropShadow orange = new DropShadow();
		orange.setOffsetY(3.0f);
		orange.setColor(Color.ORANGE);
		o.setEffect(orange);

		Text r = new Text("r");
		r.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		DropShadow yellow = new DropShadow();
		yellow.setOffsetY(3.0f);
		yellow.setColor(Color.YELLOW);
		r.setEffect(yellow);

		Text r2 = new Text("r");
		r2.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		DropShadow green = new DropShadow();
		green.setOffsetY(3.0f);
		green.setColor(Color.GREEN);
		r2.setEffect(green);

		Text y = new Text("y");
		y.setFont(Font.font("Verdana", FontWeight.BOLD, 80));
		DropShadow blue = new DropShadow();
		blue.setOffsetY(3.0f);
		blue.setColor(Color.BLUE);
		y.setEffect(blue);

		Text exc = new Text("!");
		exc.setFont(Font.font("Verdana", FontWeight.BOLD, 85));
		DropShadow ds5 = new DropShadow();
		ds5.setOffsetY(3.0f);
		ds5.setColor(Color.PURPLE);
		exc.setEffect(ds5);

		text.getChildren().addAll(s, o, r, r2, y, exc);

		return text;
	}

	/**
	 * Calculate all possible next moves based on the given card name.
	 * 
	 * @param card a String representing the card name
	 */
	private void cardToMoves(String card) {
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
				option1.setText("You must move a pawn forward 10 \nspaces, or move backward 1 space.");
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
				option1.setText("You may BUMP any opponent's \npawn with your pawn from START");
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

	/**
	 * Set the possible moves for each piece of the current player.
	 * 
	 * @param moveNumList list of available movement number
	 */
	private void setPossibleMoves(ArrayList<Integer> moveNumList) {
		selfPieceSet.clear();
		Player currentPlayer = players.get(currentTurn);
		for (Piece p : currentPlayer.getPieces()) {
			selfPieceSet.add(p.getLocation().get(0).toString());
		}
		for (Piece p : currentPlayer.getPieces()) {
			ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
			for (int cardNum : moveNumList) {
				ArrayList<Integer> move = new ArrayList<>();
				if (cardNum >= 1 && cardNum <= 5) {
					//Can the piece finish with the given move
					if (canPieceFinish(p, cardNum)) {
						//p.getPlayer().addFinishedPieces();
						move = p.getColor().getHomeCoords().get(p.getHomeIndex());
					}
					//Is the piece already in the safety zone?
					if (p.isPieceSafe()) {
						//Can the piece move within the safety zone?
						if (canMoveWithinSafe(p, cardNum)) {
							move = moveWithinSafeZone(p, cardNum);
						}
					} else {
						if (p.getIsInPlay()) {
							//Can the piece move to the safety zone from the main path?
							if (canMoveToSafe(p, cardNum)) {
								move = getSafeLocation(p, cardNum);
							} else {
								move = getMoveFromInt(board, p, cardNum);
							}
						} else if (cardNum >= 1 && cardNum <= 2) { // Start a pawn
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

	/**
	 * Returns true if piece can finish the game, false otherwise
	 * 
	 * @param p
	 * @param card
	 * @return true if piece can finish, false otherwise
	 */
	public boolean canPieceFinish(Piece p, int card) {
		if (p.isPieceSafe()) {
			int i = getIndexOfSafeArray(p);
			if (i + card == 5) {
				//p.setCanGoHome();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if piece can move within safe zone, false otherwise.
	 * 
	 * @param p- piece to be moved
	 * @param card- number representation of the card
	 * @return true if piece can move within safe zone, false otherwise
	 */
	public boolean canMoveWithinSafe(Piece p, int card) {
		int i = getIndexOfSafeArray(p);
		if (i + card > p.getColor().getSafeCoords().size() - 1) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the index of safe array.
	 * 
	 * @param p- piece to move
	 * @return index of the safe array of the piece
	 */
	public int getIndexOfSafeArray(Piece p) {
		for (int i = 0; i < p.getColor().getSafeCoords().size(); i++) {
			if (p.getColor().getSafeCoords().get(i).get(0) == p.getLocation().get(0).get(0)
					&& p.getColor().getSafeCoords().get(i).get(1) == p.getLocation().get(0).get(1)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Returns the next location within the safe zone of the piece given the card.
	 * 
	 * @param p- the target piece for the next move
	 * @param card- the numerical presentation of the card
	 * @return the next location within the safe zone of the piece given the card
	 */
	public ArrayList<Integer> moveWithinSafeZone(Piece p, int card) {
		ArrayList<Integer> newLocation = new ArrayList<Integer>();
		int i = getIndexOfSafeArray(p);
		newLocation = p.getColor().getSafeCoords().get(i + card);
		return newLocation;
	}

	/**
	 * Returns true if the piece can move to home given the card
	 * 
	 * @param p- the target piece for the next move
	 * @param card- the numerical presentation of the card
	 * @return true if the piece can move to home given the card, false otherwise
	 */
	public boolean canMoveToSafe(Piece p, int card) {
		int currentIndex = Board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = Board.getPathIndex(p.getColor().getLastSpot().get(0));
		if (p.getColor() instanceof Blue && currentIndex >= 10) {

			playersLastSpot += 60;
		}
		if ((currentIndex <= playersLastSpot && currentIndex + card > playersLastSpot)
				&& (card - (playersLastSpot - currentIndex) - 1) < p.getColor().getSafeCoords().size()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the piece can move to home given the card.
	 * 
	 * @param p- the target piece for the next move
	 * @param card- the numerical presentation of the card
	 * @return true if the piece can move to home given the card, false otherwise
	 */
	public boolean canMoveToHome(Piece p, int card) {
		int currentIndex = Board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = Board.getPathIndex(p.getColor().getLastSpot().get(0));
		if (p.getColor() instanceof Blue) {
			playersLastSpot += 60;
		}
		if ((currentIndex <= playersLastSpot && currentIndex + card > playersLastSpot)
				&& (card - (playersLastSpot - currentIndex) - 1) == p.getColor().getSafeCoords().size()) {
			//p.setCanGoHome();
			return true;
		}
		return false;
	}

	/**
	 * Gets the safe zone location of the piece based on the card
	 * 
	 * @param p- the target piece for the next move
	 * @param card- the numerical presentation of the card
	 * @return ArrayList of new location
	 */
	public ArrayList<Integer> getSafeLocation(Piece p, int card) {
		int currentIndex = Board.getPathIndex(p.getLocation().get(0));
		int playersLastSpot = Board.getPathIndex(p.getColor().getLastSpot().get(0));

		ArrayList<Integer> newLocation = new ArrayList<Integer>();

		ArrayList<ArrayList<Integer>> playersSafeArray = p.getColor().getSafeCoords();
		if (p.getColor() instanceof Blue && currentIndex >= 10) {

			playersLastSpot += 60;
		}
		newLocation = playersSafeArray.get(card - (playersLastSpot - currentIndex) - 1);

		return newLocation;
	}

	/**
	 * Reset the colors of each square.
	 */
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
			}
		}
		return piecesOnBoard;
	}

	/**
	 * Switch to the next turn.
	 */
	public void nextTurn() {
		Player currentPlayer = players.get(currentTurn);
		sideBar.getChildren().remove(4);
		sideBar.getChildren().add(4, new CardPane(new Card("Draw")));
		DisplayWinner d = new DisplayWinner(currentPlayer);
		d.Start(new Stage());
		//If the player has 4 pieces home they win!!
		if (currentPlayer.getPiecesHome() == Player.getNumPieces()) {
			
			directions.setText("Player" + currentPlayer.getPlayerColor() + "wins!");
			endGame();
		}
		currentTurn = (currentTurn + 1) % players.size();
		resetText();
	}

	/**
	 * Reset the texts on the screen.
	 */
	private void resetText() {

		switch (currentTurn) {
		case 0:
			turnText.setText("Blue player's turn");
			turnText.setFill(Color.BLUE);
			break;
		case 1:
			turnText.setText("Yellow player's turn");
			turnText.setFill(Color.YELLOW);

			break;
		case 2:
			turnText.setText("Green player's turn");
			turnText.setFill(Color.GREEN);
			break;
		case 3:
			turnText.setText("Red player's turn");
			turnText.setFill(Color.RED);
		}
		option1.setText("");
		option2.setText("");
		directions.setText("Please draw a card.");
	}

	/**
	 * Gets the next location based on the given increment.
	 * 
	 * @param board- the game board
	 * @param piece-the target piece for the next move
	 * @param increment- the numerical increment of the next move
	 * @return the next location
	 */
	public ArrayList<Integer> getMoveFromInt(Board board, Piece piece, int increment) {
		ArrayList<Integer> move = new ArrayList<Integer>();
		for (int i = 0; i < board.getPathCoords().size(); i++) {
			if (piece.getLocation().get(0).get(0) == board.getPathCoords().get(i).get(0)
					&& piece.getLocation().get(0).get(1) == board.getPathCoords().get(i).get(1)) {
				int boardIndex = Math.floorMod((i + increment), board.getPathLength());
				int newX = board.getPathCoords().get(boardIndex).get(0);
				int newY = board.getPathCoords().get(boardIndex).get(1);
				move.add(newX);
				move.add(newY);
				return move;
			}
		}
		return move;
	}

	/**
	 * Fills in the squares with different color and effects.
	 * 
	 * @param grid- the internal representation of the grid
	 * @param inside- the inside color of the square
	 * @param outside- the outside color of the square
	 * @param radius- the radius of the effect circle
	 * @param pane- the pane containing the grid
	 */
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

	/**
	 * Gets the node from the grid pane
	 * 
	 * @param gridPane- the grid pane of the game
	 * @param col- column in the grid pane
	 * @param row- row in the grid pane
	 * @return
	 */
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Draws piece in the GUI.
	 * 
	 * @param p- piece to draw
	 * @param primaryStage- stage used to be shown the pane
	 * @param pane- the pane for having the board
	 * @param board- the game board
	 * @param i home index of the piece
	 */
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

	/**
	 * Draws the GUI of the game board.
	 * 
	 * @param pane- the pane to draw the board
	 * @param board- the game board
	 */
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
						movePiece(location);
					}

				});

			}
		}
		fillInSquares(board.getPathCoords(), Color.LIGHTGRAY, Color.BLACK, 1, pane);
	}

	/**
	 * Move the selected piece to the target location
	 * 
	 * @param location- the target location of the selected piece
	 */
	private void movePiece(ArrayList<ArrayList<Integer>> location) {
		returnSquareColor();
		if (location == null || location.size() == 0) {
			return;
		}
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
					if ((selected.getPossibleMoves().get(k).get(0) == location.get(0).get(0))
							&& selected.getPossibleMoves().get(k).get(1) == location.get(0).get(1)) {

						// Check if it can bump other players' pawns
						if (otherPieceMap.containsKey(location.toString())) {
							removeBumpedPiece(location);
						}
						ArrayList<ArrayList<Integer>> homeCoords = selected.getPlayer().getPlayerColor().getHomeCoords();
						
						if(homeCoords.get(selected.getHomeIndex()).get(0) == location.get(0).get(0) &&
								homeCoords.get(selected.getHomeIndex()).get(1) == location.get(0).get(1)) {
							selected.getPlayer().addFinishedPieces();
						}
							
						for (int i = 0; i < selected.getColor().getSafeCoords().size(); i++) {
							if ((location.get(0).get(0) == selected.getColor().getSafeCoords().get(i).get(0))
									&& (location.get(0).get(1) == selected.getColor().getSafeCoords().get(i).get(1))) {
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
							if (selected.getLocation().get(0).get(0) == selected.getColor().getSafeCoords().get(i)
									.get(0)
									&& selected.getLocation().get(0).get(1) == selected.getColor().getSafeCoords()
											.get(i).get(1)) {
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

	/**
	 * Removes a piece from start.
	 * 
	 * @param p- piece to be removed
	 * @param pane- Pane of the game board
	 * @param circle- the circle of the piece in the GUI
	 */
	public void moveFromStart(Piece p, GridPane pane, Circle circle) {
		ArrayList<ArrayList<Integer>> firstSpot = p.getColor().getFirstSpot();
		pane.getChildren().remove(circle);
		pane.add(circle, firstSpot.get(0).get(0), firstSpot.get(0).get(1));
		p.setLocation(firstSpot);
		p.setInPlay();
		circle.setStroke(Color.BLACK);
	}


	/**
	 * Removes the other piece from the location.
	 * 
	 * @param location-  the location for pumping
	 */
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

	/**
	 * Get bumped piece based on the given location
	 * 
	 * @param location- the location of bumping
	 * @return bumped Piece if found, otherwise null
	 */
	public Piece getBumpedPiece(ArrayList<ArrayList<Integer>> location) {
		for (Piece piece : getPiecesOnBoard()) {
			if (piece.getLocation() == location) {
				return piece;
			}
		}
		return null;
	}

	private void displayWinner(Player p) {
		
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
