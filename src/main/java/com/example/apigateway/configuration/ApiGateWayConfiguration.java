package com.example.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API Gateway configuration for routing requests to microservices.
 *
 * <p>
 * This class defines the routes for the API Gateway using Spring Cloud Gateway.
 *
 * <b>What is an API Gateway?</b>
 * <ul>
 *   <li>Acts as a single entry point for all client requests to your microservices.</li>
 *   <li>Handles cross-cutting concerns such as authentication, rate limiting, logging, and tracing.</li>
 *   <li>Routes requests to the appropriate backend service based on path or other criteria.</li>
 *   <li>Can perform request/response transformations, aggregation, and protocol translation.</li>
 * </ul>
 *
 * <b>What are Predicates?</b>
 * <ul>
 *   <li>Predicates are conditions that must be true for a route to match an incoming request.</li>
 *   <li>Examples: Path, Method, Host, Header, Query, etc.</li>
 *   <li>In this config, Path predicates are used to match request URLs.</li>
 * </ul>
 *
 * <b>What are Filters?</b>
 * <ul>
 *   <li>Filters are used to modify requests and responses, or to apply cross-cutting concerns.</li>
 *   <li>Examples: adding/removing headers, rate limiting, circuit breaking, logging, etc.</li>
 *   <li>Filters can be applied globally or per route.</li>
 * </ul>
 *
 * <b>Example Routes:</b>
 * <ul>
 *   <li><b>/currency-exchange/**</b> → currency-exchange-service</li>
 *   <li><b>/currency-conversion/**</b> → currency-conversion-service</li>
 *   <li><b>/currency-conversion-feign/**</b> → currency-conversion-service</li>
 * </ul>
 */
@Configuration
public class ApiGateWayConfiguration {
    /**
     * Defines the routes for the API Gateway.
     *
     * @param builder RouteLocatorBuilder
     * @return RouteLocator with configured routes
     */
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(p -> p.path("/currency-exchange/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("currencyExchangeCB").setFallbackUri("forward:/fallback/currency-exchange")))
                .uri("lb://currency-exchange-service"))
            .route(p -> p.path("/currency-conversion/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("currencyConversionCB").setFallbackUri("forward:/fallback/currency-conversion")))
                .uri("lb://currency-conversion-service"))
            .route(p -> p.path("/currency-conversion-feign/**")
                .filters(f -> f.circuitBreaker(c -> c.setName("currencyConversionCB").setFallbackUri("forward:/fallback/currency-conversion")))
                .uri("lb://currency-conversion-service"))
            .route(p->p.path("/currency-conversion-new/**").filters(f->f.rewritePath(
                "/currency-conversion-new/(?<segment>.*)",
                "/currency-conversion-feign/${segment}"))
            .uri("lb://currency-conversion-service"))
            .route(p->p.path("/currency-conversion-feign-circuit/**")
                .uri("lb://currency-conversion-service"))

            .build();
    }

}
