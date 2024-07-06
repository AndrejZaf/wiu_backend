package com.writeitup.wiu_post_service.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestUtil {

    private static final String SECRET = "your-256-bit-secret-your-256-bit-secret";

    public static String generateToken(String userId, String role) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issuer("test-issuer")
                .expirationTime(new Date(new Date().getTime() + 60 * 1000 * 60)) // 1 hour
                .claim("realm_access", Map.of("roles", List.of(role)))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}
