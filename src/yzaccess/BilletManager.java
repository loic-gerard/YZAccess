/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.sql.ResultSet;
/**
 *
 * @author lgerard
 */
public class BilletManager {
    public static void scanProcess(KDCCommunication kdc, GraphicPortCom interf, String code){
        
        Billet billet = new Billet(code, null);
        
        if(!billet.exists()){
            //BILLET INEXISTANT
            
            interf.setActivite("Billet "+code+" INEXISTANT");
            interf.setRed();
            interf.clearScreenWithDelay();
            if(kdc != null){
                kdc.kdcCmd_message("ERR1");
                kdc.kdcCmd_redLight();
                kdc.kdcCmd_errorBeep();
            }
            return;
        }
        
        if(!billet.isValide()){
            //BILLET INVALIDE
            interf.setActivite("Billet "+code+" INVALIDE");
            interf.setRed();
            interf.clearScreenWithDelay();
            billet.logActivity("BILLET INVALIDE");
            
            if(kdc != null){
                kdc.kdcCmd_message("ERR2");
                kdc.kdcCmd_redLight();
                kdc.kdcCmd_errorBeep();
            }
            return;
        }
        
        if(!billet.istypeValide()){
            //TYPE BILLET INVALIDE
            interf.setActivite("Type billet "+billet.getType()+" INVALIDE");
            interf.setRed();
            interf.clearScreenWithDelay();
            billet.logActivity("TYPE "+billet.getType()+" INVALIDE");
            
            if(kdc != null){
                kdc.kdcCmd_message("ERR3");
                kdc.kdcCmd_redLight();
                kdc.kdcCmd_errorBeep();
            }
            return;
        }
        
        //TODO : deja valide
        if(billet.isAlreadyValidated()){
            System.out.println("DEJA VALIDE");
            
            
            //TYPE BILLET DEJA VALIDE
            interf.setActivite("Billet "+code+" déjà validé \n\rle "+billet.getLastValidation());
            interf.setRed();
            interf.clearScreenWithDelay();
            billet.logActivity("Billet "+code+" déjà validé \n\rle "+billet.getLastValidation());
            
            if(kdc != null){
                kdc.kdcCmd_message("ERR3");
                kdc.kdcCmd_redLight();
                kdc.kdcCmd_errorBeep();
            }
            
            return;
        }
        
        interf.setActivite("Billet "+code+" VALIDE");
        interf.setGreen();
        interf.clearScreenWithDelay();
        billet.logActivity("OK");
        
        if(kdc != null){
            kdc.kdcCmd_message("OK !");
            kdc.kdcCmd_greenLight();
            kdc.kdcCmd_okBeep();
        }
        
    }
}
