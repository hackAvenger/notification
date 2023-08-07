package com.credable.notification.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.credable.notification.model.Product;
import com.credable.notification.repository.ProductRepository;
import com.credable.notification.service.AuthenticationService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {
  @Autowired
  private ProductRepository productDAO;
  
  @Override
  public Boolean authenticateProduct(HttpServletRequest request) {
    String productKey = request.getHeader("X-PRODUCT-KEY");
    log.info("Product Key {}", productKey);
    if (StringUtils.hasText(productKey)){
        Product product = productDAO.findByProductKey(productKey);
        if (product != null) {
            request.setAttribute("product", product);
            return true;
        }
    }
    return false;
  }
}
