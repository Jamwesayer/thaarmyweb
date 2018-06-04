/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Color;
import java.awt.Dimension;
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
import model.Signaal;
import model.SignaalLijst;

/**
 *
 * @author mucis
 */
class AfwijkingLijst extends JPanel{
    private JList jList;
    private static JScrollPane scroller;
    
    DefaultListModel model;

    int counter = 15;
    
    public AfwijkingLijst()
    {
        //paneel dimension
        setPreferredSize(new Dimension(355,0));
        setBorder(BorderFactory.createTitledBorder("Gevonden afwijkingen"));
        
        setLayout(new GridLayout(2,1,1,1));
        setPanel();
        addPanel();
    }
    
    public void setPanel() {
        
        try {
            model = new DefaultListModel();
            SignaalLijst mySignaalLijst = new SignaalLijst();
            ArrayList<Signaal> list = mySignaalLijst.getSignalen();
            
            for(Signaal signaal : list) {
                model.addElement(signaal.getAlgemene_tekst());
            }
            
            jList = new JList(model);
            
            scroller = new JScrollPane(jList);
            jList.setBackground(Color.WHITE);
        } catch (ClassNotFoundException | ParseException | SQLException ex) {
            Logger.getLogger(AfwijkingLijst.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addPanel()
    {
        add(scroller);
    }
}
