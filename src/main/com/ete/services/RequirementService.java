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
import main.com.ete.managers.RequirementManager;
import main.com.ete.managers.UserManager;
import main.com.ete.model.RequestParameterModel;
import main.com.ete.model.RequirementModel;

@Path("/requirement")
public class RequirementService {

	private final Logger LOGGER = Logger.getLogger(RequirementService.class.getName());
	private final String CLASS_NAME = RequirementService.class.getName();

	@Path("/getRequirements")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRequirements(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".getRequirements";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getAccessUserModel()
				&& 0 <= requestParameter.getAccessUserModel().getId()) {
			try {
				if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
					if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_REQUIREMENT,
							Constants.ACCESS_READ)) {
						RequirementManager manager = new RequirementManager();
						JSONObject object = CommonUtilities
								.getJSONObjectFromModelList(manager.getRequirements(requestParameter));
						result = "" + object;
					} else {
						JSONObject object = CommonUtilities.getInvalidAccessJSON(requestParameter.getAccessUserModel(),
								Constants.MODEL_REQUIREMENT, Constants.ACCESS_READ);
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

	@Path("/createRequirement")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRequirement(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".createRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getRequirementModel()
				&& requestParameter.getRequirementModel().isValidObjectForCreation()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_REQUIREMENT,
								Constants.ACCESS_CREATE)) {
							RequirementManager manager = new RequirementManager();
							RequirementModel requirement = manager.createRequirement(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromCreatedObject(requirement);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_REQUIREMENT,
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

	@Path("/updateRequirement")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateRequirement(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".updateRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getRequirementModel()
				&& requestParameter.getRequirementModel().isValidObjectForUpdate()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_REQUIREMENT,
								Constants.ACCESS_UPDATE)) {
							RequirementManager manager = new RequirementManager();
							RequirementModel requirement = manager.updateRequirement(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromUpdatedObject(requirement);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_REQUIREMENT,
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

	@Path("/deleteRequirement")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRequirement(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".deleteRequirement";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getRequirementModel()
				&& requestParameter.getRequirementModel().isValidObjectForDelete()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_REQUIREMENT,
								Constants.ACCESS_DELETE)) {
							RequirementManager manager = new RequirementManager();
							RequirementModel requirement = manager.deleteRequirement(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromDeletedObject(requirement);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_REQUIREMENT,
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
