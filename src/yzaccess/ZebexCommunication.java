/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import com.sun.comm.Win32Driver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;

/**
 *
 * @author lgerard
 */
public class ZebexCommunication extends Thread{
    private String port;			//Port COM utilisé
    private CommPortIdentifier portIdentifier;	//Identifiant du port COM
    private SerialPort serialPort;		//Objet Port serie
    private BufferedReader inStream;		//flux de lecture du port
    private OutputStream outStream;		//flux d'écriture du port
    
    private String readed = "";			//Chaine lue du buffer.
    private GraphicPortCom interf;
    
    private boolean toOpen = true;
    
    public ZebexCommunication(String portCOM, GraphicPortCom in_interf){
	//Initialisation des variables
	port = portCOM;
	interf = in_interf;
    }
    
    public GraphicPortCom getInterface(){
        return interf;
    }
    
    private void open(){
        interf.setOrange();
        
        log("Debut d'initialisation d'un ZEBEX");
        interf.setActivite("Initialisation...");
	
	//initialisation du driver
	log("Initialisation du driver portCOM Windows : WAIT");
        interf.setActivite("Initialisation du port COM..."); 
	Win32Driver w32Driver = new Win32Driver();
	w32Driver.initialize();
	log("Initialisation du driver portCOM Windows : OK");
	
	//récupération de l'identifiant du port
	log("Recuperation de l'identifiant du port : WAIT");
        interf.setActivite("Récupération de l'identifiant port COM..."); 
	try {
	    portIdentifier= CommPortIdentifier.getPortIdentifier(port);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	log("Recuperation de l'identifiant du port : OK");
	
	//ouverture du port
	log("Ouverture du port : WAIT");
        interf.setActivite("Ouverture du port COM..."); 
	try {
	    serialPort = (SerialPort) portIdentifier.open("ZEBEX-"+port, 30000);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	log("Ouverture du port : OK");
	
	//règle les paramètres de la connexion
	log("Réglage des paramètres de la connexion du port : WAIT");
        interf.setActivite("Réglages des paramètres du port..."); 
	try {
	    serialPort.setSerialPortParams(
		9600,
		SerialPort.DATABITS_8,
		SerialPort.STOPBITS_1,
		SerialPort.PARITY_NONE);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	log("Réglage des paramètres de la connexion du port : OK");
	
	//récupération du flux de lecture et écriture du port
	log("Ouverture des flux de lecture/ecriture sur port : WAIT");
        interf.setActivite("Ouverture du flux..."); 
	try {
	    outStream = serialPort.getOutputStream();
	    inStream = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	log("Ouverture des flux de lecture/ecriture sur port : OK");
	
	log("Fin d'initialisation d'un ZEBEX : SUCCES");
	
	interf.setWhite();
	interf.setActivite("Prêt !");
        interf.setOpened();
        
        if(!this.isAlive()){
            start();
        }
    }
    
    public void close(){
        interf.setOrange();
        interf.setActivite("Déconnecté");
        this.interrupt();
        this.stop();
        serialPort.close();
        interf.setClosed();
    }
   
     
    private void log(String message){
	Log.log(port+" : "+message);
    }
    
    public void run(){
        if(toOpen){
            toOpen = false;
            open();
        }
        
	try{
	    while(1 == 1){
		int c = inStream.read();
		char ca = (char) c;
                
		if(ca != 13 && ca != 10){
                    readed += ca;
                }
		if(ca == 13){
                    log("CODE BARRE RECU : "+readed);
                    BilletManager.scanProcess(null, interf, readed);
                    readed = "";
		}
		
	    }
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
    
    public String getPort(){
	return port;
    }
}
