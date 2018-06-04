/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import ldhapp.ConnectionDataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import model.ConnectionString;
import model.Signaal;

/**
 *
 * @author J_Administrator
 */
public class ConnectionSignaalDataBase {
        
        //Class Variable
        Connection conn;
        PreparedStatement prepStat;
        ConnectionDataBase myDatabase;
        String user;
        String pass;
        String driver;
        String url;
        ArrayList<Signaal> signalenLijst;
        
        //Constructor
        public ConnectionSignaalDataBase() throws ClassNotFoundException {
            
            myDatabase = new ConnectionDataBase();
            driver = myDatabase.getDriver();
            url = myDatabase.getURLDB();
            
            Class.forName(driver);
            
            try {
                conn = DriverManager.getConnection(url);
            }   
            catch(SQLException e) {
                infoBox(e.toString());
            }
            catch(Exception e) {
                infoBox(e.toString());
            } 
        }
        
        //Methode voor het invoegen van signaal naar signaaldatabase
        public void insertSignal(String userId, String signaalType, String algemeen, int connectieString, String variable){
            try{
                //SQL Statement
                String sql =  "use Test_Signaal_Database "
                            + "INSERT INTO SignalenTabel "
                            + "VALUES(?,?,?,?,?,?,NULL,NULL,NULL); ";
                
                //Init variables
                Date date = new Date();
                String lastCrawlDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastCrawlDate); 
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());                
                
                //Statements
                prepStat = conn.prepareStatement(sql);
                prepStat.setString(1, userId);
                prepStat.setString(2, signaalType);
                prepStat.setString(3, algemeen);
                prepStat.setString(4, variable);
                prepStat.setInt(5, connectieString);
                prepStat.setDate(6, sqlDate);
                prepStat.executeUpdate();
            }
            
            catch(SQLException | ParseException e) {
                infoBox(e.toString());
            }
        }
        
        //Methode voor het catchen van exceptions
        public static void infoBox(String infoMessage) {
            JOptionPane.showMessageDialog(null, infoMessage, "Foutmelding", JOptionPane.INFORMATION_MESSAGE);
        }
        
        //Alle signalen van de signalen database tonen
        public ArrayList<Signaal> showSignalen(){
            try {
                String sql = "use Test_Signaal_Database "
                           + " SELECT * FROM SignalenTabel";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                ArrayList<Signaal> signalenLijst = new ArrayList<>();

                while(rs.next()) { 
                    String userId = rs.getString("userId");
                    String signaalType = rs.getString("signaalType");
                    String algemeneTekst = rs.getString("Algemene_Tekst");
                    String variableTekst = rs.getString("Variable_Tekst");
                    int connectionString = rs.getInt("Connectie_String");
                    Date optreding = rs.getDate("Eerst_Optreding");
                    Date opgelost = rs.getDate("Opgelost");
                    Signaal signal = new Signaal(userId, connectionString, signaalType, algemeneTekst,variableTekst,optreding,opgelost);

                    sql = "use Test_Signaal_Database SELECT * FROM ConnectionString WHERE ConnectionID = " + connectionString;
                    Statement stmts = conn.createStatement();
                    ResultSet rss = stmts.executeQuery(sql);
                    while(rss.next()) {
                        String servernaam = rss.getString("ServerNaam");
                        String userConnectie = rss.getString("UserConnectie");
                        String databaseNaam = rss.getString("DatabaseNaam");
                        Timestamp timestamp = rss.getTimestamp("Timestamp");

                        signal.setConnection(new ConnectionString(servernaam, userConnectie, databaseNaam, timestamp));
                    }

                    signalenLijst.add(signal);
                }
                return signalenLijst;
            }
            catch(SQLException e) {
                System.out.println(e);
                infoBox(e.toString());
            }
        return signalenLijst;
    }
}
