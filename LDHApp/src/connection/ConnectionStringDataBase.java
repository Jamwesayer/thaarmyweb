/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import ldhapp.ConnectionDataBase;
import static connection.ConnectionSignaalDataBase.infoBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.ConnectionString;

/**
 *
 * @author J_Administrator
 */
public class ConnectionStringDataBase {
    Connection conn;
    PreparedStatement prepStat;
    ConnectionDataBase myDatabase;

    String user;
    String pass;

    //Constructor
    public ConnectionStringDataBase() throws ClassNotFoundException{
        myDatabase = new ConnectionDataBase();

        String driver = myDatabase.getDriver();
        String url = myDatabase.getURLDB();
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

    //Connectiestring toevoegen in connectie string tabel
    public int insertConnectionString(ConnectionString connectionString){

        int identity = 0;

        try{
            String sql = "use Test_Signaal_Database "
                + "INSERT INTO ConnectionString "
                + "VALUES(?,?,?,?); "
                + "SELECT SCOPE_IDENTITY() AS you;";

            prepStat = conn.prepareStatement(sql);

            ConnectionString CS = connectionString;
            prepStat.setString(1, CS.getServerNaam());
            prepStat.setString(2, CS.getUserConnectie());
            prepStat.setString(3, CS.getDatabaseName());
            prepStat.setTimestamp(4, CS.getTimestamp());
            //prepStat.executeUpdate();
            ResultSet rs = prepStat.executeQuery();
            while(rs.next()) {
                identity = rs.getInt("you");
            }
        }
        catch(SQLException e) {
            infoBox(e.toString());
        }
        return identity;            
    }        
}