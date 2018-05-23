/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J_Administrator
 */
public class Signaal {
    
    private int signaalID;
    private String userID;
    private String signaalType;
    private String algemene_tekst;
    private String variable_tekst;
    private int connectieString;
    private Date eersteOptreden;
    private Date opgelost;
    private String impactEntiteit;
    private String impactOrganisatie;
    private ConnectionString connection;
    
    public Signaal(String userID, String signaalType, String algemene_tekst, String variable_tekst, int connectieString, Date eersteOptreden, Date opgelost) {
        this.userID = userID;
        this.signaalType = signaalType;
        this.algemene_tekst = algemene_tekst;
        this.variable_tekst = variable_tekst;        
        this.connectieString = connectieString;
        this.eersteOptreden = eersteOptreden;
        this.opgelost = opgelost;
    }
    
    public Signaal(String userID, String signaalType, String algemene_tekst, String variable_tekst, Date eersteOptreden) {
        this.userID = userID;
        this.signaalType = signaalType;        
        this.algemene_tekst = algemene_tekst;
        this.variable_tekst = variable_tekst;
        this.eersteOptreden = eersteOptreden;
    }
    
    public Signaal(String userID, int connectieString, String signaalType, String algemene_tekst, String variable_tekst, Date eersteOptreden) {
        this.userID = userID;
        this.signaalType = signaalType;        
        this.algemene_tekst = algemene_tekst;
        this.variable_tekst = variable_tekst;
        this.eersteOptreden = eersteOptreden;
        this.connectieString = connectieString;
    }    
    
    public void setConnection(ConnectionString connection) {
        this.connection = connection;
    }
    
    public ConnectionString getConnection() {
        return connection;
    }
    
    public String getConnectionText() {
        return connection.getConnectionString();
    }
    
    public void showSignaal() {
        Field[] fields = this.getClass().getDeclaredFields();
        Object variable = null;
        String hadouken = "";
        for(Field field : fields) {
            try {
                Field f = this.getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                try {
                    variable = f.get(this) != null ? f.get(this) : "NULL";
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(Signaal.class.getName()).log(Level.SEVERE, null, ex);
                }
                if("opgelost".equals(field.getName()) && variable != "NULL" || "eersteOptreden".equals(field.getName()) && variable != "NULL") {
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");  
                    String stringVariable = df.format(variable); 
                    hadouken = stringVariable.replaceAll("\\s+","");
                }
                else {
                    String stringVariable = variable.toString();
                    hadouken = stringVariable.replaceAll("\\s+","");
                }
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(Signaal.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*try {
                System.out.print(hadouken + " ");
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Signaal.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        //System.out.println("");
    }

    public Object[] generateSignal() {
        return new Object[] { algemene_tekst, variable_tekst, connectieString 
                ,eersteOptreden ,opgelost ,impactEntiteit ,impactOrganisatie };
    }
    
    public boolean addToSignalTable(DefaultTableModel tableModel) {
        tableModel.addRow(new Object[] {algemene_tekst,variable_tekst,connection.getConnectionString(),eersteOptreden,opgelost
                ,impactEntiteit,impactOrganisatie});
        return false;
    }
    
    //Getter and Setters
    public int getSignaalID() {
        return signaalID;
    }

    public void setSignaalID(int signaalID) {
        this.signaalID = signaalID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSignaalType() {
        return signaalType;
    }

    public void setSignaalType(String signaalType) {
        this.signaalType = signaalType;
    }    
    
    public String getAlgemene_tekst() {
        return algemene_tekst;
    }

    public void setAlgemene_tekst(String algemene_tekst) {
        this.algemene_tekst = algemene_tekst;
    }

    public String getVariable_tekst() {
        return variable_tekst;
    }

    public void setVariable_tekst(String variable_tekst) {
        this.variable_tekst = variable_tekst;
    }

    public int getConnectieString() {
        return connectieString;
    }

    public void setConnectieString(int connectieString) {
        this.connectieString = connectieString;
    }

    public Date getEersteOptreden() {
        return eersteOptreden;
    }

    public void setEersteOptreden(Date eersteOptreden) {
        this.eersteOptreden = eersteOptreden;
    }

    public Date getOpgelost() {
        return opgelost;
    }

    public void setOpgelost(Date opgelost) {
        this.opgelost = opgelost;
    }    
    
}
