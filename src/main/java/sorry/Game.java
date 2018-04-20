package sorry;

/**
* @author Yuhang Lin
*/
public class Game {
	int currentTerm = 0; // current term for the player
	Player[] players;
	Deck deck = new Deck();
	public Game() {
		Player blue = new Computer(new Blue(), Computer.NiceLevel.MEAN, Computer.SmartLevel.SMART); // for testing only
		Player green = new Player(new Green());
		Player red = new Player(new Red());
		Player yellow = new Player(new Yellow());
		players = new Player[]{ blue, green, red, yellow };
	}
	
	public void start() {
		while (true) {
			Player currentPlayer = players[currentTerm];
			if (currentPlayer.getPiecesHome() == 4) {
				System.out.println("Player" + currentPlayer.getPlayerColor() + "win!");
				end();
				break;
			}
			Card card = deck.draw();
			// Setting possible moves for all pieces
			// Choose the best move if it is computer; wait for the user to choose the move
			currentTerm = (currentTerm + 1) % players.length; 
		}
	}
	
	private void end() {
		// Send result to MySQL database
		// add user if not found
		// get user id
		// add record
	}
}
