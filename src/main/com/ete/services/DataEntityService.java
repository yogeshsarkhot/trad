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
import main.com.ete.managers.DataEntityManager;
import main.com.ete.managers.UserManager;
import main.com.ete.model.DataEntityModel;
import main.com.ete.model.RequestParameterModel;

@Path("/dataentity")
public class DataEntityService {

	private final Logger LOGGER = Logger.getLogger(DataEntityService.class.getName());
	private final String CLASS_NAME = DataEntityService.class.getName();

	@Path("/getDataEntities")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDataEntities(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".getDataEntities";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getAccessUserModel()
				&& 0 <= requestParameter.getAccessUserModel().getId()) {
			try {
				if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
					if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_DATA_ENTITY,
							Constants.ACCESS_READ)) {
						DataEntityManager manager = new DataEntityManager();
						JSONObject object = CommonUtilities
								.getJSONObjectFromModelList(manager.getDataEntities(requestParameter));
						result = "" + object;
					} else {
						JSONObject object = CommonUtilities.getInvalidAccessJSON(requestParameter.getAccessUserModel(),
								Constants.MODEL_DATA_ENTITY, Constants.ACCESS_READ);
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

	@Path("/createDataEntity")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createDataEntity(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".createDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getDataEntityModel()
				&& requestParameter.getDataEntityModel().isValidObjectForCreation()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_DATA_ENTITY,
								Constants.ACCESS_CREATE)) {
							DataEntityManager manager = new DataEntityManager();
							DataEntityModel dataEntity = manager.createDataEntity(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromCreatedObject(dataEntity);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_DATA_ENTITY,
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

	@Path("/updateDataEntity")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateDataEntity(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".updateDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getDataEntityModel()
				&& requestParameter.getDataEntityModel().isValidObjectForUpdate()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_DATA_ENTITY,
								Constants.ACCESS_UPDATE)) {
							DataEntityManager manager = new DataEntityManager();
							DataEntityModel dataEntity = manager.updateDataEntity(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromUpdatedObject(dataEntity);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_DATA_ENTITY,
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

	@Path("/deleteDataEntity")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteDataEntity(RequestParameterModel requestParameter) throws JSONException {
		final String METHOD_NAME = CLASS_NAME + ".deleteDataEntity";
		LOGGER.log(Level.INFO, METHOD_NAME + Constants.METHOD_ENTRY_LOG);
		String result = new String();
		if (null != requestParameter && null != requestParameter.getDataEntityModel()
				&& requestParameter.getDataEntityModel().isValidObjectForDelete()) {
			if (null != requestParameter.getAccessUserModel() && 0 <= requestParameter.getAccessUserModel().getId()) {
				try {
					if (UserManager.isValidUserId(requestParameter.getAccessUserModel().getId())) {
						if (UserManager.isActionAllowed(requestParameter, Constants.MODEL_DATA_ENTITY,
								Constants.ACCESS_DELETE)) {
							DataEntityManager manager = new DataEntityManager();
							DataEntityModel dataEntity = manager.deleteDataEntity(requestParameter);
							JSONObject object = CommonUtilities.getJSONObjectFromDeletedObject(dataEntity);
							result = "" + object;
						} else {
							JSONObject object = CommonUtilities.getInvalidAccessJSON(
									requestParameter.getAccessUserModel(), Constants.MODEL_DATA_ENTITY,
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
