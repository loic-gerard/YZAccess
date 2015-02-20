/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

/**
 *
 * @author lgerard
 */
public class KDCCommand {
    private String command;
    private String response;
    private boolean called = false;
    private boolean returned = false;
    private boolean error = false;
    private TimelapsThread ctrlThread;
    private KDCCommunication com;
    private int retryCount = 0;
    
    public KDCCommand(String cmd, KDCCommunication in_com){
	command = cmd;
	com = in_com;
    }
    
    public void setCalled(){
	called = true;
	if(command == "W"){
	    ctrlThread = new TimelapsThread(Config.getCmdRetryTime(), this, "controlExecution");
	}
    }
    
    public void controlExecution(){
	if(called && !returned && ctrlThread != null){
	    retryCount++;
	    
	    
	    if(retryCount == Config.getCmdRetryCount()){
		com.cmdTimeout();
	    }else{
		ctrlThread = null;
		com.sendNext();
	    }
	}
    }
    
    public String getCommand(){
	return command;
    }
    
    public void setResponse(String in_response){
	if(ctrlThread != null){
	    ctrlThread.interrupt();
	}
	returned = true;
	called = true;
	response = in_response;
	
    }
    
    public void setError(){
	if(ctrlThread != null){
	    ctrlThread.interrupt();
	}
	returned = true;
	error = true;
    }
}
