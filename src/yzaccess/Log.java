/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import java.util.Date;

public class Log {
    public static void log(String message){
	Date date = new Date();
	message = date.toString()+" : "+message;
	
	if(Config.isVerbose()){
	    System.out.println(message);
	}
	if(YZAccess.interf != null){
	    YZAccess.interf.writeInLog(message);
	}
    }
}
