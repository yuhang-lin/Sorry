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
	public int numUsed = 0;
	public Card[] cards = new Card[NUM_CARDS];

	public Deck() {
		// Create cards
		// Five 1 cards; four each of 2, 3, 4, 5, 7, 8, 10, 11, 12 and four Sorry cards.
		for (int i = 0; i < cards.length; i++) {
			if (i < 5) {
				cards[i] = new Card("1", "Either start a pawn OR move one pawn forward 1 space.");
			} else if (i < 9) {
				cards[i] = new Card("2",
						"Either start a pawn OR move one pawn forward 2 spaces. Whichever you do！or even if you couldn¨t move！DRAW AGAIN and move accordingly.");
			} else if (i < 13) {
				cards[i] = new Card("3", "Move one pawn forward 3 spaces.");
			} else if (i < 17) {
				cards[i] = new Card("4", "Move one pawn backward 4 spaces.");
			} else if (i < 21) {
				cards[i] = new Card("5", "Move one pawn forward 5 spaces.");
			} else if (i < 25) {
				cards[i] = new Card("7",
						"Either move one pawn forward 7 spaces！OR split the forward move between any two pawns.");
			} else if (i < 29) {
				cards[i] = new Card("8", "Move one pawn forward 8 spaces.");
			} else if (i < 33) {
				cards[i] = new Card("10", "Either move one pawn forward 10 spaces！OR move one pawn backward 1 space.");
			} else if (i < 37) {
				cards[i] = new Card("11",
						"Move one pawn forward 11 spaces！OR switch any one of your pawns with one of any opponent¨s.");
			} else if (i < 41) {
				cards[i] = new Card("12", "Move one pawn forward 12 spaces.");
			} else if (i < 45) {
				cards[i] = new Card("Sorry",
						"Take one pawn from your START, place it on any space that is occupied by any opponent, and BUMP that opponent's pawn back to its START. If there is no pawn on your START or no opponent's pawn on any space you can move to, you forfeit your move.");
			}
		}
		shuffle();
	}

	/**
	 * Shuffle the cards of the deck.
	 */
	public void shuffle() {
		int randomNum = 0;
		for (int i = 0; i < cards.length; i++) {
			while (true) {
				randomNum = ThreadLocalRandom.current().nextInt(0, NUM_CARDS);
				if (!cards[i].getName().equals(cards[randomNum].getName())) { // make sure that we swap the card with a different card
					break;
				}
			}
			Card temp = cards[i];
			cards[i] = cards[randomNum];
			cards[randomNum] = temp;
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
