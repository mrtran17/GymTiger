package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer findByEmail(String email);

    List<Customer> findByKwds(String s);

    Customer findById(Integer id);
}
