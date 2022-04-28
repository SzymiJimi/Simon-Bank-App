package com.simon.bank.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Product product;
    private BigDecimal balance;
    private String displayName;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private String denomination;
    private String status;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customer +
                ", productId=" + product +
                ", balance=" + balance +
                ", displayName='" + displayName + '\'' +
                ", openingDate=" + openingDate +
                ", closingDate=" + closingDate +
                ", denomination='" + denomination + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
