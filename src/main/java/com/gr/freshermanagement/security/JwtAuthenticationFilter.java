package com.gr.freshermanagement.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.utils.ExceptionHandlerUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private CustomUserDetailsService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        try {
            // Check for token in header
            String authorization = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Check if Header has Bearer and extract the token and get user from token
            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                username = jwtUtility.getUsernameFromToken(token);
            }

            // Check if valid user or not
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(username);

                // Add this to security context
                if (jwtUtility.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            // Continue to the next filter
            filterChain.doFilter(request, response);
        } catch (Exception ex) {

            response.setStatus(401);
            ExceptionHandlerUtil.handleException(response, ex);
        }
    }

}