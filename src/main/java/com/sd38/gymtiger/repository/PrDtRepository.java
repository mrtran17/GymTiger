package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrDtRepository extends JpaRepository<ProductDetail, Integer> {
    List<ProductDetail> findAllByAndStatusOrderByIdDesc(Integer status);
}
