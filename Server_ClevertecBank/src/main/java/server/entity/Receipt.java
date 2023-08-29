package server.entity;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class Receipt {
    private final TransactionType transactionType;
    private final String senderBank;
    private final String recipientBank;
    private final String senderAccountNumber;
    private final String recipientAccountNumber;
    private final BigDecimal amount;
    private final CurrencyType currency;
    private final Timestamp timestamp;
}