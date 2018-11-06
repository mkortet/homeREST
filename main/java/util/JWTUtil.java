package util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import model.Device;
import model.Person;

public class JWTUtil {
	
	private Device d = null;
	private Person p = null;
	private String token;
	
	@Value("${jwt.password}")
	private String pass;

	
	public JWTUtil(Device d) {
		this.d = d;
	}
	
	public JWTUtil(Person p) {
		this.p = p;
	}
	
	public String createToken() {
		
		if (p == null && d == null)
			return "";
		
		if (p != null) {
			Map<String, Object> headerClaims = new HashMap<>();
			headerClaims.put("alg", "HS256");
			headerClaims.put("typ", "JWT");
			
			this.token = JWT.create()
						.withHeader(headerClaims)
						.withClaim("userName", p.getUserName())
						.withClaim("admin", p.getAdmin())
						//.withIssuedAt(new Date())
						.sign(Algorithm.HMAC256(pass));
			return this.token;
		}
		else {
			Map<String, Object> headerClaims = new HashMap<>();
			headerClaims.put("alg", "HS256");
			headerClaims.put("typ", "JWT");
			
			this.token = JWT.create()
					.withHeader(headerClaims)
					.withClaim("name", d.getName())
					.withClaim("type", d.getType().toString())
					.sign(Algorithm.HMAC256(pass));
			
			return this.token;
		}
	}
}
