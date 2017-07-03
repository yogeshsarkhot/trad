package test.com.ete.services;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import main.com.ete.commom.Constants;

public class OrganizationServiceTest {

	@Test
	public void testOrganizationServices() {
		try {
			Client client = Client.create();

			// Create Organization
			WebResource webResource = client
					.resource("http://localhost:8080/localrgt/api/organization/createOrganization");
			String input = "{ \"organizationModel\" : {\"name\":\"JUnit Test Organization\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.getEntity(String.class);
			JSONObject jsonResponse = new JSONObject(output);
			assertEquals("successful organization creation", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Created.")));

			// Get Organization
			webResource = client.resource("http://localhost:8080/localrgt/api/organization/getOrganizations");
			input = "{ \"organizationModel\" : {\"name\":\"JUnit Test Organization\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful organization search", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Returning List Of Object(s)")));
			JSONArray jsonArray = jsonResponse.getJSONArray(Constants.RESPONSE_OBJECT);
			long organizationId = 0;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				organizationId = object.getLong("id");
			}

			// Update Organization
			webResource = client.resource("http://localhost:8080/localrgt/api/organization/updateOrganization");
			input = "{ \"organizationModel\" : {\"id\": \"" + organizationId
					+ "\", \"name\" : \"JUnit Organization\", \"modificationComment\":\"Junit Update\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful organization update", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Updated.")));

			// Delete Organization
			webResource = client.resource("http://localhost:8080/localrgt/api/organization/deleteOrganization");
			input = "{ \"organizationModel\" : {\"id\": \"" + organizationId
					+ "\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful organization deletion", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Deleted.")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}