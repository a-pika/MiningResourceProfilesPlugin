package org.processmining.plugins.miningresourceprofiles.inputs;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.analysis.RegressionData;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;
@SuppressWarnings("serial")
public class DefineRegressionParams extends JDialog{
	
	public Vector<String> selectRegressionVars(final Connection con, final Vector<String> regviews) throws Exception
   {
	
	Vector<String> allviewsnames = new Vector<String>();
	Vector<String> allviewsdefs = new Vector<String>();
	Vector<String> usedviews = new Vector<String>();
	Vector<String> allvars = new Vector<String>();
	Vector<String> usedvars = new Vector<String>();
	Vector<String> usedregviewsDefs = new Vector<String>();

	
	
	Statement dbStatement = con.createStatement();
	
	String sqlQuery0 = "SELECT * FROM regressionviews";
	
	ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
	rs0.beforeFirst();
	
	while(rs0.next())
	{
		String curID = rs0.getString("id");
		String curDef = rs0.getString("definition");
		if (regviews.contains(curID))
		{usedregviewsDefs.add(curDef);}
	}
	
	String sqlQuery = "SELECT * FROM views ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{allviewsnames.add(rs.getString("name"));
	allviewsdefs.add(rs.getString("definition"));}
	
	for(int j=0; j<usedregviewsDefs.size(); j++)
	{	String curRegViewDef = usedregviewsDefs.elementAt(j);
	for (int i=0; i<allviewsnames.size(); i++)
	{
		if(curRegViewDef.contains(" "+allviewsnames.elementAt(i)+" ") || (curRegViewDef.contains(" "+allviewsnames.elementAt(i)) && curRegViewDef.length() == curRegViewDef.lastIndexOf(" "+allviewsnames.elementAt(i))+allviewsnames.elementAt(i).length()+1))
		{
			usedviews.add(allviewsdefs.elementAt(i));
		}
	}
	usedviews.add(curRegViewDef);
	}
	
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
			if(usedviews.elementAt(i).contains(allvars.elementAt(j)+"()") && !(usedvars.contains(allvars.elementAt(j))) && !(allvars.elementAt(j).equals("t1")) && !(allvars.elementAt(j).equals("t2")) && !(allvars.elementAt(j).equals("c")) && !(allvars.elementAt(j).equals("eventid")))
			{
				usedvars.add(allvars.elementAt(j));
			}
		}
	}
																		
  return usedvars;
} 



	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineVars(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> regviews) throws Exception
   {
	Vector<String> allResources = new Vector<String>();
	Vector<String> allTasks = new Vector<String>();

	Statement dbStatement1 = con.createStatement();
	String sqlQuery1 = "SELECT distinct resource from eventlog";
	ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
	rs1.beforeFirst();
	
	while(rs1.next())
	{	
	String resource = rs1.getString("resource");
	allResources.add(resource);
	}

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
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify values of the variables used in the selected regression views: </h3></html>");
	c.ipadx = 400;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final Vector<JLabel> varnames = new Vector<JLabel>();
	final Vector<ProMTextField> varvalues = new Vector<ProMTextField>();
	final Vector<String> otherVars = new Vector<String>();

	final boolean R1;
	final boolean Task;
	
	if(vars.contains("R1")){R1 = true;}else{R1 = false;};
	if(vars.contains("Task")){Task = true;}else{Task = false;};
	int count = 0;
	
	System.out.println("Vars: "+vars);
	System.out.println("R1: "+R1);
	System.out.println("Task: "+Task);
	
	
	
	for(int i=0; i<vars.size(); i++)
	{
		if(!(vars.elementAt(i).equals("R1")) && !(vars.elementAt(i).equals("Task")))
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
		
	DefaultComboBoxModel model1 = new DefaultComboBoxModel();
	for(int j=0; j<allResources.size(); j++)
	{
		model1.addElement(allResources.elementAt(j));
	}
    final ProMComboBox jopRes = new ProMComboBox(model1);

	
	DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	for(int j=0; j<allTasks.size(); j++)
	{
		model2.addElement(allTasks.elementAt(j));
	}
    final ProMComboBox jopTask = new ProMComboBox(model2);

    
	if(R1)
			{
		otherVars.add("R1");		
		
		JLabel R1lab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;R1</html>");  
		R1lab.setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = count+2;
		pane.add(R1lab, c);
	
			 	c.gridx = 1;
				c.gridy = count+2;
				c.ipadx = 200;
				pane.add(jopRes, c);
count++;

			}
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
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = count+2;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	  
                	   Vector<String> varValues = new Vector<String>();
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                	  
                	  if(R1)
                	  {
                		  varValues.add((String)jopRes.getSelectedItem());
                	  }
                	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                
                   	  for(int i=0; i<regviews.size(); i++)
                	  {ip.regressionInputs.add(new RegressionData("regview"+regviews.elementAt(i),vars,varValues));}
                	
                   	  ip.decNum = 3; 		
       	                         
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



	
	public InputParameters defineVarsPrev(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> regviews) throws Exception
   {
	
	//Statement dbStatement = con.createStatement();
		
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	final JLabel lab9 = new JLabel("Number of decimals after point:");  
	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(lab9, c);
	
	final JTextField decimals = new JTextField();
	c.gridx = 1;
	c.gridy = 0;
	c.ipadx = 100;
	pane.add(decimals, c);
		
	
	final JLabel defVarsText=new JLabel();
	if(vars.size() == 0)
	{defVarsText.setText("<html><h3>No variables are used </h3></html>");}else
	{defVarsText.setText("<html><h3>Specify values of the variables used in the selected views: </h3></html>");}
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(defVarsText, c);
		
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
                	  
                	  Vector<String> varValues = new Vector<String>();
                	  for(int i=0; i<varvalues.size(); i++)
                	  {varValues.add(varvalues.elementAt(i).getText());} 
                                             
                	  for(int i=0; i<regviews.size(); i++)
                	  {ip.regressionInputs.add(new RegressionData("regview"+regviews.elementAt(i),vars,varValues));}
                	
                	ip.decNum = Integer.parseInt(decimals.getText()); 		
       	  
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
	public InputParameters defineTSParams(final InputParameters ip, final Connection con) throws Exception
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
                		ip.numberOfSlots = Integer.parseInt(numslots.getText()); 	
                		ip.decNum = 3; 		
    	  	
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
                	   
                	   ip.slotSize = unit*slotsize;
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize);
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
	public InputParameters defineTSParamsPrev(final InputParameters ip) throws Exception
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
		


	JButton but=new JButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = 11;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                		DateTime dt = new DateTime(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText()), Integer.parseInt(second.getText())); // 
                		ip.startTime = dt.getMillis();
                		ip.numberOfSlots = Integer.parseInt(numslots.getText());	
                		
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
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> selectTimeViews(final Connection con, final InputParameters ip) throws Exception
   {
		final HeaderBar pane = new HeaderBar("");	
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
					
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
	
	final Vector<String> regviews = new Vector<String>();	
	final Vector<String> idsOut = new Vector<String>();
	final Vector<String> namesOut = new Vector<String>();
	final Vector<String> idsBehaviour = new Vector<String>();
	final Vector<String> namesBehaviour = new Vector<String>();

	Statement dbStatement = con.createStatement();
		
	String sqlQuery2 = "SELECT * FROM regressionviews WHERE type='time_outcome'";
	
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{	
	idsOut.add(rs2.getString("id"));
	namesOut.add(rs2.getString("name"));
	
	}
	
	String sqlQuery3 = "SELECT * FROM regressionviews WHERE type='time_behaviour'";
	ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
	rs3.beforeFirst();
		
	while(rs3.next())
	{	
		idsBehaviour.add(rs3.getString("id"));
		namesBehaviour.add(rs3.getString("name"));
	}
		 
 	final JLabel SelectText2=new JLabel();
 	SelectText2.setText("<html>Select Time Outcome View: </html>");
	SelectText2.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
 	c.gridx = 0;
 	c.gridy = 0;
 	pane.add(SelectText2, c);
 	
 		
 	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
 	 for(int i=0; i<namesOut.size(); i++)
 	 {
 		 model2.addElement(namesOut.elementAt(i));
 	 }
      final ProMComboBox jop2 = new ProMComboBox(model2);
      c.gridx = 0;
  	 c.gridy = 1;
  	 pane.add(jop2, c);
  	 
  	final JLabel SelectText3=new JLabel();
 	SelectText3.setText("<html>Select Time Behaviour Views: </html>");
	SelectText3.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
 	c.gridx = 0;
 	c.gridy = 2;
 	pane.add(SelectText3, c);
 
 	
 	Vector<DefaultComboBoxModel> expModels = new Vector<DefaultComboBoxModel>();
 	final Vector<ProMComboBox> expJCB = new Vector<ProMComboBox>();
 		
 	for (int i=0; i<ip.numberExpVars; i++)
 	{
 		 for(int j=0; j<namesBehaviour.size(); j++)
 	 	 {
 			expModels.add(new DefaultComboBoxModel());
 			expModels.elementAt(i).addElement(namesBehaviour.elementAt(j));
 	 	 }
 		 
 		expJCB.add(new ProMComboBox(expModels.elementAt(i)));
 		c.gridx = 0;
 		c.gridy = i+3;
 		pane.add(expJCB.elementAt(i), c);

 	}

 	SlickerButton but=new SlickerButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = ip.numberExpVars+3;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
              				String selectedNameOut = (String) jop2.getSelectedItem();
							String selectedIDOut = idsOut.elementAt(namesOut.indexOf(selectedNameOut));
							regviews.add(selectedIDOut);	
					
							Vector<String> selectedNameBeh = new Vector<String>();
							Vector<String> selectedNIDBeh = new Vector<String>();
							
							for(int i=0; i<ip.numberExpVars; i++)
							{
								selectedNameBeh.add((String) expJCB.elementAt(i).getSelectedItem());
								selectedNIDBeh.add(idsBehaviour.elementAt(namesBehaviour.indexOf(selectedNameBeh.elementAt(i))));
								regviews.add(selectedNIDBeh.elementAt(i));	
							}
							
							dispose();
							
                                                }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return regviews;
   } 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> selectTaskViews(final Connection con, final InputParameters ip) throws Exception
   {
		final HeaderBar pane = new HeaderBar("");	
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
					
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		
		
	final Vector<String> regviews = new Vector<String>();	
	final Vector<String> idsDef = new Vector<String>();
	final Vector<String> namesDef = new Vector<String>();
	final Vector<String> idsOut = new Vector<String>();
	final Vector<String> namesOut = new Vector<String>();
	final Vector<String> idsBehaviour = new Vector<String>();
	final Vector<String> namesBehaviour = new Vector<String>();


	
	Statement dbStatement = con.createStatement();

	String sqlQuery = "SELECT * FROM regressionviews WHERE type='task_definition'";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	idsDef.add(rs.getString("id"));
	namesDef.add(rs.getString("name"));
	
	}
	
	String sqlQuery2 = "SELECT * FROM regressionviews WHERE type='task_outcome'";
	
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{	
	idsOut.add(rs2.getString("id"));
	namesOut.add(rs2.getString("name"));
	
	}
	
	String sqlQuery3 = "SELECT * FROM regressionviews WHERE type='task_behaviour'";
	ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
	rs3.beforeFirst();
		
	while(rs3.next())
	{	
		idsBehaviour.add(rs3.getString("id"));
		namesBehaviour.add(rs3.getString("name"));
	}


	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select Task Definition View: </html>");
	SelectText.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<namesDef.size(); i++)
	 {
		 model.addElement(namesDef.elementAt(i));
	 }
     final ProMComboBox jop = new ProMComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);

 	 
 	final JLabel SelectText2=new JLabel();
 	SelectText2.setText("<html>Select Task Outcome View: </html>");
	SelectText2.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
  	c.gridx = 0;
 	c.gridy = 2;
 	pane.add(SelectText2, c);
 	
 		
 	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
 	 for(int i=0; i<namesOut.size(); i++)
 	 {
 		 model2.addElement(namesOut.elementAt(i));
 	 }
      final ProMComboBox jop2 = new ProMComboBox(model2);
      c.gridx = 0;
  	 c.gridy = 3;
  	 pane.add(jop2, c);
  	 
  	final JLabel SelectText3=new JLabel();
 	SelectText3.setText("<html>Select Task Behaviour Views: </html>");
	SelectText3.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
 	c.gridx = 0;
 	c.gridy = 4;
 	pane.add(SelectText3, c);
 
 	
 	Vector<DefaultComboBoxModel> expModels = new Vector<DefaultComboBoxModel>();
 	final Vector<ProMComboBox> expJCB = new Vector<ProMComboBox>();
 		
 	for (int i=0; i<ip.numberExpVars; i++)
 	{
 		 for(int j=0; j<namesBehaviour.size(); j++)
 	 	 {
 			expModels.add(new DefaultComboBoxModel());
 			expModels.elementAt(i).addElement(namesBehaviour.elementAt(j));
 	 	 }
 		 
 		expJCB.add(new ProMComboBox(expModels.elementAt(i)));
 		c.gridx = 0;
 		c.gridy = i+5;
 		pane.add(expJCB.elementAt(i), c);

 	}

 	SlickerButton but=new SlickerButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = ip.numberExpVars+5;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
             				String selectedNameDef = (String) jop.getSelectedItem();
							String selectedIDDef = idsDef.elementAt(namesDef.indexOf(selectedNameDef));
							regviews.add(selectedIDDef);	
							String selectedNameOut = (String) jop2.getSelectedItem();
							String selectedIDOut = idsOut.elementAt(namesOut.indexOf(selectedNameOut));
							regviews.add(selectedIDOut);	
					
							Vector<String> selectedNameBeh = new Vector<String>();
							Vector<String> selectedNIDBeh = new Vector<String>();
							
							for(int i=0; i<ip.numberExpVars; i++)
							{
								selectedNameBeh.add((String) expJCB.elementAt(i).getSelectedItem());
								selectedNIDBeh.add(idsBehaviour.elementAt(namesBehaviour.indexOf(selectedNameBeh.elementAt(i))));
								regviews.add(selectedNIDBeh.elementAt(i));	
							}
							
							dispose();
							
                                                }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return regviews;
   } 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> selectCaseViews(final Connection con, final InputParameters ip) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
				
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
	
	final Vector<String> regviews = new Vector<String>();	
	final Vector<String> idsDef = new Vector<String>();
	final Vector<String> namesDef = new Vector<String>();
	final Vector<String> idsOut = new Vector<String>();
	final Vector<String> namesOut = new Vector<String>();
	final Vector<String> idsBehaviour = new Vector<String>();
	final Vector<String> namesBehaviour = new Vector<String>();

	
	Statement dbStatement = con.createStatement();
	
	String sqlQuery = "SELECT * FROM regressionviews WHERE type='case_definition'";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	
	while(rs.next())
	{	
	idsDef.add(rs.getString("id"));
	namesDef.add(rs.getString("name"));
	
	}
	
	String sqlQuery2 = "SELECT * FROM regressionviews WHERE type='case_outcome'";
	
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	rs2.beforeFirst();
	
	while(rs2.next())
	{	
	idsOut.add(rs2.getString("id"));
	namesOut.add(rs2.getString("name"));
	
	}
	
	String sqlQuery3 = "SELECT * FROM regressionviews WHERE type='case_behaviour'";
	ResultSet rs3 = dbStatement.executeQuery(sqlQuery3);
	rs3.beforeFirst();
		
	while(rs3.next())
	{	
		idsBehaviour.add(rs3.getString("id"));
		namesBehaviour.add(rs3.getString("name"));
	}


	
	final JLabel SelectText=new JLabel();
	SelectText.setText("<html>Select Case Definition View: </html>");
	SelectText.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<namesDef.size(); i++)
	 {
		 model.addElement(namesDef.elementAt(i));
	 }
     final ProMComboBox jop = new ProMComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);

 	 
 	final JLabel SelectText2=new JLabel();
 	SelectText2.setText("<html>Select Case Outcome View: </html>");
	SelectText2.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
 	c.gridx = 0;
 	c.gridy = 2;
 	pane.add(SelectText2, c);
 	
 		
 	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
 	 for(int i=0; i<namesOut.size(); i++)
 	 {
 		 model2.addElement(namesOut.elementAt(i));
 	 }
      final ProMComboBox jop2 = new ProMComboBox(model2);
      c.gridx = 0;
  	 c.gridy = 3;
  	 pane.add(jop2, c);
  	 
  	final JLabel SelectText3=new JLabel();
 	SelectText3.setText("<html>Select Case Behaviour Views: </html>");
	SelectText3.setForeground(UISettings.TextLight_COLOR);

 	c.ipadx = 200; 
 	c.gridx = 0;
 	c.gridy = 4;
 	pane.add(SelectText3, c);
 
 	
 	Vector<DefaultComboBoxModel> expModels = new Vector<DefaultComboBoxModel>();
 	final Vector<ProMComboBox> expJCB = new Vector<ProMComboBox>();
 		
 	for (int i=0; i<ip.numberExpVars; i++)
 	{
 		 for(int j=0; j<namesBehaviour.size(); j++)
 	 	 {
 			expModels.add(new DefaultComboBoxModel());
 			expModels.elementAt(i).addElement(namesBehaviour.elementAt(j));
 	 	 }
 		 
 		expJCB.add(new ProMComboBox(expModels.elementAt(i)));
 		c.gridx = 0;
 		c.gridy = i+5;
 		pane.add(expJCB.elementAt(i), c);

 	}

 	SlickerButton but=new SlickerButton();
	but.setText("Submit");
	c.gridx = 0;
	c.gridy = ip.numberExpVars+5;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                 			String selectedNameDef = (String) jop.getSelectedItem();
							String selectedIDDef = idsDef.elementAt(namesDef.indexOf(selectedNameDef));
							regviews.add(selectedIDDef);	
							String selectedNameOut = (String) jop2.getSelectedItem();
							String selectedIDOut = idsOut.elementAt(namesOut.indexOf(selectedNameOut));
							regviews.add(selectedIDOut);	
					
							Vector<String> selectedNameBeh = new Vector<String>();
							Vector<String> selectedNIDBeh = new Vector<String>();
							
							for(int i=0; i<ip.numberExpVars; i++)
							{
								selectedNameBeh.add((String) expJCB.elementAt(i).getSelectedItem());
								selectedNIDBeh.add(idsBehaviour.elementAt(namesBehaviour.indexOf(selectedNameBeh.elementAt(i))));
								regviews.add(selectedNIDBeh.elementAt(i));	
							}
							
							dispose();
							
                                                }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return regviews;
   } 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters selectPerspective(final InputParameters ip) throws Exception
   {
	
		final HeaderBar pane = new HeaderBar("");
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
				
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;

	
	
	final JLabel SelectText1=new JLabel();
	SelectText1.setText("<html>Select perspective: </html>");
	SelectText1.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText1, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 model.addElement("case");
	 model.addElement("task");
	 model.addElement("time");
	 final ProMComboBox perspective = new ProMComboBox(model);
	 c.ipadx = 100;
	 c.gridx = 1;
 	 c.gridy = 0;
 	 pane.add(perspective, c);
 	 
 	final JLabel SelectText2=new JLabel();
 	SelectText2.setText("<html>Number of independent variables: </html>");
 	SelectText2.setForeground(UISettings.TextLight_COLOR);
 	c.ipadx = 200; 
 	c.gridwidth = 1;
 	c.gridx = 0;
 	c.gridy = 1;
 	pane.add(SelectText2, c);
 	
 	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	 for(int i=1; i<21; i++)
	 {model2.addElement(i);}
 	 final ProMComboBox expVars = new ProMComboBox(model2);
 	 c.ipadx = 100; 
 	 c.gridx = 1;
 	 c.gridy = 1;
 	 pane.add(expVars, c);


 	final JLabel SelectReg=new JLabel();
 	SelectReg.setText("<html>Select regression type: </html>");
 	SelectReg.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 200; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(SelectReg, c);
	
		
	 DefaultComboBoxModel SelectRegmodel = new DefaultComboBoxModel();
	 SelectRegmodel.addElement("linear");
	 SelectRegmodel.addElement("non-parametric");
	 final ProMComboBox regType = new ProMComboBox(SelectRegmodel);
	 c.ipadx = 100;
	 c.gridx = 1;
 	 c.gridy = 2;
 	 pane.add(regType, c);

		
	SlickerButton but=new SlickerButton();
	but.setText("Next");
	c.gridx = 0;
	c.gridy = 3;
	c.gridwidth = 2;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                     			
							ip.regType = (String) perspective.getSelectedItem();
							ip.regressionMethod = (String) regType.getSelectedItem();
							ip.numberExpVars = (Integer) expVars.getSelectedItem();
							dispose();
						
                                                }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 




}





