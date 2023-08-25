package model.operations;

import lombok.Getter;
import model.Message;
import model.MessageType;

import java.math.BigDecimal;

@Getter
public class TransferRequestMessage implements Message {
    private final String account;
    private final BigDecimal bigDecimal;

    public TransferRequestMessage(String account, BigDecimal bigDecimal) {
        this.account = account;
        this.bigDecimal = bigDecimal;
    }

    @Override
    public MessageType getType() {
        return null;
    }
}
