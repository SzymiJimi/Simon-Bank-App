package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Transfer;
import com.simon.bank.domain.enums.TransactionStatus;
import com.simon.bank.domain.enums.TransactionType;
import com.simon.bank.repository.AccountRepository;
import com.simon.bank.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    protected Transfer sendHardSettlementToAccount(Account toAccount, BigDecimal credit){
        Transfer transfer = this.createTransferObject(toAccount, credit);
        log.info("Created transfer object: " + transfer.toString());

        Transfer finishedTransfer = this.transferRepository.save(transfer);
        this.updateAccountBalance(finishedTransfer.getToAccount(), credit);
        log.info("Saved transfer and updated account balance, account: " +
                toAccount +
                ", transfer: " +
                finishedTransfer);

        return finishedTransfer;
    }

    protected Transfer createTransferObject(Account toAccount, BigDecimal credit){
        return Transfer.builder()
                .type(TransactionType.HARD_SETTLEMENT.toString())
                .amount(credit)
                .toAccount(toAccount)
                .date(LocalDateTime.now())
                .status(TransactionStatus.ACCEPTED.toString())
                .build();
    }

    protected List<Transfer> findAllByAccountId(Account account){
        return this.transferRepository.findAllByToAccountOrFromAccount(account, account);
    }

    protected void updateAccountBalance(Account account, BigDecimal amountToAdd){
        account.setBalance(account.getBalance().add(amountToAdd));
        this.accountRepository.save(account);
    }

}
