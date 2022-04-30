package com.simon.bank.controller;

import com.simon.bank.domain.Account;
import com.simon.bank.services.AccountService;
import com.simon.bank.services.dto.OpenAccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/open-account")
    public ResponseEntity<Account> openNewAccount(@RequestBody OpenAccountDTO openAccountDTO){

        Account result = this.accountService.openNewCurrentAccountAndSendTransfer(openAccountDTO);
        return ResponseEntity.ok(result);
    }
}
