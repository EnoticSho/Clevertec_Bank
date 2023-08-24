package server.entity;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class Transaction {
    private int transactionId;
    private BigDecimal amount;
    private Timestamp transactionDate;
    private int senderAccountId;
    private int receiverAccountId;
    private String transactionType;
}
