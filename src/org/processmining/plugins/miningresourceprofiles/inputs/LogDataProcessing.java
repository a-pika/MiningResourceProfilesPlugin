package org.processmining.plugins.miningresourceprofiles.inputs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;


public class LogDataProcessing {
	
public void createDB(final Connection con, XLog log, InputParameters ip)throws Exception
{
		Statement dbStatement = con.createStatement();
		String db = ip.dbname;
	
		dbStatement.executeUpdate("DROP DATABASE IF EXISTS "+db);
		dbStatement.executeUpdate("CREATE DATABASE "+db);
		dbStatement.executeQuery("use "+db);
		
		dbStatement.executeUpdate("CREATE TABLE change_points(ts_id int(11), cpmtype varchar(250), arl varchar(250), startup varchar(250), cp varchar(5000))");
		dbStatement.executeUpdate("CREATE TABLE deainput(id int(11) NOT NULL AUTO_INCREMENT, name varchar(500), definition varchar(5000), PRIMARY KEY (`id`))");
		dbStatement.executeUpdate("CREATE TABLE deaints(ts_id int(11) NOT NULL AUTO_INCREMENT, input_vars varchar(5000), input_values varchar(5000), ts_times blob, ts_values varchar(5000), resource varchar(500), inid varchar(50), PRIMARY KEY (`ts_id`))");
		dbStatement.executeUpdate("CREATE TABLE deaoutput(id int(11) NOT NULL AUTO_INCREMENT, name varchar(500), definition varchar(5000), PRIMARY KEY (`id`))");
		dbStatement.executeUpdate("CREATE TABLE deaoutts(ts_id int(11) NOT NULL AUTO_INCREMENT, input_vars varchar(5000), input_values varchar(5000), ts_times blob, ts_values varchar(5000), resource varchar(500), outid varchar(50), PRIMARY KEY (`ts_id`))");
		dbStatement.executeUpdate("CREATE TABLE outliers(ts_id int(11), method varchar(250), outliers_right varchar(5000), outliers_left varchar(5000))");
		dbStatement.executeUpdate("CREATE TABLE rbis(id int(11) NOT NULL AUTO_INCREMENT, name varchar(500), definition varchar(5000), PRIMARY KEY (`id`))");
		dbStatement.executeUpdate("CREATE TABLE regressionviews(id int(11) NOT NULL AUTO_INCREMENT, name varchar(500), definition varchar(5000), type varchar(50), PRIMARY KEY (`id`))");
		dbStatement.executeUpdate("CREATE TABLE trends(ts_id int(11), slope double, intercept double, period varchar(20))");
		dbStatement.executeUpdate("CREATE TABLE ts(ts_id int(11) NOT NULL AUTO_INCREMENT, rbi_id varchar(50), input_vars varchar(5000), input_values varchar(5000), ts_times blob, ts_values varchar(5000), PRIMARY KEY (`ts_id`))");
		dbStatement.executeUpdate("CREATE TABLE ts_compare(ts_id int(11), pvalue double, mean_type varchar(20), exclude_zeros int(11))");
		dbStatement.executeUpdate("CREATE TABLE ts_mean(ts_id int(11), ts_values varchar(5000), mean_type varchar(20), exclude_zeros int(11))");
		dbStatement.executeUpdate("CREATE TABLE vars(name varchar(20), type varchar(20))");
		dbStatement.executeUpdate("CREATE TABLE views(name varchar(500), description varchar(5000), definition varchar(5000))");
		
		Vector <String> ea = new Vector<String>();
		Vector <String> ca = new Vector<String>();
		List<XAttribute> eal = log.getGlobalEventAttributes(); 
		List<XAttribute> cal = log.getGlobalTraceAttributes();
		 
		for (int i=0; i<eal.size();i++)
		{ea.add(eal.get(i).getKey());}
		for (int i=0; i<cal.size();i++)
		{ca.add(cal.get(i).getKey());}
		
		DefineELDBMapping mapfun = new DefineELDBMapping();
		ELDBmapping map = new ELDBmapping();
		map = mapfun.getELMapping(ea);
		
		DefineELDBMapping mapfun2 = new DefineELDBMapping();
		map = mapfun2.getCLMapping(ca, map);
		String casetype = map.DBCLTypes.elementAt(0);
		
		String eastring = "";
		for(int i=0; i<map.DBELAttributes.size(); i++)
		{
			if (!map.DBELAttributes.elementAt(i).equalsIgnoreCase("NONE") && !map.DBELTypes.elementAt(i).equalsIgnoreCase("NONE"))
			{
				eastring+= map.DBELAttributes.elementAt(i) +" "+ map.DBELTypes.elementAt(i)+", ";
			}
		}
		
		if(eastring.length()>0){eastring = eastring.substring(0, eastring.length()-2); }
		
		String elquery = "CREATE TABLE eventlog(caseid "+casetype+", "+eastring+")";
		dbStatement.executeUpdate(elquery);
		
		String castring = "";
		
		for(int i=0; i<map.DBCLAttributes.size(); i++)
		{
			if (!map.DBCLAttributes.elementAt(i).equalsIgnoreCase("NONE") && !map.DBCLTypes.elementAt(i).equalsIgnoreCase("NONE"))
			{
				castring+= map.DBCLAttributes.elementAt(i) +" "+ map.DBCLTypes.elementAt(i)+", ";
			}
		}
		
		if(castring.length()>0){castring = castring.substring(0, castring.length()-2); }
	
		String clquery = "CREATE TABLE caselog("+castring+")";
		dbStatement.executeUpdate(clquery);
		
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('R1','string')");
		dbStatement.executeUpdate("CREATE FUNCTION R1() RETURNS varchar(100) deterministic no sql RETURN @R1");
		
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('R','string')");
		dbStatement.executeUpdate("CREATE FUNCTION R() RETURNS varchar(100) deterministic no sql RETURN @R");
		
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('Task','string')");
		dbStatement.executeUpdate("CREATE FUNCTION Task() RETURNS varchar(100) deterministic no sql RETURN @Task");
	
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('c','string')");
		dbStatement.executeUpdate("CREATE FUNCTION c() RETURNS varchar(100) deterministic no sql RETURN @c");
	
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('eventid','string')");
		dbStatement.executeUpdate("CREATE FUNCTION eventid() RETURNS varchar(100) deterministic no sql RETURN @eventid");
			
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('t1','time')");
		dbStatement.executeUpdate("CREATE FUNCTION t1() RETURNS datetime deterministic no sql RETURN @t1");
	
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('t2','time')");
		dbStatement.executeUpdate("CREATE FUNCTION t2() RETURNS datetime deterministic no sql RETURN @t2");
		
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('intVar','int')");
		dbStatement.executeUpdate("CREATE FUNCTION intVar() RETURNS INTEGER deterministic no sql RETURN @intVar");
	
		dbStatement.executeUpdate("INSERT INTO vars VALUES ('doubleVar','double')");
		dbStatement.executeUpdate("CREATE FUNCTION doubleVar() RETURNS double deterministic no sql RETURN @doubleVar");
	
		createSQLViews(con);
		
		//TODO - different log pre-processing versions
		
		//V2 - old
		//logToDB(log, con, map);
		//addAttributesV2(log, con, map);

		//V3 - old
		//logToDBV2(log, con, map);
		
		//V4 (pre-processing in memory - with sort)
		//logToDBV3(log, con, map);
		
		//V1 (querying DB - long processing times)
		//logToDB(log, con, map);
		//addAttributes(log, con, map);
		
		//V5 (pre-processing in memory - map of resource events)
		logToDBV4(log, con, map);

};	 


public void logToDB(XLog log, Connection con, ELDBmapping map) throws Exception
{


Vector <String> cadb = new Vector <String>(); 
Vector <String> cael = new Vector <String>(); 
Vector <String> eadb = new Vector <String>(); 
Vector <String> eael = new Vector <String>(); 

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(!map.ELAttributes.elementAt(i).equals("NONE") && !map.ELAttributes.elementAt(i).equals("CALCULATE") && !map.DBELAttributes.elementAt(i).equals("NONE"))
	{
		eael.add(map.ELAttributes.elementAt(i));
		eadb.add(map.DBELAttributes.elementAt(i));
	}
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(!map.CLAttributes.elementAt(i).equals("NONE") && !map.CLAttributes.elementAt(i).equals("CALCULATE") && !map.DBCLAttributes.elementAt(i).equals("NONE"))
	{
		cael.add(map.CLAttributes.elementAt(i));
		cadb.add(map.DBCLAttributes.elementAt(i));
	}
}


String edbline = "";
Vector <String> equeries = new Vector<String>();
String cdbline = "";
Vector <String> cqueries = new Vector<String>();

String casedbnames = "";
for(int i=0; i<cadb.size(); i++)
{
	casedbnames+=cadb.elementAt(i)+",";
}
casedbnames = casedbnames.substring(0, casedbnames.length()-1);

String eventdbnames = "caseid,";
for(int i=0; i<eadb.size(); i++)
{
	eventdbnames+=eadb.elementAt(i)+",";
}
eventdbnames = eventdbnames.substring(0, eventdbnames.length()-1);

String caselogdata = "";
String eventlogdata = "";

for (XTrace t : log) 
{
	caselogdata = "";
	XAttributeLiteral caseidattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(0));
	String caseid = caseidattr.getValue();
	
	
	for(int i=0; i<cael.size(); i++)
	{
		XAttributeLiteral nextcaseattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(i));
		String next = nextcaseattr.getValue();
		caselogdata+= "'"+next+"',";
	}
	
	caselogdata = caselogdata.substring(0, caselogdata.length()-1);
	//System.out.println(caselogdata);
	
	cdbline = "insert into caselog("+casedbnames+") values("+caselogdata+")";	
	cqueries.add(cdbline);

	for (XEvent e : t) 
	{eventlogdata = "'"+caseid+"',";
		
			
		for(int i=0; i<eael.size(); i++)
		{
			if(eadb.elementAt(i).equals("time"))
			{
				XAttributeTimestamp nexteventattr = (XAttributeTimestamp)e.getAttributes().get(eael.elementAt(i));
				Date nexte = nexteventattr.getValue();
				long nexttime = nexte.getTime();
				Timestamp sqltime = new Timestamp(nexttime);
				
				eventlogdata+= "'"+sqltime+"',";
			}
			else
			{
			XAttributeLiteral nexteventattr = (XAttributeLiteral)e.getAttributes().get(eael.elementAt(i));
			String nexte;
			if(!(nexteventattr == null))
			{nexte = nexteventattr.getValue();}else{nexte = "NULL";}
			eventlogdata+= "'"+nexte+"',";
			}
			
		}
		
		eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
		//System.out.println(eventlogdata);

		
		edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
		equeries.add(edbline);

	}
	
}

Statement statement = con.createStatement();

for (String equery : equeries) {
    statement.addBatch(equery);
}

for (String cquery : cqueries) {
    statement.addBatch(cquery);
}
statement.executeBatch();
statement.close();

System.out.println("basic log imported");

}


public void createSQLViews(Connection con) throws Exception
{
	Statement dbStatement = con.createStatement();
	Statement dbStatement2 = con.createStatement();
	Statement dbStatement3 = con.createStatement();
	
	
	Vector<String> eviews = new Vector<String>();
	Vector<String> erbis = new Vector<String>();
	Vector<String> eregviews = new Vector<String>();
	String sqlViews = "select name from views";
	String sqlRBIs = "select id from rbis";
	String sqlRegViews = "select id from regressionviews";
	ResultSet rs1 = dbStatement.executeQuery(sqlViews);
	rs1.beforeFirst();
	while(rs1.next())
	{
		eviews.add(rs1.getString("name"));
	}
	ResultSet rs2 = dbStatement2.executeQuery(sqlRBIs);
	rs2.beforeFirst();
	while(rs2.next())
	{
		erbis.add("rbi"+rs2.getString("id"));
	}
	ResultSet rs3 = dbStatement3.executeQuery(sqlRegViews);
	rs3.beforeFirst();
	while(rs3.next())
	{
		eregviews.add("regview"+rs3.getString("id"));
	}

	if(eviews.size()>0)
	{
		for(int i=0; i<eviews.size(); i++)
		{
			dbStatement.executeUpdate("DROP VIEW IF EXISTS "+eviews.elementAt(i));
		}
	}
	
	if(erbis.size()>0)
	{
		for(int i=0; i<erbis.size(); i++)
		{
			dbStatement.executeUpdate("DROP VIEW IF EXISTS "+erbis.elementAt(i));
		}
	}
	
	if(eregviews.size()>0)
	{
		for(int i=0; i<eregviews.size(); i++)
		{
			dbStatement.executeUpdate("DROP VIEW IF EXISTS "+eregviews.elementAt(i));
		}
	}
	dbStatement.executeUpdate("delete from views");
	dbStatement2.executeUpdate("delete from rbis");
	dbStatement3.executeUpdate("delete from regressionviews");

			String viewdef = "";
			String viewname = "";
			String viewid = "";
			
			viewdef = "select * from eventlog where time>=t1() and time<t2()";
			viewid = "et";
			viewname = "Events recorded during time slot";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select * from et where resource=R1()";
			viewid = "etr";
			viewname = "Events recorded during time slot in which a given resource was involved";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select * from et where type=''complete''";
			viewid = "ect";
			viewname = "Events completed during time slot";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select * from ect where resource=R1()";
			viewid = "ectr";
			viewname = "Events completed by a resource during time slot";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select caseid from caselog where enddate>=t1() and enddate<t2()";
			viewid = "cct";
			viewname = "Cases completed during time slot";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select caseid from cct where caseid in (select caseid from eventlog where resource=R1())";
			viewid = "cctr";
			viewname = "Cases completed during time slot in which a resource was involved";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
			
			viewdef = "select distinct caseid from et where resource=R1()";
			viewid = "ctr";
			viewname = "Cases active during time slot in which a resource was involved";
			dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+viewid+"','"+viewname+"','"+viewdef+"')");
	
			
			Vector<String> viewids = new Vector<String>();
			Vector<String> viewdefs = new Vector<String>();
			String sqlQuery0 = "SELECT * FROM views";
			ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
			rs0.beforeFirst();
			while(rs0.next())
			{
				viewids.add(rs0.getString("name"));
				viewdefs.add(rs0.getString("definition"));
			}
			
			for(int i=0; i<viewids.size(); i++)
			{
				dbStatement.executeUpdate("CREATE VIEW "+viewids.elementAt(i)+" as "+viewdefs.elementAt(i));
			}
	
			String rbidef = "";
			String rbiname = "";
			
			rbiname = "Distinct Activities: number of distinct tasks completed by a resource";
			rbidef = "select count(distinct task) from ectr";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Activity Frequency: fraction of completions of a given task by a resource";
			rbidef = "select (select count(*) from ectr where task=Task())/(select count(*) from ectr)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
		
			rbiname = "Activity Completions: number of task instances completed by a resource";
			rbidef = "select count(*) from ectr";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Activity Completions: number of completions of a given task by a resource";
			rbidef = "select count(*) from ectr where task=Task()";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Number of case completions: number of cases completed in which a resource was involved";
			rbidef = "select count(*) from cctr";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Fraction of task completions: fraction of task instances completed by a resource";
			rbidef = "select (select count(*) from ectr)/(select count(*) from ect)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Fraction of case completions: fraction of cases completed in which a resource was involved";
			rbidef = "select (select count(*) from cctr)/(select count(*) from cct)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Average workload";
			rbidef = "select (select sum(workload*workload_duration) from etr)/(select sum(workload_duration) from etr)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
	
			rbiname = "Multitasking";
			rbidef = "select (select sum(workload_duration) from etr where workload>1)/(select sum(workload_duration) from etr where workload>0)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Activity Reassignments - without activity repetitions";
			rbidef = "select count(*) from etr where type=''start'' and exists (select * from eventlog where type=''complete'' and resource<>R1() and etr.caseid=eventlog.caseid and etr.task=eventlog.task and etr.time<eventlog.time)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
		
			rbiname = "Average duration of a task completed by a resource - in hours";
			rbidef = "select avg(duration/3600000) from ectr";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Average duration of a given task completed by a resource - in hours";
			rbidef = "select avg(duration/3600000) from ectr where task=Task()";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Average duration of a case in which a resource was involved - in hours";
			rbidef = "select avg(duration/3600000) from caselog where caseid in (select * from cctr)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
		
			rbiname = "Average number of resources involved in the same cases with a given resource";
			rbidef = "select avg(number_of_resources) from caselog where caseid in (select * from cctr)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
		
			rbiname = "Social position: fraction of resources involved in the same cases with a given resource";
			rbidef = "select (select count(distinct resource) from et where resource<>R1() and caseid in (select caseid from ctr))/(select count(distinct resource) from et where resource<>R1())";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Number of distinct resources that executed a given activity";
			rbidef = "select count(distinct resource) from ect where task=Task()";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
			
			rbiname = "Interactions between two given resources";
			rbidef = "select count(*) from cctr where caseid in (select distinct caseid from eventlog where resource=R()) and caseid in (select caseid from cct)";
			dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+rbiname+"','"+rbidef+"')");
	
			Vector<String> rbiids = new Vector<String>();
			Vector<String> rbidefs = new Vector<String>();
			String sqlQuery = "SELECT * FROM RBIs";
			ResultSet rs = dbStatement.executeQuery(sqlQuery);
			rs.beforeFirst();
			while(rs.next())
			{
				rbiids.add(rs.getString("id"));
				rbidefs.add(rs.getString("definition"));
			}
			
			for(int i=0; i<rbiids.size(); i++)
			{
				dbStatement.executeUpdate("CREATE VIEW rbi"+rbiids.elementAt(i)+" as "+rbidefs.elementAt(i));
			}
			
			String regViewName;
			String regViewDef;
			String regViewType;
			
			regViewName = "All cases";
			regViewDef = "select caseid from caselog";
			regViewType = "case_definition";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "All cases in which a resource was involved";
			regViewDef = "select caseid from caselog where caseid in (select distinct caseid from eventlog where resource=R1())";
			regViewType = "case_definition";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");

			regViewName = "All completed tasks";
			regViewDef = "select distinct eventid from eventlog where type=''complete''";
			regViewType = "task_definition";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "Tasks completed by a resource";
			regViewDef = "select distinct eventid from eventlog where resource=R1() and type=''complete''";
			regViewType = "task_definition";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "All work items of a given task";
			regViewDef = "select distinct eventid from eventlog where task=Task() and type=''complete''";
			regViewType = "task_definition";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
		
			regViewName = "Number of resources involved in a case";
			regViewDef = "select number_of_resources from caselog where caseid=c()";
			regViewType = "case_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "Number of tasks completed in a case";
			regViewDef = "select count(*) from eventlog where caseid=c() and type=''complete''";
			regViewType = "case_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "Percentage of tasks in a case executed by a given resource";
			regViewDef = "select (select count(distinct eventid) from eventlog where caseid=c() and resource=R1())/(select count(distinct eventid) from eventlog where caseid=c())";
			regViewType = "case_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
	  
			regViewName = "Task repetition index in a case";
			regViewDef = "select task_index from eventlog where eventid=eventid()";
			regViewType = "task_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
	
			
		
			regViewName = "Number of distinct tasks completed by a resource";
			regViewDef = "select count(distinct task) from ectr";
			regViewType = "time_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "Number of tasks completed by a resource";
			regViewDef = "select count(*) from ectr";
			regViewType = "time_behaviour";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
	
			
			regViewName = "Case duration";
			regViewDef = "select duration from caselog where caseid=c()";
			regViewType = "case_outcome";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
	
			
			regViewName = "Task duration - in hours";
			regViewDef = "select duration/3600000 from eventlog where eventid=eventid() and type=''complete''";
			regViewType = "task_outcome";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
	
			
			regViewName = "Number of tasks completed by a resource";
			regViewDef = "select count(*) from ectr";
			regViewType = "time_outcome";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
			
			regViewName = "Average duration of a task completed by a resource - in hours";
			regViewDef = "select avg(duration/3600000) from ectr";
			regViewType = "time_outcome";
			dbStatement.executeUpdate("INSERT INTO regressionviews(name, definition,type) VALUES ('"+regViewName+"','"+regViewDef+"','"+regViewType+"')");
		
			Vector<String> regviewids = new Vector<String>();
			Vector<String> regviewdefs = new Vector<String>();
			String sqlQuery4 = "SELECT * FROM regressionviews";
			ResultSet rs4 = dbStatement.executeQuery(sqlQuery4);
			rs4.beforeFirst();
			while(rs4.next())
			{
				regviewids.add(rs4.getString("id"));
				regviewdefs.add(rs4.getString("definition"));
			}
			
			for(int i=0; i<regviewids.size(); i++)
			{
				dbStatement.executeUpdate("CREATE VIEW regview"+regviewids.elementAt(i)+" as "+regviewdefs.elementAt(i));
			}

			String DEAInName;
			String DEAInDef;
			
			DEAInName = "No inputs";
			DEAInDef = "select 1";
			
			dbStatement.executeUpdate("INSERT INTO deainput(name, definition) VALUES ('"+DEAInName+"','"+DEAInDef+"')");
		
			String sqlQuery1 = "SELECT MAX(id) as m FROM deainput";
			ResultSet rs11 = dbStatement.executeQuery(sqlQuery1);
			rs11.beforeFirst();
			String lastid = "";
			if (rs11.next()){lastid = rs11.getString("m");}
									
			String sqlQuery2 = "SELECT definition FROM deainput where id='"+lastid+"'";
			ResultSet rs22 = dbStatement.executeQuery(sqlQuery2);
			rs22.beforeFirst();
			String newDefinition = "";
			if (rs22.next()){newDefinition = rs22.getString("definition");}
			
			dbStatement.executeUpdate("CREATE VIEW input"+lastid+" as "+newDefinition);
			DEAInName = "Number of cases active during the time slot";
			DEAInDef = "select count(distinct caseid) from et";
			
			dbStatement.executeUpdate("INSERT INTO deainput(name, definition) VALUES ('"+DEAInName+"','"+DEAInDef+"')");
		
			String sqlQuery10 = "SELECT MAX(id) as m FROM deainput";
			ResultSet rs110 = dbStatement.executeQuery(sqlQuery10);
			rs110.beforeFirst();
			String lastid0 = "";
			if (rs110.next()){lastid0 = rs110.getString("m");}
									
			String sqlQuery20 = "SELECT definition FROM deainput where id='"+lastid0+"'";
			ResultSet rs220 = dbStatement.executeQuery(sqlQuery20);
			rs220.beforeFirst();
			String newDefinition0 = "";
			if (rs220.next()){newDefinition0 = rs220.getString("definition");}
			
			dbStatement.executeUpdate("CREATE VIEW input"+lastid0+" as "+newDefinition0);
		
			String DEAOutName = "Number of task instances completed";
			String DEAOutDef = "select count(*) from ectr";
			
			dbStatement.executeUpdate("INSERT INTO deaoutput(name, definition) VALUES ('"+DEAOutName+"','"+DEAOutDef+"')");
				
			String sqlQuery5 = "SELECT MAX(id) as m FROM deaoutput";
			ResultSet rs5 = dbStatement.executeQuery(sqlQuery5);
			rs5.beforeFirst();
			String lastid5 = "";
			if (rs5.next()){lastid5 = rs5.getString("m");}
									
			String sqlQuery50 = "SELECT definition FROM deaoutput where id='"+lastid5+"'";
			ResultSet rs50 = dbStatement.executeQuery(sqlQuery50);
			rs50.beforeFirst();
			String newDefinition5 = "";
			if (rs50.next()){newDefinition5 = rs50.getString("definition");}
			
			dbStatement.executeUpdate("CREATE VIEW output"+lastid5+" as "+newDefinition5);
			
		
			DEAOutName = "Number of completions of a given task";
			DEAOutDef = "select count(*) from ectr where task=Task()";
			
			dbStatement.executeUpdate("INSERT INTO deaoutput(name, definition) VALUES ('"+DEAOutName+"','"+DEAOutDef+"')");
		
			String sqlQuery6 = "SELECT MAX(id) as m FROM deaoutput";
			ResultSet rs6 = dbStatement.executeQuery(sqlQuery6);
			rs6.beforeFirst();
			String lastid6 = "";
			if (rs6.next()){lastid6 = rs6.getString("m");}
									
			String sqlQuery60 = "SELECT definition FROM deaoutput where id='"+lastid6+"'";
			ResultSet rs60 = dbStatement.executeQuery(sqlQuery60);
			rs60.beforeFirst();
			String newDefinition6 = "";
			if (rs60.next()){newDefinition6 = rs60.getString("definition");}
			
			dbStatement.executeUpdate("CREATE VIEW output"+lastid6+" as "+newDefinition6);
			
			System.out.println("Views created!");

}


public void addAttributes(XLog log, Connection con, ELDBmapping map) throws Exception
{

	Statement dbStatementFirst = con.createStatement();
	String sqlQuery = "SELECT * FROM eventlog";
	ResultSet rs = dbStatementFirst.executeQuery(sqlQuery);

String edbline = "";
Vector <String> equeries = new Vector<String>();
String cdbline = "";
Vector <String> cqueries = new Vector<String>();

Integer eventindex = 0; 

Boolean eventidb = false;
Boolean task_indexb = false;
Boolean durationb = false;
Boolean workloadb = false;
Boolean workload_durationb = false;

Boolean number_of_resourcesb = false;
Boolean casedurationb = false;
Boolean enddateb = false;

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("eventid"))
	{
		eventidb = true;
		//System.out.println("eventid true");
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("task_index"))
	{
		task_indexb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("duration"))
	{
		durationb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("workload"))
	{
		workloadb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("workload_duration"))
	{
		workload_durationb = true;
	}
	
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("number_of_resources"))
	{
		number_of_resourcesb = true;
	}
	
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("duration"))
	{
		casedurationb = true;
	}
	
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("enddate"))
	{
		enddateb = true;
	}
}



while(rs.next()) {
	
	Statement dbStatement = con.createStatement();

	eventindex++;
		
	String caseid = rs.getString("caseid");
	String task = rs.getString("task");
	String type = rs.getString("type"); 
	Timestamp time = rs.getTimestamp("time");
	String resource = rs.getString("resource");
	
	//System.out.println("time: "+time);
	
	String task_index = "0";
	String duration = "0";
	String workload = "0";
	String workload_duration = "0";
	String eventid = "0";

	String caseduration = "0";
	String numres = "0";
	String enddate = "1970-1-1 10:00:00";

	if(eventidb)
	{
		eventid = eventindex.toString();
		System.out.println("eventid assigned");
	}

	if(task_indexb)
	{
		String sqlQuery4 = "SELECT count(*) FROM eventlog WHERE caseid='" +caseid+
				"' AND type='"+type+"' AND task='"+task+"' AND time<'"+time+"'"; 
		
		ResultSet rs4 = dbStatement.executeQuery(sqlQuery4);
		rs4.beforeFirst();
		
		rs4.next();
		if (rs4.getInt(1)>0) 
		{
			Integer count = rs4.getInt(1)+1;
			task_index=count.toString();
		}
		else
		{task_index="1";}
		System.out.println("task_index assigned");

	}

	if(durationb)
	{
		if(type.equalsIgnoreCase("complete"))
		{
			String sqlQuery3 = "SELECT time FROM eventlog WHERE caseid='" +caseid+"' AND type='start' AND task='"+task+"' AND time<='"+time+"'"; 
	     	 
			
			//assumption - the first suitable start event
	/*		ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
			if(rs3.next())
			{rs3.beforeFirst();
			
			rs3.next();
			if (!(rs3.getTimestamp(1)==null)) 
			{
				Timestamp startTime = rs3.getTimestamp(1);
				Long task_duration = time.getTime()-startTime.getTime();
				duration = task_duration.toString();
			}else
			{duration="0";}
			}else {duration = "0";}
   */			
			
			//assumption - the latest suitable start event
			ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
			if(rs3.next())
			{rs3.beforeFirst();}
			
			Vector<Timestamp> startTime = new Vector<Timestamp>();
			
			while (rs3.next()) 
			{
				if (!(rs3.getTimestamp(1)==null)) 
					startTime.add(rs3.getTimestamp(1));
			}
			
			if(startTime.size() == 0)
			{
				duration="0";
			}
			else
			{
				Timestamp latesttime = null;
				
				for(int i=0; i<startTime.size(); i++)
				{
					if(latesttime == null)
						{latesttime = startTime.elementAt(i);}
					else
					{
						if(latesttime.before(startTime.elementAt(i)))
							latesttime = startTime.elementAt(i);	
					}
				}
				
				Long task_duration = time.getTime()-latesttime.getTime();
				duration = task_duration.toString();
			}
			
		}
		else
		{duration = "0";}
		System.out.println("duration assigned");

	}


	if(workloadb)
	{
		//before the event time
		String sqlQuery1 = "SELECT (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" +
				" AND (type='start' OR type='resume')) - (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" +
				" AND (type='complete' OR type='suspend'))";
								     	 
		ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
		rs1.beforeFirst();
			if(rs1.next())
			{
				Integer wl = rs1.getInt(1);
				if(wl>0)
				{workload = wl.toString();}else
				{workload = "0";}
			}
		
			System.out.println("workload assigned");

	}


	if(workload_durationb)
	{
		String sqlQuery2 = "SELECT max(time) FROM eventlog WHERE resource='" +resource+"' AND time<'"+time+"'"; 
    	 
		ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
		if(rs2.next())
		{rs2.beforeFirst();
		
		rs2.next();
		if (!(rs2.getTimestamp(1)==null)) 
		{
			Timestamp prevTime = rs2.getTimestamp(1);
			Long wl_duration = time.getTime()-prevTime.getTime();
			workload_duration = wl_duration.toString();
		}else
		{workload_duration="0";}}else
		{workload_duration="0";}
		System.out.println("workload dur assigned");

	}


	if(number_of_resourcesb)
	{
		String sqlQuery7 = "SELECT count(distinct resource) FROM eventlog WHERE caseid='" +caseid+"'"; 
    	 
		ResultSet rs7 = dbStatement.executeQuery(sqlQuery7);
		rs7.beforeFirst();
		if(rs7.next())
			{
				 Integer res_num = rs7.getInt(1);
				 numres=res_num.toString();
			}
		System.out.println("numres assigned");

	}
	String sqlQuery8 = "SELECT min(time) FROM eventlog WHERE caseid='" +caseid+"'"; 
	 
	ResultSet rs8 = dbStatement.executeQuery(sqlQuery8);
	rs8.beforeFirst();
	rs8.next();
	
	Timestamp firsttime = rs8.getTimestamp(1);
			
	String sqlQuery9 = "SELECT max(time) FROM eventlog WHERE caseid='" +caseid+"'"; 
 	 
	ResultSet rs9 = dbStatement.executeQuery(sqlQuery9);
	rs9.beforeFirst();
	rs9.next();
	
	Timestamp lasttime = rs9.getTimestamp(1);
	System.out.println("lasttime: "+lasttime.toString());

	
	if(casedurationb)
	{
		Long case_long_duration = lasttime.getTime()-firsttime.getTime();
		caseduration = case_long_duration.toString();
		System.out.println("casedur assigned");

	}

	if(enddateb)
	{
		enddate = lasttime.toString();
		System.out.println("enddate assigned");

	}

	
	edbline = "update eventlog set eventid='"+eventid+"', task_index='"+task_index+"', duration='"+duration+"', workload='"+workload+"', workload_duration='"+workload_duration+"' where caseid='"+caseid+"' and task='"+task+"' and time='"+time+"' and type='" + type +"' and resource='"+resource+"'";
	equeries.add(edbline);
	
	cdbline = "update caselog set number_of_resources='"+numres+"', duration='"+caseduration+"', enddate='"+enddate+"' where caseid='"+caseid+"'";
	cqueries.add(cdbline);

System.out.println(edbline);
System.out.println(cdbline);
	
}

dbStatementFirst.close();


Statement statement = con.createStatement();

for (String equery : equeries) {
	statement.addBatch(equery);
}

for (String cquery : cqueries) {
	statement.addBatch(cquery);
}

System.out.println("batches added");

statement.executeBatch();
statement.close();


System.out.println("batch executed");

}


public void addAttributesV2(XLog log, Connection con, ELDBmapping map) throws Exception
{

	Statement dbStatementFirst = con.createStatement();
	String sqlQuery = "SELECT * FROM eventlog";
	ResultSet rs = dbStatementFirst.executeQuery(sqlQuery);

String edbline = "";
Vector <String> equeries = new Vector<String>();
String cdbline = "";
Vector <String> cqueries = new Vector<String>();

Integer eventindex = 0; 

Boolean eventidb = false;
Boolean task_indexb = false;
Boolean durationb = false;
Boolean workloadb = false;
Boolean workload_durationb = false;

Boolean number_of_resourcesb = false;
Boolean casedurationb = false;
Boolean enddateb = false;

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("eventid"))
	{
		eventidb = true;
		//System.out.println("eventid true");
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("task_index"))
	{
		task_indexb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("duration"))
	{
		durationb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("workload"))
	{
		workloadb = true;
	}
	if(map.ELAttributes.elementAt(i).equals("CALCULATE") && map.DBELAttributes.elementAt(i).equals("workload_duration"))
	{
		workload_durationb = true;
	}
	
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("number_of_resources"))
	{
		number_of_resourcesb = true;
	}
	
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("duration"))
	{
		casedurationb = true;
	}
	
	if(map.CLAttributes.elementAt(i).equals("CALCULATE") && map.DBCLAttributes.elementAt(i).equals("enddate"))
	{
		enddateb = true;
	}
}

Vector<Vector<String>> eventlog = new Vector<Vector<String>>();

while(rs.next()) {
	
	Vector<String> event = new Vector<String>();
	String caseid = rs.getString("caseid");
	String task = rs.getString("task");
	String type = rs.getString("type"); 
	Timestamp time = rs.getTimestamp("time");
	String resource = rs.getString("resource");
	
	event.add(caseid);
	event.add(task);
	event.add(type);
	event.add(time.toString());
	event.add(resource);
	
	eventlog.add(event);
	}

for(Vector<String> event : eventlog)
{
	String caseid = event.elementAt(0);
	String task = event.elementAt(1);
	String type = event.elementAt(2);
	Timestamp time = Timestamp.valueOf(event.elementAt(3));
	String resource = event.elementAt(4);

	eventindex++;
	
	String task_index = "0";
	String duration = "0";
	String workload = "0";
	String workload_duration = "0";
	String eventid = "0";

	String caseduration = "0";
	String numres = "0";
	String enddate = "1970-1-1 10:00:00";

	if(eventidb)
	{
		eventid = eventindex.toString();
		//System.out.println("eventid assigned");
	}
	
	Integer count = 0;
	if(task_indexb)
	{
		//String sqlQuery4 = "SELECT count(*) FROM eventlog WHERE caseid='" +caseid+
		//		"' AND type='"+type+"' AND task='"+task+"' AND time<'"+time+"'"; 
		
		for(int i=0; i<eventlog.size(); i++)
			{
			Vector<String> e = eventlog.elementAt(i);
			
			if(e.elementAt(0).equals(caseid) && e.elementAt(1).equals(task) && e.elementAt(2).equals(type) && Timestamp.valueOf(e.elementAt(3)).before(time))
			count++;
			}
			
		count++;
		task_index=count.toString();
		
		//System.out.println("task_index assigned");

	}

	if(durationb)
	{
		if(type.equalsIgnoreCase("complete"))
		{
			//String sqlQuery3 = "SELECT time FROM eventlog WHERE caseid='" +caseid+"' AND type='start' AND task='"+task+"' AND time<'"+time+"'"; 
			Timestamp startTime = null;
			
			for(int i=0; i<eventlog.size(); i++)
			{
				Vector<String> e = eventlog.elementAt(i);
			
				if(e.elementAt(0).equals(caseid) && e.elementAt(1).equals(task) && e.elementAt(2).equals("start") && (Timestamp.valueOf(e.elementAt(3)).before(time) || (Timestamp.valueOf(e.elementAt(3)) == time)))
				{
					if(startTime == null) 
					{
						startTime = Timestamp.valueOf(e.elementAt(3));
					}
					else
					{
						if(startTime.before(Timestamp.valueOf(e.elementAt(3)))) {startTime = Timestamp.valueOf(e.elementAt(3));}
					}
				}
			}

			if (startTime != null) 
			{
				
				Long task_duration = time.getTime()-startTime.getTime();
				duration = task_duration.toString();
			}else
			{duration="0";}
		
		}else {duration = "0";}}
		else
		{duration = "0";}
		//System.out.println("duration assigned");

	Integer count1 = 0;
	Integer count2 = 0;
	if(workloadb)
	{
		//String sqlQuery1 = "SELECT (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" +
		//		" AND (type='start' OR type='resume')) - (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" +
		//		" AND (type='complete' OR type='suspend'))";
								     	 
		for(int i=0; i<eventlog.size(); i++)
		{
			Vector<String> e = eventlog.elementAt(i);
		
			if(e.elementAt(4).equals(resource) && (e.elementAt(2).equals("start") || e.elementAt(2).equals("resume")) && Timestamp.valueOf(e.elementAt(3)).before(time))
			count2++;
			
			if(e.elementAt(4).equals(resource) && (e.elementAt(2).equals("complete") || e.elementAt(2).equals("suspend")) && Timestamp.valueOf(e.elementAt(3)).before(time))
			count1++;
		}

				Integer wl = count2- count1;
				if(wl>0)
				{workload = wl.toString();}else
				{workload = "0";}
		
			//System.out.println("workload assigned");

	}


	if(workload_durationb)
	{
		//String sqlQuery2 = "SELECT max(time) FROM eventlog WHERE resource='" +resource+"' AND time<'"+time+"'"; 
		Timestamp prevTime = null;
		Timestamp curTime = null;
		
		for(int i=0; i<eventlog.size(); i++)
		{
			Vector<String> e = eventlog.elementAt(i);
		
			if(e.elementAt(4).equals(resource) && Timestamp.valueOf(e.elementAt(3)).before(time))
			curTime = Timestamp.valueOf(e.elementAt(3));
			
			if(prevTime == null || prevTime.before(curTime))
			prevTime = curTime;
		}
   	 
		if (prevTime != null) 
		{
			
			Long wl_duration = time.getTime()-prevTime.getTime();
			workload_duration = wl_duration.toString();
		}else
		{workload_duration="0";}}else
		{workload_duration="0";}
		//System.out.println("workload dur assigned");

	Set<String> resources = new HashSet<String>();
	if(number_of_resourcesb)
	{
		//String sqlQuery7 = "SELECT count(distinct resource) FROM eventlog WHERE caseid='" +caseid+"'"; 
		for(int i=0; i<eventlog.size(); i++)
		{
			Vector<String> e = eventlog.elementAt(i);
			
			if(e.elementAt(0).equals(caseid))
			resources.add(e.elementAt(4));
		}
  
				 Integer res_num = resources.size();
				 numres=res_num.toString();
			
		//System.out.println("numres assigned: " + numres);

	}
	//String sqlQuery8 = "SELECT min(time) FROM eventlog WHERE caseid='" +caseid+"'"; 
	//String sqlQuery9 = "SELECT max(time) FROM eventlog WHERE caseid='" +caseid+"'"; 
	
	Timestamp firsttime = null;
	Timestamp lasttime = null;
	Timestamp curTime = null;
	
	for(int i=0; i<eventlog.size(); i++)
	{
		Vector<String> e = eventlog.elementAt(i);
	
		if(e.elementAt(0).equals(caseid))
		curTime = Timestamp.valueOf(e.elementAt(3));
		
		if(firsttime == null || firsttime.after(curTime))
		firsttime = curTime;
		
		if(lasttime == null || lasttime.before(curTime))
		lasttime = curTime;
	}
	
	
	if(casedurationb)
	{
		Long case_long_duration = lasttime.getTime()-firsttime.getTime();
		
		caseduration = case_long_duration.toString();
		//System.out.println("casedur assigned");

	}

	if(enddateb)
	{
		enddate = lasttime.toString();
		//System.out.println("enddate assigned");

	}

	
	edbline = "update eventlog set eventid='"+eventid+"', task_index='"+task_index+"', duration='"+duration+"', workload='"+workload+"', workload_duration='"+workload_duration+"' where caseid='"+caseid+"' and task='"+task+"' and time='"+time+"' and type='" + type +"' and resource='"+resource+"'";
	equeries.add(edbline);
	
	cdbline = "update caselog set number_of_resources='"+numres+"', duration='"+caseduration+"', enddate='"+enddate+"' where caseid='"+caseid+"'";
	cqueries.add(cdbline);

//System.out.println(edbline);
//System.out.println(cdbline);
	
}

dbStatementFirst.close();


Statement statement = con.createStatement();

for (String equery : equeries) {
	statement.addBatch(equery);
}

for (String cquery : cqueries) {
	statement.addBatch(cquery);
}

System.out.println("additional attributes created");

statement.executeBatch();
statement.close();


System.out.println("batch executed");

}

public void logToDBV2(XLog log, Connection con, ELDBmapping map) throws Exception
{

Vector <String> cadb = new Vector <String>(); 
Vector <String> cael = new Vector <String>(); 
Vector <String> eadb = new Vector <String>(); 
Vector <String> eael = new Vector <String>(); 

Vector <String> caseDBLines = new Vector <String>(); 
caseDBLines.add("caseid");
caseDBLines.add("number_of_resources");
caseDBLines.add("duration");
caseDBLines.add("enddate");

Vector <String> eventDBLines = new Vector <String>(); 
eventDBLines.add("caseid");
eventDBLines.add("task");
eventDBLines.add("type");
eventDBLines.add("time");
eventDBLines.add("resource");
eventDBLines.add("eventid");
eventDBLines.add("task_index");
eventDBLines.add("duration");
eventDBLines.add("workload");
eventDBLines.add("workload_duration");

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(!map.ELAttributes.elementAt(i).equals("NONE") && !map.ELAttributes.elementAt(i).equals("CALCULATE") && !map.DBELAttributes.elementAt(i).equals("NONE"))
	{
		eael.add(map.ELAttributes.elementAt(i));
		eadb.add(map.DBELAttributes.elementAt(i));
	}
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(!map.CLAttributes.elementAt(i).equals("NONE") && !map.CLAttributes.elementAt(i).equals("CALCULATE") && !map.DBCLAttributes.elementAt(i).equals("NONE"))
	{
		cael.add(map.CLAttributes.elementAt(i));
		cadb.add(map.DBCLAttributes.elementAt(i));
	}
}


String casedbnames = "caseid,number_of_resources,duration,enddate,";
for(int i=0; i<cadb.size(); i++)
{
	String ca = cadb.elementAt(i);
	if(!ca.equals("caseid") && !ca.equals("number_of_resources") && !ca.equals("duration") && !ca.equals("enddate"))
	{casedbnames+=ca +",";
	caseDBLines.add(ca);
	}
}
casedbnames = casedbnames.substring(0, casedbnames.length()-1);

String eventdbnames = "caseid,task,type,time,resource,eventid,task_index,duration,workload,workload_duration,";
for(int i=0; i<eadb.size(); i++)
{
	String ea = eadb.elementAt(i);
	if(!ea.equals("caseid") && !ea.equals("task") && !ea.equals("type") && !ea.equals("time") && !ea.equals("resource") && !ea.equals("eventid") && !ea.equals("task_index") && !ea.equals("duration") && !ea.equals("workload") && !ea.equals("workload_duration"))
	{eventdbnames+= ea+",";
	eventDBLines.add(ea);}
}
eventdbnames = eventdbnames.substring(0, eventdbnames.length()-1);

Vector<Vector<String>> caselog = new Vector<Vector<String>>(); 
Vector<Vector<String>> eventlog = new Vector<Vector<String>>(); 

for (XTrace t : log) 
{
	XAttributeLiteral caseidattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(0));
	String caseid = caseidattr.getValue();
	Vector<String> caseLine = new Vector<String>(); 
	
	for(int i=0; i<cael.size(); i++)
	{
		XAttributeLiteral nextcaseattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(i));
		String next = nextcaseattr.getValue();
		caseLine.add(next); 
	}
	caselog.add(caseLine); 
	
	
	for (XEvent e : t) 
	{
		Vector<String> event = new Vector<String>(); 
		event.add(caseid); 
		
		for(int i=0; i<eael.size(); i++)
		{
			if(eadb.elementAt(i).equals("time"))
			{
				XAttributeTimestamp nexteventattr = (XAttributeTimestamp)e.getAttributes().get(eael.elementAt(i));
				Date nexte = nexteventattr.getValue();
				Long nexttime = nexte.getTime();
				
				event.add(nexttime.toString()); 
				
			}
			else
			{
			XAttributeLiteral nexteventattr = (XAttributeLiteral)e.getAttributes().get(eael.elementAt(i));
			String nexte;
			if(!(nexteventattr == null))
			{nexte = nexteventattr.getValue();}else{nexte = "NULL";}
			event.add(nexte); 
			}
			
		}
		
		eventlog.add(event);

	}
	
}

eadb.add(0, "caseid");
System.out.println("cadb" + cadb);
System.out.println("eadb" + eadb);
System.out.println("caseDBLines" + caseDBLines);
System.out.println("eventDBLines" + eventDBLines);
System.out.println("caselog" + caselog);
System.out.println("eventlog" + eventlog);

String cdbline = "";
String edbline = "";
Vector <String> cqueries = new Vector<String>();
Vector <String> equeries = new Vector<String>();
String eventlogdata = "";
String caselogdata = "";

Map<String,Boolean> caAdd = new HashMap<String,Boolean>();
Map<String,Boolean> eaAdd = new HashMap<String,Boolean>();

for(int i=0; i<caseDBLines.size(); i++)
{
	String nextCA = caseDBLines.elementAt(i);
		
	if(cadb.contains(nextCA))
		caAdd.put(nextCA,false);
	else
		caAdd.put(nextCA,true);
}

for(int i=0; i<eventDBLines.size(); i++)
{
	String nextEA = eventDBLines.elementAt(i);
		
	if(eadb.contains(nextEA))
		eaAdd.put(nextEA,false);
	else
		eaAdd.put(nextEA,true);
}

System.out.println("Starting calculating case attributes: "+System.nanoTime());
//add case attributes
for(int i=0; i<caselog.size(); i++)
{	
	caselogdata = "";
	boolean caseFound = false;
	int caseStart = 0;
	
	Vector<String> caseline = caselog.elementAt(i);
	String caseid = caseline.elementAt(0);
	String numres = "0";
	String caseduration = "0";
	String enddate = "NULL";
	
	//calculate extra case attributes
	if(caAdd.get("number_of_resources") || caAdd.get("duration") || caAdd.get("enddate"))
	{
	
	Set<String> resources = new HashSet<String>();
	Timestamp firsttime = null;
	Timestamp lasttime = null;
	Timestamp curTime = null;
	
	for(int k=caseStart; k<eventlog.size(); k++)
	{
			Vector<String> e = eventlog.elementAt(k);
			
			if(e.elementAt(0).equals(caseid) && !caseFound)
			{
				caseFound = true;
				caseStart = i;
			}else
			{
				if(!e.elementAt(0).equals(caseid) && caseFound) 
				{
					caseStart = i;
					caseFound = false;
					break;
				}
			}	
				
			if(e.elementAt(0).equals(caseid))
			{
				resources.add(e.elementAt(4));
				curTime = new Timestamp(Long.valueOf(e.elementAt(3)));
			
				if(firsttime == null || firsttime.after(curTime))
					firsttime = curTime;
					
				if(lasttime == null || lasttime.before(curTime))
					lasttime = curTime;
			
			}
			
		}
	  
	Integer res_num = resources.size();
	numres=res_num.toString();
	
	Long case_long_duration = lasttime.getTime()-firsttime.getTime();
	caseduration = case_long_duration.toString();

	enddate = lasttime.toString();
	}
	
	if(caAdd.get("number_of_resources"))
	caseline.add(1, numres);
	
	if(caAdd.get("duration"))
	caseline.add(2, caseduration); 
	
	if(caAdd.get("enddate"))
	caseline.add(3, enddate); 	
	
	for(int j=0; j<caseline.size(); j++)
	caselogdata+= "'"+caseline.elementAt(j)+"',";
	
	caselogdata = caselogdata.substring(0, caselogdata.length()-1);
	cdbline = "insert into caselog("+casedbnames+") values("+caselogdata+")";	
	cqueries.add(cdbline);
}

//Save case attributes -----------------------------------------------------------------------------------

System.out.println("Case attributes calculated: "+System.nanoTime());
Statement cstatement = con.createStatement();

for (String cquery : cqueries) {
  cstatement.addBatch(cquery);
}
cstatement.executeBatch();
cstatement.close();

System.out.println("Case attributes added: "+System.nanoTime());


//add event attributes
String task_index = "0";
String duration = "0";
String workload = "0";
String workload_duration = "0";
String eventid = "0";

if(eaAdd.get("eventid") || eaAdd.get("task_index") || eaAdd.get("duration") || eaAdd.get("workload") || eaAdd.get("workload_duration"))
{

Integer eventindex = 0;

for(int i=0; i<eventlog.size(); i++)
{	
	eventlogdata = "";
	
			Vector<String> event = eventlog.elementAt(i);
			
			String caseid = event.elementAt(0);
			String task = event.elementAt(1);
			String type = event.elementAt(2);
			Long time = Long.valueOf(event.elementAt(3));
			String resource = event.elementAt(4);

			//eventindex-------------------------------------
			eventindex++;
			eventid = eventindex.toString();
			
			Integer count1 = 0;
			Integer count2 = 0;
			Long prevTime = null;
			Long curTime = null;
			Integer count = 0;
			Long startTime = null;
									     	 
			for(int k=0; k<eventlog.size(); k++)
			{
				Vector<String> e = eventlog.elementAt(k);
					
				//task_index	
				if(e.elementAt(0).equals(caseid) && e.elementAt(1).equals(task) && e.elementAt(2).equals(type) && Long.valueOf(e.elementAt(3))<time)
				count++;
					
				//duration
				if(type.equalsIgnoreCase("complete"))
				{
					//assumption - the closest 'start' event
					if(e.elementAt(0).equals(caseid) && e.elementAt(1).equals(task) && e.elementAt(2).equals("start") && (Long.valueOf(e.elementAt(3))<=time))
						{
							if(startTime == null) 
							{
								startTime = Long.valueOf(e.elementAt(3));
							}
							else
							{
								if(startTime<Long.valueOf(e.elementAt(3))) {startTime = Long.valueOf(e.elementAt(3));}
							}
						}
				}else {duration = "0";}
		
					
				//workload
				if(e.elementAt(4).equals(resource) && (e.elementAt(2).equals("start") || e.elementAt(2).equals("resume")) && Long.valueOf(e.elementAt(3))<time)
				count2++;
					
				if(e.elementAt(4).equals(resource) && (e.elementAt(2).equals("complete") || e.elementAt(2).equals("suspend")) && Long.valueOf(e.elementAt(3))<time)
				count1++;
				
				//workload_duration
				if(e.elementAt(4).equals(resource) && Long.valueOf(e.elementAt(3))<time)
				curTime = Long.valueOf(e.elementAt(3));
						
				if(prevTime == null || prevTime < curTime)
					prevTime = curTime;
			
				}

						Integer wl = count2 - count1;
						
						if(wl>0)
						{workload = wl.toString();}else
						{workload = "0";}
						
						if (prevTime != null) 
						{
							Long wl_duration = time-prevTime;
							workload_duration = wl_duration.toString();
						}else
						{workload_duration="0";}
						
						count++;
						task_index=count.toString();
						
						if (startTime != null) 
						{
							Long task_duration = time-startTime;
							duration = task_duration.toString();
						}else
						{duration="0";}

				//eventid
				if(eaAdd.get("eventid"))
				event.add(5, eventid); 
					
				//task_index
				if(eaAdd.get("task_index"))
				event.add(6, task_index); 
				
				//duration
				if(eaAdd.get("duration"))
				event.add(7, duration); 
				
				//workload
				if(eaAdd.get("workload"))
				event.add(8, workload); 
					
				//workload_duration
				if(eaAdd.get("workload_duration"))
				event.add(9, workload_duration);
				
				for(int j=0; j<3; j++)
					eventlogdata+= "'"+event.elementAt(j)+"',";
				
				Timestamp event_time = new Timestamp(Long.valueOf(event.elementAt(3)));
				String etime = event_time.toString();
				eventlogdata+= "'"+etime+"',";
					
				for(int j=4; j<event.size(); j++)
				eventlogdata+= "'"+event.elementAt(j)+"',";
				
				eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
				edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
				equeries.add(edbline);
		
}

	
	
}


//Add event attributes -----------------------------------------------------------------------------------
System.out.println("Event attributes calculated: "+System.nanoTime());
Statement estatement = con.createStatement();

for (String equery : equeries) {
    estatement.addBatch(equery);
}

estatement.executeBatch();
estatement.close();
System.out.println("Event attributes added: "+System.nanoTime());


}

//pre-processing in memory - same result as using DB 
public void logToDBV4(XLog log, Connection con, ELDBmapping map) throws Exception
{

Vector <String> cadb = new Vector <String>(); 
Vector <String> cael = new Vector <String>(); 
Vector <String> eadb = new Vector <String>(); 
Vector <String> eael = new Vector <String>(); 

Vector <String> caseDBLines = new Vector <String>(); 
caseDBLines.add("caseid");
caseDBLines.add("number_of_resources");
caseDBLines.add("duration");
caseDBLines.add("enddate");

Vector <String> eventDBLines = new Vector <String>(); 
eventDBLines.add("caseid");
eventDBLines.add("task");
eventDBLines.add("type");
eventDBLines.add("time");
eventDBLines.add("resource");
eventDBLines.add("eventid");
eventDBLines.add("task_index");
eventDBLines.add("duration");
eventDBLines.add("workload");
eventDBLines.add("workload_duration");

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(!map.ELAttributes.elementAt(i).equals("NONE") && !map.ELAttributes.elementAt(i).equals("CALCULATE") && !map.DBELAttributes.elementAt(i).equals("NONE"))
	{
		eael.add(map.ELAttributes.elementAt(i));
		eadb.add(map.DBELAttributes.elementAt(i));
	}
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(!map.CLAttributes.elementAt(i).equals("NONE") && !map.CLAttributes.elementAt(i).equals("CALCULATE") && !map.DBCLAttributes.elementAt(i).equals("NONE"))
	{
		cael.add(map.CLAttributes.elementAt(i));
		cadb.add(map.DBCLAttributes.elementAt(i));
	}
}


String casedbnames = "caseid,number_of_resources,duration,enddate,";
for(int i=0; i<cadb.size(); i++)
{
	String ca = cadb.elementAt(i);
	if(!ca.equals("caseid") && !ca.equals("number_of_resources") && !ca.equals("duration") && !ca.equals("enddate"))
	{casedbnames+=ca +",";
	caseDBLines.add(ca);
	}
}
casedbnames = casedbnames.substring(0, casedbnames.length()-1);

String eventdbnames = "caseid,task,type,time,resource,eventid,task_index,duration,workload,workload_duration,";
for(int i=0; i<eadb.size(); i++)
{
	String ea = eadb.elementAt(i);
	if(!ea.equals("caseid") && !ea.equals("task") && !ea.equals("type") && !ea.equals("time") && !ea.equals("resource") && !ea.equals("eventid") && !ea.equals("task_index") && !ea.equals("duration") && !ea.equals("workload") && !ea.equals("workload_duration"))
	{eventdbnames+= ea+",";
	eventDBLines.add(ea);}
}
eventdbnames = eventdbnames.substring(0, eventdbnames.length()-1);

Vector<Vector<String>> caselog = new Vector<Vector<String>>(); 
Vector<Vector<String>> eventlog = new Vector<Vector<String>>(); 

for (XTrace t : log) 
{
	XAttributeLiteral caseidattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(0));
	String caseid = caseidattr.getValue();
	Vector<String> caseLine = new Vector<String>(); 
	
	for(int i=0; i<cael.size(); i++)
	{
		XAttributeLiteral nextcaseattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(i));
		String next = nextcaseattr.getValue();
		caseLine.add(next); 
	}
	caselog.add(caseLine); 
	
	
	for (XEvent e : t) 
	{
		Vector<String> event = new Vector<String>(); 
		event.add(caseid); 
		
		for(int i=0; i<eael.size(); i++)
		{
			if(eadb.elementAt(i).equals("time"))
			{
				XAttributeTimestamp nexteventattr = (XAttributeTimestamp)e.getAttributes().get(eael.elementAt(i));
				Date nexte = nexteventattr.getValue();
				Long nexttime = nexte.getTime();
				
				event.add(nexttime.toString()); 
				
			}
			else
			{
			XAttributeLiteral nexteventattr = (XAttributeLiteral)e.getAttributes().get(eael.elementAt(i));
			String nexte;
			if(!(nexteventattr == null))
			{nexte = nexteventattr.getValue();}else{nexte = "NULL";}
			event.add(nexte); 
			}
			
		}
		
		eventlog.add(event);

	}
	
}

eadb.add(0, "caseid");
System.out.println("cadb" + cadb);
System.out.println("eadb" + eadb);
System.out.println("caseDBLines" + caseDBLines);
System.out.println("eventDBLines" + eventDBLines);
System.out.println("caselog" + caselog);
System.out.println("eventlog" + eventlog);

String cdbline = "";
String edbline = "";
Vector <String> cqueries = new Vector<String>();
Vector <String> equeries = new Vector<String>();
String eventlogdata = "";
String caselogdata = "";

Map<String,Boolean> caAdd = new HashMap<String,Boolean>();
Map<String,Boolean> eaAdd = new HashMap<String,Boolean>();

for(int i=0; i<caseDBLines.size(); i++)
{
	String nextCA = caseDBLines.elementAt(i);
		
	if(cadb.contains(nextCA))
		caAdd.put(nextCA,false);
	else
		caAdd.put(nextCA,true);
}

for(int i=0; i<eventDBLines.size(); i++)
{
	String nextEA = eventDBLines.elementAt(i);
		
	if(eadb.contains(nextEA))
		eaAdd.put(nextEA,false);
	else
		eaAdd.put(nextEA,true);
}

System.out.println("Starting calculating case attributes: "+System.nanoTime());

//add case attributes
for(int i=0; i<caselog.size(); i++)
{	
	caselogdata = "";
	boolean caseFound = false;
	int caseStart = 0;
	
	Vector<String> caseline = caselog.elementAt(i);
	String caseid = caseline.elementAt(0);
	String numres = "0";
	String caseduration = "0";
	String enddate = "NULL";
	
	//calculate extra case attributes
	if(caAdd.get("number_of_resources") || caAdd.get("duration") || caAdd.get("enddate"))
	{
	
	Set<String> resources = new HashSet<String>();
	Timestamp firsttime = null;
	Timestamp lasttime = null;
	Timestamp curTime = null;
	
	for(int k=caseStart; k<eventlog.size(); k++)
	{
			Vector<String> e = eventlog.elementAt(k);
			
			if(e.elementAt(0).equals(caseid) && !caseFound)
			{
				caseFound = true;
				caseStart = i;
			}else
			{
				if(!e.elementAt(0).equals(caseid) && caseFound) 
				{
					caseStart = i;
					caseFound = false;
					break;
				}
			}	
				
			if(e.elementAt(0).equals(caseid))
			{
				resources.add(e.elementAt(4));
				curTime = new Timestamp(Long.valueOf(e.elementAt(3)));
			
				if(firsttime == null || firsttime.after(curTime))
					firsttime = curTime;
					
				if(lasttime == null || lasttime.before(curTime))
					lasttime = curTime;
			
			}
			
		}
	  
	Integer res_num = resources.size();
	numres=res_num.toString();
	
	Long case_long_duration = lasttime.getTime()-firsttime.getTime();
	caseduration = case_long_duration.toString();

	enddate = lasttime.toString();
	}
	
	if(caAdd.get("number_of_resources"))
	caseline.add(1, numres);
	
	if(caAdd.get("duration"))
	caseline.add(2, caseduration); 
	
	if(caAdd.get("enddate"))
	caseline.add(3, enddate); 	
	
	for(int j=0; j<caseline.size(); j++)
	caselogdata+= "'"+caseline.elementAt(j)+"',";
	
	caselogdata = caselogdata.substring(0, caselogdata.length()-1);
	cdbline = "insert into caselog("+casedbnames+") values("+caselogdata+")";	
	cqueries.add(cdbline);
}

//Save case attributes -----------------------------------------------------------------------------------

System.out.println("Case attributes calculated: "+System.nanoTime());
Statement cstatement = con.createStatement();

for (String cquery : cqueries) {
cstatement.addBatch(cquery);
}
cstatement.executeBatch();
cstatement.close();

System.out.println("Case attributes added: "+System.nanoTime());
///////////////////////////////////////////////////////////////////////////////////////////////////////////

//add event attributes
String task_index = "0";
String duration = "0";
String workload = "0";
String workload_duration = "0";
String eventid = "0";


//calculating task index and duration

if(eaAdd.get("eventid") || eaAdd.get("task_index") || eaAdd.get("duration"))
{

Integer eventindex = 0;
String currentCase = null;
int caseStart = 0;

for(int i=0; i<eventlog.size(); i++)
{	
	eventlogdata = "";
	
			Vector<String> event = eventlog.elementAt(i);
			
			String caseid = event.elementAt(0);
			String task = event.elementAt(1);
			String type = event.elementAt(2);
			Long time = Long.valueOf(event.elementAt(3));
			
			if(i == 0)
				{
					currentCase = caseid;
				}
			else
				{
					if(!caseid.equals(currentCase))
					{
						currentCase = caseid;
						caseStart = i;
					}
				}
	
			//event ID -------------------------------------------------------
			eventindex++;
			eventid = eventindex.toString();
			
			Integer count = 0;
			Long startTime = null;
			
			for(int k=caseStart; k<eventlog.size(); k++)
			{
					Vector<String> e = eventlog.elementAt(k);
					String ecaseid = e.elementAt(0);
					String etask = e.elementAt(1);
					String etype = e.elementAt(2);
					Long etime = Long.valueOf(e.elementAt(3));
			
			if(!caseid.equals(ecaseid))
				break;
				
			//task_index -----------------------------------------------------
							
				if(etask.equalsIgnoreCase(task) && etype.equals(type) && etime<time)
				count++;
						
			//duration--------------------------------------------------------
					
				if(type.equalsIgnoreCase("complete"))
					{
						//assumption - the closest 'start' event
						if(etask.equals(task) && etype.equals("start") && etime<=time)
							{
								if(startTime == null) 
								{
									startTime = Long.valueOf(e.elementAt(3));
								}
								else
								{
									if(startTime<etime) {startTime = etime;}
								}
							}
					}else {duration = "0";}
			
				}
							
				//////////////////////////////////		
						count++;
						task_index=count.toString();
						
						if (startTime != null) 
						{
							Long task_duration = time-startTime;
							duration = task_duration.toString();
						}else
						{duration="0";}
						
				//eventid
				if(eaAdd.get("eventid"))
				event.add(5, eventid); 
					
				//task_index
				if(eaAdd.get("task_index"))
				event.add(6, task_index); 
				
				//duration
				if(eaAdd.get("duration"))
				event.add(7, duration); 
			
			//if workload is not needed
			if(!eaAdd.get("workload") && !eaAdd.get("workload_duration"))
			{	
				//workload
				if(eaAdd.get("workload"))
				event.add(8, "0"); 
					
				//workload_duration
				if(eaAdd.get("workload_duration"))
				event.add(9, "0");
				
				for(int j=0; j<3; j++)
					eventlogdata+= "'"+event.elementAt(j)+"',";
				
				Timestamp event_time = new Timestamp(Long.valueOf(event.elementAt(3)));
				String etime = event_time.toString();
				eventlogdata+= "'"+etime+"',";
					
				for(int j=4; j<event.size(); j++)
				eventlogdata+= "'"+event.elementAt(j)+"',";
				
				eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
				edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
				equeries.add(edbline);
			}
		
}


}


//calculating workload
if(eaAdd.get("workload") || eaAdd.get("workload_duration"))
{
	Map<String,Vector<Vector <String>>> resource_log = new HashMap<String,Vector<Vector <String>>>();
	
	//creating resource logs
	for(int i=0; i<eventlog.size(); i++)
	{	
		eventlogdata = "";
		
				Vector<String> event = eventlog.elementAt(i);
				String resource = event.elementAt(4);
				if(resource_log.get(resource) == null)
				{
					Vector<Vector <String>> resLog = new Vector<Vector <String>>();
					resLog.add(event);
					resource_log.put(resource, resLog);
					
				}
				else
				{
					resource_log.get(resource).add(event);
				}
	}
	
	
for(int i=0; i<eventlog.size(); i++)
{	
	eventlogdata = "";
	
			Vector<String> event = eventlog.elementAt(i);
			//String type = event.elementAt(2);
			Long time = Long.valueOf(event.elementAt(3));
			String resource = event.elementAt(4);
			
			Vector<Vector <String>> resLog = resource_log.get(resource);
			
			workload = "0";
			workload_duration = "0";
			
	if(resLog != null)
	{
		Long curMaxPrevTime = (long) 0;
		Long startedCounter = (long) 0;
		Long completedCounter = (long) 0;
		
		for(int j=0; j<resLog.size(); j++)
		{
			Vector<String> curEvent = resLog.elementAt(j);
			String curType = curEvent.elementAt(2);
			Long curTime = Long.valueOf(curEvent.elementAt(3));
			
			//workload
			//"SELECT (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" + " AND (type='start' OR type='resume')) 
			//- (SELECT COUNT(*) FROM eventlog WHERE time < '"+time+"' AND resource='" +resource+"'" + " AND (type='complete' OR type='suspend'))"

			if(curTime < time && (curType.equalsIgnoreCase("start") || curType.equalsIgnoreCase("resume")))
				startedCounter ++;
			
			if(curTime < time && (curType.equalsIgnoreCase("complete") || curType.equalsIgnoreCase("suspend")))
				completedCounter ++;
		
			//workload_duration
			//"SELECT max(time) FROM eventlog WHERE resource='" +resource+"' AND time<'"+time+"'" 
		
			if(curTime < time && curTime > curMaxPrevTime)
				curMaxPrevTime = curTime;
		}
		
		Long wl_dur = (long) 0;
		Long wl = (long) 0;
		
		if(curMaxPrevTime > 0)
			wl_dur = time - curMaxPrevTime;
		
		if(startedCounter > completedCounter)
			wl = startedCounter - completedCounter;
		
		workload = wl.toString();
		workload_duration = wl_dur.toString();

		
	}
	
				//workload
				if(eaAdd.get("workload"))
				event.add(8, workload); 
					
				//workload_duration
				if(eaAdd.get("workload_duration"))
				event.add(9, workload_duration);
				
				for(int j=0; j<3; j++)
					eventlogdata+= "'"+event.elementAt(j)+"',";
				
				Timestamp event_time = new Timestamp(Long.valueOf(event.elementAt(3)));
				String etime = event_time.toString();
				eventlogdata+= "'"+etime+"',";
					
				for(int j=4; j<event.size(); j++)
				eventlogdata+= "'"+event.elementAt(j)+"',";
				
				eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
				edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
				equeries.add(edbline);

}
	
}


//Add event attributes -----------------------------------------------------------------------------------
System.out.println("Event attributes calculated: "+System.nanoTime());
Statement estatement = con.createStatement();

for (String equery : equeries) {
  estatement.addBatch(equery);
}

estatement.executeBatch();
estatement.close();
System.out.println("Event attributes added: "+System.nanoTime());


}


//pre-processing in memory using sort - different approach to workload and workload_duration
public void logToDBV3(XLog log, Connection con, ELDBmapping map) throws Exception
{

Vector <String> cadb = new Vector <String>(); 
Vector <String> cael = new Vector <String>(); 
Vector <String> eadb = new Vector <String>(); 
Vector <String> eael = new Vector <String>(); 

Vector <String> caseDBLines = new Vector <String>(); 
caseDBLines.add("caseid");
caseDBLines.add("number_of_resources");
caseDBLines.add("duration");
caseDBLines.add("enddate");

Vector <String> eventDBLines = new Vector <String>(); 
eventDBLines.add("caseid");
eventDBLines.add("task");
eventDBLines.add("type");
eventDBLines.add("time");
eventDBLines.add("resource");
eventDBLines.add("eventid");
eventDBLines.add("task_index");
eventDBLines.add("duration");
eventDBLines.add("workload");
eventDBLines.add("workload_duration");

for(int i=0; i<map.ELAttributes.size(); i++)
{
	if(!map.ELAttributes.elementAt(i).equals("NONE") && !map.ELAttributes.elementAt(i).equals("CALCULATE") && !map.DBELAttributes.elementAt(i).equals("NONE"))
	{
		eael.add(map.ELAttributes.elementAt(i));
		eadb.add(map.DBELAttributes.elementAt(i));
	}
}

for(int i=0; i<map.CLAttributes.size(); i++)
{
	if(!map.CLAttributes.elementAt(i).equals("NONE") && !map.CLAttributes.elementAt(i).equals("CALCULATE") && !map.DBCLAttributes.elementAt(i).equals("NONE"))
	{
		cael.add(map.CLAttributes.elementAt(i));
		cadb.add(map.DBCLAttributes.elementAt(i));
	}
}


String casedbnames = "caseid,number_of_resources,duration,enddate,";
for(int i=0; i<cadb.size(); i++)
{
	String ca = cadb.elementAt(i);
	if(!ca.equals("caseid") && !ca.equals("number_of_resources") && !ca.equals("duration") && !ca.equals("enddate"))
	{casedbnames+=ca +",";
	caseDBLines.add(ca);
	}
}
casedbnames = casedbnames.substring(0, casedbnames.length()-1);

String eventdbnames = "caseid,task,type,time,resource,eventid,task_index,duration,workload,workload_duration,";
for(int i=0; i<eadb.size(); i++)
{
	String ea = eadb.elementAt(i);
	if(!ea.equals("caseid") && !ea.equals("task") && !ea.equals("type") && !ea.equals("time") && !ea.equals("resource") && !ea.equals("eventid") && !ea.equals("task_index") && !ea.equals("duration") && !ea.equals("workload") && !ea.equals("workload_duration"))
	{eventdbnames+= ea+",";
	eventDBLines.add(ea);}
}
eventdbnames = eventdbnames.substring(0, eventdbnames.length()-1);

Vector<Vector<String>> caselog = new Vector<Vector<String>>(); 
Vector<Vector<String>> eventlog = new Vector<Vector<String>>(); 

for (XTrace t : log) 
{
	XAttributeLiteral caseidattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(0));
	String caseid = caseidattr.getValue();
	Vector<String> caseLine = new Vector<String>(); 
	
	for(int i=0; i<cael.size(); i++)
	{
		XAttributeLiteral nextcaseattr = (XAttributeLiteral)t.getAttributes().get(cael.elementAt(i));
		String next = nextcaseattr.getValue();
		caseLine.add(next); 
	}
	caselog.add(caseLine); 
	
	
	for (XEvent e : t) 
	{
		Vector<String> event = new Vector<String>(); 
		event.add(caseid); 
		
		for(int i=0; i<eael.size(); i++)
		{
			if(eadb.elementAt(i).equals("time"))
			{
				XAttributeTimestamp nexteventattr = (XAttributeTimestamp)e.getAttributes().get(eael.elementAt(i));
				Date nexte = nexteventattr.getValue();
				Long nexttime = nexte.getTime();
				
				event.add(nexttime.toString()); 
				
			}
			else
			{
			XAttributeLiteral nexteventattr = (XAttributeLiteral)e.getAttributes().get(eael.elementAt(i));
			String nexte;
			if(!(nexteventattr == null))
			{nexte = nexteventattr.getValue();}else{nexte = "NULL";}
			event.add(nexte); 
			}
			
		}
		
		eventlog.add(event);

	}
	
}

eadb.add(0, "caseid");
System.out.println("cadb" + cadb);
System.out.println("eadb" + eadb);
System.out.println("caseDBLines" + caseDBLines);
System.out.println("eventDBLines" + eventDBLines);
System.out.println("caselog" + caselog);
System.out.println("eventlog" + eventlog);

String cdbline = "";
String edbline = "";
Vector <String> cqueries = new Vector<String>();
Vector <String> equeries = new Vector<String>();
String eventlogdata = "";
String caselogdata = "";

Map<String,Boolean> caAdd = new HashMap<String,Boolean>();
Map<String,Boolean> eaAdd = new HashMap<String,Boolean>();

for(int i=0; i<caseDBLines.size(); i++)
{
	String nextCA = caseDBLines.elementAt(i);
		
	if(cadb.contains(nextCA))
		caAdd.put(nextCA,false);
	else
		caAdd.put(nextCA,true);
}

for(int i=0; i<eventDBLines.size(); i++)
{
	String nextEA = eventDBLines.elementAt(i);
		
	if(eadb.contains(nextEA))
		eaAdd.put(nextEA,false);
	else
		eaAdd.put(nextEA,true);
}

System.out.println("Starting calculating case attributes: "+System.nanoTime());

//add case attributes
for(int i=0; i<caselog.size(); i++)
{	
	caselogdata = "";
	boolean caseFound = false;
	int caseStart = 0;
	
	Vector<String> caseline = caselog.elementAt(i);
	String caseid = caseline.elementAt(0);
	String numres = "0";
	String caseduration = "0";
	String enddate = "NULL";
	
	//calculate extra case attributes
	if(caAdd.get("number_of_resources") || caAdd.get("duration") || caAdd.get("enddate"))
	{
	
	Set<String> resources = new HashSet<String>();
	Timestamp firsttime = null;
	Timestamp lasttime = null;
	Timestamp curTime = null;
	
	for(int k=caseStart; k<eventlog.size(); k++)
	{
			Vector<String> e = eventlog.elementAt(k);
			
			if(e.elementAt(0).equals(caseid) && !caseFound)
			{
				caseFound = true;
				caseStart = i;
			}else
			{
				if(!e.elementAt(0).equals(caseid) && caseFound) 
				{
					caseStart = i;
					caseFound = false;
					break;
				}
			}	
				
			if(e.elementAt(0).equals(caseid))
			{
				resources.add(e.elementAt(4));
				curTime = new Timestamp(Long.valueOf(e.elementAt(3)));
			
				if(firsttime == null || firsttime.after(curTime))
					firsttime = curTime;
					
				if(lasttime == null || lasttime.before(curTime))
					lasttime = curTime;
			
			}
			
		}
	  
	Integer res_num = resources.size();
	numres=res_num.toString();
	
	Long case_long_duration = lasttime.getTime()-firsttime.getTime();
	caseduration = case_long_duration.toString();

	enddate = lasttime.toString();
	}
	
	if(caAdd.get("number_of_resources"))
	caseline.add(1, numres);
	
	if(caAdd.get("duration"))
	caseline.add(2, caseduration); 
	
	if(caAdd.get("enddate"))
	caseline.add(3, enddate); 	
	
	for(int j=0; j<caseline.size(); j++)
	caselogdata+= "'"+caseline.elementAt(j)+"',";
	
	caselogdata = caselogdata.substring(0, caselogdata.length()-1);
	cdbline = "insert into caselog("+casedbnames+") values("+caselogdata+")";	
	cqueries.add(cdbline);
}

//Save case attributes -----------------------------------------------------------------------------------

System.out.println("Case attributes calculated: "+System.nanoTime());
Statement cstatement = con.createStatement();

for (String cquery : cqueries) {
cstatement.addBatch(cquery);
}
cstatement.executeBatch();
cstatement.close();

System.out.println("Case attributes added: "+System.nanoTime());
///////////////////////////////////////////////////////////////////////////////////////////////////////////

//add event attributes
String task_index = "0";
String duration = "0";
String workload = "0";
String workload_duration = "0";
String eventid = "0";


//calculating task index and duration

if(eaAdd.get("eventid") || eaAdd.get("task_index") || eaAdd.get("duration"))
{

Integer eventindex = 0;
String currentCase = null;
int caseStart = 0;

for(int i=0; i<eventlog.size(); i++)
{	
	eventlogdata = "";
	
			Vector<String> event = eventlog.elementAt(i);
			
			String caseid = event.elementAt(0);
			String task = event.elementAt(1);
			String type = event.elementAt(2);
			Long time = Long.valueOf(event.elementAt(3));
			
			if(i == 0)
				{
					currentCase = caseid;
				}
			else
				{
					if(!caseid.equals(currentCase))
					{
						currentCase = caseid;
						caseStart = i;
					}
				}
	
			//event ID -------------------------------------------------------
			eventindex++;
			eventid = eventindex.toString();
			
			Integer count = 0;
			Long startTime = null;
			
			for(int k=caseStart; k<eventlog.size(); k++)
			{
					Vector<String> e = eventlog.elementAt(k);
					String ecaseid = e.elementAt(0);
					String etask = e.elementAt(1);
					String etype = e.elementAt(2);
					Long etime = Long.valueOf(e.elementAt(3));
			
			if(!caseid.equals(ecaseid))
				break;
				
			//task_index -----------------------------------------------------
							
				if(etask.equals(task) && etype.equals(type) && etime<time)
				count++;
						
			//duration--------------------------------------------------------
					
				if(type.equalsIgnoreCase("complete"))
					{
						//assumption - the closest 'start' event
						if(etask.equals(task) && etype.equals("start") && etime<=time)
							{
								if(startTime == null) 
								{
									startTime = Long.valueOf(e.elementAt(3));
								}
								else
								{
									if(startTime<etime) {startTime = etime;}
								}
							}
					}else {duration = "0";}
			
				}
							
				//////////////////////////////////		
						count++;
						task_index=count.toString();
						
						if (startTime != null) 
						{
							Long task_duration = time-startTime;
							duration = task_duration.toString();
						}else
						{duration="0";}
						
				//eventid
				if(eaAdd.get("eventid"))
				event.add(5, eventid); 
					
				//task_index
				if(eaAdd.get("task_index"))
				event.add(6, task_index); 
				
				//duration
				if(eaAdd.get("duration"))
				event.add(7, duration); 
			
			//if workload is not needed
			if(!eaAdd.get("workload") && !eaAdd.get("workload_duration"))
			{	
				//workload
				if(eaAdd.get("workload"))
				event.add(8, "0"); 
					
				//workload_duration
				if(eaAdd.get("workload_duration"))
				event.add(9, "0");
				
				for(int j=0; j<3; j++)
					eventlogdata+= "'"+event.elementAt(j)+"',";
				
				Timestamp event_time = new Timestamp(Long.valueOf(event.elementAt(3)));
				String etime = event_time.toString();
				eventlogdata+= "'"+etime+"',";
					
				for(int j=4; j<event.size(); j++)
				eventlogdata+= "'"+event.elementAt(j)+"',";
				
				eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
				edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
				equeries.add(edbline);
			}
		
}


}


//calculating workload
if(eaAdd.get("workload") || eaAdd.get("workload_duration"))
{
	
	//create a copy of eventLog
	Vector<Vector <String>> eventlogSorted = new Vector<Vector <String>>();
	
	eventlogSorted = sortLogJava(eventlog,eventlogSorted);
	//eventlogSorted = sortLogInsert(eventlog,eventlogSorted);
	//eventlogSorted = sortLogInsertMy(eventlog,eventlogSorted);
	//eventlogSorted = sortLogInsertMyCur(eventlog,eventlogSorted);
	
	Map<String,Long> resource_workload = new HashMap<String,Long>();
	Map<String,Long> resource_workload_time = new HashMap<String,Long>();

	//Set<String> prevResources = new HashSet<String>();
	//Long prevTime = null;
	
for(int i=0; i<eventlogSorted.size(); i++)
{	
	eventlogdata = "";
	
			Vector<String> event = eventlogSorted.elementAt(i);
			
			String type = event.elementAt(2);
			Long time = Long.valueOf(event.elementAt(3));
			String resource = event.elementAt(4);

			//workload
			if(resource_workload.get(resource) == null)
			{
				workload = "0";
				workload_duration = "0";
				resource_workload_time.put(resource,time); 
				
				if(type.equals("start") || type.equals("resume"))
				{
					resource_workload.put(resource, new Long(1));
				}
				else
				{
					resource_workload.put(resource,new Long(0));
				}
			}
			else
			{
				Long prevWL = resource_workload.get(resource);
				Long prevWLTime = resource_workload_time.get(resource);
				Long wl_dur = time - prevWLTime;
				workload_duration = wl_dur.toString();
				workload = prevWL.toString();
				
				
				//&& prevResources.contains(resource)
				//|| !prevResources.contains(resource)
				//if(time != prevTime && !prevResources.contains(resource))
				
				if(time != prevWLTime)
				{
					resource_workload_time.put(resource,time);
					
					if(type.equals("start") || type.equals("resume"))
					{
						resource_workload.put(resource,prevWL+1);
					}
					else
					{
						long wl = prevWL-1;
						
						if(wl>0)
							resource_workload.put(resource,wl);
						else
							resource_workload.put(resource,new Long(0));
					}
				
				}
				
				//if(prevWLTime != time) 
				//	resource_workload_time.put(resource,time);
			
				
			}

				//workload
				if(eaAdd.get("workload"))
				event.add(8, workload); 
					
				//workload_duration
				if(eaAdd.get("workload_duration"))
				event.add(9, workload_duration);
				
				for(int j=0; j<3; j++)
					eventlogdata+= "'"+event.elementAt(j)+"',";
				
				Timestamp event_time = new Timestamp(Long.valueOf(event.elementAt(3)));
				String etime = event_time.toString();
				eventlogdata+= "'"+etime+"',";
					
				for(int j=4; j<event.size(); j++)
				eventlogdata+= "'"+event.elementAt(j)+"',";
				
				eventlogdata = eventlogdata.substring(0, eventlogdata.length()-1);
				edbline = "insert into eventlog("+eventdbnames+") values("+eventlogdata+")";	
				equeries.add(edbline);

//update prev. resources & time values
/*if(prevTime != null & prevTime != time)			
{
	prevResources.clear();
	prevResources.add(resource);
}
else
	prevResources.add(resource);
	
prevTime = time;
*/
}

	
	
}


//Add event attributes -----------------------------------------------------------------------------------
System.out.println("Event attributes calculated: "+System.nanoTime());
Statement estatement = con.createStatement();

for (String equery : equeries) {
  estatement.addBatch(equery);
}

estatement.executeBatch();
estatement.close();
System.out.println("Event attributes added: "+System.nanoTime());


}


Vector<Vector <String>> sortLogJava(Vector<Vector <String>> eventlog, Vector<Vector <String>> eventlogSorted)
{
	eventlogSorted.addAll(eventlog);

	//sort event log
	long sstartTime = System.nanoTime();
	System.out.println("sorting started: "+ sstartTime);

	Collections.sort(eventlogSorted, new Comparator<Vector<String>>(){
	  @Override  public int compare(Vector<String> v1, Vector<String> v2) {
	  	
	    	if(Long.valueOf(v1.elementAt(3)) == Long.valueOf(v2.elementAt(3)))
	  		return 0;
	  	
	    	if(Long.valueOf(v1.elementAt(3)) == null)
				return -1;
	    	
	    	if(Long.valueOf(v2.elementAt(3)) == null)
				return 1;
		  	
	    	if(Long.valueOf(v1.elementAt(3)) < Long.valueOf(v2.elementAt(3)))
				return -1;
	    	else
	    		return 1;
	}});
	long sendTime = System.nanoTime();
	System.out.println("sorting ended: "+ sendTime);
	long sdur = sendTime-sstartTime;

	System.out.println("sorting duration: "+sdur);
	
	return eventlogSorted;
}


Vector<Vector <String>> sortLogInsert(Vector<Vector <String>> eventlog, Vector<Vector <String>> eventlogSorted)
{
	
	long sstartTime = System.nanoTime();
	System.out.println("sorting started: "+ sstartTime);
	
	////////////////////////////
	
	Vector <String> temp = new Vector<String>();
    for (int i = 1; i < eventlog.size(); i++) {
        for(int j = i ; j > 0 ; j--){
            if(Long.valueOf(eventlog.elementAt(j).elementAt(3)) < Long.valueOf(eventlog.elementAt(j-1).elementAt(3))){
                temp = eventlog.elementAt(j);
                eventlog.set(j, eventlog.elementAt(j-1));
                eventlog.set(j-1,temp);
            }
        }
    }
    

	///////////////////////////

	long sendTime = System.nanoTime();
	System.out.println("sorting ended: "+ sendTime);
	long sdur = sendTime-sstartTime;
	System.out.println("sorting duration: "+sdur);
	
	return eventlog;
}


Vector<Vector <String>> sortLogInsertMy(Vector<Vector <String>> eventlog, Vector<Vector <String>> eventlogSorted)
{
	
	long sstartTime = System.nanoTime();
	System.out.println("sorting started: "+ sstartTime);

	/////////////////////////////////////////////////////
	
	eventlogSorted.add(eventlog.elementAt(0));
	Vector <String> newItem = new Vector<String>();
	Vector <String> prev = new Vector<String>();
	
	for(int i=1; i<eventlog.size(); i++)
	{
		newItem = eventlog.elementAt(i);
			
		for(int j=eventlogSorted.size(); j > 0 ; j--)
		{
			prev = eventlogSorted.elementAt(j-1);
			
			if(Long.valueOf(prev.elementAt(3)) <= Long.valueOf(newItem.elementAt(3)))
			{
				eventlogSorted.add(j, newItem);
				break;
			}
				
		}

	}
	
	//////////////////////////////////////////////////////

	long sendTime = System.nanoTime();
	System.out.println("sorting ended: "+ sendTime);
	long sdur = sendTime-sstartTime;
	System.out.println("sorting duration: "+sdur);
	
	return eventlogSorted;
}


Vector<Vector <String>> sortLogInsertMyCur(Vector<Vector <String>> eventlog, Vector<Vector <String>> eventlogSorted)
{
	
	long sstartTime = System.nanoTime();
	System.out.println("sorting started: "+ sstartTime);
	
	////////////////////////////

	Vector <String> newItem = new Vector<String>();
	Vector <String> next = new Vector<String>();
	Vector <String> prev = new Vector<String>();
	Vector <String> current = new Vector<String>();
	String  compare = "";
	
	eventlogSorted.add(eventlog.elementAt(0));
	int currentPosition = 0;
	current = eventlogSorted.elementAt(currentPosition);
	
	for(int i=1; i<eventlog.size(); i++)
	{
		newItem = eventlog.elementAt(i);
		
		if(Long.valueOf(newItem.elementAt(3)) > Long.valueOf(current.elementAt(3)))
			compare = "bigger"; 
		if(Long.valueOf(newItem.elementAt(3)) == Long.valueOf(current.elementAt(3)))
			compare = "equal"; 
		if(Long.valueOf(newItem.elementAt(3)) < Long.valueOf(current.elementAt(3)))
			compare = "smaller"; 
		
		//current is last & newItem is bigger or equal than current
		if((compare.equals("bigger") || compare.equals("equal")) && eventlogSorted.size() == (currentPosition+1))
		{
			eventlogSorted.add(newItem);
			currentPosition = eventlogSorted.size()-1;
			current = eventlogSorted.elementAt(currentPosition);
			continue;
		}
		
		//newItem is equal to current
		if(compare.equals("equal"))
		{
			currentPosition++;
			eventlogSorted.add(currentPosition, newItem);
			current = eventlogSorted.elementAt(currentPosition);
			continue;
		}
	
		//newItem bigger than current
		if(compare.equals("bigger"))
		{
			for(int j=currentPosition+1; j<eventlogSorted.size(); j++)
			{
				next = eventlogSorted.elementAt(j);
				
				if(Long.valueOf(newItem.elementAt(3)) <= Long.valueOf(next.elementAt(3)))
				{
					eventlogSorted.add(j, newItem);
					currentPosition = j;
					current = eventlogSorted.elementAt(currentPosition);
					break;
				}
					
			}
			
			eventlogSorted.add(newItem);
			currentPosition = eventlogSorted.size()-1;
			current = eventlogSorted.elementAt(currentPosition);
	

		}
		
		//newItem is smaller than current
		if(compare.equals("smaller"))
		{
			for(int j=currentPosition-1; j >= 0 ; j--)
			{
				prev = eventlogSorted.elementAt(j);
				if(Long.valueOf(prev.elementAt(3)) <= Long.valueOf(newItem.elementAt(3)))
				{
					eventlogSorted.add(j+1, newItem);
					currentPosition = j+1;
					current = eventlogSorted.elementAt(currentPosition);
					break;
				}
					
			}
			
			eventlogSorted.add(0, newItem);
			currentPosition = 0;
			current = eventlogSorted.elementAt(currentPosition);
		

		}
		
			
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////

	long sendTime = System.nanoTime();
	System.out.println("sorting ended: "+ sendTime);
	long sdur = sendTime-sstartTime;
	System.out.println("sorting duration: "+sdur);
	
	return eventlogSorted;
}

}
