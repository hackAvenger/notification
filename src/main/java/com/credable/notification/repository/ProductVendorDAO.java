package com.credable.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.credable.notification.constants.NotificationType;
import com.credable.notification.model.ProductVendor;

public interface ProductVendorDAO extends JpaRepository<ProductVendor, Integer>{

  @Query("select pv from ProductVendor pv where pv.product.id=:productId and pv.vendor.id=:vendorId and pv.status='ACTIVE'")
  ProductVendor findByProductAndVendor(@Param("productId") Long id, @Param("vendorId")Long id2);

  @Query("select pv from ProductVendor pv where pv.product.id=:productId and pv.vendor.vendorType=:vendorType and pv.status='ACTIVE'")
  ProductVendor findByProductAndType(@Param("productId") Long productId, @Param("vendorType") NotificationType type);
}
