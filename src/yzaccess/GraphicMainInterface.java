/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author lgerard
 */
public class GraphicMainInterface  extends JFrame{
    
    private JTextArea logs;
    private GraphicActivityPanel activityPanel; 
    
    public GraphicMainInterface(){
	super();
	
	setSize(1000,755);
	setTitle(YZAccess.appzName);
	
	WindowAdapter winCloser = new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	};
	addWindowListener(winCloser);
	
	Container contenu = getContentPane();
	contenu.setLayout(new GridLayout(0,2));
	
	
	//Logs d'activité
	logs = new JTextArea();
        logs.setVisible(true);
        logs.setText("");

        JScrollPane ScrollBar = new javax.swing.JScrollPane();
        ScrollBar.setViewportView(logs);
        ScrollBar.setVisible(true);
	ScrollBar.setBounds(20, 90, 500, 200);
	
	contenu.add(ScrollBar, BorderLayout.EAST);
	logs.setText("");
	
	//Panel avec les ports COM
	activityPanel = new GraphicActivityPanel();
	contenu.add(activityPanel);
	
	
	setEnabled(true);
	setVisible(true);
	this.toFront();
	
	this.pack();
	this.setDefaultLookAndFeelDecorated(true);
	this.setExtendedState(this.MAXIMIZED_BOTH);
    }
    
    public void addDouchette(GraphicPortCom douchette){
	activityPanel.addDouchette(douchette);
    }
    
    public void writeInLog(String log){
	logs.setText(log+"\n"+logs.getText());
    }
    
}
