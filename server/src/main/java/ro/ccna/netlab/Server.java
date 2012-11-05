package ro.ccna.netlab;

import java.io.IOException;
import java.util.Map;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;


public class Server {

    private SshServer sshd;
    private int port;
    private Console console;

    public Server (int port) {
    	this.port = port;
    }
    
    public Server (int port, Console console) {
    	this.port = port;
    	this.console = console;
    }
    
    public void start () {
        
        try {
        	
        	sshd = SshServer.setUpDefaultServer();
            sshd.setPort(port);
            sshd.setKeyPairProvider(new FileKeyPairProvider(new String[] { "hostkey.pem" }));
            sshd.setShellFactory(new ServerShellFactory(console));
            sshd.setPasswordAuthenticator(new ServerPasswordAuthenticator());
            sshd.setPublickeyAuthenticator(new ServerPublickeyAuthenticator());
						
			Map<String, String> properties = sshd.getProperties();
			
			properties.put(SshServer.IDLE_TIMEOUT, "1200000");
			properties.put(SshServer.MAX_CONCURRENT_SESSIONS, "20");
			
			sshd.start();
			
		} catch (IOException e) {
			
			System.err.println();
			
		}
        
    }
}

