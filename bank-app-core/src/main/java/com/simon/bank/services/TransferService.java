package com.simon.bank.services;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Transfer;
import com.simon.bank.domain.enums.TransactionStatus;
import com.simon.bank.domain.enums.TransactionType;
import com.simon.bank.repository.TransferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;

    protected Transfer sendHardSettlementToAccount(Account toAccount, BigDecimal credit){
        Transfer transfer = Transfer.builder()
                .type(TransactionType.HARD_SETTLEMENT.toString())
                .amount(credit)
                .toAccount(toAccount)
                .date(LocalDateTime.now())
                .status(TransactionStatus.ACCEPTED.toString())
                .build();
        log.info("Created transfer object: " + transfer.toString());
        return this.transferRepository.save(transfer);
    }

}
