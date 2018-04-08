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
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
}
