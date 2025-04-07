package com.iqeq.service;

import com.iqeq.config.WebClientConfig;
import com.iqeq.dto.common.*;
import com.iqeq.dto.common.AuthenticationResponse;
import com.iqeq.exception.ClientErrorException;
import com.iqeq.exception.CustomException;
import com.iqeq.exception.ServerErrorException;
import com.iqeq.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final JwtService jwtService;

	private final WebClientConfig webClientConfig;

	private final UserService userService;

	@Value("${entitlement.base-url}")
	private String entBaseUrl;

	@Value("${entitlement.get-role-permissions}")
	private String fetchRolePermissionsByUserExtAppIdUrl;

	@Value("${iqeq.claims.token.expiry.hours}")
	private int claimsTokenExpiryHours;

	public void authenticate(String accessToken, AuthenticationResponse authenticationResponse)
			throws CustomException, SSLException {

		boolean isTokenExpired = jwtService.isTokenExpiredForOpenId(accessToken);
		if (isTokenExpired) {
			throw new TokenExpiredException("Token has expired");
		}

		UserDto userDto = getUserDetails(accessToken);
		String externalAppId = jwtService.extractAppId(accessToken);
		Map<String, Object> existingClaims = jwtService.getAllClaimsForOpenId(accessToken);
			authenticationResponse.setUser(userDto);
			authenticationResponse.setClaimsToken(jwtService.buildTokenOpenId(existingClaims, userDto.getWorkEmail(),
					 userDto.getId()));
	}

	private UserDto getUserDetails(String accessToken) throws CustomException {
	    String userEmail = jwtService.extractUserEmailForOpenId(accessToken);

	    UserDto userDto = userService.getUserByEmail(userEmail);
	    
	    if (Objects.nonNull(userDto)) {
	        return userDto;
	    }

	    String fullName = jwtService.extractUserNameForOpenId(accessToken);
	    String firstName = "";
	    String lastName = "";

	    if (fullName != null) {
	        
	        List<String> fullNameList = Arrays.stream(fullName.split(" ")).toList();
	        
	        if (fullNameList.size() == 1) {
	            firstName = fullNameList.get(0);
	            lastName = "";
	        } else if (fullNameList.size() > 1) {
	            firstName = fullNameList.get(0).trim();
	            lastName = fullName.replace(firstName, "").trim();
	        }
	    }

	    userDto = new UserDto();
	    userDto.setFirstName(firstName);
	    userDto.setLastName(lastName);
	    userDto.setWorkEmail(userEmail);

	    return userDto;
	}
	
	public ResponseEntity<Response> getRolePermissionsByUserAndExtApplication(String userEmail, String extApplicationId) throws SSLException {
	    String getRolePermUrl = entBaseUrl
	        .concat(fetchRolePermissionsByUserExtAppIdUrl.replace("{userEmail}", userEmail))
	        .replace("{extApplicationId}", extApplicationId);

	    return webClientConfig.webClient()
	        .get()
	        .uri(getRolePermUrl)
	        .retrieve()
	        .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
	            Mono.error(new ClientErrorException("An error occurred while fetching role permissions " + clientResponse.bodyToMono(String.class)))
	        )
	        .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
	            Mono.error(new ServerErrorException("Server error occurred while fetching role permissions " + clientResponse.bodyToMono(String.class)))
	        )
	        .toEntity(Response.class)
	        .block();
	}

	private boolean isExpiryDateNotToday(Date expirationTime) {
	    LocalDate localDate = LocalDate.now();
	    LocalDateTime startTime = localDate.atStartOfDay();
	    LocalDateTime endTime = localDate.atTime(LocalTime.MAX);
	    LocalDateTime expTime = expirationTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    return !((expTime.equals(startTime)) || expTime.isAfter(startTime) && expTime.isBefore(endTime));
	}

	private Date add24HrsToCurDate() {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
	    calendar.setTime(new Date());
	    calendar.add(Calendar.HOUR_OF_DAY, claimsTokenExpiryHours);
	    return calendar.getTime();
	}

	private Date getTodayEndTime() {
	    Calendar today = Calendar.getInstance();
	    today.setTimeZone(TimeZone.getTimeZone("UTC"));
	    today.set(Calendar.HOUR_OF_DAY, 23);
	    today.set(Calendar.MINUTE, 59);
	    today.set(Calendar.SECOND, 59);
	    today.set(Calendar.MILLISECOND, 999);
	    return today.getTime();
	}



}
