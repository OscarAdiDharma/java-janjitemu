package com.janjitemu.app.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseToken(request);
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                Claims claims = jwtUtils.getClaimsFromToken(token);
                String id = claims.get("id", String.class);
                String role = claims.get("role", String.class);
                String name = claims.get("name", String.class);

                if (role == null) {
                    role = "customer";
                }

                String roleAuthority = "ROLE_" + role.toUpperCase();
                UserPrincipal principal = new UserPrincipal(
                        id,
                        name,
                        claims.getSubject(),
                        "",
                        role,
                        Collections.singletonList(new SimpleGrantedAuthority(roleAuthority))
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth)) {
            if (headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7).trim();
            }
            return headerAuth.trim();
        }

        return null;
    }
}
