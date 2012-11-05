package ro.ccna.netlab.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="devices_models") 
public class DeviceModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "url")
	private String url;
	
	@OneToMany(targetEntity=ro.ccna.netlab.models.Device.class, mappedBy="model")
	private List<Device> devices;
	
	@ManyToOne(targetEntity=ro.ccna.netlab.models.DeviceType.class)
	private DeviceType type;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DeviceType getType() {
		return type;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setType(DeviceType type) {
		this.type = type;
	}
	
	public List<Device> getDevices() {
		return devices;
	}
}
