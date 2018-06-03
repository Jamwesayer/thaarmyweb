/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mucis
 */
public class middelpuntPanel extends JPanel {
    //start algemeen
    private final ConnectionDataBase DB;
    private final GridLayout STANDAARD;
    //end algemeen
    
    private JList jList;
    private JScrollPane scroller;
    private DefaultListModel model;
    private JTextArea tekstLog;
    private JScrollPane scrollTekst;
    private JPanel businessRulePanel;
    private JPanel afwijkingPanel;
    private GridBagConstraints gc;
    
    /////////start signaal Table algmeen
    private JPanel sTabelPanel;
    private JPanel pTabelPanel;
    private JPanel iTabelPanel;
    
    private static DefaultTableModel table_model;
    
    public middelpuntPanel(ConnectionDataBase db)
    {
        setLayout(new GridLayout(3,1,1,1)); // aantalRij, aantalKolom,VerticaalGap,HorizontaalGap
        STANDAARD = new GridLayout();
        setPreferredSize(new Dimension(355,0));
        setBorder(BorderFactory.createTitledBorder("Gevonden afwijkingen"));
        DB = db;
        
        add(sTabelPanel);
        add(pTabelPanel);
        add(iTabelPanel);
    }
    

}