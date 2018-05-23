/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import static connection.ConnectionSignaalDataBase.infoBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.ConnectionString;

/**
 *
 * @author J_Administrator
 */
public class ConnectionStringDataBase {
        Connection conn;
        PreparedStatement prepStat;    
        String driver;
        String url;
        String user;
        String pass;
        
        public ConnectionStringDataBase() throws ClassNotFoundException{
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            url = "jdbc:sqlserver://localhost;integratedSecurity=true";
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
        
        public void insertConnectionString(ConnectionString connectionString){
            try{
                String sql = "use Test_Signaal_Database "
                    + "INSERT INTO ConnectionString "
                    + "VALUES(?,?,?,?);";
                
                prepStat = conn.prepareStatement(sql);     
                
                ConnectionString CS = connectionString;
                prepStat.setString(1, CS.getDatabaseName());
                prepStat.setString(2, CS.getServerNaam());
                prepStat.setString(3, CS.getUserConnectie());
                prepStat.setTimestamp(4, CS.getTimestamp());
                prepStat.executeUpdate();
            }
            catch(SQLException e) {
                infoBox(e.toString());
            }
        }        
}
