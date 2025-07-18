package com.authmodule;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Handles authorization requests
 */
@RestController
public class AuthorizationController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationController.class);

    private final AuthorizationService authService;

    public AuthorizationController(AuthorizationService authService) {
        this.authService = authService;
    }

    @PostMapping("/authorize")
    public DeferredResult<ResponseEntity<AuthorizationResponse>> authorize(
            @Valid @RequestBody AuthorizationRequest request) {

        log.info("Received authorization request: userId={}, action={}, resource={}",
                request.getUserId(), request.getAction(), request.getResource());

        // timeout set to 2 seconds when waiting for service response
        DeferredResult<ResponseEntity<AuthorizationResponse>> output = new DeferredResult<>(2000L);

        // if timed out, return HTTP 504
        output.onTimeout(() -> {
            log.warn("Request timed out");
            output.setResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(null));
        });

        // if error, return HTTP 500
        output.onError((Throwable t) -> {
            log.error("Request failed", t);
            output.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
        });

        // else, return result of authorization request
        // output is ignored if timeout/error (above) occurred
        authService.isAuthorized(request.getUserId(), request.getAction(), request.getResource())
                .thenAccept(result -> {
                    log.info("Authorization result: {}", result);
                    output.setResult(ResponseEntity.ok(new AuthorizationResponse(result)));
                });

        return output;
    }
}