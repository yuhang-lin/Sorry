package sorry;

import java.sql.*;

public class TestConnection {
	public static String listRecord() {
		String sqlQuery = "SELECT record.id, player.name, pc1, pc2, pc3, color, result FROM `record` JOIN player ON player.id = player";
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
				String result = myResult.getString("result");
				outputList.append(id + " " + name + " " + pc1 + " " + pc2 + " " + pc3 + " " + color + " " + result + "\n");
			}
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
