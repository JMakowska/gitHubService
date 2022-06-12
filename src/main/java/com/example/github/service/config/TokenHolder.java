package com.example.github.service.config;

import org.assertj.core.util.Strings;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class TokenHolder {
    private final String token;
    private boolean tokenDefined = true;

    TokenHolder() {
        this.token = System.getenv("GH_TOKEN");
        if ( Strings.isNullOrEmpty(token) ) {
            this.tokenDefined = false;
            log.debug("Environment variable GH_TOKEN does''t exists. Unauthenticated users have restricted number of requests per hour.");
        }
    }

}
