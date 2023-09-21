package com.esc.bluespring.common.resolver;

import com.esc.bluespring.common.resolver.annotation.Me;
import com.esc.bluespring.domain.member.MemberServiceFacade;
import com.esc.bluespring.domain.member.entity.Student;
import com.esc.bluespring.domain.member.exception.MemberException.LoginRequiredException;
import com.esc.bluespring.domain.member.exception.MemberException.MemberNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MeResolver implements HandlerMethodArgumentResolver {
    private final MemberServiceFacade memberServiceFacade;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Student.class) || (parameter.getParameterType().equals(
            Long.class) && parameter.getParameterAnnotation(Me.class) != null);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Long memberId = (Long) request.getSession().getAttribute("memberId");
        if (memberId == null) {
            throw new LoginRequiredException();
        }
        if (parameter.getParameterType().equals(Long.class)) {
            return memberId;
        }
        if (memberServiceFacade.find(memberId) instanceof Student student) {
            return student;
        }
        throw new MemberNotFoundException();
    }
}
