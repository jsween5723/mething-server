package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.entity.Member;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomJwtEncoder {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(Member member) {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder().claim("id", member.getId()).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public String generateRefreshToken() {
        return jwtEncoder.encode(JwtEncoderParameters.from(
                JwtClaimsSet.builder().claim("id", UUID.randomUUID().toString()).build()))
            .getTokenValue();
    }
}
