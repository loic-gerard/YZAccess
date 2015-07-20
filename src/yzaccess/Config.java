/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import org.ini4j.Wini;
import java.io.File;

public class Config {
    
    private static boolean config_verbose = true;
    private static int config_cmdretrycount = 10;
    private static int config_cmdretrytime = 500;
    private static String[] config_typesbilletacceptes;
    private static String[] config_kdc_ports;
    private static String[] config_kdc_names;
    private static String[] config_zebex_ports;
     private static String[] config_zebex_names;
    
    public static void init(){
	try{
	    Wini ini = new Wini(new File("config.ini"));
	    if(!ini.get("config", "verbose").equals("true")){
		config_verbose = false;
	    }
	    config_cmdretrycount = Integer.parseInt(ini.get("config", "cmdretrycount"));
	    config_cmdretrytime = Integer.parseInt(ini.get("config", "cmdretrytime"));
	    config_typesbilletacceptes = ini.get("config", "typesbilletacceptes").split(",");
            if(ini.get("config", "kdc_ports").equals("")){
                config_kdc_ports = new String[0];
            }else{
                config_kdc_ports = ini.get("config", "kdc_ports").split(",");
            }
            if(ini.get("config", "kdc_names").equals("")){
                config_kdc_names = new String[0];
            }else{
                config_kdc_names = ini.get("config", "kdc_names").split(",");
            }
            if(ini.get("config", "zebex_ports").equals("")){
                config_zebex_ports = new String[0];
            }else{
                config_zebex_ports = ini.get("config", "zebex_ports").split(",");
            }
            if(ini.get("config", "zebex_names").equals("")){
                config_zebex_names = new String[0];
            }else{
                config_zebex_names = ini.get("config", "zebex_names").split(",");
            }
           
            
	}catch(Exception e){
	    new CrashError("Erreur de lecture du fichier de configuration", e, null);
	}
    }
    
    public static String[] getKDCPorts(){
        return config_kdc_ports;
    }
    
    public static String[] getZebexPorts(){
        return config_zebex_ports;
    }
    
    public static String getKDCName(int i){
        return config_kdc_names[i];
    }
    
    public static String getZebexName(int i){
        return config_zebex_names[i];
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
	    if (config_typesbilletacceptes[i].equals(typebillet)){
		return true;
	    }
	}
	return false;
    }
}
