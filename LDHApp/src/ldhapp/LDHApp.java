/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import javax.swing.JFrame;


/**
 *
 * @author mucis
 */
public class LDHApp {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //JFrame setting
        ConnectionDataBase db = new ConnectionDataBase();
        
        //LoginFrame loginFrame = new LoginFrame(db);
        JFrame AppFrame = new MainFrame("Signalen applicatie",db);
        AppFrame.setSize(1900,1000);
        AppFrame.setResizable(true);
        AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AppFrame.setVisible(true);
    }
}
