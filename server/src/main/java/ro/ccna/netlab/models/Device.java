package ro.ccna.netlab.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ro.ccna.netlab.Console;
import ro.ccna.netlab.Server;


@Entity
@Table(name="devices") 
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(targetEntity=ro.ccna.netlab.models.DeviceType.class)
	private DeviceType type;
	
	@ManyToOne(targetEntity=ro.ccna.netlab.models.DeviceModel.class) 
	private DeviceModel model;
	
	@Column(name = "ts_host")
	private String ts_host;
	
	@Column(name = "ts_port")
	private int ts_port;
	
	@Column(name = "status")
	private int status;
	
	@ManyToOne(targetEntity=ro.ccna.netlab.models.DeviceGroup.class) 
	private DeviceGroup group;
	
	@Transient
	private Console console;
	
	@Transient
	private Server server;
	
	public int getId() {
		return id;
	}
	
	public Console getConsole() {
		return console;
	}
	
	public void setConsole(Console console) {
		this.console = console;
	}
	
	public Server getServer() {
		return server;
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DeviceGroup getGroup() {
		return group;
	}
	
	public void setGroup(DeviceGroup group) {
		this.group = group;
	}
	
	public DeviceModel getModel() {
		return model;
	}
	
	public void setModel(DeviceModel model) {
		this.model = model;
	}
	
	public DeviceType getType() {
		return type;
	}
	
	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getTsHost() {
		return ts_host;
	}
	
	public void setTsHost(String tsHost) {
		this.ts_host = tsHost;
	}
	
	public int getTsPort() {
		return ts_port;
	}
	
	public void setTsPort(int tsPort) {
		this.ts_port = tsPort;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}