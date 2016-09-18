package org.processmining.plugins.miningresourceprofiles.analysis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Vector;

import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.rosuda.JRI.Rengine;


public class GetDEAProductivity{
	
	public String getOutMultResMultTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
		
		Vector<String> outts = new Vector<String>();
		String matrixts = "";
		
		String sqlQuery0 = "SELECT count(distinct resource) as c FROM deaoutts";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		int rescount = rs0.getInt("c");
		
		String sqlQuery = "SELECT ts_values FROM deaoutts";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			outts.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		
		int nrow = rescount*ip.numberOfSlots;
		int ncol = outts.size()/rescount;
		
		String outputs = "y <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
				
		
		return outputs;
	}
	
	public String getInMultResMultTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
		Vector<String> ints = new Vector<String>();
		String matrixts = "";
		
		String sqlQuery0 = "SELECT count(distinct resource) as c FROM deaints";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		int rescount = rs0.getInt("c");
		
		String sqlQuery = "SELECT ts_values FROM deaints";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			ints.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		
		int nrow = rescount*ip.numberOfSlots;
		int ncol = ints.size()/rescount;
		
		String inputts = "x <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
				
		
		return inputts;
	}
	
	public String getOutMultResOneTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
				
		Vector<String> outts = new Vector<String>();
		String matrixts = "";
			
		String sqlQuery0 = "SELECT count(distinct resource) as c FROM deaoutts";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		int rescount = rs0.getInt("c");
		
		String sqlQuery = "SELECT ts_values FROM deaoutts";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			outts.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		int nrow = rescount;
		int ncol = outts.size()/nrow;
		
		String outputts = "y <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
		
		return outputts;
	}
	
	public String getInMultResOneTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
				
		Vector<String> ints = new Vector<String>();
		String matrixts = "";
		
		String sqlQuery0 = "SELECT count(distinct resource) as c FROM deaints";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		int rescount = rs0.getInt("c");
		
		String sqlQuery = "SELECT ts_values FROM deaints";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			ints.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		
		int nrow = rescount;
		int ncol = ints.size()/nrow;
		
		String inputts = "x <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
				
		
		return inputts;
	}
	
	public String getOutOneResMultTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
				
		Vector<String> outts = new Vector<String>();
		String matrixts = "";
		
		String sqlQuery = "SELECT ts_values FROM deaoutts";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			outts.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		
		int nrow = ip.numberOfSlots;
		int ncol = outts.size();
		
		String inputts = "y <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
				
		
		return inputts;
	}
	
	public String getInOneResMultTime(InputParameters ip, Connection con) throws Exception
	{
		Statement dbStatement = con.createStatement();
				
		Vector<String> ints = new Vector<String>();
		String matrixts = "";
		
		String sqlQuery = "SELECT ts_values FROM deaints";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{
			String onets = rs.getString("ts_values");
			ints.add(onets);
			matrixts+=onets+",";
		}
		
		if(!matrixts.isEmpty())
		{matrixts = matrixts.substring(0, matrixts.length()-1);} 
		
		
		int nrow = ip.numberOfSlots;
		int ncol = ints.size();
		
		String inputts = "x <- matrix(c("+matrixts+"),nrow="+nrow+",ncol="+ncol+")";
				
		
		return inputts;
	}

	
	
public String getOutputCost (InputParameters ip, Connection con) throws Exception

{
	
	String costs = "p <- matrix(c(";	
	HashSet <String> idsvars = new HashSet<String>();
	Vector<Double> cost = new Vector<Double>();
	
	for (int i=0; i<ip.DEA_Outputs.size(); i++)
	{
		String curid = ip.DEA_Outputs.elementAt(i).inout+ip.DEA_Outputs.elementAt(i).varName.toString()+ip.DEA_Outputs.elementAt(i).varValue.toString();
		Double curcost = ip.DEA_Outputs.elementAt(i).cost;
		
		if(!idsvars.contains(curid))
		{
			idsvars.add(curid);
			cost.add(curcost);
		}
	}
	
	
	for (int i=0; i<cost.size(); i++)
	{
		costs += cost.elementAt(i).toString()+",";
	}
	
	if(!costs.isEmpty())
	{costs = costs.substring(0, costs.length()-1);} 
	
	costs+= "),nrow=1,ncol=" +cost.size()+")";

	return costs;
		
}



public String getInputCost (InputParameters ip, Connection con) throws Exception

{

String costs = "w <- matrix(c(";	
HashSet <String> idsvars = new HashSet<String>();
Vector<Double> cost = new Vector<Double>();

for (int i=0; i<ip.DEA_Inputs.size(); i++)
{
	String curid = ip.DEA_Inputs.elementAt(i).inout+ip.DEA_Inputs.elementAt(i).varName.toString()+ip.DEA_Inputs.elementAt(i).varValue.toString();
	Double curcost = ip.DEA_Inputs.elementAt(i).cost;
	
	if(!idsvars.contains(curid))
	{
		idsvars.add(curid);
		cost.add(curcost);
	}
}


for (int i=0; i<cost.size(); i++)
{
	costs += cost.elementAt(i).toString()+",";
}

if(!costs.isEmpty())
{costs = costs.substring(0, costs.length()-1);} 

costs+= "),nrow=1,ncol=" +cost.size()+")";

return costs;
	
}

	
public String getDEA(InputParameters ip, Connection con, Rengine re, String in, String out) throws Exception
	{
		
		String deascores = "";
		re.eval("library(Benchmarking)");
		re.eval(in);
		re.eval(out);
	
		String RTS = ip.RTS; 
		String orientation = "in";//"out" 
		
		re.eval("res <- dea(x,y, RTS = \"" + RTS +"\", ORIENTATION = \"" + orientation +"\")");
		
		double[] scores = re.eval("res$eff").asDoubleArray();
		
		for(int i=0;i<scores.length;i++)
		{
			double curscore = scores[i];
			deascores+=curscore+",";
		}
		
		if (scores.length>0) {deascores = deascores.substring(0, deascores.length()-1); }	
		
		System.out.println("DEA: " + deascores);
		
		return deascores;
	}
	
	
	public String getRevenueDEA(InputParameters ip, Connection con, Rengine re, String in, String out, String outcost) throws Exception
	{
		
		String deascores = "";
		re.eval("library(Benchmarking)");
		re.eval(in);
		re.eval(out);
		re.eval(outcost);
	
	
		String RTS = ip.RTS; 
		
		re.eval("yopt <- revenue.opt(x,y,p,RTS = \"" + RTS +"\")");
		re.eval("robs <- y %*% t(p)");
		re.eval("ropt <- yopt$y %*% t(p)");
		re.eval("re <- robs/ropt");
		
		double[] scores = re.eval("re").asDoubleArray();
		
		for(int i=0;i<scores.length;i++)
		{
		deascores+=scores[i]+",";
		}
		
		if (scores.length>0) {deascores = deascores.substring(0, deascores.length()-1); }	
		
		System.out.println("revenue dea: " + deascores);
		
		return deascores;
	}
	

	public String getCostDEA(InputParameters ip, Connection con, Rengine re, String in, String out, String incost) throws Exception
	{
		
		String deascores = "";
		re.eval("library(Benchmarking)");
		re.eval(in);
		re.eval(out);
		re.eval(incost);
	
	
		String RTS = ip.RTS.toLowerCase(); 
		re.eval("xopt <- cost.opt(x,y,w,RTS = \"" + RTS +"\")");
		re.eval("cobs <- x %*% t(w)");
		re.eval("copt <- xopt$x %*% t(w)");
		re.eval("re <- copt/cobs");
		
		double[] scores = re.eval("re").asDoubleArray();
		
		for(int i=0;i<scores.length;i++)
		{
		deascores+=scores[i]+",";
		}
		
		if (scores.length>0) {deascores = deascores.substring(0, deascores.length()-1); }	
		System.out.println("cost dea: " + deascores);
		return deascores;
	}
	

	public String getProfitDEA(InputParameters ip, Connection con, Rengine re, String in, String out, String incost, String outcost) throws Exception
	{
		
		String deascores = "";
		re.eval("library(Benchmarking)");
		re.eval(in);
		re.eval(out);
		re.eval(incost);
		re.eval(outcost);
	
	
		String RTS = ip.RTS; 
		
		re.eval("popt <- profit.opt(x,y,w,p,RTS = \"" + RTS +"\")");
		re.eval("robs <- y %*% t(p)");
		re.eval("cobs <- x %*% t(w)");
		re.eval("pobs <- robs - cobs");
		double[] pobs = re.eval("pobs").asDoubleArray();
		double min = 1000000000.0;
		for(int i=0; i<pobs.length; i++)
		if(pobs[i] < min) min = pobs[i];
		if(min < 0) 
		{re.eval("min <- -"+min);
		re.eval("re <- (pobs+min)/(popt$profit+min)");}
		else
		{re.eval("re <- pobs/popt$profit");}	
		
		double[] scores = re.eval("re").asDoubleArray();
		
		for(int i=0;i<scores.length;i++)
		{
		deascores+=scores[i]+",";
		}
		
		if (scores.length>0) {deascores = deascores.substring(0, deascores.length()-1); }	
		
		System.out.println("profit dea: " + deascores);
		
		return deascores;
	}
	

}




