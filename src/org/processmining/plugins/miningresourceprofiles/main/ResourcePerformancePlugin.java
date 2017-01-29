package org.processmining.plugins.miningresourceprofiles.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.miningresourceprofiles.inputs.DatabaseHandler;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineRBIParams;
import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;

import com.fluxicon.slickerbox.components.HeaderBar;

public class ResourcePerformancePlugin {
	static MainGUILeft maingui = new MainGUILeft();
	static DefineRBIParams defPar = new DefineRBIParams();
	
		@Plugin(
			name = "Mining Resource Profiles", 
			parameterLabels = { "Event log" }, 
			returnLabels = {"Resource Analysis Results"}, 
			returnTypes = {HeaderBar.class}, 
			userAccessible = true, 
			help = ""
		)
		@PluginVariant(variantLabel = "Mining Resource Profiles", requiredParameterLabels = { 0 })
		@UITopiaVariant(
			affiliation = "QUT", 
			author = "A.Pika", 
			email = "a.pika@qut.edu.au"
		)

public HeaderBar  main (UIPluginContext context, XLog inputlog) throws Exception {
		
			//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true"); same error
			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
			
			InputParameters ip = new InputParameters();
			ip = defPar.defineDBParams(ip);
			String host = ip.hostname;
			String user = ip.dbuser;
			String pass = ip.password;
			String db = ip.dbname;
			
			DatabaseHandler dh = new DatabaseHandler();
			Connection con = dh.getDBConnection("jdbc:mysql://"+host, user, pass);
			Statement dbStatement = con.createStatement();
			ResultSet rs = dbStatement.executeQuery("SELECT IF('"+db+"' IN(SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA), 1, 0) AS found");
	    	rs.beforeFirst();
	      	rs.next();
	     	int dbexists = rs.getInt("found");
	       // System.out.println(dbexists);
	        
	        if (dbexists == 1)
	        {con = dh.getDBConnection("jdbc:mysql://"+host+"/"+db, user, pass);}else
	        {
	        	dbStatement.executeUpdate("CREATE DATABASE "+db);
	        	con = dh.getDBConnection("jdbc:mysql://"+host+"/"+db, user, pass);
	        	
	        }
	 

			Rengine re=new Rengine (new String [] {"--vanilla"}, false, null);	
			return maingui.displayMainGUI(con, inputlog, re, host, user, pass, db);
			
		


}
}
