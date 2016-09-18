package org.processmining.plugins.miningresourceprofiles.inputs;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;
import org.processmining.framework.util.ui.widgets.ProMCheckComboBox;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;
@SuppressWarnings("serial")
public class DefineDEAParams extends JDialog{
	

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector<String> defineDEAVars(final Connection con, final InputParameters ip, final Vector<String> vars) throws Exception
   {
	
	Vector<String> allTasks = new Vector<String>();

	Statement dbStatement2 = con.createStatement();
	String sqlQuery2 = "SELECT distinct task from eventlog";
	ResultSet rs2 = dbStatement2.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{	
	String task = rs2.getString("task");
	allTasks.add(task);
	}


	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;

	
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify variable values for the selected input/output: </h3></html>");
	c.ipadx = 400;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<ProMTextField> varvalues = new Vector<ProMTextField>();
	final Vector<String> otherVars = new Vector<String>();

	//final boolean R1;
	final boolean Task;
	
	if(vars.contains("Task")){Task = true;}else{Task = false;};
	int count = 0;
	
	System.out.println("Vars: "+vars);
	System.out.println("Task: "+Task);
	
	for(int i=0; i<vars.size(); i++)
	{
		if(!(vars.elementAt(i).equals("Task")))
		{
		otherVars.add(vars.elementAt(i));	
		varnames.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+vars.elementAt(i)+"</html>"));  
		varnames.elementAt(count).setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = i+1;
		pane.add(varnames.elementAt(count), c);
	
		varvalues.add(new ProMTextField());
		c.gridx = 1;
		c.gridy = i+1;
		c.ipadx = 200;
		pane.add(varvalues.elementAt(count), c);
		
		count++;
		}
	}	
		
	DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	for(int j=0; j<allTasks.size(); j++)
	{
		model2.addElement(allTasks.elementAt(j));
	}
    final ProMComboBox jopTask = new ProMComboBox(model2);

	if(Task)
	{	otherVars.add("Task");		
	
	JLabel Tasklab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task</html>");  
	Tasklab.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = count+2;
	pane.add(Tasklab, c);

	 	c.gridx = 1;
		c.gridy = count+2;
		c.ipadx = 200;
		pane.add(jopTask, c);
count++;
	}
   
	
	SlickerButton but=new SlickerButton();
	but.setText("Next");
	c.gridx = 0;
	c.gridy = count+2;
	c.gridwidth = 2;
	pane.add(but, c);
      
	final Vector<String> varValues = new Vector<String>();
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                 	  
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                 	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                	  
                	  System.out.println("varValues: "+varValues);
                	           
                      dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return varValues;
   } 

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> defineDEAVarsCost(final Connection con, final InputParameters ip, final Vector<String> vars) throws Exception
   {
	
	Vector<String> allTasks = new Vector<String>();

	Statement dbStatement2 = con.createStatement();
	String sqlQuery2 = "SELECT distinct task from eventlog";
	ResultSet rs2 = dbStatement2.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{	
	String task = rs2.getString("task");
	allTasks.add(task);
	}

	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;

	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify variable values and/or cost of the selected input/output: </h3></html>");
	c.ipadx = 400;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<ProMTextField> varvalues = new Vector<ProMTextField>();
	final Vector<String> otherVars = new Vector<String>();

	//final boolean R1;
	final boolean Task;
	
	if(vars.contains("Task")){Task = true;}else{Task = false;};
	int count = 0;
	
	System.out.println("Vars: "+vars);
	System.out.println("Task: "+Task);
	
	for(int i=0; i<vars.size(); i++)
	{
		if(!(vars.elementAt(i).equals("Task")))
		{
		otherVars.add(vars.elementAt(i));	
		varnames.add(new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+vars.elementAt(i)+"</html>"));  
		varnames.elementAt(count).setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = i+1;
		pane.add(varnames.elementAt(count), c);
	
		varvalues.add(new ProMTextField());
		c.gridx = 1;
		c.gridy = i+1;
		c.ipadx = 200;
		pane.add(varvalues.elementAt(count), c);
		
		count++;
		}
	}	
		
	DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	for(int j=0; j<allTasks.size(); j++)
	{
		model2.addElement(allTasks.elementAt(j));
	}
    final ProMComboBox jopTask = new ProMComboBox(model2);

	if(Task)
	{	otherVars.add("Task");		
	
	JLabel Tasklab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task</html>");  
	Tasklab.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = count+2;
	pane.add(Tasklab, c);
	
	 	c.gridx = 1;
		c.gridy = count+2;
		c.ipadx = 200;
		pane.add(jopTask, c);
count++;
	}
   
	JLabel costlab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cost:</html>");  
	costlab.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = count+2;
	pane.add(costlab, c);

	final ProMTextField costval = new ProMTextField("1");
	c.gridx = 1;
	c.gridy = count+2;
	c.ipadx = 200;
	pane.add(costval, c);
	
	SlickerButton but=new SlickerButton();
	but.setText("Next");
	c.gridx = 0;
	c.gridy = count+3;
	c.gridwidth = 2;
	pane.add(but, c);
      
	final Vector<String> varValues = new Vector<String>();
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
               	  
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                 	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                	  
                	 varValues.add(0,costval.getText());
                	  
                	  System.out.println("varValues: "+varValues);
                	           
                      dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return varValues;
   } 


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public InputParameters selectMultDEAInOut(final Connection con, final InputParameters ip) throws Exception
   {
	final Vector<String> selIn = new Vector<String>();	
	final Vector<String> selOut = new Vector<String>();	
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;

	final JLabel selectedRBIs = new JLabel();
	selectedRBIs.setOpaque(true);
	selectedRBIs.setForeground(UISettings.Text_COLOR);
	selectedRBIs.setBackground(UISettings.BG_COLOR);
	
	selectedRBIs.setText("<html>&nbsp; &nbsp; &nbsp; &nbsp;Please select resource inputs and outputs to be considered when evaluating productivity</html>");
	
	ProMScrollPane scrollPane = new ProMScrollPane(selectedRBIs);
	c.ipadx = 800;
	c.ipady = 400;     
	c.fill = JScrollPane.WIDTH;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(scrollPane, c);
	
	final Vector<String> ids = new Vector<String>();
	final Vector<String> names = new Vector<String>();
	final Vector<String> definitions = new Vector<String>();

	
	Statement dbStatement = con.createStatement();

	String sqlQuery = "SELECT * FROM deainput";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	ids.add(rs.getString("id"));
	names.add(rs.getString("name"));
	definitions.add(rs.getString("definition"));
	}
		
	final JLabel SelectText=new JLabel();
	SelectText.setForeground(UISettings.TextLight_COLOR);
	SelectText.setText("<html>&nbsp; &nbsp;Select resource input: </html>");
	c.ipadx = 800; 
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 1;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final ProMComboBox jop = new ProMComboBox(model);
     c.ipady = 20; 
     c.gridx = 0;
 	 c.gridy = 2;
 	 pane.add(jop, c);

		
	SlickerButton but=new SlickerButton();
	but.setText("Add input");
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 3;
	pane.add(but, c);
     
   but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   //	Statement dbStatement;
                 	try {
                 			
                 			Vector<String> deaIn = new Vector <String>();	

   							String selectedRBIName = (String) jop.getSelectedItem();
							String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
							String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
							
							deaIn.add("input"+selectedRBIID);
							deaIn.add(selectedRBIName);
							deaIn.add(selectedRBIDefinition);
							
							DefineDEAParams defDEAParams = new DefineDEAParams();
							DefineRBIParams defRBIParams = new DefineRBIParams();
				            
			            	Vector<String> vars = new Vector<String>();
			             	Vector<String> varValues = new Vector<String>();
			            	
		            		vars.addAll(defRBIParams.selectRBIVars(con,deaIn));
		            		if(vars.contains("R1"))
		            		{vars.remove("R1");}
		            		
		            	 	
		            		double deacost = 1.0;		            		
		            		if(ip.useInputCosts)
		            		{varValues.addAll(defDEAParams.defineDEAVarsCost(con, ip, vars));
		            		deacost = Double.parseDouble(varValues.elementAt(0));
		            		varValues.remove(0);}else
		            		{
		            			if(vars.size()>0)
		            			{
		            				varValues.addAll(defDEAParams.defineDEAVars(con, ip, vars));
		            			}
		            		}	
		            		
		                  	for (int j=0; j<ip.resources.size();j++)
		      				{ip.DEA_Inputs.add(new DEA_InOut(deaIn.elementAt(0),vars,varValues,ip.resources.elementAt(j),deacost));}
		                 		
							selIn.add(selectedRBIName+"<br/>");
							
							String theText = "<html>Inputs:<br/>";
							for(int i=0; i<selIn.size(); i++)
							{
								theText += selIn.elementAt(i);
							}
							theText += "<html>Outputs:<br/>";
							for(int i=0; i<selOut.size(); i++)
							{
								theText += selOut.elementAt(i);
							}
							theText += "</html>";
							
							selectedRBIs.setText(theText);
					       	pane.validate();
							pane.repaint();
		           
							
						}catch (Exception e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);};
                                                }
                                   }
                           );
       
	final Vector<String> idsout = new Vector<String>();
	final Vector<String> namesout = new Vector<String>();
	final Vector<String> definitionsout = new Vector<String>();

	
	Statement dbStatementout = con.createStatement();

	String sqlQueryout = "SELECT * FROM deaoutput";
	
	ResultSet rsout = dbStatementout.executeQuery(sqlQueryout);
	rsout.beforeFirst();
	
	while(rsout.next())
	{	
	idsout.add(rsout.getString("id"));
	namesout.add(rsout.getString("name"));
	definitionsout.add(rsout.getString("definition"));
	}
		
	final JLabel SelectTextout=new JLabel();
	SelectTextout.setForeground(UISettings.TextLight_COLOR);
	SelectTextout.setText("<html>&nbsp; &nbsp;Select resource output: </html>");
	c.ipadx = 800; 
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 4;
	pane.add(SelectTextout, c);
	
		
	 DefaultComboBoxModel modelout = new DefaultComboBoxModel();
	 for(int i=0; i<namesout.size(); i++)
	 {
		 modelout.addElement(namesout.elementAt(i));
	 }
     final ProMComboBox jopout = new ProMComboBox(modelout);
     c.ipady = 20; 
     c.gridx = 0;
 	 c.gridy = 5;
 	 pane.add(jopout, c);

		
	SlickerButton butout=new SlickerButton();
	butout.setText("Add output");
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 6;
	pane.add(butout, c);
      
    butout.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                 	try {
                 			
                 			Vector<String> deaout = new Vector <String>();	
							
							String selectedRBIName = (String) jopout.getSelectedItem();
							String selectedRBIID = idsout.elementAt(namesout.indexOf(selectedRBIName));
							String selectedRBIDefinition = definitionsout.elementAt(namesout.indexOf(selectedRBIName));
							
							deaout.add("output"+selectedRBIID);
							deaout.add(selectedRBIName);
							deaout.add(selectedRBIDefinition);
							
							DefineDEAParams defDEAParams = new DefineDEAParams();
							DefineRBIParams defRBIParams = new DefineRBIParams();
				            
			            	Vector<String> vars = new Vector<String>();
			             	Vector<String> varValues = new Vector<String>();
			            	
		            		vars.addAll(defRBIParams.selectRBIVars(con,deaout));
		            		if(vars.contains("R1"))
		            		{vars.remove("R1");}
		            		
		            		double deacost = 1.0;
		            		
		            		if(ip.usecosts)
		            		{varValues.addAll(defDEAParams.defineDEAVarsCost(con, ip, vars));
		            		deacost = Double.parseDouble(varValues.elementAt(0));
		            		varValues.remove(0);}else
		            		{
		            			if(vars.size()>0)
		            			{
		            				varValues.addAll(defDEAParams.defineDEAVars(con, ip, vars));
		            			}
		            		}	
		            		
		                	for (int j=0; j<ip.resources.size();j++)
		      				{ip.DEA_Outputs.add(new DEA_InOut(deaout.elementAt(0),vars,varValues,ip.resources.elementAt(j),deacost));}
		                 		
							selOut.add(selectedRBIName+"<br/>");
							
							String theText = "<html>Inputs:<br/>";
							for(int i=0; i<selIn.size(); i++)
							{
								theText += selIn.elementAt(i);
							}
							theText += "<html>Outputs:<br/>";
							for(int i=0; i<selOut.size(); i++)
							{
								theText += selOut.elementAt(i);
							}
							theText += "</html>";
							
							selectedRBIs.setText(theText);
							
							selectedRBIs.setText(theText);
					       	pane.validate();
							pane.repaint();
		           
							
						}catch (Exception e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);};
                                                }
                                   }
                           );
       
    
    
	SlickerButton okbut=new SlickerButton();
	okbut.setText("Submit");
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 7;
	pane.add(okbut, c);
     
    	
	okbut.addActionListener(
              new ActionListener(){
                  public void actionPerformed(
                        ActionEvent e) {
          				dispose();} }
                          );
       
       setSize(1040,785);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSParamsResORMT(final InputParameters ip, final Connection con) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Specify time series settings: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("Start time:");  
	lab0.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab0, c);
	
		Statement dbStatementStart = con.createStatement();
		String sqlQueryStart = "SELECT min(time) from eventlog";
		ResultSet rsStart = dbStatementStart.executeQuery(sqlQueryStart);
		rsStart.beforeFirst();
		rsStart.next();
		Timestamp starttime = rsStart.getTimestamp(1);

		long timestamp = starttime.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		Integer syear = cal.get(Calendar.YEAR);
		Integer smonth = cal.get(Calendar.MONTH)+1;
		Integer sday = cal.get(Calendar.DATE);
		Integer shour = cal.get(Calendar.HOUR_OF_DAY);
		Integer smin = cal.get(Calendar.MINUTE);
		Integer ssec = cal.get(Calendar.SECOND);
		
		String iyear = syear.toString(); 
		String imonth = smonth.toString(); 
		String iday = sday.toString(); 
		String ihour = shour.toString(); 
		String imin = smin.toString(); 
		String isec = ssec.toString(); 
		
		Statement dbStatementEnd = con.createStatement();
		String sqlQueryEnd = "SELECT max(time) from eventlog";
		ResultSet rsEnd = dbStatementEnd.executeQuery(sqlQueryEnd);
		rsEnd.beforeFirst();
		rsEnd.next();
		Timestamp endtime = rsEnd.getTimestamp(1);
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
		double numWeeks = log_duration/1000/60/60/24/7;
		Long weeks = (long) numWeeks+1;

	final JTextField year=new JTextField(iyear,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 3;
	c.gridy = 1;
	pane.add(year, c);
	
	final JLabel lab1 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab1.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 4;
	c.gridy = 1;
	pane.add(lab1, c);
		
	final JTextField month=new JTextField(imonth,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 5;
	c.gridy = 1;
	pane.add(month, c);
	
	final JLabel lab2 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab2.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 6;
	c.gridy = 1;
	pane.add(lab2, c);
			
	final JTextField day=new JTextField(iday,50);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 7;
	c.gridy = 1;
	pane.add(day, c);
	
	final JLabel lab3 = new JLabel("<html><h3>&nbsp;&nbsp;.&nbsp;&nbsp;</h3></html>"); 
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 8;
	c.gridy = 1;
	pane.add(lab3, c);

	final JTextField hour=new JTextField(ihour,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 9;
	c.gridy = 1;
	pane.add(hour, c);
	
	final JLabel lab5 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");
	lab5.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 10;
	c.gridy = 1;
	pane.add(lab5, c);
		
	final JTextField minute=new JTextField(imin,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 11;
	c.gridy = 1;
	pane.add(minute, c);
	
	final JLabel lab6 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");  
	lab6.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 12;
	c.gridy = 1;
	pane.add(lab6, c);

	final JTextField second=new JTextField(isec,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 13;
	c.gridy = 1;
	pane.add(second, c);
	
			
	final JLabel lab7 = new JLabel("Slot size:");  
	lab7.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab7, c);
		
	final ProMTextField tslotnum = new ProMTextField("1");
	c.ipadx = 30;
	c.gridwidth = 5;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(tslotnum, c);
			
		
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("week");
	model.addElement("month");
	model.addElement("year");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("minute");
	model.addElement("second");
	   
	final ProMComboBox tslotunit = new ProMComboBox(model);
	c.ipadx = 30;
	c.gridwidth = 6;
	c.gridx = 8;
	c.gridy = 2;
	pane.add(tslotunit, c);

		
	final JLabel lab8 = new JLabel("Number of time slots:"); 
	lab8.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 3;
	pane.add(lab8, c);
		
	final ProMTextField numslots = new ProMTextField(weeks.toString());
	c.ipadx = 30;
	c.gridwidth = 6;
	c.gridx = 5;
	c.gridy = 3;
	pane.add(numslots, c);
	
	tslotnum.getDocument().addDocumentListener(new DocumentListener()
    {
			   public void changedUpdate(DocumentEvent e) {
        	   long unit = 0;
        	   int slotsize = Integer.parseInt(tslotnum.getText());
        	   String slotunit = (String) tslotunit.getSelectedItem();
        	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
        	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
        	   else if(slotunit.equals("week")){unit = 604800000l;}
        	   else if(slotunit.equals("day")){unit = 604800000l/7;}
        	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
        	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
        	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
        	   
        	   double numWeeks = log_duration/(unit*slotsize);
        	   Long weeks = (long) numWeeks+1;

        	   numslots.setText(weeks.toString());
        	   pane.revalidate();
        	   pane.repaint();
       	
        }
			   public void insertUpdate(DocumentEvent e) {
	        	   long unit = 0;
            	   int slotsize = Integer.parseInt(tslotnum.getText());
            	   String slotunit = (String) tslotunit.getSelectedItem();
            	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
            	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
            	   else if(slotunit.equals("week")){unit = 604800000l;}
            	   else if(slotunit.equals("day")){unit = 604800000l/7;}
            	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
            	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
            	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
            	   
            	   double numWeeks = log_duration/(unit*slotsize);
            	   Long weeks = (long) numWeeks+1;

            	   numslots.setText(weeks.toString());
            	   pane.revalidate();
            	   pane.repaint();
           	
            }
        
			   public void removeUpdate(DocumentEvent e) {
				   
				   if(!tslotnum.getText().equals(""))
			   	   {long unit = 0;
            	   int slotsize = Integer.parseInt(tslotnum.getText());
            	   String slotunit = (String) tslotunit.getSelectedItem();
            	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
            	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
            	   else if(slotunit.equals("week")){unit = 604800000l;}
            	   else if(slotunit.equals("day")){unit = 604800000l/7;}
            	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
            	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
            	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
            	   
            	   double numWeeks = log_duration/(unit*slotsize);
            	   Long weeks = (long) numWeeks+1;

            	   numslots.setText(weeks.toString());
            	   pane.revalidate();
            	   pane.repaint();
			   	   }
	           }
     
    });

tslotunit.addActionListener(
    new ActionListener(){
        public void actionPerformed(
                ActionEvent e) {
        	
       	   long unit = 0;
    	   int slotsize = Integer.parseInt(tslotnum.getText());
    	   String slotunit = (String) tslotunit.getSelectedItem();
    	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
    	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
    	   else if(slotunit.equals("week")){unit = 604800000l;}
    	   else if(slotunit.equals("day")){unit = 604800000l/7;}
    	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
    	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
    	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
    	   
    	   double numWeeks = log_duration/(unit*slotsize);
    	   Long weeks = (long) numWeeks+1;

    	   numslots.setText(weeks.toString());
    	   pane.revalidate();
    	   pane.repaint();

        	
        }
        
    });

		Vector<String> allResources = new Vector<String>();
	
		Statement dbStatement1 = con.createStatement();
		String sqlQuery1 = "SELECT distinct resource from eventlog";
		ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
		rs1.beforeFirst();
		
		while(rs1.next())
		{	
		String resource = rs1.getString("resource");
		allResources.add(resource);
		}

		DefaultComboBoxModel model1 = new DefaultComboBoxModel();
		for(int j=0; j<allResources.size(); j++)
		{
			model1.addElement(allResources.elementAt(j));
		}
	    final ProMComboBox jopRes = new ProMComboBox(model1);

			
			JLabel R1lab = new JLabel("Select resource:");  
			R1lab.setForeground(UISettings.TextLight_COLOR);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 4;
			pane.add(R1lab, c);
		
			c.gridx = 5;
			c.gridy = 4;
			c.gridwidth = 6;
			pane.add(jopRes, c);
	

	SlickerButton but=new SlickerButton();
	c.ipadx = 600;
	but.setText("Next");
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 5;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
                		ip.startTime = dt.getMillis();
                		ip.numberOfSlots = Integer.parseInt(numslots.getText());// 	
                		ip.decNum = 3;// 		
    	  	
                	   long unit = 0;
                	   int slotsize = Integer.parseInt(tslotnum.getText());
                	   String slotunit = (String) tslotunit.getSelectedItem();
                	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
                	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
                	   else if(slotunit.equals("week")){unit = 604800000l;}
                	   else if(slotunit.equals("day")){unit = 604800000l/7;}
                	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
                	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
                	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
                	   
                	   ip.resources.add((String) jopRes.getSelectedItem());
                	   ip.slotSize = unit*slotsize;
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize + ip.resources.elementAt(0));
                	   dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSParamsResMRMT(final InputParameters ip, final Connection con) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Specify time series settings: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("Start time:");  
	lab0.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab0, c);
	
		Statement dbStatementStart = con.createStatement();
		String sqlQueryStart = "SELECT min(time) from eventlog";
		ResultSet rsStart = dbStatementStart.executeQuery(sqlQueryStart);
		rsStart.beforeFirst();
		rsStart.next();
		Timestamp starttime = rsStart.getTimestamp(1);

		long timestamp = starttime.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		Integer syear = cal.get(Calendar.YEAR);
		Integer smonth = cal.get(Calendar.MONTH)+1;
		Integer sday = cal.get(Calendar.DATE);
		Integer shour = cal.get(Calendar.HOUR_OF_DAY);
		Integer smin = cal.get(Calendar.MINUTE);
		Integer ssec = cal.get(Calendar.SECOND);
		
		String iyear = syear.toString(); 
		String imonth = smonth.toString(); 
		String iday = sday.toString(); 
		String ihour = shour.toString(); 
		String imin = smin.toString(); 
		String isec = ssec.toString(); 
		
		Statement dbStatementEnd = con.createStatement();
		String sqlQueryEnd = "SELECT max(time) from eventlog";
		ResultSet rsEnd = dbStatementEnd.executeQuery(sqlQueryEnd);
		rsEnd.beforeFirst();
		rsEnd.next();
		Timestamp endtime = rsEnd.getTimestamp(1);
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
		double numWeeks = log_duration/1000/60/60/24/7;
		Long weeks = (long) numWeeks+1;
	
	final JTextField year=new JTextField(iyear,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 3;
	c.gridy = 1;
	pane.add(year, c);
	
	final JLabel lab1 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab1.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 4;
	c.gridy = 1;
	pane.add(lab1, c);
		
	final JTextField month=new JTextField(imonth,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 5;
	c.gridy = 1;
	pane.add(month, c);
	
	final JLabel lab2 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab2.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 6;
	c.gridy = 1;
	pane.add(lab2, c);
			
	final JTextField day=new JTextField(iday,50);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 7;
	c.gridy = 1;
	pane.add(day, c);
	
	final JLabel lab3 = new JLabel("<html><h3>&nbsp;&nbsp;.&nbsp;&nbsp;</h3></html>"); 
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 8;
	c.gridy = 1;
	pane.add(lab3, c);

	final JTextField hour=new JTextField(ihour,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 9;
	c.gridy = 1;
	pane.add(hour, c);
	
	final JLabel lab5 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");
	lab5.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 10;
	c.gridy = 1;
	pane.add(lab5, c);
		
	final JTextField minute=new JTextField(imin,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 11;
	c.gridy = 1;
	pane.add(minute, c);
	
	final JLabel lab6 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");  
	lab6.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 12;
	c.gridy = 1;
	pane.add(lab6, c);

	final JTextField second=new JTextField(isec,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 13;
	c.gridy = 1;
	pane.add(second, c);
	
	final JLabel lab7 = new JLabel("Slot size:");  
	lab7.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab7, c);
		
	final ProMTextField tslotnum = new ProMTextField("1");
	c.ipadx = 30;
	c.gridwidth = 5;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(tslotnum, c);
			
		
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("week");
	model.addElement("month");
	model.addElement("year");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("minute");
	model.addElement("second");
	   
	final ProMComboBox tslotunit = new ProMComboBox(model);
	c.ipadx = 30;
	c.gridwidth = 6;
	c.gridx = 8;
	c.gridy = 2;
	pane.add(tslotunit, c);

		
	final JLabel lab8 = new JLabel("Number of time slots:"); 
	lab8.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 3;
	pane.add(lab8, c);
		
	final ProMTextField numslots = new ProMTextField(weeks.toString());
	c.ipadx = 30;
	c.gridwidth = 6;
	c.gridx = 5;
	c.gridy = 3;
	pane.add(numslots, c);
	
	tslotnum.getDocument().addDocumentListener(new DocumentListener()
    {
			   public void changedUpdate(DocumentEvent e) {
        	   long unit = 0;
        	   int slotsize = Integer.parseInt(tslotnum.getText());
        	   String slotunit = (String) tslotunit.getSelectedItem();
        	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
        	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
        	   else if(slotunit.equals("week")){unit = 604800000l;}
        	   else if(slotunit.equals("day")){unit = 604800000l/7;}
        	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
        	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
        	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
        	   
        	   double numWeeks = log_duration/(unit*slotsize);
        	   Long weeks = (long) numWeeks+1;

        	   numslots.setText(weeks.toString());
        	   pane.revalidate();
        	   pane.repaint();
       	
        }
			   public void insertUpdate(DocumentEvent e) {
	        	   long unit = 0;
            	   int slotsize = Integer.parseInt(tslotnum.getText());
            	   String slotunit = (String) tslotunit.getSelectedItem();
            	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
            	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
            	   else if(slotunit.equals("week")){unit = 604800000l;}
            	   else if(slotunit.equals("day")){unit = 604800000l/7;}
            	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
            	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
            	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
            	   
            	   double numWeeks = log_duration/(unit*slotsize);
            	   Long weeks = (long) numWeeks+1;

            	   numslots.setText(weeks.toString());
            	   pane.revalidate();
            	   pane.repaint();
           	
            }
        
			   public void removeUpdate(DocumentEvent e) {
				   
				   if(!tslotnum.getText().equals(""))
			   	   {long unit = 0;
            	   int slotsize = Integer.parseInt(tslotnum.getText());
            	   String slotunit = (String) tslotunit.getSelectedItem();
            	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
            	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
            	   else if(slotunit.equals("week")){unit = 604800000l;}
            	   else if(slotunit.equals("day")){unit = 604800000l/7;}
            	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
            	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
            	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
            	   
            	   double numWeeks = log_duration/(unit*slotsize);
            	   Long weeks = (long) numWeeks+1;

            	   numslots.setText(weeks.toString());
            	   pane.revalidate();
            	   pane.repaint();
			   	   }
					   
	           }
     
    });

tslotunit.addActionListener(
    new ActionListener(){
        public void actionPerformed(
                ActionEvent e) {
        	
       	   long unit = 0;
    	   int slotsize = Integer.parseInt(tslotnum.getText());
    	   String slotunit = (String) tslotunit.getSelectedItem();
    	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
    	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
    	   else if(slotunit.equals("week")){unit = 604800000l;}
    	   else if(slotunit.equals("day")){unit = 604800000l/7;}
    	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
    	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
    	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
    	   
    	   double numWeeks = log_duration/(unit*slotsize);
    	   Long weeks = (long) numWeeks+1;

    	   numslots.setText(weeks.toString());
    	   pane.revalidate();
    	   pane.repaint();

        	
        }
        
    });

	
		Vector<String> allResources = new Vector<String>();
	
		Statement dbStatement1 = con.createStatement();
		String sqlQuery1 = "SELECT distinct resource from eventlog";
		ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
		rs1.beforeFirst();
		
		while(rs1.next())
		{	
		String resource = rs1.getString("resource");
		allResources.add(resource);
		}

		String[] model1 = new String[allResources.size()];
		
		for(int j=0; j<allResources.size(); j++)
		{
			model1[j] = allResources.elementAt(j);
		}
	 
		
		final ProMCheckComboBox jopRes = new ProMCheckComboBox(model1);

			
			JLabel R1lab = new JLabel("Select resources:");  
			R1lab.setForeground(UISettings.TextLight_COLOR);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 4;
			pane.add(R1lab, c);
		
			c.gridx = 5;
			c.gridy = 4;
			c.gridwidth = 6;
			pane.add(jopRes, c);
	
		
	SlickerButton but=new SlickerButton();
	c.ipadx = 600;
	but.setText("Next");
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 5;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
                		ip.startTime = dt.getMillis();
                		ip.numberOfSlots = Integer.parseInt(numslots.getText());// 	
                		ip.decNum = 3;// 		
    	  	
                	   long unit = 0;
                	   int slotsize = Integer.parseInt(tslotnum.getText());
                	   String slotunit = (String) tslotunit.getSelectedItem();
                	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
                	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
                	   else if(slotunit.equals("week")){unit = 604800000l;}
                	   else if(slotunit.equals("day")){unit = 604800000l/7;}
                	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
                	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
                	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
                	   
                	   ip.resources.addAll(jopRes.getSelectedItems());
                	   ip.slotSize = unit*slotsize;
                	  
                	   
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize + ip.resources);
                	   dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSParamsResMROT(final InputParameters ip, final Connection con) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Specify time series settings: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("Start time:");  
	lab0.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab0, c);
	
		Statement dbStatementStart = con.createStatement();
		String sqlQueryStart = "SELECT min(time) from eventlog";
		ResultSet rsStart = dbStatementStart.executeQuery(sqlQueryStart);
		rsStart.beforeFirst();
		rsStart.next();
		Timestamp starttime = rsStart.getTimestamp(1);

		long timestamp = starttime.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		Integer syear = cal.get(Calendar.YEAR);
		Integer smonth = cal.get(Calendar.MONTH)+1;
		Integer sday = cal.get(Calendar.DATE);
		Integer shour = cal.get(Calendar.HOUR_OF_DAY);
		Integer smin = cal.get(Calendar.MINUTE);
		Integer ssec = cal.get(Calendar.SECOND);
		
		String iyear = syear.toString(); 
		String imonth = smonth.toString(); 
		String iday = sday.toString(); 
		String ihour = shour.toString(); 
		String imin = smin.toString(); 
		String isec = ssec.toString(); 
		
		Statement dbStatementEnd = con.createStatement();
		String sqlQueryEnd = "SELECT max(time) from eventlog";
		ResultSet rsEnd = dbStatementEnd.executeQuery(sqlQueryEnd);
		rsEnd.beforeFirst();
		rsEnd.next();
		//Timestamp endtime = rsEnd.getTimestamp(1);
		//final Long log_duration = endtime.getTime() - starttime.getTime();
		//double numWeeks = log_duration/1000/60/60/24/7;
		//Long weeks = (long) numWeeks+1;
	
	final JTextField year=new JTextField(iyear,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 3;
	c.gridy = 1;
	pane.add(year, c);
	
	final JLabel lab1 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab1.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 4;
	c.gridy = 1;
	pane.add(lab1, c);
		
	final JTextField month=new JTextField(imonth,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 5;
	c.gridy = 1;
	pane.add(month, c);
	
	final JLabel lab2 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;/</h3></html>");  
	lab2.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 6;
	c.gridy = 1;
	pane.add(lab2, c);
			
	final JTextField day=new JTextField(iday,50);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 7;
	c.gridy = 1;
	pane.add(day, c);
	
	final JLabel lab3 = new JLabel("<html><h3>&nbsp;&nbsp;.&nbsp;&nbsp;</h3></html>"); 
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 8;
	c.gridy = 1;
	pane.add(lab3, c);

	final JTextField hour=new JTextField(ihour,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 9;
	c.gridy = 1;
	pane.add(hour, c);
	
	final JLabel lab5 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");
	lab5.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 10;
	c.gridy = 1;
	pane.add(lab5, c);
		
	final JTextField minute=new JTextField(imin,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 11;
	c.gridy = 1;
	pane.add(minute, c);
	
	final JLabel lab6 = new JLabel("<html><h3>&nbsp;&nbsp;&nbsp;&nbsp;:</h3></html>");  
	lab6.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 12;
	c.gridy = 1;
	pane.add(lab6, c);

	final JTextField second=new JTextField(isec,30);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 13;
	c.gridy = 1;
	pane.add(second, c);
	
			
	final JLabel lab7 = new JLabel("Time period:");  
	lab7.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 3;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab7, c);
		
	final ProMTextField tslotnum = new ProMTextField("1");
	c.ipadx = 30;
	c.gridwidth = 5;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(tslotnum, c);
			
		
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("month");
	model.addElement("week");
	model.addElement("year");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("minute");
	model.addElement("second");
	   
	final ProMComboBox tslotunit = new ProMComboBox(model);
	c.ipadx = 30;
	c.gridwidth = 6;
	c.gridx = 8;
	c.gridy = 2;
	pane.add(tslotunit, c);

		
		Vector<String> allResources = new Vector<String>();
	
		Statement dbStatement1 = con.createStatement();
		String sqlQuery1 = "SELECT distinct resource from eventlog";
		ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
		rs1.beforeFirst();
		
		while(rs1.next())
		{	
		String resource = rs1.getString("resource");
		allResources.add(resource);
		}

		String[] model1 = new String[allResources.size()];
		
		for(int j=0; j<allResources.size(); j++)
		{
			model1[j] = allResources.elementAt(j);
		}
	 
		
		final ProMCheckComboBox jopRes = new ProMCheckComboBox(model1);

			
			JLabel R1lab = new JLabel("Select resources:");  
			R1lab.setForeground(UISettings.TextLight_COLOR);
			c.gridwidth = 3;
			c.gridx = 0;
			c.gridy = 3;
			pane.add(R1lab, c);
		
			
			c.gridx = 5;
			c.gridy = 3;
			c.gridwidth = 6;
			pane.add(jopRes, c);
	

	SlickerButton but=new SlickerButton();
	c.ipadx = 600;
	but.setText("Next");
	c.gridwidth = 14;
	c.gridx = 0;
	c.gridy = 4;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
                		ip.startTime = dt.getMillis();
                		ip.numberOfSlots = 1; 
                		ip.decNum = 3;// 		
    	  	
                	   long unit = 0;
                	   int slotsize = Integer.parseInt(tslotnum.getText());
                	   String slotunit = (String) tslotunit.getSelectedItem();
                	   if(slotunit.equals("year")){unit = (604800000l/7)*365;}
                	   else if(slotunit.equals("month")){unit = (604800000l/7)*30;}
                	   else if(slotunit.equals("week")){unit = 604800000l;}
                	   else if(slotunit.equals("day")){unit = 604800000l/7;}
                	   else if(slotunit.equals("hour")){unit = 604800000l/(7*24);}
                	   else if(slotunit.equals("minute")){unit = 604800000l/(7*24*60);}
                	   else if(slotunit.equals("second")){unit = 604800000l/(7*24*60*60);}
                	   
                	   ip.resources.addAll(jopRes.getSelectedItems());
                	   ip.slotSize = unit*slotsize;
                	  
                	   
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize + ip.resources);
                	   dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineDEAAnalysisParams(final InputParameters ip) throws Exception
   {
	
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify DEA analysis parameters: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab1 = new JLabel("Use costs?");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab1, c);
	
	 DefaultComboBoxModel model1 = new DefaultComboBoxModel();
     model1.addElement("Yes");
     model1.addElement("No");
      final JComboBox orient = new JComboBox(model1);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 1;
	c.ipadx = 100;
	pane.add(orient, c);

		
	final JLabel lab2 = new JLabel("Return to scale:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab2, c);
	
	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
     model2.addElement("crs");
     model2.addElement("vrs");
     final JComboBox rts = new JComboBox(model2);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(rts, c);

	

	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 3;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
               		String usec = (String) orient.getSelectedItem();
               		if(usec.equals("Yes"))
               		{ip.usecosts = true;}else
               		{ip.usecosts = false;}
               		ip.RTS = (String) rts.getSelectedItem();
            	   
                 	dispose(); }
                                   }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters selectDEAInputs(final Connection con, final InputParameters ip) throws Exception
   {
	
	final Vector<String> deaIn = new Vector <String>();	
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	final GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final Vector<String> ids = new Vector<String>();
	final Vector<String> names = new Vector<String>();
	final Vector<String> definitions = new Vector<String>();

	
	Statement dbStatement = con.createStatement();
	
	String sqlQuery = "SELECT * FROM deainput ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	ids.add(rs.getString("id"));
	names.add(rs.getString("name"));
	definitions.add(rs.getString("definition"));
	}
			
	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select DEA Input view: </html>");
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final JComboBox jop = new JComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);
 	 

 	 
  	final JButton subbut=new JButton();
 	subbut.setText("Add and Submit");
  	 
 	final JButton addbut=new JButton();
	addbut.setText("Add and Continue");
	
	final JTextField cost = new JTextField();
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<JTextField> varvalues = new Vector<JTextField>();
	final Vector<String> vars = new Vector<String>();
	
 	 jop.addActionListener (new ActionListener () {
 	 public void actionPerformed(ActionEvent e) {
 	    	
 		deaIn.removeAllElements();
 		varnames.removeAllElements();
 		varvalues.removeAllElements();
 		vars.removeAllElements();
 		
 	   	String selectedRBIName = (String) jop.getSelectedItem();
		String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
		String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
		
		deaIn.add("input"+selectedRBIID);
		deaIn.add(selectedRBIName);
		deaIn.add(selectedRBIDefinition);
	
		try {
			vars.addAll(selectUsedVars(con,deaIn));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
		}
	
				
		final JLabel costText=new JLabel();
		costText.setText("<html><h3>Cost: </h3></html>");
		c.ipadx = 300;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(costText, c);

		c.gridx = 1;
		c.gridy = 4;
		pane.add(cost, c);


		for(int i=0; i<vars.size(); i++)
		{
			
			varnames.add(new JLabel(vars.elementAt(i)));  
			c.ipadx = 150; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = i+5;
			pane.add(varnames.elementAt(i), c);
			
			varvalues.add(new JTextField());
			c.gridx = 1;
			c.gridy = i+5;
			c.ipadx = 150;
			pane.add(varvalues.elementAt(i), c);

		}
	 	    
 	c.gridx = 0;
	c.gridy = vars.size()+5;
 	pane.add(addbut, c);
 	    
 	c.gridx = 1;
 	c.gridy = vars.size()+5;
	pane.add(subbut, c);
	  
 	pane.validate();
	pane.repaint();
   
 	    }
 	});

     	
     addbut.addActionListener(
             new ActionListener(){
                 public void actionPerformed(
                         ActionEvent e) {
                    
                	 String deaid = deaIn.elementAt(0);
                	 double deacost = Double.parseDouble(cost.getText()); 
                	  
                	 Vector<String> varValues = new Vector<String>();
                	 for(int i=0; i<varvalues.size(); i++)
                	 {varValues.add(varvalues.elementAt(i).getText());} 
                	  
                	for (int j=0; j<ip.resources.size();j++)
      				{ip.DEA_Inputs.add(new DEA_InOut(deaid,vars,varValues,ip.resources.elementAt(j),deacost));}
          
                	 
                	pane.removeAll();
                	c.gridx = 0;
                	c.gridy = 1;
                	pane.add(jop, c);
                	pane.validate();
            		pane.repaint();
               							                             }
                                 }
                         );

 	 
       subbut.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                        
                     String deaid = deaIn.elementAt(0);
                   	 double deacost = Double.parseDouble(cost.getText()); 
                   	  
                   	 Vector<String> varValues = new Vector<String>();
                   	 for(int i=0; i<varvalues.size(); i++)
                   	 {varValues.add(varvalues.elementAt(i).getText());} 
                   	  
                   	for (int j=0; j<ip.resources.size();j++)
         				{ip.DEA_Inputs.add(new DEA_InOut(deaid,vars,varValues,ip.resources.elementAt(j),deacost));}
         
                 		dispose();
											                             }
                                   }
                           );
       
       setSize(900,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 




	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters selectDEAOutputs(final Connection con, final InputParameters ip) throws Exception
   {
	
	final Vector<String> deaOut = new Vector <String>();	
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	final GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final Vector<String> ids = new Vector<String>();
	final Vector<String> names = new Vector<String>();
	final Vector<String> definitions = new Vector<String>();

	
	Statement dbStatement = con.createStatement();
	
	String sqlQuery = "SELECT * FROM deaoutput ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	ids.add(rs.getString("id"));
	names.add(rs.getString("name"));
	definitions.add(rs.getString("definition"));
	}
			
	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select DEA Output view: </html>");
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final JComboBox jop = new JComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);
 	 

 	 
  	final JButton subbut=new JButton();
 	subbut.setText("Add and Submit");
 	 
 	final JButton addbut=new JButton();
	addbut.setText("Add and Continue");
	
	final JTextField cost = new JTextField();
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<JTextField> varvalues = new Vector<JTextField>();
	final Vector<String> vars = new Vector<String>();
	
 	 jop.addActionListener (new ActionListener () {
 	 public void actionPerformed(ActionEvent e) {
 	    	
 		deaOut.removeAllElements();
 		varnames.removeAllElements();
 		varvalues.removeAllElements();
 		vars.removeAllElements();
 		
 	   	String selectedRBIName = (String) jop.getSelectedItem();
		String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
		String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
		
		deaOut.add("output"+selectedRBIID);
		deaOut.add(selectedRBIName);
		deaOut.add(selectedRBIDefinition);
	
		try {
			vars.addAll(selectUsedVars(con,deaOut));
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
		}
	
		final JLabel costText=new JLabel();
		costText.setText("<html><h3>Cost: </h3></html>");
		c.ipadx = 300;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(costText, c);

		c.gridx = 1;
		c.gridy = 4;
		pane.add(cost, c);


		for(int i=0; i<vars.size(); i++)
		{
			
			varnames.add(new JLabel(vars.elementAt(i)));  
			c.ipadx = 150; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = i+5;
			pane.add(varnames.elementAt(i), c);
			
			varvalues.add(new JTextField());
			c.gridx = 1;
			c.gridy = i+5;
			c.ipadx = 150;
			pane.add(varvalues.elementAt(i), c);

		}
	 	    
 	c.gridx = 0;
	c.gridy = vars.size()+5;
 	pane.add(addbut, c);
 	    
 	c.gridx = 1;
 	c.gridy = vars.size()+5;
	pane.add(subbut, c);
	  
 	pane.validate();
	pane.repaint();
   
 	    }
 	});

     	
     addbut.addActionListener(
             new ActionListener(){
                 public void actionPerformed(
                         ActionEvent e) {
                    
                 	 String deaid = deaOut.elementAt(0);
                	 double deacost = Double.parseDouble(cost.getText()); 
                	  
                	 Vector<String> varValues = new Vector<String>();
                	 for(int i=0; i<varvalues.size(); i++)
                	 {varValues.add(varvalues.elementAt(i).getText());} 
                	  
                	for (int j=0; j<ip.resources.size();j++)
      				{ip.DEA_Outputs.add(new DEA_InOut(deaid,vars,varValues,ip.resources.elementAt(j),deacost));}
          
                	 
                	pane.removeAll();
                	c.gridx = 0;
                	c.gridy = 1;
                	pane.add(jop, c);
                	pane.validate();
            		pane.repaint();
               							                             }
                                 }
                         );

 	 
       subbut.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                        
                    	 String deaid = deaOut.elementAt(0);
                   	 double deacost = Double.parseDouble(cost.getText()); 
                   	  
                   	 Vector<String> varValues = new Vector<String>();
                   	 for(int i=0; i<varvalues.size(); i++)
                   	 {varValues.add(varvalues.elementAt(i).getText());} 
                   	  
                   	for (int j=0; j<ip.resources.size();j++)
         				{ip.DEA_Outputs.add(new DEA_InOut(deaid,vars,varValues,ip.resources.elementAt(j),deacost));}
         
                 		dispose();
											                             }
                                   }
                           );
       
       setSize(900,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	
	public InputParameters defineDEAOut(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> deaview) throws Exception
   {
	
	//Statement dbStatement = con.createStatement();
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify cost and the values of the variables used in the selected DEA view: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
	
	final JLabel costText=new JLabel();
	costText.setText("<html><h3>Cost: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(costText, c);

	final JTextField cost = new JTextField();
	c.gridx = 1;
	c.gridy = 1;
	pane.add(cost, c);

		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<JTextField> varvalues = new Vector<JTextField>();

	for(int i=0; i<vars.size(); i++)
	{
		
		varnames.add(new JLabel(vars.elementAt(i)));  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = i+2;
		pane.add(varnames.elementAt(i), c);
		
		varvalues.add(new JTextField());
		c.gridx = 1;
		c.gridy = i+2;
		c.ipadx = 150;
		pane.add(varvalues.elementAt(i), c);

	}
	
	
	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = vars.size()+2;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   String deaid = deaview.elementAt(0);
                	   double deacost = Double.parseDouble(cost.getText()); 
                	  
                	   Vector<String> varValues = new Vector<String>();
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                	  
                	for (int j=0; j<ip.resources.size();j++)
      				{ip.DEA_Outputs.add(new DEA_InOut(deaid,vars,varValues,ip.resources.elementAt(j),deacost));}
                    dispose(); }
                    }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	public Vector<String> selectUsedVars(final Connection con, final Vector<String> deaview) throws Exception
   {
	
	Vector<String> allviewsnames = new Vector<String>();
	Vector<String> allviewsdefs = new Vector<String>();
	Vector<String> usedviews = new Vector<String>();
	Vector<String> allvars = new Vector<String>();
	Vector<String> usedvars = new Vector<String>();
	
	Statement dbStatement = con.createStatement();

	String sqlQuery = "SELECT * FROM views ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{allviewsnames.add(rs.getString("name"));
	allviewsdefs.add(rs.getString("definition"));}
	
	for (int i=0; i<allviewsnames.size(); i++)
	{	
		if(deaview.elementAt(2).contains(" "+allviewsnames.elementAt(i)+")") || deaview.elementAt(2).contains(" "+allviewsnames.elementAt(i)+" ") || (deaview.elementAt(2).contains(" "+allviewsnames.elementAt(i)) && deaview.elementAt(2).length() == deaview.elementAt(2).lastIndexOf(" "+allviewsnames.elementAt(i))+allviewsnames.elementAt(i).length()+1))
		{
			usedviews.add(allviewsdefs.elementAt(i));
		}
	}
	usedviews.add(deaview.elementAt(2));
	
	
	String sqlQuery2 = "SELECT * FROM vars ";
	
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{allvars.add(rs2.getString("name"));
	}

	for(int i=0; i<usedviews.size(); i++)
	{
		for(int j=0; j<allvars.size(); j++)
		{
			if(usedviews.elementAt(i).contains(allvars.elementAt(j)+"()") && !(usedvars.contains(allvars.elementAt(j))) && !(allvars.elementAt(j).equals("t1")) && !(allvars.elementAt(j).equals("t2")) && !(allvars.elementAt(j).equals("R1")))
			{
				usedvars.add(allvars.elementAt(j));
			}
		}
	}
																		
  return usedvars;
} 




	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> selectDEAOutput(final Connection con) throws Exception
   {
	
	final Vector<String> deaOut = new Vector <String>();	
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	final GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final Vector<String> ids = new Vector<String>();
	final Vector<String> names = new Vector<String>();
	final Vector<String> definitions = new Vector<String>();

	
	Statement dbStatement = con.createStatement();

	String sqlQuery = "SELECT * FROM deaoutput ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	ids.add(rs.getString("id"));
	names.add(rs.getString("name"));
	definitions.add(rs.getString("definition"));
	}
	
		
	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select DEA Output view: </html>");
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final JComboBox jop = new JComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);
 	 
 	final JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 2;
	pane.add(but, c);
 	 
      	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   	@SuppressWarnings("unused")
					Statement dbStatement;
                 		try {
							dbStatement = con.createStatement();
							
							String selectedRBIName = (String) jop.getSelectedItem();
							String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
							String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
							
							deaOut.add("output"+selectedRBIID);
							deaOut.add(selectedRBIName);
							deaOut.add(selectedRBIDefinition);
							
							dispose();
							
							} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
			                         JOptionPane.ERROR_MESSAGE);}
                                                }
                                   }
                           );
       
       setSize(600,200);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return deaOut;
   } 



	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters getResTSmrot (final InputParameters ip, final Connection con) throws Exception
	   {
		
		final Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
		Vector<String> resources = new Vector<String>();
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT distinct resource FROM eventlog";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{	
		String curres = rs.getString(1);
		resources.add(curres);
		}

		
		final JLabel defVarsText=new JLabel();
		defVarsText.setText("<html><h3>Specify time parameters: </h3></html>");
		c.ipadx = 300;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(defVarsText, c);
			
		final JLabel lab0 = new JLabel("Specify start date:");  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		pane.add(lab0, c);
			
			
		final JLabel lab1 = new JLabel("Year:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(lab1, c);
			
		final JTextField year = new JTextField();
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 100;
		pane.add(year, c);
			
			final JLabel lab2 = new JLabel("Month:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			pane.add(lab2, c);
			
			final JTextField month = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			c.ipadx = 100;
			pane.add(month, c);

			final JLabel lab3 = new JLabel("Day:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			pane.add(lab3, c);
			
			final JTextField day = new JTextField();
			c.gridx = 1;
			c.gridy = 4;
			c.ipadx = 100;
			pane.add(day, c);

			final JLabel lab4 = new JLabel("Hour:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 5;
			pane.add(lab4, c);
			
			final JTextField hour = new JTextField();
			c.gridx = 1;
			c.gridy = 5;
			c.ipadx = 100;
			pane.add(hour, c);

			final JLabel lab5 = new JLabel("Minute:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 6;
			pane.add(lab5, c);
			
			final JTextField minute = new JTextField();
			c.gridx = 1;
			c.gridy = 6;
			c.ipadx = 100;
			pane.add(minute, c);

			final JLabel lab6 = new JLabel("Second:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 7;
			pane.add(lab6, c);
			
			final JTextField second = new JTextField();
			c.gridx = 1;
			c.gridy = 7;
			c.ipadx = 100;
			pane.add(second, c);

			final JLabel lab7 = new JLabel("Time period:");  
			c.ipadx = 100; 
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 8;
			pane.add(lab7, c);
			
			final JTextField tslotnum = new JTextField();
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotnum, c);
			
	
			 DefaultComboBoxModel model = new DefaultComboBoxModel();
		     model.addElement("years");
		     model.addElement("months");
		     model.addElement("weeks");
		     model.addElement("days");
		     model.addElement("hours");
		     model.addElement("minutes");
		     model.addElement("seconds");
		   
		     final JComboBox tslotunit = new JComboBox(model);
		    c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotunit, c);
			
			
			final JLabel lab8 = new JLabel("Number of perioids:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 10;
			pane.add(lab8, c);
			
			final JLabel numslots = new JLabel("1");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 10;
			pane.add(numslots, c);
		
			
			
			final JLabel lab9 = new JLabel("Number of decimals after point:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 11;
			pane.add(lab9, c);
			
			final JTextField decimals = new JTextField();
			c.gridx = 1;
			c.gridy = 11;
			c.ipadx = 100;
			pane.add(decimals, c);

			final JLabel reslab = new JLabel("Select resources (holding CTRL key):");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 12;
			pane.add(reslab, c);
			
			final JList places;
			places = new JList(resources); 
			places.setVisibleRowCount(1); 
			places.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			c.gridx = 1;
			c.gridy = 12;
			c.gridwidth = 1;
			c.ipadx = 100;
			pane.add(new JScrollPane(places), c);
			
				
		
		JButton but=new JButton();
		but.setText("Submit");
		c.gridx = 0;
		c.gridy = 13;
		c.gridwidth = 2;
		pane.add(but, c);
	      
	     	
	       but.addActionListener(
	               new ActionListener(){
	                   @SuppressWarnings("deprecation")
					public void actionPerformed(
	                           ActionEvent e) {
	                	   
	                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
	                		ip.startTime = dt.getMillis();
	                		ip.numberOfSlots = 1;// 	
	                		ip.decNum = Integer.parseInt(decimals.getText()); 		
	    	  	
	                	   long unit = 0;
	                	   int slotsize = Integer.parseInt(tslotnum.getText());
	                	   String slotunit = (String) tslotunit.getSelectedItem();
	                	   if(slotunit.equals("years")){unit = (604800000l/7)*365;}
	                	   else if(slotunit.equals("months")){unit = (604800000l/7)*30;}	                	   else if(slotunit.equals("weeks")){unit = 604800000l;}
	                	   else if(slotunit.equals("days")){unit = 604800000l/7;}
	                	   else if(slotunit.equals("hours")){unit = 604800000l/(7*24);}
	                	   else if(slotunit.equals("minutes")){unit = 604800000l/(7*24*60);}
	                	   else if(slotunit.equals("seconds")){unit = 604800000l/(7*24*60*60);}
	                	   
	                	   ip.slotSize = unit*slotsize;
	                	   
	                	   Object obj[ ] = places.getSelectedValues(); 
	                	   for(int i = 0; i < obj.length; i++) 
	                	   { ip.resources.add((String) obj[i]); } 
	                	   	                	   
	                	   //
	                	       	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize+ ip.resources);
	                	           dispose(); }
	                                   }
	                           );
	       
	  	  
	       setSize(400,400);
	       setModal(true);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		return ip;	
	   }
	
	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters getResTSmrmt (final InputParameters ip, final Connection con) throws Exception
	   {
		
		final Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
		Vector<String> resources = new Vector<String>();
		Statement dbStatement = con.createStatement();
		
		String sqlQuery = "SELECT distinct resource FROM eventlog"; 
		
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{	
		String curres = rs.getString(1);
		resources.add(curres);
		}

		
		final JLabel defVarsText=new JLabel();
		defVarsText.setText("<html><h3>Specify time series values: </h3></html>");
		c.ipadx = 300;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(defVarsText, c);
			
		final JLabel lab0 = new JLabel("Specify start date:");  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		pane.add(lab0, c);
			
			
		final JLabel lab1 = new JLabel("Year:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(lab1, c);
			
		final JTextField year = new JTextField();
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 100;
		pane.add(year, c);
			
			final JLabel lab2 = new JLabel("Month:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			pane.add(lab2, c);
			
			final JTextField month = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			c.ipadx = 100;
			pane.add(month, c);

			final JLabel lab3 = new JLabel("Day:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			pane.add(lab3, c);
			
			final JTextField day = new JTextField();
			c.gridx = 1;
			c.gridy = 4;
			c.ipadx = 100;
			pane.add(day, c);

			final JLabel lab4 = new JLabel("Hour:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 5;
			pane.add(lab4, c);
			
			final JTextField hour = new JTextField();
			c.gridx = 1;
			c.gridy = 5;
			c.ipadx = 100;
			pane.add(hour, c);

			final JLabel lab5 = new JLabel("Minute:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 6;
			pane.add(lab5, c);
			
			final JTextField minute = new JTextField();
			c.gridx = 1;
			c.gridy = 6;
			c.ipadx = 100;
			pane.add(minute, c);

			final JLabel lab6 = new JLabel("Second:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 7;
			pane.add(lab6, c);
			
			final JTextField second = new JTextField();
			c.gridx = 1;
			c.gridy = 7;
			c.ipadx = 100;
			pane.add(second, c);

			final JLabel lab7 = new JLabel("Time series slot size:");  
			c.ipadx = 100; 
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 8;
			pane.add(lab7, c);
			
			final JTextField tslotnum = new JTextField();
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotnum, c);
			
	
			 DefaultComboBoxModel model = new DefaultComboBoxModel();
		     model.addElement("years");
		     model.addElement("months");
		     model.addElement("weeks");
		     model.addElement("days");
		     model.addElement("hours");
		     model.addElement("minutes");
		     model.addElement("seconds");
		   
		     final JComboBox tslotunit = new JComboBox(model);
		    c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotunit, c);
			
			
			final JLabel lab8 = new JLabel("Number of slots:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 10;
			pane.add(lab8, c);
			
			final JTextField numslots = new JTextField();
			c.gridx = 1;
			c.gridy = 10;
			c.ipadx = 100;
			pane.add(numslots, c);
			
			final JLabel lab9 = new JLabel("Number of decimals after point:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 11;
			pane.add(lab9, c);
			
			final JTextField decimals = new JTextField();
			c.gridx = 1;
			c.gridy = 11;
			c.ipadx = 100;
			pane.add(decimals, c);

			final JLabel reslab = new JLabel("Select resources (holding CTRL key):");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 12;
			pane.add(reslab, c);
			
			final JList places;
			places = new JList(resources); 
			places.setVisibleRowCount(1); 
			places.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			c.gridx = 1;
			c.gridy = 12;
			c.gridwidth = 1;
			c.ipadx = 100;
			pane.add(new JScrollPane(places), c);
			
			
		JButton but=new JButton();
		but.setText("Submit");
		c.gridx = 0;
		c.gridy = 13;
		c.gridwidth = 2;
		pane.add(but, c);
	      
	     	
	       but.addActionListener(
	               new ActionListener(){
	                   @SuppressWarnings("deprecation")
					public void actionPerformed(
	                           ActionEvent e) {
	                	   
	                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
	                		ip.startTime = dt.getMillis();
	                		ip.numberOfSlots = Integer.parseInt(numslots.getText());	
	                		ip.decNum = Integer.parseInt(decimals.getText());		
	    	  	
	                	   long unit = 0;
	                	   int slotsize = Integer.parseInt(tslotnum.getText());
	                	   String slotunit = (String) tslotunit.getSelectedItem();
	                	   if(slotunit.equals("years")){unit = (604800000l/7)*365;}
	                	   else if(slotunit.equals("months")){unit = (604800000l/7)*30;}
	                	   else if(slotunit.equals("weeks")){unit = 604800000l;}
	                	   else if(slotunit.equals("days")){unit = 604800000l/7;}
	                	   else if(slotunit.equals("hours")){unit = 604800000l/(7*24);}
	                	   else if(slotunit.equals("minutes")){unit = 604800000l/(7*24*60);}
	                	   else if(slotunit.equals("seconds")){unit = 604800000l/(7*24*60*60);}
	                	   
	                	   ip.slotSize = unit*slotsize;
	                	   
	                	   Object obj[ ] = places.getSelectedValues(); 
	                	   for(int i = 0; i < obj.length; i++) 
	                	   { ip.resources.add((String) obj[i]); } 
	                	   	                	   
	                	  
	                	       	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize+ ip.resources);
	                	           dispose(); }
	                                   }
	                           );
	       
	  	  
	       setSize(400,400);
	       setModal(true);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		return ip;	
	   }
	
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters getResTSormt (final InputParameters ip, final Connection con) throws Exception
	   {
		
		final Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
		Vector<String> resources = new Vector<String>();
		Statement dbStatement = con.createStatement();
		String sqlQuery = "SELECT distinct resource FROM eventlog";
		ResultSet rs = dbStatement.executeQuery(sqlQuery);
		rs.beforeFirst();
		while(rs.next())
		{	
		String curres = rs.getString(1);
		resources.add(curres);
		}

			
		final JLabel defVarsText=new JLabel();
		defVarsText.setText("<html><h3>Specify time series values: </h3></html>");
		c.ipadx = 300;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(defVarsText, c);
			
		final JLabel lab0 = new JLabel("Specify start date:");  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		pane.add(lab0, c);
			
			
		final JLabel lab1 = new JLabel("Year:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(lab1, c);
			
		final JTextField year = new JTextField();
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 100;
		pane.add(year, c);
			
			final JLabel lab2 = new JLabel("Month:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			pane.add(lab2, c);
			
			final JTextField month = new JTextField();
			c.gridx = 1;
			c.gridy = 3;
			c.ipadx = 100;
			pane.add(month, c);

			final JLabel lab3 = new JLabel("Day:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 4;
			pane.add(lab3, c);
			
			final JTextField day = new JTextField();
			c.gridx = 1;
			c.gridy = 4;
			c.ipadx = 100;
			pane.add(day, c);

			final JLabel lab4 = new JLabel("Hour:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 5;
			pane.add(lab4, c);
			
			final JTextField hour = new JTextField();
			c.gridx = 1;
			c.gridy = 5;
			c.ipadx = 100;
			pane.add(hour, c);

			final JLabel lab5 = new JLabel("Minute:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 6;
			pane.add(lab5, c);
			
			final JTextField minute = new JTextField();
			c.gridx = 1;
			c.gridy = 6;
			c.ipadx = 100;
			pane.add(minute, c);

			final JLabel lab6 = new JLabel("Second:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 7;
			pane.add(lab6, c);
			
			final JTextField second = new JTextField();
			c.gridx = 1;
			c.gridy = 7;
			c.ipadx = 100;
			pane.add(second, c);

			final JLabel lab7 = new JLabel("Time series slot size:");  
			c.ipadx = 100; 
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 8;
			pane.add(lab7, c);
			
			final JTextField tslotnum = new JTextField();
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotnum, c);
			
	
			 DefaultComboBoxModel model = new DefaultComboBoxModel();
		     model.addElement("years");
		     model.addElement("months");
		     model.addElement("weeks");
		     model.addElement("days");
		     model.addElement("hours");
		     model.addElement("minutes");
		     model.addElement("seconds");
		   
		     final JComboBox tslotunit = new JComboBox(model);
		    c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 9;
			c.ipadx = 100;
			pane.add(tslotunit, c);
			
			
			final JLabel lab8 = new JLabel("Number of slots:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 10;
			pane.add(lab8, c);
			
			final JTextField numslots = new JTextField();
			c.gridx = 1;
			c.gridy = 10;
			c.ipadx = 100;
			pane.add(numslots, c);
			
			final JLabel lab9 = new JLabel("Number of decimals after point:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 11;
			pane.add(lab9, c);
			
			final JTextField decimals = new JTextField();
			c.gridx = 1;
			c.gridy = 11;
			c.ipadx = 100;
			pane.add(decimals, c);

			final JLabel reslab = new JLabel("Select resource:");  
			c.ipadx = 100; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 12;
			pane.add(reslab, c);
			
			 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
			 for(int i=0; i<resources.size(); i++)
			 {model2.addElement(resources.elementAt(i));}
		     final JComboBox resource = new JComboBox(model2);
		    c.gridwidth = 1;
			c.gridx = 1;
			c.gridy = 12;
			c.ipadx = 100;
			pane.add(resource, c);

		JButton but=new JButton();
		but.setText("Submit");
		c.gridx = 0;
		c.gridy = 13;
		c.gridwidth = 2;
		pane.add(but, c);
	      
	     	
	       but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	   
	                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
	                		ip.startTime = dt.getMillis();
	                		ip.numberOfSlots = Integer.parseInt(numslots.getText());	
	                		ip.decNum = Integer.parseInt(decimals.getText());		
	    	  	
	                	   long unit = 0;
	                	   int slotsize = Integer.parseInt(tslotnum.getText());
	                	   String slotunit = (String) tslotunit.getSelectedItem();
	                	   if(slotunit.equals("years")){unit = (604800000l/7)*365;}
	                	   else if(slotunit.equals("months")){unit = (604800000l/7)*30;}
	                	   else if(slotunit.equals("weeks")){unit = 604800000l;}
	                	   else if(slotunit.equals("days")){unit = 604800000l/7;}
	                	   else if(slotunit.equals("hours")){unit = 604800000l/(7*24);}
	                	   else if(slotunit.equals("minutes")){unit = 604800000l/(7*24*60);}
	                	   else if(slotunit.equals("seconds")){unit = 604800000l/(7*24*60*60);}
	                	   
	                	   ip.slotSize = unit*slotsize;
	                	   ip.resources.add((String) resource.getSelectedItem());
	                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize+ ip.resources);
	                	              dispose(); }
	                                   }
	                           );
	       
	  	  
	       setSize(400,400);
	       setModal(true);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		return ip;	
	   }
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters getDEAType (final InputParameters ip) throws Exception
	   {
		
		final HeaderBar pane = new HeaderBar("");
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
			
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
				
	
		final JLabel lab2 = new JLabel("Select the number of resources and time periods to be considered:");  
		lab2.setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 100; 
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(lab2, c);
		
		 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	     model2.addElement("One resource - multiple time slots");
	     model2.addElement("Multiple resources - one time period");
	     model2.addElement("Multiple resources - multiple time slots");
	     final ProMComboBox deatype = new ProMComboBox(model2);
	    c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		c.ipadx = 100;
		pane.add(deatype, c);
		
		final JLabel lab1 = new JLabel("Use output costs?");  
		lab1.setForeground(UISettings.TextLight_COLOR);
		
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(lab1, c);
		
		 DefaultComboBoxModel model1 = new DefaultComboBoxModel();
	     model1.addElement("No");
	     model1.addElement("Yes");
	     final ProMComboBox orient = new ProMComboBox(model1);
	    c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 100;
		pane.add(orient, c);
		
		final JLabel lab1in = new JLabel("Use input costs?");  
		lab1in.setForeground(UISettings.TextLight_COLOR);
		
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(lab1in, c);
		
		 DefaultComboBoxModel model1in = new DefaultComboBoxModel();
		 model1in.addElement("No");
		 model1in.addElement("Yes");
	     final ProMComboBox orientIn = new ProMComboBox(model1in);
	    c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 100;
		pane.add(orientIn, c);


			
		final JLabel lab3 = new JLabel("Return to scale:");  
		lab3.setForeground(UISettings.TextLight_COLOR);
		
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(lab3, c);
		
		 DefaultComboBoxModel model3 = new DefaultComboBoxModel();
	     model3.addElement("Constant");
	     model3.addElement("Variable");
	     model3.addElement("Increasing");
	     model3.addElement("Decreasing");
		  
	     final ProMComboBox rts = new ProMComboBox(model3);
	    c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 4;
		c.ipadx = 100;
		pane.add(rts, c);
		
		SlickerButton but=new SlickerButton();
		but.setText("Submit");
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		pane.add(but, c);
	      
	     	
	       but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	   
	               		     	   
	                 	String type = (String) deatype.getSelectedItem();
	                	if(type.equals("One resource - multiple time slots")){ip.deatype = "ormt";}
	                	if(type.equals("Multiple resources - one time period")){ip.deatype = "mrot";}
	                	if(type.equals("Multiple resources - multiple time slots")){ip.deatype = "mrmt";}
	                	
	                	String usec = (String) orient.getSelectedItem();
	               		if(usec.equals("Yes"))
	               		{ip.usecosts = true;}else
	               		{ip.usecosts = false;}
	               		
	               		String useInc = (String) orientIn.getSelectedItem();
	               		if(useInc.equals("Yes"))
	               		{ip.useInputCosts = true;}else
	               		{ip.useInputCosts = false;}
	               	
	               		
	               		String dearts = (String) rts.getSelectedItem();
	               		if(dearts.equals("Constant")){ip.RTS = "crs";}
	               		if(dearts.equals("Decreasing")){ip.RTS = "drs";}
	               		if(dearts.equals("Increasing")){ip.RTS = "irs";}
	               		if(dearts.equals("Variable")){ip.RTS = "vrs";}
			             	               		 
	                  	dispose(); }
	                                   }
	                           );
	       
	       setSize(700,350);
	       setModal(true);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		return ip;	
	   }
	
	public InputParameters defineDBParams(final InputParameters ip) throws Exception
   {
	
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify database parameters: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("Specify the name of existing database if you wish to use it or the new DB name if you want to import new data");  
	c.ipadx = 150; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 2;
	pane.add(lab0, c);
		
		
	final JLabel lab1 = new JLabel("DB Host name:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab1, c);
		
	final JTextField dbhost = new JTextField();
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(dbhost, c);
		
		final JLabel lab2 = new JLabel("DB User name:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(lab2, c);
		
		final JTextField dbuser = new JTextField();
		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 100;
		pane.add(dbuser, c);

		final JLabel lab3 = new JLabel("DB User password:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(lab3, c);
		
		final JTextField dbpass = new JTextField();
		c.gridx = 1;
		c.gridy = 4;
		c.ipadx = 100;
		pane.add(dbpass, c);

		final JLabel lab4 = new JLabel("DB name:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(lab4, c);
		
		final JTextField dbname = new JTextField();
		c.gridx = 1;
		c.gridy = 5;
		c.ipadx = 100;
		pane.add(dbname, c);

	
	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 12;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		ip.hostname = dbhost.getText();
                		ip.dbuser = dbuser.getText();
                		ip.password = dbpass.getText();
                		ip.dbname = dbname.getText();
                		
                		dispose(); }
                                   }
                           );
       
       setSize(800,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSAnalysisParams(final InputParameters ip) throws Exception
   {
	
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify time series analysis parameters: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab1 = new JLabel("Get change points?");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab1, c);
	
	 DefaultComboBoxModel model1 = new DefaultComboBoxModel();
     model1.addElement("yes");
     model1.addElement("no");
     final JComboBox cp = new JComboBox(model1);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 1;
	c.ipadx = 100;
	pane.add(cp, c);

		
	final JLabel lab2 = new JLabel("Get outliers?");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab2, c);
	
	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
     model2.addElement("yes");
     model2.addElement("no");
     final JComboBox out = new JComboBox(model2);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(out, c);

	
	final JLabel lab3 = new JLabel("Estimate trend?");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 3;
	pane.add(lab3, c);
	
	 DefaultComboBoxModel model3 = new DefaultComboBoxModel();
     model3.addElement("yes");
     model3.addElement("no");
     final JComboBox trend = new JComboBox(model3);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 3;
	c.ipadx = 100;
	pane.add(trend, c);

	final JLabel lab4 = new JLabel("Change Point method:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 4;
	pane.add(lab4, c);
	
	 DefaultComboBoxModel model4 = new DefaultComboBoxModel();
     model4.addElement("Mann-Whitney");
     model4.addElement("Mood");
     model4.addElement("Lepage");
     model4.addElement("Kolmogorov-Smirnov");
     model4.addElement("Cramer-von-Mises");
     final JComboBox cpmtype = new JComboBox(model4);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 4;
	c.ipadx = 100;
	pane.add(cpmtype, c);


	final JLabel lab5 = new JLabel("ARL0:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 5;
	pane.add(lab5, c);
	
	 DefaultComboBoxModel model5 = new DefaultComboBoxModel();
     model5.addElement("1000");
     model5.addElement("2000");
     model5.addElement("5000");
     model5.addElement("10000");
     model5.addElement("100");
     final JComboBox arl0 = new JComboBox(model5);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 5;
	c.ipadx = 100;
	pane.add(arl0, c);


	final JLabel lab6 = new JLabel("Startup:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 6;
	pane.add(lab6, c);
	
	 DefaultComboBoxModel model6 = new DefaultComboBoxModel();
     model6.addElement("20");
     model6.addElement("30");
     model6.addElement("50");
     model6.addElement("100");
     model6.addElement("1000");
     final JComboBox startup = new JComboBox(model6);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 6;
	c.ipadx = 100;
	pane.add(startup, c);

	final JLabel lab7 = new JLabel("Outlier identification method:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 7;
	pane.add(lab7, c);
	
	 DefaultComboBoxModel model7 = new DefaultComboBoxModel();
     model7.addElement("I");
     model7.addElement("II");
     final JComboBox method = new JComboBox(model7);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 7;
	c.ipadx = 100;
	pane.add(method, c);

	final JLabel lab8 = new JLabel("Trend estimation period:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 8;
	pane.add(lab8, c);
	
	 DefaultComboBoxModel model8 = new DefaultComboBoxModel();
     model8.addElement("recent");
     model8.addElement("full");
     final JComboBox period = new JComboBox(model8);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 8;
	c.ipadx = 100;
	pane.add(period, c);

	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 9;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
               		ip.cpmType = (String) cpmtype.getSelectedItem();
            		ip.ARL0 = (String) arl0.getSelectedItem();
            		ip.startup = (String) startup.getSelectedItem();
            		ip.method = (String) method.getSelectedItem();
            		ip.period = (String) period.getSelectedItem();

                   	String cpa = (String) cp.getSelectedItem();
                	if(cpa.equals("yes")){ip.cp = 1;}else{ip.cp = 0;}
                	   
                 	String outa = (String) out.getSelectedItem();
                	if(outa.equals("yes")){ip.out = 1;}else{ip.out = 0;}
                
                 	String trenda = (String) trend.getSelectedItem();
                	if(trenda.equals("yes")){ip.trend = 1;}else{ip.trend = 0;}
                
                 	dispose(); }
                                   }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 




	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSParams(final InputParameters ip) throws Exception
   {
	
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify time series values: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("Specify start date:");  
	c.ipadx = 150; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 2;
	pane.add(lab0, c);
		
		
	final JLabel lab1 = new JLabel("Year:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab1, c);
		
	final JTextField year = new JTextField();
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(year, c);
		
		final JLabel lab2 = new JLabel("Month:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(lab2, c);
		
		final JTextField month = new JTextField();
		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 100;
		pane.add(month, c);

		final JLabel lab3 = new JLabel("Day:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(lab3, c);
		
		final JTextField day = new JTextField();
		c.gridx = 1;
		c.gridy = 4;
		c.ipadx = 100;
		pane.add(day, c);

		final JLabel lab4 = new JLabel("Hour:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(lab4, c);
		
		final JTextField hour = new JTextField();
		c.gridx = 1;
		c.gridy = 5;
		c.ipadx = 100;
		pane.add(hour, c);

		final JLabel lab5 = new JLabel("Minute:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 6;
		pane.add(lab5, c);
		
		final JTextField minute = new JTextField();
		c.gridx = 1;
		c.gridy = 6;
		c.ipadx = 100;
		pane.add(minute, c);

		final JLabel lab6 = new JLabel("Second:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 7;
		pane.add(lab6, c);
		
		final JTextField second = new JTextField();
		c.gridx = 1;
		c.gridy = 7;
		c.ipadx = 100;
		pane.add(second, c);

		final JLabel lab7 = new JLabel("Time series slot size:");  
		c.ipadx = 100; 
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 8;
		pane.add(lab7, c);
		
		final JTextField tslotnum = new JTextField();
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 9;
		c.ipadx = 100;
		pane.add(tslotnum, c);
		
		
			
		
		 DefaultComboBoxModel model = new DefaultComboBoxModel();
	     model.addElement("years");
	     model.addElement("months");
	     model.addElement("weeks");
	     model.addElement("days");
	     model.addElement("hours");
	     model.addElement("minutes");
	     model.addElement("seconds");
	   
	     final JComboBox tslotunit = new JComboBox(model);
	    c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 9;
		c.ipadx = 100;
		pane.add(tslotunit, c);
		

		
		final JLabel lab8 = new JLabel("Number of slots:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 10;
		pane.add(lab8, c);
		
		final JTextField numslots = new JTextField();
		c.gridx = 1;
		c.gridy = 10;
		c.ipadx = 100;
		pane.add(numslots, c);
		
		final JLabel lab9 = new JLabel("Number of decimals after point:");  
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		pane.add(lab9, c);
		
		final JTextField decimals = new JTextField();
		c.gridx = 1;
		c.gridy = 11;
		c.ipadx = 100;
		pane.add(decimals, c);



	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 12;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
                		ip.startTime = dt.getMillis();
                		ip.numberOfSlots = Integer.parseInt(numslots.getText()); 	
                		ip.decNum = Integer.parseInt(decimals.getText()); 		
    	  	
                	   long unit = 0;
                	   int slotsize = Integer.parseInt(tslotnum.getText());
                	   String slotunit = (String) tslotunit.getSelectedItem();
                	   if(slotunit.equals("years")){unit = (604800000l/7)*365;}
                	   else if(slotunit.equals("months")){unit = (604800000l/7)*30;}
                	   else if(slotunit.equals("weeks")){unit = 604800000l;}
                	   else if(slotunit.equals("days")){unit = 604800000l/7;}
                	   else if(slotunit.equals("hours")){unit = 604800000l/(7*24);}
                	   else if(slotunit.equals("minutes")){unit = 604800000l/(7*24*60);}
                	   else if(slotunit.equals("seconds")){unit = 604800000l/(7*24*60*60);}
                	   
                	   ip.slotSize = unit*slotsize;
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize);
                	              dispose(); }
                                   }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	
	public InputParameters defineMultVars(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> rbi) throws Exception
   {
	
	//final Statement dbStatement = con.createStatement();
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify values of the variables used in the selected indicator: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<JTextField> varvalues = new Vector<JTextField>();

	for(int i=0; i<vars.size(); i++)
	{
		
		varnames.add(new JLabel(vars.elementAt(i)));  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = i+1;
		pane.add(varnames.elementAt(i), c);
		
		varvalues.add(new JTextField());
		c.gridx = 1;
		c.gridy = i+1;
		c.ipadx = 150;
		pane.add(varvalues.elementAt(i), c);

	}
	
	
	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = vars.size()+1;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   String rbiid = rbi.elementAt(0);
                	   System.out.println("rbiid: "+rbiid);
                	  
                	   Vector<String> varValues = new Vector<String>();
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());
                	  System.out.println("varvalue "+i+": "+ varValues.elementAt(i));} 
                	  
                	  int resIndex = -1;
                	  int taskIndex = -1;
                	  
                	  if (vars.contains("R1") && varValues.elementAt(vars.indexOf("R1")).equalsIgnoreCase("all"))
                	  {resIndex = vars.indexOf("R1");}
                	  
                	  if (vars.contains("Task") && varValues.elementAt(vars.indexOf("Task")).equalsIgnoreCase("all"))
                	  {taskIndex = vars.indexOf("Task");}
                
                	System.out.println(resIndex+"***"+taskIndex);	
                	try{
                	
                	if(resIndex>-1 && taskIndex==-1)
                	{System.out.println("all resources one task");
                		String sql1 = "select distinct resource from eventlog";
                		final Statement dbStatement2 = con.createStatement();
                		ResultSet rs1 = dbStatement2.executeQuery(sql1);
                		rs1.beforeFirst();
                		while(rs1.next())
                		{
                		String nextres = rs1.getString(1);
                		varValues.set(resIndex,nextres);
                		ip.rbi_inputs.add(new Indicator_Input(rbiid, vars, varValues));
                		System.out.println("rbiid: "+rbiid+" vars: "+vars.toString()+" varValues: "+varValues.toString());
                		}
                	}
                      
                	if(resIndex==-1 && taskIndex>-1)
                	{System.out.println("all tasks of one resource");
                		String res = varValues.elementAt(vars.indexOf("R1"));
                		String sql1 = "select distinct task from eventlog where resource='"+res+"'";
                		final Statement dbStatement3 = con.createStatement();
                		ResultSet rs1 = dbStatement3.executeQuery(sql1);
                		rs1.beforeFirst();
                		while(rs1.next())
                		{
                		String nextTask = rs1.getString(1);
                		varValues.set(taskIndex,nextTask);
                		ip.rbi_inputs.add(new Indicator_Input(rbiid, vars, varValues));
                		System.out.println("rbiid: "+rbiid+" vars: "+vars.toString()+" varValues: "+varValues.toString());
                		}
                	}
               
                	if(resIndex>-1 && taskIndex>-1)
                	{System.out.println("all tasks for all resources");
                		
                		String sql1 = "select distinct resource from eventlog";
                		final Statement dbStatement4 = con.createStatement();
                		ResultSet rs1 = dbStatement4.executeQuery(sql1);
                		rs1.beforeFirst();
                		while(rs1.next())
                		{
                		String nextres = rs1.getString(1);
                		varValues.set(resIndex,nextres);
                		String sql2 = "select distinct task from eventlog where resource='"+nextres+"'";
                		final Statement dbStatement5 = con.createStatement();
                		ResultSet rs2 = dbStatement5.executeQuery(sql2);
                		rs2.beforeFirst();
                		while(rs2.next())
                		{
                		String nextTask = rs2.getString(1);
                		varValues.set(taskIndex,nextTask);
                		ip.rbi_inputs.add(new Indicator_Input(rbiid, vars, varValues));
                		System.out.println("rbiid: "+rbiid+" vars: "+vars.toString()+" varValues: "+varValues.toString());
                		}
                 		}
               	  	}
                	
                	
                 	if(resIndex==-1 && taskIndex==-1)
                	{System.out.println("one resource one task");
                	ip.rbi_inputs.add(new Indicator_Input(rbiid, vars, varValues));
                	System.out.println("rbiid: "+rbiid+" vars: "+vars.toString()+" varValues: "+varValues.toString());
                	}
           
                	 
                	}catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                            JOptionPane.ERROR_MESSAGE);}
                    dispose(); }
                                   }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 




	
	public InputParameters defineVars(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> rbi) throws Exception
   {
	
	//Statement dbStatement = con.createStatement();
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify values of the variables used in the selected indicator: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<JTextField> varvalues = new Vector<JTextField>();

	for(int i=0; i<vars.size(); i++)
	{
		
		varnames.add(new JLabel(vars.elementAt(i)));  
		c.ipadx = 150; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = i+1;
		pane.add(varnames.elementAt(i), c);
		
		varvalues.add(new JTextField());
		c.gridx = 1;
		c.gridy = i+1;
		c.ipadx = 150;
		pane.add(varvalues.elementAt(i), c);

	}
	
	
	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = vars.size()+1;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   String rbiid = rbi.elementAt(0);
                	  
                	   Vector<String> varValues = new Vector<String>();
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                                             
                	  ip.rbi_inputs.add(new Indicator_Input(rbiid, vars, varValues));
                                        dispose(); }
                                   }
                           );
       
       setSize(400,400);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 


	
	public Vector<String> selectRBIVars(final Connection con, final Vector<String> rbi) throws Exception
   {
	
	Vector<String> allviewsnames = new Vector<String>();
	Vector<String> allviewsdefs = new Vector<String>();
	Vector<String> usedviews = new Vector<String>();
	Vector<String> allvars = new Vector<String>();
	Vector<String> usedvars = new Vector<String>();
	
	Statement dbStatement = con.createStatement();
	
	String sqlQuery = "SELECT * FROM views ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{allviewsnames.add(rs.getString("name"));
	allviewsdefs.add(rs.getString("definition"));}
	
	for (int i=0; i<allviewsnames.size(); i++)
	{	
		if(rbi.elementAt(2).contains(" "+allviewsnames.elementAt(i)+")") || rbi.elementAt(2).contains(" "+allviewsnames.elementAt(i)+" ") || (rbi.elementAt(2).contains(" "+allviewsnames.elementAt(i)) && rbi.elementAt(2).length() == rbi.elementAt(2).lastIndexOf(" "+allviewsnames.elementAt(i))+allviewsnames.elementAt(i).length()+1))
		{
			usedviews.add(allviewsdefs.elementAt(i));
		}
	}
	usedviews.add(rbi.elementAt(2));
	
	
	String sqlQuery2 = "SELECT * FROM vars ";
	
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{allvars.add(rs2.getString("name"));
	}

	for(int i=0; i<usedviews.size(); i++)
	{
		for(int j=0; j<allvars.size(); j++)
		{
			if(usedviews.elementAt(i).contains(allvars.elementAt(j)+"()") && !(usedvars.contains(allvars.elementAt(j))) && !(allvars.elementAt(j).equals("t1")) && !(allvars.elementAt(j).equals("t2")))
			{
				usedvars.add(allvars.elementAt(j));
				}
		}
	}
																		
  return usedvars;
} 




	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector<String> selectRBI(final Connection con) throws Exception
   {
	
	final Vector<String> theRBI = new Vector <String>();	
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final Vector<String> ids = new Vector<String>();
	final Vector<String> names = new Vector<String>();
	final Vector<String> definitions = new Vector<String>();

	
	Statement dbStatement = con.createStatement();

	String sqlQuery = "SELECT * FROM rbis ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	ids.add(rs.getString("id"));
	names.add(rs.getString("name"));
	definitions.add(rs.getString("definition"));
	}
	
		
	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select Resource Behaviour Indicator: </html>");
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final JComboBox jop = new JComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);

		
	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   	@SuppressWarnings("unused")
					Statement dbStatement;
                 		try {
							dbStatement = con.createStatement();
							
							String selectedRBIName = (String) jop.getSelectedItem();
							String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
							String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
							
							theRBI.add("rbi"+selectedRBIID);
							theRBI.add(selectedRBIName);
							theRBI.add(selectedRBIDefinition);
							
							dispose();
							
							} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
			                         JOptionPane.ERROR_MESSAGE);}
                                                }
                                   }
                           );
       
       setSize(600,200);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return theRBI;
   } 



}





