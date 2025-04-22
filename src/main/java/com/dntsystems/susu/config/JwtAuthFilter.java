package com.dntsystems.susu.config;

import java.io.IOException;
import java.util.List;

import com.dntsystems.susu.service.CustomUserDetailsService;
import com.dntsystems.susu.service.JwtService;
import com.dntsystems.susu.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> OPEN_API_ENDPOINTS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/api/swagger-config",
            "/api",
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String jwtToken;
            final String username;

            String requestURI = request.getRequestURI();
            // Bypass JWT authentication for Swagger and public API end points
            if (OPEN_API_ENDPOINTS.stream().anyMatch(endpoint -> pathMatcher.match(endpoint, requestURI))) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            // Check if token exists and starts with Bearer
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtToken = authHeader.substring(7);
            username = jwtService.extractUsername(jwtToken);

            System.out.println("Token: "+jwtToken);
            System.out.println("Username: "+username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.validateAccessToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            handleJwtException(response, "Token expired! Please log in again.");
        } catch (MalformedJwtException | SignatureException ex) {
            handleJwtException(response, "Invalid token!");
        } catch (Exception ex) {
            handleJwtException(response, "Authentication error: " + ex.getMessage());
        }
    }

    private void handleJwtException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        ApiResponse<?> errorResponse = new ApiResponse<>(false, message, null);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
