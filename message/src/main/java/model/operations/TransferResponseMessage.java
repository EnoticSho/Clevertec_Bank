package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class TransferResponseMessage implements Message {
    private final String message;

    public TransferResponseMessage(String account, BigDecimal bigDecimal) {
        this.message = "Перевод " + bigDecimal + " на счет: " + account + " Успешно выполнен";
    }

    @Override
    public MessageType getType() {
        return null;
    }
}
