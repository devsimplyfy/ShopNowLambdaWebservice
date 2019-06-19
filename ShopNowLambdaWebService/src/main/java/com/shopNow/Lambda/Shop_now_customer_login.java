package com.shopNow.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONObject;

public class Shop_now_customer_login implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");

	

		final String email = input.get("email").toString();
		final String psw = input.get("password").toString();

		JSONObject jsonObject_login_result = new JSONObject();
		String strMsg;

		if (email == "" || email == null) {
			if (psw == "" || psw == null) {

				strMsg = "Email-Id and Password cannot be empty.";
				jsonObject_login_result.put("status", "0");
				jsonObject_login_result.put("message", strMsg);

			} else {

				strMsg = "Email-Id cannot be empty";
				jsonObject_login_result.put("status", "0");
				jsonObject_login_result.put("message", strMsg);

			}

		} else if (psw == "" || psw == null) {

			strMsg = "Password cannot be empty";
			jsonObject_login_result.put("status", "0");
			jsonObject_login_result.put("message", strMsg);

		} else {

			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";
				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery("SELECT id FROM wsimcpsn_shopnow.customers where email='"
						+ email + "' and password='" + psw + "'");

				if (resultSet.next()) {

					strMsg = "Login Sucessfull";
					jsonObject_login_result.put("status", "1");
					jsonObject_login_result.put("message", strMsg);
					jsonObject_login_result.put("id", resultSet.getInt("id"));
				} else {

					strMsg = "Incorrect Email-Id or Password";
					jsonObject_login_result.put("status", "0");
					jsonObject_login_result.put("message", strMsg);
				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}

		}
		return jsonObject_login_result;
	}
}
