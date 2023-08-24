package model;

import java.math.BigDecimal;

public class TransferMessage implements Message{
    private String account;
    private BigDecimal bigDecimal;

    public TransferMessage(String account, BigDecimal bigDecimal) {
        this.account = account;
        this.bigDecimal = bigDecimal;
    }

    public TransferMessage() {}

    @Override
    public MessageType getType() {
        return null;
    }
}
