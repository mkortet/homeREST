package config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtAdminFilter extends GenericFilterBean {
	
	@Value("${server.ssl.key-store-password}")
	private String keyStorePwd;
	
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String authHeader = request.getHeader("authorization");

		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);

			chain.doFilter(req, res);
		} else {

			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new ServletException("Missing or invalid Authorization header");
			}

			final String token = authHeader.substring(7);

			DecodedJWT jwt;
			try {
				jwt = JWT.require(Algorithm.HMAC256(keyStorePwd)).build().verify(token);
			} catch (SignatureVerificationException e) {
				throw new ServletException("Invalid token");
			}
			
			System.out.println(jwt.getClaim("admin").asString());
			if (!jwt.getClaim("admin").asString().matches("true"))
				throw new ServletException("Invalid user");

			chain.doFilter(req, res);
		}
	}
}
