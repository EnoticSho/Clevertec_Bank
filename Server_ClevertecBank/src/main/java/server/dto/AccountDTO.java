package server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.entity.CurrencyType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AccountDTO {
    private int accountId;
    private String accountNumber;
    private String bankName;
    private String clientName;
    private CurrencyType currencyType;
    private BigDecimal amount;
}
