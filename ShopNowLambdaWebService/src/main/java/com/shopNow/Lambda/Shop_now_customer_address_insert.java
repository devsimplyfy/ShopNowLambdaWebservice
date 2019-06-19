package com.shopNow.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONObject;

public class Shop_now_customer_address_insert implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");

		JSONObject jsonObject_customerAddress_insert = new JSONObject();
		String Str_msg = null;

		if (input.get("customerid") != null && input.get("customerid") != ""
				&& input.get("type_billing_shipping") != null && input.get("type_billing_shipping") != ""
				&& input.get("lastName") != null && input.get("lastName") != "" && input.get("firstName") != null
				&& input.get("firstName") != "" && input.get("address1") != null && input.get("address1") != ""
				&& input.get("city") != null && input.get("city") != "" && input.get("state") != null
				&& input.get("state") != "" && input.get("country") != null && input.get("country") != ""
				&& input.get("phoneNumber") != null && input.get("phoneNumber") != "") {

			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";
				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();

				final String sql1 = "select id from customers where id=" + input.get("customerid") + "";

				ResultSet resultSet = stmt.executeQuery(sql1);
				if (resultSet.next()) {

					final String sql = "INSERT INTO address (customerId,type_billing_shipping,firstName,lastName,address1,address2,address3,city,state,country,phoneNumber) VALUES("
							+ input.get("customerid") + ",'" + input.get("type_billing_shipping").toString() + "','"
							+ input.get("firstName").toString() + "','" + input.get("lastName").toString() + "','"
							+ input.get("address1").toString() + "','" + input.get("address2").toString() + "','"
							+ input.get("address3").toString() + "','" + input.get("city").toString() + "','"
							+ input.get("state").toString() + "','" + input.get("country").toString() + "','"
							+ input.get("phoneNumber").toString() + "')";

					int i = stmt.executeUpdate(sql);
					if (i > 0) {
						Str_msg = "Record insert successfully";
						jsonObject_customerAddress_insert.put("status", "1");
						jsonObject_customerAddress_insert.put("message", Str_msg);

					}

					else {
						Str_msg = "Record not inserted";
						jsonObject_customerAddress_insert.put("status", "0");
						jsonObject_customerAddress_insert.put("message", Str_msg);

					}
				} else {
					Str_msg = "Record not inserted because customerid not valid";
					jsonObject_customerAddress_insert.put("status", "0");
					jsonObject_customerAddress_insert.put("message", Str_msg);

				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}
		} else {
			Str_msg = "enter fieldes are null so not insert record";
			jsonObject_customerAddress_insert.put("status", "0");
			jsonObject_customerAddress_insert.put("message", Str_msg);

		}

		return jsonObject_customerAddress_insert;
	}
}
