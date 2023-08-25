package model.operations;

import model.Message;
import model.MessageType;

import java.math.BigDecimal;

public class TransferRequestMessage implements Message {
    private String account;
    private BigDecimal bigDecimal;

    public TransferRequestMessage(String account, BigDecimal bigDecimal) {
        this.account = account;
        this.bigDecimal = bigDecimal;
    }

    public TransferRequestMessage() {}

    @Override
    public MessageType getType() {
        return null;
    }
}
