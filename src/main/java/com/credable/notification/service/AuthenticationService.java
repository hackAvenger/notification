package com.credable.notification.service;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
  public Boolean authenticateProduct(HttpServletRequest request);
}
