/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhproject;

import model.SignaalLijst;
import Database.Database;
import connection.ConnectionSignaalDataBase;
import connection.ConnectionStringDataBase;
import excelExport.excelExportHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.ConnectionString;
import model.Signaal;

/**
 *
 * @author James
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    private final static int WIDTH = 720;
    private final static int HEIGHT = 480;
    private static JFrame frame = new JFrame("LdH");
    private static JList list;
    private static ArrayList<Signaal> lijst;
    private static ArrayList<Signaal> signalenLijst;
    private static DefaultTableModel table_model;
    private static JScrollPane scroller;
    private static ConnectionSignaalDataBase signalDB;
    private static excelExportHandler myExport;
    private static SignaalLijst mySignaalCalc;
    private static Database myDatabase;
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        
        signalDB = new ConnectionSignaalDataBase();
        myExport = new excelExportHandler(table_model);
        myDatabase = new Database();
        
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main Container
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        // List
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        
        //Signalen
        mySignaalCalc = new SignaalLijst();
        lijst = mySignaalCalc.getSignalen();
        signalenLijst = new ArrayList<>();
        
        //JList element
        list  = new JList(dlm);
        list.setName("SignaalList");
        list.setBackground(Color.WHITE);
        
        //JScroll element
        scroller = new JScrollPane();  
        scroller.setPreferredSize(new Dimension(350, 200));
        scroller.getViewport().add(list);
        scroller.getComponents();
        container.add(scroller, BorderLayout.EAST);
        
        // Table
        String kolom_namen[] = {"Naam","Variable","Connectiestring","Datum","Opgelost Datum"
        ,"Impact matrix entiteiten", "Impact matrix organisaties"};
        
        JTable table = new JTable();
        JScrollPane tableSP = new JScrollPane(table);
        table_model = (DefaultTableModel) table.getModel();
        table_model.setColumnIdentifiers(kolom_namen);        
        table.setModel(table_model);
        
        table.setBackground(Color.red);
        container.add(tableSP, BorderLayout.CENTER);

        // Button Holder
        JPanel buttonCtr = new JPanel();
        buttonCtr.setBackground(Color.BLUE);
        buttonCtr.setPreferredSize(new Dimension(200, 100));
        buttonCtr.setLayout(new BoxLayout(buttonCtr, BoxLayout.Y_AXIS));
        
        // Buttons
        JButton knop1 = new JButton("Afwijkingen vanuit auditDB tonen");
        JButton knop2 = new JButton("Actuele signalen tonen");
        JButton knop3 = new JButton("Add to signal Dataset");
        JButton exportKnop = new JButton("export to excel");
        JButton uitloggen = new JButton("Log out");
        
        //Add buttons to container
        buttonCtr.add(knop1);
        buttonCtr.add(knop2);
        buttonCtr.add(knop3);
        buttonCtr.add(exportKnop); 
        buttonCtr.add(uitloggen);
        
        JLabel label= new JLabel("<html><font color='red' size='5'>database is verbonden!</font></html>");
        buttonCtr.add(label);
        
        container.putClientProperty("SignaalList", list);
        container.add(buttonCtr, BorderLayout.WEST);
        
        //Frame settings
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
        
        //initActionListener
        knop1.addActionListener(showSignalenAction());
        knop2.addActionListener(showSignalenDBAction());
        knop3.addActionListener(getAddSignalDatasetAction()); 
        exportKnop.addActionListener(writeToExcelAction());
        
        showSignalen();
        showSignalenDB();
    }
 
    
    //Action listener
    private static ActionListener getAddSignalDatasetAction() {
        ActionListener action = (ActionEvent e) -> {
            try {
                addSignalDataset();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return action;
    }
    
    private static ActionListener showSignalenAction() {
        ActionListener action = (ActionEvent e) -> {
            showSignalen();
        };
        return action;
    }
    
    private static ActionListener showSignalenDBAction() {
        
        ActionListener action = (ActionEvent e) -> {
            try {
                showSignalenDB();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return action;
    }
    
    private static ActionListener writeToExcelAction() {
        ActionListener action = (ActionEvent e) -> {
            myExport = new excelExportHandler(table_model);
            myExport.writeToExcel();
        };
        return action;
    }
    
    //---Methods for listener---
    private static void showSignalen(){
        
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        for(Signaal signaal : lijst){
            dlm.addElement(signaal.getAlgemene_tekst());
        }
        list  = new JList(dlm);
        list.setBackground(Color.WHITE);
        scroller.getViewport().add(list);        
    }
    
    //Signalen naar tabel schrijven
    private static void addSignalDataset() throws SQLException, ClassNotFoundException{
        
        Signaal signaal =  lijst.get(list.getSelectedIndex());

        if(!mySignaalCalc.checkDuplicate(signaal.getUserID(),signalenLijst)) {
            ConnectionStringDataBase CSDB = new ConnectionStringDataBase();
            int lastId = CSDB.insertConnectionString(mySignaalCalc.getCS());
            signaal.setConnectieString(lastId);
            setConnectionForSignal(signaal);
            signalenLijst.add(signaal);
            signaal.addToSignalTable(table_model);
            try {
                addSignalToSignalDB();
            } catch (SQLException ex) {
                System.out.println(ex);
            } catch (ParseException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }
        else {
            System.out.println("--- DUPLICATE ---");
        }
    }
    
    //Signalen naar Database schrijven
    private static void addSignalToSignalDB() throws SQLException, ParseException {
        Signaal signaal = lijst.get(list.getSelectedIndex());
        signalDB.insertSignal(signaal.getUserID(), signaal.getSignaalType(), signaal.getAlgemene_tekst(), signaal.getConnectieString(), signaal.getVariable_tekst());
    }
    
    //Database signalen tonen
    private static void showSignalenDB() throws SQLException {
        //Signalen van Signaal Database
        ArrayList<Signaal> requiredList = signalDB.showSignalen();
        //Check voor duplicatie
        boolean in = false;
        for(Signaal signaal : requiredList) {
            for(Signaal signaal2 : lijst) {
                if(signaal.getUserID() == null ? signaal2.getUserID() == null : signaal.getUserID().replaceAll("\\s+","").equals(signaal2.getUserID())){
                    //Er bestaat duplicatie
                    in = true;
                }
            }
            if(in){
                //Reset
                in = false;
            }
            else {
                Date date = new Date();
                signaal.setOpgelost(date);
            }
        }
        
        for(Signaal signaal : requiredList) {
            if(!mySignaalCalc.checkDuplicate(signaal.getUserID(),signalenLijst)) {
                signalenLijst.add(signaal);
                signaal.addToSignalTable(table_model);
            }
        }
    }
    
    //Object signaal variable connectionstring vullen voor tabel
    public static Signaal setConnectionForSignal(Signaal signal) throws SQLException {        
        Connection con = DriverManager.getConnection(myDatabase.getUrl());
        String sql = "use Test_Signaal_Database SELECT * FROM ConnectionString WHERE ConnectionID = " + signal.getConnectieString();
        Statement stmts = con.createStatement();
        ResultSet rss = stmts.executeQuery(sql);
        
        while(rss.next()) {
            String servernaam = rss.getString("ServerNaam");
            String userConnectie = rss.getString("UserConnectie");
            String databaseNaam = rss.getString("DatabaseNaam");
            Timestamp timestamp = rss.getTimestamp("Timestamp");
            signal.setConnection(new ConnectionString(servernaam, userConnectie, databaseNaam, timestamp));
        }    
        
        return signal;
    }    
    
}
