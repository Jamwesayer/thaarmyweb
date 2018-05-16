/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author J_Administrator
 */
public class Signaal {
    String algemeneSignaal;
    String variableSignaal;
    String connectieString;
    Date eersteOptreden;
    Date opgelost;

    public String getAlgemeneSignaal() {
        return algemeneSignaal;
    }

    public void setAlgemeneSignaal(String algemeneSignaal) {
        this.algemeneSignaal = algemeneSignaal;
    }

    public String getVariableSignaal() {
        return variableSignaal;
    }

    public void setVariableSignaal(String variableSignaal) {
        this.variableSignaal = variableSignaal;
    }

    public String getConnectieString() {
        return connectieString;
    }

    public void setConnectieString(String connectieString) {
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
    
    public Signaal(String algemeneSignaal, String variableSignaal, String connectieString, Date eersteOptreden, Date opgelost) {
        this.algemeneSignaal = algemeneSignaal;
        this.variableSignaal = variableSignaal;        
        this.connectieString = connectieString;
        this.eersteOptreden = eersteOptreden;
        this.opgelost = opgelost;
    }
    
    public Signaal(String algemeneSignaal, String variableSignaal) {
        this.algemeneSignaal = algemeneSignaal;
        this.variableSignaal = variableSignaal;
        this.eersteOptreden = Calendar.getInstance().getTime();
    }    
    
    public void showSignaal() {
        //System.out.println(algemeneSignaal + " " + variableSignaal + " " + eersteOptreden);
    }
    
    public boolean addToSignalTable(DefaultTableModel tableModel) {
        
        tableModel.addRow(new Object[] {algemeneSignaal,variableSignaal,eersteOptreden,"NEE"});
        
        return false;
    }
}
