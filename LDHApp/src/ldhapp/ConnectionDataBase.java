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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String url = "jdbc:sqlserver://localhost;databaseName=Test_Signaal_Database;integratedSecurity=true";
        
        //user gegevens
        private String gebruikerUsername;
        private boolean isBeheerder = false;
        private String werkeenheid = ""; 
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
            gebruikerUsername = username;
        }
        
        public String systeemGegevens()
        {
            DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
            Date dateobj = new Date();
            return "Datum: "+df.format(dateobj)+""
                    + "\n\n Database connectiestring: "+getURLDB()
                    +"\n\n Ingelogd als: "+ gebruikerUsername 
                    +"\n\n "
                    +"Werkeenheid: " + werkeenheid
                    +" \n\n Als technisch beheerder \n ingelogd: " + (this.isBeheerder ? "Ja" : "Nee");
        }
        
        public boolean inloggen(String gebruikersnaam)
        {
            connectDB();
            
            try{
                String sql = "use AuditBlackBox " 
                            + "select  pc.persoonid, code,ParentContainer from dbo.PersoonCodes pc " 
                            + "left join  [AD-Export] a on a.Username_Pre2000 = pc.Code "
                            + "where CodesoortenID = 981 AND pc.IsVerwijderd = 0 " 
                            + "AND (getDate() <= pc.einddatum OR pc.einddatum is NULL) "
                            + "AND Startdatum <= GETDATE() "
                            + "AND code = ? "
                            + "ORDER BY pc.AangemaaktDoor DESC";
                
                System.out.println(sql);
                 prepStat = conn.prepareStatement(sql, 
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                prepStat.setString(1, gebruikersnaam);
                
                rs = prepStat.executeQuery();
                rs.last();
                System.out.println(rs.getRow());
                
                    if(rs.getRow() != 0)
                    {
                        //gebruiker die vergeten waren om zichzelf uit te loggen, wordt verwijderd uit userActive
                        emptyActiveUser();
                     
                        //kijk of de gebruiker al actief is.
                        if(checkActiveUser(rs.getString("Code")) == 0)
                        {
                            InsertUserActive(rs.getString("Code"));
                            
                            this.gebruikerUsername = rs.getString("Code");
                            this.werkeenheid = rs.getString("ParentContainer");
                            System.out.println(rs.getString("parentContainer"));
                            if(rs.getString("parentContainer").contains("002 Stichtingsbureau Almere") 
                                    || rs.getString("parentContainer").contains("018 Dienstverkening"))
                            {
                                sql = "use AuditBlackBox " 
                                   + "select id from teamlid tl "
                                   + "where tl.BeginDatum < GETDATE() "
                                   + "AND (tl.EindDatum >= GETDATE() OR tl.Einddatum is NULL) "
                                   + "AND tl.IsVerwijderd = 0 AND  PersoonID = ? ";


                               prepStat = conn.prepareStatement(sql, 
                               ResultSet.TYPE_SCROLL_INSENSITIVE,
                               ResultSet.CONCUR_READ_ONLY);
                               prepStat.setString(1, rs.getString("persoonID"));

                               rs = prepStat.executeQuery();
                               if(rs.getRow() == 0)
                               {
                                   this.isBeheerder = true;   
                               }
                            }

                            return true;
                        }
                        else
                        {
                            infoBox("Gebruiker: " + rs.getString("Code") + " is actief, controleer of de gebuiker wel uitgelogd is.");
                        }
                        
                    }
                    else
                    {   
                        infoBox("U hebt geen toegang tot de signalen, neem contact op met uw gemandeteerde.");
                        return false;
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
        
        private int checkActiveUser(String userid)
        {
            String sql = "use Test_Signaal_Database " 
                        + "select * from userActive where datum = CAST(GETDATE() as date) AND UserId = ? ";

            try{ 
                prepStat = conn.prepareStatement(sql, 
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
                prepStat.setString(1, userid);
                ResultSet res = prepStat.executeQuery();
                res.last(); //pak laatste rijen om een aantal rij op te halen
                System.out.println(res.getRow());
                System.out.println(res.getRow());
                return res.getRow();
            }
            catch(SQLException e)
            {
                infoBox("Database userActive inlezen is fout gelopen.");
            }
            return 1;
                            
        }

    private void InsertUserActive(String userid) {
        String sql = "use Test_Signaal_Database " 
                        + "insert into userActive VALUES (?,getDate()) ";
                            
            try{ 
             prepStat = conn.prepareStatement(sql, 
             ResultSet.TYPE_SCROLL_INSENSITIVE,
             ResultSet.CONCUR_READ_ONLY);
             prepStat.setString(1, userid);

             prepStat.execute();
            }
            catch(SQLException e)
            {
                infoBox("Actieve gebruiker in de database is fout gelopen.");
            }
    }
    
    public void emptyActiveUser()
    {
        String sql = "use Test_Signaal_Database " 
                        + "delete from userActive where datum < CAST(GETDATE() as date); ";
                            
            try{ prepStat = conn.prepareStatement(sql);
             prepStat.execute();
            }
            catch(SQLException e)
            {
                infoBox("Actieve gebruiker van gisteren of ouder verwijderen is fout gelopen.");
            }
    }
    
    public boolean getBeheerder()
    {
        return isBeheerder;
    }
}
