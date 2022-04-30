package com.simon.bank.services;

import com.simon.bank.domain.Customer;
import com.simon.bank.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void whenFindAll_thenCallRepository() {
        //given
        List<Customer> customers = this.createCustomers();
        when(customerRepository.findAll()).thenReturn(customers);

        //when
        List<Customer> result = this.customerService.findAll();

        //then
        Assertions.assertEquals(customers, result);
    }

    List<Customer> createCustomers(){
        return List.of(new Customer(1L, "Sara", "Novak", LocalDate.of(1990, 5, 10)),
                new Customer(2L, "Sebastian", "Hamilton", LocalDate.of(1985, 10,26)));
    }
}