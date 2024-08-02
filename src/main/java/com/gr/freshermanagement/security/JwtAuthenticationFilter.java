package com.gr.freshermanagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gr.freshermanagement.utils.ExceptionHandlerUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

    private final JWTUtility jwtUtility;
    private final CustomUserDetailsService userService;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        try {
            Cache cache = cacheManager.getCache("accessTokens");
            // Check for token in header
            String authorization = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Check if Header has Bearer and extract the token and get user from token
            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                username = jwtUtility.getUsernameFromToken(token);

                if (cache != null){
                    if (cache.get(token) == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is no longer valid");
                        return;
                    }
                }

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
            ExceptionHandlerUtils.handleException(response, ex);
        }
    }

}