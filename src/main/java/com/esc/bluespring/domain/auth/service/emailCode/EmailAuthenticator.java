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

  public void authenticatePhoneCode(String email, String code) {
    EmailCode originalCode = getCode(email);
    originalCode.authenticate(code);
  }

  public boolean isAuthenticatedEmail(String email) {
    return getCode(email).isAuthenticated();
  }

  private EmailCode getCode(String email) {
    EmailCode originalCode = getCache().get(email, EmailCode.class);
    if (originalCode == null) {
      throw new EmailCodeNotFoundException();
    }
    return originalCode;
  }

  boolean isExistCode(String email, String code) {
    try {
      return getCode(email).getCode().equals(code);
    } catch (EmailCodeNotFoundException e) {
      return false;
    }
  }

  private Cache getCache() {
    return cacheManager.getCache(CacheConfiguration.CODE_CACHE_NAME);
  }
}
