package server.entity;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private Integer transactionId;
    private BigDecimal amount;
    private Timestamp transactionDate;
    private Integer senderAccountId;
    private Integer receiverAccountId;
    private TransactionType transactionType;
}
