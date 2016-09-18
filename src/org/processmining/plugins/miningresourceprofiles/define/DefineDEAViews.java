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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.framework.util.ui.widgets.ProMTextField;
import org.processmining.plugins.miningresourceprofiles.outputs.UISettings;

import com.fluxicon.slickerbox.components.HeaderBar;
import com.fluxicon.slickerbox.components.SlickerButton;

@SuppressWarnings("serial")
public class DefineDEAViews extends JDialog{
	
	public String allVars = new String("<html>No variables are currently defined <br></html>");
	public String allInputs = new String("<html>No DEA inputs are currently defined <br>");
	public String allOutputs = new String("<html>No DEA outputs are currently defined <br>");
	public String allViews = new String("<html>No supporting views are currently defined <br></html>");

	
	public JLabel definedVars;
	public JLabel definedRBIs;
	public JLabel inputVar;
	public JLabel inputType;


public void defineDEAInput(final Connection con) throws Exception
{
	
	final HeaderBar pane = new HeaderBar("");	
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
			
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
definedVars.setText("<html> <h3>Variables:</h3><br>"+allVars+"</html>");
definedVars.setOpaque(true);
definedVars.setForeground(UISettings.Text_COLOR);
definedVars.setBackground(UISettings.BG_COLOR);

ProMScrollPane scrollPane = new ProMScrollPane(definedVars);
c.ipadx = 180;
c.ipady = 400;     
c.fill = JScrollPane.WIDTH;
c.gridx = 0;
c.gridy = 0;
pane.add(scrollPane, c);


String sqlQuery0 = "SELECT * FROM views ";
ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
rs0.beforeFirst();
if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
rs0.beforeFirst();

while(rs0.next())
{	
	String name = rs0.getString("name");
	String description = rs0.getString("description");

	allViews+= name+" - "+ description + "<br>";
}

String sqlQuery = "SELECT * FROM deainput ";
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){allInputs = allViews; allInputs += "<h3>Defined DEA inputs:</h3><br>";}else{allInputs += allViews;}
rs.beforeFirst();

while(rs.next())
{	
String id = rs.getString("id");
String name = rs.getString("name");
allInputs+= id+". "+name + "<br>";
}
allInputs+="</html>";

definedRBIs=new JLabel();
definedRBIs.setText(allInputs);
definedRBIs.setOpaque(true);
definedRBIs.setForeground(UISettings.Text_COLOR);
definedRBIs.setBackground(UISettings.BG_COLOR);
ProMScrollPane scrollPane2 = new ProMScrollPane(definedRBIs);
c.ipadx = 750;      
c.ipady = 400;     
c.gridx = 1;
c.gridy = 0;
pane.add(scrollPane2, c);


inputVar=new JLabel();
inputVar.setText("<html>DEA input name: </html>");
inputVar.setForeground(UISettings.TextLight_COLOR);
c.ipadx = 180;
c.ipady = 20; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 1;
pane.add(inputVar, c);

final ProMTextField RBIName=new ProMTextField();
RBIName.setText(""); 
c.ipady = 20;
c.gridx = 1;
c.gridy = 1;
pane.add(RBIName, c);


inputType=new JLabel();
inputType.setText("<html>DEA input definition: </html>");
inputType.setForeground(UISettings.TextLight_COLOR);
c.ipady = 10; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 2;
pane.add(inputType, c);

final ProMTextField RBIDef=new ProMTextField();
RBIDef.setText(""); 
c.fill = JScrollPane.HEIGHT;
c.ipady = 20; 
c.gridx = 1;
c.gridy = 2;
pane.add(RBIDef, c);
 

SlickerButton but=new SlickerButton();
but.setText("Add DEA input");
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
			
					String sqlQuery0 = "SELECT * FROM views ";
					ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
					rs0.beforeFirst();
					if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
					rs0.beforeFirst();

					while(rs0.next())
					{	
						String name = rs0.getString("name");
						String description = rs0.getString("description");

						allViews+= name+" - "+ description + "<br>";
					}

					String sqlQuery = "SELECT * FROM deainput ";
					ResultSet rs = dbStatement.executeQuery(sqlQuery);
					rs.beforeFirst();
					if (rs.next()){allInputs = allViews; allInputs += "<h3>Defined DEA inputs:</h3><br>";}else{allInputs += allViews;}
					rs.beforeFirst();

					while(rs.next())
					{	
					String id = rs.getString("id");
					String name = rs.getString("name");
					allInputs+= id+". "+name + "<br>";
					}
					allInputs+="</html>";
					definedRBIs.setText(allInputs);
					
					
		           	pane.validate();
					pane.repaint();
           
				
				
				} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                        JOptionPane.ERROR_MESSAGE);}
            	
            	       }
                      }
                    );

SlickerButton defnewvarbut=new SlickerButton();
	defnewvarbut.setText("Define new variable");
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 1;
	pane.add(defnewvarbut, c);
	
	defnewvarbut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
             	   DefineVar defVar = new DefineVar();
             	   defVar.addVar(con);
             	   
              		Statement dbStatement = con.createStatement();
                    
             	  String sqlQuery2 = "SELECT * FROM vars ";
             	  	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
             	  	if (rs2.next()){allVars = "";}
             	  	rs2.beforeFirst();

             	  	while(rs2.next())
             	  	{	
             	  	String varName = rs2.getString("name");
             	  	String varType = rs2.getString("type");
             	  	allVars+= varName + " - " + varType + "<br>";
             	  	}
             	 
             	 definedVars.setText("<html> <h3>Variables:</h3><br>"+allVars+"</html>");
             	  				
 				
 		       	pane.validate();
 				pane.repaint();
             
             	   
                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );

	SlickerButton defnewviewbut=new SlickerButton();
		defnewviewbut.setText("Define new supporting view");
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		pane.add(defnewviewbut, c);


		defnewviewbut.addActionListener(
	         new ActionListener(){
	             public void actionPerformed(
	                     ActionEvent e) 
	             {try{
	             	DefineView defView = new DefineView();
	             	defView.addView(con);
		             	
	             	Statement dbStatement = con.createStatement();

					String sqlQuery0 = "SELECT * FROM views ";
					ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
					rs0.beforeFirst();
					if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
					rs0.beforeFirst();

					while(rs0.next())
					{	
						String name = rs0.getString("name");
						String description = rs0.getString("description");

						allViews+= name+" - "+ description + "<br>";
					}

					String sqlQuery = "SELECT * FROM deainput ";
					ResultSet rs = dbStatement.executeQuery(sqlQuery);
					rs.beforeFirst();
					if (rs.next()){allInputs = allViews; allInputs += "<h3>Defined DEA inputs:</h3><br>";}else{allInputs += allViews;}
					rs.beforeFirst();

					while(rs.next())
					{	
					String id = rs.getString("id");
					String name = rs.getString("name");
					allInputs+= id+". "+name + "<br>";
					}
					allInputs+="</html>";
					definedRBIs.setText(allInputs);
					
					
			       	pane.validate();
					pane.repaint();
	             	
	              }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	             }
	             				
	                             }
	                     );
    
 //----------------------------------------------------------------------------------------------------------------------    
    setSize(1130,650);
    setModal(true);
    setLocationRelativeTo(null);
    setVisible(true);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
} 


	
public void defineDEAOutput(final Connection con) throws Exception
{
	final HeaderBar pane = new HeaderBar("");
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
			
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
definedVars.setText("<html> <h3>Variables:</h3><br>"+allVars+"</html>");
definedVars.setOpaque(true);
definedVars.setForeground(UISettings.Text_COLOR);
definedVars.setBackground(UISettings.BG_COLOR);
ProMScrollPane scrollPane = new ProMScrollPane(definedVars);
c.ipadx = 180;
c.ipady = 400;     
c.fill = JScrollPane.WIDTH;
c.gridx = 0;
c.gridy = 0;
pane.add(scrollPane, c);


String sqlQuery0 = "SELECT * FROM views ";
ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
rs0.beforeFirst();
if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
rs0.beforeFirst();

while(rs0.next())
{	
	String name = rs0.getString("name");
	String description = rs0.getString("description");

	allViews+= name+" - "+ description + "<br>";
}

	
String sqlQuery = "SELECT * FROM deaoutput ";
ResultSet rs = dbStatement.executeQuery(sqlQuery);
rs.beforeFirst();
if (rs.next()){allOutputs = allViews; allOutputs += "<h3>Defined DEA outputs:</h3><br>";}else{allOutputs += allViews;}
rs.beforeFirst();

while(rs.next())
{	
String id = rs.getString("id");
String name = rs.getString("name");
allOutputs+= id+". "+name + "<br>";
}
allOutputs+="</html>";

definedRBIs=new JLabel();
definedRBIs.setText(allOutputs);
definedRBIs.setOpaque(true);
definedRBIs.setForeground(UISettings.Text_COLOR);
definedRBIs.setBackground(UISettings.BG_COLOR);
ProMScrollPane scrollPane2 = new ProMScrollPane(definedRBIs);
c.ipadx = 750;      
c.ipady = 400;     
c.gridx = 1;
c.gridy = 0;
pane.add(scrollPane2, c);

inputVar=new JLabel();
inputVar.setText("<html>DEA output name: </html>");
inputVar.setForeground(UISettings.TextLight_COLOR);
c.ipadx = 180;
c.ipady = 20; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 1;
pane.add(inputVar, c);

final ProMTextField RBIName=new ProMTextField();
RBIName.setText(""); 
c.ipady = 20;
c.gridx = 1;
c.gridy = 1;
pane.add(RBIName, c);


inputType=new JLabel();
inputType.setText("<html>DEA output definition: </html>");
inputType.setForeground(UISettings.TextLight_COLOR);
c.ipady = 20; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 2;
pane.add(inputType, c);

final ProMTextField RBIDef=new ProMTextField();
RBIDef.setText(""); 
c.fill = JScrollPane.HEIGHT;
c.ipady = 20; 
c.gridx = 1;
c.gridy = 2;
pane.add(RBIDef, c);
 
SlickerButton but=new SlickerButton();
but.setText("Add DEA output");
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
								
					String sqlQuery0 = "SELECT * FROM views ";
					ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
					rs0.beforeFirst();
					if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
					rs0.beforeFirst();

					while(rs0.next())
					{	
						String name = rs0.getString("name");
						String description = rs0.getString("description");

						allViews+= name+" - "+ description + "<br>";
					}

					
					String sqlQuery = "SELECT * FROM deaoutput ";
					ResultSet rs = dbStatement.executeQuery(sqlQuery);
					rs.beforeFirst();
					if (rs.next()){allOutputs = allViews; allOutputs += "<h3>Defined DEA outputs:</h3><br>";}else{allOutputs += allViews;}
					rs.beforeFirst();

					while(rs.next())
					{	
					String id = rs.getString("id");
					String name = rs.getString("name");
					allOutputs+= id+". "+name + "<br>";
					}
					allOutputs+="</html>";
					definedRBIs.setText(allOutputs);
					
					
		           	pane.validate();
					pane.repaint();
           
				
				
				} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                        JOptionPane.ERROR_MESSAGE);}
            	
            	       }
                      }
                    );


SlickerButton defnewvarbut=new SlickerButton();
	defnewvarbut.setText("Define new variable");
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 1;
	pane.add(defnewvarbut, c);
	
	defnewvarbut.addActionListener(
            new ActionListener(){
                public void actionPerformed(
                        ActionEvent e) 
                {try{
             	   DefineVar defVar = new DefineVar();
             	   defVar.addVar(con);
             	   
             		Statement dbStatement = con.createStatement();
                    
             	  String sqlQuery2 = "SELECT * FROM vars ";
             	  	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
             	  	if (rs2.next()){allVars = "";}
             	  	rs2.beforeFirst();

             	  	while(rs2.next())
             	  	{	
             	  	String varName = rs2.getString("name");
             	  	String varType = rs2.getString("type");
             	  	allVars+= varName + " - " + varType + "<br>";
             	  	}
             	 
             	 definedVars.setText("<html> <h3>Variables:</h3><br>"+allVars+"</html>");
             	  				
 				
 		       	pane.validate();
 				pane.repaint();
             
             	   
                 }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
                         JOptionPane.ERROR_MESSAGE);};
                }
                				
                                }
                        );

	SlickerButton defnewviewbut=new SlickerButton();
		defnewviewbut.setText("Define new supporting view");
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		pane.add(defnewviewbut, c);


		defnewviewbut.addActionListener(
	         new ActionListener(){
	             public void actionPerformed(
	                     ActionEvent e) 
	             {try{
	             	DefineView defView = new DefineView();
	             	defView.addView(con);
		             	
	             	Statement dbStatement = con.createStatement();

					String sqlQuery0 = "SELECT * FROM views ";
					ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
					rs0.beforeFirst();
					if (rs0.next()){allViews = "<html><h3>Defined supporting views:</h3><br>";}
					rs0.beforeFirst();

					while(rs0.next())
					{	
						String name = rs0.getString("name");
						String description = rs0.getString("description");

						allViews+= name+" - "+ description + "<br>";
					}

					
					String sqlQuery = "SELECT * FROM deaoutput ";
					ResultSet rs = dbStatement.executeQuery(sqlQuery);
					rs.beforeFirst();
					if (rs.next()){allOutputs = allViews; allOutputs += "<h3>Defined DEA outputs:</h3><br>";}else{allOutputs += allViews;}
					rs.beforeFirst();

					while(rs.next())
					{	
					String id = rs.getString("id");
					String name = rs.getString("name");
					allOutputs+= id+". "+name + "<br>";
					}
					allOutputs+="</html>";
					definedRBIs.setText(allOutputs);
					
					
			       	pane.validate();
					pane.repaint();
	             	
	              }catch(Exception e1){JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);};
	             }
	             				
	                             }
	                     );
    
    setSize(1130,650);
    setModal(true);
    setLocationRelativeTo(null);
    setVisible(true);
    setDefaultCloseOperation(HIDE_ON_CLOSE);
} 


}




