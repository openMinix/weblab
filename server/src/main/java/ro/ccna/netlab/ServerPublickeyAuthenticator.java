package ro.ccna.netlab;

import java.security.PublicKey;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import ro.ccna.netlab.models.User;
import ro.ccna.netlab.models.UserPublicKey;

public class ServerPublickeyAuthenticator implements PublickeyAuthenticator {

    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        
    	EntityManager em = Netlab.getEntityManager();

		Query q = em.createQuery("SELECT u  FROM User u WHERE u.username = '" + username + "'");
		
		if (q.getResultList().size() == 0) 
			return false;
		
		User user = (User)q.getSingleResult();
		
		AuthorizedKeysDecoder decoder = new AuthorizedKeysDecoder();
		PublicKey userKey;
		
		for (UserPublicKey k : user.getPublicKeys()) {
			
			try {
				
				userKey = decoder.decodePublicKey(k.getKey());
				
				if (key.equals(userKey))
					return true;
				
			} catch (Exception e) {
				
				return false;
			}
    	}
		
		return false;
    }
}
