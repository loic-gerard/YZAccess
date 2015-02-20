/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import org.ini4j.Wini;
import java.io.File;

public class Config {
    
    private static String[] config_notusedcom;
    private static boolean config_verbose = true;
    private static int config_cmdretrycount = 10;
    private static int config_cmdretrytime = 500;
    private static String[] config_typesbilletacceptes;
    
    public static void init(){
	try{
	    Wini ini = new Wini(new File("config.ini"));
	    config_notusedcom = ini.get("config", "notusedcom").split(",");
	    if(!ini.get("config", "verbose").equals("true")){
		config_verbose = false;
	    }
	    config_cmdretrycount = Integer.parseInt(ini.get("config", "cmdretrycount"));
	    config_cmdretrytime = Integer.parseInt(ini.get("config", "cmdretrytime"));
	    config_typesbilletacceptes = ini.get("config", "typesbilletacceptes").split(",");
	}catch(Exception e){
	    new CrashError("Erreur de lecture du fichier de configuration", e, null);
	}
    }
    
    public static boolean isComAllowed(String port){
	for(int i = 0; i < config_notusedcom.length; i++){
	    if(config_notusedcom[i].equals(port)){
		return false;
	    }
	}
	return true;
    }
    
    public static boolean isVerbose(){
	return config_verbose;
    }
    
    public static int getCmdRetryCount(){
	return config_cmdretrycount;
    }
    
    public static int getCmdRetryTime(){
	return config_cmdretrytime;
    }
    
    public static boolean isTypeBilletAccepte(String typebillet){
	for(int i = 0; i < config_typesbilletacceptes.length; i++){
	    if (config_typesbilletacceptes[i] == typebillet){
		return true;
	    }
	}
	return false;
    }
}
