package com.iqeq.security;


import com.iqeq.exception.TokenExpiredException;
import com.iqeq.service.JwtService;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUserEmailForOpenId(jwt);

        authenticateToken(request, response, jwt, userEmail);
        filterChain.doFilter(request, response);
    }
    
    private void authenticateToken(HttpServletRequest request, HttpServletResponse response, String jwt, String userEmail) throws IOException {
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean isTokenExpired = jwtService.isTokenExpiredForOpenId(jwt);
            if (!isTokenExpired) {
                StringAuthenticationToken stringAuthenticationToken = new StringAuthenticationToken(jwt);
                stringAuthenticationToken.setAuthenticated(true);
                stringAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(stringAuthenticationToken);
            } else {
                try {
                    throw new TokenExpiredException("Token has expired");
                } catch (TokenExpiredException e) {
                    authenticationEntryPoint.commence(request, response, null);
                }
            }
        }
    }

}

