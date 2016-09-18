package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
public class GetTimeSeries{
	
	 
public InputParameters getOneRBITSIP (InputParameters ip, Connection con, int rbi_input_id) throws Exception

{
	String rbiID = ip.rbi_inputs.elementAt(rbi_input_id).rbi;
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
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varName.size();i++)
	{
		varNames.add(ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i));
		varNamesString+=ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i)+",";
	}
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varValue.size();i++)
	{
		varValues.add(ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i));
		varValuesString+=ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i)+",";
	}
	
	varNamesString = varNamesString.substring(0, varNamesString.length()-1); 
	varValuesString = varValuesString.substring(0, varValuesString.length()-1); 

	System.out.println(rbiID);
	System.out.println(ts_times);
	System.out.println(varNamesString);
	System.out.println(varValuesString);

	String ts = getOneTS(ip, con, rbi_input_id, varNames, varValues);
	System.out.println(ts);
	
	Statement dbStatement = con.createStatement();
	dbStatement.executeUpdate("INSERT INTO ts(rbi_id,input_vars,input_values,ts_times,ts_values) values('"+rbiID+"','"+varNamesString+"','"+varValuesString+"','"+ts_times+"','"+ts+"')");
	
	String sqlQuery = "SELECT max(ts_id) as m FROM ts";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	rs.next();
	int maxtsid = rs.getInt("m");
	ip.rbi_ts = maxtsid;
	
	return ip;	
	
}


	 
public void getOneRBITS (InputParameters ip, Connection con, int rbi_input_id) throws Exception

{
	String rbiID = ip.rbi_inputs.elementAt(rbi_input_id).rbi;
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
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varName.size();i++)
	{
		varNames.add(ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i));
		varNamesString+=ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i)+",";
	}
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varValue.size();i++)
	{
		varValues.add(ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i));
		varValuesString+=ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i)+",";
	}
	
	varNamesString = varNamesString.substring(0, varNamesString.length()-1); 
	varValuesString = varValuesString.substring(0, varValuesString.length()-1); 

	
	String ts = getOneTS(ip, con, rbi_input_id, varNames, varValues);
	
	System.out.println(rbiID);
	System.out.println(ts);
	System.out.println(ts_times);
	System.out.println(varNamesString);
	System.out.println(varValuesString);
	
	Statement dbStatement = con.createStatement();
	dbStatement.executeUpdate("DELETE FROM ts");
	dbStatement.executeUpdate("INSERT INTO ts(rbi_id,input_vars,input_values,ts_times,ts_values) values('"+rbiID+"','"+varNamesString+"','"+varValuesString+"','"+ts_times+"','"+ts+"')");
		
	
}


public String getOneTSTest(InputParameters ip, Connection con, int rbi) throws Exception
{
	String ts = "";
	Statement dbStatement = con.createStatement();
		
		
	String sqlQuery = "SELECT ts_values FROM ts where ts_id=" +rbi;
		
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	rs.next();
	ts = rs.getString("ts_values");
	
return ts;
}



public String getOneTS(InputParameters ip, Connection con, int rbi_input_id, Vector<String> varNames, Vector<String> varValues) throws Exception
{
	String ts = "";
	Statement dbStatement2 = con.createStatement();
	String rbi = ip.rbi_inputs.elementAt(rbi_input_id).rbi;
	long tsPointTime=ip.startTime;
	
	String vars = "";
	for (int i=0; i<varNames.size();i++)
	{
		vars+="(SELECT @"+varNames.elementAt(i)+":='"+varValues.elementAt(i)+"') "+varNames.elementAt(i)+", ";
	}
	
	System.out.println("ts function vars: "+ vars);
	
	for(int i=0; i<ip.numberOfSlots; i++)
	{
		Timestamp sqlt1 = getSlotStart(ip, (tsPointTime+i*ip.slotSize));
		Timestamp sqlt2 = getSlotEnd(ip, (tsPointTime+i*ip.slotSize));
	
		String sqlQuery = "SELECT "+rbi+
				".* FROM " + vars+
				"(SELECT @t1:='"+sqlt1+
				"') t1, (SELECT @t2:='"+sqlt2+
				"') t2, "+rbi;
	
	System.out.println("ts function query: "+ sqlQuery);	
		
	ResultSet rs = dbStatement2.executeQuery(sqlQuery);
	rs.beforeFirst();
	while (rs.next())
	{
		ts+=rs.getDouble(1)+",";
	}
	}
		
	ts = ts.substring(0, ts.length()-1); 
	System.out.println(ts);
	return ts;
}

public Timestamp getSlotStart(InputParameters ip, long t) throws Exception
{
	Timestamp start = new Timestamp(t+ip.slotStartOffset);
	
	return start;
	
}

public Timestamp getSlotEnd(InputParameters ip, long t) throws Exception
{
	Timestamp end = new Timestamp(t+ip.slotSize+ip.slotEndOffset);
	
	return end;
	
}

public void getOneRBITSMean (InputParameters ip, Connection con, int rbi_input_id) throws Exception

{
	String rbiID = ip.rbi_inputs.elementAt(rbi_input_id).rbi;
	Vector <String> varNames = new Vector <String>();
	Vector <String> varValues = new Vector <String>();
	
	long startTime = ip.startTime;
	long slotSize = ip.slotSize;
	int numberOfSlots = ip.numberOfSlots;
	long endTime = startTime+numberOfSlots*slotSize;
	Timestamp startTimeSQL = new Timestamp(startTime);
	Timestamp endTimeSQL = new Timestamp(endTime);
	System.out.println(startTimeSQL.toString()+"---"+endTimeSQL.toString());

	String ts_times = "";
	String varNamesString = "";
	String varValuesString = "";
	
	for(int i=0; i<numberOfSlots; i++)
	{
		Timestamp time = new Timestamp(startTime+i*slotSize);
		ts_times += time.toString()+",";
	
	}
	
	ts_times = ts_times.substring(0, ts_times.length()-1); 	
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varName.size();i++)
	{
		varNames.add(ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i));
		varNamesString+=ip.rbi_inputs.elementAt(rbi_input_id).varName.elementAt(i)+",";
	}
	
	for (int i=0; i<ip.rbi_inputs.elementAt(rbi_input_id).varValue.size();i++)
	{
		varValues.add(ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i));
		varValuesString+=ip.rbi_inputs.elementAt(rbi_input_id).varValue.elementAt(i)+",";
	}
	
	varNamesString = varNamesString.substring(0, varNamesString.length()-1); 
	varValuesString = varValuesString.substring(0, varValuesString.length()-1); 

	Statement dbStatement = con.createStatement();
	String sqlQuery = "SELECT ts_id from ts where rbi_id= '"+rbiID+"' and " +
			"input_vars= '"+varNamesString+"' and " +
			"input_values= '"+varValuesString+"' and " +
			"ts_times= '"+ts_times+"'";
					
String ts_id = "";	
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){ts_id = rs.getString("ts_id");}
System.out.println(ts_id);

 

ConcurrentSkipListSet<String> resource_set = new ConcurrentSkipListSet<String>();
String sqlQuery2 = "SELECT * from eventlog where time > '"+startTimeSQL+"' and " +
		"time < '"+endTimeSQL+"'";
				
String next_resource = "";	
ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
rs2.beforeFirst();

while (rs2.next())
{
	next_resource = rs2.getString("resource");
	resource_set.add(next_resource);
	
}

Vector<String> resources = new Vector<String>();
resources.addAll(resource_set);

int resourceVarIndex = varNames.indexOf("R1");
System.out.println("R1 index: "+resourceVarIndex);
String ourResource = "";
if(resourceVarIndex>-1)
{ourResource = varValues.elementAt(resourceVarIndex);
System.out.println("R1: "+ourResource);
System.out.println("resources contains our resource before: "+resources.contains(ourResource));
System.out.println("size before"+resources.size());
resources.remove(ourResource);
System.out.println("resources contains our resource after: "+resources.contains(ourResource));
System.out.println("size after"+resources.size());

}
//----------------------------------------------

Vector<String> allTS = new Vector<String>();

for(int i=0; i<resources.size(); i++)
{
	String ts = getOneTSRes(ip, con, rbi_input_id, varNames, varValues, resources.elementAt(i));
	if(isActive(ts))
	{allTS.add(ts);}
}

System.out.println("resources: "+resources.size());
System.out.println("active resources: "+allTS.size());

Vector<Vector<Double>> allTSDouble = new Vector<Vector<Double>>();
for(int i=0; i<allTS.size(); i++)
{
	allTSDouble.add(new Vector<Double>());
	String oneTS[] = allTS.elementAt(i).split(",");
	for(int j=0; j<oneTS.length;j++)
	{
		allTSDouble.elementAt(i).add(Double.parseDouble(oneTS[j]));
	}
	
}

Vector<Double> allTSMean = new Vector<Double>();


int index = 0;
while(index<numberOfSlots)
{ 	
	Vector <Double> oneTSDouble = new Vector<Double>();
	
	if(!ip.excludeTSZeros)
	{
		for(int i=0; i<allTSDouble.size();i++)
		{
		oneTSDouble.add(allTSDouble.elementAt(i).elementAt(index));
		}
	}else
	{
		for(int i=0; i<allTSDouble.size();i++)
		{
			if(allTSDouble.elementAt(i).elementAt(index)>0)
			{oneTSDouble.add(allTSDouble.elementAt(i).elementAt(index));}
		}
	}
	
	Double oneTSMean = getMean(oneTSDouble, ip);
	allTSMean.add(oneTSMean);
	index++;
}

String means = "";

for(int i=0; i<allTSMean.size(); i++)
{
	Double oneMean = allTSMean.elementAt(i);
	means += oneMean.toString()+",";
}	

means = means.substring(0, means.length()-1); 
System.out.println(means);	

String mean_type = ip.meanMethod;
int exclude_zeros = 0;
if(ip.excludeTSZeros){exclude_zeros = 1;}

dbStatement.executeUpdate("delete from ts_mean");
dbStatement.executeUpdate("INSERT INTO ts_mean values('"+ts_id+"','"+means+"','"+ mean_type+"','"+exclude_zeros+"')");

}

public String getOneTSRes(InputParameters ip, Connection con, int rbi_input_id, Vector<String> varNames, Vector<String> varValues, String resource) throws Exception
{
	int resourceVarIndex = varNames.indexOf("R1");
	System.out.println(resourceVarIndex);
	if(resourceVarIndex>-1)
	{System.out.println(varValues.elementAt(resourceVarIndex));
	varNames.remove(resourceVarIndex);
	varValues.remove(resourceVarIndex);	}
	
	String ts = "";
	Statement dbStatement = con.createStatement();
	String rbi = ip.rbi_inputs.elementAt(rbi_input_id).rbi;
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
	
		String sqlQuery = "SELECT "+rbi+
				".* FROM " + vars+
				"(SELECT @t1:='"+sqlt1+
				"') t1, (SELECT @t2:='"+sqlt2+
				"') t2, (SELECT @R1:='"+resource+
				"') R1, "+rbi;
		
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	while (rs.next())
	{
		ts+=rs.getDouble(1)+",";
	}
	}
		
	ts = ts.substring(0, ts.length()-1); 
	return ts;
}

public boolean isActive(String ts)
{
boolean ret = false;

if (ts.contains("1") ||
		ts.contains("2") ||
		ts.contains("3") ||
		ts.contains("4") ||
		ts.contains("5") ||
		ts.contains("6") ||
		ts.contains("7") ||
		ts.contains("8") ||
		ts.contains("9"))
{ret = true;}

return ret;
}

public Double getMean(Vector<Double> oneTSDouble, InputParameters ip)
{
Double ret = 0.0;
String meanMethod = ip.meanMethod;

if(meanMethod.equalsIgnoreCase("median"))
{
	ret = DoubleMedian(oneTSDouble);	
}else
{
	ret = DoubleAverage(oneTSDouble);	
}

return ret;
}

public Double DoubleMedian(Vector<Double> numbers)
{
	Collections.sort(numbers);
	double median = 0.0;
	int size = numbers.size();
	if (size!=0)
	{if( (size % 2) != 0)
	{median = numbers.elementAt((size+1)/2-1);}
	else
	{median = 0.5*numbers.elementAt(size/2-1)+0.5*numbers.elementAt(size/2);}}
	return median;
}

public Double DoubleAverage(Vector<Double> numbers)
{
		double average = 0.0;
		int size = numbers.size();
		double sum = 0.00;
		
		for (int i=0; i<size; i++)
		{
			sum+=numbers.elementAt(i);
		}

		average = sum/size;
		
return average;
}

}




