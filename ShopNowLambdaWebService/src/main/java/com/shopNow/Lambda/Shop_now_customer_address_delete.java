
package com.shopNow.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.json.simple.JSONObject;

public class Shop_now_customer_address_delete implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {
		LambdaLogger logger = context.getLogger();

		JSONObject jsonObject_customerAddress_Delete = new JSONObject();
		String Str_msg = null;
		if (input.get("customerid") != null && input.get("id") != null && input.get("customerid") != ""
				&& input.get("id") != "") {

			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";
				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();

				final String sql = "DELETE FROM address WHERE customerId ='" + input.get("customer_id") + "' and id='"
						+ input.get("id") + "'";

				int i = stmt.executeUpdate(sql);
				if (i > 0) {
					Str_msg = "Record deleted successfully";
					jsonObject_customerAddress_Delete.put("status", "1");
					jsonObject_customerAddress_Delete.put("message", Str_msg);

				}

				else {
					Str_msg = "Record not Found";
					jsonObject_customerAddress_Delete.put("status", "0");
					jsonObject_customerAddress_Delete.put("message", Str_msg);

				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}
		} else {
			Str_msg = "enter valid customer ID or id";
			jsonObject_customerAddress_Delete.put("status", "0");
			jsonObject_customerAddress_Delete.put("message", Str_msg);

		}

		return jsonObject_customerAddress_Delete;
	}
}
