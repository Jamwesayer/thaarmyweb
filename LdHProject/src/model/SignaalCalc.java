/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import connection.ConnectionStringDataBase;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author J_Administrator
 */
public class SignaalCalc {
        
        String driver;
        String url;
        String user;
        String pass;
        ArrayList<Signaal> signalenLijst;
        ConnectionString CS;
        ConnectionStringDataBase CSDB;
        
        
    public SignaalCalc() throws ClassNotFoundException, ParseException, ParseException, SQLException {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            url = "jdbc:sqlserver://localhost;integratedSecurity=true";
            
            Connection con = DriverManager.getConnection(url);            
            DatabaseMetaData dmd = con.getMetaData();
            String dmdUrl = dmd.getURL();
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());            
            
            CS = new ConnectionString("localhost","USER","AuditBlackBox",timestamp);
            CSDB = new ConnectionStringDataBase();
            bepaalSignaalAdAfas(driver, url);
        }     
    
    public void getMedewerkers(String driver, String url) throws ClassNotFoundException {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url);
            
            int count = 0;
            
            /*String sql = "SELECT * FROM PersoonCodes pc "
                    + "JOIN Medewerker m ON pc.PersoonID = m.PersoonID "
                    + "JOIN Werkzaam w ON w.MedewerkerID = m.PersoonID "
                    + "JOIN OrganisatieEenheid o ON o.OrganisatieID = w.OrganisatieEenheidID "
                    + "WHERE pc.CodeSoortenID = 981;";*/
            
            String sql = "SELECT * FROM ";
            
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String persoonId = rs.getString("PersoonID");
                String codeId = rs.getString("CodesoortenID");
                String medewerkerCodes = rs.getString("MedewerkerNummer");
                String naam = rs.getString("Naam");
                String organisatieID = rs.getString("organisatieEenheidID");
                count++;
            }
        }
        catch(SQLException e) {
             e.printStackTrace();
        }
        finally {
            
        }    
    }
    
    public boolean checkDuplicate(String auditName, ArrayList<Signaal> signaal) {
        
                String[] checkers = {"profit","ad","onbekend"};
                boolean duplicate = false;
                
                for(Signaal i : signaal){
                    if(i.getUserID().replaceAll("\\s+","").equals(auditName.replaceAll("\\s+",""))){
                        for(String check : checkers){
                            if(i.getSignaalType().replaceAll("\\s+","").equals(check)) {
                                duplicate = true; 
                                break;
                            }
                        }
                        break;
                    }
                }  
                return duplicate;
    }
    
    public void bepaalSignaalAdAfas(String driver, String url) throws ClassNotFoundException, ParseException {
        
        signalenLijst = new ArrayList<Signaal>();
        
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url);
            
            String sql = "use AuditBlackBox SELECT ad.Username_Pre2000, afas.EmployeeUsername, afas.ContractEndDate, ad.Disabled FROM [AfasProfit-Export] afas "
                    + "FULL OUTER JOIN [AD-Export] ad ON ad.Username_Pre2000 = afas.EmployeeUsername ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                //Attributes
                String employerName = rs.getString("Username_Pre2000");
                String auditName = rs.getString("EmployeeUsername");
                String eindDatum = rs.getString("ContractEndDate");
                int disabled = rs.getInt("Disabled");
                //Date
                Date date = new Date();
                String lastCrawlDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastCrawlDate); 
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                Signaal signal;
                //Add
                if(eindDatum != null && disabled == 0) {
                    signal = new Signaal(auditName, 9, "profit", auditName + " Medewerker uit dienst in Profit , account is in AD actief", "PROFIT-MISSING",sqlDate);
                    signalenLijst.add(signal);
                }
                if(employerName == null) {
                    signal = new Signaal(auditName, 9, "ad", "RDS " + auditName + " in Profit bestaat niet in de AD ", "AD-MISSING",sqlDate);
                    signalenLijst.add(signal);
                }
                if(auditName == null) {
                    signal = new Signaal(employerName, 9, "onbekend", employerName + " AD Account, onbekend in Profit", "ONBEKEND-PROFIT",sqlDate);
                    signalenLijst.add(signal);
                }
                
            }
            CSDB.insertConnectionString(CS);            
        }
        catch(SQLException e) {
             e.printStackTrace();
        }
        finally {
            
        }
        
    }
    
    public void bepaalSignaalClever(String driver, String url) throws ClassNotFoundException {
        ArrayList<String> signalenLijst = new ArrayList<String>();
        
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url);
            
            /*String sql = "SELECT ad.Username_Pre2000, afas.EmployeeUsername, afas.ContractEndDate, ad.Disabled FROM [AfasProfit-Export] afas "
                    + "FULL OUTER JOIN [AD-Export] ad ON ad.Username_Pre2000 = afas.EmployeeUsername ";*/
            String sql = "SELECT * FROM PersoonCodes pc"
                    + " FULL OUTER JOIN Persoon p ON p.ID = pc.PersoonID"
                    + " FULL OUTER JOIN Medewerker w ON w.PersoonID = p.ID"
                    + " FULL OUTER JOIN [AfasProfit-Export] afas ON afas.EmployerCode = w.MedewerkerNummer";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String employerName = rs.getString("Voornaam");
                String Persoon = rs.getString("ID");
                String adId = rs.getString("ADUserID");   
            }
        }
        catch(SQLException e) {
             e.printStackTrace();
        }
        finally {
   
        }
    }    
    
    public ArrayList<Signaal> getSignalen(){
        return signalenLijst;
    }
    
}
