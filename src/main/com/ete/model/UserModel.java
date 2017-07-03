package main.com.ete.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlRootElement;

import org.json.JSONObject;

import main.com.ete.commom.Constants;

@XmlRootElement
public class UserModel extends BaseModel {

	private final static Logger LOGGER = Logger.getLogger(UserModel.class.getName());
	private final static String CLASS_NAME = UserModel.class.getName();

	private String name;
	private String username;
	private String password;
	private String requirementAccessCreate;
	private String requirementAccessRead;
	private String requirementAccessUpdate;
	private String requirementAccessDelete;
	private String dataEntityAccessCreate;
	private String dataEntityAccessRead;
	private String dataEntityAccessUpdate;
	private String dataEntityAccessDelete;
	private String isSuperUser;
	private long organizationId;

	public UserModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRequirementAccessCreate() {
		return requirementAccessCreate;
	}

	public void setRequirementAccessCreate(String requirementAccessCreate) {
		this.requirementAccessCreate = requirementAccessCreate;
	}

	public String getRequirementAccessRead() {
		return requirementAccessRead;
	}

	public void setRequirementAccessRead(String requirementAccessRead) {
		this.requirementAccessRead = requirementAccessRead;
	}

	public String getRequirementAccessUpdate() {
		return requirementAccessUpdate;
	}

	public void setRequirementAccessUpdate(String requirementAccessUpdate) {
		this.requirementAccessUpdate = requirementAccessUpdate;
	}

	public String getRequirementAccessDelete() {
		return requirementAccessDelete;
	}

	public void setRequirementAccessDelete(String requirementAccessDelete) {
		this.requirementAccessDelete = requirementAccessDelete;
	}

	public String getDataEntityAccessCreate() {
		return dataEntityAccessCreate;
	}

	public void setDataEntityAccessCreate(String dataEntityAccessCreate) {
		this.dataEntityAccessCreate = dataEntityAccessCreate;
	}

	public String getDataEntityAccessRead() {
		return dataEntityAccessRead;
	}

	public void setDataEntityAccessRead(String dataEntityAccessRead) {
		this.dataEntityAccessRead = dataEntityAccessRead;
	}

	public String getDataEntityAccessUpdate() {
		return dataEntityAccessUpdate;
	}

	public void setDataEntityAccessUpdate(String dataEntityAccessUpdate) {
		this.dataEntityAccessUpdate = dataEntityAccessUpdate;
	}

	public String getDataEntityAccessDelete() {
		return dataEntityAccessDelete;
	}

	public void setDataEntityAccessDelete(String dataEntityAccessDelete) {
		this.dataEntityAccessDelete = dataEntityAccessDelete;
	}

	public String getIsSuperUser() {
		return isSuperUser;
	}

	public void setIsSuperUser(String isSuperUser) {
		this.isSuperUser = isSuperUser;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		StringBuffer toStringValue = new StringBuffer();
		toStringValue.append(super.toString());
		toStringValue.append(" [name = ");
		toStringValue.append(name);
		toStringValue.append(" , username = ");
		toStringValue.append(username);
		toStringValue.append(" , password = ");
		toStringValue.append(password);
		toStringValue.append(" , requirementAccessCreate = ");
		toStringValue.append(requirementAccessCreate);
		toStringValue.append(" , requirementAccessRead = ");
		toStringValue.append(requirementAccessRead);
		toStringValue.append(" , requirementAccessUpdate = ");
		toStringValue.append(requirementAccessUpdate);
		toStringValue.append(" , requirementAccessDelete = ");
		toStringValue.append(requirementAccessDelete);
		toStringValue.append(" , dataEntityAccessCreate = ");
		toStringValue.append(dataEntityAccessCreate);
		toStringValue.append(" , dataEntityAccessRead = ");
		toStringValue.append(dataEntityAccessRead);
		toStringValue.append(" , dataEntityAccessUpdate = ");
		toStringValue.append(dataEntityAccessUpdate);
		toStringValue.append(" , dataEntityAccessDelete = ");
		toStringValue.append(dataEntityAccessDelete);
		toStringValue.append(" , isSuperUser = ");
		toStringValue.append(isSuperUser);
		toStringValue.append(" , organizationId = ");
		toStringValue.append(organizationId);
		toStringValue.append(" ] ");
		return toStringValue.toString();
	}

	public JSONObject convertToJSONObject() {
		final String METHOD_NAME = CLASS_NAME + ".convertToJSONObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject jsonObject = super.convertToJSONObject();
		jsonObject.put("name", this.name);
		jsonObject.put("username", this.username);
		jsonObject.put("password", this.password);
		jsonObject.put("requirement access create", this.requirementAccessCreate);
		jsonObject.put("requirement access read", this.requirementAccessRead);
		jsonObject.put("requirement access update", this.requirementAccessUpdate);
		jsonObject.put("requirement access delete", this.requirementAccessDelete);
		jsonObject.put("data entity access create", this.dataEntityAccessCreate);
		jsonObject.put("data entity access read", this.dataEntityAccessRead);
		jsonObject.put("data entity access update", this.dataEntityAccessUpdate);
		jsonObject.put("data entity access delete", this.dataEntityAccessDelete);
		jsonObject.put("is super user", this.isSuperUser);
		jsonObject.put("organization id", this.organizationId);
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, jsonObject);
		return jsonObject;
	}

	public void setResultSetValues(ResultSet resultSet) throws SQLException {
		final String METHOD_NAME = CLASS_NAME + ".setResultSetValues";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		super.setResultSetValues(resultSet);
		name = resultSet.getString("name");
		username = resultSet.getString("usrnm");
		password = resultSet.getString("pswrd");
		requirementAccessCreate = resultSet.getString("rqrmnt_crt");
		requirementAccessRead = resultSet.getString("rqrmnt_rd");
		requirementAccessUpdate = resultSet.getString("rqrmnt_updt");
		requirementAccessDelete = resultSet.getString("rqrmnt_dlt");
		dataEntityAccessCreate = resultSet.getString("dt_enty_crt");
		dataEntityAccessRead = resultSet.getString("dt_enty_rd");
		dataEntityAccessUpdate = resultSet.getString("dt_enty_updt");
		dataEntityAccessDelete = resultSet.getString("dt_enty_dlt");
		isSuperUser = resultSet.getString("is_spr_usr");
		organizationId = resultSet.getLong("orgnztn_id");
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
		if (null != username) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.usrnm) LIKE LOWER('%");
			queryStringBuffer.append(username);
			queryStringBuffer.append("%') ");
		}
		if (null != requirementAccessCreate) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.rqrmnt_crt) LIKE LOWER('%");
			queryStringBuffer.append(requirementAccessCreate);
			queryStringBuffer.append("%') ");
		}
		if (null != requirementAccessRead) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.rqrmnt_rd) LIKE LOWER('%");
			queryStringBuffer.append(requirementAccessRead);
			queryStringBuffer.append("%') ");
		}
		if (null != requirementAccessUpdate) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.rqrmnt_updt) LIKE LOWER('%");
			queryStringBuffer.append(requirementAccessUpdate);
			queryStringBuffer.append("%') ");
		}
		if (null != requirementAccessDelete) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.rqrmnt_dlt) LIKE LOWER('%");
			queryStringBuffer.append(requirementAccessDelete);
			queryStringBuffer.append("%') ");
		}
		if (null != dataEntityAccessCreate) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.dt_enty_crt) LIKE LOWER('%");
			queryStringBuffer.append(dataEntityAccessCreate);
			queryStringBuffer.append("%') ");
		}
		if (null != dataEntityAccessRead) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.dt_enty_rd) LIKE LOWER('%");
			queryStringBuffer.append(dataEntityAccessRead);
			queryStringBuffer.append("%') ");
		}
		if (null != dataEntityAccessUpdate) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.dt_enty_updt) LIKE LOWER('%");
			queryStringBuffer.append(dataEntityAccessUpdate);
			queryStringBuffer.append("%') ");
		}
		if (null != dataEntityAccessDelete) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.dt_enty_dlt) LIKE LOWER('%");
			queryStringBuffer.append(dataEntityAccessDelete);
			queryStringBuffer.append("%') ");
		}
		if (null != isSuperUser) {
			if (seachParameterCount > 1) {
				queryStringBuffer.append(" AND ");
			}
			queryStringBuffer.append(" LOWER(a.is_spr_usr) LIKE LOWER('%");
			queryStringBuffer.append(isSuperUser);
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
		if (null != name) {
			queryStringBuffer.append(", name = '");
			queryStringBuffer.append(name);
			queryStringBuffer.append("' ");
		}
		if (null != username) {
			queryStringBuffer.append(", usrnm = '");
			queryStringBuffer.append(username);
			queryStringBuffer.append("' ");
		}
		if (null != password) {
			queryStringBuffer.append(", pswrd = '");
			queryStringBuffer.append(password);
			queryStringBuffer.append("' ");
		}
		if (null != requirementAccessCreate) {
			queryStringBuffer.append(", rqrmnt_crt = '");
			queryStringBuffer.append(requirementAccessCreate);
			queryStringBuffer.append("' ");
		}
		if (null != requirementAccessRead) {
			queryStringBuffer.append(", rqrmnt_rd = '");
			queryStringBuffer.append(requirementAccessRead);
			queryStringBuffer.append("' ");
		}
		if (null != requirementAccessUpdate) {
			queryStringBuffer.append(", rqrmnt_updt = '");
			queryStringBuffer.append(requirementAccessUpdate);
			queryStringBuffer.append("' ");
		}
		if (null != requirementAccessDelete) {
			queryStringBuffer.append(", rqrmnt_dlt = '");
			queryStringBuffer.append(requirementAccessDelete);
			queryStringBuffer.append("' ");
		}
		if (null != dataEntityAccessCreate) {
			queryStringBuffer.append(", dt_enty_crt = '");
			queryStringBuffer.append(dataEntityAccessCreate);
			queryStringBuffer.append("' ");
		}
		if (null != dataEntityAccessRead) {
			queryStringBuffer.append(", dt_enty_rd = '");
			queryStringBuffer.append(dataEntityAccessRead);
			queryStringBuffer.append("' ");
		}
		if (null != dataEntityAccessUpdate) {
			queryStringBuffer.append(", dt_enty_updt = '");
			queryStringBuffer.append(dataEntityAccessUpdate);
			queryStringBuffer.append("' ");
		}
		if (null != dataEntityAccessDelete) {
			queryStringBuffer.append(", dt_enty_dlt = '");
			queryStringBuffer.append(dataEntityAccessDelete);
			queryStringBuffer.append("' ");
		}
		if (null != isSuperUser) {
			queryStringBuffer.append(", is_spr_usr = '");
			queryStringBuffer.append(isSuperUser);
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
			if (null != name && name.length() != 0 && null != username && username.length() != 0 && null != password
					&& password.length() != 0 && null != requirementAccessCreate
					&& requirementAccessCreate.length() != 0 && null != requirementAccessRead
					&& requirementAccessRead.length() != 0 && null != requirementAccessUpdate
					&& requirementAccessUpdate.length() != 0 && null != requirementAccessDelete
					&& requirementAccessDelete.length() != 0 && null != dataEntityAccessCreate
					&& dataEntityAccessCreate.length() != 0 && null != dataEntityAccessRead
					&& dataEntityAccessRead.length() != 0 && null != dataEntityAccessUpdate
					&& dataEntityAccessUpdate.length() != 0 && null != dataEntityAccessDelete
					&& dataEntityAccessDelete.length() != 0 && null != isSuperUser && isSuperUser.length() != 0
					&& organizationId > 0) {
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
			if (null == name && null == username && null == password && null == requirementAccessCreate
					&& null == requirementAccessRead && null == requirementAccessUpdate
					&& null == requirementAccessDelete && null == dataEntityAccessCreate && null == dataEntityAccessRead
					&& null == dataEntityAccessUpdate && null == dataEntityAccessDelete && null == isSuperUser
					&& organizationId <= 0) {
				isValidObjectForUpdate = false;
			} else {
				isValidObjectForUpdate = true;
			}
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, isValidObjectForUpdate);
		return isValidObjectForUpdate;
	}
}
