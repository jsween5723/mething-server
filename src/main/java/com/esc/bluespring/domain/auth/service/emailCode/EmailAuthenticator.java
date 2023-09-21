package com.esc.bluespring.domain.auth.service.emailCode;

import com.esc.bluespring.common.configuration.CacheConfiguration;
import com.esc.bluespring.domain.auth.exception.AuthException.EmailCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EmailAuthenticator {
  private final CacheManager cacheManager;

  public void assignPhoneCode(String email, EmailCode code) {
    getCache().put(email, code);
  }

  public void authenticatePhoneCode(String phoneNumber, String code) {
    EmailCode originalCode = getPhoneCode(phoneNumber);
    originalCode.authenticate(code);
  }

  public boolean isAuthenticatedPhone(String phoneNumber) {
    return getPhoneCode(phoneNumber).isAuthenticated();
  }

  private EmailCode getPhoneCode(String phoneNumber) {
    EmailCode originalCode = getCache().get(phoneNumber, EmailCode.class);
    if (originalCode == null) {
      throw new EmailCodeNotFoundException();
    }
    return originalCode;
  }

  boolean isExistCode(String phoneNumber, String code) {
    try {
      return getPhoneCode(phoneNumber).getCode().equals(code);
    } catch (EmailCodeNotFoundException e) {
      return false;
    }
  }

  private Cache getCache() {
    return cacheManager.getCache(CacheConfiguration.PHONE_CODE_CACHE_NAME);
  }
}
