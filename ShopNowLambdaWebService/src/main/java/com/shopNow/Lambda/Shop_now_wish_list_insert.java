package com.shopNow.Lambda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.simple.JSONObject;

/**
 * Lambda function that simply prints "Hello World" if the input String is not
 * provided, otherwise, print "Hello " with the provided input String.
 * 
 * @param <JSONObject>
 */

public class Shop_now_wish_list_insert implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {

		Statement stmt = null;
		Connection con = null;
		String url = "";
		String username = "";
		String password = "";
		ResultSet resultSet;
		String strMsg;

		JSONObject jsonObject_InsertWishList_Result = new JSONObject();
		String customerId = null;
		String product_id = null;

		LambdaLogger logger = context.getLogger();
		logger.log("Invoked JDBCSample.getCurrentTime");

		if (input.get("customerid") == null || input.get("productid") == null || input.get("customerid") == ""
				|| input.get("productid") == "") {
			jsonObject_InsertWishList_Result.put("status", "0");
			jsonObject_InsertWishList_Result.put("message", "input invalid");

			return jsonObject_InsertWishList_Result;

		} else {
			customerId = input.get("customerid").toString();
			product_id = input.get("productid").toString();

			try {
				con = DriverManager.getConnection(url, username, password);
				stmt = con.createStatement();
				resultSet = stmt
						.executeQuery("SELECT id FROM wsimcpsn_shopnow.customers where id='" + customerId + "'");
				if (resultSet.next()) {

					resultSet.close();
					resultSet = stmt
							.executeQuery("SELECT id FROM wsimcpsn_shopnow.products where id='" + product_id + "'");
				}

				if (resultSet.next()) {

					resultSet.close();
					resultSet = stmt.executeQuery("SELECT customer_id,product_id FROM wish_list where customer_id='"
							+ customerId + "' and product_id='" + product_id + "'");
				}
				if (resultSet.next()) {

					strMsg = "customerId and productId Already present in wishlist";
					jsonObject_InsertWishList_Result.put("status", "0");
					jsonObject_InsertWishList_Result.put("message", strMsg);

				} else {

					stmt.executeUpdate("INSERT INTO wish_list (customer_id,product_id)VALUES('" + customerId + "','"
							+ product_id + "')");

					strMsg = "insert Record sucessfully";
					jsonObject_InsertWishList_Result.put("status", "0");
					jsonObject_InsertWishList_Result.put("message", strMsg);

				}

			}

			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}
		return jsonObject_InsertWishList_Result;
	}
}