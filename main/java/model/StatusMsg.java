package model;

public class StatusMsg {
	
	String status;
	String message;
	
	public StatusMsg() {}
	
	public static StatusMsg nok(String err) {
		StatusMsg sm = new StatusMsg();
		
		sm.status = "NOK";
		sm.message = err;
		
		return sm;
	}
	public static StatusMsg ok() {
		StatusMsg sm = new StatusMsg();
		
		sm.status = "OK";
		sm.message = "null";
		
		return sm;
	}
	public static StatusMsg ok(String msg) {
		StatusMsg sm = new StatusMsg();
		
		sm.status = "OK";
		sm.message = msg;
		
		return sm;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String msg) {
		this.message = msg;
	}
}
