package com.sd38.gymtiger.service.impl;

import com.sd38.gymtiger.repository.CustomerRepository;
import com.sd38.gymtiger.model.Customer;
import com.sd38.gymtiger.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
