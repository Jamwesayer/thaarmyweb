/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
        
        
        public SignaalCalc() throws ClassNotFoundException {
            driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

            //---- For Windows Authentication ----
            //String url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;integratedSecurity=true;";

            //---- For SQL server Authentication ----
          
            /*
            url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;";
            user = "root";
            pass = "wingedhawk0";             
            
            //getMedewerkers(driver, url, user, pass);
            bepaalSignaalAdAfas(driver, url, user, pass);
            */
            
            url = "jdbc:sqlserver://localhost;databaseName=AuditBlackBox;integratedSecurity=true";
            
            //getMedewerkers(driver, url, user);
            bepaalSignaalAdAfas(driver, url);
            
            //bepaalSignaalClever(driver, url);            
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
                
                //System.out.println(persoonId + " " + codeId + " " 
                //        + medewerkerCodes + " " + organisatieID + " " + naam);
                count++;
            }
            //System.out.println(count);
        }
        catch(SQLException e) {
             e.printStackTrace();
        }
        finally {
            
        }    
    }
    
    public void bepaalSignaalAdAfas(String driver, String url) throws ClassNotFoundException {
        
        signalenLijst = new ArrayList<Signaal>();
        
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url);
            
            String sql = "SELECT ad.Username_Pre2000, afas.EmployeeUsername, afas.ContractEndDate, ad.Disabled FROM [AfasProfit-Export] afas "
                    + "FULL OUTER JOIN [AD-Export] ad ON ad.Username_Pre2000 = afas.EmployeeUsername ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                String employerName = rs.getString("Username_Pre2000");
                String auditName = rs.getString("EmployeeUsername");
                String eindDatum = rs.getString("ContractEndDate");
                int disabled = rs.getInt("Disabled");
                
                //System.out.println(employerName + " " + auditName + " " + eindDatum + " " + disabled);
                if(eindDatum != null && disabled == 0) {
                    signalenLijst.add(new Signaal(auditName + " Medewerker uit dienst in Profit , account is in AD actief","PROFIT-MISSING"));
                }
                if(employerName == null) {
                    signalenLijst.add(new Signaal("RDS " + auditName + " in Profit bestaat niet in de AD ","AD-MISSING"));
                }
                if(auditName == null) {
                    signalenLijst.add(new Signaal(employerName + " AD Account, onbekend in Profit", "ONBEKEND-PROFIT"));
                }
            }
        }
        catch(SQLException e) {
             e.printStackTrace();
        }
        finally {
            
        }
        
        for(Signaal object : signalenLijst) {
            object.showSignaal();
        }
        
        //System.out.println(signalenLijst.size() + " signalen gevonden");
        
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
                //System.out.println(employerName + " " + adId + " " + Persoon);   
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
