package com.collieReact.jwt;


import com.collieReact.dto.CustomUserDetails;
import com.collieReact.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if ("/api/join".equals(requestURI) || "/api/accounts/check-email".equals(requestURI) || requestURI.startsWith("/uploads/")) {
            System.out.println("Bypassing JWT filter for: " + requestURI); // Log the bypassing
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null or does not start with Bearer");
            filterChain.doFilter(request, response);

            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            // filterChain으로 방금 받은 요청, 응답 토스
            filterChain.doFilter(request, response);

            return;
        }

        String email = jwtUtil.getEmail(token);


        User user = new User();
        user.setEmail(email);
        user.setPassword("temppassword");

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken  = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}