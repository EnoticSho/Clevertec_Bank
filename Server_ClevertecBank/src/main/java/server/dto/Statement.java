package server.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Builder
public final class Statement {
    private final String clientName;
    private final String account;
    private final String currency;
    private final String period;
    private final Timestamp timestamp;
    private final BigDecimal balance;
    private final List<Note> notes;

    @Override
    public String toString() {
        StringBuilder checkData = new StringBuilder();

        checkData.append("Statement\n");
        checkData.append("Clevertec_Bank\n");
        checkData.append(formatOutput("Client", clientName));
        checkData.append(formatOutput("Account", account));
        checkData.append(formatOutput("Currency", currency));
        checkData.append(formatOutput("Period", period));
        checkData.append(formatOutput("Date of formation", timestamp.toString()));
        checkData.append(formatOutput("Balance", balance.toString()));
        String header = String.format("%-20s | %-20s | %-20s\n", "Date", "Transaction Type", "Amount");
        checkData.append(header);
        checkData.append(new String(new char[header.length()]).replace('\0', '-'));
        checkData.append("\n");
        for (Note note : notes) {
            checkData.append(note.toString());
        }
        return checkData.toString();
    }

    private String formatOutput(String title, String value) {
        return String.format("%-25s | %-25s\n", title, value);
    }
}
