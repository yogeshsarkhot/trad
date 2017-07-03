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
import main.com.ete.managers.OrganizationManager;
import main.com.ete.managers.UserManager;
import main.com.ete.model.OrganizationModel;
import main.com.ete.model.RequestParameterModel;

@Path("/organization")
public class OrganizationService {

	private final Logger LOGGER = Logger.getLogger(OrganizationService.class.getName());
	private final String CLASS_NAME = OrganizationService.class.getName();

	@Path("/getOrganizations")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOrganizations(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".getOrganizations";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getAccessUserModel()
				&& 0 <= requestParameter.getAccessUserModel().getId()) {
			try {
				if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
					if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_ORGANIZATION,
							Constants.ACCESS_READ)) {
						OrganizationManager manager = new OrganizationManager();
						JSONObject object = CommonUtilities
								.getJSONObjectFromModelList(manager.getOrganizations(requestParameter));
						result = "" + object;
					} else {
						JSONObject object = CommonUtilities.getInvalidAccessJSON(requestParameter.getAccessUserModel(),
								Constants.MODEL_ORGANIZATION, Constants.ACCESS_READ);
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

	@Path("/createOrganization")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOrganization(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".createOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getOrganizationModel()
				&& requestParameter.getOrganizationModel().isValidObjectForCreation()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_ORGANIZATION,
								Constants.ACCESS_CREATE)) {
							OrganizationManager manager = new OrganizationManager();
							OrganizationModel organization = manager.createOrganization(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromCreatedObject(organization);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_ORGANIZATION,
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

	@Path("/updateOrganization")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateOrganization(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".updateOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getOrganizationModel()
				&& requestParameter.getOrganizationModel().isValidObjectForUpdate()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_ORGANIZATION,
								Constants.ACCESS_UPDATE)) {
							OrganizationManager manager = new OrganizationManager();
							OrganizationModel organization = manager.updateOrganization(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromUpdatedObject(organization);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_ORGANIZATION,
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

	@Path("/deleteOrganization")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".deleteOrganization";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getOrganizationModel()
				&& requestParameter.getOrganizationModel().isValidObjectForDelete()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_ORGANIZATION,
								Constants.ACCESS_DELETE)) {
							OrganizationManager manager = new OrganizationManager();
							OrganizationModel organization = manager.deleteOrganization(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromDeletedObject(organization);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_ORGANIZATION,
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
