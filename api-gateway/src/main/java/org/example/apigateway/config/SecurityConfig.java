package org.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
//
//        serverHttpSecurity.csrf().disable()
//                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/eureka/**").permitAll()
//                        .anyExchange().authenticated())
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
//        return serverHttpSecurity.build();
//    }
//
//    @Bean
//    public ReactiveJwtDecoder reactiveJwtDecoder() {
//        return ReactiveJwtDecoders.fromIssuerLocation("http://localhost:8181/realms/spring-boot-microservices-realm");
//    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {

        serverHttpSecurity.csrf().disable()
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec.pathMatchers("/**").permitAll());
        return serverHttpSecurity.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return new ReactiveJwtDecoder() {
            @Override
            public Mono<Jwt> decode(String token) throws JwtException {
                return null;
            }
        };
    }
}
