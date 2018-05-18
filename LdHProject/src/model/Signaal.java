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
    String algemene_tekst;
    String variable_tekst;
    String connectieString;
    Date eersteOptreden;
    Date opgelost;
    String impactEntiteit;
    String impactOrganisatie;

    public String getalgemene_tekst() {
        return algemene_tekst;
    }

    public void setalgemene_tekst(String algemene_tekst) {
        this.algemene_tekst = algemene_tekst;
    }

    public String getvariable_tekst() {
        return variable_tekst;
    }

    public void setvariable_tekst(String variable_tekst) {
        this.variable_tekst = variable_tekst;
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
    
    public Signaal(String algemene_tekst, String variable_tekst, String connectieString, Date eersteOptreden, Date opgelost) {
        this.algemene_tekst = algemene_tekst;
        this.variable_tekst = variable_tekst;        
        this.connectieString = connectieString;
        this.eersteOptreden = eersteOptreden;
        this.opgelost = opgelost;
    }
    
    public Signaal(String algemene_tekst, String variable_tekst) {
        this.algemene_tekst = algemene_tekst;
        this.variable_tekst = variable_tekst;
        this.eersteOptreden = Calendar.getInstance().getTime();
    }    
    
    public void showSignaal() {
        //System.out.println(algemene_tekst + " " + variable_tekst + " " + eersteOptreden);
    }
    
    public boolean addToSignalTable(DefaultTableModel tableModel) {
        
        tableModel.addRow(new Object[] {algemene_tekst,variable_tekst,"connectie",eersteOptreden,"NEE"
                ,impactEntiteit,impactOrganisatie});
        return false;
    }
}
