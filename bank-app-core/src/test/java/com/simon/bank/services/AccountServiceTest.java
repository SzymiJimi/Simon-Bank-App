package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Customer;
import com.simon.bank.domain.Product;
import com.simon.bank.domain.Transfer;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.crypto.CipherInputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    AccountRepository repository;

    @Mock
    TransferService transferService;

    @InjectMocks
    AccountService accountService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testOpeningCurrentAccountAndSendingTransfer_whenInitialCreditIsGreaterThanZero() {
        //given
        BigDecimal initialCredit = BigDecimal.valueOf(200.50);
        Long customerId = 2L;
        OpenAccountDTO openAccountDTO = mockBeanCalls(customerId, initialCredit);

        //when
        Account account = this.accountService.openNewCurrentAccountAndSendTransfer(openAccountDTO);

        //then
        verify(customerRepository, times(1)).findById(eq(customerId));
        verify(productRepository, times(1)).findFirstByName(any(String.class));
        verify(transferService, times(1)).sendHardSettlementToAccount(eq(account), eq(initialCredit));
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void testOpeningCurrentAccountAndSendingTransfer_whenInitialCreditIsZero() {
        //given
        BigDecimal initialCredit = BigDecimal.valueOf(0.0);
        Long customerId = 2L;
        OpenAccountDTO openAccountDTO = this.mockBeanCalls(customerId, initialCredit);

        //when
        this.accountService.openNewCurrentAccountAndSendTransfer(openAccountDTO);

        //then
        verify(customerRepository, times(1)).findById(eq(customerId));
        verify(productRepository, times(1)).findFirstByName(any(String.class));
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void testOpeningCurrentAccountAndSendingTransfer_whenExceptionIsThrown() {
        //given
        BigDecimal initialCredit = BigDecimal.valueOf(200.50);
        Long customerId = 2L;
        when(customerRepository.findById(any(Long.class))).thenThrow(new RuntimeException("Exception"));

        //when then
        assertThrows(AccountNotCreatedException.class,
                () -> this.accountService.openNewCurrentAccountAndSendTransfer(new OpenAccountDTO(customerId, initialCredit)));
    }

    @Test
    void testCheckRequestData_whenCustomerIsNull() {
        //given
        BigDecimal initialCredit = BigDecimal.valueOf(200.50);
        Long customerId = null;

        //when then
        assertThrows(RequestDataNullException.class,
                () -> this.accountService.checkRequestData(new OpenAccountDTO(customerId, initialCredit)));
    }

    @Test
    void testCheckRequestData_whenCreditIsNull() {
        //given
        BigDecimal initialCredit = null;
        Long customerId = 2L;

        //when then
        assertThrows(RequestDataNullException.class,
                () -> this.accountService.checkRequestData(new OpenAccountDTO(customerId, initialCredit)));
    }

    @Test
    void testGetAccounts_whenAccountsExists_shouldTransformToDTO(){
        //given
        Account account = createAccount();
        when(repository.findAll()).thenReturn(List.of(account));
        when(transferService.findAllByAccountId(any(Account.class))).thenReturn(Collections.emptyList());

        //when
        this.accountService.getAccounts();

        //then
        verify(transferService, times(1)).findAllByAccountId(any(Account.class));
    }

    @Test
    void testGetAccounts_whenAccountsNotExists_shouldNotTransformToDTO(){
        //given
        Account account = createAccount();
        when(repository.findAll()).thenReturn(Collections.emptyList());

        //when
        this.accountService.getAccounts();

        //then
        verify(transferService, times(0)).findAllByAccountId(any(Account.class));
    }

    @Test
    void testOpeningCurrentAccount(){
        //given
        Long customerId = 1L;
        this.mockBeanCalls(customerId, BigDecimal.ZERO);

        //when
        this.accountService.openNewCurrentAccount(customerId);

        //then
        verify(repository, times(1)).save(any(Account.class));
    }

    @Test
    void testOpeningCurrentAccount_whenCustomerNotFound(){
        //given
        Long customerId = 1L;
        when(customerRepository.findById(eq(customerId))).thenReturn(Optional.empty());

        //when then
        assertThrows(CustomerNotFoundException.class, () -> this.accountService.openNewCurrentAccount(customerId));
        verify(repository, times(0)).save(any(Account.class));
    }

    @Test
    void testCreationAccountObject(){
        //given
        String productName = "current account";
        when(productRepository.findFirstByName(eq(productName))).thenReturn(Optional.of(new Product()));
        Customer customer = new Customer();

        //when
        Account result = this.accountService.createAccountObject(customer);

        //then
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertNotNull(result.getCustomer());
        assertNotNull(result.getProduct());
        assertNotNull(result.getDisplayName());
        assertNotNull(result.getOpeningDate());
        assertNull(result.getClosingDate());
        assertNotNull(result.getDenomination());
        assertEquals(AccountStatus.OPEN.toString(), result.getStatus());
    }

    @Test
    void testGettingCurrentAccount(){
        //given
        String productName = "current account";
        when(productRepository.findFirstByName(eq(productName))).thenReturn(Optional.of(new Product()));

        //when
        Product result = this.accountService.getCurrentAccountProduct();

        //then
        assertNotNull(result);
    }

    @Test
    void testGettingCurrentAccount_whenProductNotFound(){
        //given
        String productName = "current account";
        when(productRepository.findFirstByName(eq(productName))).thenReturn(Optional.empty());

        //when //then
        assertThrows(ProductNotFoundException.class, () -> this.accountService.getCurrentAccountProduct());

    }

    @Test
    void testSendInitialTransfer_whenInitialCreditGreaterThanZero(){
        //given
        BigDecimal initialCredit = BigDecimal.valueOf(100.5);
        Account toAccount = new Account();
        when(transferService.sendHardSettlementToAccount(eq(toAccount), eq(initialCredit))).thenReturn(new Transfer());

        //when
        this.accountService.sendInitialTransfer(initialCredit, toAccount);

        // then
        verify(transferService, times(1)).sendHardSettlementToAccount(eq(toAccount), eq(initialCredit));

    }

    @Test
    void testSendInitialTransfer_whenInitialCreditIsZero(){
        //given
        BigDecimal initialCredit = BigDecimal.ZERO;
        Account toAccount = new Account();
        when(transferService.sendHardSettlementToAccount(eq(toAccount), eq(initialCredit))).thenReturn(new Transfer());

        //when
        this.accountService.sendInitialTransfer(initialCredit, toAccount);

        // then
        verify(transferService, times(0)).sendHardSettlementToAccount(eq(toAccount), eq(initialCredit));

    }

    @Test
    void testTransformToDTO(){
        //given
        Account account = createAccount();
        when(transferService.findAllByAccountId(any(Account.class))).thenReturn(Collections.emptyList());

        //when
        AccountDTO result = this.accountService.transformToDTO(account);

        //then
        assertEquals(account.getId(), result.getId());
        assertEquals(account.getCustomer(), result.getCustomer());
        assertEquals(account.getBalance(), result.getBalance());
        assertEquals(0, result.getTransfers().size());
    }

    private OpenAccountDTO mockBeanCalls(Long customerId, BigDecimal initialCredit){
        String productName = "current account";
        Customer customerToReturn = Customer.builder().id(customerId).build();

        when(customerRepository.findById(eq(customerToReturn.getId()))).thenReturn(Optional.of(customerToReturn));
        when(productRepository.findFirstByName(eq(productName))).thenReturn(Optional.of(new Product()));
        when(repository.save(any(Account.class))).thenReturn(new Account());

        return new OpenAccountDTO(customerId, initialCredit);
    }

    private Account createAccount(){
        return Account.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(2.0))
                .customer(new Customer())
                .build();
    }
}