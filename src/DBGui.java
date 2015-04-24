import java.sql.*;
import java.util.Properties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DBGui {
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION  = "jdbc:mysql://68.175.70.96/ESPN";
	
	private static JButton t_info_btn;
	private static JTextField t_info_id;
	private static JTextArea t_info_res;
	
	private static JButton p_info_btn;
	private static JTextField p_info_name;
	private static JTextArea p_info_res;

	private static JButton tournament_info_btn;
	private static JTextField tournament_info_name;
	private static JTextArea tournament_info_res; 
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Database Results");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       	addComponentsToPane(frame.getContentPane());
        //frame.getContentPane().add(label);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public static void addComponentsToPane(Container contentPane) {
        contentPane.setLayout(new GridLayout(0,4)); // 4 columns
        //////Team Info row
        contentPane.add(new JLabel("Team Info. Please enter team id."));
        t_info_id = new JTextField();  // enter the id
        contentPane.add(t_info_id);

        t_info_btn = new JButton("Search Database");
        t_info_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			t_info_res.setText(teamStats(Integer.parseInt(t_info_id.getText())));        			
        		} catch (Exception ex) {}
        	}
        });
        contentPane.add(t_info_btn);
        t_info_res = new JTextArea(2,42);
        contentPane.add(t_info_res);
        
        //////Player Info row
        contentPane.add(new JLabel("Player Info. Please enter Player Name."));
        p_info_name = new JTextField();  // enter the id
        contentPane.add(p_info_name);

        p_info_btn = new JButton("Search Database");
        p_info_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			p_info_res.setText(playerInfo(p_info_name.getText()));
        		} catch (Exception ex) {}
        	}
        });
        contentPane.add(p_info_btn);
        p_info_res = new JTextArea(2,42);
        contentPane.add(p_info_res);
        //////Tournament Info row
        contentPane.add(new JLabel("Tournament Info. Please enter Tournament Name."));
        tournament_info_name = new JTextField();  // enter the id
        contentPane.add(tournament_info_name);

        tournament_info_btn = new JButton("Search Database");
        tournament_info_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			tournament_info_res.setText(tournamentInfo(tournament_info_name.getText()));
        		} catch (Exception ex) {}
        	}
        });
        contentPane.add(tournament_info_btn);
        tournament_info_res = new JTextArea(2,42);
        contentPane.add(tournament_info_res);
        
    }
    
    public static String teamStats(int id) throws Exception {
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user","miles");
        p.put("password","ESPN");
        Connection con = DriverManager.getConnection(CONNECTION,p);

        String team_info = "select * from Team where team_id = ?;";
        PreparedStatement pstmt_tinfo = con.prepareStatement(team_info);
        pstmt_tinfo.setInt(1,id);
        
        ResultSet rs = pstmt_tinfo.executeQuery();
        String ret = "";
        while (rs.next())
             ret = rs.getString("team_id")+ " (" + rs.getString("name") + "), " +
            		 rs.getInt("rank");
        ret +="\n";
        
        String players =
        		"select * from Player P, Team T where T.team_id = ? and P.team_id = T.team_id;";
        PreparedStatement pstmt_pinfo = con.prepareStatement(players);
        pstmt_pinfo.setInt(1,id);
        
        ResultSet rs2 = pstmt_pinfo.executeQuery();
        while (rs2.next())
             ret += "\t" +rs2.getString("player_name") + ", " + rs2.getString("player_role") +
             			", " + rs2.getFloat("kdr") + " \n";
             
        con.close();
        return ret;
    }
    
    public static String playerInfo(String pname) throws Exception {
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user","miles");
        p.put("password","ESPN");
        Connection con = DriverManager.getConnection(CONNECTION,p);

        String query = "select * from Player P where P.player_name = ?;";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1,pname);
        
        ResultSet rs = pstmt.executeQuery();
        String ret = "";
        while (rs.next()) {
             ret += "name: " + rs.getString("player_name") + ", email: " +
                rs.getString("email") + ", role: " + rs.getString("player_role") + 
                	", team_id: " + rs.getInt("team_id") + ", kdr: " + 
                		rs.getInt("kdr");
        }
        con.close();
        return ret;
    }

	public static String tournamentInfo(String tournamentName) throws Exception{
        Class.forName(dbClassName);
        Properties p = new Properties();
        p.put("user","miles");
        p.put("password","ESPN");
        Connection con = DriverManager.getConnection(CONNECTION,p);

        String query = "select * from Tournament T where T.tournament_name = ?;";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1,tournamentName);
        
        ResultSet rs = pstmt.executeQuery();
        String ret = "";
        while (rs.next()) {
             ret += "Tournament Name: " + rs.getString("tournament_name") + "\n Year: " +
                rs.getString("tournament_year") + "\n Winning Team ID: " + rs.getInt("winning_team_id") + 
                	"\n Held in: " + rs.getString("country");
        }
        con.close();
        return ret;
		
	}
}