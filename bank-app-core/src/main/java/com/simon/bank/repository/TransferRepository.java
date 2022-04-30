package com.simon.bank.repository;

import com.simon.bank.domain.Account;
import com.simon.bank.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByToAccountOrFromAccount(Account toAccount, Account fromAccount);
}
