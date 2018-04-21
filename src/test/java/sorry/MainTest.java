package sorry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainTest {

	@Test
	@DisplayName("Compare NumUsed before and after restoring")
	void test() {
		Main main = new Main();
		int prevNumUsed = main.deck.getNumUsed();
		main.save();
		main.resume();
		assertEquals(prevNumUsed, main.deck.getNumUsed());
	}

}
