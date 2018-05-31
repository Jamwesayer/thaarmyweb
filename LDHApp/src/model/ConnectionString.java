/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author J_Administrator
 */
public class ConnectionString {
    //Class Variable
    private int connectionID;
    private String serverNaam;
    private String userConnectie;
    private String databaseName;
    private Timestamp timestamp;
    
    public ConnectionString(int connectionID, String serverNaam, String userConnectie, String databaseName, Timestamp timestamp) {
        this.connectionID = connectionID;
        this.serverNaam = serverNaam;
        this.userConnectie = userConnectie;
        this.databaseName = databaseName;
        this.timestamp = timestamp;
    }
    
    public ConnectionString(String serverNaam, String userConnectie, String databaseName, Timestamp timestamp) {
        this.serverNaam = serverNaam;
        this.userConnectie = userConnectie;
        this.databaseName = databaseName;
        this.timestamp = timestamp;
    }    

    public String getConnectionString() {
        String hi = databaseName + "/" + serverNaam + ", " + userConnectie + ", " + timestamp;
        return hi.replaceAll("\\s+","");
    }
    
    public int getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public String getServerNaam() {
        return serverNaam;
    }

    public void setServerNaam(String serverNaam) {
        this.serverNaam = serverNaam;
    }

    public String getUserConnectie() {
        return userConnectie;
    }

    public void setUserConnectie(String userConnectie) {
        this.userConnectie = userConnectie;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
