package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.entity.Member;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtEncoder {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(Member member) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(15, ChronoUnit.MINUTES);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder().claim("id", member.getId())
            .issuedAt(issuedAt).expiresAt(expiration).build();
        JwsHeader.Builder with = JwsHeader.with(SignatureAlgorithm.RS512);
        JwsHeader jwt = with.type("jwt").build();
        return jwtEncoder.encode(
                JwtEncoderParameters.from(jwt, claimsSet))
            .getTokenValue();
    }

    public String generateRefreshToken() {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder().claim("id", UUID.randomUUID().toString()).build()))
            .getTokenValue();
    }
}
