package server.service;

import server.dto.Note;
import server.dto.Statement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

/**
 * The StatementService class provides methods to generate, save, and manage account statements.
 * This includes the ability to create textual statements, save them to a file, and ensure the
 * proper directories exist for storing these files.
 */
public class StatementService {

    private final String Statement_DIR = "statement";

    /**
     * Constructs a new StatementService and ensures the directory for storing
     * statement files exists.
     */
    public StatementService() {
        ensureStatementDirectoryExists();
    }

    /**
     * Generates an account statement based on the provided parameters.
     *
     * @param clientName The name of the client.
     * @param account The account number.
     * @param currencyType The type of currency.
     * @param period The period for which the statement is generated.
     * @param amount The balance amount.
     * @param noteList List of notes associated with the statement.
     * @return A string representation of the generated statement.
     */
    public String generateStatement(String clientName,
                                  String account,
                                  String currencyType,
                                  String period,
                                  BigDecimal amount,
                                  List<Note> noteList) {
        Statement statement = Statement.builder()
                .clientName(clientName)
                .account(account)
                .currency(currencyType)
                .period(period)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .balance(amount)
                .notes(noteList)
                .build();
        try {
            saveStatementToFile(statement);
        } catch (IOException ex) {
            throw new RuntimeException("Error generating receipt", ex);
        }
        return statement.toString();
    }

    /**
     * Saves the provided statement to a file.
     *
     * @param statement The statement to save.
     * @throws IOException If there's an error during the file writing process.
     */
    private void saveStatementToFile(Statement statement) throws IOException {
        String filename = Statement_DIR + "/" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(statement.toString());
        }
    }

    /**
     * Ensures the directory for saving statement files exists. If it doesn't,
     * this method attempts to create it.
     */
    private void ensureStatementDirectoryExists() {
        Path path = Paths.get(Statement_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for statement.", e);
            }
        }
    }
}
