package org.processmining.plugins.miningresourceprofiles.outputs;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.util.ShapeUtilities;
import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;


public class VisualiseResults{
	
	
	public void showMultiChart(Vector<ChartPanel> chartPanels,String title)
	{
	JDialog f = new JDialog(); 
	f.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
    for(int i=0; i<chartPanels.size(); i++) 
   {c.ipadx = 750;
	c.ipady = 300;
    c.gridx = 0;
   	c.gridy = i;
	f.add(chartPanels.elementAt(i), c);
   }
     
     f.setSize(800, 970);
     f.setLocationRelativeTo(null);
     f.setModal(true);
     f.setVisible(true);
     f.setTitle(title);
     f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public void showChart(ChartPanel chartPanel, String title)
	{JDialog f = new JDialog(); 
    f.setTitle(title);
    f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    f.setLayout(new BorderLayout());
    f.setSize(800, 400);
    f.add(chartPanel, BorderLayout.CENTER);
    f.setLocationRelativeTo(null);
    f.setModal(true);
    f.setVisible(true);
	}

	
	public ChartPanel getBarChart(InputParameters ip, String prod) throws Exception
	{
		
Vector<String> res = ip.resources;
String[] parts = prod.split(",");
Vector<Double> prods = new  Vector<Double>();
for(int i=0; i<parts.length; i++)
{Double cur = Double.parseDouble(parts[i]);
prods.add(cur);}
DefaultCategoryDataset bardataset = new DefaultCategoryDataset();  

for(int i=0; i<res.size();i++)
{
	bardataset.setValue(prods.elementAt(i),"Productivity" ,res.elementAt(i));  	
}
		  
			JFreeChart barchart = ChartFactory.createBarChart(  
	         "Resources-Productivity",   
	         "Resources",     
	         "Productivity",       
	         bardataset,            
	         PlotOrientation.VERTICAL,       
	         false,               
	         true,               
	         false                
	      );  
	     barchart.getTitle().setPaint(Color.BLUE);    
		
		return(new ChartPanel(barchart));
	}

	
	public ChartPanel plotOneTS(InputParameters ip, Connection con, int ts_id, int inID) throws Exception
	{
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(getTS(ip,con,ts_id));
		
		String title = "";
		String rbi = ip.rbi_inputs.elementAt(inID).rbi;
		String rbiid = rbi.substring(3);
		String rbiName = "";
		
		Statement dbStatement1 = con.createStatement();
	
		String sqlQuery2 = "SELECT name FROM rbis where id='"+rbiid+"'";
			ResultSet rs2 = dbStatement1.executeQuery(sqlQuery2);
			rs2.beforeFirst();
			rs2.next();
			rbiName = rs2.getString("name");
		
		title = rbiName + ": ";
		
		for (int i=0; i<ip.rbi_inputs.elementAt(inID).varName.size(); i++)
		{
				title+= ip.rbi_inputs.elementAt(inID).varValue.elementAt(i);
		}
		
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setDataset(0,dataset);
		XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer();
		chart.getXYPlot().setRenderer(0,rr);
	
		return(new ChartPanel(chart));
	}
	
	@SuppressWarnings("deprecation")
	public ChartPanel plotTSDisplayNew(InputParameters ip, Connection con, int ts_id, int inID) throws Exception
	{
			
		
		TimeSeries ts = getTS(ip,con,ts_id);	
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		ts_dataset.addSeries(ts);
		
		TimeSeries trend = getTSTrend(ip,con,ts_id);	
		TimeSeriesCollection trend_dataset = new TimeSeriesCollection();
		trend_dataset.addSeries(trend);
		
		TimeSeries cp = getTSCP(ip,con,ts_id);	
		TimeSeriesCollection cp_dataset = new TimeSeriesCollection();
		cp_dataset.addSeries(cp);
		
		TimeSeries out = getTSOut(ip,con,ts_id);	
		TimeSeriesCollection out_dataset = new TimeSeriesCollection();
		out_dataset.addSeries(out);
		 
		
		String title = "";
		String rbi = ip.rbi_inputs.elementAt(inID).rbi;
		String rbiid = rbi.substring(3);
		String rbiName = "";
		
		Statement dbStatement1 = con.createStatement();
	
		String sqlQuery2 = "SELECT name FROM rbis where id='"+rbiid+"'";
			ResultSet rs2 = dbStatement1.executeQuery(sqlQuery2);
			rs2.beforeFirst();
			rs2.next();
			rbiName = rs2.getString("name");
		
		
		title = rbiName + ": ";
		
		for (int i=0; i<ip.rbi_inputs.elementAt(inID).varName.size(); i++)
		{
				title+= ip.rbi_inputs.elementAt(inID).varValue.elementAt(i);
			
		}
		
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		
	
		int i=0;
		if(!cp.isEmpty())
		{
			xyplot.setDataset(i,cp_dataset);
			XYLineAndShapeRenderer rr0 = new XYLineAndShapeRenderer();
			rr0.setSeriesLinesVisible(0, false);
			rr0.setSeriesShapesVisible(0, true);
			rr0.setPaint(Color.MAGENTA);//yellow
			rr0.setSeriesShape(0,ShapeUtilities.createUpTriangle(6.0f));
			chart.getXYPlot().setRenderer(i++,rr0);
		}
		
		if(!out.isEmpty())
		{		
			xyplot.setDataset(i,out_dataset);
			XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
			rr1.setSeriesLinesVisible(0, false);
			rr1.setSeriesShapesVisible(0, true);
			rr1.setPaint(Color.red);
			rr1.setSeriesShape(0,ShapeUtilities.createDiamond(6.0f));
			chart.getXYPlot().setRenderer(i++,rr1);
		} 

			
		xyplot.setDataset(i,ts_dataset);
		XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
		rr2.setSeriesLinesVisible(0, true);
		rr2.setSeriesShapesVisible(0, false);
		rr2.setPaint(Color.blue);
		chart.getXYPlot().setRenderer(i++,rr2);
	
		xyplot.setDataset(i,trend_dataset);
		
		@SuppressWarnings("serial")
		XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer(){
	        @Override
	        public Stroke getItemStroke(int row, int col){
	            Stroke dash1 = new BasicStroke(2.0f,
	                        BasicStroke.CAP_SQUARE,
	                        BasicStroke.JOIN_MITER,
	                        10.0f,
	                        new float[] {10.0f,10.0f},
	                        0.0f);
	            return dash1;
	        }
	    };
	    rr3.setSeriesLinesVisible(0, true);
		rr3.setSeriesShapesVisible(0, false);
		rr3.setPaint(Color.green);//cyan
		rr3.setDrawSeriesLineAsPath(true);
		chart.getXYPlot().setRenderer(i++,rr3);

		return(new ChartPanel(chart));
	}


	
	@SuppressWarnings("deprecation")
	public ChartPanel plotCompareTS(InputParameters ip, Connection con, int ts_id, double pvalue) throws Exception
	{
		
		
		TimeSeries ts = getTS(ip,con,ts_id);	
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		ts_dataset.addSeries(ts);
		
		TimeSeries ts_mean = getTSMean(ip,con,ts_id);	
		TimeSeriesCollection ts_mean_dataset = new TimeSeriesCollection();
		ts_mean_dataset.addSeries(ts_mean);
	
		
		String title = "";
		String rbi = ip.rbi_inputs.elementAt(0).rbi;
		String rbiid = rbi.substring(3);
		String rbiName = "";
		
		Statement dbStatement1 = con.createStatement();
	
		String sqlQuery2 = "SELECT name FROM rbis where id='"+rbiid+"'";
			ResultSet rs2 = dbStatement1.executeQuery(sqlQuery2);
			rs2.beforeFirst();
			rs2.next();
			rbiName = rs2.getString("name");
		
		title = rbiName + ", p-value: "+ String.format("%.2f", pvalue);
			
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		XYPlot xyplot = chart.getXYPlot();
		
		xyplot.setDataset(0,ts_dataset);
		XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
		rr2.setSeriesLinesVisible(0, true);
		rr2.setSeriesShapesVisible(0, false);
		rr2.setPaint(Color.blue);
		chart.getXYPlot().setRenderer(0,rr2);
			
		xyplot.setDataset(1,ts_mean_dataset);
		XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer();
		rr3.setSeriesLinesVisible(0, true);
		rr3.setSeriesShapesVisible(0, false);
		rr3.setPaint(Color.green);
		chart.getXYPlot().setRenderer(1,rr3);
	
	return(new ChartPanel(chart));
	}

	

	
	@SuppressWarnings("deprecation")
	public void plotTSDisplay(InputParameters ip, Connection con, int ts_id, int inID) throws Exception
	{
			
		TimeSeries ts = getTS(ip,con,ts_id);	
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		ts_dataset.addSeries(ts);
		
		TimeSeries cp = getTSCP(ip,con,ts_id);	
		TimeSeriesCollection cp_dataset = new TimeSeriesCollection();
		cp_dataset.addSeries(cp);
		
		TimeSeries out = getTSOut(ip,con,ts_id);	
		TimeSeriesCollection out_dataset = new TimeSeriesCollection();
		out_dataset.addSeries(out);
		
		TimeSeries trend = getTSTrend(ip,con,ts_id);	
		TimeSeriesCollection trend_dataset = new TimeSeriesCollection();
		trend_dataset.addSeries(trend);
	
		String title = "";
		String rbi = ip.rbi_inputs.elementAt(inID).rbi;
		String rbiid = rbi.substring(3);
		String rbiName = "";
		
		Statement dbStatement1 = con.createStatement();
	
		String sqlQuery2 = "SELECT name FROM rbis where id='"+rbiid+"'";
			ResultSet rs2 = dbStatement1.executeQuery(sqlQuery2);
			rs2.beforeFirst();
			rs2.next();
			rbiName = rs2.getString("name");
		
		
		title = rbiName + ": ";
		
		for (int i=0; i<ip.rbi_inputs.elementAt(inID).varName.size(); i++)
		{
			title+= ip.rbi_inputs.elementAt(inID).varName.elementAt(i)+" - "+ ip.rbi_inputs.elementAt(inID).varValue.elementAt(i)+ ", ";
		}
		
		
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		
		xyplot.setDataset(0,cp_dataset);
		XYLineAndShapeRenderer rr0 = new XYLineAndShapeRenderer();
		rr0.setSeriesLinesVisible(0, false);
		rr0.setSeriesShapesVisible(0, true);
		rr0.setPaint(Color.MAGENTA);//yellow
		rr0.setSeriesShape(0,ShapeUtilities.createUpTriangle(6.0f));
		chart.getXYPlot().setRenderer(0,rr0);
				
		xyplot.setDataset(1,out_dataset);
		XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
		rr1.setSeriesLinesVisible(0, false);
		rr1.setSeriesShapesVisible(0, true);
		rr1.setPaint(Color.red);
		rr1.setSeriesShape(0,ShapeUtilities.createDiamond(6.0f));
		chart.getXYPlot().setRenderer(1,rr1);

		xyplot.setDataset(2,ts_dataset);
		XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
		rr2.setSeriesLinesVisible(0, true);
		rr2.setSeriesShapesVisible(0, false);
		rr2.setPaint(Color.blue);
		chart.getXYPlot().setRenderer(2,rr2);
			
		xyplot.setDataset(3,trend_dataset);
		
		@SuppressWarnings("serial")
		XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer(){
	        @Override
	        public Stroke getItemStroke(int row, int col){
	            Stroke dash1 = new BasicStroke(2.0f,
	                        BasicStroke.CAP_SQUARE,
	                        BasicStroke.JOIN_MITER,
	                        10.0f,
	                        new float[] {10.0f,10.0f},
	                        0.0f);
	            return dash1;
	        }
	    };
		
		rr3.setSeriesLinesVisible(0, true);
		rr3.setSeriesShapesVisible(0, false);
		rr3.setPaint(Color.green);//cyan
		rr3.setDrawSeriesLineAsPath(true);
		chart.getXYPlot().setRenderer(3,rr3);
		
	
		String rbi_id = "";
		
		try {
			String sqlQuery = "SELECT rbi_id FROM ts WHERE ts_id='" +ts_id+"'"; 
			Statement dbStatement = con.createStatement();
			 
			ResultSet rs = dbStatement.executeQuery(sqlQuery);
			rs.beforeFirst();
			
			if (rs.next()) 
			{
				rbi_id = rs.getString("rbi_id");
			}
	
			
		ChartUtilities.saveChartAsJPEG(new File("C:\\temp\\"+rbi_id+"_ts"+ts_id+".jpg"), chart, 1200, 400);
		} catch (IOException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                JOptionPane.ERROR_MESSAGE);
		}

	}

	
	public ChartPanel plotMultiTS(InputParameters ip, Connection con, Vector<Integer> ts_id, String rbi_title) throws Exception
	{
		
		
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		for(int i=0;i<ts_id.size();i++)
		{
		ts_dataset.addSeries(getTS(ip,con,ts_id.elementAt(i)));
		}
	
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				rbi_title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		
		
		xyplot.setDataset(0,ts_dataset);
		XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer();
		chart.getXYPlot().setRenderer(0,rr);
			

		return(new ChartPanel(chart));
	}

	
	
	@SuppressWarnings("deprecation")
	public void plotTS(InputParameters ip, Connection con, int ts_id, String rbi_title) throws Exception
	{
		
		
		TimeSeries ts = getTS(ip,con,ts_id);	
		TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
		ts_dataset.addSeries(ts);
		
		TimeSeries ts_mean = getTSMean(ip,con,ts_id);	
		TimeSeriesCollection ts_mean_dataset = new TimeSeriesCollection();
		ts_mean_dataset.addSeries(ts_mean);
	
		TimeSeries cp = getTSCP(ip,con,ts_id);	
		TimeSeriesCollection cp_dataset = new TimeSeriesCollection();
		cp_dataset.addSeries(cp);
		
		TimeSeries out = getTSOut(ip,con,ts_id);	
		TimeSeriesCollection out_dataset = new TimeSeriesCollection();
		out_dataset.addSeries(out);
		
		TimeSeries trend = getTSTrend(ip,con,ts_id);	
		TimeSeriesCollection trend_dataset = new TimeSeriesCollection();
		trend_dataset.addSeries(trend);
	
	
	
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				rbi_title, // Title
				"Date", // x-axis Label
				"RBI value", // y-axis Label
				ts_dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		
		xyplot.setDataset(0,cp_dataset);
		XYLineAndShapeRenderer rr0 = new XYLineAndShapeRenderer();
		rr0.setSeriesLinesVisible(0, false);
		rr0.setSeriesShapesVisible(0, true);
		rr0.setPaint(Color.MAGENTA);//yellow
		rr0.setSeriesShape(0,ShapeUtilities.createUpTriangle(6.0f));
		chart.getXYPlot().setRenderer(0,rr0);
				
		xyplot.setDataset(1,out_dataset);
		XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
		rr1.setSeriesLinesVisible(0, false);
		rr1.setSeriesShapesVisible(0, true);
		rr1.setPaint(Color.red);
		rr1.setSeriesShape(0,ShapeUtilities.createDiamond(6.0f));
		chart.getXYPlot().setRenderer(1,rr1);

		xyplot.setDataset(2,ts_dataset);
		XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
		rr2.setSeriesLinesVisible(0, true);
		rr2.setSeriesShapesVisible(0, false);
		rr2.setPaint(Color.blue);
		chart.getXYPlot().setRenderer(2,rr2);
			
		xyplot.setDataset(3,ts_mean_dataset);
		XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer();
		rr3.setSeriesLinesVisible(0, true);
		rr3.setSeriesShapesVisible(0, false);
		rr3.setPaint(Color.green);
		chart.getXYPlot().setRenderer(3,rr3);
	
		xyplot.setDataset(4,trend_dataset);
		
		@SuppressWarnings("serial")
		XYLineAndShapeRenderer rr4 = new XYLineAndShapeRenderer(){
	        @Override
	        public Stroke getItemStroke(int row, int col){
	            Stroke dash1 = new BasicStroke(2.0f,
	                        BasicStroke.CAP_SQUARE,
	                        BasicStroke.JOIN_MITER,
	                        10.0f,
	                        new float[] {10.0f,10.0f},
	                        0.0f);
	            return dash1;
	        }
	    };
		
		rr4.setSeriesLinesVisible(0, true);
		rr4.setSeriesShapesVisible(0, false);
		rr4.setPaint(Color.green);//cyan
		rr4.setDrawSeriesLineAsPath(true);
		chart.getXYPlot().setRenderer(4,rr4);
		
	
		String rbi_id = "";
		
		try {
			
			String sqlQuery = "SELECT rbi_id FROM ts WHERE ts_id='" +ts_id+"'"; 
			Statement dbStatement = con.createStatement();
			 
			ResultSet rs = dbStatement.executeQuery(sqlQuery);
			rs.beforeFirst();
			
			if (rs.next()) 
			{
				rbi_id = rs.getString("rbi_id");
			}
	
			
		ChartUtilities.saveChartAsJPEG(new File("C:\\temp\\"+rbi_id+"_ts"+ts_id+".jpg"), chart, 1200, 400);
		} catch (IOException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public TimeSeries getTSTrend(InputParameters ip, Connection con, int ts_id) throws Exception
	{
		
		TimeSeries ts = new TimeSeries("Trend");	
		Statement dbStatement = con.createStatement();
		
		String sqlQuery0 = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		String times = "";
		times = rs0.getString("ts_times");
		String times_array[] = times.split(",");
		
		Vector<Date> ts_times = new Vector<Date>();
		for(int i=0; i<times_array.length; i++)
		{
			ts_times.add(Timestamp.valueOf(times_array[i]));
		}
		
			
		String sqlQuery = "SELECT * FROM trends WHERE ts_id='"+ts_id+"'" +
				" AND period='"+ip.period+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
if(rs.next())
{		
		String slope = rs.getString("slope");
		String intercept = rs.getString("intercept"); 
		Double v1 = 0.0;
		Double v2 = 0.0;
			
if(ip.period.equals("full"))
{
			Date d1 = ts_times.firstElement();
			Date d2 = ts_times.lastElement();
			
			v1 = Double.valueOf(intercept);
			v2 = Double.valueOf(slope)*(ts_times.size()-1) + Double.valueOf(intercept);
			
			ts.add(new Second(d1),v1);
			ts.add(new Second(d2),v2);
}else
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
		
		Date d1 = ts_times.elementAt(maxCP);
		Date d2 = ts_times.lastElement();
		
		v1 = Double.valueOf(slope)*maxCP + Double.valueOf(intercept);
		v2 = Double.valueOf(slope)*(ts_times.size()-1) + Double.valueOf(intercept);
		
		ts.add(new Second(d1),v1);
		ts.add(new Second(d2),v2);
		
	}
	else
	{
		Date d1 = ts_times.firstElement();
		Date d2 = ts_times.lastElement();
		
		v1 = Double.valueOf(intercept);
		v2 = Double.valueOf(slope)*(ts_times.size()-1) + Double.valueOf(intercept);
		
		ts.add(new Second(d1),v1);
		ts.add(new Second(d2),v2);
	}


}	
		
		System.out.println(ts.getDataItem(0).getPeriod()+" - "+ts.getDataItem(0).getValue());
		System.out.println(ts.getDataItem(1).getPeriod()+" - "+ts.getDataItem(1).getValue());
	
}		
		
		return ts;
		
	}
	
	

	
	public TimeSeries getTSOut(InputParameters ip, Connection con, int ts_id) throws Exception
	{
		
		TimeSeries ts = new TimeSeries("Outliers");	
		Statement dbStatement = con.createStatement();
		
		String sqlQuery0 = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		String times = "";
		times = rs0.getString("ts_times");
		String times_array[] = times.split(",");
		
		Vector<Date> ts_times = new Vector<Date>();
		for(int i=0; i<times_array.length; i++)
		{
			ts_times.add(Timestamp.valueOf(times_array[i]));
		}
		
		String rbits = "";
		rbits = rs0.getString("ts_values");
		String ts_array[] = rbits.split(",");
			Vector<Double> ts_values = new Vector<Double>();
		for(int i=0; i<ts_array.length; i++)
		{
			ts_values.add(Double.valueOf(ts_array[i]));
		}
		
		String sqlQuery = "SELECT * FROM outliers WHERE ts_id='"+ts_id+"'" +
				" AND method='"+ip.method+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		
if(rs.next())
{
			
		String out_right = "";
		out_right = rs.getString("outliers_right");
		String or_array[] = out_right.split(",");
		Vector<Integer> or_times = new Vector<Integer>();
	if(or_array.length > 0 && !or_array[0].equals(""))
	{	
		
		for(int i=0; i<or_array.length; i++)
		{
			or_times.add(Integer.valueOf(or_array[i]));
			}
		
		for(int i=0; i<or_times.size(); i++)
		{
			ts.add(new Second(ts_times.elementAt(or_times.elementAt(i)-1)),ts_values.elementAt(or_times.elementAt(i)-1));
			
			System.out.println(ts.getDataItem(i).getPeriod()+" - "+ts.getDataItem(i).getValue());
			
		}
		
	}	
		String out_left = "";
		out_left = rs.getString("outliers_left");
		String ol_array[] = out_left.split(",");
		
		Vector<Integer> ol_times = new Vector<Integer>();
	if(ol_array.length > 0 && !ol_array[0].equals(""))
	{
		for(int i=0; i<ol_array.length; i++)
		{
			ol_times.add(Integer.valueOf(ol_array[i]));
		}
		
		for(int i=0; i<ol_times.size(); i++)
		{
			ts.add(new Second(ts_times.elementAt(ol_times.elementAt(i)-1)),ts_values.elementAt(ol_times.elementAt(i)-1));
			
			System.out.println(ts.getDataItem(i).getPeriod()+" - "+ts.getDataItem(i).getValue());
			
		}
	}
}		
		return ts;
		
	}
	

	
	public TimeSeries getTSCP(InputParameters ip, Connection con, int ts_id) throws Exception
	{
		
		TimeSeries ts = new TimeSeries("Change Points");	
		Statement dbStatement = con.createStatement();
		
		String sqlQuery0 = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		String times = "";
		times = rs0.getString("ts_times");
		String times_array[] = times.split(",");
		
		Vector<Date> ts_times = new Vector<Date>();
		for(int i=0; i<times_array.length; i++)
		{
			ts_times.add(Timestamp.valueOf(times_array[i]));
		}
		
		String rbits = "";
		rbits = rs0.getString("ts_values");
		String ts_array[] = rbits.split(",");
		Vector<Double> ts_values = new Vector<Double>();
		for(int i=0; i<ts_array.length; i++)
		{
			ts_values.add(Double.valueOf(ts_array[i]));
		}
		
		String sqlQuery = "SELECT * FROM change_points WHERE ts_id='"+ts_id+"'" +
				" AND cpmtype='"+ip.cpmType+"' AND arl='"+ip.ARL0+"' AND startup='"+ip.startup+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
if(rs.next())
{		
		String tscp = "";
		tscp = rs.getString("cp");
		String cp_array[] = tscp.split(",");
		Vector<Integer> cp_times = new Vector<Integer>();
		
	if(cp_array.length > 0 && !cp_array[0].equals(""))
	{	
		for(int i=0; i<cp_array.length; i++)
		{
			cp_times.add(Integer.valueOf(cp_array[i]));
		}
		
		for(int i=0; i<cp_times.size(); i++)
		{
			ts.add(new Second(ts_times.elementAt(cp_times.elementAt(i)-1)),ts_values.elementAt(cp_times.elementAt(i)-1));
			
			System.out.println(ts.getDataItem(i).getPeriod()+" - "+ts.getDataItem(i).getValue());
			
		}
	}	
}		
		
		return ts;
		
	}
	
	

public TimeSeries getTSMean(InputParameters ip, Connection con, int ts_id) throws Exception
	{
		
		TimeSeries ts = new TimeSeries("Mean values for other active resources");	
	
		Statement dbStatement = con.createStatement();
		
		String sqlQuery0 = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
		rs0.beforeFirst();
		rs0.next();
		String times = "";
		times = rs0.getString("ts_times");
		String times_array[] = times.split(",");
		
		Vector<Date> ts_times = new Vector<Date>();
		for(int i=0; i<times_array.length; i++)
		{
			ts_times.add(Timestamp.valueOf(times_array[i]));
		}
		
		//String mean_type = ip.meanMethod;
		//int exclude_zeros = 0;
		//if(ip.excludeTSZeros){exclude_zeros=1;};
		String sqlQuery = "SELECT * FROM ts_mean"; 
	
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
if(rs.next())
{	
		
		String rbitsmean = "";
		rbitsmean = rs.getString("ts_values");
		String ts_array[] = rbitsmean.split(",");
		Vector<Double> ts_values = new Vector<Double>();
		for(int i=0; i<ts_array.length; i++)
		{
			ts_values.add(Double.valueOf(ts_array[i]));
		}
		
		for(int i=0; i<times_array.length; i++)
		{
			ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));
			
			System.out.println(ts.getDataItem(i).getPeriod()+" - "+ts.getDataItem(i).getValue());
			
		}
		
}		
		
		return ts;
		
	}
	
	
//@SuppressWarnings("deprecation")
public TimeSeries getTS(InputParameters ip, Connection con, int ts_id) throws Exception
{
	
		
	Statement dbStatement = con.createStatement();
	String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	rs.next();
	
	String rbits = "";
	String times = "";
	//String invars = rs.getString("input_vars");
	String invalues = rs.getString("input_values");

	rbits = rs.getString("ts_values");
	//System.out.println("rbits: "+rbits);
	
	String ts_array[] = rbits.split(",");
	Vector<Double> ts_values = new Vector<Double>();
	for(int i=0; i<ts_array.length; i++)
	{
		ts_values.add(Double.valueOf(ts_array[i]));
	}
	
	times = rs.getString("ts_times");
	String times_array[] = times.split(",");
	
	Vector<Date> ts_times = new Vector<Date>();
	for(int i=0; i<times_array.length; i++)
	{
		ts_times.add(Timestamp.valueOf(times_array[i]));
	}
	
	//TimeSeries ts = new TimeSeries(invalues,Second.class); 
	TimeSeries ts = new TimeSeries(invalues);
	
	for(int i=0; i<times_array.length; i++)
	{
		ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i));
			
	}
	
	return ts;
	
}


public TimeSeries getTSinDays(InputParameters ip, Connection con, int ts_id) throws Exception
{
	
	TimeSeries ts = new TimeSeries("Resource values");	
	Statement dbStatement = con.createStatement();
	String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	rs.next();
	
	String rbits = "";
	String times = "";
	rbits = rs.getString("ts_values");
	String ts_array[] = rbits.split(",");
	Vector<Double> ts_values = new Vector<Double>();
	for(int i=0; i<ts_array.length; i++)
	{
		ts_values.add(Double.valueOf(ts_array[i])/1000/60/60/24);
	}
	
	times = rs.getString("ts_times");
	String times_array[] = times.split(",");
	
	Vector<Date> ts_times = new Vector<Date>();
	for(int i=0; i<times_array.length; i++)
	{
		ts_times.add(Timestamp.valueOf(times_array[i]));
	}
	
	for(int i=0; i<times_array.length; i++)
	{
		ts.add(new Day(ts_times.elementAt(i)),ts_values.elementAt(i));
		
	}
	
	
	
	return ts;
	
}


	@SuppressWarnings("deprecation")
	public void testJChartTimeSeries()
	{
		
//-------------TS----------------------
		
		TimeSeries pop = new TimeSeries("Population", Day.class);
		pop.add(new Day(10, 1, 2004), 100);
		pop.add(new Day(10, 2, 2004), 150);
		pop.add(new Day(10, 3, 2004), 250);
		pop.add(new Day(10, 4, 2004), 275);
		pop.add(new Day(10, 5, 2004), 325);		
		
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(pop);
		
		TimeSeries pop2 = new TimeSeries("Population2", Day.class);
		pop2.add(new Day(10, 1, 2004), 200);
		pop2.add(new Day(10, 2, 2004), 250);
		pop2.add(new Day(10, 3, 2004), 350);
		pop2.add(new Day(10, 4, 2004), 375);
		pop2.add(new Day(10, 5, 2004), 425);		
		
		TimeSeriesCollection dataset2 = new TimeSeriesCollection();
		dataset2.addSeries(pop2);
		
		TimeSeries pop3 = new TimeSeries("Trend", Day.class);
		pop3.add(new Day(10, 1, 2004), 100);
		pop3.add(new Day(10, 5, 2004), 500);
				
		
		TimeSeriesCollection dataset3 = new TimeSeriesCollection();
		dataset3.addSeries(pop3);
	
	
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Heading", // Title
				"Date", // x-axis Label
				"Population", // y-axis Label
				dataset, // Dataset
				true, // Show Legend
				true, // Use tooltips
				false // Configure chart to generate URLs?
				);
		
		
		XYPlot xyplot = chart.getXYPlot();
		xyplot.setDataset(0,dataset);
		xyplot.setDataset(1,dataset2);
		xyplot.setDataset(2,dataset3);
				
		XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
		rr1.setSeriesLinesVisible(0, true);
		rr1.setSeriesShapesVisible(0, true);
		rr1.setPaint(Color.green);
		chart.getXYPlot().setRenderer(0,rr1);


		XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer();
		rr.setSeriesLinesVisible(0, false);
		rr.setSeriesShapesVisible(0, true);
		rr.setPaint(Color.blue);
		chart.getXYPlot().setRenderer(1,rr);
		
		XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer();
		rr3.setSeriesLinesVisible(0, true);
		rr3.setSeriesShapesVisible(0, false);
		rr3.setPaint(Color.red);
		chart.getXYPlot().setRenderer(2,rr3);
	
		
		try {
		ChartUtilities.saveChartAsJPEG(new File("C:\\temp\\chart.jpg"), chart, 500, 300);
		} catch (IOException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/*
	public void testJChartMultDataSets()
	{

String sYAxis = "Strain";
String sYAxisUOM = "J";
String sYAxisLabel = sYAxis + "(" + sYAxisUOM + ")";
String sXAxis = "Temperature";
String sXAxisUOM = "C";
String sXAxisLabel = sXAxis + "(" + sXAxisUOM + ")";


XYSeriesCollection dataset = new XYSeriesCollection();
XYSeriesCollection dataset1 = new XYSeriesCollection();
XYSeriesCollection dataset2 = new XYSeriesCollection();
XYSeriesCollection dataset3 = new XYSeriesCollection();

XYSeries series = new XYSeries("XYGraph1");
series.add(1, 2);
series.add(2, 3);
series.add(3, 4);
series.add(4, 5);
series.add(5, 6);

XYSeries series1 = new XYSeries("XYGraph2");
series1.add(3, 4);
series1.add(4, 5);

XYSeries series2 = new XYSeries("XYGraph3");

series2.add(5, 6);

XYSeries series3 = new XYSeries("XYGraph4");

series3.add(0, 0);
series3.add(7, 7);

JFreeChart chart = ChartFactory.createXYLineChart(
"Heading", // Title
sXAxisLabel, // x-axis Label
sYAxisLabel, // y-axis Label
dataset, // Dataset
PlotOrientation.VERTICAL, // Plot Orientation
true, // Show Legend
true, // Use tooltips
false // Configure chart to generate URLs?
);

XYPlot xyplot = chart.getXYPlot();

NumberAxis domainAxis = new NumberAxis(sXAxisLabel);
NumberAxis rangeAxis = new NumberAxis(sYAxisLabel);

dataset.addSeries(series);
dataset1.addSeries(series1);
dataset2.addSeries(series2);
dataset3.addSeries(series3);

xyplot.setDataset(0,dataset2);
xyplot.setDataset(1,dataset1);
xyplot.setDataset(2,dataset);
xyplot.setDataset(3,dataset3);

xyplot.setDomainAxis(domainAxis);
xyplot.setRangeAxis(rangeAxis);

chart.setTitle("Heading");
chart.setBackgroundPaint(Color.white);



XYLineAndShapeRenderer rr1 = new XYLineAndShapeRenderer();
rr1.setSeriesLinesVisible(0, false);
rr1.setSeriesShapesVisible(0, true);
rr1.setPaint(Color.green);
chart.getXYPlot().setRenderer(0,rr1);


XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer();
rr.setSeriesLinesVisible(0, false);
rr.setSeriesShapesVisible(0, true);
rr.setPaint(Color.blue);
chart.getXYPlot().setRenderer(1,rr);

XYLineAndShapeRenderer rr2 = new XYLineAndShapeRenderer();
rr2.setSeriesLinesVisible(0, true);
rr2.setSeriesShapesVisible(0, true);
rr2.setPaint(Color.yellow);
chart.getXYPlot().setRenderer(2,rr2);

XYLineAndShapeRenderer rr3 = new XYLineAndShapeRenderer();
rr3.setSeriesLinesVisible(0, true);
rr3.setSeriesShapesVisible(0, false);
rr3.setPaint(Color.red);
chart.getXYPlot().setRenderer(3,rr3);



BufferedImage bImage1 = (BufferedImage) chart.createBufferedImage(800, 400);
JLabel label1 = new JLabel();
ImageIcon imageIcon1 = new ImageIcon(bImage1);
label1.setIcon(imageIcon1);

Frame frame = new Frame(" Graph");
frame.add(label1,BorderLayout.NORTH);

frame.setSize(1000,700);
frame.setVisible(true);
frame.addWindowListener(new WindowAdapter()
{
public void windowClosing(WindowEvent e)
{
System.exit(0);
}
});



	}
	
	public void testJChart()
	{
		
			XYSeries series = new XYSeries("XYGraph");
			series.add(1, 1);
			series.add(1, 2);
			series.add(2, 1);
			series.add(3, 9);
			series.add(4, 10);
			
			XYSeries series2 = new XYSeries("XYGraph2");
			series2.add(1, 2);
			series2.add(1, 3);
			series2.add(2, 2);
			series2.add(3, 10);
			series2.add(4, 11);
		
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(series);
			dataset.addSeries(series2);
			JFreeChart chart = ChartFactory.createXYAreaChart(//createXYLineChart
			"XY Chart", // Title
			"x-axis", // x-axis Label
			"y-axis", // y-axis Label
			dataset, // Dataset
			PlotOrientation.VERTICAL, // Plot Orientation
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
			);
			try {
			ChartUtilities.saveChartAsJPEG(new File("C:\\temp\\chart.jpg"), chart, 500, 300);
			} catch (IOException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
				}
	}
	 
	public void testoneTSAll (InputParameters ip, Connection con, int ts_id, Rengine re) throws Exception
	{
		
				
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT * FROM ts WHERE ts_id='"+ts_id+"'";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		rs.next();
		
		String ts = "";
		String times = "";
		ts = rs.getString("ts_values");
		String ts_array[] = ts.split(",");
		
		System.out.println("ts: "+ts);
		
		times = rs.getString("ts_times");
		String times_array[] = times.split(",");
		
		System.out.println("times: "+times);
		
		String sqlQuery2 = "SELECT * FROM change_points WHERE ts_id='"+ts_id+"'";
		ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
		rs2.beforeFirst();
		rs2.next();
		
		String change_points = "";
		change_points = rs2.getString("cp");
		String cp_times[] = change_points.split(",");
		String cp_values[] = new String [cp_times.length];
		for (int i=0; i<cp_times.length;i++)
		{
			cp_values[i] = ts_array[Integer.parseInt(cp_times[i])-1];
		}
				
		for (int i=0; i<cp_times.length;i++)
		{
			
			System.out.println("cp#"+i+": "+cp_times[i]+"---"+cp_values[i]);
		}
	
		String sqlQuery3 = "SELECT * FROM outliers WHERE ts_id='"+ts_id+"'";
		ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
		rs3.beforeFirst();
		rs3.next();
		
		String routliers = "";
		String loutliers = "";
		routliers = rs3.getString("outliers_right");
		loutliers = rs3.getString("outliers_left");
		
		String rout_times[] = routliers.split(",");
		String rout_values[] = new String [rout_times.length];
		
		for (int i=0; i<rout_times.length;i++)
		{
			rout_values[i] = ts_array[Integer.parseInt(rout_times[i])-1];
		}
				
		for (int i=0; i<rout_times.length;i++)
		{
			
			System.out.println("rout#"+i+": "+rout_times[i]+"---"+rout_values[i]);
		}
		
		String lout_times[] = loutliers.split(",");
		String lout_values[] = new String [lout_times.length];
		
		for (int i=0; i<lout_times.length;i++)
		{
			lout_values[i] = ts_array[Integer.parseInt(lout_times[i])-1];
		}
				
		for (int i=0; i<lout_times.length;i++)
		{
			
			System.out.println("lout#"+i+": "+lout_times[i]+"---"+lout_values[i]);
		}

		
	}
	
	*/
public TimeSeries getTSfromString(InputParameters ip, String tsname, String plotTS) throws Exception
{
	
TimeSeries ts = new TimeSeries(tsname);	
	
	
	String outts = "";
	outts = plotTS;
	String ts_array[] = outts.split(",");
	Vector<Double> ts_values = new Vector<Double>();
	for(int i=0; i<ts_array.length; i++)
	{
		ts_values.add(Double.valueOf(ts_array[i]));
		System.out.println("value: "+ts_values.elementAt(i).toString());
	}
	
	Vector<Date> ts_times = new Vector<Date>();	
	long tsPointTime=ip.startTime;
	
	for(int i=0; i<ip.numberOfSlots; i++)
	{
		tsPointTime+=ip.slotSize;
		ts_times.add(new Date (tsPointTime));
		System.out.println(ts_times.elementAt(i).toString());
	}
	
		
	for(int i=0; i<ip.numberOfSlots; i++)
	{
		ts.add(new Second(ts_times.elementAt(i)),ts_values.elementAt(i)); //RACQ
		
		System.out.println(ts.getDataItem(i).getPeriod()+" - "+ts.getDataItem(i).getValue());
		
	}
	
	
	
	return ts;
	
}



public ChartPanel plotMultiTSfromStrings(InputParameters ip, Vector<String> ts_strings, String mainTitle) throws Exception
{
	TimeSeriesCollection ts_dataset = new TimeSeriesCollection();
	for(int i=0;i<ts_strings.size();i++)
	{
		System.out.println("test!");
	ts_dataset.addSeries(getTSfromString(ip,ip.resources.elementAt(i),ts_strings.elementAt(i)));
	}

	JFreeChart chart = ChartFactory.createTimeSeriesChart(
			mainTitle, // Title
			"Date", // x-axis Label
			"Productivity", // y-axis Label
			ts_dataset, // Dataset
			true, // Show Legend
			true, // Use tooltips
			false // Configure chart to generate URLs?
			);
	
	
	XYPlot xyplot = chart.getXYPlot();
	
	
	xyplot.setDataset(0,ts_dataset);
	XYLineAndShapeRenderer rr = new XYLineAndShapeRenderer();
	chart.getXYPlot().setRenderer(0,rr);
	
	return new ChartPanel(chart);
	
}

public Vector<String> splitString(InputParameters ip, String all)
{
	Vector<String> vec = new Vector<String>();
	int resNum = ip.resources.size();
	int numSlots = ip.numberOfSlots;
	System.out.println("resNum: "+resNum+"numSlots: "+numSlots);
	String ts_array[] = all.split(",");
	
	for(int i=0; i<(resNum*numSlots); i+=numSlots)
	{
		String curVal="";
		for(int j=0; j<numSlots; j++)
		{	int index = i+j;
		System.out.println("i: "+i+" j: "+j+" index: "+index);
		curVal += ts_array[index]+",";
		}
		
		curVal = curVal.substring(0, curVal.length()-1);
		System.out.println("curVal: "+curVal);
		vec.add(curVal);
		
	}
	
	
	return vec;
	
}

	
}




