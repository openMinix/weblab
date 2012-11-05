package ro.ccna.netlab;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TelnetCommand;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.apache.commons.net.telnet.SuppressGAOptionHandler;
import org.apache.commons.net.telnet.InvalidTelnetOptionException;

import ro.ccna.netlab.ServerShellFactory.ServerShell;
import ro.ccna.netlab.models.Device;


public class Console implements Runnable
{
    private TelnetClient tc = null;
    private String host;
    private int port;
    private OutputStream outstr;
    private ArrayList<ServerShell> clients = new ArrayList<ServerShell>();
    private Device device;
    private int connectErrors = 0;
    private int allowedConnectErrors = 20;

    public Console (Device device)
    {
    	this.device = device;
    	this.host = device.getTsHost();
    	this.port = device.getTsPort();
    	this.device.setConsole(this);
    }
    
    public void addClient (ServerShell client) {
    	if (!clients.contains(client))
    		clients.add(client);
    }
    
    public Device getDevice () {
    	return device;
    }
    
    public void removeClient (ServerShell client) {
    	clients.remove(client);
    }
    
    public int getClientsCount () {
    	return clients.size();
    }
    
    public void tryReconnect () {
    	
    	if (connectErrors < allowedConnectErrors) {
    		
    		if (!connect()) 
    			connectErrors++;
    	}
    }
    
    public boolean isConnected () {
    	
    	if (tc == null)
    		return false;
    	
    	return tc.isConnected();
    }
    
    public boolean connect () {

    	if (!isConnected()) {
    		
	        tc = new TelnetClient();
	        
	        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
	        EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
	        SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);
	
	        try
	        {
	            tc.addOptionHandler(ttopt);
	            tc.addOptionHandler(echoopt);
	            tc.addOptionHandler(gaopt);
	        }
	        catch (InvalidTelnetOptionException e)
	        {
	            System.err.println("Error registering option handlers: " + e.getMessage());
	            
	            return false;
	            
	        } catch (IOException e) {
				
	        	 System.err.println("Error registering option handlers: " + e.getMessage());
	        	 
	        	 return false;
			}
	
	        try
	        {
	            tc.connect(host, port);
	            outstr = tc.getOutputStream();
	            
	            Thread reader = new Thread (this);
	            reader.start();
	            
	            device.setConsole(this);
	        }
	        catch (IOException e)
	        {
	            System.err.println("Exception while connecting:" + e.getMessage());
	            
	            return false;
	        }
	        
	        return true;
    	}
  
    	return false;
    }
    
    public void disconnect () {
    	try
        {
    		if (isConnected())
    			tc.disconnect();
        }
        catch (IOException e)
        {
            System.err.println("Exception while connecting:" + e.getMessage());
        }
    }

    public void write (byte[] command, int length) {
    	
    	write(new String(command, 0, length));
    }
    
    public void write (byte[] command) {
    	
    	write(new String(command, 0, command.length));
    }
    
    public void write (String command) {
    	
	        try
	        {
	        	if (!isConnected())
	        		tryReconnect();
	        	
	        	if (isConnected()) {

	        		outstr.write(command.getBytes());
	        		outstr.flush();
	        		tc.sendCommand((byte) TelnetCommand.EOF);
	        	}
	        }
	        catch (IOException e)
	        {
	             System.err.println("Write in console failed: " + e.getMessage());
	        }
    }
    
    public void write (byte x) {
    	byte[] y = new byte[1];
    	y[0] = x;
    	write(y);
    }

    /**
     * Reader thread.
     **/
    public void run()
    {
        InputStream instr = tc.getInputStream();

        try
        {
        	byte b = 1;
        	
            do {
            	
            	if (isConnected()) {
            	
	                b = (byte) instr.read();
	                
	                for (ServerShell client : clients)
	                	client.write(b);
	                
            	}
            	
            } while (b >= 0);
        }
        catch (IOException e)
        {
            System.err.println("Exception while reading socket:" + e.getMessage());
        }

        try
        {
            tc.disconnect();
        }
        catch (IOException e)
        {
            System.err.println("Exception while closing telnet:" + e.getMessage());
        }
    }
}

