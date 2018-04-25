package sorry;

import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class TestConnection {
	public static String listRecord() {
		String sqlQuery = "SELECT record.id, player.name, pc1, pc2, pc3, color, time, result FROM `record` JOIN player ON player.id = player";
		StringBuffer outputList = new StringBuffer("");
		try (Connection mysqlConn = MysqlConnect.myConnect(); Statement statement = mysqlConn.createStatement()) {
			ResultSet myResult = statement.executeQuery(sqlQuery);
			while (myResult.next()) {
				String id = myResult.getString("id");
				String name = myResult.getString("name");
				String pc1 = myResult.getString("pc1") != null ? myResult.getString("pc1") : "";
				String pc2 = myResult.getString("pc2") != null ? myResult.getString("pc2") : "";
				String pc3 = myResult.getString("pc3") != null ? myResult.getString("pc3") : "";
				String color = myResult.getString("color");
				String time = myResult.getString("time");
				String result = myResult.getString("result");
				outputList.append(
						id + " " + name + " " + pc1 + " " + pc2 + " " + pc3 + " " + color + " " + time + " " + result + "\n");
			}
			String userName = "Yuhang Lin";
			sqlQuery = "INSERT INTO `player`(`name`) VALUES (?)";
			PreparedStatement preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setString(1, userName);
			try {
				preStatement.executeUpdate();
			} catch (MySQLIntegrityConstraintViolationException e) {
				System.out.println("Already exists");
			}
			sqlQuery = "SELECT id FROM `player` where name = ?";
			preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setString(1, userName);
			myResult = preStatement.executeQuery();
			int userId = 0;
			while (myResult.next()) {
				userId = myResult.getInt("id");
			}
			System.out.println(userId);
			sqlQuery = String.format("UPDATE player SET last_game = now() WHERE id = %d", userId);
			statement.executeUpdate(sqlQuery);
			String pc1 = "nice & smart";
			String pc2 = "mean & smart";
			String pc3 = "nice & smart";
			String color = "red";
			String result = "win";
			sqlQuery = "INSERT INTO `record` (`player`, `pc1`, `pc2`, `pc3`, `color`, `result`) VALUES (?, ?, ?, ?, ?, ?)";
			preStatement = mysqlConn.prepareStatement(sqlQuery);
			preStatement.setInt(1, userId);
			preStatement.setString(2, pc1);
			preStatement.setString(3, pc2);
			preStatement.setString(4, pc3);
			preStatement.setString(5, color);
			preStatement.setString(6, result);
			preStatement.executeUpdate();
		} catch (SQLException e) {// Catch exception if any
			System.out.println("SQL-> " + sqlQuery.toString());
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return outputList.toString();
	}

	public static void main(String[] args) {
		System.out.println(listRecord());
	}

}
