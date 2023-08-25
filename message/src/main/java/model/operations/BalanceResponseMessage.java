package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class BalanceResponseMessage implements Message{
    private final String message;

    public BalanceResponseMessage(BigDecimal balance) {
        message = "Ваш остаток: " + balance;
    }

    @Override
    public MessageType getType() {
        return null;
    }

}
