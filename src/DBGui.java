import java.sql.*;
import java.util.Properties;
import java.util.ArrayList;

import javax.swing.*;

public class DBGui {

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Database Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
      "jdbc:mysql://68.175.70.96/ESPN";

    public static String rundb () throws Exception {
        Class.forName(dbClassName);

        Properties p = new Properties();
        p.put("user","miles");
        p.put("password","ESPN");

        Connection con = DriverManager.getConnection(CONNECTION,p);

        Statement stmt = null;
        String query = "select * from Player P, Team T where P.team_id = T.team_id;";

        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        String ret = "";
        
        while (rs.next()) {
            String name = rs.getString("player_name") + ", " +
                rs.getString("email");
            int tid = rs.getInt("team_id");
            String tname = rs.getString("T.name");
            ret += "Team " + tid + " (" + tname + " ) " + name; 
        }
        con.close();
        return ret;
    }
}
