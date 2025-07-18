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

/**
 * Filtre pour limiter le taux de requêtes entrantes.
 * Ce filtre est utilisé pour protéger les endpoints sensibles contre les abus.
 */
@Component
public class RateLimitingFilter implements Filter {

    /**
     * Map pour stocker les requêtes des clients avec leur timestamp.
     */
    private final Map<String, List<Long>> requestCounts = new ConcurrentHashMap<>();

    /**
     * Nombre maximum de requêtes autorisées par heure.
     */
    private final int MAX_REQUESTS = 100; // par heure

    /**
     * Méthode principale du filtre qui est appelée à chaque requête.
     *
     * @param request  La requête servlet.
     * @param response La réponse servlet.
     * @param chain    La chaîne de filtres.
     * @throws IOException      Si une erreur d'entrée/sortie se produit.
     * @throws ServletException Si une erreur de servlet se produit.
     */
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

    /**
     * Détermine si un endpoint doit être limité en taux de requêtes.
     *
     * @param path   Le chemin de la requête.
     * @param method La méthode HTTP utilisée.
     * @return true si le chemin et la méthode doivent être limités, false sinon.
     */
    private boolean shouldRateLimit(String path, String method) {
        return ("POST".equals(method) &&
                (path.equals("/login") || path.equals("/register")));
    }

    /**
     * Récupère l'adresse IP du client.
     *
     * @param request La requête HTTP.
     * @return L'adresse IP du client.
     */
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null) ? request.getRemoteAddr() : xfHeader.split(",")[0];
    }

    /**
     * Vérifie si un client a dépassé le taux de requêtes autorisées.
     *
     * @param clientIp L'adresse IP du client.
     * @return true si le client a dépassé le taux de requêtes autorisées, false sinon.
     */
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