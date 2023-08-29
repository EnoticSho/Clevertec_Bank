package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class DepositResponseMessage implements Message {
    private final String message;

    public DepositResponseMessage(BigDecimal bigDecimal) {
        this.message = "Успешное пополнение счёта на: " + bigDecimal;
    }

    @Override
    public MessageType getType() {
        return MessageType.Deposit;
    }
}
