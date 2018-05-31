/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldhapp;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author mucis
 */
public class LogAction extends JPanel{
   private JTextArea tekstLog;
   private JScrollPane scrollTekst;
   
    public LogAction()
    {
        tekstLog = new JTextArea(17,12);
        tekstLog.setEditable(false);
        
        scrollTekst = new JScrollPane(tekstLog,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //paneel dimension
        setPreferredSize(new Dimension(100, 350));
        setBorder(BorderFactory.createTitledBorder("Algemeen logs"));
        add(scrollTekst);
    }
    
    public void insertLog(String tekst)
    {
        DateFormat df = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date dateobj = new Date();
        tekstLog.append(df.format(dateobj) +"\n"+ tekst + "\n\n");
    }
}
