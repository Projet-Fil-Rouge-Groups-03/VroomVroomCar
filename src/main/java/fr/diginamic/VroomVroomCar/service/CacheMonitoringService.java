package fr.diginamic.VroomVroomCar.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Service pour monitorer les statistiques du cache Caffeine
 * afin d'assurer son fonctionnement.
 */
@Service
@RequiredArgsConstructor
public class CacheMonitoringService {

    private final CacheManager cacheManager;

    /**
     * Récupère les statistiques du cache des routes
     */
    public Map<String, Object> getRoutesCacheStats() {
        return getCacheStats("routes");
    }

    /**
     * Récupère les statistiques du cache des coordonnées
     */
    public Map<String, Object> getCoordinatesCacheStats() {
        return getCacheStats("coordinates");
    }

    /**
     * Récupère les statistiques d'un cache spécifique
     */
    private Map<String, Object> getCacheStats(String cacheName) {
        Map<String, Object> stats = new HashMap<>();

        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache instanceof CaffeineCache) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();

            CacheStats cacheStats = nativeCache.stats();

            stats.put("hitCount", cacheStats.hitCount());
            stats.put("missCount", cacheStats.missCount());
            stats.put("hitRate", String.format("%.2f%%", cacheStats.hitRate() * 100));
            stats.put("missRate", String.format("%.2f%%", cacheStats.missRate() * 100));
            stats.put("estimatedSize", nativeCache.estimatedSize());
            stats.put("evictionCount", cacheStats.evictionCount());
            stats.put("averageLoadTime", String.format("%.2f ms", cacheStats.totalLoadTime() / 1_000_000.0));
        }

        return stats;
    }

    /**
     * Affiche les statistiques dans la console
     */
    public void printCacheStats() {
        System.out.println("=== STATISTIQUES DU CACHE ===");

        Map<String, Object> routesStats = getRoutesCacheStats();
        System.out.println("Cache Routes:");
        routesStats.forEach((key, value) -> System.out.println("  " + key + ": " + value));

        Map<String, Object> coordsStats = getCoordinatesCacheStats();
        System.out.println("Cache Coordonnées:");
        coordsStats.forEach((key, value) -> System.out.println("  " + key + ": " + value));

        System.out.println("=================================\n");
    }
}
