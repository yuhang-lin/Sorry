package sorry;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI of the help pane.
 * 
 * @author Austin Batistoni
 */
public class Help {
	Pane currentPane;

	public void start(Stage stage) {
		// ------------------------Root pane-------------------------------
		VBox root = new VBox();
		HBox buttons = new HBox();
		final ToggleGroup group = new ToggleGroup();
		ToggleButton sorryRules = new ToggleButton("Sorry Rules");
		sorryRules.setSelected(true);
		sorryRules.setToggleGroup(group);
		ToggleButton ourRules = new ToggleButton("Our implementation");
		sorryRules.setToggleGroup(group);
		buttons.getChildren().add(sorryRules);
		buttons.getChildren().add(ourRules);
		root.getChildren().add(buttons);

		// ------------------------Sorry rules pane-----------------------------
		Pane sorryRulesPane = new Pane();
		currentPane = sorryRulesPane;
		VBox pane = new VBox(5);
		sorryRulesPane.getChildren().add(pane);
		Text t1 = new Text("Sorry! Rules:");
		t1.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
		t1.setUnderline(true);
		Text t2 = new Text("To Start a Pawn:");
		t2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t3 = new Text("To move a pawn from your START out onto the track, you "
				+ "must draw either a 1 or a 2. If it is a 2, do as it says, then draw "
				+ "again and move if possible.\nYou may not start a pawn out with " + "any other cards!");

		Text t4 = new Text("Jumping and Bumping:");
		t4.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t5 = new Text("You may JUMP over your own or another player’s pawn that’s "
				+ "in your way, counting it as one space. BUT...if you land on a "
				+ "space that’s already occupied\nby an opponent’s pawn, BUMP "
				+ "that pawn back to its own color START space.");

		Text t6 = new Text("Moving Backwards:");
		t6.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t7 = new Text("The 4 and 10 cards move you backward. If you have successfully "
				+ "moved a pawn backward at least two spaces beyond your own "
				+ "START space, you may\non a subsequent turn, move into your "
				+ "own SAFETY ZONE without moving all the way around the " + "board.");

		Text t8 = new Text("The Cards:");
		t8.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t9 = new Text("1- Either start a pawn OR move one pawn forward 1 space.\n"
				+ "2- Either start a pawn OR move one pawn forward 2 spaces. "
				+ "Whichever you do—or even if you couldn’t move—DRAW " + "AGAIN and move accordingly.\n"
				+ "3- Move one pawn forward 3 spaces.\n" + "4- Move one pawn backward 4 spaces.\n"
				+ "5- Move one pawn forward 5 spaces.\n" + "7- Either move one pawn forward 7 spaces—OR split the "
				+ "forward move between any two pawns.\n" + "8- Move one pawn forward 8 spaces.\n"
				+ "10- Either move one pawn forward 10 spaces—OR move one " + "pawn backward 1 space.\n"
				+ "11- Move one pawn forward 11 spaces—OR switch any one of"
				+ "your pawns with one of any opponent’s.\n" + "12- Move one pawn forward 12 spaces.\n"
				+ "SORRY!- Take one pawn from your START, place it on "
				+ "any space that is occupied by any opponent, and BUMP "
				+ "that opponent’s pawn back to its START.\n If there is no "
				+ "pawn on your START or no opponent’s pawn on any " + "space you can move to, you forfeit your move.");

		pane.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9);

		// -----------------------Our Implementation Pane-------------------------//
		Pane ourRulesPane = new Pane();
		VBox box = new VBox(5);
		Text t10 = new Text("Our Implementation:");
		t10.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
		t10.setUnderline(true);
		Text t11 = new Text("Game Play:");
		t11.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t12 = new Text("When it is your turn, you must first draw a card. If there are any available moves, "
				+ "clicking on one of your color pieces will show the available moves for that piece. \nAvailable moves will "
				+ "be shown by the your color highlight over the corresponding board square. If there are available moves, clicking "
				+ "on one of the highlighted \nboardsquares will move your piece to that square.");

		Text t13 = new Text("Sorry Card:");
		t13.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t14 = new Text("If a sorry card is drawn, select one of your pieces in start to show the available moves. "
				+ "All other players' pieces that are in play will light up. Selecting one of these \nspaces will swap "
				+ "your selected piece with the opponent piece and will send the opponent piece back to its start.");

		Text t15 = new Text("Finishing A Piece:");
		t15.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

		Text t16 = new Text(
				"The finished area is denoted by the block of four squares at the end of each color's safe zone. "
						+ "If at any point you draw a card that will bring the selected piece \nexactly to the finish area, one of the "
						+ "finish squares will light up as an available move. Selecting that square will move your piece to finish. Whichever "
						+ "player \nreaches 4 finished pieces first wins the game!");

		box.getChildren().addAll(t10, t11, t12, t13, t14, t15, t16);
		ourRulesPane.getChildren().add(box);

		// ----------------------------Button handlers----------------------------//
		sorryRules.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (currentPane != sorryRulesPane) {
					sorryRules.setSelected(true);
					ourRules.setSelected(false);
					currentPane = sorryRulesPane;
					root.getChildren().remove(ourRulesPane);
					root.getChildren().add(sorryRulesPane);
				} else {
					sorryRules.setSelected(false);
				}
			}
		});

		ourRules.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (currentPane != ourRulesPane) {
					ourRules.setSelected(true);
					sorryRules.setSelected(false);
					currentPane = ourRulesPane;
					root.getChildren().remove(sorryRulesPane);
					root.getChildren().add(ourRulesPane);
				} else {
					ourRules.setSelected(false);
				}
			}
		});

		root.getChildren().add(sorryRulesPane);
		stage.setTitle("Help");
		stage.setWidth(1000);
		stage.setHeight(550);

		final Label label = new Label("Sorry! Help");
		label.setFont(new Font("Arial", 20));
		Scene scene = new Scene(root, 1200, 735);
		stage.setScene(scene);
		stage.show();
	}
}
