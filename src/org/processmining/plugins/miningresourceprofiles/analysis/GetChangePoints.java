package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;


public class GetChangePoints{
	 
	public void getOneTSCP(InputParameters ip, Connection con, int ts_id, Rengine re) throws Exception
	{
		String ts = "";
		String change_points = "";
		
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		rs.next();
		
		ts = rs.getString("ts_values");
			
		re.eval("library(cpm)");
		String y = "";
		String cpmType = ip.cpmType; 
		String ARL0 = ip.ARL0; 
		String startup = ip.startup; 
		
		y = "y <- c("+ts+")"; 
		re.eval(y);
		re.eval("res <- processStream(y, cpmType = \"" + cpmType +"\", ARL0 = " + ARL0 + ", startup = " + startup +")");
		double[] cp = re.eval("res$changePoints").asDoubleArray();
		
		for(int i=0;i<cp.length;i++)
		{
			change_points+=(int)cp[i]+",";
		}
		
		if (cp.length>0) {change_points = change_points.substring(0, change_points.length()-1); }	
		
		System.out.println(change_points);
		
		dbStatement.executeUpdate("INSERT INTO change_points values('"+ts_id+"','"+cpmType+"','"+ARL0+"','"+startup+"','"+change_points+"')");

	}
	
}




