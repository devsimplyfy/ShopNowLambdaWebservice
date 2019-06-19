package com.shopNow.Lambda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Lambda function that simply prints "Hello World" if the input String is not
 * provided, otherwise, print "Hello " with the provided input String.
 * 
 * @param <JSONObject>
 */

public class Shop_now_wish_list_select implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject j, Context context) {

		Statement stmt = null;
		Connection con = null;
		String url = "";
		String username = "";
		String password = "";
		ResultSet resultSet;
		int customer_id = 0;

		if (j.get("id") != null && j.get("id") != "") {
			customer_id = Integer.parseInt(j.get("id").toString());
		} else {
			customer_id = 0;
		}

		JSONObject jsonObject_wishList_result = new JSONObject();
		JSONArray json_array_wishlist = new JSONArray();

		try {
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			resultSet = stmt.executeQuery(
					"SELECT pa.id,pa.name,pa.regular_price,pa.sale_price,pa.image,wish_list.customer_id FROM products AS pa INNER JOIN wish_list ON wish_list.product_id=pa.id WHERE wish_list.customer_id="
							+ customer_id + "");
			if (resultSet.next()) {
				while (resultSet.next()) {

					JSONObject jsonObject_wishList = new JSONObject();

					jsonObject_wishList.put("product_id", resultSet.getString("id"));
					jsonObject_wishList.put("product_name", resultSet.getString("name"));
					jsonObject_wishList.put("regular_price", resultSet.getFloat("regular_price"));
					jsonObject_wishList.put("sale_price", resultSet.getFloat("sale_price"));
					// jo.put("customer_id",rs.getInt("customer_id"));
					jsonObject_wishList.put("image", resultSet.getString("image"));
					json_array_wishlist.add(jsonObject_wishList);
				}

				jsonObject_wishList_result.put("wishlist", json_array_wishlist);

			} else {

				jsonObject_wishList_result.put("message", "product details for this customer_id not presant");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject_wishList_result;
	}
}