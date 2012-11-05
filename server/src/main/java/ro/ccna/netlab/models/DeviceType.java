package ro.ccna.netlab.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="devices_types") 
public class DeviceType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@OneToMany(targetEntity=ro.ccna.netlab.models.Device.class, mappedBy="type")
	private List<Device> devices;
	
	@OneToMany(targetEntity=ro.ccna.netlab.models.DeviceModel.class, mappedBy="type")
	private List<DeviceModel> models;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Device> getDevices() {
		return devices;
	}
	
	public List<DeviceModel> getModels() {
		return models;
	}
}