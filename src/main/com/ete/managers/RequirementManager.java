package main.com.ete.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.com.ete.commom.CommonUtilities;
import main.com.ete.commom.ConnectionManager;
import main.com.ete.commom.Constants;
import main.com.ete.model.DataEntityModel;
import main.com.ete.model.RequestParameterModel;
import main.com.ete.model.RequirementModel;

public class RequirementManager {

	private final static Logger LOGGER = Logger.getLogger(RequirementManager.class.getName());
	private final static String CLASS_NAME = RequirementManager.class.getName();

	@SuppressWarnings({ "rawtypes" })
	public ArrayList getRequirements(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getRequirements";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		ArrayList list = new ArrayList();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = new String();
			String searchParameterString = new String();
			if (UserManager.isSuperUser(requestParameter.getAccessUserModel())) {
				if (null != requestParameter.getRequirementModel()) {
					searchParameterString = requestParameter.getRequirementModel().generateSearchQueryString(true);
				}
				queryString = "SELECT a.id, a.rqrmnt_txt, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_rqrmnt a ";
				if (null != searchParameterString && searchParameterString.length() != 0) {
					queryString = queryString + " WHERE " + searchParameterString;
				}
			} else {
				if (null != requestParameter.getRequirementModel()) {
					searchParameterString = requestParameter.getRequirementModel().generateSearchQueryString(false);
				}
				queryString = "SELECT a.id, a.rqrmnt_txt, a.orgnztn_id, a.crtn_dt, a.crtd_by_usr_id, a.mdfctn_dt, a.mdfd_by_usr_id, a.mdfctn_cmnt FROM rgt_rqrmnt a, rgt_usr b WHERE a.orgnztn_id = b.orgnztn_id AND b.id = ? ";
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
			list = CommonUtilities.getModelListFromResultSet(resultSet, new RequirementModel());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return list;
	}

	public RequirementModel createRequirement(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".createRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		RequirementModel response = new RequirementModel();
		if (checkForDataEntityOrganization(requestParameter, Constants.ACCESS_CREATE)) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				String insertRequirementQueryString = "INSERT INTO rgt_rqrmnt (id, rqrmnt_txt, orgnztn_id, crtn_dt, crtd_by_usr_id, mdfctn_dt, mdfd_by_usr_id, mdfctn_cmnt) VALUES (rgt_rqrmnt_seq.NEXTVAL, ?, ?, SYSDATE, ?, SYSDATE, ?, ?)";
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(insertRequirementQueryString);
				preparedStatement.setString(1, requestParameter.getRequirementModel().getRequirement());
				preparedStatement.setLong(2, requestParameter.getRequirementModel().getOrganizationId());
				preparedStatement.setLong(3, requestParameter.getAccessUserModel().getId());
				preparedStatement.setLong(4, requestParameter.getAccessUserModel().getId());
				preparedStatement.setString(5, Constants.CREATION_COMMENT);
				int requirementRowCount = preparedStatement.executeUpdate();
				if (requirementRowCount != 1) {
					throw new Exception("Requirement Creation Failed.");
				}
				connection.commit();
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
				LOGGER.log(Level.INFO, "Requirement Created Successfully.");
				// response = Get Requirement with ID
				response = new RequirementModel();
				response.setId(getNewlyCreatedRequirementId(requestParameter));
				try {
					// Create Data Entity Requirement Mapping for the whole list
					for (DataEntityModel dataEntityModel : requestParameter.getRequirementModel().getDataEntityList()) {
						boolean isDataEntityRequirementMappingCreated = DataEntityRequirementMappingManager
								.createDataEntityRequirementMapping(dataEntityModel.getId(), response.getId(),
										requestParameter.getAccessUserModel().getId());
						if (isDataEntityRequirementMappingCreated) {
							LOGGER.log(Level.INFO, "Data Entity - Requirement Mapping Created Successfully.");
						}
					}
				} catch (SQLException sqe) {
					// Delete requirement as the mapping creation failed
					try {
						deleteRequirementWithDependentDataEntity(response.getId());
					} catch (SQLException sq) {
						LOGGER.log(Level.SEVERE, sq.getMessage());
						throw sq;
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, e.getMessage());
						throw e;
					} finally {
						ConnectionManager.closeAll(resultSet, preparedStatement, connection);
					}
					LOGGER.log(Level.SEVERE, "Data Entity - Requirement Mapping Creation Failed.");
					throw new Exception("Data Entity - Requirement Mapping Creation Failed.");
				}
			} catch (SQLException sqe) {
				LOGGER.log(Level.SEVERE, sqe.getMessage());
				if (sqe.getMessage().contains("RGT_RQRMNT_UK1")) {
					Exception e = new Exception("Requirement Text should be unique within organization. '"
							+ requestParameter.getRequirementModel().getRequirement()
							+ "' is already being used within organization.");
					throw e;
				}
				throw sqe;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw e;
			} finally {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
		} else {
			response = null;
			Exception e = new Exception(Constants.CREATION_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public RequirementModel updateRequirement(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".updateRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		RequirementModel response = new RequirementModel();
		if (checkForDataEntityOrganization(requestParameter, Constants.ACCESS_UPDATE)) {
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				String queryString = "UPDATE rgt_rqrmnt SET mdfctn_dt = SYSDATE "
						+ requestParameter.getRequirementModel().generateUpdateQueryString() + ", mdfd_by_usr_id = "
						+ requestParameter.getAccessUserModel().getId() + " WHERE id = "
						+ requestParameter.getRequirementModel().getId();
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(queryString);
				int rowCount = preparedStatement.executeUpdate();
				if (rowCount != 1) {
					LOGGER.log(Level.SEVERE, Constants.UPDATE_COMMENT_UNSUCCESSFUL);
					Exception e = new Exception(Constants.UPDATE_COMMENT_UNSUCCESSFUL);
					throw e;
				}
				// Add or Delete Data Entity Mappings
				ArrayList<Long> currentDataEntityIdList = DataEntityRequirementMappingManager
						.getDataEntityIdListForRequirement(requestParameter.getRequirementModel().getId());
				List<DataEntityModel> newDataEntityModelList = requestParameter.getRequirementModel()
						.getDataEntityList();
				ArrayList<Long> newDataEntityIdList = new ArrayList<Long>();
				for (DataEntityModel dataEntityModel : newDataEntityModelList) {
					newDataEntityIdList.add(dataEntityModel.getId());
				}
				for (Long currentDataEntityId : currentDataEntityIdList) {
					// Delete Data Entity Mapping if it is present in current
					// list and not present in new data list
					if (!newDataEntityIdList.contains(currentDataEntityId)) {
						DataEntityRequirementMappingManager.deleteDataEntityRequirementMapping(currentDataEntityId,
								requestParameter.getRequirementModel().getId());
					}
				}
				for (Long newDataEntityId : newDataEntityIdList) {
					// Create Data Entity Mapping if it is not present in
					// current list and it is present in new data list
					if (!currentDataEntityIdList.contains(newDataEntityId)) {
						DataEntityRequirementMappingManager.createDataEntityRequirementMapping(newDataEntityId,
								requestParameter.getRequirementModel().getId(),
								requestParameter.getAccessUserModel().getId());
					}
				}
				connection.commit();
			} catch (SQLException sqe) {
				LOGGER.log(Level.SEVERE, sqe.getMessage());
				throw sqe;
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw e;
			} finally {
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
			// response = Get Requirement with ID
			response = new RequirementModel();
			response.setId(requestParameter.getRequirementModel().getId());
			// Check if data entities are needed - to be added, deleted or
			// updated
		} else {
			response = null;
			Exception e = new Exception(Constants.UPDATE_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	public RequirementModel deleteRequirement(RequestParameterModel requestParameter) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		RequirementModel response = new RequirementModel();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "DELETE FROM rgt_rqrmnt WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requestParameter.getRequirementModel().getId());
			int rowCount = preparedStatement.executeUpdate();
			if (rowCount != 1) {
				LOGGER.log(Level.SEVERE, Constants.DELETION_COMMENT_UNSUCCESSFUL);
				Exception e = new Exception(Constants.DELETION_COMMENT_UNSUCCESSFUL);
				throw e;
			}
			connection.commit();
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			if (sqe.getMessage().contains("RGT_DT_ENTY_RQRMNT_MP_FK2")) {
				Exception e = new Exception(
						"Requirement has references to data entities. It can not be deleted until related requirement mapping(s) are deleted from requirement text.");
				LOGGER.log(Level.SEVERE, e.getMessage());
				throw e;
			}
			throw sqe;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		// response = Get requirement with ID
		response = new RequirementModel();
		response.setId(requestParameter.getRequirementModel().getId());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return response;
	}

	private boolean checkForDataEntityOrganization(RequestParameterModel requestParameter, String action)
			throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".checkForDataEntityOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean checkForDataEntityOrganization = true;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		// Retrieve Data Entities to check if they are from the same
		// organization
		try {
			long requirementOrganizationId = 0;
			if (Constants.ACCESS_CREATE.equalsIgnoreCase(action)) {
				requirementOrganizationId = requestParameter.getRequirementModel().getOrganizationId();
			} else if (Constants.ACCESS_UPDATE.equalsIgnoreCase(action)) {
				requirementOrganizationId = getOrganizationIdForRequirementId(
						requestParameter.getRequirementModel().getId());
			}
			for (DataEntityModel dataEntityModel : requestParameter.getRequirementModel().getDataEntityList()) {
				checkForDataEntityOrganization = false;
				boolean isRecordPresent = false;
				String queryString = "SELECT a.orgnztn_id FROM rgt_dt_enty a WHERE a.id = ?";
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(queryString);
				preparedStatement.setLong(1, dataEntityModel.getId());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					isRecordPresent = true;
					long dataEntityModelOrganizationId = resultSet.getLong("orgnztn_id");
					if (dataEntityModelOrganizationId != requirementOrganizationId) {
						checkForDataEntityOrganization = false;
						ConnectionManager.closeAll(resultSet, preparedStatement, connection);
						throw new Exception("Organization ID " + dataEntityModelOrganizationId + " of Data Entity ID "
								+ dataEntityModel.getId() + " does not match Organization ID of Requirement "
								+ requestParameter.getRequirementModel().getOrganizationId());
					} else {
						checkForDataEntityOrganization = true;
					}
				}
				if (!isRecordPresent) {
					checkForDataEntityOrganization = false;
					throw new Exception("Data Entity ID " + dataEntityModel.getId() + " is not valid.");
				}
				ConnectionManager.closeAll(resultSet, preparedStatement, connection);
			}
			LOGGER.log(Level.INFO, "All Data Entities are valid.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return checkForDataEntityOrganization;
	}

	private long getNewlyCreatedRequirementId(RequestParameterModel requestParameter) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".getNewlyCreatedRequirementId";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		long requirementId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "SELECT id FROM rgt_rqrmnt WHERE rqrmnt_txt = ? AND orgnztn_id = ? ";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setString(1, requestParameter.getRequirementModel().getRequirement());
			preparedStatement.setLong(2, requestParameter.getRequirementModel().getOrganizationId());
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				requirementId = resultSet.getLong("id");
			}
			LOGGER.log(Level.INFO, "New Created Requirement ID : " + requirementId);
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			throw sqe;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return requirementId;
	}

	private boolean deleteRequirementWithDependentDataEntity(long requirementId) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".deleteRequirementWithDependentDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		LOGGER.log(Level.INFO,
				"Deleted newly created requirement due to problem in creating data entity-requirement mapping.");
		boolean isDeletedRequirementWithDependentDataEntity = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (DataEntityRequirementMappingManager.deleteDataEntityRequirementMappingForRequirement(requirementId)) {
				String queryString = "DELETE FROM rgt_rqrmnt WHERE id = ?";
				connection = ConnectionManager.getConnection();
				preparedStatement = connection.prepareStatement(queryString);
				preparedStatement.setLong(1, requirementId);
				int rowCount = preparedStatement.executeUpdate();
				if (rowCount != 1) {
					LOGGER.log(Level.SEVERE, Constants.DELETION_COMMENT_UNSUCCESSFUL);
					Exception e = new Exception(Constants.DELETION_COMMENT_UNSUCCESSFUL);
					throw e;
				}
				connection.commit();
				isDeletedRequirementWithDependentDataEntity = true;
			}
			LOGGER.log(Level.INFO, "Requirement with related Data Entity Mapping deleted successfully.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return isDeletedRequirementWithDependentDataEntity;
	}

	private long getOrganizationIdForRequirementId(long requirementId) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".getOrganizationIdForRequirementId";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		long requirementOrganizationId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String queryString = "SELECT orgnztn_id FROM rgt_rqrmnt WHERE id = ?";
			connection = ConnectionManager.getConnection();
			preparedStatement = connection.prepareStatement(queryString);
			preparedStatement.setLong(1, requirementId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				requirementOrganizationId = resultSet.getLong("orgnztn_id");
			}
			LOGGER.log(Level.INFO,
					"Organization ID is " + requirementOrganizationId + " for Requirement ID : " + requirementId);
		} catch (SQLException sqe) {
			LOGGER.log(Level.SEVERE, sqe.getMessage());
			throw sqe;
		} finally {
			ConnectionManager.closeAll(resultSet, preparedStatement, connection);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return requirementOrganizationId;
	}
}
