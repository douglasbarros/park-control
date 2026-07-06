package com.dbsinfosolutions.parkcontrol.infrastructure.configuration;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestBodyCachingFilter extends OncePerRequestFilter {

    private static final int REQUEST_BODY_CACHE_LIMIT = 10_240;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request, REQUEST_BODY_CACHE_LIMIT);
        filterChain.doFilter(wrappedRequest, response);
    }
}
