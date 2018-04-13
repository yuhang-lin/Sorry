package sorry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	void test() {
		Main main = new Main();
		int prevNumUsed = main.deck.getNumUsed();
		main.save();
		main.restore();
		assertEquals(prevNumUsed, main.deck.getNumUsed());
	}

}
