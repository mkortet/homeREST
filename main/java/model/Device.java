package model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;


@Entity
@Inheritance
@DiscriminatorColumn(name="DEV_TYPE")
@Table(name="DEVICE")
public class Device {
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING)
	public enum Type {
		RGBLIGHT,
		CONTROLDEV, // Device controller (controls other devices) 
		DEVICE; // ON-OFF type of device
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	
	// Assigned during registration
	private String name;
	private String description;
	
	// Assigned during login
	private String ipAddr;
	
	// Device control
	private String power;
	
	private Type type;
	
	// CONTROLDEVICE only. shows which device to control
	private String deviceToControl;
	
	// Default constructor needed because of @Requestbody
	public Device() {}
	
	// initializing the type is mandatory!
	protected Device(Type t) {this.type = t;}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDeviceToControl() {
		return deviceToControl;
	}

	public void setDeviceToControl(String deviceToControl) {
		this.deviceToControl = deviceToControl;
	}

}
