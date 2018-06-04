/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.Signaal;
import model.SignaalLijst;

/**
 *
 * @author mucis
 */
public class RechterPanel extends JPanel {

    private JList jList;
    private JScrollPane scroller;
    private DefaultListModel model;
    private JTextArea tekstLog;
    
    private JScrollPane scrollTekst;
    
    private JPanel businessRulePanel;
    private JPanel afwijkingPanel;
    
    private final GridLayout STANDAARD;
    private GridBagConstraints gc;
    

    public RechterPanel(ConnectionDataBase db)
    {
        setLayout(new GridBagLayout()); // aantalRij, aantalKolom,VerticaalGap,HorizontaalGap
        STANDAARD = new GridLayout();
        setPreferredSize(new Dimension(355,0));
        setBorder(BorderFactory.createTitledBorder("Gevonden afwijkingen"));

        setPanel();
        addPanel();
        //afwijkingLijst = new AfwijkingLijst();
        //add(afwijkingLijst);
    }
    
    public void setPanel()
    {
        try {
            model = new DefaultListModel();
            
            SignaalLijst mySignaalLijst = new SignaalLijst();
            ArrayList<Signaal> list = mySignaalLijst.getSignalen();
            
            for(Signaal signaal : list){
                model.addElement(signaal.getAlgemene_tekst());
            }
            
            jList = new JList(model);
            scroller = new JScrollPane(jList);
            jList.setBackground(Color.WHITE);
            afwijkingPanel = new JPanel();
            afwijkingPanel.setLayout(STANDAARD);
            afwijkingPanel.add(scroller);
            afwijkingPanel.setBorder(BorderFactory.createTitledBorder("Alle gevonden afwijkingen"));
            
            tekstLog = new JTextArea(10,12);
            tekstLog.setEditable(false);
            scrollTekst = new JScrollPane(tekstLog,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            businessRulePanel = new JPanel();
            businessRulePanel.setLayout(STANDAARD);
            businessRulePanel.add(scrollTekst);
            businessRulePanel.setBorder(BorderFactory.createTitledBorder("Aantal Afwijkingen per Business Rules"));
            
        } catch (ClassNotFoundException | ParseException | SQLException ex) {
            Logger.getLogger(RechterPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addPanel()
    {
        gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.1;
        gc.weighty = 0.1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 0;
        add(businessRulePanel,gc);
        gc.weighty = 100;
        
        gc.gridheight = 120;
        gc.gridx = 0;
        gc.gridy = 1;
        add(afwijkingPanel,gc);
    }
}