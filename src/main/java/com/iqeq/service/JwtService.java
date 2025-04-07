package com.iqeq.service;

import com.nimbusds.jwt.JWTParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class JwtService {

	public String extractUserEmailForOpenId(String token) {
		Object userName = extractClaimForOpenId(token, claims -> claims.get("unique_name"));
		return userName.toString();
	}

	public String extractUserNameForOpenId(String token) {
		Object userName = extractClaimForOpenId(token, claims -> claims.get("name"));
		return userName.toString();
	}

	public String extractAppId(String token) {
		Object externalAppId = extractClaimForOpenId(token, claims -> claims.get("appid"));
		return externalAppId.toString();
	}

	public Map<String, Set<Long>> extractDocumentIds(String token) {
		return (Map<String, Set<Long>>) extractClaimForOpenId(token, claims -> claims.get("documentTypeIds"));
	}

	public <T> T extractClaimForOpenId(String token, Function<Map<String, Object>, T> claimsResolver) {
		final Map<String, Object> claims = getAllClaimsForOpenId(token);
		return claimsResolver.apply(claims);
	}

	public Map<String, Object> getAllClaimsForOpenId(String token) {
		try {
			return JWTParser.parse(token).getJWTClaimsSet().getClaims();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String buildTokenOpenId(Map<String, Object> extraClaims, String userEmail, Long userId) {

		return Jwts.builder().setClaims(extraClaims).claim("email", userEmail).claim("userId", userId)
				.setSubject(userEmail).setIssuedAt(new Date(System.currentTimeMillis()))
				// .signWith(getSignInKey(), SignatureAlgorithm.RS256)
				.compact();
	}

	public boolean isTokenExpiredForOpenId(String token) {
		return extractExpirationForOpenId(token).before(new Date());
	}

	private Date extractExpirationForOpenId(String token) {
		return (Date) extractClaimForOpenId(token, claims -> claims.get("exp"));
	}

}
