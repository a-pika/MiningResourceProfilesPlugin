package org.processmining.plugins.miningresourceprofiles.inputs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

@SuppressWarnings("serial")
public class DefineELDBMapping extends JDialog{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ELDBmapping getELMapping(final Vector <String> ea) throws Exception
   {
	
	final HeaderBar mainPane = new HeaderBar("");
	setContentPane(mainPane);
	mainPane.setLayout(new GridBagLayout());
	GridBagConstraints cMain = new GridBagConstraints();
		
	final HeaderBar pane = new HeaderBar("");	
	pane.setLayout(new GridLayout(0,3));
	
	JLabel lab1 = new JLabel(".");
	JLabel lab3 = new JLabel(".");

	SlickerButton but=new SlickerButton();
	but.setText("Save mapping");
	pane.add(lab1);
	pane.add(but);
	pane.add(lab3);

		
	final JLabel defELA=new JLabel();
	defELA.setForeground(UISettings.TextLight_COLOR);
	defELA.setText("<html><h3>Event log attributes: </h3><br></html>");
	defELA.setHorizontalAlignment(JLabel.CENTER);
	pane.add(defELA);
	   
	final JLabel defDBA=new JLabel();
	defDBA.setForeground(UISettings.TextLight_COLOR);
	defDBA.setText("<html><h3>Database attribute names: </h3><br></html>");
	defDBA.setHorizontalAlignment(JLabel.CENTER);
	pane.add(defDBA);	
	
	final JLabel defDBType=new JLabel();
	defDBType.setForeground(UISettings.TextLight_COLOR);
	defDBType.setText("<html><h3>Database attribute types: </h3><br></html>");
	defDBType.setHorizontalAlignment(JLabel.CENTER);
	pane.add(defDBType);	

	
 	 
	int defTask = 0;
 	DefaultComboBoxModel task_model = new DefaultComboBoxModel();
	for(int i=0; i<ea.size(); i++)
	{task_model.addElement(ea.elementAt(i));
	if(ea.elementAt(i).equals("concept:name"))
	{defTask = i;}
	}
	task_model.addElement("NONE");
	task_model.setSelectedItem(ea.elementAt(defTask));
	 
 	 final ProMComboBox task_el = new ProMComboBox(task_model);
   	 pane.add(task_el);
 	 
 	 final JLabel task_db = new JLabel("task");
 	 task_db.setForeground(UISettings.TextLight_COLOR);
 	 task_db.setHorizontalAlignment(JLabel.CENTER);
 	 pane.add(task_db);
	 
	 DefaultComboBoxModel task_typemodel = new DefaultComboBoxModel();
	 task_typemodel.addElement("varchar(250)");
	 task_typemodel.addElement("int");
	 task_typemodel.addElement("bigint");
	 task_typemodel.addElement("double");
	 task_typemodel.addElement("datetime");
	 task_typemodel.addElement("varchar(20)");
	 task_typemodel.addElement("varchar(50)");
	 task_typemodel.addElement("varchar(100)");
	 task_typemodel.addElement("varchar(1000)");
	 
	 final ProMComboBox task_dbtype = new ProMComboBox(task_typemodel);
  	 pane.add(task_dbtype);

  	int defType = 0;
 	DefaultComboBoxModel type_model = new DefaultComboBoxModel();
 	for(int i=0; i<ea.size(); i++)
 	{type_model.addElement(ea.elementAt(i));
 	if(ea.elementAt(i).equals("lifecycle:transition"))
 	{
 		defType = i;
 	}
 	}
 	type_model.addElement("NONE");
 	type_model.setSelectedItem(ea.elementAt(defType));
 	 
 	final ProMComboBox type_el = new ProMComboBox(type_model);
  	pane.add(type_el);
 	 
 	final JLabel type_db = new JLabel("type");
 	type_db.setForeground(UISettings.TextLight_COLOR);
 	type_db.setHorizontalAlignment(JLabel.CENTER);
 	pane.add(type_db);
	 
	 DefaultComboBoxModel type_typemodel = new DefaultComboBoxModel();
	 type_typemodel.addElement("varchar(20)");
	 type_typemodel.addElement("int");
	 type_typemodel.addElement("bigint");
	 type_typemodel.addElement("double");
	 type_typemodel.addElement("datetime");
	 type_typemodel.addElement("varchar(50)");
	 type_typemodel.addElement("varchar(100)");
	 type_typemodel.addElement("varchar(250)");
	 type_typemodel.addElement("varchar(1000)");
	 
	 final ProMComboBox type_dbtype = new ProMComboBox(type_typemodel);
  	 pane.add(type_dbtype);

  	int defTime = 0;
 	DefaultComboBoxModel time_model = new DefaultComboBoxModel();
 	for(int i=0; i<ea.size(); i++)
 	{time_model.addElement(ea.elementAt(i));
	if(ea.elementAt(i).equals("time:timestamp"))
 	{
		defTime = i;
 	}
 
 	}
 	time_model.addElement("NONE");
 	time_model.setSelectedItem(ea.elementAt(defTime));
 	 
 	 final ProMComboBox time_el = new ProMComboBox(time_model);
 	 pane.add(time_el);
 	 
 	 final JLabel time_db = new JLabel("time");
 	 time_db.setForeground(UISettings.TextLight_COLOR);
 	 time_db.setHorizontalAlignment(JLabel.CENTER);
 	 pane.add(time_db);
	 
	 DefaultComboBoxModel time_typemodel = new DefaultComboBoxModel();
	 time_typemodel.addElement("datetime");
	 time_typemodel.addElement("varchar(1000)");
	 
	 final ProMComboBox time_dbtype = new ProMComboBox(time_typemodel);
   	 pane.add(time_dbtype);

 	int defRes = 0; 
 	DefaultComboBoxModel resource_model = new DefaultComboBoxModel();
 	for(int i=0; i<ea.size(); i++)
 	{resource_model.addElement(ea.elementAt(i));
 	if(ea.elementAt(i).equals("org:resource"))
 	{
 		defRes = i;
 	}
 
 	}
 	resource_model.addElement("NONE");
 	resource_model.setSelectedItem(ea.elementAt(defRes));
 	 
 	 final ProMComboBox resource_el = new ProMComboBox(resource_model);
    pane.add(resource_el);
 	 
 	 final JLabel resource_db = new JLabel("resource");
 	 resource_db.setForeground(UISettings.TextLight_COLOR);
 	 resource_db.setHorizontalAlignment(JLabel.CENTER);
  	 pane.add(resource_db);
	 
	 DefaultComboBoxModel resource_typemodel = new DefaultComboBoxModel();
	 resource_typemodel.addElement("varchar(100)");
	 resource_typemodel.addElement("int");
	 resource_typemodel.addElement("bigint");
	 resource_typemodel.addElement("double");
	 resource_typemodel.addElement("datetime");
	 resource_typemodel.addElement("varchar(20)");
	 resource_typemodel.addElement("varchar(50)");
	 resource_typemodel.addElement("varchar(250)");
	 resource_typemodel.addElement("varchar(1000)");

	 
	 final ProMComboBox resource_dbtype = new ProMComboBox(resource_typemodel);
  	 pane.add(resource_dbtype);

 	 
 	DefaultComboBoxModel eventid_model = new DefaultComboBoxModel();
 	eventid_model.addElement("CALCULATE");
	eventid_model.addElement("NONE");
	for(int i=0; i<ea.size(); i++)
	{eventid_model.addElement(ea.elementAt(i));}
	
	 final ProMComboBox eventid_el = new ProMComboBox(eventid_model);
  	 pane.add(eventid_el);
 	 
 	final JLabel eventid_db = new JLabel("eventid");
 	eventid_db.setHorizontalAlignment(JLabel.CENTER);
 	eventid_db.setForeground(UISettings.TextLight_COLOR);
	pane.add(eventid_db);

	final JLabel eventid_dbtype = new JLabel("int");
	eventid_dbtype.setHorizontalAlignment(JLabel.CENTER);
	eventid_dbtype.setForeground(UISettings.TextLight_COLOR);
	pane.add(eventid_dbtype);
	
	 DefaultComboBoxModel taskindex_model = new DefaultComboBoxModel();
	 taskindex_model.addElement("CALCULATE");
	 taskindex_model.addElement("NONE");
		for(int i=0; i<ea.size(); i++)
		{taskindex_model.addElement(ea.elementAt(i));}
		
		final ProMComboBox taskindex_el = new ProMComboBox(taskindex_model);
	  	pane.add(taskindex_el);
	 	 
	 	final JLabel taskindex_db = new JLabel("task_index");
	 	taskindex_db.setHorizontalAlignment(JLabel.CENTER);
	 	taskindex_db.setForeground(UISettings.TextLight_COLOR);
		pane.add(taskindex_db);

		final JLabel taskindex_dbtype = new JLabel("int");
		taskindex_dbtype.setHorizontalAlignment(JLabel.CENTER);
		taskindex_dbtype.setForeground(UISettings.TextLight_COLOR);
		pane.add(taskindex_dbtype);

		 
		DefaultComboBoxModel duration_model = new DefaultComboBoxModel();
		duration_model.addElement("CALCULATE");
		duration_model.addElement("NONE");
		for(int i=0; i<ea.size(); i++)
		{duration_model.addElement(ea.elementAt(i));}
			
		final ProMComboBox duration_el = new ProMComboBox(duration_model);
		pane.add(duration_el);
		 	 
		final JLabel duration_db = new JLabel("duration");
		duration_db.setHorizontalAlignment(JLabel.CENTER);
		duration_db.setForeground(UISettings.TextLight_COLOR);
		pane.add(duration_db);

		final JLabel duration_dbtype = new JLabel("bigint");
		duration_dbtype.setHorizontalAlignment(JLabel.CENTER);
		duration_dbtype.setForeground(UISettings.TextLight_COLOR);
		pane.add(duration_dbtype);

		
		DefaultComboBoxModel workload_model = new DefaultComboBoxModel();
		workload_model.addElement("CALCULATE");
		workload_model.addElement("NONE");
		for(int i=0; i<ea.size(); i++)
		{workload_model.addElement(ea.elementAt(i));}
			
		final ProMComboBox workload_el = new ProMComboBox(workload_model);
		pane.add(workload_el);
		 	 
		final JLabel workload_db = new JLabel("workload");
		workload_db.setForeground(UISettings.TextLight_COLOR);
		workload_db.setHorizontalAlignment(JLabel.CENTER);
		pane.add(workload_db);

		final JLabel workload_dbtype = new JLabel("int");
		workload_dbtype.setHorizontalAlignment(JLabel.CENTER);
		workload_dbtype.setForeground(UISettings.TextLight_COLOR);
		pane.add(workload_dbtype);

		
		DefaultComboBoxModel workload_duration_model = new DefaultComboBoxModel();
		workload_duration_model.addElement("CALCULATE");
		workload_duration_model.addElement("NONE");
		for(int i=0; i<ea.size(); i++)
		{workload_duration_model.addElement(ea.elementAt(i));}
			
		final ProMComboBox workload_duration_el = new ProMComboBox(workload_duration_model);
		pane.add(workload_duration_el);
		 	 
		final JLabel workload_duration_db = new JLabel("workload_duration");
		workload_duration_db.setHorizontalAlignment(JLabel.CENTER);
		workload_duration_db.setForeground(UISettings.TextLight_COLOR);
		pane.add(workload_duration_db);

		final JLabel workload_duration_dbtype = new JLabel("bigint");
		workload_duration_dbtype.setHorizontalAlignment(JLabel.CENTER);
		workload_duration_dbtype.setForeground(UISettings.TextLight_COLOR);
		pane.add(workload_duration_dbtype);
	 

Vector <DefaultComboBoxModel> amodels = new Vector<DefaultComboBoxModel>();
final Vector <ProMComboBox> acombos = new Vector<ProMComboBox>();
final Vector <ProMTextField> dbnames = new Vector<ProMTextField>();
Vector <DefaultComboBoxModel> tmodels = new Vector<DefaultComboBoxModel>();
final Vector <ProMComboBox> tcombos = new Vector<ProMComboBox>();



for (int j=0; j<ea.size(); j++)
{
	 amodels.add(new DefaultComboBoxModel()); 
	 amodels.elementAt(j).addElement("NONE");
	 for(int i=0; i<ea.size(); i++)
	{amodels.elementAt(j).addElement(ea.elementAt(i));}

	
	 tmodels.add(new DefaultComboBoxModel()); 
	 tmodels.elementAt(j).addElement("NONE");
	 tmodels.elementAt(j).addElement("int");
	 tmodels.elementAt(j).addElement("bigint");
	 tmodels.elementAt(j).addElement("double");
	 tmodels.elementAt(j).addElement("datetime");
	 tmodels.elementAt(j).addElement("varchar(20)");
	 tmodels.elementAt(j).addElement("varchar(50)");
	 tmodels.elementAt(j).addElement("varchar(100)");
	 tmodels.elementAt(j).addElement("varchar(250)");
	 tmodels.elementAt(j).addElement("varchar(1000)");
	
}



for (int j=0; j<ea.size(); j++)
{
	 
	 acombos.add(new ProMComboBox(amodels.elementAt(j)));
	 pane.add(acombos.elementAt(j));
	 
	 dbnames.add(new ProMTextField());
	 pane.add(dbnames.elementAt(j));

	 tcombos.add(new ProMComboBox(tmodels.elementAt(j)));
	 pane.add(tcombos.elementAt(j));

	
}

final ELDBmapping map = new ELDBmapping();	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                	 
                	  
                	   map.ELAttributes.add((String) task_el.getSelectedItem());
                	   map.ELAttributes.add((String) type_el.getSelectedItem());
                	   map.ELAttributes.add((String) time_el.getSelectedItem());
                	   map.ELAttributes.add((String) resource_el.getSelectedItem());
                	   map.ELAttributes.add((String) eventid_el.getSelectedItem());
                	   map.ELAttributes.add((String) taskindex_el.getSelectedItem());
                	   map.ELAttributes.add((String) duration_el.getSelectedItem());
                	   map.ELAttributes.add((String) workload_el.getSelectedItem());
                	   map.ELAttributes.add((String) workload_duration_el.getSelectedItem());
                  	   
                   	  
                	   map.DBELAttributes.add(task_db.getText());
                	   map.DBELAttributes.add(type_db.getText());
                	   map.DBELAttributes.add(time_db.getText());
                	   map.DBELAttributes.add(resource_db.getText());
                	   map.DBELAttributes.add(eventid_db.getText());
                	   map.DBELAttributes.add(taskindex_db.getText());
                	   map.DBELAttributes.add(duration_db.getText());
                	   map.DBELAttributes.add(workload_db.getText());
                	   map.DBELAttributes.add(workload_duration_db.getText());
            	   
                 	  
                	   map.DBELTypes.add((String) task_dbtype.getSelectedItem());
                	   map.DBELTypes.add((String) type_dbtype.getSelectedItem());
                	   map.DBELTypes.add((String) time_dbtype.getSelectedItem());
                	   map.DBELTypes.add((String) resource_dbtype.getSelectedItem());
                	   map.DBELTypes.add(eventid_dbtype.getText());
                	   map.DBELTypes.add(taskindex_dbtype.getText());
                	   map.DBELTypes.add(duration_dbtype.getText());
                	   map.DBELTypes.add(workload_dbtype.getText());
                	   map.DBELTypes.add(workload_duration_dbtype.getText());
                	   
                	   for (int i=0; i<ea.size(); i++)
                	   {
                		   map.ELAttributes.add((String) acombos.elementAt(i).getSelectedItem());
                	   }
           
                	   for (int i=0; i<ea.size(); i++)
                	   {
                		   String text = dbnames.elementAt(i).getText();
                		   if (text.isEmpty())
                		   {map.DBELAttributes.add("NONE");}else
                		   {map.DBELAttributes.add(text);}
                	   }
           
                	   for (int i=0; i<ea.size(); i++)
                	   {
                		   map.DBELTypes.add((String) tcombos.elementAt(i).getSelectedItem());
                	   }
           
                	   
                	   dispose();
                  	                   }
                                   }
                           );
       
       
     int rows = ea.size()+11;
     pane.setPreferredSize(new Dimension(830, rows*30+30));//
     ProMScrollPane scrollPane = new ProMScrollPane(pane);
     cMain.ipadx = 860;
   	 cMain.ipady = 580;
   	 cMain.gridx = 0;
   	 cMain.gridy = 0;
   	 mainPane.add(scrollPane,cMain);
      
         
       setSize(900,650);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
     
       return map;
   } 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ELDBmapping getCLMapping(final Vector <String> ea, final ELDBmapping map) throws Exception
	   {
		
		final HeaderBar mainPane = new HeaderBar("");
		setContentPane(mainPane);
		mainPane.setLayout(new GridBagLayout());
		GridBagConstraints cMain = new GridBagConstraints();
			
		
		final HeaderBar pane = new HeaderBar("");	
		pane.setLayout(new GridLayout(0,3));
		
		JLabel lab1 = new JLabel(".");
		JLabel lab3 = new JLabel(".");

		SlickerButton but=new SlickerButton();
		but.setText("Save mapping");
		pane.add(lab1);
		pane.add(but);
		pane.add(lab3);

			
		final JLabel defELA=new JLabel();
		defELA.setForeground(UISettings.TextLight_COLOR);
		defELA.setHorizontalAlignment(JLabel.CENTER);
		defELA.setText("<html><h3>Case log attributes: </h3><br></html>");
		pane.add(defELA);
		   
		final JLabel defDBA=new JLabel();
		defDBA.setForeground(UISettings.TextLight_COLOR);
		defDBA.setHorizontalAlignment(JLabel.CENTER);
		defDBA.setText("<html><h3>Database attribute names: </h3><br></html>");
		pane.add(defDBA);	
		
		final JLabel defDBType=new JLabel();
		defDBType.setForeground(UISettings.TextLight_COLOR);
		defDBType.setHorizontalAlignment(JLabel.CENTER);
		defDBType.setText("<html><h3>Database attribute types: </h3><br></html>");
		pane.add(defDBType);	

		
		 
			int defCase = 0;	
			DefaultComboBoxModel case_model = new DefaultComboBoxModel();
			for(int i=0; i<ea.size(); i++)
			{case_model.addElement(ea.elementAt(i));
			if(ea.elementAt(i).equals("concept:name"))
			{
				defCase = i;
			}
			}
			case_model.addElement("NONE");
			case_model.setSelectedItem(ea.elementAt(defCase));
			
		 
		 final ProMComboBox case_el = new ProMComboBox(case_model);
	   	 pane.add(case_el);
	 	 
	 	 final JLabel case_db = new JLabel("caseid");
	 	 case_db.setForeground(UISettings.TextLight_COLOR);
	 	 case_db.setHorizontalAlignment(JLabel.CENTER);
	 	 pane.add(case_db);
		 
		 DefaultComboBoxModel case_typemodel = new DefaultComboBoxModel();
		 case_typemodel.addElement("varchar(250)");
		 case_typemodel.addElement("int");
		 case_typemodel.addElement("bigint");
		 case_typemodel.addElement("double");
		 case_typemodel.addElement("datetime");
		 case_typemodel.addElement("varchar(20)");
		 case_typemodel.addElement("varchar(50)");
		 case_typemodel.addElement("varchar(100)");
		 case_typemodel.addElement("varchar(1000)");

		 
		 final ProMComboBox case_dbtype = new ProMComboBox(case_typemodel);
	 	 pane.add(case_dbtype);
		 
	 	 
	 	DefaultComboBoxModel numres_model = new DefaultComboBoxModel();
	 	numres_model.addElement("CALCULATE");
	 	numres_model.addElement("NONE");
		for(int i=0; i<ea.size(); i++)
		{numres_model.addElement(ea.elementAt(i));}
		
		 final ProMComboBox numres_el = new ProMComboBox(numres_model);
	  	 pane.add(numres_el);
	 	 
	 	 final JLabel numres_db = new JLabel("number_of_resources");
	 	 numres_db.setForeground(UISettings.TextLight_COLOR);
	 	 numres_db.setHorizontalAlignment(JLabel.CENTER);
	 	 pane.add(numres_db);

		 final JLabel numres_dbtype = new JLabel("int");
		 numres_dbtype.setHorizontalAlignment(JLabel.CENTER);
		 numres_dbtype.setForeground(UISettings.TextLight_COLOR);
		 pane.add(numres_dbtype);

		
			DefaultComboBoxModel duration_model = new DefaultComboBoxModel();
			duration_model.addElement("CALCULATE");
			duration_model.addElement("NONE");
			for(int i=0; i<ea.size(); i++)
			{duration_model.addElement(ea.elementAt(i));}
				
			final ProMComboBox duration_el = new ProMComboBox(duration_model);
			pane.add(duration_el);
			 	 
			final JLabel duration_db = new JLabel("duration");
			duration_db.setHorizontalAlignment(JLabel.CENTER);
			duration_db.setForeground(UISettings.TextLight_COLOR);
			pane.add(duration_db);

			final JLabel duration_dbtype = new JLabel("bigint");
			duration_dbtype.setHorizontalAlignment(JLabel.CENTER);
			duration_dbtype.setForeground(UISettings.TextLight_COLOR);
			pane.add(duration_dbtype);

		 
			
			DefaultComboBoxModel active_duration_model = new DefaultComboBoxModel();
			active_duration_model.addElement("CALCULATE");
			active_duration_model.addElement("NONE");
			for(int i=0; i<ea.size(); i++)
			{active_duration_model.addElement(ea.elementAt(i));}
				
			final ProMComboBox active_duration_el = new ProMComboBox(active_duration_model);
			pane.add(active_duration_el);
			 	 
			final JLabel active_duration_db = new JLabel("enddate");
			active_duration_db.setHorizontalAlignment(JLabel.CENTER);
			active_duration_db.setForeground(UISettings.TextLight_COLOR);
			pane.add(active_duration_db);

			final JLabel active_duration_dbtype = new JLabel("datetime");
			active_duration_dbtype.setHorizontalAlignment(JLabel.CENTER);
			active_duration_dbtype.setForeground(UISettings.TextLight_COLOR);
			pane.add(active_duration_dbtype);

		 
		 
	 Vector <DefaultComboBoxModel> amodels = new Vector<DefaultComboBoxModel>();
	 final Vector <ProMComboBox> acombos = new Vector<ProMComboBox>();
	 final Vector <ProMTextField> dbnames = new Vector<ProMTextField>();
	 Vector <DefaultComboBoxModel> tmodels = new Vector<DefaultComboBoxModel>();
	 final Vector <ProMComboBox> tcombos = new Vector<ProMComboBox>();
	 
	 for (int j=0; j<ea.size(); j++)
	 {
		 amodels.add(new DefaultComboBoxModel()); 
		 amodels.elementAt(j).addElement("NONE");
		 for(int i=0; i<ea.size(); i++)
		{amodels.elementAt(j).addElement(ea.elementAt(i));}
		 
 tmodels.add(new DefaultComboBoxModel()); 
		 
		 tmodels.elementAt(j).addElement("NONE");
		 tmodels.elementAt(j).addElement("int");
		 tmodels.elementAt(j).addElement("bigint");
		 tmodels.elementAt(j).addElement("double");
		 tmodels.elementAt(j).addElement("datetime");
		 tmodels.elementAt(j).addElement("varchar(20)");
		 tmodels.elementAt(j).addElement("varchar(50)");
		 tmodels.elementAt(j).addElement("varchar(100)");
		 tmodels.elementAt(j).addElement("varchar(250)");
		 tmodels.elementAt(j).addElement("varchar(1000)");

		
	 }
	 
	 
	 for (int j=0; j<ea.size(); j++)
	 {
		 acombos.add(new ProMComboBox(amodels.elementAt(j)));
		 pane.add(acombos.elementAt(j));
		 
		 dbnames.add(new ProMTextField());
		 pane.add(dbnames.elementAt(j));

		 tcombos.add(new ProMComboBox(tmodels.elementAt(j)));
		 pane.add(tcombos.elementAt(j));

	 }

			 
	       but.addActionListener(
	               new ActionListener(){
	                   public void actionPerformed(
	                           ActionEvent e) {
	                	 
	                	   map.CLAttributes.add((String) case_el.getSelectedItem());
	                	   map.CLAttributes.add((String) numres_el.getSelectedItem());
	                	   map.CLAttributes.add((String) duration_el.getSelectedItem());
	                	   map.CLAttributes.add((String) active_duration_el.getSelectedItem());
	                  	   
	                   	   map.DBCLAttributes.add(case_db.getText());
	                	   map.DBCLAttributes.add(numres_db.getText());
	                	   map.DBCLAttributes.add(duration_db.getText());
	                	   map.DBCLAttributes.add(active_duration_db.getText());
	            	   
	                 	   map.DBCLTypes.add((String) case_dbtype.getSelectedItem());
	                	   map.DBCLTypes.add(numres_dbtype.getText());
	                	   map.DBCLTypes.add(duration_dbtype.getText());
	                	   map.DBCLTypes.add(active_duration_dbtype.getText());
	                	   
	                	   for (int i=0; i<ea.size(); i++)
	                	   {
	                		   map.CLAttributes.add((String) acombos.elementAt(i).getSelectedItem());
	                	   }
	           
	                	   for (int i=0; i<ea.size(); i++)
	                	   {
	                		   String text = dbnames.elementAt(i).getText();
	                		   if (text.isEmpty())
	                		   {map.DBCLAttributes.add("NONE");}else
	                		   {map.DBCLAttributes.add(text);}
	                	   }
	           
	                	   for (int i=0; i<ea.size(); i++)
	                	   {
	                		   map.DBCLTypes.add((String) tcombos.elementAt(i).getSelectedItem());
	                	   }
	           
	                	   
	                	   dispose();
	                  	                   }
	                                   }
	                           );
	       
	       int rows = ea.size()+5;
	       pane.setPreferredSize(new Dimension(830, rows*30+30));//
	       ProMScrollPane scrollPane = new ProMScrollPane(pane);
	       cMain.ipadx = 860;
	       cMain.ipady = 280;
	       cMain.gridx = 0;
	       cMain.gridy = 0;
	       mainPane.add(scrollPane,cMain);
	        
	       setSize(900,350);
	       setModal(true);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	     
	       return map;
	   } 

}







