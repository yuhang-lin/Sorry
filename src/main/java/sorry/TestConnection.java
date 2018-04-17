package sorry;

import java.sql.*;

public class TestConnection {
	public static String listOfPeople() {

		String firstNameCol = "fldFirstName";
		String lastNameCol = "fldLastName";
		String sqlQuery = new String("SELECT " + firstNameCol + ", " + lastNameCol + " FROM tblPeople;");

		String personFirstName;
		String personLastName;

		StringBuffer outputList = new StringBuffer("");

		try (Connection mysqlConn = MysqlConnect.myConnect(); Statement select = mysqlConn.createStatement()) {

			outputList.append("<h2 class=\"alternateRows\">Meet the Jetsons!</h2>");

			ResultSet myResult = select.executeQuery(sqlQuery);

			while (myResult.next()) {

				personFirstName = myResult.getString(firstNameCol);
				personLastName = myResult.getString(lastNameCol);

				outputList.append("<p>");

				outputList.append(personFirstName);

				outputList.append(" ");
				outputList.append(personLastName);
				outputList.append("</p>");

			} // while

		} catch (SQLException e) {// Catch exception if any

			System.out.println("SQL-> " + sqlQuery.toString());
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();

		}

		return outputList.toString();

	}

	public static void main(String[] args) {
		System.out.println(listOfPeople());
	}

}
