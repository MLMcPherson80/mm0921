package mm0921.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import mm0921.Models.Tool;

public class DatabaseHelper {
	String url = "jdbc:sqlite:ToolDB.db";
	
	public DatabaseHelper() {
		try {
			Connection conn = getConnection();
			String sqlCreate = "CREATE TABLE IF NOT EXISTS tTool " +
					"(toolID INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"toolType TEXT NOT NULL, " +
					"brandName TEXT NOT NULL, " +
					"toolCode TEXT NOT NULL, " +
					"dailyRate DOUBLE NOT NULL, " +
					"weekdayCharge BOOL, " +
					"weekendCharge BOOL, " +
					"holidayCharge BOOL)";
			PreparedStatement pStmnt = conn.prepareStatement(sqlCreate);
			pStmnt.execute();
			
			pStmnt.close();
			conn.close();
			
			if (selectToolCount() == 0)
				populateTools();
		} catch (SQLException ex) {
			System.out.print("Error executing statement, exception: " + ex.getMessage());
		}
	}
	
	public Connection getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException | ClassNotFoundException ex) {
			System.out.print("Error establishing database connection, exception: " + ex.getMessage());
			return null;
		}
	}
	
	public int selectToolCount() {
		String sqlQuery = "SELECT COUNT(toolID) AS totalRecords FROM tTool";
		try (Connection connection = getConnection()) {
			try (PreparedStatement pStmnt = connection.prepareStatement(sqlQuery)) {
				ResultSet result = pStmnt.executeQuery();
				return result.getInt("totalRecords");
			}
		} catch (SQLException ex) {
			System.out.print("Error executing query, exception: " + ex.getMessage());
			return 0;
		}
	}
	
	public Tool selectToolByToolCode(String toolCode) {
		String sqlQuery = "SELECT toolType, brandName, toolCode, dailyRate, weekdayCharge, weekendCharge, holidayCharge FROM tTool WHERE toolCode = ?";
	
		try (Connection connection = getConnection()) {
			try (PreparedStatement pStmnt = connection.prepareStatement(sqlQuery)) {
				pStmnt.setString(1, toolCode);
				ResultSet results = pStmnt.executeQuery();
				
				Tool dbTool = new Tool();
				dbTool.setToolType(results.getString("toolType"));
				dbTool.setBrandName(results.getString("brandName"));
				dbTool.setToolCode(results.getString("toolCode"));
				dbTool.setDailyRate(results.getDouble("dailyRate"));
				dbTool.setWeekdayCharge(results.getInt("weekdayCharge") == 1);
				dbTool.setWeekendCharge(results.getInt("weekendCharge") == 1);
				dbTool.setHolidayCharge(results.getInt("holidayCharge") == 1);
				
				return dbTool;
			}
		} catch (SQLException ex) {
			System.out.print("Error executing query, exception: " + ex.getMessage());
			return null;
		}
	}
	
	public ArrayList<Tool> selectAllTools() {
		String sqlQuery = "SELECT toolType, brandName, toolCode, dailyRate, weekdayCharge, weekendCharge, holidayCharge FROM tTool";
		
		try (Connection connection = getConnection()) {
			try (PreparedStatement pStmnt = connection.prepareStatement(sqlQuery)) {
				ResultSet results = pStmnt.executeQuery();
				ArrayList<Tool> allTools = new ArrayList<>();
				while(results.next()) {
					Tool dbTool = new Tool();
					dbTool.setToolType(results.getString("toolType"));
					dbTool.setBrandName(results.getString("brandName"));
					dbTool.setToolCode(results.getString("toolCode"));
					dbTool.setDailyRate(results.getDouble("dailyRate"));
					dbTool.setWeekdayCharge(results.getInt("weekdayCharge") == 1);
					dbTool.setWeekendCharge(results.getInt("weekendCharge") == 1);
					dbTool.setHolidayCharge(results.getInt("holidayCharge") == 1);
					allTools.add(dbTool);
				}
				
				return allTools;
			}
		} catch (SQLException ex) {
			System.out.print("Error executing query, exception: " + ex.getMessage());
			return null;
		}
	}

	public void populateTools() {
		String sqlInsert = "INSERT INTO tTool (toolType, brandName, toolCode, dailyRate, weekdayCharge, weekendCharge, holidayCharge) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		try {
			Connection conn = getConnection();
			PreparedStatement pStmnt = conn.prepareStatement(sqlInsert);
			pStmnt.setString(1, "Ladder");
			pStmnt.setString(2, "Werner");
			pStmnt.setString(3, "LADW");
			pStmnt.setDouble(4, 1.99);
			pStmnt.setBoolean(5, true);
			pStmnt.setBoolean(6, true);
			pStmnt.setBoolean(7, false);
			pStmnt.execute();
			
			pStmnt.setString(1, "Chainsaw");
			pStmnt.setString(2, "Stihl");
			pStmnt.setString(3, "CHNS");
			pStmnt.setDouble(4, 1.49);
			pStmnt.setBoolean(5, true);
			pStmnt.setBoolean(6, false);
			pStmnt.setBoolean(7, true);
			pStmnt.execute();
			
			pStmnt.setString(1, "Jackhammer");
			pStmnt.setString(2, "Ridgid");
			pStmnt.setString(3, "JAKR");
			pStmnt.setDouble(4, 2.99);
			pStmnt.setBoolean(5, true);
			pStmnt.setBoolean(6, false);
			pStmnt.setBoolean(7, false);
			pStmnt.execute();
			
			pStmnt.setString(1, "Jackhammer");
			pStmnt.setString(2, "DeWalt");
			pStmnt.setString(3, "JAKD");
			pStmnt.setDouble(4, 2.99);
			pStmnt.setBoolean(5, true);
			pStmnt.setBoolean(6, false);
			pStmnt.setBoolean(7, false);
			pStmnt.execute();
			
			pStmnt.close();
			conn.close();
		} catch (SQLException ex) {
			System.out.print("Error populating database, exception: " + ex.getMessage());
		}
	}
}
