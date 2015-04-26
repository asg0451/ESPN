import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class DBEntryGui {
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION  = "jdbc:mysql://68.175.70.96/ESPN";

	private static JButton t_info_btn;
	private static JTextField t_info_id1;
	private static JTextField t_info_id2;
	private static JTextField t_info_tname;
	private static JTextArea t_info_res;


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
	contentPane.setLayout(new GridLayout(0,5));

	contentPane.add(new JLabel("New Game entry. Please enter team ids, winning team first, and tournament name."));
	t_info_id1 = new JTextField();
	contentPane.add(t_info_id1);
	t_info_id2 = new JTextField();
	contentPane.add(t_info_id2);
	t_info_tname = new JTextField();
	contentPane.add(t_info_tname);

	t_info_btn = new JButton("Add Game");
	t_info_btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
					JFrame results = new JFrame("Results");
					results.setVisible(true);
					Container results_con = results.getContentPane();
					int res = addGame(Integer.parseInt(t_info_id1.getText()),Integer.parseInt(t_info_id2.getText()),t_info_tname.getText());
					t_info_res.setText(res != 0 ? "Succeeded" : "Failed");
					results_con.add(t_info_res);
					results.pack();
			} catch (Exception ex) {}
		}
	});
	contentPane.add(t_info_btn);
	t_info_res = new JTextArea(2,42);

    }

    public static int addGame(int idw, int idl, String tname) throws Exception {
	Class.forName(dbClassName);
	Properties p = new Properties();
	p.put("user","miles");
	p.put("password","ESPN");
	Connection con = DriverManager.getConnection(CONNECTION,p);

	//find highest game number
	String numquery = "select MAX(game_number) from Game;";
	PreparedStatement numquery_stmt = con.prepareStatement(numquery);
	//pstmt_tinfo.setInt(1,id);
	ResultSet rs = numquery_stmt.executeQuery();
	rs.next();
	int game_num = 1 + rs.getInt(1); // first and only col
	
	String insert_query_str = "insert into Game values (?, ?, ?, ?, ? , ?);";
	PreparedStatement insert_query = con.prepareStatement(insert_query_str);
	insert_query.setInt(1,game_num);
	insert_query.setString(2, tname);
	insert_query.setInt(3,idw); // winning id
	insert_query.setInt(4,idw);
	insert_query.setInt(5,idl);
	insert_query.setInt(6,(int)(Math.random() * 40)); //length

	int res = insert_query.executeUpdate();
	
	// for each player in each team, create new pms entry
	// get all players:

	String pquery1 = "select P.player_name as player_name from Team T, Player P where P.team_id = T.team_id and P.team_id = ?;";
	PreparedStatement pquery1_stmt = con.prepareStatement(pquery1);
	pquery1_stmt.setInt(1,idw);
	ResultSet rsp1 = pquery1_stmt.executeQuery();
	
	ArrayList<String> p1 = new ArrayList<String>(10);
	while(rsp1.next())
		p1.add(rsp1.getString(1));
	
	String pquery2 = "select P.player_name as player_name from Team T, Player P where P.team_id = T.team_id and P.team_id = ?;";
	PreparedStatement pquery2_stmt = con.prepareStatement(pquery2);
	pquery2_stmt.setInt(1,idl);
	ResultSet rsp2 = pquery2_stmt.executeQuery();
	
	ArrayList<String> p2 = new ArrayList<String>(10);
	while(rsp2.next())
		p2.add(rsp2.getString(1));
	
	//add pms's
	for(String pname : p1) {
		PreparedStatement pmsadd = con.prepareStatement("insert into PlayerMatchStat values (?,?,?,?,?,?,?)");
		pmsadd.setString(1, pname);
		pmsadd.setInt(2, game_num);
		pmsadd.setString(3, "Hero" + ((int)(Math.random() * 10)) );
		pmsadd.setInt(4, ((int)(Math.random() * 10)) ); //kills
		pmsadd.setInt(5, ((int)(Math.random() * 10)) ); //d
		pmsadd.setInt(6, ((int)(Math.random() * 10)) ); //a
		pmsadd.setInt(7, ((int)(Math.random() * 10)) ); //creep score
		pmsadd.executeUpdate();
	}
	for(String pname : p2) {
		PreparedStatement pmsadd = con.prepareStatement("insert into PlayerMatchStat values (?,?,?,?,?,?,?)");
		pmsadd.setString(1, pname);
		pmsadd.setInt(2, game_num);
		pmsadd.setString(3, "Hero" + ((int)(Math.random() * 10)) );
		pmsadd.setInt(4, ((int)(Math.random() * 10)) ); //kills
		pmsadd.setInt(5, ((int)(Math.random() * 10)) ); //d
		pmsadd.setInt(6, ((int)(Math.random() * 10)) ); //a
		pmsadd.setInt(7, ((int)(Math.random() * 10)) ); //creep score
		pmsadd.executeUpdate();
	}
	
	con.close();
	return res;
    }
}
