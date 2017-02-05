package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;


public class GetOutliers{
	 
	public void getOneTSOut(InputParameters ip, Connection con, int ts_id, Rengine re) throws Exception
	{
		String ts = "";
		String outliers_right = "";
		String outliers_left = "";
		
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		rs.next();
		
		ts = rs.getString("ts_values");
		re.eval("library(extremevalues)");
		String y = "";
		String method = ip.method;
			
		y = "y <- c("+ts+")"; 
		re.eval(y);
		re.eval("res <- getOutliers(y, method = \"" + method +"\")");
		double[] outr = re.eval("res$iRight").asDoubleArray();
		if (outr != null) {for (int i=0; i<outr.length;i++)
		{
			outliers_right+=(int)outr[i]+",";
		}
		}
		double[] outl = re.eval("res$iLeft").asDoubleArray();
		if (outl != null) {for (int i=0; i<outl.length;i++)
		{
			outliers_left+=(int)outl[i]+",";
		}
		}
		
		if (outr.length>0) {outliers_right = outliers_right.substring(0, outliers_right.length()-1); }	
		if (outl.length>0) {outliers_left = outliers_left.substring(0, outliers_left.length()-1); }	
		
		//System.out.println("right: "+outliers_right);
		//System.out.println("left: "+outliers_left);
		
		
		dbStatement.executeUpdate("INSERT INTO outliers values('"+ts_id+"','"+method+"','"+outliers_right+"','"+outliers_left+"')");

	}
	
}




