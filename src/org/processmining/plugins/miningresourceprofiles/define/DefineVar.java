package org.processmining.plugins.miningresourceprofiles.define;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

@SuppressWarnings("serial")
public class DefineVar extends JDialog{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addVar(final Connection con) throws Exception
   {
		
	 String allVars = new String("<html>&nbsp; &nbsp;No variable functions are currently defined <br></html>");
	 final JLabel definedVars;
	 JLabel inputVar;
	 JLabel inputType;
	
	 final HeaderBar pane = new HeaderBar("");	
	 setContentPane(pane);
	 getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
		
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;
	
	Statement dbStatement = con.createStatement();
	String sqlQuery = "SELECT * FROM vars ";
	
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	if (rs.next()){allVars = "";}
	rs.beforeFirst();

	while(rs.next())
	{	
	String name = rs.getString("name");
	String type = rs.getString("type");
	allVars+= "&nbsp; &nbsp;"+name + " - " + type + "<br>";
	}
	
	definedVars=new JLabel();
	definedVars.setOpaque(true);
	definedVars.setForeground(UISettings.Text_COLOR);
	definedVars.setBackground(UISettings.BG_COLOR);

	definedVars.setText("<html><h3>&nbsp; &nbsp;Defined variable functions: </h3><br>"+allVars+"</html>");
	ProMScrollPane scrollPane = new ProMScrollPane(definedVars);
	c.ipady = 150;      
	c.gridwidth = 3;
	c.fill = JScrollPane.WIDTH;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(scrollPane, c);
		
	
	inputVar=new JLabel();
	inputVar.setForeground(UISettings.TextLight_COLOR);
	inputVar.setText("<html>&nbsp; &nbsp;Variable function name: </html>");
	c.ipady = 10; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(inputVar, c);
	
	final ProMTextField varName=new ProMTextField();
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(varName, c);
  	
	inputType=new JLabel();
	inputType.setForeground(UISettings.TextLight_COLOR);
	inputType.setText("<html>&nbsp; &nbsp;Variable function type: </html>");
	c.gridx = 0;
	c.gridy = 3;
	pane.add(inputType, c);
	
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
     model.addElement("int");
     model.addElement("time");
     model.addElement("string");
     model.addElement("double");
     final ProMComboBox jop = new ProMComboBox(model);
     c.gridx = 1;
 	 c.gridy = 3;
 	 pane.add(jop, c);

		
 	SlickerButton but=new SlickerButton();
	but.setText("Add variable function");
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 3;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   	Statement dbStatement;
                   	String newAllVars = "";
						try {
							dbStatement = con.createStatement();
							
							String newVarName = varName.getText();
							String newVarType = (String) jop.getSelectedItem();
							dbStatement.executeUpdate("INSERT INTO vars VALUES ('"+newVarName+"','"+newVarType+"')");
							
							if(newVarType.contains("int"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS INTEGER deterministic no sql RETURN @"+newVarName);
								
							};
							if(newVarType.contains("string"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS varchar(5000) deterministic no sql RETURN @"+newVarName);
								
							};
							if(newVarType.contains("time"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS datetime deterministic no sql RETURN @"+newVarName);
								
							};
							
							if(newVarType.contains("double"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS double deterministic no sql RETURN @"+newVarName);
								
							};
						
							
							String sqlQuery = "SELECT * " +
	                    	     	  "FROM vars ";
	                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	                    	rs.beforeFirst();
	                    	
	                    	if (rs.next()){newAllVars = "";}
	                    	rs.beforeFirst();

	                    	while(rs.next())
	                    	{	
	                    	String name = rs.getString("name");
	                    	String type = rs.getString("type");
	                    	newAllVars+= "&nbsp; &nbsp;"+name + " - " + type + "<br>";
	                    	}
						} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);}
                   	
                   	
                   	  		
											definedVars.setText("<html><h3>&nbsp; &nbsp;Defined variable functions: </h3><br>"+newAllVars+"</html>");
                                          	pane.validate();
                   						pane.repaint();
                                         }
                                   }
                           );
       
       setSize(480,350);
       setModal(true);
       setLocationRelativeTo(null);
       setVisible(true);
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
   } 

}




