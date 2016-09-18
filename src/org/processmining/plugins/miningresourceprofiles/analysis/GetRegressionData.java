package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
public class GetRegressionData{
	
	public String getTaskRegData(InputParameters ip, Connection con, int regdata_id, Vector<String> eventids) throws Exception
	{
		String returnRegData = "";
		String regviewid = ip.regressionInputs.elementAt(regdata_id).regviewid;
		Statement dbStatement = con.createStatement();

		Vector<String> varNames = new Vector<String>();
		Vector<String> varValues = new Vector<String>();
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varName.size();i++)
		{
			varNames.add(ip.regressionInputs.elementAt(regdata_id).varName.elementAt(i));
		}
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varValue.size();i++)
		{
			varValues.add(ip.regressionInputs.elementAt(regdata_id).varValue.elementAt(i));
		}

		String vars = "";
		for (int i=0; i<varNames.size();i++)
		{
			vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
		}
		
		for(int i=0; i<eventids.size(); i++)
		{
			String cureventid = eventids.elementAt(i);
			String sqlQuery = "SELECT "+regviewid+
					".* FROM " + vars+
					"(SELECT @eventid:='"+cureventid+
					"') eventid, "+regviewid;
			
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while (rs.next())
		{
			returnRegData+=rs.getDouble(1)+",";
		}
		}
			
		returnRegData = returnRegData.substring(0, returnRegData.length()-1); 
		System.out.println(returnRegData);
		return returnRegData;
	}



	
	public String getCaseRegData(InputParameters ip, Connection con, int regdata_id, Vector<String> cases) throws Exception
	{
		String returnRegData = "";
		String regviewid = ip.regressionInputs.elementAt(regdata_id).regviewid;
		Statement dbStatement = con.createStatement();

		Vector<String> varNames = new Vector<String>();
		Vector<String> varValues = new Vector<String>();
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varName.size();i++)
		{
			varNames.add(ip.regressionInputs.elementAt(regdata_id).varName.elementAt(i));
		}
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varValue.size();i++)
		{
			varValues.add(ip.regressionInputs.elementAt(regdata_id).varValue.elementAt(i));
		}

		String vars = "";
		for (int i=0; i<varNames.size();i++)
		{
			vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
		}
		
		for(int i=0; i<cases.size(); i++)
		{
			String curcase = cases.elementAt(i);
			String sqlQuery = "SELECT "+regviewid+
					".* FROM " + vars+
					"(SELECT @c:='"+curcase+
					"') c, "+regviewid;
			
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while (rs.next())
		{
			returnRegData+=rs.getDouble(1)+",";
		}
		}
			
		returnRegData = returnRegData.substring(0, returnRegData.length()-1); 
		System.out.println(returnRegData);
		return returnRegData;
	}


	
	public Vector<String> geteventids(InputParameters ip, Connection con, int regdata_id) throws Exception
	{
		Vector<String> eventids = new Vector<String>();
		String regviewid = ip.regressionInputs.elementAt(regdata_id).regviewid;
		Statement dbStatement = con.createStatement();

		Vector<String> varNames = new Vector<String>();
		Vector<String> varValues = new Vector<String>();
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varName.size();i++)
		{
			varNames.add(ip.regressionInputs.elementAt(regdata_id).varName.elementAt(i));
		}
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varValue.size();i++)
		{
			varValues.add(ip.regressionInputs.elementAt(regdata_id).varValue.elementAt(i));
		}
	
		String vars = "";
		for (int i=0; i<varNames.size();i++)
		{
			vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
		}
		
		
			String sqlQuery = "SELECT "+regviewid+
					".* FROM " + vars+ regviewid;
			
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while (rs.next())
		{
			String nexteventid = rs.getString(1);
			eventids.add(nexteventid);
		}
		System.out.println("eventids: "+eventids);
		return eventids;
	}
	
	public Vector<String> getCases(InputParameters ip, Connection con, int regdata_id) throws Exception
	{
		Vector<String> cases = new Vector<String>();
		String regviewid = ip.regressionInputs.elementAt(regdata_id).regviewid;
		Statement dbStatement = con.createStatement();

		Vector<String> varNames = new Vector<String>();
		Vector<String> varValues = new Vector<String>();
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varName.size();i++)
		{
			varNames.add(ip.regressionInputs.elementAt(regdata_id).varName.elementAt(i));
		}
		
		for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varValue.size();i++)
		{
			varValues.add(ip.regressionInputs.elementAt(regdata_id).varValue.elementAt(i));
		}
	
		String vars = "";
		for (int i=0; i<varNames.size();i++)
		{
			vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
		}
		
		
			String sqlQuery = "SELECT "+regviewid+
					".* FROM " + vars+ regviewid;
			
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while (rs.next())
		{
			String nextcase = rs.getString(1);
			cases.add(nextcase);
			//System.out.println(nextcase);
		}
		
		System.out.println("caseIDs: "+cases);
		return cases;
	}

	 
public String getTimeRegData(InputParameters ip, Connection con, int regdata_id) throws Exception
{
	String returnRegData = "";
	String regviewid = ip.regressionInputs.elementAt(regdata_id).regviewid;
	Statement dbStatement = con.createStatement();

	Vector<String> varNames = new Vector<String>();
	Vector<String> varValues = new Vector<String>();
	
	for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varName.size();i++)
	{
		varNames.add(ip.regressionInputs.elementAt(regdata_id).varName.elementAt(i));
	}
	
	for(int i=0; i<ip.regressionInputs.elementAt(regdata_id).varValue.size();i++)
	{
		varValues.add(ip.regressionInputs.elementAt(regdata_id).varValue.elementAt(i));
	}

	long tsPointTime=ip.startTime;
	
	String vars = "";
	for (int i=0; i<varNames.size();i++)
	{
		vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
	}
	
	for(int i=0; i<ip.numberOfSlots; i++)
	{
		Timestamp sqlt1 = getSlotStart(ip, (tsPointTime+i*ip.slotSize));
		Timestamp sqlt2 = getSlotEnd(ip, (tsPointTime+i*ip.slotSize));
	
		String sqlQuery = "SELECT "+regviewid+
				".* FROM " + vars+
				"(SELECT @t1:='"+sqlt1+
				"') t1, (SELECT @t2:='"+sqlt2+
				"') t2, "+regviewid;
		
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	while (rs.next())
	{
		returnRegData+=rs.getDouble(1)+",";
	}
	}
		
	returnRegData = returnRegData.substring(0, returnRegData.length()-1); 
	System.out.println(returnRegData);
	return returnRegData;
}


public Timestamp getSlotStart(InputParameters ip, long t) throws Exception
{
	Timestamp start = new Timestamp(t);
	
	return start;
	
}

public Timestamp getSlotEnd(InputParameters ip, long t) throws Exception
{
	Timestamp end = new Timestamp(t+ip.slotSize);
	
	return end;
	
}

}




