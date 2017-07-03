package main.com.ete.commom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import main.com.ete.model.BaseModel;
import main.com.ete.model.UserModel;

public class CommonUtilities {

	private final static Logger LOGGER = Logger.getLogger(CommonUtilities.class.getName());
	private final static String CLASS_NAME = CommonUtilities.class.getName();

	@SuppressWarnings("rawtypes")
	public static JSONObject getJSONObjectFromModelList(ArrayList baseModelList) {
		final String METHOD_NAME = CLASS_NAME + ".getJSONObjectFromModelList";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, baseModelList);
		JSONObject response = new JSONObject();
		JSONArray jsonarray = new JSONArray();
		for (Object item : baseModelList) {
			JSONObject currentRowJSONObject = new JSONObject();
			if (item instanceof BaseModel) {
				BaseModel baseModel = (BaseModel) item;
				currentRowJSONObject = baseModel.convertToJSONObject();
			}
			jsonarray.put(currentRowJSONObject);
		}
		response.put(Constants.RESPONSE_OBJECT, jsonarray);
		response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_SUCCESS);
		response.put(Constants.RESPONSE_MESSAGE, "Returning List Of Object(s)");
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList getModelListFromResultSet(ResultSet resultSet, Object object) throws Exception {
		final String METHOD_NAME = CLASS_NAME + ".getModelListFromResultSet";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, resultSet);
		ArrayList list = new ArrayList();
		try {
			while (resultSet.next()) {
				if (object instanceof BaseModel) {
					BaseModel model = new BaseModel();
					model.setResultSetValues(resultSet);
					list.add(model);
				}
			}
		} catch (SQLException e) {
			list = null;
			LOGGER.log(Level.SEVERE, e.getMessage());
			throw e;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, list);
		return list;
	}

	public static String convertTimeStampToString(Timestamp timeStamp) {
		final String METHOD_NAME = CLASS_NAME + ".convertTimeStampToString";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, timeStamp);
		String value = new String();
		if (null != timeStamp) {
			value = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).format(timeStamp);
		} else {
			value = "";
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, value);
		return value;
	}

	public static JSONObject getInvalidAccessJSON(UserModel user, String model, String action) {
		final String METHOD_NAME = CLASS_NAME + ".getInvalidAccessJSON";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, user + model + action);
		JSONObject response = new JSONObject();
		response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
		if (null != user) {
			response.put(Constants.RESPONSE_MESSAGE,
					"User ID '" + user.getId() + "' does not have access to " + action + " the " + model);
		} else {
			response.put(Constants.RESPONSE_MESSAGE, "User does not have access to " + action + " the " + model);
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	public static JSONObject getInvalidAccessUserJSON() {
		final String METHOD_NAME = CLASS_NAME + ".getInvalidAccessUserJSON";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject response = new JSONObject();
		response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
		response.put(Constants.RESPONSE_MESSAGE, "Access user data is missing or incorrect.");
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	public static JSONObject getExceptionJSON(Exception e) {
		final String METHOD_NAME = CLASS_NAME + ".getExceptionJSON";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		JSONObject response = new JSONObject();
		response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
		response.put(Constants.RESPONSE_MESSAGE, e.getMessage());
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	public static JSONObject getJSONObjectFromCreatedObject(BaseModel model) {
		final String METHOD_NAME = CLASS_NAME + ".getJSONObjectFromCreatedObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, model);
		JSONObject response = new JSONObject();
		if (null != model && model.getId() > 0) {
			response.put(Constants.RESPONSE_OBJECT, model);
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_SUCCESS);
			response.put(Constants.RESPONSE_MESSAGE, "Object Created.");
		} else {
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
			response.put(Constants.RESPONSE_MESSAGE, "Object Creation Failed.");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	public static JSONObject getJSONObjectFromUpdatedObject(BaseModel model) {
		final String METHOD_NAME = CLASS_NAME + ".getJSONObjectFromUpdatedObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, model);
		JSONObject response = new JSONObject();
		if (null != model && model.getId() > 0) {
			response.put(Constants.RESPONSE_OBJECT, model);
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_SUCCESS);
			response.put(Constants.RESPONSE_MESSAGE, "Object Updated.");
		} else {
			response.put(Constants.RESPONSE_OBJECT, model);
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
			response.put(Constants.RESPONSE_MESSAGE, "Object Update Failed.");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}

	public static JSONObject getJSONObjectFromDeletedObject(BaseModel model) {
		final String METHOD_NAME = CLASS_NAME + ".getJSONObjectFromDeletedObject";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG, model);
		JSONObject response = new JSONObject();
		if (null != model && model.getId() > 0) {
			response.put(Constants.RESPONSE_OBJECT, model);
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_SUCCESS);
			response.put(Constants.RESPONSE_MESSAGE, "Object Deleted.");
		} else {
			response.put(Constants.RESPONSE_OBJECT, model);
			response.put(Constants.RESPONSE_STATUS, Constants.RESPONSE_STATUS_FAILURE);
			response.put(Constants.RESPONSE_MESSAGE, "Object Deletion Failed.");
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG, response);
		return response;
	}
}
