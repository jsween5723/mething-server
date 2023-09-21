package com.esc.bluespring.common.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SwaggerConfiguration {
  @Bean
  ForwardedHeaderFilter forwardedHeaderFilter() {
    return new ForwardedHeaderFilter();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    Server server = new Server();
    server.setUrl("http://api.bluespring-esc.com:8080");
    return new OpenAPI().servers(List.of(server));
  }
}
