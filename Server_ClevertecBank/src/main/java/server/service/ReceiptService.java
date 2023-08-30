package server.service;

import lombok.extern.slf4j.Slf4j;
import server.entity.CurrencyType;
import server.entity.Receipt;
import server.entity.TransactionType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

/**
 * Provides functionality for generating and storing receipts for transactions.
 */
@Slf4j
public class ReceiptService {

    private static final String RECEIPT_DIR = "check";

    /**
     * Constructor that ensures the receipt directory exists upon instantiation.
     */
    public ReceiptService() {
        ensureReceiptDirectoryExists();
    }

    /**
     * Generates a receipt based on transaction details provided.
     *
     * @param transactionType The type of the transaction.
     * @param senderBank Name of the sender's bank.
     * @param senderAccountNumber Account number of the sender.
     * @param recipientBank Name of the recipient's bank.
     * @param recipientAccountNumber Account number of the recipient.
     * @param amount The amount of the transaction.
     * @param currency The currency used in the transaction.
     * @throws IOException if an error occurs during receipt file generation.
     */
    public void generateReceipt(TransactionType transactionType, String senderBank, String senderAccountNumber, String recipientBank, String recipientAccountNumber, BigDecimal amount, CurrencyType currency) throws IOException {
        Receipt receipt = Receipt.builder()
                .transactionType(transactionType)
                .senderBank(senderBank)
                .recipientBank(recipientBank)
                .senderAccountNumber(senderAccountNumber)
                .recipientAccountNumber(recipientAccountNumber)
                .amount(amount)
                .currency(currency)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();

        try {
            saveReceiptToFile(receipt);
        } catch (IOException ex) {
            log.error("Failed to save receipt to file", ex);
            throw new RuntimeException("Error generating receipt", ex);
        }
    }

    /**
     * Saves the generated receipt to a file.
     *
     * @param receipt The receipt to be saved.
     * @throws IOException if an error occurs during file writing.
     */
    private void saveReceiptToFile(Receipt receipt) throws IOException {
        String filename = RECEIPT_DIR + "/" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(receipt.toString());
        }
    }

    /**
     * Ensures that the directory to store receipt files exists.
     * If it doesn't, this method tries to create it.
     */
    private void ensureReceiptDirectoryExists() {
        Path path = Paths.get(RECEIPT_DIR);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory for receipts.", e);
            }
        }
    }
}
