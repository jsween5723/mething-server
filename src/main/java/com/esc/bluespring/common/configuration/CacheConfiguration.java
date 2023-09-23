package com.esc.bluespring.common.configuration;

import com.esc.bluespring.common.cache.ExpireConcurrentMapCache;
import java.util.Set;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfiguration implements CachingConfigurer {
  static public String CODE_CACHE_NAME = "verificationCode";
  static public String LOCATION_DISTRICT_CACHE_NAME = "locationDistricts";

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(Set.of(
        new ConcurrentMapCache(LOCATION_DISTRICT_CACHE_NAME),
        new ExpireConcurrentMapCache(CODE_CACHE_NAME, 5L * 60)));
    return cacheManager;
  }

}
