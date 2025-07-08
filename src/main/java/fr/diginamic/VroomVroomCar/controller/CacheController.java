package fr.diginamic.VroomVroomCar.controller;

import fr.diginamic.VroomVroomCar.service.CacheMonitoringService;
import fr.diginamic.VroomVroomCar.service.OpenRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controller pour gérer et monitorer le cache
 */
@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
public class CacheController {

    private final OpenRouteService openRouteService;
    private final CacheMonitoringService cacheMonitoringService;

    /**
     * Récupère les statistiques du cache des routes
     */
    @GetMapping("/stats/routes")
    public ResponseEntity<Map<String, Object>> getRoutesCacheStats() {
        return ResponseEntity.ok(cacheMonitoringService.getRoutesCacheStats());
    }

    /**
     * Récupère les statistiques du cache des coordonnées
     */
    @GetMapping("/stats/coordinates")
    public ResponseEntity<Map<String, Object>> getCoordinatesCacheStats() {
        return ResponseEntity.ok(cacheMonitoringService.getCoordinatesCacheStats());
    }

    /**
     * Vide le cache des routes
     */
    @DeleteMapping("/routes")
    public ResponseEntity<String> clearRoutesCache() {
        openRouteService.clearRoutesCache();
        return ResponseEntity.ok("Cache des routes vidé avec succès");
    }

    /**
     * Vide le cache des coordonnées
     */
    @DeleteMapping("/coordinates")
    public ResponseEntity<String> clearCoordinatesCache() {
        openRouteService.clearCoordinatesCache();
        return ResponseEntity.ok("Cache des coordonnées vidé avec succès");
    }

    /**
     * Vide tous les caches
     */
    @DeleteMapping("/all")
    public ResponseEntity<String> clearAllCaches() {
        openRouteService.clearAllCaches();
        return ResponseEntity.ok("Tous les caches vidés avec succès");
    }
}