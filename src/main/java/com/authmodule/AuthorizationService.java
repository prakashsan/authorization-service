package com.authmodule;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * This service performs user authorizations to perform actions on resources.
 */
@Service
public class AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    // in-memory map of users/actions/resources
    private final Map<String, Boolean> mockPermissions = Map.of(
            "123:read:document-456", true,
            "123:write:document-456", false
    );

    private final MeterRegistry meterRegistry;

    public AuthorizationService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // using thread pool defined in com.authmodule.AppConfig
    @Async("taskExecutor")
    public CompletableFuture<Boolean> isAuthorized(String userId, String action, String resource) {

        String key = userId + ":" + action + ":" + resource;

        // Simulate delay for timeout testing
        // AuthorizationController sets timeout to 2 seconds; when this hardcoded
        // value > 2000, controller will time out
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        boolean result = mockPermissions.getOrDefault(key, false);
        log.info("Request: {}, result: {}", key, result);

        if (result) {
            meterRegistry.counter("authorize.requests.success").increment();
        } else {
            meterRegistry.counter("authorize.requests.denied").increment();
        }

        return CompletableFuture.completedFuture(result);
    }
}