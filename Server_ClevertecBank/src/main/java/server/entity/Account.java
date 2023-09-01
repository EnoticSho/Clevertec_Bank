package server.entity;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Account {
    private int accountId;
    private String accountNumber;
    private BigDecimal balance;
    private CurrencyType currency;
    private Integer clientId;
    private Integer bankId;
}
