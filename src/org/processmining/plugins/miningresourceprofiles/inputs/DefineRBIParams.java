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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
public class DefineRBIParams extends JDialog{
	
	JDialog jd = this;
	
	public InputParameters defineDBParams(final InputParameters ip) throws Exception
   {
	
		
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	setUndecorated(true);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;
	
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(Color.WHITE);
	defVarsText.setText("<html><h3>Database parameters (DB will be created if not exists):</h3> </html>");
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab1 = new JLabel("Host name:");  
	lab1.setForeground(Color.WHITE);
	lab1.setAlignmentX(RIGHT_ALIGNMENT);
	c.ipadx = 50; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab1, c);
		
	final ProMTextField dbhost = new ProMTextField("localhost");
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 50;
	pane.add(dbhost, c);
		
		final JLabel lab2 = new JLabel("User name:");
		lab2.setForeground(Color.WHITE);

		c.ipadx = 50; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(lab2, c);
		
		final ProMTextField dbuser = new ProMTextField("root");
		dbuser.setAlignmentX(LEFT_ALIGNMENT);
		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 50;
		pane.add(dbuser, c);

		final JLabel lab3 = new JLabel("User password:");  
		lab3.setForeground(Color.WHITE);

		c.ipadx = 50; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(lab3, c);
		
		final ProMTextField dbpass = new ProMTextField("mysqlpass");
		c.gridx = 1;
		c.gridy = 4;
		c.ipadx = 50;
		pane.add(dbpass, c);

		final JLabel lab4 = new JLabel("Database name:");  
		lab4.setForeground(Color.WHITE);

		c.ipadx = 50; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(lab4, c);
		
		final ProMTextField dbname = new ProMTextField("rbitest");
		c.gridx = 1;
		c.gridy = 5;
		c.ipadx = 50;
		pane.add(dbname, c);

	
	SlickerButton but=new SlickerButton();
	but.setText("OK");
	c.gridx = 0;
	c.gridy = 7;
	c.gridwidth = 1;
	pane.add(but, c);
	
	SlickerButton but2=new SlickerButton();
	but2.setText("Cancel");
	c.gridx = 1;
	c.gridy = 7;
	c.gridwidth = 1;
	pane.add(but2, c);
  
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
       
       but2.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                 		jd.dispose();}
                                   }
                           );
       
       
       setSize(500,260);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSSlotParams(final InputParameters ip) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Modify TS slot Start and End: </h3></html>");
	c.ipadx = 300;      
	c.gridwidth = 5;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab0 = new JLabel("TS slotStart = t ");  
	lab0.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab0, c);
	
	DefaultComboBoxModel model1 = new DefaultComboBoxModel();
	model1.addElement("+");
	model1.addElement("-");
	
	final ProMComboBox startSign = new ProMComboBox(model1);
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 1;
	pane.add(startSign, c);
			
	final ProMTextField startNum = new ProMTextField("0");
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 2;
	c.gridy = 1;
	pane.add(startNum, c);
			
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("week");
	model.addElement("month");
	model.addElement("year");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("minute");
	model.addElement("second");
	   
	final ProMComboBox startUnit = new ProMComboBox(model);
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 3;
	c.gridy = 1;
	pane.add(startUnit, c);
	
	final JLabel lab01 = new JLabel("TS slotEnd = t + slotSize ");  
	lab01.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 30; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab01, c);
	
	DefaultComboBoxModel model11 = new DefaultComboBoxModel();
	model11.addElement("+");
	model11.addElement("-");
	
	final ProMComboBox endSign = new ProMComboBox(model11);
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 2;
	pane.add(endSign, c);
			
	final ProMTextField endNum = new ProMTextField("0");
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 2;
	c.gridy = 2;
	pane.add(endNum, c);
			
	DefaultComboBoxModel model2 = new DefaultComboBoxModel();
	model2.addElement("week");
	model2.addElement("month");
	model2.addElement("year");
	model2.addElement("day");
	model2.addElement("hour");
	model2.addElement("minute");
	model2.addElement("second");
	   
	final ProMComboBox endUnit = new ProMComboBox(model2);
	c.ipadx = 30;
	c.gridwidth = 1;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(endUnit, c);

	SlickerButton but=new SlickerButton();
	c.ipadx = 600;
	but.setText("Done");
	c.gridwidth = 5;
	c.gridx = 0;
	c.gridy = 3;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	   
                	   int startN = Integer.parseInt(startNum.getText());
                	   long startU = 0;
                	   boolean StartS = true;
                	   
                	   String startsign = (String) startSign.getSelectedItem();
                	   if(startsign.equals("+")){StartS = true;}else{StartS = false;}
                                	  
                	   String startslotunit = (String) startUnit.getSelectedItem();
                	   if(startslotunit.equals("year")){startU = (604800000l/7)*365;}
                	   else if(startslotunit.equals("month")){startU = (604800000l/7)*30;}
                	   else if(startslotunit.equals("week")){startU = 604800000l;}
                	   else if(startslotunit.equals("day")){startU = 604800000l/7;}
                	   else if(startslotunit.equals("hour")){startU = 604800000l/(7*24);}
                	   else if(startslotunit.equals("minute")){startU = 604800000l/(7*24*60);}
                	   else if(startslotunit.equals("second")){startU = 604800000l/(7*24*60*60);}
                	   
                	   if(StartS)
                	   ip.slotStartOffset = startN*startU;
                	   else
                	   ip.slotStartOffset = -startN*startU; 
                	   
                	   int endN = Integer.parseInt(endNum.getText());
                	   long endU = 0;
                	   boolean EndS = true;
                	   
                	   String endsign = (String) endSign.getSelectedItem();
                	   if(endsign.equals("+")){EndS = true;}else{EndS = false;}
                                	  
                	   String endslotunit = (String) endUnit.getSelectedItem();
                	   if(endslotunit.equals("year")){endU = (604800000l/7)*365;}
                	   else if(endslotunit.equals("month")){endU = (604800000l/7)*30;}
                	   else if(endslotunit.equals("week")){endU = 604800000l;}
                	   else if(endslotunit.equals("day")){endU = 604800000l/7;}
                	   else if(endslotunit.equals("hour")){endU = 604800000l/(7*24);}
                	   else if(endslotunit.equals("minute")){endU = 604800000l/(7*24*60);}
                	   else if(endslotunit.equals("second")){endU = 604800000l/(7*24*60*60);}
                	   
                	   if(EndS)
                	   ip.slotEndOffset = endN*endU;
                	   else
                	   ip.slotEndOffset = -endN*endU;   
                	   
                	  
                	              dispose(); }
                                   }
                           );
       
       setSize(900,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 

    //---------------------------------------------
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineTSParamsRes(final InputParameters ip, final Connection con) throws Exception
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
                	   
                	   ip.resource = (String)jopRes.getSelectedItem();
                	   
                	   ip.slotSize = unit*slotsize;
                	   System.out.println(dt+"---"+ip.numberOfSlots+"---"+ ip.slotSize + ip.resource);
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
	public InputParameters defineTSAnalysisParams(final InputParameters ip) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
			
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
			
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setText("<html><h3>Specify time series analysis parameters: </h3></html>");
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	c.ipadx = 300;      
	c.gridwidth = 2;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(defVarsText, c);
		
	final JLabel lab1 = new JLabel("Get change points?");  
	lab1.setForeground(UISettings.TextLight_COLOR);

	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 1;
	pane.add(lab1, c);
	
	 DefaultComboBoxModel model1 = new DefaultComboBoxModel();
     model1.addElement("yes");
     model1.addElement("no");
     final ProMComboBox cp = new ProMComboBox(model1);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 1;
	c.ipadx = 100;
	pane.add(cp, c);

		
	final JLabel lab2 = new JLabel("Get outliers?");  
	lab2.setForeground(UISettings.TextLight_COLOR);

	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(lab2, c);
	
	 DefaultComboBoxModel model2 = new DefaultComboBoxModel();
     model2.addElement("yes");
     model2.addElement("no");
     final ProMComboBox out = new ProMComboBox(model2);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(out, c);

	
	final JLabel lab3 = new JLabel("Estimate trend?"); 
	lab3.setForeground(UISettings.TextLight_COLOR);

	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 3;
	pane.add(lab3, c);
	
	 DefaultComboBoxModel model3 = new DefaultComboBoxModel();
     model3.addElement("yes");
     model3.addElement("no");
     final ProMComboBox trend = new ProMComboBox(model3);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 3;
	c.ipadx = 100;
	pane.add(trend, c);

	final JLabel lab4 = new JLabel("Change Point method:");  
	lab4.setForeground(UISettings.TextLight_COLOR);

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
     final ProMComboBox cpmtype = new ProMComboBox(model4);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 4;
	c.ipadx = 100;
	pane.add(cpmtype, c);


	final JLabel lab5 = new JLabel("ARL0:");  
	lab5.setForeground(UISettings.TextLight_COLOR);

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
     final ProMComboBox arl0 = new ProMComboBox(model5);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 5;
	c.ipadx = 100;
	pane.add(arl0, c);


	final JLabel lab6 = new JLabel("Startup:");  
	lab6.setForeground(UISettings.TextLight_COLOR);

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
     final ProMComboBox startup = new ProMComboBox(model6);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 6;
	c.ipadx = 100;
	pane.add(startup, c);

	final JLabel lab7 = new JLabel("Outlier identification method:");  
	lab7.setForeground(UISettings.TextLight_COLOR);

	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 7;
	pane.add(lab7, c);
	
	 DefaultComboBoxModel model7 = new DefaultComboBoxModel();
     model7.addElement("I");
     model7.addElement("II");
     final ProMComboBox method = new ProMComboBox(model7);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 7;
	c.ipadx = 100;
	pane.add(method, c);

	final JLabel lab8 = new JLabel("Trend estimation period:");  
	lab8.setForeground(UISettings.TextLight_COLOR);

	c.ipadx = 100; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 8;
	pane.add(lab8, c);
	
	 DefaultComboBoxModel model8 = new DefaultComboBoxModel();
     model8.addElement("recent");
     model8.addElement("full");
     final ProMComboBox period = new ProMComboBox(model8);
    c.gridwidth = 1;
	c.gridx = 1;
	c.gridy = 8;
	c.ipadx = 100;
	pane.add(period, c);

		

	SlickerButton but=new SlickerButton();
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
       
       setSize(700,350);
       setModal(true);
       setLocationRelativeTo(null);
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
       		
     	SlickerButton but2=new SlickerButton();
     	c.ipadx = 10;
     	but2.setText("TS");
     	c.gridwidth = 1;
     	c.gridx = 0;
     	c.gridy = 5;
     	pane.add(but2, c);
           
          	
            but2.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(
                                ActionEvent e) {
                        	
                  				try {
                  					
                  				DefineRBIParams defRBIParams = new DefineRBIParams();
                  				defRBIParams.defineTSSlotParams(ip);
								} catch (Exception e1) {e1.printStackTrace();}
					   	        //dispose(); 
					   	        }
                                        }
                                );
       
       setSize(700,330);
       setModal(true);
       setLocationRelativeTo(null);
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



	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters defineVarsAllRes(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> rbi) throws Exception
   {
	Vector<String> allResources = new Vector<String>();
	Vector<String> allTasks = new Vector<String>();

	Statement dbStatement1 = con.createStatement();
	String sqlQuery1 = "SELECT distinct resource from eventlog order by resource";
	ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
	rs1.beforeFirst();
	
	while(rs1.next())
	{	
	String resource = rs1.getString("resource");
	allResources.add(resource);
	}

	Statement dbStatement2 = con.createStatement();
	String sqlQuery2 = "SELECT distinct task from eventlog order by task";
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
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify values of the variables used in the selected RBI: </h3></html>");
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
		
	String[] model1 = new String[allResources.size()];
	
	for(int j=0; j<allResources.size(); j++)
	{
		model1[j] = allResources.elementAt(j);
	}
    
	final ProMCheckComboBox jopRes = new ProMCheckComboBox(model1);

	
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
				
	
	
	SlickerButton but=new SlickerButton();
	but.setText("Next");
	c.gridx = 0;
	c.gridy = count+2;
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
                 	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                	  
                  	  if(R1)
                	  {
                		 Vector<String> res = new Vector<String>();
                		 res.addAll(jopRes.getSelectedItems());
                  		 
                		 for(int i=0; i<res.size();i++)
                		  {
                			  String nextRes = res.elementAt(i);
                			  varValues.add(nextRes);
                		 	  ip.rbi_inputs.add(new Indicator_Input(rbiid, otherVars, varValues));
                		 	  System.out.println(varValues);
                		 	  varValues.remove(varValues.size()-1);
                              
                		  }
                  		 
                	  }
                                             
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
	public InputParameters defineVars(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> rbi) throws Exception
   {
	Vector<String> allResources = new Vector<String>();
	Vector<String> allTasks = new Vector<String>();

	Statement dbStatement1 = con.createStatement();
	String sqlQuery1 = "SELECT distinct resource from eventlog order by resource";
	ResultSet rs1 = dbStatement1.executeQuery(sqlQuery1);
	rs1.beforeFirst();
	
	while(rs1.next())
	{	
	String resource = rs1.getString("resource");
	allResources.add(resource);
	}

	Statement dbStatement2 = con.createStatement();
	String sqlQuery2 = "SELECT distinct task from eventlog order by task";
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
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify values of the variables used in the selected RBI: </h3></html>");
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
	but.setText("Next");
	c.gridx = 0;
	c.gridy = count+2;
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
                	  
                	  if(R1)
                	  {
                		  varValues.add((String)jopRes.getSelectedItem());
                	  }
                	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                
                                             
                	  ip.rbi_inputs.add(new Indicator_Input(rbiid, otherVars, varValues));
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




	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<String> selectRBI(final Connection con) throws Exception
   {
	
	final Vector<String> theRBI = new Vector <String>();	
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;

	
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
	SelectText.setForeground(UISettings.TextLight_COLOR);
	SelectText.setText("<html>&nbsp; &nbsp;Select Resource Behaviour Indicator: </html>");
	c.ipadx = 400; 
	c.gridx = 0;
	c.gridy = 0;
	pane.add(SelectText, c);
	
		
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
	 for(int i=0; i<names.size(); i++)
	 {
		 model.addElement(names.elementAt(i));
	 }
     final ProMComboBox jop = new ProMComboBox(model);
     c.gridx = 0;
 	 c.gridy = 1;
 	 pane.add(jop, c);

		
	SlickerButton but=new SlickerButton();
	but.setText("Next");
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
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return theRBI;
   } 




	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParameters selectMultRBI(final Connection con, final InputParameters ip) throws Exception
   {
	final Vector<String> selRBIs = new Vector<String>();	
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
	
	selectedRBIs.setText("<html>&nbsp; &nbsp; &nbsp; &nbsp;Please select RBIs you would like to include in the resource profile</html>");
	
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
	SelectText.setForeground(UISettings.TextLight_COLOR);
	SelectText.setText("<html>&nbsp; &nbsp;Select Resource Behaviour Indicator: </html>");
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
	but.setText("Select Indicator");
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 3;
	pane.add(but, c);
      
    but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   	@SuppressWarnings("unused")
					Statement dbStatement;
                 	try {
                 			
                 			Vector<String> theRBI = new Vector <String>();	

                 			dbStatement = con.createStatement();
							
							String selectedRBIName = (String) jop.getSelectedItem();
							String selectedRBIID = ids.elementAt(names.indexOf(selectedRBIName));
							String selectedRBIDefinition = definitions.elementAt(names.indexOf(selectedRBIName));
							
							theRBI.add("rbi"+selectedRBIID);
							theRBI.add(selectedRBIName);
							theRBI.add(selectedRBIDefinition);
							
							DefineRBIParams defRBIParams2 = new DefineRBIParams();
							DefineRBIParams defRBIParams3 = new DefineRBIParams();
				            
			            	Vector<String> vars = new Vector<String>();
			             	Vector<String> varValues = new Vector<String>();
			            	
		            		vars.addAll(defRBIParams2.selectRBIVars(con,theRBI));
		            		
		            		if(vars.contains("R1"))
		            		{vars.remove("R1");}
		            		
		            		if(vars.size() < 1)
		            		{varValues.add(ip.resource);
		            		vars.add("R1");
		            		ip.rbi_inputs.add(new Indicator_Input(theRBI.elementAt(0), vars, varValues));
		            		System.out.println("varValues R1: "+varValues);}
		            		else
		            		{
		            		ip.rbi_inputs.addAll(defRBIParams3.defineVarsProfile(con, ip, vars, theRBI));
		            		}
		                 		
							
							selRBIs.add(selectedRBIName+"<br/>");
							
							String theText = "<html>";
							for(int i=0; i<selRBIs.size(); i++)
							{
								theText += selRBIs.elementAt(i);
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
       
		
	SlickerButton okbut=new SlickerButton();
	okbut.setText("Submit");
	c.ipady = 20; 
	c.gridx = 0;
	c.gridy = 4;
	pane.add(okbut, c);
     
    	
	okbut.addActionListener(
              new ActionListener(){
                  public void actionPerformed(
                        ActionEvent e) {
          				dispose();} }
                          );
	
	
	
SlickerButton defProfile1but=new SlickerButton();
defProfile1but.setText("Get default profile - log only has complete events");
c.ipady = 20; 
c.gridx = 0;
c.gridy = 5;
pane.add(defProfile1but, c);
  
defProfile1but.addActionListener(
           new ActionListener(){
               public void actionPerformed(
                       ActionEvent e) {
                                       
               	@SuppressWarnings("unused")
				Statement dbStatement;
             	try {
             		Vector<String> vars = new Vector<String>();
	             	Vector<String> varValues = new Vector<String>();
	             	varValues.add(ip.resource);
            		vars.add("R1");
	            	
             		ip.rbi_inputs.add(new Indicator_Input("rbi1", vars, varValues));
             		ip.rbi_inputs.add(new Indicator_Input("rbi3", vars, varValues));	
             		ip.rbi_inputs.add(new Indicator_Input("rbi5", vars, varValues));	
             		ip.rbi_inputs.add(new Indicator_Input("rbi6", vars, varValues));	
             		ip.rbi_inputs.add(new Indicator_Input("rbi7", vars, varValues));	
             		ip.rbi_inputs.add(new Indicator_Input("rbi14", vars, varValues));	
             		ip.rbi_inputs.add(new Indicator_Input("rbi15", vars, varValues));	
                 	
             		dispose();
             		
	           
						
					}catch (Exception e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
                                            }
                               }
                       );
   

SlickerButton defProfile2but=new SlickerButton();
defProfile2but.setText("Get default profile - log has start and complete events");
c.ipady = 20; 
c.gridx = 0;
c.gridy = 6;
pane.add(defProfile2but, c);

defProfile2but.addActionListener(
       new ActionListener(){
           public void actionPerformed(
                   ActionEvent e) {
                                   
           	@SuppressWarnings("unused")
			Statement dbStatement;
         	try {
         			
         		Vector<String> vars = new Vector<String>();
             	Vector<String> varValues = new Vector<String>();
            	
            	varValues.add(ip.resource);
        		vars.add("R1");
            	
        		ip.rbi_inputs.add(new Indicator_Input("rbi1", vars, varValues));
         		ip.rbi_inputs.add(new Indicator_Input("rbi3", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi5", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi6", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi7", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi14", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi15", vars, varValues));
          		ip.rbi_inputs.add(new Indicator_Input("rbi8", vars, varValues));
         		ip.rbi_inputs.add(new Indicator_Input("rbi9", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi10", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi11", vars, varValues));	
         		ip.rbi_inputs.add(new Indicator_Input("rbi13", vars, varValues));	
         		
           		
         		dispose();
         	
  					
				}catch (Exception e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                                        }
                           }
                   );


	
       
       setSize(1170,750);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<Indicator_Input> defineVarsProfile(final Connection con, final InputParameters ip, final Vector<String> vars, final Vector<String> rbi) throws Exception
   {
	
	final Vector<Indicator_Input> rbi_inputs = new Vector<Indicator_Input>();
	
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
	defVarsText.setText("<html><h3>&nbsp; &nbsp;Specify values of the variables used in the selected RBI: </h3></html>");
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
		
	String[] model1 = new String[allResources.size()];
	
	for(int j=0; j<allResources.size(); j++)
	{
		model1[j] = allResources.elementAt(j);
	}
    
	final ProMCheckComboBox jopRes = new ProMCheckComboBox(model1);

	
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
				
	
	
	SlickerButton but=new SlickerButton();
	but.setText("Next");
	c.gridx = 0;
	c.gridy = count+2;
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
                 	  
                	  if(Task)
                	  {
                		  varValues.add((String)jopTask.getSelectedItem());
                	  }
                	  
                	  otherVars.add("R1");
                  	  varValues.add(ip.resource);
                 	  rbi_inputs.add(new Indicator_Input(rbiid, otherVars, varValues));
                	  System.out.println("varValues: "+varValues);
                	           
                      dispose(); }
                                   }
                           );
       
       setSize(700,300);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return rbi_inputs;
   } 


	

	
}





