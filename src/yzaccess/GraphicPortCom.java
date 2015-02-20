/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;

/**
 *
 * @author lgerard
 */
public class GraphicPortCom extends JPanel {
    private JLabel activite;
    private JButton retry;
    
    public GraphicPortCom(String name){
	super();
	
	this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	
	Border paddingBorder = BorderFactory.createEmptyBorder(20,20,20,20);
	setBackground(new Color(255, 114, 0));
	
	JLabel numero = new JLabel(name+"\n");
	float x = 0;
	numero.setAlignmentX(x);
	numero.setFont(new Font("Arial",Font.BOLD,50));
	numero.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	add(numero);
	
	activite = new JLabel("<html>connect to port COM<br>Please wait...</html>", SwingConstants.CENTER);
	activite.setAlignmentX(x);
	activite.setFont(new Font("Arial", Font.BOLD, 20));
	activite.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	add(activite);
	
	JPanel bContainer = new JPanel();
	bContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	retry = new JButton("Tenter la reconnexion");
	ActionListener btn_retryListener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    }
	};
	retry.addActionListener(btn_retryListener);
	retry.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	bContainer.add(retry);
	add(bContainer);
	
	setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK));
    }
    
    public void setGreen(){
	setBackground(new Color(70,149,0));
    }
    
    public void setOrange(){
	setBackground(new Color(255, 114, 0));
    }
    
    public void setRed(){
	setBackground(new Color(255, 0, 0));
    }
    
    public void hideRetry(){
	retry.setVisible(false);
    }
    
    public void showRetry(){
	retry.setVisible(true);
    }
    
    public void setActivite(String texte){
	activite.setText(texte);
    }
}
