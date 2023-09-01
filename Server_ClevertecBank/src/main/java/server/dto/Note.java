package server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import server.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
public final class Note {
    private final LocalDate localDate;
    private final TransactionType transactionType;
    private final BigDecimal amount;

    @Override
    public String toString() {
        return String.format("%-20s | %-20s | %-20s \n", localDate.toString(), transactionType.toString(), amount.toString());
    }
}
