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
public class DefineView extends JDialog{
	
	public String allVars = new String("<html>&nbsp; &nbsp;No variable functions are currently defined <br></html>");
	public String allViews = new String("<html>&nbsp; &nbsp;No supporting views are currently defined <br></html>");

	
	public JLabel definedVars;
	public JLabel definedRBIs;
	public JLabel inputVar;
	public JLabel inputType;
	public JLabel shortName;


public void addView(final Connection con) throws Exception
{
	final HeaderBar pane = new HeaderBar("");
	setContentPane(pane);
	getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY) );
	
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.RELATIVE;
	
Statement dbStatement = con.createStatement();

String sqlQuery2 = "SELECT * FROM vars ";
	ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
	if (rs2.next()){allVars = "<html>";}
	rs2.beforeFirst();

	while(rs2.next())
	{	
	String varName = rs2.getString("name");
	String varType = rs2.getString("type");
	allVars+= "&nbsp; &nbsp;"+varName + "() - " + varType + "<br>";
	}
allVars+="</html>";

definedVars=new JLabel();
definedVars.setText("<html><h3>&nbsp; &nbsp;Defined variable functions:</h3><br>"+allVars+"</html>");
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
if (rs0.next()){allViews = "<html><h3>&nbsp; &nbsp;Defined supporting views:</h3><br>";}
rs0.beforeFirst();

while(rs0.next())
{	
	String name = rs0.getString("name");
	String description = rs0.getString("description");

	allViews+= "&nbsp; &nbsp;"+name+" - "+ description + "<br>";
}
allViews+="</html>";



definedRBIs=new JLabel();
definedRBIs.setOpaque(true);
definedRBIs.setForeground(UISettings.Text_COLOR);
definedRBIs.setBackground(UISettings.BG_COLOR);

definedRBIs.setText(allViews);
ProMScrollPane scrollPane2 = new ProMScrollPane(definedRBIs);
c.ipadx = 800;      
c.ipady = 400;     
c.gridx = 1;
c.gridy = 0;
pane.add(scrollPane2, c);

inputVar=new JLabel();
inputVar.setForeground(UISettings.TextLight_COLOR);
inputVar.setText("<html>&nbsp; &nbsp;View name: </html>");
c.ipadx = 180;
c.ipady = 30; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 1;
pane.add(inputVar, c);

final ProMTextField RBIName=new ProMTextField();
RBIName.setText(""); 
//JScrollPane scrollPane4 = new JScrollPane(RBIName);
c.ipady = 30;
c.gridx = 1;
c.gridy = 1;
pane.add(RBIName, c);


inputType=new JLabel();
inputType.setForeground(UISettings.TextLight_COLOR);
inputType.setText("<html>&nbsp; &nbsp;View definition: </html>");
c.ipady = 10; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 2;
pane.add(inputType, c);

final ProMTextField RBIDef=new ProMTextField();
RBIDef.setText(""); 
//JScrollPane scrollPane3 = new JScrollPane(RBIDef);
c.fill = JScrollPane.HEIGHT;
c.ipady = 30; 
c.gridx = 1;
c.gridy = 2;
pane.add(RBIDef, c);


shortName=new JLabel();
shortName.setForeground(UISettings.TextLight_COLOR);
shortName.setText("<html>&nbsp; &nbsp;View shortcut (unique): </html>");
c.ipadx = 180;
c.ipady = 30; 
c.gridwidth = 1;
c.gridx = 0;
c.gridy = 3;
pane.add(shortName, c);

final ProMTextField shortcut=new ProMTextField();
shortcut.setText(""); 
//JScrollPane scrollPane5 = new JScrollPane(shortcut);
c.ipady = 30;
c.gridx = 1;
c.gridy = 3;
pane.add(shortcut, c);
 
 
SlickerButton but=new SlickerButton();
but.setText("Add view");
c.gridwidth = 2;
c.ipady = 10;
c.gridx = 0;
c.gridy = 4;
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
						String newRBIID = shortcut.getText();
						
						dbStatement.executeUpdate("INSERT INTO views(name, description, definition) VALUES ('"+newRBIID+"','"+newRBIName+"','"+newRBIDef+"')");
					
						
						String lastid = newRBIID;
													
						String sqlQuery2 = "SELECT definition FROM views where name='"+lastid+"'";
						ResultSet rs2 = dbStatement.executeQuery(sqlQuery2);
						rs2.beforeFirst();
						String newDefinition = "";
						if (rs2.next()){newDefinition = rs2.getString("definition");}
						
							
						dbStatement.executeUpdate("CREATE VIEW "+lastid+" as "+newDefinition);
									
			
						String sqlQuery0 = "SELECT * FROM views ";
						ResultSet rs0 = dbStatement.executeQuery(sqlQuery0);
						rs0.beforeFirst();
						if (rs0.next()){allViews = "<html><h3>&nbsp; &nbsp;Defined supporting views:</h3><br>";}
						rs0.beforeFirst();

						while(rs0.next())
						{	
							String name = rs0.getString("name");
							String description = rs0.getString("description");

							allViews+= "&nbsp; &nbsp;"+name+" - "+ description + "<br>";
						}
						allViews+="</html>";
						definedRBIs.setText(allViews);
	
				        pane.validate();
						pane.repaint();
 					
					} catch (SQLException e1) {JOptionPane.showMessageDialog(null, e1.toString(), "Error",
	                         JOptionPane.ERROR_MESSAGE);}
                	
                	       }
                          }
                        );
    
     
    setSize(1100,700);
    setModal(true);
    setLocationRelativeTo(null);
    setVisible(true);
   setDefaultCloseOperation(DISPOSE_ON_CLOSE);

} 



}




