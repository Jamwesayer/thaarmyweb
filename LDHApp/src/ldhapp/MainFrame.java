/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author mucis
 */
public class MainFrame extends JFrame{
 private static LinkerPanel linkerPanel;
 private static RechterPanel rechterPanel;
    
    
    public MainFrame(String title, ConnectionDataBase db)
    {
        //setting
        super(title);
        setLayout(new BorderLayout());
        
        Container c = getContentPane();
        
        //Add components
        linkerPanel = new LinkerPanel(db);
        
        rechterPanel = new RechterPanel(db);
         
        c.add(linkerPanel,BorderLayout.WEST);
        c.add(rechterPanel,BorderLayout.EAST);
    }
}
