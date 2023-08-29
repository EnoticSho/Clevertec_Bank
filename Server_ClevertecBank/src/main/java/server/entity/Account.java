package server.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    private int accountId;
    private String accountNumber;
    private BigDecimal balance;
    private CurrencyType currency;
    private Integer clientId;
    private Integer bankId;
}
