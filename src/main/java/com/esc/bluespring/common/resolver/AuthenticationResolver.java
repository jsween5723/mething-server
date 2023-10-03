package com.esc.bluespring.common.resolver;

import static com.esc.bluespring.domain.member.entity.Member.ANONYMOUS;

import com.esc.bluespring.domain.member.entity.Admin;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        return parameterType.equals(Member.class) || parameterType.equals(Student.class)
            || parameterType.equals(Admin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
            .contains("ROLE_"+ANONYMOUS)) {
            return null;
        }
        Member principal = authentication.getDetails() instanceof Member member ? member : null;
        if (parameter.getParameterType().equals(Member.class)) {
            return principal;
        }
        if (principal instanceof Student student && parameter.getParameterType()
            .equals(Student.class)) {
            return student;
        }
        if (principal instanceof Admin admin && parameter.getParameterType().equals(Admin.class)) {
            return admin;
        }
        return null;
    }
}
