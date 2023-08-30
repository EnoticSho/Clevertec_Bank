package server.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private int accountId;
    private String accountNumber;
    private String bankName;
}
