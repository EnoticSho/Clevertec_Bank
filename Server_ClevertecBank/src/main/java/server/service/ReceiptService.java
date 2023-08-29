package server.service;

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

public class ReceiptService {

    private static final String RECEIPT_DIR = "check";

    public ReceiptService() {
        ensureReceiptDirectoryExists();
    }

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

        saveReceiptToFile(receipt);
    }

    private void saveReceiptToFile(Receipt receipt) throws IOException {
        String filename = RECEIPT_DIR + "/" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(receipt.toString());
        }
    }

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
