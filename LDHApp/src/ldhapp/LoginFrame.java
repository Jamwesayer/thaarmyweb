/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mucis
 */
public class LoginFrame {

    public LoginFrame(ConnectionDataBase db) {
        
        JFrame frame = new JFrame();
        ConnectionDataBase DB = db;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        
        GridBagConstraints d = new GridBagConstraints();
        
        d.insets = new Insets(0,0,0,0);
        
        frame.add(panel);
        JLabel gebruikersnaamTekst = new JLabel("PersoonID: ");
        panel.add(gebruikersnaamTekst);
        
        JTextField text = new JTextField();
        text.setPreferredSize(new Dimension(200,25));
        panel.add(text,d);
        
        JButton button = new JButton("Inloggen");
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(DB.inloggen(text.getText()))
                {
                    JFrame AppFrame = new MainFrame("Signalen applicatie",db);
                    AppFrame.setSize(1900,1000);
                    AppFrame.setResizable(true);
                    AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    //Run JFrame
                    frame.dispose();
                    AppFrame.setVisible(true);
                }
            }
        });
        c.insets = new Insets(10,10,10,0); 
        panel.add(button,c);

        frame.setSize(new Dimension(500,200));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setTitle("Login LDH");
        frame.setResizable(false);
        frame.setVisible(true);
        
        
        
        
    }
    
}
