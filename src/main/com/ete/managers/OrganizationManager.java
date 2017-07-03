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
import main.com.ete.model.OrganizationModel;
import main.com.ete.model.RequestParameterModel;

public class OrganizationManager {

	private final Logger LOGGER = Logger.getLogger(OrganizationManager.class.getName());
	private final String CLASS_NAME = OrganizationManager.class.getName();

	@SuppressWarnings({ "rawtypes" })
	public ArrayList getOrganizations(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getOrganizations";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		ArrayList list = new ArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = new String();
			String searchParameterString = new String();
			if (UserManager.isSuperUser(requestParameter.getAccessUserModel())) {
				if (null != requestParameter.getOrganizationModel()) {
					searchParameterString = requestParameter.getOrganizationModel().generateSearchQueryString(true);
				}
				queryString = "SELECT a.id, a.name, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_orgnztn a ";
				if (null != searchParameterString && searchParameterString.length() != 0) {
					queryString = queryString + " WHERE " + searchParameterString;
				}
			} else {
				if (null != requestParameter.getOrganizationModel()) {
					searchParameterString = requestParameter.getOrganizationModel().generateSearchQueryString(false);
				}
				queryString = "SELECT a.id, a.name, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_orgnztn a, rgt_usr b WHERE a.id = b.orgnztn_id AND b.id = ? ";
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
			list = CommonUtilities.getModelListFromResultSet(resultSet, new OrganizationModel());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return list;
	}

	public OrganizationModel createOrganization(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".createOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		OrganizationModel response = new OrganizationModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "INSERT INTO rgt_orgnztn (id, name, crtn_dt, crtd_by_usr_id, mdfctn_dt, mdfd_by_usr_id, mdfctn_cmnt) VALUES (rgt_orgnztn_seq.NEXTVAL, ?, SYSDATE, ?, SYSDATE, ?, ?)";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getOrganizationModel().getName());
			preparedStatement.setLong(2, requestParameter.getAccessUserModel().getId());
			preparedStatement.setLong(3, requestParameter.getAccessUserModel().getId());
			preparedStatement.setString(4, Constants.CREATION_COMMENT);
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.CREATION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.CREATION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_ORGNZTN_UK1")) {
				Exception e = new Exception("Organization name should be unique. '"
						+ requestParameter.getOrganizationModel().getName() + "' is already being used.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get Organization with ID
		response = new OrganizationModel();
		try {
			String queryString = "SELECT id FROM rgt_orgnztn WHERE name = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getOrganizationModel().getName());
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

	public OrganizationModel updateOrganization(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".updateOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		OrganizationModel response = new OrganizationModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "UPDATE rgt_orgnztn SET mdfctn_dt = SYSDATE "
					+ requestParameter.getOrganizationModel().generateUpdateQueryString() + ", mdfd_by_usr_id = "
					+ requestParameter.getAccessUserModel().getId() + " WHERE id = "
					+ requestParameter.getOrganizationModel().getId();
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
			if (sqe.getMessage().contains("RGT_ORGNZTN_UK1")) {
				Exception e = new Exception("Organization name should be unique. '"
						+ requestParameter.getOrganizationModel().getName() + "' is already being used.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get Organization with ID
		response = new OrganizationModel();
		response.setId(requestParameter.getOrganizationModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public OrganizationModel deleteOrganization(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		OrganizationModel response = new OrganizationModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "DELETE FROM rgt_orgnztn WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requestParameter.getOrganizationModel().getId());
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.DELETION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.DELETION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_USR_FK3")) {
				Exception e = new Exception(
						"Organization has user(s) related to it. It can not be deleted until related users are deleted.");
				throw e;
			} else if (sqe.getMessage().contains("RGT_DT_ENTY_FK3")) {
				Exception e = new Exception(
						"Organization has data entities related to it. It can not be deleted until related data entities are deleted.");
				throw e;
			} else if (sqe.getMessage().contains("RGT_RQRMNT_FK3")) {
				Exception e = new Exception(
						"Organization has requirement(s) related to it. It can not be deleted until related requirement(s) are deleted.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get Organization with ID
		response = new OrganizationModel();
		response.setId(requestParameter.getOrganizationModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}
}
