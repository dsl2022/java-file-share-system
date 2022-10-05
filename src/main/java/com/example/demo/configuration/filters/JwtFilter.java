package com.example.demo.configuration.filters;

import com.example.demo.dto.JwtUserPayload;
import com.example.demo.services.UserService;
import com.example.demo.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import reactor.core.publisher.Mono;

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
        return requiresAuthenticationMatcher.matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .flatMap(matchResult -> check(exchange))
                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
                .flatMap(authentication -> authenticate(exchange, chain, authentication));
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
        if(clientToken==null || clientToken.equals("")){
            return Mono.empty();
        }
        JwtUserPayload jwtUserPayload = JWTUtils.parseToken(clientToken);
        if (userService.verify(jwtUserPayload)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUserPayload, null, AuthorityUtils.createAuthorityList("USER"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return Mono.just(authentication);
        }
        return Mono.empty();
    }
}
