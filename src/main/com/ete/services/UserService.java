package main.com.ete.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import main.com.ete.commom.CommonUtilities;
import main.com.ete.commom.Constants;
import main.com.ete.managers.UserManager;
import main.com.ete.model.RequestParameterModel;
import main.com.ete.model.UserModel;

@Path("/user")
public class UserService {

	private final Logger LOGGER = Logger.getLogger(UserService.class.getName());
	private final String CLASS_NAME = UserService.class.getName();

	@Path("/getUsers")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".getUsers";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getAccessUserModel()
				&& 0 <= requestParameter.getAccessUserModel().getId()) {
			try {
				if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
					if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_USER, Constants.ACCESS_READ)) {
						UserManager manager = new UserManager();
						JSONObject object = CommonUtilities
								.getJSONObjectFromModelList(manager.getUsers(requestParameter));
						result = "" + object;
					} else {
						JSONObject object = CommonUtilities.getInvalidAccessJSON(requestParameter.getAccessUserModel(),
								Constants.MODEL_USER, Constants.ACCESS_READ);
						result = "" + object;
					}
				} else {
					Exception e = new Exception(Constants.INVALID_ACCESS_USER_ID_EXCEPTION_MESSAGE
							+ requestParameter.getAccessUserModel().getId());
					LOGGER.log(Level.SEVERE, e.getMessage());
					JSONObject object = CommonUtilities.getExceptionJSON(e);
					result = "" + object;
				}
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
				JSONObject object = CommonUtilities.getExceptionJSON(e);
				result = "" + object;
			}
		} else {
			JSONObject object = CommonUtilities.getInvalidAccessUserJSON();
			result = "" + object;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return Response.status(200).entity(result).build();
	}

	@Path("/createUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createUser(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".createUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getUserModel()
				&& requestParameter.getUserModel().isValidObjectForCreation()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_USER,
								Constants.ACCESS_CREATE)) {
							UserManager manager = new UserManager();
							UserModel user = manager.createUser(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromCreatedObject(user);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_USER,
									Constants.ACCESS_CREATE);
							result = "" + object;
						}
					} else {
						Exception e = new Exception(Constants.INVALID_ACCESS_USER_ID_EXCEPTION_MESSAGE
								+ requestParameter.getAccessUserModel().getId());
						LOGGER.log(Level.SEVERE, e.getMessage());
						JSONObject object = CommonUtilities.getExceptionJSON(e);
						result = "" + object;
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
					JSONObject object = CommonUtilities.getExceptionJSON(e);
					result = "" + object;
				}
			} else {
				JSONObject object = CommonUtilities.getInvalidAccessUserJSON();
				result = "" + object;
			}
		} else {
			Exception e = new Exception(Constants.CREATION_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			JSONObject object = CommonUtilities.getExceptionJSON(e);
			result = "" + object;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return Response.status(200).entity(result).build();
	}

	@Path("/updateUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".updateUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getUserModel()
				&& requestParameter.getUserModel().isValidObjectForUpdate()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_USER,
								Constants.ACCESS_UPDATE)) {
							UserManager manager = new UserManager();
							UserModel user = manager.updateUser(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromUpdatedObject(user);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_USER,
									Constants.ACCESS_UPDATE);
							result = "" + object;
						}
					} else {
						Exception e = new Exception(Constants.INVALID_ACCESS_USER_ID_EXCEPTION_MESSAGE
								+ requestParameter.getAccessUserModel().getId());
						LOGGER.log(Level.SEVERE, e.getMessage());
						JSONObject object = CommonUtilities.getExceptionJSON(e);
						result = "" + object;
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
					JSONObject object = CommonUtilities.getExceptionJSON(e);
					result = "" + object;
				}
			} else {
				JSONObject object = CommonUtilities.getInvalidAccessUserJSON();
				result = "" + object;
			}
		} else {
			Exception e = new Exception(Constants.UPDATE_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			JSONObject object = CommonUtilities.getExceptionJSON(e);
			result = "" + object;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return Response.status(200).entity(result).build();
	}

	@Path("/deleteUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".deleteUser";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getUserModel()
				&& requestParameter.getUserModel().isValidObjectForDelete()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_USER,
								Constants.ACCESS_DELETE)) {
							UserManager manager = new UserManager();
							UserModel user = manager.deleteUser(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromDeletedObject(user);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_USER,
									Constants.ACCESS_DELETE);
							result = "" + object;
						}
					} else {
						Exception e = new Exception(Constants.INVALID_ACCESS_USER_ID_EXCEPTION_MESSAGE
								+ requestParameter.getAccessUserModel().getId());
						LOGGER.log(Level.SEVERE, e.getMessage());
						JSONObject object = CommonUtilities.getExceptionJSON(e);
						result = "" + object;
					}
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, e.getMessage());
					JSONObject object = CommonUtilities.getExceptionJSON(e);
					result = "" + object;
				}
			} else {
				JSONObject object = CommonUtilities.getInvalidAccessUserJSON();
				result = "" + object;
			}
		} else {
			Exception e = new Exception(Constants.DELETION_INVALID_OBJECT_EXCEPTION_MESSAGE);
			LOGGER.log(Level.SEVERE, e.getMessage());
			JSONObject object = CommonUtilities.getExceptionJSON(e);
			result = "" + object;
		}
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_EXIT_LOG);
		return Response.status(200).entity(result).build();
	}
}
