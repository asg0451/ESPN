import java.sql.*;
import java.util.Properties;
import java.util.ArrayList;

import javax.swing.*;

public class DBGui {

    private static void createAndShowGUI() {
	//Create and set up the window.
	JFrame frame = new JFrame("HelloWorldSwing");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//Add the ubiquitous "Hello World" label.
	JLabel label;
	try {
	    label = new JLabel(rundb());
	} catch (Exception e) { label  = new JLabel(e.toString());}
	frame.getContentPane().add(label);

	//Display the window.
	frame.pack();
	frame.setVisible(true);
    }

    public static void main(String[] args) {
	//Schedule a job for the event-dispatching thread:
	//creating and showing this application's GUI.
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		createAndShowGUI();
	    }
	});
    }

    private static final String dbClassName = "com.mysql.jdbc.Driver";
    private static final String CONNECTION =
      "jdbc:mysql://68.175.70.96/testing";

    public static String rundb () throws Exception {
	Class.forName(dbClassName);

	Properties p = new Properties();
	p.put("user","miles");
	p.put("password","ESPN");

	Connection con = DriverManager.getConnection(CONNECTION,p);

	Statement stmt = null;
	String query = "select * from employees;";

	stmt = con.createStatement();
	ResultSet rs = stmt.executeQuery(query);

	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();

	while (rs.next()) {
	    String name = rs.getString("first_name") + " " +
		rs.getString("last_name");
	    int id = rs.getInt("id");
	    ids.add(id);
	    names.add(name);
	}

	String ret = "";

	for(int i=0; i< ids.size(); i++) {
	    ret += (ids.get(i)+ " " + names.get(i));
	}
	con.close();
	return ret;
    }
}