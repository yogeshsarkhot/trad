package main.com.ete.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

import main.com.ete.commom.Constants;

@XmlRootElement
public class DataEntityModel extends BaseModel {

	private final static Logger LOGGER = Logger.getLogger(DataEntityModel.class.getName());
	private final static String CLASS_NAME = DataEntityModel.class.getName();

	private long organizationId;
	private String name;
	// private List<String> columnList;
	// private int columnCount;
	//
	// public DataEntityModel() {
	// columnCount = Constants.DATA_ENTITY_COLUMN_COUNT;
	// columnList = Arrays.asList(new
	// String[Constants.DATA_ENTITY_COLUMN_COUNT]);
	// }

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// public List<String> getColumnList() {
	// return columnList;
	// }
	//
	// public void setColumnList(List<String> columnList) {
	// this.columnList = columnList;
	// }
	//
	// public long getColumnCount() {
	// return columnCount;
	// }
	//
	// public void setColumnCount(int columnCount) {
	// this.columnCount = columnCount;
	// }

	@Override
	public String toString() {
		StringBuffer toStringValue = new StringBuffer();
		toStringValue.append(super.toString());
		toStringValue.append(" [name = ");
		toStringValue.append(name);
		toStringValue.append(" , organizationId = ");
		toStringValue.append(organizationId);
		// toStringValue.append(" columnCount = ");
		// toStringValue.append(columnCount);
		// int counter = 0;
		// for (String columnValue : columnList) {
		// counter++;
		// toStringValue.append(" column" + counter + " = ");
		// toStringValue.append(columnValue);
		// }
		toStringValue.append(" ] ");
		return toStringValue.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = super.convertToJSONObject();
		jsonObject.put("name", this.name);
		jsonObject.put("organization id", this.organizationId);
		// int counter = 0;
		// for (String columnValue : columnList) {
		// counter++;
		// jsonObject.put("column" + counter, columnValue);
		// }
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		super.setResultSetValues(resultSet);
		name = resultSet.getString("name");
		organizationId = resultSet.getLong("orgnztn_id");
		// for (int i = 0; i < Constants.DATA_ENTITY_COLUMN_COUNT; i++) {
		// columnList.set(i, resultSet.getString("clmn" + (i + 1)));
		// }
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
	}

	public String generateSearchQueryString(boolean isSuperUserQuery) {
		final String METHOD_NAME = CLASS_NAME + ".generateSearchQueryString";
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
		if (0 != organizationId) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" a.orgnztn_id = ");
			queryStringBuffer.append(organizationId);
			queryStringBuffer.append(" ");
		}
		// int counter = 0;
		// for (String columnValue : columnList) {
		// counter++;
		// if (null != columnValue) {
		// if (seachParameterCount > 1) {
		// queryStringBuffer.append(" AND ");
		// }
		// queryStringBuffer.append(" LOWER(a.clmn" + counter + ") LIKE
		// LOWER('%");
		// queryStringBuffer.append(columnValue);
		// queryStringBuffer.append("%') ");
		// }
		// }
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
			if (null != name && name.length() != 0 && organizationId > 0) {
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
			if (null == name && organizationId <= 0) {
				isValidObjectForUpdate = false;
			} else {
				isValidObjectForUpdate = true;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}
}
