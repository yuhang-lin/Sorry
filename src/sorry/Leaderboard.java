package sorry;
// Write the base code of Leaderboard class.
// Implement GUI for Leaderboard class.
// Implement GUI for Main class.

public class Leaderboard {
	
	private String username;
	private int ranking;
	private double elapsedTime;
	private int totalNumOfMoves;
	
	//What are the next steps??
	
	
	
	public String getUsername() {
		return username;
	}
	
	
	public int getRanking() {
		return ranking;
	}
	
	public double getElapsedTime() {
		return elapsedTime;
	}
	
	public int totalNumOfMoves() {
		return totalNumOfMoves;
	}
	
	
}
//What should the leaderboard display?
//I am guessing that it should display the top 10 games played by the user. First they will be ordered
//by whether the player came in first. Then, they should be ranked by the amount of time it took the player to win.
//So, we need to store this information in a file
