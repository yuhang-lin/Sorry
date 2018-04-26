package sorry;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * GUI of the card.
 * 
 * @author Austin Batistoni
 */
public class CardPane extends BorderPane {
	String card;
	String description;
	String color;

	public CardPane(Card c) {
		this.card = c.getName();
		this.description = c.getInfo();

		//Sets the colors for each individual card
		switch (c.getName()) {
		case "1":
			color = "rgba(237, 42, 42, 0.58);";
			break;
		case "2":
			color = "rgba(237, 140, 42, 0.58);";
			break;
		case "3":
			color = "rgba(237, 237, 42, 0.58);";
			break;
		case "4":
			color = "rgba(140, 237, 42, 0.58);";
			break;
		case "5":
			color = "rgba(42, 237, 140, 0.58);";
			break;
		case "7":
			color = "rgba(42, 237, 237, 0.58);";
			break;
		case "8":
			color = "rgba(42, 140, 237, 0.58);";
			break;
		case "10":
			color = "rgba(42, 42, 237, 0.58);";
			break;
		case "11":
			color = "rgba(140, 42, 237, 0.58);";
			break;
		case "12":
			color = "rgba(237, 42, 237, 0.58);";
			break;
		case "Sorry":
			color = "rgba(237, 42, 140, 0.58);";
			break;
		default:
			color = "rgba(0, 0, 0, 0.3);";

		}

		setPrefSize(20, 500);
		maxWidth(20);
		setPadding(new Insets(10));

		setStyle("-fx-background-color:" + color + "-fx-background-radius: 10px; " + "-fx-border-radius: 10px; "
				+ "-fx-border-color: rgba(0, 0, 0, 1); " + "-fx-border-width: 3px;");

		BorderPane topLabel = new BorderPane(); //Top of the card
		BorderPane bottomLabel = new BorderPane(); //Bottom of the card

		Text cardText = new Text(card);
		Text cardDesc = new Text(description);
		Text cardDesc1 = new Text(description);
		Text cardText1 = new Text(card);
		Text cardText2 = new Text(card);

		cardText.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 80));
		cardText.setRotate(315);
		cardText1.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));
		cardText2.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 20));

		//Large text in the middle of which card it is
		setCenter(cardText);

		//Add card and description in the corners
		topLabel.setLeft(cardText1);
		topLabel.setRight(cardDesc);
		setTop(topLabel);

		bottomLabel.setLeft(cardDesc1);
		bottomLabel.setRight(cardText2);

		setBottom(bottomLabel);

		BorderPane.setAlignment(topLabel, Pos.TOP_LEFT);
		BorderPane.setAlignment(cardText1, Pos.BOTTOM_RIGHT);

	}

}
