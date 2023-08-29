package server.entity;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@Builder
public class Receipt {
    private final TransactionType transactionType;
    private final String senderBank;
    private final String recipientBank;
    private final String senderAccountNumber;
    private final String recipientAccountNumber;
    private final BigDecimal amount;
    private final CurrencyType currency;
    private final Timestamp timestamp;

    @Override
    public String toString() {
        String border = "+----------------------------------------------------+";
        StringBuilder checkData = new StringBuilder();

        checkData.append(String.format("| %-50s |\n", "Bank Check"));
        checkData.append(String.format("| %-20s %29s |\n", "Date:", timestamp));
        checkData.append(String.format("| %-20s %29s |\n", "Transaction:", transactionType));
        checkData.append(String.format("| %-20s %29s |\n", "Sender Bank:", senderBank));
        checkData.append(String.format("| %-20s %29s |\n", "Receiver Bank:", recipientBank));
        checkData.append(String.format("| %-20s %29s |\n", "Sender Account:", senderAccountNumber));
        checkData.append(String.format("| %-20s %29s |\n", "Receiver Account:", recipientAccountNumber));
        checkData.append(String.format("| %-20s %29.2f |\n", "Amount:", amount));

        return border + "\n" + checkData + border;
    }
}