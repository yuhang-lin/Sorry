package sorry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class BoardTest {

	@Test
	void test() {
		Board board = new Board();
		ArrayList<Integer> coords = new ArrayList<>(Arrays.asList(0, 0));
		int[][] pathArray = { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 3, 0 }, { 4, 0 }, { 5, 0 }, { 6, 0 }, { 7, 0 }, { 8, 0 },
				{ 9, 0 }, { 10, 0 }, { 11, 0 }, { 12, 0 }, { 13, 0 }, { 14, 0 }, { 15, 0 }, { 15, 1 }, { 15, 2 },
				{ 15, 3 }, { 15, 4 }, { 15, 5 }, { 15, 6 }, { 15, 7 }, { 15, 8 }, { 15, 9 }, { 15, 10 }, { 15, 11 },
				{ 15, 12 }, { 15, 13 }, { 15, 14 }, { 15, 15 }, { 14, 15 }, { 13, 15 }, { 12, 15 }, { 11, 15 },
				{ 10, 15 }, { 9, 15 }, { 8, 15 }, { 7, 15 }, { 6, 15 }, { 5, 15 }, { 4, 15 }, { 3, 15 }, { 2, 15 },
				{ 1, 15 }, { 0, 15 }, { 0, 14 }, { 0, 13 }, { 0, 12 }, { 0, 11 }, { 0, 10 }, { 0, 9 }, { 0, 8 },
				{ 0, 7 }, { 0, 6 }, { 0, 5 }, { 0, 4 }, { 0, 3 }, { 0, 2 }, { 0, 1 } };
		for (int i = 0; i < pathArray.length; i++) {
			int[] path = pathArray[i];
			for (int j = 0; j < path.length; j++) {
				coords.set(j, path[j]);
			}
			assertEquals(i, Board.getPathIndex(coords));
		}
	}

}
