package com.esc.bluespring.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RSAEncoder {

    private final JwtEncoder jwtEncoder;
}
