package com.writeitup.wiu_user_service.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;

@UtilityClass
public class JwtUtil {

    public static String getJwtClaim(final String key) {
        final Map<String, Object> claims = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims();
        return (String) claims.get(key);
    }
}
