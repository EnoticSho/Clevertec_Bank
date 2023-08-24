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
    private String currency;
    private int clientId;
    private int bankId;
}
