package com.app.api_gateway.filter;

import com.app.api_gateway.Exception.Entity.Error;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpCookie;


@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    private final DiscoveryClient discoveryClient;

    private final WebClient.Builder webClientBuilder;

    public JwtValidationFilter(WebClient.Builder webClientBuilder,DiscoveryClient discoveryClient) {
        super(Config.class);
        this.discoveryClient = discoveryClient;
        this.webClientBuilder = webClientBuilder;
    }

    public static class Config {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Retrieve the JWT cookie
            HttpCookie cookie = exchange.getRequest().getCookies().getFirst("JWT");

            // If the cookie is present, make the API call to validate the token
            if (cookie != null) {
                String token = cookie.getValue();  // Retrieve the value of the JWT cookie

                var serviceUrl = discoveryClient.getInstances("loginService").getFirst().getUri().toString();

                return webClientBuilder.build()
                        .get()
                        .uri(serviceUrl+"/api/auth/validate")
                        .cookie("JWT",token)
                        .retrieve()
                        .bodyToMono(String.class)  // Assuming the response body is a string
                        .doOnTerminate(() -> {
                            // After the call is complete, you can add any logic you want
                            System.out.println("API call finished");
                        })
                        .flatMap(response -> {
                            // You can use the response to modify the exchange or headers if needed
                            System.out.println("API response: " + response);
                            // If the token is valid, proceed with the request
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> {
                            // Handle errors, e.g., if the API call fails or the token is invalid
                            Error errorResponse = new Error(HttpStatus.UNAUTHORIZED.value(),"Invalid JWT token");
                            return sendErrorResponse(exchange, errorResponse, HttpStatus.UNAUTHORIZED);
//                            System.err.println("JWT validation failed: " + e.getMessage());
                            // Optionally, return an error response or stop the request flow
//                            return Mono.error(new RuntimeException("Invalid JWT"));
                        });
            } else {
                // If no JWT cookie is present, reject the request or pass it along based on your logic
                Error errorResponse = new Error(HttpStatus.UNAUTHORIZED.value(),"Token Not Found");
                return sendErrorResponse(exchange, errorResponse, HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, Error errorResponse, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        byte[] responseBytes = toJson(errorResponse);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
    }

    private byte[] toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsBytes(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize error response", e);
        }
    }
}
