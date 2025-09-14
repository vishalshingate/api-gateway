package com.example.apigateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Fallback endpoints used by circuit breakers when downstream services are unavailable or too slow.
 *
 * <p>These lightweight responses prevent propagating errors to clients and demonstrate the
 * circuit breaker 'open' / 'fallback' behavior.</p>
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping(value = "/currency-exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> currencyExchangeFallback() {
        log.warn("Fallback triggered for currency-exchange-service");
        return Mono.just("{\"message\":\"Currency Exchange Service temporarily unavailable. Please retry later.\"}");
    }

    @GetMapping(value = "/currency-conversion", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> currencyConversionFallback() {
        log.warn("Fallback triggered for currency-conversion-service");
        return Mono.just("{\"message\":\"Currency Conversion Service temporarily unavailable. Please retry later.\"}");
    }
}

