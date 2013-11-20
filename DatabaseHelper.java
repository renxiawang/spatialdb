import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleResultSet;

public class DatabaseHelper {
	private static final String	dbUser = "SYS as SYSDBA";
	private static final String	dbPassword = "root";
	private static final String	dbUrl = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	
	//private static final String	dbUser = "system";
	//private static final String	dbPassword = "hw2";
	//private static final String	dbUrl = "jdbc:oracle:thin:@localhost:1521:hw2";

	private Connection mainConnection = null;
	private Statement mainStatement = null;

	public void connect() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			mainConnection = DriverManager.getConnection(dbUrl, dbUser,
					dbPassword);
			mainStatement = mainConnection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void clean(String tableName) {
		try {
			mainStatement.executeUpdate("delete from " + tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(String query) {
		try {
			mainStatement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public OracleResultSet query(String query) {
		try {
			return (OracleResultSet) mainStatement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void disconnect() {
		try {
			mainConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
