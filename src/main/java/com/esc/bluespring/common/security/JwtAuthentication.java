package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {
    public JwtAuthentication(Member member) {
        super(member.getAuthorities());
        setDetails(member);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
