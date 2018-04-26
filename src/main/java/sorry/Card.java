package sorry;

/**
 * Stores the information about the card.
 * 
 * Yuhang Lin wrote the first version and Austin Batistoni shortened some
 * descriptions of the card.
 * 
 * @author Yuhang Lin, Austin Batistoni
 */
public class Card {

	private String name; // Card name
	private String info; // Description of the card

	public Card(String name) {
		this.name = name;
		switch (name) {
		case "1":
			info = "Either start a pawn OR move one \npawn forward 1 space.";
			break;
		case "2":
			info = "Either start a pawn OR move one \npawn forward 2 spaces.";
			break;
		case "3":
			info = "Move one pawn forward 3 spaces.";
			break;
		case "4":
			info = "Move one pawn backward 4 spaces.";
			break;
		case "5":
			info = "Move one pawn forward 5 spaces.";
			break;
		case "7":
			info = "Move one pawn forward 7 spaces.";
			break;
		case "8":
			info = "Move one pawn forward 8 spaces.";
			break;
		case "10":
			info = "Move one pawn forward 10 spaces.";
			break;
		case "11":
			info = "Move one pawn forward 11 spaces.";
			break;
		case "12":
			info = "Move one pawn forward 12 spaces.";
			break;
		case "Sorry":
			info = "Pick a piece to bump \nhome and take their spot!";
			break;
		default:
			info = "Draw";
			break;
		}
	}

	/**
	 * Return the information of the card as String.
	 */
	@Override
	public String toString() {
		return name + "\n" + info + "\n";
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}
}
