/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import model.Signaal;

/**
 *
 * @author J_Administrator
 */
public class ConnectionSignaalDataBase {
        
        Connection conn;
        //Scanner sc;
        PreparedStatement prepStat;    
        String driver;
        String url;
        String user;
        String pass;
        ArrayList<Signaal> signalenLijst;
        
        public ConnectionSignaalDataBase() throws ClassNotFoundException, SQLException {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            //---- For Windows Authentication ----
            //String url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;integratedSecurity=true;";

            //---- For SQL server Authentication ----
            url = "jdbc:sqlserver://localhost;databaseName=Test_Signaal_Database;";
            user = "root";
            pass = "wingedhawk0";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);            
        }
        
        public void insertSignal(String sql_query, String first, String second) throws SQLException, ParseException{
            String sql = sql_query;
            prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, first);
            prepStat.setString(2, second);
            String lastCrawlDate = "2014-01-28";
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastCrawlDate);            
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); 
            prepStat.setDate(3, sqlDate);
            prepStat.executeUpdate();
        }
        
        public ArrayList<Signaal> showSignalen(String sql_query) throws SQLException{
            String sql = sql_query;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<Signaal> signalenLijst = new ArrayList<>();
            
            while(rs.next()) { 
                String algemeneTekst = rs.getString("Algemene_Tekst");
                String variableTekst = rs.getString("Variable_Tekst");
                String opgelost = rs.getString("Opgelost");
                String optreding = rs.getString("Eerst_Optreding");
                signalenLijst.add(new Signaal(algemeneTekst,variableTekst));
            }
            
            return signalenLijst;
        }            
}
