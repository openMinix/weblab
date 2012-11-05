package ro.ccna.netlab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import ro.ccna.netlab.models.Device;
import ro.ccna.netlab.models.DeviceGroup;
import ro.ccna.netlab.models.Room;


public class ServerShellFactory implements Factory<Command> {

	private Console console;
	
	public ServerShellFactory () {
		
	}
	
	public ServerShellFactory (Console console) {
		this.console = console;
	}
	
    public Command create() {
        return new ServerShell(console);
    }

    /**
     * Shell class
     * @author robert
     */
    protected static class ServerShell implements Command, Runnable {
    	
        private InputStream in;
        private OutputStream out;
        @SuppressWarnings("unused")
		private OutputStream err;
        private ExitCallback callback;
        private Thread thread;
        private Console console;
        
        public ServerShell (Console console) {
        	this.console = console;
        }
        
        /**
         * Sets the input stream
         */
        public void setInputStream(InputStream in) {
            this.in = in;
        }

        /**
         * Sets the output stream
         */
        public void setOutputStream(OutputStream out) {
            this.out = out;
        }

        /**
         * Sets the error stream
         */
        public void setErrorStream(OutputStream err) {
            this.err = err;
        }

        public void setExitCallback(ExitCallback callback) {
            this.callback = callback;
        }

        /**
         * Starts the reading in another thread
         */
        public void start(Environment env) {
        	
        	// Created a tread for reading
            thread = new Thread(this, "ServerShell");
            thread.start();
            
            connectConsole(console);
        }

        private void connectConsole (Console console) {
        	
        	// Adds client the to the console list of clients
        	if (console != null) {
        		
        		this.console = console;
        		console.addClient(this);
        		
        		writeln("Press Ctrl + S to return to selection mode");
				writeln("Connected to " + console.getDevice().getGroup().getRoom().getName() + " - " + console.getDevice().getGroup().getName() + " - " + console.getDevice().getName());
        		
        		reportStatus();
        	}
        }
        
        private void disconnectConsole () {
        	
        	// Removes ssh client
        	if (console != null) {
        		
        		console.removeClient(this);
        		console = null;
        	}
        }
        
        /**
         * Send the connected users number
         */
        private void reportStatus () {
        	
        	int count = console.getClientsCount()-1;
    		
    		switch (count) {
    			case 0:
    				write("There are no other users conected");
    				break;
    			case 1:
    				write("There is 1 conected user");
    				break;
    			default:
    				writeln("There are " + count + " conected users");
    				break;
    		}
        }
        
        /**
         * Stops the thread
         */
        public void destroy() {
            
        	disconnectConsole();
        	
        	// Stops the thread
        	thread.interrupt();
        }

        /**
         * Sends a message to the ssh client
         * @param c
         */
        public void write(byte c) {
            
        	try {
				
            	out.write(c);
            	out.flush();
				
			} catch (IOException e) {
				
				System.err.println("Write in ssh server failed");
			}
        }
        
        /**
         * Sends a message to the ssh client
         * @param c
         */
        public void write(byte[] c) {
            
        	try {
				
            	out.write(c);
            	out.flush();
				
			} catch (IOException e) {
				
				System.err.println("Write in ssh server failed: " + e.getMessage());
			}
        }
        
        /**
         * Sends a message to the ssh client
         * @param c
         */
        public void write(String c) {
        	write(c.getBytes());
        }
        
        /**
         * Sends a message to the ssh client
         * @param c
         */
        public void writeln(String c) {
        	write((c+"\r\n").getBytes());
        }
        
        /**
         * Thread run
         */
        public void run() {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            try {
            	
                while (true) {
                    
                	// If console is not selected enters in selection mode
                	if (console != null) {
                		
	                	char c = (char)br.read();
	                	
	                	if (c == 0)
	                        return;
	                	
	                	if (c == 19)
	                		disconnectConsole();
	                	else
	                		console.write((byte)c);
	                	
                	} else {
                		
                		List<Room> rooms = Netlab.getRooms();
                		
                		writeln("Devices list:");
                		writeln("");
                		
                		int i = 0;
                		
                		for (Room room : rooms) {
                			
                			writeln(room.getName());
                			
                			for (DeviceGroup group : room.getGroups()) {
                				
                				writeln("\t" + group.getName());
                				
                				for (Device device : group.getDevices()) {
                					
                					if (device.getStatus() == 1) {
                						
                						i++;
                						writeln("\t\t[" + i + "] " + device.getName());
                					}
                				}
                				
                			}
                			
                		}
         
                		writeln("");
                		write("Select the device: ");
                		
                		String line = br.readLine();
                		
                		try {
                			
	                		int x = Integer.parseInt(line);
	                		
	                		if (x > 0 && x <= i) {
	                			
	                			i = 0;
	                			
	                			for (Room room : rooms) {
	                				
	                    			for (DeviceGroup group : room.getGroups()) {
	                    				
	                    				for (Device device : group.getDevices()) {
	                    					
	                    					if (device.getStatus() == 1) {
	                    						
		                    					i++;
		                    					
		                    					if (i == x) {
		                    						
		                    						writeln("");
		                    						connectConsole(device.getConsole());
		                    					}
	                    					}
	                    				}
	                    			}
	                			}
	                		}
	                		
                		} catch (Exception e) {
                			
                		}
                	}
                }
                
            } catch (Exception e) {
            	
            	System.err.println("SSH Server error occured: " + e.getMessage());
            	
            } finally {
            	
                callback.onExit(0);
            }
        }
    }
}
