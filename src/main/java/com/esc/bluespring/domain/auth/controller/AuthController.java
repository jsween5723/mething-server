package com.esc.bluespring.domain.auth.controller;

import com.esc.bluespring.common.BaseResponse;
import com.esc.bluespring.domain.auth.classes.AuthDto;
import com.esc.bluespring.domain.auth.exception.AuthException.SessionHasNotEmailException;
import com.esc.bluespring.domain.auth.service.emailCode.EmailAuthenticationService;
import com.esc.bluespring.domain.auth.service.phoneCode.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final PhoneService phoneService;
  private final EmailAuthenticationService emailAuthenticationService;

//  @PostMapping("phone-codes/generate")
//  @Operation(summary = "요청 유저의 핸드폰 인증 문자를 생성한다. (SMS발송)")
//  @ResponseStatus(HttpStatus.CREATED)
//  public void generatePhoneCode(@Valid @RequestBody PhoneCodeGenerate dto, HttpSession session) {
//    phoneService.genereatePhoneCode(dto.phoneNumber());
//    session.setAttribute("phone-number", dto.phoneNumber());
//  }
//
//  @PostMapping("phone-codes/authenticate")
//  @Operation(summary = "요청 유저의 핸드폰 인증을 완료한다. (인증 마킹)")
//  public void authenticatePhoneCode(@Valid @RequestBody PhoneCodeAuthenticate dto, HttpSession session) {
//    phoneService.authenticatePhoneCode((String) session.getAttribute("phone-number"), dto.phoneCode());
//    session.removeAttribute("phone-number");
//  }

  @PostMapping("email-codes/generate")
  @Operation(summary = "요청 유저의 인증 메일를 발송한다. ")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<Boolean> generateEmailCode(@Valid @RequestBody AuthDto.EmailCodeGenerate dto, HttpSession session) {
    emailAuthenticationService.genereate(dto.email());
    session.setAttribute("email", dto.email());
    return new BaseResponse<>(true);
  }

  @PostMapping("email-codes/authenticate")
  @Operation(summary = "요청 유저의 이메일 인증을 완료한다. (인증 마킹)")
  public BaseResponse<Boolean> authenticateEmailCode(@Valid @RequestBody AuthDto.EmailCodeAuthenticate dto, HttpSession session) {
    if(session.getAttribute("email") == null) {
      throw new SessionHasNotEmailException();
    }
    emailAuthenticationService.authenticate((String) session.getAttribute("email"),dto.code());
    session.removeAttribute("email");
    return new BaseResponse<>(true);
  }
}
