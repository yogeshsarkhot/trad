package main.com.ete.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import main.com.ete.commom.Constants;

public class DataEntityRequirementMapModel extends BaseModel {

	private final static Logger LOGGER = Logger.getLogger(DataEntityRequirementMapModel.class.getName());
	private final static String CLASS_NAME = DataEntityRequirementMapModel.class.getName();

	private long dataEntityId;
	private long requirementId;

	public DataEntityRequirementMapModel() {
	}

	public long getDataEntityId() {
		return dataEntityId;
	}

	public void setDataEntityId(long dataEntityId) {
		this.dataEntityId = dataEntityId;
	}

	public long getRequirementId() {
		return requirementId;
	}

	public void setRequirementId(long requirementId) {
		this.requirementId = requirementId;
	}

	@Override
	public String toString() {
		StringBuffer toStringValue = new StringBuffer(super.toString());
		toStringValue.append(" [ dataEntityId = ");
		toStringValue.append(dataEntityId);
		toStringValue.append(" , requirementId = ");
		toStringValue.append(requirementId);
		toStringValue.append(" ] ");
		return toStringValue.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = super.convertToJSONObject();
		jsonObject.put("data entity id", this.dataEntityId);
		jsonObject.put("requirement id", this.requirementId);
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		super.setResultSetValues(resultSet);
		dataEntityId = resultSet.getLong("dt_enty_id");
		requirementId = resultSet.getLong("rqrmnt_id");
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
	}

	public String generateSearchQueryString(boolean isSuperUserQuery) {
		final String METHOD_NAME = CLASS_NAME + ".getSearchQueryString";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		StringBuffer queryStringBuffer = new StringBuffer();
		int seachParameterCount = 1;
		// This is incremented to include the current access user id
		// parameter update done in main calling method
		if (!isSuperUserQuery) {
			seachParameterCount++;
		}
		if (0 != id) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" a.id = ");
			queryStringBuffer.append(id);
			queryStringBuffer.append(" ");
		}
		if (0 != dataEntityId) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" a.dt_enty_id = ");
			queryStringBuffer.append(dataEntityId);
			queryStringBuffer.append(" ");
		}
		if (0 != requirementId) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" a.rqrmnt_id = ");
			queryStringBuffer.append(requirementId);
			queryStringBuffer.append(" ");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, queryStringBuffer);
		return queryStringBuffer.toString();
	}

	public String generateUpdateQueryString() {
		final String METHOD_NAME = CLASS_NAME + ".generateUpdateQueryString";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		StringBuffer queryStringBuffer = new StringBuffer(super.generateUpdateQueryString());
		if (dataEntityId > 0) {
			queryStringBuffer.append(", dt_enty_id = ");
			queryStringBuffer.append(dataEntityId);
			queryStringBuffer.append(" ");
		}
		if (requirementId > 0) {
			queryStringBuffer.append(", rqrmnt_id = ");
			queryStringBuffer.append(requirementId);
			queryStringBuffer.append(" ");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, queryStringBuffer);
		return queryStringBuffer.toString();
	}

	public boolean isValidObjectForCreation() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForCreation";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForCreation = false;
		isValidObjectForCreation = super.isValidObjectForCreation();
		if (isValidObjectForCreation == true) {
			if (dataEntityId > 0 && requirementId > 0) {
				isValidObjectForCreation = true;
			} else {
				isValidObjectForCreation = false;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForCreation);
		return isValidObjectForCreation;
	}

	public boolean isValidObjectForUpdate() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForUpdate";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForUpdate = false;
		isValidObjectForUpdate = super.isValidObjectForUpdate();
		if (isValidObjectForUpdate == true) {
			if (dataEntityId <= 0 && requirementId <= 0) {
				isValidObjectForUpdate = false;
			} else {
				isValidObjectForUpdate = true;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}
}
