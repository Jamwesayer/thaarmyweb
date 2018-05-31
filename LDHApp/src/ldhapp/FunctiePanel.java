/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author mucis
 */
public class FunctiePanel extends JPanel {
    private JComboBox filterlijst;
    private LogAction logAction;
    private GridBagConstraints gc;
    
    private ConnectionDataBase DB;
    
    private JButton  afwijkingStartenBtn;
    private JButton  afwijkingTestenBtn;
    private JButton  afwijkingWegschrijvenBtn;
    private JButton  impactStartBtn;
    private JButton  DBInlezenBtn;
    private JButton  datasetExporterenBtn;
    
    public FunctiePanel(LogAction logAction, ConnectionDataBase db){
        this.logAction = logAction;
        settingPanel();
        addPanel();
        DB = db;
    }
    
    private void repaintPanel()
    {
        revalidate();
        repaint();
    }
    
    private void settingPanel()
    {
        //paneel dimension  
        setBorder(BorderFactory.createTitledBorder("Functieknoppen"));

        afwijkingStartenBtn = new JButton("Afwijkingen starten");
        afwijkingTestenBtn = new JButton("<html>Afwijkingen <font color='red'>testen</font></html>");
        afwijkingWegschrijvenBtn = new JButton("<html>Afwijkingen<br /> wegschrijven</html>");

        impactStartBtn = new JButton("Impacten starten");

        //knoppen declareren
        DBInlezenBtn = new JButton("Database inlezen");
        datasetExporterenBtn = new JButton("Dataset exporteren");
        //filter klaarzetten
        String[] filterStrings = { "Alleen actueel", "Alleen Opgelost", "Alles" };
        filterlijst = new JComboBox(filterStrings);
        //String van filterlijst op center zetten.
        ((JLabel)filterlijst.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        filterlijst.setSelectedIndex(2);
    }
    
    private void addPanel()
    {
        setLayout(new GridBagLayout());
        gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 0.1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.ipady = 5;
        gc.fill = gc.HORIZONTAL;
        add(DBInlezenBtn,gc);

        DBInlezenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e) 
          {
            logAction.insertLog("Database ingelezen");
                          
            if(DB.SelectSignalen())
            {
                gc.gridx = 0;
                gc.gridy = 1;
                add(datasetExporterenBtn,gc);

                gc.gridx = 0;
                gc.gridy = 2;
                add(filterlijst,gc);

                //afwijkingen
                gc.gridx = 0;
                gc.gridy = 3;
                add(afwijkingTestenBtn,gc);

                gc.gridx = 0;
                gc.gridy = 4;
                add(afwijkingStartenBtn,gc);
                repaintPanel();
            }
          }
        });
        
        datasetExporterenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            logAction.insertLog("Dataset geëxporteerd");
          }
        });
        
        afwijkingTestenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            logAction.insertLog("Afwijkingen getest");
          }
        });
        
        afwijkingStartenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            logAction.insertLog("Afwijkingen gestart");
             
            gc.gridx = 0;
            gc.gridy = 5;
            add(afwijkingWegschrijvenBtn,gc);
            repaintPanel();
          }
        });

        afwijkingWegschrijvenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            logAction.insertLog("Afwijkingen weg-\ngeschreven");
            //impacten
            gc.gridx = 0;
            gc.gridy = 6;
            add(impactStartBtn,gc);
            repaintPanel();
          }
        });

        impactStartBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
             logAction.insertLog("Impacten gestart");
          }
        });
        
        filterlijst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                String gekozen = (String) comboBox.getSelectedItem();
                logAction.insertLog("filter gekozen:\n "+ gekozen);
            }
        });
    }
}
