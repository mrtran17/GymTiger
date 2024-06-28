package com.sd38.gymtiger.repository;

import com.sd38.gymtiger.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);

    @Query("select k from Customer k " +
            "where k.status=1 and k.role.id= 3 and k.name like ?1 " +
            "or k.status=1 and k.role.id= 3 and k.email like ?1 " +
            "or k.status=1 and k.role.id= 3 and k.phoneNumber like ?1 ")
    List<Customer> findByKwds(String s);
}
