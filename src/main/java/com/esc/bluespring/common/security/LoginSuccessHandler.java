package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.classes.MemberDto.JwtToken;
import com.esc.bluespring.domain.member.entity.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CustomJwtEncoder jwtEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String accessToken = jwtEncoder.generateAccessToken((Member) authentication.getPrincipal());
        Cookie refreshToken = new Cookie("refreshToken", jwtEncoder.generateRefreshToken());
        refreshToken.isHttpOnly();
        refreshToken.setSecure(true);
        response.addCookie(refreshToken);
        JwtToken jwtTokenDto = new JwtToken(accessToken);
        String responseBody = objectMapper.writeValueAsString(jwtTokenDto);
        response.getWriter().append(responseBody);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
