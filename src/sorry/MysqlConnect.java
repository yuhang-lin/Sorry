package sorry;
import java.sql.*;
import java.util.ResourceBundle;

//############################################################################
//
// this class makes the connection to the mySQL database.
//
// used by all other classes
//
//############################################################################
public class MysqlConnect {
	public static Connection myConnect() {
		// System.out.println("MySQL Connect Example.");
		Connection conn = null;

		ResourceBundle properties = ResourceBundle.getBundle("MysqlConnect");
		String url = properties.getString("url");
		String dbName = properties.getString("dbName");
		String driver = properties.getString("driver");
		String userName = properties.getString("userName");
		String password = properties.getString("password");

		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception e) {
			System.err.println("Error MysqlConnect.myConnect: " + e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}
}


