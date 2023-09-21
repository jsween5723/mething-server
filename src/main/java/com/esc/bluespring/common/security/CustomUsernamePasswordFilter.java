package com.esc.bluespring.common.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;


@Component
public class CustomUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {
    public CustomUsernamePasswordFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("email");
    }
}
