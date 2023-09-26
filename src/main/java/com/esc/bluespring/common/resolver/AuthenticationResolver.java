package com.esc.bluespring.common.resolver;

import com.esc.bluespring.common.resolver.annotation.AllowAnonymous;
import com.esc.bluespring.domain.auth.exception.AuthException.ForbiddenException;
import com.esc.bluespring.domain.member.entity.Admin;
import com.esc.bluespring.domain.member.entity.Member;
import com.esc.bluespring.domain.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
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
        return Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser") && parameter.getParameterAnnotation(AllowAnonymous.class) != null) {
            return null;
        }
        Member principal = (Member) authentication.getDetails();
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
        throw new ForbiddenException();
    }
}
