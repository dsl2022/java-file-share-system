package com.example.demo.configuration.filters;

import com.example.demo.dto.JwtUserPayload;
import com.example.demo.services.UserService;
import com.example.demo.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.handler.DefaultWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtFilter implements WebFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;
    private ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.pathMatchers("/api/**");
    private ServerAuthenticationSuccessHandler authenticationSuccessHandler = new WebFilterChainServerAuthenticationSuccessHandler();
    private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(new HttpBasicServerAuthenticationEntryPoint());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // type conversion Type Cast
        logger.trace("test chain.toString {}",((DefaultWebFilterChain)chain).getFilters());
        return requiresAuthenticationMatcher.matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .doOnNext(res -> logger.trace("After matchResult {}", String.valueOf(res)))
                .flatMap(matchResult -> check(exchange))
                .doOnNext(res -> logger.trace("After check {}", String.valueOf(res)))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .doOnNext(res -> logger.trace("After switchIfEmpty {}", String.valueOf(res)))
                .flatMap(authentication -> authenticate(exchange, chain, authentication))
                .doOnNext(res -> logger.trace("After authenticate {}", String.valueOf(res)));

    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, Authentication authentication) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        if (!authentication.isAuthenticated()) {
            logger.trace("Authentication failure for user {}", authentication.getPrincipal());
            return authenticationFailureHandler.onAuthenticationFailure(webFilterExchange, new BadCredentialsException("Request is invalid"));
        }
        logger.trace("Authentication success for user {}", authentication.getPrincipal());
        return onAuthenticationSuccess(authentication, webFilterExchange);
    }

    private Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {

        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private Mono<Authentication> check(ServerWebExchange exchange) {
        String clientToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        logger.trace("Token {}", clientToken);
        if (clientToken != null && !clientToken.equals("")) {
            Optional<JwtUserPayload> jwtUserPayload = JWTUtils.parseToken(clientToken);
            if (jwtUserPayload.isPresent() && userService.verify(jwtUserPayload.get())) {
                //jwtUserPayload.get(), use .get because jwtUserPayload is optiona.
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUserPayload.get(), null, AuthorityUtils.createAuthorityList("USER"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return Mono.just(authentication);
            }
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken("anonymous", "anonymous", AuthorityUtils.createAuthorityList("USER"));
        anonymousAuthenticationToken.setAuthenticated(false);
        return Mono.just(anonymousAuthenticationToken);
    }
}
