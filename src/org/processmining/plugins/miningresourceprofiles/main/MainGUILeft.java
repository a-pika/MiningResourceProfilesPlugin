package org.processmining.plugins.miningresourceprofiles.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.plugins.miningresourceprofiles.analysis.CompareTS;
import org.processmining.plugins.miningresourceprofiles.analysis.GetChangePoints;
import org.processmining.plugins.miningresourceprofiles.analysis.GetDEAProductivity;
import org.processmining.plugins.miningresourceprofiles.analysis.GetOutliers;
import org.processmining.plugins.miningresourceprofiles.analysis.GetRegression;
import org.processmining.plugins.miningresourceprofiles.analysis.GetRegressionData;
import org.processmining.plugins.miningresourceprofiles.analysis.GetTimeSeries;
import org.processmining.plugins.miningresourceprofiles.analysis.GetTrend;
import org.processmining.plugins.miningresourceprofiles.analysis.RegressionData;
import org.processmining.plugins.miningresourceprofiles.define.DefineDEAViews;
import org.processmining.plugins.miningresourceprofiles.define.DefineRBI;
import org.processmining.plugins.miningresourceprofiles.define.DefineRegressionVars;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineDEAParams;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineRBIParams;
import org.processmining.plugins.miningresourceprofiles.inputs.DefineRegressionParams;
import org.processmining.plugins.miningresourceprofiles.inputs.GetDEAInOut;
import org.processmining.plugins.miningresourceprofiles.inputs.Indicator_Input;
import org.processmining.plugins.miningresourceprofiles.inputs.InputParameters;
import org.processmining.plugins.miningresourceprofiles.inputs.LogDataProcessing;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;
import org.processmining.plugins.miningresourceprofiles.outputs.VisualiseResults;
import org.rosuda.JRI.Rengine;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

public class MainGUILeft{
	
 
	public HeaderBar displayMainGUI(final Connection con, final XLog log, final Rengine re, final String host, final String user, final String pass, final String db) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");
	pane.setLayout(new GridBagLayout());
	final GridBagConstraints c = new GridBagConstraints();
		
	HeaderBar menuPane = new HeaderBar("");
	menuPane.setLayout(new GridLayout(0, 1));
	
	final HeaderBar mainPane = new HeaderBar("");
	mainPane.setLayout(new GridBagLayout());
	final GridBagConstraints cMain = new GridBagConstraints();
	
	final ProMScrollPane scrollPane = new ProMScrollPane(mainPane);
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double w = screenSize.getWidth();
	double h = screenSize.getHeight();
	final int widthMenu = (int) (0.93*w/6);
	final int widthMain = widthMenu*5;
	final int height = (int) (0.80*h);
	
	final int imgW = widthMenu * 4;
	final int imgH = widthMenu * 2;

	System.out.println(w +"---"+widthMenu+"---"+widthMain);
	System.out.println(h+"---"+height);
	
	JLabel importlab = new JLabel("<html><h1>Import</h1></html>");
	importlab.setForeground(UISettings.TextLight_COLOR);
	importlab.setHorizontalAlignment(JLabel.CENTER);
	
	JLabel definelab = new JLabel("<html><h1>Define</h1></html>");
	definelab.setForeground(UISettings.TextLight_COLOR);
	definelab.setHorizontalAlignment(JLabel.CENTER);
	
	JLabel analyzelab = new JLabel("<html><h1>Analyse</h1></html>");
	analyzelab.setForeground(UISettings.TextLight_COLOR);
	analyzelab.setHorizontalAlignment(JLabel.CENTER);
	
	JLabel lab0 = new JLabel(".");
	JLabel lab1 = new JLabel(".");

	SlickerButton createdbbut=new SlickerButton();
	createdbbut.setText("Import event log to DB (old data will be removed)");
	createdbbut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
                	mainPane.removeAll(); 
            		mainPane.updateUI();
            		mainPane.revalidate();
            		mainPane.repaint();
            		
                	LogDataProcessing procLog = new LogDataProcessing();
                	InputParameters ip = new InputParameters();
            		ip.setDB(host, user, pass, db);
            		
            		long start = System.nanoTime();
            	   	procLog.createDB(con, log, ip);
            	   	long end = System.nanoTime();
            	   	long dur = (end - start)/1000000000;
            	   	System.out.println("dur: " + dur);
            	   	
                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );

	
		SlickerButton analyseProfilebut=new SlickerButton();
		analyseProfilebut.setText("Get resource profile");


		analyseProfilebut.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	mainPane.removeAll(); 
	                	mainPane.setPreferredSize(new Dimension(widthMain, height));
		             	mainPane.updateUI();
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            
	            		
	                	DefineRBIParams defRBIParams = new DefineRBIParams();
	            		DefineRBIParams defRBIParams4 = new DefineRBIParams();
	            		
	              		VisualiseResults visRes = new VisualiseResults();
	            		
	            		GetTimeSeries getTS = new GetTimeSeries();
	            		InputParameters ip = new InputParameters();
	            		ip.setDB(host, user, pass, db);
	            		
	            		ip = defRBIParams4.defineTSParamsRes(ip,con); 
		            	
	            		ip = defRBIParams.selectMultRBI(con,ip);            		
	            		
	            		
	               		Vector<Integer> ts_id = new Vector<Integer>();
	            		
	            		int count = 0;
	            	
	            		mainPane.setDoubleBuffered(true);
	            		
	            		for(int i=0; i<ip.rbi_inputs.size(); i++)
	            		{
	            		System.out.println("rbi:" + ip.rbi_inputs.elementAt(i).rbi + "vars:" + ip.rbi_inputs.elementAt(i).varName + "values:" + ip.rbi_inputs.elementAt(i).varValue);
	            		ip = getTS.getOneRBITSIP(ip, con, i); 
	            		ts_id.add(ip.rbi_ts);
	            		System.out.println("ip rbi ts"+ip.rbi_ts);
	            		
	            		cMain.gridwidth = 1;
	            		cMain.gridheight = 1;
	            		cMain.ipadx = imgW;
	            		cMain.ipady = imgH;       
	            		cMain.gridx = 0;
	            		cMain.gridy = i;
	            			
	            		mainPane.add(visRes.plotOneTS(ip, con, ip.rbi_ts, i), cMain);
	            		count ++;
	            		}
	            		
	            		mainPane.setPreferredSize(new Dimension(imgW, imgH*count+50));
	             		mainPane.revalidate();
	            		mainPane.repaint();
	            		
	            		mainPane.setSize(new Dimension(imgW, imgH*count+50));
	            	    final BufferedImage bi = new BufferedImage(mainPane.getWidth(), mainPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
	              	   	mainPane.paintAll(bi.getGraphics());
	            	    
	               		SlickerButton saveBut = new SlickerButton("Save profile");
	              		cMain.anchor = GridBagConstraints.NORTH;
	              		cMain.gridwidth = 1;
	            		cMain.gridheight = 1;
	            		cMain.ipadx = 30;
	            		cMain.ipady = 10;       
	            		cMain.gridx = 1;
	            		cMain.gridy = 0;
	            		mainPane.add(saveBut, cMain);
	            
	            		saveBut.addActionListener(
	            	            new ActionListener(){
	            	                public void actionPerformed(
	            	                        ActionEvent e) 
	            	                {try{
	            	                	
	            	                	final String EXTENSION = ".png";
	            	                	final String FORMAT_NAME = "png";

	            	                  	 JFileChooser fileChooser = new JFileChooser();
	            	                  	 int status = fileChooser.showSaveDialog(new JFrame());

	            	                	 if (status == JFileChooser.APPROVE_OPTION) {
	            	                	 File selectedFile = fileChooser.getSelectedFile();

	            	                	        try {
	            	                	            String fileName = selectedFile.getCanonicalPath();
	            	                	            if (!fileName.endsWith(EXTENSION)) {
	            	                	                selectedFile = new File(fileName + EXTENSION);
	            	                	            }
	            	                	            ImageIO.write(bi, FORMAT_NAME, selectedFile);
	            	                	        } catch (IOException e1) {
	            	                	        	JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	            	           	                         JOptionPane.ERROR_MESSAGE);
	            	                	        }
	            	                	    

	            	                	    
	            	                	}
	            	                	
	            	                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	           	                         JOptionPane.ERROR_MESSAGE);};
	            	                }
	            	                				
	            	                                }
	            	                        );
	              	   	
	           
	                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );
	
	SlickerButton analyseOneRBIMultResbut=new SlickerButton();
	analyseOneRBIMultResbut.setText("Get an RBI for different resources");

	analyseOneRBIMultResbut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
                	mainPane.removeAll(); 
                	mainPane.setPreferredSize(new Dimension(widthMain, height));
            		mainPane.updateUI();
            		mainPane.revalidate();
            		mainPane.repaint();
            
            		
                	DefineRBIParams defRBIParams = new DefineRBIParams();
            		DefineRBIParams defRBIParams2 = new DefineRBIParams();
            		DefineRBIParams defRBIParams3 = new DefineRBIParams();
            		DefineRBIParams defRBIParams4 = new DefineRBIParams();
            			
            		VisualiseResults visRes = new VisualiseResults();
            		
            		GetTimeSeries getTS = new GetTimeSeries();
            		InputParameters ip = new InputParameters();
            		ip.setDB(host, user, pass, db);
            		            		
            		Vector<String> rbi = new Vector<String>();
            		rbi.addAll(defRBIParams.selectRBI(con));
            		Vector<String> vars = new Vector<String>();
            		
            		vars.addAll(defRBIParams2.selectRBIVars(con,rbi));
            		
            		if(vars.size()>0)
            		{ip = defRBIParams3.defineVarsAllRes(con, ip, vars, rbi);}else
            		{
            			vars.add("R1");
            		}
            		
            		ip = defRBIParams4.defineTSParams(ip,con); 
            		Vector<Integer> ts_id = new Vector<Integer>();
            		for(int i=0; i<ip.rbi_inputs.size(); i++)
            		{
            		ip = getTS.getOneRBITSIP(ip, con, i); 
            		ts_id.add(ip.rbi_ts);
            		}
            		
            		String iprbi = ip.rbi_inputs.elementAt(0).rbi;
            		String rbiid = iprbi.substring(3);
            		Statement dbStatement1 = con.createStatement();
            	
            		String sqlQuery2 = "SELECT name FROM rbis where id='"+rbiid+"'";
            		ResultSet rs2 = dbStatement1.executeQuery(sqlQuery2);
            		rs2.beforeFirst();
            		rs2.next();
            		String rbiName = rs2.getString("name");
            	
            		
            		cMain.gridwidth = 1;
            		cMain.gridheight = 1;
            		cMain.ipadx = imgW;
            		cMain.ipady = imgH;       
            		cMain.gridx = 0;
            		cMain.gridy = 0;
            			
            		mainPane.add(visRes.plotMultiTS(ip, con, ts_id, rbiName), cMain);
            		mainPane.revalidate();
            		mainPane.repaint();
            		mainPane.revalidate();
            		mainPane.repaint();
                        		
            		
                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                        JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );
	
	
		SlickerButton analyseOneRBIbut=new SlickerButton();
		analyseOneRBIbut.setText("Analyse an RBI");

		analyseOneRBIbut.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	mainPane.removeAll();
	                	mainPane.setPreferredSize(new Dimension(widthMain, height));
	            		mainPane.updateUI();
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            		
	                	DefineRBIParams defRBIParams = new DefineRBIParams();
	            		DefineRBIParams defRBIParams2 = new DefineRBIParams();
	            		DefineRBIParams defRBIParams3 = new DefineRBIParams();
	            		DefineRBIParams defRBIParams4 = new DefineRBIParams();
	            		DefineRBIParams defRBIParams6 = new DefineRBIParams();
	            		
	            		GetChangePoints getCP = new GetChangePoints();
	            		GetOutliers getOut = new GetOutliers();
	            		GetTrend getTrend = new GetTrend();
	            		VisualiseResults visRes = new VisualiseResults();
	            		
	            		GetTimeSeries getTS = new GetTimeSeries();
	            		InputParameters ip = new InputParameters();
	            		ip.setDB(host, user, pass, db);
	            		            		
	            		Vector<String> rbi = new Vector<String>();
	            		rbi.addAll(defRBIParams.selectRBI(con));
	            		Vector<String> vars = new Vector<String>();
	            		
	            		vars.addAll(defRBIParams2.selectRBIVars(con,rbi));
	            		System.out.println("vars: "+vars);
	            		if(vars.size()>0)
	            		{ip = defRBIParams3.defineVars(con, ip, vars, rbi);}else
	            		{
	            			Vector<String> varValues = new Vector<String>();
	            			Vector<String> otherVars = new Vector<String>();
	            			String rbiid = rbi.elementAt(0);
	            			otherVars.add("R1");
	            			varValues.add("test");
	            			ip.rbi_inputs.add(new Indicator_Input(rbiid, otherVars, varValues));
	            		}
	            		
	            		ip = defRBIParams4.defineTSParams(ip,con); 
	            		ip = getTS.getOneRBITSIP(ip, con, 0);   
	            		ip = defRBIParams6.defineTSAnalysisParams(ip);
	            		
	            		System.out.println(ip.cp+ip.out+ip.trend+ip.cpmType+ip.ARL0+ip.startup+ip.method+ip.period);
	            		
	            		if(ip.cp==1)
	            		{getCP.getOneTSCP(ip,con,ip.rbi_ts,re);}	
	            		
	            		if(ip.out==1)
	            		{getOut.getOneTSOut(ip,con,ip.rbi_ts,re);}
	            		
	            		if(ip.trend==1)
	            		{getTrend.getOneTSTrend(ip,con,ip.rbi_ts,re);}
	              		
	            		cMain.gridwidth = 1;
	            		cMain.gridheight = 1;
	            		cMain.ipadx = 1100;
	            		cMain.ipady = 500;       
	            		cMain.gridx = 0;
	            		cMain.gridy = 0;
	            				
	            		mainPane.add(visRes.plotTSDisplayNew(ip, con, ip.rbi_ts, 0), cMain);
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            		
	           
	                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );	
		
			SlickerButton compareRBIbut=new SlickerButton();
			compareRBIbut.setText("Compare with others");

			compareRBIbut.addActionListener(
		            new ActionListener(){
		                public void actionPerformed(
		                        ActionEvent e) 
		                {try{
		                	mainPane.removeAll(); 
		                	mainPane.setPreferredSize(new Dimension(widthMain, height));
		            		mainPane.updateUI();
		            		mainPane.revalidate();
		            		mainPane.repaint();
		            
		            		
		                	DefineRBIParams defRBIParams = new DefineRBIParams();
		            		DefineRBIParams defRBIParams2 = new DefineRBIParams();
		            		DefineRBIParams defRBIParams3 = new DefineRBIParams();
		            		DefineRBIParams defRBIParams4 = new DefineRBIParams();
		              		
		             		VisualiseResults visRes = new VisualiseResults();
		            		
		            		GetTimeSeries getTS = new GetTimeSeries();
		            		CompareTS compTS = new CompareTS();
		            		InputParameters ip = new InputParameters();
		            		ip.setDB(host, user, pass, db);
		            		            		
		            		Vector<String> rbi = new Vector<String>();
		            		rbi.addAll(defRBIParams.selectRBI(con));
		            		Vector<String> vars = new Vector<String>();
		            		
		            		vars.addAll(defRBIParams2.selectRBIVars(con,rbi));
		            		
		            		if(vars.size()>0)
		            		{ip = defRBIParams3.defineVars(con, ip, vars, rbi);}else
		            		{
		            			vars.add("R1");
		            		}
		            		
		            		ip = defRBIParams4.defineTSParams(ip,con); 
		            		ip = getTS.getOneRBITSIP(ip, con, 0);   
		            		int ts_id = ip.rbi_ts;
		            		getTS.getOneRBITSMean (ip, con, 0);
		            		double pvalue = compTS.compareTS ( ip,  con, ts_id, re);
		            		
		                   		
		            		cMain.gridwidth = 1;
		            		cMain.gridheight = 1;
		            		cMain.ipadx = imgW;
		            		cMain.ipady = imgH;       
		            		cMain.gridx = 0;
		            		cMain.gridy = 0;
		            			
		            		mainPane.add(visRes.plotCompareTS(ip, con, ip.rbi_ts, pvalue), cMain);
		            		mainPane.revalidate();
		            		mainPane.repaint();
		            		
		             
		                }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);};
		                }
		                				
		                                }
		                        );	
		

		
			SlickerButton analyseRegbut=new SlickerButton();
			analyseRegbut.setText("Regression Analysis");

			analyseRegbut.addActionListener(
		            new ActionListener(){
		                public void actionPerformed(
		                        ActionEvent e) 
		                {try{
		                	
		                	mainPane.removeAll(); 
		                	mainPane.setPreferredSize(new Dimension(widthMain, height));
		            		mainPane.updateUI();
		            		mainPane.revalidate();
		            		mainPane.repaint();
		            
		           
		                	DefineRegressionParams defRegParams = new DefineRegressionParams();
		                	DefineRegressionParams defRegParams2 = new DefineRegressionParams();
		                	DefineRegressionParams defRegParams3 = new DefineRegressionParams();
		                	DefineRegressionParams defRegParams4 = new DefineRegressionParams();
		                	DefineRegressionParams defRegParams5 = new DefineRegressionParams();
		                	DefineRegressionParams defRegParams6 = new DefineRegressionParams();
		                  	DefineRegressionParams defRegParams7 = new DefineRegressionParams();
		           		
		            		InputParameters ip = new InputParameters();
		            		GetRegression getreg = new GetRegression();
		            		GetRegressionData getregdata = new GetRegressionData();
		            		
		             		ip = defRegParams.selectPerspective(ip); 
		            		System.out.println(ip.regType+ip.numberExpVars);
		              		
		            		Vector<String> regviews = new Vector<String>();
		            		
		            		if(ip.regType.equals("case"))
		            		{	
		            			regviews = defRegParams2.selectCaseViews(con,ip);
		            			System.out.println(regviews);
		            	    }
		            		if(ip.regType.equals("task"))
		            		{
		            			regviews = defRegParams3.selectTaskViews(con,ip);
		            			System.out.println(regviews);
		            		}
		            		if(ip.regType.equals("time"))
		            		{
		            			regviews = defRegParams4.selectTimeViews(con,ip);
		            			System.out.println(regviews);
		            			ip = defRegParams5.defineTSParams(ip,con);
		                            
		            		}
		            		
		            		Vector<String> usedvars = new Vector<String>();
		            		usedvars = defRegParams6.selectRegressionVars(con, regviews);
		            		System.out.println(usedvars);
		            		
		            		if(usedvars.size()>0)
		            		{ip = defRegParams7.defineVars(con, ip, usedvars, regviews);}else
		            		{
		            		for(int i=0; i<regviews.size(); i++)
		                   	{ip.regressionInputs.add(new RegressionData("regview"+regviews.elementAt(i),usedvars,usedvars));}
		                    ip.decNum = 3;
		            		}
		            		
		              		Vector<String> ExpVars = new Vector<String>();	
		            		String depVar = "";
		            		if(ip.regType.equals("case"))
		            		{	
		            			
		            			depVar = getregdata.getCaseRegData(ip, con, 1, getregdata.getCases(ip, con, 0));
		            			System.out.println(depVar);
			            		for(int i=0; i<ip.numberExpVars;i++)
		            			{ExpVars.add(getregdata.getCaseRegData(ip, con, i+2, getregdata.getCases(ip, con, 0)));}
		    	            }
		            		if(ip.regType.equals("task"))
		            		{
		            			depVar = getregdata.getTaskRegData(ip, con, 1, getregdata.geteventids(ip, con, 0));
		            			for(int i=0; i<ip.numberExpVars;i++)
			            		{ExpVars.add(getregdata.getTaskRegData(ip, con, i+2, getregdata.geteventids(ip, con, 0)));}
			            	
		            		}
		            		if(ip.regType.equals("time"))
		            		{
		            			depVar = getregdata.getTimeRegData(ip, con, 0);
		            			for(int i=0; i<ip.numberExpVars;i++)
			            		{ExpVars.add(getregdata.getTimeRegData(ip, con, i+1));}
			            	
		            		}
		            	
		            		String filename = "regressionTest";
		            		double[] regRes = null;
		            		Vector<String> regResNPMult = new Vector<String>();
		            		
		            		if(ip.regressionMethod.equalsIgnoreCase("non-parametric") && ExpVars.size()>1)
		            		{
		            			regResNPMult = getreg.getRegressionNewNPMult(ip, con, re, depVar, ExpVars, filename);
		            		}
		            		else
		            		{
		            			regRes = getreg.getRegressionNew(ip, con, re, depVar, ExpVars, filename);
		            		}
		            	
		            	int regIMGH = (int) (1.4*imgH);
		            	int regIMGW = (int) (1.6*imgH);
		            	
		            	//Linear regression
		             	if(ip.regressionMethod.equalsIgnoreCase("linear"))
		            	{	
		            		if(ExpVars.size() == 1)
		            		{
			             		cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = regIMGW;
			            		cMain.ipady = regIMGH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(getreg.visualiseRegOne(depVar, ExpVars.elementAt(0), regRes), cMain);
			            		mainPane.revalidate();
			            		mainPane.repaint();
			          		} 
		            		else
		            		{
			                	cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = regIMGW;
			            		cMain.ipady = regIMGH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(getreg.visualiseRegMult(depVar, ExpVars.elementAt(0), regRes), cMain);
			            		mainPane.revalidate();
			            		mainPane.repaint();
			            		mainPane.revalidate();
			            		mainPane.repaint();
			            	}
		            	}
		             	
		             	//visualize non-parametric
		             	if(ip.regressionMethod.equalsIgnoreCase("non-parametric"))
		            	{
		             		if(ExpVars.size() == 1)
		            		{
			             		cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = regIMGW;
			            		cMain.ipady = regIMGH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(getreg.visualiseNPRegOne(depVar, ExpVars.elementAt(0), regRes), cMain);
			            		
			            		mainPane.revalidate();
			            		mainPane.repaint();
			          		} 
		            		else
		            		{
			                	cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = regIMGW;
			            		cMain.ipady = regIMGH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(getreg.visualiseNPRegMult(regResNPMult), cMain);
			            		
			            		mainPane.revalidate();
			            		mainPane.repaint();
			            		mainPane.revalidate();
			            		mainPane.repaint();
			            	}
		            	}
		            		
		    
		                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);};
		                }
		                				
		                                }
		                        );
			
			
			SlickerButton DEAbut=new SlickerButton();
			DEAbut.setText("Evaluate productivity");
	
			DEAbut.addActionListener(
		            new ActionListener(){
		                public void actionPerformed(
		                        ActionEvent e) 
		                {try{
		                	mainPane.removeAll(); 
		                	mainPane.setPreferredSize(new Dimension(widthMain, height));
		            		mainPane.updateUI();
		            		mainPane.revalidate();
		            		mainPane.repaint();
		            		
		            		Statement dbStatement = con.createStatement();
		    				dbStatement.executeUpdate("delete FROM deaints");
		    				dbStatement.executeUpdate("delete FROM deaoutts");
		    				dbStatement.close();
	                		
		                	InputParameters ip = new InputParameters();
		                	DefineDEAParams defDeaParam = new DefineDEAParams();
		                	DefineDEAParams defDeaParam2 = new DefineDEAParams();
		                	DefineDEAParams defDeaParam3 = new DefineDEAParams();
		                	DefineDEAParams defDeaParam4 = new DefineDEAParams();
		                 	DefineDEAParams defDeaParam5 = new DefineDEAParams();
		                  	GetDEAInOut getdea = new GetDEAInOut();
		                	GetDEAProductivity getdeaeff = new GetDEAProductivity();
    	
		                	ip = defDeaParam.getDEAType(ip);
		                	
		                	if (ip.deatype.equalsIgnoreCase("ormt"))
		                	{
		                		ip = defDeaParam2.defineTSParamsResORMT(ip,con);
			       	                			                		
		                	}
		                	if (ip.deatype.equalsIgnoreCase("mrot"))
		                	{
		                		ip = defDeaParam3.defineTSParamsResMROT(ip,con);
			                	
		       	       	}
		                	if (ip.deatype.equalsIgnoreCase("mrmt"))
		                	{
		                		ip = defDeaParam4.defineTSParamsResMRMT(ip,con);
			                	
		       	          	}
		               
		                	ip = defDeaParam5.selectMultDEAInOut(con, ip);
			            	
		            		for (int i=0; i<ip.DEA_Inputs.size(); i++)
		            		{
		            			getdea.storeOneInputTS(ip, con, i);
		            			
		            		}
		            	
		            		for (int i=0; i<ip.DEA_Outputs.size(); i++)
		            		{
		            			getdea.storeOneOutputTS(ip, con, i);
		            		}
		            		
		            		String outcost = getdeaeff.getOutputCost(ip, con);
		            		//System.out.println("outcost: "+outcost);
		            		
		            		String incost = getdeaeff.getInputCost(ip, con);
		            		//System.out.println("incost: "+incost);
		            		
		            		            		
		            		if (ip.deatype.equalsIgnoreCase("ormt"))
		                	{
		                		             		
		            			String in = getdeaeff.getInOneResMultTime(ip,con);
			            		String out = getdeaeff.getOutOneResMultTime(ip,con);
			            		//System.out.println(in);
			            		//System.out.println(out);
			             				
			            		Vector<String> resProd = new Vector<String>();
			            		
			            		if(ip.usecosts && !ip.useInputCosts)
			            		resProd.add(getdeaeff.getRevenueDEA(ip, con, re, in, out, outcost));
			            		
			            		if(!ip.usecosts && ip.useInputCosts)
				            	resProd.add(getdeaeff.getCostDEA(ip, con, re, in, out, incost));
				            	
			            		if(ip.usecosts && ip.useInputCosts)
				            	resProd.add(getdeaeff.getProfitDEA(ip, con, re, in, out, incost, outcost));
				        		
			            		if(!ip.usecosts && !ip.useInputCosts)	
			            		{resProd.add(getdeaeff.getDEA(ip, con, re, in, out));}
			             	
			            		
			            		//System.out.println(resProd.elementAt(0));
			            		
			            		VisualiseResults vis = new VisualiseResults();
						        cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = imgW;
			            		cMain.ipady = imgH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(vis.plotMultiTSfromStrings(ip, resProd, "Productivity"), cMain);
			            		mainPane.revalidate();
			            		mainPane.repaint();
					        	
			        		                		
		                	}
		                	if (ip.deatype.equalsIgnoreCase("mrot"))
		                	{
		                 		
		                 		String out = getdeaeff.getOutMultResOneTime(ip,con);
			            		//System.out.println(out);
			            		String in = getdeaeff.getInMultResOneTime(ip,con);
			            		//System.out.println(in);
			            		
			            		String prod = "";
			            			
			            		if(ip.usecosts && !ip.useInputCosts)
				            	prod = getdeaeff.getRevenueDEA(ip, con, re, in, out, outcost);
				            		
				            	if(!ip.usecosts && ip.useInputCosts)
				            	prod = getdeaeff.getCostDEA(ip, con, re, in, out, incost);
					            	
				            	if(ip.usecosts && ip.useInputCosts)
				            	prod = getdeaeff.getProfitDEA(ip, con, re, in, out, incost, outcost);
					        		
				            	if(!ip.usecosts && !ip.useInputCosts)	
				            	prod = getdeaeff.getDEA(ip, con, re, in, out);
				             
			            		VisualiseResults vis = new VisualiseResults();
						        cMain.gridwidth = 1;
			            		cMain.gridheight = 1;
			            		cMain.ipadx = imgW;
			            		cMain.ipady = imgH;       
			            		cMain.gridx = 0;
			            		cMain.gridy = 0;
			            			
			            		mainPane.add(vis.getBarChart(ip, prod), cMain);
			            		mainPane.revalidate();
			            		mainPane.repaint();
			            
		        	       	}
		                	
		                	if (ip.deatype.equalsIgnoreCase("mrmt"))
		                	{
		                  		
		                    	
		                		String in = getdeaeff.getInMultResMultTime(ip,con);
			            		//System.out.println(in);
			            		String out = getdeaeff.getOutMultResMultTime(ip,con);
			            		//System.out.println(out);
								
								String allProd = "";
								
								if(ip.usecosts && !ip.useInputCosts)
								allProd = getdeaeff.getRevenueDEA(ip, con, re, in, out, outcost);
					            		
					            if(!ip.usecosts && ip.useInputCosts)
					            allProd = getdeaeff.getCostDEA(ip, con, re, in, out, incost);
						            	
					            if(ip.usecosts && ip.useInputCosts)
					            allProd = getdeaeff.getProfitDEA(ip, con, re, in, out, incost, outcost);
						        		
					            if(!ip.usecosts && !ip.useInputCosts)	
					            allProd = getdeaeff.getDEA(ip, con, re, in, out);
					    		
								//System.out.println("DEA Productivity: " + allProd);
									
								Vector<String> resProd = new Vector<String>();
			            		VisualiseResults vis = new VisualiseResults();
			            		
			            		resProd.addAll(vis.splitString(ip, allProd));
			            		//System.out.println(resProd);
			            		
			            		cMain.gridwidth = 1;
				            	cMain.gridheight = 1;
				            	cMain.ipadx = imgW;
				            	cMain.ipady = imgH;       
				            	cMain.gridx = 0;
				            	cMain.gridy = 0;
				            			
				            	mainPane.add(vis.plotMultiTSfromStrings(ip, resProd, "Productivity"), cMain);
				            	mainPane.revalidate();
				            	mainPane.repaint();
				  			     
			      
			       	        }
			        	  	
		                      }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		 	                         JOptionPane.ERROR_MESSAGE);};
		                }
		                				
		                                }
		                        );
	
	SlickerButton defnewrbibut=new SlickerButton();
	defnewrbibut.setText("Define new RBI");
	
	
	defnewrbibut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
                	mainPane.removeAll(); 
            		mainPane.updateUI();
            		mainPane.revalidate();
            		mainPane.repaint();
            
            		
                	DefineRBI defRBI = new DefineRBI();
             	   defRBI.addIndicator(con);
                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );
	
	
	SlickerButton defregviewbut=new SlickerButton();
	defregviewbut.setText("Define new regression view");
	
	defregviewbut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
                	mainPane.removeAll(); 
            		mainPane.updateUI();
            		mainPane.revalidate();
            		mainPane.repaint();
            
            		
                	DefineRegressionVars defRegVar = new DefineRegressionVars();
                	defRegVar.addRegressionView(con);
                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );
	
		
		SlickerButton defnewdeainbut=new SlickerButton();
		defnewdeainbut.setText("Define new DEA Input");
	
		defnewdeainbut.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	mainPane.removeAll(); 
	            		mainPane.updateUI();
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            	            		
	                	DefineDEAViews defdea = new DefineDEAViews();
	                	defdea.defineDEAInput(con);
	                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );
		
		SlickerButton defnewdeaoutbut=new SlickerButton();
		defnewdeaoutbut.setText("Define new DEA output");
	
		defnewdeaoutbut.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) 
	                {try{
	                	mainPane.removeAll(); 
	            		mainPane.updateUI();
	            		mainPane.revalidate();
	            		mainPane.repaint();
	            	            		
	                	DefineDEAViews defdea = new DefineDEAViews();
	                	defdea.defineDEAOutput(con);
	                      }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	 	                         JOptionPane.ERROR_MESSAGE);};
	                }
	                				
	                                }
	                        );
		
		
menuPane.add(importlab);
menuPane.add(createdbbut);
menuPane.add(lab0);

menuPane.add(analyzelab);
menuPane.add(analyseProfilebut);
menuPane.add(analyseOneRBIMultResbut);
menuPane.add(analyseOneRBIbut);
menuPane.add(compareRBIbut);
menuPane.add(analyseRegbut);
menuPane.add(DEAbut);

menuPane.add(lab1);
menuPane.add(definelab);
menuPane.add(defnewrbibut);
menuPane.add(defregviewbut);
menuPane.add(defnewdeainbut);
menuPane.add(defnewdeaoutbut);

c.gridwidth = 1;
c.gridheight = 1;
c.ipadx = widthMenu;
c.ipady = height;
c.gridx = 0;
c.gridy = 0;
pane.add(menuPane,c);	

c.gridwidth = 1;
c.gridheight = 1;
c.ipadx = widthMain;
c.ipady = height;       
c.gridx = 1;
c.gridy = 0;
pane.add(scrollPane,c);	
   
   return pane;
   } 

}




