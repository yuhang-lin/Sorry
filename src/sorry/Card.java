/**
 * Store the information about the card.
 */
package sorry;

/**
 * @author Yuhang Lin
 *
 */
public class Card {

	public String name; // Card name
	public String info; // Description of the card
	
	public Card(String name, String info) {
		this.name = name;
		this.info = info;
	}
	
	/**
	 * Return the information of the card as String.
	 */
	@Override
	public String toString() {
		return name + "\n" + info + "\n";
	}
}
