
package com.shopNow.Lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;

public class Shop_now_customer_Change_password implements RequestHandler<JSONObject, JSONObject> {

	@SuppressWarnings("unchecked")
	public JSONObject handleRequest(JSONObject input, Context context) {

		String url = "";
		String username = "";
		String password = "";
		Connection conn;
		Statement stmt = null;
		String str_msg = null;
		JSONObject jsonObject_change_psw = new JSONObject();
		int i = 0;

		if (input.get("email") == null || input.get("email") == "" || input.get("password") == null
				|| input.get("password") == "" || input.get("newPassword") == null || input.get("newPassword") == "") {

			str_msg = "email,psw,newpsw not null";
			jsonObject_change_psw.put("status", "0");
			jsonObject_change_psw.put("message", str_msg);

		} else {
			final String email = input.get("email").toString();
			final String cust_psw = input.get("password").toString();
			String newpsw = input.get("newPassword").toString();
			int lentgth = newpsw.length();

			final String sql = "update wsimcpsn_shopnow.customers set password='" + newpsw + "' where email='" + email
					+ "' and password='" + cust_psw + "'";
			LambdaLogger logger = context.getLogger();
			logger.log(sql);

			try {
				conn = DriverManager.getConnection(url, username, password);
				stmt = conn.createStatement();
				i = stmt.executeUpdate(sql);
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			if (i > 0 && lentgth > 1) {
				str_msg = "password update sussesfully";
				jsonObject_change_psw.put("status", "1");
				jsonObject_change_psw.put("message", str_msg);

			}

			else {
				str_msg = "password  not update fail";
				jsonObject_change_psw.put("status", "0");
				jsonObject_change_psw.put("message", str_msg);

			}
		}
		return jsonObject_change_psw;

	}
}