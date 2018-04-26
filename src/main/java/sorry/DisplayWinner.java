package sorry;



import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class represnents a pop-up window that 
 * displays the winner of the game as well as options
 * to quit or play again.
 * 
 * @author Austin Batistoni
 */
public class DisplayWinner {
	Player p;
	
	public DisplayWinner(Player p) {
		this.p = p;
	}
	public void Start(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 1200, 735);
		stage.setTitle("Game over!");
		stage.setWidth(350);
		stage.setHeight(200);
		HBox winnerText = new HBox();
		Text winner = new Text("");
	
		if(p.getPlayerColor() instanceof Blue) {
			winner.setText("Blue");
			winner.setFill(Color.BLUE);
		}else if(p.getPlayerColor() instanceof Yellow) {
			winner.setText("Yellow");
			winner.setFill(Color.YELLOW);
		}else if(p.getPlayerColor() instanceof Green) {
			winner.setText("Green");
			winner.setFill(Color.GREEN);
		}else {
			winner.setText("Red");
			winner.setFill(Color.RED);
		}
		
		Button ok = new Button("OK");
		ok.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage = (Stage) ok.getScene().getWindow();
			    // do what you have to do
			    stage.close();
			}
		});
		
	
		Text text = new Text(" player won the game!");
		text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		winner.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
		
		text.setEffect(ds);
		winner.setEffect(ds);
	
		winnerText.getChildren().addAll(winner,text);
		winnerText.setAlignment(Pos.CENTER);
		BorderPane.setAlignment(winnerText, Pos.TOP_CENTER);
		root.setCenter(winnerText);
		BorderPane.setAlignment(ok, Pos.BOTTOM_CENTER);
		root.setBottom(ok);
		
		
		stage.setScene(scene);
		stage.show();
	}
}
