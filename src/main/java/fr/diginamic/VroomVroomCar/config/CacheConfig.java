package fr.diginamic.VroomVroomCar.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        // Cache pour les routes : 6h, 1000 entrées max
        CaffeineCache routesCache = new CaffeineCache("routes",
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(6, TimeUnit.HOURS)
                        .recordStats()
                        .build()
        );

        // Cache pour les coordonnées : 24h, 2000 entrées max
        CaffeineCache coordinatesCache = new CaffeineCache("coordinates",
                Caffeine.newBuilder()
                        .maximumSize(2000)
                        .expireAfterWrite(24, TimeUnit.HOURS)
                        .recordStats()
                        .build()
        );

        cacheManager.setCaches(Arrays.asList(routesCache, coordinatesCache));
        return cacheManager;
    }
}