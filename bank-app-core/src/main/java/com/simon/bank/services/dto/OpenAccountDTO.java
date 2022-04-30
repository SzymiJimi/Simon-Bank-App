package com.simon.bank.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpenAccountDTO implements Serializable {

    private Long customerId;
    private BigDecimal initialCredit;

    @Override
    public String toString() {
        return "OpenAccountDTO{" +
                "customerId=" + customerId +
                ", initialCredit=" + initialCredit +
                '}';
    }
}
