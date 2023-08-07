package com.credable.notification.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import com.credable.notification.Utils.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestClientConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @Bean
  @Primary
  public RestClient restClient(RestTemplate restTemplate) {
    return new RestClient(restTemplate, objectMapper);
  }


  @Bean
  public RestClient restClientTimeOut20s() {
    RestTemplate restTemplate = new RestTemplateBuilder()
	    .setConnectTimeout(Duration.ofSeconds(20))
	    .setReadTimeout(Duration.ofSeconds(20))
	    .build();
    return new RestClient(restTemplate, objectMapper);
  }


}
