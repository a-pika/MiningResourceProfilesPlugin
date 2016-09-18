package org.processmining.plugins.miningresourceprofiles.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.processmining.plugins.miningresourceprofiles.analysis.CompareTS;
import org.processmining.plugins.miningresourceprofiles.analysis.GetChangePoints;
import org.processmining.plugins.miningresourceprofiles.analysis.GetDEAProductivity;
import org.processmining.plugins.miningresourceprofiles.analysis.GetOutliers;
import org.processmining.plugins.miningresourceprofiles.analysis.GetRegression;
import org.processmining.plugins.miningresourceprofiles.analysis.GetRegressionData;
import org.processmining.plugins.miningresourceprofiles.analysis.GetTimeSeries;
import org.processmining.plugins.miningresourceprofiles.analysis.GetTrend;
import org.processmining.plugins.miningresourceprofiles.define.DefineRBI;
import org.processmining.plugins.miningresourceprofiles.define.DefineRegressionVars;
import org.processmining.plugins.miningresourceprofiles.define.DefineVar;
import org.processmining.plugins.miningresourceprofiles.inputs.DatabaseHandler;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineDEAInOut;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineELDBMapping;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineRBIParams;
import org.processmining.plugins.miningresourceprofiles.inputs.GetDEAInOut;
import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.processmining.plugins.miningresourceprofiles.inputs.LogDataProcessing;
import org.processmining.plugins.miningresourceprofiles.outputs.VisualiseResults;

//for testing
public class Main {
	
	static DefineVar defVar = new DefineVar();
	static DefineRBI defRBI = new DefineRBI();
	static GetTimeSeries getTS = new GetTimeSeries();
	static GetChangePoints getCP = new GetChangePoints();
	static GetOutliers getOut = new GetOutliers();
	static GetTrend getTrend = new GetTrend();
	static CompareTS compTS = new CompareTS();
	static VisualiseResults visRes = new VisualiseResults();
	static DefineDEAInOut defdea = new DefineDEAInOut();
	static GetDEAInOut getdea = new GetDEAInOut();
	static GetDEAProductivity getdeaeff = new GetDEAProductivity();
	static GetRegression getreg = new GetRegression();
	static DefineRegressionVars defregvars = new DefineRegressionVars();
	static GetRegressionData getregdata = new GetRegressionData();
	static LogDataProcessing procLog = new LogDataProcessing();
	static DefineELDBMapping map = new DefineELDBMapping();
	static DefineRBIParams defPar = new DefineRBIParams();
	
	
	public static void main (String args[]) throws Exception {
		
		InputParameters ip = new InputParameters();
		ip.hostname = "localhost";
		ip.dbuser = "root";
		ip.dbname = "dbname";
		ip.password = "mysqlpass";
		
		String host = ip.hostname;
		String user = ip.dbuser;
		String pass = ip.password;
		String db = ip.dbname;
	
		DatabaseHandler dh = new DatabaseHandler();
		Connection con = dh.getDBConnection("jdbc:mysql://"+host, user, pass);
		Statement dbStatement00 = con.createStatement();
		ResultSet rs00 = dbStatement00.executeQuery("SELECT IF('"+db+"' IN(SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA), 1, 0) AS found");
    	rs00.beforeFirst();
      	rs00.next();
     	int dbexists = rs00.getInt("found");
        System.out.println(dbexists);
        
        //process builder
    /*    ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "SET");
        Map<String, String> env = pb.environment();
        env.put("R_HOME2", "myValue");
        env.put("Path2", "myValue2");
        Process p = pb.start();
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        char[] buf = new char[1024];
        while (!isr.ready()) {
            ;
        }
        while (isr.read(buf) != -1) {
            System.out.println(buf);
        }
*/        
       
 
        
        
        
  
	}

	
}
