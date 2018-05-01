package sorry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * GUI for the leader board.
 * <p>
 * Margaret Caron wrote some initial functions then Yuhang Lin rewrote this
 * class completely.
 * 
 * @author Yuhang Lin, Margaret Caron
 */
public class Leaderboard {

	private TableView<GameRecord> table = new TableView<GameRecord>();
	private final ObservableList<GameRecord> data = FXCollections.observableArrayList();

	public void start(Stage stage) {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		Scene scene = new Scene(pane);
		stage.setTitle("Leader Board of Sorry!");
		stage.setWidth(1250);
		stage.setHeight(500);

		final Label label = new Label("Leader Board");
		label.setFont(new Font("Arial", 20));

		Text stats = new Text(getStatistics());
		stats.setFont(new Font("Arial", 15));

		table.setEditable(false);
		getGameRecord();

		TableColumn<GameRecord, Integer> recordNumCol = new TableColumn<>("Record Number");
		recordNumCol.setMinWidth(100);
		recordNumCol.setCellValueFactory(new PropertyValueFactory<GameRecord, Integer>("recordNum"));
		recordNumCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> nameCol = new TableColumn<>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("name"));
		nameCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> pc1Col = new TableColumn<>("Computer Opponent 1");
		pc1Col.setMinWidth(200);
		pc1Col.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("pc1"));
		pc1Col.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> pc2Col = new TableColumn<>("Computer Opponent 2");
		pc2Col.setMinWidth(200);
		pc2Col.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("pc2"));
		pc2Col.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> pc3Col = new TableColumn<>("Computer Opponent 3");
		pc3Col.setMinWidth(200);
		pc3Col.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("pc3"));
		pc3Col.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> colorCol = new TableColumn<>("Color");
		colorCol.setMinWidth(100);
		colorCol.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("color"));
		colorCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> timeCol = new TableColumn<>("Time");
		timeCol.setMinWidth(200);
		timeCol.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("time"));
		timeCol.setSortType(TableColumn.SortType.ASCENDING);

		TableColumn<GameRecord, String> resultCol = new TableColumn<>("Result");
		resultCol.setMinWidth(100);
		resultCol.setCellValueFactory(new PropertyValueFactory<GameRecord, String>("result"));
		resultCol.setSortType(TableColumn.SortType.DESCENDING);

		table.setItems(data);
		table.getColumns().addAll(recordNumCol, nameCol, pc1Col, pc2Col, pc3Col, colorCol, timeCol, resultCol);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		// vbox.setPadding(new Insets(10, 0, 0, 10));
		vbox.getChildren().addAll(table);
		pane.setTop(label);
		pane.setCenter(vbox);
		pane.setBottom(stats);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Gets statistics from MySQL database.
	 */
	private String getStatistics() {
		String sqlQuery = "";
		StringBuilder sb = new StringBuilder();
		try (Connection mysqlConn = MysqlConnect.myConnect(); Statement statement = mysqlConn.createStatement()) {
			sqlQuery = "SELECT count(1) as count FROM `record`";
			ResultSet myResult = statement.executeQuery(sqlQuery);
			int total = 0;
			int numWon = 0;
			while (myResult.next()) {
				total = myResult.getInt("count");
				sb.append(String.format("Number of games played:  %d, \t", total));
			}
			sqlQuery = "SELECT count(1) as count FROM `record` WHERE result='won'";
			myResult = statement.executeQuery(sqlQuery);
			while (myResult.next()) {
				numWon = myResult.getInt("count");
				sb.append(String.format("Number of games won: %d, \t", numWon));
			}
			sb.append(String.format("Number of games lost: %d\t", total - numWon));
		} catch (SQLException e) {// Catch exception if any
			System.out.println("SQL-> " + sqlQuery.toString());
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Gets game record from MySQL database.
	 */
	private void getGameRecord() {
		String sqlQuery = "SELECT record.id, player.name, pc1, pc2, pc3, color, time, result FROM `record` JOIN player ON player.id = player";
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
				data.add(new GameRecord(id, name, pc1, pc2, pc3, color, time, result));
			}
		} catch (SQLException e) {// Catch exception if any
			System.out.println("SQL-> " + sqlQuery.toString());
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Game record for displaying in the table
	 * 
	 * @author Yuhang Lin
	 */
	public static class GameRecord {

		private final SimpleIntegerProperty recordNum;
		private final SimpleStringProperty name;
		private final SimpleStringProperty pc1;
		private final SimpleStringProperty pc2;
		private final SimpleStringProperty pc3;
		private final SimpleStringProperty color;
		private final SimpleStringProperty time;
		private final SimpleStringProperty result;

		public GameRecord(String recordNum, String name, String pc1, String pc2, String pc3, String color, String time,
				String result) {
			super();
			this.recordNum = new SimpleIntegerProperty(Integer.parseInt(recordNum));
			this.name = new SimpleStringProperty(name);
			this.pc1 = new SimpleStringProperty(pc1);
			this.pc2 = new SimpleStringProperty(pc2);
			this.pc3 = new SimpleStringProperty(pc3);
			this.color = new SimpleStringProperty(color);
			this.time = new SimpleStringProperty(time);
			this.result = new SimpleStringProperty(result);
		}

		public int getRecordNum() {
			return recordNum.get();
		}

		public String getName() {
			return name.get();
		}

		public String getPc1() {
			return pc1.get();
		}

		public String getPc2() {
			return pc2.get();
		}

		public String getPc3() {
			return pc3.get();
		}

		public String getColor() {
			return color.get();
		}

		public String getTime() {
			return time.get();
		}

		public String getResult() {
			return result.get();
		}

	}
}
