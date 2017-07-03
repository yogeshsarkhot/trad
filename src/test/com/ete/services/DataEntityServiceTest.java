package test.com.ete.services;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import main.com.ete.commom.Constants;

public class DataEntityServiceTest {

	@Test
	public void testDataEntityServices() {
		try {
			Client client = Client.create();
			// Create Data Entity
			WebResource webResource = client.resource("http://localhost:8080/localrgt/api/dataentity/createDataEntity");
			String input = "{ \"dataEntityModel\" : {\"name\" : \"JUnit Test Data Entity\", \"organizationId\" : \"1\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.getEntity(String.class);
			JSONObject jsonResponse = new JSONObject(output);
			assertEquals("successful data entity creation", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Created.")));

			// Get Data Entity
			webResource = client.resource("http://localhost:8080/localrgt/api/dataentity/getDataEntities");
			input = "{ \"dataEntityModel\" : {\"name\" : \"JUnit Test Data Entity\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful data entity search", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Returning List Of Object(s)")));
			JSONArray jsonArray = jsonResponse.getJSONArray(Constants.RESPONSE_OBJECT);
			long dataEntityId = 0;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				dataEntityId = object.getLong("id");
			}

			// Update Data Entity
			webResource = client.resource("http://localhost:8080/localrgt/api/dataentity/updateDataEntity");
			input = "{ \"dataEntityModel\" : {\"id\": \"" + dataEntityId
					+ "\", \"name\" : \"JUnit Data Entity\", \"modificationComment\":\"Junit Update\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful data entity update", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Updated.")));

			// Delete Data Entity
			webResource = client.resource("http://localhost:8080/localrgt/api/dataentity/deleteDataEntity");
			input = "{ \"dataEntityModel\" : {\"id\": \"" + dataEntityId
					+ "\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful data entity deletion", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Deleted.")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
