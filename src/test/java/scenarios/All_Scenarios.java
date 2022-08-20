package scenarios;

import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class All_Scenarios {

	 String url = "https://fakerestapi.azurewebsites.net";
	 JsonPath jp;
	 Response response;
	 ArrayList<Object> listid,username,password;

	@Test (priority = 0)
	public void A_get_data() {
		// Scenario 1
		
		String endpoint = "/api/v1/Users";
		String usergetdatauri = url.concat(endpoint);
		Response response = RestAssured.get(usergetdatauri);
		int code = response.getStatusCode();
		String strresponse = response.getBody().asString();
		jp = new JsonPath(strresponse);
		listid = jp.get("id");
		username = jp.get("userName");
		password = jp.get("password");
		
		Assert.assertEquals(code, 200);
		
//		System.out.println("Arraylistid2"+listid );
//		System.out.println("arraylistusername"+username);
//		System.out.println("arraylistpassword"+password);

	}

	@Test (priority = 1)
	public void Bverify_id_username_password() {
		// Scenario 2

		String endpoint = "/api/v1/Users/" + listid.get(5);

		String usergetdata = url.concat(endpoint);
		 response = RestAssured.get(usergetdata);
		int code = response.getStatusCode();

		Assert.assertEquals(code, 200);
		Assert.assertEquals(6, listid.get(5));
		Assert.assertEquals("User 6", username.get(5));
		Assert.assertEquals("Password6", password.get(5));

	}

	@Test (priority = 2)
	public void Cid_notfound()
	// Scenario3
	{

		String endpoint = "/api/v1/Users/" + listid.get(9) + 1;
		given()
		.get(url.concat(endpoint))
		.then()
		.assertThat()
		.statusCode(404)
		.body("title", equalTo("Not Found"));
	}

	@SuppressWarnings("unchecked")
	@Test (priority = 3)
	 public void Dupdate_response_in_Post_Api() {
		// Scenario 4

		JSONObject objjson = new JSONObject();

		String endpoint = "/api/v1/Users";

		objjson.put("id", 14);
		objjson.put("userName", "vinod");
		objjson.put("password", "newpassword");

		 given()
		 .header("Content-Type", "application/json")
		 .contentType(ContentType.JSON)
				.accept(ContentType.JSON)
				.body(objjson.toString()).log().all()
				.when()
				.post(url.concat(endpoint))
				.then()
				.statusCode(200)
				.body("id", equalTo(14))
				.body("userName", equalTo("vinod"))
				.body("password", equalTo("newpassword"));

	}

	@Test (priority = 4)
	public void Eupdate_id_integer_To_String() {
		// Scenario 5
		String endpoint = "/api/v1/Users";
		 		given()
				.queryParam("id", "text")
				.queryParam("userName", "test")
				.queryParam("password", "testing")
				.when()
				.accept(ContentType.JSON).contentType("application/json")
				.post(url.concat(endpoint))
				.then()
				.assertThat()
				.statusCode(400)
				.body("title", equalTo("One or more validation errors occurred."));

	}

	@Test (priority = 5)
	public void Fdelete_id() {
		// Scenario 6
		String endpoint = "/api/v1/Users/6";
		given()
		.when()
		.delete(url.concat(endpoint))
		.then()
		.assertThat()
		.statusCode(200);
		

	}

	@Test (priority = 6)
	public void Ginvalid_id_enter() {
		// Scenario 7
		String endpoint = "/api/v1/Users/6a";
		String usergetdatauri = url.concat(endpoint);
		Response response = RestAssured.delete(usergetdatauri);
		int code = response.getStatusCode();
		String strresponse = response.getBody().asString();
		JsonPath jsonPath = new JsonPath(strresponse);
		
		Assert.assertEquals(code, 400);
		Assert.assertEquals("One or more validation errors occurred.", jsonPath.get("title"));
		Assert.assertEquals("The value '6a' is not valid.", jsonPath.getList("errors.id").get(0));
		
	}

}
