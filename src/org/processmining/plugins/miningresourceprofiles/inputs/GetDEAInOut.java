package org.processmining.plugins.miningresourceprofiles.inputs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;
public class GetDEAInOut{
	 
public void storeOneInputTS (InputParameters ip, Connection con, int input_id) throws Exception

{
	Statement dbStatement = con.createStatement();
	String inputID = ip.DEA_Inputs.elementAt(input_id).inout;
	String resource = ip.DEA_Inputs.elementAt(input_id).resource;
	Vector <String> varNames = new Vector <String>();
	Vector <String> varValues = new Vector <String>();
	
	long startTime = ip.startTime;
	long slotSize = ip.slotSize;
	int numberOfSlots = ip.numberOfSlots;
	String ts_times = "";
	String varNamesString = "";
	String varValuesString = "";
	
	for(int i=0; i<numberOfSlots; i++)
	{
		Timestamp time = new Timestamp(startTime+i*slotSize);
		ts_times += time.toString()+",";
	
	}
	
	ts_times = ts_times.substring(0, ts_times.length()-1); 	
	
	for (int i=0; i<ip.DEA_Inputs.elementAt(input_id).varName.size();i++)
	{
		varNames.add(ip.DEA_Inputs.elementAt(input_id).varName.elementAt(i));
		varNamesString+=ip.DEA_Inputs.elementAt(input_id).varName.elementAt(i)+",";
	}
	
	for (int i=0; i<ip.DEA_Inputs.elementAt(input_id).varValue.size();i++)
	{
		varValues.add(ip.DEA_Inputs.elementAt(input_id).varValue.elementAt(i));
		varValuesString+=ip.DEA_Inputs.elementAt(input_id).varValue.elementAt(i)+",";
	}
	
	if(!varNamesString.isEmpty())
	{varNamesString = varNamesString.substring(0, varNamesString.length()-1);}
	
	if(!varValuesString.isEmpty())
	{varValuesString = varValuesString.substring(0, varValuesString.length()-1);} 
	
	String ts = getOneInputTS(ip, con, input_id, varNames, varValues, resource);
	
	System.out.println(inputID);
	System.out.println(ts);
	System.out.println(ts_times);
	System.out.println(varNamesString);
	System.out.println(varValuesString);
	System.out.println(resource);
	
	dbStatement.executeUpdate("INSERT INTO deaints(input_vars,input_values,ts_times,ts_values,resource,inid) values('"+varNamesString+"','"+varValuesString+"','"+ts_times+"','"+ts+"','"+resource+"','"+inputID+"')");
	
}

public void storeOneOutputTS (InputParameters ip, Connection con, int output_id) throws Exception

{
	
	Statement dbStatement = con.createStatement();
	String outputID = ip.DEA_Outputs.elementAt(output_id).inout;
	String resource = ip.DEA_Outputs.elementAt(output_id).resource;
	Vector <String> varNames = new Vector <String>();
	Vector <String> varValues = new Vector <String>();
	
	long startTime = ip.startTime;
	long slotSize = ip.slotSize;
	int numberOfSlots = ip.numberOfSlots;
	String ts_times = "";
	String varNamesString = "";
	String varValuesString = "";
	
	for(int i=0; i<numberOfSlots; i++)
	{
		Timestamp time = new Timestamp(startTime+i*slotSize);
		ts_times += time.toString()+",";
	
	}
	
	ts_times = ts_times.substring(0, ts_times.length()-1); 	
	
	for (int i=0; i<ip.DEA_Outputs.elementAt(output_id).varName.size();i++)
	{
		varNames.add(ip.DEA_Outputs.elementAt(output_id).varName.elementAt(i));
		varNamesString+=ip.DEA_Outputs.elementAt(output_id).varName.elementAt(i)+",";
	}
	
	for (int i=0; i<ip.DEA_Outputs.elementAt(output_id).varValue.size();i++)
	{
		varValues.add(ip.DEA_Outputs.elementAt(output_id).varValue.elementAt(i));
		varValuesString+=ip.DEA_Outputs.elementAt(output_id).varValue.elementAt(i)+",";
	}
	
	if(!varNamesString.isEmpty())
	{varNamesString = varNamesString.substring(0, varNamesString.length()-1);}
	
	if(!varValuesString.isEmpty())
	{varValuesString = varValuesString.substring(0, varValuesString.length()-1);} 
	
	String ts = getOneOutputTS(ip, con, output_id, varNames, varValues, resource);
	
	System.out.println(outputID);
	System.out.println(ts);
	System.out.println(ts_times);
	System.out.println(varNamesString);
	System.out.println(varValuesString);
	System.out.println(resource);
	
	dbStatement.executeUpdate("INSERT INTO deaoutts(input_vars,input_values,ts_times,ts_values,resource,outid) values('"+varNamesString+"','"+varValuesString+"','"+ts_times+"','"+ts+"','"+resource+"','"+outputID+"')");
	
}


public String getOneInputTS(InputParameters ip, Connection con, int input_id, Vector<String> varNames, Vector<String> varValues, String resource) throws Exception
{
	String inputs = "";
	Statement dbStatement = con.createStatement();
	String input = ip.DEA_Inputs.elementAt(input_id).inout;
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
	
		String sqlQuery = "SELECT "+input+
				".* FROM " + vars+
				"(SELECT @t1:='"+sqlt1+
				"') t1, (SELECT @t2:='"+sqlt2+
				"') t2, (SELECT @R1:='"+resource+"') R1, "+input;
		
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	while (rs.next())
	{
		inputs+=rs.getDouble(1)+",";
	}
	}
		
	inputs = inputs.substring(0, inputs.length()-1); 
	System.out.println(inputs);
	return inputs;
}

public String getOneOutputTS(InputParameters ip, Connection con, int output_id, Vector<String> varNames, Vector<String> varValues, String resource) throws Exception
{
	String outputs = "";
	Statement dbStatement = con.createStatement();
	String output = ip.DEA_Outputs.elementAt(output_id).inout;
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
	
		String sqlQuery = "SELECT "+output+
				".* FROM " + vars+
				"(SELECT @t1:='"+sqlt1+
				"') t1, (SELECT @t2:='"+sqlt2+
				"') t2, (SELECT @R1:='"+resource+"') R1, "+output;
		
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	while (rs.next())
	{
		outputs+=rs.getDouble(1)+",";
	}
	}
		
	outputs = outputs.substring(0, outputs.length()-1); 
	System.out.println(outputs);
	return outputs;
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




