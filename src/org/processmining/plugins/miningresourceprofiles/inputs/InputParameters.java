package org.processmining.plugins.miningresourceprofiles.inputs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.joda.time.DateTime;
import org.processmining.plugins.miningresourceprofiles.analysis.RegressionData;

public class InputParameters{
	
	public long slotStartOffset;
	public long slotEndOffset;
	
	public String hostname;
	public String dbuser;
	public String password;
	public String dbname;
	
	public int cp;
	public int out;
	public int trend;
	
	public String regType;
	public int numberExpVars;
	
	public Vector<Indicator_Input> rbi_inputs = new Vector<Indicator_Input>();
	public Vector<DEA_InOut> DEA_Inputs = new Vector<DEA_InOut>();
	public Vector<DEA_InOut> DEA_Outputs = new Vector<DEA_InOut>();
	public Vector<String> resources = new Vector<String>();
	public Vector<String> tasks = new Vector<String>();
	public Vector<String> roles = new Vector<String>();
	public Vector<RegressionData> regressionInputs = new Vector<RegressionData>();
	
	public String logtable;
	public String caseid;
	public String activity;
	public String resource;
	public String role;
	public String time;
	public String type;
	public String caseidtype = "varchar(500)";
	public String activitytype = "varchar(500)";
	public String resourcetype = "varchar(500)";
	public String roletype = "varchar(500)";
	public String timetype = "datetime";
	public String typetype = "varchar(100)";
	
	public Integer rbi_ts;
	
	public long startTime;
	public long slotSize;
	public int numberOfSlots;
	public int decNum; 
	
	public String meanMethod; 
	public String regressionMethod; 
	public boolean excludeTSZeros;
	
	public String deatype; 
	public String RTS;
	public Boolean usecosts; //DEA output costs
	public Boolean useInputCosts;
	
	public String cpmType;
	public String ARL0;
	public String startup;
	
	public String method;
	
	public String period;
	
	public InputParameters() {
		
		meanMethod = "average";
		regressionMethod = "linear";//"non-parametric"
		excludeTSZeros = false;
		decNum = 3;
		slotStartOffset = 0;//-604800000l;
		slotEndOffset = 0;//604800000l;
	};
	
	public void setDB(String host, String user, String pass, String db)
	{
		hostname = host;
		dbuser = user;
		password = pass;
		dbname = db;
	}


InputParameters(Connection con) throws SQLException {
	
	Statement dbStatement = con.createStatement();
		
	DateTime dt = new DateTime(2012, 7, 1, 0, 0, 0); 
	startTime = dt.getMillis();
	slotSize = 604800000l;
	numberOfSlots = 109;		
	decNum = 1000; 
	
	String curres = resources.elementAt(0);
	String sqlQuery = "SELECT distinct task FROM el where resource='"+curres+"'"; 
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	while(rs.next())
	{	
	String curtask = rs.getString(1);
	tasks.add(curtask);
	}
		
			Vector<String> varNames = new Vector<String>();
			Vector<String> varValues = new Vector<String>();
			varNames.add("Task");
			DEA_Inputs.add(new DEA_InOut("input4",varNames,varValues,resources.elementAt(0),1.0));
			
			for(int i=0; i<tasks.size(); i++)
			{
				varValues.removeAllElements();
				varValues.add(tasks.elementAt(i));
				DEA_Outputs.add(new DEA_InOut("output1",varNames,varValues,curres,1.0));
				}
		
	}
	
	InputParameters(String test1, String test2) {
		
					};
	
	public void addRBIInput(String rbiid, Vector<String> varNames, Vector<String> varValues)
	{
		rbi_inputs.add(new Indicator_Input(rbiid, varNames, varValues));
	}
	
	InputParameters(String testing) {
			
			
		DateTime dt = new DateTime(2012, 7, 16, 0, 0, 0); 
		startTime = dt.getMillis();
		slotSize = 604800000l;
		numberOfSlots = 100;		
			
		meanMethod = "average";
		decNum = 100; 
		excludeTSZeros = true;
		
		Vector<String> varNames = new Vector<String>();
		varNames.add("R1");
		varNames.add("Var");
		Vector<String> varValues = new Vector<String>();
		varValues.add(resources.elementAt(0));
		varValues.add(roles.elementAt(0));
		
	
	cpmType = "Mann-Whitney";
	ARL0 = "10000";
	startup = "20";
	method = "I";
	period = "recent";
	
		
	} 
	
}




