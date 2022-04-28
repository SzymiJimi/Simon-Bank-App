package com.simon.bank.services;

import com.simon.bank.domain.Customer;
import com.simon.bank.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll(){
        return this.customerRepository.findAll();
    }
}
