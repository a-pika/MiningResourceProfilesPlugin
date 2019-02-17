package org.processmining.plugins.miningresourceprofiles.bp;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.jfree.chart.ChartPanel;
import org.joda.time.DateTime;
import org.processmining.framework.util.ui.widgets.ProMCheckComboBox;
import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

@SuppressWarnings("serial")
public class DefineBPparameters extends JDialog{
	
	@SuppressWarnings({ "unchecked" })
	public InputParametersBP getBPparameters(final InputParametersBP ip, XLog log) throws Exception
   {
		final Vector <String> eventData = new Vector<String>();
		List<XAttribute> eal = log.getGlobalEventAttributes(); 
		
		HashSet<String> ea = new HashSet<String>();
		ea.add("concept:name");
		ea.add("lifecycle:transition");
		ea.add("org:resource");
		ea.add("time:timestamp");
		ea.add("Activity");
		ea.add("Resource");
		
		for (int i=0; i<eal.size();i++)
			{
				String next = eal.get(i).getKey();
				if (!ea.contains(next))
					eventData.add(next);
			}
		
		HashSet<String> resources = new HashSet<String>();
		HashSet<String> activities = new HashSet<String>();
		Vector<String> allResources = new Vector<String>();
		Vector<String> allActivities = new Vector<String>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				if(e.getAttributes().get("org:resource") != null)
				resources.add(e.getAttributes().get("org:resource").toString());
				
				if(e.getAttributes().get("concept:name") != null)
				activities.add(e.getAttributes().get("concept:name").toString());
				
			}
			
		}
		
		for(String r : resources)
			allResources.add(r);
		
		for(String a : activities)
			allActivities.add(a);
		
		Collections.sort(allResources);
		Collections.sort(allActivities);
		
		///////////////////////////////////
		
		final HeaderBar pane = new HeaderBar("");	
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.RELATIVE;

		int count = 0;
		
		final JLabel defVarsText=new JLabel();
		defVarsText.setForeground(UISettings.TextLight_COLOR);
		defVarsText.setText("<html><h3>&nbsp; &nbsp;Select activities, resources and data: </h3></html>");
		c.ipadx = 400;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(defVarsText, c);
		
		String[] model1 = new String[allResources.size()+1];
		model1[0] = "NONE";
		
		for(int j=0; j<allResources.size(); j++)
			model1[j+1] = allResources.elementAt(j);
		
	 	final ProMCheckComboBox jopRes = new ProMCheckComboBox(model1);
	 	
	 	
	 	String[] model2 = new String[allActivities.size()];
		
		for(int j=0; j<allActivities.size(); j++)
			model2[j] = allActivities.elementAt(j);
		
	 	final ProMCheckComboBox jopTask = new ProMCheckComboBox(model2);

	 	
		JLabel Tasklab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Activity</html>");  
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
		
		JLabel R1lab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Resource</html>");  
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
		
		///////////////// Case data ////////////////
		
		final Vector<ProMCheckComboBox> dmb = new Vector<ProMCheckComboBox>();
		
		for(int i=0; i<eventData.size();i++)
		{
			
			JLabel dataLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+eventData.elementAt(i)+"</html>");  
			dataLab.setForeground(UISettings.TextLight_COLOR);
			c.ipadx = 200; 
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = count+2;
			pane.add(dataLab, c);
			
			HashSet<String> dataValues = new HashSet<String>();
			Vector<String> allValues = new Vector<String>();
			
			for (XTrace t : log) 
			{
				for (XEvent e : t) 
				{
					if(e.getAttributes().get(eventData.elementAt(i)) != null)
						dataValues.add(e.getAttributes().get(eventData.elementAt(i)).toString());
				}
			}
			
			for(String v : dataValues)
				allValues.add(v);
			
			Collections.sort(allValues);
			
			String[] dataModel = new String[allValues.size()+1];
			
			dataModel[0] = "NONE";
			
			for(int j=0; j<allValues.size(); j++)
				dataModel[j+1] = allValues.elementAt(j);
			
			
			final ProMCheckComboBox jopData = new ProMCheckComboBox(dataModel);
		    dmb.add(jopData);
		    
			c.gridx = 1;
			c.gridy = count+2;
			c.ipadx = 200;
			pane.add(dmb.elementAt(i), c);
			
			count++;
			
		}
		
		
		////////////////////////////////////////////
		
		SlickerButton but=new SlickerButton();
		but.setText("OK");
		c.gridx = 0;
		c.gridy = count+2;
		c.gridwidth = 2;
		pane.add(but, c);
		
	     but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	  
	                	   Vector<String> selectedResources = new Vector<String>();
	                	   selectedResources.addAll(jopRes.getSelectedItems());
	                	   selectedResources.remove("NONE");
	                	   
	              	     if(selectedResources.size()>0)
            	    	 {
            	    	 	ip.bpResource.addAll(selectedResources);
            	    	 	ip.resource = true;
            	    	 }
            
	              	     	ip.bpActivity.addAll(jopTask.getSelectedItems());
	                 		 
	              	  
	                 		 
	                 		 for(int i=0; i<eventData.size(); i++)
	                 		 {
	                 			 String attr = eventData.elementAt(i);
	                 			 Vector<String> values = new Vector<String>();
	                 			 values.addAll(dmb.elementAt(i).getSelectedItems());
	                 			 
	                 			values.remove("NONE");
	                 			 
	                 			 if(values.size()>0)
	                 			 {
	                 				 ip.bpData.put(attr, values);
	                 				 ip.caseData = true;
	                 			 }
	                 		 }
	                 		
	                 		 dispose(); }
	                                }
	                           );
	       
	       setSize(800,500);
	       setModal(true);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	return ip;
   } 

	
	@SuppressWarnings({ "unchecked" })
	public InputParametersBP getBPparametersConfiguration(final InputParametersBP ip) throws Exception
   {
			
		final HeaderBar pane = new HeaderBar("");	
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.RELATIVE;

		final JLabel bpConfText=new JLabel();
		bpConfText.setForeground(UISettings.TextLight_COLOR);
		bpConfText.setText("<html><h3>&nbsp; &nbsp;Batch processing configuration</h3></html>");
		c.ipadx = 400;      
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(bpConfText, c);
		
		// Minimum batch size
		
		DefaultComboBoxModel model1 = new DefaultComboBoxModel();
		for(int j=0; j<99; j++)
		{
			Integer val = j+2;
			model1.addElement(val.toString());
		}
	
		final ProMComboBox batchSize = new ProMComboBox(model1);
		
		JLabel batchSizeLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Minimum batch size:</html>");  
		batchSizeLab.setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 100; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(batchSizeLab, c);

		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 100;
		pane.add(batchSize, c);
		
		// Interruptions
		
		DefaultComboBoxModel model2 = new DefaultComboBoxModel();
		model2.addElement("Unlimited");
		for(int j=0; j<100; j++)
		{
			Integer val = j+1;
			model2.addElement(val.toString()+"%");
		}
		model2.addElement("200%");
		model2.addElement("300%");
		model2.addElement("400%");
		model2.addElement("500%");
		model2.addElement("1000%");
	
		final ProMComboBox interruptions = new ProMComboBox(model2);
		
		JLabel interruptionsLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Interruptions tolerance:</html>");  
		interruptionsLab.setForeground(UISettings.TextLight_COLOR);
		//c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(interruptionsLab, c);

		c.gridx = 1;
		c.gridy = 3;
		//c.ipadx = 200;
		pane.add(interruptions, c);
		
		// Time unit
		
		DefaultComboBoxModel model3 = new DefaultComboBoxModel();
		model3.addElement("1 hour");
		model3.addElement("1 day");
		model3.addElement("1 week");
		model3.addElement("1 millisecond");	
		model3.addElement("1 second");
		model3.addElement("1 minute");
				
		final ProMComboBox timeUnit = new ProMComboBox(model3);
				
		JLabel timeUnitLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Time unit:</html>");  
		timeUnitLab.setForeground(UISettings.TextLight_COLOR);
		//c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		pane.add(timeUnitLab, c);

		c.gridx = 1;
		c.gridy = 4;
		//c.ipadx = 200;
		pane.add(timeUnit, c);
		
	// Periodic patterns
		
		DefaultComboBoxModel model4 = new DefaultComboBoxModel();
		model4.addElement("Do not detect");
		model4.addElement("Consider batch start times");
		model4.addElement("Consider batch duration");
				
		final ProMComboBox periodicity = new ProMComboBox(model4);
				
		JLabel periodicityLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Periodic patterns:</html>");  
		periodicityLab.setForeground(UISettings.TextLight_COLOR);
		//c.ipadx = 200; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		pane.add(periodicityLab, c);

		c.gridx = 1;
		c.gridy = 5;
		//c.ipadx = 200;
		pane.add(periodicity, c);
		
	  	
		// Modify time slot size check box
  	  	
		final JCheckBox tsCheckBox = new JCheckBox("Configure time slot size");
		tsCheckBox.setBackground(Color.BLACK);
		tsCheckBox.setForeground(UISettings.TextLight_COLOR);
		c.gridx = 0;
		c.gridy = 6;
		//c.ipadx = 200;
		c.gridwidth = 2;
		pane.add(tsCheckBox, c);
	

		// Working time
	
		final JCheckBox workingTime = new JCheckBox("Consider working time");
		workingTime.setBackground(Color.BLACK);
		workingTime.setForeground(UISettings.TextLight_COLOR);
		c.gridx = 0;
		c.gridy = 7;
		//c.ipadx = 200;
		c.gridwidth = 2;
		pane.add(workingTime, c);
		
		// Merge chunks
		
		final JCheckBox merge = new JCheckBox("Merge similar chunks");
		merge.setBackground(Color.BLACK);
		merge.setForeground(UISettings.TextLight_COLOR);
		c.gridx = 0;
		c.gridy = 8;
		//c.ipadx = 200;
		c.gridwidth = 2;
		pane.add(merge, c);
		
		// Outliers
		
		final JCheckBox outliers = new JCheckBox("Detect outliers");
		outliers.setBackground(Color.BLACK);
		outliers.setForeground(UISettings.TextLight_COLOR);
		c.gridx = 0;
		c.gridy = 9;
		//c.ipadx = 200;
		c.gridwidth = 2;
		pane.add(outliers, c);
		
		////////////////////////////////////////////
		
		SlickerButton but=new SlickerButton();
		but.setText("OK");
		c.gridx = 0;
		c.gridy = 10;
		c.gridwidth = 2;
		pane.add(but, c);
		
	     but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	  
	                	   String timeUnitVal = (String) timeUnit.getSelectedItem();
	                	   if(timeUnitVal.equals("1 millisecond"))
	                		   ip.timeUnit = 1;
	                	   if(timeUnitVal.equals("1 second"))
	                		   ip.timeUnit = 1000;
	                	   if(timeUnitVal.equals("1 minute"))
	                		   ip.timeUnit = 60000;
	                	   if(timeUnitVal.equals("1 hour"))
	                		   ip.timeUnit = 3600000; 
	                	   if(timeUnitVal.equals("1 day"))
	                		   ip.timeUnit = 3600000*24; 
	                	   if(timeUnitVal.equals("1 week"))
	                		   ip.timeUnit = 3600000*24*7; 
	                	
	                	   String batchSizeVal = (String) batchSize.getSelectedItem();
	                	   ip.minNumberOfCases = Integer.parseInt(batchSizeVal);
	                	   
	                	   String interruptionsVal = (String) interruptions.getSelectedItem();
	                	   
	                	   if(interruptionsVal.equals("Unlimited"))
	                		   ip.checkInterruptions = false;
	                	   else
	                	   {
	                		   ip.checkInterruptions = true;
	                		   String inVal = interruptionsVal.substring(0, interruptionsVal.length()-1);
	                		   Double inValD = Double.parseDouble(inVal);
	                 		   ip.interruptionsTolerance = inValD/100;
	                	   }
	                	   
	                	   String periodicityVal = (String) periodicity.getSelectedItem();
	                	   
	                	   if(periodicityVal.equals("Do not detect"))
	                		   ip.detectSeasonality = false;
	                	   else
	                	   {
	                		   ip.detectSeasonality = true;
	                		   if(periodicityVal.equals("Consider batch start times"))
	                			   ip.periodicityConsiderBatchStart = true;
	                		   else 
	                			   ip.periodicityConsiderBatchStart = false;
	                 	   }
	                 	  		 
	                 		if(outliers.isSelected())
	                 			ip.detectOutliers = true;
	                 		else
	                 			ip.detectOutliers = false;
	                 		
	                 		if(merge.isSelected())
	                 			ip.mergeChunks = true;
	                 		else
	                 			ip.mergeChunks = false;
	                 	
	                 		if(workingTime.isSelected())
	                 			ip.considerWorkingTime = true;
	                 		else
	                 			ip.considerWorkingTime = false;     
	                 		
	                 		if(tsCheckBox.isSelected())
	           	 			 ip.showTSCheckBox = true;
	           	 	
	                 		 dispose(); }
	                                }
	                           );
	       
	       setSize(500,330);
	       setModal(true);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	return ip;
   } 

	
	@SuppressWarnings({ "unchecked" })
	public InputParametersBP getBPparametersMinDistance(final InputParametersBP ip, Vector<Chunk> chunks, ChartPanel chart) throws Exception
   {
			
		final HeaderBar pane = new HeaderBar("");	
		setContentPane(pane);
		getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.RELATIVE;
		
		final JLabel bpConfText=new JLabel();
		bpConfText.setForeground(UISettings.TextLight_COLOR);
		bpConfText.setText("<html><h3>&nbsp; &nbsp;"+ip.currentActivity+" - "+ip.currentResource+"</h3></html>");
		c.ipadx = 700; 
		c.ipady = 20;     
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(bpConfText, c);
	
		
		//Chart
		c.ipadx = 700; 
		c.ipady = 300;     
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(chart, c);
		
		// Minimum distance from mean
		
		DefaultComboBoxModel model1 = new DefaultComboBoxModel();
		
		Set<Double> distances = new HashSet<Double>();
		
		for(int j=0; j<chunks.size(); j++)
		{
			Double val = chunks.elementAt(j).distanceFromMean;
			distances.add(val);
		}
		
		Vector<Double> dist = new Vector<Double>();
		for(Double el: distances)
			dist.add(el);
	
		Collections.sort(dist);
		
		for(int i=0; i<dist.size(); i++)
		{
		model1.addElement(dist.elementAt(i).toString());
		}
		
		final ProMComboBox minDist = new ProMComboBox(model1);
		
		JLabel minDistLab = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Minimum distance from the mean event density:</html>");  
		minDistLab.setForeground(UISettings.TextLight_COLOR);
		c.ipadx = 300; 
		c.ipady = 20; 
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(minDistLab, c);

		c.gridx = 1;
		c.gridy = 3;
		c.ipadx = 100;
		pane.add(minDist, c);
		
		
		
		////////////////////////////////////////////
		
		SlickerButton but=new SlickerButton();
		but.setText("OK");
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		pane.add(but, c);
		
	     but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	  
	                	   String minDistVal = (String) minDist.getSelectedItem();
	                	   ip.minDistanceFromMean = Double.parseDouble(minDistVal);
	                	   
	                 		 dispose(); }
	                                }
	                           );
	       
	       setSize(750,500);
	       setModal(true);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	return ip;
   } 


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParametersBP defineTSParamsBP(final InputParametersBP ip, XLog log, BPTS bpts) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Time slot size (" + ip.currentActivity +" : "+ ip.currentResource + ") </h3></html>");
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
	
		Timestamp starttime = getLogStart(log);

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
		
		Timestamp endtime = getLogEnd(log);
		
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
		double numMinutes = log_duration/1000/60;
		Long minutes = (long) numMinutes+1;
	
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
	
	
	Long slotSize = getslotSize(log,ip,bpts);
	Long slotSizeInMinutes = (Long) (slotSize/60000);
		
		
	final ProMTextField tslotnum = new ProMTextField(slotSizeInMinutes.toString());
	c.ipadx = 30;
	c.gridwidth = 5;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(tslotnum, c);
			
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("minute");
	model.addElement("year");
	model.addElement("month");
	model.addElement("week");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("second");
	model.addElement("millisecond");

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
	
	Long slots = minutes/slotSizeInMinutes;

	final ProMTextField numslots = new ProMTextField(slots.toString());
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
        	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
        	   
        	   double numSlots = log_duration/(unit*slotsize);
        	   Long slots = (long) numSlots+1;

        	   numslots.setText(slots.toString());
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
            	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
            	   
            	   double numSlots = log_duration/(unit*slotsize);
            	   Long slots = (long) numSlots+1;

            	   numslots.setText(slots.toString());
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
            	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
            	   
            	   double numSlots = log_duration/(unit*slotsize);
            	   Long slots = (long) numSlots+1;

            	   numslots.setText(slots.toString());
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
    	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
    	   
    	   double numSlots = log_duration/(unit*slotsize);
    	   Long slots = (long) numSlots+1;

    	   numslots.setText(slots.toString());
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
                		ip.numberOfSlots = Long.parseLong(numslots.getText()); 	
                		    	  	
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
                	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
                	   
                	   ip.slotSize = unit*slotsize;
                	  
                	              dispose(); }
                                   }
                           );
   
       setSize(700,330);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   } 



	public InputParametersBP defineTSParamsBPNoInputInMinutes(final InputParametersBP ip, XLog log, BPTS bpts) throws Exception
   {
	
	Timestamp starttime = getLogStart(log);
	
	Timestamp endtime = getLogEnd(log);
	
	final Long log_duration = endtime.getTime() - starttime.getTime();
	double numMinutes = log_duration/1000/60;
	Long minutes = (long) numMinutes+1;
	

	Long slotSize = getslotSize(log,ip,bpts);
	
	if(slotSize > -1)
	{
	Long slotSizeInMinutes = (Long) (slotSize/60000);
	
	Long slots = minutes/slotSizeInMinutes;

	ip.startTime = starttime.getTime();
	ip.numberOfSlots = slots;
	ip.slotSize = slotSize; 
	}
	else
		ip.slotSize = -2;
                		
        return ip;
   } 

	public InputParametersBP defineTSParamsBPNoInput(final InputParametersBP ip, XLog log, BPTS bpts) throws Exception
	   {
		
		Timestamp starttime = getLogStart(log);
		Timestamp endtime = getLogEnd(log);
		final Long log_duration = endtime.getTime() - starttime.getTime();
			
		Long slotSize = getslotSize(log,ip,bpts);
		
		if(slotSize > -1)
		{
		Long slots = log_duration/slotSize + 1;

		ip.startTime = starttime.getTime();
		ip.numberOfSlots = slots;
		ip.slotSize = slotSize; 
		}
		else
			ip.slotSize = -2;
	                		
	        return ip;
	   } 
	
	public InputParametersBP defineTSParamsGivenSlot(final InputParametersBP ip, XLog log, BPTS bpts) throws Exception
	   {
		
		Timestamp starttime = getLogStart(log);
		
		Timestamp endtime = getLogEnd(log);
		
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
		long slotSize = ip.slotSizeScript;
		
		Long slots = log_duration/slotSize + 1;

		ip.startTime = starttime.getTime();
		ip.numberOfSlots = slots;
		ip.slotSize = slotSize; 
		
	                		
	        return ip;
	   } 


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputParametersBP defineTSParamsWT(final InputParametersBP ip, XLog log) throws Exception
   {
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
		
	final JLabel defVarsText=new JLabel();
	defVarsText.setForeground(UISettings.TextLight_COLOR);
	defVarsText.setText("<html><h3>Time slot WT ("+ip.currentResource+"):</h3></html>");
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
	
		Timestamp starttime = getLogStart(log);

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
		
		Timestamp endtime = getLogEnd(log);
		
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
		double numMinutes = log_duration/1000/60;
		Long minutes = (long) numMinutes+1;
	
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
	
	Integer slotSizeInMinutes = (int) getslotSizeWT(log,ip)/60000; 
	
	
	final ProMTextField tslotnum = new ProMTextField(slotSizeInMinutes.toString());
	c.ipadx = 30;
	c.gridwidth = 5;
	c.gridx = 3;
	c.gridy = 2;
	pane.add(tslotnum, c);
			
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	model.addElement("minute");
	model.addElement("year");
	model.addElement("month");
	model.addElement("week");
	model.addElement("day");
	model.addElement("hour");
	model.addElement("second");
	model.addElement("millisecond");

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
	
	Long slots = minutes/slotSizeInMinutes + 1;

	final ProMTextField numslots = new ProMTextField(slots.toString());
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
        	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
        	   
        	   double numSlots = log_duration/(unit*slotsize);
        	   Long slots = (long) numSlots+1;

        	   numslots.setText(slots.toString());
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
            	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
            	   
            	   double numSlots = log_duration/(unit*slotsize);
            	   Long slots = (long) numSlots+1;

            	   numslots.setText(slots.toString());
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
            	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
            	   
            	   double numSlots = log_duration/(unit*slotsize);
            	   Long slots = (long) numSlots+1;

            	   numslots.setText(slots.toString());
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
    	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
    	   
    	   double numSlots = log_duration/(unit*slotsize);
    	   Long slots = (long) numSlots+1;

    	   numslots.setText(slots.toString());
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
                		ip.startTimeWT = dt.getMillis();
                		ip.numberOfSlotsWT = Long.parseLong(numslots.getText()); 	
                		    	  	
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
                	   else if(slotunit.equals("millisecond")){unit = 604800000l/(7*24*60*60*1000);}
                	   
                	   ip.slotSizeWT = unit*slotsize;
                	  
                	              dispose(); }
                                   }
                           );
   
       setSize(700,330);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       
       return ip;
   }
	
	
	public InputParametersBP defineTSParamsWTNoInputInMinutes(final InputParametersBP ip, XLog log) throws Exception
   {
		Timestamp starttime = getLogStart(log);
		Timestamp endtime = getLogEnd(log);
		
		final Long log_duration = endtime.getTime() - starttime.getTime();
		double numMinutes = log_duration/1000/60;
		Long minutes = (long) numMinutes+1;
	
	Long slotSize = getslotSizeWT(log,ip);
	
	if(slotSize > -1)
	{
	Integer slotSizeInMinutes = (int) (slotSize/60000); 
	Long slots = minutes/slotSizeInMinutes + 1;

	ip.startTimeWT = starttime.getTime();
	ip.numberOfSlotsWT = slots;
	ip.slotSizeWT = slotSize;
	
	
	}
	else 
		ip.slotSizeWT = slotSize;		

     return ip;
   }
	
	
	public InputParametersBP defineTSParamsWTNoInput(final InputParametersBP ip, XLog log) throws Exception
   {
		Timestamp starttime = getLogStart(log);
		Timestamp endtime = getLogEnd(log);
		
		final Long log_duration = endtime.getTime() - starttime.getTime();
		
	Long slotSize = getslotSizeWT(log,ip);
	
	if(slotSize > -1)
	{
	Long slots = log_duration/slotSize + 1;

	ip.startTimeWT = starttime.getTime();
	ip.numberOfSlotsWT = slots;
	ip.slotSizeWT = slotSize;
	}
	else 
		ip.slotSizeWT = slotSize;		

     return ip;
   }

	
	
	
	public Timestamp getLogStart(XLog log) throws Exception
	   {
		long min = System.currentTimeMillis()*10;
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				if(time.getValueMillis() < min)
					min = time.getValueMillis();
			}
		}
		
		Timestamp starttime = new Timestamp(min);
		return starttime;
	   }
	
	
	public Timestamp getLogEnd(XLog log) throws Exception
	   {
		long max = 0;
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				if(time.getValueMillis() > max)
					max = time.getValueMillis();

			}
		}
		
		Timestamp endtime = new Timestamp(max);
		
		return endtime;
	   }
	
	
	public long getslotSizeV1(XLog log) throws Exception //getslotSize
	   {
		long slotSize = System.currentTimeMillis()*10;
		
		Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				times.add(time.getValueMillis());
				

			}
		}
		
		Collections.sort(times);
		
		for(int i=1; i<times.size(); i++)		
		{
			long diff = times.elementAt(i)-times.elementAt(i-1);
			if (diff > 0 && diff < slotSize)
				slotSize = diff;
		}
				
		return slotSize;
	   }
	
	public long getslotSizeAR(XLog log, InputParametersBP ip) throws Exception
	   {
		long slotSize = System.currentTimeMillis()*10;
		
		String resource = ip.bpResource.elementAt(0).toLowerCase();
		String activity = ip.bpActivity.elementAt(0).toLowerCase();
		
		Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				XAttributeLiteral res = (XAttributeLiteral) e.getAttributes().get("org:resource");
				XAttributeLiteral task = (XAttributeLiteral) e.getAttributes().get("concept:name");
				
				if(resource.equals(res.toString().toLowerCase()) && activity.equals(task.toString().toLowerCase()))
					times.add(time.getValueMillis());
				
			}
		}
		
		Collections.sort(times);
		
		for(int i=1; i<times.size(); i++)		
		{
			long diff = times.elementAt(i)-times.elementAt(i-1);
			if (diff > 0 && diff < slotSize)
				slotSize = diff;
		}
				
		return slotSize;
	   }

	public long getslotSize(XLog log, InputParametersBP ip, BPTS bpts) throws Exception
	   {
	
		Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				XAttributeLiteral res = (XAttributeLiteral) e.getAttributes().get("org:resource");
				XAttributeLiteral task = (XAttributeLiteral) e.getAttributes().get("concept:name");
				XAttributeLiteral type = (XAttributeLiteral) e.getAttributes().get("lifecycle:transition");
				
				boolean addEvent = true;
				
				if(ip.caseData)
					for(String key : ip.bpData.keySet())
					{
						XAttributeLiteral dataAttr = (XAttributeLiteral) e.getAttributes().get(key);
						
						if(dataAttr != null)
						{
							if(!ip.bpData.get(key).contains(dataAttr.toString()))
								addEvent = false;
						}
						else
						{
							addEvent = false;
						}
					}
				
				if(addEvent && ip.resource)
				{
					if(ip.currentResource.equals(res.toString()) && 
							ip.currentActivity.equals(task.toString()) &&
							 	ip.eventType.equals(type.toString()))
										times.add(time.getValueMillis());
			
				}
				
				if(addEvent && !ip.resource)
				{
					if(ip.currentActivity.equals(task.toString()) &&
							 	ip.eventType.equals(type.toString()))
										times.add(time.getValueMillis());
	
				}

			}
		}
		
		Long slotSize = (long) 0;
		
		if(times.size() > 1)
			slotSize = getGivenSlotSize(times,ip, bpts);
		else 
			slotSize = (long) -1;
		
		return slotSize;
	   
	   }
	

	public long getslotSizeWT(XLog log, InputParametersBP ip) throws Exception
	   {
	
		Vector<Long> times = new Vector<Long>();
		
		for (XTrace t : log) 
		{
			for (XEvent e : t) 
			{
				
				XAttributeTimestamp time = (XAttributeTimestamp) e.getAttributes().get("time:timestamp");
				XAttributeLiteral res = (XAttributeLiteral) e.getAttributes().get("org:resource");
			
				if(ip.resource)
				{
					if(ip.currentResource.equals(res.toString()))
						times.add(time.getValueMillis());
				}
				else
				{
					times.add(time.getValueMillis());
				}
			
			}
		}
		
		return getGivenSlotSizeWT(times,ip);
	   
	   }
	

	public Long getGivenSlotSize(Vector<Long> times, InputParametersBP ip, BPTS bpts)
	{
		long slotSize = System.currentTimeMillis()*10;
		
		Collections.sort(times);
		
		Vector<Long> timeDiff = new Vector<Long>();
		
		if(ip.considerWorkingTime)
		{
			long t1 = times.elementAt(0);
			long t2 = times.elementAt(1);
			
			for(int i=1; i<times.size()-1; i++)		
			{
				
				if(!hasNWT(t1, t2, ip, bpts))
				{
					long diff = t2-t1;
					
					if(diff > 0)
						timeDiff.add(diff);
					
					t1 = times.elementAt(i);
					t2 = times.elementAt(i+1);
				}
				else
				{
					t1 = times.elementAt(i);
					t2 = times.elementAt(i+1);
					
				}

			}

		}
		else
		{
			for(int i=1; i<times.size(); i++)		
			{
				long diff = times.elementAt(i)-times.elementAt(i-1);
				
				if(diff > 0)
					timeDiff.add(diff);
			}
		
		}
		
		
		if(timeDiff.size() > 0)
		{
		if(ip.bptsSlotType == 1)
			slotSize = getMin(timeDiff);
		
		if(ip.bptsSlotType == 2)
			slotSize = getMedian(timeDiff);
		
		if(ip.bptsSlotType == 3)
			slotSize = getMean(timeDiff);
		
		if(ip.bptsSlotType == 4)
			slotSize = getGivenPart(timeDiff,ip);
		}
		else
			slotSize = -1;
			
		return slotSize;
	  
	}
	

	public Long getGivenSlotSizeWT(Vector<Long> times, InputParametersBP ip)
	{
		long slotSize = System.currentTimeMillis()*10;
		
		Collections.sort(times);
		
		Vector<Long> timeDiff = new Vector<Long>();
		
			for(int i=1; i<times.size(); i++)		
			{
				long diff = times.elementAt(i)-times.elementAt(i-1);
				
				if(diff > 0)
					timeDiff.add(diff);
			}
		
		
		if(timeDiff.size() > 0)
		{
		if(ip.bptsSlotTypeWT == 1)
			slotSize = getMin(timeDiff);
		
		if(ip.bptsSlotTypeWT == 2)
			slotSize = getMedian(timeDiff);
		
		
		if(ip.bptsSlotTypeWT == 3)
			slotSize = getMean(timeDiff);
		
		if(ip.bptsSlotTypeWT == 4)
			slotSize = getGivenPart(timeDiff,ip);
		}
		else slotSize = -1;
			
		
		return slotSize;
	  
	}
	

	Long getMin(Vector<Long> diff)
	{	
		long slotSize = System.currentTimeMillis()*10;
	
			for(int i=0; i<diff.size(); i++)		
			{
				if (diff.elementAt(i) > 0 && diff.elementAt(i) < slotSize)
					slotSize = diff.elementAt(i);
			}
	
		return slotSize;
	}
	
	Long getMedian(Vector<Long> diff)
	{
		
		Collections.sort(diff);
		
		double median;
		
		if (diff.size() % 2 == 0)
		    median = ((double)diff.elementAt(diff.size()/2) + (double)diff.elementAt(diff.size()/2 - 1))/2;
		else
		    median = diff.elementAt(diff.size()/2);
		
		return (long) median;
	}
	
	Long getMean(Vector<Long> diff)
	{
		long slotSize = 0;
		long sum = 0;
		
		for(int i=0; i<diff.size(); i++)		
		{
			sum += diff.elementAt(i);
		}
		
		slotSize = sum/diff.size();

	return slotSize;

	}
	
	
	Long getGivenPart(Vector<Long> diff, InputParametersBP ip)
	{
		Long slotSize = (long) 0;
		Double percentile = ip.slotSizeEventDistancePercentile;
		Collections.sort(diff);
		
		Integer i = (int) (diff.size()*percentile);
		
		if(i == 0)
			slotSize = diff.elementAt(0);
		else
			slotSize = diff.elementAt(i-1);
		
		return slotSize;
	}

	Boolean hasNWT(Long t1, Long t2, InputParametersBP ip, BPTS bpts)
	{
		boolean hasnwt = false;
		
		Vector<Chunk> nwt = bpts.nwt;
		
		Date d1 = new Date(t1);
		Date d2 = new Date(t2);
		
		for(int i=0; i< nwt.size(); i++)
		{
			Date start = nwt.elementAt(i).chunkStart;
			Date end = nwt.elementAt(i).chunkEnd;
			
			if(start.after(d1) && end.after(d1) && end.before(d2) && start.before(d2))
				hasnwt = true;
		}
		
		
		return hasnwt;
	}
}







