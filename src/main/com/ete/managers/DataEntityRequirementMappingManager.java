package main.com.ete.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.com.ete.commom.ConnectionManager;
import main.com.ete.commom.Constants;

public class DataEntityRequirementMappingManager {

	private final static Logger LOGGER = Logger.getLogger(DataEntityRequirementMappingManager.class.getName());
	private final static String CLASS_NAME = DataEntityRequirementMappingManager.class.getName();

	public static boolean createDataEntityRequirementMapping(long dataEntityId, long requirementId, long userId)
			throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".createDataEntityRequirementMapping";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isDataEntityRequirementMapping = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// Create Data Entity Requirement Mapping
			String queryString = "INSERT INTO rgt_dt_enty_rqrmnt_mp (id, dt_enty_id, rqrmnt_id, crtn_dt, crtd_by_usr_id, mdfctn_dt, mdfd_by_usr_id, mdfctn_cmnt) VALUES (rgt_dt_enty_rqrmnt_mp_seq.NEXTVAL, ?, ?, SYSDATE, ?, SYSDATE, ? , ?)";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, dataEntityId);
			preparedStatement.setLong(2, requirementId);
			preparedStatement.setLong(3, userId);
			preparedStatement.setLong(4, userId);
			preparedStatement.setString(5, Constants.CREATION_COMMENT);
			int rowCount = preparedStatement.executeUpdate();
			connection.commit();
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			if (rowCount != 1) {
				throw new Exception("Data Entity - Requirement Mapping Failed.");
			}
			isDataEntityRequirementMapping = true;
			LOGGER.log(Level.INFO, "Data Entity - Requirement Mapping Created Successfully.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return isDataEntityRequirementMapping;
	}

	public static boolean deleteDataEntityRequirementMapping(long dataEntityId, long requirementId) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteDataEntityRequirementMapping";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isDeleteDataEntityRequirementMapping = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "DELETE FROM rgt_dt_enty_rqrmnt_mp WHERE dt_enty_id = ? AND rqrmnt_id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, dataEntityId);
			preparedStatement.setLong(2, requirementId);
			int rowCount = preparedStatement.executeUpdate();
			connection.commit();
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.DELETION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.DELETION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			isDeleteDataEntityRequirementMapping = true;
			LOGGER.log(Level.INFO, "Data Entity-Requirement Mapping deleted successfully.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return isDeleteDataEntityRequirementMapping;
	}

	public static ArrayList<Long> getDataEntityIdListForRequirement(long requirementId) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getDataEntityIdListForRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		ArrayList<Long> dataEntityList = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "SELECT a.dt_enty_id FROM rgt_dt_enty_rqrmnt_mp a WHERE a.rqrmnt_id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requirementId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				dataEntityList.add(resultSet.getLong("dt_enty_id"));
			}
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			throw new Exception(sqe);
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return dataEntityList;
	}

	public static boolean deleteDataEntityRequirementMappingForRequirement(long requirementId) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteDataEntityRequirementMappingForRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isDeleteDataEntityRequirementMappingForRequirement = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "DELETE FROM rgt_dt_enty_rqrmnt_mp WHERE rqrmnt_id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requirementId);
			preparedStatement.executeUpdate();
			connection.commit();
			isDeleteDataEntityRequirementMappingForRequirement = true;
			LOGGER.log(Level.INFO, "Data Entity Mapping(s) related to the Requirement deleted successfully.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return isDeleteDataEntityRequirementMappingForRequirement;
	}
}
