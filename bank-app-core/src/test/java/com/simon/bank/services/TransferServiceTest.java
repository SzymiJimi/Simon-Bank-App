package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Transfer;
import com.simon.bank.domain.enums.TransactionStatus;
import com.simon.bank.domain.enums.TransactionType;
import com.simon.bank.repository.AccountRepository;
import com.simon.bank.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TransferServiceTest {

    @Mock
    TransferRepository transferRepository;

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    TransferService transferService;
    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    public void testSendHardSettlementToAccount(){
        //given
        Account toAccount = this.createAccount();
        BigDecimal credit = BigDecimal.valueOf(10.5);
        Transfer finishedTransfer = this.createTransfer(toAccount, credit);
        when(transferRepository.save(any(Transfer.class))).thenReturn(finishedTransfer);
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        //when
        Transfer transfer = this.transferService.sendHardSettlementToAccount(toAccount, credit);

        //then
        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        verify(transferRepository, times(1)).save(transferCaptor.capture());
        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(toAccount, transferCaptor.getValue().getToAccount());
        assertEquals(credit, transferCaptor.getValue().getAmount());
    }

    @Test
    public void testCreationOfTransferObject(){
        //given
        Account toAccount = this.createAccount();
        BigDecimal credit = BigDecimal.valueOf(10.5);

        //when
        Transfer result = this.transferService.createTransferObject(toAccount, credit);

        //then
        assertEquals(TransactionType.HARD_SETTLEMENT.toString(), result.getType());
        assertEquals(credit, result.getAmount());
        assertEquals(toAccount, result.getToAccount());
        assertNotNull(result.getDate());
        assertTrue(result.getDate().isBefore(LocalDateTime.now()));
        assertEquals(TransactionStatus.ACCEPTED.toString(), result.getStatus());
    }

    @Test
    public void testFindAllByAccountId(){
        //given
        Account account = this.createAccount();
        when(transferRepository.save(any(Transfer.class))).thenReturn(new Transfer());

        //when
        this.transferService.findAllByAccountId(account);

        //then
        verify(transferRepository, times(1)).findAllByToAccountOrFromAccount(any(Account.class), any(Account.class));
    }

    @Test
    public void testUpdateAccountBalance(){
        //given
        Account account = this.createAccount();
        BigDecimal credit = BigDecimal.valueOf(10.5);
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        //when
        this.transferService.updateAccountBalance(account, credit);

        //then
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository, times(1)).save(accountCaptor.capture());
        assertEquals(credit, accountCaptor.getValue().getBalance());
    }

    private Account createAccount(){
        return Account.builder()
                .id(1L)
                .balance(BigDecimal.ZERO)
                .build();
    }

    private Transfer createTransfer(Account toAccount, BigDecimal amount){
        return Transfer.builder()
                .id(1L)
                .toAccount(toAccount)
                .amount(amount)
                .build();
    }
}