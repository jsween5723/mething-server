package com.esc.bluespring.common.security;

import com.esc.bluespring.domain.member.MemberServiceFacade;
import com.esc.bluespring.domain.member.entity.Member;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final MemberServiceFacade memberServiceFacade;
    @Override
    @Transactional(readOnly = true)
    public AbstractAuthenticationToken convert(Jwt source) {
        UUID id = UUID.fromString(source.getClaim("id"));
        System.out.println("id = " + id);
        Member member = memberServiceFacade.find(id);
        return new JwtAuthentication(member);
    }
}
