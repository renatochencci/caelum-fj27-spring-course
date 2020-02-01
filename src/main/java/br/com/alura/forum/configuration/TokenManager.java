package br.com.alura.forum.configuration;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.alura.forum.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenManager {

	@Value("${alura.forum.jwt.secret}")
	private String secret;
	
	@Value("${alura.forum.jwt.expiration}")
	private long expirationInMillis;
	
	public String GenerateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		final Date now = new Date();
		final Date expiration = new Date(now.getTime() + this.expirationInMillis);
		return Jwts.builder()
				.setIssuer("Alura forum API")
				.setSubject(Long.toString(user.getId()))
				.setIssuedAt(now)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, this.secret)
				.compact();
	}
	
	public boolean isValid(String jwt) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
	
	public Long getUserIdFromToken (String jwt) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt).getBody();
//		String userId = claims.getSubject();
		return Long.parseLong(claims.getSubject());
	}
}







