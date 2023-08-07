package com.credable.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.credable.notification.constants.NotificationType;
import com.credable.notification.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor,Long> {

    @Query("select v from ProductVendor pv join pv.vendor v join pv.product p where v.status = 'ACTIVE' and pv.status = 'ACTIVE' and v.vendorType =:vendorType and p.id=:productId")
    List<Vendor> findActiveVendorByProduct(@Param("vendorType") NotificationType vendorType, @Param("productId") Long productId);

}
