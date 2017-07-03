package test.com.ete.services;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import main.com.ete.commom.Constants;

public class UserServiceTest {

	@Test
	public void testUserServices() {
		try {
			Client client = Client.create();

			// Create User
			WebResource webResource = client.resource("http://localhost:8080/localrgt/api/user/createUser");
			String input = "{ \"userModel\" : {\"name\" : \"JUnit Test 1\", \"username\" : \"JUnit Test 1\", \"password\" : \"JUnit Test 1\", \"requirementAccessCreate\" : \"YES\", \"requirementAccessRead\" : \"YES\", \"requirementAccessUpdate\" : \"YES\", \"requirementAccessDelete\" : \"YES\", \"dataEntityAccessCreate\" : \"YES\", \"dataEntityAccessRead\" : \"YES\", \"dataEntityAccessUpdate\" : \"YES\", \"dataEntityAccessDelete\" : \"YES\", \"isSuperUser\" : \"NO\", \"organizationId\" : \"1\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			ClientResponse response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			String output = response.getEntity(String.class);
			JSONObject jsonResponse = new JSONObject(output);
			assertEquals("successful user creation", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Created.")));

			// Get User
			webResource = client.resource("http://localhost:8080/localrgt/api/user/getUsers");
			input = "{ \"userModel\" : {\"name\" : \"JUnit Test 1\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful user search", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Returning List Of Object(s)")));
			JSONArray jsonArray = jsonResponse.getJSONArray(Constants.RESPONSE_OBJECT);
			long userId = 0;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject explrObject = jsonArray.getJSONObject(i);
				userId = explrObject.getLong("id");
			}

			// Update User
			webResource = client.resource("http://localhost:8080/localrgt/api/user/updateUser");
			input = "{\"userModel\" : {\"id\":\"" + userId
					+ "\", \"name\" : \"JUnit 1\", \"modificationComment\": \"Updated for JUnit\"}, \"accessUserModel\" : {\"id\":\"1\"}}";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful user update", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Updated.")));

			// Delete User
			webResource = client.resource("http://localhost:8080/localrgt/api/user/deleteUser");
			input = "{ \"userModel\" : {\"id\":\"" + userId + "\"}, \"accessUserModel\" : {\"id\":\"1\"} }";
			response = webResource.type("application/json").post(ClientResponse.class, input);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			output = response.getEntity(String.class);
			jsonResponse = new JSONObject(output);
			assertEquals("successful user deletion", true,
					(jsonResponse.get(Constants.RESPONSE_STATUS).equals(Constants.RESPONSE_STATUS_SUCCESS)
							&& jsonResponse.get(Constants.RESPONSE_MESSAGE).equals("Object Deleted.")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
