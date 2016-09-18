package org.processmining.plugins.miningresourceprofiles.define;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DefineIndicators extends JDialog{
	
	public String allVars = new String("<html>No variables are currently defined <br></html>");
	public String allRBIs = new String("<html>No indicators are currently defined <br></html>");
	
	public JLabel definedVars;
	public JLabel definedRBIs;
	public JLabel inputVar;
	public JLabel inputType;


public void addIndicator(final Connection con) throws Exception
{
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;

	
Statement dbStatement = con.createStatement();

String sqlQuery2 = "SELECT * FROM vars ";
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	if (rs2.next()){allVars = "<html>";}
	rs2.beforeFirst();

	while(rs2.next())
	{	
	String varName = rs2.getString("name");
	String varType = rs2.getString("type");
	allVars+= varName + " - " + varType + "<br>";
	}
allVars+="</html>";

definedVars=new JLabel();
definedVars.setText("<html> Variables:<br>"+allVars+"</html>");
JScrollPane scrollPane = new JScrollPane(definedVars);
c.ipadx = 80;
c.ipady = 400;     
c.fill = JScrollPane.WIDTH;
c.gridx = 0;
c.gridy = 0;
pane.add(scrollPane, c);

	
String sqlQuery = "SELECT * FROM RBIs ";
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){allRBIs = "<html>Defined indicators:<br>";}
rs.beforeFirst();

while(rs.next())
{	
String id = rs.getString("id");
String name = rs.getString("name");
allRBIs+= id+". "+name + "<br>";
}
allRBIs+="</html>";

definedRBIs=new JLabel();
definedRBIs.setText(allRBIs);
JScrollPane scrollPane2 = new JScrollPane(definedRBIs);
c.ipadx = 1000;      
c.ipady = 400;     
c.gridx = 1;
c.gridy = 0;
pane.add(scrollPane2, c);

inputVar=new JLabel();
inputVar.setText("<html>Indicator name: </html>");
c.ipadx = 80;
c.ipady = 30; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 1;
pane.add(inputVar, c);

final JTextField RBIName=new JTextField();
RBIName.setText(""); 
JScrollPane scrollPane4 = new JScrollPane(RBIName);
c.ipady = 30;
c.gridx = 1;
c.gridy = 1;
pane.add(scrollPane4, c);

inputType=new JLabel();
inputType.setText("<html>Indicator definition: </html>");
c.ipady = 10; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 2;
pane.add(inputType, c);


final JTextField RBIDef=new JTextField();
RBIDef.setText(""); 
JScrollPane scrollPane3 = new JScrollPane(RBIDef);
c.fill = JScrollPane.HEIGHT;
c.ipady = 30; 
c.gridx = 1;
c.gridy = 2;
pane.add(scrollPane3, c);
 
 
JButton but=new JButton();
but.setText("Add indicator");
c.gridwidth = 2;
c.ipady = 10;
c.gridx = 0;
c.gridy = 3;
pane.add(but, c);
 
	
    but.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) {
                                        
                	Statement dbStatement;
					try {
						dbStatement = con.createStatement();
						
						String newRBIName = RBIName.getText();
						String newRBIDef = RBIDef.getText();
						
						dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+newRBIName+"','"+newRBIDef+"')");
					
						String sqlQuery1 = "SELECT MAX(id) as m FROM RBIs";
						ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
						rs1.beforeFirst();
						String lastid = "";
						if (rs1.next()){lastid = rs1.getString("m");}
												
						String sqlQuery2 = "SELECT definition FROM RBIs where id='"+lastid+"'";
						ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
						rs2.beforeFirst();
						String newDefinition = "";
						if (rs2.next()){newDefinition = rs2.getString("definition");}
						
						dbStatement.executeUpdate("CREATE VIEW rbi"+lastid+" as "+newDefinition);
									
						String sqlQuery = "SELECT * " +
                    	     	  "FROM RBIs ";
                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
                    	rs.beforeFirst();
                    	if (rs.next()){allRBIs = "<html>Defined indicators:<br>";}
                    	rs.beforeFirst();

                    	while(rs.next())
                    	{	
                    		String id = rs.getString("id");
                    		String name = rs.getString("name");
                    		allRBIs+= id+". "+name + "<br>";
                 }
					
                    	allRBIs+="</html>";
                    	definedRBIs.setText(allRBIs);
                          
                       	pane.validate();
						pane.repaint();
               
					
					
					} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);}
                	
                	       }
                          }
                        );
    
    setSize(1200,600);
    setModal(true);
    setVisible(true);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
} 

 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addVar(final Connection con) throws Exception
   {
	final Container pane = getContentPane();
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.CENTER;
	
		   
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
	allVars+= name + " - " + type + "<br>";
	}
	
	definedVars=new JLabel();
	definedVars.setText("<html>Defined variables: <br>"+allVars+"</html>");
	JScrollPane scrollPane = new JScrollPane(definedVars);
	c.ipady = 60;     
	c.gridwidth = 3;
	c.fill = JScrollPane.WIDTH;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(scrollPane, c);

	
	inputVar=new JLabel();
	inputVar.setText("<html>New variable name: </html>");
	c.ipady = 10; 
	c.gridwidth = 1;
	c.gridx = 0;
	c.gridy = 2;
	pane.add(inputVar, c);
	
	final JTextField varName=new JTextField();
	c.gridx = 1;
	c.gridy = 2;
	c.ipadx = 100;
	pane.add(varName, c);
  	
	inputType=new JLabel();
	inputType.setText("<html>New variable type: </html>");
	c.gridx = 0;
	c.gridy = 3;
	pane.add(inputType, c);
	
	 DefaultComboBoxModel model = new DefaultComboBoxModel();
     model.addElement("int");
     model.addElement("time");
     model.addElement("string");
     final JComboBox jop = new JComboBox(model);
     c.gridx = 1;
 	 c.gridy = 3;
 	 pane.add(jop, c);

		
	JButton but=new JButton();
	but.setText("Add variable");
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 3;
	pane.add(but, c);
      
     	
       but.addActionListener(
               new ActionListener(){
                   public void actionPerformed(
                           ActionEvent e) {
                                           
                   	Statement dbStatement;
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
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS varchar(50) deterministic no sql RETURN @"+newVarName);
								
							};
							if(newVarType.contains("time"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS datetime deterministic no sql RETURN @"+newVarName);
								
							};
							
							String sqlQuery = "SELECT * " +
	                    	     	  "FROM vars ";
	                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	                    	rs.beforeFirst();
	                    	if (rs.next()){allVars = "";}
	                    	rs.beforeFirst();

	                    	while(rs.next())
	                    	{	
	                    	String name = rs.getString("name");
	                    	String type = rs.getString("type");
	                    	allVars+= name + " - " + type + "<br>";
	                    	}
						} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);}
                   	
                   	
                                    		
											definedVars.setText("<html>Defined variables: <br>"+allVars+"</html>");
                                          	pane.validate();
                   						pane.repaint();
                                         }
                                   }
                           );
       
       setSize(350,250);
       setModal(true);
       setVisible(true);
       setDefaultCloseOperation(HIDE_ON_CLOSE);
   } 


	
	public void addVarOld(final Connection con) throws Exception
    {

	Statement dbStatement = con.createStatement();
	String sqlQuery = "SELECT * " +
	     	  "FROM vars ";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();
	if (rs.next()){allVars = "";}
	rs.beforeFirst();

	while(rs.next())
	{	
	String name = rs.getString("name");
	String type = rs.getString("type");
	allVars+= name + " - " + type + "<br>";
	}
	
	definedVars=new JLabel();
	definedVars.setText("<html>Defined variables: <br>"+allVars+"</html>");
    
	final JTextField varName=new JTextField();
    varName.setText("Variable name"); 
    final JTextField varType=new JTextField();
    varType.setText("Variable type: int/string/time"); 
     
    JButton but=new JButton();
    but.setText("Add variable");
       
        final Container pane = getContentPane();
    	pane.add(definedVars, BorderLayout.PAGE_START);
    	pane.add(varName, BorderLayout.CENTER);
    	pane.add(varType, BorderLayout.LINE_END);
    	pane.add(but, BorderLayout.PAGE_END);
    	
        but.addActionListener(
                new ActionListener(){
                    public void actionPerformed(
                            ActionEvent e) {
                                            
                    	Statement dbStatement;
						try {
							dbStatement = con.createStatement();
							
							String newVarName = varName.getText();
							String newVarType = varType.getText();
							dbStatement.executeUpdate("INSERT INTO vars VALUES ('"+newVarName+"','"+newVarType+"')");
							
							if(newVarType.contains("int"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS INTEGER deterministic no sql RETURN @"+newVarName);
								
							};
							if(newVarType.contains("string"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS varchar(50) deterministic no sql RETURN @"+newVarName);
								
							};
							if(newVarType.contains("time"))
							{
								dbStatement.executeUpdate("CREATE FUNCTION "+newVarName+"() RETURNS datetime deterministic no sql RETURN @"+newVarName);
								
							};
							
							String sqlQuery = "SELECT * " +
	                    	     	  "FROM vars ";
	                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	                    	rs.beforeFirst();
	                    	if (rs.next()){allVars = "";}
	                    	rs.beforeFirst();

	                    	while(rs.next())
	                    	{	
	                    	String name = rs.getString("name");
	                    	String type = rs.getString("type");
	                    	allVars+= name + " - " + type + "<br>";
	                    	}
						} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
		                         JOptionPane.ERROR_MESSAGE);}
                    	
                                      		
											definedVars.setText("<html>Defined variables: <br>"+allVars+"</html>");
                                           	pane.validate();
                    						pane.repaint();
                                          }
                                    }
                            );
        
        setSize(400,400);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    } 

public void addIndicatorOld(final Connection con) throws Exception
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
     	  "FROM RBIs ";
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){allRBIs = "<html>Defined indicators:<br>";}
rs.beforeFirst();

while(rs.next())
{	
String id = rs.getString("id");
String name = rs.getString("name");
allRBIs+= id+". "+name + "<br>";
}
allRBIs+="</html>";

definedVars=new JLabel();
definedVars.setText(allVars);

definedRBIs=new JLabel();
definedRBIs.setText(allRBIs);


final JTextField RBIName=new JTextField();
RBIName.setText("Indicator name"); 
final JTextField RBIDef=new JTextField();
RBIDef.setText("Indicator definition"); 
 
 
JButton but=new JButton();
but.setText("Add indicator");
 
    final Container pane = getContentPane();
   	pane.add(definedRBIs, BorderLayout.PAGE_START);
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
						
						dbStatement.executeUpdate("INSERT INTO RBIs(name, definition) VALUES ('"+newRBIName+"','"+newRBIDef+"')");
						
						String sqlQuery1 = "SELECT MAX(id) as m FROM RBIs";
						ResultSet rs1 = dbStatement.executeQuery(sqlQuery1);
						rs1.beforeFirst();
						String lastid = "";
						if (rs1.next()){lastid = rs1.getString("m");}
												
						String sqlQuery2 = "SELECT definition FROM RBIs where id='"+lastid+"'";
						ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
						rs2.beforeFirst();
						String newDefinition = "";
						if (rs2.next()){newDefinition = rs2.getString("definition");}
						
						dbStatement.executeUpdate("CREATE VIEW rbi"+lastid+" as "+newDefinition);
									
						String sqlQuery = "SELECT * " +
                    	     	  "FROM RBIs ";
                    	ResultSet rs = dbStatement.executeQuery(sqlQuery);
                    	rs.beforeFirst();
                    	if (rs.next()){allRBIs = "<html>Defined indicators:<br>";}
                    	rs.beforeFirst();

                    	while(rs.next())
                    	{	
                    		String id = rs.getString("id");
                    		String name = rs.getString("name");
                    		allRBIs+= id+". "+name + "<br>";
                 }
					
                    	allRBIs+="</html>";
                    	definedRBIs.setText(allRBIs);
                          
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


public Vector <String> getVars(final Connection con) throws Exception
{
Vector <String> definedRBIVars = new Vector<String>(); 
Statement dbStatement = con.createStatement();

String sqlQuery = "SELECT * " +
	   	  "FROM vars ";
	ResultSet rs = dbStatement.executeQuery(sqlQuery);
	rs.beforeFirst();

	while(rs.next())
	{	
	String varName = rs.getString("name");
	definedRBIVars.add(varName);
	}
	
return definedRBIVars;
}


}




