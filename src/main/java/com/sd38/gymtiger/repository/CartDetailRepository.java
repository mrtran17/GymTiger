package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
}
