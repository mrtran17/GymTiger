package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<Cart,Integer> {
//    List<Cart> findAllBy(Integer status);

//    Page<Cart> findAllByStatusOrderByUpdateDateDesc(Pageable pageable);

    Cart findByCustomerId(Integer accountId);
}
