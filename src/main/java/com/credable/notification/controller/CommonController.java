package com.credable.notification.controller;

import com.credable.notification.constants.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credable.notification.constants.ApiUriConstant;
import com.credable.notification.response.ResponseWO;

@RestController
@RequestMapping(ApiUriConstant.BASE_COMMON_URI)
public class CommonController {

  @Autowired
  @Qualifier("healthCheck")
  private HealthIndicator healthIndicator;

  @Autowired
  private Environment env;


  @GetMapping(value = ApiUriConstant.HEALTH_CHECK)
  public ResponseEntity<ResponseWO<Health>> healthCheck() {
    ResponseWO<Health> response = null;
    Health health = healthIndicator.health();
    response = ResponseWO.okBuilder(health).setMessageCode(ApiMessage.HEALTH_CHECK_INFO).setDescription(env.getProperty(ApiMessage.HEALTH_CHECK_INFO)).build();
    return ResponseEntity.ok(response);
  }


}
