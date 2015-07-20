/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.awt.Color;
import java.util.Vector;


/**
 *
 * @author lgerard
 */
public class YZAccess {

    public static GraphicMainInterface interf;
    private static Vector douchettes;
    public static DbConnexion cnx;
    
    public static String appzName = "Yzeure And Rock - Access";
    public static String version = "Version 0.0.1";
    public static YZAccess appz;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	appz = new YZAccess();
    }
    
    public YZAccess(){
	
	Log.log(appzName);
	Log.log(version);
	Log.log("----------------------------------------");
	
	//Création de l'interface
	interf = new GraphicMainInterface();
	
	//Chargement du fichier de configuration (config.ini)
	Config.init();
        
        //Connexion à la DBB
        cnx = new DbConnexion("../YZDatabase/database.s3db");
        cnx.connect();

	//Initialisation des douchettes
	douchettes = new Vector();
        String[] ports = Config.getKDCPorts();
        for(int i = 0; i < ports.length; i++){
            String port = ports[i];
            GraphicPortCom pc = new GraphicPortCom(Config.getKDCName(i)+" ("+port+")", "KDC", port);
            KDCCommunication kdc = new KDCCommunication(port, pc);
            interf.addDouchette(pc);
            douchettes.add(kdc);
        }
        
        ports = Config.getZebexPorts();
        for(int i = 0; i < ports.length; i++){
            String port = ports[i];
            GraphicPortCom pc = new GraphicPortCom(Config.getZebexName(i)+" ("+port+")", "ZEBEX", port);
            ZebexCommunication kdc = new ZebexCommunication(port, pc);
            interf.addDouchette(pc);
            douchettes.add(kdc);
        }
	
	
	//Force reload interface
	interf.repaint();
	interf.revalidate();
	
	//Log ready
	Log.log("Configuration et interface prêtes à l'utilisation");
	
	//Lancement du processus
	for(int i = 0; i < douchettes.size(); i++){
            if(douchettes.get(i) instanceof KDCCommunication){
                KDCCommunication d = (KDCCommunication)douchettes.get(i);
                d.start();
            }else if(douchettes.get(i) instanceof ZebexCommunication){
                ZebexCommunication d = (ZebexCommunication)douchettes.get(i);
                d.start();
            }
	}
    }
    
    public static void closeDouchette(GraphicPortCom pc){
        for(int i = 0; i < douchettes.size(); i++){
            if(douchettes.get(i) instanceof KDCCommunication){
                KDCCommunication d = (KDCCommunication)douchettes.get(i);
                if(d.getInterface().equals(pc)){
                    d.close();
                    return;
                }
            }else if(douchettes.get(i) instanceof ZebexCommunication){
                ZebexCommunication d = (ZebexCommunication)douchettes.get(i);
                if(d.getInterface().equals(pc)){
                    d.close();
                    return;
                }
            }
            
	}
    }
    
    public static void openDouchette(GraphicPortCom pc){
        for(int i = 0; i < douchettes.size(); i++){
            if(douchettes.get(i) instanceof KDCCommunication){
                KDCCommunication d = (KDCCommunication)douchettes.get(i);
                if(d.getInterface().equals(pc)){
                    douchettes.remove(i);
                    KDCCommunication nd = new KDCCommunication(d.getPort(), pc);
                    nd.start();
                    douchettes.add(nd);
                    
                    return;
                }
            }else if(douchettes.get(i) instanceof ZebexCommunication){
                ZebexCommunication d = (ZebexCommunication)douchettes.get(i);
                if(d.getInterface().equals(pc)){
                    douchettes.remove(i);
                    ZebexCommunication nd = new ZebexCommunication(d.getPort(), pc);
                    nd.start();
                    douchettes.add(nd);
                    
                    return;
                }
            }
            
	}
    }
   
    
    
}
