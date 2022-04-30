package com.simon.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private String type;

    private BigDecimal amount;

    @ManyToOne
    private Account fromAccount;
    @ManyToOne
    private Account toAccount;
    private LocalDateTime date;
    private String status;

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", from_account=" + fromAccount +
                ", to_account=" + toAccount +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
