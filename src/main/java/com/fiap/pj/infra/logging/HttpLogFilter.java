package com.fiap.pj.infra.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpLogFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        try {
            MDC.put("http.method", req.getMethod());
            MDC.put("http.path", req.getRequestURI());
            MDC.put("http.client_ip", req.getRemoteAddr());

            chain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }
}