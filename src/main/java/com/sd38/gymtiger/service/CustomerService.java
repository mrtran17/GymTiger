package com.sd38.gymtiger.service;

import com.sd38.gymtiger.model.Customer;

public interface CustomerService {
    Customer findByEmail(String email);
}
