package ro.ccna.netlab;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

public class ServerPasswordAuthenticator  implements PasswordAuthenticator {

    public boolean authenticate(String username, String password, ServerSession session) {
    	
    	EntityManager em = Netlab.getEntityManager();
    	
		try {
			
			Query q;
			
			if (username == "session") {
				
				q = em.createQuery("SELECT u  FROM User u WHERE u.session_id = '" + password + "'");
				
			} else {
				
				password = md5(password);
				
				q = em.createQuery("SELECT u  FROM User u WHERE u.username = '" + username + "' AND u.password = '" + password + "'");
			}
			
			return (q.getResultList().size() > 0) ? true : false;
			
		} catch (NoSuchAlgorithmException e) {
			
		}
    	
		return false;
    }
    
    public static String md5 (String password) throws NoSuchAlgorithmException {
    	 
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
 
        byte byteData[] = md.digest();
 
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return sb.toString();
    }
}
