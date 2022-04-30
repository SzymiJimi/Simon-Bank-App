package com.simon.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
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
