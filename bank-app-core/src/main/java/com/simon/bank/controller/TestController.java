package com.simon.bank.controller;

import com.simon.bank.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final CustomerService customerService;

    @GetMapping(value = "/home")
    public String testRest(){
        return this.customerService.findAll().get(0).toString();
    }

}
