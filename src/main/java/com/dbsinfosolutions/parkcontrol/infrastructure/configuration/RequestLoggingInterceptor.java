package com.dbsinfosolutions.parkcontrol.infrastructure.configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
    private static final String START_TIME_ATTRIBUTE =
            RequestLoggingInterceptor.class.getName() + ".startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.nanoTime());
        log.info("Incoming request method={} uri={} query={} remote={}", request.getMethod(),
                request.getRequestURI(), request.getQueryString(), request.getRemoteAddr());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long durationMs = startTime == null ? -1L
                : Duration.ofNanos(System.nanoTime() - startTime).toMillis();
        String payload = extractPayload(request);
        if (ex == null) {
            log.info("Completed request method={} uri={} status={} durationMs={} payload={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs,
                    payload);
        } else {
            log.warn("Failed request method={} uri={} status={} durationMs={} payload={} error={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), durationMs,
                    payload, ex.getMessage());
        }
    }

    private String extractPayload(HttpServletRequest request) {
        if (!(request instanceof ContentCachingRequestWrapper wrappedRequest)) {
            return "<unavailable>";
        }

        byte[] content = wrappedRequest.getContentAsByteArray();
        if (content.length == 0) {
            return "<empty>";
        }

        Charset charset = request.getCharacterEncoding() == null ? StandardCharsets.UTF_8
                : Charset.forName(request.getCharacterEncoding());
        String payload = new String(content, charset);
        return payload.length() > 2000 ? payload.substring(0, 2000) + "..." : payload;
    }
}
