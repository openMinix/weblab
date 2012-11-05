package ro.ccna.netlab.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private int id;
	
	@Column(name = "type")
    private int type;
	
	@Column(name = "username")
    private String username;
	
	@Column(name = "password")
    private String password;
	
	@Column(name = "first_name")
    private String first_name;
	
	@Column(name = "last_name")
    private String last_name;
	
	@Column(name = "email")
    private String email;
	
	@Column(name = "session_id")
    private String session_id;
	
	@OneToMany(targetEntity=ro.ccna.netlab.models.UserPublicKey.class, mappedBy="user", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
	private List<UserPublicKey> public_keys;
	
	@Column(name = "created")
    private Date created;
	
	public int getId() {
        return id;
    }
    
	public int getType() {
        return type;
    }
    
	public void setType(int type) {
        this.type = type;
    }
   
    public String getUsername() {
    	return username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
	public String getEmail() {
        return email;
    }
    
	public void setEmail(String email) {
        this.email = email;
    }
    
	public String getName() {
        return first_name + ' ' +  last_name;
    }
    
	public String getFirstName() {
        return first_name;
    }
    
	public void setFirstName(String firstName) {
        this.first_name = firstName;
    }
	
	public String getSessionId() {
        return session_id;
    }
    
	public void setSessionId(String sessionId) {
        this.session_id = sessionId;
    }
    
	public String getLastName() {
        return last_name;
    }
    
	public void setLastName(String lastName) {
        this.last_name = lastName;
    }
	
	public Date getCreationDate () {
		return created;
	}
	
	public List<UserPublicKey> getPublicKeys () {
		return public_keys;
	}
    
    public String getRole() {
    	
    	int userType = getType();
    	
    	switch (userType) {
    		case 0:
    			return "inactive";
    		case 1:
    			return "member";
    	}
    	
    	return "guest";
    }
}
