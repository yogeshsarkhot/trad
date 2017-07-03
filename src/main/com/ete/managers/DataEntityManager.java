package main.com.ete.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.com.ete.commom.CommonUtilities;
import main.com.ete.commom.ConnectionManager;
import main.com.ete.commom.Constants;
import main.com.ete.model.DataEntityModel;
import main.com.ete.model.RequestParameterModel;

public class DataEntityManager {
	private final Logger LOGGER = Logger.getLogger(DataEntityManager.class.getName());
	private final String CLASS_NAME = DataEntityManager.class.getName();

	@SuppressWarnings({ "rawtypes" })
	public ArrayList getDataEntities(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getDataEntities";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		ArrayList list = new ArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = new String();
			String searchParameterString = new String();
			if (UserManager.isSuperUser(requestParameter.getAccessUserModel())) {
				if (null != requestParameter.getDataEntityModel()) {
					searchParameterString = requestParameter.getDataEntityModel().generateSearchQueryString(true);
				}
				queryString = "SELECT a.id, a.name, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_dt_enty a ";
				if (null != searchParameterString && searchParameterString.length() != 0) {
					queryString = queryString + " WHERE " + searchParameterString;
				}
			} else {
				if (null != requestParameter.getDataEntityModel()) {
					searchParameterString = requestParameter.getDataEntityModel().generateSearchQueryString(false);
				}
				queryString = "SELECT a.id, a.name, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_dt_enty a, rgt_usr b WHERE a.id = b.orgnztn_id AND b.id = ? ";
				if (null != searchParameterString && searchParameterString.length() != 0) {
					queryString = queryString + " " + searchParameterString;
				}
			}
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			if (!UserManager.isSuperUser(requestParameter.getAccessUserModel())) {
				preparedStatement.setLong(1, requestParameter.getAccessUserModel().getId());
			}
			resultSet = preparedStatement.executeQuery();
			list = CommonUtilities.getModelListFromResultSet(resultSet, new DataEntityModel());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return list;
	}

	public DataEntityModel createDataEntity(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".createDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		DataEntityModel response = new DataEntityModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "INSERT INTO rgt_dt_enty (id, name, orgnztn_id, crtn_dt, crtd_by_usr_id, mdfctn_dt, mdfd_by_usr_id, mdfctn_cmnt) VALUES (rgt_dt_enty_seq.NEXTVAL, ?, ?, SYSDATE, ?, SYSDATE, ?, ?)";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getDataEntityModel().getName());
			preparedStatement.setLong(2, requestParameter.getDataEntityModel().getOrganizationId());
			preparedStatement.setLong(3, requestParameter.getAccessUserModel().getId());
			preparedStatement.setLong(4, requestParameter.getAccessUserModel().getId());
			preparedStatement.setString(5, Constants.CREATION_COMMENT);
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.CREATION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.CREATION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_DT_ENTY_UK1")) {
				Exception e = new Exception("Data Entity name should be unique in organization. '"
						+ requestParameter.getDataEntityModel().getName() + "' is already being used.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get Data Entity with ID
		response = new DataEntityModel();
		try {
			String queryString = "SELECT id FROM rgt_dt_enty WHERE name = ? AND orgnztn_id = ? ";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getDataEntityModel().getName());
			preparedStatement.setLong(2, requestParameter.getDataEntityModel().getOrganizationId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				response.setId(resultSet.getLong("id"));
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public DataEntityModel updateDataEntity(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".updateDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		DataEntityModel response = new DataEntityModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "UPDATE rgt_dt_enty SET mdfctn_dt = SYSDATE "
					+ requestParameter.getDataEntityModel().generateUpdateQueryString() + ", mdfd_by_usr_id = "
					+ requestParameter.getAccessUserModel().getId() + " WHERE id = "
					+ requestParameter.getDataEntityModel().getId();
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.UPDATE_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.UPDATE_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_DT_ENTY_UK1")) {
				Exception e = new Exception("Data Entity name should be unique in organization. '"
						+ requestParameter.getDataEntityModel().getName() + "' is already being used.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get Data Entity with ID
		response = new DataEntityModel();
		response.setId(requestParameter.getDataEntityModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public DataEntityModel deleteDataEntity(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		DataEntityModel response = new DataEntityModel();
		if (null != requestParameter.getDataEntityModel()
				&& requestParameter.getDataEntityModel().isValidObjectForDelete()) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				String queryString = "DELETE FROM rgt_dt_enty WHERE id = ?";
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(queryString);
				preparedStatement.setLong(1, requestParameter.getDataEntityModel().getId());
				int rowCount = preparedStatement.executeUpdate();
				if (rowCount != 1) {
					LOGGER.log(Level.SEVERE, Constants.DELETION_COMMENT_UNSUCCESSFUL);
					Exception e = new Exception(Constants.DELETION_COMMENT_UNSUCCESSFUL);
					throw e;
				}
				connection.commit();
			} catch (SQLException sqe) {
				LOGGER.log(Level.SEVERE, sqe.getMessage());
				if (sqe.getMessage().contains("RGT_DT_ENTY_RQRMNT_MP_FK1")) {
					Exception e = new Exception(
							"Data entities are referred by Requirement(s). It can not be deleted until related requirement references(s) are deleted from requirement text.");
					throw e;
				}
				throw sqe;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw e;
			} finally {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
			// response = Get Data Entity with ID
			response = new DataEntityModel();
			response.setId(requestParameter.getDataEntityModel().getId());
		} else {
			response = null;
			Exception e = new Exception(Constants.DELETION_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}
}
