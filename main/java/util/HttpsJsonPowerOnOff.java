package util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpsJsonPowerOnOff extends HttpsJsonAbstract {

	private String onoff;
	
	public HttpsJsonPowerOnOff(String ip, String onoff) throws MalformedURLException {
		super(ip);
		this.onoff = onoff;
	}
	
	public String connect() throws IOException, JSONException {
		JSONObject json = new JSONObject();
		
		//try {
		json.put("power", this.onoff);
		//}catch(JSONException e) {}
		
		super.setJSON(json);

		return super.connect();
	}
}
