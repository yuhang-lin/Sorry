/**
 * Deck of cards in the game.
 */
package sorry;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Yuhang Lin
 *
 */
public class Deck {

	public final int NUM_CARDS = 45;
	private int numUsed = 0;
	private Card[] cards = new Card[NUM_CARDS];

	public Card[] getCards() {
		return cards;
	}

	public void setCards(Card[] cards) {
		this.cards = cards;
	}

	public int getNumUsed() {
		return numUsed;
	}

	public void setNumUsed(int numUsed) {
		this.numUsed = numUsed;
	}

	public Deck() {
		// Create cards
		// Five 1 cards; four each of 2, 3, 4, 5, 7, 8, 10, 11, 12 and four Sorry cards.
		for (int i = 0; i < cards.length; i++) {
			if (i < 5) {
				cards[i] = new Card("1");
			} else if (i < 9) {
				cards[i] = new Card("2");
			} else if (i < 13) {
				cards[i] = new Card("3");
			} else if (i < 17) {
				cards[i] = new Card("4");
			} else if (i < 21) {
				cards[i] = new Card("5");
			} else if (i < 25) {
				cards[i] = new Card("7");
			} else if (i < 29) {
				cards[i] = new Card("8");
			} else if (i < 33) {
				cards[i] = new Card("10");
			} else if (i < 37) {
				cards[i] = new Card("11");
			} else if (i < 41) {
				cards[i] = new Card("12");
			} else if (i < 45) {
				cards[i] = new Card("Sorry");
			}
		}
		shuffle();
	}

	/**
	 * Shuffle the cards of the deck.
	 */
	public void shuffle() {
		int randomNum = 0;
		int numShuffle = 2;
		for (int j = 0; j < numShuffle; j++) {
			for (int i = 0; i < cards.length; i++) {
				while (true) {
					randomNum = ThreadLocalRandom.current().nextInt(0, NUM_CARDS);
					if (!cards[i].getName().equals(cards[randomNum].getName())) { // make sure that we swap the card
																					// with a different card
						break;
					}
				}
				Card temp = cards[i];
				cards[i] = cards[randomNum];
				cards[randomNum] = temp;
			}
		}
	}

	/**
	 * Return next card from the deck.
	 */
	public Card draw() {
		if (numUsed >= NUM_CARDS) {
			shuffle();
			numUsed = 0;
		}
		return cards[numUsed++];
	}
}
