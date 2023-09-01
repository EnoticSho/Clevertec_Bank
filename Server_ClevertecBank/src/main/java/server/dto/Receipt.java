package server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.entity.CurrencyType;
import server.entity.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
@Builder
public final class Receipt {
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

        String checkData = String.format("| %-50s |\n", "Bank Check") +
                String.format("| %-20s %29s |\n", "Date:", timestamp) +
                String.format("| %-20s %29s |\n", "Transaction:", transactionType) +
                String.format("| %-20s %29s |\n", "Sender Bank:", senderBank) +
                String.format("| %-20s %29s |\n", "Receiver Bank:", recipientBank) +
                String.format("| %-20s %29s |\n", "Sender Account:", senderAccountNumber) +
                String.format("| %-20s %29s |\n", "Receiver Account:", recipientAccountNumber) +
                String.format("| %-20s %29.2f |\n", "Amount:", amount);

        return border + "\n" + checkData + border;
    }
}