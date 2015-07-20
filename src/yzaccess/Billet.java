/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author lgerard
 */
public class Billet {

    protected boolean exists = false;
    protected int pk;
    protected String codeBarre;
    protected boolean valide = false;
    protected String type;
    protected Vector history = new Vector();
    
    public Billet(String code, Integer pk){
        if(code != null){
            YZAccess.cnx.populateBillet(this, code);
        }else if(pk != null){
            YZAccess.cnx.populateBillet(this, pk);
        }else{
            new CrashError("Appel Billet incorrect : code ou pk requis", null, null);
        }
    }
    
    public boolean exists(){
        return exists;
    }
    
    public boolean isValide(){
        return valide;
    }
    
    public boolean istypeValide(){
        if(Config.isTypeBilletAccepte(type)){
            return true;
        }
        return false;
    }
    
    public String getType(){
        return type;
    }
    
    public boolean isAlreadyValidated(){
        for(int i = 0; i < history.size(); i++){
            BilletHistory bh = (BilletHistory)history.get(i);
            if(bh.type.equals("OK")){
                return true;
            }
        }
        return false;
    }
    
    public String getLastValidation(){
        for(int i = 0; i < history.size(); i++){
            BilletHistory bh = (BilletHistory)history.get(i);
            if(bh.type.equals("OK")){
                return bh.datetime;
            }
        }
        return "Jamais";
    }
    
    public void logActivity(String code){
        YZAccess.cnx.setScanned(pk, code);
    }
    
    
    
}
