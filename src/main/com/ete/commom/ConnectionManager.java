package main.com.ete.commom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionManager {

	private final static Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());
	private final static String CLASS_NAME = ConnectionManager.class.getName();

	public static Connection getConnection() {
		final String METHOD_NAME = CLASS_NAME + ".getConnection";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		Connection connection = null;
		try {
			// InitialContext context = new InitialContext();
			// DataSource dataSource = (DataSource)
			// context.lookup(Constants.DATASOURCE_NAME);
			// connection = dataSource.getConnection();
			Class.forName(Constants.DATABASE_DRIVER);
			connection = DriverManager.getConnection(Constants.DATABASE_CONNECTION_STRING,
					Constants.DATABASE_CONNECTION_USER_NAME, Constants.DATABASE_CONNECTION_PASSWORD);
			// } catch (NamingException e) {
			// LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (ClassNotFoundException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return connection;
	}

	public static void closeAll(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
		final String METHOD_NAME = CLASS_NAME + ".closeAll";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
		if (null != preparedStatement) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
	}
}
