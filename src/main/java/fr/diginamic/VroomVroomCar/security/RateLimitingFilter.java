package fr.diginamic.VroomVroomCar.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter implements Filter {
    private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 100; // par heure

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // Limiter uniquement certains endpoints
        if (shouldRateLimit(path, httpRequest.getMethod())) {
            String clientIp = getClientIp(httpRequest);
            if (isRateLimited(clientIp)) {
                httpResponse.setStatus(429); // Too Many Requests
                httpResponse.getWriter().write("Trop de requêtes, réessayez plus tard.");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean shouldRateLimit(String path, String method) {
        return ("POST".equals(method) &&
                (path.equals("/login") || path.equals("/register")));
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null) ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }

    private boolean isRateLimited(String clientIp) {
        long now = System.currentTimeMillis();
        List<Long> requests = requestCounts.computeIfAbsent(clientIp, k -> new ArrayList<>());

        // Nettoyer les anciennes requêtes (> 1h)
        requests.removeIf(time -> now - time > 3600000);

        if (requests.size() >= MAX_REQUESTS) {
            return true;
        }

        requests.add(now);
        return false;
    }
}