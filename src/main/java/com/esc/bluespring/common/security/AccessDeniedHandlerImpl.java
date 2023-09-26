package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {
        String responseBody = objectMapper.writeValueAsString(new ForbiddenException());
        response.getWriter().append(responseBody);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
