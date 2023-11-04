package com.esc.bluespring.common.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.RSAKey.Builder;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;

@Configuration
public class RSAConfiguration {

    @Value("${rsa.public-key}")
    private String publicKey;
    @Value("${rsa.private-key}")
    private String privateKey;

    @Bean
    public JWKSource<SecurityContext> keyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        byte[] decodedBase64PubKey = Base64.getDecoder().decode(publicKey);
        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(privateKey);
        PublicKey aPublic = factory.generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
        PrivateKey aPrivate = factory.generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
        RSAKey keys = new Builder((RSAPublicKey) aPublic).privateKey(aPrivate).expirationTime(Date.from(
                Instant.now().plus(120, ChronoUnit.MINUTES)))
            .build();
        JWKSet jwkSet = new JWKSet(keys);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> source) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(source);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> source) {
        return new NimbusJwtEncoder(source);
    }
}
