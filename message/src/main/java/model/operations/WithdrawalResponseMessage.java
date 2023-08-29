package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class WithdrawalResponseMessage implements Message {
    private final String message;
    public WithdrawalResponseMessage(BigDecimal amount) {
        message = "Успешное снятие : " + amount;
    }

    @Override
    public MessageType getType() {
        return MessageType.Withdrawal;
    }
}
