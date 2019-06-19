package com.shopNow.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Shop_now_customer_list implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");

		logger.log("Input" + input.get("id"));

		String currentTime2 = "unavailable";

		JSONArray customer_array = new JSONArray();
		JSONObject jsonObject_customer_result = new JSONObject();

		if (input.get("id") != null && input.get("id") != "") {
			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";

				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(
						"SELECT id, first_name,last_name,email,phone_number,image FROM wsimcpsn_shopnow.customers where id="
								+ input.get("id"));

				while (resultSet.next()) {

					JSONObject json_obj_customer = new JSONObject();

					json_obj_customer.put("first_name", resultSet.getString("first_name"));
					json_obj_customer.put("last_name", resultSet.getString("last_name"));
					json_obj_customer.put("email", resultSet.getString("email"));
					json_obj_customer.put("phone_number", resultSet.getInt("phone_number"));
					json_obj_customer.put("photo", resultSet.getString("image"));

					customer_array.add(json_obj_customer);

					jsonObject_customer_result.put("customer", customer_array);
					currentTime2 = jsonObject_customer_result.toString();
				}

				logger.log("Successfully executed query.  Result: " + currentTime2);

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}

			return jsonObject_customer_result;
		} else {
			return jsonObject_customer_result;

		}

	}
}
