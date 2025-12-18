package com.suivie_academique.suivie_academique.logging;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // Récupération IP (IPv4 prioritaire)
            String ip = getClientIp(httpRequest);
            MDC.put("ip", ip);

            // Utilisateur connecté
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                MDC.put("UserName", auth.getName());
            } else {
                MDC.put("UserName", "ANONYMOUS");
            }

            chain.doFilter(request, response);

        } finally {
            MDC.clear();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        // Proxy / Load Balancer
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }

        String ip = request.getRemoteAddr();

        // IPv6 localhost → IPv4 localhost
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }
}
