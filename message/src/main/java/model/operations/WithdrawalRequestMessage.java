package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class WithdrawalRequestMessage implements Message {
    private final BigDecimal amount;
    public WithdrawalRequestMessage(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public MessageType getType() {
        return MessageType.Withdrawal;
    }
}
