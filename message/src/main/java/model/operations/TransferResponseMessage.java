package model.operations;

import model.Message;
import model.MessageType;

import java.math.BigDecimal;

public class TransferResponseMessage implements Message {
    private String account;
    private BigDecimal bigDecimal;

    public TransferResponseMessage(String account, BigDecimal bigDecimal) {
        this.account = account;
        this.bigDecimal = bigDecimal;
    }

    public TransferResponseMessage() {}

    @Override
    public MessageType getType() {
        return null;
    }
}
