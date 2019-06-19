package com.shopNow.Lambda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.simple.JSONObject;

/**
 * Lambda function that simply prints "Hello World" if the input String is not
 * provided, otherwise, print "Hello " with the provided input String.
 * 
 * @param <JSONObject>
 */

public class Shop_now_wish_list_delete implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {

		Statement stmt = null;
		Connection con = null;
		String url = "";
		String username = "";
		String password = "";
		ResultSet resultSet;
		int i = 0;
		String str_Msg = null;
		JSONObject jsonObject_wishListDelete_Result = new JSONObject();

		if (input.get("customerid") == null || input.get("productid") == null || input.get("customerid") == ""
				|| input.get("productid") == "") {

			str_Msg = "enter valid customerId and productId";
			jsonObject_wishListDelete_Result.put("status", "0");
			jsonObject_wishListDelete_Result.put("message", str_Msg);

			return jsonObject_wishListDelete_Result;

		} else {
			final int customerId = Integer.parseInt(input.get("customerid").toString());
			final int product_id = Integer.parseInt(input.get("productid").toString());
			final String sql = "delete from wish_list where customer_id=" + customerId + " and product_id=" + product_id
					+ "";

			String sql1 = ("SELECT id FROM wish_list where customer_id='" + customerId + "' and product_id='"
					+ product_id + "'");
			try {

				con = DriverManager.getConnection(url, username, password);
				stmt = con.createStatement();
				resultSet = stmt.executeQuery(sql1);

				if (resultSet.next()) {

					try {

						i = stmt.executeUpdate(sql);
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					str_Msg = " number of rows =" + i + " delete product in wish_list sussesfully";
					jsonObject_wishListDelete_Result.put("status", "1");
					jsonObject_wishListDelete_Result.put("message", str_Msg);
				} else {

					str_Msg = "enter valid customerId and productId";
					jsonObject_wishListDelete_Result.put("status", "0");
					jsonObject_wishListDelete_Result.put("message", str_Msg);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return jsonObject_wishListDelete_Result;
		}
	}
}