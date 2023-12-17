package com.esc.bluespring.domain.policyterm;


import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.domain.policyterm.entity.UserPolicyterm;
import com.esc.bluespring.domain.policyterm.mapper.UserPolicytermMapper;
import com.esc.bluespring.domain.policyterm.model.UserPolicytermDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/student-policiterms")
@RequiredArgsConstructor
@Tag(name = "학생 가입 약관 컨트롤러")
public class UserPolicytermController {
    private final UserPolicytermService policytermService;
    private final UserPolicytermMapper mapper = UserPolicytermMapper.INSTANCE;
    @GetMapping
    @Operation(description = "유저용 약관 페이지 항목들 불러오기 기능입니다.", summary = "유저용 약관 불러오기")
    public BaseResponse<List<UserPolicytermDto>> getPolicyterms() {
        List<UserPolicyterm> buyerPolicyterms = policytermService.getBuyerPolicyterms();
        return new BaseResponse<>(buyerPolicyterms.stream().map(mapper::toDto).toList());
    }
}
