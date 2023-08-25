package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class DepositRequestMessage implements Message {
    private final BigDecimal amount;
    public DepositRequestMessage(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public MessageType getType() {
        return null;
    }
}
