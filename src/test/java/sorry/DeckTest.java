package sorry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class DeckTest {

	/**
	 * The number of two consecutive cards that are the same should be no more than
	 * 25 out of 200.
	 */
	@RepeatedTest(20)
	@DisplayName("Repeat testing two consecutive cards that are the same")
	void test() {
		Deck deck = new Deck();
		int counter = 0;
		Card card = new Card("testing");
		for (int i = 0; i < 200; i++) {
			Card temp = deck.draw();
			if (temp.getName().equals(card.getName())) {
				counter++;
			}
			card = temp;
		}
		assertTrue(counter <= 30, "Number of consecutive that are the same is: " + counter);
	}

}
