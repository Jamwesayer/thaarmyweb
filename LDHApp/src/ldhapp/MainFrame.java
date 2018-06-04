/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.SignaalLijst;

/**
 *
 * @author mucis
 */
public class MainFrame extends JFrame  implements ActionListener{
 private static JPanel linkerPanel;
  private static JPanel middelpuntPanel;
 private static JPanel rechterPanel;
 
 
 //start algemeen
    private final ConnectionDataBase DB;
    private final GridLayout STANDAARD;
    //end algemeen
    
    //////start functieknoppen
    private JPanel buttonPanel;
    private JComboBox filterlijst;
    private GridBagConstraints gc;  
    private JButton  afwijkingStartenBtn;
    private JButton  afwijkingWegschrijvenBtn;
    private JButton  impactStartBtn;
    private JButton  DBInlezenBtn;
    private JButton  uitlogBtn;
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
    
    //rechterPaneel
    
    private JList jList;
    private JScrollPane scroller;
    private DefaultListModel model;
    private JTextArea afwijkAantal;
    
    private JPanel businessRulePanel;
    private JPanel afwijkingPanel;
    
    
    
    //middelpuntPanel
    
    /////////start signaal Table algmeen
    private JPanel sTabelPanel;
    private JPanel pTabelPanel;
    private JPanel iTabelPanel;

    private static DefaultTableModel table_model;
    
    
    public MainFrame(String title, ConnectionDataBase db)
    {
        //setting
        super(title);
        setLayout(new BorderLayout());
        STANDAARD = new GridLayout();
        DB = db;
        Container c = getContentPane();
        
        //Add components
        linkerPanel = new JPanel();
        setLinkerPanel();
        
        middelpuntPanel = new JPanel();
        setMiddelpuntPanel();
        
        rechterPanel = new RechterPanel(db);
        setRechterPanel();
        
        c.add(linkerPanel,BorderLayout.WEST);
        c.add(middelpuntPanel,BorderLayout.CENTER);
        c.add(rechterPanel,BorderLayout.EAST);
    }
    
    public void setLinkerPanel()
    {
        linkerPanel.setLayout(new GridLayout(3,1,1,1)); // aantalRij, aantalKolom,VerticaalGap,HorizontaalGap
        linkerPanel.setPreferredSize(new Dimension(155, 150));
        
        //button
        setButtonPanel();
        addButtonPanel();

        //algemeen
        setLogPanel();
        addLogPanel();

        //algemeen gegevens
        setInfoPanel();
        
        linkerPanel.add(buttonPanel);
        linkerPanel.add(logPanel);
        linkerPanel.add(gegevensPanel);
    }
    
     private void repaintPanel()
    {
        revalidate();
        repaint();
    }
    
    public void setButtonPanel()
    {
        //knoppen declareren
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Activiteiten"));

        afwijkingStartenBtn = new JButton("Afwijkingen starten");
        afwijkingWegschrijvenBtn = new JButton("<html>Afwijkingen<br /> wegschrijven</html>");

        impactStartBtn = new JButton("Impacten starten");
        
        DBInlezenBtn = new JButton("Database inlezen");
        
        uitlogBtn = new JButton("<html><font color=red>Uitloggen</font>");
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

        gc.gridx = 0;
        gc.gridy = 1;
        buttonPanel.add(filterlijst,gc);

        gc.gridx = 0;
        gc.gridy = 2;
        buttonPanel.add(datasetExporterenBtn,gc);


        gc.gridx = 0;
        gc.gridy = 3;
        buttonPanel.add(afwijkingStartenBtn,gc);
        repaintPanel(); 

        if(!DB.getBeheerder())
        {
            gc.gridx = 0;
            gc.gridy = 4;
            buttonPanel.add(afwijkingWegschrijvenBtn,gc);
        }
       
        gc.gridx = 0;
        gc.gridy = 5;
        buttonPanel.add(uitlogBtn,gc);
        repaintPanel();  
        datasetExporterenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Dataset geëxporteerd");
          }
        });
        
        
        afwijkingStartenBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            insertLog("Afwijkingen gestart");
             
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
            gc.gridy = 5;
            repaintPanel();
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
    
    
    
    public void setMiddelpuntPanel()
    {
        middelpuntPanel.setLayout(new GridLayout(3,1,1,1)); // aantalRij, aantalKolom,VerticaalGap,HorizontaalGap
        middelpuntPanel.setPreferredSize(new Dimension(355,0));
        sTabelPanel = new JPanel();
        setSTablePanel(sTabelPanel);
        middelpuntPanel.add(sTabelPanel);
        //middelpuntPanel.add(pTabelPanel);
        //middelpuntPanel.add(iTabelPanel);
    }
    
    
    public void setSTablePanel(JPanel sTabelPanel)
    {
        
        sTabelPanel.setLayout(STANDAARD);
        sTabelPanel.setBorder(BorderFactory.createTitledBorder("Activiteiten"));
        
        String kolom_namen[] = {"Naam","Variable","Connectiestring","Datum","Huidige datum","Opgelost Datum"
                , "Impact matrix entiteiten"
                , "Impact matrix organisaties"};

        JTable table = new JTable();
        JScrollPane tableSP = new JScrollPane(table);
        table_model = (DefaultTableModel) table.getModel();
        table_model.setColumnIdentifiers(kolom_namen);        
        table.setModel(table_model); 
        
        sTabelPanel.add(tableSP);
    }

    private void setRechterPanel() {
        rechterPanel.setPreferredSize(new Dimension(355,0));
        rechterPanel.setBorder(BorderFactory.createTitledBorder("Gevonden afwijkingen"));

        setBothList();
        addBothList();
    }
    
    public void setBothList()
    {
        model = new DefaultListModel();
        
        jList = new JList(model);
        scroller = new JScrollPane(jList);
        jList.setBackground(Color.WHITE);
        afwijkingPanel = new JPanel();
        afwijkingPanel.setLayout(STANDAARD);
        afwijkingPanel.add(scroller);
        afwijkingPanel.setBorder(BorderFactory.createTitledBorder("Alle gevonden afwijkingen"));
        
        afwijkAantal = new JTextArea(10,12);
        afwijkAantal.setEditable(false);
        scrollTekst = new JScrollPane(afwijkAantal,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        businessRulePanel = new JPanel();
        businessRulePanel.setLayout(STANDAARD);
        businessRulePanel.add(scrollTekst);
        businessRulePanel.setBorder(BorderFactory.createTitledBorder("Aantal Afwijkingen per Business Rules"));
        String BRPText = "";
        SignaalLijst lijst = null;
        try {
            lijst = new SignaalLijst();
        } catch (ClassNotFoundException | ParseException | SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        int[] afwijkingen = lijst.getCount();
        String[] afwijkingenText = {"Vergelijk Profit met AD ","Vergelijk Clever met de AD ", "Vergelijk Profit met Clever "};
        int count = 0;
        for(String string : afwijkingenText){
            BRPText += string + afwijkingen[count] + "Gevonden. \n";
            count++;
        }
        afwijkAantal.setText(BRPText);
        System.out.println(BRPText);
    }

    public void addBothList()
    {
        gc = new GridBagConstraints();

        gc.anchor = GridBagConstraints.NORTH;
        gc.weightx = 0.1;
        gc.weighty = 0.1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 0;
        rechterPanel.add(businessRulePanel,gc);
        gc.weighty = 100;
        
        gc.gridheight = 120;
        gc.gridx = 0;
        gc.gridy = 1;
        rechterPanel.add(afwijkingPanel,gc);
    }
}
