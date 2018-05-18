/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhproject;

import connection.ConnectionSignaalDataBase;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import model.Signaal;
import model.SignaalCalc;

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
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        signalDB = new ConnectionSignaalDataBase();
        
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        //frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main Container
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        // List
        DefaultListModel<String> dlm = new DefaultListModel<String>();
        
        //Signalen
        SignaalCalc mySignaalCalc = new SignaalCalc();
        lijst = mySignaalCalc.getSignalen();
        signalenLijst = new ArrayList<>();
        
        list  = new JList(dlm);
        list.setName("SignaalList");
        list.setBackground(Color.WHITE);
        
        scroller = new JScrollPane();  
        
        //#Plagiaat LOL
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
        JButton knop1 = new JButton("Afwijkingen vanuit DB tonen");
        buttonCtr.add(knop1);
        //Later beter
        JButton knop2 = new JButton("Actuele signalen tonen");
        buttonCtr.add(knop2);
        JButton knop3 = new JButton("Add to signal Dataset");
        buttonCtr.add(knop3);
        JButton uitloggen = new JButton("Log out");
        buttonCtr.add(uitloggen);
        
        JLabel label= new JLabel("<html><font color='red' size='5'>database is verbonden!</font></html>");
        buttonCtr.add(label);
        
        container.putClientProperty("SignaalList", list);
        container.add(buttonCtr, BorderLayout.WEST);
        
        frame.add(container);
        frame.pack();
        frame.setVisible(true);
        
        knop1.addActionListener(showSignalenAction());
        knop2.addActionListener(showSignalenDBAction());
        knop3.addActionListener(getButtonAction()); 
    }
 
    private static ActionListener getButtonAction() {
        ActionListener action = (ActionEvent e) -> {
            System.out.println(list.getSelectedValue());
            lijst.get(list.getSelectedIndex()).addToSignalTable(table_model);
            try {
                addSignalToSignalDB();
            } catch (SQLException ex) {
                System.out.println(ex);
            } catch (ParseException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return action;
    }
    
    private static ActionListener showSignalenAction () {
        
        DefaultListModel<String> dlm = new DefaultListModel<String>();        
        
        ActionListener action = (ActionEvent e) -> {
            for(Signaal signaal : lijst){
                signalenLijst.add(signaal);
                dlm.addElement(signaal.getalgemene_tekst());
            }
            list  = new JList(dlm);
            list.setBackground(Color.WHITE);
            scroller.getViewport().add(list);
        };
        return action;
    }
    
    private static ActionListener showSignalenDBAction () {
        
        ActionListener action = (ActionEvent e) -> {
            try {
                showSignal();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        return action;
    }
    
    private static void addSignalToSignalDB() throws SQLException, ParseException, SQLException{
        String sql = "INSERT INTO SignalenTabel "
                + "VALUES(?,?,NULL,?,NULL,NULL,NULL,NULL);";
        Signaal signaal = lijst.get(list.getSelectedIndex());
        signalDB.insertSignal(sql, signaal.getalgemene_tekst(), signaal.getvariable_tekst());
    }
    
    private static void showSignal() throws SQLException {
        String sql = "SELECT * FROM SignalenTabel";
        ArrayList<Signaal> requiredList = signalDB.showSignalen(sql);
        for(Signaal signaal : requiredList) {
            signalenLijst.add(signaal);
            signaal.addToSignalTable(table_model);
        }
    }
}
