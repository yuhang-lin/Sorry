/**
 * Store the information about the card.
 */
package sorry;

/**
 * @author Yuhang Lin
 *
 */
public class Card {

	private String name; // Card name
	private String info; // Description of the card

	public Card(String name) {
		this.name = name;
		switch (name) {
		case "1":
			info = "Either start a pawn OR move one pawn forward 1 space.";
			break;
		case "2":
			info = "Either start a pawn OR move one pawn forward 2 spaces. Whichever you do or even if you couldn't move, DRAW AGAIN and move accordingly.";
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
			info = "Either move one pawn forward 7 spaces. OR split the forward move between any two pawns.";
			break;
		case "8":
			info = "Move one pawn forward 8 spaces.";
			break;
		case "10":
			info = "Either move one pawn forward 10 spaces. OR move one pawn backward 1 space.";
			break;
		case "11":
			info = "Move one pawn forward 11 spaces. OR switch any one of your pawns with one of any opponent's.";
			break;
		case "12":
			info = "Move one pawn forward 12 spaces.";
			break;
		case "Sorry":
			info = "Take one pawn from your START, place it on any space that is occupied by any opponent, and BUMP that opponent's pawn back to its START. If there is no pawn on your START or no opponent's pawn on any space you can move to, you forfeit your move.";
			break;
		default:
			info = "Unknown Card";
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
