package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.MemberServiceFacade;
import com.esc.bluespring.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final MemberServiceFacade memberServiceFacade;
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Long id = source.getClaim("id");
        Member member = memberServiceFacade.find(id);
        return new JwtAuthentication(member);
    }
}
