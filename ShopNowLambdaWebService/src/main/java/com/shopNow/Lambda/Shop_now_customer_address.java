
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

public class Shop_now_customer_address implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {
		
		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");
		logger.log("Input" + input.get("customerid"));

		JSONArray jsonArray_customerAddress = new JSONArray();
		JSONObject jsonObject_customerAddress_Result = new JSONObject();
		if (input.get("customerid") != null && input.get("customerid") != "") {

			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";
				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(
						"select id,customerId,type_billing_shipping,firstName,lastName,address1,address2,address3,city,state,country,phoneNumber,IsPrimary,createdDatetime from address where customerId="
								+ input.get("customerid"));

				while (resultSet.next()) {

					JSONObject jsonObject_customerAddress = new JSONObject();
					jsonObject_customerAddress.put("id", resultSet.getString("id"));
					jsonObject_customerAddress.put("firstName", resultSet.getString("firstName"));
					jsonObject_customerAddress.put("lastName", resultSet.getString("lastName"));
					jsonObject_customerAddress.put("address1", resultSet.getString("address1"));
					jsonObject_customerAddress.put("address2", resultSet.getString("address2"));
					jsonObject_customerAddress.put("address3", resultSet.getString("address3"));
					jsonObject_customerAddress.put("city", resultSet.getString("city"));
					jsonObject_customerAddress.put("state", resultSet.getString("state"));
					jsonObject_customerAddress.put("country", resultSet.getString("country"));
					jsonObject_customerAddress.put("phoneNumber", resultSet.getString("phoneNumber"));
					jsonArray_customerAddress.add(jsonObject_customerAddress);

				}

				jsonObject_customerAddress_Result.put("Address", jsonArray_customerAddress);

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}

			return jsonObject_customerAddress_Result;
		} else {
			String Str_msg = "enter valid customer ID ";
			jsonObject_customerAddress_Result.put("status", "0");
			jsonObject_customerAddress_Result.put("message", Str_msg);
			return jsonObject_customerAddress_Result;
		}
	}
}
