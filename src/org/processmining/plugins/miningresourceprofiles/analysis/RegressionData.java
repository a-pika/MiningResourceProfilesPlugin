package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;


public class RegressionData{
	 
	public String regviewid;
	public Vector <String> varName = new Vector<String>();
	public Vector <String> varValue = new Vector<String>();
	
	 
	RegressionData() {
		
		regviewid = "";
					       
	    } 
	
public RegressionData(String regID, Vector<String> varname, Vector<String> varvalue)
{
	regviewid = regID;
			
	for (int i=0; i<varname.size();i++)
	{
		varName.add(varname.elementAt(i));
	}
	
	for (int i=0; i<varvalue.size();i++)
	{
		varValue.add(varvalue.elementAt(i));
	}
}

public void getUserInput(Connection con) throws Exception{

	//Vector<String> used_vars = new Vector<String>();
	String rbi_id = regviewid.replace("rbi","");
	Statement dbStatement = con.createStatement();
	
	String sqlQuery = "SELECT definition FROM RBIs where id='"+rbi_id+"'";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	//String definition = "";
	//if (rs.next()){definition = rs.getString("definition");}

}
	
}




