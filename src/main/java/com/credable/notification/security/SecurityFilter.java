package com.credable.notification.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.credable.notification.constants.CacheConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.credable.notification.exception.UnAuthorizedSessionException;
import com.credable.notification.service.AuthenticationService;

@Component
@Log4j2
public class SecurityFilter extends OncePerRequestFilter{
  
  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private Environment env;
  private final Set<String> excludedUrls = new HashSet<>();

  @PostConstruct
  public void init() {
    log.info("Creating excluded url set");
    String excludedString = env.getProperty(CacheConstant.EXCLUDE_URLS);
    if (StringUtils.hasText(excludedString)) {
      excludedUrls.addAll(Arrays.asList(excludedString.split(",")));
    }

  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if(excludedUrls.contains(request.getRequestURI())) {
      filterChain.doFilter(request, response);
    } else {
      if (Boolean.TRUE.equals(authenticationService.authenticateProduct(request))) {
        filterChain.doFilter(request, response);
      } else {
        throw new UnAuthorizedSessionException("UNAUTHORIZED", "Invalid Headers");
      }
    }
  }
  
}
