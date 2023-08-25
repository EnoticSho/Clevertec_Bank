package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class BalanceResponseMessage implements Message {

    private final BigDecimal balance;

    public BalanceResponseMessage(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public MessageType getType() {
        return null;
    }

}
