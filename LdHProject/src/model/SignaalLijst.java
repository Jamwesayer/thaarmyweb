/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Database.Database;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J_Administrator
 */
public class SignaalLijst {
        
        Database myDatabase;

        String user;
        String pass;
        ArrayList<Signaal> signalenLijst;
        String driver;
        String url;
        ConnectionString CS;
        ConnectionStringDataBase CSDB;
        Connection con;
        Statement stmt;
        int count = 0;
        
        
    public ConnectionString getCS() {
        return CS;
    }
        
    public SignaalLijst() throws ClassNotFoundException, ParseException, ParseException, SQLException {
            myDatabase = new Database();
            signalenLijst = new ArrayList<Signaal>();
            
            driver = myDatabase.getDriver();
            url = myDatabase.getUrl();
            
            Connection con = DriverManager.getConnection(url);  
            DatabaseMetaData dmd = con.getMetaData();
            String dmdUrl = dmd.getURL();
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());            
            
            CS = new ConnectionString(dmdUrl.substring(dmdUrl.indexOf("//") + 1, dmdUrl.indexOf(";")),"USER","AuditBlackBox",timestamp);
            CSDB = new ConnectionStringDataBase();
            
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);            
            
            callQueries();
            //bepaalSignaalAdAfas(driver, url);
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
    
    //For bold making duplicate signals
    public boolean checkDuplicate(String auditName, ArrayList<Signaal> signaal) {
        
                String[] checkers = {"profit","ad","onbekend","BaAcAD","MwUitDi","BAAccNotProf"
                ,"NaamClevernew","ClevernewNietAd","accadact","acconbclever","UserNietClever","clevAcMedeDienst"
                ,"userNietAfas"};
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
    
    //Used for initiating query
    private void callQueries() {
        baAccountNotInAD();
        medewerkerUitDienst();
        baAccountNotInProfit();
        geenNaamClevernew();
        cleverNewNietInAd();
        accoundAdActief();
        accountOnbekendClever();
        userNaamNietInClever();
        cleverActiefMedewerkerUitdienst();
        userNaamNietInAfas();
    }
    
    //--- Use Case Queries ---//
    public void baAccountNotInAD() {
        /*Als BA-Account in Afas Profit niet voorkomt in BA-Account van de AD */
        String sql = "use AuditBlackBox SELECT * from [AD-Export] ad "
                     + "WHERE Username_Pre2000 NOT IN (SELECT EmployeeUsername FROM [AfasProfit-Export])";
        String text = "RDS User naam in Profit bestaat niet in de AD";
        String type = "ba account not in AD";
        String typeSignal = "BaAcAD";
        String auditName = "Username_Pre2000";
        initQuery(sql,text,type,typeSignal, auditName);
    }

    public void medewerkerUitDienst() {
        /*Als BA-Account een Datum Einde Contract heeft in Profit 
        maar nog actief is in de AD Genereer SIgnaal "Medewerker uit dienst in Profit, account is in AD actief"*/
        String sql = "use AuditBlackBox SELECT * FROM [AD-Export] ad "
                + "LEFT JOIN [AfasProfit-Export] af ON ad.Username_Pre2000 = af.EmployeeUsername "
                    + "WHERE af.ContractEndDate < GETDATE() and Disabled = 0";
        String text = "Medewerker uit dienst in profit";
        String type = "Medewerker uit dienst";
        String typeSignal = "MwUitDi";
        String auditName = "EmployeeUsername";
        initQuery(sql,text,type,typeSignal, auditName);
    }

    public void baAccountNotInProfit() {
        //Als een BA Account in de AD niet voorkomt in Profit genereer Signaal "AD Account, onbekend in Profit"
        String sql = "use AuditBlackBox SELECT * FROM [AfasProfit-Export] af " +
                     "WHERE EmployeeUsername NOT IN (SELECT Username_Pre2000 FROM [AD-Export])";
        String text = "AD Account, onbekend in Profit";
        String type = "ba account not in profit";
        String typeSignal = "BAAccNotProf";
        String auditName = "EmployeeUsername";
        initQuery(sql,text,type,typeSignal, auditName);
    }

    public void geenNaamClevernew() {
        /*Als in Clever geen BA Account is ingevuld voor een gebruiker 
        geef signaal "RDS naam in Clevernew is niet ingevuld"*/
        String sql = "use AuditBlackBox SELECT * FROM persoonCodes WHERE CodesoortenID = 981";
        String text = "RDS naam in Clevernew is niet ingevuld";
        String type = "geen naam clevernew";
        String typeSignal = "NaamClevernew";
        String auditName = "PersoonID";
        initQuery(sql,text,type,typeSignal,auditName);

    }

    public void cleverNewNietInAd() {
        //Als in Clever een BA Account is ingevuld dat niet als geldig account voorkomt in de AD geef Signaal "RDS naam  in CleverNew bestaat niet in AD"
        String sql = "use AuditBlackBox SELECT * FROM PersoonCodes WHERE codesoortenID = 981 AND IsVerwijderd = 0 AND code NOT IN (SELECT Username_Pre2000 FROM [AD-Export])";
        String text = "RDS naam in CleverNew bestaat niet in AD";
        String type = "clevernew niet in ad";
        String typeSignal = "ClevernewNietAd";
        String auditName = "PersoonID";
        initQuery(sql,text,type,typeSignal,auditName);
    }

    public void accoundAdActief() {
        //Als in Clever een medewerker uit dienst is maar het BA Account komt nog voor in de AD geef Signaal "Medewerker uit dienst in CleverNew, account in AD actief"
        String sql = "use AuditBlackBox SELECT * FROM PersoonCodes pc " +
                    "LEFT JOIN [AD-Export] ad ON pc.Code = ad.Username_Pre2000 " +
                    "WHERE disabled = 0 AND Einddatum < GETDATE()";
        String text = "Medewerker uit dienst in CleverNew, account in AD actief";
        String type = "account ad actief";
        String typeSignal = "accadact";
        String auditName = "Username_Pre2000";
        initQuery(sql,text,type,typeSignal,auditName);
    }

    public void accountOnbekendClever() {
        //Als een BA Account in de AD niet voorkomt in Clever genereel Signaal "AD Account, onbekend in Clever"
        String sql = "use AuditBlackBox SELECT * FROM [AD-Export] WHERE Username_Pre2000 NOT IN " +
                     " (SELECT code FROM PersoonCodes WHERE CodesoortenID = 981)";
        String text = "AD Account, onbekend in Clever";
        String type = "account onbekend clever";
        String typeSignal = "acconbclever";
        String auditName = "Username_Pre2000";
        initQuery(sql, text, type, typeSignal, auditName);
    }

    public void userNaamNietInClever() {
        //Als BA-Account in Afas Profit niet voorkomt in BA-Account van Clever Genereer signaal "RDS User naam in Profit bestaat niet in Clever"
        String sql = "use AuditBlackBox SELECT * FROM [AfasProfit-Export] af " +
                     "LEFT JOIN PersoonCodes p ON p.Code = af.EmployeeUsername " +
                     "WHERE code IS null";
        String text = "RDS User naam in Profit bestaat niet in Clever";
        String type = "usernaam niet in clever";
        String typeSignal = "UserNietClever";
        String auditName = "EmployeeUsername";
        initQuery(sql, text, type, typeSignal, auditName);
    }

    public void cleverActiefMedewerkerUitdienst() {
        //Als BA-Account een Datum Einde Contract heeft in Profit maar nog actief is in Clever Genereer SIgnaal "Medewerker uit dienst in Profit, account is in Clever actief"
        String sql = "use AuditBlackBox SELECT * FROM [AfasProfit-Export] af " +
                     "INNER JOIN PersoonCodes pc ON af.EmployeeUsername = pc.Code " +
                     "WHERE af.ContractEndDate < GETDATE() AND pc.IsVerwijderd = 0;";
        String text = "Medewerker uit dienst in Profit, account is in Clever actief";
        String type = "clever actief medewerker uit dienst";
        String typeSignal = "clevAcMedeDienst";
        String auditName = "EmployeeUsername";
        initQuery(sql, text, type, typeSignal ,auditName);
        
    }

    public void userNaamNietInAfas() {
        //Als BA-Account in Clever niet voorkomt in BA-Account van Afas Profit Genereer signaal "RDS User naam in Clever bestaat niet in Afas Profit"
        String sql = "use AuditBlackBox SELECT * FROM PersoonCodes WHERE CodesoortenID = 981 AND code NOT IN " +
                     "(SELECT EmployeeUsername FROM [AfasProfit-Export])";
        String text = "RDS User naam in Clever bestaat niet in Afas Profit";
        String type = "usernaam niet in afas";
        String typeSignal = "userNietAfas";
        String auditName = "PersoonID";
        initQuery(sql, text, type, typeSignal, auditName);
    }

    public void initQuery(String sql, String text, String type, String typeSignal, String audit) {
            try {
                ResultSet rs = stmt.executeQuery(sql);
                try {
                    while(rs.next()) {
                        //Attributes
                        String auditName = rs.getString(audit);
                        //Date
                        Date date = new Date();
                        String lastCrawlDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        Date utilDate = null;
                        try {
                            utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(lastCrawlDate);
                        } catch (ParseException ex) {
                            Logger.getLogger(SignaalLijst.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        Signaal signal;
                        signal = new Signaal(auditName, CS.getConnectionID(), typeSignal, auditName + " " + text, type,sqlDate);
                        signalenLijst.add(signal);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SignaalLijst.class.getName()).log(Level.SEVERE, null, ex);        
                }
            } catch (SQLException ex) {
                Logger.getLogger(SignaalLijst.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    signal = new Signaal(auditName, CS.getConnectionID(), "profit", auditName + " Medewerker uit dienst in Profit , account is in AD actief", "PROFIT-MISSING",sqlDate);
                    signalenLijst.add(signal);
                }
                if(employerName == null) {
                    signal = new Signaal(auditName, CS.getConnectionID(), "ad", "RDS " + auditName + " in Profit bestaat niet in de AD ", "AD-MISSING",sqlDate);
                    signalenLijst.add(signal);
                }
                if(auditName == null) {
                    signal = new Signaal(employerName, CS.getConnectionID(), "onbekend", employerName + " AD Account, onbekend in Profit", "ONBEKEND-PROFIT",sqlDate);
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
    
    //Get signaal lijst
    public ArrayList<Signaal> getSignalen(){
        return signalenLijst;
    }

}
