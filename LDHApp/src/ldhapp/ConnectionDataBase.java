/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

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
import javax.swing.JOptionPane;

/**
 *
 * @author J_Administrator
 */
public class ConnectionDataBase {
        Connection conn;
        PreparedStatement prepStat; 
        ResultSet rs;
        String driver;
        String url;
        
        //user gegevens
        String GebruikerUsername;
        
        /* voor toekomst
        String user;
        String pass;
        */
        public String getURLDB()
        {
            return url;
        }
        
        public boolean SelectSignalen()
        {
            connectDB();
            try{
                String sql = "use Test_Signaal_Database "
                        + "select * from SignalenTabel";
                prepStat = conn.prepareStatement(sql, 
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
               
               rs = prepStat.executeQuery();
               
               if(rs.last())
               {
                   if(rs.getRow() != 0)
                   {
                        infoBox("Aantal rijen: "+rs.getRow()); 
                        return true;
                   }
                   else
                   {   
                        return false;
                   }
               }
            }
            catch(SQLException e)
            {
                infoBox(e.toString());
            }
            finally
            {
                closeDB();
            }
            return false;
        }
        
        public void insertSignal(String sql_query, String first, String second){
            connectDB();
            
            try{
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
            catch(SQLException e)
            {
                infoBox(e.toString());
            }
            catch(ParseException e)
            {
                infoBox(e.toString());
            }
            finally
            {
                closeDB();
            }
        }
        
        private void connectDB()
        {
            //---- For Windows Authentication ----
            //String url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;integratedSecurity=true;";

            //---- For SQL server Authentication ----
           /* url = "jdbc:sqlserver://localhost;databaseName=Test_Signaal_Database;";
            user = "root";
            pass = "wingedhawk0";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);            
            */
            
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            url = "jdbc:sqlserver://localhost;integratedSecurity=true";
            try{
                Class.forName(driver);
            }
            catch(ClassNotFoundException e)
            {
                infoBox(e.toString());
            }
            
            try{
                conn = DriverManager.getConnection(url);
            }   
            catch(SQLException e)
            {
                infoBox(e.toString());
            }
            catch(Exception e)
            {
                infoBox(e.toString());
            }
        }
        
        private void connectDBToAggDB() throws ClassNotFoundException
        {
            //---- For Windows Authentication ----
            //String url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;integratedSecurity=true;";

            //---- For SQL server Authentication ----
           /* url = "jdbc:sqlserver://localhost;databaseName=Test_Signaal_Database;";
            user = "root";
            pass = "wingedhawk0";
            
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);            
            */
            url = "jdbc:sqlserver://localhost;databaseName=Test_Signaal_Database;integratedSecurity=true";
            
            Class.forName(driver);
            try{
                conn = DriverManager.getConnection(url);
            }   
            catch(SQLException e)
            {
                infoBox(e.toString());
            }
            catch(Exception e)
            {
                infoBox(e.toString());
            }
        }
        
        private void closeDB()
        {
            try{
                prepStat.close();
                conn.close();
            }
            catch(SQLException e)
            {
                infoBox("Er is fout gegaan met het breken van de connectie van de database. \n" + e);
            }
        }
        
        public static void infoBox(String infoMessage)
        {
            JOptionPane.showMessageDialog(null, infoMessage, "Foutmelding", JOptionPane.INFORMATION_MESSAGE);
        }
        
        public void setGebruikerUsername(String username)
        {
            GebruikerUsername = username;
        }
}
