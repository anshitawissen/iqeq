package com.iqeq.service;

import com.iqeq.config.WebClientConfig;
import com.iqeq.dto.common.AuthenticationResponse;
import com.iqeq.dto.common.OpenIdResponseDto;
import com.iqeq.exception.ClientErrorException;
import com.iqeq.exception.CustomException;
import com.iqeq.exception.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OpenIdConfigurationService {
	@Value("${iqeq.security.oauth2.client.provider.azure.issuer-uri}")
	private String issuerUri;

	@Value("${iqeq.security.oauth2.client.registration.azure.tenant-id}")
	private String tenantId;

	@Value("${iqeq.security.oauth2.client.registration.azure.client-id}")
	private String clientId;

	@Value("${iqeq.security.oauth2.client.registration.azure.client-secret}")
	private String clientSecret;

	@Value("${iqeq.security.oauth2.client.redirect-url}")
	private String redirectUrl;

	private final WebClientConfig webC1ientConfig;

	private final AuthenticationService authenticationService;

	private static final String OPENID_CONFIGURATION_URL_TEMPLATE = ".well-known/openid-configuration";

	public AuthenticationResponse getToken(Map<String, String> body) throws SSLException, CustomException {
		String authCode = body.get("authorizationCode");
		String codeVerifier = body.get("codeVerifier");
		OpenIdResponseDto openIdConfDto = getTokenEndpoint();
		AuthenticationResponse authenticationResponse = getTokenResponse(authCode, codeVerifier,
				Objects.requireNonNull(openIdConfDto).getTokenEndpoint());
		return getAuthenticationResponse(Objects.requireNonNull(authenticationResponse));
	}

	private AuthenticationResponse getAuthenticationResponse(AuthenticationResponse authenticationResponse)
			throws CustomException, SSLException {
		authenticationService.authenticate(authenticationResponse.getAccessToken(), authenticationResponse);
		return authenticationResponse;
	}

	private OpenIdResponseDto getTokenEndpoint() throws SSLException {

		String tokenEndpoint = issuerUri + "/" + tenantId + "/v2.0/" + OPENID_CONFIGURATION_URL_TEMPLATE;

		return webC1ientConfig.webClient().get().uri(tokenEndpoint).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ClientErrorException(
						"An error occurred while fetching token endpoint " + clientResponse.bodyToMono(String.class))))
				.onStatus(HttpStatusCode::is5xxServerError,
						clientResponse -> Mono
								.error(new ServerErrorException("Server error occurred while fetching token endpoint "
										+ clientResponse.bodyToMono(String.class))))
				.bodyToMono(OpenIdResponseDto.class).block();
	}

	private AuthenticationResponse getTokenResponse(String authCode, String codeVerifier, String tokenEndpoint)
			throws SSLException {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("code", authCode);
		params.add("client_id", clientId);
		params.add("redirect_uri", redirectUrl);
		params.add("code_verifier", codeVerifier);
		params.add("client_secret", clientSecret);
		params.add("scope", "openid profile email offline_access");

		return webC1ientConfig.webClient().post().uri(tokenEndpoint).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(params)).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ClientErrorException(
						"An error occurred while fetching token response " + clientResponse.bodyToMono(String.class))))
				.onStatus(HttpStatusCode::is5xxServerError,
						clientResponse -> Mono
								.error(new ServerErrorException("Server error occurred while fetching token response "
										+ clientResponse.bodyToMono(String.class))))
				.bodyToMono(AuthenticationResponse.class).block();
	}

	public AuthenticationResponse getRefreshToken(String refreshToken) throws SSLException {
		OpenIdResponseDto openIdConfDto = getTokenEndpoint();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "refresh_token");
		params.add("refresh_token", refreshToken);
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("scope", "openid profile email offline_access");

		return webC1ientConfig.webClient().post().uri(Objects.requireNonNull(openIdConfDto).getTokenEndpoint())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData(params)).retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ClientErrorException(
						"An error occurred while refresh token response " + clientResponse.bodyToMono(String.class))))
				.onStatus(HttpStatusCode::is5xxServerError,
						clientResponse -> Mono
								.error(new ServerErrorException("Server error occurred while refresh token response"
										+ clientResponse.bodyToMono(String.class))))
				.bodyToMono(AuthenticationResponse.class).block();
	}

	public AuthenticationResponse getRefreshTokenResponse(String refreshToken) throws SSLException, CustomException {
		return getAuthenticationResponse(getRefreshToken(refreshToken));
	}

}
