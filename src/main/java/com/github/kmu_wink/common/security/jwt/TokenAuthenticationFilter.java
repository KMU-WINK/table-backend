package com.github.kmu_wink.common.security.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kmu_wink.common.api.ApiException;
import com.github.kmu_wink.common.api.ApiResponse;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = extractToken(request);

        try {
            if (accessToken != null && tokenProvider.validateToken(accessToken)) {

                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ApiException e) {
            handleException(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        return (authorization != null && authorization.startsWith("Bearer "))
            ? authorization.substring(7)
            : null;
    }

    private void handleException(HttpServletResponse response, ApiException e) throws IOException {

        ApiResponse<?> apiResponse = ApiResponse.error(e);

        String content = new ObjectMapper().writeValueAsString(apiResponse);

        response.addHeader("Content-Type", "application/json");
        response.getWriter().write(content);
        response.getWriter().flush();
    }
}
