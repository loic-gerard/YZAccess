/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yzaccess;

import javax.comm.CommPortIdentifier;
import java.util.Enumeration; 
import java.util.Vector;

/**
 *
 * @author lgerard
 */
public class GetAvailableComPorts {
    public static Vector getComPorts(){  
        String     port_type;  
        Enumeration  enu_ports  = CommPortIdentifier.getPortIdentifiers();  
	
	Vector ports = new Vector();
	
        while (enu_ports.hasMoreElements()) {  
            CommPortIdentifier port_identifier = (CommPortIdentifier) enu_ports.nextElement();  
  
            switch(port_identifier.getPortType()){  
                case CommPortIdentifier.PORT_SERIAL:
		    ports.add(port_identifier.getName());
                    port_type   =   "Serial";  
                    break;  
                default:  
                    port_type   =   "Unknown";  
                    break;  
            }  
        } 
	return ports;
    }  
}
