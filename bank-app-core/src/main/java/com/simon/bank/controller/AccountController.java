package com.simon.bank.controller;

import com.simon.bank.domain.Account;
import com.simon.bank.services.AccountService;
import com.simon.bank.services.dto.AccountDTO;
import com.simon.bank.services.dto.OpenAccountDTO;
import com.simon.bank.services.exception.AccountNotCreatedException;
import com.simon.bank.services.exception.RequestDataNullException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(value = "/open-account")
    public ResponseEntity<Account> openNewAccount(@RequestBody OpenAccountDTO openAccountDTO) {
        Account result = this.accountService.openNewCurrentAccountAndSendTransfer(openAccountDTO);
        return ResponseEntity.ok(result);

    }

    @GetMapping(value = "/accounts")
    public ResponseEntity<List<AccountDTO>> getAccounts() {
        List<AccountDTO> result = this.accountService.getAccounts();
        return ResponseEntity.ok(result);

    }

    @ExceptionHandler(value = AccountNotCreatedException.class)
    protected ResponseEntity<Object> handleAccountNotOpened(AccountNotCreatedException ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(value = RequestDataNullException.class)
    protected ResponseEntity<Object> handleRequestDataNull(RequestDataNullException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
