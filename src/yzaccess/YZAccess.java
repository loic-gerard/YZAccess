/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.util.Vector;


/**
 *
 * @author lgerard
 */
public class YZAccess {

    public static GraphicMainInterface interf;
    private static Vector douchettes;
    
    public static String appzName = "Yzeure And Rock - Access";
    public static String version = "Version 0.0.1";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	new YZAccess();
    }
    
    public YZAccess(){
	
	Log.log(appzName);
	Log.log(version);
	Log.log("----------------------------------------");
	
	//Création de l'interface
	interf = new GraphicMainInterface();
	
	//Chargement du fichier de configuration (config.ini)
	Config.init();
	
	//Initialisation des douchettes
	douchettes = new Vector();
	Vector ports = GetAvailableComPorts.getComPorts();
	for(int i = 0; i < ports.size(); i++){
	    if(Config.isComAllowed((String)ports.get(i))){
		GraphicPortCom pc = new GraphicPortCom((String)ports.get(i));
		KDCCommunication kdc = new KDCCommunication((String)ports.get(i), pc);
		interf.addDouchette(pc);
		douchettes.add(kdc);
	    }
	}
	
	//Force reload interface
	interf.repaint();
	interf.revalidate();
	
	//Log ready
	Log.log("Configuration et interface prêtes à l'utilisation");
	
	//Lancement du processus
	for(int i = 0; i < douchettes.size(); i++){
	    KDCCommunication d = (KDCCommunication)douchettes.get(i);
	    d.init();
	}
    }
}
