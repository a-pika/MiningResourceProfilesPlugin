package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;

public class GetTrend{
	 
	public void getOneTSTrend (InputParameters ip, Connection con, int ts_id, Rengine re) throws Exception
	{
		
				
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		rs.next();
		
		String ts = "";
		String x = "";
		ts = rs.getString("ts_values");
		
if(ip.period.equals("full"))
{
		
		String y = "";
		y = "y <- c("+ts+")"; 
		re.eval(y);
		
		int numberOfSlots = ip.numberOfSlots;	
		x = "x <- c(1:"+numberOfSlots+")";
		re.eval(x);
}
else
{
		String sqlQuery1 = "SELECT * FROM change_points WHERE ts_id='"+ts_id+"'" +
				" AND cpmtype='"+ip.cpmType+"' AND arl='"+ip.ARL0+"' AND startup='"+ip.startup+"'";
		ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
		rs1.beforeFirst();
		Vector<Integer> cp_times = new Vector<Integer>();

if(rs1.next())
		{		
				String tscp = "";
				tscp = rs1.getString("cp");
				String cp_array[] = tscp.split(",");
				
			if(cp_array.length > 0 && !cp_array[0].equals(""))
			{	
				for(int i=0; i<cp_array.length; i++)
				{
					cp_times.add(Integer.valueOf(cp_array[i]));
					
				}
					
			}	
		}	

if(cp_times.size()>0)
{
	Integer maxCP = 0;
	for (int i=0;i<cp_times.size();i++)
	{
		if(cp_times.elementAt(i)>maxCP)
		{
			maxCP = cp_times.elementAt(i);
		}
	}
	String ts_array[] = ts.split(",");
	String afterCPTS = "";
	for(int i=maxCP; i<ts_array.length; i++)
	{
		afterCPTS+=ts_array[i]+",";
		
	}
	
	
	afterCPTS = (String) afterCPTS.subSequence(0, afterCPTS.length()-1);
	System.out.println(afterCPTS);
	String y = "";
	y = "y <- c("+afterCPTS+")"; 
	re.eval(y);
	
	int numberOfSlots = ip.numberOfSlots;	
	x = "x <- c("+(maxCP+1)+":"+numberOfSlots+")";
	System.out.println(x);
	re.eval(x);
	
}
else
{
	String y = "";
	y = "y <- c("+ts+")"; 
	re.eval(y);
	
	int numberOfSlots = ip.numberOfSlots;	
	x = "x <- c(1:"+numberOfSlots+")";
	re.eval(x);
}

}	
		
	re.eval("res <- lm(y~x)");
	double slope = re.eval("res$coefficients[2]").asDouble();
	double intercept = re.eval("res$coefficients[1]").asDouble();
		
	
		System.out.println(slope +"---"+intercept );
		
		dbStatement.executeUpdate("INSERT INTO trends VALUES('"+ts_id+"','"+slope+"','"+intercept+"','"+ip.period+"')");

	}
	
	
}




