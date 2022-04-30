package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Customer;
import com.simon.bank.domain.Product;
import com.simon.bank.domain.enums.AccountStatus;
import com.simon.bank.repository.AccountRepository;
import com.simon.bank.repository.CustomerRepository;
import com.simon.bank.repository.ProductRepository;
import com.simon.bank.services.dto.AccountDTO;
import com.simon.bank.services.dto.OpenAccountDTO;
import com.simon.bank.services.exception.AccountNotCreatedException;
import com.simon.bank.services.exception.CustomerNotFoundException;
import com.simon.bank.services.exception.ProductNotFoundException;
import com.simon.bank.services.exception.RequestDataNullException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final AccountRepository repository;
    private final TransferService transferService;

    private final Predicate<OpenAccountDTO> openAccountDataNotNull = o ->
            o.getCustomerId() == null || o.getInitialCredit() == null;
    public Account openNewCurrentAccountAndSendTransfer(OpenAccountDTO openAccountDTO) {
        this.checkRequestData(openAccountDTO);
        try{
            log.info("Opening new account with data: " + openAccountDTO);
            Account createdAccount = this.openNewCurrentAccount(openAccountDTO.getCustomerId());
            log.info("Opened and saved new account: " + createdAccount);
            this.sendInitialTransfer(openAccountDTO.getInitialCredit(), createdAccount);
            return createdAccount;
        }catch(Exception e){
            log.error("Exception during account opening: " + e.getMessage());
            throw new AccountNotCreatedException(e.getMessage());
        }
    }

    public List<AccountDTO> getAccounts(){
        return this.repository.findAll()
                .stream()
                .map(this::transformToDTO)
                .collect(Collectors.toList());
    }

    protected void checkRequestData(OpenAccountDTO openAccountDTO){
        if(openAccountDataNotNull.test(openAccountDTO)){
            throw new RequestDataNullException("Request data cannot be null");
        }
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
    protected AccountDTO transformToDTO(Account account){
        return AccountDTO.builder()
                .transfers(this.transferService.findAllByAccountId(account))
                .id(account.getId())
                .customer(account.getCustomer())
                .balance(account.getBalance())
                .build();
    }

}
