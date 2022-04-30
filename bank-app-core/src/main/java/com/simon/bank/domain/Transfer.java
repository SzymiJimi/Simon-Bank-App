package com.simon.bank.domain;

import lombok.*;

import javax.persistence.*;
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
