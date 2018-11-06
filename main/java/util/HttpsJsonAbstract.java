package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class HttpsJsonAbstract {
	private final URL url;
	protected HttpsURLConnection con;
	private JSONObject json;
	
	public HttpsJsonAbstract(String ip) throws MalformedURLException {
		url = new URL("https://"+ip);
	}
	
	public void setJSON(JSONObject json) {this.json = json;}
	
	public String connect() throws IOException, JSONException {
		StringBuilder sb = new StringBuilder();  
		con = (HttpsURLConnection) url.openConnection();
		
		OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		
		int HttpResult = con.getResponseCode(); 
		if (HttpResult == HttpsURLConnection.HTTP_OK) {
		    BufferedReader br = new BufferedReader(
		            new InputStreamReader(con.getInputStream(), "utf-8"));
		    String line = null;  
		    while ((line = br.readLine()) != null) {  
		        sb.append(line + "\n");  
		    }
		    br.close();
		    return sb.toString();  
		} else {
		    return con.getResponseMessage();  
		} 
	}
}
