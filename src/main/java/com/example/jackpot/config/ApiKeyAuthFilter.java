package com.example.jackpot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final List<String> validKeys;
    private final List<String> openPaths = Arrays.asList(
            "/swagger-ui.html",
            "/swagger-ui",
            "/swagger-ui/",
            "/swagger-ui/index.html",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/",
            "/v3/api-docs/swagger-config",
            "/h2-console",
            "/h2-console/"
    );

    public ApiKeyAuthFilter(@Value("${api.keys.valid}") List<String> keys) {
        this.validKeys = keys;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (openPaths.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getHeader(API_KEY_HEADER);
        if (key == null || !validKeys.contains(key)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
