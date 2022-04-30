package com.simon.bank.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
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
