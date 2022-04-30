package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Customer;
import com.simon.bank.domain.Product;
import com.simon.bank.domain.enums.AccountStatus;
import com.simon.bank.repository.AccountRepository;
import com.simon.bank.repository.CustomerRepository;
import com.simon.bank.repository.ProductRepository;
import com.simon.bank.services.dto.OpenAccountDTO;
import com.simon.bank.services.exception.CustomerNotFoundException;
import com.simon.bank.services.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AccountRepository repository;
    private final TransferService transferService;

    public Account openNewCurrentAccountAndSendTransfer(OpenAccountDTO openAccountDTO) {
        log.info("Opening new account with data: " + openAccountDTO);
        Account createdAccount = this.openNewCurrentAccount(openAccountDTO.getCustomerId());
        log.info("Opened and saved new account: " + createdAccount);
        this.sendInitialTransfer(openAccountDTO.getInitialCredit(), createdAccount);
        return createdAccount;
    }

    protected Account openNewCurrentAccount(Long customerId){
        return this.customerRepository.findById(customerId)
                .map(this::createAccountObject)
                .map(this.repository::save)
                .orElseThrow(() -> new CustomerNotFoundException("Not found customer with specified ID"));
    }

    protected Account createAccountObject(Customer customer){
        Product product = this.getCurrentAccountProduct();
        String denomination = "EUR";
        Account account = Account.builder()
                .customer(customer)
                .product(product)
                .balance(BigDecimal.ZERO)
                .displayName("Current")
                .openingDate(LocalDateTime.now())
                .denomination(denomination)
                .status(AccountStatus.OPEN.toString())
                .build();
        log.info("Created account object: " + account.toString());
        return account;
    }

    protected Product getCurrentAccountProduct(){
        String productName = "current account";
        return this.productRepository.findFirstByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    protected void sendInitialTransfer(BigDecimal initialCredit, Account toAccount){
        log.info("Sending initial transfer with amount: " +
                initialCredit.toString() +
                ", for account: " +
                toAccount.toString());

        Optional.of(initialCredit)
                .filter(ic -> ic.compareTo(BigDecimal.ZERO) > 0)
                .map(ic -> this.transferService.sendHardSettlementToAccount(toAccount, ic));
    }
}
