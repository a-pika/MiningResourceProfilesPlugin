package org.processmining.plugins.miningresourceprofiles.inputs;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHandler {
	
	public Connection getDBConnection(String url, String user, String pwd) 
			throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, user, pwd);
		return con;
	}
}
