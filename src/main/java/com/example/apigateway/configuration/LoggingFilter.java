package com.example.apigateway.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * LoginFilter is a custom GlobalFilter for Spring Cloud Gateway.
 *
 * <p>
 * <b>What is a GlobalFilter?</b>
 * <ul>
 *   <li>GlobalFilters are applied to all requests passing through the API Gateway.</li>
 *   <li>They are useful for cross-cutting concerns like logging, authentication, tracing, etc.</li>
 * </ul>
 *
 * <b>What does this filter do?</b>
 * <ul>
 *   <li>Logs the path of every HTTP request received by the gateway.</li>
 *   <li>Demonstrates how to implement a custom filter for learning purposes.</li>
 * </ul>
 *
 * <b>How does it work?</b>
 * <ul>
 *   <li>The filter method is called for every request.</li>
 *   <li>It logs the request URI, then passes control to the next filter in the chain.</li>
 * </ul>
 *
 * <b>Where to use?</b>
 * <ul>
 *   <li>Use this as a template for adding your own logic (e.g., authentication, metrics, etc.).</li>
 * </ul>
 */
@Component
public class LoggingFilter implements GlobalFilter{
    private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Path of the request received -> {}",exchange.getRequest().getURI().toString());
        return chain.filter(exchange);
    }
}
