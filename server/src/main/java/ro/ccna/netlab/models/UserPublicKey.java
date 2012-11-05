package ro.ccna.netlab.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="users_public_keys")
public class UserPublicKey {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private int id;
	
	@Column(name="pkey", length = 8000, columnDefinition="TEXT")
    private String pkey;
	
	@ManyToOne(targetEntity=ro.ccna.netlab.models.User.class) 
	@JoinColumn(name="user_id")
    private User user;
	
	public int getId() {
        return id;
    }
    
	public String getKey() {
        return pkey;
    }
    
	public void setKey(String key) {
        this.pkey = key;
    }
   
    public User getUser() {
    	return user;
    }
    
    public void setUser(User user) {
    	this.user = user;
    }
}
