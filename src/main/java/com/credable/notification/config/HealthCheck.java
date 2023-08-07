package com.credable.notification.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("healthCheck")
public class HealthCheck implements HealthIndicator {

  @Override
  public Health health() {
    return Health.up().build();
  }

}
