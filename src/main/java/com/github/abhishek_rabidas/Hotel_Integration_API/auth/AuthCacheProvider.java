package com.github.abhishek_rabidas.Hotel_Integration_API.auth;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AuthCacheProvider {
    private final Cache<String, Authentication> authCache = CacheBuilder.newBuilder().build();
}
