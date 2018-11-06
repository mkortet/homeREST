package model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RGBDEVICE")
public class LightDevice extends Device {
	
	private String rValue = "0";
	private String gValue = "0";
	private String bValue = "0";
	
	public LightDevice(Device d) {
		super(Type.RGBLIGHT);
		this.setName(d.getName());
		this.setDescription(d.getDescription());
		this.setIpAddr(d.getIpAddr());
		this.setPower(d.getPower());
	}
	
	public LightDevice() {
		super(Type.RGBLIGHT);
	}

	public String getrValue() {
		return rValue;
	}

	public void setrValue(String rValue) {
		this.rValue = rValue;
	}

	public String getgValue() {
		return gValue;
	}

	public void setgValue(String gValue) {
		this.gValue = gValue;
	}

	public String getbValue() {
		return bValue;
	}

	public void setbValue(String bValue) {
		this.bValue = bValue;
	}
	
	public Device castToDevice() {
		Device d = new Device();
		
		d.setName(this.getName());
		d.setIpAddr(this.getIpAddr());
		d.setPower(this.getPower());
		d.setType(this.getType());
		d.setDescription(this.getDescription());
		
		return d;
	}
}
