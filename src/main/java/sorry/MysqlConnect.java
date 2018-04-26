package sorry;

import java.sql.*;
import java.util.ResourceBundle;

/**
 * Makes the connection to the mySQL database.
 * 
 * @author Yuhang Lin
 */
public class MysqlConnect {
	public static Connection myConnect() {
		Connection conn = null;

		ResourceBundle properties = ResourceBundle.getBundle("MysqlConnect");
		String url = properties.getString("url");
		String dbName = properties.getString("dbName");
		String driver = properties.getString("driver");
		String userName = properties.getString("userName");
		String password = properties.getString("password");

		try {
			Class.forName(driver).getDeclaredConstructor().newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception e) {
			System.err.println("Error MysqlConnect.myConnect: " + e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
}
