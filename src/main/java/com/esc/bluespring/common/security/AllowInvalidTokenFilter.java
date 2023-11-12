package com.esc.bluespring.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AllowInvalidTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getHeader("ALLOW-INVALID-TOKEN") != null && request.getHeader(
            "ALLOW-INVALID-TOKEN").equals("true")) {
            request = new NoAuthorizationHeaderRequest(request);
        }
        filterChain.doFilter(request, response);
    }

    class NoAuthorizationHeaderRequest extends HttpServletRequestWrapper {

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @throws IllegalArgumentException if the request is null
         */
        public NoAuthorizationHeaderRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            return name.equals("Authorization") ? null : super.getHeader(name);
        }
    }
}
