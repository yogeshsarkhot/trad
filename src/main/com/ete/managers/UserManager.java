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
import main.com.ete.model.RequestParameterModel;
import main.com.ete.model.UserModel;

public class UserManager {

	private final static Logger LOGGER = Logger.getLogger(UserManager.class.getName());
	private final static String CLASS_NAME = UserManager.class.getName();

	public static boolean isActionAllowed(RequestParameterModel requestParameter, String model, String action)
			throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".isActionAllowed";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, requestParameter + model + action);
		boolean isActionAllowed = false;
		UserModel user = requestParameter.getAccessUserModel();
		if (isSuperUser(user)) {
			isActionAllowed = true;
		} else {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String queryString = new String();
			try {
				String columnName = new String();
				if (Constants.MODEL_REQUIREMENT.equalsIgnoreCase(model)) {
					columnName = "rqrmnt_";
				} else if (Constants.MODEL_DATA_ENTITY.equalsIgnoreCase(model)) {
					columnName = "dt_enty_";
				} else {
					// Add any further access rules for other model objects
					isActionAllowed = false;
					LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isActionAllowed);
					return isActionAllowed;
				}
				if (Constants.ACCESS_CREATE.equalsIgnoreCase(action)) {
					columnName = columnName + "crt";
				} else if (Constants.ACCESS_READ.equalsIgnoreCase(action)) {
					columnName = columnName + "rd";
				} else if (Constants.ACCESS_UPDATE.equalsIgnoreCase(action)) {
					columnName = columnName + "updt";
				} else if (Constants.ACCESS_DELETE.equalsIgnoreCase(action)) {
					columnName = columnName + "dlt";
				} else {
					// Add any further access rules for other actions
					isActionAllowed = false;
					LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isActionAllowed);
					return isActionAllowed;
				}
				queryString = "SELECT " + columnName + " FROM rgt_usr WHERE id = ?";
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(queryString);
				preparedStatement.setLong(1, user.getId());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					String columnValue = resultSet.getString(columnName);
					if (Constants.YES.equalsIgnoreCase(columnValue)) {
						isActionAllowed = true;
					} else {
						isActionAllowed = false;
					}
				}
			} catch (Exception e) {
				isActionAllowed = false;
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw e;
			} finally {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
			if (isActionAllowed
					&& (Constants.MODEL_REQUIREMENT.equalsIgnoreCase(model)
							|| Constants.MODEL_DATA_ENTITY.equalsIgnoreCase(model))
					&& (Constants.ACCESS_CREATE.equalsIgnoreCase(action)
							|| Constants.ACCESS_UPDATE.equalsIgnoreCase(action)
							|| Constants.ACCESS_DELETE.equalsIgnoreCase(action))) {
				long organizationIDForAccessUser = 0;
				long organizationIDForModel = 0;
				try {
					queryString = "SELECT orgnztn_id FROM rgt_usr WHERE id = ?";
					connection = ConnectionManager.getConnection();
					preparedStatement = connection.prepareStatement(queryString);
					preparedStatement.setLong(1, user.getId());
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						organizationIDForAccessUser = resultSet.getLong("orgnztn_id");
					}
				} catch (Exception e) {
					isActionAllowed = false;
					LOGGER.log(Level.SEVERE, e.getMessage());
					throw e;
				} finally {
					ConnectionManager.closeAll(resultSet, preparedStatement, connection);
				}
				if (Constants.ACCESS_UPDATE.equalsIgnoreCase(action)
						|| Constants.ACCESS_DELETE.equalsIgnoreCase(action)) {
					try {
						if (Constants.MODEL_DATA_ENTITY.equalsIgnoreCase(model)) {
							queryString = "SELECT orgnztn_id FROM rgt_dt_enty WHERE id = ?";
						} else if (Constants.MODEL_REQUIREMENT.equalsIgnoreCase(model)) {
							queryString = "SELECT orgnztn_id FROM rgt_rqrmnt WHERE id = ?";
						}
						connection = ConnectionManager.getConnection();
						preparedStatement = connection.prepareStatement(queryString);
						if (Constants.MODEL_DATA_ENTITY.equalsIgnoreCase(model)) {
							preparedStatement.setLong(1, requestParameter.getDataEntityModel().getId());
						} else if (Constants.MODEL_REQUIREMENT.equalsIgnoreCase(model)) {
							preparedStatement.setLong(1, requestParameter.getRequirementModel().getId());
						}
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							organizationIDForModel = resultSet.getLong("orgnztn_id");
						}
					} catch (Exception e) {
						isActionAllowed = false;
						LOGGER.log(Level.SEVERE, e.getMessage());
						throw e;
					} finally {
						ConnectionManager.closeAll(resultSet, preparedStatement, connection);
					}
				} else if (Constants.ACCESS_CREATE.equalsIgnoreCase(action)) {
					// In case action is CREATE
					if (Constants.MODEL_DATA_ENTITY.equalsIgnoreCase(model)) {
						organizationIDForModel = requestParameter.getDataEntityModel().getOrganizationId();
					} else if (Constants.MODEL_REQUIREMENT.equalsIgnoreCase(model)) {
						organizationIDForModel = requestParameter.getRequirementModel().getOrganizationId();
					}
				}
				if (organizationIDForModel == organizationIDForAccessUser) {
					isActionAllowed = true;
				} else {
					// User doesn't access to CRUD the data entity
					// in the organization provided in data entity.
					isActionAllowed = false;
					LOGGER.log(Level.INFO, "User does not have access to " + action + " the " + model
							+ " in the organization provided in " + model);
				}
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isActionAllowed);
		return isActionAllowed;
	}

	public static boolean isSuperUser(UserModel user) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".isSuperUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, user);
		boolean isSuperUser = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "SELECT is_spr_usr FROM rgt_usr WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, user.getId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String isSprUsr = resultSet.getString("is_spr_usr");
				if (Constants.YES.equalsIgnoreCase(isSprUsr)) {
					isSuperUser = true;
				}
			}
		} catch (Exception e) {
			isSuperUser = false;
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isSuperUser);
		return isSuperUser;
	}

	@SuppressWarnings({ "rawtypes" })
	public ArrayList getUsers(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getUsers";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		ArrayList list = new ArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = new String();
			String searchParameterString = new String();
			if (UserManager.isSuperUser(requestParameter.getAccessUserModel())) {
				if (null != requestParameter.getUserModel()) {
					searchParameterString = requestParameter.getUserModel().generateSearchQueryString(true);
				}
				queryString = "SELECT a.id, a.name, a.usrnm, a.pswrd, a.rqrmnt_crt, a.rqrmnt_rd, a.rqrmnt_updt, a.rqrmnt_dlt, a.dt_enty_crt, a.dt_enty_rd, a.dt_enty_updt, a.dt_enty_dlt, a.is_spr_usr, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_usr a ";
				if (null != searchParameterString && searchParameterString.length() != 0) {
					queryString = queryString + " WHERE " + searchParameterString;
				}
			} else {
				if (null != requestParameter.getUserModel()) {
					searchParameterString = requestParameter.getUserModel().generateSearchQueryString(false);
				}
				queryString = "SELECT a.id, a.name, a.usrnm, a.pswrd, a.rqrmnt_crt, a.rqrmnt_rd, a.rqrmnt_updt, a.rqrmnt_dlt, a.dt_enty_crt, a.dt_enty_rd, a.dt_enty_updt, a.dt_enty_dlt, a.is_spr_usr, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_usr a, rgt_usr b WHERE a.orgnztn_id = b.orgnztn_id AND b.id = ? ";
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
			list = CommonUtilities.getModelListFromResultSet(resultSet, new UserModel());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return list;
	}

	public UserModel createUser(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".createUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		UserModel response = new UserModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "INSERT INTO rgt_usr (id, name, usrnm, pswrd, rqrmnt_crt, rqrmnt_rd, rqrmnt_updt, rqrmnt_dlt, dt_enty_crt, dt_enty_rd, dt_enty_updt, dt_enty_dlt, is_spr_usr, orgnztn_id, crtn_dt, crtd_by_usr_id, mdfctn_dt, mdfd_by_usr_id, mdfctn_cmnt) VALUES (rgt_usr_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?, ?)";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getUserModel().getName());
			preparedStatement.setString(2, requestParameter.getUserModel().getUsername());
			preparedStatement.setString(3, requestParameter.getUserModel().getPassword());
			preparedStatement.setString(4, requestParameter.getUserModel().getRequirementAccessCreate());
			preparedStatement.setString(5, requestParameter.getUserModel().getRequirementAccessRead());
			preparedStatement.setString(6, requestParameter.getUserModel().getRequirementAccessUpdate());
			preparedStatement.setString(7, requestParameter.getUserModel().getRequirementAccessDelete());
			preparedStatement.setString(8, requestParameter.getUserModel().getDataEntityAccessCreate());
			preparedStatement.setString(9, requestParameter.getUserModel().getDataEntityAccessRead());
			preparedStatement.setString(10, requestParameter.getUserModel().getDataEntityAccessUpdate());
			preparedStatement.setString(11, requestParameter.getUserModel().getDataEntityAccessDelete());
			preparedStatement.setString(12, requestParameter.getUserModel().getIsSuperUser());
			preparedStatement.setLong(13, requestParameter.getUserModel().getOrganizationId());
			preparedStatement.setLong(14, requestParameter.getAccessUserModel().getId());
			preparedStatement.setLong(15, requestParameter.getAccessUserModel().getId());
			preparedStatement.setString(16, Constants.CREATION_COMMENT);
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.CREATION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.CREATION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_USR_UK1")) {
				Exception e = new Exception("Username should be unique within organization. '"
						+ requestParameter.getUserModel().getUsername()
						+ "' is already being used within organization.");
				throw e;
			}
			// RGT_USR_FK1
			// RGT_USR_FK2
			// RGT_USR_FK3
			// RGT_USR_CHK1
			// RGT_USR_CHK2
			// RGT_USR_CHK3
			// RGT_USR_CHK4
			// RGT_USR_CHK5
			// RGT_USR_CHK6
			// RGT_USR_CHK7
			// RGT_USR_CHK8
			// RGT_USR_CHK9
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get User with ID
		response = new UserModel();
		try {
			String queryString = "SELECT id FROM rgt_usr WHERE usrnm = ? AND orgnztn_id = ? ";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getUserModel().getUsername());
			preparedStatement.setLong(2, requestParameter.getUserModel().getOrganizationId());
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

	public UserModel updateUser(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".updateUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		UserModel response = new UserModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "UPDATE rgt_usr SET mdfctn_dt = SYSDATE "
					+ requestParameter.getUserModel().generateUpdateQueryString() + ", mdfd_by_usr_id = "
					+ requestParameter.getAccessUserModel().getId() + " WHERE id = "
					+ requestParameter.getUserModel().getId();
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
			if (sqe.getMessage().contains("RGT_USR_UK1")) {
				Exception e = new Exception("Username should be unique within organization. '"
						+ requestParameter.getUserModel().getUsername()
						+ "' is already being used within organization.");
				throw e;
			}
			// RGT_USR_FK1
			// RGT_USR_FK2
			// RGT_USR_FK3
			// RGT_USR_CHK1
			// RGT_USR_CHK2
			// RGT_USR_CHK3
			// RGT_USR_CHK4
			// RGT_USR_CHK5
			// RGT_USR_CHK6
			// RGT_USR_CHK7
			// RGT_USR_CHK8
			// RGT_USR_CHK9
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get User with ID
		response = new UserModel();
		response.setId(requestParameter.getUserModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public UserModel deleteUser(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		UserModel response = new UserModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "DELETE FROM rgt_usr WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requestParameter.getUserModel().getId());
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
						"User has organization(s) related to it. It can not be deleted until related organization(s) are deleted.");
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get User with ID
		response = new UserModel();
		response.setId(requestParameter.getUserModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public static boolean isValidUserId(long userId) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".isValidUserId";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidUserId = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "SELECT COUNT(1) AS usr_count FROM rgt_usr WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, userId);
			resultSet = preparedStatement.executeQuery();
			int count = 0;
			while (resultSet.next()) {
				count = resultSet.getInt("usr_count");
			}
			if (count > 0) {
				isValidUserId = true;
			} else {
				isValidUserId = false;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidUserId);
		return isValidUserId;
	}
}