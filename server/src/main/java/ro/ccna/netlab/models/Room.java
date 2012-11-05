package ro.ccna.netlab.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;

@Entity
@Table(name="rooms") 
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(targetEntity=ro.ccna.netlab.models.DeviceGroup.class, mappedBy="room") 
	private List<DeviceGroup> groups;
	
	public int getId() {
		return id;
	}
	
	public List<DeviceGroup> getGroups() {
		return groups;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}