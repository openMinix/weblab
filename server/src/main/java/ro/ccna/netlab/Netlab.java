package ro.ccna.netlab;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ro.ccna.netlab.models.Device;
import ro.ccna.netlab.models.DeviceGroup;
import ro.ccna.netlab.models.Room;

public class Netlab {

	private static EntityManager em;
	private static Server mainServer;
	private static List<Room> rooms;
	
	public static void main (String[] args) {
		new Netlab();
	}
	
	public static EntityManager getEntityManager () {
		return em;
	}
	
	private void connectDatabase () {
		
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("openjpa");
		em = factory.createEntityManager();
	}
	
	@SuppressWarnings("unchecked")
	private void loadRooms () {
		
		Query q = em.createQuery("SELECT r  FROM Room r");
		rooms = (List<Room>)q.getResultList();
	}
	
	public static List<Room> getRooms () {
		
		return rooms;
	}
	
	private void connectDevices () {
		
		for (Room room : rooms) {
			
			for (DeviceGroup group : room.getGroups()) {
				
				for (Device device : group.getDevices()) {
					
					// Gets if device is enabled
					if (device.getStatus() == 1) {
						
						System.out.println("Connecting to TS " + device.getTsHost() + " on port " + device.getTsPort());
						
						// Connecting to device console
						Console console = new Console(device);
						console.connect();
						
						// Checks if connection was made
						if (console.isConnected()) {
							
							System.out.println("Connected to TS " + device.getTsHost() + " on port " + device.getTsPort());
						}
					}
				}
			}
		}
	}
	
	private void startMainServer () {
		
		int port = 8000;
		
		System.out.println("Starting main SSH server on port " + port);
		
		mainServer = new Server(port);
		mainServer.start();
		
		System.out.println("Main SSH server started on port " + port);
	}
	
	public Netlab () {
		
		connectDatabase();
		loadRooms();
		connectDevices();
		startMainServer();
	}
}
