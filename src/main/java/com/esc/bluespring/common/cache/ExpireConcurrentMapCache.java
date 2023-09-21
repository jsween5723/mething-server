package com.esc.bluespring.common.cache;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cache.concurrent.ConcurrentMapCache;

public class ExpireConcurrentMapCache extends ConcurrentMapCache {

  private final Map<Object, LocalDateTime> expires = new ConcurrentHashMap<>();
  private final Long expireAfter;

  public ExpireConcurrentMapCache(final String name, final Long expireAfter) {
    super(name);
    this.expireAfter = expireAfter;
  }

  @Override
  protected Object lookup(final Object key) {
    LocalDateTime expiredDate = expires.get(key);
    if (Objects.isNull(expiredDate) || LocalDateTime.now().isBefore(expiredDate)) {
      return super.lookup(key);
    }
    expires.remove(key);
    super.evict(key);
    return null;
  }

  @Override
  public void put(final Object key, final Object value) {
    LocalDateTime expiredAt =
        (expireAfter == null) ? null : LocalDateTime.now().plusSeconds(expireAfter);
    expires.put(key, expiredAt);
    super.put(key, value);
  }

}
