package com.shopNow.Lambda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Lambda function that simply prints "Hello World" if the input String is not
 * provided, otherwise, print "Hello " with the provided input String.
 * 
 * @param <JSONObject>
 */

public class Shop_now_product_detail implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject j, Context context) {

		Object id;

		if (j.get("id") != null && j.get("id") != "") {
			id = j.get("id");
		} else {
			id = 0;
		}
		ResultSet resultSet, resultSet1, resultSet2 = null;
		Statement stmt = null;
		Connection con = null;
		String url = "";
		String username = "";
		String password = "";
		JSONArray jsonArray_product = new JSONArray();
		JSONObject jsonObject_product_Result = new JSONObject();
		JSONObject jsonObject_image = new JSONObject();

		LambdaLogger logger = context.getLogger();

		try {
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			resultSet = stmt.executeQuery(
					"SELECT p.id,p.name,p.description,p.regular_price,p.sale_price,p.stock,categories.name AS category,p.image,p.product_url,p.similar_product_id,p.recommended_product_id,p.vendor_id FROM products AS p INNER JOIN  categories ON p.category_id=categories.id WHERE p.id="
							+ id);
			while (resultSet.next()) {
				JSONObject jsonObject_product_detail = new JSONObject();

				jsonObject_product_detail.put("id", resultSet.getString("id"));
				jsonObject_product_detail.put("name", resultSet.getString("name"));
				jsonObject_product_detail.put("description", resultSet.getString("description"));
				jsonObject_product_detail.put("regular_price", resultSet.getFloat("regular_price"));
				jsonObject_product_detail.put("sale_price", resultSet.getFloat("sale_price"));
				jsonObject_product_detail.put("stock", resultSet.getString("stock"));
				jsonObject_product_detail.put("product_url", resultSet.getString("product_url"));
				jsonObject_image.put("image", resultSet.getString("image"));

				JSONArray jsonArray_image = new JSONArray();

				jsonArray_image.add(jsonObject_image);
				jsonObject_product_detail.put("Image", jsonArray_image);

				jsonObject_product_detail.put("category", resultSet.getString("category"));

				jsonArray_product.add(jsonObject_product_detail);
				String similar_products_id = null;

				similar_products_id = resultSet.getString("similar_product_id");

				String[] similar_product_array = {};
				if (similar_products_id != null) {
					similar_product_array = similar_products_id.split(",");
				}

				String recommended_product_id = resultSet.getString("recommended_product_id");

				String[] recommended_product_id_array = {};
				if (recommended_product_id != null) {
					recommended_product_id_array = recommended_product_id.split(",");
				}

				String vendor_id = resultSet.getString("vendor_id");

				String[] vendor_id_array = {};
				if (vendor_id != null) {
					vendor_id_array = vendor_id.split(",");
				}

				jsonObject_product_Result.put("product_detail", jsonArray_product);
				jsonObject_product_Result.put("Similar Products", similar_product_array);
				jsonObject_product_Result.put("Recommended Products", recommended_product_id_array);
				jsonObject_product_Result.put("vendors", vendor_id_array);

			}
			resultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.log("\n Invoked products fields");

		String sql_product_att = "SELECT pa.product_id,GROUP_CONCAT(att_group_name,'\":\"',av.att_value) AS attribute_value,pa.price_change AS price,pa.product_url FROM product_attributes pa  \n"
				+ "INNER JOIN\n"
				+ "attributes_value av ON av.id=pa.att_group_val_id INNER JOIN attributes a ON a.id=pa.att_group_id  where pa.product_id="
				+ id;

		try {

			resultSet1 = stmt.executeQuery(sql_product_att);
			while (resultSet1.next()) {
				String attribute_value = resultSet1.getString("attribute_value");

				String[] attribute_value1 = {};
				if (attribute_value != null) {
					attribute_value1 = attribute_value.split(",");
				}

				jsonObject_product_Result.put("attribute", attribute_value1);
			}
			resultSet1.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.log("\n Invoked products_attribute fields");

		String sql_product_option = "SELECT table1.product_id,GROUP_CONCAT(table1.option_name,'\":\"',product_option_value.value) AS product_option FROM(\n"
				+ "SELECT product_options.id,product_options.product_id,product_option_group.option_name FROM product_options INNER JOIN  product_option_group ON product_options.id=product_option_group.id WHERE product_options.product_id=16) AS table1\n"
				+ "INNER JOIN\n" + "product_option_value ON table1.id=product_option_value.id WHERE table1.product_id="
				+ id;

		try {

			resultSet2 = stmt.executeQuery(sql_product_option);
			while (resultSet2.next()) {
				String product_option = resultSet2.getString("product_option");
				String[] product_option_array = {};
				if (product_option != null) {
					product_option_array = product_option.split(",");
				}

				jsonObject_product_Result.put("option", product_option_array);
			}
			resultSet2.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject_product_Result;
	}
}