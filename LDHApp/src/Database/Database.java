/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author J_Administrator
 */
public class Database {
    private final String driver;
    private final String url;
    
    public Database() {
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        url = "jdbc:sqlserver://localhost;integratedSecurity=true";        
    }
    
    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }    
    
}
