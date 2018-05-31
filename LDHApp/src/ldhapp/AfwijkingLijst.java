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
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    
    public void setPanel()
    {
    model = new DefaultListModel();
    for (int i = 0; i < 15; i++)
    {
        model.addElement("Element " + i);
    }
        jList = new JList(model);
    
        scroller = new JScrollPane(jList);
        jList.setBackground(Color.WHITE);
    }
    
    public void addPanel()
    {
        add(scroller);
    }
}
