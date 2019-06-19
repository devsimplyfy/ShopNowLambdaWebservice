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

public class Shop_now_category implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings({ "unchecked" })
	public JSONObject handleRequest(JSONObject input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");

		JSONArray category_array = new JSONArray();
		JSONObject jsonObject_category_result = new JSONObject();

		if (input.get("product_id") != null && input.get("product_id") != "") {

			// Get time from DB server
			try {
				String url = "";
				String username = "";
				String password = "";
				Connection conn = DriverManager.getConnection(url, username, password);

				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(
						"SELECT * FROM wsimcpsn_shopnow.categories where parent_id =" + input.get("product_id"));

				while (resultSet.next()) {
					
					JSONObject jsonObject_category = new JSONObject();
					jsonObject_category.put("id", resultSet.getString(1));
					jsonObject_category.put("name", resultSet.getString(2));
					jsonObject_category.put("image", resultSet.getString(4));

					category_array.add(jsonObject_category);

					jsonObject_category_result.put("categories", category_array);

				}

			} catch (Exception e) {
				e.printStackTrace();
				logger.log("Caught exception: " + e.getMessage());
			}
			return jsonObject_category_result;
		} else {
			return jsonObject_category_result;

		}

	}
}
