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

	private static JButton g_info_btn;
	private static JTextField g_info_num;
	private static JTextArea g_info_res;

	private static JButton tournament_info_btn;
	private static JTextField tournament_info_name;
	private static JTextArea tournament_info_res;

	private static String[] tournament_game_info;
	private static JComboBox dropdown;


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
	contentPane.setLayout(new GridLayout(0,3)); // 4 columns
	//////Team Info row
	contentPane.add(new JLabel("Team Info. Please enter team id."));
	t_info_id = new JTextField();  // enter the id
	contentPane.add(t_info_id);

	t_info_btn = new JButton("Search Database");
	t_info_btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
					JFrame results = new JFrame("Search Results");
					results.setVisible(true);
					Container results_con = results.getContentPane();
				t_info_res.setText(teamStats(Integer.parseInt(t_info_id.getText())));
					results_con.add(t_info_res);
					results.pack();
			} catch (Exception ex) {}
		}
	});
	contentPane.add(t_info_btn);
	t_info_res = new JTextArea(2,42);
//        contentPane.add(t_info_res);

	//////Player Info row
	contentPane.add(new JLabel("Player Info. Please enter Player Name."));
	p_info_name = new JTextField();  // enter the id
	contentPane.add(p_info_name);

	p_info_btn = new JButton("Search Database");
	p_info_btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
					JFrame results = new JFrame("Search Results");
					results.setVisible(true);
					Container result_con = results.getContentPane();
				p_info_res.setText(playerInfo(p_info_name.getText()));
					result_con.add(p_info_res);
					results.pack();
			} catch (Exception ex) {}
		}
	});
	contentPane.add(p_info_btn);
	p_info_res = new JTextArea(2,42);
//        contentPane.add(p_info_res);

		///////Game Info row
		contentPane.add(new JLabel("Game Info. Please enter game ID."));
		g_info_num = new JTextField(); //Enter game id
		contentPane.add(g_info_num);

		g_info_btn = new JButton("Search Database");
		g_info_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFrame results = new JFrame("Search Results");
					results.setVisible(true);
					Container result_con = results.getContentPane();
					g_info_res.setText(matchInfo(Integer.parseInt(g_info_num.getText())));
					result_con.add(g_info_res);
					results.pack();
				} catch (Exception ex) {}
			}
		});
		contentPane.add(g_info_btn);
		g_info_res = new JTextArea(2,42);
//		contentPane.add(g_info_res);

		///////Tournament Info
	contentPane.add(new JLabel("Tournament Info. Please enter Tournament Name."));
	tournament_info_name = new JTextField();  // enter the id
	contentPane.add(tournament_info_name);

	tournament_info_btn = new JButton("Search Database");
	tournament_info_btn.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {

					JFrame results = new JFrame("Search Results");
					results.setVisible(true);
					Container result_con = results.getContentPane();
					tournament_info_res = new JTextArea();
				tournament_info_res.setText(tournamentInfo(tournament_info_name.getText()));
					result_con.add(tournament_info_res);
					tournament_game_info = tournamentGameInfo(tournament_info_name.getText());
					for (int i = 0; i < tournament_game_info.length; i++) {
						dropdown.addItem(tournament_game_info[i]);
					}
					dropdown.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JFrame tgame = new JFrame("Search Results");
							tgame.setVisible(true);
							Container tgame_results = tgame.getContentPane();
							try {
								g_info_res.setText(matchInfo(Integer.parseInt(dropdown.getSelectedItem().toString())));
							} catch (Exception ex) {}
							tgame_results.add(g_info_res);
							tgame.pack();
						}
					});
					result_con.add(dropdown);
					results.pack();
			} catch (Exception ex) {}
		}
	});
	contentPane.add(tournament_info_btn);
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
	     ret += "name: " + rs.getString("player_name") + "\n email: " +
		rs.getString("email") + "\n role: " + rs.getString("player_role") +
			"\n team_id: " + rs.getInt("team_id") + "\n kdr: " +
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

	public static String[] tournamentGameInfo(String tournamentName) throws Exception {
		Class.forName(dbClassName);
		Properties p = new Properties();
		p.put("user","miles");
		p.put("password","ESPN");
		Connection con = DriverManager.getConnection(CONNECTION,p);


		String query = "select game_number from Game M, Tournament T where G.tournament_name = T.tournament_name and G.tournament_name = ?;";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setString(1,tournamentName);

		ResultSet rs = pstmt.executeQuery();
		String[] tournament_games = new String[rs.getFetchSize()];
		int i = 0;
		while (rs.next()){
			tournament_games[i] = rs.getString("G.game_number");
			i = i + 1;
		}
		System.out.println(tournament_games.toString());
		return tournament_games;


	}

	public static String matchInfo(int matchID) throws Exception{
		Class.forName(dbClassName);
		Properties p = new Properties();
		p.put("user","miles");
		p.put("password","ESPN");
		Connection con = DriverManager.getConnection(CONNECTION,p);

		String query = "select * from Game G where G.game_number = ?;";
		PreparedStatement pstmt = con.prepareStatement(query);
		pstmt.setInt(1,matchID);

		ResultSet rs = pstmt.executeQuery();

		String ret = "";

		rs.next();
		//just some things to make later string-making and querying simpler
		int winner = rs.getInt("winning_team_id");
		int team1 =  rs.getInt("team1_id");
		int team2 =  rs.getInt("team2_id");

		ret += "\nMatch Number: " + rs.getString("game_number") + "\n Match Length: " + rs.getInt("length") + "\n In Tournament: " +
		rs.getString("tournament_name");

		//look for the winner
		String query2 = "select * from Team T where T.team_id = ?;";
		PreparedStatement pstmt2 = con.prepareStatement(query2);
		pstmt2.setInt(1,winner);

		ResultSet rs2 = pstmt2.executeQuery();
		rs2.next();

		ret += "\n Winning Team: " + rs2.getString("name") + " ( Team ID: " + winner + " )";

		//look for team 1
		String query3 = "select name from Team T where T.team_id = ?;";
		PreparedStatement pstmt3 = con.prepareStatement(query3);
		pstmt3.setInt(1,team1);

		ResultSet rs3 = pstmt3.executeQuery();
		rs3.next();

		ret += "\n Team 1: " + rs3.getString("name") + " ( Team ID: " + team1 + " )";

		//look for team 2
		String query4 = "select name from Team T where T.team_id = ?;";
		PreparedStatement pstmt4 = con.prepareStatement(query4);
		pstmt4.setInt(1,team2);

		ResultSet rs4 = pstmt4.executeQuery();
		rs4.next();

		ret += "\n Team 2: " + rs4.getString("name") + " ( Team ID: " + team2 + " )";;

		con.close();
		return ret;
		}
}
