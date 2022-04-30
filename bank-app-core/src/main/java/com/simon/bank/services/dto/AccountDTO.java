package com.simon.bank.services.dto;

import com.simon.bank.domain.Customer;
import com.simon.bank.domain.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDTO {

    private Long id;

    private Customer customer;
    private BigDecimal balance;
    private List<Transfer> transfers;

}
