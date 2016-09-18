package org.processmining.plugins.miningresourceprofiles.inputs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DefineDEAInOut extends JFrame{
	
	public String allVars = new String("<html>No variables are currently defined <br></html>");
	public String allInputs = new String("<html>No inputs are currently defined <br></html>");
	public String allOutputs = new String("<html>No outputs are currently defined <br></html>");
	
	public JLabel definedVars;
	public JLabel definedInputs;
	public JLabel definedOutputs;
	 
	public void addDEAInput(final Connection con) throws Exception
{

Statement dbStatement = con.createStatement();

String sqlQuery2 = "SELECT * " +
	   	  "FROM vars ";
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	if (rs2.next()){allVars = "<html>Variables:<br>";}
	rs2.beforeFirst();

	while(rs2.next())
	{	
	String varName = rs2.getString("name");
	String varType = rs2.getString("type");
	allVars+= varName + " - " + varType + "<br>";
	}
allVars+="</html>";

String sqlQuery = "SELECT * " +
     	  "FROM deainput ";
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){allInputs = "<html>Defined DEA Inputs:<br>";}
rs.beforeFirst();

while(rs.next())
{	
String id = rs.getString("id");
String name = rs.getString("name");
allInputs+= id+". "+name + "<br>";
}
allInputs+="</html>";

definedVars=new JLabel();
definedVars.setText(allVars);

definedInputs=new JLabel();
definedInputs.setText(allInputs);

final JTextField RBIName=new JTextField();
RBIName.setText("DEA Input name"); 
final JTextField RBIDef=new JTextField();
RBIDef.setText("DEA Input definition"); 
 
 
JButton but=new JButton();
but.setText("Add DEA Input");
 
    final Container pane = getContentPane();
   	pane.add(definedInputs, BorderLayout.PAGE_START);
	pane.add(RBIName, BorderLayout.LINE_START);
	pane.add(RBIDef, BorderLayout.CENTER);
	pane.add(definedVars, BorderLayout.LINE_END);
	pane.add(but, BorderLayout.PAGE_END);
	
    but.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) {
                                        
                	Statement dbStatement;
					try {
						dbStatement = con.createStatement();
						
						String newRBIName = RBIName.getText();
						String newRBIDef = RBIDef.getText();
						
						dbStatement.executeUpdate("INSERT INTO deainput(name, definition) VALUES ('"+newRBIName+"','"+newRBIDef+"')");
					
						String sqlQuery1 = "SELECT MAX(id) as m FROM deainput";
						ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
						rs1.beforeFirst();
						String lastid = "";
						if (rs1.next()){lastid = rs1.getString("m");}
												
						String sqlQuery2 = "SELECT definition FROM deainput where id='"+lastid+"'";
						ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
						rs2.beforeFirst();
						String newDefinition = "";
						if (rs2.next()){newDefinition = rs2.getString("definition");}
						
						dbStatement.executeUpdate("CREATE VIEW input"+lastid+" as "+newDefinition);
									
						String sqlQuery = "SELECT * " +
                    	     	  "FROM deainput ";
                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
                    	rs.beforeFirst();
                    	if (rs.next()){allInputs = "<html>Defined DEA inputs:<br>";}
                    	rs.beforeFirst();

                    	while(rs.next())
                    	{	
                    		String id = rs.getString("id");
                    		String name = rs.getString("name");
                    		allInputs+= id+". "+name + "<br>";
                 }
					
                    	allInputs+="</html>";
                    	definedInputs.setText(allInputs);
                          
                       	pane.validate();
						pane.repaint();
               
					
					
					} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);}
                	
                	       }
                          }
                        );
    
    setSize(1300,700);
    setVisible(true);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
} 

	public void addDEAOutput(final Connection con) throws Exception
	{

	Statement dbStatement = con.createStatement();

	String sqlQuery2 = "SELECT * " +
		   	  "FROM vars ";
		ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
		if (rs2.next()){allVars = "<html>Variables:<br>";}
		rs2.beforeFirst();

		while(rs2.next())
		{	
		String varName = rs2.getString("name");
		String varType = rs2.getString("type");
		allVars+= varName + " - " + varType + "<br>";
		}
	allVars+="</html>";

	String sqlQuery = "SELECT * " +
	     	  "FROM deaoutput ";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	if (rs.next()){allOutputs = "<html>Defined DEA outputs:<br>";}
	rs.beforeFirst();

	while(rs.next())
	{	
	String id = rs.getString("id");
	String name = rs.getString("name");
	allOutputs+= id+". "+name + "<br>";
	}
	allOutputs+="</html>";

	definedVars=new JLabel();
	definedVars.setText(allVars);

	definedOutputs=new JLabel();
	definedOutputs.setText(allOutputs);

	final JTextField RBIName=new JTextField();
	RBIName.setText("DEA Output name"); 
	final JTextField RBIDef=new JTextField();
	RBIDef.setText("DEA Output definition"); 
	 
	 
	JButton but=new JButton();
	but.setText("Add DEA Output");
	 
	    final Container pane = getContentPane();
	   	pane.add(definedOutputs, BorderLayout.PAGE_START);
		pane.add(RBIName, BorderLayout.LINE_START);
		pane.add(RBIDef, BorderLayout.CENTER);
		pane.add(definedVars, BorderLayout.LINE_END);
		pane.add(but, BorderLayout.PAGE_END);
		
	    but.addActionListener(
	            new ActionListener(){
	                public void actionPerformed(
	                        ActionEvent e) {
	                                        
	                	Statement dbStatement;
						try {
							dbStatement = con.createStatement();
							
							String newRBIName = RBIName.getText();
							String newRBIDef = RBIDef.getText();
							
							dbStatement.executeUpdate("INSERT INTO deaoutput(name, definition) VALUES ('"+newRBIName+"','"+newRBIDef+"')");
						
							String sqlQuery1 = "SELECT MAX(id) as m FROM deaoutput";
							ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
							rs1.beforeFirst();
							String lastid = "";
							if (rs1.next()){lastid = rs1.getString("m");}
													
							String sqlQuery2 = "SELECT definition FROM deaoutput where id='"+lastid+"'";
							ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
							rs2.beforeFirst();
							String newDefinition = "";
							if (rs2.next()){newDefinition = rs2.getString("definition");}
							
							dbStatement.executeUpdate("CREATE VIEW output"+lastid+" as "+newDefinition);
										
							String sqlQuery = "SELECT * " +
	                    	     	  "FROM deaoutput ";
	                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	                    	rs.beforeFirst();
	                    	if (rs.next()){allOutputs = "<html>Defined Outputs:<br>";}
	                    	rs.beforeFirst();

	                    	while(rs.next())
	                    	{	
	                    		String id = rs.getString("id");
	                    		String name = rs.getString("name");
	                    		allOutputs+= id+". "+name + "<br>";
	                 }
						
	                    	allOutputs+="</html>";
	                    	definedOutputs.setText(allOutputs);
	                          
	                       	pane.validate();
							pane.repaint();
	               
						
						
						} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);}
	                	
	                	       }
	                          }
	                        );
	    
	    setSize(1300,700);
	    setVisible(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	} 

}




