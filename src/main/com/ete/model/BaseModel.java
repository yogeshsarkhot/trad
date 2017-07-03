package main.com.ete.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

import main.com.ete.commom.CommonUtilities;
import main.com.ete.commom.Constants;

@XmlRootElement
public class BaseModel {

	private final static Logger LOGGER = Logger.getLogger(BaseModel.class.getName());
	private final static String CLASS_NAME = BaseModel.class.getName();

	protected long id;
	protected Timestamp creationDate;
	protected long createdByUserId;
	protected Timestamp modificationDate;
	protected long modifiedByUserId;
	protected String modificationComment;

	public BaseModel() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}

	public long getModifiedByUserId() {
		return modifiedByUserId;
	}

	public void setModifiedByUserId(long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}

	public String getModificationComment() {
		return modificationComment;
	}

	public void setModificationComment(String modificationComment) {
		this.modificationComment = modificationComment;
	}

	@Override
	public String toString() {
		StringBuffer toStringBuffer = new StringBuffer();
		toStringBuffer.append("[id = ");
		toStringBuffer.append(id);
		toStringBuffer.append(" , creationDate = ");
		toStringBuffer.append(creationDate);
		toStringBuffer.append(" , createdByUserId = ");
		toStringBuffer.append(createdByUserId);
		toStringBuffer.append(" , modificationDate = ");
		toStringBuffer.append(modificationDate);
		toStringBuffer.append(" , modifiedByUserId = ");
		toStringBuffer.append(modifiedByUserId);
		toStringBuffer.append(" , modificationComment = ");
		toStringBuffer.append(modificationComment);
		toStringBuffer.append(" ]");
		return toStringBuffer.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", this.id);
		jsonObject.put("creation date", CommonUtilities.convertTimeStampToString(this.creationDate));
		jsonObject.put("created by user id", this.createdByUserId);
		jsonObject.put("modification date", CommonUtilities.convertTimeStampToString(this.modificationDate));
		jsonObject.put("modified by user id", this.modifiedByUserId);
		jsonObject.put("modification comment", this.modificationComment);
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		id = resultSet.getLong("id");
		creationDate = resultSet.getTimestamp("crtn_dt");
		createdByUserId = resultSet.getLong("crtd_by_usr_id");
		modificationDate = resultSet.getTimestamp("mdfctn_dt");
		modifiedByUserId = resultSet.getLong("mdfd_by_usr_id");
		modificationComment = resultSet.getString("mdfctn_cmnt");
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
		StringBuffer queryStringBuffer = new StringBuffer();
		if (null != modificationComment) {
			queryStringBuffer.append(", mdfctn_cmnt = '");
			queryStringBuffer.append(modificationComment);
			queryStringBuffer.append("' ");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, queryStringBuffer);
		return queryStringBuffer.toString();
	}

	public void updateSearchParameters(PreparedStatement preparedStatement, String searchParameterString,
			boolean isSuperUserQuery) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".updateSearchParameters";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		if (null != searchParameterString && searchParameterString.length() != 0) {
			int seachParameterCount = 1;
			// This is incremented to include the current access user id
			// parameter update done in main calling method
			if (!isSuperUserQuery) {
				seachParameterCount++;
			}
			// Order of these if-else blocks is critical and it should match the
			// search parameter set order in all places where the search
			// parameters are set i.e. getSearchQueryString method
			if (searchParameterString.contains("id")) {
				preparedStatement.setLong(seachParameterCount++, id);
			}
			if (searchParameterString.contains("mdfctn_cmnt")) {
				preparedStatement.setString(seachParameterCount++, modificationComment);
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
	}

	public boolean isValidObjectForCreation() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForCreation";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForCreation = true;
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForCreation);
		return isValidObjectForCreation;
	}

	public boolean isValidObjectForUpdate() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForUpdate";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForUpdate = false;
		if (id > 0 && null != modificationComment && modificationComment.length() != 0) {
			isValidObjectForUpdate = true;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}

	public boolean isValidObjectForDelete() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForDelete";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForDelete = false;
		if (id > 0) {
			isValidObjectForDelete = true;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForDelete);
		return isValidObjectForDelete;
	}

	public boolean isValidObjectForReference() {
		final String METHOD_NAME = CLASS_NAME + ".isValidObjectForReference";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		boolean isValidObjectForReference = false;
		if (id > 0) {
			isValidObjectForReference = true;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForReference);
		return isValidObjectForReference;
	}
}