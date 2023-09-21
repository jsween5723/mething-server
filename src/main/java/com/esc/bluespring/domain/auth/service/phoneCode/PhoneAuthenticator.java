package com.esc.bluespring.domain.auth.service.phoneCode;

import com.esc.bluespring.common.configuration.CacheConfiguration;
import com.esc.bluespring.domain.auth.exception.AuthException.PhoneCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class PhoneAuthenticator {
  private final CacheManager cacheManager;

  public void assignPhoneCode(String phoneNumber, PhoneCode code) {
    getCache().put(phoneNumber, code);
  }

  public void authenticatePhoneCode(String phoneNumber, String code) {
    PhoneCode originalCode = getPhoneCode(phoneNumber);
    originalCode.authenticate(code);
  }

  public boolean isAuthenticatedPhone(String phoneNumber) {
    return getPhoneCode(phoneNumber).isAuthenticated();
  }

  private PhoneCode getPhoneCode(String phoneNumber) {
    PhoneCode originalCode = getCache().get(phoneNumber, PhoneCode.class);
    if (originalCode == null) {
      throw new PhoneCodeNotFoundException();
    }
    return originalCode;
  }

  boolean isExistCode(String phoneNumber, String code) {
    try {
      return getPhoneCode(phoneNumber).getCode().equals(code);
    } catch (PhoneCodeNotFoundException e) {
      return false;
    }
  }

  private Cache getCache() {
    return cacheManager.getCache(CacheConfiguration.PHONE_CODE_CACHE_NAME);
  }
}
