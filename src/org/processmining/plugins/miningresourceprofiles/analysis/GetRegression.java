package org.processmining.plugins.miningresourceprofiles.analysis;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;
import org.rosuda.JRI.Rengine;


public class GetRegression{
	
		public String getRegression(InputParameters ip, Connection con, Rengine re, String depVar, Vector<String> ExpVars, String filename) throws Exception
	{
		
			String expvars = "";
			re.eval("y <- c("+depVar+")");
		
			for(int i=0;i<ExpVars.size();i++)
			{
				re.eval("x"+i+" <- c("+ExpVars.elementAt(i)+")");
				expvars+="x"+i+"+";
			}
	
			if(!expvars.isEmpty())
			{expvars = expvars.substring(0, expvars.length()-1);} 
				
			re.eval("res <- lm(y~" + expvars +")");
		
			double slope = re.eval("res$coefficients[2]").asDouble();
			double intercept = re.eval("res$coefficients[1]").asDouble();

			System.out.println("slope: "+slope+" intercept: "+intercept);
		
			double rsquared = re.eval("summary(res)$r.squared").asDouble();
			System.out.println("r-squared: "+rsquared);
			Double rs = rsquared;
		
			double adjrsquared = re.eval("summary(res)$adj.r.squared").asDouble();
			System.out.println("adjusted r-squared: "+adjrsquared);
			//Double r2 = adjrsquared;
		
		double pvalue = re.eval("summary(res)$coefficients[2,4]").asDouble();
		System.out.println("p-value: "+pvalue);
		//Double pv = pvalue;
		
			
		//int numOfExpVars = ExpVars.size();
		
		//String out = "R2: "+ String.format("%.2f", r2) +" p-value: "+ String.format("%.2f", pv);
		
			
		return rs.toString();
		
	}
	

		public double[] getRegressionNew(InputParameters ip, Connection con, Rengine re, String depVar, Vector<String> ExpVars, String filename) throws Exception
	{
		//linear regression
		if(ip.regressionMethod.equalsIgnoreCase("linear"))
		{
			String expvars = "";
			re.eval("y <- c("+depVar+")");
		
			for(int i=0;i<ExpVars.size();i++)
			{
				re.eval("x"+i+" <- c("+ExpVars.elementAt(i)+")");
				expvars+="x"+i+"+";
			}
	
			if(!expvars.isEmpty())
			{expvars = expvars.substring(0, expvars.length()-1);} 
				
			re.eval("res <- lm(y~" + expvars +")");
		
			double intercept = re.eval("res$coefficients[1]").asDouble();

			double rsquared = re.eval("summary(res)$r.squared").asDouble();
			System.out.println("r-squared: "+rsquared);
			//Double rs = rsquared;
		
			double adjrsquared = re.eval("summary(res)$adj.r.squared").asDouble();
			System.out.println("adjusted r-squared: "+adjrsquared);
			//Double r2 = adjrsquared;
		
			double pvalue = re.eval("summary(res)$coefficients[2,4]").asDouble();
			System.out.println("p-value: "+pvalue);
			//Double pv = pvalue;
		
			//int numOfExpVars = ExpVars.size();
			//double slope = re.eval("res$coefficients[2]").asDouble();
			//System.out.println("slope: "+slope+" intercept: "+intercept);
		
		
			//String out = "R2: "+ String.format("%.2f", r2) +" p-value: "+ String.format("%.2f", pv);
			int resSize = ExpVars.size()+4;
			double[] result = new double[resSize];// = {rsquared,adjrsquared,pvalue,intercept};
			result[0] = rsquared;
			result[1] = adjrsquared;
			result[2] = pvalue;
			result[3] = intercept;
			
			for(int i=0;i<ExpVars.size();i++)
			{
				double slope = re.eval("res$coefficients["+(i+2)+"]").asDouble();
				result[i+4] = slope;
			}
			
			for(int i=0; i<result.length; i++)
			System.out.println("result "+i+": "+result[i]);
		
			return result;}
			
		 // non-parametric regression
		if(ip.regressionMethod.equalsIgnoreCase("non-parametric"))
		{
			String expvars = "";
			
			re.eval("y <- c("+depVar+")");
			
			for(int i=0;i<ExpVars.size();i++)
			{
			re.eval("x"+i+" <- c("+ExpVars.elementAt(i)+")");
			expvars+="x"+i+"+";
			}

			if(!expvars.isEmpty())
			{expvars = expvars.substring(0, expvars.length()-1);} 
			
			System.out.println("depVar: "+depVar);
			System.out.println("expvars: "+expvars);
	
			re.eval("library(np)");
			re.eval("res <- npreg(y~" + expvars +")");
			
			double rsquared = re.eval("res$R2").asDouble();
			System.out.println("Fitted R2: "+rsquared);
			
			double [] mean = re.eval("res$mean").asDoubleArray();
			
			double[] result = new double[mean.length+1+ExpVars.size()];
			result[0] = rsquared;
			
			for(int i=0; i<mean.length; i++)
				result[i+1] = mean[i];
			
			re.eval("pval <- npsigtest(res)");
			double [] pval = re.eval("pval$P").asDoubleArray();
			
			for(int i=0;i<ExpVars.size();i++)
				result[mean.length+1+i] = pval[i];
			
			
	/*		org.rosuda.JRI.REXP s = re.eval("capture.output(summary(res))");
			String[] res = s.asStringArray();
			for(int i=0; i<res.length; i++)
				System.out.println("res "+i +": "+res[i]);
			
			org.rosuda.JRI.REXP s2 = re.eval("capture.output(summary(pval))");
			String[] res2 = s2.asStringArray();
			for(int i=0; i<res2.length; i++)
				System.out.println("res2 "+i +": "+res2[i]);
*/		
			
			return result;
		}
			
			return null;
	}
		
		
		public Vector<String> getRegressionNewNPMult(InputParameters ip, Connection con, Rengine re, String depVar, Vector<String> ExpVars, String filename) throws Exception
	{
			Vector<String> result = new Vector<String>();
			
			String expvars = "";
			
			re.eval("y <- c("+depVar+")");
			
			for(int i=0;i<ExpVars.size();i++)
			{
			re.eval("x"+i+" <- c("+ExpVars.elementAt(i)+")");
			expvars+="x"+i+"+";
			}

			if(!expvars.isEmpty())
			{expvars = expvars.substring(0, expvars.length()-1);} 
			
			System.out.println("depVar: "+depVar);
			System.out.println("expvars: "+expvars);
	
			re.eval("library(np)");
			re.eval("res <- npreg(y~" + expvars +")");
			re.eval("pval <- npsigtest(res)");
			
			org.rosuda.JRI.REXP s = re.eval("capture.output(summary(res))");
			String[] res = s.asStringArray();
			for(int i=0; i<res.length; i++)
				{
					System.out.println("res "+i +": "+res[i]);
					result.add(res[i]);
				}
			result.add("");
			//result.add("Significance test summary:");
			//result.add("");
			
			org.rosuda.JRI.REXP s2 = re.eval("capture.output(summary(pval))");
			String[] res2 = s2.asStringArray();
			for(int i=0; i<res2.length; i++)
				{
					System.out.println("res2 "+i +": "+res2[i]);
					result.add(res2[i]);
				}
	
			//double rsquared = re.eval("res$R2").asDouble();
			//System.out.println("Fitted R2: "+rsquared);
			
			//double [] mean = re.eval("res$mean").asDoubleArray();
			
			//double[] result = new double[mean.length+1+ExpVars.size()];
			//result[0] = rsquared;
			
			//for(int i=0; i<mean.length; i++)
			//	result[i+1] = mean[i];
			
			
			//double [] pval = re.eval("pval$P").asDoubleArray();
			
			//for(int i=0;i<ExpVars.size();i++)
			//	result[mean.length+1+i] = pval[i];
			
			
			
			
			return result;
		
	}

		
		
		
		public void visReg( String dep, String exp, double[] regres)
		{
			
			
			double slope = regres[0];
			double intercept = regres[1];
			
			String[] y = dep.split(",");
			String[] x = exp.split(",");
			double maxX = 0;
			for(int i=0; i<x.length; i++)
			{
				Double cur = Double.parseDouble(x[i]);
				if (maxX < cur) maxX = cur;
			}
			
			final double X1 = 0;
			final double Y1 = intercept;
			final double X2 = maxX;
			final double Y2 = slope*maxX + intercept;
			
			XYSeriesCollection result = new XYSeriesCollection();
			XYSeries series = new XYSeries("Regression Analysis Results");
			    for (int i = 0; i < y.length; i++) {
			    double x1 = Double.parseDouble(x[i]);
			    double y1 = Double.parseDouble(y[i]);
			    series.add(x1, y1);
			   System.out.println(x1 + " --- "+ y1);
			   }
			result.addSeries(series);
		
			String title = "Adjusted R2: "+ String.format("%.2f", regres[3]) +" p-value: "+ String.format("%.2f", regres[4]);;
			
			JFreeChart chart = ChartFactory.createScatterPlot(
						title, // chart title
			            "Independent variable", // x axis label
			            "Dependent variable", // y axis label
			            result, // data  
			            PlotOrientation.VERTICAL,
			            true, // include legend
			            true, // tooltips
			            false // urls
			            );
			 
			 XYPlot plot = chart.getXYPlot();
		     XYLineAnnotation line = new XYLineAnnotation(
		     X1, Y1, X2, Y2, new BasicStroke(2f), Color.blue);
		     plot.addAnnotation(line);
				
				JDialog f = new JDialog();
			    f.setTitle("Regression analysis results");
			    f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    f.setLayout(new BorderLayout());
			    f.setSize(800, 800);
			    f.add(new ChartPanel(chart), BorderLayout.CENTER);
			    f.setLocationRelativeTo(null);
			    f.setModal(true);
			    f.setVisible(true);
		}


		
		public ChartPanel visualiseRegOne( String dep, String exp, double[] regres)
		{
			//{rsquared,adjrsquared,pvalue,intercept,slope};
			
			double slope = regres[4];
			double intercept = regres[3];
			
			String[] y = dep.split(",");
			String[] x = exp.split(",");
			double maxX = 0;
			for(int i=0; i<x.length; i++)
			{
				Double cur = Double.parseDouble(x[i]);
				if (maxX < cur) maxX = cur;
			}
			
			final double X1 = 0;
			final double Y1 = intercept;
			final double X2 = maxX;
			final double Y2 = slope*maxX + intercept;
			
			XYSeriesCollection result = new XYSeriesCollection();
			XYSeries series = new XYSeries("Regression Analysis Results");
			    for (int i = 0; i < y.length; i++) {
			    double x1 = Double.parseDouble(x[i]);
			    double y1 = Double.parseDouble(y[i]);
			    series.add(x1, y1);
			   System.out.println(x1 + " --- "+ y1);
			   }
			result.addSeries(series);
		
			String title = "R-squared: "+ String.format("%.2f", regres[1]) +" p-value: "+ String.format("%.2f", regres[2]);;
			
			JFreeChart chart = ChartFactory.createScatterPlot(
						title, // chart title
			            "Independent variable", // x axis label
			            "Dependent variable", // y axis label
			            result, // data  
			            PlotOrientation.VERTICAL,
			            true, // include legend
			            true, // tooltips
			            false // urls
			            );
			 
			 XYPlot plot = chart.getXYPlot();
		     XYLineAnnotation line = new XYLineAnnotation(
		     X1, Y1, X2, Y2, new BasicStroke(2f), Color.blue);
		     plot.addAnnotation(line);
				
		     
		return new ChartPanel(chart);}
		
	
		
		public ChartPanel visualiseNPRegOne( String dep, String exp, double[] regres)
		{
				
			String[] y = dep.split(",");
			String[] x = exp.split(",");
			double maxX = 0;
			for(int i=0; i<x.length; i++)
			{
				Double cur = Double.parseDouble(x[i]);
				if (maxX < cur) maxX = cur;
			}
			
				
			XYSeriesCollection data = new XYSeriesCollection();
			XYSeries series = new XYSeries("Data points");
			    for (int i = 0; i < y.length; i++) {
			    double x1 = Double.parseDouble(x[i]);
			    double y1 = Double.parseDouble(y[i]);
			    series.add(x1, y1);
			  // System.out.println(x1 + " --- "+ y1);
			   }
			data.addSeries(series);
			
			XYSeriesCollection fitted = new XYSeriesCollection();    
			XYSeries mean = new XYSeries("Fitted model");
		    for (int i = 0; i < x.length; i++) {
		    double x1 = Double.parseDouble(x[i]);
		    double y1 = regres[i+1];
		    mean.add(x1, y1);
		   //System.out.println(x1 + " --- "+ y1);
		   }
		
			fitted.addSeries(mean);
		
		
			String title = "R-squared: "+ String.format("%.2f", regres[0]) + ", p-value: "+ String.format("%.2f", regres[regres.length-1]);
			
			JFreeChart chart = ChartFactory.createScatterPlot(
						title, // chart title
			            "Independent variable", // x axis label
			            "Dependent variable", // y axis label
			            data, // data  
			            PlotOrientation.VERTICAL,
			            true, // include legend
			            true, // tooltips
			            false // urls
			            );
			
			 XYPlot plot = chart.getXYPlot();
			
						
				plot.setDataset(0,fitted);
				XYSplineRenderer r1 = new XYSplineRenderer();
				chart.getXYPlot().setRenderer(0,r1);
				
				plot.setDataset(1,data);
				XYLineAndShapeRenderer r2 = new XYLineAndShapeRenderer();
				r2.setSeriesLinesVisible(0, false);
				r2.setSeriesShapesVisible(0, true);
				chart.getXYPlot().setRenderer(1,r2);
	
		return new ChartPanel(chart);}


		
		public JLabel visualiseRegMult( String dep, String exp, double[] regres)
		{
			//{rsquared,adjrsquared,pvalue,intercept,slope};
			int num = regres.length - 4;
			
			String model = "Linear model: y = ";
			for(int i=0; i<num; i++)
			{
				model += "c"+(i+1)+"*x"+(i+1) +" + ";
			}
			model += "c"+(num+1);
			model += "<br/>";
			
			for(int i=4; i<num+4; i++)
			{
				model += "c"+(i-3)+" = "+ String.format("%.2f", regres[i])+"<br/>";
			}
			
			model += "c"+(num+1)+" = "+ String.format("%.2f", regres[3])+"<br/>";
	
			
			String title2 = "<html><h1>"+model+"<br/>R<sup>2</sup>: "+ String.format("%.2f", regres[1]) +", <br/>p-value: "+ String.format("%.2f", regres[2])+"</h1></html>";
			
		
			
	/*		String coefficients = "Linear model coefficients: ";
			for(int i=4; i<num+4; i++)
			{
				coefficients += String.format("%.2f", regres[i])+", ";
			}
			
			coefficients += String.format("%.2f", regres[3])+", ";
			
			String title = "<html><h1>"+coefficients+"adjusted R2: "+ String.format("%.2f", regres[1]) +", p-value: "+ String.format("%.2f", regres[2])+"</h1></html>";
	*/		
			JLabel jl = new JLabel(title2);
			jl.setForeground(UISettings.TextLight_COLOR);
			return jl;
		}
		
		//prev. version - not used
		public JLabel visualiseNPRegMult( String dep, String exp, double[] regres)
		{
			int size = dep.split(",").length+1;
			int expVars = regres.length - size;
			String pval = "<br/>p-value: ( ";
			for(int i=0; i<expVars; i++)
			{
				pval += String.format("%.2f", regres[size+i]) + " ";
			}
			pval += ")";
			
			String title = "<html><h1>R<sup>2</sup>: "+ String.format("%.2f", regres[0]) + pval + "</h1></html>";
			
			JLabel jl = new JLabel(title);
			jl.setForeground(UISettings.TextLight_COLOR);
			return jl;
		}
		
		public JLabel visualiseNPRegMult( Vector<String> result)
		{
			
			String res = "<html><h1>Fitted model details:</h1>";
			
			for(int i=0; i<result.size(); i++)
			{
				res += "<br/>"+result.elementAt(i);
			}
			
			res += "</html>";
			
			JLabel jl = new JLabel(res);
			jl.setForeground(UISettings.TextLight_COLOR);
			return jl;
		}




}




