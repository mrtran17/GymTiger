package com.sd38.gymtiger.service.impl;

import com.sd38.gymtiger.repository.CustomerRepository;
import com.sd38.gymtiger.model.Customer;
import com.sd38.gymtiger.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByKwds(String s) {
        return customerRepository.findByKwds(s);
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.getById(id);
    }
}
