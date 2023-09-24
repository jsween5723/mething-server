package com.esc.bluespring.common.configuration;

import com.esc.bluespring.common.resolver.AuthenticationResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
  private final AuthenticationResolver authenticationResolver;
  @Value("${cors-list:http://localhost:8080}")
  private List<String> list;
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    CorsRegistration cors = registry.addMapping("/api/**")
        .allowedMethods("*")
        .allowCredentials(true)
        .allowedHeaders(SwaggerConfiguration.AUTHORIZATION_HEADER)
        .maxAge(3600);
    list.forEach(cors::allowedOrigins);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(authenticationResolver);
  }
}
