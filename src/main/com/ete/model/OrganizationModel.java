package main.com.ete.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

import main.com.ete.commom.Constants;

@XmlRootElement
public class OrganizationModel extends BaseModel {

	private final static Logger LOGGER = Logger.getLogger(OrganizationModel.class.getName());
	private final static String CLASS_NAME = OrganizationModel.class.getName();

	private String name;

	public OrganizationModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuffer toStringValue = new StringBuffer(super.toString());
		toStringValue.append(" [ name = ");
		toStringValue.append(name);
		toStringValue.append(" ] ");
		return toStringValue.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = super.convertToJSONObject();
		jsonObject.put("name", this.name);
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		super.setResultSetValues(resultSet);
		name = resultSet.getString("name");
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
		if (null != name) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.name) LIKE LOWER('%");
			queryStringBuffer.append(name);
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
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, queryStringBuffer);
		return queryStringBuffer.toString();
	}

	public String generateUpdateQueryString() {
		final String METHOD_NAME = CLASS_NAME + ".generateUpdateQueryString";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		StringBuffer queryStringBuffer = new StringBuffer(super.generateUpdateQueryString());
		if (null != name) {
			queryStringBuffer.append(", name = '");
			queryStringBuffer.append(name);
			queryStringBuffer.append("' ");
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
			if (null != name && name.length() != 0) {
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
			if (null != name && name.length() != 0) {
				isValidObjectForUpdate = true;
			} else {
				isValidObjectForUpdate = false;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}
}
