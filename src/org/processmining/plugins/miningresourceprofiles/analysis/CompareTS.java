package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;


public class CompareTS{
	 
	public double compareTS (InputParameters ip, Connection con, int ts_id, Rengine re) throws Exception
	{
		
				
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		rs.next();		
		String ts = "";
		ts = rs.getString("ts_values");
		System.out.println("ts1: "+ts);
		String y = "";
		y = "y <- c("+ts+")"; 
		re.eval(y);
		System.out.println("y: "+y);
		
		
		
		String mean_type = ip.meanMethod;
		int exclude_zeros = 0;
		if(ip.excludeTSZeros){exclude_zeros = 1;}
		
		
		String sqlQuery2 = "SELECT * FROM ts_mean"; 

		ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
		rs2.beforeFirst();
		rs2.next();
		
		String ts2 = "";
		ts2 = rs2.getString("ts_values");
		System.out.println("ts2: "+ts2);
		
		String x = "";
		x = "x <- c("+ts2+")"; 
		re.eval(x);
		System.out.println("x: "+x);
		
	
		re.eval("res <- wilcox.test(y,x)");
		System.out.println("evaluated in r");
		double pvalue = re.eval("res$p.value").asDouble();
		
		System.out.println(pvalue);
		
		dbStatement.executeUpdate("INSERT INTO ts_compare VALUES('"+ts_id+"','"+pvalue+"','"+ mean_type+"','"+exclude_zeros+"')");
		return pvalue;
	}
	
	
}




