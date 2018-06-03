/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author mucis
 */
public class LinkerPanel extends JPanel implements ActionListener {
    //start algemeen
    private final ConnectionDataBase DB;
    private final GridLayout STANDAARD;
    //end algemeen
    
    //////start functieknoppen
    private JPanel buttonPanel;
    private JComboBox filterlijst;
    private GridBagConstraints gc;  
    private JButton  afwijkingStartenBtn;
    private JButton  afwijkingTestenBtn;
    private JButton  afwijkingWegschrijvenBtn;
    private JButton  impactStartBtn;
    private JButton  DBInlezenBtn;
    private JButton  datasetExporterenBtn;
    /////////////end functieknoppen
    
    /////////start log algmeen
    private JPanel logPanel;
    private JTextArea tekstLog;
    private JScrollPane scrollTekst;
    /////////end log algemeen
    
    //////////start gegevens
    private JPanel gegevensPanel;
    private JTextArea gegevensArea;
    private JTextArea liveTime;
    ////////// end gegevens
    
    
    
    public LinkerPanel(ConnectionDataBase db)
    {
        STANDAARD = new GridLayout();
        setLayout(new GridLayout(3,1,1,1)); // aantalRij, aantalKolom,VerticaalGap,HorizontaalGap
        setPreferredSize(new Dimension(155, 350));
        DB = db;

        //button
        setButtonPanel();
        addButtonPanel();

        //algemeen
        setLogPanel();
        addLogPanel();

        //algemeen gegevens
        setInfoPanel();
        
        add(buttonPanel);
        add(logPanel);
        add(gegevensPanel);
    }
    
    //////start methode button
    private void repaintPanel()
    {
        revalidate();
        repaint();
    }
    
    public void setButtonPanel()
    {
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Activiteiten"));

        afwijkingStartenBtn = new JButton("Afwijkingen starten");
        afwijkingTestenBtn = new JButton("<html>Afwijkingen <font color='red'>testen</font></html>");
        afwijkingWegschrijvenBtn = new JButton("<html>Afwijkingen<br /> wegschrijven</html>");

        impactStartBtn = new JButton("Impacten starten");

        //knoppen declareren
        DBInlezenBtn = new JButton("Database inlezen");
        datasetExporterenBtn = new JButton("Dataset exporteren");
        //filter klaarzetten
        String[] filterStrings = { "Alleen actueel", "Alleen Opgelost", "Alles"};
        filterlijst = new JComboBox(filterStrings);
        //String van filterlijst op center zetten.
        ((JLabel)filterlijst.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        filterlijst.setSelectedIndex(2);
    }

    public void addButtonPanel()
    {
        buttonPanel.setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 0.1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.ipady = 5;
        gc.fill = gc.HORIZONTAL;
        buttonPanel.add(DBInlezenBtn,gc);

        DBInlezenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Database ingelezen");
                          
            if(DB.SelectSignalen())
            {
                gc.gridx = 0;
                gc.gridy = 1;
                buttonPanel.add(datasetExporterenBtn,gc);

                gc.gridx = 0;
                gc.gridy = 2;
                buttonPanel.add(filterlijst,gc);

                //afwijkingen
                gc.gridx = 0;
                gc.gridy = 3;
                buttonPanel.add(afwijkingTestenBtn,gc);

                gc.gridx = 0;
                gc.gridy = 4;
                buttonPanel.add(afwijkingStartenBtn,gc);
                repaintPanel(); 
            }
          }
        });
        
        datasetExporterenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Dataset geëxporteerd");
          }
        });
        
        afwijkingTestenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Afwijkingen getest");
          }
        });
        
        afwijkingStartenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Afwijkingen gestart");
             
            gc.gridx = 0;
            gc.gridy = 5;
            buttonPanel.add(afwijkingWegschrijvenBtn,gc);
            repaintPanel();
          }
        });

        afwijkingWegschrijvenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Afwijkingen weg-\ngeschreven");
            //impacten
            gc.gridx = 0;
            gc.gridy = 6;
            buttonPanel.add(impactStartBtn,gc);
            repaintPanel();
          }
        });

        impactStartBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Impacten gestart");
          }
        });
        
        filterlijst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JComboBox comboBox = (JComboBox) event.getSource();
                String gekozen = (String) comboBox.getSelectedItem();
                insertLog("filter gekozen:\n "+ gekozen);
            }
        });
    }
    //////end methode button
    
    ///////start algemeen log
    public void setLogPanel()
    {
        logPanel = new JPanel();
        logPanel.setLayout(STANDAARD);
        logPanel.setBorder(BorderFactory.createTitledBorder("Activiteiten"));

        tekstLog = new JTextArea(17,12);
        tekstLog.setEditable(false);

        scrollTekst = new JScrollPane(tekstLog,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);   
    }
    
    public void addLogPanel()
    {
        logPanel.add(scrollTekst);
        logPanel.setBorder(BorderFactory.createTitledBorder("Algemeen logs"));
    }   
    
    public void insertLog(String tekst)
    {
        DateFormat df = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date dateobj = new Date();
        tekstLog.append(df.format(dateobj) +"\n"+ tekst + "\n\n");
    }
    ///////end algemeen log
    
    //////start methode gegevens
    public void setInfoPanel()
    {
        Timer t = new Timer(1000, this);
        t.start();
        
        gegevensPanel = new JPanel();
        gegevensPanel.setLayout(new GridBagLayout());
        gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.weightx = 0.1;
        gc.weighty = 0.1;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = gc.HORIZONTAL;
        
        liveTime = new JTextArea();
        liveTime.setEditable(false);
        liveTime.setLineWrap(true);
        
        gegevensPanel.add(liveTime,gc);
        
        gegevensArea = new JTextArea();
        gegevensArea.setEditable(false);
        gegevensArea.setLineWrap(true);
        gegevensArea.append(DB.systeemGegevens());
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weighty = 10;
        gc.fill = gc.BOTH;
        
        gegevensPanel.add(gegevensArea,gc);
        gegevensPanel.setPreferredSize(new Dimension(100, 350));
        gegevensPanel.setBorder(BorderFactory.createTitledBorder("Systeem gegevens"));
    }
    
    public void actionPerformed(ActionEvent ae) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        liveTime.setText("Tijd: "+ sdf.format(d));
  }
    //////end methode gegevens
}