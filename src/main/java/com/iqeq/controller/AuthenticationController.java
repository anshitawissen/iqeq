package com.iqeq.controller;

import com.iqeq.dto.common.AuthenticationResponse;
import com.iqeq.exception.CustomException;
import com.iqeq.service.OpenIdConfigurationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Data
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final OpenIdConfigurationService openIdConfigurationService;

    @PostMapping("/authenticate")
    public AuthenticationResponse getAccessToken(@RequestBody Map<String, String> body, HttpServletResponse response) 
            throws IOException, CustomException {
        AuthenticationResponse authenticationResponse = openIdConfigurationService.getToken(body);
        response.addCookie(createCookie(authenticationResponse.getRefreshToken()));
        return authenticationResponse;
    }

    @PostMapping("/refreshToken")
    public AuthenticationResponse refreshToken(@CookieValue(value = "refreshToken") String refreshToken, 
                                               HttpServletResponse response) 
            throws IOException, CustomException {
        AuthenticationResponse authenticationResponse = openIdConfigurationService.getRefreshTokenResponse(refreshToken);
        response.addCookie(createCookie(authenticationResponse.getRefreshToken()));
        
        // Empty refresh token
        authenticationResponse.setRefreshToken("");
        return authenticationResponse;
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
}

