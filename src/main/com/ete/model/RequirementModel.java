package main.com.ete.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import main.com.ete.commom.Constants;

public class RequirementModel extends BaseModel {

	private final static Logger LOGGER = Logger.getLogger(RequirementModel.class.getName());
	private final static String CLASS_NAME = RequirementModel.class.getName();

	private long organizationId;
	private String requirement;
	private List<DataEntityModel> dataEntityList;

	public RequirementModel() {
		dataEntityList = new ArrayList<DataEntityModel>();
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public List<DataEntityModel> getDataEntityList() {
		return dataEntityList;
	}

	public void setDataEntityList(List<DataEntityModel> dataEntityList) {
		this.dataEntityList = dataEntityList;
	}

	@Override
	public String toString() {
		StringBuffer toStringValue = new StringBuffer(super.toString());
		toStringValue.append(" [ requirement = ");
		toStringValue.append(requirement);
		toStringValue.append(" , organizationId = ");
		toStringValue.append(organizationId);
		for (int i = 0; i < dataEntityList.size(); i++) {
			DataEntityModel dataEntity = dataEntityList.get(i);
			toStringValue.append(" , data entity " + i + " = ");
			toStringValue.append(dataEntity);
		}
		toStringValue.append(" ] ");
		return toStringValue.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = super.convertToJSONObject();
		jsonObject.put("requirement", this.requirement);
		jsonObject.put("organization id", this.organizationId);
		JSONArray dataEntityArray = new JSONArray();
		for (int i = 0; i < dataEntityList.size(); i++) {
			DataEntityModel dataEntityModel = dataEntityList.get(i);
			JSONObject dataEntityJSONObject = dataEntityModel.convertToJSONObject();
			dataEntityArray.put(dataEntityJSONObject);
		}
		jsonObject.put("data entity list", dataEntityArray);
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		super.setResultSetValues(resultSet);
		requirement = resultSet.getString("rqrmnt_txt");
		organizationId = resultSet.getLong("orgnztn_id");
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
		if (null != requirement) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.rqrmnt_txt) LIKE LOWER('%");
			queryStringBuffer.append(requirement);
			queryStringBuffer.append("%') ");
		}
		if (null != modificationComment) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.mdfctn_cmnt) LIKE LOWER('%");
			queryStringBuffer.append(modificationComment);
			queryStringBuffer.append("%') ");
		}
		if (0 != organizationId) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" a.orgnztn_id = ");
			queryStringBuffer.append(organizationId);
			queryStringBuffer.append(" ");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, queryStringBuffer);
		return queryStringBuffer.toString();
	}

	public String generateUpdateQueryString() {
		final String METHOD_NAME = CLASS_NAME + ".generateUpdateQueryString";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		StringBuffer queryStringBuffer = new StringBuffer(super.generateUpdateQueryString());
		if (null != requirement) {
			queryStringBuffer.append(", rqrmnt_txt = '");
			queryStringBuffer.append(requirement);
			queryStringBuffer.append("' ");
		}
		if (organizationId > 0) {
			queryStringBuffer.append(", orgnztn_id = ");
			queryStringBuffer.append(organizationId);
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
			if (null != requirement && requirement.length() != 0 && organizationId > 0) {
				isValidObjectForCreation = true;
			} else {
				isValidObjectForCreation = false;
			}
			if (isValidObjectForCreation) {
				for (DataEntityModel dataEntityModel : dataEntityList) {
					isValidObjectForCreation = dataEntityModel.isValidObjectForReference();
					if (!isValidObjectForCreation) {
						break;
					}
				}
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
			if (null == requirement && organizationId <= 0) {
				isValidObjectForUpdate = false;
			} else {
				isValidObjectForUpdate = true;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}
}
